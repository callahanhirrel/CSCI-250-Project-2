package everythingElse;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
//import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
//import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;


public class ProjectGuiController {
	//public List<File> arrays = new ArrayList<>();
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
	void rm_file() {
		
	}
	
	@FXML
	void add_file() {
		addFile.getScene().getWindow().hide();
		try{
			/*
			ArrayList<String> trans = new ArrayList<>();
			//addFile.setText("Test");
			for (int j = 0; j < arrays.size(); j++) {
				trans.add(arrays.get(j));
			}
			*/
			FXMLLoader loader = new FXMLLoader();
			FXMLLoader loader2 = new FXMLLoader();
			loader.setLocation(ProjectGuiController.class.getResource("Project_GUI.fxml"));
			loader2.setLocation(LoginGuiController.class.getResource("Main_GUI.fxml"));
			AnchorPane root2 = (AnchorPane) loader2.load();
			AnchorPane root = (AnchorPane) loader.load();
			
			ProjectGuiController pgc = (ProjectGuiController)loader.getController();
			ClientController Client = (ClientController)loader2.getController();
			/*
			for (File files : arrays) {
				pgc.arrays.add(files);
			}
			*/
			Client.new_tab(root);
			Stage stage = new Stage();
			Scene scene = new Scene(root2);
			stage.setScene(scene);
			stage.show();
			
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle(addFile.getText());
			fileChooser.getExtensionFilters().addAll(
					new ExtensionFilter("All Files", "*.*")
					//new ExtensionFilter("MP3", "*.mp3"),
					//new ExtensionFilter("WAV", "*.wav")
					);
			
			//ArrayList<String> trans2 = new ArrayList<>();
			//addFile.setText("Test");
			/*
			for (int x = 0; x < trans.size(); x++) {
				trans2.add(trans.get(x));
			}
			*/
			List<File> list = fileChooser.showOpenMultipleDialog(stage);
			
			if (list != null) {
				//add.add(file.getName());
				/*
				for (int k = 0; k < trans2.size(); k++) {
					arrays.add(trans2.get(k));
				}
				*/
				File f = new File("store_message.txt");
				PrintWriter printer = new PrintWriter(new FileWriter(f, true));
				
				for (File file : list) {
					printer.println(file.getName());
					
				}
				
				printer.close();
				
				try {
					Scanner input = new Scanner(f);
					//System.out.print(f.getName());
					while (input.hasNextLine()) { 
						String line = input.nextLine();
						//System.out.print(line);
						pgc.show_file(line);
					}
				
					input.close();
					
				} catch (Exception exc) {
					exc.printStackTrace();
				}
			}					
		} catch (Exception exc) {
			exc.printStackTrace();
		}

	}
}
