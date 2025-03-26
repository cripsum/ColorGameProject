package entita;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;

import javax.sql.rowset.serial.SerialBlob;

import Interfacce.Messaggi;
import Interfacce.NomiParametri;
import strumenti.DBmanager;
import strumenti.Strumenti;

public class Utente implements NomiParametri, Messaggi {
	//attributi
	private String idUtente;
	private String username;
	private String password;
	private String nome;
	private String cognome;
	private String email;
	private Date dataNascita;
	private LocalDateTime dataRegistrazione;
	private Blob fotoProfilo;
	private String tipo;
	private boolean utenteBannato;
	
	//costruttore
	public Utente(String idUtente, String username, String password, String nome, String cognome, String email, Date dataNascita, LocalDateTime dataRegistrazione, Blob fotoProfilo, String tipo, boolean utenteBannato) {
		this.idUtente = idUtente;
		this.username = username;
		this.password = password;
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		this.dataNascita = dataNascita;
		this.dataRegistrazione = dataRegistrazione;
		this.fotoProfilo = fotoProfilo;
		this.tipo = tipo;
		this.utenteBannato = false;
	}

	//metodi stastici
	public static Utente getUserFromEmail(String email) throws SQLException {
	    String sqlQuery = "SELECT * FROM utente WHERE LOWER(" + DB_EMAIL + ") =LOWER(?)";
	   	    try (Connection conn = DBmanager.getConnection();PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
	        pstmt.setString(1, email);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return new Utente(
	                    rs.getString(DB_IDUTENTE),
	                    rs.getString(DB_USERNAME),
	                    rs.getString(DB_PASSWORD),
	                    rs.getString(DB_NOME),
	                    rs.getString(DB_COGNOME),
	                    rs.getString(DB_EMAIL),
	                    rs.getDate(DB_DATA_NASCITA),
	                    rs.getTimestamp(DB_DATA_REGISTRAZIONE).toLocalDateTime(),
	                    rs.getBlob(DB_FOTO_PROFILO),
	                    rs.getString(DB_TIPOUTENTE),
	                    rs.getBoolean(DB_UTENTE_BANNATO)
	                );
	            }
	        }
	    }
	    return null;
	}

	public static Utente getUserFromId(String idUtente) throws SQLException {
	    String sqlQuery = "SELECT * FROM utente WHERE " + DB_IDUTENTE + " = ?";
	    try (Connection conn = DBmanager.getConnection();PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
	        pstmt.setString(1, idUtente);
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return new Utente(
	                    rs.getString(DB_IDUTENTE),
	                    rs.getString(DB_USERNAME),
	                    rs.getString(DB_PASSWORD),
	                    rs.getString(DB_NOME),
	                    rs.getString(DB_COGNOME),
	                    rs.getString(DB_EMAIL),
	                    rs.getDate(DB_DATA_NASCITA),
	                    rs.getTimestamp(DB_DATA_REGISTRAZIONE).toLocalDateTime(),
	                    rs.getBlob(DB_FOTO_PROFILO),
	                    rs.getString(DB_TIPOUTENTE),
	                    rs.getBoolean(DB_UTENTE_BANNATO)
	                );
	            }
	        }
	    }
	    return null;
	}
	
	public static boolean addUtente(String username,String nome, String cognome, String password, String email,String dataNascita) throws SQLException, Exception {
		if(!Strumenti.isEmailValid(email)) {
			return false;
		}
		String idUtente = strumenti.Strumenti.generaId();
		String sqlQuery = "INSERT INTO utente (" + DB_IDUTENTE + "," + DB_USERNAME + "," + DB_NOME + "," + DB_COGNOME + "," + DB_PASSWORD + "," + DB_EMAIL + "," + DB_DATA_NASCITA + "," + DB_FOTO_PROFILO +"," + DB_TIPOUTENTE +") VALUES (?,?,?,?,?,?,?,?,?)";
		try (Connection conn = DBmanager.getConnection();PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
			pstmt.setString(1, idUtente);
			pstmt.setString(2, username);
			pstmt.setString(3, nome);
			pstmt.setString(4, cognome);
			pstmt.setString(5, password);
			pstmt.setString(6, email);
			pstmt.setString(7, dataNascita);
			InputStream is = Utente.class.getClassLoader().getResourceAsStream("img/fotoProfiloDefault.jpg");
			if (is != null) {
			    pstmt.setBinaryStream(8, is, is.available());
			} else {
			    pstmt.setBytes(8, new byte[0]);
			}
			pstmt.setString(9, "utente");
			pstmt.executeUpdate();
			return true;
		}

	}
	
	public static boolean updateUtente(String idUtente,String email, String password, String username, String nome, String cognome, String dataNascita, String fotoProfilo) throws SQLException {
		Utente a = getUserFromId(idUtente);
		Blob foto = null;
		if(email == null) {
			email = a.getEmail();
		}
		if(password == null) {
			password = a.getPassword();
		}
		if(username == null) {
			username = a.getUsername();
		}
		if(nome == null) {
			nome = a.getNome();
		}
		if(cognome == null) {
			cognome = a.getCognome();
		}
		if(dataNascita == null) {
			dataNascita = a.getDataNascita().toString();
		}
		if(fotoProfilo != null) {
			foto =  new SerialBlob(fotoProfilo.getBytes());
		}
		else {
			foto = a.getFotoProfilo();
		}
		String sqlQuery = "UPDATE utente SET " + DB_EMAIL + " = ?, " + DB_PASSWORD + " = ?, " + DB_USERNAME + " = ?, " + DB_NOME + " = ?, " + DB_COGNOME + " = ?, " + DB_DATA_NASCITA + " = ?, " + DB_FOTO_PROFILO + " = ? WHERE " + DB_IDUTENTE + " = ?";
		try (Connection conn = DBmanager.getConnection();PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			pstmt.setString(3, username);
			pstmt.setString(4, nome);
			pstmt.setString(5, cognome);
			pstmt.setString(6, dataNascita);
			pstmt.setBlob(7,foto);
			pstmt.setString(8, idUtente);
			pstmt.executeUpdate();
			return true;
		}
	}
	
	public static boolean setTipo(String idUtente, String tipo) throws SQLException {
		String sqlQuery = "UPDATE utente SET " + DB_TIPOUTENTE + " = ? WHERE " + DB_IDUTENTE + " = ?";
		try (Connection conn = DBmanager.getConnection();PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
			pstmt.setString(1, tipo);
			pstmt.setString(2, idUtente);
			pstmt.executeUpdate();
			return true;
		}
	}
	
	public static boolean setBannato(String idUtente, boolean bannato) throws SQLException {
		String sqlQuery = "UPDATE utente SET " + DB_UTENTE_BANNATO + " = ? WHERE " + DB_IDUTENTE + " = ?";
		try (Connection conn = DBmanager.getConnection();PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
			pstmt.setBoolean(1, bannato);
			pstmt.setString(2, idUtente);
			pstmt.executeUpdate();
			return true;
		}
	}
	
	//metodi getter
	public String getIdUtente() {
		return idUtente;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getNome() {
		return nome;
	}

	public String getCognome() {
		return cognome;
	}

	public String getEmail() {
		return email;
	}

	public Date getDataNascita() {
		return dataNascita;
	}

	public LocalDateTime getDataRegistrazione() {
		return dataRegistrazione;
	}

	public Blob getFotoProfilo() {
		return fotoProfilo;
	}

	public String getTipo() {
		return tipo;
	}

	public boolean isUtenteBannato() {
		return utenteBannato;
	}

	public void setUtenteBannato(boolean utenteBannato) {
		this.utenteBannato = utenteBannato;
	}
}
