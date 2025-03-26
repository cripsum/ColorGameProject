package entita;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import Interfacce.Messaggi;
import Interfacce.NomiParametri;
import strumenti.DBmanager;
import strumenti.Strumenti;
import strumenti.TurnoPartita;

public class Partita implements NomiParametri, Messaggi {
	private String idPartita;
	private LocalDateTime dataEOraInizio;
	private LocalDateTime dataEOraFine;
	private String idUtente;
	private int punteggio;
	
	private TurnoPartita turno;
	
	public Partita(String idPartita, LocalDateTime dataEOraInizio, LocalDateTime dataEOraFine, String idUtente, String punteggio) {
		this.idPartita = idPartita;
		this.dataEOraInizio = dataEOraInizio;
		this.dataEOraFine = dataEOraFine;
		this.idUtente = idUtente;
		this.punteggio = Integer.parseInt(punteggio);
	}
	
	public Partita(String idUtente) {
		this.idUtente = idUtente;
		this.idPartita = Strumenti.generaId();
		this.dataEOraInizio = LocalDateTime.now();
		this.punteggio = 0;
		
	}
	
	public void saveOnDB() {
		String idUtente = strumenti.Strumenti.generaId();
		String sqlQuery = "INSERT INTO partita (" + DB_IDPARTITA + ", " + DB_DATAEORAINIZIO + ", " + DB_DATAEORAFINE + ", " + DB_IDUTENTE + ", " + DB_PUNTEGGIO + ") VALUES (?, ?, ?, ?, ?)";
		try (Connection conn = DBmanager.getConnection();PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
			
			pstmt.setString(1, idPartita);
			pstmt.setString(2, dataEOraInizio.toString());
			pstmt.setString(3, LocalDateTime.now().toString());
			pstmt.setString(4, idUtente);
			pstmt.setInt(5, punteggio);
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
		}
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

	public int getPunteggio() {
		return punteggio;
	}

	public void setPunteggio(int punteggio) {
		this.punteggio = punteggio;
	}
	
	public TurnoPartita newTurno() {
		return new TurnoPartita(punteggio);
	}
	
	public TurnoPartita getTurno() {
		return turno;
	}
}
