import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Staff {
	static Scanner scn = new Scanner(System.in);
	private static String database;
	private static String user;
	private static String password;
	private static Connection con;
	private static Statement stmt;
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	static Admin obj;

	Staff() {
		database = "bankmodule";
		user = "root";
		password = "";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database, user, password);
			stmt = con.createStatement();
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR :connector class not found");
		} catch (SQLException e) {
			System.out.println("ERROR :can't connect to database");
		}
	}

	static private boolean searchInTransactions(int id) {
		String query = "SELECT * FROM `transaction` WHERE acId = " + id;
		try {
			ResultSet result = stmt.executeQuery(query);
			int k = 0;
			while (result.next())
				k++;
			return k == 0 ? false : true;
		} catch (SQLException e) {
			System.out.println("invalid query or connection problem");
		}
		return false;
	}

	@SuppressWarnings("static-access")
	static void deposit() {
		obj = new Admin();
		System.out.println("enter account id:");
		int id = scn.nextInt();
		if (!obj.searchById(id)) {
			System.out.println("account doesnot exists !!!!");
			return;
		}
		System.out.println("enter balance to deposit(>0):");
		int amount = scn.nextInt();
		if (amount <= 0)
			return;
		String query = "SELECT * FROM `account` WHERE acId = " + id;

		ResultSet result;
		String trDate = "";
		int acID = 0;
		String trType = "";
		int trAmount = 0;
		Double trBalance = 0.0;

		String querySet = "INSERT INTO `transaction`(`acId`, `trDate`, `trType`, `typeAmount`, `trBalance`) VALUES ("
				+ acID + ",'" + trDate + "','" + trType + "'," + trAmount + "," + trBalance + ")";
		try {
			result = stmt.executeQuery(query);
			trDate = formatter.format(new Date());
			acID = id;
			trType = "deposit";
			trAmount = amount;
			trBalance = result.getDouble("acBalance") + trAmount;
//			stmt.executeUpdate(querySet);

		} catch (SQLException e) {
			System.out.println("query syntax error");
		}
	}

	public static void main(String[] args) {
		Staff obj1 = new Staff();
		int exit = 0;
		String staffPassword = "staff";
		System.out.println("Enter staff password:");
		String in = scn.nextLine();

		if (in.equals(staffPassword)) {
			while (exit != 1) {
				int choice = -1;

				System.out.println("1.deposit");
				System.out.println("2.withdraw");
				System.out.println("3.transaction");
				System.out.println("4.current balance");
				System.out.println("5.user info");
				System.out.println("6.exit application");
				System.out.println("enter your choice:");

				choice = scn.nextInt();
				switch (choice) {
				case 1:
					deposit();
					break;
				case 2:
					break;
				case 3: {
					int id;
					System.out.println("enter acId of account you want to modify details:");
					id = scn.nextInt();
				}
					break;
				case 4:
					break;
				case 5: {
					System.out.println("enter id of account that you want to delete :");
					int id = scn.nextInt();
				}
					break;
				case 6:
					exit = 1;
					break;
				default:
					System.out.println("wrong choice ..... try again");
					break;
				}
			}
		} else
			System.out.println("wrong password ... try again or contact software developer");
	}

}
