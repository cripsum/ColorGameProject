package Entita;

import java.time.LocalDateTime;

public class Partita {
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
	
	
	
	
}	
