package organization.wrappers;

/**
 * Wrapper class used for modifying employees CRUD rights by organization.
 * @author Pajovic Petar
 *  
 *
 */
public class EmployeeInternalRightsWrapper {
	
	private int employeeId;
	private int organizationId;
	private boolean internalReadRight;
	private boolean internalCreateRight;
	private boolean internalUpdateRight;
	private boolean internalDeleteRight;
	
	public EmployeeInternalRightsWrapper(int employeeId, int organizationId, boolean internalCreateRight, boolean internalReadRight,
			boolean internalUpdateRight, boolean internalDeleteRight) {
		this.employeeId = employeeId;
		this.organizationId = organizationId;
		this.internalCreateRight = internalCreateRight;
		this.internalReadRight = internalReadRight;
		this.internalUpdateRight = internalUpdateRight;
		this.internalDeleteRight = internalDeleteRight;
	}

	public EmployeeInternalRightsWrapper() {
		
	}
	
	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public int getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}

	public boolean isInternalReadRight() {
		return internalReadRight;
	}

	public void setInternalReadRight(boolean internalReadRight) {
		this.internalReadRight = internalReadRight;
	}

	public boolean isInternalCreateRight() {
		return internalCreateRight;
	}

	public void setInternalCreateRight(boolean internalCreateRight) {
		this.internalCreateRight = internalCreateRight;
	}

	public boolean isInternalUpdateRight() {
		return internalUpdateRight;
	}

	public void setInternalUpdateRight(boolean internalUpdateRight) {
		this.internalUpdateRight = internalUpdateRight;
	}

	public boolean isInternalDeleteRight() {
		return internalDeleteRight;
	}

	public void setInternalDeleteRight(boolean internalDeleteRight) {
		this.internalDeleteRight = internalDeleteRight;
	}
	

}
