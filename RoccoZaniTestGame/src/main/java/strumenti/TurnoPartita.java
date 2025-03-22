package strumenti;

import java.awt.Color;

public class TurnoPartita {
	private int dimGriglia;
	private int corX;
	private int corY;
	private Color colore;
	private Color coloreDiverso;

	public TurnoPartita(int punteggio) {
			this.dimGriglia = (punteggio+1)%4+(punteggio)/10;
			this.colore = randColor();
			this.coloreDiverso = randColorDiverso(colore, punteggio);
			this.corX = (int)(Math.random()*dimGriglia);
			this.corY = (int)(Math.random()*dimGriglia);	
		}

	public Color randColor() {
		int r = (int) (Math.random() * 256);
		int g = (int) (Math.random() * 256);
		int b = (int) (Math.random() * 256);
		return new Color(r, g, b);
	}

	public Color randColorDiverso(Color colore, int punteggio) {
		int r = colore.getRed();
		int g = colore.getGreen();
		int b = colore.getBlue();

		int adjustment = Math.max(1, 10 - punteggio);

		r = (r + (int) (Math.random() * adjustment * 2) - adjustment) % 256;
		g = (g + (int) (Math.random() * adjustment * 2) - adjustment) % 256;
		b = (b + (int) (Math.random() * adjustment * 2) - adjustment) % 256;

		r = Math.min(255, Math.max(0, r));
		g = Math.min(255, Math.max(0, g));
		b = Math.min(255, Math.max(0, b));

		return new Color(r, g, b);
	}

	public int getDimGriglia() {
		return dimGriglia;
	}

	public int getCorX() {
		return corX;
	}

	public int getCorY() {
		return corY;
	}

	public Color getColore() {
		return colore;
	}

	public Color getColoreDiverso() {
		return coloreDiverso;
	}

}
