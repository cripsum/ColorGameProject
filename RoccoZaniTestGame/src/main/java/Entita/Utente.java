package Entita;

import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;

import DBmanager.DBmanager;

public class Utente {
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
}
