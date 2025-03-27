package webService;

import java.sql.SQLException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import Interfacce.Messaggi;
import Interfacce.NomiParametri;
import entita.RecordClassifica;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import strumenti.GameManager;
import strumenti.JwtToken;
import strumenti.JwtToken.Token;
import strumenti.Strumenti;

@Path("/gioco")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GameService implements Messaggi, NomiParametri {
	@GET
	@Path("/checkPartita")
	public Response getPartita(@Context HttpHeaders headers) {
		Response verifica = JwtToken.verificaToken(headers);
		if (verifica.getStatus() != 200)
			return verifica;
		Token tok = (Token) verifica.getEntity();

		if (GameManager.cercaPartita(tok.getIdUtente())) {
			return Response.status(Response.Status.OK)
					.entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, SUCCESSO_PARTITA_TROVATA)).build();
		}
		return Response.status(Response.Status.NOT_FOUND)
				.entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_PARTITA_NON_TROVATA)).build();
	}

	@POST
	@Path("/addPartita")
	public Response addPartita(@Context HttpHeaders headers) {
		Response verifica = JwtToken.verificaToken(headers);
		if (verifica.getStatus() != 200)
			return verifica;
		Token tok = (Token) verifica.getEntity();

		GameManager.aggiungiPartita(tok.getIdUtente());
		return Response.status(Response.Status.OK)
				.entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, SUCCESSO_INSERIMENTO)).build();
	}

	@POST
	@Path("/checkAnswer")
	public Response checkAnswer(String jsonInput, @Context HttpHeaders headers) {
		JsonObject obj = new Gson().fromJson(jsonInput, JsonObject.class);
		int corX = obj.get("x").getAsInt();
		int corY = obj.get("y").getAsInt();
		Response verifica = JwtToken.verificaToken(headers);
		if (verifica.getStatus() != 200)
			return verifica;
		Token tok = (Token) verifica.getEntity();

		if (corX < 0 || corY < 0) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_RICHIESTA_NON_VALIDA)).build();
		}
		if (!GameManager.cercaPartita(tok.getIdUtente())) {
			return Response.status(Response.Status.NOT_FOUND)
					.entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_PARTITA_NON_TROVATA)).build();
		}
		
		int punteggio = GameManager.getPartita(tok.getIdUtente()).getPunteggio();
		JsonObject obj1 = new JsonObject();
		if (GameManager.checkAnswer(tok.getIdUtente(), corX, corY)) {
			obj1.addProperty(MESSAGGIO, RISPOSTA_CORRETTA);
			obj1.addProperty(PUNTEGGIO, GameManager.getPartita(tok.getIdUtente()).getPunteggio());
			obj1.addProperty("bool", true);
			System.out.println(obj1.toString());
			return Response.status(Response.Status.OK)
					.entity(obj1.toString()).build();
		}
		obj1.addProperty(MESSAGGIO, RISPOSTA_ERRATA);
		obj1.addProperty(PUNTEGGIO, punteggio);
		obj1.addProperty("bool", false);
		System.out.println(obj1.toString());
		return Response.status(Response.Status.OK)
				.entity(obj1.toString()).build();
	}

	@POST
	@Path("/getPartita")
	public Response getTurno(@Context HttpHeaders headers) {
		Response verifica = JwtToken.verificaToken(headers);
		if (verifica.getStatus() != 200)
			return verifica;
		Token tok = (Token) verifica.getEntity();

		if (GameManager.cercaPartita(tok.getIdUtente())) {
			JsonObject obj = new JsonObject();
			obj.addProperty(MESSAGGIO, SUCCESSO_PARTITA_TROVATA);
			obj.addProperty("punteggio", GameManager.getPartita(tok.getIdUtente()).getPunteggio());
			obj.addProperty("dimGriglia", GameManager.getPartita(tok.getIdUtente()).getTurno().getDimGriglia());
			obj.addProperty("corX", GameManager.getPartita(tok.getIdUtente()).getTurno().getCorX());
			obj.addProperty("corY", GameManager.getPartita(tok.getIdUtente()).getTurno().getCorY());
			obj.addProperty("colore", Strumenti.colorToHex(GameManager.getPartita(tok.getIdUtente()).getTurno().getColore()));
			obj.addProperty("coloreDiverso", Strumenti.colorToHex(GameManager.getPartita(tok.getIdUtente()).getTurno().getColoreDiverso()));
			System.out.println(obj.toString());
			return Response.status(Response.Status.OK)
					.entity(obj.toString()).build();
		}
		return Response.status(Response.Status.NOT_FOUND)
				.entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_PARTITA_NON_TROVATA)).build();
	}
	
	
	@GET
	@Path("/getClassifica")
	public Response getClassifica(@Context HttpHeaders headers) {
		List<RecordClassifica> classifica;
		try {
			classifica = Strumenti.getClassifica();

			if (classifica.isEmpty()) {
				return Response.status(Response.Status.NOT_FOUND)
						.entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_CLASSIFICA_VUOTA)).build();
			}
			return Response.status(Response.Status.OK).entity(new Gson().toJson(classifica)).build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_SQL + " " + e.getMessage())).build();
		}
	}

}
