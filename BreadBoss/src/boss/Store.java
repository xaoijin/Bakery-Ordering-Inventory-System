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

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class Store {
	Vector <Products> item; // Vector to Hold Products
	Vector <Users> account; //Vector to Hold All Users
	Vector <Orders> orders; //Vector to Hold Order Information
	int numUupdates;
	int loggedinuser = -1;
	Scanner Keyboard;
	String loggedinUserID = "";
	int test = 0;
	public static final String ACCOUNT_SID = "AC4180b85d756a05941ed58e3bb5320073";
	public static final String AUTH_TOKEN = "adbeb581decd50be761d8037395f3f62";

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

	/*******************************START LOAD PRODUCTS********************************/

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
					loggedinUserID = account.get(i).getUserID();
					loggedinuser = i;
				}
			}
			if(!isValid) {
				System.out.println("Invalid Username or Password!");
			}
		} while(counter < 3 && !isValid);

		if(!isValid) {
			System.out.println("Max Attempts Exceeded, Shutting Down System");
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
		boolean validOption = false;
		do {
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
				exitBOSSSystem();
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
			System.out.println("5. Exit");
			System.out.print("Choose a system process (1-5): ");
			option = Keyboard.nextInt();
			switch (option)
			{
			case (1):
			{	 viewBakery();
			break;
			}
			case (2):
			{	checkStatus();
			break;
			}
			case (3):
			{	 cancelOrder();
			break;
			}
			case (4):
			{	viewHistory();//shows orders fulfilled
			break;
			}
			case (5):
			{	
				exitBOSSSystem();
				System.out.println("\n\n");
				System.out.println("****************************************************************************");	
				System.out.println("Thank you for using BOSS - Program Terminated!");
				System.out.println("****************************************************************************");	
				System.exit(0);
			}
			default:
			{	System.out.println("Invalid choice, please choose between 1-5"); }

			} //end of switch
		}while (option != 5);


	}


	/************************************************END CUSTOMER MENU METHOD***************************************************/			

	private void viewHistory() {
		// TODO Auto-generated method stub
		
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
			{	 viewOrders();
			break;
			}
			case (3):
			{	 contactCustomer();
				break;
			}
			case (4):
			{	
				exitBOSSSystem();

			System.out.println("Thank you for using BOSS System, - Program Terminated!");
			System.out.println("****************************************************************************\n\n");	
			System.exit(0);
			}
			default:
			{	System.out.println("Invalid choice, please choose between 1-6"); }

			} //end of switch
		}while (option != 4);


	}

	private void viewOrders() {
		// TODO Auto-generated method stub
		
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
		
		for (Integer i = 0; i < item.size(); i++)
        {
            System.out.println(item.get(i).getProductID()+ " | "+ item.get(i).getProductName() + " | " + item.get(i).getDescription() + " | " + item.get(i).getPrice()+ "$");
        }
		
		while(!inputValid) {
			System.out.println("\nDo you want to place an order?(yes/no)");
			input = Keyboard.next();
			if(input.equals("yes")) {
				placeOrder();
				inputValid = true;
			}else if(input.equals("no")){
				showCustomerMenu();
				inputValid = true;
			}else {
				System.out.println("Invalid Input, Try Again!");
			}
		}
	}
	/***************************************************END VIEW BAKERY*******************************************************/		


	/************************************************START PLACE ORDER********************************************************/

	@SuppressWarnings("null")
	void placeOrder() throws SQLException //Done by Johnson Li
, ParseException
	{
		System.out.println("\n\n");
		System.out.println("-------------------------------------------");
		System.out.println("              PLACING ORDER.....           ");
		System.out.println("-------------------------------------------");	
		
		String chosenPID = "";
		String validPID = "";
		String userItemQuantity = "";
		int validUIQ = 0;
		Vector<String> userItems = new Vector<String>();
		Vector<Integer> userItemQuantities= new Vector<Integer>();
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
					totalPrice = totalPrice + (PIDprice * validUIQ);//adds to the total price of the items
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
				
				System.out.printf("\nOrder Total: $%.2fct",totalPrice);
				System.out.println("\nSent Email Invoice, requesting payment!");
				//Start code for adding to orders vector
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

				LocalDate date = LocalDate.now();
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
		System.out.println("           CHECKING ORDER STATUS           ");
		System.out.println("-------------------------------------------");	
		for(Integer x = 0; x < orders.size(); x++) {
			if(loggedinUserID.equals(orders.get(x).getUserID())) {
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
				System.out.printf("Total Price of Order ID" + orders.get(x).getOrderID() + ": $%.2fct \n", getTotalPrice);
				
			}
		}
		
		
	}

	/**************************************************END CHECK STATUS METHOD********************************************/
	/***************************************************Cancel Order  METHOD *********************************************/
	//For Customer Menu
	void cancelOrder() {
		
	}
	/**************************************************END Cancel Order Method ********************************************/
	/***************************************************Change Status METHOD *********************************************/

	//For Employee menu
	void changeStatus ()
	{
		String oid = ""; //OrderID
		String uid = ""; //UserID//
		String newStatus = ""; //UserInput for Status Change

		boolean isValid = false;

		System.out.println("\n\n");
		System.out.println("-------------------------------------------");
		System.out.println("            Update Order Status            ");
		System.out.println("-------------------------------------------");	

		System.out.println("Enter OrderID To Be Updated: ");
		uid = Keyboard.next();

		for(int i = 0; i < orders.size(); i++) {

			if( oid.equals(orders.get(i).getOrderID())) {
				isValid = true;

				System.out.println("Enter New Status To Be Updated: ");

				newStatus = Keyboard.next();

				System.out.println("New Order Status for Order#: " + oid + " is: " + orders.get(2) + " " +getSystemDate());
			}
		}
	}			
	/***************************************************END Change Status Method **************************************************/							

	/****************************************START CREATE ACCOUNT METHOD*******************************************************/


    void createAccount() {
        //DISPLAY MENU HEADER
        System.out.println("\n\n");
        System.out.println("-------------------------------------------");
        System.out.println("            CREATE NEW ACCOUNT             ");
        System.out.println("-------------------------------------------");  

        Scanner input = new Scanner(System.in);
        

             String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
             StringBuilder id = new StringBuilder();
             Random rnd = new Random();
             
            while(id.length() < 3) {//length of random string
                int index = (int) (rnd.nextFloat() * CHARS.length());
                        id.append(CHARS.charAt(index));
            }
             String CharStr = id.toString();
             
             
             String ALPHA = "1234567890";
             StringBuilder id2 = new StringBuilder();
             Random rnd1 = new Random();
             
            while(id2.length() < 1) {//length of random string
                int index = (int) (rnd.nextFloat() * CHARS.length());
                        id2.append(CHARS.charAt(index));
            }
             String AlphaStr = id2.toString();
        
             String userID = CharStr + AlphaStr;        
        
        /*****************************************************************/
    
        System.out.print("Enter New UserName: ");
        String username = input.nextLine();
        Boolean usernameValid = false;
        
        while(!usernameValid) {
         for (int i = 0; i < account.size(); i++)
            {
               if(account.get(i).getUsername() == username) 
               {
                   System.out.print("Invalid UserName Entry - UserName Already Exists");
                  
               }
               else
               {
                   username = input.next();
                   usernameValid = true;
               }
            }
        }   
/**********************************************************************************/
        System.out.print("Enter New Password ");
        String password = input.nextLine();

        System.out.print("Enter Phone: ");
        String phone = input.nextLine();
        Boolean phoneValid = false;
        
        while(!phoneValid) {
             for (int i = 0; i < account.size(); i++)
                {
                   if(account.get(i).getPhone() ==phone) 
                   {
                       System.out.print("Invalid Phone Number Entry - Phone Number Already Exists");
                      
                   }
                   else
                   {
                       phone = input.next();
                       phoneValid = true;
                   }
                }
        }
/*****************************************************************************************/     
        System.out.print("Enter Email: ");
        String email = input.nextLine();
        Boolean emailValid = false;
        
        while(!emailValid) {
             for (int i = 0; i < account.size(); i++)
                {
                   if(account.get(i).getEmail() ==email) 
                   {
                       System.out.print("Invalid Email Entry - Email Already Exists");
                      
                   }
                   else
                   {
                       email = input.next();
                      emailValid = true;
                   }
                }
        }
        
/******************************************************************************************/        
        System.out.print("Enter Name: ");
        String name= input.nextLine();

        //Employee Pin A2XVM232:
        
        System.out.print("Is User an Employee(Enter Employee Access Code Otherwise Enter Any Four Digits): ");
        String code = input.nextLine();
        Boolean employee = false;
    
        while(!employee) {
            if(code == "A2XVM232") { 
                 employee = true;
                
            }
            else {
                employee = false;
                
            }
        }
        
/*****************************************************************************************************************/     
        Users nUser = new Users (userID,username, password, phone, email,name,
                employee);


        account.add(nUser);


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
			System.out.println("Customer Email: " + account.get(b).getEmail());
			System.out.println("____________________________________________");
		}
	}
	
	Twilio.init(ACCOUNT_SID,AUTH_TOKEN);
	Message.creator(
		    new PhoneNumber("+15167846338"),
		    new PhoneNumber("+18057495675"),
		    "Watch YOuafoubaosfb")
		  .create();
	}
    
	/*************************************END CONTACT CUSTOMER******************************************************/	
	
	 

	/************************************************GET TIME STAMP**********************************************************/
	String getSystemDate()
	{ 	String timestamp = ""; //Create a string to hold the date
	String pattern = "MM-dd-yyyy"; //Determine the pattern for the date and time fields
	SimpleDateFormat formatter = new SimpleDateFormat(pattern); //Set your date and time pattern
	Date date = new Date(0); //Capture the system datetime in milliseconds
	timestamp = formatter.format(date); //Format the date based on the pattern
	return timestamp;
	}
	/***********************************************END GET TIME STAMP*******************************************************/
	/******************************EXIT BOSS SYSTEM**************************************************************************/
	void exitBOSSSystem() throws SQLException
	{	System.out.println("\n\n--------------------------------------------");
	System.out.println("Thank you for using BOSS System, Program Ended!");
	System.out.println("-----------------------------------------------");
	System.exit(0);
	connection.close();
	}
	/***********************************************END EXIT BOSS SYSTEM****************************************************/

}