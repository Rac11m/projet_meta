package inteface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;


public class MainPage extends Application {

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ressources/Page1.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1400, 820);
              scene.getStylesheets().add(getClass().getResource("ressources/application.css").toExternalForm());

            stage.setTitle("MKP!");
            stage.setScene(scene);
            stage.show();


            stage.setOnCloseRequest(event -> {
                event.consume();
                logout(stage);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logout(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Quitter");
        alert.setHeaderText("Vous êtes sur le point de quitter cette page!");
        alert.setContentText("Souhaitez-vous continuer ? ");

        if (alert.showAndWait().get() == ButtonType.OK) {
//            stage = (Stage) scenePane.getScene().getWindow();
            stage.close();
        }
    }
}
