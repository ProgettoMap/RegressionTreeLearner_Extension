
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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


/**
 * Classe di tipo controller che gestisce la schermata dove viene 
 * eseguita la fase di predizione 
 *
 */
public class PredictionController {

	@FXML
	private TextArea txtAreaLoad;

	@FXML
	private Button btnPredPhase;

	@FXML
	private Button btnSubmit;

	private ObjectOutputStream out;
	private ObjectInputStream in;

	@FXML
	private ComboBox<String> cmbxChoiseBranch;

	@FXML
	private ImageView btnHome;
	
	final String regularEx = new String("[0-9]+:(.*)");
	
	@FXML
	private void initialize() {
		
		in = CustomSocket.getInputStream();
		try {
			printRules();
			printTree();

			String answer = in.readObject().toString();
			if (!answer.equals("OK")) {
				UtilityMethods.printError("Error Dialog", "Generic error", "There has been some generic error. Detail error: " + answer);
				return;
			}
		} catch (IOException | ClassNotFoundException e) {
			UtilityMethods.printError("Error Dialog", "Connection error",
					"Cannot initialize the connection with the server. Detail error: " + e);
			CustomSocket.closeSocketIfOpened(CustomSocket.getIstance());
		}
	}

	
	/** 
	 * Metodo che inizializza la fase di predizione dell'albero di regressione fornito dall'utente
	 * 
	 * @param event Oggetto che rappresenta l'azione effettuata (click del Button)
	 * @throws IOException Eccezione lanciata quando si verifica un problema con l'output stream
	 * @throws ClassNotFoundException Eccezione lanciata quando si hanno problemi con la serialiazzazione
	 */
	@FXML
	private void predictionPhase(ActionEvent event) throws ClassNotFoundException, IOException {
		
		out = CustomSocket.getOutputStream(); // Aggiorno il valore di out e di in
		in = CustomSocket.getInputStream();
		String answer = "";
		btnPredPhase.setDisable(true);
		cmbxChoiseBranch.setVisible(true);
		btnSubmit.setVisible(true);
		
		out.writeObject(3); // Stampo 3 per far capire al server che sto iniziando fase predizione
		txtAreaLoad.appendText("\nStarting prediction phase!\n");
		answer = in.readObject().toString(); // Reading tree.predictClass() from server

		if (answer.equals("QUERY")) {
			answer = in.readObject().toString(); // Read trees
			txtAreaLoad.appendText(answer+"\n");
			setComboItem(answer);
		} else {
			UtilityMethods.printError("Error Dialog", "Message error from the server", "There has been some errors with the answer from the server. Detail error: " + answer);
		}
	}

	
	/** 
	 * Metodo scatenato all'invio del bottone che invia la scelta effettuata dall'utente 
	 * @param event Oggetto che rappresenta l'azione effettuata (click del Button)
	 * @throws IOException Eccezione lanciata quando si verifica un problema con l'output stream
	 */
	@FXML
	private void submitChoice(ActionEvent event) throws IOException {

		out = CustomSocket.getOutputStream();
		in = CustomSocket.getInputStream();
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
				txtAreaLoad.appendText("\n****************");
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
		cmbxChoiseBranch.setValue(list.get(0));
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
		btnSubmit.setVisible(false);
		cmbxChoiseBranch.setVisible(false);
	}


	/**
	 * Oggetto che rappresenta l'azione effettuata (click del Button)
	 * @param event Oggetto che rappresenta l'azione effettuata (click del Button)
	 * @throws IOException Eccezione lanciata quando si verifica un problema con l'output stream
	 */
    @FXML
    private void backHome(MouseEvent event) throws IOException {
		
		//Riapertura della socket 
		CustomSocket.restartSocket();

		Parent tableViewParent = FXMLLoader.load(getClass().getResource("resources/HomeScene.fxml"));
		Scene tableViewScene = new Scene(tableViewParent); // This line gets the Stage information
		Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
		window.setScene(tableViewScene);
		window.setMinHeight(300);
		window.setMinWidth(429);
        window.setHeight(300);
        window.setWidth(429);
		window.show(); 

	
	}

	
	/** 
	 * Metodo che riceve dal server le regole e le stampa nell'area di testo
	 * @throws ClassNotFoundException Eccezione lanciata quando si hanno problemi con la serialiazzazione
	 * @throws IOException Eccezione lanciata quando si verifica un problema con l'output stream
	 */
    private void printRules() throws ClassNotFoundException, IOException {

		String answer = "";
		
		while (!(answer = in.readObject().toString()).equals("FINISH")) {
			if (!answer.toLowerCase().contains("error"))
				txtAreaLoad.setText(txtAreaLoad.getText() + "\n" + answer); // Reading rules
			else
				UtilityMethods.printError("Error Dialog", "Error in printing rules", "There has been an error while printing rules. Detail error: " + answer);
		}
	
	}

	
	/** 
	 * Metodo che riceve dal server l'albero e lo stampa nell'area di testo
	 * @throws ClassNotFoundException Eccezione lanciata quando si hanno problemi con la serialiazzazione
	 * @throws IOException Eccezione la nciata quando si verifica un problema con l'output stream
	 */
    private void printTree() throws ClassNotFoundException, IOException {
		
		String answer = "";

		while (!(answer = in.readObject().toString()).equals("FINISH")) {
			if (!answer.toLowerCase().contains("error"))
				txtAreaLoad.setText(txtAreaLoad.getText() + "\n" + answer); // Reading tree
			else
				UtilityMethods.printError("Error Dialog", "Error in printing tree", "There has been an error while printing tree. Detail error: " + answer);
		}
	
	}

}
