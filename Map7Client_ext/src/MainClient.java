
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
        - Gestione di tutte le eccezioni (sopratutto cercare quelle printStackTrace)
        - Commentare codice
        - Rifattorizzazione metodi
        - Estrazione metodi utility
        - Aggiungere guide
        - Alla fine confrontare se necessario i due progetti server        
    */


    @Override
    public void start(Stage stage) throws Exception {
        
        // All'avvio del programma leggo il file contenente i settings per connettersi al server
        File f = new File(ConnectionController.settingsPath);
        if(f.exists()) { // Se il file esiste, faccio partire il server con quei parametri.
            
            ArrayList<String> settings = ConnectionController.readSettingsFromFile();
            String ip = settings.get(0);
            Integer port = new Integer(settings.get(1));

            try {
                CustomSocket.initSocket(ip, port);
            } catch (IOException e) {

                // Se non è stato possibile effettuare la connessione al server con le configurazioni già impostate,
                // viene aperta la schermata per la revisione dei parametri
                
                UtilityMethods.printError("Error Dialog", "Connection error",
                        "Cannot initialize the connection with the server.\n"
                        + "We'll redirect you now to the settings window, so you can check if the settings are correct.\n Detail error: " + e.toString());
                
                Parent root;
                try { //TODO: come vogliamo gestire i commenti sul FXML?
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/connected.fxml"));
                    root = loader.load();
                    Stage stage2 = new Stage();
                    stage2.setTitle("Regression Tree Learner - Settings");
                    stage2.getIcons().add(new Image("resources/favicon.png"));
                    stage.setMinHeight(345);
                    stage.setMinWidth(330);
                    stage2.setScene(new Scene(root));
                    stage2.show();
                    
                } catch (IOException e1) {
                    UtilityMethods.printError("Error Dialog", "Input/Output Error",
                        "Something has gone wrong while executing the program.\nDetail Error: " + e1.toString());
                }            
                return;
            }

            // Se sono riuscito a connettermi alla socket, allora apro la schermata di home

            Parent tableViewParent = FXMLLoader.load(getClass().getResource("resources/HomeScene.fxml"));
            Scene scene = new Scene(tableViewParent);
            stage.setTitle("Regression Tree Learner");
            stage.getIcons().add(new Image("resources/favicon.png"));
            stage.setOnCloseRequest(event -> { // Quando clicco sul pulsante di chiusura della schermata principale, chiuderò la socket (se aperta)
                CustomSocket.closeSocketIfOpened(CustomSocket.getIstance());
            });
            stage.setMinHeight(266);
            stage.setMinWidth(429);
            stage.setScene(scene);
            stage.show();

        } else { 
            // Se il file di configurazione non esiste (es. alla primissima apertura del programma) 
            // aprirò la schermata di configurazione iniziale
            Parent root = new FXMLLoader(getClass().getResource("resources/connected.fxml")).load();
            Scene scene = new Scene(root);
            stage.setTitle("Regression Tree Learner - Settings");
            stage.getIcons().add(new Image("resources/favicon.png"));
            stage.setMinWidth(330);
            stage.setMinHeight(345);
            stage.setScene(scene);
            stage.show(); 
        }
    }
  
    /**
     * @param args valori di default
     * 
     * Funzione di avvio del programma
     */
    public static void main(String[] args) {
        launch(args);
    }
}