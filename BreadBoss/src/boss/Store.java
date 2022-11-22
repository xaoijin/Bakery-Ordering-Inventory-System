package boss;

import java.util.Scanner;
import java.util.Vector;

//this is where all the methods will be
public class Store {
	Vector <Products> item;
	Vector <Users> account;
	Vector <Orders> invoice;
	Scanner Keyboard;
	int test = 0;
	Store(){
		item = new Vector<Products>();
		account = new Vector<Users>();
		invoice = new Vector<Orders>();
		Keyboard = new Scanner (System.in);
		
	}
}
