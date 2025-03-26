package strumenti;

import java.util.ArrayList;
import java.util.List;

import Interfacce.Messaggi;
import Interfacce.NomiParametri;
import entita.Partita;

public abstract class GameManager implements NomiParametri, Messaggi {
	private static List<Partita> partiteInCorso=new ArrayList<Partita>();
	
	public static boolean cercaPartita(String idUtente) {
        for (Partita p : partiteInCorso) {
            if (p.getIdUtente().equals(idUtente)) {
                return true;
            }
        }
        return false;
    }
	
	public static void aggiungiPartita(String idUtente) {
        if (!cercaPartita(idUtente)) {
            Partita p = new Partita(idUtente);
            p.newTurno();
            partiteInCorso.add(p);
        }
        else {
			for (Partita p : partiteInCorso) {
				if (p.getIdUtente().equals(idUtente)) {
					finePartita(idUtente);
					aggiungiPartita(idUtente);
				}
			}
		}
    }
	
	public static boolean checkAnswer(String idUtente, int x, int y) {
		for (Partita p : partiteInCorso) {
			if (p.getIdUtente().equals(idUtente)) {
				if (p.getTurno().getCorX() == x && p.getTurno().getCorY() == y) {
					p.setPunteggio(p.getPunteggio() + 1);
					p.newTurno();
					return true;
				}
				else {
					finePartita(idUtente);
					return false;
				}
			}
		}
		return false;
	}
	
	public static  void finePartita(String idUtente) {
		for (Partita p : partiteInCorso) {
			if (p.getIdUtente().equals(idUtente)) {
				p.saveOnDB();
				partiteInCorso.remove(p);
			}
		}
	}
	
	public static Partita getPartita(String idUtente) {
		for (Partita p : partiteInCorso) {
			if (p.getIdUtente().equals(idUtente)) {
				return p;
			}
		}
		return null;
	}
	
}
