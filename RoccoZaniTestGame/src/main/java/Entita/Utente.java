package Entita;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;

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

	
	public static boolean addUtente(String username,String nome, String cognome, String password, String email,String dataNascita) throws SQLException {
		if(!Strumenti.isEmailValid(email)) {
			return false;
		}
		String idUtente = strumenti.Strumenti.generaId();
		String sqlQuery = "INSERT INTO utente (" + DB_IDUTENTE + "," + DB_USERNAME + "," + DB_NOME + "," + DB_COGNOME + "," + DB_PASSWORD + "," + DB_EMAIL + "," + DB_DATA_NASCITA + ") VALUES (?,?,?,?,?,?,?)";
		try (Connection conn = DBmanager.getConnection();PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
			pstmt.setString(1, idUtente);
			pstmt.setString(2, username);
			pstmt.setString(3, nome);
			pstmt.setString(4, cognome);
			pstmt.setString(5, password);
			pstmt.setString(6, email);
			pstmt.setString(7, dataNascita);
			pstmt.executeUpdate();
			return true;
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
			return false;
		}

	}
	
	//metodi getter e setter
	public String getIdUtente() {
		return idUtente;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(Date dataNascita) {
		this.dataNascita = dataNascita;
	}

	public LocalDateTime getDataRegistrazione() {
		return dataRegistrazione;
	}

	public Blob getFotoProfilo() {
		return fotoProfilo;
	}

	public void setFotoProfilo(Blob fotoProfilo) {
		this.fotoProfilo = fotoProfilo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public boolean isUtenteBannato() {
		return utenteBannato;
	}

	public void setUtenteBannato(boolean utenteBannato) {
		this.utenteBannato = utenteBannato;
	}
}
