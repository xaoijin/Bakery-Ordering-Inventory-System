/* Written By: Johnson Li, Cyril Thomas, & Talha
 * CSC229
 * DueDate:12-10-22
 * BOSS BAKERY SYSTEM*/

//package
package boss;

//imports
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

public class Store {
	Vector <Products> item; // Vector to Hold Products
	Vector <Users> account; //Vector to Hold All Users
	Vector <Orders> invoice; //Vector to Hold Order Information
	int numUupdates;
	int loggedinuser = -1;
	Scanner Keyboard;

	int test = 0;


	//Database objects
	Connection connection;
	Statement statement;
	ResultSet resultSet;

	/************************************* CONNECT TO DATABASE*************************************************/
	void connectDB() throws ClassNotFoundException, SQLException
	{
		// Step 1: Loading or registering JDBC driver class 

		Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

		// Step 2: Opening database connection
		String msAccDB = "bossdatabase.accdb"; //Check if this correct - merged and might have messed this comnnect
		String dbURL = "jdbc:ucanaccess://" + msAccDB; 

		// Step 3: Create and get connection using DriverManager class
		connection = DriverManager.getConnection(dbURL); 

		// Step 4: Creating JDBC Statement 
		// It is scrollable so we can use next() and last() & It is updatable so we can enter new records
		statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);


		System.out.println("Database Connected!");
	}
	/************************************************ END OF DATABASE CONNECT******************************************************/	 
	/*******************************************************CONSTRUCTOR*****************************************************/
	Store()
	{
		item = new Vector<Products>();
		account = new Vector<Users>();
		invoice = new Vector<Orders>();
		Keyboard = new Scanner (System.in);

	}
	/*******************************************************ENDOFCONSTRUCTOR*****************************************************/

	/*******************************************************LOAD METHODS
	 * @throws SQLException ******************************************************/
	//this method calls the load methods for each text files

	void loadData() throws IOException, SQLException
	{


		loadUsers();
		loadProducts();
		loadOrders();

	}
	/**************************************************ENDOFLOADMETHODS******************/
	/*******************************************************LOAD USER********************
	 * @throws SQLException ******************************/

	void loadUsers() throws IOException, SQLException {
		String i = ""; //userID
		String en = ""; //username
		String psw = "";//password
		String pn = ""; //phone
		String em = ""; //email
		String n = ""; //Full Name
		Boolean isEm = false;

		int totalrows = 0, index = 0;

		resultSet = statement.executeQuery("SELECT * FROM Users");
		while (resultSet.next()) //tests for the eof
		{   totalrows = resultSet.getRow();

		i = resultSet.getString("UserID"); // or 	i = resultSet.getString(1);
		en = resultSet.getString("UserName");
		psw = resultSet.getString("Password");
		pn = resultSet.getString("Phone");
		em =  resultSet.getString("Email");
		n = resultSet.getString("FullName");

		isEm = Boolean.parseBoolean(resultSet.getString("isEmployee")); //checks if user is a employee
		account.add(new Users(i,en,psw,pn,em,n,isEm));
		index++;
		}
		System.out.println("Employees Loaded");
	} 
	/*******************************************************END OF LOAD USER*****************************/

	/*******************************************************LOAD PRODUCTS********************************
	 * @throws SQLException *****************************/

	//Need to Check this Load Products - Not sure if logic is right

	void loadProducts() throws IOException, SQLException
	{
		String pi = ""; //productID
		String pn = ""; //productName
		double pc = 0.00; //productPrice
		String ds = ""; //productDescriptiom

		int totalrows = 0, index = 0;

		resultSet = statement.executeQuery("SELECT * FROM Products"); 
		while (resultSet.next()) //tests for the eof
		{   totalrows = resultSet.getRow();
		pi = resultSet.getString("ProductID");
		pn = resultSet.getString("Name");
		pc = Double.parseDouble(resultSet.getString("Price"));
		ds = resultSet.getString("Description");

		item.add(new Products(pi,pn,pc,ds));


		index++;
		}
		System.out.println("Bakery Loaded");
	} //end of loadProducts() method

	/*******************************************************END OF LOAD PRODUCTS******************************/

	/*******************************************************LOAD INVOICE******************************/

	void loadOrders() throws IOException, SQLException
	{

		//create the variables for each field in the database
		String uID = ""; //userID
		String oID = ""; //orderID
		String oS = ""; //orderStatus
		String oD = ""; //orderDate
		String cD = ""; //completedDate
		String oI; //array of orderedItems
		String oQ; //array of ordered Quantity


		double p = 0.00;
		int totalrows = 0, index = 0;

		//Get the total rows in the table to loop through the result set
		resultSet = statement.executeQuery("SELECT * FROM Orders"); 

		while (resultSet.next()) //tests for the eof
		{   totalrows = resultSet.getRow();
		uID = resultSet.getString("UserID"); //userID
		oID = resultSet.getString("OrderID"); //orderID
		oS = resultSet.getString("OrderStatus"); //orderStatus
		oD = resultSet.getString("OrderDate"); //orderDate
		cD = resultSet.getString("CompletedDate");  //completedDate
		oI = resultSet.getString("OrderItems");  //orderID
		oQ= resultSet.getString("Quantities"); //orderquanity
		p = resultSet.getDouble("Price"); //price
		String tempoI[] = oI.split("\\+");
		String[] tempoQ = oQ.split("\\+");

		invoice.add(new Orders(uID,oID,oS,oD,cD, tempoI,tempoQ,p)); //add to the arraylist
		index++;
		}
		System.out.println("Orders Loaded");
	} 

	/*******************************************************END OF LOADINVOICE******************************/ 
	/*******************************************************LOGIN METHOD * @throws IOException 
	 * @throws SQLException ***************/

	void login() throws IOException, SQLException {
		String username ="", password ="";

		boolean isValid = false;
		int counter = 0;
		System.out.println("*********************************************************************LOGIN SCREEN*********************************************");

		do {
			counter++;
			System.out.print("Enter UserName: ");
			username = Keyboard.next();
			System.out.print("Enter Password: ");
			password = Keyboard.next();

			for(int i = 0; i < account.size(); i++) {
				if(username.equals(account.get(i).getUsername()) && password.equals(account.get(i).getPassword())) {
					isValid = true;

					loggedinuser = i;

				}
			}
			System.out.println("Invalid Username or Password!");
		} while(counter < 3 && !isValid);

		if(!isValid) {
			System.out.println("Max Attempts Exceeded, Shutting Down System");
			System.exit(0);

		}
		//Welcome the Logged In User

		System.out.println("Welcome "+ account.get(loggedinuser).getUsername() + " Employee: " + account.get(loggedinuser).isEmployee());
		if(account.get(loggedinuser).isEmployee()) {

			welcome();
			showMenu();

		}else{
			showEmployeeMenu();
		}

	}

	/*******************************************************END OF LOGIN METHOD***************************************************/			
	/*******************************************************WELCOME METHOD******************************************************/

	//Welcome Menu
	void welcome()

	{
		System.out.println("\n\n");
		System.out.println("--------------------------------------------------------------------------------------------------------------------------");
		System.out.println("                                                                         WELCOME!!!                                       ");
		System.out.println("---------------------------------------------------------------------------------------------------------------------------");	
		System.out.println("                    \\                     .----------------.  .----------------.  .----------------.  j.----------------.");
		System.out.println("                     @                     | .--------------. || .--------------.| |.--------------. |.--------------.|");
		System.out.println("                 #;;;;;;;#                 | |   ______     | || |     ____     || ||   _________  | |||    _______   | |");
		System.out.println("                ;;;+;;;;;;;                | |  |_   _ \\   | || |   .'    `.   || ||  /  ___    | | |||   /  ___  |  | |");
		System.out.println("               ;;;;;(;;;O;;;               | |    | |_) |   | || |  /  .--.  \\ || ||  |  (__ \\_| | |||  |  (__ \\_| | |");
		System.out.println("            ;;;@+;;;;;;;;+;;;;+            | |    |  __'.   | || |  | |    | |  || ||   '.___`-.   | |||   '.___`-.   | |");
		System.out.println("          +;;;;;;;;;;;;;;);;;;;;;@         | |   _| |__) |  | || |  \\ `--'  /  || ||  |`\\____) | | |||  |`\\____) | | |");
		System.out.println("      ;;;;;;;;;;;;;;;;;;;;;;;;@;;;;        | |  |_______/   | || |   `.____.'   || ||  |_______.'  | |||  |_______.'  | |");
		System.out.println("      @;;;;;;;;;O;;;;;;;;;+;;;;;(;;;;#     | |              | || |              || ||              | |||              | |");
		System.out.println(" ;;@;;;;;;;;;);;;;;;;;;;;;;;;;;;;;;;;;;;;' | '--------------' || '--------------'|  '--------------' ||'--------------'|");
		System.out.println(" ;;;;;;;;O;;;;;;;;;;;;;;;;;;);;;;;;+;;;;@   '----------------'  '----------------'  '----------------'  '----------------' ");
		System.out.println("   ;;;;;);;;;;;;;;;;O;;;;;;;;;;;+;;;;;;;    .----------------.  .----------------.  .----------------.  .----------------.");
		System.out.println("   ;;;;;;;;;;;;;;;;;;;;+#+;;;;;;;;;;'@#     | .--------------. || .--------------.| |.--------------. |.--------------.|");
		System.out.println("   #,'@@@',..+@++#@+......;.;@#@@@;...#     | |   ______     | || |     ____     || ||   _________  | |||    _______   | |");
		System.out.println("   $...$.......$..........$......$...$      | |  |_   _ \\   | || |   .'    `.   || ||  /  ___    | | |||   /  ___  |  | |");
		System.out.println("    ...'.......;..........@......@...;      | |    | |_) |   | || |  /  .--.  \\ || ||  |  (__ \\_| | |||  |  (__ \\_| | |");
		System.out.println("     @...;......+........@.....#...@        | |    |  __'.   | || |  | |    | |  || ||   '.___`-.   | |||   '.___`-.   | |");
		System.out.println("      @...,.....@.......;......@...,        | |   _| |__) |  | || |  \\ `--'  /  || ||  |`\\____) | | |||  |`\\____) | | |");
		System.out.println("      ;...@.....;.......;.....,....         | |  |_______/   | || |   `.____.'   || ||  |_______.'  | |||  |_______.'  | |");
		System.out.println("       ;..;.....;.......@.....'...@         | |              | || |              || ||              | |||              | |");
		System.out.println("         @+.....,.......#......@@           | '--------------' || '--------------'|  '--------------' || '--------------'|");
		System.out.println("               @@#';,;;+@@@                 '----------------'  '----------------'  '----------------'  '----------------' ");


		System.out.println("****************************************************************************************************************************");	
		System.out.println("                                                                  Welcome to BOSS System!");
		System.out.println("****************************************************************************************************************************");
		System.out.println();
		System.out.println();
		System.out.println();
	}

	/**************************************************END OF WELCOME METHOD******************************************************/			

	/*******************************************************SHOW USER MENU METHOD * @throws IOException 
	 * @throws SQLException *********/

	//arrays = 0 (index - 1)

	//Display Menu
	void showMenu() throws IOException, SQLException
	{	
		int option = -1;

		do {

			System.out.println("\n\n");
			System.out.println("-------------------------------------------");
			System.out.println("             BOSS USER MENU            ");
			System.out.println("-------------------------------------------");	
			System.out.println("1. View Bakery");
			System.out.println("2. Place Order");
			System.out.println("3. Check Order Status");
			System.out.println("4. Cancel Order");
			System.out.println("5. Display Invoice");
			System.out.println("6. Register New User");
			System.out.println("7. Exit");
			System.out.print("Choose a system process (1-6): ");
			option = Keyboard.nextInt();
			switch (option)
			{
			case (1):
			{	 viewBakery();
			break;
			}
			case (2):
			{	createOrder();
			break;
			}
			case (3):
			{	 checkStatus();
			break;
			}
			//	case 4):  - Needs to be written
			//	{	//cancelOrder);
			//		break;
			//	}

			case (5):
			{	viewInvoice();
			break;
			}
			case (6):
			{	addNewUser();
			break;
			}
			case (7):
			{	

				updateRecords();
				exitBOSSSystem();

				System.out.println("\n\n");
				System.out.println("****************************************************************************");	
				System.out.println("Thank you for using BOSS - Program Terminated!");
				System.out.println("****************************************************************************");	
				System.exit(0);
			}
			default:
			{	System.out.println("Invalid choice, please choose between 1-7"); }

			} //end of switch
		}while (option != 7);


	}


	/************************************************END OF SHOW USER MENU METHOD********************
	 * @throws IOException ******************************************************/			

	/************************************************SHOW EMPLOYEE MENU METHOD************************
	 * @throws IOException 
	 * @throws SQLException ******************************************************/
	//Display Menu
	void showEmployeeMenu() throws IOException, SQLException
	{	
		int option = -1;

		do {
			//DISPLAY MENU HEADER
			System.out.println("\n\n");
			System.out.println("-------------------------------------------");
			System.out.println("                BOSS EMPLOYEE MENU               ");
			System.out.println("-------------------------------------------");	
			System.out.println("1. View Bakery");
			System.out.println("2. Update Order Status");
			System.out.println("3. Contact Customer");
			System.out.println("4. Exit");
			System.out.print("Choose a system process (1-4): ");
			option = Keyboard.nextInt();
			switch (option)
			{
			case (1):{
				viewBakery();
				break;
			}
			case (2):
			{	 changeStatus();
			break;
			}
			case (3):
			{	 //contactUser(); Needs to be completed
				break;
			}
			case (4):
			{	updateRecords();
			exitBOSSSystem();

			System.out.println("Thank you for using BOSS System, - Program Terminated!");
			System.out.println("****************************************************************************\n\n");	
			System.exit(0);
			}
			default:
			{	System.out.println("Invalid choice, please choose between 1-4"); }

			} //end of switch
		}while (option != 4);


	}

	/************************************************END OF SHOW EMPLOYEE MENU METHOD*******************************************/			

	/****************************************************START VIEW BAKERY****************************/
	@SuppressWarnings("unchecked")


	void viewBakery() {
		System.out.println("View BOSS Bakery:");
		System.out.println();
		while (((Iterator<String>) item).hasNext()) {
			System.out.print(Products.next() + " ");
		}
	}
	/***************************************************END VIEW BAKERY*******************************************************/		


	/************************************************CREATE ORDER************************************************************/

	void createOrder() throws SQLException 
	{
		Scanner scan = new Scanner(System.in); 

		System.out.println("Please enter the userID: ");
		String tempuserID = scan.nextLine();
		System.out.println("Please enter the orderId: ");
		String temporderID = scan.nextLine();
		System.out.println("Please enter the order Status: ");
		String temporderStatus = scan.nextLine();
		System.out.println("Please enter the order Date: ");
		String temporderDate = scan.nextLine();
		System.out.println("Please enter the order completed date: ");
		String tempcompletedDate = scan.nextLine();
		System.out.println("Please enter the order items: ");
		String temporderItems = scan.nextLine();
		System.out.println("Please enter the order quantity: ");
		String temporderQuantity = scan.nextLine();
		System.out.println("Please enter the order price: ");
		double tempprice = scan.nextDouble();


		//johnson please check next three lines	- Needs Review/Check Logic/Implementation

		//	 Orders newOrder = new Orders (tempuserID,temporderID, temporderStatus, temporderDate, 
		//			 tempcompletedDate,temporderItems[],temporderQuantity[],tempprice);

		//	 invoice.add(newOrder);

	}

	/** *********************************************ENDOFCREATEORDER*******************************************************/	

	/************************************************Check Status METHOD **************************************************/

	void checkStatus ()
	{
		String oid = ""; //OrderID
		String uid = ""; //UserID
		boolean isValid = false;

		System.out.println("Check Your Order Status:");
		System.out.println("Enter Your UserID");
		uid = Keyboard.next();
		System.out.println("Enter Your OrderID");
		oid = Keyboard.next();

		for(int i = 0; i < invoice.size(); i++) {

			if( oid.equals(invoice.get(i).getOrderID())) {
				isValid = true;
				loggedinuser = i;


				System.out.println("Order Status for  Order#:" + oid + " " + invoice.get(2) + " " + getSystemDate());
			}
		}

		//Check LOGIC - NOT SURE IF CORRECT OR VALID @Johnson
	}

	/**************************************************END Check Status Method ********************************************/

	/***************************************************Change Status METHOD *********************************************/


	void changeStatus ()
	{
		String oid = ""; //OrderID
		String uid = ""; //UserID//
		String newStatus = ""; //UserInput for Status Change

		boolean isValid = false;

		System.out.println("Update Order Status: ");

		System.out.println("Enter OrderID To Be Updated: ");
		uid = Keyboard.next();

		for(int i = 0; i < invoice.size(); i++) {

			if( oid.equals(invoice.get(i).getOrderID())) {
				isValid = true;
				loggedinuser = i;

				System.out.println("Enter New Status To Be Updated: ");

				newStatus = Keyboard.next();

				// ((Orders) invoice).setOrderStatus(newStatus); //issues on this line - @Johnson - Also Check Logic

				System.out.println("New Order Status for Order#: " + oid + " is: " + invoice.get(2) + " " +getSystemDate());

			}
		}}			
	/***************************************************END Change Status Method **************************************************/							
	/** *************************************************START search INVOICE****************************************************/

	@SuppressWarnings({ "static-access", "unchecked" })
	void viewSearch() throws SQLException 
	{
		//DISPLAY MENU HEADER
		System.out.println("\n\n");
		System.out.println("-------------------------------------------");
		System.out.println("            Search INVOICE:              ");
		System.out.println("-------------------------------------------");	
		Scanner scan = new Scanner(System.in); 
		int result=-1;
		System.out.println("Please Enter the OrderID: ");
		String order = scan.nextLine(); 

		result=invoice.indexOf(order);  //logic check - @Johnson
		if (result>-1)
		{
			System.out.print("UserID: " + invoice.get(result).getUserID());
			System.out.print("UserName: " + account.get(result).getUsername());
			System.out.print("OrderDate: " + invoice.get(result).getOrderDate());
			System.out.print("CompletedDate: " + invoice.get(result).getCompletedDate());
			System.out.print(invoice.get(result).getOrderItems());
			System.out.print(invoice.get(result).getOrderItems());
			System.out.print(invoice.get(result).getOrderItems());
		}	 
		else 
		{
			System.out.println("The orderID of "+result+" does not exist exist.");
		}
	}								
	/*****************************************END search INVOICE *************************************************************/

	/*******************************************************ADD NEW USER*******************************************************/

	void addNewUser() {
		//DISPLAY MENU HEADER
		System.out.println("\n\n");
		System.out.println("-------------------------------------------");
		System.out.println("            CREATE NEW USER               ");
		System.out.println("-------------------------------------------");	

		Scanner input = new Scanner(System.in);

		System.out.print("Enter User ID: ");
		String userID = input.nextLine();

		System.out.print("Enter New UserName: ");
		String username = input.nextLine();

		System.out.print("Enter New Password ");
		String password = input.nextLine();

		System.out.print("Enter Phone: ");
		String phone = input.nextLine();

		System.out.print("Enter Email: ");
		String email = input.nextLine();

		System.out.print("Enter Name: ");
		String name= input.nextLine();

		System.out.print("Is User an Employee(True/False): ");
		Boolean employee = input.nextBoolean();

		Users nUser = new Users (userID,username, password, phone, email,name,
				employee);


		account.add(nUser);


		numUupdates++;	//update the User flag	

	}							
	/*******************************************************ENDOFADDUSER******************************************************/							 	
	/********************************************************UPDATE RECORDS* @throws IOException ****************************/
	void updateRecords() throws IOException
	{
		//update Employee records
		if (numUupdates >0)
		{
			FileWriter fw=new FileWriter("users2.dat", true); //check if this is still valid @Johnson since were using DB
			BufferedWriter bw = new BufferedWriter(fw);
			String em = "false";

			for (int i = 0; i < account.size(); i++)
			{	 bw.write(account.get(i).getUserID() + "," +
					account.get(i).getUsername() + "," + 
					account.get(i).getPassword() + "," +
					account.get(i).getPhone()+ "," +
					account.get(i).getEmail() + "," +
					account.get(i).getName() + "," );

			if (account.get(i).isEmployee()) em = "true";
			bw.write(em +  "\n");
			em = "false"; //reset manager
			} //write each line separated by the enter key

			System.out.println("User File Update Successfully " + getSystemDate());
			bw.close();


		}

	}
	/************************************************ END OFUPDATE RECORDS****************************************************/
	
	/***************************************************start view invoice
	 * @throws SQLException *******************************************************/
	 @SuppressWarnings("unchecked")
		void viewInvoice() throws SQLException {
			 while (((Iterator<String>) item).hasNext()) {
		            System.out.println(((ResultSet) invoice).next() + " ");
		        }
			}
		 /***************************************************end view invoice*******************************************************/
	
	 /** *********************************************start delete order***************************************************/
	 
	 void deleteOrder() throws SQLException 
	 {
		 Scanner scan = new Scanner(System.in); 
		 int result=-1;
		 
		 System.out.println("Please enter the orderID you want to delete: ");
		 String temporderID = scan.nextLine();
		 
		 result=invoice.indexOf(temporderID);  //logic check
		 
		 String tempuserID = (invoice.get(result).getUserID());
		 String temporderStatus = (invoice.get(result).getOrderStatus());
		 String temporderDate = (invoice.get(result).getOrderDate());
		 String tempcompletedDate = (invoice.get(result).getCompletedDate());
		 String[] temporderItems = (invoice.get(result).getOrderItems());
		 String[] temporderQuantity = (invoice.get(result).getOrderQuantity());
		 double tempprice = (invoice.get(result).getPrice());
						 				 
                                                                            //johnson please check next three lines	
		 
	//	 Orders deleteOrder = new Orders (tempuserID,temporderID, temporderStatus, temporderDate, 
	//			 tempcompletedDate,temporderItems[],temporderQuantity[],tempprice);
		 
	//	 invoice.delete(deleteOrder);
		 				 
	 }
	 
	 /** *********************************************end delete order***************************************************/
	 
	 
	 
	
	/************************************************CAPTURE DATE*************************************************************/

	/************************************************GET TIME STAMP**********************************************************/
	String getSystemDate()
	{ 	String timestamp = ""; //Create a string to hold the date
	String pattern = "MM-dd-yyyy HH:mm:ss"; //Determine the pattern for the date and time fields
	SimpleDateFormat formatter = new SimpleDateFormat(pattern); //Set your date and time pattern
	Date date = new Date(0); //Capture the system datetime in milliseconds
	timestamp = formatter.format(date); //Format the date based on the pattern
	return timestamp;
	}
	/***********************************************END GET TIME STAMP*******************************************************/
	/******************************EXIT SIM SYSTEM**************************************************************************/
	void exitBOSSSystem() throws SQLException
	{	System.out.println("\n\n--------------------------------------------");
	System.out.println("Thank you for using BOSS System, Program Ended!");
	System.out.println("-----------------------------------------------");
	System.exit(0);
	connection.close();
	}
	/***********************************************END EXIT SIM SYSTEM****************************************************/

}