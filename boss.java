package project;
import java.util.Scanner;
public class boss {

   String userid;
   String userName; 
   String email;
   String phone;
   String password;

   public boss() {
       this.userid= " ";
       this.userName = "";
       this.email = "";
       this.phone = "";
       this.password = "";
   }
  
   public boss (String userid, String userName,String name, String email, String phone, String password) {
       this.userid = userid;
       this.userName= userName;
       this.email = email;
       this.phone = phone;
       this.password = password;
      
   }
   public String getUserid(){
       return userid;
   }
   public void setUserid(String userid) {
       this.userid = userid;
    }
  
   public String getUserName() {
       return userName;
    }

   public void setUsername(String userName) {
       this.userName = userName;
   }
  
  
   public String getEmail() {
       return email;
   }

   public void setEmail(String email) {
       this.email = email;
   }
   public String getPassword() {
       return password;
   }

   public void setPassword(String password) {
       this.password = password;
   }

   public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    
    System.out.print("Enter Useer ID : ");
    String userid = sc.nextLine();
          
    System.out.print("Enter User Name : ");
    String userName = sc.nextLine();
             
    System.out.print("Enter User Password : ");
    String password = sc.nextLine();
  
    System.out.print("Enter User Email : ");
    String email = sc.nextLine();
          
    System.out.print("Enter User Phone : ");
    String phone = sc.nextLine();

    boss boss = new boss(userid, userName, userName, email, phone, password);

    login(boss, sc);

   }

   public static void login(boss boss,Scanner sc){
        int MAX_attempt = 3;
        int count = 0;
        System.out.println("\nKindly Enter your User Name and Password for login..");
        while (count<MAX_attempt) {
            System.out.print("\nEnter User Name : ");
            String userid = sc.nextLine();
            System.out.print("Enter Password : ");
            String password=sc.nextLine();

            if(boss.userName.equals(userid))
                {if(boss.password.equals(password)) {
                    System.out.println("\nWelcome user");
                    break;  
                }else {
                System.out.println("wrong Password try again");
                }
            }else{
                System.out.println("Wrong UserName...try agian");
            }
            count++;
            System.out.println("\nAttempt left : "+(MAX_attempt-count));
        }

    }
}
 
  


