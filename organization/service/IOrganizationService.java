package organization.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import organization.model.Organization;
import organization.model.OrganizationExternalAccess;
import organization.wrappers.EmployeeInternalRightsWrapper;
import organization.wrappers.OrganizationExternalAccessWrapper;

public interface IOrganizationService {

	ResponseEntity<?> AddNewOrganization(Organization organization);
	ResponseEntity<?> ModifyEmployeeRights(EmployeeInternalRightsWrapper employee);
	ResponseEntity<List<Organization>> ListAllOrganizations();
	ResponseEntity<?> ListAllEmployees(int organizationId);
	ResponseEntity<?> AddExternalAccess(OrganizationExternalAccess oea);
	ResponseEntity<?> ModifyExternalAccess(OrganizationExternalAccessWrapper oeaw);
}
