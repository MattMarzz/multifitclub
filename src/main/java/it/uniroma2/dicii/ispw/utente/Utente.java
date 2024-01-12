package it.uniroma2.dicii.ispw.utente;

import it.uniroma2.dicii.ispw.enums.Ruolo;

import java.io.Serializable;
import java.util.Date;

public class Utente implements Serializable {
    private String name;
    private String surname;
    private String cf;
    private Date birthDate;
    private String email;
    private String password;
    private Ruolo ruolo;

    public Utente(){}


    public Utente(String name, String surname, String cf, Date birthDate, String email, String password, Ruolo ruolo) {
        this.name = name;
        this.surname = surname;
        this.cf = cf;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }
}
