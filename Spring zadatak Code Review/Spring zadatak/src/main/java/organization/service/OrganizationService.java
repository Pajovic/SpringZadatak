package organization.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import organization.model.Employee;
import organization.model.Organization;
import organization.model.OrganizationExternalAccess;
import organization.repository.EmployeeRepository;
import organization.repository.OrganizationExternalAccessRepository;
import organization.repository.OrganizationRepository;
import organization.validation.Validation;
import organization.wrappers.EmployeeInternalRightsWrapper;
import organization.wrappers.OrganizationExternalAccessWrapper;

@Component("OrganizationService")
public class OrganizationService implements IOrganizationService {

	@Autowired
	private OrganizationRepository organizationRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private OrganizationExternalAccessRepository oeaRepository;
			
	@Override
	public ResponseEntity<String> AddNewOrganization(Organization organization) {
		
		if(organizationRepository == null) {
			throw new NullPointerException("organizationService is null.");
		}
		if(organization == null) {
			return new ResponseEntity<String>("No organization data was provided.",HttpStatus.NO_CONTENT);
		}
		
		Validation.ValidateOrganization(organization);
		
		if(organizationRepository.findById(organization.getId()) != null) {
			return new ResponseEntity<String>(String.format("Organization with id %d already exists.",organization.getId()),HttpStatus.FORBIDDEN);
		}
		
		organizationRepository.save(organization);
		return new ResponseEntity<String>(String.format("New organization with id %d added.",organization.getId()),HttpStatus.CREATED);
	}

	@Override
	public ResponseEntity<String> ModifyEmployeeRights(EmployeeInternalRightsWrapper employee) {
		
		if(employeeRepository == null) {
			throw new NullPointerException("employeeService is null.");
		}
		Employee emp = employeeRepository.findByEmployeeIdAndOrganizationId(employee.getEmployeeId(),employee.getOrganizationId());
		if(emp == null) {
			return new ResponseEntity<String>(String.format("Employee with id %d and organization id %d not found",
					employee.getEmployeeId(),employee.getOrganizationId()),HttpStatus.FORBIDDEN);
		}
		
		emp.setCreateRight(employee.isInternalCreateRight());
		emp.setReadRight(employee.isInternalReadRight());
		emp.setUpdateRight(employee.isInternalUpdateRight());
		emp.setDeleteRight(employee.isInternalDeleteRight());
		employeeRepository.save(emp);
		return new ResponseEntity<String>(HttpStatus.OK);	
	}

	@Override
	public ResponseEntity<List<Organization>> ListAllOrganizations() {
		
		if(organizationRepository == null) {
			throw new NullPointerException("organizationService is null.");
		}
		List<Organization> organizations = organizationRepository.findAll();
		if(organizations.isEmpty()) {
			return new ResponseEntity<List<Organization>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Organization>>(organizations,HttpStatus.OK);	
	}

	@Override
	public ResponseEntity<?> ListAllEmployees(int organizationId) {
		
		if(organizationRepository == null) {
			throw new NullPointerException("organizationService is null.");
		}
		Organization org = organizationRepository.findById(organizationId);
		if(org == null) {
			return new ResponseEntity<String>(String.format("No organization found with id: %d."
					,organizationId),HttpStatus.FORBIDDEN);
		}
		ArrayList<Employee> employeeList = employeeRepository.findByOrganizationId(organizationId);
		if(employeeList.isEmpty()) {
			return new ResponseEntity<String>(String.format("No employees found for organization id: %d."
					,organizationId),HttpStatus.FORBIDDEN);
		}
		
		return new ResponseEntity<ArrayList<Employee>>(employeeList,HttpStatus.OK);			
	}

	@Override
	public ResponseEntity<String> AddExternalAccess(OrganizationExternalAccess oea) {
		
		if(oeaRepository == null) {
			throw new NullPointerException("organizationExternalAccessRepository is null.");
		}
		oea.setId(oea.getExternalAccessId(),oea.getOrganizationId());
		if(oeaRepository.findById(oea.getId()) != null) {
			return new ResponseEntity<String>(String.format("External Access with id %s already exists.",oea.getId()),HttpStatus.FORBIDDEN);
		}
		
		oeaRepository.save(oea);
		return new ResponseEntity<String>(String.format("New external access added for organization: %d", oea.getOrganizationId()),HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> ModifyExternalAccess(OrganizationExternalAccessWrapper oeaw) {
		
		if(oeaRepository == null) {
			throw new NullPointerException("organizationExternalAccessRepository is null.");
		}
		OrganizationExternalAccess orgExtAccess = oeaRepository.findById(oeaw.getId()); 
		if(orgExtAccess == null){
			return new ResponseEntity<String>(String.format("OrganizationExternalAccess with id %s not found.",oeaw.getId()),HttpStatus.FORBIDDEN);
		}	
		orgExtAccess.setAllowed(oeaw.getIsAllowed());
		oeaRepository.save(orgExtAccess);
		return new ResponseEntity<String>(String.format("OrganizationExternalAccess with id %s now has %b external right.",
				oeaw.getId(),oeaw.getIsAllowed()),HttpStatus.OK);
	}

}
