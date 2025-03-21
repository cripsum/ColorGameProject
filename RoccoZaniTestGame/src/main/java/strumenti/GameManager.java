package strumenti;

import java.awt.Color;
import java.util.Map;

import Entita.Partita;
import Interfacce.Messaggi;
import Interfacce.NomiParametri;

public abstract class GameManager implements NomiParametri, Messaggi {
	private Map<Partita,Turno>partiteInCorso;
	
	public boolean cercaPartita(String idUtente) {
		for(Partita p: partiteInCorso.keySet()) {
			if(p.getIdUtente().equals(idUtente)) {
				return true;
			}
		}
		return false;
	}
	
	public void aggiunhiPartita(String idUtente) {
		if(!cercaPartita(idUtente)) {
			Partita p = new Partita(idUtente);
			Turno t = new Turno(0, 0, 0, Color.BLACK, Color.WHITE);
			partiteInCorso.put(p, t);
		}
	}
	
	class Turno {
		private int dimGriglia;
		private int corX;
		private int corY;
		private Color colore;
		private Color coloreDiverso;
		
		public Turno(int dimGriglia, int corX, int corY, Color colore, Color coloreDiverso) {
			this.dimGriglia = dimGriglia;
			this.corX = corX;
			this.corY = corY;
			this.colore = colore;
			this.coloreDiverso = coloreDiverso;
		}
		
		public Turno newTurno(int punteggio) {
			dimGriglia = (punteggio+1)%4+(punteggio)/10;
			colore = randColor();
			return null;
		}
		
		public Color randColor() {
			int r = (int)(Math.random()*256);
			int g = (int)(Math.random()*256);
			int b = (int)(Math.random()*256);
			return new Color(r, g, b);
		}
		public Color randColorDiverso(Color colore, int punteggio) {
			return null;
			
		}
		
		public int getDimGriglia() {
			return dimGriglia;
		}

		public void setDimGriglia(int dimGriglia) {
			this.dimGriglia = dimGriglia;
		}

		public int getCorX() {
			return corX;
		}

		public void setCorX(int corX) {
			this.corX = corX;
		}

		public int getCorY() {
			return corY;
		}

		public void setCorY(int corY) {
			this.corY = corY;
		}

		public Color getColore() {
			return colore;
		}

		public void setColore(Color colore) {
			this.colore = colore;
		}

		public Color getColoreDiverso() {
			return coloreDiverso;
		}

		public void setColoreDiverso(Color coloreDiverso) {
			this.coloreDiverso = coloreDiverso;
		}
	}
}
