package webService;

import Interfacce.Messaggi;
import Interfacce.NomiParametri;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("/rest/gioco")
public class GiocoAPI implements Messaggi, NomiParametri {
	@GET
	@Path("/hello")
    @Produces(MediaType.APPLICATION_JSON)
    public String sayHello() {
        return "Ciao, il tuo servizio REST funziona!";
    }
	
	@POST
	@Path("/getTurno")
	@Produces(MediaType.APPLICATION_JSON)
	public String getTurno(@PathParam(PUNTEGGIO)int id, @PathParam("idPartita")String idPartita) {
		
		return "Turno 1";
	}
}
