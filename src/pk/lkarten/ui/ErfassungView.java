package pk.lkarten.ui;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pk.lkarten.Lernkarte;
import pk.lkarten.Lernkartei;
import pk.lkarten.UngueltigeKarteException;
import pk.lkarten.ui.util.DialogUtil;

public abstract class ErfassungView extends Stage {
	private Lernkarte karte;
	private Lernkartei lernkartei;
	private ObservableList<Lernkarte> liste;

	private TextField kategorieField;
	private TextField titelField;
	private TextField frageField;
	private GridPane gp;

	public ErfassungView(Stage primaryStage, Lernkarte karte, Lernkartei lernkartei, ObservableList<Lernkarte> liste) {
		this.initOwner(primaryStage);
		this.initModality(Modality.WINDOW_MODAL);
		this.karte = karte;
		this.lernkartei = lernkartei;
		this.liste = liste;
		this.gp = new GridPane();
	}

	public void showView() {
		kategorieField = new TextField(karte != null ? karte.getKategorie() : "");
		titelField = new TextField(karte != null ? karte.getTitel() : "");
		frageField = new TextField(karte != null ? karte.getFrage() : "");

		BorderPane bp = new BorderPane();
		gp.setPadding(new Insets(10.0));
		gp.setHgap(5.0);
		gp.setVgap(10.0);
		ColumnConstraints con1 = new ColumnConstraints();
		con1.setPercentWidth(20);
		ColumnConstraints con2 = new ColumnConstraints();
		con2.setPercentWidth(60);
		ColumnConstraints con3 = new ColumnConstraints();
		con3.setPercentWidth(20);
		gp.getColumnConstraints().addAll(con1, con2, con3);

		Label kategorieLabel = new Label("Kategorie:");
		GridPane.setHalignment(kategorieLabel, HPos.RIGHT);
		GridPane.setHgrow(kategorieLabel, Priority.NEVER);
		GridPane.setHgrow(kategorieField, Priority.ALWAYS);
		gp.add(kategorieLabel, 0, 0);
		gp.add(kategorieField, 1, 0, 2, 1);

		Label titelLabel = new Label("Titel:");
		GridPane.setHalignment(titelLabel, HPos.RIGHT);
		GridPane.setHgrow(titelLabel, Priority.NEVER);
		GridPane.setHgrow(titelField, Priority.ALWAYS);
		gp.add(titelLabel, 0, 1);
		gp.add(titelField, 1, 1, 2, 1);

		Label frageLabel = new Label("Frage:");
		GridPane.setHalignment(frageLabel, HPos.RIGHT);
		GridPane.setHgrow(frageLabel, Priority.NEVER);
		GridPane.setHgrow(frageField, Priority.ALWAYS);
		gp.add(frageLabel, 0, 2);
		gp.add(frageField, 1, 2, 2, 1);

		bp.setCenter(gp);

		Button ok = new Button("OK");
		ok.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				onNeuAction();
			}
		});
		Button abbrechen = new Button("Abbrechen");
		abbrechen.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ErfassungView.this.close();
			}
		});

		FlowPane fp = new FlowPane();
		fp.setAlignment(Pos.CENTER);
		fp.setPadding(new Insets(20.0));
		fp.setHgap(20.0);

		fp.getChildren().addAll(ok, abbrechen);
		bp.setBottom(fp);

		Scene scene = new Scene(bp);
		setScene(scene);

		showAndWait();
	}

	protected void onNeuAction() {
		karte.setKategorie(kategorieField.getText());
		karte.setTitel(titelField.getText());
		karte.setFrage(frageField.getText());

		try {
			lernkartei.hinzufuegen(karte);
			liste.add(karte);
			this.close();
		} catch (UngueltigeKarteException exp) {
			DialogUtil.showErrorDialog("Ungültige Karte: ", exp.getFehlerAusgabe());
		}
	}

	protected GridPane getGridPane() {
		return this.gp;
	}

	protected Lernkarte getLernkarte() {
		return this.karte;
	}
}
