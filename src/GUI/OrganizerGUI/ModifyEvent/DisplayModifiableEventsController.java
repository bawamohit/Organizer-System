package GUI.OrganizerGUI.ModifyEvent;

import GUI.SceneParents.DisplayEventsController;
import GUI.DataHolders.ManagersStorage;
import UseCases.EventManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * The subscene where modifiable events are displayed
 */
public class DisplayModifiableEventsController extends DisplayEventsController {
    /**
     * Initializes the Modify Event scene.
     */
    public void initialize(){
        EventManager eventManager = ManagersStorage.getInstance().getEventManager();
        LocalDateTime currTime = LocalDateTime.now();
        List<UUID> availableEventIDs = eventManager.getAvailableEvents(currTime);
        List<List<String>> eventsInfo = eventManager.getAllEventsInfo(availableEventIDs);
        generateEventButtons("EventInfoModify", eventsInfo);
    }
}
