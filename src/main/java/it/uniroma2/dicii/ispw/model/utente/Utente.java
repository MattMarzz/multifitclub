package it.uniroma2.dicii.ispw.model.utente;

import it.uniroma2.dicii.ispw.App;
import it.uniroma2.dicii.ispw.bean.CommunicationBean;
import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.enums.Ruolo;
import it.uniroma2.dicii.ispw.enums.TypesOfCommunications;
import it.uniroma2.dicii.ispw.enums.TypesOfPersistenceLayer;
import it.uniroma2.dicii.ispw.enums.UserRoleInCourse;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.notification.Client;
import it.uniroma2.dicii.ispw.model.communication.CommunicationBase;
import it.uniroma2.dicii.ispw.model.communication.CommunicationFactory;
import it.uniroma2.dicii.ispw.model.corso.Corso;
import it.uniroma2.dicii.ispw.model.corso.dao.UtenteCorsoDAO;
import it.uniroma2.dicii.ispw.model.corso.dao.UtenteCorsoDBMS;
import it.uniroma2.dicii.ispw.utils.DateParser;
import it.uniroma2.dicii.ispw.utils.LoggerManager;

import java.util.Objects;
import java.io.Serializable;
import java.util.ArrayList;
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

    private transient UtenteCorsoDAO utenteCorsoDAO;

    public Utente(){
        if(App.getPersistenceLayer().equals(TypesOfPersistenceLayer.JDBC)) {
            utenteCorsoDAO = new UtenteCorsoDBMS();
        }
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

    public void sendCommunication(TypesOfCommunications toc, CommunicationBean comBean, Client client) {
        CommunicationBase communicationBase;
        if(toc.equals(TypesOfCommunications.ANNOUNCEMENT)) {
            communicationBase = new CommunicationFactory().createAnnouncement(this.cf, comBean.getTitle(), comBean.getText(), comBean.getDate());
        } else {
            communicationBase = new CommunicationFactory().createRoomRequest(this.cf, comBean.getId(), comBean.getTitle(), comBean.getText(),
                    comBean.getDate(), comBean.getRoom(), comBean.getWhen(), comBean.getStatus());
        }
        communicationBase.sendCommunication(client);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utente utente = (Utente) o;
        return Objects.equals(this.cf, utente.cf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.cf);
    }

    public Utente(UtenteBean ub) throws InvalidDataException {
        this();
        this.name = ub.getName();
        this.surname = ub.getSurname();
        this.cf = ub.getCf();
        this.birthDate = DateParser.parseStringToDateUtil(ub.getBirthDate());
        this.email = ub.getEmail();
        this.password = ub.getPassword();
        this.ruolo = ub.getRuolo();
    }

    public void updateInfos(UtenteBean ub) throws InvalidDataException {
        this.name = ub.getName();
        this.surname = ub.getSurname();
        this.birthDate = DateParser.parseStringToDateUtil(ub.getBirthDate());
        this.email = ub.getEmail();
        this.ruolo = ub.getRuolo();
    }

    public List<Corso> getEnrollments() {
        if(this.enrollments == null) {
            try {
                this.enrollments = utenteCorsoDAO.getCoursesByUserId(this.cf, UserRoleInCourse.ENROLLMENT);
            } catch (Exception e) {
                enrollments = new ArrayList<>();
                LoggerManager.logInfoException(e.getMessage(), e);
            }
        }
        return enrollments;
    }

    public List<Corso> getTeachings() {
        if(this.teachings == null && this.ruolo == Ruolo.ISTRUTTORE) {
            try {
                this.teachings = utenteCorsoDAO.getCoursesByUserId(this.cf, UserRoleInCourse.TEACHING);
            } catch (Exception e) {
                teachings = new ArrayList<>();
                LoggerManager.logInfoException(e.getMessage(), e);
            }
        }
        return teachings;
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
    public void setEnrollments(List<Corso> enrollments) {
        this.enrollments = enrollments;
    }
    public void setTeachings(List<Corso> teachings) {
        this.teachings = teachings;
    }

}