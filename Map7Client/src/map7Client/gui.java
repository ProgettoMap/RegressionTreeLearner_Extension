package map7Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class gui extends Application {

    @Override
    public void start(Stage stage) throws Exception {

		  Parent root = FXMLLoader.load(getClass().getResource("resources/first_scene.fxml"));

		  Scene scene = new Scene(root);

		  stage.setScene(scene);
		  stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}