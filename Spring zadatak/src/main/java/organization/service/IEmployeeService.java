package organization.service;

import org.springframework.http.ResponseEntity;


import organization.model.Employee;
import organization.wrappers.EmployeeDataWrapper;

public interface IEmployeeService {
	
	ResponseEntity<String> AddNewEmployee(Employee employee);
	ResponseEntity<String> AddNewData(EmployeeDataWrapper wrapper);
	ResponseEntity<?> ListAllOrganizationData(String id);
	ResponseEntity<String> UpdateOrganizationData(EmployeeDataWrapper wrapper);
	ResponseEntity<String> DeleteOrganizationData(EmployeeDataWrapper wrapper);
	
}
