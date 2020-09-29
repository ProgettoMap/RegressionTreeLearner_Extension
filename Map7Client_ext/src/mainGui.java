import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class mainGui extends Application {

    @Override
    public void start(Stage stage) throws Exception {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("resources/first_scene.fxml"));

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