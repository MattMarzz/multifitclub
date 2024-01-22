package it.uniroma2.dicii.ispw.model.corso;

import it.uniroma2.dicii.ispw.App;
import it.uniroma2.dicii.ispw.enums.TypesOfPersistenceLayer;
import it.uniroma2.dicii.ispw.enums.UserRoleInCourse;
import it.uniroma2.dicii.ispw.model.corso.dao.UtenteCorsoDAO;
import it.uniroma2.dicii.ispw.model.corso.dao.UtenteCorsoDBMS;
import it.uniroma2.dicii.ispw.model.lezione.Lezione;
import it.uniroma2.dicii.ispw.model.lezione.dao.LezioneDAO;
import it.uniroma2.dicii.ispw.model.lezione.dao.LezioneDBMS;
import it.uniroma2.dicii.ispw.model.utente.Utente;
import it.uniroma2.dicii.ispw.utils.LoggerManager;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Corso implements Serializable {
    private String name;
    private Date startDate;
    private List<Lezione> lezioneList;
    private List<Utente> enrolledUsers;
    private List<Utente> teachers;

    private transient UtenteCorsoDAO utenteCorsoDAO;
    private transient LezioneDAO lezioneDAO;

    public Corso() {
        if(App.getPersistenceLayer().equals(TypesOfPersistenceLayer.JDBC)){
            utenteCorsoDAO = new UtenteCorsoDBMS();
            lezioneDAO = new LezioneDBMS();
        }
//      else
//          utenteCorsoDAO = new UtenteCorsoFS();
    }

    public Corso(String name, Date startDate) {
        this();
        this.name = name;
        this.startDate = startDate;
    }

    public Corso(String name) {
        this();
        this.name = name;
    }

    public Corso(String name, Date startDate, List<Lezione> lezioneList) {
        this();
        this.name = name;
        this.startDate = startDate;
        this.lezioneList = lezioneList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public List<Lezione> getLezioneList() {
        if(this.lezioneList == null) {
            try {
                this.lezioneList = lezioneDAO.getLezioniByCourseId(this.name);
            }catch (Exception e) {
                LoggerManager.logInfoException(e.getMessage(), e);
                return lezioneList;
            }
        }
        return lezioneList;
    }

    public void setLezioneList(List<Lezione> lezioneList) {
        this.lezioneList = lezioneList;
    }

    public List<Utente> getEnrolledUsers() {
        if(this.enrolledUsers == null) {
            try {
                this.enrolledUsers = utenteCorsoDAO.getUsersByCourseId(this.name, UserRoleInCourse.ENROLLMENT);
            } catch (Exception e) {
                LoggerManager.logInfoException(e.getMessage(), e);
                return enrolledUsers;
            }
        }
        return enrolledUsers;
    }

    public void setEnrolledUsers(List<Utente> enrolledUsers) {
        this.enrolledUsers = enrolledUsers;
    }

    public List<Utente> getTeachers() {
        if(this.teachers == null) {
            try {
                this.teachers = utenteCorsoDAO.getUsersByCourseId(this.name, UserRoleInCourse.TEACHING);
            } catch (Exception e) {
                LoggerManager.logInfoException(e.getMessage(), e);
                return teachers;
            }
        }
        return teachers;
    }

    public void setTeachers(List<Utente> teachers) {
        this.teachers = teachers;
    }
}
