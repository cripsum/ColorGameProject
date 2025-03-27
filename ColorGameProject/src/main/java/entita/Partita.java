package entita;

import java.time.LocalDateTime;
import Interfacce.Messaggi;
import Interfacce.NomiParametri;
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
	
	public void newTurno() {
		turno = new TurnoPartita(punteggio);
	}
	
	public TurnoPartita getTurno() {
		return turno;
	}
}
