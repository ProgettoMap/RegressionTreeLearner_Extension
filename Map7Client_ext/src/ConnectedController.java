
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

	static Socket socket = null;
	static ObjectOutputStream out = null;
	static ObjectInputStream in = null;

    @FXML
    void convalidateConnection(ActionEvent event) throws IOException {

		String ip=txtIpAddres.getText();
		String port=txtPort.getText();

		// Validazione parametri in input

		if (!ip.isEmpty() & !port.isEmpty()) {
			if (!ip.matches(
					"^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) { // Formato
																													// ip
																													// non
																													// valido
				printError("Error Dialog", "There's some error with the IP...",
						"The IP that you've entered isn't correct. Please, start again the program and insert a valid ip.");
				return;
			}
			if (!port
					.matches("^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$")) { // Formato
																														// porta
																														// non
																														// valido
				printError("Error Dialog", "There's some error with the port...",
						"The port that you've entered isn't correct. Please, start again the program and insert a valid port (value between 1 and 65535)");
				return;
			}
		} else { // Numero parametri insufficiente
			printError("Error Dialog", "Settings doesn't match the right format...",
					"Please review your settings, there's some errors within it.");
			return;
		}
/* 
			OutputStream fos= new BufferedOutputStream(new FileOutputStream("src\\resources\\settings.bin"));  
			fos.write(ip.getBytes());
			fos.write(port.getBytes());
			fos.flush();
			fos.close();
		  
		 */
		InetAddress addr;
		try {
			addr = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			printError("Error Dialog", "Generic error", e.toString());
			return;
		}

		try {

			String msg = "Trying to connect to the server " + addr + "... \n";
			//log_arr.add(msg);

			//log_lbl.setText(msg);
			socket = new Socket(addr, new Integer(port).intValue());
			msg = "Connected to the server: " + socket;
			//log_arr.add(msg);

			//log_lbl.setText(msg);

			// stream con richieste del client
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			printError("Error Dialog", "Connection error",
					"Cannot initialize the connection with the server. Detail error: " + e.toString());
			try {
				closeSocketIfOpened();
			} catch (IOException e1) {
				printError("Error Dialog", "Socket error", "Socket has not been closed correctly");
			}
			return;
		}

		Parent tableViewParent = FXMLLoader.load(getClass().getResource("resources/first_scene.fxml"));
		Scene tableViewScene = new Scene(tableViewParent);
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(tableViewScene);
		window.show();

    }
	public void printError(String title, String headerText, String contentText) {

		//log_arr.add(contentText);

		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);

		alert.showAndWait();
	}

	void closeSocketIfOpened() throws IOException {
		if (socket != null && !socket.isClosed())
			socket.close(); // TODO: siamo sicuri che debba stare?
		in.close();
		out.close();
	}

}
