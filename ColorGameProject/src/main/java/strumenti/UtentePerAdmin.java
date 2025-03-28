package strumenti;

public class UtentePerAdmin {
	private String idUtente;
	private String username;
	private String password;
	private String nome;
	private String cognome;
	private String email;
	private String dataNascita;
	private String dataRegistrazione;
	private String fotoProfilo;
	private String tipo;
	private boolean utenteBannato;
	
	public UtentePerAdmin(String idUtente, String username, String password, String nome, String cognome, String email, String dataNascita, String dataRegistrazione, String fotoProfilo, String tipo, boolean utenteBannato) {
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
		this.utenteBannato = utenteBannato;
	}

	public String getIdUtente() {
		return idUtente;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getNome() {
		return nome;
	}

	public String getCognome() {
		return cognome;
	}

	public String getEmail() {
		return email;
	}

	public String getDataNascita() {
		return dataNascita;
	}

	public String getDataRegistrazione() {
		return dataRegistrazione;
	}

	public String getFotoProfilo() {
		return fotoProfilo;
	}

	public String getTipo() {
		return tipo;
	}

	public boolean isUtenteBannato() {
		return utenteBannato;
	}
	
	
	
	
}
