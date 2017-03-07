package everythingElse;

import java.io.File;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class audioPlaybackGUIController {
	
	@FXML
	VBox addedBox;
	@FXML
	VBox receivedBox;
	
	@FXML
	void initialize(ArrayList<String> addedFiles,
			ArrayList<String> receivedFiles) {
		addFilesToBox(addedFiles, addedBox);
		addFilesToBox(receivedFiles, receivedBox);
	}
	
	@FXML
	void addFilesToBox(ArrayList<String> files, VBox destination) {
		for (String fileName : files) {
			Label label = createAudioFileLabel(fileName);
			destination.getChildren().add(label);
		}
	}
	
	@FXML
	Label createAudioFileLabel(String fileName) {
		Label label = new Label();
		label.setText(fileName);
		label.setOnMouseClicked(event -> {
			playAudioFile(label.getText());
		});
		return label;
	}
	
	public void playAudioFile(String fileName) {
		Media media = new Media(new File(fileName).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
	}

}
