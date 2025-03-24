package webService;

import Interfacce.Messaggi;
import Interfacce.NomiParametri;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import strumenti.GameManager;
import strumenti.JwtToken;
import strumenti.Strumenti;

@Path("/rest/gioco")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GameService implements Messaggi, NomiParametri {
	@GET
	@Path("/checkPartita")
	public Response getPartita(@PathParam(IDUTENTE) String idUtente, @Context HttpHeaders headers) {
		String token = headers.getHeaderString(TOKEN);
		if (token == null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity(Strumenti.messaggioSempliceJSON(ERRORE, ERRORE_TOKEN_MANCANTE)).build();
		}
		if (JwtToken.verifyToken(token)==null) {
			return Response.status(Response.Status.UNAUTHORIZED).entity(Strumenti.messaggioSempliceJSON(ERRORE, ERRORE_TOKEN_NON_VALIDO)).build();
		}
		if(JwtToken.verifyToken(token)!=null) {
			return Response.status(Response.Status.OK).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, "Partita trovata")).build();
		}
		else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Strumenti.messaggioSempliceJSON(ERRORE, ERRORE_GENERICO)).build();
		}
		
	}

}
