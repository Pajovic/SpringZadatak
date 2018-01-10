package organization.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import autovalue.shaded.org.apache.commons.lang.NullArgumentException;
import organization.model.Employee;
import organization.model.Organization;
import organization.model.OrganizationData;
import organization.model.OrganizationExternalAccess;
import organization.model.OrganizationExternalAccess.EmployeeRight;
import organization.repository.EmployeeRepository;
import organization.repository.OrganizationDataRepository;
import organization.repository.OrganizationExternalAccessRepository;
import organization.repository.OrganizationRepository;
import organization.validation.Validation;
import organization.wrappers.EmployeeDataWrapper;


@Component("EmployeeService")
public class EmployeeService implements IEmployeeService{

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private OrganizationRepository organizationRepository;
	
	@Autowired
	private OrganizationDataRepository organizationDataRepository;
	
	@Autowired
	private OrganizationExternalAccessRepository oeaRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;

	private final String Line = "------------------------------------------------------------------------------";
	private final String Name = "Name: ";
	private final String Amount = "Amount: ";
	private final String Price = "Price: ";
	private final String Rights = "Rights: ";
	
	private StringBuilder employeeRights = null;
	
	@Override
	public ResponseEntity<?> AddNewEmployee(Employee employee) {
		
		Validation.ValidateEmployee(employee);			
		Organization org = organizationRepository.findByid(employee.getOrganizationId());
		if(org == null) {
			return new ResponseEntity<String>(String.format("Employee with id %d coulnd't be added, organization %s does not exist.",
					employee.getId(), employee.getOrganizationName()), HttpStatus.FORBIDDEN);
		}
		
		employee.setId(employee.getEmployeeId(), employee.getOrganizationId());
		if(employeeRepository.findById(employee.getId()) != null) {
			return new ResponseEntity<String>(String.format("Employee with id %s already exists in organization %s",
					employee.getId(),employee.getOrganizationName())
					, HttpStatus.FORBIDDEN);
		}			
		employee.setOrganizationName(org.getName());
	
		employeeRepository.save(employee);		
		return new ResponseEntity<String>(String.format("New employee added with id: %d in organization: %s",
				employee.getEmployeeId(), employee.getOrganizationName()), HttpStatus.CREATED);
		
	}

	@Override
	public ResponseEntity<?> AddNewData(EmployeeDataWrapper wrapper) {
		Validation.ValidateEmployeeDataWrapper(wrapper);
		
		Employee emp = employeeRepository.findByEmployeeIdAndOrganizationId(wrapper.getEmployeeId(), wrapper.getOrganizationId());
		if(emp == null) {
			return new ResponseEntity<String>(String.format("Employee with id %d does not exist in organization %s.",wrapper.getEmployeeId(),
					wrapper.getOrganizationId()), HttpStatus.FORBIDDEN);
		}
		if(!emp.getCreateRight()) {
			return new ResponseEntity<String>("This does not have permission to add new data.", HttpStatus.FORBIDDEN);	
		}
			
		if(organizationDataRepository.findByName(wrapper.getName()) != null) {
			return new ResponseEntity<String>(String.format("Data with name %s already exists.", wrapper.getName()),HttpStatus.FORBIDDEN);
		}
		
		OrganizationData data = new OrganizationData(wrapper.getName(),wrapper.getAmount(),wrapper.getPrice(), emp.getOrganizationId());
		
		organizationDataRepository.save(data);
		return new ResponseEntity<String>("New data added.",HttpStatus.OK);		
	}

	@Override
	public ResponseEntity<?> ListAllOrganizationData(String id) {
		if(id == null) {
			throw new NullArgumentException("Id is null.");
		}
		Employee emp = employeeRepository.findById(id);		
		
		if(emp == null) {
			return new ResponseEntity<String>(String.format("Employee with id: %s not found.", id), HttpStatus.FORBIDDEN);
		}
					
		ArrayList<String> returnList = GetInternalOrganizationData(emp);			
		HashMap<String,List<String>> dataAndRights = GetExternalOrganizationData(emp);
		returnList = CombineInternalAndExternalData(returnList, dataAndRights);
		return new ResponseEntity<ArrayList<String>>(returnList,HttpStatus.OK);	
	}

	@Override
	public ResponseEntity<?> UpdateOrganizationData(EmployeeDataWrapper wrapper) {
		Organization org = organizationRepository.findByid(wrapper.getOrganizationId());
		if(org == null) {
			return new ResponseEntity<String>(String.format("Organization %d does not exist.",wrapper.getOrganizationId()),HttpStatus.FORBIDDEN);
		}
		Employee emp = employeeRepository.findByEmployeeIdAndOrganizationId(wrapper.getEmployeeId(), wrapper.getOrganizationId());
		if(emp == null) {
			return new ResponseEntity<String>(String.format("Employee with id %d does not exist in organization %d.",wrapper.getEmployeeId(),
					wrapper.getOrganizationId()), HttpStatus.FORBIDDEN);
		}

		if(wrapper.getOrganizationIdOfData() == emp.getOrganizationId()) {
			if(emp.getUpdateRight()) {
				return ModifyInternalData(wrapper);
			}
			return new ResponseEntity<String>(String.format("Employee %d does not have Update rights.",emp.getEmployeeId()),HttpStatus.FORBIDDEN);			
		}
		else {
			return ModifyExternalData(wrapper);
		}			
	}
	
		private ResponseEntity<String> ModifyExternalData(EmployeeDataWrapper wrapper){
			try {
				List<OrganizationExternalAccess> oeaList = oeaRepository.findByorganizationId(wrapper.getOrganizationIdOfData());
				for(OrganizationExternalAccess oea : oeaList) {
					if(oea.getIsAllowed() && oea.getRight() == EmployeeRight.U) {					
						List<OrganizationData> retrievedData = RetrieveDataByCriteria(oea.getCriteria(), oea.getOrganizationId());
						for(OrganizationData data : retrievedData) {
							if(wrapper.getName().equals(data.getName())) {
								data.setAmount(wrapper.getAmount());
								data.setPrice(wrapper.getPrice());		
								organizationDataRepository.save(data);
								return new ResponseEntity<String>(String.format("Employee %d successfully modified data %s",wrapper.getEmployeeId(),
										wrapper.getName()),HttpStatus.OK);
							}
						}
					}								
				}
			}
			catch(Exception ex) {
				return new ResponseEntity<String>(String.format("Exception caught in ModifyExternalData: %s", ex.toString()),HttpStatus.FORBIDDEN);
			}
			return new ResponseEntity<String>("No data to be updated found.",HttpStatus.OK);			
		}

	@Override
	public ResponseEntity<?> DeleteOrganizationData(EmployeeDataWrapper wrapper) {
		Organization org = organizationRepository.findByid(wrapper.getOrganizationId());
		if(org == null) {
			return new ResponseEntity<String>(String.format("Organization %d does not exist.",wrapper.getOrganizationId()),HttpStatus.FORBIDDEN);
		}
		Employee emp = employeeRepository.findByEmployeeIdAndOrganizationId(wrapper.getEmployeeId(), wrapper.getOrganizationId());
		if(emp == null) {
			return new ResponseEntity<String>(String.format("Employee with id %d does not exist in organization %d.",wrapper.getEmployeeId(),
					wrapper.getOrganizationId()), HttpStatus.FORBIDDEN);
		}

		if(wrapper.getOrganizationIdOfData() == emp.getOrganizationId()) {
			if(emp.getDeleteRight()) {
				return DeleteInternalData(wrapper);
			}
			return new ResponseEntity<String>(String.format("Employee %d does not have Update rights.",emp.getEmployeeId()),HttpStatus.FORBIDDEN);			
		}
		else {
			return DeleteExternalData(wrapper);
		}			
	}
	
	private ResponseEntity<String> DeleteInternalData(EmployeeDataWrapper wrapper){
		OrganizationData orgData = organizationDataRepository.findByName(wrapper.getName());
		if(orgData == null) {
			return new ResponseEntity<String>(String.format("Data with name %s does not exists.", wrapper.getName()),HttpStatus.FORBIDDEN);
		}				
		organizationDataRepository.delete(orgData);		
		return new ResponseEntity<String>(String.format("Employee %d successfully deleted data %s",wrapper.getEmployeeId(),
				wrapper.getName()),HttpStatus.OK);		
	}
	
	private ResponseEntity<String> DeleteExternalData(EmployeeDataWrapper wrapper){
		try {
			List<OrganizationExternalAccess> oeaList = oeaRepository.findByorganizationId(wrapper.getOrganizationIdOfData());
			for(OrganizationExternalAccess oea : oeaList) {
				if(oea.getIsAllowed() && oea.getRight() == EmployeeRight.D) {					
					List<OrganizationData> retrievedData = RetrieveDataByCriteria(oea.getCriteria(), oea.getOrganizationId());
					for(OrganizationData data : retrievedData) {
						if(wrapper.getName().equals(data.getName())) {
							organizationDataRepository.delete(data);
							return new ResponseEntity<String>(String.format("Employee %d successfully deleted data %s",wrapper.getEmployeeId(),
									wrapper.getName()),HttpStatus.OK);
						}
					}
				}								
			}
		}
		catch(Exception ex) {
			return new ResponseEntity<String>(String.format("Exception caught in DeleteExternalData: %s", ex.toString()),HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<String>("No data to be deleted found.",HttpStatus.OK);	
	}

	/**
	 * Gets list of internal data and employee rights over them.
	 * @param emp Employee object.
	 * @return List of internal data and employee rights over them.
	 */
	private ArrayList<String> GetInternalOrganizationData(Employee emp){
		
		ArrayList<String> returnList = new ArrayList<String>();	
		ArrayList<OrganizationData> organizationDataList = new ArrayList<OrganizationData>();
		returnList.add(Line);
		returnList.add(emp.getName() + " " + emp.getLastName()); 
		returnList.add(Line);		
		if(!emp.getReadRight()) {
			return returnList;
		}
		organizationDataList = organizationDataRepository.findByOrganizationId(emp.getOrganizationId());			
		for(OrganizationData orgData : organizationDataList) {
			returnList.add(Name + orgData.getName());
			returnList.add(Amount + orgData.getAmount());
			returnList.add(Price + orgData.getPrice());
			returnList.add(Rights + GetInternalEmployeeRights(emp));
			returnList.add(Line);
		}
		
		return returnList;
	}
	
	/**
	 * Gets HashMap of external data and employee rights over them.
	 * @param emp Employee object.
	 * @return HashMap of external data and employee rights over them.
	 */
	private HashMap<String,List<String>> GetExternalOrganizationData(Employee emp){
		try {
			HashMap<String,List<String>> dataAndRights = new HashMap<String,List<String>>();
			List<Organization> organizations = organizationRepository.findAll();						
			for(Organization org : organizations) {
				if(org.getId() == emp.getOrganizationId()) {				
					continue;
				}
					List<OrganizationExternalAccess> oeaList = oeaRepository.findByorganizationId(org.getId());
					for(OrganizationExternalAccess oea : oeaList) {
						if(!oea.getIsAllowed() || !oea.getOrganizationIds().contains(emp.getOrganizationId())) {
							continue;
						}																
						List<OrganizationData> retrievedData = RetrieveDataByCriteria(oea.getCriteria(), oea.getOrganizationId());					
						for(OrganizationData orgData : retrievedData) {									
							if(!dataAndRights.containsKey(orgData.getName())) {
								List<String> listOfRights = new ArrayList<String>();
								listOfRights.add(oea.getRight().toString());
								dataAndRights.put(orgData.getName(),listOfRights);
							}
							else {
								if(!dataAndRights.get(orgData.getName()).contains(oea.getRight().toString())) {
									dataAndRights.get(orgData.getName()).add(oea.getRight().toString());	
							}								
						}
					}
				}				
			}
			return dataAndRights;
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Combines Internal and external data and rights over them retrieved by employee.
	 * @param returnList List of all data and rights.
	 * @param dataAndRights HashMap of external data and employee rights over them.
	 * @return All organization data and rights over them which employee can read.
	 */
	private ArrayList<String> CombineInternalAndExternalData(ArrayList<String> returnList, HashMap<String,List<String>> dataAndRights) {
			
		for (Entry<String, List<String>> entry : dataAndRights.entrySet()) {
			if(!entry.getValue().contains(EmployeeRight.R.toString())) {
				continue;
			}
			OrganizationData orgData = organizationDataRepository.findByName(entry.getKey());		   
		    returnList.add(Name + orgData.getName());
			returnList.add(Amount + orgData.getAmount());
			returnList.add(Price + orgData.getPrice());			
		    returnList.add(Rights);		   
		    returnList.add(GetExternalEmployeeRights(entry.getValue()));
		    returnList.add(Line);
		}
		return returnList;
	}

	/**
	 * Gets all of employees external rights.
	 * @param rights : List of all employees rights.
	 * @return Appended acronyms of employees rights.
	 */
	private String GetExternalEmployeeRights(List<String> rights) {
		employeeRights = new StringBuilder();
	    for(String right : rights) {
	    	employeeRights.append(right.toString());
	    }
	    return employeeRights.toString();
	}
	
	/**
	 * Gets internal employee CRUD rights.
	 * @param employee
	 * @return
	 */
	private String GetInternalEmployeeRights(Employee employee) {
		employeeRights = new StringBuilder();
		if(employee.getCreateRight()) {
			employeeRights.append(EmployeeRight.C);			
		}
		if(employee.getReadRight()) {
			employeeRights.append(EmployeeRight.R);
		}
		if(employee.getUpdateRight()) {
			employeeRights.append(EmployeeRight.U);
		}
		if(employee.getDeleteRight()) {
			employeeRights.append(EmployeeRight.D);
		}
		return employeeRights.toString();
	}
	
	private List<OrganizationData> RetrieveDataByCriteria(String criteria, Integer organizationId){		
		String fullCriteria = String.format("{ %s , organizationId : %d }",criteria,organizationId);
		BasicQuery query = new BasicQuery(fullCriteria);
		return mongoTemplate.find(query, OrganizationData.class);
	}
	

	private ResponseEntity<String> ModifyInternalData(EmployeeDataWrapper wrapper){
		
		OrganizationData orgData = organizationDataRepository.findByName(wrapper.getName());
		if(orgData == null) {
			return new ResponseEntity<String>(String.format("Data with name %s does not exists.", wrapper.getName()),HttpStatus.FORBIDDEN);
		}				
		orgData.setName(wrapper.getName());				
		orgData.setAmount(wrapper.getAmount());		
		orgData.setPrice(wrapper.getPrice());	
		organizationDataRepository.save(orgData);
		return new ResponseEntity<String>(String.format("Employee %d successfully modified data %s",wrapper.getEmployeeId(),
				wrapper.getName()),HttpStatus.OK);		
	}
	
	
	
}
