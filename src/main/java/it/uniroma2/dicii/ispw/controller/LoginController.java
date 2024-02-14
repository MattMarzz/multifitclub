package it.uniroma2.dicii.ispw.controller;

import it.uniroma2.dicii.ispw.App;
import it.uniroma2.dicii.ispw.bean.LoginBean;
import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.enums.TypesOfPersistenceLayer;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.utils.*;
import it.uniroma2.dicii.ispw.notification.Client;
import it.uniroma2.dicii.ispw.model.utente.Utente;
import it.uniroma2.dicii.ispw.model.utente.dao.UtenteDAO;
import it.uniroma2.dicii.ispw.model.utente.dao.UtenteDBMS;
import it.uniroma2.dicii.ispw.model.utente.dao.UtenteFS;

import java.io.IOException;
import java.net.Socket;
import java.util.Map;


public class LoginController {
    private UtenteDAO utenteDAO;
    public LoginController() {
        if(App.getPersistenceLayer().equals(TypesOfPersistenceLayer.JDBC))
            utenteDAO = new UtenteDBMS();
        else {
            try {
                utenteDAO = new UtenteFS();
            } catch (IOException e) {
                LoggerManager.logSevereException("Impossibile accedere", e);
            }
        }

    }

    public UtenteBean login(LoginBean loginBean) throws ItemNotFoundException, InvalidDataException {
        if(loginBean.getEmail().isBlank() || loginBean.getPassword().isBlank())
            throw new InvalidDataException("E-mail e password necessarie!");

        if (EmailValidator.isNotValid(loginBean.getEmail()))
            throw new InvalidDataException("Email non valida!");

        Utente u = utenteDAO.auth(loginBean);
        if (u == null) throw new ItemNotFoundException("Accesso negato!");

        Socket socket = null;
        try {
            socket = new Socket("127.0.0.1", 1234);
        } catch (IOException e) {
            LoggerManager.logSevereException("Impossibile creare socket", e);
        }

        if(socket != null) {
            Client client = new Client(socket, u);
            //put client listen notification in new thread
            client.listenForMessage();

            LoginManager lm = LoginManager.getInstance();
            lm.setHashMap(u, client);
        }

        return new UtenteBean(u.getName(), u.getSurname(), u.getCf(), DateParser.parseDateToString(u.getBirthDate()),
                    u.getEmail(), u.getPassword(), u.getRuolo());

    }

    public void attachObserver(UtenteBean ub, Observer view) {
        Utente u = null;
        try {
            u = new GestioneUtentiController().getUtenteByCf(ub.getCf());
        } catch (ItemNotFoundException e) {
            LoggerManager.logSevere(e.getMessage());
        }
        Client c = LoginManager.getInstance().getHashMap().get(u);
        if(c != null)
            c.attach(view);
    }
    public void detachObserver(UtenteBean ub, Observer view) {
        Utente u = null;
        try {
            u = new GestioneUtentiController().getUtenteByCf(ub.getCf());
        } catch (ItemNotFoundException e) {
            LoggerManager.logSevereException("Errore di login", e);
        }
        Client c = LoginManager.getInstance().getHashMap().get(u);
        if(c != null)
            c.detach(view);
    }

    public void logout(UtenteBean ub) {
        Utente u = null;
        try {
            u = new GestioneUtentiController().getUtenteByCf(ub.getCf());
        } catch (ItemNotFoundException e) {
            LoggerManager.logSevere(e.getMessage());
        }
        Map<Utente, Client> hm = LoginManager.getInstance().getHashMap();
        hm.remove(u);
    }

}