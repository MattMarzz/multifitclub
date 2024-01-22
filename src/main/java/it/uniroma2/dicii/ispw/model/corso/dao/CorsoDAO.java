package it.uniroma2.dicii.ispw.model.corso.dao;

import it.uniroma2.dicii.ispw.bean.CorsoBean;
import it.uniroma2.dicii.ispw.model.corso.Corso;

import java.util.List;

public interface CorsoDAO {
    public Corso getCorsoByNome(String nome) throws Exception;
    public List<Corso> getAllCorsi() throws Exception;
    public String addCorso(Corso corso) throws Exception;
    public String removeCorso(Corso corso) throws Exception;
}
