package pk.lkarten.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import pk.lkarten.Lernkarte;
import pk.lkarten.Lernkartei;
import pk.lkarten.MehrfachantwortKarte;

public class MehrfachantwortKarteErfassungView extends ErfassungView {
	private MehrfachantwortKarte karte;
	private List<TextArea> antwortAreas;
	private List<CheckBox> antwortCheckBoxes;

	public MehrfachantwortKarteErfassungView(Stage primaryStage, MehrfachantwortKarte karte, Lernkartei lernkartei,
			ObservableList<Lernkarte> liste) {
		super(primaryStage, karte, lernkartei, liste);
		this.karte = karte;
		this.antwortAreas = new ArrayList<>(5);
		this.antwortCheckBoxes = new ArrayList<>(5);
	}

	@Override
	public void showView() {
		setTitle("Erfassung einer Mehrfachantwortkarte");

		String[] antworten = (karte != null && karte.getMoeglicheAntworten() != null) ? karte.getMoeglicheAntworten()
				: new String[0];
		int[] richtigAntworten = (karte != null && karte.getRichtigeAntworten() != null) ? karte.getRichtigeAntworten()
				: new int[0];

		for (int i = 0; i < 5; i++) { // es werden 5 Antwortfelder erzeugt
			Label antwortLabel = new Label("Antwort " + (i + 1) + ":");
			GridPane.setHalignment(antwortLabel, HPos.RIGHT);

			String antwort = (antworten.length > i && antworten[i] != null) ? antworten[i] : "";
			TextArea antwortArea = new TextArea(antwort);
			GridPane.setHgrow(antwortArea, Priority.ALWAYS);
			GridPane.setVgrow(antwortArea, Priority.ALWAYS);
			CheckBox checkBox = new CheckBox("Richtig?");
			checkBox.setSelected(istRichtigeAntwort(i, richtigAntworten));
			super.getGridPane().addRow(i + 3, antwortLabel, antwortArea, checkBox);
			// Controls fuer das spaetere Auslesen merken
			antwortAreas.add(antwortArea);
			antwortCheckBoxes.add(checkBox);
		}

		super.showView();
	}

	private boolean istRichtigeAntwort(int nummer, int[] richtigeAntworten) {
		for (int antwortNr : richtigeAntworten) {
			if (antwortNr == nummer) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onNeuAction() {
		karte.setMoeglicheAntworten(leseAntwortAreas());
		karte.setRichtigeAntworten(leseAntwortCheckBoxes());
		super.onNeuAction();
	}

	private String[] leseAntwortAreas() {
		List<String> antworten = new ArrayList<String>(5);
		for (TextArea antwort : antwortAreas) {
			String text = antwort.getText();
			if (text != null && !text.isBlank()) {
				antworten.add(antwort.getText());
			}
		}
		return antworten.toArray(new String[antworten.size()]);
	}

	private int[] leseAntwortCheckBoxes() {
		List<Integer> antworten = new ArrayList<Integer>(5);
		for (int i = 0; i < antwortCheckBoxes.size(); i++) {
			CheckBox box = antwortCheckBoxes.get(i);
			if (box.isSelected()) {
				antworten.add(i);
			}
		}
		// Sehr unelegant ohne Streams :-(
		int[] ergebnis = new int[antworten.size()];
		for (int i = 0; i < antworten.size(); i++) {
			ergebnis[i] = antworten.get(i);
		}
		return ergebnis;
	}
}
