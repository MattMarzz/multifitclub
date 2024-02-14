package it.uniroma2.dicii.ispw.view.cli.segreteria;

import it.uniroma2.dicii.ispw.bean.LezioneBean;
import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.controller.ProgrammazioneController;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.exception.ItemAlreadyExistsException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.utils.DateParser;
import it.uniroma2.dicii.ispw.utils.LoggerManager;
import it.uniroma2.dicii.ispw.view.cli.TemplateView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SchedulingView extends TemplateView {

    private static final Set<String> VALID_WEEK_DAYS = new HashSet<>(
            Arrays.asList("Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato", "Domenica"));

    public SchedulingView(UtenteBean usrBean) {
        super(usrBean);
    }

    @Override
    public int userChoice() {
        printHeader("Programmazione");
        List<String> options = new ArrayList<>();
        options.add("Visualizza programmazione");
        options.add("Inserisci lezioni");
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
                case 1 -> getActivities();
                case 2 -> insertLesson();
                case 3 -> cond = false;
                default -> System.out.println("Riprova");
            }
        }
    }

    private void getActivities() {
        printTable(new ProgrammazioneController().getAllLezioni());
    }

    private void insertLesson() {
        LezioneBean lb = new LezioneBean();

        try {
            lb.setCorso(getDesiredIn("Corso","Inserisci il nome del corso per il quale si vogliono aggiungere lezioni: " ));
            lessonForm(lb);
        } catch (IOException e) {
            System.out.println("ATTENZIONE: formato dati non valido");
        }
        if (lb.getOra() != null) {
            try {
                new ProgrammazioneController().insertLezioni(lb, this.usrBean);
                System.out.println("Lezioni inserite con successo!");
            } catch (ItemNotFoundException | InvalidDataException | ItemAlreadyExistsException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void lessonForm(LezioneBean lb) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        printHeader("Inserisci informazioni lezioni");
        List<String> weekDays = null;
        boolean isValid = false;

        while (!isValid) {
            System.out.print("Inserisci giorni separati da uno spazio: ");
            String days = reader.readLine();

            String[] daysArray = days.split("\\s+");

            weekDays = Arrays.stream(daysArray)
                    .map(SchedulingView::capitalizeFirstLetter)
                    .toList();

            isValid = VALID_WEEK_DAYS.containsAll(weekDays);

            if (!isValid) {
                System.out.println("Inserimento non valido. Assicurati di inserire giorni della settimana corretti.");
            }
        }
        lb.setGiornoList(weekDays);

        System.out.print("Inserisci sala: ");
        String room = reader.readLine();
        lb.setSala(room);

        System.out.print("Inserisci orario inizio (hh:mm:ss): ");
        String hour = reader.readLine();

        try {
            lb.setOra(DateParser.parseStringToTime(hour));
        } catch (InvalidDataException e) {
            LoggerManager.logSevereException(e.getMessage(), e);
            lb.setOra(null);
        }

    }

    private static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
