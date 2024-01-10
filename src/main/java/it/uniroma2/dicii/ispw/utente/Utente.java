package it.uniroma2.dicii.ispw.utente;

import java.io.Serializable;
import java.util.Date;

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

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getCf() {
        return cf;
    }

    public Date getBirthDate() {
        return birthDate;
    }
}
