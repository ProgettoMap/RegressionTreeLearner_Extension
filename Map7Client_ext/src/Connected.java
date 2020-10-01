
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Connected extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        
        String path = "src/resources/settings.bin";
        File f = new File(path);
        if(f.exists()) { // Se il file esiste, faccio partire il server con quei parametri.
            try(BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
                String line = bufferedReader.readLine();
                int k = 0;
                String ip = "";
                Integer port = 0;
                while(line != null) {
                    if (k == 0)
                        ip = line;
                    else 
                        port = new Integer(line);
                    
                    line = bufferedReader.readLine();
                    k++;
                }
                
                CustomSocket.initSocket(ip, port);
            }

            Parent tableViewParent = FXMLLoader.load(getClass().getResource("resources/first_scene.fxml"));
            Scene scene = new Scene(tableViewParent);
            stage.setScene(scene);
            stage.show(); 
           
        } else {
            Parent root = new FXMLLoader(getClass().getResource("resources/connected.fxml")).load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show(); 
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}