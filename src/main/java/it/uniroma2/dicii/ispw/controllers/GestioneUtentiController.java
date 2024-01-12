package it.uniroma2.dicii.ispw.controllers;


import it.uniroma2.dicii.ispw.App;
import it.uniroma2.dicii.ispw.beans.UtenteBean;
import it.uniroma2.dicii.ispw.enums.TypersOfPersistenceLayer;
import it.uniroma2.dicii.ispw.exceptions.InvalidDataException;
import it.uniroma2.dicii.ispw.models.utente.Utente;
import it.uniroma2.dicii.ispw.models.utente.dao.UtenteDAO;
import it.uniroma2.dicii.ispw.models.utente.dao.UtenteDBMS;
import it.uniroma2.dicii.ispw.models.utente.dao.UtenteFS;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GestioneUtentiController {
    private static final int CF_LENGTH = 16;
    private static final String EMAIL_PATTERN =
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);

    private UtenteDAO utenteDAO;

    public GestioneUtentiController() {
        if(App.getPersistenceLayer().equals(TypersOfPersistenceLayer.JDBC))
            utenteDAO = new UtenteDBMS();
        else
            utenteDAO = new UtenteFS();
    }

    public static boolean validate(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //TODO: if we are adding a istructor or user we need to specify the courses
    public String insertUtente(UtenteBean utenteBean) throws InvalidDataException{
        String res = null;
        if(utenteBean.getCf().length() != CF_LENGTH) throw new InvalidDataException("Il codice fiscale non è valido!");
        if(utenteBean.getName().isBlank() || utenteBean.getSurname().isBlank())
            throw new InvalidDataException("Nome e/o cognome mancanti");
        if(utenteBean.getEmail() == null || !validate(utenteBean.getEmail()))
            throw new InvalidDataException("Email non valida");
        if(utenteBean.getPassword() == null)
            throw new InvalidDataException("La password non può essere vuota");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date  = sdf.parse(utenteBean.getBirthDate());
        } catch (ParseException e) {
            throw new InvalidDataException("Formato data non valida" + e.getMessage());
        }
        Utente utente = new Utente(utenteBean.getName(), utenteBean.getSurname(), utenteBean.getCf(), date, utenteBean.getEmail(), utenteBean.getPassword(), utenteBean.getRuolo());
        try {
            res = utenteDAO.insertUtente(utente);
        } catch (Exception e) {
            //error connection with persistence layer
            throw new RuntimeException(e);
        }
        return res;
    }

}