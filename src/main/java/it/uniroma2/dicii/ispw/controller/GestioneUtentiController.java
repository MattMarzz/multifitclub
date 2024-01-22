package it.uniroma2.dicii.ispw.controller;


import it.uniroma2.dicii.ispw.App;
import it.uniroma2.dicii.ispw.bean.CorsoBean;
import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.enums.TypesOfPersistenceLayer;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
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
import it.uniroma2.dicii.ispw.utils.LoggerManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GestioneUtentiController {
    private static final int CF_LENGTH = 16;
    private static final String EMAIL_PATTERN =
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);

    private UtenteDAO utenteDAO;
    private CorsoDAO corsoDAO;
    private UtenteCorsoDAO utenteCorsoDAO;

    public GestioneUtentiController() {
        if(App.getPersistenceLayer().equals(TypesOfPersistenceLayer.JDBC)) {
            utenteDAO = new UtenteDBMS();
            corsoDAO = new CorsoDBMS();
            utenteCorsoDAO = new UtenteCorsoDBMS();
        } else {
            utenteDAO = new UtenteFS();
        }
    }

    public static boolean validate(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static Date parseStringToDate(String str) throws InvalidDataException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date  = sdf.parse(str);
        } catch (ParseException e) {
            throw new InvalidDataException("Formato data non valida" + e.getMessage());
        }
        return date;
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
        Date date = parseStringToDate(utenteBean.getBirthDate());
        Utente utente = new Utente(utenteBean.getName(), utenteBean.getSurname(), utenteBean.getCf(), date, utenteBean.getEmail(), utenteBean.getPassword(), utenteBean.getRuolo());
        try {
            res = utenteDAO.insertUtente(utente);
        } catch (Exception e) {
            //error connection with persistence layer
            throw new RuntimeException(e);
        }
        return res;
    }
    
    public List<UtenteBean> getAllUtenti() {
        List<Utente> users;
        List<UtenteBean> utenteBeans = new ArrayList<>();

        try {
            users = utenteDAO.getAllUtenti();
        } catch (Exception e) {
            LoggerManager.logSevereException(e.getMessage(), e);
            return utenteBeans;
        }

        for (Utente u: users) {
            UtenteBean ub = new UtenteBean(u.getName(), u.getSurname(), u.getCf(), u.getBirthDate().toString(), u.getEmail(), null, u.getRuolo());
            utenteBeans.add(ub);
        }
        return utenteBeans;
    }

    public String editUtente(UtenteBean utenteBean) throws Exception {
        String res = "";
        Utente utente = utenteDAO.getUtenteById(utenteBean.getCf());
        if (utente == null) throw new ItemNotFoundException("Utente sconosciuto");
        if(utenteBean.getName().isBlank() || utenteBean.getSurname().isBlank())
            throw new InvalidDataException("Nome e/o cognome mancanti");
        if(utenteBean.getEmail() == null || !validate(utenteBean.getEmail()))
            throw new InvalidDataException("Email non valida");

        utente.setName(utenteBean.getName());
        utente.setSurname(utenteBean.getSurname());
        utente.setEmail(utenteBean.getEmail());
        utente.setBirthDate(parseStringToDate(utenteBean.getBirthDate()));
        utente.setRuolo(utenteBean.getRuolo());

        res = utenteDAO.editUtente(utente);

        return res;
    }

    public List<CorsoBean> getEnrollmentsByUtente(UtenteBean utenteBean) throws Exception {
        Utente utente = utenteDAO.getUtenteById(utenteBean.getCf());
        List<CorsoBean> corsoBeanList = new ArrayList<>();
        for (Corso c: utente.getEnrollments()) {
            CorsoBean corsoBean = new CorsoBean(c.getName());
            corsoBeanList.add(corsoBean);
        }
        return corsoBeanList;
    }

    public String removeEnrollmentByUtente(UtenteBean utenteBean, CorsoBean corsoBean) throws Exception {
        Utente utente = utenteDAO.getUtenteById(utenteBean.getCf());
        Corso corso = corsoDAO.getCorsoByNome(corsoBean.getName());
        return utenteCorsoDAO.removeEnrollmentByUtente(utente, corso);
    }

    public String addEnrollmentToUtente(UtenteBean utenteBean, CorsoBean corsoBean) throws Exception {
        Utente utente = utenteDAO.getUtenteById(utenteBean.getCf());
        Corso corso = corsoDAO.getCorsoByNome(corsoBean.getName());
        return utenteCorsoDAO.addEnrollmentToUtente(utente, corso);
    }

    public List<CorsoBean> getTeachingByUtente(UtenteBean utenteBean) throws Exception {
        Utente utente = utenteDAO.getUtenteById(utenteBean.getCf());
        List<CorsoBean> corsoBeanList = new ArrayList<>();
        for (Corso c: utente.getTeachings()) {
            CorsoBean corsoBean = new CorsoBean(c.getName());
            corsoBeanList.add(corsoBean);
        }
        return corsoBeanList;
    }
}