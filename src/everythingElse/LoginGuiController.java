package everythingElse;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginGuiController {

	@FXML
	Button logOut;

	@FXML
	Label currentUser;

	String Username = "";

	boolean userLoggedIn;

	@FXML
	TextField Output;

	@FXML
	TextField username;

	@FXML
	PasswordField password;

	@FXML
	TextField setUsername;

	@FXML
	PasswordField setPassword;

	@FXML
	PasswordField confirmPassword;

	@FXML
	Button signIn;

	@FXML
	Button signUp;

	User users = new User();

	public void initialize() {
		users = User.loadUsers();
		Output.setEditable(false);
	}

	@FXML
	void SignUp() {
		if (isEmptyString(setUsername)) {
			Output.setText("Invalid Username");
		} else {
			if (users.containsUser(setUsername.getText())) {
				Output.setText("Username is taken");
				setSignUpTextBlank();
			} else {
				if (setPassword.getText().equals(confirmPassword.getText())) {
					users.add(setUsername.getText(), confirmPassword.getText());
					Output.setText("Account Successfully Created");
				} else {
					Output.setText("Passwords don't match");
				}
				User.saveUsers(users);
				setSignUpTextBlank();
			}
		}
	}

	@FXML
	void SignIn() {
		if (users.containsUser(username.getText())) {
			String signInPassword = password.getText();
			String usernamePassword = users.getHashMap().get(username.getText());

			if (passwordsEqual(signInPassword, usernamePassword)) {
				currentUser.setText(username.getText());
				Output.setText(username.getText() + " successfully logged in");
				Username = username.getText();
				setSignInTextBlank();
				createMainGuiWindow();
			}
			setSignInTextBlank();
			currentUser.setText("");
			Output.setText("");

		} else {
			setSignInTextBlank();
			Output.setText("Invalid sign in information");
		}
	}
	
	private boolean passwordsEqual(String signInPassword, String usernamePassword) {
		return signInPassword.equals(usernamePassword);
	}
	
	private boolean isEmptyString(TextField field) {
		return field.getText().equals("");
	}

	private void setSignInTextBlank() {
		username.setText("");
		password.setText("");
	}

	private void setSignUpTextBlank() {
		setUsername.setText("");
		setPassword.setText("");
		confirmPassword.setText("");
	}
	
	private void createMainGuiWindow() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(LoginGuiController.class.getResource("Main_GUI.fxml"));
			AnchorPane root = (AnchorPane) loader.load();
			everythingElse.MainGUIController Client = (everythingElse.MainGUIController) loader.getController();
			Stage ClientStage = new Stage();
			Scene scene = new Scene(root);
			Client.initialize(Username);
			Client.setUsername(Username);
			ClientStage.setScene(scene);
			ClientStage.show();
			signIn.getScene().getWindow().hide();

		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}
}
