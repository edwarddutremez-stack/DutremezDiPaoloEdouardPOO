package app.poo4examen.controller;

import app.poo4examen.model.Etat;
import app.poo4examen.model.JsonDataManager;
import app.poo4examen.model.Tache;
import app.poo4examen.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class FormTacheController {

    @FXML private TextField txtNom;
    @FXML private TextField txtPersonne;
    @FXML private RadioButton rbToDo;
    @FXML private RadioButton rbDoing;
    @FXML private RadioButton rbDone;
    @FXML private ComboBox<Integer> cbPriorite;
    @FXML private Button btnValider;
    @FXML private Button btnAnnuler;

    private Tache tacheEnEdition = null;

    @FXML
    public void initialize() {
        cbPriorite.getItems().addAll(1, 2, 3, 4, 5);
        cbPriorite.getSelectionModel().selectFirst();

        tacheEnEdition = TachesController.getTacheSelectionnee();
        if (tacheEnEdition != null) {
            txtNom.setText(tacheEnEdition.getNom());
            txtPersonne.setText(tacheEnEdition.getPersonne());
            cbPriorite.setValue(tacheEnEdition.getPriorite());

            if (tacheEnEdition.getEtat() == Etat.TO_DO) rbToDo.setSelected(true);
            else if (tacheEnEdition.getEtat() == Etat.DOING) rbDoing.setSelected(true);
            else if (tacheEnEdition.getEtat() == Etat.DONE) rbDone.setSelected(true);
        } else {
            rbToDo.setSelected(true);
        }

        btnValider.setOnAction(e -> enregistrer());
        btnAnnuler.setOnAction(e -> SceneManager.changerScene("TachesView.fxml", "Organisation des tâches"));
    }

    private void enregistrer() {
        String nom = txtNom.getText();
        String personne = txtPersonne.getText();
        int priorite = cbPriorite.getValue() != null ? cbPriorite.getValue() : 1;

        Etat etat = Etat.TO_DO;
        if (rbDoing.isSelected()) etat = Etat.DOING;
        else if (rbDone.isSelected()) etat = Etat.DONE;

        final Etat etatFinal = etat;

        // MULTITHREADING pour l'enregistrement JSON
        Thread threadSauvegarde = new Thread(() -> {
            List<Tache> liste = JsonDataManager.chargerTaches();

            if (tacheEnEdition != null) {
                liste.removeIf(t -> t.getNom().equalsIgnoreCase(tacheEnEdition.getNom()));
            }

            liste.add(new Tache(nom, personne, etatFinal, priorite));
            JsonDataManager.sauvegarderTaches(liste);

            javafx.application.Platform.runLater(() ->
                    SceneManager.changerScene("TachesView.fxml", "Organisation des tâches")
            );
        });

        threadSauvegarde.setDaemon(true);
        threadSauvegarde.start();
    }
}