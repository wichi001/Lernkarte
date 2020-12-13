package pk.lkarten.ui;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import pk.lkarten.EinzelantwortKarte;
import pk.lkarten.Lernkarte;
import pk.lkarten.Lernkartei;

public class EinzelantwortKarteErfassungView extends ErfassungView {
	private EinzelantwortKarte karte;

	private TextArea antwortArea;

	public EinzelantwortKarteErfassungView(Stage primaryStage, EinzelantwortKarte karte, Lernkartei lernkartei,
			ObservableList<Lernkarte> liste) {
		super(primaryStage, karte, lernkartei, liste);
		this.karte = karte;
	}

	@Override
	public void showView() {
		setTitle("Erfassung einer Einzelantwortkarte");

		Label antwortLabel = new Label("Antwort:");
		this.antwortArea = new TextArea(karte != null ? karte.getAntwort() : "");
		GridPane.setHalignment(antwortLabel, HPos.RIGHT);
		GridPane.setHgrow(antwortArea, Priority.ALWAYS);
		GridPane.setVgrow(antwortArea, Priority.ALWAYS);
		super.getGridPane().add(antwortLabel, 0, 3);
		super.getGridPane().add(antwortArea, 1, 3, 2, 1);

		super.showView();
	}

	@Override
	protected void onNeuAction() {
		karte.setAntwort(antwortArea.getText());
		super.onNeuAction();
	}
}

