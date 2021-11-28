package jukeBox;

import java.sql.*;
import java.util.Scanner;

public class User {
	static Scanner sc = new Scanner(System.in);

	private String userId; // user defined
	private String userName;
	private String password;

	//method to create or validate user
	public String userInterface(Statement smt) throws SQLException {
		System.out.println("|**************************************************|\n"
				+ "| Welcome to Juke Box - Entertainment in Your Hand |\n"
				+ "****************************************************");
		boolean done = false;
		while(!done) {
			try {
				System.out.println("\n\t1.Sign Up\t2.Log In\t3.Exit");
				int opt = sc.nextInt();
				if(opt<1 || opt>3) throw new InvalidChoiceException("Invalid Choice !");
				if(opt==3) return "exit";
				System.out.println("Enter User-Id");
				userId = sc.next();
				System.out.println("Enter Password");
				password = sc.next();
				done=option(userId,userName,password,smt,opt,done);	//calling to update details according to user
			}catch(InvalidChoiceException | AccountExistsException e) {
				System.out.println(e.getMessage());
				continue;
			}
		}
		ResultSet rs1 = smt.executeQuery("select username from user where userid = '"+userId+"' and password = '"+password+"'");
		while(rs1.next()) {
			userName = rs1.getString(1);
			System.out.println("\n\t    Welcome "+userName+"\n\t***************************");
		}
		return userId;
	}
	//method to validate and update user details
	public static boolean option(String userId, String userName, String password, Statement smt, int opt, boolean done) throws SQLException, AccountExistsException {
		switch (opt) {
		case 1: {
			ResultSet rs = smt.executeQuery("select * from user where userid = '"+userId+"'");
			if(rs.next()) throw new AccountExistsException("Account already exists with user-Id "+userId+" !");
			System.out.println("Enter your name");
			userName = new Scanner(System.in).nextLine();
			smt.executeUpdate("insert into user values ('"+userId+"','"+userName+"','"+password+"')");//inserting user details in database
			System.out.println("Account successfully created !\n*| User-Id : "+userId+" and Password : "+password+" |*");
			done = true;
			break;
		}
		case 2: {
			ResultSet rs = smt.executeQuery("select * from user where userid = '"+userId+"' and password = '"+password+"'");
			if(!rs.next()) {
				System.out.println("\tWrong userId or password !!");
				break;
			}
			System.out.println("Successfully Logged in !");
			done = true;
			break;
		}
		default:
			System.out.println("Invalid option selected");
			done=false;
		}
		return done;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + "]";
	}
}
