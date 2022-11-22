package boss;

public class Users {
	private String userID;
	private String username;
	private String password;
	private String phone;
	private String email;
	private String name;
	private boolean isEmployee;
	public Users(String userID, String username, String password, String phone, String email, String name,
			boolean isEmployee) {
		super();
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.phone = phone;
		this.email = email;
		this.name = name;
		this.isEmployee = isEmployee;
	}
	public Users() {
		this.userID = "NULL";
		this.username = "NULL";
		this.password = "NULL";
		this.phone = "NULL";
		this.email = "NULL";
		this.name = "NULL";
		this.isEmployee = false;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isEmployee() {
		return isEmployee;
	}
	public void setEmployee(boolean isEmployee) {
		this.isEmployee = isEmployee;
	}
	
	//random
	
	
}
