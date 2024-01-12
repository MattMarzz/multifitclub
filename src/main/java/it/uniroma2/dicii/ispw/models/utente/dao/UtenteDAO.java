package it.uniroma2.dicii.ispw.models.utente.dao;

import it.uniroma2.dicii.ispw.beans.UtenteBean;
import it.uniroma2.dicii.ispw.models.utente.Utente;


public interface UtenteDAO {
    public String insertUtente(Utente utente) throws Exception;
    public Utente auth(UtenteBean utenteBean) throws Exception;
    public Utente getUtenteById(String cf) throws Exception;

}