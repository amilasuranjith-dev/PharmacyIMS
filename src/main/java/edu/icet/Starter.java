package edu.icet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Starter extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Keep this path in sync with: src/main/resources/view/main-form.view
        URL location = getClass().getResource("/view/main-form.fxml");
        if (location == null) {
            throw new IllegalStateException(
                    "FXML not found at /view/main-form.fxml. " +
                    "Expected file: src/main/resources/view/main-form.fxml"
            );
        }

        Parent root = FXMLLoader.load(location);

        stage.setTitle("Pharmacy IMS");
        stage.setScene(new Scene(root, 900, 600));
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
