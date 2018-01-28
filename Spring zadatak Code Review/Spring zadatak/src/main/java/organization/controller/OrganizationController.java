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
import organization.service.IOrganizationService;
import organization.wrappers.EmployeeInternalRightsWrapper;
import organization.wrappers.OrganizationExternalAccessWrapper;

@RestController
public class OrganizationController {
	
	@Autowired
	private IOrganizationService organizationService;
	
		
	@RequestMapping(value = "/organization/addNewOrganization", method = RequestMethod.POST)
	public ResponseEntity<String> AddNewOrganization(@RequestBody Organization organization) {
		
		return organizationService.AddNewOrganization(organization);			
	}
	
	@RequestMapping(value = "/organization/modifyEmployeeRights", method = RequestMethod.PUT)
	public ResponseEntity<String> ModifyEmployeeRights(@RequestBody EmployeeInternalRightsWrapper employee) {
	
		return organizationService.ModifyEmployeeRights(employee);
	}
	
	
	@RequestMapping(value = "/organization/listAllOrganizations", method = RequestMethod.GET)
	public ResponseEntity<List<Organization>> ListAllOrganizations() {
		
		return organizationService.ListAllOrganizations();
	}

	@RequestMapping(value = "/organization/listAllEmployees{organizationId}", method = RequestMethod.GET)
	public ResponseEntity<?> ListAllEmployees(@RequestParam("organizationId") int organizationId) {		
		
		return organizationService.ListAllEmployees(organizationId);		
	}

	@RequestMapping(value = "/organization/addExternalAccess", method = RequestMethod.POST)
	public ResponseEntity<String> AddExternalAccess(@RequestBody OrganizationExternalAccess oea){
		
		return organizationService.AddExternalAccess(oea);
	}
	
	@RequestMapping(value = "/organization/modifyExternalAccess", method = RequestMethod.PUT)
	public ResponseEntity<String> ModifyExternalAccess(@RequestBody OrganizationExternalAccessWrapper oeaw){
		
		return organizationService.ModifyExternalAccess(oeaw);
	}
	 

}