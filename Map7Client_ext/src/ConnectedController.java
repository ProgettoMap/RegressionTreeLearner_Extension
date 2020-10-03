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
