package organization.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class OrganizationData {
		
	@Id	
	private String name;
	private int organizationId;	
	private int amount;
	private float price;	
	

	public OrganizationData() {
		
	}
	
	public OrganizationData(String name, int amount, float price, int organizationId) {		
		this.name = name;
		this.amount = amount;
		this.price = price;
		this.organizationId = organizationId;
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
