package it.uniroma2.dicii.ispw.view.cli.utente;

import it.uniroma2.dicii.ispw.bean.CommunicationBean;
import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.controller.CommunicationController;
import it.uniroma2.dicii.ispw.controller.LoginController;
import it.uniroma2.dicii.ispw.controller.ProgrammazioneController;
import it.uniroma2.dicii.ispw.enums.RoomRequestStatus;
import it.uniroma2.dicii.ispw.enums.TypesOfCommunications;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.utils.DateParser;
import it.uniroma2.dicii.ispw.utils.LoggerManager;
import it.uniroma2.dicii.ispw.utils.Observer;
import it.uniroma2.dicii.ispw.view.cli.TemplateView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class IstruttoreView extends TemplateView implements Observer {

    public IstruttoreView(UtenteBean usrBean) {
        super(usrBean);
        new LoginController().attachObserver(usrBean, this);
    }


    @Override
    public int userChoice() {
        printHeader("ISTRUTTORE PANEL");
        List<String> options = new ArrayList<>();
        options.add("Visulizza Annunci");
        options.add("Visualizza i tuoi impegni");
        options.add("Visualizza programmazione");
        options.add("Viualizza tue prenotazioni");
        options.add("Richiedi nuova prenotazione");
        options.add("Esci");
        return operationMenu("Come vuoi procedere? ", options);
    }

    @Override
    public void control() {
        int choice;

        while (true) {
            choice = this.userChoice();
            switch (choice) {
                case 1 -> getAllAnnounces();
                case 2 -> getYourLessons();
                case 3 -> getAllLessons();
                case 4 -> getYourReservation();
                case 5 -> askForRoom();
                case 6 -> {
                    LoginController controller = new LoginController();
                    controller.detachObserver(usrBean, this);
                    controller.logout(usrBean);
                    System.exit(0);
                }
            }
        }
    }

    private void getAllAnnounces() {
        printTable(new CommunicationController().getAllAnnouncementBean());
    }

    private void getYourLessons() {
        try {
            printTable(new ProgrammazioneController().getLezioniByUtente(usrBean.getCf()));
        } catch (ItemNotFoundException e) {
            LoggerManager.logSevereException(e.getMessage(), e);
        }
    }

    private void getAllLessons() {
        printTable(new ProgrammazioneController().getAllLezioni());
    }

    private void getYourReservation() {
        printTable(new CommunicationController().getRoomRequestByUtente(usrBean.getCf()));
    }

    private void askForRoom() {
        CommunicationBean cb = new CommunicationBean();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            printHeader("Compila Richiesta");

            System.out.print("Inserisci titolo: ");
            String title = reader.readLine();
            cb.setTitle(title);

            System.out.print("Inserisci testo: ");
            String testo = reader.readLine();
            cb.setText(testo);

            System.out.print("Inserisci sala: ");
            String sala = reader.readLine();
            cb.setRoom(sala);

            System.out.print("Inserisci per quando (yyyy-MM-dd hh:mm:ss): ");
            String when = reader.readLine();
            cb.setWhen(DateParser.parseStringToTimestamp(when));

            cb.setDate(Timestamp.valueOf(LocalDateTime.now()));
            cb.setSender(this.usrBean.getCf());
            cb.setStatus(RoomRequestStatus.PENDING);

            new CommunicationController().forwardCommunication(usrBean, cb, TypesOfCommunications.ROOM_REQUEST);

        } catch (IOException e) {
            System.out.println("Impossibile leggere input.");
        } catch (InvalidDataException | ItemNotFoundException e) {
            LoggerManager.logSevereException(e.getMessage(), e);
        }
    }


    @Override
    public void update(String... msg) {
        if(msg.length != 0)
            System.out.println("\033[33m\n*** Nuova notifica: [" + msg[0] + "] ***\033[0m");
    }

}