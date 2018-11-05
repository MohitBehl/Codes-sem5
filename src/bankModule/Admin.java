import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class Admin {
	static Scanner scn = new Scanner(System.in);
	private static String database;
	private static String user;
	private static String password;
	private static Connection con;
	private static Statement stmt;
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	Admin() {
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

	void updateBalance(int id, Double balance) {
		String query = "UPDATE `account` SET `acBalance`=" + balance + "WHERE acId = " + id;
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println("invalid query or connection problem");
			e.printStackTrace();
		}
	}

	int getPassword(int id) {
		String query = "SELECT `acId`, `acHolderName`, `acDate`, `acBalance`,`acPassword`FROM `account` WHERE acId = "
				+ id;
		try {
			ResultSet result = stmt.executeQuery(query);
			result.next();
			return result.getInt("acPassword");
		} catch (SQLException e) {
			System.out.println("invalid query or connection problem");
		}
		return -1;
	}

	double getBalance(int id) {
		String query = "SELECT `acId`, `acHolderName`, `acDate`, `acBalance`,`acPassword`FROM `account` WHERE acId = "
				+ id;
		try {
			ResultSet result = stmt.executeQuery(query);
			result.next();
			return result.getInt("acBalance");
		} catch (SQLException e) {
			System.out.println("invalid query or connection problem");
		}
		return -1;
	}

	static boolean searchById(int id) {
		String query = "SELECT `acId`, `acHolderName`, `acDate`, `acBalance`,`acPassword`FROM `account` WHERE acId = "
				+ id;
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

	private static int generateId() {
		Random random = new Random();
		int ranId = random.nextInt(9999999);
		while (searchById(ranId)) {
			ranId = random.nextInt(9999999);
		}
		return ranId;
	}

	private static void createAccount() {

		Account newAc = new Account();
		newAc.setId(generateId());
		scn.nextLine();

		System.out.println("Enter name of user:");
		String name = scn.nextLine();
		newAc.setName(name);
		System.out.println("Enter starting balance of user:");
		double bal = scn.nextDouble();
		if (bal < 0) {
			System.out.println("starting balance needs to be greater than equal to 0");
			return;
		}
		newAc.setBalance(bal);
		newAc.setDate(formatter.format(new Date()));
		System.out.println("enter password (less than or equal to 4 digits) :");
		int password = scn.nextInt();
		newAc.setPassword(password);

		Client tempObj = new Client();
		try {
			// System.out.println(newAc.getId() + " " + newAc.getBalance());
			String query = "INSERT INTO `account`(`acId`, `acHolderName`, `acDate`, `acBalance`,`acPassword`) VALUES ('"
					+ newAc.getId() + "','" + newAc.getName() + "','" + newAc.getDate() + "','" + newAc.getBalance()
					+ "','" + newAc.getPassword() + "')";

			tempObj.addTransaction(newAc.getDate(), newAc.getId(), "deposit", newAc.getBalance(), 0.0);
			stmt.executeUpdate(query);
			System.out.println("insertion successful");
		} catch (SQLException e) {
			System.out.println("insertion unsuccessful");
			e.printStackTrace();
		}
	}

	private static void deleteAccount() {
		System.out.println("enter id of account that you want to delete :");
		int id = scn.nextInt();
		if (!searchById(id)) {
			System.out.println("no such id found ....!");
			return;
		}
		try {
			String query = "DELETE FROM `account` WHERE acId = " + id;
			stmt.executeUpdate(query);
			System.out.println("deletion successful");
		} catch (SQLException e) {
			System.out.println("deletion unsuccessful");
		}

	}

	private static void showAllAccounts() {
		try {
			String query = "SELECT * FROM `account` WHERE 1";
			ResultSet result = stmt.executeQuery(query);
			System.out.println("AccountID\tHolderName\tDateOfCommencment\tAccountBalance");
			while (result.next()) {
				System.out.println(result.getInt("acId") + "\t\t" + result.getString("acHolderName") + "\t\t"
						+ result.getString("acDate") + "\t\t" + result.getDouble("acBalance"));
			}
			System.out.println("query execution successful");
		} catch (SQLException e) {
			System.out.println("query execution unsuccessful");
		}

	}

	private static void modify(int id) {
		if (!searchById(id)) {
			System.out.println("no such id found ....!");
			return;
		}
		System.out.println("what do you want to modify?");
		System.out.println("1.name");
		System.out.println("2.balance");
		int choice = scn.nextInt();
		String query = "";
		switch (choice) {
		case 1: {
			String updatedName = "";
			scn.nextLine();
			System.out.println("enter new name:");
			updatedName = scn.nextLine();
			query = "UPDATE `account` SET `acHolderName`='" + updatedName + "' WHERE acId = " + id;
		}
			break;
		case 2: {
			Double updatedBalance = 0.0;
			System.out.println("enter new balance:");
			updatedBalance = scn.nextDouble();
			query = "UPDATE `account` SET `acHolderName`=" + updatedBalance + " WHERE acId = " + id;
		}
			break;
		default:
			System.out.println("wrong choice");
			break;
		}
		if (!query.equals("")) {
			try {
				stmt.executeUpdate(query);
				System.out.println("modification successful");
			} catch (SQLException e) {
				System.out.println("modification unsuccessful");
			}

		}
	}

	static void printTransactions(int id) {
		if (!searchById(id)) {
			System.out.println("no such id found ....!");
			return;
		}
		try {
			String query = "SELECT `acId`, `trDate`, `trType`, `typeAmount`, `trBalance` FROM `transaction` WHERE acID = "
					+ id;
			ResultSet result = stmt.executeQuery(query);
			System.out.println("acID \t trDate \t trType \t typeAmount \t trBalance");
			while (result.next()) {
				System.out.println(
						result.getInt("acID") + " \t " + result.getString("trDate") + " \t " + result.getString("trType")
								+ " \t " + result.getInt("typeAmount") + " \t " + result.getDouble("trBalance"));
			}
			System.out.println("print successful");
		} catch (SQLException e) {
			System.out.println("print unsuccessful");
		}
	}

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Admin obj = new Admin();
		int exit = 0;
		String adminPassword = "admin";
		System.out.println("Enter admin password:");
		String in = scn.nextLine();

		if (in.equals(adminPassword)) {
			while (exit != 1) {
				int choice = -1;

				System.out.println("1.add account");
				System.out.println("2.delete account");
				System.out.println("3.modify account");
				System.out.println("4.show all accounts");
				System.out.println("5.show all transactions");
				System.out.println("6.exit application");
				System.out.println("enter your choice:");

				choice = scn.nextInt();
				switch (choice) {
				case 1:
					createAccount();
					break;
				case 2:
					deleteAccount();
					break;
				case 3: {
					showAllAccounts();
					int id;
					System.out.println("enter acId of account you want to modify details:");
					id = scn.nextInt();
					modify(id);
				}
					break;
				case 4:
					showAllAccounts();
					break;
				case 5: {
					System.out.println("enter id of account that you want to delete :");
					int id = scn.nextInt();
					printTransactions(id);
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
