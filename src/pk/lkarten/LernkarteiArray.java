/**
 * 
 * @author Patrick Winter
 * @date 05.12.2019
 */


package pk.lkarten;


import java.util.Random;
import java.io.IOException;


public class LernkarteiArray {
	
	private Lernkarte[] lernkartei;
	
	
	public LernkarteiArray(int maxKarten) {
		this.lernkartei = new Lernkarte[maxKarten];
	}
	
	
	public void hinzufuegen(Lernkarte karte) {
		int aktuellerIndex = getFreienKarteiPlatz();
		
		if (aktuellerIndex == -1) {
			System.out.println("Karte konnte nicht hinzugefgt werden! Kartei bereits voll!");
		} else {
			lernkartei[aktuellerIndex] = karte;
		}
	}
	
	public void druckeAlleKarten() throws IOException {
		for (Lernkarte karte : lernkartei) {
			if (karte != null) {
				karte.druckeKarte(null);
			}
		}
	}
	
	public int getAnzahlKarten() {
		int anzahlKarten = 0;
		
		for (Lernkarte karte : lernkartei) {
			if (karte != null) {
				anzahlKarten++;
			}
		}
		
		return anzahlKarten;
	}
	
	public Lernkarte[] gibKartenZuKategorie(String kategorie) {
		int belegt = 0;
		
		for (Lernkarte karte : lernkartei) {
			if (karte != null && karte.getKategorie().equals(kategorie)) {
				belegt++;
			}
		}
		
		Lernkarte[] kategorieKarten = new Lernkarte[belegt];
		int index = 0;
		
		for (Lernkarte karte : lernkartei) {
			if (karte != null && karte.getKategorie().equals(kategorie)) {
				kategorieKarten[index] = karte;
				index++;
			}
		}
		
		return kategorieKarten;
	}
	
	public Lernkarte[] erzeugeDeck(int anzahlKarten) {
		Lernkarte[] deck = new Lernkarte[anzahlKarten];
		Random random = new Random();
		
		for (int i = 0; i < deck.length; i++) {
			int id = random.nextInt(Lernkarte.anzahlKarten) + 1;
			
			for (Lernkarte karte : lernkartei) {
				if (karte.getID() == id) {
					deck[i] = karte;
					break;
				}
			}
		}
		
		return deck;
	}
	
	
	private int getFreienKarteiPlatz() {
		int index = -1;
		
		for (int i = 0; i < lernkartei.length; i++) {
			if (lernkartei[i] == null) {
				return i;
			}
		}
		
		return index;
	}
	
}
