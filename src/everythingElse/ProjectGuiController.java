package everythingElse;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ProjectGuiController {

	FileChecker fileChecker = new FileChecker();
	@FXML VBox fileContainer;
	@FXML VBox receivedFiles;
	@FXML Button addFile;
	@FXML Button rmFile;
	@FXML Button send;
	@FXML Button playButton;
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
										MainGUIController.USERNAME, filename.getText());
								sendRequest(target, request);
								target.close();
								confirmFileSent();
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

	}

	private void displayReceived() {
		new Thread(() -> {
			for (;;) {
				String path = System.getProperty("user.dir") + "/receivedFiles/";
				File dir = new File(path);
				File[] directoryListing = dir.listFiles();
				String name = directoryListing[directoryListing.length - 1].getName();
				Label toDisplay = new Label(name);
				receivedFiles.getChildren().add(toDisplay);
			}
		}).start();

	}

	private void getError(String error) {
		Alert alert = new Alert(AlertType.ERROR, error, ButtonType.OK);
		alert.showAndWait();
	}

	public void show_file(String filename) {
		Label label = new Label(filename);
		if (label.getText().endsWith(".aif")) {
			label.setTextFill(Color.RED);
		}
		fileContainer.getChildren().add(label);
	}

	@FXML
	void add_file() {
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle(addFile.getText());
			fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Audio Files", "*.aif"));

			List<File> list = fileChooser.showOpenMultipleDialog(addFile.getScene().getWindow());
			if (list != null) {

				File dir = new File("new_folder");
				dir.mkdir();
				for (File file : list) {
					String filename = fileChecker.check_file(file, dir);
					Files.copy(file.toPath(), (new File(dir.getPath() + "/" + filename)).toPath(),
							StandardCopyOption.REPLACE_EXISTING);
					Label label = new Label(filename);
					fileContainer.getChildren().add(label);
				}
			}

		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	public void setProjectName(String name) {
		this.projectName = name;
	}

	@FXML
	void confirmFileSent() {
		Alert alert = new Alert(AlertType.CONFIRMATION, "File Successfully Sent", ButtonType.OK);
		alert.showAndWait();
	}
	
	@FXML
	public void playAudioFile() {
		new Thread(() -> {
			for (Node item : fileContainer.getChildren()) {
				Label filename = (Label) item;
				filename.setOnMouseClicked(new EventHandler<MouseEvent>() {

					@Override
					public void handle(MouseEvent event) {
						Media media = new Media(
								new File(System.getProperty("user.dir") + "/new_folder/" + filename.getText()).toURI()
										.toString());
						MediaPlayer mediaPlayer = new MediaPlayer(media);
						mediaPlayer.play();
						playButton.setText("Stop Audio");
						playButton.setOnAction(event2 -> {
							mediaPlayer.stop();
							playButton.setText("Play Audio");
							playButton.setOnAction(event3 -> {
								playAudioFile();
							});
						});
					}
				});
			}
		}).start();
	}
}
