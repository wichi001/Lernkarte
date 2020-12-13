/**
 * @author Patrick Winter
 * @date 11.12.2019
 */


package pk.lkarten;


import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Menu {
	
	private Lernkartei lernkartei;
	private Scanner scanner;
	
	public Menu() {
		this.lernkartei = new Lernkartei();
		this.scanner = new Scanner(System.in);
	}
	
	
	private String eingabeKategorie() {
		String kategorie = JOptionPane.showInputDialog(null, "Bitte geben Sie eine Kategorie ein: ");
		
		return kategorie;
	}
	
	private String eingabeTitel() {
		String titel = JOptionPane.showInputDialog(null, "Geben Sie einen Titel ein: ");
		
		return titel;
	}
	
	private String eingabeFrage() {
		String frage = JOptionPane.showInputDialog(null, "Geben Sie eine Frage ein: ");
		
		return frage;
	}
	
	private String eingabeAntwort() {
		String antwort = JOptionPane.showInputDialog(null, "Geben Sie die Antwort ein: ");
		
		return antwort;
	}
	
	private void warteAufEnter() {
		try {
			System.in.read(new byte[2]);
		} catch (IOException e) {
			System.err.println("Fehler: " + e.getMessage());
		} 
	}
	
	private int printMenu() {		
		System.out.println();
		System.out.println("Lernkarten-App");
		System.out.println("1. Lernen!");
		System.out.println("2. Einzelantwortkarte hinzufügen");
		System.out.println("3. Drucke alle Karten");
		System.out.println("4. Drucke Karten zu Kategorie");
		System.out.println("5. CSV-Export");
		System.out.println("6. Lade aus Datei");
		System.out.println("7. Speichere in Datei");
		System.out.println("8. Beenden");
		System.out.print("Bitte Aktion wählen: ");
		
		int auswahl = Integer.parseInt(scanner.nextLine());
		
		return auswahl;
	}
	
	private void druckeKategorie() throws IOException, NullPointerException {
		String kategorie = eingabeKategorie();
		Lernkarte[] arrKategorie = lernkartei.gibKartenZuKategorie(kategorie);
		FileOutputStream fos = new FileOutputStream(new File(kategorie + ".txt"));
		
		if (arrKategorie.length == 0) {
			System.out.println("Kategorie nicht vorhanden!");
		} else {
			for (Lernkarte karte : arrKategorie) {
				karte.druckeKarte(fos);
			}
		}
	}
	
	private void einzelantwortKarteHinzufuegen() {
		try {
			String kategorie = eingabeKategorie();
			String titel = eingabeTitel();
			String frage = eingabeFrage();
			String antwort = eingabeAntwort();
		
			Lernkarte karte = new EinzelantwortKarte(kategorie, titel, frage, antwort);
			lernkartei.hinzufuegen(karte);
		} catch (UngueltigeKarteException ex) {
			JOptionPane.showMessageDialog(null, ex.getFehlerAusgabe());
		}
		
	}
	
	private void lernen() throws IOException, NullPointerException {
		Lernkarte[] deck = lernkartei.erzeugeDeck(5);
		FileOutputStream fos = new FileOutputStream(new File("DruckeRandomDeck.txt"));
		
		for (Lernkarte karte : deck) {
			karte.zeigeVorderseite(fos);
			System.out.println("<Drücken Sie Enter, um die Rückseite der Karte zu sehen.>");
			warteAufEnter();
			karte.zeigeRueckseite(fos);
			System.out.println("<Drücken Sie Enter, um die nächste Karte zu sehen.>");
			warteAufEnter();
			System.out.println();
		}	
		
		System.out.println("<Alle Karten betrachtet.>");
	}
	
	private void exportiereEintraege() throws IOException {
		String dateiname = JOptionPane.showInputDialog("Geben Sie einen Dateinamen ein:");
		
		if (dateiname.isEmpty() || dateiname == null) {
			JOptionPane.showMessageDialog(null, "Keinen Dateinamen eingegeben, bitte versuchen Sie es nochmal");
			exportiereEintraege();
		}
		
		dateiname += ".csv";
		
		File datei = new File(dateiname);
		if (datei.exists()) {
			int ueberschreiben = JOptionPane.showConfirmDialog(null, "Datei existiert bereits! Soll die Datei überschrieben werden?");
			
			if (ueberschreiben == 0) {
				datei.delete();
				datei = new File(dateiname);
			} else if (ueberschreiben == 1) {
				exportiereEintraege();
			} else {
				return;
			}
		} 
		
		lernkartei.exportiereEintraegeAlsCsv(datei);
	}	
	
	public void startLernkartei() {
		try {
			int auswahl = printMenu();  
			
			while (auswahl != 8) {
				switch (auswahl) {
					case 1:
						lernen();
						break;
					case 2:
						einzelantwortKarteHinzufuegen();
						break;					
					case 3:
						lernkartei.druckeAlleKarten(new FileOutputStream(new File("AusgabeAlleKarten.txt")));
						break;
					case 4:
						druckeKategorie();				
						break;
					case 5:
						exportiereEintraege();						
						break;
					case 6:
						lernkartei.laden();
						break;
					case 7:
						lernkartei.speichern();
						break;
					case 8:
						break;
					default:
						throw new NumberFormatException();							
				}
			
				auswahl = printMenu();
			}	
			
		} catch (NumberFormatException e) {
			System.out.println("Ungültige Eingabe!");
			startLernkartei();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Fehler beim Datenschreiben!");
			startLernkartei();
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
			startLernkartei();
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
			startLernkartei();
		}	finally {		
			scanner.close();
		}
	}
	
}
