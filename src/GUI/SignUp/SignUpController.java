package GUI.SignUp;

import Entities.UserType;
import GUI.WelcomeController;
import GUI.DataHolders.ManagersStorage;
import UseCases.UserManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The scene for signing up accounts
 */
public class SignUpController{
    private UserManager userManager;

    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private TextField passwordFieldRe;
    @FXML private TextField nameField;
    @FXML private Text prompt;
    @FXML private ToggleGroup group;
    @FXML private RadioButton radioButton;
    @FXML private RadioButton radioButton2;

    /**
     * Initializes the Signup scene.
     */
    public void initialize(){
        this.userManager = ManagersStorage.getInstance().getUserManager();
    }

    /**
     * Handles action of when the sign up button is clicked. Signups account
     */
    @FXML protected void handleSignUpButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String passwordRe = passwordFieldRe.getText();
        String name = nameField.getText();
        if(username.isEmpty() || password.isEmpty() || passwordRe.isEmpty() || name.isEmpty()){
            prompt.setText("Please complete the fields.");
        } else if(userManager.isRegistered(username)){
            prompt.setText("This username is already registered.");
        } else if(!password.equals(passwordRe)){
            prompt.setText("Your passwords do not match.");
        } else{
            UserType userType = null;
            if(radioButton.isSelected()){
                userType = UserType.ATTENDEE;
            }else if(radioButton2.isSelected()){
                userType = UserType.ORGANIZER;
            }
            userManager.registerUser(userType, name, username, password);
            prompt.setText("Signed Up!");
        }
    }

    /**
     * Handles action of when the check availability button is clicked. Checks if username is available
     */
    @FXML protected void handleCheckAvailableButtonAction(ActionEvent event) {
        if(userManager.isRegistered(usernameField.getText())) {
            prompt.setText("This username is already registered.");
        }else if(usernameField.getText().isEmpty()){
            prompt.setText("Please enter an username.");
        }else{
            prompt.setText("This username is available!");
        }
    }

    /**
     * Handles action of when the back button is clicked. Switches display back to Welcome scene
     */
    @FXML protected void handleBackButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/Welcome.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }
}
