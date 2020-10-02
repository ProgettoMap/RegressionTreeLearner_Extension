import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SettingsController {

    @FXML
    private Button btnUpdateSettings;

    @FXML
    private TextField txtIpAddres;

    @FXML
    private TextField txtPort;

    void loadSettings() {
        String path = "src/resources/settings.bin";
        try {
            File f = new File(path);
            if(f.exists()) { // Se il file esiste, faccio partire il server con quei parametri.
                try(BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
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
        if(validateSettings()){
            try(BufferedWriter out = new BufferedWriter(new FileWriter("src/resources/settings.bin", false))){
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

        }
    }
    
    boolean validateSettings() {
        return true;
    }

}
