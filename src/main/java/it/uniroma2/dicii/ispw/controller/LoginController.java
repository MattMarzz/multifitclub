package it.uniroma2.dicii.ispw.controller;

import it.uniroma2.dicii.ispw.App;
import it.uniroma2.dicii.ispw.bean.LoginBean;
import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.enums.TypesOfPersistenceLayer;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.model.utente.Utente;
import it.uniroma2.dicii.ispw.model.utente.dao.UtenteDAO;
import it.uniroma2.dicii.ispw.model.utente.dao.UtenteDBMS;
import it.uniroma2.dicii.ispw.model.utente.dao.UtenteFS;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginController {
    private UtenteDAO utenteDAO;
    private static final String EMAIL_PATTERN =
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
    public LoginController() {
        if(App.getPersistenceLayer().equals(TypesOfPersistenceLayer.JDBC))
            utenteDAO = new UtenteDBMS();
        else
            utenteDAO = new UtenteFS();
    }

    public static boolean validate(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public UtenteBean login(LoginBean loginBean) throws Exception {
        //here we expect no blank email and pwd fields
        if (!validate(loginBean.getEmail())) throw new InvalidDataException("Email non valida!");
        Utente u = utenteDAO.auth(loginBean);
        //don't load pwd for security purpose
        return new UtenteBean(u.getName(), u.getSurname(), u.getCf(), u.getBirthDate().toString(), u.getEmail(), null, u.getRuolo());
    }

}