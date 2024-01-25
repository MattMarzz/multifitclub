package it.uniroma2.dicii.ispw.model.corso.dao;

import it.uniroma2.dicii.ispw.exception.ItemAlreadyExistsException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.model.corso.Corso;

import java.util.List;

public interface CorsoDAO {
    public Corso getCorsoByNome(String nome) throws ItemNotFoundException;
    public List<Corso> getAllCorsi();
    public void insertCorso(Corso corso) throws ItemAlreadyExistsException;
    public void removeCorso(Corso corso);
}
