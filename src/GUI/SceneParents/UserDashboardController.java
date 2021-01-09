package GUI.SceneParents;

import GUI.WelcomeController;
import GUI.DataHolders.ManagersStorage;
import GUI.OrganizerGUI.ModifyEvent.EditEventController;
import GUI.OrganizerGUI.ModifyEvent.ModifySpeakerController;
import GUI.DataHolders.UserHolder;
import UseCases.EventManager;
import UseCases.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public abstract class UserDashboardController implements Observer {
    private String username;
    private SubScene subScene;
    private UserManager userManager;
    private EventManager eventManager;
    private FXMLLoader loader;

    @FXML private GridPane gridPane;
    @FXML private Label profile;

    public void initData(String path){
        this.userManager = ManagersStorage.getInstance().getUserManager();
        this.eventManager = ManagersStorage.getInstance().getEventManager();
        this.username = UserHolder.getInstance().getUsername();
        profile.setText(userManager.getName(username));
        loadSubScene(path);
        gridPane.add(subScene, 1, 0);
    }

    public String getUsername() {
        return username;
    }

    public SubScene getSubScene() {
        return subScene;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    /**
     * Handles action when the logout button is clicked. Reverts back to welcome scene.
     */
    @FXML public void handleLogOutButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUI/Welcome.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.hide();

        stage.setScene(scene);
        stage.show();
    }

    /**
     * Loads the subscene of the given path
     *
     * @param path path of subscene fxml file
     */
    public void loadSubScene(String path){
        loader = new FXMLLoader(getClass().getResource(path + ".fxml"));

        Parent root = null;
        try {
            root = loader.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        if(subScene == null) {
            subScene = new SubScene(root, 900, 700);
        }else{
            subScene.setRoot(root);
        }
    }

    /**
     * This class is an observer. This method is an abstract update for its children.
     *
     * @param o the class that this class observes
     * @param arg an arbitrary argument
     */
    @Override
    public abstract void update(Observable o, Object arg);

    /**
     * Adds this class to the list of observers of the DisplayEventController currently associated with loader.
     */
    public void observeDisplayEvents(){
        DisplayEventsController controller = loader.getController();
        controller.addObserver(this);
    }

    /**
     * Adds this class to the list of observers of the ModifySpeakerController currently associated with loader.
     */
    public void observeModifySpeaker(){
        ModifySpeakerController controller = loader.getController();
        controller.addObserver(this);
    }

    /**
     * Adds this class to the list of observers of the EditEventController currently associated with loader.
     */
    public void observeEditEvent(){
        EditEventController controller = loader.getController();
        controller.addObserver(this);
    }
}
