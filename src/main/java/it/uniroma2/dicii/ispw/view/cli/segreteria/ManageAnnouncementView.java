package it.uniroma2.dicii.ispw.view.cli.segreteria;

import it.uniroma2.dicii.ispw.bean.CommunicationBean;
import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.controller.CommunicationController;
import it.uniroma2.dicii.ispw.enums.TypesOfCommunications;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.utils.LoggerManager;
import it.uniroma2.dicii.ispw.view.cli.TemplateView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class ManageAnnouncementView extends TemplateView {

    public ManageAnnouncementView(UtenteBean usrBean) {
        super(usrBean);
    }

    @Override
    public void control() {
        int choice;
        boolean cond = true;

        while (cond) {
            choice = this.userChoice();
            switch (choice) {
                case 1 -> getAnnouncements();
                case 2 -> publishAnnouncement();
                case 3 -> cond = false;
                default -> System.out.println("Riprova");
            }
        }
    }

    @Override
    public List<String> getOptions() {
        return List.of("Visualizza annunci", "Pubblica annuncio", "Indietro");
    }

    @Override
    public String getHeader() {
        return "Gestione Annunci";
    }

    private void getAnnouncements() {
        printTable(new CommunicationController().getAllAnnouncementBean());
    }

    private void publishAnnouncement() {
        CommunicationBean cb;
        try {
            cb = announcementForm();
            new CommunicationController().forwardCommunication(this.usrBean, cb, TypesOfCommunications.ANNOUNCEMENT);
        } catch (IOException e) {
            System.out.println("ATTENZIONE: formato dati non valido");
        } catch (InvalidDataException | ItemNotFoundException e) {
            LoggerManager.logSevereException(e.getMessage(), e);
        }
    }

    private CommunicationBean announcementForm() throws IOException {
        CommunicationBean cb = new CommunicationBean();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        printHeader("Scrivi Annuncio");

        System.out.print("Inserisci titolo: ");
        String title = reader.readLine();
        cb.setTitle(title);

        System.out.print("Inserisci testo: ");
        String testo = reader.readLine();
        cb.setText(testo);

        cb.setDate(Timestamp.valueOf(LocalDateTime.now()));
        cb.setSender(this.usrBean.getCf());

        return cb;
    }
}
