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
import organization.wrappers.DataRightsWrapper;
import organization.wrappers.EmployeeDataResponse;
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
	
	private StringBuilder employeeRights = null;
	/**
	 * HashMap which contains OrganizationData id as key and CRUD rights as value.
	 */
	private HashMap<String,List<String>> dataAndRights = null;
	
	@Override
	public ResponseEntity<String> AddNewEmployee(Employee employee) {
		
		if(employeeRepository == null) {
			throw new NullPointerException("employeeRepository is null.");
		}
		Validation.ValidateEmployee(employee);			
		Organization org = organizationRepository.findById(employee.getOrganizationId());
		if(org == null) {
			return new ResponseEntity<String>(String.format("Employee with id %d coulnd't be added, organization %d does not exist.",
					employee.getId(), employee.getOrganizationId()), HttpStatus.FORBIDDEN);
		}
		
		employee.setId(employee.getEmployeeId(), employee.getOrganizationId());
		if(employeeRepository.findById(employee.getId()) != null) {
			return new ResponseEntity<String>(String.format("Employee with id %s already exists in organization %d",
					employee.getId(),employee.getOrganizationId())
					, HttpStatus.FORBIDDEN);
		}					
	
		employeeRepository.save(employee);		
		return new ResponseEntity<String>(String.format("New employee added with id: %d in organization: %d",
				employee.getEmployeeId(), employee.getOrganizationId()), HttpStatus.CREATED);		
	}

	@Override
	public ResponseEntity<String> AddNewData(EmployeeDataWrapper wrapper) {
		if(employeeRepository == null) {
			throw new NullPointerException("employeeRepository is null.");
		}
		if(organizationDataRepository == null) {
			throw new NullPointerException("IOrganizationDataRepository is null.");
		}
		
		Validation.ValidateEmployeeDataWrapper(wrapper);
		
		Employee emp = employeeRepository.findByEmployeeIdAndOrganizationId(wrapper.getEmployeeId(), wrapper.getOrganizationId());
		if(emp == null) {
			return new ResponseEntity<String>(String.format("Employee with id %d does not exist in organization %s.",wrapper.getEmployeeId(),
					wrapper.getOrganizationId()), HttpStatus.FORBIDDEN);
		}
		if(!emp.getCreateRight()) {
			return new ResponseEntity<String>("This does not have permission to add new data.", HttpStatus.FORBIDDEN);	
		}
		
		OrganizationData data = new OrganizationData(wrapper.getDataId() ,emp.getOrganizationId(), wrapper.getName(),wrapper.getAmount(),wrapper.getPrice());
		data.setId(wrapper.getDataId(),wrapper.getOrganizationId());
		if(organizationDataRepository.findById(data.getId()) != null) {
			return new ResponseEntity<String>(String.format("Data with name %s already exists.", wrapper.getName()),HttpStatus.FORBIDDEN);
		}

		organizationDataRepository.save(data);
		return new ResponseEntity<String>("New data added.",HttpStatus.OK);		
	}

	@Override
	public ResponseEntity<?> ListAllOrganizationData(String id) {
	
		if(employeeRepository == null) {
			throw new NullPointerException("employeeRepository is null.");
		}
		if(id == null) {
			throw new NullArgumentException("Id is null.");
		}
		Employee emp = employeeRepository.findById(id);		
		
		if(emp == null) {
			return new ResponseEntity<String>(String.format("Employee with id: %s not found.", id), HttpStatus.FORBIDDEN);
		}
		EmployeeDataResponse edr = new EmployeeDataResponse(emp.getName(),emp.getLastName());
		GetInternalOrganizationData(emp, edr);			
	    dataAndRights = GetExternalOrganizationData(emp);
		CombineInternalAndExternalData(dataAndRights, edr);
		return new ResponseEntity<EmployeeDataResponse>(edr,HttpStatus.OK);	
	}

	@Override
	public ResponseEntity<String> UpdateOrganizationData(EmployeeDataWrapper wrapper) {
		
		if(employeeRepository == null) {
			throw new NullPointerException("employeeRepository is null.");
		}
		if(organizationRepository == null) {
			throw new NullPointerException("organizationRepository is null.");
		}
		Organization org = organizationRepository.findById(wrapper.getOrganizationId());
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
				return ModifyInternalData(wrapper,EmployeeRight.U);
			}
			return new ResponseEntity<String>(String.format("Employee %d does not have Update rights.",emp.getEmployeeId()),HttpStatus.FORBIDDEN);			
		}
		else {
			return ModifyExternalData(wrapper, EmployeeRight.U);
		}			
	}
	
	@Override
	public ResponseEntity<String> DeleteOrganizationData(EmployeeDataWrapper wrapper) {

		if(employeeRepository == null) {
			throw new NullPointerException("employeeRepository is null.");
		}
		if(organizationRepository == null) {
			throw new NullPointerException("organizationRepository is null.");
		}
		Organization org = organizationRepository.findById(wrapper.getOrganizationId());
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
				return ModifyInternalData(wrapper, EmployeeRight.D);
			}
			return new ResponseEntity<String>(String.format("Employee %d does not have Update rights.",emp.getEmployeeId()),HttpStatus.FORBIDDEN);			
		}
		else {
			return ModifyExternalData(wrapper, EmployeeRight.D);
		}		
	}
	
	
	/**
	 * Modifies OrganizationData which is considered to be internal by an employee ( from its own organization ). 
	 * @param wrapper Wrapper which encapsulates employees id, organizationId and OrganizationData properties. 
	 * @return Message of successfulness of the modify. 
	 */
	private ResponseEntity<String> ModifyInternalData(EmployeeDataWrapper wrapper, EmployeeRight right){
		
		OrganizationData orgData = organizationDataRepository.findByDataIdAndOrganizationId(wrapper.getDataId(),wrapper.getOrganizationId());
		if(orgData == null) {
			return new ResponseEntity<String>(String.format("Organization Data with id %d does not exists.", wrapper.getDataId()),HttpStatus.FORBIDDEN);
		}
		if(right == EmployeeRight.U) {
			orgData.setName(wrapper.getName());				
			orgData.setAmount(wrapper.getAmount());		
			orgData.setPrice(wrapper.getPrice());	
			organizationDataRepository.save(orgData);
			return new ResponseEntity<String>(String.format("Employee %d successfully updated data %s",wrapper.getEmployeeId(),
					wrapper.getName()),HttpStatus.OK);	
		}
		else {
			organizationDataRepository.delete(orgData);		
			return new ResponseEntity<String>(String.format("Employee %d successfully deleted data %d",wrapper.getEmployeeId(),
					wrapper.getDataId()),HttpStatus.OK);	
		}
	}
	
	/**
	 * Modifies OrganizationData which is considered to be external by an employee ( from external organization ). 
	 * @param wrapper
	 * @return Message of successfulness of the modify. 
	 */
	private ResponseEntity<String> ModifyExternalData(EmployeeDataWrapper wrapper, EmployeeRight right){
		try {
			List<OrganizationExternalAccess> oeaList = oeaRepository.findByorganizationId(wrapper.getOrganizationIdOfData());
			for(OrganizationExternalAccess oea : oeaList) {
				if(oea.getIsAllowed() && oea.getRight() == right) {					
					List<OrganizationData> retrievedData = RetrieveDataByCriteria(oea.getCriteria(), oea.getOrganizationId());
					for(OrganizationData data : retrievedData) {
						if(wrapper.getDataId() == data.getDataId()) {
							if(right == EmployeeRight.U) {
								data.setName(wrapper.getName());
								data.setAmount(wrapper.getAmount());
								data.setPrice(wrapper.getPrice());		
								organizationDataRepository.save(data);
								return new ResponseEntity<String>(String.format("Employee %d successfully updated data %d",wrapper.getEmployeeId(),
										wrapper.getDataId()),HttpStatus.OK);
							}
							else {
								organizationDataRepository.delete(data);
								return new ResponseEntity<String>(String.format("Employee %d successfully deleted data %d",wrapper.getEmployeeId(),
										wrapper.getDataId()),HttpStatus.OK);
							}
						}
					}
				}
			}
		}
		catch(Exception ex) {
			return new ResponseEntity<String>(String.format("Exception caught in ModifyExternalData: %s", ex.toString()),HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<String>("No data found.",HttpStatus.OK);			
		}


	/**
	 * Gets list of internal data and employee rights over them.
	 * @param emp Employee object.
	 * @return List of internal data and employee rights over them.
	 */
	private void GetInternalOrganizationData(Employee emp, EmployeeDataResponse edr){
				
		ArrayList<OrganizationData> organizationDataList = new ArrayList<OrganizationData>();				
		if(emp.getReadRight()) {
			organizationDataList = organizationDataRepository.findByOrganizationId(emp.getOrganizationId());			
			for(OrganizationData orgData : organizationDataList) {
				DataRightsWrapper drw = new DataRightsWrapper();
				drw.setId(orgData.getId());
				drw.setName(orgData.getName());
				drw.setAmount(String.valueOf(orgData.getAmount()));
				drw.setPrice(String.valueOf(orgData.getPrice()));
				drw.setRights(GetInternalEmployeeRights(emp)); 				
				edr.getDataAndRights().add(drw);
			}
		}
	}
	
	/**
	 * Gets HashMap of external data and employee rights over them.
	 * @param emp Employee object.
	 * @return HashMap of external data and employee rights over them.
	 */
	private HashMap<String,List<String>> GetExternalOrganizationData(Employee emp){
		try {
			dataAndRights = new HashMap<String,List<String>>();			
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
							if(!dataAndRights.containsKey(orgData.getId())) {
								List<String> listOfRights = new ArrayList<String>();
								listOfRights.add(oea.getRight().toString());
								dataAndRights.put(orgData.getId(),listOfRights);
							}
							else {
								if(!dataAndRights.get(orgData.getId()).contains(oea.getRight().toString())) {
									dataAndRights.get(orgData.getId()).add(oea.getRight().toString());	
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
	 * @param dataAndRights HashMap of external data and employee rights over them.
	 * @param edr Response object.
	 */	
	private void CombineInternalAndExternalData(HashMap<String,List<String>> dataAndRights, EmployeeDataResponse edr) {
			
		for (Entry<String, List<String>> entry : dataAndRights.entrySet()) {
			if(!entry.getValue().contains(EmployeeRight.R.toString())) {
				continue;
			}
			OrganizationData orgData = organizationDataRepository.findById(entry.getKey());	
			DataRightsWrapper drw = new DataRightsWrapper();
			drw.setId(orgData.getId());
			drw.setName(orgData.getName());
			drw.setAmount(String.valueOf(orgData.getAmount()));
			drw.setPrice(String.valueOf(orgData.getPrice()));
			drw.setRights(GetExternalEmployeeRights(entry.getValue()));		    
			edr.getDataAndRights().add(drw);
		}
	}

	/**
	 * Gets all of employees external rights.
	 * @param rights : List of all employees rights.
	 * @return String of concatenated acronyms of CRUD operations which employee has in external Organization.
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
	 * @return String of concatenated acronyms of CRUD operations which employee has in Organization.
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
	
	/**
	 * Retrieves a list of OrganizationData objects from the database which are exposed by an organization with a certain criteria.
	 * @param criteria
	 * @param organizationId
	 * @return List of OrganizationData.
	 */
	protected List<OrganizationData> RetrieveDataByCriteria(String criteria, Integer organizationId){	
		
		String fullCriteria = String.format("{ %s , organizationId : %d }",criteria, organizationId);				
		BasicQuery query = new BasicQuery(fullCriteria);
		return mongoTemplate.find(query, OrganizationData.class);
	}	
}
