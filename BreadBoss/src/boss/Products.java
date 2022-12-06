package boss;

public class Products {
	
	private String productID; 
	private String productName;
	private double price;
	private String description;
	public Products(String productID, String productName, double price, String description) {
		super();
		this.productID = productID;
		this.productName = productName;
		this.price = price;
		this.description = description;
	}
	public Products() {
		this.productID = "NULL";
		this.productName = "NULL";
		this.price = 0.00;
		this.description = "NULL";
	}
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public static String next() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
