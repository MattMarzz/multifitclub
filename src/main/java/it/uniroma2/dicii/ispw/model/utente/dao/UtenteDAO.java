package it.uniroma2.dicii.ispw.model.utente.dao;

import it.uniroma2.dicii.ispw.bean.LoginBean;
import it.uniroma2.dicii.ispw.exception.ItemAlreadyExistsException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.model.utente.Utente;

import java.util.List;


public interface UtenteDAO {
    public String insertUtente(Utente utente) throws ItemAlreadyExistsException;
    public Utente auth(LoginBean loginBean) throws ItemNotFoundException;
    public Utente getUtenteByCf(String cf) throws ItemNotFoundException;
    public List<Utente> getAllUtenti();
    public String editUtente(Utente utente);
}