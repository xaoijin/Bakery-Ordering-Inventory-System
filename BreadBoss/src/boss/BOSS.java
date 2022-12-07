package boss;


import java.util.Iterator;

import java.io.IOException;
import java.sql.SQLException;


//this is where we run the system itself
public class BOSS {

	public static void main(String[] args) throws Exception, SQLException {
		// TODO Auto-generated method stub

		


		
		Store boss = new Store();
		
		try
		{
			boss.connectDB();
			boss.loadData();
			boss.welcome();
			boss.login(); //@Johnson check algorithm 1.load data 2.login or 3. welcome screen first? //code in login and welcome might need to be adjusted
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
			System.out.println("Input-Output Exception, Program Terminated!");
			System.exit(1000);
			
		}
		
	
	
	}
}
