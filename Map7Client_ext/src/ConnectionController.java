import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Classe di tipo controller per la gestione del primo avvio del programma
 */
public class ConnectionController {

    @FXML
    private Button btnConnected;
    @FXML
    private TextField txtIpAddres;
    @FXML
    private TextField txtPort;
    @FXML
    private Label topLabel;
    @FXML
    private Label bottomLabel2;
    @FXML
    private Label bottomLabel1;

    static final String settingsPath = "src/resources/settings.bin";
    /**
     * File di salvataggio settings
     */
    static final File f = new File(settingsPath); 
    private static final int CORRECT_LENGTH_SETTINGS = 2;
    static final int IP_POSITION_IN_SETTINGS = 0;
    static final int PORT_POSITION_IN_SETTINGS = 1;
    
    /**
     * Carico i settaggi all'apertura della finestra, per prepopolare le inputbox
     */
    @FXML
    private void initialize() {

        if (f.exists()) { // Se il file esiste, faccio partire il server con quei parametri.

            Image img = new Image("resources/image/setting.png");
            ImageView view = new ImageView(img);
            view.setFitHeight(80);
            view.setPreserveRatio(true);
            topLabel.setGraphic(view);
            topLabel.setText("Settings");
            topLabel.setFont(new Font("Arial", 30));
            bottomLabel1.setVisible(false);
            bottomLabel2.setVisible(false);
            btnConnected.setDisable(false);

            ArrayList<String> arrSettings = readSettingsFromFile();
            if (arrSettings.size() != CORRECT_LENGTH_SETTINGS)
                return;

            txtIpAddres.setText(arrSettings.get(IP_POSITION_IN_SETTINGS));
            txtPort.setText(arrSettings.get(PORT_POSITION_IN_SETTINGS));

        } else {
            topLabel.setText("Welcome. Insert the parameters for connection to the server.");
            bottomLabel1.setVisible(true);
            bottomLabel2.setVisible(true);
        }

    }

    /**
     * Metodo che convalida la connessione validando inizialmente l'indirizzo ip e
     * la porta fornite dall'utente
     * 
     * @param event Oggetto che rappresenta l'azione effettuata (click dell'bottone)
     */
    @FXML
    private void convalidateConnection(ActionEvent event) {

        // Validazione parametri in input
        String ip = txtIpAddres.getText();
        String port = txtPort.getText();

        // Scrivo nel file i parametri
        /* Check if the ip and the port are in the correct format */
        try {
            if (CustomSocket.validateSettings(ip, new Integer(port))) {
                if (f.exists()) {
                    // Try to enstablish a connection with the server
                    if (CustomSocket.tryConnection(ip, new Integer(port))) {
                        writeSettingsInFile(ip, port);
                        CustomSocket.restartSocket();
                        Stage stage = (Stage) btnConnected.getScene().getWindow();
                        stage.close();
                    }
                } else {
                    try {
                        CustomSocket.initSocket(ip, new Integer(port));
                        writeSettingsInFile(ip, port);
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        UtilityMethods.open(stage,getClass(),"resources/HomeScene.fxml", "Regression Tree Learner", 429, 300);                        

                    } catch (IOException e) {
                        UtilityMethods.printError("Error Dialog", "Connection error",
                        "Cannot initialize the connection with the server. Make sure that the server is on and the port is correct!");
                    }
                }
            } else {
                txtIpAddres.setText("");
                txtPort.setText("");
            }
        } catch (NumberFormatException e) {
            UtilityMethods.printError("Error Dialog", "Connection error",
            "Cannot convert the string inserted into a number. Please retry with another port!");
        }
        
	}
    /**
     * Metodo che permette di abilitare il pulsante btnConnection,dopo aver 
     * riempito le caselle di testo IpAddres e Port
     * 
     * @param event Oggetto che rappresenta l'azione effettuata (rilascio del pulsante)
     */
	@FXML
	private void checkOnReleased(KeyEvent event) {
		btnConnected.setDisable(txtIpAddres.getText().isEmpty() || txtPort.getText().isEmpty() ? true : false);
		txtPort.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					txtPort.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});
	}
    
    /**
     * Metodo che permette di leggere dal file l'impostazioni fornite precedentemente dall'utente
     * @return Parametri di connessione sottoforma di ArrayList di stringhe
     */
	static ArrayList<String> readSettingsFromFile() {

        ArrayList<String> settings = new ArrayList<>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(settingsPath))) {

            String line = bufferedReader.readLine();
            int k = 0;
            String ip = "";
            Integer port = 0;
            while(line != null) {
                if (k == IP_POSITION_IN_SETTINGS)
                    ip = line;
                else 
                    port = new Integer(line);
                
                line = bufferedReader.readLine();
                k++;
            }
            settings.add(ip);
            settings.add(port.toString());

        } catch(FileNotFoundException e) {
            UtilityMethods.printError("Error Dialog", "File not found",
            "We haven't found the file for the settings. Are you sure that this file exists? Detail error: " + e);
        } catch (IOException e) {
            UtilityMethods.printError("Error Dialog", "Cannot read from the file",
                            "The connection has been lost with the file. Please restart the program. Detail Error: " + e);
        }
        return settings;

    }
    /**
     * Metodo che scrive nel file l'indirizzo ip e la porta dell'host
     * 
     * @param ipAddress Indirizzo IP dell'host con la quale si vuole creare la
	 *                  comunicazione
	 * @param port      Porta dell'host sulla quale è avviato il servizio
	 * 
	 * 
	 */
	private static void writeSettingsInFile(String ipAddress, String port) {

		try (BufferedWriter out = new BufferedWriter(new FileWriter(settingsPath, false))) {
			out.write(ipAddress);
			out.newLine();
			out.write(port);
		} catch (IOException e) {
			UtilityMethods.printError("Error Dialog", "Cannot read from the file",
					"The connection has been lost with the file. Please restart the program. Detail Error: " + e);
        }
        
	}

}
