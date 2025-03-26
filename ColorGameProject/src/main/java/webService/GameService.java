package webService;

import java.sql.SQLException;
import java.util.List;

import com.google.gson.Gson;

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
	public Response checkAnswer(@QueryParam(CORDX) int corX, @QueryParam(CORDY) int corY,
			@Context HttpHeaders headers) {
		Response verifica = JwtToken.verificaToken(headers);
		if (verifica.getStatus() != 200)
			return verifica;
		Token tok = (Token) verifica.getEntity();

		if (corX < 0 || corY < 0) {
			return Response.status(Response.Status.BAD_REQUEST)
					.entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_RICHIESTA_NON_VALIDA)).build();
		}

		if (GameManager.checkAnswer(tok.getIdUtente(), corX, corY)) {
			return Response.status(Response.Status.OK)
					.entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, RISPOSTA_CORRETTA)).build();
		}
		return Response.status(Response.Status.OK).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, RISPOSTA_ERRATA))
				.build();
	}

	@POST
	@Path("/getPartita")
	public Response getTurno(@Context HttpHeaders headers) {
		Response verifica = JwtToken.verificaToken(headers);
		if (verifica.getStatus() != 200)
			return verifica;
		Token tok = (Token) verifica.getEntity();

		if (GameManager.cercaPartita(tok.getIdUtente())) {
			return Response.status(Response.Status.OK)
					.entity(new Gson().toJson(GameManager.getPartita(tok.getIdUtente()))).build();
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
