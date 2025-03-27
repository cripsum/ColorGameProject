package strumenti;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import Interfacce.Messaggi;
import Interfacce.NomiParametri;
import entita.Partita;

public abstract class GameManager implements NomiParametri, Messaggi {
    private static Map<String, Partita> partiteInCorso = new HashMap<>();

    public static boolean cercaPartita(String idUtente) {
        return partiteInCorso.containsKey(idUtente); 
    }

    public static void aggiungiPartita(String idUtente) {
        if (!cercaPartita(idUtente)) {
            Partita p = new Partita(idUtente);
            p.newTurno();
            partiteInCorso.put(idUtente, p); 
        } else {
            finePartita(idUtente); 
            aggiungiPartita(idUtente); 
        }
    }

    public static boolean checkAnswer(String idUtente, int x, int y) {
        Partita p = partiteInCorso.get(idUtente);
        if (p != null) {
            if (p.getTurno().getCorX() == x && p.getTurno().getCorY() == y) {
                p.setPunteggio(p.getPunteggio() + 1);
                p.newTurno();
                return true;
            } else {
                finePartita(idUtente);
                return false;
            }
        }
        return false;
    }

    public static void finePartita(String idUtente) {
        Partita p = partiteInCorso.remove(idUtente); 
        if (p != null) {
        	String idPartita = strumenti.Strumenti.generaId();
    		String sqlQuery = "INSERT INTO partita (" + DB_IDPARTITA + ", " + DB_DATAEORAINIZIO + ", " + DB_DATAEORAFINE + ", " + DB_IDUTENTE + ", " + DB_PUNTEGGIO + ") VALUES (?, ?, ?, ?, ?)";
    		try (Connection conn = DBmanager.getConnection();PreparedStatement pstmt = conn.prepareStatement(sqlQuery)) {
    			pstmt.setString(1, idPartita);
    			pstmt.setString(2, p.getDataEOraInizio().toString());
    			pstmt.setString(3, LocalDateTime.now().toString());
    			pstmt.setString(4, idUtente);
    			pstmt.setInt(5, p.getPunteggio());
    			pstmt.executeUpdate();
    			System.out.println("partita inserita");
    		}
    		catch (SQLException e) {
    			System.out.println(e.getMessage());
    		}
            System.out.println(p.getIdPartita());
        }
    }

    public static Partita getPartita(String idUtente) {
        return partiteInCorso.get(idUtente); 
    }
}