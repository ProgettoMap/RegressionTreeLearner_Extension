
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainClient extends Application {

    /*
    TODO List:
        - Aggiustare tutti i pannelli, ancorarli e renderli responsive
        - Gestione di tutte le eccezioni
        - Commentare codice
        - Rifattorizzazione metodi
        - Estrazione metodi utility
        - CustomSocket
        - Aggiustare Log
    */

    @Override
    public void start(Stage stage) throws Exception {
        
        File f = new File(SettingsController.settingsPath);
        if(f.exists()) { // Se il file esiste, faccio partire il server con quei parametri.
            
            ArrayList<String> settings = SettingsController.readSettingsFromFile();
            String ip = settings.get(0);
            Integer port = new Integer(settings.get(1));

            try {
                CustomSocket.initSocket(ip, port);
            } catch (IOException e) {
                UtilityMethods.printError("Error Dialog", "Connection error",
                        "Cannot initialize the connection with the server. Detail error: " + e.toString());

                Parent root;
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/SettingsScene.fxml"));
                    root = loader.load();
                    Stage stage2 = new Stage();
                    stage2.setTitle("Regression Tree Learner - Settings");
                    stage2.setScene(new Scene(root));
                    stage2.show();
                    
                    SettingsController settingsctlr = (SettingsController) loader.getController();
                    settingsctlr.loadSettings();
                } catch (IOException e1) {
                    e.printStackTrace();
                }

                CustomSocket.closeSocketIfOpened();               
                return;
            }

            Parent tableViewParent = FXMLLoader.load(getClass().getResource("resources/HomeScene.fxml"));
            Scene scene = new Scene(tableViewParent);
            stage.setTitle("Regression Tree Learner");
            stage.getIcons().add(new Image("resources/favicon.png"));
            
            stage.setScene(scene);
            stage.show(); 

        } else {
            Parent root = new FXMLLoader(getClass().getResource("resources/connected.fxml")).load();
            Scene scene = new Scene(root);
            stage.setTitle("Regression Tree Learner - Settings");
            stage.setScene(scene);
            stage.show(); 
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}