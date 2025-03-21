package Entita;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import DBmanager.DBmanager;
import Interfacce.Messaggi;
import Interfacce.NomiParametri;

public class Partita implements NomiParametri, Messaggi {
	private String idPartita;
	private LocalDateTime dataEOraInizio;
	private LocalDateTime dataEOraFine;
	private String idUtente;
	private String idPunteggio;
	
	public Partita(String idPartita, LocalDateTime dataEOraInizio, LocalDateTime dataEOraFine, String idUtente, String idPunteggio) {
		this.idPartita = idPartita;
		this.dataEOraInizio = dataEOraInizio;
		this.dataEOraFine = dataEOraFine;
		this.idUtente = idUtente;
		this.idPunteggio = idPunteggio;
	}

	public String getIdPartita() {
		return idPartita;
	}

	public void setIdPartita(String idPartita) {
		this.idPartita = idPartita;
	}

	public LocalDateTime getDataEOraInizio() {
		return dataEOraInizio;
	}

	public void setDataEOraInizio(LocalDateTime dataEOraInizio) {
		this.dataEOraInizio = dataEOraInizio;
	}

	public LocalDateTime getDataEOraFine() {
		return dataEOraFine;
	}

	public void setDataEOraFine(LocalDateTime dataEOraFine) {
		this.dataEOraFine = dataEOraFine;
	}

	public String getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(String idUtente) {
		this.idUtente = idUtente;
	}

	public String getIdPunteggio() {
		return idPunteggio;
	}

	public void setIdPunteggio(String idPunteggio) {
		this.idPunteggio = idPunteggio;
	}
	
	//logica del gioco
	public static int getPartitaUtente(String PARTITAIDUTENTE) {
		String sqlQuery = "SELECT punteggio FROM partite WHERE "+DB_PARTITAIDUTENTE+" = ?";
		try (Connection conn = DBmanager.getConnection();PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
			pstmt.setString(1, PARTITAIDUTENTE);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					System.out.println("Utente trovato");

					System.out.println("Utente non trovato");
				}
			}

		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
			return -1;
		}
		return 0;
	}	
}
