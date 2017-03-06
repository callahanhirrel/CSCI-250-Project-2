package everythingElse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(MainGUIController.class.getResource("Project_GUI.fxml"));
				AnchorPane root = (AnchorPane)loader.load();

				ProjectGuiController projCtrl = (ProjectGuiController)loader.getController();

				Tab newProject = new Tab();
				newProject.setText(projectName.getText());
				newProject.setContent(root);
				projects.getTabs().add(projects.getTabs().size() - 1, newProject);
				projectName.setText("");
				newProjectSetup(projCtrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void new_tab(AnchorPane root) {
			Tab newProject = new Tab();
			newProject.setText(projectName.getText());
			newProject.setContent(root);
			projects.getTabs().add(projects.getTabs().size() - 1, newProject);
		}
// TODO: come back to this after jack finishes project controller class
		@FXML
		void newProjectSetup(ProjectGuiController projCtrl) {

		}

		public void setUsername(String username) {
			this.username = username;
		}
}
