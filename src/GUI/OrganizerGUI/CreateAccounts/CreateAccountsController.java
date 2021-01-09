package GUI.OrganizerGUI.CreateAccounts;

import Entities.UserType;
import GUI.WelcomeController;
import GUI.DataHolders.ManagersStorage;
import UseCases.UserManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;

/**
 * The subscene where organizers can create accounts
 */
public class CreateAccountsController {
    private UserManager userManager;

    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private TextField passwordFieldRe;
    @FXML private TextField nameField;
    @FXML private ToggleGroup group;
    @FXML private RadioButton radioButton;
    @FXML private RadioButton radioButton2;
    @FXML private RadioButton radioButton3;
    @FXML private RadioButton radioButton4;

    /**
     * Initializes the Create Accounts scene.
     */
    public void initialize(){
        this.userManager = ManagersStorage.getInstance().getUserManager();
    }

    /**
     * Handles action when the signup button is clicked. Signs up the account.
     */
    @FXML protected void handleSignUpButtonAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String passwordRe = passwordFieldRe.getText();
        String name = nameField.getText();
        if(username.isEmpty() || password.isEmpty() || passwordRe.isEmpty() || name.isEmpty()){
            createAlert("Please complete the fields.");
        } else if(userManager.isRegistered(username)){
            createAlert("This username is already registered.");
        } else if(!password.equals(passwordRe)){
            createAlert("Your passwords do not match.");
        } else{
            UserType userType = null;
            if(radioButton.isSelected()){
                userType = UserType.ATTENDEE;
            }else if(radioButton2.isSelected()){
                userType = UserType.ORGANIZER;
            }else if(radioButton3.isSelected()){
                userType = UserType.ADMIN;
            }else if(radioButton4.isSelected()){
                userType = UserType.SPEAKER;
            }
            userManager.registerUser(userType, name, username, password);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Account created!");
            alert.showAndWait();
        }
    }

    /**
     * Handles action when the check availability button is clicked. Checks if username is available.
     */
    @FXML protected void handleCheckAvailableButtonAction() {
        if(userManager.isRegistered(usernameField.getText())) {
            createAlert("This username is already registered.");
        }else if(usernameField.getText().isEmpty()){
            createAlert("Please enter an username.");
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("This username is available!");
            alert.showAndWait();
        }
    }

    /**
     * Displays a pop-up message.
     */
    private void createAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
