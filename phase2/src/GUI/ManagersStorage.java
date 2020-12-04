package GUI;

import Gateways.EventGateway;
import Gateways.MessageGateway;
import Gateways.UserGateway;
import UseCases.EventManager;
import UseCases.MessageManager;
import UseCases.UserManager;

import java.io.File;
import java.io.IOException;

public final class ManagersStorage {
    private UserGateway userGateway;
    private EventGateway eventGateway;
    private MessageGateway messageGateway;
    private File userInfo = new File("./phase2/src/Data/userManager.json");
    private File eventInfo = new File("./phase2/src/Data/eventManager.json");
    private File messageInfo = new File("./phase2/src/Data/messageManager.json");
    private UserManager userManager;
    private EventManager eventManager;
    private MessageManager messageManager;

    private final static ManagersStorage INSTANCE = new ManagersStorage();

    private ManagersStorage(){
        userGateway = new UserGateway();
        eventGateway = new EventGateway();
        messageGateway = new MessageGateway();
        try {
            userManager = userGateway.readFromFile(userInfo.getPath());
            eventManager = eventGateway.readFromFile(eventInfo.getPath());
            messageManager = messageGateway.readFromFile(messageInfo.getPath());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static ManagersStorage getInstance(){
        return INSTANCE;
    }

    public UserManager getUserManager(){
        return userManager;
    }

    public EventManager getEventManager(){
        return eventManager;
    }

    public MessageManager getMessageManager(){
        return messageManager;
    }

    public void save(){
        try {
            userGateway.saveToFile(userInfo.getPath(), userManager);
            eventGateway.saveToFile(eventInfo.getPath(), eventManager);
            messageGateway.saveToFile(messageInfo.getPath(), messageManager);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
