package strumenti;
import java.sql.*;

public abstract class DBmanager {
	private static String URL = "jdbc:mysql://localhost:3306";
	private static String database = "ZaniTestGame";
	private static String user = "root";
	private static String password = "";

	public static ResultSet executeSQL(String query){
		try (Connection con = DriverManager.getConnection( URL+"/"+database, user, password); Statement stat = con.createStatement()){

	        try(ResultSet result = stat.executeQuery(query);){
	        	return result;
	        }
	    }
	    catch (SQLException exception) {
	        System.out.println("Errore!"+exception.getMessage());
	    }
		return null;
	}
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL+"/"+database, user, password);
	}
}
