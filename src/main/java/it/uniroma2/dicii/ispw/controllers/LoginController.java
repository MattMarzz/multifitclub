package it.uniroma2.dicii.ispw.controllers;

import it.uniroma2.dicii.ispw.App;
import it.uniroma2.dicii.ispw.beans.UtenteBean;
import it.uniroma2.dicii.ispw.exceptions.InvalidDataException;
import it.uniroma2.dicii.ispw.exceptions.ItemNotFoundException;
import it.uniroma2.dicii.ispw.utente.Utente;
import it.uniroma2.dicii.ispw.utente.dao.UtenteDAO;
import it.uniroma2.dicii.ispw.utente.dao.UtenteDBMS;
import it.uniroma2.dicii.ispw.utente.dao.UtenteFS;

import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginController {
    private UtenteDAO utenteDAO;
    private static final String EMAIL_PATTERN =
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
    public LoginController() {
        switch (App.persistenceLayer){
            case JDBC -> utenteDAO = new UtenteDBMS();
            case FileSystem -> utenteDAO = new UtenteFS();
        }
    }

    public static boolean validate(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public Utente login(UtenteBean utenteBean) throws Exception {
        Utente utente = null;
        //here we expect no blank email and pwd fields
        if (!validate(utenteBean.getEmail())) throw new InvalidDataException("Email non valida!");
        utente = utenteDAO.auth(utenteBean);
        return utente;
    }
}