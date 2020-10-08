import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/** Main del client */
public class HomeController {

	ArrayList<String> log_arr = new ArrayList<String>();

	@FXML
	private RadioButton rblearn;

	@FXML
	private RadioButton rbload;

	@FXML
	private ToggleGroup radiogroup;

	@FXML
	private MenuItem logMenuItem;

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private Label log_lbl;

	@FXML
	private Button processBtn;

	@FXML
	private TextField input_txt_filename;

	Socket socket = CustomSocket.getIstance();
	ObjectOutputStream out = CustomSocket.getOutputStream();
	ObjectInputStream in = CustomSocket.getInputStream();

	/** Metodo richiamato allo scatenamento dell'evento del click di un radioButton */
	public void pressSelection(ActionEvent event) throws IOException {
		input_txt_filename.setDisable(false);
		input_txt_filename.setEditable(true);	
	}
	
    @FXML
    void textField(KeyEvent event) {
		processBtn.setDisable(input_txt_filename.getText().isEmpty());

    }
	/** Metodo richiamato allo scatenamento dell'evento del click del bottone Process */
	public void processButton(ActionEvent event) {

		int decision = 0;
		String tableName = null;

		tableName = input_txt_filename.getText();
		decision = rblearn.isSelected() ? 1 : 2;
	
		try {
			choice(decision, tableName); // Comunica la decisione al server
			FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/PredictionScene.fxml"));
			Parent tableViewParent = loader.load();
			Scene tableViewScene = new Scene(tableViewParent);
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			//PredictionController loadctrl = (PredictionController) loader.getController();
			window.setScene(tableViewScene);
			window.show();
		
		} catch ( IOException | ClassNotFoundException e ) {
			UtilityMethods.printError("Error Dialog", "Connection error",
				"Cannot initialize the connection with the server. Detail error: " + e.toString());
			CustomSocket.closeSocketIfOpened();
			return;
		} catch (TableNotFoundException e) {
			UtilityMethods.printError("Error Dialog", "Connection error",
				"The table that you've inserted was not found. Please retry. \n Detail error: " + e.toString());
			//CustomSocket.closeSocketIfOpened();
			CustomSocket.restartSocket();
			return;
		}
		


	}

	@FXML
	void showLogDialog(ActionEvent event) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Log dei messaggi");
		alert.setHeaderText(null);
		alert.setContentText(null);

		Iterator<String> iterator = log_arr.iterator();

		String log = "";
		while (iterator.hasNext()) {
			log += iterator.next() + "\n";
		}

		TextArea textArea = new TextArea(log);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}

	@FXML
    void showSettings(ActionEvent event) {
		Parent root;
        try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/SettingsScene.fxml"));
            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Regression Tree Learner - Settings");
			stage.setScene(new Scene(root));
			stage.show();
			
			SettingsController settingsctlr = (SettingsController) loader.getController();
	        settingsctlr.loadSettings();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
	}
	/**
	 * Metodo che comunica al server la decisione del client
	 */
	public void choice(int decision, String tableName) throws IOException, ClassNotFoundException, TableNotFoundException{
		String answer = "";
		if (decision == 1) { // Learn regression tree
			out.writeObject(0);
			out.writeObject(tableName);
			answer = in.readObject().toString();
			if (!answer.equals("OK")) {
				UtilityMethods.printError("Error Dialog", "Message error from the server", "There has been some errors with the answer from the server. Detail error: " + answer);
				throw new TableNotFoundException();
			}
			out.writeObject(1);
		} else { // Load tree from archive
			out.writeObject(2);
			out.writeObject(tableName);
		}
		//printRules();
	}
}
