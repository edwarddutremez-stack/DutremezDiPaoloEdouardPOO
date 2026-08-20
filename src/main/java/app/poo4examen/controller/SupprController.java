package app.poo4examen.controller;

import app.poo4examen.model.JsonDataManager;
import app.poo4examen.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.ArrayList;

public class SupprController {

    @FXML private Button btnValiderSuppr;
    @FXML private Button btnAnnulerSuppr;

    @FXML
    public void initialize() {
        btnValiderSuppr.setOnAction(e -> {
            // MULTITHREADING pour la suppression
            Thread threadSuppr = new Thread(() -> {
                JsonDataManager.sauvegarderTaches(new ArrayList<>());
                javafx.application.Platform.runLater(() ->
                        SceneManager.changerScene("TachesView.fxml", "Organisation des tâches")
                );
            });
            threadSuppr.setDaemon(true);
            threadSuppr.start();
        });

        btnAnnulerSuppr.setOnAction(e ->
                SceneManager.changerScene("TachesView.fxml", "Organisation des tâches")
        );
    }
}