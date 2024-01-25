package it.uniroma2.dicii.ispw.controller;

import it.uniroma2.dicii.ispw.App;
import it.uniroma2.dicii.ispw.bean.LoginBean;
import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.enums.TypesOfPersistenceLayer;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.model.utente.Utente;
import it.uniroma2.dicii.ispw.model.utente.dao.UtenteDAO;
import it.uniroma2.dicii.ispw.model.utente.dao.UtenteDBMS;
import it.uniroma2.dicii.ispw.model.utente.dao.UtenteFS;
import it.uniroma2.dicii.ispw.utils.EmailValidator;


public class LoginController {
    private UtenteDAO utenteDAO;
    public LoginController() {
        if(App.getPersistenceLayer().equals(TypesOfPersistenceLayer.JDBC))
            utenteDAO = new UtenteDBMS();
        else
            utenteDAO = new UtenteFS();
    }

    public UtenteBean login(LoginBean loginBean) throws Exception {
        if(loginBean.getEmail().isBlank() || loginBean.getPassword().isBlank())
            throw new InvalidDataException("E-mail e password necessarie!");

        if (EmailValidator.isNotValid(loginBean.getEmail()))
            throw new InvalidDataException("Email non valida!");

        Utente u = utenteDAO.auth(loginBean);
        if (u == null) throw new ItemNotFoundException("Accesso negato!");

        //don't load pwd for security purpose
        return new UtenteBean(u.getName(), u.getSurname(), u.getCf(), u.getBirthDate().toString(),
                    u.getEmail(), null, u.getRuolo());

    }

}