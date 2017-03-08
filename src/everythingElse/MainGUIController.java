package everythingElse;

//import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
//import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
//import java.net.Socket;
//import java.util.concurrent.ArrayBlockingQueue;
import java.util.Scanner;

//import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
//import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


public class MainGUIController {
		FileChecker fileChecker = new FileChecker();

		@FXML Button createProject;
		@FXML TextField projectName;
		@FXML TabPane projects;
		static String USERNAME;
		Server server;
		static int PORT = 8881; // gonna use 8881 as the port for now

		@FXML
		public void initialize(String username) throws IOException {
			new Thread(() -> {
				try {
					MainGUIController.USERNAME = username;
					server = new Server(MainGUIController.PORT, MainGUIController.USERNAME);
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
				File f = new File("Test_Store.txt");
				//File f2 = new File("Current_Work.txt");
				PrintWriter printer = new PrintWriter(new FileWriter(f, true));
				//System.out.println(fileChecker.check_existence("Test_Store.txt", projectName.getText()));
				if (fileChecker.check_existence("Test_Store.txt", projectName.getText()) == true) {
					printer.close();
					createAlert("Invalid Project Name");
				} else {
//					if (!fileChecker.check_existence("Current_Work.txt", projectName.getText()) && projectName.getText() != "") {
//						FileWriter writer = new FileWriter(f2, false);
//						writer.write(projectName.getText());
//						writer.close();
//					}
					printer.println(projectName.getText());
					printer.close();
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(MainGUIController.class.getResource("Project_GUI.fxml"));
					AnchorPane root = (AnchorPane) loader.load();

					ProjectGuiController projCtrl = (ProjectGuiController) loader.getController();

					Tab newProject = new Tab();
					//newProject.setId(projectName.getText());
					newProject.setText(projectName.getText());
					newProject.setContent(root);
					projects.getTabs().add(projects.getTabs().size() - 1, newProject);
					projectName.setText("");
					newProjectSetup(projCtrl);
				}
			} catch (IOException e) {
				createAlert("Invalid Project Name");
				e.printStackTrace();
			}
		} else {
			createAlert("Invalid Project Name");
		}
	}
	private void createAlert(String message) {
		Alert alert = new Alert(AlertType.ERROR, message, ButtonType.OK);
		alert.showAndWait();
	}

//	public void new_tab(AnchorPane root, String project_name) {
//		Tab newProject = new Tab();
//		newProject.setId(project_name);
//			newProject.setText(project_name);
//			newProject.setContent(root);
//			projects.getTabs().add(projects.getTabs().size() - 1, newProject);
//			//return newProject;
//	}

//	public Scanner get_tabs() throws FileNotFoundException {
//		File f = new File("Test_Store.txt");
//		Scanner input = new Scanner(f);
//				//System.out.print(f.getName());
//		return input;
//	}
// TODO: come back to this after jack finishes project controller class
	void newProjectSetup(ProjectGuiController projCtrl) {
		projCtrl.setProjectName(projectName.getText());
	}

	public void setUsername(String username) {
		this.USERNAME = username;
	}
}
