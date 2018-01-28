package organization.wrappers;

/**
 * Wrapper class used for modifying allowance of organizations external RUD rights.
 * @author Pajovic Petar
 *
 */
public class OrganizationExternalAccessWrapper {

	private String id;
	private boolean isAllowed;
	
	public OrganizationExternalAccessWrapper() {
		
	}
	public OrganizationExternalAccessWrapper(String id, boolean isAllowed) {
		
		this.id = id;
		this.isAllowed = isAllowed;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean getIsAllowed() {
		return isAllowed;
	}

	public void setAllowed(boolean isAllowed) {
		this.isAllowed = isAllowed;
	}
}