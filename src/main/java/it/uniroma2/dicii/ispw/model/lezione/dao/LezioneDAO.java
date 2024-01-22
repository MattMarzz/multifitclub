package it.uniroma2.dicii.ispw.model.lezione.dao;

import it.uniroma2.dicii.ispw.model.lezione.Lezione;

import java.util.List;

public interface LezioneDAO {
    public List<Lezione> getLezioniByCourseId(String nomeCorso) throws Exception;
}
