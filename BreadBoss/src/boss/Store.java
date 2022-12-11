/* Written By: Johnson Li, Cyril Thomas, & Talha
 * CSC229
 * DueDate:12-10-22
 * BOSS BAKERY SYSTEM*/

//package
package boss;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class Store {
	Vector <Products> item; // Vector to Hold Products
	Vector <Users> account; //Vector to Hold All Users
	Vector <Orders> orders; //Vector to Hold Order Information
	int numUupdates;
	int loggedinuser = -1;
	Scanner Keyboard;
	String loggedinUserID = "";
	int test = 0;
	

	//Database objects
	Connection connection;
	Statement statement;
	PreparedStatement secureStatement;
	ResultSet resultSet;
	
	/************************************* CONNECT TO DATABASE*************************************************/
	void connectDB() throws ClassNotFoundException, SQLException
	{
		// Step 1: Loading or registering JDBC driver class 

		Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");

		// Step 2: Opening database connection
		String msAccDB = "bossdatabase.accdb"; 
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
		orders = new Vector<Orders>();
		Keyboard = new Scanner (System.in);

	}
	/*******************************************************ENDOFCONSTRUCTOR*****************************************************/

	/*******************************************************LOAD METHODS******************************************************/
	//this method calls the load methods for each text files

	void loadData() throws IOException, SQLException, ParseException
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
		Boolean isEm = false; //Checks if Employee

		int totalrows = 0, index = 0;

		resultSet = statement.executeQuery("SELECT * FROM Users");
		while (resultSet.next()) 
		{   totalrows = resultSet.getRow();

		i = resultSet.getString("UserID"); 
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

	/*******************************START LOAD PRODUCTS********************************/

	void loadProducts() throws IOException, SQLException
	{
		String pi = ""; //productID
		String pn = ""; //productName
		double pc = 0.00; //productPrice
		String ds = ""; //productDescriptiom

		int totalrows = 0, index = 0;

		resultSet = statement.executeQuery("SELECT * FROM Products"); 
		while (resultSet.next()) 
		{   totalrows = resultSet.getRow();
		pi = resultSet.getString("ProductID");
		pn = resultSet.getString("Name");
		pc = Double.parseDouble(resultSet.getString("Price"));
		ds = resultSet.getString("Description");

		item.add(new Products(pi,pn,pc,ds));


		index++;
		}
		System.out.println("Bakery Loaded");
	} 

	/*********************************END OF LOAD PRODUCTS******************************/

	/*********************************START LOAD ORDERS
	  ******************************/

	void loadOrders() throws IOException, SQLException, ParseException
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
		oD = resultSet.getString("OrderDate");
		cD = resultSet.getString("CompletedDate");  //completedDate
		oI = resultSet.getString("OrderItems");  //orderID
		oQ= resultSet.getString("Quantities"); //order quanity
		p = resultSet.getDouble("Price"); //price
		String[] tempoI = oI.split("//+");
		String[] tempoQ = oQ.split("//+");
		orders.add(new Orders(uID,oID,oS,oD,cD, tempoI,tempoQ,p)); //add to the vector
		index++;
		}
		System.out.println("Orders Loaded");
	} 

	/*******************************************END OF LOAD ORDERS******************************/ 
	/***************************************START LOGIN METHOD***************/

	void login() throws Throwable {
		String username ="", password ="";

		boolean isValid = false;
		int counter = 0;
		System.out.println("\n\n");
        System.out.println("-------------------------------------------");
        System.out.println("            Login to Account               ");
        System.out.println("-------------------------------------------");
		do {
			counter++;
			System.out.print("Enter UserName: ");
			username = Keyboard.next();
			System.out.print("Enter Password: ");
			password = Keyboard.next();

			for(int i = 0; i < account.size(); i++) {
				if(username.equals(account.get(i).getUsername()) && password.equals(account.get(i).getPassword())) {
					isValid = true;
					loggedinUserID = account.get(i).getUserID();
					loggedinuser = i;
				}
			}
			if(!isValid) { //Checks of Valid Username or Password
				System.out.println("Invalid Username or Password!");
			}
		} while(counter < 3 && !isValid);

		if(!isValid) {
			System.out.println("Max Attempts Exceeded, Shutting Down System"); //Gives user 3 Attempts
			System.exit(0);

		}
		//Welcome the Logged In User

		System.out.println("Welcome "+ account.get(loggedinuser).getUsername());
		if(account.get(loggedinuser).isEmployee()) {
			showEmployeeMenu();

		}else{
			showCustomerMenu();
		}

	}

	/************************************END OF LOGIN METHOD***********************/			
	/**********************************START WELCOME METHOD***********************/

	//Welcome Menu
	void welcome() throws Throwable

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
		
		
		boolean validOption = false; //boolean trigger checking case option choice
		do {
			//Initial Menu
			System.out.println("1. Login");
			System.out.println("2. Create Account");
			System.out.println("3. Exit");
			System.out.print("Choose a system process (1-3): ");
			String option = Keyboard.next();
			switch (option) {
			case("1"):{
				login();
				validOption = false;
				break;
			}
			case("2"):{
				createAccount();
				validOption = false;
				break;
			}
			case("3"):{
				exitBOSS();
				validOption = false;
				break;
			}
			default:
			{	System.out.println("Invalid choice, please choose between 1-3"); }
			
			}
		}while(!validOption );
		
	}

	/**************************************************END OF WELCOME METHOD******************************************************/			

	/**************************************************START CUSTOMER MENU METHOD *****************************************************/
	//Display Customer Menu
	void showCustomerMenu() throws Throwable
	{	
		int option = -1;

		do {

			System.out.println("\n\n");
			System.out.println("-------------------------------------------");
			System.out.println("             BOSS CUSTOMER MENU            ");
			System.out.println("-------------------------------------------");
			System.out.println("1. View Bakery");
			System.out.println("2. Check Order Status");
			System.out.println("3. Cancel Order");
			System.out.println("4. View History");
			System.out.println("5. Logout");
			System.out.println("6. Exit System");
			System.out.print("Choose a system process (1-6): ");
			option = Keyboard.nextInt();
			switch (option) {
			case (1): {
				viewBakery(); //View Bakery Products
				break;
			}
			case (2): {
				checkStatus(); //Check Status of Existing Orders
				break;
			}
			case (3): {
				cancelOrder(); //Changes Order Status to Canceled
				break;
			}
			case (4): {
				viewHistory();// shows orders fulfilled
				break;
			}
			case (5): { //LogOut
				System.out.println("Successfully logged out!");
				loggedinUserID = "";
				welcome();// shows orders fulfilled
				break;
			}
			case (6): { //Exits System
				exitBOSS();
			}
			default: {
				System.out.println("Invalid choice, please choose between 1-6");
			}

			} // end of switch
		} while (option != 6);

	}


	/************************************************END CUSTOMER MENU METHOD***************************************************/			

	private void viewHistory() {
		System.out.println("\n\n");
		System.out.println("-------------------------------------------");
		System.out.println("               Order History               ");
		System.out.println("-------------------------------------------");	
		boolean empty = true;
		for(Integer x = 0; x < orders.size(); x++) {
			if(loggedinUserID.equals(orders.get(x).getUserID())) {
				empty = false;
				System.out.println("___________________________________________________________");	
				System.out.println("Status for OrderID "+ orders.get(x).getOrderID() + ": " + orders.get(x).getOrderStatus());
				System.out.println("Listing order items...");
				String convertOIarr = Arrays.toString(orders.get(x).getOrderItems());
				convertOIarr = convertOIarr.replaceAll("\\[", "");//removes left bracket
				convertOIarr = convertOIarr.replaceAll("\\]", "");//removes right bracket
				convertOIarr = convertOIarr.replaceAll("\\s+", "");//removes any spaces
				String convertOQarr = Arrays.toString(orders.get(x).getOrderQuantity());
				convertOQarr = convertOQarr.replaceAll("\\[", "");//removes left bracket
				convertOQarr = convertOQarr.replaceAll("\\]", "");//removes right bracket
				convertOQarr = convertOQarr.replaceAll("\\s+", "");//removes any spaces
				String convertOIstr[] = convertOIarr.substring(0,convertOIarr.length()).split("\\+");//turns the string back to an array with delimiter of "+"
				String convertOQstr[] = convertOQarr.substring(0,convertOQarr.length()).split("\\+");//turns the string back to an array with delimiter of "+"
					System.out.println("You have: ");
					for(Integer s = 0; s < convertOIstr.length; s++) {
						for(Integer a = 0; a < item.size();a++) {
							if(convertOIstr[s].equals(item.get(a).getProductID())) {
								System.out.println(convertOQstr[s] + " " + item.get(a).getProductName());
							}
						}
					}
				double getTotalPrice = orders.get(x).getPrice();
				System.out.printf("Total Price of Order ID: " + orders.get(x).getOrderID() + ": $%.2f \n", getTotalPrice);
				System.out.println("___________________________________________________________");	
			}
		}
		if(empty) {
			System.out.println("No orders found......."); //If No Orders Returned
		}
		
	}
	/************************************************START EMPLOYEE MENU METHOD******************************************************/
	
	//Display Employee Menu
	void showEmployeeMenu() throws Error, Throwable
	{	
		int option = -1;

		do {
			//DISPLAY MENU HEADER
			System.out.println("\n\n");
			System.out.println("-------------------------------------------");
			System.out.println("                BOSS EMPLOYEE MENU         ");
			System.out.println("-------------------------------------------");	
			System.out.println("1. View Bakery");
			System.out.println("2. View Orders");
			System.out.println("3. Update Order Status");
			System.out.println("4. Contact Customer");
			System.out.println("5. Logout");
			System.out.println("6. Exit System");
			System.out.print("Choose a system process (1-6): ");
			option = Keyboard.nextInt();
			switch (option)
			{
			case (1):{
				viewBakery();
				break;
			}
			case (2):
			{	 viewOrders(); //View All Existing Orders
			break;
			}
			case (3):
			{	 changeStatus(); //Change The Status of Order using case
				break;
			}
			case (4):
			{	contactCustomer();//shows orders fulfilled
			break;
			}
			case (5):
			{	
				System.out.println("Successfully logged out!");
				loggedinUserID = "";
				welcome();//shows orders fulfilled
			break;
			}
			case (6):
			{	
				exitBOSS(); //Exits System
			}
			default:
			{	System.out.println("Invalid choice, please choose between 1-6"); }

			} //end of switch
		}while (option != 6);


	}
//view Order Method - Employee Menu
	private void viewOrders() {
		System.out.println("\n\n");
		System.out.println("-------------------------------------------");
		System.out.println("             Viewing All Orders            ");
		System.out.println("-------------------------------------------");
		
		
		for (Integer i = 0; i < orders.size(); i++) {
			String orderName = "";
			System.out.println("___________________________________________________________");
			for(Integer a = 0; a < account.size();a++) {
				if(orders.get(i).getUserID().equals(account.get(a).getUserID())) {
					orderName = account.get(a).getName();
				}
			}
			System.out.println("Order For: " + orderName);
			System.out.println("OrderID: " + orders.get(i).getOrderID());
			System.out.println("Order Status: " + orders.get(i).getOrderStatus());
			System.out.println("Order Started On: " + StringUtils.left(orders.get(i).getOrderDate(), 10));
			System.out.println("Order Pick-uped On: " + StringUtils.left(orders.get(i).getCompletedDate(), 10));
			System.out.println("Listing order items...");
			String convertOIarr = Arrays.toString(orders.get(i).getOrderItems());
			convertOIarr = convertOIarr.replaceAll("\\[", "");// removes left bracket
			convertOIarr = convertOIarr.replaceAll("\\]", "");// removes right bracket
			convertOIarr = convertOIarr.replaceAll("\\s+", "");// removes any spaces
			String convertOQarr = Arrays.toString(orders.get(i).getOrderQuantity());
			convertOQarr = convertOQarr.replaceAll("\\[", "");// removes left bracket
			convertOQarr = convertOQarr.replaceAll("\\]", "");// removes right bracket
			convertOQarr = convertOQarr.replaceAll("\\s+", "");// removes any spaces
			String convertOIstr[] = convertOIarr.substring(0, convertOIarr.length()).split("\\+");// turns the string
																									// back to an array
																									// with delimiter of
																									// "+"
			String convertOQstr[] = convertOQarr.substring(0, convertOQarr.length()).split("\\+");// turns the string
																									// back to an array
																									// with delimiter of
																									// "+"
			System.out.println("You have: ");
			for (Integer s = 0; s < convertOIstr.length; s++) {
				for (Integer a = 0; a < item.size(); a++) {
					if (convertOIstr[s].equals(item.get(a).getProductID())) {
						System.out.println(convertOQstr[s] + " " + item.get(a).getProductName());
					}
				}
			}
			double getTotalPrice = orders.get(i).getPrice();
			System.out.printf("Total Price of Order ID: " + orders.get(i).getOrderID() + ": $%.2f \n", getTotalPrice);
		}
		System.out.println("___________________________________________________________");
	}
	/************************************************END EMPLOYEE MENU METHOD*******************************************/			

	/******************************************************START VIEW BAKERY****************************************************/
	void viewBakery() throws Throwable,Error {
		System.out.println("\n\n");
		System.out.println("-------------------------------------------");
		System.out.println("              Bakery Products              ");
		System.out.println("-------------------------------------------");	
		String input = "";
		boolean inputValid = false;
		boolean Customer = true;
		for (Integer i = 0; i < item.size(); i++) {
			System.out.println("_______________________________________");
			System.out.println("Product ID: " + item.get(i).getProductID());
			System.out.println("Product Name: " + item.get(i).getProductName());
			System.out.println("Product Description: " + item.get(i).getDescription());
			System.out.println("Product Price: " + item.get(i).getPrice() + "$");
			System.out.println("_______________________________________");
		}
		for (Integer k = 0; k < account.size(); k++) {
			if (loggedinUserID.equals(account.get(k).getUserID())) { //Checks if viewBakery is called on Employee or Customer - Customer gets asked to place order
				if (account.get(k).isEmployee()) {
					Customer = false;
				}
			}
		}
		if (Customer) {
			while (!inputValid) {
				System.out.println("\nDo you want to place an order?(yes/no)");
				input = Keyboard.next();
				if (input.equals("yes")) {
					placeOrder();
					inputValid = true;
				} else if (input.equals("no")) {
					showCustomerMenu();
					inputValid = true;
				} else {
					System.out.println("Invalid Input, Try Again!");
				}
			}
		}
	}
	/***************************************************END VIEW BAKERY*******************************************************/		


	/************************************************START PLACE ORDER********************************************************/

	@SuppressWarnings("null")
	void placeOrder() throws SQLException 
, ParseException
	{
		System.out.println("\n\n");
		System.out.println("-------------------------------------------");
		System.out.println("              PLACING ORDER.....           ");
		System.out.println("-------------------------------------------");	
		
		String chosenPID = ""; //chosen ProductID
		String validPID = ""; //valid ProductID
		String userItemQuantity = ""; //Quantity of Product
		int validUIQ = 0;
		Vector<String> userItems = new Vector<String>(); //vector to store items
		Vector<Integer> userItemQuantities= new Vector<Integer>(); //vector to store quantities
		boolean finishedOrdering = false;
		boolean validProductID = false;
		boolean validProductQuantity = false;
		double totalPrice = 0.00;
		String addmoreitems = "";
		boolean isValidYesOrNo = false;
		while(!finishedOrdering) {
			for (Integer i = 0; i < item.size(); i++)//displays all products
	        {
	            System.out.println(item.get(i).getProductID()+ " | "+ item.get(i).getProductName() + " | " + item.get(i).getDescription() + " | " + item.get(i).getPrice());
	            
	        }
			while(!validProductID) {//input validation
				System.out.println("\nEnter Product ID of the product: ");
				chosenPID = Keyboard.next().toUpperCase();
				for(Integer x = 0; x < item.size(); x++) {
					if(chosenPID.equals(item.get(x).getProductID())) {//checks if product id exist
						validPID = item.get(x).getProductID();
						userItems.add(validPID);
						validProductID = true;
					}
				}
				if(!validProductID) { 
					System.out.println("Invalid Product ID, Try Again!");
				}
			}
			System.out.println("How many would you like?");
			while(!validProductQuantity) {//input validation
				System.out.print("Enter here: ");
				userItemQuantity = Keyboard.next();
				try 
				{ 
					Integer.parseInt(userItemQuantity); 
					validUIQ = Integer.parseInt(userItemQuantity);
					userItemQuantities.add(validUIQ);
					validProductQuantity = true;
				
				}  
				catch (NumberFormatException e)  
				{ 
					 
				} 
				if(!validProductQuantity) {
					System.out.println(userItemQuantity + " is a invalid Item Quantity, Try Again!");
				}
	
			}
			for(Integer y = 0; y < item.size(); y++) {//runs a loop through products
				if(validPID.equals(item.get(y).getProductID())) {//finds the position of the price of the product ID
					double PIDprice = item.get(y).getPrice();
					totalPrice = totalPrice + (PIDprice * validUIQ);//adds to the total price of the items [Calculation}
					BigDecimal bd = new BigDecimal(totalPrice).setScale(2, RoundingMode.HALF_UP);//converts it to be within 2 decimal places
					totalPrice = bd.doubleValue();
				}
			}
			while(!isValidYesOrNo) {//input validation
				System.out.println("\nDo you want to add more items?(yes/no)");
				addmoreitems = Keyboard.next();
				if(addmoreitems.equals("yes") || addmoreitems.equals("no")) {
					isValidYesOrNo = true;
				}else {
					System.out.println("Invalid Input, Try Again!");
				}
			}
			if(addmoreitems.toLowerCase().equals("no")){
				boolean uniqueOID = false;
				String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
				String newOID = "";
				Random rnd = new Random();
				String[] convertedPIDs = userItems.toArray(new String[userItems.size()]);//converts the user items from a vector to an String array and is the variable used to create an object
				Integer[] convertedUIQs = userItemQuantities.toArray(new Integer[userItemQuantities.size()]);// converts the item quantities from a vector to an array 
				String stringAPIDs = Arrays.toString(convertedPIDs).replaceAll("\\,", "+");//turns the user items array to a String and is the variable used for injecting to Access/MYSQL
				stringAPIDs = stringAPIDs.replaceAll("\\[", "");//removes left bracket
				stringAPIDs = stringAPIDs.replaceAll("\\]", "");//removes right bracket
				stringAPIDs = stringAPIDs.replaceAll("\\s+", "");//removes any spaces
				String stringAUIQs = Arrays.toString(convertedUIQs).replaceAll("\\,", "+");//turns the item quantities array to a String and is the variable used for injecting to Access/MYSQL
				stringAUIQs = stringAUIQs.replaceAll("\\[", "");//removes left bracket
				stringAUIQs = stringAUIQs.replaceAll("\\]", "");//removes right bracket
				stringAUIQs = stringAUIQs.replaceAll("\\s+", "");//removes any spaces
				String arUIQs[] = stringAUIQs.substring(0,stringAUIQs.length()).split("\\+");//turns the item quantities string to an String array and is the variable used to create an object
				
				System.out.println(Arrays.toString(arUIQs));
				while(!uniqueOID) {//keeps looping till created order id is unique
					StringBuilder chars = new StringBuilder();
					while(chars.length() < 8) {//length of random string
		                int index = (int) (rnd.nextFloat() * CHARS.length());
		                        chars.append(CHARS.charAt(index));
		            }
		             String CharStr = chars.toString();
		             newOID = CharStr;
		              
		              for(Integer z = 0; z < orders.size(); z++) {
		            	  if(newOID.equals(orders.get(z).getOrderID())) {
		            		  uniqueOID = false;
		            	  }else {
		            		  uniqueOID = true;
		            	  }
		              }
				}
				System.out.println("Your OrderID is: " + newOID);
				
				if(convertedUIQs.length > 1) {//displays an order of multiple items
					System.out.println("You have: ");
					for(Integer s = 0; s < convertedPIDs.length; s++) {
						for(Integer a = 0; a < item.size();a++) {
							if(convertedPIDs[s].equals(item.get(a).getProductID())) {
								System.out.println(convertedUIQs[s].toString() + " " + item.get(a).getProductName());
							}
						}
					}
					
					
				}else {//displays an order of 1 item
					for(Integer a = 0; a < item.size();a++) {
						if(convertedPIDs[0].equals(item.get(a).getProductID())) {
							System.out.println("You have: ");
							System.out.println(convertedUIQs[0]+" " + item.get(a).getProductName());
						}
					}
				}
				
				System.out.printf("\nOrder Total: $%.2f",totalPrice);
				System.out.println("\nSent Email Invoice, requesting payment!");
				//Start code for adding to orders vector
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

				LocalDate date = LocalDate.now(); //Captures Local Date
				String cDate = date.format(formatter);
				String defaultStat = "Waiting for Payment";
				String dDate = "null";
				Orders newOrder = new Orders(loggedinUserID, newOID, defaultStat, cDate, "null", convertedPIDs, arUIQs, totalPrice);//creates a new order object
				orders.add(newOrder);//adds the order to the vector
				//End code for adding to orders vector
				
				//Start code for adding to Access
				DateFormat format = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
				java.util.Date util_StartDate = format.parse( cDate );//prepares java date holder
				java.sql.Date sql_StartDate = new java.sql.Date( util_StartDate.getTime()  ); // converts it to sql date holder
				String sql = "INSERT INTO Orders VALUES (?, ?, ?, ?, ?, ?, ?, ?)";//prepare sql injection
				secureStatement = connection.prepareStatement(sql);
				secureStatement.setString(1, loggedinUserID);
				secureStatement.setString(2, newOID);
				secureStatement.setString(3, defaultStat);
				secureStatement.setDate(4, sql_StartDate);
				secureStatement.setDate(5, null);
				secureStatement.setString(6, stringAPIDs);
				secureStatement.setString(7, stringAUIQs);
				secureStatement.setDouble(8, totalPrice);
				secureStatement.executeUpdate();//adds the order to orders table
				//End code for adding to Access
				finishedOrdering = true;
			}
			
			if(addmoreitems.toLowerCase().equals("yes")) {//resets input validation checkers
				validProductID = false;
				validProductQuantity = false;
				isValidYesOrNo = false;
			}
			
		}
		
	}

	/************************************************END OF PLACE ORDER*******************************************************/	

	/********************************************START CHECK STATUS METHOD ***********************************************/
	//For Customer menu
	void checkStatus ()
	{
		System.out.println("\n\n");
		System.out.println("-------------------------------------------");
		System.out.println("         CHECKING ORDER'S STATUS...        ");
		System.out.println("-------------------------------------------");	
		boolean empty = true;
		for(Integer x = 0; x < orders.size(); x++) {
			if(loggedinUserID.equals(orders.get(x).getUserID()) && orders.get(x).getOrderStatus().equals("Cancelled") && orders.get(x).getOrderStatus().equals("Fulfilled")) {
				empty = false;
				System.out.println("___________________________________________________________");	
				System.out.println("Status for OrderID "+ orders.get(x).getOrderID() + ": " + orders.get(x).getOrderStatus());
				System.out.println("Listing order items...");
				String convertOIarr = Arrays.toString(orders.get(x).getOrderItems());
				convertOIarr = convertOIarr.replaceAll("\\[", "");//removes left bracket
				convertOIarr = convertOIarr.replaceAll("\\]", "");//removes right bracket
				convertOIarr = convertOIarr.replaceAll("\\s+", "");//removes any spaces
				String convertOQarr = Arrays.toString(orders.get(x).getOrderQuantity());
				convertOQarr = convertOQarr.replaceAll("\\[", "");//removes left bracket
				convertOQarr = convertOQarr.replaceAll("\\]", "");//removes right bracket
				convertOQarr = convertOQarr.replaceAll("\\s+", "");//removes any spaces
				String convertOIstr[] = convertOIarr.substring(0,convertOIarr.length()).split("\\+");//turns the string back to an array with delimiter of "+"
				String convertOQstr[] = convertOQarr.substring(0,convertOQarr.length()).split("\\+");//turns the string back to an array with delimiter of "+"
					System.out.println("You have: ");
					for(Integer s = 0; s < convertOIstr.length; s++) {
						for(Integer a = 0; a < item.size();a++) {
							if(convertOIstr[s].equals(item.get(a).getProductID())) {
								System.out.println(convertOQstr[s] + " " + item.get(a).getProductName());
							}
						}
					}
				double getTotalPrice = orders.get(x).getPrice();
				System.out.printf("Total Price of Order ID: " + orders.get(x).getOrderID() + ": $%.2f \n", getTotalPrice);
				System.out.println("___________________________________________________________");	
			}
		}
		if(empty) { //Checks if any orders are returned based on if conditional
			System.out.println("No orders found.......");
		}
		
		
	}

	/**************************************************END CHECK STATUS METHOD********************************************/
	/***************************************************Cancel Order  METHOD  *********************************************/
	//For Customer Menu
	void cancelOrder() throws Throwable {
		System.out.println("\n\n");
		System.out.println("-------------------------------------------");
		System.out.println("            Cancelling Order...            ");
		System.out.println("-------------------------------------------");	
		for(Integer x = 0; x < orders.size(); x++) {
			if(loggedinUserID.equals(orders.get(x).getUserID()) && !orders.get(x).getOrderStatus().equals("Cancelled") && !orders.get(x).getOrderStatus().equals("Fulfilled")) {
				System.out.println("___________________________________________________________");	
				System.out.println("Status for OrderID "+ orders.get(x).getOrderID() + ": " + orders.get(x).getOrderStatus());
				System.out.println("Listing order items...");
				String convertOIarr = Arrays.toString(orders.get(x).getOrderItems());
				convertOIarr = convertOIarr.replaceAll("\\[", "");//removes left bracket
				convertOIarr = convertOIarr.replaceAll("\\]", "");//removes right bracket
				convertOIarr = convertOIarr.replaceAll("\\s+", "");//removes any spaces
				String convertOQarr = Arrays.toString(orders.get(x).getOrderQuantity());
				convertOQarr = convertOQarr.replaceAll("\\[", "");//removes left bracket
				convertOQarr = convertOQarr.replaceAll("\\]", "");//removes right bracket
				convertOQarr = convertOQarr.replaceAll("\\s+", "");//removes any spaces
				String convertOIstr[] = convertOIarr.substring(0,convertOIarr.length()).split("\\+");//turns the string back to an array with delimiter of "+"
				String convertOQstr[] = convertOQarr.substring(0,convertOQarr.length()).split("\\+");//turns the string back to an array with delimiter of "+"
					System.out.println("You have: ");
					for(Integer s = 0; s < convertOIstr.length; s++) {
						for(Integer a = 0; a < item.size();a++) {
							if(convertOIstr[s].equals(item.get(a).getProductID())) {
								System.out.println(convertOQstr[s] + " " + item.get(a).getProductName());
							}
						}
					}
				double getTotalPrice = orders.get(x).getPrice();
				System.out.printf("Total Price of Order ID: " + orders.get(x).getOrderID() + ": $%.2f \n", getTotalPrice);
				System.out.println("___________________________________________________________");	
			}
		}
		boolean doAnother = true;
		String custInput = "";
		while(doAnother) {
			System.out.println("Enter OrderID: ");
			custInput = Keyboard.next();
			boolean validOrderID = false;
			while(!validOrderID) {
				for(Integer x = 0; x < orders.size(); x++) {
					if(custInput.equals(orders.get(x).getOrderID()) && orders.get(x).getUserID().equals(loggedinUserID)) {
						validOrderID = true;
					}
				}
				if(!validOrderID) {
					System.out.println("Invalid OrderID, Try Again!");
					System.out.println("Enter OrderID: ");
					custInput = Keyboard.next();
				}
			}
			
			for(Integer x = 0; x < orders.size();x++) {
				if(custInput.equals(orders.get(x).getOrderID())) {
					String orderSelected = orders.get(x).getOrderID();
					String sql = "update Orders set OrderStatus = ? where OrderID = ?";
					orders.get(x).setOrderStatus("Cancelled");
					secureStatement = connection.prepareStatement(sql);
					secureStatement.setString(1, "Cancelled");
					secureStatement.setString(2, orderSelected);
					secureStatement.executeUpdate();
					System.out.println("Order " + orderSelected + " has been Cancelled!");
					
				}
			}
			System.out.println("Do you want to cancel another order?(yes/no)");
			custInput = Keyboard.next().toLowerCase();
			boolean validYesOrNo = false;
			while (!validYesOrNo) {

				if (custInput.equals("yes") || custInput.equals("no")) {
					validYesOrNo = true;
				} else {
					System.out.println("Incorrect Input, Try Again!");
					System.out.println("Do you want to change an order status?(yes/no)");
					custInput = Keyboard.next().toLowerCase();
				}
			}
			if(custInput.equals("no")) {
				doAnother = false;
				showCustomerMenu();
			}
		}
		
		
	}
	/**************************************************END Cancel Order Method ********************************************/
	/***************************************************Change Status METHOD  *********************************************/

	//For Employee menu
	void changeStatus () throws Throwable
	{
		System.out.println("\n\n");
		System.out.println("-------------------------------------------");
		System.out.println("            Update Order Status            ");
		System.out.println("-------------------------------------------");	
		viewOrders();
		boolean validYesOrNo = false;
		String empInput = "";
		System.out.println("Do you want to change an order status?(yes/no)");
		empInput = Keyboard.next().toLowerCase();
		while (!validYesOrNo) {

			if (empInput.equals("yes") || empInput.equals("no")) {
				validYesOrNo = true;
			} else {
				System.out.println("Incorrect Input, Try Again!");
				System.out.println("Do you want to change an order status?(yes/no)");
				empInput = Keyboard.next().toLowerCase();
			}
		}

		boolean validOrderID = false;
		String selectedOrderID = "";
		String sql = "update Orders set OrderStatus = ? where OrderID = ?";
		boolean changeAnother = true;
		if (empInput.equals("yes")) {
			while (changeAnother) {
				System.out.println("Enter Order ID: ");
				empInput = Keyboard.next().toUpperCase();
				while (!validOrderID) {
					for (Integer x = 0; x < orders.size(); x++) {
						if (empInput.equals(orders.get(x).getOrderID())) {
							selectedOrderID = empInput;
							validOrderID = true;
						}
					}
					if (!validOrderID) {
						System.out.println("Order does not exist, Try Again!");
						System.out.print("Enter Order ID: ");
						empInput = Keyboard.next().toUpperCase();
					}
				}

				System.out.println("Enter New Status");
				System.out.println("1: Waiting for Payment");
				System.out.println("2: In-Progress");
				System.out.println("3: Ready for Pick-Up");
				System.out.println("4: Delayed");
				System.out.println("5: Fulfilled");
				System.out.println("6: Cancelled");
				System.out.println("Choose new Status: 1-5");
				empInput = Keyboard.next();
				switch (empInput) {
				case "1":
					for (Integer x = 0; x < orders.size(); x++) {// sets vector of order status by orderID position
						if (empInput.equals(orders.get(x).getOrderID())) {
							orders.get(x).setOrderStatus("Waiting for Payment");
						}
					}

					secureStatement = connection.prepareStatement(sql);
					secureStatement.setString(1, "Waiting for Payment");
					secureStatement.setString(2, selectedOrderID);
					secureStatement.executeUpdate();
					for (Integer x = 0; x < orders.size(); x++) {// sets vector of order status by orderID position
						if (empInput.equals(orders.get(x).getOrderID())) {
							System.out.print("OrderID: " + orders.get(x).getOrderID() + " | Status Changed To: " + "Waiting for Payment" );
						}
					}
					break;
				case "2":
					for (Integer x = 0; x < orders.size(); x++) {// sets vector of order status by orderID position
						if (empInput.equals(orders.get(x).getOrderID())) {
							orders.get(x).setOrderStatus("In-Progress");
						}
					}

					secureStatement = connection.prepareStatement(sql);
					secureStatement.setString(1, "In-Progress");
					secureStatement.setString(2, selectedOrderID);
					secureStatement.executeUpdate();
					for (Integer x = 0; x < orders.size(); x++) {// sets vector of order status by orderID position
						if (empInput.equals(orders.get(x).getOrderID())) {
							System.out.print("OrderID: " + orders.get(x).getOrderID() + " | Status Changed To: " + "In-Progress" );
						}
					}
					break;
				case "3":
					for (Integer x = 0; x < orders.size(); x++) {// sets vector of order status by orderID position
						if (empInput.equals(orders.get(x).getOrderID())) {
							orders.get(x).setOrderStatus("Ready for Pick-Up");
						}
					}

					secureStatement = connection.prepareStatement(sql);
					secureStatement.setString(1, "Ready for Pick-Up");
					secureStatement.setString(2, selectedOrderID);
					secureStatement.executeUpdate();
					for (Integer x = 0; x < orders.size(); x++) {// sets vector of order status by orderID position
						if (empInput.equals(orders.get(x).getOrderID())) {
							System.out.print("OrderID: " + orders.get(x).getOrderID() + " | Status Changed To: " + "Ready for Pick-Up" );
						}
					}
					break;
				case "4":
					for (Integer x = 0; x < orders.size(); x++) {// sets vector of order status by orderID position
						if (empInput.equals(orders.get(x).getOrderID())) {
							orders.get(x).setOrderStatus("Delayed");
						}
					}

					secureStatement = connection.prepareStatement(sql);
					secureStatement.setString(1, "Delayed");
					secureStatement.setString(2, selectedOrderID);
					secureStatement.executeUpdate();
					for (Integer x = 0; x < orders.size(); x++) {// sets vector of order status by orderID position
						if (empInput.equals(orders.get(x).getOrderID())) {
							System.out.print("OrderID: " + orders.get(x).getOrderID() + " | Status Changed To: " + "Delayed" );
						}
					}
					break;
				case "5":
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
					LocalDate date = LocalDate.now();
					String cDate = date.format(formatter);
					for (Integer x = 0; x < orders.size(); x++) {// sets vector of order status by orderID position
						if (empInput.equals(orders.get(x).getOrderID())) {
							orders.get(x).setOrderStatus("Fulfilled");
						}
					}
					secureStatement = connection.prepareStatement(sql);
					secureStatement.setString(1, "Fulfilled");
					secureStatement.setString(2, selectedOrderID);
					secureStatement.executeUpdate();
					String sqlCompletedDate = "update Orders set CompletedDate = ? where OrderID = ?";
					secureStatement = connection.prepareStatement(sqlCompletedDate);
					DateFormat format = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
					java.util.Date util_CompletedDate = format.parse( cDate );//prepares java date holder
					java.sql.Date sql_CompletedDate = new java.sql.Date( util_CompletedDate.getTime()  ); // converts it to sql date holder
					
					
					secureStatement.setDate(1, sql_CompletedDate);
					secureStatement.setString(2, selectedOrderID);
					secureStatement.executeUpdate();
					for (Integer x = 0; x < orders.size(); x++) {// sets vector of order status by orderID position
						if (empInput.equals(orders.get(x).getOrderID())) {
							System.out.print("OrderID: " + orders.get(x).getOrderID() + " | Status Changed To: " + "Fulfilled" );
						}
					}
					break;
				case "6":
					for (Integer x = 0; x < orders.size(); x++) {// sets vector of order status by orderID position
						if (empInput.equals(orders.get(x).getOrderID())) {
							orders.get(x).setOrderStatus("Cancelled");
						}
					}
					secureStatement = connection.prepareStatement(sql);
					secureStatement.setString(1, "Cancelled");
					secureStatement.setString(2, selectedOrderID);
					secureStatement.executeUpdate();
					for (Integer x = 0; x < orders.size(); x++) {// sets vector of order status by orderID position
						if (empInput.equals(orders.get(x).getOrderID())) {
							System.out.print("OrderID: " + orders.get(x).getOrderID() + " | Status Changed To: " + "Cancelled" );
						}
					}
					break;
				default:
					System.out.println("Invalid input! Choose between 1-5");

					break;
				}
				System.out.println("Do you want to change another order's status?(yes/no)");
				empInput = Keyboard.next();
				validYesOrNo = false;
				while (!validYesOrNo) {

					if (empInput.equals("yes") || empInput.equals("no")) {
						validYesOrNo = true;
					} else {
						System.out.println("Incorrect Input, Try Again!");
						System.out.println("Do you want to change another order's status?(yes/no)");
						empInput = Keyboard.next().toLowerCase();
					}
				}
				if(empInput.equals("no")) {
					changeAnother = false;
					showEmployeeMenu();
				}
				validOrderID = false;
			}
		}
	}
	
	/***************************************************END Change Status Method **************************************************/							

	/****************************************START CREATE ACCOUNT METHOD
 *******************************************************/


    void createAccount() throws SQLException {
        //DISPLAY MENU HEADER
        System.out.println("\n\n");
        System.out.println("-------------------------------------------");
        System.out.println("            CREATE NEW ACCOUNT             ");
        System.out.println("-------------------------------------------");  
		boolean idValid = false;
		boolean usernameValid = false;
		boolean phoneValid = false;
		boolean emailValid = false;
		boolean employee = false;
		String userID = "";
		String cUsername = "";
		String cPassword = "";
		String cPhone = "";
		String uEmail = "";
		String fName = "";
		String lName = "";
		String fullName = "";
		String employeeCode = "A2XVM232";
		String inputEmpCode = "";
		String regex = "^(.+)@(.+)$";
		String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
				+ "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(regexPattern);
		
		while (!idValid) {// loops till it is a unique user ID
			String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
			StringBuilder id = new StringBuilder();
			Random rnd = new Random();

			while (id.length() < 3) {// length of random string
				int index = (int) (rnd.nextFloat() * CHARS.length());
				id.append(CHARS.charAt(index));
			}
			String CharStr = id.toString();

			String ALPHA = "1234567890";
			StringBuilder id2 = new StringBuilder();
			Random rnd1 = new Random();

			while (id2.length() < 1) {// length of random string
				int index = (int) (rnd.nextFloat() * ALPHA.length());
				id2.append(ALPHA.charAt(index));
			}
			String AlphaStr = id2.toString();

			userID = CharStr + AlphaStr;
			for (Integer x = 0; x < account.size(); x++) {
				if (!userID.equals(account.get(x).getUserID())) {
					idValid = true;
				}
			}
		}
		
		while(!usernameValid) {//loops till it is a unique username
			 System.out.println("Enter New Username: ");
			 cUsername = Keyboard.next();
	         for (int i = 0; i < account.size(); i++)
	            {
	               if(cUsername.equals(account.get(i).getUsername())) 
	               {
	                   System.out.println("Invalid UserName Entry - UserName Already Exists");
	                   System.out.println("Enter New Username: ");
	      			   cUsername = Keyboard.next();
	               }
	               else
	               {
	        
	                   usernameValid = true;
	               }
	            }
	        }   
		
		System.out.println("Enter New Password: ");//creates user password
        cPassword = Keyboard.next();
        
        
        boolean phFormat = false;
		while (!phoneValid) {// loops till unique phone number
			System.out.println("Enter Phone Number(xxx-xxx-xxxx): ");
			cPhone = Keyboard.next();
			while (!phFormat) {//loops till phone number is correct format
				if (cPhone.length() == 12) {
					phFormat = true;
				}else {
					System.out.println("Invalid Phone Number Format");
					System.out.println("Enter Phone Number(xxx-xxx-xxxx): ");
					cPhone = Keyboard.next();
				}
			}
			for (int i = 0; i < account.size(); i++) {
				if (cPhone.equals(account.get(i).getPhone())) {
					System.out.println("Invalid Phone Number Entry - Phone Number Already Exists");
					System.out.println("Enter Phone Number(xxx-xxx-xxxx): ");
					cPhone = Keyboard.next();
				}

				else {
					phoneValid = true;
				}
			}
		}
		
		
		boolean emailExist = false;
		while (!emailValid) {//loops till email is unique in database
			System.out.println("Enter Email: ");
			uEmail = Keyboard.next();
			
			while(!emailExist) {//loops till email exists in a correct format
				Matcher matcher = pattern.matcher(uEmail);
				if(matcher.matches()) {
					emailExist = true;
				}else {
					System.out.println("Email does not exist! Try Again!");
					System.out.println("Enter Email: ");
					uEmail = Keyboard.next();
				}
			}
			for (int i = 0; i < account.size(); i++) {
				

				if (uEmail.equals(account.get(i).getEmail())) {
					System.out.println("Invalid Email Entry - Email Already Exists");

				} else {
			
					emailValid = true;
				}
			}
		}
		
		System.out.println("Enter First Name: ");
        fName= Keyboard.next();
        System.out.println("Enter Last Name: ");
        lName= Keyboard.next();
		
        fullName = fName + " " + lName;
        
        
        boolean validYesOrNo = false;
        
        System.out.println("Is the account an employee?(yes/no)");
        inputEmpCode = Keyboard.next();
        while(!validYesOrNo) {
        	if(inputEmpCode.equals("yes") || inputEmpCode.equals("no") ) {
        		validYesOrNo = true;
        	}else {
        		System.out.println("Please type yes or no, Try Again!");
        		System.out.println("Is the account an employee?(yes/no)");
                inputEmpCode = Keyboard.next();
        	}
        }
		
        int counter = 1;
        //A2XVM232 
        if(inputEmpCode.equals("yes")) {//making account to be employee type
        	System.out.println("Enter Security Code: ");
    		inputEmpCode = Keyboard.next().toUpperCase();
        	while(counter < 3 || employee) {//loops till 3 times or correct code
        		if(inputEmpCode.equals(employeeCode)) {
        			employee = true;
        		}
        		else{
        			System.out.println("Incorrect Security Code, Try Again! ");
        			System.out.println("Enter Security Code: ");
            		inputEmpCode = Keyboard.next().toUpperCase();
            		counter++;
            		if(counter == 3 && !inputEmpCode.equals(employeeCode)) {
                    	System.out.println("Max attempts exceeded! Shutting Down System!");
                    	exitBOSS();
                    }
        		}
        	}
        }
        System.out.println("Account Created Successfully!");
        
        
        Users nUser = new Users (userID,cUsername, cPassword, cPhone, uEmail,fullName,employee); // creates a new user object
        account.add(nUser);//adds the created user to the vector
        
        String sql = "INSERT INTO Users VALUES (?, ?, ?, ?, ?, ?, ?)";//prepare sql injection
		secureStatement = connection.prepareStatement(sql);
		secureStatement.setString(1, userID);
		secureStatement.setString(2, cUsername);
		secureStatement.setString(3, cPassword);
		secureStatement.setString(4, cPhone);
		secureStatement.setString(5, uEmail);
		secureStatement.setString(6, fullName);
		secureStatement.setBoolean(7, employee);
		secureStatement.executeUpdate();//executes the sql injection
        
        numUupdates++;  //update the User flag  

    }   

					
	/*************************************END CREATE ACCOUNT METHOD******************************************************/							 	
	
	
	/*************************************CONTACT CUSTOMER METHOD******************************************************/							 	

    void contactCustomer() throws SQLException {
	System.out.println("Showing all Customers: ");
	System.out.println("____________________________________________");
	for(Integer b = 0; b < account.size(); b++) {
		if(!account.get(b).isEmployee()) {
			System.out.println("Customer Name: " + account.get(b).getName());
			System.out.println("Customer Phone Number: " + account.get(b).getPhone());
			System.out.println("Customer Email: " + account.get(b).getEmail());
			System.out.println("____________________________________________");
		}
	}
    }
	
	
    
	/*************************************END CONTACT CUSTOMER******************************************************/	
	
	/******************************EXIT BOSS SYSTEM**************************************************************************/
	void exitBOSS() throws SQLException
	{	System.out.println("\n\n--------------------------------------------");
	System.out.println("Thank you for using BOSS System, Program Ended!");
	System.out.println("-----------------------------------------------");
	System.exit(0);
	connection.close();
	}
	/***********************************************END EXIT BOSS SYSTEM****************************************************/
    
}