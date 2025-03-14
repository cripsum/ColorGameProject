package Entita;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import DBmanager.DBmanager;
import Interfacce.Messaggi;
import Interfacce.NomiParametri;

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
	
	//costruttore
	public Utente(String idUtente, String username, String password, String nome, String cognome, String email, Date dataNascita, LocalDateTime dataRegistrazione, Blob fotoProfilo, String tipo) {
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
	}

	//metodi stastici
	
	public static Utente getUser(String email) throws SQLException {
		String sqlQuery = "SELECT * FROM user WHERE email =" + "'" + email + "'";
		ResultSet rs = DBmanager.executeSQL(sqlQuery);
		if (rs.next()) {
			Utente utente = new Utente(rs.getString(IDUTENTE), rs.getString(USERNAME), rs.getString(PASSWORD), rs.getString(NOME), rs.getString(COGNOME), rs.getString(EMAIL), rs.getDate(DATA_NASCITA), rs.getTimestamp(DATA_REGISTRAZIONE).toLocalDateTime(), rs.getBlob(FOTO_PROFILO), rs.getString(TIPOUTENTE));
			return utente;
		}
		return null;
	}
	
	public static boolean addUtente(String username,String nome, String cognome, String password, String email,String dataNascita, String tipo) throws SQLException {
		String idUtente = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
		String sqlQuery = "INSERT INTO user (idUtente, username, nome, cognome, password, email, dataNascita, dataRegistrazione, fotoprofilo, tipo) VALUES ('" + idUtente + "', '" + username + "', '" + nome + "', '" + cognome + "', '" + password + "', '" + email + "', '" + dataNascita + "', '" + Timestamp.valueOf(LocalDateTime.now()) + "', '" + tipo + "')";
		try {
			DBmanager.executeSQL(sqlQuery);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//metodi getter e setter
	public String getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(String idUtente) {
		this.idUtente = idUtente;
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

	public void setDataRegistrazione(LocalDateTime dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
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
}
