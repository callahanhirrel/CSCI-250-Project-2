package network;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ProjectController {

	@FXML Label collaborator;
	@FXML ScrollPane addedFiles;
	@FXML ScrollPane receivedFiles;
	@FXML Button addFile;
	@FXML Button removeFromAdded;
	@FXML Button removeFromReceived;
	@FXML Button saveReceivedFile;
	@FXML TableView<LogDetails> history;
	@FXML TableColumn<TableView<LogDetails>, String> date;
	@FXML TableColumn<TableView<LogDetails>, String> time;
	@FXML TableColumn<TableView<LogDetails>, String> details;

	@FXML
	public void initialize() {
		// TODO come back to this after implementing
	}
}
