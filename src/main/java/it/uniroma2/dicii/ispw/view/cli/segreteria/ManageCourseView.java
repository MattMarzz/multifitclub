package it.uniroma2.dicii.ispw.view.cli.segreteria;

import it.uniroma2.dicii.ispw.bean.CorsoBean;
import it.uniroma2.dicii.ispw.controller.GestioneCorsiController;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.exception.ItemAlreadyExistsException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.utils.DateParser;
import it.uniroma2.dicii.ispw.utils.LoggerManager;
import it.uniroma2.dicii.ispw.view.cli.TemplateView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ManageCourseView extends TemplateView {
    @Override
    public void control() {
        int choice;
        boolean cond = true;

        while (cond) {
            choice = this.userChoice();
            switch (choice) {
                case 1 -> insertCourse();
                case 2 -> getAllCourses();
                case 3 -> getLessonsByCourse();
                case 4 -> removeCourse();
                case 5 -> cond = false;
                default -> System.out.println("Riprova");
            }
        }
    }

    @Override
    public List<String> getOptions() {
        return List.of("Inserisci corso", "Visualizza corsi", "Visualizza lezioni del corso",
                "Elimina corso", "Indietro");
    }

    @Override
    public String getHeader() {
        return "Gestione Corsi";
    }

    private void insertCourse() {
        CorsoBean cb = null;
        try {
            cb = courseForm();
        } catch (IOException e) {
            System.out.println("ATTENZIONE: formato dati non valido");
        }
        if (cb != null) {
            try {
                new GestioneCorsiController().insertCourse(cb);
                System.out.println("Corso inserito con successo!");
            } catch (ItemAlreadyExistsException | InvalidDataException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void getAllCourses() {
        printTable(new GestioneCorsiController().getAllCorsi());
    }

    private void getLessonsByCourse() {
        CorsoBean cb = new CorsoBean();
        try {
            cb.setName(getDesiredIn("Corso","inserisci nome corso: " ));
            printTable(new GestioneCorsiController().getLezioniByCorsoId(cb));
        } catch (IOException e) {
            System.out.println("Impossibile leggere dati");
        } catch (ItemNotFoundException | InvalidDataException e) {
            LoggerManager.logSevereException(e.getMessage(), e);
        }
    }

    private void removeCourse() {
        CorsoBean cb = new CorsoBean();
        try {
            cb.setName(getDesiredIn("Corso","Inserisci nome corso da eliminare: " ));
            new GestioneCorsiController().removeCorso(cb);
        } catch (IOException e) {
            System.out.println("Impossibile leggere dati");
        }catch (ItemNotFoundException | InvalidDataException e) {
            LoggerManager.logSevereException(e.getMessage(), e);
        }
    }
    private CorsoBean courseForm() throws IOException {
        CorsoBean cb = new CorsoBean();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        printHeader("Inserisci informazioni corso");

        System.out.print("Inserisci nome: ");
        String name = reader.readLine();
        cb.setName(name);

        System.out.print("Inserisci data inizio corso (yyyy-MM-dd): ");
        String date = reader.readLine();
        try {
            cb.setStartDate(DateParser.parseStringToDateUtil(date));
        } catch (InvalidDataException e) {
            LoggerManager.logSevereException(e.getMessage(), e);
        }

        return cb;
    }
}
