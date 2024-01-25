package it.uniroma2.dicii.ispw.model.utente.dao;

import it.uniroma2.dicii.ispw.bean.LoginBean;
import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.model.announcement.Announcement;
import it.uniroma2.dicii.ispw.model.corso.Corso;
import it.uniroma2.dicii.ispw.model.utente.Utente;

import java.util.List;


public interface UtenteDAO {
    public String insertUtente(Utente utente) throws Exception;
    public Utente auth(LoginBean loginBean) throws Exception;
    public Utente getUtenteById(String cf) throws Exception;
    public List<Utente> getAllUtenti() throws Exception;
    public String editUtente(Utente utente) throws Exception;
}