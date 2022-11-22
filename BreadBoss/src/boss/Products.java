package boss;

public class Products {
	
	private String productID; 
	private String productName;
	private double price;
	private boolean isVegan; //checksif ProductisVegan
	private boolean isNutFree; //checks if Product is Nut Free
	private boolean isDairyFree; //checks if Product is Dairy Free
	
	public Products(String productID, String productName, double price, boolean isVegan, boolean isNutFree,
			boolean isDairyFree) 
	{
		super();
		this.productID = productID;
		this.productName = productName;
		this.price = price;
		this.isVegan = isVegan;
		this.isNutFree = isNutFree;
		this.isDairyFree = isDairyFree;
	}
	
	public Products() 
	{
		this.productID ="NULL";
		this.productName = "NULL";
		this.price = 0.00;
		this.isVegan = false;
		this.isNutFree = false;
		this.isDairyFree = false;
	}
	
	
	public String getProductID() 
	{
		return productID;
	}
	public void setProductID(String productID)
	{
		this.productID = productID;
	}
	public String getProductName()
	{
		return productName;
	}
	public void setProductName(String productName) 
	{
		this.productName = productName;
	}
	public double getPrice() 
	{
		return price;
	}
	public void setPrice(double price)
	{
		this.price = price;
	}
	public boolean isVegan() 
	{
		return isVegan;
	}
	public void setVegan(boolean isVegan)
	{
		this.isVegan = isVegan;
	}
	public boolean isNutFree() 
	{
		return isNutFree;
	}
	public void setNutFree(boolean isNutFree) 
	{
		this.isNutFree = isNutFree;
	}
	public boolean isDairyFree() {
		return isDairyFree;
	}
	public void setDairyFree(boolean isDairyFree) 
	{
		this.isDairyFree = isDairyFree;
	}
	
	
	
	
}
