package it.uniroma2.dicii.ispw.model.corso.dao;

import it.uniroma2.dicii.ispw.enums.UserRoleInCourse;
import it.uniroma2.dicii.ispw.model.corso.Corso;
import it.uniroma2.dicii.ispw.model.utente.Utente;

import java.util.List;

public interface UtenteCorsoDAO {
    public List<Corso> getCoursesByUserId(String cf, UserRoleInCourse userRoleInCourse) throws Exception;
    public List<Utente> getUsersByCourseId(String nomeCorso, UserRoleInCourse userRoleInCourse) throws Exception;
    public String removeEnrollmentByUtente(Utente utente, Corso corso) throws Exception;
    public String addEnrollmentToUtente(Utente utente, Corso corso) throws Exception;

}