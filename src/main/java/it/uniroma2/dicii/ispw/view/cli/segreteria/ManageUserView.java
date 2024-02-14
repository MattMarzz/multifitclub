package it.uniroma2.dicii.ispw.view.cli.segreteria;

import it.uniroma2.dicii.ispw.bean.CorsoBean;
import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.controller.GestioneUtentiController;
import it.uniroma2.dicii.ispw.enums.Ruolo;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.utils.LoggerManager;
import it.uniroma2.dicii.ispw.view.cli.TemplateView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ManageUserView extends TemplateView {

    @Override
    public int userChoice() {
        printHeader("Gestione Utenti");
        List<String> options = new ArrayList<>();
        options.add("Inserisci utente");
        options.add("Visualizza utenti");
        options.add("Visualizza iscrizioni utente");
        options.add("Visualizza insegnamenti utente");
        options.add("Indietro");
        return operationMenu("Che operazione desideri effettuare?", options);
    }

    @Override
    public void control() {
        int choice;
        boolean cond = true;

        while (cond) {
            choice = this.userChoice();
            switch (choice) {
                case 1 -> insertUsr();
                case 2 -> getAllUsrs();
                case 3 -> getEnrollments();
                case 4 -> getTeachings();
                case 5 -> cond = false;

            }
        }
    }

    private void insertUsr() {
        UtenteBean ub = null;;
        try {
            ub = memberForm();
        } catch (IOException e) {
            System.out.println("ATTENZIONE: formato dati non valido");
        }
        if (ub != null) {
            try {
                System.out.println(new GestioneUtentiController().insertUtente(ub));
            } catch (InvalidDataException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void getAllUsrs() {
        printTable(new GestioneUtentiController().getAllUtenti());
    }

    private void getEnrollments() {
        UtenteBean ub = new UtenteBean();
        try {
            ub.setCf(getDesiredIn("Utente","inserisci codice fiscale: " ));
        } catch (IOException e) {
            System.out.println("Impossibile leggere dati");
        }

        if(ub.getCf() != null) {
            try {
                List<CorsoBean> corsoBeanList = new GestioneUtentiController().getEnrollmentsByUtente(ub);
                printTable(corsoBeanList);

            } catch (ItemNotFoundException e) {
                LoggerManager.logSevereException(e.getMessage(), e);
            }
        }
    }

    private void getTeachings() {
        UtenteBean ub = new UtenteBean();
        try {
            ub.setCf(getDesiredIn("Utente","inserisci codice fiscale: " ));
        } catch (IOException e) {
            System.out.println("Impossibile leggere dati");
        }

        if(ub.getCf() != null) {
            try {
                List<CorsoBean> corsoBeanList = new GestioneUtentiController().getTeachingByUtente(ub);
                printTable( corsoBeanList);

            } catch (ItemNotFoundException e) {
                LoggerManager.logSevereException(e.getMessage(), e);
            }
        }
    }

    private UtenteBean memberForm() throws IOException {
        UtenteBean ub = new UtenteBean();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        printHeader("Inserisci informazioni utente");

        System.out.print("Inserisci codice fiscale: ");
        String cf = reader.readLine();
        ub.setCf(cf);

        System.out.print("Inserisci nome: ");
        String name = reader.readLine();
        ub.setName(name);

        System.out.print("Inserisci cognome: ");
        String srnm = reader.readLine();
        ub.setSurname(srnm);

        System.out.print("Inserisci data di nascita (yyyy-MM-dd): ");
        String date = reader.readLine();
        ub.setBirthDate(date);


        System.out.print("Inserisci email: ");
        String email = reader.readLine();
        ub.setEmail(email);

        ub.setPassword("default1");

        int roleId;
        do {
            System.out.print("Ruolo (2->segreteria, 1->istruttore, 0->utente): ");
            try {
                roleId = Integer.parseInt(reader.readLine());
                if (roleId < 0 || roleId > 2) {
                    System.out.println("Inserisci un numero tra 0 e 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input non valido. Inserisci un numero.");
                roleId = -1;
            } catch (IOException e) {
                System.out.println("Errore durante la lettura dell'input.");
                roleId = -1;
            }
        } while (roleId < 0 || roleId > 2);

        ub.setRuolo(Ruolo.getRuolo(roleId));

        return ub;
    }
}