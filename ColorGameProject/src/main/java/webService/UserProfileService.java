package webService;

import java.sql.SQLException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import Interfacce.Messaggi;
import Interfacce.NomiParametri;
import entita.Utente;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import strumenti.JwtToken;
import strumenti.JwtToken.Token;
import strumenti.Strumenti;

@Path("/userProfile")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserProfileService implements Messaggi, NomiParametri {
	@GET
	@Path("/getProfile")
	public Response getProfile(@Context HttpHeaders headers) {
		Response verifica = JwtToken.verificaToken(headers);
		if (verifica.getStatus() != 200)
			return verifica;
		Token tok = (Token) verifica.getEntity();
		try {
			Utente a = Utente.getUserFromId(tok.getIdUtente());
			if (a != null) {
				JsonObject obj = new JsonObject();
				obj.addProperty(IDUTENTE, a.getIdUtente());
				obj.addProperty(USERNAME, a.getUsername());
				obj.addProperty(NOME, a.getNome());
				obj.addProperty(COGNOME, a.getCognome());
				obj.addProperty(DATA_NASCITA, a.getDataNascita().toString());
				obj.addProperty(FOTO_PROFILO, Strumenti.fotoProfiloToBase64(a.getFotoProfilo()));
				obj.addProperty(EMAIL, a.getEmail());
				return Response.status(Response.Status.OK).entity(obj.toString()).build();
			}
			return Response.status(Response.Status.NOT_FOUND).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_UTENTE_NON_TROVATO)).build();
		}catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_GENERICO + " " + e.getMessage())).build();
		}
		
	}
	
	@GET
	@Path("/getPublicProfile")
	public Response getPublicProfile(@Context HttpHeaders headers, @QueryParam(IDUTENTE) String idUtente) {
		try {
			Utente a = Utente.getUserFromId(idUtente);
			if (a != null) {
				JsonObject obj = new JsonObject();
				obj.addProperty(IDUTENTE, a.getIdUtente());
				obj.addProperty(USERNAME, a.getUsername());
				obj.addProperty(FOTO_PROFILO, Strumenti.fotoProfiloToBase64(a.getFotoProfilo()));
				
				return Response.status(Response.Status.OK).entity(obj.toString()).build();
			}
			return Response.status(Response.Status.NOT_FOUND)
					.entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_UTENTE_NON_TROVATO)).build();
		}catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_GENERICO + " " + e.getMessage())).build();
		}
		
	}

	@POST
	@Path("/updateProfile")
	public Response updateProfile(@Context HttpHeaders headers, String jsonInput) {
		Response verifica = JwtToken.verificaToken(headers);
		if (verifica.getStatus() != 200)
			return verifica;
		Token tok = (Token) verifica.getEntity();

		Gson gson = new Gson();
		JsonObject obj = gson.fromJson(jsonInput, JsonObject.class);

		String nome = null;
		String cognome = null;
		String dataNascita = null;
		String fotoProfilo = null;
		String email = null;
		String password = null;
		String username = null;
		if (obj.has(NOME))
			nome = obj.get(NOME).getAsString();
		if (obj.has(COGNOME))
			cognome = obj.get(COGNOME).getAsString();
		if (obj.has(DATA_NASCITA))
			dataNascita = obj.get(DATA_NASCITA).getAsString();
		if (obj.has(FOTO_PROFILO))
			fotoProfilo = obj.get(FOTO_PROFILO).getAsString();
		if (obj.has(EMAIL))
			email = obj.get(EMAIL).getAsString();
		if (obj.has(PASSWORD))
			password = obj.get(PASSWORD).getAsString();
		if (obj.has(USERNAME))
			username = obj.get(USERNAME).getAsString();

		if(email!=null) {
			if (!Strumenti.isEmailValid(email)) {
	            return Response.status(Response.Status.BAD_REQUEST).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_FORMATO_MAIL)).build();
	        }
			 if (Strumenti.emailAccountEsistente(email) || email == null) {
		            return Response.status(Response.Status.BAD_REQUEST).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_EMAIL_ESISTENTE)).build();
		        }
		}
		if(username!=null) {
			if (Strumenti.usernameAccountEsistente(username) || username == null) {
				return Response.status(Response.Status.BAD_REQUEST)
						.entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_USERNAME_ESISTENTE)).build();
			}
		}
       

		try {
			Utente.updateUtente(tok.getIdUtente(), email, password, username, nome, cognome, dataNascita, fotoProfilo);
			return Response.status(Response.Status.OK).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, SUCCESSO_MODIFICHE_EFFETTUATE)).build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_SQL + " " + e.getMessage())).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_GENERICO)).build();
		}
	}
			

}
