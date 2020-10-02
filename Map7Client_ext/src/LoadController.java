
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
//TODO ERRORI INCONTRATI DOPO N iterazioni del bottone predictPhase esce un errore riga 155 

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
			setComboItem(countBranches(answer));
		} else {
			// print error
		}
	}

	@FXML
	void submitChoice(ActionEvent event) throws IOException {
		String elementSelected = cmbxChoiseBranch.getSelectionModel().getSelectedItem();
		int path = Integer.parseInt(elementSelected);
		out.writeObject(path);

		String answer;
		try {
			answer = in.readObject().toString();
			if (answer.equalsIgnoreCase("QUERY")) {
				answer = in.readObject().toString();
				txtAreaLoad.appendText(answer);
				setComboItem(countBranches(answer));
			} else if (answer.equalsIgnoreCase("OK")) {
				answer = in.readObject().toString();
				txtAreaLoad.appendText("\nPredicted class:" + answer);
				repeatPrediction();

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

	private void setComboItem(int numberBranch) {

		String[] items = new String[numberBranch];
		for (int i = 0; i < numberBranch; i++) {
			items[i] = "" + i;
		}
		ObservableList<String> list = FXCollections.observableArrayList(items);
		cmbxChoiseBranch.setItems(list);

	}

	private int countBranches(String toMaches) {

		Pattern branch = Pattern.compile(regularEx);
		Matcher countBranch = branch.matcher(toMaches);
		int count = 0;

		while (countBranch.find()) {
			count++;
		}

		return count;

	}

	public void choiceBranch() {
		TextInputDialog dlg = new TextInputDialog();
		dlg.setX(250);
		dlg.setY(250);
		dlg.setTitle("Choice branches to predict ");
		dlg.getDialogPane().setContentText("Name of branch");
		Optional<String> result = dlg.showAndWait();
		TextField input = dlg.getEditor();
		final Button btn = (Button) dlg.getDialogPane().getButtonTypes();

	}

	private void repeatPrediction() throws IOException {


		Alert alert = new Alert(AlertType.CONFIRMATION, "Would you repeat the prediction ?", ButtonType.YES,
				ButtonType.NO);
		alert.showAndWait();

		if (alert.getResult() == ButtonType.YES) {
			btnPredPhase.setDisable(false);
		}
//		alert.showAndWait().ifPresent(type -> {
//		        if (type == ButtonType.YES) {
//		        	btnPredPhase.setDisable(false);
//		        	predictionPhase(null);
//		        } else if (type == ButtonType.NO) {
//		        	try {
//		        		alert.setContentText("Thank you for having used this Regression Tree Learner! See you soon...");
//						out.writeObject(0);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//		        	
//		        }
//		});
	}

}
