import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class LogController {

    @FXML
    private TextArea logArea;

    @FXML
    public void initialize() {
        logArea.setText(Log.getAllMessages());   
    }
    
}
