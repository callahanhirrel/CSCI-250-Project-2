package everythingElse;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
		Output.setEditable(false);
		
	}
	
	@FXML
	void LogOut() {
		if (currentUser.getText().equals("")) {
			Output.setText("No user logged in");
		} else {
			Output.setText(currentUser.getText() + " logged out");
			currentUser.setText("");
		}
	}
	
	@FXML
	void SignUp() {
		if (setUsername.getText().equals("")) {
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
				setSignUpTextBlank();
			}
		}
	}
	
	@FXML
	void SignIn() {
		if (!userSignedIn()) {
			if (users.containsUser(username.getText())) {
				String signInPassword = password.getText();
				String usernamePassword = username.getText();
				if (signInPassword.equals(usernamePassword)) {
					currentUser.setText(username.getText());
					Output.setText(username.getText() + " successfully logged in");
					setSignInTextBlank();
				}

			} else {
				setSignInTextBlank();
				Output.setText("Invalid sign in information");
			}
		} else {
			Output.setText("User already signed in");
		}
	}
	
	public boolean userSignedIn() {
		return !currentUser.getText().equals("");
	}
	
	public void setSignInTextBlank() {
		username.setText("");
		password.setText("");
	}
	
	public void setSignUpTextBlank() {
		setUsername.setText("");
		setPassword.setText("");
		confirmPassword.setText("");
	}
	
}
