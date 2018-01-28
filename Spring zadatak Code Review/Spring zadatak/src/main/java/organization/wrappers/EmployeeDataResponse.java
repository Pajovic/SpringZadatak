package organization.wrappers;

import java.util.ArrayList;
/**
 * Class used for ResponseEntity.
 * @author Pajovic Petar
 *
 */
public class EmployeeDataResponse {

	private String employeeName;
	private String employeeLastName;
	private ArrayList<DataRightsWrapper> dataAndRights = new ArrayList<DataRightsWrapper>();
	
		
	public EmployeeDataResponse(String employeeName, String employeeLastName) {
		this.employeeName = employeeName;
		this.employeeLastName = employeeLastName;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeLastName() {
		return employeeLastName;
	}

	public void setEmployeeLastName(String employeeLastName) {
		this.employeeLastName = employeeLastName;
	}

	public ArrayList<DataRightsWrapper> getDataAndRights() {
		return dataAndRights;
	}

	public void setDataAndRights(ArrayList<DataRightsWrapper> dataAndRights) {
		this.dataAndRights = dataAndRights;
	}
}
