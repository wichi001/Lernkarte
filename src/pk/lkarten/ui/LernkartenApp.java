package pk.lkarten.ui;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pk.lkarten.EinzelantwortKarte;
import pk.lkarten.Lernkarte;
import pk.lkarten.Lernkartei;
import pk.lkarten.MehrfachantwortKarte;
import pk.lkarten.UngueltigeKarteException;
import pk.lkarten.ui.util.DialogUtil;

public class LernkartenApp extends Application {
	private Lernkartei lernkartei;
	private ObservableList<Lernkarte> liste;

	@Override
	public void start(Stage primaryStage) {
		lernkartei = new Lernkartei();

		liste = FXCollections.<Lernkarte>observableArrayList();

		BorderPane rootPane = new BorderPane(new ListView<Lernkarte>(liste));
		rootPane.setTop(createMenuBar(primaryStage));

//		Button lernenButton = new Button("Lernen!");
//		Spinner<Integer> spinner = new Spinner<Integer>(5, 15, 5);
//		lernenButton.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//				Lernkarte[] deck = lernkartei.erzeugeDeck(spinner.getValue());
//				for (var karte : deck) {
//					if (karte != null) {
//						DialogUtil.showTextDialog(karte.getTitel(), karte.getKategorie(), karte.zeigeVorderseite(),
//								"Rückseite zeigen");
//						DialogUtil.showTextDialog(karte.getTitel(), karte.getKategorie(), karte.zeigeRueckseite(),
//								"Nächste Karte zeigen");
//					}
//				}
//			}
//		});
//
//		HBox hBox = new HBox(5.0, lernenButton, spinner);
//		hBox.setPadding(new Insets(5.0));
//		hBox.setAlignment(Pos.CENTER);
//		rootPane.setBottom(hBox);

		primaryStage.setTitle("Lernkarten-App");
		primaryStage.setScene(new Scene(rootPane, 500, 300));
		primaryStage.show();
	}

	private MenuBar createMenuBar(Stage primaryStage) {
		MenuBar bar = new MenuBar();

		Menu datei = new Menu("Datei");
		MenuItem ladenItem = new MenuItem("Laden");
		ladenItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					lernkartei.laden();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Iterator<Lernkarte> it = lernkartei.getIterator();
				while (it.hasNext()) {
					liste.add(it.next());
				}
			}
		});

		MenuItem speichernItem = new MenuItem("Speichern");
		speichernItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					lernkartei.speichern();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		MenuItem exportItem = new MenuItem("CSV-Export");
		exportItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					lernkartei.exportiereEintraegeAlsCsv(erfrageDateiname(primaryStage));
				} catch (IOException e) {
					DialogUtil.showErrorDialog("Fehlermeldung", "Fehler beim CSV-Export: " + e.getMessage());
				}
			}
		});

		MenuItem beendenItem = new MenuItem("Beenden");
		beendenItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Platform.exit();
			}
		});

		datei.getItems().addAll(ladenItem, speichernItem, new SeparatorMenuItem(), exportItem, new SeparatorMenuItem(),
				beendenItem);
		bar.getMenus().add(datei);

		Menu eintrag = new Menu("Lernkartei");
		MenuItem eakNeuItem = new MenuItem("Einzelantwortkarte hinzufügen");
		eakNeuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				EinzelantwortKarteErfassungView eakew = new EinzelantwortKarteErfassungView(primaryStage,
						new EinzelantwortKarte(null,null,null,null), lernkartei,liste);
				eakew.showView();
			}
		});

		MenuItem makNeuItem = new MenuItem("Mehrfachantwortkarte hinzufügen");
		makNeuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				MehrfachantwortKarteErfassungView makew = new MehrfachantwortKarteErfassungView(primaryStage,
						new MehrfachantwortKarte(null,null,null,null,null), lernkartei,liste);
				makew.showView();
			}
		});

		eintrag.getItems().addAll(eakNeuItem, makNeuItem);
		bar.getMenus().add(eintrag);

		return bar;
	}

	private File erfrageDateiname(Stage primaryStage) {
		boolean ok = false;
		File file = null;
		do {
			String name = DialogUtil.showInputDialog("Eingabe", "Bitte geben Sie einen Dateinamen ein: ");
			if (name == null || name.isEmpty()) {
				DialogUtil.showMessageDialog("Fehlermeldung", "Leerer Dateiname, bitte Dateiname eingeben!");
				continue;
			}

			file = new File(name);
			if (file.exists() && !DialogUtil.showConfirmDialog("Datei existiert",
					"Datei existiert bereits und wird überschrieben - trotzdem fortfahren?")) {
				continue;
			}

			ok = true;
		} while (!ok);
		return file;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
