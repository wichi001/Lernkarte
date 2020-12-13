/**
 * 
 * @author Patrick Winter
 * @date 11.12.2019
 */


package pk.lkarten;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class TestLernkartei {
	
	public static void main (String[] args) {
		/* Testen des Menüs */
		Menu menu = new Menu();
		menu.startLernkartei();
		
		
		/* Testen von Serializable und druckAlleKarten mit beiden Kartentypen */
		try {
			Lernkartei lernkartei = new Lernkartei();
			
			Lernkarte eKarte1 = new EinzelantwortKarte("Objektorientierung", "Konstruktoren", "Welchem Zweck dient ein Konstruktor?", "Eine spezielle Methode zur Initialisierung von Objekten");
			Lernkarte eKarte2 = new EinzelantwortKarte("Objektorientierung", "Instanzen", "Welches Schlsselwort wird bentigt um eine Instanz zu erstellen?", "new");
		
			Lernkarte mKarte1 = new MehrfachantwortKarte("Schnittstellen", "Methoden in Schnittstellen", "Welche Arten von Methoden sind in Schnittstellen erlaubt?", 
					new String[] {"public abstract", "protected static", "package", "public default", "private"},
					new int[] {0, 3, 4});
			Lernkarte mKarte2 = new MehrfachantwortKarte("Objektorientierung", "Vererbung", "Welche Art von Vererbung hat Java?", 
					new String[] {"Einfachvererbung", "Doppelvererbung", "Mehrfachvererbung", "Klassenvererbung"},
					new int[] {0});
		
			lernkartei.hinzufuegen(eKarte1);
			lernkartei.hinzufuegen(eKarte2);
			lernkartei.hinzufuegen(mKarte1);
			lernkartei.hinzufuegen(mKarte2);
			
			lernkartei.speichern();
			Lernkartei copy = new Lernkartei();
			copy.laden();
			
			lernkartei.druckeAlleKarten(new FileOutputStream(new File("TestDruckeAlleKartenMitBeidenTypen.txt")));
			copy.druckeAlleKarten(new FileOutputStream(new File("Copy.txt")));
			
		} catch (IOException e) {
			System.out.println("Datei beideKartenTypen.csv bereits vorhanden");
		} catch (ClassNotFoundException e) {
			
		} catch (UngueltigeKarteException e) {
			System.out.println(e.getMessage());
		}
	}
}
