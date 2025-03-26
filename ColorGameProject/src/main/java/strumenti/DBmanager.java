package strumenti;
import java.sql.*;

public abstract class DBmanager {
	private static String URL = "jdbc:mysql://localhost:3306";
	private static String database = "colorgameproject";
	private static String user = "root";
	private static String password = "";

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL+"/"+database, user, password);
	}
}
