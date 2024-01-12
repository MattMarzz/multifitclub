package it.uniroma2.dicii.ispw.models.utente.dao;

import it.uniroma2.dicii.ispw.beans.UtenteBean;
import it.uniroma2.dicii.ispw.models.utente.Utente;

public class UtenteFS implements UtenteDAO {
    @Override
    public String insertUtente(Utente utente) {
        return "";
    }

    @Override
    public Utente auth(UtenteBean utenteBean){
        return null;
    }

    @Override
    public Utente getUtenteById(String cf) throws Exception {
        return null;
    }

}
