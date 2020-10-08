
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

public class PredictionController {

	@FXML
	private TextArea txtAreaLoad;

	@FXML
	private Button btnPredPhase;

	@FXML
	private Button btnSubmit;

	Socket socket = CustomSocket.getIstance();
	ObjectOutputStream out = CustomSocket.getOutputStream();
	ObjectInputStream in = CustomSocket.getInputStream();

//	FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/log.fxml"));
//	LogController logctr = (LogController) loader.getController();

	@FXML
	private ComboBox<String> cmbxChoiseBranch;

	final String regularEx = new String("[0-9]+:(.*)");

	//TODO serve un metodo start che lanci printRules e printtree

	@FXML
	private void initialize() {
		String answer = "";
		try {
			while (!(answer = in.readObject().toString()).equals("FINISH")) {
				if (!answer.toLowerCase().contains("error"))
					txtAreaLoad.setText(txtAreaLoad.getText() + "\n" + answer); // Reading rules
				else
					UtilityMethods.printError("Error Dialog", "Error in printing rules", "There has been an error while printing rules. Detail error: " + answer);
			}

			while (!(answer = in.readObject().toString()).equals("FINISH")) { // TODO: Perchè nel print rules sta anche il print tree?
				if (!answer.toLowerCase().contains("error"))
					txtAreaLoad.setText(txtAreaLoad.getText() + "\n" + answer); // Reading rules
				else
					UtilityMethods.printError("Error Dialog", "Error in printing rules", "There has been an error while printing rules. Detail error: " + answer);
			}
			answer = in.readObject().toString();
			if (!answer.equals("OK")) {
				UtilityMethods.printError("Error Dialog", "Error in printing rules", "There has been some generic error. Detail error: " + answer);
				return;
			}
		} catch (IOException | ClassNotFoundException e) {
			UtilityMethods.printError("Error Dialog", "Connection error",
					"Cannot initialize the connection with the server. Detail error: " + e);
			CustomSocket.closeSocketIfOpened();
		}
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
			UtilityMethods.printError("Error Dialog", "Message error from the server", "There has been some errors with the answer from the server. Detail error: " + answer);
		}
	}

	@FXML
	void submitChoice(ActionEvent event) throws IOException {
		String elementSelected = cmbxChoiseBranch.getSelectionModel().getSelectedItem();
		String[] st = elementSelected.split(":");

		int path = Integer.parseInt(st[0]);
		out.writeObject(path);

		String answer;
		try {
			answer = in.readObject().toString();
			if (answer.equalsIgnoreCase("QUERY")) {
				answer = in.readObject().toString();
				txtAreaLoad.appendText(answer);
				setComboItem(answer);
			} else if (answer.equalsIgnoreCase("OK")) {
				answer = in.readObject().toString();
				txtAreaLoad.appendText("\nPredicted class:" + answer);
				repeatPrediction(answer);
			} else {
				UtilityMethods.printError("Error Dialog", "Message error from the server", "There has been some errors with the answer from the server. Detail error: " + answer);
			}
		} catch (ClassNotFoundException e) {
			UtilityMethods.printError("Error Dialog", "Message error", "There are some components in the program that cannot be found. Detail error: " + e);
		} // Read trees
	}

	private void setComboItem(String answer) {

		String[] answerSplitted = answer.split("\n");
		ObservableList<String> list = FXCollections.observableArrayList(answerSplitted);
		cmbxChoiseBranch.setItems(list);

	}
	
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
		
		//Riapertura della socket 
		CustomSocket.restartSocket();

		Parent tableViewParent = FXMLLoader.load(getClass().getResource("resources/HomeScene.fxml"));
		Scene tableViewScene = new Scene(tableViewParent); // This line gets the Stage information
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(tableViewScene);
		window.show(); 
	
	}

}
