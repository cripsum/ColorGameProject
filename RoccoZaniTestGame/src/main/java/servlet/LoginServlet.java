package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import Entita.Utente;
import Interfacce.Messaggi;
import Interfacce.NomiParametri;

/**
 * Servlet implementation class LogIn
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet implements NomiParametri, Messaggi {
	private static final long serialVersionUID = 1L;
	
	/**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String message = null; //risposta al client
		Utente a = null; //utente
		HttpSession session = null; //parametro di sessione
		
		String email = request.getParameter(EMAIL); 
		String password = request.getParameter(PASSWORD);
		try {
			a = Utente.getUserFromEmail(email);
			if (a != null) {
				System.out.println("ee");
				if (a.getPassword().equals(password)) {//password corretta
					if(a.isUtenteBannato()) {//utente bannato
						System.out.println("aa");

						message = UTENTE_BANNATO;
					}
					else {//utente non bannato
						session = request.getSession();
						session.setAttribute(IDUTENTE, a.getIdUtente());
						session.setAttribute(USERNAME, a.getUsername());
						session.setAttribute(NOME, a.getNome());
						session.setAttribute(COGNOME, a.getCognome());
						session.setAttribute(EMAIL, a.getEmail());
						session.setAttribute(DATA_NASCITA, a.getDataNascita());
						session.setAttribute(DATA_REGISTRAZIONE, a.getDataRegistrazione());
						session.setAttribute(FOTO_PROFILO, a.getFotoProfilo());
						session.setAttribute(UTENTE_BANNATO, a.isUtenteBannato());
						if (a.getTipo().equals("cliente")) {
							System.out.println("bb");

							message = email + "$cliente";
							session.setAttribute(TIPOUTENTE, "cliente");
						} else if (a.getTipo().equals("admin")) {
							System.out.println("cc");

							message = email + "$admin";
							session.setAttribute(TIPOUTENTE, "admin");
						}
					} 
				}
				else{//password errata
					System.out.println("dd");
					message = PASSWORD_ERROR_MESSAGE;
				}
			} else {//utente non registrato
				message = LOGIN_ERROR_MESSAGE;
			}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				message = ERRORE_LOGIN_SQL;
			} catch (Exception e) {
				e.printStackTrace();
		}

		//invia la risposta al client
		
		PrintWriter writer = response.getWriter();
		System.out.println(message);
		writer.println(message);
	}


}
