/**
 * @author Patrick Winter
 * @date 10.01.2020
 */


package pk.lkarten;


import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import java.nio.file.Files;
import java.nio.file.Path;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;


public class Lernkartei {
	
	private Set<Lernkarte> lernkartei;
	
	
	public Lernkartei() {
		this.lernkartei = new HashSet<>();
	}
	
	
	public void hinzufuegen (Lernkarte karte) throws UngueltigeKarteException {
		karte.validiere();
		lernkartei.add(karte);
	}
	
	public void druckeAlleKarten(OutputStream stream) throws IOException, NullPointerException {	
		ArrayList<Lernkarte> copyLernkartei = new ArrayList<>(lernkartei);
		Collections.sort(copyLernkartei);
		
		Iterator<Lernkarte> iterator = copyLernkartei.iterator();		
		
		while (iterator.hasNext()) {
			Lernkarte karte = iterator.next();
			karte.druckeKarte(stream);
		}
		
		stream.close();
	}
	
	public int getAnzahlKarten() {
		return lernkartei.size();
	}
	
	public Lernkarte[] gibKartenZuKategorie (String kategorie) {
		HashSet<Lernkarte> listKategorie = new HashSet<>();
		
		for (Lernkarte karte : lernkartei) {
			if (karte.getKategorie().equals(kategorie)) {
				listKategorie.add(karte);
			}
		}
		
		return listKategorie.toArray(new Lernkarte[listKategorie.size()]);
	}
	
	public Lernkarte[] erzeugeDeck (int anzahlKarten) {
		Lernkarte[] deck = new Lernkarte[anzahlKarten];
		ArrayList<Lernkarte> copyLernkartei = new ArrayList<>(lernkartei);
		
		Random random = new Random();
		
		for (int i = 0; i < deck.length; i++) {
			int id = random.nextInt(lernkartei.size());
			deck[i] = copyLernkartei.get(id);
		}
		 
		return deck;
	}
	
	public List<Lernkarte> getListe() {
		return new ArrayList<Lernkarte>(lernkartei);
	}
	
	public void exportiereEintraegeAlsCsvNio (File datei) throws IOException {
		Path path = datei.toPath();
		Files.createFile(path);
		
		ArrayList<Lernkarte> copy = new ArrayList<>(lernkartei);
		Collections.sort(copy);
		
		StringBuilder sb = new StringBuilder("ID,Kategorie,Titel,Frage,Antwort(en),Richtige Antwort(en)");
		
		Iterator<Lernkarte> iterator = copy.iterator();
		
		while (iterator.hasNext()) {
			Lernkarte karte = iterator.next();
			sb.append("\n");
			sb.append(karte.exportiereAlsCsv());
		}
		
		Files.write(path, sb.toString().getBytes());
	}
	
	public void exportiereEintraegeAlsCsv (File datei) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(datei)) {
			ArrayList<Lernkarte> copy = new ArrayList<>(lernkartei);
			Collections.sort(copy);
			
			StringBuilder sb = new StringBuilder("ID,Kategorie,Titel,Frage,Antwort(en),Richtige Antwort(en)");
			
			Iterator<Lernkarte> iterator = copy.iterator();
			
			while (iterator.hasNext()) {
				Lernkarte karte = iterator.next();
				sb.append("\n");
				sb.append(karte.exportiereAlsCsv());
			}
			
			fos.write(sb.toString().getBytes());
		} 
	}
	
	public void speichern() throws IOException {
		try (FileOutputStream fos = new FileOutputStream(new File("kartei.ser"));
				ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			ArrayList<Lernkarte> copyKartei = new ArrayList<>(lernkartei);
			Collections.sort(copyKartei);
			
			oos.writeObject(copyKartei);
		} catch (IOException e) {
			throw new IOException("Fehler beim Karteispeichern");
		}
	}
	
	public void laden() throws IOException, ClassNotFoundException {
		try (FileInputStream fis = new FileInputStream(new File("kartei.ser"));
				ObjectInputStream ois = new ObjectInputStream(fis)) {
			ArrayList<Lernkarte> copyKartei = (ArrayList<Lernkarte>) ois.readObject();
			
			for (Lernkarte karte : copyKartei) {
				String kategorie = karte.getKategorie();
				String titel = karte.getTitel();
				String frage = karte.getFrage();
				
				if (karte instanceof EinzelantwortKarte) {
					String antwort = ((EinzelantwortKarte) karte).getAntwort();
					
					lernkartei.add(new EinzelantwortKarte(kategorie, titel, frage, antwort));
				}
				
				if (karte instanceof MehrfachantwortKarte) {
					String[] moeglicheAntworten = ((MehrfachantwortKarte) karte).getMoeglicheAntworten();
					int[] richtigeAntworten = ((MehrfachantwortKarte) karte).getRichtigeAntworten();
					
					lernkartei.add(new MehrfachantwortKarte(kategorie, titel, frage, moeglicheAntworten, richtigeAntworten));
				}
			}
			
		} catch (IOException e) {
			throw new IOException("Fehler beim Kartei einlesen");
		} catch (ClassNotFoundException e) {
			throw new ClassNotFoundException("Klasse nicht gefunden!");
		}
	}
	
	public Iterator<Lernkarte> getIterator() {
		return lernkartei.iterator();
	}	
	
}