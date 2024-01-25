package it.uniroma2.dicii.ispw.model.utente.dao;

import it.uniroma2.dicii.ispw.bean.LoginBean;
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
    public Utente getUtenteByCf(String cf) {
        return null;
    }

    @Override
    public List<Utente> getAllUtenti() {
        return null;
    }

    @Override
    public String editUtente(Utente utente) {
        return null;
    }

}
