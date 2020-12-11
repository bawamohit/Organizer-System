package GUI.AdminGUI;

import GUI.DisplayEventsController;
import GUI.EventInfoController;
import GUI.ManagersStorage;
import UseCases.EventManager;

import java.util.List;
import java.util.UUID;

public class DisplayDeletableEventsController extends DisplayEventsController {
    private EventManager eventManager;

    public void initialize(){
        this.eventManager = ManagersStorage.getInstance().getEventManager();
        List<UUID> deletableEventIds = eventManager.getEmptyEvents();
        List<List<String>> eventsInfo = eventManager.getAllEventsInfo(deletableEventIds);
        generateEventButtons("/GUI/AdminGUI/EventInfoDelete", eventsInfo);
    }

}
