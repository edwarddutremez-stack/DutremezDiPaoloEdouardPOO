package app.poo4examen.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {

    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static Object changerScene(String fxmlFile, String titre) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    SceneManager.class.getResource("/app/poo4examen/views/" + fxmlFile)
            );
            Parent root = loader.load();
            primaryStage.setTitle(titre);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            return loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}