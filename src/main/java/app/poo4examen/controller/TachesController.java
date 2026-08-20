package app.poo4examen.controller;

import app.poo4examen.model.Etat;
import app.poo4examen.model.JsonDataManager;
import app.poo4examen.model.Tache;
import app.poo4examen.util.SceneManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
        colToDo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getToDoText()));
        colDoing.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDoingText()));
        colDone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDoneText()));

        rafraichirTableau();

        btnAjouter.setOnAction(e -> {
            tacheSelectionnee = null;
            SceneManager.changerScene("AjoutTacheView.fxml", "Ajout d'une nouvelle tâche");
        });

        // Modification : Récupère la tâche selon la colonne cliquée/sélectionnée
        btnModifier.setOnAction(e -> {
            TacheRow row = tableTaches.getSelectionModel().getSelectedItem();
            if (row != null) {
                // Détermine la colonne actuellement sélectionnée
                TablePosition<?, ?> pos = tableTaches.getSelectionModel().getSelectedCells().isEmpty()
                        ? null : tableTaches.getSelectionModel().getSelectedCells().get(0);

                if (pos != null) {
                    int col = pos.getColumn();
                    if (col == 0) tacheSelectionnee = row.getTacheToDo();
                    else if (col == 1) tacheSelectionnee = row.getTacheDoing();
                    else if (col == 2) tacheSelectionnee = row.getTacheDone();
                }

                // Fallback si la colonne n'est pas identifiée directement
                if (tacheSelectionnee == null) {
                    tacheSelectionnee = row.getTachePremierDisponible();
                }

                if (tacheSelectionnee != null) {
                    SceneManager.changerScene("ModifTacheView.fxml", "Modification d'une tâche");
                }
            }
        });

        btnSupprimerTout.setOnAction(e ->
                SceneManager.changerScene("SupprimerToutView.fxml", "Suppression de toutes les tâches")
        );
    }

    private void rafraichirTableau() {
        List<Tache> liste = JsonDataManager.chargerTaches();

        // 1. Mise à jour ProgressBar
        long total = liste.size();
        long faites = liste.stream().filter(t -> t.getEtat() == Etat.DONE).count();
        double ratio = (total == 0) ? 0.0 : (double) faites / total;
        progressBar.setProgress(ratio);

        // 2. Séparation par état
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

            rows.add(new TacheRow(tDo, tDoing, tDone));
        }

        tableTaches.setItems(rows);
    }

    // Classe interne ajustée pour porter les 3 tâches de la ligne séparément
    public static class TacheRow {
        private final Tache tacheToDo;
        private final Tache tacheDoing;
        private final Tache tacheDone;

        public TacheRow(Tache tacheToDo, Tache tacheDoing, Tache tacheDone) {
            this.tacheToDo = tacheToDo;
            this.tacheDoing = tacheDoing;
            this.tacheDone = tacheDone;
        }

        public String getToDoText() {
            return tacheToDo != null ? tacheToDo.getNom() + " (" + tacheToDo.getPersonne() + ")" : "";
        }

        public String getDoingText() {
            return tacheDoing != null ? tacheDoing.getNom() + " (" + tacheDoing.getPersonne() + ")" : "";
        }

        public String getDoneText() {
            return tacheDone != null ? tacheDone.getNom() + " (" + tacheDone.getPersonne() + ")" : "";
        }

        public Tache getTacheToDo() { return tacheToDo; }
        public Tache getTacheDoing() { return tacheDoing; }
        public Tache getTacheDone() { return tacheDone; }

        public Tache getTachePremierDisponible() {
            if (tacheToDo != null) return tacheToDo;
            if (tacheDoing != null) return tacheDoing;
            return tacheDone;
        }
    }
}