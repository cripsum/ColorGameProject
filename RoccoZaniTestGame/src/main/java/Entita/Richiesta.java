package Entita;

public class Richiesta {
	private String idRichiesta;
	private String idUtente;
	private String messaggio;
	private String email;
	private String stato;
	
	public Richiesta(String idRichiesta, String idUtente, String messaggio, String email, String stato) {
		this.idRichiesta = idRichiesta;
		this.idUtente = idUtente;
		this.messaggio = messaggio;
		this.email = email;
		this.stato = stato;
	}

	public String getIdRichiesta() {
		return idRichiesta;
	}

	public String getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(String idUtente) {
		this.idUtente = idUtente;
	}

	public String getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}
}
