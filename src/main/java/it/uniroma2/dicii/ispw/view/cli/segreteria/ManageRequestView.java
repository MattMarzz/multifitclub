package it.uniroma2.dicii.ispw.view.cli.segreteria;

import it.uniroma2.dicii.ispw.bean.CommunicationBean;
import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.controller.CommunicationController;
import it.uniroma2.dicii.ispw.enums.RoomRequestStatus;
import it.uniroma2.dicii.ispw.enums.TypesOfCommunications;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.utils.LoggerManager;
import it.uniroma2.dicii.ispw.view.cli.TemplateView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ManageRequestView extends TemplateView {

    public ManageRequestView(UtenteBean usrBean) {
        super(usrBean);
    }

    @Override
    public void control() {
        int choice;
        boolean cond = true;

        while (cond) {
            choice = this.userChoice();
            switch (choice) {
                case 1 -> getAllRequests();
                case 2 -> replyRequest();
                case 3 -> cond = false;
                default -> System.out.println("Riprova");
            }
        }
    }

    @Override
    public List<String> getOptions() {
        return List.of("Visualizza richieste sla", "Rispondi ad una richiesta", "Indietro");
    }

    @Override
    public String getHeader() {
        return "Gestione Richiesta Sala";
    }

    private void getAllRequests() {
        printTable(new CommunicationController().getAllRoomRequestBean());
    }

    private void replyRequest() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        CommunicationBean cb = new CommunicationBean();
        int id = 0;
        int status = 0;
        boolean isValid = false;

         while (!isValid) {
             try {
                 id = Integer.parseInt(getDesiredIn("Richiesta sala", "Inserisci l'id della richiesta: "));

                 System.out.print("1->Accetta, 2->Rifiuta: ");
                 status = Integer.parseInt(reader.readLine());

                 isValid = true;
             } catch (IOException | NumberFormatException e) {
                    System.out.println("Errore: inserire un numero intero.");
             }
         }

         try {
            cb.setId(id);
            cb.setStatus(RoomRequestStatus.getStatus(status));
            new CommunicationController().forwardCommunication(this.usrBean, cb, TypesOfCommunications.ROOM_REQUEST);

        } catch (ItemNotFoundException | InvalidDataException e) {
            LoggerManager.logSevereException(e.getMessage(), e);
        }

    }
}
