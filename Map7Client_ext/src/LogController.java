import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class LogController {

    @FXML
    private TextArea logArea;

    public void inserisciMessaggio(String messaggio) {
    	logArea.appendText(messaggio);
    }
    
}
