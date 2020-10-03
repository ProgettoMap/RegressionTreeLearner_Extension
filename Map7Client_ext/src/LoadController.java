
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoadController {

	@FXML
	private TextArea txtAreaLoad;

	@FXML
	private Button btnPredPhase;

	@FXML
	private Button btnSubmit;

	Socket socket = CustomSocket.getIstance();
	ObjectOutputStream out = CustomSocket.getOutputStream();
	ObjectInputStream in = CustomSocket.getInputStream();

	@FXML
	private ComboBox<String> cmbxChoiseBranch;

	final String regularEx = new String("[0-9]+:(.*)");

	public void printRules() {
	String answer = "";

		try {
			while (!(answer = in.readObject().toString()).equals("FINISH")) {
				if (answer.toLowerCase().contains("error"))
					// TODO define the headerText
					printError("Error Dialog", "??", answer);
				else
					txtAreaLoad.setText(txtAreaLoad.getText() + "\n" + answer); // Reading rules
			}

			while (!(answer = in.readObject().toString()).equals("FINISH")) {
				if (answer.toLowerCase().contains("error"))
					// TODO define the headerText
					printError("Error Dialog", "??", answer);
				else
					txtAreaLoad.setText(txtAreaLoad.getText() + "\n" + answer); // Reading rules
			}
			answer = in.readObject().toString();
			if (!answer.equals("OK")) {
				System.err.println(answer);
				return;
			}

		} catch (IOException | ClassNotFoundException e) {
			printError("Error Dialog", "Connection error",
					"Cannot initialize the connection with the server. Detail error: " + e.toString());
			try {
				socket.close();
			} catch (IOException e1) {
				printError("Error Dialog", "Socket error", "Socket has not been closed correctly");
			}
		}
	}

	public void choice(int decision, String tableName) {
		String answer = "";
		try {
			if (decision == 1) { // Learn regression tree

				// log_lbl.setText("Starting data acquisition phase!");

				out.writeObject(0);
				out.writeObject(tableName);
				answer = in.readObject().toString();
				if (!answer.equals("OK")) {
					// System.err.println(answer); // C'� stato qualche errore
					printError("Error Dialog", "Message error from the server", answer);
					return;
				}
//				log_lbl.setText("Starting learning phase!");
				out.writeObject(1);

			} else { // Load tree from archive
				out.writeObject(2);
				out.writeObject(tableName);
			}

		} catch (IOException | ClassNotFoundException e) {
			printError("Error Dialog", "Connection error",
					"Cannot initialize the connection with the server. Detail error: " + e.toString());
			try {
				socket.close();
			} catch (IOException e1) {
				printError("Error Dialog", "Socket error", "Socket has not been closed correctly");
				// Chiudere java
			}
		}

		printRules();
	}

	@FXML
	void predictionPhase(ActionEvent event) throws ClassNotFoundException, IOException {
		
		String answer = "";
		btnPredPhase.setDisable(true);
		cmbxChoiseBranch.setVisible(true);
		btnSubmit.setVisible(true);

		
		out.writeObject(3); // Stampo 3 per far capire al server che sto iniziando fase predizione
		txtAreaLoad.appendText("Starting prediction phase!\n");
		answer = in.readObject().toString(); // Reading tree.predictClass() from server

		if (answer.equals("QUERY")) {
			answer = in.readObject().toString(); // Read trees
			txtAreaLoad.appendText(answer);
			setComboItem(answer);
		} else {
			// print error
		}
	}

	@FXML
	void submitChoice(ActionEvent event) throws IOException {
		String elementSelected = cmbxChoiseBranch.getSelectionModel().getSelectedItem();
		//Pattern patt = Pattern.compile(regularEx);
		String[] st = elementSelected.split(":");

		int path = Integer.parseInt(st[0]);
		out.writeObject(path);

		String answer;
		try {
			answer = in.readObject().toString();
			if (answer.equalsIgnoreCase("QUERY")) {
				answer = in.readObject().toString();
				txtAreaLoad.appendText(answer);
				//setComboItem(countBranches(answer));
				setComboItem(answer);
			} else if (answer.equalsIgnoreCase("OK")) {
				answer = in.readObject().toString();
				txtAreaLoad.appendText("\nPredicted class:" + answer);
				repeatPrediction(answer);

			} else {
				// verificare l'header error
				printError("Error", "??", answer);
			}

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Read trees

	}

	public void printError(String title, String headerText, String contentText) {

		// log_arr.add(contentText);

		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);

		alert.showAndWait();
	}

	private void setComboItem(String answer) {

		String[] answerSplitted = answer.split("\n");
		ObservableList<String> list = FXCollections.observableArrayList(answerSplitted);
		cmbxChoiseBranch.setItems(list);

	}

	/* private int countBranches(String toMaches) {

		Pattern branch = Pattern.compile(regularEx);
		Matcher countBranch = branch.matcher(toMaches);
		int count = 0;

		while (countBranch.find()) {
			count++;
		}

		return count;

	} */

	private void repeatPrediction(String answer) throws IOException {

		Alert alert = new Alert(AlertType.CONFIRMATION, "Would you like to repeat the prediction ?", ButtonType.YES,
				ButtonType.NO);
				alert.setHeaderText("The predicted value is: " + answer);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.YES) {
			btnPredPhase.setDisable(false);
		} 
		btnSubmit.setDisable(true);
		cmbxChoiseBranch.setDisable(true);
	}

	@FXML
    private ImageView btnHome;

    @FXML
	public void backHome(MouseEvent event) throws IOException {
		
		CustomSocket.closeSocketIfOpened();
		ArrayList<String> settings = SettingsController.readSettingsFromFile();
		String ip = settings.get(0);
		Integer port = new Integer(settings.get(1));
		CustomSocket.initSocket(ip, port);

		Parent tableViewParent = FXMLLoader.load(getClass().getResource("resources/first_scene.fxml"));
		Scene tableViewScene = new Scene(tableViewParent); // This line gets the Stage information
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(tableViewScene);
		window.show(); 
	
	}

}
