package it.uniroma2.dicii.ispw.utente.dao;

import it.uniroma2.dicii.ispw.beans.UtenteBean;
import it.uniroma2.dicii.ispw.utente.Utente;


public interface UtenteDAO {
    public String insertUtente(Utente utente) throws Exception;
    public Utente auth(UtenteBean utenteBean) throws Exception;

}