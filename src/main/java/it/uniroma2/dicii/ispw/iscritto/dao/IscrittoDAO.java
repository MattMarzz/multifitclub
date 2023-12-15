package it.uniroma2.dicii.ispw.iscritto.dao;

import it.uniroma2.dicii.ispw.iscritto.Iscritto;

import java.util.List;

public interface IscrittoDAO {
    public void insertIscritto(Iscritto iscritto);
    public List<Iscritto> getAllIscritti();
}
