package app.poo4examen.controller;

import app.poo4examen.model.Etat;
import app.poo4examen.model.JsonDataManager;
import app.poo4examen.model.Tache;
import app.poo4examen.util.SceneManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.stream.Collectors;

public class TachesController {

    @FXML private ProgressBar progressBar;
    @FXML private TableView<TacheRow> tableTaches;
    @FXML private TableColumn<TacheRow, String> colToDo;
    @FXML private TableColumn<TacheRow, String> colDoing;
    @FXML private TableColumn<TacheRow, String> colDone;

    @FXML private Button btnAjouter;
    @FXML private Button btnModifier;
    @FXML private Button btnSupprimerTout;

    private static Tache tacheSelectionnee = null;

    public static Tache getTacheSelectionnee() {
        return tacheSelectionnee;
    }

    @FXML
    public void initialize() {
        colToDo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getToDo()));
        colDoing.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDoing()));
        colDone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDone()));

        chargerEtAfficherDonnees();

        btnAjouter.setOnAction(e -> {
            tacheSelectionnee = null;
            SceneManager.changerScene("AjoutTacheView.fxml", "Ajout d'une nouvelle tâche");
        });

        btnModifier.setOnAction(e -> {
            TacheRow row = tableTaches.getSelectionModel().getSelectedItem();
            if (row != null && row.getTacheOriginale() != null) {
                tacheSelectionnee = row.getTacheOriginale();
                SceneManager.changerScene("ModifTacheView.fxml", "Modification d'une tâche");
            }
        });

        btnSupprimerTout.setOnAction(e ->
                SceneManager.changerScene("SupprimerToutView.fxml", "Suppression de toutes les tâches")
        );
    }

    private void chargerEtAfficherDonnees() {
        List<Tache> liste = JsonDataManager.chargerTaches();

        // Utilisation des STREAMS & LAMBDAS pour trier et filtrer
        List<Tache> listToDo = liste.stream()
                .filter(t -> t.getEtat() == Etat.TO_DO)
                .sorted((t1, t2) -> Integer.compare(t1.getPriorite(), t2.getPriorite()))
                .collect(Collectors.toList());

        List<Tache> listDoing = liste.stream()
                .filter(t -> t.getEtat() == Etat.DOING)
                .collect(Collectors.toList());

        List<Tache> listDone = liste.stream()
                .filter(t -> t.getEtat() == Etat.DONE)
                .collect(Collectors.toList());

        int maxSize = Math.max(listToDo.size(), Math.max(listDoing.size(), listDone.size()));
        var rows = FXCollections.<TacheRow>observableArrayList();

        for (int i = 0; i < maxSize; i++) {
            Tache tDo = i < listToDo.size() ? listToDo.get(i) : null;
            Tache tDoing = i < listDoing.size() ? listDoing.get(i) : null;
            Tache tDone = i < listDone.size() ? listDone.get(i) : null;

            rows.add(new TacheRow(
                    tDo != null ? tDo.getNom() + " (" + tDo.getPersonne() + ")" : "",
                    tDoing != null ? tDoing.getNom() + " (" + tDoing.getPersonne() + ")" : "",
                    tDone != null ? tDone.getNom() + " (" + tDone.getPersonne() + ")" : "",
                    tDo != null ? tDo : (tDoing != null ? tDoing : tDone)
            ));
        }

        tableTaches.setItems(rows);

        // Mettre à jour la ProgressBar via un Thread séparé
        new Thread(() -> {
            long total = liste.size();
            long faites = liste.stream().filter(t -> t.getEtat() == Etat.DONE).count();
            double ratio = (total == 0) ? 0.0 : (double) faites / total;

            javafx.application.Platform.runLater(() -> progressBar.setProgress(ratio));
        }).start();
    }

    public static class TacheRow {
        private final String toDo;
        private final String doing;
        private final String done;
        private final Tache tacheOriginale;

        public TacheRow(String toDo, String doing, String done, Tache tacheOriginale) {
            this.toDo = toDo;
            this.doing = doing;
            this.done = done;
            this.tacheOriginale = tacheOriginale;
        }

        public String getToDo() { return toDo; }
        public String getDoing() { return doing; }
        public String getDone() { return done; }
        public Tache getTacheOriginale() { return tacheOriginale; }
    }
}