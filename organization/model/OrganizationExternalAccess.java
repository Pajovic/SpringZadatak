package organization.model;

import java.util.HashSet;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Query;

@Document
public class OrganizationExternalAccess{
	
	
	@Id
	private String id;
	private Integer externalAccessId;	
	private Integer organizationId;		
	private String criteria;	
	private EmployeeRight right;
	private boolean isAllowed = false;
	private HashSet<Integer> organizationIds = new HashSet<Integer>();
	
	public OrganizationExternalAccess() {
		
	}
	
	public OrganizationExternalAccess(Integer externalAccessId, Integer organizationId, String criteria, EmployeeRight exRight, HashSet<Integer> organizationIds) {
		this.externalAccessId = externalAccessId;
		this.organizationId = organizationId;
		this.criteria = criteria;
		this.right = exRight;
		this.organizationIds = organizationIds;
		
	}
	
	
	public HashSet<Integer> getOrganizationIds() {
		return organizationIds;
	}

	public void setOrganizationIds(HashSet<Integer> organizationIds) {
		this.organizationIds = organizationIds;
	}

	
	public Integer getExternalAccessId() {
		return externalAccessId;
	}

	public void setExternalAccessId(Integer externalAccessId) {
		this.externalAccessId = externalAccessId;
	}
	
	public EmployeeRight getRight() {
		return right;
	}
	
	public void setRight(EmployeeRight right) {
		this.right = right;
	}

	public String getId() {
		return id;
	}

	public void setId(Integer externalAccessId, Integer organizationId) {
		StringBuilder str = new StringBuilder();
		str.append(externalAccessId);
		str.append("_");
		str.append(organizationId);
		this.id = str.toString();
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	
	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}
	
	public boolean getIsAllowed() {
		return isAllowed;
	}

	public void setAllowed(boolean isAllowed) {
		this.isAllowed = isAllowed;
	}
	/**
	 * 
	 * @author Pajovic Petar
	 *Acronyms of Create, Read, Update or Delete rights.
	 */
	public enum EmployeeRight {
		C,
		R, 
		U,
		D
	}
}




