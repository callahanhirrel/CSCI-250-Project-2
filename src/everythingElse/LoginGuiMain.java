package everythingElse;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LoginGuiMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(LoginGuiMain.class.getResource("LoginGUI2.fxml"));
		Pane root = (Pane) loader.load();
		
		Scene scene = new Scene(root, 425, 450);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args) throws IOException {
		launch(args);
		Server s = new Server(8881); // gonna use 8881 as the port for now
		s.listen();
	}
	
}
