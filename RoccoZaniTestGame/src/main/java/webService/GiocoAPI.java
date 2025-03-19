package webService;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;


@Path("/gioco")
public class GiocoAPI {
	@GET
	@Path("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String sayHello() {
        return "Ciao, il tuo servizio REST funziona!";
    }
}
