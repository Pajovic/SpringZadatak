package organization.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import organization.model.Organization;
import organization.model.OrganizationExternalAccess;
import organization.repository.EmployeeRepository;
import organization.repository.OrganizationExternalAccessRepository;
import organization.repository.OrganizationRepository;
import organization.service.IOrganizationService;
import organization.wrappers.EmployeeInternalRightsWrapper;
import organization.wrappers.OrganizationExternalAccessWrapper;

@RestController
public class OrganizationController {
	
	@Autowired
	private IOrganizationService organizationService;
	
		
	/**
	 * Restfull RequestMethod.POST method for adding new organization.
	 * @param organization New Organization.
	 * @return ResponseEntity with information if new organization was added successfully or not.
	 */
	@RequestMapping(value = "/organization/addNewOrganization", method = RequestMethod.POST)
	public ResponseEntity<?> AddNewOrganization(@RequestBody Organization organization) {
		
		return organizationService.AddNewOrganization(organization);	
		
	}
	/**
	 * Restfull RequestMethod.PUT method for modifying employee rights in an organization.	
	 * @param employee Employee whose rights are to be modified.
	 * @return ResponseEntity with information if given employees rights were successfully modified or not.
	 */
	@RequestMapping(value = "/organization/modifyEmployeeRights", method = RequestMethod.PUT)
	public ResponseEntity<?> ModifyEmployeeRights(@RequestBody EmployeeInternalRightsWrapper employee) {
	
		return organizationService.ModifyEmployeeRights(employee);
	}
	
	/**
	 * Restfull RequestMethod.GET method for listing all organizations.
	 * @return All organizations.
	 */
	@RequestMapping(value = "/organization/listAllOrganizations", method = RequestMethod.GET)
	public ResponseEntity<List<Organization>> ListAllOrganizations() {
		
		return organizationService.ListAllOrganizations();
	}
	/**
	 * Restfull RequestMethod.GET method for listing all employees in an organization.
	 * @param organizationId Id of the organization whose employees need to be listed.
	 * @return ResponseEntity which returns list of employees of an organization if organizationId is valid, error message if it is not.
	 */
	@RequestMapping(value = "/organization/listAllEmployees{organizationId}", method = RequestMethod.GET)
	public ResponseEntity<?> ListAllEmployees(@RequestParam("organizationId") int organizationId) {		
		
		return organizationService.ListAllEmployees(organizationId);		
	}

	@RequestMapping(value = "/organization/addExternalAccess", method = RequestMethod.POST)
	public ResponseEntity<?> AddExternalAccess(@RequestBody OrganizationExternalAccess oea){
		
		return organizationService.AddExternalAccess(oea);
	}
	
	@RequestMapping(value = "/organization/modifyExternalAccess", method = RequestMethod.PUT)
	public ResponseEntity<?> ModifyExternalAccess(@RequestBody OrganizationExternalAccessWrapper oeaw){
		
		return organizationService.ModifyExternalAccess(oeaw);
	}
	 

}