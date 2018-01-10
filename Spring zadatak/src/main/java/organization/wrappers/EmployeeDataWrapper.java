package organization.wrappers;

/**
 * Wrapper class used for entering new data by employees.
 * @author Pajovic Petar
 * 
 */
public class EmployeeDataWrapper {
	
	private int employeeId;
	private int organizationId;
	private int dataId;
	private int organizationIdOfData;	
	private String name;
	private int amount;
	private float price;
	
	
	public EmployeeDataWrapper(int employeeId,int dataId, int organizationId,int organizationIdOfData, String name, int amount, float price) {
		this.employeeId = employeeId;
		this.name = name;
		this.amount = amount;
		this.price = price;
		this.organizationId = organizationId;
		this.organizationIdOfData = organizationIdOfData;
		this.dataId = dataId;
	}
	public EmployeeDataWrapper(int employeeId, int dataId, int organizationId, String name, int amount, float price) {
		this.employeeId = employeeId;
		this.name = name;
		this.amount = amount;
		this.price = price;
		this.organizationId = organizationId;
		this.dataId = dataId;
	}
	public int getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
	}
	public EmployeeDataWrapper() {
		
	}
	
	public int getDataId() {
		return dataId;
	}
	public void setDataId(int dataId) {
		this.dataId = dataId;
	}
	public int getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getOrganizationIdOfData() {
		return organizationIdOfData;
	}
	public void setOrganizationIdOfData(int organizationIdOfData) {
		this.organizationIdOfData = organizationIdOfData;
	}

}
