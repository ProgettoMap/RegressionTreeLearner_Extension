import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Classe contenente metodi complementari al pacchetto
 * applicativo
 *
 */

class UtilityMethods {
	/**
	 * 
	 * @param title Titolo che comparirà nella finestra di popup
	 * @param headerText Intestazione della finestra popup
	 * @param contentText Contenuto del testo della finestra popup
	 * 
	 * Apre una finestra popup con titolo, intestazione e contenuto dei parametri passati in input
	 */
	static void printError(String title, String headerText, String contentText) {

		Log.inserisciMessaggio(contentText);
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);

        alert.showAndWait();
        
	}


}
