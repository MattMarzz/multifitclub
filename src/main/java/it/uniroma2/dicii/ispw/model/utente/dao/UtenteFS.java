package it.uniroma2.dicii.ispw.model.utente.dao;

import it.uniroma2.dicii.ispw.bean.LoginBean;
import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.model.announcement.Announcement;
import it.uniroma2.dicii.ispw.model.corso.Corso;
import it.uniroma2.dicii.ispw.model.utente.Utente;

import java.util.List;

public class UtenteFS implements UtenteDAO {
    @Override
    public String insertUtente(Utente utente) {
        return "";
    }

    @Override
    public Utente auth(LoginBean loginBean){
        return null;
    }

    @Override
    public Utente getUtenteById(String cf) throws Exception {
        return null;
    }

    @Override
    public List<Utente> getAllUtenti() {
        return null;
    }

    @Override
    public String editUtente(Utente utente) throws Exception {
        return null;
    }

}
