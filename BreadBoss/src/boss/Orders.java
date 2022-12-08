package boss;

import java.util.Date;

public class Orders {
	
	private static String userID;
	private String orderID;
	private String orderStatus; //Order Status - Started, In Progress, Completed, Declined
	private Date orderDate; //Order Placed Date
	private Date completedDate; //Order Completed Date
	private String [] orderItems ; //Array of Ordered Items
	private String [] orderQuantity;// Array of Ordered Quantity
	private double price;
	
	public Orders(String userID, String orderID, String orderStatus, Date orderDate,
			Date completedDate, String[] orderItems, String[] orderQuantity, double price) 
	

	{
		super();
		this.userID = userID;
		this.orderID = orderID;
		this.orderStatus = orderStatus;
		
		this.orderDate = orderDate;
		this.completedDate = completedDate;
		this.orderItems = orderItems;
		this.orderQuantity = orderQuantity;
		this.price = price;
	}
	
	public Orders() {
		this.userID = "NULL";
		this.orderID = "NULL";
		this.orderStatus = "NULL";
		
		this.orderDate = null;
		this.completedDate = null;
		this.orderItems = null;
		this.orderQuantity = null;
		this.price = 00.00;
	}
	
	public static String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getOrderID() {
		return orderID;
	}
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public Date getCompletedDate() {
		return completedDate;
	}
	public void setCompletedDate(Date completedDate) {
		this.completedDate = completedDate;
	}
	public String[] getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(String[] orderItems) {
		this.orderItems = orderItems;
	}
	public String [] getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(String [] orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
}
