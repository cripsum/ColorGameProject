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
				return Response.status(Response.Status.OK).entity(a).build();
			}
			return Response.status(Response.Status.NOT_FOUND)
					.entity(Strumenti.messaggioSempliceJSON(ERRORE, ERRORE_UTENTE_NON_TROVATO)).build();
		}catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(Strumenti.messaggioSempliceJSON(ERRORE, ERRORE_GENERICO + " " + e.getMessage())).build();
		}
		
	}
	
	@GET
	@Path("/getPublicProfile")
	public Response getPublicProfile(@Context HttpHeaders headers, @QueryParam("idUtente") String idUtente) {
		try {
			Utente a = Utente.getUserFromId(idUtente);
			if (a != null) {
				JsonObject obj = new JsonObject();
				obj.addProperty(IDUTENTE, a.getIdUtente());
				obj.addProperty(USERNAME, a.getUsername());
				obj.addProperty(FOTO_PROFILO, a.getFotoProfilo().toString());
				
				return Response.status(Response.Status.OK).entity(obj.toString()).build();
			}
			return Response.status(Response.Status.NOT_FOUND)
					.entity(Strumenti.messaggioSempliceJSON(ERRORE, ERRORE_UTENTE_NON_TROVATO)).build();
		}catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(Strumenti.messaggioSempliceJSON(ERRORE, ERRORE_GENERICO + " " + e.getMessage())).build();
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

		String nome = obj.get(NOME).getAsString();
		String cognome = obj.get(COGNOME).getAsString();
		String dataNascita = obj.get(DATA_NASCITA).getAsString();
		String fotoProfilo = obj.get(FOTO_PROFILO).getAsString();
		String email = obj.get(EMAIL).getAsString();
		String password = obj.get(PASSWORD).getAsString();
		String username = obj.get(USERNAME).getAsString();

		if (!Strumenti.isEmailValid(email)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_FORMATO_MAIL)).build();
        }
        if (Strumenti.usernameAccountEsistente(username)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_USERNAME_ESISTENTE)).build();
        }
        if (Strumenti.emailAccountEsistente(email)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_EMAIL_ESISTENTE)).build();
        }

		try {
			Utente.updateUtente(tok.getIdUtente(), email, password, username, nome, cognome, dataNascita, fotoProfilo);
			return Response.status(Response.Status.OK).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, SUCCESSO_MODIFICHE_EFFETTUATE)).build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(Strumenti.messaggioSempliceJSON(ERRORE, ERRORE_SQL + " " + e.getMessage())).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(String.format("{\"%s\":\"%s\"}", ERRORE, ERRORE_GENERICO + " " + e.getMessage())).build();
		}
	}
			

}
