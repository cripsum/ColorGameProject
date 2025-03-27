package webService;

import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import Interfacce.Messaggi;
import Interfacce.NomiParametri;
import entita.Utente;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import strumenti.Strumenti;
import strumenti.JwtToken;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthService implements NomiParametri, Messaggi {
	@POST
	@Path("/login")
	public Response login(String jsonInput, @Context HttpServletRequest request) {	
		try {
			Gson gson = new Gson();
			JsonObject obj = gson.fromJson(jsonInput, JsonObject.class);
			String email = obj.get(EMAIL).getAsString();
			String password = obj.get(PASSWORD).getAsString();
			Utente a = Utente.getUserFromEmail(email);
			if (a != null) {
				if (a.getPassword().equals(password)) {
					if (a.isUtenteBannato()) {
						return Response.status(Response.Status.FORBIDDEN).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_UTENTE_BANNATO)).build();
					}

					JsonObject json = new JsonObject();
					json.addProperty(IDUTENTE, a.getIdUtente());
					json.addProperty(USERNAME, a.getUsername());
					json.addProperty(NOME, a.getNome());
					json.addProperty(COGNOME, a.getCognome());
					json.addProperty(EMAIL, a.getEmail());
					json.addProperty(DATA_NASCITA, a.getDataNascita().toString());
					json.addProperty(FOTO_PROFILO, Strumenti.fotoProfiloToBase64(a.getFotoProfilo()));
					json.addProperty(UTENTE_BANNATO, a.isUtenteBannato());
					json.addProperty(TIPOUTENTE, a.getTipo());
					json.addProperty(TOKEN, JwtToken.generateToken(a.getIdUtente(), a.getTipo(), a.getDataRegistrazione().toString()));
					json.addProperty(MESSAGGIO, SUCCESSO_LOGIN);
					return Response.ok(json.toString()).build();
				} else {
					return Response.status(Response.Status.UNAUTHORIZED).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_PASSWORD_SBAGLIATA)).build();
				}
			} else {
				return Response.status(Response.Status.NOT_FOUND).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_UTENTE_NON_REGISTRATO)).build();
			}
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_SQL+" "+e.getMessage())).build();
		} catch(NullPointerException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_RICHIESTA_NON_VALIDA)).build();
		}
		catch(Exception e) {
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
	        
	        boolean success = Utente.addUtente(username, nome, cognome,password, email, dataNascita);
	        if (success) {
	            return Response.status(Response.Status.CREATED).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, SUCCESSO_INSERIMENTO)).build();
	        } else {
	            return Response.status(Response.Status.BAD_REQUEST).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_INSERIMENTO)).build();
	        }
	    } catch (Exception e) {
	    	System.out.println(e.getMessage());
	        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_GENERICO)).build();
	    }
	}
	
	@GET
	@Path("/check")
	public Response check(@Context HttpHeaders headers) {
		String token = headers.getHeaderString("Authorization");
	    if (token == null || !token.startsWith("Bearer ")) {
	        return Response.status(Response.Status.UNAUTHORIZED)
	                .entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_TOKEN_MANCANTE))
	                .build();
	    }

	    token = token.substring(7);
	    if (JwtToken.verifyToken(token) != null) {
	        return Response.ok(Strumenti.messaggioSempliceJSON(MESSAGGIO,SUCCESSO_VERIFICA )).build();
	    } else {
	        return Response.status(Response.Status.UNAUTHORIZED)
	                .entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_TOKEN_NON_VALIDO))
	                .build();
	    }
	}
}
