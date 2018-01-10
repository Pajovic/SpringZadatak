package organization.repository;
import organization.model.Employee;
import organization.model.OrganizationData;

import java.util.ArrayList;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(collectionResourceRel = "employee", path = "employee")
public interface EmployeeRepository extends MongoRepository<Employee, String> {
		
	Employee findByEmployeeIdAndOrganizationId(Integer employeeId, Integer organizationId);
	Employee findById(String id);
	ArrayList<Employee> findByOrganizationId(int id);
}
