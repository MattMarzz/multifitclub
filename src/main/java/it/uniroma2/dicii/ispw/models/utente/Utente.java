package it.uniroma2.dicii.ispw.models.utente;

import it.uniroma2.dicii.ispw.App;
import it.uniroma2.dicii.ispw.enums.Ruolo;
import it.uniroma2.dicii.ispw.enums.TypersOfPersistenceLayer;
import it.uniroma2.dicii.ispw.enums.UserRoleInCourse;
import it.uniroma2.dicii.ispw.models.corso.Corso;
import it.uniroma2.dicii.ispw.models.corso.dao.UtenteCorsoDAO;
import it.uniroma2.dicii.ispw.models.corso.dao.UtenteCorsoDBMS;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Utente implements Serializable {
    private String name;
    private String surname;
    private String cf;
    private Date birthDate;
    private String email;
    private String password;
    private Ruolo ruolo;
    private List<Corso> enrollments;
    private List<Corso> teachings;

    private UtenteCorsoDAO utenteCorsoDAO;

    public Utente(){
        if(App.getPersistenceLayer().equals(TypersOfPersistenceLayer.JDBC))
            utenteCorsoDAO = new UtenteCorsoDBMS();
//        else
//            utenteCorsoDAO = new UtenteCorsoFS();
    }


    public Utente(String name, String surname, String cf, Date birthDate, String email, String password, Ruolo ruolo) {
        this();
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

    public List<Corso> getEnrollments() {
        if(this.enrollments == null) {
            try {
                this.enrollments = utenteCorsoDAO.getCoursesByUserId(this.cf, UserRoleInCourse.ENROLLMENT);
            } catch (Exception e) {
                enrollments = null;
                System.out.println(e.getMessage());
            }
        }
        return enrollments;
    }

    public void setEnrollments(List<Corso> enrollments) {
        this.enrollments = enrollments;
    }

    public List<Corso> getTeachings() {
        if(this.teachings != null && this.ruolo == Ruolo.ISTRUTTORE) {
            try {
                this.teachings = utenteCorsoDAO.getCoursesByUserId(this.cf, UserRoleInCourse.TEACHING);
            } catch (Exception e) {
                teachings = null;
                System.out.println(e.getMessage());
            }
        }
        return teachings;
    }

    public void setTeachings(List<Corso> teachings) {
        this.teachings = teachings;
    }
}