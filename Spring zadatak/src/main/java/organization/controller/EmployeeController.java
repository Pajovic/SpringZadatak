package organization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import organization.model.Employee;
import organization.service.IEmployeeService;
import organization.wrappers.EmployeeDataWrapper;

@RestController
public class EmployeeController {	
		
	@Autowired
	private IEmployeeService employeeService;


	@RequestMapping(value = "employee/addNewEmployee", method = RequestMethod.POST)
	public ResponseEntity<String> AddNewEmployee(@RequestBody Employee employee) {
				
		return employeeService.AddNewEmployee(employee);
	}
	

	@RequestMapping(value = "/employee/addOrganizationData", method = RequestMethod.POST)
	public ResponseEntity<String> AddNewData(@RequestBody EmployeeDataWrapper wrapper){				
				
			return employeeService.AddNewData(wrapper);
	}
	

	@RequestMapping(value = "/employee/listAllData{id}", method = RequestMethod.GET)
	public ResponseEntity<?> ListAllOrganizationData(@RequestParam("id") String id) {	
		
		return employeeService.ListAllOrganizationData(id);
	}	
		

	@RequestMapping(value = "/employee/updateData", method = RequestMethod.PUT)
	public ResponseEntity<String> UpdateOrganizationData(@RequestBody  EmployeeDataWrapper wrapper) {
		
		return employeeService.UpdateOrganizationData(wrapper);
	}
	
	@RequestMapping(value = "/employee/deleteData", method = RequestMethod.DELETE)
	public ResponseEntity<String> DeleteOrganizationData(@RequestBody EmployeeDataWrapper wrapper){	
		
		return employeeService.DeleteOrganizationData(wrapper);
	}
}