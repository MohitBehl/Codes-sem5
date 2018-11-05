
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class jdbc {
	public static void main(String args[]) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String database = "bankmodule";
		String user = "root";
		String password = "";
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database, user, password);
		try {
			Statement stmt = con.createStatement();
			ResultSet result = stmt.executeQuery("SELECT `student`.* FROM `student`;");

			while (result.next()) {
				System.out.println(result.getInt("roll") + " " + result.getString("name"));
			}
			System.out.println(result.getFetchSize());
			System.out.println(result.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			con.close();
		}
	}
}
