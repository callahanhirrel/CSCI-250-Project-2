package network;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class ClientController {

		@FXML Button createProject;
		@FXML Button connect;
		@FXML TextField projectName;
		@FXML TextField ip;
		@FXML Label message;
		@FXML TabPane projects;

		@FXML
		public void initialize() {
			resetGUI();
		}

		@FXML
		void createNewProject() {
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(ClientController.class.getResource("Project_GUI.fxml"));
				AnchorPane root = (AnchorPane)loader.load();

				ProjectController projCtrl = (ProjectController)loader.getController();

				Tab newProject = new Tab();
				newProject.setText(projectName.getText());
				newProject.setContent(root);
				projects.getTabs().add(projects.getTabs().size() - 1, newProject);
				resetGUI();
				newProjectSetup(projCtrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@FXML // TODO: come back to this after implementing project controller class
		void newProjectSetup(ProjectController projCtrl) {

		}

		@FXML
		private void resetGUI() {
			createProject.setDisable(true);
			message.setText("");
			projectName.setText("");
			ip.setText("");
		}
}
