package everythingElse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ProjectGuiController {
	private ArrayList<String> arrays = new ArrayList<>();
	//private ArrayList<String> transfer = new ArrayList<>();

	public void initialize() {

	}

	public void show_file(String filename) {
		Label label = new Label(filename);
		fileContainer.getChildren().add(label);
	}

	@FXML
	VBox fileContainer;

	@FXML
	Button addFile;

	@FXML
	Button rmFile;

	@FXML
	ScrollPane hasAddedFile;

	@FXML
	void add_file() {
		try{
			ArrayList<String> trans = new ArrayList<>();
			//addFile.setText("Test");
			for (int j = 0; j < arrays.size(); j++) {
				trans.add(arrays.get(j));
			}

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ProjectGuiMain.class.getResource("Project_GUI.fxml"));
			AnchorPane root = (AnchorPane) loader.load();

			ProjectGuiController pgc = (ProjectGuiController)loader.getController();

			Stage stage = new Stage();
			Scene scene = new Scene(root, 600, 371);
			stage.setScene(scene);
			stage.show();
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle(addFile.getText());
			fileChooser.getExtensionFilters().addAll(
					new ExtensionFilter("All Files", "*.*")
					//new ExtensionFilter("MP3", "*.mp3"),
					//new ExtensionFilter("WAV", "*.wav")
					);

			List<File> list = fileChooser.showOpenMultipleDialog(stage);

			for (int k = 0; k < trans.size(); k++) {
				arrays.add(trans.get(k));
			}

			if (list != null) {
				//add.add(file.getName());

				for (File file : list) {
					arrays.add(file.getName());
				}

				for (int i = 0; i < arrays.size(); i++) {
					pgc.show_file(arrays.get(i));
				}
			}
			addFile.getScene().getWindow().hide();
		} catch (Exception exc) {
			exc.printStackTrace();
		}

	}
}
