package organization.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import organization.model.OrganizationExternalAccess;
import organization.model.OrganizationExternalAccess.EmployeeRight;

@RepositoryRestResource(collectionResourceRel = "organizationExternalAccess", path = "organizationExternalAccess")
public interface OrganizationExternalAccessRepository extends MongoRepository<OrganizationExternalAccess, String> {

	OrganizationExternalAccess findById(String id);
	List<OrganizationExternalAccess> findByorganizationId(Integer organizationId);
	List<EmployeeRight> findByOrganizationId(Integer organizationId);
	
}
