package app.poo4examen.controller;

import app.poo4examen.model.Etat;
import app.poo4examen.model.JsonDataManager;
import app.poo4examen.model.Tache;
import app.poo4examen.util.SceneManager;
import javafx.collections.FXCollections;
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

    @FXML
    public void initialize() {
        cbPriorite.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5));

        Tache tacheAEditer = TachesController.getTacheSelectionnee();

        // Pré-remplissage si modification, sinon valeurs par défaut
        if (tacheAEditer != null) {
            txtNom.setText(tacheAEditer.getNom());
            txtPersonne.setText(tacheAEditer.getPersonne());
            cbPriorite.setValue(tacheAEditer.getPriorite());
            if (tacheAEditer.getEtat() == Etat.TO_DO) rbToDo.setSelected(true);
            else if (tacheAEditer.getEtat() == Etat.DOING) rbDoing.setSelected(true);
            else if (tacheAEditer.getEtat() == Etat.DONE) rbDone.setSelected(true);
        } else {
            cbPriorite.getSelectionModel().selectFirst();
            rbToDo.setSelected(true);
        }

        btnValider.setOnAction(e -> {
            List<Tache> liste = JsonDataManager.chargerTaches();
            Etat nouvelEtat = rbDone.isSelected() ? Etat.DONE : (rbDoing.isSelected() ? Etat.DOING : Etat.TO_DO);

            if (tacheAEditer != null) {
                // Retrouver la tâche dans la liste chargée par son nom / personne ou référence
                for (Tache t : liste) {
                    if (t.getNom().equals(tacheAEditer.getNom()) && t.getPersonne().equals(tacheAEditer.getPersonne())) {
                        t.setNom(txtNom.getText());
                        t.setPersonne(txtPersonne.getText());
                        t.setEtat(nouvelEtat);
                        t.setPriorite(cbPriorite.getValue());
                        break;
                    }
                }
            } else {
                // Nouvelle tâche
                liste.add(new Tache(txtNom.getText(), txtPersonne.getText(), nouvelEtat, cbPriorite.getValue()));
            }

            JsonDataManager.sauvegarderTaches(liste);
            SceneManager.changerScene("TachesView.fxml", "Organisation des tâches");
        });

        btnAnnuler.setOnAction(e ->
                SceneManager.changerScene("TachesView.fxml", "Organisation des tâches")
        );
    }
}