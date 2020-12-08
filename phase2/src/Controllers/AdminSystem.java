package Controllers;

import UI.AdminPresenter;

import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class AdminSystem extends UserSystem{
    private AdminPresenter presenter;

    public AdminSystem(){
        this.presenter = new AdminPresenter();
    }

    /**
     * Implements the run method for all admin users.
     * With this method, an admins can logout, message (view, send, receive), view available events,
     * signup and remove themselves from events.
     *
     */
    @Override
    void run(String username, TechConferenceSystem tcs) {
        Scanner scanner = new Scanner(System.in);
        label:
        while (true) {
            presenter.printAdminMenu();
            String choice = scanner.nextLine();
            List<UUID> emptyEvents = tcs.getEM().getEmptyEvents();

            switch (choice){
                case "0":
                    presenter.printLoggedOut();
                    break label;
                case "1":
                    presenter.printDeleteMessageMenu();
                case "2":
                    presenter.printDeleteEventMenu();
            }
        }
    }
}
