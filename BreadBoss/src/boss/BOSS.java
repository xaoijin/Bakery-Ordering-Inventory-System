package boss;

<<<<<<< HEAD
import java.util.Iterator;
=======
import java.io.IOException;
import java.sql.SQLException;
>>>>>>> branch 'master' of https://github.com/TwigJL/BreadBOSS.git

//this is where we run the system itself
public class BOSS {

	public static void main(String[] args) throws Exception, SQLException {
		// TODO Auto-generated method stub
<<<<<<< HEAD
		
=======

		
		Store boss = new Store();
		
		try
		{
			boss.connectDB();
			boss.loadData();
			boss.welcome();
			boss.login();
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
			System.out.println("Input-Output Exception, Program Terminated!");
			System.exit(1000);
			
		}
>>>>>>> branch 'master' of https://github.com/TwigJL/BreadBOSS.git
	}

	

}
