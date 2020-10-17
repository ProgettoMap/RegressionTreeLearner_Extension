import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * Classe di tipo controller che gestisce la schermata di messggi di log 
 *
 */
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
