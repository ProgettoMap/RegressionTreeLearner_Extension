import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Classe contenente metodi complementari al pacchetto applicativo
 *
 */

class UtilityMethods {

	private static final String URL_IMAGE = "resources/image/favicon.png"; // Path favicon

	/**
	 * 
	 * @param title       Titolo che comparirà nella finestra di popup
	 * @param headerText  Intestazione della finestra popup
	 * @param contentText Contenuto del testo della finestra popup
	 * 
	 *                    Apre una finestra popup con titolo, intestazione e
	 *                    contenuto dei parametri passati in input
	 */
	static void printError(String title, String headerText, String contentText) {

		Log.inserisciMessaggio(contentText);
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);

		alert.showAndWait();

	}

	/**
	 * Metodo di utility che permette l'apertura di un nuovo stage
	 * 
	 * @param stage Finestra di visualizzazione
	 * @param type Tipologia di classe
	 * @param url Path file fxml contenente il layout dello stage
	 * @param title Titolo della finestra da aprire
	 * @param width Larghezza della finestra
	 * @param heigth Altezza della finestra
	 * @throws IOException Eccezione scatenata quando avviene un problema con la lettura del file di layout
	 */
	static void open(Stage stage,Class<?> type,String url, String title, int width, int heigth) throws IOException {
		Parent root = new FXMLLoader(type.getResource(url)).load();
		Scene scene = new Scene(root);
		stage.setTitle(title);
		stage.getIcons().add(new Image(URL_IMAGE));
		stage.setMinWidth(width);
		stage.setWidth(width);
		stage.setMinHeight(heigth);
		stage.setHeight(heigth);
		if(url.equals("resources/HomeScene.fxml")){
            stage.setOnCloseRequest(event -> { // Quando clicco sul pulsante di chiusura della schermata principale, chiuderò la socket (se aperta)
                CustomSocket.closeSocketIfOpened(CustomSocket.getIstance());
            });
        }
		stage.setScene(scene);
		stage.show(); 
	}

	  

}
