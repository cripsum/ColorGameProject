package strumenti;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import Interfacce.Messaggi;
import Interfacce.NomiParametri;
import entita.RecordClassifica;

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
	
	public static String messaggioSempliceJSON(String arttibuto, String messaggio) {
		return "{\""+arttibuto+"\":\""+messaggio+"\"}";
	}
	
	public static List<RecordClassifica> getClassifica() throws SQLException {
		List<RecordClassifica> classifica = new ArrayList<RecordClassifica>();
		String sqlQuery = "SELECT * FROM classifica ORDER BY punteggio DESC";
		try (Connection conn = DBmanager.getConnection();PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				classifica.add(new RecordClassifica(rs.getString(DB_IDUTENTE),rs.getString(DB_USERNAME), rs.getInt(DB_PUNTEGGIO)));
			}
		}
		return classifica;
	}
	
	public static List<UtentePerAdmin> getUtenti() throws SQLException {
		List<UtentePerAdmin> utenti = new ArrayList<UtentePerAdmin>();
		String sqlQuery = "SELECT * FROM utente";
		try (Connection conn = DBmanager.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				utenti.add(new UtentePerAdmin(rs.getString(DB_IDUTENTE), rs.getString(DB_USERNAME), rs.getString(DB_PASSWORD),
						rs.getString(DB_NOME), rs.getString(DB_COGNOME), rs.getString(DB_EMAIL),
						rs.getDate(DB_DATA_NASCITA).toString(), rs.getTimestamp(DB_DATA_REGISTRAZIONE).toLocalDateTime().toString(),
						Strumenti.fotoProfiloToBase64(rs.getBlob(DB_FOTO_PROFILO)), rs.getString(DB_TIPOUTENTE), rs.getBoolean(DB_UTENTE_BANNATO)));
			}
		}
		return utenti;
	}
	
	public static String fotoProfiloToBase64(Blob fotoProfilo) {
        if (fotoProfilo == null) {
            return null;
        }
        try (InputStream is = fotoProfilo.getBinaryStream();
             ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(os.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}
	
	public static String colorToHex(Color color) {
		return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
	}
}
