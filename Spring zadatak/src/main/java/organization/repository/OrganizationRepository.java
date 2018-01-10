package organization.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import organization.model.Organization;


@RepositoryRestResource(collectionResourceRel = "organization", path = "organization")
public interface OrganizationRepository extends MongoRepository<Organization, String> {
	
	Organization findByname(String name);
	Organization findById(int id);
	
	
}

