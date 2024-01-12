package it.uniroma2.dicii.ispw.models.lezione.dao;

import it.uniroma2.dicii.ispw.models.lezione.Lezione;

import java.util.List;

public interface LezioneDAO {
    public List<Lezione> getLezioniByCourseId(String nomeCorso) throws Exception;
}
