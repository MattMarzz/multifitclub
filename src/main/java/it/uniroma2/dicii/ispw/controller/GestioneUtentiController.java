package it.uniroma2.dicii.ispw.controller;


import it.uniroma2.dicii.ispw.App;
import it.uniroma2.dicii.ispw.bean.CorsoBean;
import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.enums.TypesOfPersistenceLayer;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.exception.ItemAlreadyExistsException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.model.corso.Corso;
import it.uniroma2.dicii.ispw.model.corso.dao.CorsoDAO;
import it.uniroma2.dicii.ispw.model.corso.dao.CorsoDBMS;
import it.uniroma2.dicii.ispw.model.corso.dao.UtenteCorsoDAO;
import it.uniroma2.dicii.ispw.model.corso.dao.UtenteCorsoDBMS;
import it.uniroma2.dicii.ispw.model.utente.Utente;
import it.uniroma2.dicii.ispw.model.utente.dao.UtenteDAO;
import it.uniroma2.dicii.ispw.model.utente.dao.UtenteDBMS;
import it.uniroma2.dicii.ispw.model.utente.dao.UtenteFS;
import it.uniroma2.dicii.ispw.utils.DateParser;
import it.uniroma2.dicii.ispw.utils.EmailValidator;
import it.uniroma2.dicii.ispw.utils.LoggerManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GestioneUtentiController {
    private static final int CF_LENGTH = 16;
    private UtenteDAO utenteDAO;
    private CorsoDAO corsoDAO;
    private UtenteCorsoDAO utenteCorsoDAO;

    public GestioneUtentiController() {
        if(App.getPersistenceLayer().equals(TypesOfPersistenceLayer.JDBC)) {
            utenteDAO = new UtenteDBMS();
            corsoDAO = new CorsoDBMS();
            utenteCorsoDAO = new UtenteCorsoDBMS();
        } else {
            try {
                utenteDAO = new UtenteFS();
            } catch (IOException e) {
                LoggerManager.logSevereException("Impossibile dialogare con il file system", e);
            }
        }
    }

    public Utente getUtenteByCf(String cf) throws ItemNotFoundException {
        return utenteDAO.getUtenteByCf(cf);
    }

    public String insertUtente(UtenteBean utenteBean) throws InvalidDataException{
        String res = null;
        if(utenteBean.getCf().length() != CF_LENGTH) throw new InvalidDataException("Codice fiscale non valido.");
        if(utenteBean.getName().isBlank() || utenteBean.getSurname().isBlank())
            throw new InvalidDataException("Nome e/o Cognome mancanti.");
        if(utenteBean.getEmail() == null || EmailValidator.isNotValid(utenteBean.getEmail()))
            throw new InvalidDataException("Email non valida.");
        if(utenteBean.getPassword() == null)
            throw new InvalidDataException("Password mancante.");

        Utente utente = new Utente(utenteBean);
        try {
            res = utenteDAO.insertUtente(utente);
        } catch (ItemAlreadyExistsException e) {
            res = e.getMessage();
        }
        return res;
    }
    
    public List<UtenteBean> getAllUtenti() {
        List<Utente> users;
        List<UtenteBean> utenteBeans = new ArrayList<>();

        users = utenteDAO.getAllUtenti();

        for (Utente u: users) {
            UtenteBean ub = new UtenteBean(u.getName(), u.getSurname(), u.getCf(), DateParser.parseDateToString(u.getBirthDate()), u.getEmail(), null, u.getRuolo());
            utenteBeans.add(ub);
        }

        return utenteBeans;
    }

    public String updateUtente(UtenteBean utenteBean) throws InvalidDataException, ItemNotFoundException {
        Utente utente = utenteDAO.getUtenteByCf(utenteBean.getCf());
        if (utente == null) throw new ItemNotFoundException("Utente non trovato.");

        if(utenteBean.getName().isBlank() || utenteBean.getSurname().isBlank())
            throw new InvalidDataException("Nome e/o Cognome mancanti.");
        if(utenteBean.getEmail() == null || EmailValidator.isNotValid(utenteBean.getEmail()))
            throw new InvalidDataException("Email non valida.");

        utente.updateInfos(utenteBean);
        return utenteDAO.editUtente(utente);
    }

    public List<CorsoBean> getEnrollmentsByUtente(UtenteBean utenteBean) throws ItemNotFoundException {
        List<CorsoBean> corsoBeanList = new ArrayList<>();

        Utente utente = utenteDAO.getUtenteByCf(utenteBean.getCf());
        for (Corso c: utente.getEnrollments()) {
            CorsoBean corsoBean = new CorsoBean(c.getName());
            corsoBeanList.add(corsoBean);
        }
        return corsoBeanList;
    }

    public void removeEnrollmentByUtente(UtenteBean utenteBean, CorsoBean corsoBean) throws ItemNotFoundException {
        Utente utente = utenteDAO.getUtenteByCf(utenteBean.getCf());
        Corso corso = corsoDAO.getCorsoByNome(corsoBean.getName());
        if(utente != null && corso != null) {
            utenteCorsoDAO.removeEnrollmentByUtente(utente, corso);
        }
    }

    public void addEnrollmentToUtente(UtenteBean utenteBean, CorsoBean corsoBean) throws ItemNotFoundException {
        Utente utente = utenteDAO.getUtenteByCf(utenteBean.getCf());
        Corso corso = corsoDAO.getCorsoByNome(corsoBean.getName());
        if(utente != null && corso != null) {
            utenteCorsoDAO.addEnrollmentToUtente(utente, corso);
        }
    }

    public List<CorsoBean> getTeachingByUtente(UtenteBean utenteBean) throws ItemNotFoundException {
        List<CorsoBean> corsoBeanList = new ArrayList<>();

        Utente utente = utenteDAO.getUtenteByCf(utenteBean.getCf());
        if(utente != null) {
            for (Corso c : utente.getTeachings()) {
                CorsoBean corsoBean = new CorsoBean(c.getName());
                corsoBeanList.add(corsoBean);
            }
        }
        return corsoBeanList;
    }

}