package app.poo4examen.controller;

import app.poo4examen.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AccueilController {

    @FXML
    private Button btnTaches;

    @FXML
    public void initialize() {
        btnTaches.setOnAction(event ->
                SceneManager.changerScene("TachesView.fxml", "Organisation des tâches")
        );
    }
}