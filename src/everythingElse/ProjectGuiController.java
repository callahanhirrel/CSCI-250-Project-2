package everythingElse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
//import java.nio.file.Files;
//import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;


public class ProjectGuiController {
	//public List<File> arrays = new ArrayList<>();
	//private ArrayList<String> transfer = new ArrayList<>();
	FileChecker fileChecker = new FileChecker();
	//@FXML Tab tabMaster;
	@FXML VBox fileContainer;
	@FXML Button addFile;
	@FXML Button rmFile;
	@FXML Button send;
	@FXML ScrollPane hasAddedFile;
	@FXML TextField ip;
	@FXML Label message;
	@FXML Button connect;
	@FXML Label collaboraters;
	ArrayBlockingQueue<NetworkData> dataCollection = new ArrayBlockingQueue<>(20);
	HashMap<String, String> users = new HashMap<>(); // maps usernames to the IP addresses they came from
	private String projectName;


	public void initialize() throws IOException {
		new Thread(() -> {
			for (;;) {
				try {
					NetworkData data = dataCollection.take();
					unpackageData(data);
				} catch(Exception e) {
					Platform.runLater(() -> getError(e.getMessage()));
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * Determines what to do with the data received by peers
	 *
	 * @param data	the next piece of data from the blocking queue
	 */
	private void unpackageData(NetworkData data) {
		if (data.getTag().equals(NetworkData.USERNAME_TAG)) {
			Platform.runLater(() -> {message.setText(confirmConnection(data));});
		}
	}

	@FXML
	void collaborators() {
		String userReturn = "";
		for (String user : users.keySet()) {
			userReturn = userReturn.concat(user);
		}
		collaboraters.setText(userReturn);
	}

	/**
	 * This method is called upon initial connection with a peer.
	 * The peer's username and IP address are added to a HashMap field
	 *
	 * @param	a NetworkData object
	 * @return	a confirmation message to be displayed in the GUI
	 */
	private String confirmConnection(NetworkData data) {
		String connectedWith = data.getUsername();
		String msg = "You are now connected with " + connectedWith;
		users.put(connectedWith, ip.getText()); // save the username and IP for later use
		collaborators();
		return msg;
	}

	/**
	 * First method that is called when a user tries to connect with a peer
	 */
	@FXML
	public void connect() {
		new Thread(() -> {
			try {
				Socket target = new Socket(ip.getText(), MainGUIController.PORT);
				NetworkData request = new NetworkData(NetworkData.MSG_TAG,
						MainGUIController.USERNAME, "requesting connection");
				sendRequest(target, request);
				receiveData(target);
				target.close();
			} catch (Exception e) {
				Platform.runLater(() -> getError(e.getMessage()));
				e.printStackTrace();
			}
		}).start();
	}

	@FXML
	public void sendFile() {
		new Thread (() -> {
			for (Node item : fileContainer.getChildren()) {
				Label filename = (Label) item;
				filename.setOnMouseClicked(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {
						for (String username : users.keySet()) {
							try {
								Socket target = new Socket(users.get(username), MainGUIController.PORT);
								NetworkData request = new NetworkData(NetworkData.FILE_TAG,
										MainGUIController.USERNAME, projectName + "/" +
										filename.getAccessibleText());
								sendRequest(target, request);
								target.close();
							} catch (Exception e) {
								Platform.runLater(() -> getError(e.getMessage()));
								e.printStackTrace();
							}
						}
					}
				});
			}
		}).start();
	}


	/**
	 * Sends a request to a peer's server
	 *
	 * @param target	the peer's socket
	 * @param request	the data/request being sent
	 * @throws IOException
	 */
	private void sendRequest(Socket target, NetworkData request) throws IOException {
		ObjectOutputStream sockout = new ObjectOutputStream(target.getOutputStream());
		sockout.writeObject(request);
		sockout.flush();
		System.out.println("Client: Sent [" + request.getTag() + "]");
	}

	/**
	 * Waits for a response from a peer's server, then adds the server data
	 * to a blocking queue.
	 *
	 * @param target	the peer's socket
	 * @throws IOException
	 */
	private void receiveData(Socket target) throws IOException {
		ObjectInputStream sockin = new ObjectInputStream(target.getInputStream());
		try {
			NetworkData data = (NetworkData) sockin.readObject();
			System.out.println("Client: Received [" + data.getTag() + "]");
			dataCollection.add(data);
		} catch(Exception e) {
			Platform.runLater(() -> getError(e.getMessage()));
			e.printStackTrace();
		}

//		BufferedReader sockin = new BufferedReader(new InputStreamReader(target.getInputStream()));
//		while (!sockin.ready()) {}
//		while (sockin.ready()) {
//			try {
//				String data = sockin.readLine();
//				System.out.println("Client: Received [" + data + "]");
//				dataCollection.add(data);
//				System.out.print(dataCollection.toString());
//			} catch(Exception e) {
//				Platform.runLater(() -> getError(e.getMessage()));
//				e.printStackTrace();
//			}
//		}
	}

	private void getError(String error) {
		Alert alert = new Alert(AlertType.ERROR, error, ButtonType.OK);
		alert.showAndWait();
	}

	public void show_file(String filename) {
		Label label = new Label(filename);
		fileContainer.getChildren().add(label);
	}

	// TODO split this method up into smaller helper methods

	@FXML
	void add_file() {
		try{
			/*
			ArrayList<String> trans = new ArrayList<>();
			//addFile.setText("Test");
			for (int j = 0; j < arrays.size(); j++) {
				trans.add(arrays.get(j));
			}
			*/
//			FXMLLoader loader2 = new FXMLLoader();
//
//			loader2.setLocation(LoginGuiController.class.getResource("Main_GUI.fxml"));
//			AnchorPane root2 = (AnchorPane) loader2.load();
//
//
//			MainGUIController Client = (MainGUIController)loader2.getController();
//			/*
//			for (File files : arrays) {
//				pgc.arrays.add(files);
//			}
//			*/
//			Scanner scan1 = Client.get_tabs();
//			while (scan1.hasNextLine()) {
//				//System.out.print(line);
//				String name1 = scan1.nextLine();
//				FXMLLoader loader1 = new FXMLLoader();
//				loader1.setLocation(ProjectGuiController.class.getResource("Project_GUI.fxml"));
//				AnchorPane root1 = (AnchorPane) loader1.load();
//				ProjectGuiController pgc1 = (ProjectGuiController)loader1.getController();
//				Client.new_tab(root1, name1);
//
//				File f1 = new File(name1 + ".txt");
//				try {
//					Scanner input1 = new Scanner(f1);
//				//System.out.print(f.getName());
//					while (input1.hasNextLine()) {
//						String y = input1.nextLine();
//					//System.out.print(line);
//						pgc1.show_file(y);
//					}
//
//					input1.close();
//
//				} catch (Exception exc) {
//					exc.printStackTrace();
//				}
//			}
//
//			Stage stage = new Stage();
//			Scene scene = new Scene(root2);
//			stage.setScene(scene);
//			stage.show();

			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle(addFile.getText());
			fileChooser.getExtensionFilters().addAll(
					new ExtensionFilter("Audio Files", "*.aif")
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
			List<File> list = fileChooser.showOpenMultipleDialog(addFile.getScene().getWindow());

			if (list != null) {
				//System.out.println(dir);
				//add.add(file.getName());
				/*
				for (int k = 0; k < trans2.size(); k++) {
					arrays.add(trans2.get(k));
				}
				*/
//				Scanner scan = new Scanner(new File("Current_Work.txt"));
//				while (scan.hasNextLine()) {
//					//System.out.print(line);
//					String name = scan.nextLine();
//					FXMLLoader loader = new FXMLLoader();
//					loader.setLocation(ProjectGuiController.class.getResource("Project_GUI.fxml"));
//					AnchorPane root = (AnchorPane) loader.load();
//					ProjectGuiController pgc = (ProjectGuiController)loader.getController();
//					Client.new_tab(root, name);
					File dir = new File("new_folder");
					dir.mkdir();

					File f = new File("save.txt");
					PrintWriter printer = new PrintWriter(new FileWriter(f, true));

					//if (project_name.isSelected()) {

						for (File file : list) {
						//System.out.println(fileChecker.check_file(file, dir));
							String filename = fileChecker.check_file(file, dir);
							Files.copy(file.toPath(), (new File(dir.getPath() + "/" + filename)).toPath(), StandardCopyOption.REPLACE_EXISTING);
							Label label = new Label(filename);
							fileContainer.getChildren().add(label);
							printer.println(filename);

						}

						printer.close();
//
//						try {
//							Scanner input = new Scanner(f);
//						//System.out.print(f.getName());
//							while (input.hasNextLine()) {
//								String x = input.nextLine();
//							//System.out.print(line);
//								pgc.show_file(x);
//							}
//
//							input.close();
//
//						} catch (Exception exc) {
//							exc.printStackTrace();
//						}

//					} else {
//						printer.close();
//						try {
//							Scanner input = new Scanner(f);
//						//System.out.print(f.getName());
//							while (input.hasNextLine()) {
//								String x = input.nextLine();
//							//System.out.print(line);
//								pgc.show_file(x);
//							}
//
//							input.close();
//
//						} catch (Exception exc) {
//							exc.printStackTrace();
//						}
//					}

//				}
//				scan.close();
			}

		} catch (Exception exc) {
			exc.printStackTrace();
		}


	}

	@FXML
	ArrayList<String> getAddedFiles() {
		ArrayList<String> addedFiles = new ArrayList<String>();
		for (Node item : fileContainer.getChildren()) {
			Label label = (Label) item;
			if (label.getText().endsWith(".mp3")) {
				addedFiles.add(label.getText());
			}
		}
		return addedFiles;
	}

	@FXML
	void openAudioPlaybackWindow() {
		ArrayList<String> addedFiles = getAddedFiles();
		ArrayList<String> receivedFiles = new ArrayList<String>();
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(audioPlaybackGUIController.class.getResource("audioPlaybackGUI.fxml"));
			AnchorPane root = (AnchorPane) loader.load();
			everythingElse.audioPlaybackGUIController apgc = (everythingElse.audioPlaybackGUIController) loader.getController();
			Stage stage = new Stage();
			Scene scene = new Scene(root);
			apgc.initialize(addedFiles, receivedFiles);
			stage.setScene(scene);
			stage.show();
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
	public void setProjectName(String name) {
		this.projectName = name;
	}
}
