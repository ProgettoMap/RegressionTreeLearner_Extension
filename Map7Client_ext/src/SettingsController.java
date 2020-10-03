import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SettingsController {

    static final String settingsPath = "src/resources/settings.bin";

    @FXML
    private Button btnUpdateSettings;

    @FXML
    private TextField txtIpAddres;

    @FXML
    private TextField txtPort;

    void loadSettings() {
        try {
            File f = new File(settingsPath);
            if(f.exists()) { // Se il file esiste, faccio partire il server con quei parametri.
                try(BufferedReader bufferedReader = new BufferedReader(new FileReader(settingsPath))) {
                    String line = bufferedReader.readLine();
                    int k = 0;
                    while(line != null) {
                        if (k == 0)
                            txtIpAddres.setText(line);
                        else 
                            txtPort.setText(line); 
                        
                        line = bufferedReader.readLine();
                        k++;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    @FXML
    void saveSettings(ActionEvent event) {
        String ipAddress = txtIpAddres.getText();
        String port = txtPort.getText();
        if(CustomSocket.validateSettings(ipAddress, new Integer(port))){
            if(CustomSocket.tryConnection(ipAddress, new Integer(port))) {
                try(BufferedWriter out = new BufferedWriter(new FileWriter(settingsPath, false))){
                    try {
                        out.write(ipAddress);
                        out.newLine();
                        out.write(port);
                        Stage stage = (Stage) btnUpdateSettings.getScene().getWindow();
                        stage.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                UtilityMethods.printError("Error Dialog", "Connection error",
                            "Cannot initialize the connection with the server. Make sure that the server is on and the port is correct!");
            }
            
        }    
    }

    static ArrayList<String> readSettingsFromFile() {

        ArrayList<String> settings = new ArrayList<>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(settingsPath))) {
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
            settings.add(ip);
            settings.add(port.toString());

        } catch(FileNotFoundException e) {
            UtilityMethods.printError("Error Dialog", "File not found",
            " Detail error: " + e.toString());
        } catch (IOException e) {
            UtilityMethods.printError("Error Dialog", "Cannot read from the file",
            "Detail error: " + e.toString());
        }
        return settings;
    }

}
