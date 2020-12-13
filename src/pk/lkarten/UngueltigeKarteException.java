/**
 * @author Patrick Winter
 * @date 19.11.19
 */


package pk.lkarten;


import java.util.ArrayList;


public class UngueltigeKarteException extends Exception {

	private ArrayList<String> fehler;
	
	
	public UngueltigeKarteException() {
		super();
		this.fehler = new ArrayList<>();
	}
	
	public void addFehler (String fehler) {
		this.fehler.add(fehler);
	}
	
	public String getFehlerAusgabe() {
		String ausgabe = "";
		
		for (String str : fehler) {
			ausgabe += "* " + str + "\n";
		}
		
		return ausgabe;
	}
}
