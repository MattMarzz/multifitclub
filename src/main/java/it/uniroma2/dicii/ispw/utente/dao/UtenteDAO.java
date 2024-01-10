package it.uniroma2.dicii.ispw.utente.dao;

import it.uniroma2.dicii.ispw.utente.Utente;

import java.util.List;

public interface UtenteDAO {
    public void insertIscritto(Utente utente);
    public List<Utente> getAllIscritti();
}
