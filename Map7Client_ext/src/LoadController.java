
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;

public class LoadController {

	@FXML
	private TextArea txtAreaLoad;

	@FXML
	private Button btnPredPhase;

	@FXML
	private TextField txtChoise;

	@FXML
	private Button btnSubmit;

	Socket socket = ConnectedController.socket;
	ObjectOutputStream out = ConnectedController.out;
	ObjectInputStream in = ConnectedController.in;

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
					//System.err.println(answer); // C'� stato qualche errore
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
	void predictionPhase(ActionEvent event) {
		String answer = "";
		txtChoise.setVisible(true);
		btnSubmit.setVisible(true);

		try {
			answer = in.readObject().toString();
			if (!answer.equals("OK")) {
				System.err.println(answer);
				return;
			}

			char risp = 'y';
			do {
				out.writeObject(3);
				txtAreaLoad.setText(txtAreaLoad.getText() + "\n" + "Starting prediction phase!");
				answer = in.readObject().toString(); // Reading tree.predictClass()

				while (answer.equals("QUERY")) {
					// Formulating query, reading answer
					answer = in.readObject().toString(); // Read trees
					txtAreaLoad.setText(txtAreaLoad.getText() + "\n" + answer);
					choiceBranch();

//						int path = Integer.parseInt(txtChoise.getText());
//						out.writeObject(path);
//						answer = in.readObject().toString();
//					}
				}
//					int path = Keyboard.readInt();
//					out.writeObject(path);
//					answer = in.readObject().toString();
//				}
//
//				if (answer.equals("OK")) { // Reading prediction
//					answer = in.readObject().toString();
//					txtAreaLoad.setText(txtAreaLoad.getText() + "\n" + "Predicted class:"+answer);
//				} else // Printing error message
//					System.err.println(answer);
//
//				do {
//					txtAreaLoad.setText(txtAreaLoad.getText() + "\n" + 	"Do you want to repeat? (y/n)");
//					risp = Keyboard.readChar();
//				} while (Character.toUpperCase(risp) != 'Y' && Character.toUpperCase(risp) != 'N');

			} while (Character.toUpperCase(risp) == 'Y');

			// Aggiunta stampa per far capire al server che l'esecuzione del client vuole
			// terminare

			out.writeObject(0);
			System.out.println("Thank you for having used this Regression Tree Learner! See you soon...");

		} catch (IOException | ClassNotFoundException e) {
			System.out.println(e.toString());
		} finally {
			try {
				socket.close();
			} catch (IOException e1) {
				System.err.println("[!] Error [!] Socket has not been closed correctly.");

			}
			System.out.println("\n \n \nPress Any Key To Exit...");
		}
	}

	public void printError(String title, String headerText, String contentText) {

		// log_arr.add(contentText);

		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);

		alert.showAndWait();
	}
	public void choiceBranch() {
		TextInputDialog dlg = new TextInputDialog();
		dlg.setX(250);
		dlg.setY(250);
		dlg.setTitle("Choice branches to predict ");
		dlg.getDialogPane().setContentText("Name of branch");
		Optional<String> result =dlg.showAndWait();
		TextField input =dlg.getEditor();
		final Button btn = (Button) dlg.getDialogPane().getButtonTypes();
		//DA FINIRE QUANDO SI CLICCA OK BISOGNA FAR RESTITUIRE il path

	}

}
