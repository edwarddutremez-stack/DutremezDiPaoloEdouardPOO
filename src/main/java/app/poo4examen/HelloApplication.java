package app.poo4examen;

import app.poo4examen.util.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneManager.setPrimaryStage(primaryStage);
        SceneManager.changerScene("AccueilView.fxml", "Bienvenue");
    }

    public static void main(String[] args) {
        launch(args);
    }
}