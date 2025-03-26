package entita;

public class RecordClassifica {

	private String idUtente;
	private String username;
	private int punteggio;

	public RecordClassifica(String idUtente, String username, int punteggio) {
			this.idUtente = idUtente;
			this.username = username;
			this.punteggio = punteggio;
		}

	public String getIdUtente() {
		return idUtente;
	}

	public String getUsername() {
		return username;
	}

	public int getPunteggio() {
		return punteggio;
	}

}
