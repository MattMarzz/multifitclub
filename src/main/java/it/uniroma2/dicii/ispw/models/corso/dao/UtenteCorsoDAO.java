package it.uniroma2.dicii.ispw.models.corso.dao;

import it.uniroma2.dicii.ispw.enums.UserRoleInCourse;
import it.uniroma2.dicii.ispw.models.corso.Corso;
import it.uniroma2.dicii.ispw.models.utente.Utente;

import java.util.List;

public interface UtenteCorsoDAO {
    public List<Corso> getCoursesByUserId(String cf, UserRoleInCourse userRoleInCourse) throws Exception;
    public List<Utente> getUsersByCourseId(String nomeCorso, UserRoleInCourse userRoleInCourse) throws Exception;

}