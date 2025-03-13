package Entita;

import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import DBmanager.DBmanager;

public class Utente {
	private String id;
	private String username;
	private String password;
	private String tipo;
	private String email;
	private Date dataNascita;
	private LocalDateTime dataRegistrazione;
	private Blob fotoProfilo;
	
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

	
	public static int isUser(String username, String password) throws SQLException, NoSuchAlgorithmException {
		int exists = 0;
		String sqlQuery = "SELECT * FROM user WHERE username =" + "'" + username + "'";
		
		//esecuzione della query
		ResultSet rs = DBmanager.executeSQL(sqlQuery);
		//analisi del risultato
		if (rs.next()) { // username trovato
			//password criptata
			String codedPassword = rs.getString("password");
			if (password.equals(codedPassword)) //le due password coincidono
				if (rs.getString("tipo").equals("cliente"))
					exists = 4; //ruolo: cliente
				else
					exists = 5; //ruolo: admin
			else
				exists = 3;
		}
		else //utente non trovato
			exists = 0;
		
		return exists;
	}
	
	public static int addUtente(String username, String password, String tipo, String email, Date dataNascita, LocalDateTime dataRegistrazione, Blob fotoProfilo) {
		String id = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
		String sqlQuery = "INSERT INTO user (id, username, password, tipo, email, dataNascita, dataRegistrazione, fotoProfilo) VALUES ('" + id + "','" + username + "', '" + password + "', '" + tipo + "', '" + email + "', '" + dataNascita + "', '" + dataRegistrazione + "', '" + fotoProfilo + "')";
		DBmanager.executeSQL(sqlQuery);
		return 1;
	}
}
