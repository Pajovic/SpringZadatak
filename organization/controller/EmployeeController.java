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

	/**
	 * Restfull RequestMethod.POST method for adding new employee to organization.
	 * @param employee New employee to be added.
	 * @return ResponseEntity which returns success response of trying to add new employee to organization.
	 */
	@RequestMapping(value = "employee/addNewEmployee", method = RequestMethod.POST)
	public ResponseEntity<?> AddNewEmployee(@RequestBody Employee employee) {
				
		return employeeService.AddNewEmployee(employee);
	}
	
	/**
	 * Restfull RequestMethod.POST method which adds new organization data.
	 * @param wrapper Wrapper which contains employee id and organization data.
	 * @return ResponseEntity which informs of the successfulness of adding new data to organization by employee. 
	 */
	@RequestMapping(value = "/employee/addOrganizationData", method = RequestMethod.POST)
	public ResponseEntity<?> AddNewData(@RequestBody EmployeeDataWrapper wrapper){				
				
			return employeeService.AddNewData(wrapper);
	}
	
	/**
	 * Restfull RequestMethod.GET method for retrieving all data from an organization.
	 * @param organizationId Id of the organization.
	 * @return ResponseEntity which returns list of data if organizationId is valid, error message if it is not. 
	 */
	@RequestMapping(value = "/employee/listAllData{id}", method = RequestMethod.GET)
	public ResponseEntity<?> ListAllOrganizationData(@RequestParam("id") String id) {	
		
		return employeeService.ListAllOrganizationData(id);
	}	
		
	@RequestMapping(value = "/employee/updateData", method = RequestMethod.PUT)
	public ResponseEntity<?> UpdateOrganizationData(@RequestBody  EmployeeDataWrapper wrapper) {
		
		return employeeService.UpdateOrganizationData(wrapper);
	}
	
	@RequestMapping(value = "/employee/deleteData", method = RequestMethod.DELETE)
	public ResponseEntity<?> DeleteOrganizationData(@RequestBody EmployeeDataWrapper wrapper){	
		
		return employeeService.DeleteOrganizationData(wrapper);
	}
}