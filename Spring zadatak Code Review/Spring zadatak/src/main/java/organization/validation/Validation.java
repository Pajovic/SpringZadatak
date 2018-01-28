package organization.validation;

import java.util.InputMismatchException;

import autovalue.shaded.org.apache.commons.lang.NullArgumentException;
import organization.model.Employee;
import organization.model.Organization;
import organization.wrappers.EmployeeDataWrapper;

/**
 * Input validation helper class.
 * @author Pajovic Petar
 *
 */
public class Validation {
	
	/**
	 * Validates employee input data.
	 * @param employee
	 */
	public static void ValidateEmployee(Employee employee) {

		if(employee.getName() == null) {
			throw new NullArgumentException("Employee name is null.");
		}
		if(employee.getLastName() == null) {
			throw new NullArgumentException("Employee lastname is null.");
		}
	}
	/**
	 * Validates EmployeeDataWrapper input data.
	 * @param wrapper
	 */
	public static void ValidateEmployeeDataWrapper(EmployeeDataWrapper wrapper) {
		
		if(wrapper.getName() == null) {
			throw new NullArgumentException("Data name is null.");
		}
		if(wrapper.getAmount() < 0) {
			throw new InputMismatchException("Amount is a negative number."); 
		}
		if(wrapper.getPrice() < 0) {
			throw new InputMismatchException("Price is a negative number.");
		}		
	}
	/**
	 * Validates Organization input data.
	 * @param organization
	 */
	public static void ValidateOrganization(Organization organization) {
		if(organization.getName() == null) {
			throw new NullArgumentException("Organization name is null.");
		}
	}
	

}
