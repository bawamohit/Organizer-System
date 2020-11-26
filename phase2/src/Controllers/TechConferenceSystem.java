package Controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import JSONGateways.UserJSONGateway;
import UI.AttendeePresenter;
import UI.OrganizerPresenter;
import UI.UserPresenter;
import UseCases.*;
import Gateways.*;
import Entities.UserType;

public class TechConferenceSystem {

    private UserPresenter presenter;
    private UserManager um;
    private EventManager em;
    private MessageManager mm;
    private RoomManager rm;
    private UserGateway userGateway;
    private EventGateway eventGateway;
    private MessageGateway messageGateway;
    private RoomGateway roomGateway;
    private UserJSONGateway userJSONGateway;

    File eventManagerInfo = new File("./src/Data/eventManager.ser");
    File messageManagerInfo = new File("./src/Data/messageManager.ser");
    File userManagerInfo = new File("./src/Data/userManager.ser");
    File roomManagerInfo = new File("./src/Data/roomManager.ser");
    File userJSONManagerInfo = new File("./src/Data/userJSONManager.json");

    public TechConferenceSystem () {
        try {
            userGateway = new UserGateway();
            eventGateway = new EventGateway();
            messageGateway = new MessageGateway();
            roomGateway = new RoomGateway();
            userJSONGateway = new UserJSONGateway();

//            um = userGateway.readFromFile(userManagerInfo.getPath());
            em = eventGateway.readFromFile(eventManagerInfo.getPath());
            mm = messageGateway.readFromFile(messageManagerInfo.getPath());
            rm = roomGateway.readFromFile(roomManagerInfo.getPath());
            um = userJSONGateway.readFromFile(userJSONManagerInfo.getPath());
        } catch (ClassNotFoundException | IOException e){
            e.printStackTrace();
        }

        presenter = new UserPresenter();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        String loggedInUsername;
        while (true) {
            loggedInUsername = startUp(scanner);
            if(loggedInUsername == null) try {
                userGateway.saveToFile(userManagerInfo.getPath(), um);
                eventGateway.saveToFile(eventManagerInfo.getPath(), em);
                messageGateway.saveToFile(messageManagerInfo.getPath(), mm);
                roomGateway.saveToFile(roomManagerInfo.getPath(), rm);
                userJSONGateway.saveToFile(userJSONManagerInfo.getPath(), um);
                break;
            } catch (IOException e){
                e.printStackTrace();
            }

            UserSystem system;
            if (um.getUserType(loggedInUsername) == UserType.ATTENDEE) {
                system = new AttendeeSystem();//TODO excess objects?
            } else if (um.getUserType(loggedInUsername) == UserType.ORGANIZER) {
                system = new OrganizerSystem();
            } else {
                system = new SpeakerSystem();
            }
            system.run(loggedInUsername, this);
        }
    }

    public String signUp(Scanner scanner, UserType accountType) {
        presenter.printAskWithBack("username");
        String username = scanner.nextLine();
        if (username.equals("")) return null;
        while(um.isRegistered(username)){
            presenter.printUsernameTaken();
            username = scanner.nextLine();
            if (username.equals("")) return null;
        }

        presenter.printAsk("password");
        String password = validInput(".+", "password", scanner);
        presenter.printAsk("name");
        String name = validInput(".+", "name", scanner);

        um.registerUser(accountType, name, username, password);
        presenter.printSignUpSuccessful(name);
        return username;
    }

    public UserPresenter presenter(){
        return presenter;
    }

    public UserManager getUM(){
        return um;
    }

    public EventManager getEM(){
        return em;
    }

    public MessageManager getMM(){
        return mm;
    }

    public RoomManager getRM(){
        return rm;
    }

    private String startUp(Scanner scanner) {
        String username = null;
        label:
        do {
            presenter.printWelcome();
            String loginOrSignUp = validInput("^[012]$", "option", scanner);

            switch (loginOrSignUp) {
                case "0":
                    break label;
                case "1":
                    username = login(scanner);
                    break;
                case "2":
                    UserType accountType = askAccountType(scanner);
                    if(accountType == null) break;
                    username = signUp(scanner, accountType);
                    break;
            }
        }while (username == null) ;

        return username;
    }

    private String login(Scanner scanner) {
        presenter.printAsk("username");
        String username = scanner.nextLine();
        presenter.printAsk("password");
        String password = scanner.nextLine();

        if (um.verifyLogin(username, password)) {
            return username;
        }
        else {
            presenter.printWrongAccountInfo();
            return null;
        }
    }

    protected String validInput(String pattern, String field, Scanner scanner){
        String input = scanner.nextLine();
        while(!input.matches(pattern)){
            presenter.printInvalidField(field);
            input = scanner.nextLine();
        }
        return input;
    }

    private UserType askAccountType(Scanner scanner) {
        UserType accountType = null;
        presenter.printAskUserType();
        presenter.printBackToMainMenu();
        String choice = validInput("^0$|^1$|^.{0}$", "type", scanner);
        if(choice.equals("")) return null;

        switch (choice) {
            case "0":
                accountType = UserType.ATTENDEE;
                break;
            case "1":
                accountType = UserType.ORGANIZER;
                break;
        }
        return accountType;
    }
}
