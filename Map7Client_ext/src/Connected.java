
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Connected extends Application {

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

                // TODO: modificare
                Parent root;
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/settings.fxml"));
                    root = loader.load();
                    Stage stage2 = new Stage();
                    stage2.setTitle("Regression Tree Learner - Settings");
                    stage2.setScene(new Scene(root));
                    stage2.show();
                    
                    SettingsController settingsctlr = (SettingsController) loader.getController();
                    settingsctlr.loadSettings();
                }
                
                catch (IOException e1) {
                    e.printStackTrace();
                }

                try {
                    CustomSocket.closeSocketIfOpened();
                } catch (IOException e1) {
                    UtilityMethods.printError("Error Dialog", "Socket error", "Socket has not been closed correctly");
                }
                
                return;
            }
        

            Parent tableViewParent = FXMLLoader.load(getClass().getResource("resources/first_scene.fxml"));
            Scene scene = new Scene(tableViewParent);
            stage.setTitle("Regression Tree Learner");
            stage.setScene(scene);
            stage.show(); 
           
        } else {
            Parent root = new FXMLLoader(getClass().getResource("resources/connected.fxml")).load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Regression Tree Learner - Settings");
            stage.show(); 
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}