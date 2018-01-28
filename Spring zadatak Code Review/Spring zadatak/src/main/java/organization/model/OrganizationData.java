package organization.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class OrganizationData {
		
	@Id	
	private String id;
	private int dataId;
	private int organizationId;
	private String name;		
	private int amount;
	private float price;	
	
	public OrganizationData() {
		
	}
	
	public OrganizationData(int dataId, int organizationId, String name, int amount, float price) {		
		this.dataId = dataId;
		this.organizationId = organizationId;
		this.name = name;
		this.amount = amount;
		this.price = price;				
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

	public int getDataId() {
		return dataId;
	}

	public void setDataId(int dataId) {
		this.dataId = dataId;
	}

	public int getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(int organizationId) {
		this.organizationId = organizationId;
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

}
