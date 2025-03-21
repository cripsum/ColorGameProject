package webService;

import java.util.Map;

import Entita.Partita;
import Interfacce.Messaggi;
import Interfacce.NomiParametri;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("/rest/gioco")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GameService implements Messaggi, NomiParametri {


}

