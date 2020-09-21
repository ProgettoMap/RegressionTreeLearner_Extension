package map7Client;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
/**
 *
 * Main del client
 *
 */
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import utility.Keyboard;

public class MainController {

	Socket socket = null;
	ObjectOutputStream out = null;
	ObjectInputStream in = null;

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

    }

    @FXML
	public void initialization() {

		String[] args = new String[2];
		args[0] = "127.0.0.1";
		args[1] = "8101";
		



		radiogroup.selectedToggleProperty (). addListener ( new ChangeListener <Toggle> () {
	        public  void changed (ObservableValue <? extends Toggle> ov,
	           Toggle old_toggle, Toggle new_toggle) {
	         if (radiogroup.getSelectedToggle () != null) {
	        	 input_txt_filename.setDisable(false);
	         }
	       }
	     });
		
		// Validazione parametri in input
		if (args.length == 2) {
			if (!args[0].matches(
					"^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) { // Formato
																													// ip
																													// non
																													// valido
				printError("Error Dialog", "There's some error with the IP...", "The IP that you've entered isn't correct. Please, start again the program and insert a valid ip.");
				return;
			}
			if (!args[1]
					.matches("^([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$")) { // Formato
																														// porta
																														// non
																														// valido
				printError("Error Dialog", "There's some error with the port...", "The port that you've entered isn't correct. Please, start again the program and insert a valid port (value between 1 and 65535)");
				return;
			}
		} else { // Numero parametri insufficiente
			printError("Error Dialog", "Settings doesn't match the right format...", "Please review your settings, because there's some errors within it.");
			return;
		}

		InetAddress addr;
		try {
			addr = InetAddress.getByName(args[0]);
		} catch (UnknownHostException e) {
			printError("Error Dialog", "Generic error", e.toString());
			return;
		}

		try {

			String msg ="Trying to connect to the server " + addr + "... \n";
			log_arr.add(msg);

			log_lbl.setText(msg);
			socket = new Socket(addr, new Integer(args[1]).intValue());
			msg = "Connected to the server: " + socket;
			log_arr.add(msg);

			log_lbl.setText(msg);

			// stream con richieste del client
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			printError("Error Dialog", "Connection error", "Cannot initialize the connection with the server. Detail error: " + e.toString());
			try {
				closeSocketIfOpened();
			} catch (IOException e1) {
				printError("Error Dialog", "Socket error", "Socket has not been closed correctly");
			}
			return;
		}
	}

	public void choise() {

		String answer = "";

		int decision = 0;
		System.out.println(" MENU ");
		System.out.println(" - Learn Regression Tree from data [1]");
		System.out.println(" - Load Regression Tree from archive [2]");
		do {
			System.out.print("-> ");
			decision = Keyboard.readInt();
		} while (!(decision == 1) && !(decision == 2));

		System.out.println("Table/file name:");
		System.out.print("-> ");
		String tableName = Keyboard.readString();
		try {
			if (decision == 1) { // Learn regression tree
				System.out.println("Starting data acquisition phase!");

				out.writeObject(0);
				out.writeObject(tableName);
				answer = in.readObject().toString();
				if (!answer.equals("OK")) {
					System.err.println(answer); // C'è stato qualche errore
					return;
				}
				System.out.println("Starting learning phase!");
				out.writeObject(1);

			} else { // Load tree from archive
				out.writeObject(2);
				out.writeObject(tableName);
			}

		} catch (IOException | ClassNotFoundException e) {
			System.out.println(e.toString());
			try {
				socket.close();
			} catch (IOException e1) {
				System.err.println("[!] Error [!] Socket has not been closed correctly.");

			}
			System.out.println("\n \n \nPress Any Key To Exit...");
			return;
		}
	}

	public void printTreeAndRules() {

		String answer = "";

		try {
			while (!(answer = in.readObject().toString()).equals("FINISH")) {
				if (answer.toLowerCase().contains("error"))
					System.err.println(answer);
				else
					System.out.println(answer); // Reading rules
			}

			while (!(answer = in.readObject().toString()).equals("FINISH")) {
				if (answer.toLowerCase().contains("error"))
					System.err.println(answer);
				else
					System.out.println(answer); // Reading rules
			}

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

	public void predictPhase() {

		String answer = "";

		try {
			answer = in.readObject().toString();
			if (!answer.equals("OK")) {
				System.err.println(answer);
				return;
			}

			char risp = 'y';
			do {
				out.writeObject(3);
				System.out.println("Starting prediction phase!");
				answer = in.readObject().toString(); // Reading tree.predictClass()

				while (answer.equals("QUERY")) {
					// Formulating query, reading answer
					answer = in.readObject().toString(); // Read trees
					System.out.println(answer);
					System.out.print("-> ");
					int path = Keyboard.readInt();
					out.writeObject(path);
					answer = in.readObject().toString();
				}

				if (answer.equals("OK")) { // Reading prediction
					answer = in.readObject().toString();
					System.out.println("Predicted class:" + answer);
				} else // Printing error message
					System.err.println(answer);

				do {
					System.out.println("Do you want to repeat? (y/n)");
					risp = Keyboard.readChar();
				} while (Character.toUpperCase(risp) != 'Y' && Character.toUpperCase(risp) != 'N');

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

		log_arr.add(contentText);

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
		while(iterator.hasNext())
		{
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

    void closeSocketIfOpened() throws IOException {
    	if(socket != null && !socket.isClosed())
			socket.close();  // TODO: siamo sicuri che debba stare?
    	in.close();
    	out.close();
    }
}
