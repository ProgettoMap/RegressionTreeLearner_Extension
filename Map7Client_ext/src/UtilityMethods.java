import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class UtilityMethods {
    
    public static void printError(String title, String headerText, String contentText) {

		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);

        alert.showAndWait();
        
	}


}
