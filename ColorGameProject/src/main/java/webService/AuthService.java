package webService;

import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import Entita.Utente;
import Interfacce.Messaggi;
import Interfacce.NomiParametri;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import strumenti.Strumenti;
import strumenti.JwtToken;

@Path("/rest/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthService implements NomiParametri, Messaggi {
	@POST
	@Path("/login")
	public Response login(@QueryParam(EMAIL) String email, @QueryParam(PASSWORD) String password, @Context HttpServletRequest request) {
		try {
			Utente a = Utente.getUserFromEmail(email);
			if (a != null) {
				if (a.getPassword().equals(password)) {
					if (a.isUtenteBannato()) {
						return Response.status(Response.Status.FORBIDDEN).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_UTENTE_BANNATO)).build();
					}
					HttpSession session = request.getSession(true);
					session.setAttribute(IDUTENTE, a.getIdUtente());
					session.setAttribute(USERNAME, a.getUsername());
					session.setAttribute(NOME, a.getNome());
					session.setAttribute(COGNOME, a.getCognome());
					session.setAttribute(EMAIL, a.getEmail());
					session.setAttribute(DATA_NASCITA, a.getDataNascita());
					session.setAttribute(FOTO_PROFILO, a.getFotoProfilo());
					session.setAttribute(UTENTE_BANNATO, a.isUtenteBannato());
					session.setAttribute(TIPOUTENTE, a.getTipo());
					session.setAttribute(TOKEN, JwtToken.generateToken(a.getIdUtente(), a.getTipo(), a.getDataRegistrazione().toString()));
					
					return Response.ok(Strumenti.messaggioSempliceJSON(MESSAGGIO, SUCCESSO_LOGIN)).build();
				} else {
					return Response.status(Response.Status.UNAUTHORIZED).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_PASSWORD_SBAGLIATA)).build();
				}
			} else {
				return Response.status(Response.Status.NOT_FOUND).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_UTENTE_NON_REGISTRATO)).build();
			}
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_SQL+" "+e.getMessage())).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_GENERICO+" " + e.getMessage())).build();
		}
	}
	
	@POST
	@Path("/register")
	public Response register(String jsonInput) {
	    try {
	        Gson gson = new Gson();
	        JsonObject jsonObject = gson.fromJson(jsonInput, JsonObject.class);

	        String username = jsonObject.get(USERNAME).getAsString();
	        String password = jsonObject.get(PASSWORD).getAsString();
	        String nome = jsonObject.get(NOME).getAsString();
	        String cognome = jsonObject.get(COGNOME).getAsString();
	        String email = jsonObject.get(EMAIL).getAsString();
	        String dataNascita = jsonObject.get(DATA_NASCITA).getAsString();
	        
	        if (!Strumenti.isEmailValid(email)) {
	            return Response.status(Response.Status.BAD_REQUEST).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_FORMATO_MAIL)).build();
	        }
	        if (Strumenti.usernameAccountEsistente(username)) {
	            return Response.status(Response.Status.BAD_REQUEST).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_USERNAME_ESISTENTE)).build();
	        }
	        if (Strumenti.emailAccountEsistente(email)) {
	            return Response.status(Response.Status.BAD_REQUEST).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_EMAIL_ESISTENTE)).build();
	        }
	        
	        boolean success = Utente.addUtente(username, password, nome, cognome, email, dataNascita);
	        if (success) {
	            return Response.status(Response.Status.CREATED).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, SUCCESSO_INSERIMENTO)).build();
	        } else {
	            return Response.status(Response.Status.BAD_REQUEST).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_INSERIMENTO)).build();
	        }
	    } catch (Exception e) {
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_GENERICO)).build();
	    }
	}
	
	@POST
	@Path("/check")
	public Response check(@Context HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_NON_AUTORIZZATO)).build();
		}
		if (JwtToken.verifyToken((String) session.getAttribute(TOKEN)) != null) {
			return Response.ok().build();
		} else {
			return Response.status(Response.Status.UNAUTHORIZED).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_NON_AUTORIZZATO)).build();
		}
	}
	
	@POST
	@Path("/logout")
	public Response logout(@Context HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		return Response.ok().build();
	}
}
