import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class LogController {

    @FXML
    private TextArea logArea;

    /**
     * 	
	 * Metodo che setta il testo della label dei messaggi
	 */
    @FXML
    public void initialize() {
        logArea.setText(Log.getAllMessages());   
    }
    
}
