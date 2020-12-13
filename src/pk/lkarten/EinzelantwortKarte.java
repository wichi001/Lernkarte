/**
 * 
 * @author Patrick Winter
 * @date 07.01.2020
 */


package pk.lkarten;


import java.io.OutputStream;
import java.io.Serializable;
import java.io.IOException;


public class EinzelantwortKarte extends Lernkarte implements CsvExportable, Serializable {
	
	private static final long serialVersionUID = -1L;
	private String antwort;
	
	
	public EinzelantwortKarte (String kategorie, String titel, String frage, String antwort) {
		super(kategorie, titel, frage);
		this.antwort = antwort;
	}
	
	public String getAntwort() {
		return antwort;
	}
	
	
	@Override
	public String zeigeVorderseite() {
		StringBuilder sb = new StringBuilder("[" + getID() + ", " + getKategorie() + "] " + getTitel());
		sb.append("\n" + getFrage() + "\n");
		
		return sb.toString();
	}
	
	@Override
	public String zeigeRueckseite() {
		StringBuilder sb = new StringBuilder("\t" + this.antwort + "\n");
		
		return sb.toString();
	}
	
	@Override
	public void zeigeRueckseite(OutputStream stream) throws IOException, NullPointerException {
		StringBuilder sb = new StringBuilder("\t" + this.antwort + "\n");
		
		try {
			stream.write(sb.toString().getBytes());
		} catch (IOException e) {
			throw new IOException("Fehler EinzelantwortKarte zeigeRueckseite()");
		} catch (NullPointerException e) {
			throw new NullPointerException("Keinen OutputStream übergeben (EinzelantwortKarte zeigeRueckseite)");
		} finally {
			System.out.println(sb.toString());
		}		
	}
	
	@Override
	public void validiere() throws UngueltigeKarteException {
		super.validiere();
		UngueltigeKarteException ex = this.getUngueltigeKarteException();
		
		if (antwort == null || antwort.isEmpty()) {
			ex.addFehler("Keine gueltige Antwort!");
		}
		
		if (!ex.getFehlerAusgabe().isEmpty()) {
			throw ex;
		}
	}
	
	@Override
	public String exportiereAlsCsv() {
		String exportString = super.exportiereAlsCsv();
		
		return exportString + "," + antwort;
	}
	
	@Override
	public String toString() {
		return super.toString() + "antwort=" + antwort + "]";
	}

	public void setAntwort(String antwort) {
		this.antwort = antwort;
	}
	
	
}