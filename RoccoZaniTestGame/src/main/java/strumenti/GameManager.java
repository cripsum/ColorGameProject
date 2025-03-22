package strumenti;

import java.util.ArrayList;
import java.util.List;


import Entita.Partita;
import Interfacce.Messaggi;
import Interfacce.NomiParametri;

public abstract class GameManager implements NomiParametri, Messaggi {
	private List<Partita> partiteInCorso=new ArrayList<Partita>();
	
	public boolean cercaPartita(String idUtente) {
        for (Partita p : partiteInCorso) {
            if (p.getIdUtente().equals(idUtente)) {
                return true;
            }
        }
        return false;
    }
	
	public void aggiungiPartita(String idUtente) {
        if (!cercaPartita(idUtente)) {
            Partita p = new Partita(idUtente);
            p.newTurno();
            partiteInCorso.add(p);
        }
    }
	
	public void checkAnswer(String idUtente, int x, int y) {
		for (Partita p : partiteInCorso) {
			if (p.getIdUtente().equals(idUtente)) {
				if (p.getTurno().getCorX() == x && p.getTurno().getCorY() == y) {
					p.setPunteggio(p.getPunteggio() + 1);
					p.newTurno();
				}
				else {
					finePartita(idUtente);
				}
			}
		}
	}
	
	public void finePartita(String idUtente) {
		for (Partita p : partiteInCorso) {
			if (p.getIdUtente().equals(idUtente)) {
				p.saveOnDB();
				partiteInCorso.remove(p);
			}
		}
	}
	
}
