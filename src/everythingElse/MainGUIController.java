package everythingElse;

//import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
//import java.io.InputStreamReader;
import java.io.PrintWriter;
//import java.net.Socket;
//import java.util.concurrent.ArrayBlockingQueue;
import java.util.Scanner;

//import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
//import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class MainGUIController {

		@FXML Button createProject;
		@FXML TextField projectName;
		@FXML TabPane projects;
		String username;
		Server server;
		static int PORT = 8881; // gonna use 8881 as the port for now

		@FXML
		public void initialize(String username) throws IOException {
			new Thread(() -> {
				try {
					this.username = username;
					server = new Server(MainGUIController.PORT, this.username);
					server.listen();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}).start();

		}


	@FXML
	void createNewProject() {
		if (!projectName.getText().equals("")) {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(MainGUIController.class.getResource("Project_GUI.fxml"));
				AnchorPane root = (AnchorPane) loader.load();

				ProjectGuiController projCtrl = (ProjectGuiController) loader.getController();

				Tab newProject = new Tab();
				
				File f = new File("Test_Store.txt");
				PrintWriter printer = new PrintWriter(new FileWriter(f, true));
				printer.println(projectName.getText());
				printer.close();
				
				newProject.setText(projectName.getText());
				newProject.setContent(root);
				projects.getTabs().add(projects.getTabs().size() - 1, newProject);
				projectName.setText("");
				newProjectSetup(projCtrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Alert alert = new Alert(AlertType.ERROR, "Invalid Project Name");
			alert.showAndWait();
		}
	}

	public void new_tab(AnchorPane root) {
		Tab newProject = new Tab();
			newProject.setText(projectName.getText());
			newProject.setContent(root);
			projects.getTabs().add(projects.getTabs().size() - 1, newProject);
<<<<<<< HEAD
		}
		
		public Scanner get_tabs() throws FileNotFoundException {
			File f = new File("Test_Store.txt");
			Scanner input = new Scanner(f);
				//System.out.print(f.getName());
			return input;
		}
=======
	}

>>>>>>> master
// TODO: come back to this after jack finishes project controller class
		@FXML
		void newProjectSetup(ProjectGuiController projCtrl) {

		}

		public void setUsername(String username) {
			this.username = username;
		}
}
