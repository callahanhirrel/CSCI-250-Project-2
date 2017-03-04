package everythingElse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class ProjectGuiController {
	//public List<File> arrays = new ArrayList<>();
	//private ArrayList<String> transfer = new ArrayList<>();

	@FXML VBox fileContainer;
	@FXML Button addFile;
	@FXML Button rmFile;
	@FXML ScrollPane hasAddedFile;
	@FXML TextField ip;
	@FXML Label message;
	@FXML Button connect;
	ArrayBlockingQueue<String> dataCollection = new ArrayBlockingQueue<>(20);
	HashMap<String, String> users = new HashMap(); // maps usernames to the IP addresses they came from


	public void initialize() throws IOException {
		new Thread(() -> {
			for (;;) {
				try {
					String data = dataCollection.take();
					useData(data);
				} catch(Exception e) {
					// TODO Platform.runLater(() -> alert method?);
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
	private void useData(String data) {
		if (data.toLowerCase().equals("connection open")) {
			Platform.runLater(() -> {message.setText(confirmConnection());});
		}
	}

	/**
	 * This method is called upon initial connection with a peer.
	 * The peer's username and IP address are added to a HashMap field
	 *
	 * @return	a confirmation message to be displayed in the GUI
	 */
	private String confirmConnection() {
		String message = "ERROR: PLEASE TRY AGAIN";
		String connectedWith;
		try {
			connectedWith = dataCollection.take();
			message = "You are now connected with " + connectedWith;
			users.put(connectedWith, ip.getText()); // save the username and IP for later use
		} catch (InterruptedException e) {
			// TODO Platform.runLater(() -> alert method?);
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * First method that is called when a user tries to connect with a peer
	 */
	@FXML
	public void connect() {
		new Thread(() -> {
			try {
				Socket target = new Socket(ip.getText(), MainGUIController.PORT);
				sendRequest(target, "requesting connection");
				receiveData(target);
				target.close();
			} catch (Exception e) {
				// TODO Platform.runLater(() -> alert method?);
				e.printStackTrace();
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
	private void sendRequest(Socket target, String request) throws IOException {
		PrintWriter sockout = new PrintWriter(target.getOutputStream());
		sockout.println(request);
		sockout.flush();
	}

	/**
	 * Waits for a response from a peer's server, then adds the server data
	 * to a blocking queue.
	 *
	 * @param target	the peer's socket
	 * @throws IOException
	 */
	private void receiveData(Socket target) throws IOException {
		BufferedReader sockin = new BufferedReader(new InputStreamReader(target.getInputStream()));
		while (!sockin.ready()) {}
		while (sockin.ready()) {
			try {
				String data = sockin.readLine();
				System.out.println("Client: Received [" + data + "]");
				dataCollection.add(data);
			} catch(Exception e) {
				// TODO Platform.runLater(() -> alert method?);
				e.printStackTrace();
			}
		}
	}

	public void show_file(String filename) {
		Label label = new Label(filename);
		fileContainer.getChildren().add(label);
	}

	// TODO split this method up into smaller helper methods
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
			loader.setLocation(ProjectGuiMain.class.getResource("Project_GUI.fxml"));
			loader2.setLocation(LoginGuiController.class.getResource("Main_GUI.fxml"));
			AnchorPane root2 = (AnchorPane) loader2.load();
			AnchorPane root = (AnchorPane) loader.load();

			ProjectGuiController pgc = (ProjectGuiController)loader.getController();
			MainGUIController Client = (MainGUIController)loader2.getController();
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

	public void playAudioFile(String fileName) {
		Media media = new Media(new File(fileName).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
	}
}
