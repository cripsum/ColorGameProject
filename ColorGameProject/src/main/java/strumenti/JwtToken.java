package strumenti;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

public abstract class JwtToken implements Interfacce.Messaggi, Interfacce.NomiParametri {
	private static final String key = "siuum";
	
	public static String generateToken(String idUtente, String ruolo, String dataDiRegistrazione) {
		String token = JWT.create()
				.withClaim("idUtente", idUtente)
				.withClaim("ruolo", ruolo)
				.withClaim("dataDiRegistrazione", dataDiRegistrazione)
				.withIssuedAt(new Date())
				.withExpiresAt(new Date(System.currentTimeMillis() + 1*ChronoUnit.DAYS.getDuration().toMillis()))
				
				.sign(Algorithm.HMAC256(key));
		
		return token;
	}
	
	public static Token verifyToken(String token) {
		DecodedJWT jwt = null;
		try {
			jwt = JWT.require(Algorithm.HMAC256(key)).build().verify(token);
			String idUtente = jwt.getClaim("idUtente").asString();
			String ruolo = jwt.getClaim("ruolo").asString();
			String dataDiRegistrazione = jwt.getClaim("dataDiRegistrazione").asString();
			Token a = new Token(idUtente, ruolo, dataDiRegistrazione);

			return a;
		} catch (JWTVerificationException e) {
			return null;
		}
	}
	
	public static Response verificaToken(HttpHeaders headers) {
		String token = headers.getHeaderString(TOKEN);
		if (token == null) {
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(Strumenti.messaggioSempliceJSON(ERRORE, ERRORE_TOKEN_MANCANTE)).build();
		}
		Token tok = JwtToken.verifyToken(token);
		if (tok == null) {
			return Response.status(Response.Status.UNAUTHORIZED)
					.entity(Strumenti.messaggioSempliceJSON(ERRORE, ERRORE_TOKEN_NON_VALIDO)).build();
		}
		return Response.ok(tok).build();
	}
	
	public static class Token {
		private String idUtente;
		private String ruolo;
		private String dataDiRegistrazione;
		
		public Token(String idUtente, String ruolo, String dataDiRegistrazione) {
			this.idUtente = idUtente;
			this.ruolo = ruolo;
			this.dataDiRegistrazione = dataDiRegistrazione;
		}
		
		public String getIdUtente() {
			return idUtente;
		}
		
		public String getRuolo() {
			return ruolo;
		}
		
		public String getDataDiRegistrazione() {
			return dataDiRegistrazione;
		}
	}
}
