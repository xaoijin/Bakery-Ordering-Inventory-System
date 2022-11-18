package boss;

public class Users {
	private String userID;
	private String username;
	private String password;
	private String Phone;
	private String Email;
	private String Name;
	private boolean isEmployee;
	public Users(String userID, String username, String password, String phone, String email, String name,
			boolean isEmployee) {
		super();
		this.userID = userID;
		this.username = username;
		this.password = password;
		Phone = phone;
		Email = email;
		Name = name;
		this.isEmployee = isEmployee;
	}
	
	public Users() {
		
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
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public boolean isEmployee() {
		return isEmployee;
	}
	public void setEmployee(boolean isEmployee) {
		this.isEmployee = isEmployee;
	}
}
