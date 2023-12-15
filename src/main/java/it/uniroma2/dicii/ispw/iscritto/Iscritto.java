package it.uniroma2.dicii.ispw.iscritto;

import it.uniroma2.dicii.ispw.iscritto.dao.FileSystemPersistence;
import it.uniroma2.dicii.ispw.iscritto.dao.IscrittoDAO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Iscritto implements Serializable {
    private String name;
    private String surname;
    private String cf;
    private Date birthDate;

    public Iscritto(){}

    public Iscritto(String name, String surname, String cf, Date birthDate) {
        this.name = name;
        this.surname = surname;
        this.cf = cf;
        this.birthDate = birthDate;
    }

    public void storeIscritto(){
        //need to clarify in which point make decision
        IscrittoDAO iscrittoDAO = new FileSystemPersistence();
        iscrittoDAO.insertIscritto(this);
    }

    public List<Iscritto> getAllIscritti(){
        IscrittoDAO iscrittoDAO = new FileSystemPersistence();
        return iscrittoDAO.getAllIscritti();
    }
}
