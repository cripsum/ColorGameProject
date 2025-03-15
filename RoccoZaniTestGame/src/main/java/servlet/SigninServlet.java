package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import Entita.Utente;
import Interfacce.Messaggi;
import Interfacce.NomiParametri;

/**
 * Servlet implementation class SigninServlet
 */
@WebServlet("/SigninServlet")
public class SigninServlet extends HttpServlet implements Messaggi, NomiParametri {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SigninServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		String message = null;
		
		// recupera i parametri inviati dal client
		String username = request.getParameter(USERNAME);
		String password = request.getParameter(PASSWORD);
		String nome = request.getParameter(NOME);
		String cognome = request.getParameter(COGNOME);
		String email = request.getParameter(EMAIL);
		String dataNascita = request.getParameter(DATA_NASCITA);
		String tipo = "cliente";
		
		// inserisce l'istanza nella tabella user
		try {
			if(Utente.addUtente(username, password, nome, cognome, email, dataNascita, tipo)) {
				message = INSERT_SUCCESS_MESSAGE;
			} else {
				message = INSERT_ERROR_MESSAGE;
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = SQL_SIGNUP_EXCEPTION_MESSAGE;
		}
		
		// invia la risposta al client
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
			message = IO_EXCEPTION_MESSAGE;
		}
		writer.println(message);
	}

}
