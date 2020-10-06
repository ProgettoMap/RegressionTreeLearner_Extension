import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.event.ActionEvent;
/**
 *
 * Main del client
 *
 */
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

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

	@FXML
	void process(MouseEvent event) {
		log_lbl.setText("<Test>");
	}

	public void pressSelection(ActionEvent event) throws IOException {
		input_txt_filename.setDisable(false);
		input_txt_filename.setEditable(true);
		processBtn.setDisable(false);
	}

	public void processButton(ActionEvent event) throws IOException {

		int decision = 0;
		String tableName = null;

		if (!input_txt_filename.getText().isEmpty()) {
			tableName = input_txt_filename.getText();
			decision = rblearn.isSelected() ? 1 : 2;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/load.fxml"));
			Parent tableViewParent = loader.load();
			Scene tableViewScene = new Scene(tableViewParent);
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        PredictionController loadctrl = (PredictionController) loader.getController();
	        loadctrl.choice(decision, tableName);
			window.setScene(tableViewScene);
			window.show();

		} else
			printError("Error Dialog", "Input error", "The name of table doesn't exist");

	}

	public void printError(String title, String headerText, String contentText) {

		// log_arr.add(contentText);

		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);

		alert.showAndWait();
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
			FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/settings.fxml"));
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

}
