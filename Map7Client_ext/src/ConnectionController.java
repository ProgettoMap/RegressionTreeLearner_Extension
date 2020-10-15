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
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Controller per la gestione del primo avvio del programma
 */
//TODO rivedere il keyRelaesed

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
	static final File f = new File(settingsPath);

	@FXML
	public void initialize() {

		if (f.exists()) {
			Image img = new Image("resources/setting.png");
			ImageView view = new ImageView(img);
			view.setFitHeight(80);
			view.setPreserveRatio(true);
			topLabel.setGraphic(view);
			topLabel.setText("Settings");
			topLabel.setFont(new Font("Arial", 30));
			bottomLabel1.setVisible(false);
			bottomLabel2.setVisible(false);
			btnConnected.setDisable(false);
		} else {
			topLabel.setText(
					"Welcome. Please, enter the server parameters for connecting to it and to predict a tree.");
			bottomLabel1.setVisible(true);
			bottomLabel2.setVisible(true);
		}
	}

	/**
	 * 
	 * @param event
	 * @throws IOException
	 * 
	 *                     Quando si tenta la connessione questo metodo controlla se
	 *                     l'indirizzo Ip e la porta sono validi e se su questo
	 *                     fosse presente anche il server dello stesso programma
	 */
	@FXML
	void convalidateConnection(ActionEvent event) throws IOException {

		// Validazione parametri in input
		String ip = txtIpAddres.getText();
		String port = txtPort.getText();

		// Scrivo nel file i parametri

		if (CustomSocket.validateSettings(ip,
				new Integer(port))) /* Check if the ip and the port are in the correct format */ {
			if (f.exists()) {
				if (CustomSocket.tryConnection(ip, new Integer(port))) { // Try to enstablish a connection with the
																			// server
					writeSettingsInFile(ip, port);
					CustomSocket.restartSocket();
					Stage stage = (Stage) btnConnected.getScene().getWindow();
					stage.close();
				}

			} else {

				CustomSocket.initSocket(ip, new Integer(port));
				writeSettingsInFile(ip, port);
				Parent tableViewParent = FXMLLoader.load(getClass().getResource("resources/HomeScene.fxml"));
				Scene tableViewScene = new Scene(tableViewParent);
				Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
				window.setScene(tableViewScene);
				window.show();

			}

		} else {
			txtIpAddres.setText("");
			txtPort.setText("");
		}

	}

	@FXML
	void checkOnReleased(KeyEvent event) {
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

	// Carica i settings dal file
	void loadSettings() {
		try { // TODO: sostituire con il metodo sotto della lettura dei settings
			File f = new File(settingsPath);
			if (f.exists()) { // Se il file esiste, faccio partire il server con quei parametri.
				try (BufferedReader bufferedReader = new BufferedReader(new FileReader(settingsPath))) {
					String line = bufferedReader.readLine();
					int k = 0;
					while (line != null) {
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
			UtilityMethods.printError("Error Dialog", "File not found",
					"We haven't found the file for the settings. Are you sure that this file exists? Detail error: "
							+ e);
		} catch (IOException e2) {
			UtilityMethods.printError("Error Dialog", "Cannot read from the file",
					"The connection has been lost with the file. Please restart the program. Detail Error: " + e2);
		}
	}

	static ArrayList<String> readSettingsFromFile() {

		ArrayList<String> settings = new ArrayList<>();
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(settingsPath))) {
			String line = bufferedReader.readLine();
			int k = 0;
			String ip = "";
			Integer port = 0;
			while (line != null) {
				if (k == 0)
					ip = line;
				else
					port = new Integer(line);

				line = bufferedReader.readLine();
				k++;
			}
			settings.add(ip);
			settings.add(port.toString());

		} catch (FileNotFoundException e) {
			UtilityMethods.printError("Error Dialog", "File not found", " Detail error: " + e.toString());
		} catch (IOException e) {
			UtilityMethods.printError("Error Dialog", "Cannot read from the file", "Detail error: " + e.toString());
		}
		return settings;
	}

	static void writeSettingsInFile(String ipAddress, String port) {
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
