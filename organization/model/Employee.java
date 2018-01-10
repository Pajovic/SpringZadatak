package organization.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Employee {
	
	@Id
	private String id;	
	private int employeeId;	
	private String name;
	private int organizationId;		
	private String lastName;
	private String organizationName;
	private boolean internalReadRight;
	private boolean internalCreateRight;
	private boolean internalUpdateRight;	
	private boolean internalDeleteRight;

	
	public Employee() {
		
	}

	public Employee(int employeeId,int organizationId, String name, String lastName) {
		this.employeeId = employeeId;
		this.organizationId = organizationId;			
		this.name = name;
		this.lastName = lastName;
				
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

	public String getId() {
		return id;
	}
	
	public void setId(int employeeId, int organizationId) {
		StringBuilder str = new StringBuilder();
		str.append(employeeId);
		str.append("_");
		str.append(organizationId);
		this.id = str.toString();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public boolean getReadRight() {
		return internalReadRight;
	}
	public void setReadRight(boolean readRight) {
		this.internalReadRight = readRight;
	}
	public boolean getCreateRight() {
		return internalCreateRight;
	}
	public void setCreateRight(boolean createRight) {
		this.internalCreateRight = createRight;
	}
	public boolean getUpdateRight() {
		return internalUpdateRight;
	}
	public void setUpdateRight(boolean updateRight) {
		this.internalUpdateRight = updateRight;
	}
	public boolean getDeleteRight() {
		return internalDeleteRight;
	}
	public void setDeleteRight(boolean deleteRight) {
		this.internalDeleteRight = deleteRight;
	}
	
}
