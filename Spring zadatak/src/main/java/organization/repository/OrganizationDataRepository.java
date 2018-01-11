package organization.repository;

import java.util.ArrayList;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import organization.model.OrganizationData;


@RepositoryRestResource(collectionResourceRel = "organizationData", path = "organizationData")
public interface OrganizationDataRepository extends MongoRepository<OrganizationData, String> {
	
	OrganizationData findByDataIdAndOrganizationId(int dataId, int organizationId);
	ArrayList<OrganizationData> findByOrganizationId(int organizationId);
	OrganizationData findByName(String name);
	OrganizationData findById(String id);
	
}