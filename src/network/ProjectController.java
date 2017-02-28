// This probably won't be used
//
//package network;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.control.ScrollPane;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.cell.PropertyValueFactory;
//
//public class ProjectController {
//
//	@FXML Label collaborator;
//	@FXML ScrollPane addedFiles;
//	@FXML ScrollPane receivedFiles;
//	@FXML Button addFile;
//	@FXML Button removeFromAdded;
//	@FXML Button removeFromReceived;
//	@FXML Button saveReceivedFile;
//	@FXML TableView<LogDetails> history;
//	@FXML TableColumn<TableView<LogDetails>, String> date;
//	@FXML TableColumn<TableView<LogDetails>, String> time;
//	@FXML TableColumn<TableView<LogDetails>, String> log;
//
//	@FXML
//	public void initialize() {
//		date.setCellValueFactory(new PropertyValueFactory<>("date"));
//		time.setCellValueFactory(new PropertyValueFactory<>("time"));
//		log.setCellValueFactory(new PropertyValueFactory<>("log"));
//	}
//}
