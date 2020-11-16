import java.io.IOException;
import java.util.*;

public class AttendeeSystem extends UserSystem{
    public AttendeeSystem (Presenter p, UserManager uMan, EventManager eMan, MessageManager mMan) {
        super(p, uMan, eMan, mMan);
    }

    public void run(String username) {
        Scanner scanner = new Scanner(System.in);
        label:
        while (true) {
            presenter.printAttendeeMenu();
            String attendeeChoice = scanner.nextLine();

            switch (attendeeChoice) {
                case "0":
                    presenter.printLoggedOut();
                    break label;
                case "1":
                    while (true) {
                        presenter.printAttendeeMessageMenu();
                        String messageChoice = scanner.nextLine();
                        super.helperMessageSystem(username, messageChoice, scanner);
                        if (!messageChoice.equals("b")) {
                            presenter.printInvalidInput();
                        } else {
                            break;
                        }
                    }
                    break;
                case "2":  //view available events
                    List<UUID> available = eventM.getAvailableEvents();
                    presenter.printAvailableEvents(formatInfo(eventM.getEventsStrings(available)));
                    break;
                case "3":
                    List<UUID> eventList = userM.getEventsAttending(username);
                    presenter.printUCReturns(eventM.convertIDtoName(eventList));
                    break;
                case "4":  //signup event
                    while(true){
                        List<UUID> availEvents = eventM.getAvailableEvents();
                        String choice = signupAttendeeHelper(username, scanner);
                        if (choice.matches("^[0-9]*$")) {
                            int eventChoice = Integer.parseInt(choice);
                            if (!(eventChoice < availEvents.size())) {
                                presenter.printInvalidInput();
                                break;
                            }
                            else{
                                UUID id = eventM.getAvailableEvents().get(eventChoice);
                                if (isAttendeeFree(username, id)){
                                    eventM.addAttendee(username, id);
                                    userM.addEventAttending(username, id);
                                    presenter.printEventSignUpSuccess();
                                }
                                else{
                                presenter.printAlreadyBookedTime();
                                break;
                                }
                            }
                        }
                        else {
                            presenter.printInvalidInput();
                            break;
                        }
                    }
                    break;
                case "5":  //cancel event
                    while(true){
                        if (cancelAttendeeHelper(username, scanner)){
                            presenter.printEventCancelSuccess();
                        } else{
                            presenter.printInvalidInput();
                            break;}
                    }

                default:
                    presenter.printInvalidInput();
                    break;
            }
        }
    }

    private String formatInfo(List<String> eventStrings){
        StringBuilder info = new StringBuilder();
        int i = 0;
        for (String event: eventStrings){
            info.append("\n").append(i).append(": ").append(event);
            i += 1;
        }
        if (info.toString().equals("")) {
            presenter.printNoEventsAvailable();
            return "";
        }
        return info.toString();
    }

    private String signupAttendeeHelper(String username, Scanner scanner) {
        List<UUID> availEvents = eventM.getAvailableEvents();
        List<String> eventInfo = eventM.getEventsStrings(availEvents);
        presenter.printAskSignUp();
        presenter.printAvailableEvents(formatInfo(eventInfo));
        String choice = scanner.nextLine();
        return choice;

    }

    private boolean cancelAttendeeHelper(String username, Scanner scanner){
        List<UUID> eventList = userM.getEventsAttending(username);
        List<String> eventNames = (eventM.convertIDtoName(eventList));
        presenter.printUCReturns(eventNames);
        presenter.printAskWhichEventCancel();
        String eventChoice = scanner.nextLine();
        if (!eventNames.contains(eventChoice)){
            return false;
        }
        UUID eventRemoving = eventList.get(eventNames.indexOf(eventChoice));
        userM.removeEventAttending(username, eventRemoving);
        eventM.removeAttendee(username, eventRemoving);

        return true;
    }

    private boolean isAttendeeFree(String username, UUID newEvent){
        List<UUID> userEvents = userM.getEventsAttending(username);
        for (UUID events :userEvents){
            if (eventM.ifTimeOverlap(events, newEvent)){
                return false;
            }
        }
        return true;
    }
}
