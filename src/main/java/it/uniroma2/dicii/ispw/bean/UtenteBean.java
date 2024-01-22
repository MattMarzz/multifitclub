package it.uniroma2.dicii.ispw.bean;

import it.uniroma2.dicii.ispw.enums.Ruolo;

public class UtenteBean {
    private String name;
    private String surname;
    private String cf;
    private String birthDate;
    private String email;
    private String password;
    private Ruolo ruolo;

    public UtenteBean() {
    }

    public UtenteBean(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UtenteBean(String name, String surname, String cf, String birthDate, String email, String password, Ruolo ruolo) {
        this.name = name;
        this.surname = surname;
        this.cf = cf;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Ruolo getRuolo() {
        return ruolo;
    }

    public void setRuolo(Ruolo ruolo) {
        this.ruolo = ruolo;
    }
}
