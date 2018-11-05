import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Client {
	static Scanner scn = new Scanner(System.in);
	private static String database;
	private static String user;
	private static String password;
	private static Connection con;
	private static Statement stmt;
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	static Admin obj;

	Client() {
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
		System.out.println("enter password:");
		int tempPassword = scn.nextInt();

		if (!obj.searchById(id)) {
			System.out.println("account doesnot exists !!!!");
			return;
		}
		int password = obj.getPassword(id);
		if (password != tempPassword) {
			System.out.println("Deposit failed due to wrong password .... pls contact bank");
			return;
		}

		System.out.println("enter balance to deposit(>0):");
		int amount = scn.nextInt();
		if (amount <= 0) {
			System.out.println("cannot add amount less than or equal to zero(0)");
			return;
		}
		String query = "SELECT * FROM `account` WHERE acId = " + id;

		ResultSet result;
		String trDate = "";
		int acID = 0;
		String trType = "";
		double trAmount = 0.0;
		Double trBalance = 0.0;

		String querySet;
		try {
			result = stmt.executeQuery(query);
			trDate = formatter.format(new Date());
			acID = id;
			trType = "deposit";
			trAmount = amount;
			result.next();
			trBalance = result.getDouble("acBalance") + trAmount;
			querySet = "INSERT INTO `transaction`(`acId`, `trDate`, `trType`, `typeAmount`, `trBalance`) VALUES ("
					+ acID + ",'" + trDate + "','" + trType + "'," + trAmount + "," + trBalance + ")";

			obj.updateBalance(acID, trBalance);
			stmt.executeUpdate(querySet);
		} catch (SQLException e) {
			System.out.println("query syntax error" + e);
		}
	}

	static void withdraw() {
		obj = new Admin();
		System.out.println("enter account id:");
		int id = scn.nextInt();
		System.out.println("enter password:");
		int tempPassword = scn.nextInt();

		if (!obj.searchById(id)) {
			System.out.println("account doesnot exists !!!!");
			return;
		}
		int password = obj.getPassword(id);
		if (password != tempPassword) {
			System.out.println("failed due to wrong password .... pls contact bank");
			return;
		}

		System.out.println("enter balance to withdraw(>0):");
		int amount = scn.nextInt();
		if (amount <= 0) {
			System.out.println("cannot withdraw amount less than or equal to zero(0)");
			return;
		}

		String trDate = "";
		int acID = 0;
		String trType = "";
		double trAmount = 0.0;
		Double trBalance = 0.0;

		String query = "SELECT * FROM `account` WHERE acId = " + id;
		ResultSet result;
		try {
			result = stmt.executeQuery(query);
			result.next();
			trBalance = result.getDouble("acBalance");
		} catch (SQLException e1) {
			System.out.println("query error");
		}

		if (trBalance < amount) {
			System.out.println("cannot withdraw extra money");
			return;
		}

		String querySet;
		try {
			trDate = formatter.format(new Date());
			acID = id;
			trType = "withdraw";
			trAmount = amount;
			trBalance -= trAmount;
			querySet = "INSERT INTO `transaction`(`acId`, `trDate`, `trType`, `typeAmount`, `trBalance`) VALUES ("
					+ acID + ",'" + trDate + "','" + trType + "'," + trAmount + "," + trBalance + ")";

			obj.updateBalance(acID, trBalance);
			stmt.executeUpdate(querySet);
		} catch (SQLException e) {
			System.out.println("query syntax error" + e);
		}
	}

	void addTransaction(String trDate, int acID, String trType, double trAmount, double trBalance) {
		try {
			String querySet = "INSERT INTO `transaction`(`acId`, `trDate`, `trType`, `typeAmount`, `trBalance`) VALUES ("
					+ acID + ",'" + trDate + "','" + trType + "'," + trAmount + "," + trBalance + ")";
			stmt.executeUpdate(querySet);
			System.out.println("transaction successful");
		} catch (SQLException e) {
			System.out.println("query syntax error" + e);
		}
	}

	public static void main(String[] args) {
		Client obj1 = new Client();
		int exit = 0;
		String staffPassword = "staff";
		System.out.println("Enter staff password:");
		String in = scn.nextLine();

		if (in.equals(staffPassword)) {
			while (exit != 1) {
				int choice = -1;

				System.out.println("1.deposit");
				System.out.println("2.withdraw");
				System.out.println("3.current balance");
				System.out.println("4.show transactions");
				System.out.println("5.exit application");
				System.out.println("enter your choice:");

				choice = scn.nextInt();
				switch (choice) {
				case 1:
					deposit();
					break;
				case 2:
					withdraw();
					break;
				case 3: {
					int id;
					System.out.println("enter acId of account:");
					id = scn.nextInt();
					Admin temp = new Admin();
					if (temp.searchById(id)) {
						System.out.println("current balance corresponding to id: " + id + "is" + obj.getBalance(id));
					} else
						System.out.println("no account found corresponding to id: " + id);
				}
					break;
				case 4: {
					Admin temp = new Admin();
					System.out.println("enter account id:");
					int id = scn.nextInt();
					System.out.println("enter password:");
					int tempPassword = scn.nextInt();

					if (!temp.searchById(id)) {
						System.out.println("account doesnot exists !!!!");
						return;
					}
					int password = temp.getPassword(id);
					if (password != tempPassword) {
						System.out.println("failed due to wrong password .... pls contact bank");
						return;
					}
					System.out.println("transactions are as follows:");
					temp.printTransactions(id);
				}
					break;
				case 5:
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
