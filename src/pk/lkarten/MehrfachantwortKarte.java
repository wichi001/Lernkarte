/**
 * 
 * @author Patrick Winter
 * @date 10.01.2020
 */


package pk.lkarten;

import java.util.Arrays;
import java.io.IOException;
import java.io.OutputStream;


public class MehrfachantwortKarte extends Lernkarte {
	
	private static final long serialVersionUID = -1L;
	
	private String[] moeglicheAntworten;
	private int[] richtigeAntworten;
	
	
	public MehrfachantwortKarte(String kategorie, String titel, String frage, String[] moeglicheAntworten, int[] richtigeAntworten) {
		super(kategorie, titel, frage);
		this.moeglicheAntworten = moeglicheAntworten;
		this.richtigeAntworten = richtigeAntworten;
	}
	
	
	public String[] getMoeglicheAntworten() {
		return moeglicheAntworten;
	}
	
	public int[] getRichtigeAntworten() {
		return richtigeAntworten;
	}
	
	
	public void setMoeglicheAntworten(String[] moeglicheAntworten) {
		this.moeglicheAntworten = moeglicheAntworten;
	}


	public void setRichtigeAntworten(int[] richtigeAntworten) {
		this.richtigeAntworten = richtigeAntworten;
	}


	@Override
	public  String zeigeVorderseite() {
		StringBuilder sb = new StringBuilder("[" + getID() + ", " + getKategorie() + "] " + getTitel());
		sb.append("\n" + getFrage() + "\n");
		
		for (int i = 0; i < moeglicheAntworten.length; i++) {
			sb.append("\t" + (i + 1) + ". " + moeglicheAntworten[i] + "\n");
		}
		
		if (richtigeAntworten.length > 1) {
			sb.append("(mehrere Antworten möglich!)\n");
		}
		
		return sb.toString();
	}
	
	@Override
	public String zeigeRueckseite() {
		StringBuilder sb = new StringBuilder("Die richtige(n) Antwort(en) ist/sind:\n");
		
		for (int i = 0; i < richtigeAntworten.length; i++) {
			sb.append("\t" + (richtigeAntworten[i] + 1) + ": " + (moeglicheAntworten[richtigeAntworten[i]]) + "\n");
		}
		
		return sb.toString();
	}
	
	@Override
	public void zeigeVorderseite(OutputStream stream) throws IOException, NullPointerException {	
		super.zeigeVorderseite(stream);		
		StringBuilder sb = new StringBuilder();
		
		try {
			for (int i = 0; i < moeglicheAntworten.length; i++) {
				sb.append("\t" + (i + 1) + ". " + moeglicheAntworten[i] + "\n");
			}
			
			if (richtigeAntworten.length > 1) {
				sb.append("(mehrere Antworten möglich!\n");
			}
			
			stream.write(sb.toString().getBytes());
		} catch (IOException e) {
			throw new IOException("Fehler Vorderseite MehrfachantwortKarte");
		} catch (NullPointerException e) {
			throw new NullPointerException("Keinen OutputStream übergeben (Mehrfachkarte zeigeVorderseite)");
		} finally {
			System.out.print(sb.toString());			
		}
	}
	
	@Override
	public void zeigeRueckseite(OutputStream stream) throws IOException, NullPointerException {
		StringBuilder sb = new StringBuilder("Die richtige(n) Antwort(en) ist/sind:\n");
		
		try {
			for (int i = 0; i < richtigeAntworten.length; i++) {
				sb.append("\t" + (richtigeAntworten[i] + 1) + ": "  + moeglicheAntworten[richtigeAntworten[i]] + "\n");
			}
			
			stream.write(sb.toString().getBytes());
		} catch (IOException e) {
			throw new IOException("Fehler Rueckseite MehrfachantwortKarte");
		} catch (NullPointerException e) {
			throw new NullPointerException("Keinen OutputStream übergeben (Mehrfachkarte zeigeRueckseite)");
		} finally {
			System.out.println(sb.toString());
		}
	}
	
	@Override
	public void validiere() throws UngueltigeKarteException {
		super.validiere();
		UngueltigeKarteException ex = getUngueltigeKarteException();
		
		if (moeglicheAntworten.length < 2) {
			ex.addFehler("Zu wenig Antwortmöglichkeiten!");
		} else {
			for (String ant : moeglicheAntworten) {
				if (ant == null || ant.isEmpty()) {
					ex.addFehler("Keine gueltige Antwort angegeben!");
					break;
				}
			}
		}
		
		if (!ex.getFehlerAusgabe().isEmpty()) {
			throw ex;
		}
	}
	
	@Override
	public String exportiereAlsCsv() {
		String exportString = super.exportiereAlsCsv();
		
		return exportString + "," + Arrays.toString(moeglicheAntworten) + "," + Arrays.toString(richtigeAntworten);
	}
	
	@Override
	public String toString() {
		return super.toString() + "antworten=" + Arrays.toString(moeglicheAntworten) + ",richtigeAntworten=" + Arrays.toString(richtigeAntworten) + "]";
	}
}