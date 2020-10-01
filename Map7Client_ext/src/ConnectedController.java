import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ConnectedController {

    @FXML
    private Button btnConnected;

    @FXML
    private TextField txtIpAddres;

    @FXML
    private TextField txtPort;

    @FXML
    void convalidateConnection(ActionEvent event) throws IOException {
		
		// Validazione parametri in input
		String ip = txtIpAddres.getText();
		String port = txtPort.getText();

		if (!ip.isEmpty() & !port.isEmpty()) {
			if (!ip.matches(
					"^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) { // Formato
																													// ip
																													// non
																													// valido
						UtilityMethods.printError("Error Dialog", "There's some error with the IP...",
						"The IP that you've entered isn't correct. Please, start again the program and insert a valid ip.");
				return;
			}
			if (!port
					.matches("^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$")) { // Formato
																														// porta
																														// non
																														// valido
						UtilityMethods.printError("Error Dialog", "There's some error with the port...",
						"The port that you've entered isn't correct. Please, start again the program and insert a valid port (value between 1 and 65535)");
				return;
			}
		} else { // Numero parametri insufficiente
			UtilityMethods.printError("Error Dialog", "Settings doesn't match the right format...",
					"Please review your settings, there's some errors within it.");
			return;
		}

		// Scrivo nel file i parametri
		try(BufferedWriter out = new BufferedWriter(new FileWriter("src/resources/settings.bin"))){
			out.write(ip);
			out.newLine();
			out.write(port);
		}
		
		CustomSocket.initSocket(ip, new Integer(port));

		Parent tableViewParent = FXMLLoader.load(getClass().getResource("resources/first_scene.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(tableViewScene);
		window.show();

    }
	



}
