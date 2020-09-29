import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class mainGui extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        
        /* String path = "src/resources/settings.bin";
        File f = new File(path);
        if(f.exists()) { 
            BufferedInputStream bufferedInputStream = new BufferedInputStream(
                      new FileInputStream(path));
                      byte[] bytes;
                      bufferedInputStream.read(bytes);

        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/connected.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Regression Tree Learner");
            stage.setScene(scene);
            stage.show();
        } */

        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/connected.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Regression Tree Learner");
        stage.setScene(scene);
        stage.show();


//        MainController main = (MainController) loader.getController();
//        main.initialization();
//        //main.pressSelection();
//
//
//
//        stage.setOnHiding( event -> {
//        	try {
//				main.closeSocketIfOpened();
//			} catch (IOException e) {
//				main.printError("", "", e.toString());
//			}
//        } );

    }

    public static void main(String[] args) {
        launch(args);
    }
}