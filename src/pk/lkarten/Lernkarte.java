/**
 * 
 * @author Patrick Winter
 * @date 10.01.2020
 */


package pk.lkarten;


import java.util.Objects;

import java.io.OutputStream;
import java.io.IOException;
import java.io.Serializable;


public abstract class Lernkarte implements ValidierbareKarte, CsvExportable, Comparable<Lernkarte>, Serializable {
	
	private static final long serialVersionUID = -1L;

	public static int anzahlKarten = 0;
	
	private transient final int id;
	
	private String kategorie;
	private String titel;
	private String frage;
	
	private UngueltigeKarteException ungueltigeKarteException;
	
	
	public Lernkarte(String kategorie, String titel, String frage) {
		this.id = ++anzahlKarten;
		this.kategorie = kategorie;
		this.titel = titel;
		this.frage = frage;
		ungueltigeKarteException = new UngueltigeKarteException();
	}
	
	
	public int getID() {
		return this.id;
	}
	
	public String getKategorie() {
		return this.kategorie;
	}
	
	public String getTitel() {
		return titel;
	}
	
	public String getFrage() {
		return frage;
	}
	
	
	public static int getAnzahlKarten() {
		return anzahlKarten;
	}


	public static void setAnzahlKarten(int anzahlKarten) {
		Lernkarte.anzahlKarten = anzahlKarten;
	}


	public void setKategorie(String kategorie) {
		this.kategorie = kategorie;
	}


	public void setTitel(String titel) {
		this.titel = titel;
	}


	public void setFrage(String frage) {
		this.frage = frage;
	}


	public void setUngueltigeKarteException(UngueltigeKarteException ungueltigeKarteException) {
		this.ungueltigeKarteException = ungueltigeKarteException;
	}


	public UngueltigeKarteException getUngueltigeKarteException() {
		return ungueltigeKarteException;
	}
	
	
	public abstract String zeigeVorderseite();
	
	public abstract String zeigeRueckseite();	
	
	public abstract void zeigeRueckseite(OutputStream stream) throws IOException;
		
	
	public void zeigeVorderseite(OutputStream stream) throws IOException, NullPointerException {		
		StringBuilder sb = new StringBuilder("[" + this.id + ", " + this.kategorie + "] " + this.titel);
		sb.append("\n" + this.frage + "\n");
		
		try {
			
			stream.write(sb.toString().getBytes());
		} catch (IOException e) {
			throw new IOException("Vorderseitefehler!");
		} catch (NullPointerException e) {
			throw new NullPointerException("Keinen OutputStream übergeben (Lernkarte zeigeVorderseite)");
		} finally {
			System.out.print(sb.toString());
		}		
	}
	
	public void druckeKarte(OutputStream stream) throws IOException, NullPointerException {
		this.zeigeVorderseite(stream);
		this.zeigeRueckseite(stream);
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(kategorie, titel, frage);
	}
	
	@Override
	public boolean equals (Object other) {
		if (other instanceof Lernkarte) {
			Lernkarte karte = (Lernkarte) other;
			
			if (karte.hashCode() == this.hashCode()) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public int compareTo(Lernkarte otherKarte) {
		if (id < otherKarte.getID()) {
			return -1;
		} else if (id > otherKarte.getID()) {
			
		}
		
		return 0;
	}
	
	@Override
	public void validiere() throws UngueltigeKarteException {		
		if (kategorie == null || kategorie.isEmpty()) {
			ungueltigeKarteException.addFehler("Keine gueltige Kategorie angegeben!");
		}
		
		if (titel == null || titel.isEmpty()) {
			ungueltigeKarteException.addFehler("Keinen gueltigen Titel angegeben!");
		}
		
		if (frage == null || frage.isEmpty()) {
			ungueltigeKarteException.addFehler("Keine gueltige Frage angegeben!");
		}
	}
	
	@Override
	public String exportiereAlsCsv() {
		return id + "," + kategorie + "," + titel + "," + frage;
	}
	
	@Override
	public String toString() {
		return "Lernkarte [id=" + id + ",kategorie=" + kategorie + ",titel=" + titel + ",frage=" + frage +",";
	}
	
}
