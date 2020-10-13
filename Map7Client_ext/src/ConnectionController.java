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

/**
 * Controller per la gestione del primo avvio del programma
 */
public class ConnectionController {

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

		//TODO: Qui non viene validato nulla! Non viene controllato se la socket è valida, se porta e ip sono validi ecc.

		// Scrivo nel file i parametri
		SettingsController.writeSettingsInFile(ip, port);

		CustomSocket.initSocket(ip, new Integer(port));

		Parent tableViewParent = FXMLLoader.load(getClass().getResource("resources/HomeScene.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(tableViewScene);
		window.show();

    }

}
