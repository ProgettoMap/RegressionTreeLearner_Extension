package map7Client;



import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class MainController1 {

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
    	log_lbl.setText("<Test>");
    }
}
