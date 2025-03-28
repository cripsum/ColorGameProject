package webService;

import java.sql.SQLException;
import java.util.List;

import com.google.gson.Gson;

import Interfacce.Messaggi;
import Interfacce.NomiParametri;
import entita.Utente;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import strumenti.JwtToken;
import strumenti.JwtToken.Token;
import strumenti.Strumenti;
import strumenti.UtentePerAdmin;
@Path("/admin")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminService implements Messaggi, NomiParametri{

	@POST
	@Path("/addAdmin")
	public Response addAdmin(String uId, @Context HttpHeaders headers){
		Response verifica = JwtToken.verificaToken(headers);
		String risposta_affermativa="";
		String risposta_negativa="";
		if (verifica.getStatus() != 200)
			return verifica;
		Token tok = (Token) verifica.getEntity();
		if (!tok.getRuolo().equals("admin")) {
			return Response.status(Response.Status.UNAUTHORIZED).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_NON_AUTORIZZATO)).build();
		}
		Gson gson = new Gson();
		String[] utenteId = gson.fromJson(uId, String[].class);
		for (String string : utenteId) {
			try {
				if(Utente.setTipo(string, "admin")) {
					risposta_affermativa+=string+", ";
				}
				else {
					risposta_negativa+=string+", ";
				}
			} catch (SQLException e) {
				risposta_negativa+=string+"("+e.getMessage()+"), ";
			}
		}
		String risposta = "Utenti promossi a admin: "+risposta_affermativa+"/nUtenti non promossi a admin: "+risposta_negativa;
		return Response.ok().entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, risposta)).build();
	}
	
	@POST
	@Path("/removeAdmin")
	public Response removeAdmin(String uId, @Context HttpHeaders headers){
		Response verifica = JwtToken.verificaToken(headers);
		String risposta_affermativa="";
		String risposta_negativa="";
		if (verifica.getStatus() != 200)
			return verifica;
		Token tok = (Token) verifica.getEntity();
		if (!tok.getRuolo().equals("admin")) {
			return Response.status(Response.Status.UNAUTHORIZED).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, null)).build();
		}
		Gson gson = new Gson();
		String[] utenteId = gson.fromJson(uId, String[].class);
		for (String string : utenteId) {
			try {
				if(Utente.setTipo(string, "utente")) {
					risposta_affermativa+=string+", ";
				}
				else {
					risposta_negativa+=string+", ";
				}
			} catch (SQLException e) {
				risposta_negativa+=string+"("+e.getMessage()+"), ";
			}
		}
		String risposta = "Utenti declassati a user: "+risposta_affermativa+"/nUtenti non declassati a user: "+risposta_negativa;
		return Response.ok().entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, risposta)).build();
	}
	
	@POST
	@Path("/banUser")
	public Response banUser(String uId, @Context HttpHeaders headers){
		Response verifica = JwtToken.verificaToken(headers);
		String risposta_affermativa="";
		String risposta_negativa="";
		if (verifica.getStatus() != 200)
			return verifica;
		Token tok = (Token) verifica.getEntity();
		if (!tok.getRuolo().equals("admin")) {
			return Response.status(Response.Status.UNAUTHORIZED).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, null)).build();
		}
		Gson gson = new Gson();
		String[] utenteId = gson.fromJson(uId, String[].class);
		for (String string : utenteId) {
			try {
				if(Utente.setBannato(string, true)) {
					risposta_affermativa+=string+", ";
				}
				else {
					risposta_negativa+=string+", ";
				}
			} catch (SQLException e) {
				risposta_negativa+=string+"("+e.getMessage()+"), ";
			}
		}
		String risposta = "Utenti bannati: "+risposta_affermativa+"/nUtenti non bannati: "+risposta_negativa;
		return Response.ok().entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, risposta)).build();
	}
	
	@POST
	@Path("/unbanUser")
	public Response unbanUser(String uId, @Context HttpHeaders headers){
		Response verifica = JwtToken.verificaToken(headers);
		String risposta_affermativa="";
		String risposta_negativa="";
		if (verifica.getStatus() != 200)
			return verifica;
		Token tok = (Token) verifica.getEntity();
		if (!tok.getRuolo().equals("admin")) {
			return Response.status(Response.Status.UNAUTHORIZED).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, null)).build();
		}
		Gson gson = new Gson();
		String[] utenteId = gson.fromJson(uId, String[].class);
		for (String string : utenteId) {
			try {
				if(Utente.setBannato(string, false)) {
					risposta_affermativa+=string+", ";
				}
				else {
					risposta_negativa+=string+", ";
				}
			} catch (SQLException e) {
				risposta_negativa+=string+"("+e.getMessage()+"), ";
			}
		}
		String risposta = "Utenti sbannati: "+risposta_affermativa+"/nUtenti non sbannati: "+risposta_negativa;
		return Response.ok().entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, risposta)).build();
	}
	
	@GET
	@Path("/getUsers")
	public Response getUsers(@Context HttpHeaders headers){
		Response verifica = JwtToken.verificaToken(headers);
		if (verifica.getStatus() != 200)
			return verifica;
		Token tok = (Token) verifica.getEntity();
		if (!tok.getRuolo().equals("admin")) {
			return Response.status(Response.Status.UNAUTHORIZED).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, null)).build();
		}
		try {
			List<UtentePerAdmin> utenti;
			utenti = Strumenti.getUtenti();
			return Response.ok().entity(new Gson().toJson(utenti).toString()).build();
		} catch (SQLException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Strumenti.messaggioSempliceJSON(MESSAGGIO, ERRORE_SQL+" "+e.getMessage())).build();
		}
	}
	
}
