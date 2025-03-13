package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import Entita.Utente;
import Interfacce.MessaggiErrore;
import Interfacce.NomiParametri;

/**
 * Servlet implementation class LogIn
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet implements NomiParametri, MessaggiErrore {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String message = null; //risposta al client
		int exists = 0; //variabile di controllo per la verifica dell'esistenza dell'utente
		HttpSession session = null; //parametro di sessione
		
		String username = request.getParameter(USER); 
		String password = request.getParameter(PASSWORD);
		
		try {
			exists = Utente.isUser(username, password);
		} catch (SQLException e) {
			e.printStackTrace();
			message = ERRORE_LOGIN_SQL;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
	
		switch (exists) {
		case 0: //utente non registrato
			message = LOGIN_ERROR_MESSAGE;
			break;
		case 3: //password errata
			message = PASSWORD_ERROR_MESSAGE;
			break;
		case 4: //utente registrato come cliente
			message = username + "$cliente";
			//avvia la sessione utente
			session = request.getSession();
			session.setAttribute(USER, username);
			session.setAttribute(TIPOUTENTE, "cliente");
			break;
		case 5: //utente registrato come admin
			message = username + "$admin";
			//avvia la sessione utente
			session = request.getSession();
			session.setAttribute(USER, username);
			session.setAttribute(TIPOUTENTE, "admin");
			break;
		}
		
		//invia la risposta al client
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
