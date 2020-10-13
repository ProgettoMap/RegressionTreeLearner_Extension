import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class HomeController {

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

	/**
	 * Metodo richiamato al click di un radioButton qualsiasi
	 * 
	 * @param event Oggetto che rappresenta l'azione effettuata (click del radioButton)
	 * @throws IOException
	 */
	public void pressSelection(ActionEvent event) throws IOException {
		input_txt_filename.setDisable(false); // Abilito e rendo editabile l'inputbox
		input_txt_filename.setEditable(true);	
	}
    
	/** 
	 * 	Controllo dinamico effettuato all'inserimento di qualsiasi testo nella inputbox del nome del file, 
	 *	per abilitare o disabilitare il bottone di process se la input è riempita o vuota
	 *
	 * 	@param event Oggetto che rappresenta l'azione effettuata (inserimento del testo)
	 */
	@FXML
    void checkOnPressed(KeyEvent event) {
		processBtn.setDisable(input_txt_filename.getText().isEmpty());
	}
	
	/**
	 * Metodo richiamato al click del bottone Process
	 * Comunica la decisione effettuata dall'utente e il nome del file / tabella da cui leggere / apprendere l'albero
	 * 
	 * @param event Oggetto che rappresenta l'azione effettuata (click del bottone)
	 */
	public void processButton(ActionEvent event) {

		int decision = rblearn.isSelected() ? 1 : 2;
		String sourceName = input_txt_filename.getText();
	
		try {
			choice(decision, sourceName); // Comunica la decisione al server
			FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/PredictionScene.fxml"));
			Parent tableViewParent = loader.load();
			Scene tableViewScene = new Scene(tableViewParent);
			Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
			window.getIcons().add(new Image("resources/favicon.png"));

			window.setScene(tableViewScene);
			window.show();
		
		}  catch (TableNotFoundException e) {
			UtilityMethods.printError("Error Dialog", "Connection error",
				"The table that you've inserted was not found. Please retry with another name.");
			CustomSocket.restartSocket();
		} catch (FileNotFoundException e) {
			UtilityMethods.printError("Error Dialog", "Connection error",
				"The file with the name that you've inserted was not found. Please retry with another name.");
			CustomSocket.restartSocket();
		} catch ( IOException | ClassNotFoundException e ) {
			UtilityMethods.printError("Error Dialog", "Connection error",
				"Cannot initialize the connection with the server. Detail error: " + e.toString());
			CustomSocket.closeSocketIfOpened();
			return;
		}
	}
	
	/** 
	 * Metodo che permette l'apertura della finestra del log degli errori
	 * 
	 * @param event Oggetto che rappresenta l'azione effettuata (click dell'elemento menu)
	 */
	@FXML
	void openLogDialog(ActionEvent event) {

		Parent root;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/LogScene.fxml"));
		try {
			root = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Log Window");
			stage.getIcons().add(new Image("resources/favicon.png"));
			stage.setScene(new Scene(root));
			stage.show();
		} catch (IOException e) {
			UtilityMethods.printError("Error Dialog", "Input/Output Error",
                        "Something has gone wrong while executing the program.\nDetail Error: " + e.toString());
		}
	}

	/** 
	 * Metodo che permette l'apertura della finestra delle impostazioni
	 * @param event Oggetto che rappresenta l'azione effettuata (click dell'elemento del menu)
	 */
	@FXML
    void openSettingsWindow(ActionEvent event) {

		Parent root;
        try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/SettingsScene.fxml"));
            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Regression Tree Learner - Settings");
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("resources/favicon.png"));
			stage.show();
			
			// Carico i settaggi all'apertura della finestra, per prepopolare le inputbox
			SettingsController settingsctlr = (SettingsController) loader.getController();
	        settingsctlr.loadSettings();
        }
        catch (IOException e) {
			UtilityMethods.printError("Error Dialog", "Input/Output Error",
			"Something has gone wrong while executing the program.\nDetail Error: " + e.toString()); 
		}
		
	}

	/** 
	 * Metodo che comunica al server la decisione del client (lettura da albero già appreso / nuovo apprendimento) e il nome del file / tabella
	 * 
	 * @param decision Numero indicante l'apprendimento da una tabella (1) o la lettura da file (2)
	 * @param sourceName Nome della sorgente di lettura/apprendimento
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws TableNotFoundException
	*/
	void choice(int decision, String sourceName) throws IOException, ClassNotFoundException, TableNotFoundException{
		String answer = "";
		ObjectOutputStream out = CustomSocket.getOutputStream();
		ObjectInputStream in = CustomSocket.getInputStream();

		if (decision == 1) { // Learn regression tree
			out.writeObject(0);
			out.writeObject(sourceName);
			answer = in.readObject().toString();
			if (!answer.equals("OK")) // Se la risposta non è OK vuol dire che la tabella non è stata trovata
				throw new TableNotFoundException();
			out.writeObject(1);
		} else { // Load tree from archive
			out.writeObject(2);
			out.writeObject(sourceName);
			answer = in.readObject().toString();
			if (!answer.equals("OK")) // Se la risposta non è OK vuol dire che la tabella non è stata trovata
				throw new FileNotFoundException();
		}
	}

}
