package it.uniroma2.dicii.ispw.utente;

import it.uniroma2.dicii.ispw.utente.dao.FileSystemPersistence;
import it.uniroma2.dicii.ispw.utente.dao.UtenteDAO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Utente implements Serializable {
    private String name;
    private String surname;
    private String cf;
    private Date birthDate;

    public Utente(){}

    public Utente(String name, String surname, String cf, Date birthDate) {
        this.name = name;
        this.surname = surname;
        this.cf = cf;
        this.birthDate = birthDate;
    }

    public void storeIscritto(){
        //need to clarify in which point make decision
        UtenteDAO utenteDAO = new FileSystemPersistence();
        utenteDAO.insertIscritto(this);
    }

    public List<Utente> getAllIscritti(){
        UtenteDAO utenteDAO = new FileSystemPersistence();
        return utenteDAO.getAllIscritti();
    }
}
