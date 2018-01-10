package organization.service;

import org.springframework.http.ResponseEntity;


import organization.model.Employee;
import organization.wrappers.EmployeeDataWrapper;

public interface IEmployeeService {
	
	ResponseEntity<?> AddNewEmployee(Employee employee);
	ResponseEntity<?> AddNewData(EmployeeDataWrapper wrapper);
	ResponseEntity<?> ListAllOrganizationData(String id);
	ResponseEntity<?> UpdateOrganizationData(EmployeeDataWrapper wrapper);
	ResponseEntity<?> DeleteOrganizationData(EmployeeDataWrapper wrapper);
	
}
