package Entita;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import DBmanager.DBmanager;

public class Utente {
	//attributi
	private String id;
	private String username;
	private String password;
	private String tipo;
	private String email;
	private Date dataNascita;
	private LocalDateTime dataRegistrazione;
	private Blob fotoProfilo;
	
	//costruttore
	public Utente(String id, String username, String password, String tipo, String email, Date dataNascita, LocalDateTime dataRegistrazione, Blob fotoProfilo) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.tipo = tipo;
		this.email = email;
		this.dataNascita = dataNascita;
		this.dataRegistrazione = dataRegistrazione;
		this.fotoProfilo = fotoProfilo;
	}

	//metodi stastici
	
	public static Utente getUser(String email) throws SQLException {
		String sqlQuery = "SELECT * FROM user WHERE email =" + "'" + email + "'";
		ResultSet rs = DBmanager.executeSQL(sqlQuery);
		if (rs.next()) {
			Utente utente = new Utente(rs.getString("id"), rs.getString("username"), rs.getString("password"), rs.getString("tipo"), rs.getString("email"), rs.getDate("dataNascita"), rs.getTimestamp("dataRegistrazione").toLocalDateTime(), rs.getBlob("fotoProfilo"));
			return utente;
		}
		return null;
	}
	
	public static int addUtente(String username, String password, String tipo, String email, Date dataNascita, LocalDateTime dataRegistrazione, Blob fotoProfilo) {
		String id = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
		String sqlQuery = "INSERT INTO user (id, username, password, tipo, email, dataNascita, dataRegistrazione, fotoProfilo) VALUES ('" + id + "','" + username + "', '" + password + "', '" + tipo + "', '" + email + "', '" + dataNascita + "', '" + dataRegistrazione + "', '" + fotoProfilo + "')";
		DBmanager.executeSQL(sqlQuery);
		return 1;
	}
	
	
	
	//metodi getter e setter
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
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


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
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
}
