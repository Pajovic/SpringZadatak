package organization.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Organization{
	
	@Id
	private int id;
	private String name;	
	
	
	public Organization() {
		
	}
	
	public Organization(int id, String name) {
		this.id = id;
		this.name = name;		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}