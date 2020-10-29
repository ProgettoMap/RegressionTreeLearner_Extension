
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Classe principale dell'applicativo contentente il metodo iniziale del programma
 *
 */
public class MainClient extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        
        // All'avvio del programma leggo il file contenente i settings per connettersi al server
    	File f = ConnectionController.f;
        if(f.exists()) { // Se il file esiste, faccio partire il server con quei parametri.
            
            ArrayList<String> settings = ConnectionController.readSettingsFromFile();
            String ip = settings.get(ConnectionController.IP_POSITION_IN_SETTINGS);
            Integer port = new Integer(settings.get(ConnectionController.PORT_POSITION_IN_SETTINGS));

            try {
                CustomSocket.initSocket(ip, port);
            } catch (IOException e) {

                // Se non è stato possibile effettuare la connessione al server con le configurazioni già impostate,
                // viene aperta la schermata per la revisione dei parametri
                UtilityMethods.printError("Error Dialog", "Connection error",
                        "Cannot initialize the connection with the server.\n"
                        + "We'll redirect you now to the settings window, so you can check if the settings are correct.\n Detail error: " + e.toString());
                try {
                    UtilityMethods.open(stage,getClass(),"resources/connected.fxml", "Regression Tree Learner - Settings", "resources/image/favicon.png", 330, 345);
                } catch (IOException e1) {
                    UtilityMethods.printError("Error Dialog", "Input/Output Error",
                        "Something has gone wrong while executing the program.\nDetail Error: " + e1.toString());
                }            
                return;
            }
            // Se sono riuscito a connettermi alla socket, allora apro la schermata di home
            UtilityMethods.open(stage,getClass(),"resources/HomeScene.fxml", "Regression Tree Learner", "resources/image/favicon.png", 429, 300);
        } else { 
            // Se il file di configurazione non esiste (es. alla primissima apertura del programma) 
            // aprirò la schermata di configurazione iniziale
            UtilityMethods.open(stage,getClass(),"resources/connected.fxml", "Regression Tree Learner - Settings", "resources/image/favicon.png", 330, 345);
          
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