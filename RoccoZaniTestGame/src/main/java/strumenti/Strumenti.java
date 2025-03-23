package strumenti;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import Interfacce.Messaggi;
import Interfacce.NomiParametri;

public abstract class Strumenti implements NomiParametri, Messaggi {
	public static boolean isEmailValid(String email) {
		String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
		return email.matches(regex);
	}	
	
	public static String generaId() {
		return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
	}
	
	public static boolean usernameAccountEsistente(String username) {
		String sqlQuery = "SELECT * FROM utente WHERE "+DB_USERNAME+" = ?";
		try (Connection conn = DBmanager.getConnection();PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return true;
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}
	public static boolean emailAccountEsistente(String email) {
		String sqlQuery = "SELECT * FROM utente WHERE "+DB_EMAIL+" = ?";
		try (Connection conn = DBmanager.getConnection();PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				return true;
			}
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;
	}
}
