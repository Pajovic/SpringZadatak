package organization.wrappers;
/**
 * Wrapper class used for entering grouping data and employees rights over them.
 * @author Pajovic Petar
 *
 */
public class DataRightsWrapper {
	
	private String id;
	private String name;
	private String amount;
	private String price;
	private String rights;
	
	public DataRightsWrapper() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getRights() {
		return rights;
	}

	public void setRights(String rights) {
		this.rights = rights;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}




}
