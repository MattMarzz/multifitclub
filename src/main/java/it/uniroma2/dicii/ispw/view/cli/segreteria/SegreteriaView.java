package it.uniroma2.dicii.ispw.view.cli.segreteria;

import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.controller.LoginController;
import it.uniroma2.dicii.ispw.utils.Observer;
import it.uniroma2.dicii.ispw.view.cli.TemplateView;

import java.util.ArrayList;
import java.util.List;

public class SegreteriaView extends TemplateView implements Observer {

    ManageUserView manageUserView = new ManageUserView();
    ManageCourseView manageCourseView = new ManageCourseView();
    SchedulingView schedulingView = new SchedulingView(this.usrBean);
    ManageAnnouncementView manageAnnouncementView = new ManageAnnouncementView(this.usrBean);
    ManageRequestView manageRequestView = new ManageRequestView(this.usrBean);

    public SegreteriaView(UtenteBean usrBean) {
        super(usrBean);
        new LoginController().attachObserver(usrBean, this);
    }

    @Override
    public void control() {
        int choice;
        boolean running = true;

        while (running) {
            choice = this.userChoice();
            switch (choice) {
                case 1 -> manageUserView.control();
                case 2 -> manageCourseView.control();
                case 3 -> schedulingView.control();
                case 4 -> manageAnnouncementView.control();
                case 5 -> manageRequestView.control();
                case 6 -> {
                    LoginController controller = new LoginController();
                    controller.detachObserver(usrBean, this);
                    controller.logout(usrBean);
                    running = false;
                }
                default -> System.out.println("Riprova");
            }
        }

        System.exit(0);
    }

    @Override
    public List<String> getOptions() {
        return List.of("Gestione Utenti", "Gestione Corsi", "Programmazione", "Gestione Annunci",
                "Gestione Prenotazioni", "Esci");
    }

    @Override
    public String getHeader() {
        return "SEGRETERIA ADMINISTRATION PANEL";
    }

    @Override
    public void update(String... msg) {
        if(msg.length != 0)
            System.out.println("\033[33m\n*** Nuova notifica: [" + msg[0] + "] ***\033[0m");
    }
}
