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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
		String currentUsername = "";
		static int PORT = 8881;
		ArrayBlockingQueue<String> dataCollection = new ArrayBlockingQueue<>(20);
		String yourUsername;

		@FXML
		public void initialize() {
			new Thread(() -> {
				for (;;) {
					try {
						String username = dataCollection.take();
						Platform.runLater(() -> {message.setText("You are now connected with " + username);
						createProject.setDisable(false);});
					} catch(Exception e) {
						//TODO Platform.runLater(() -> alert method?);
						e.printStackTrace();
					}
				}
			}).start();
		}

		void connect() {
			new Thread(() -> {
				try {
					Socket target = new Socket(ip.getText(), ClientController.PORT);
					requestConnection(target);
					confirmConnection(target);
					target.close();
				} catch (Exception e) {
					//TODO Platform.runLater(() -> alert method?);
					e.printStackTrace();
				}
			}).start();
		}

		void requestConnection(Socket target) throws IOException {
			PrintWriter sockout = new PrintWriter(target.getOutputStream());
			sockout.println("Requesting connection");
			sockout.flush();
		}

		void confirmConnection(Socket target) throws IOException {
			BufferedReader sockin = new BufferedReader(new InputStreamReader(target.getInputStream()));
			while (!sockin.ready()) {}
			while (sockin.ready()) {
				try {
					String data = sockin.readLine();
					dataCollection.add(data);
				} catch(Exception e) {
					//TODO Platform.runLater(() -> alert method?);
					e.printStackTrace();
				}
			}
		}

	@FXML
	void createNewProject() {
		if (!projectName.getText().equals("")) {
			try {
				System.out.println("made it this far");
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(ClientController.class.getResource("Project_GUI.fxml"));
				AnchorPane root = (AnchorPane) loader.load();

				ProjectGuiController projCtrl = (ProjectGuiController) loader.getController();
				System.out.println("created da gui");
				Tab newProject = new Tab();
				newProject.setText(projectName.getText());
				newProject.setContent(root);
				projects.getTabs().add(projects.getTabs().size() - 1, newProject);
				resetGUI();
				newProjectSetup(projCtrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {

			Alert alert = new Alert(AlertType.ERROR, "invalid project name", ButtonType.OK);
			alert.showAndWait();

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

		public void setYourUsername(String name) {
			this.yourUsername = name;
		}

		@FXML
		private void resetGUI() {
			createProject.setDisable(true);
			message.setText("");
			projectName.setText("");
			ip.setText("");
		}

		public void setUsername(String username) {
			currentUsername = username;
		}
}
