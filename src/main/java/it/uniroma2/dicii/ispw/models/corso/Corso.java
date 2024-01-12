package it.uniroma2.dicii.ispw.models.corso;

import it.uniroma2.dicii.ispw.App;
import it.uniroma2.dicii.ispw.enums.TypersOfPersistenceLayer;
import it.uniroma2.dicii.ispw.enums.UserRoleInCourse;
import it.uniroma2.dicii.ispw.models.corso.dao.UtenteCorsoDAO;
import it.uniroma2.dicii.ispw.models.corso.dao.UtenteCorsoDBMS;
import it.uniroma2.dicii.ispw.models.lezione.Lezione;
import it.uniroma2.dicii.ispw.models.lezione.dao.LezioneDAO;
import it.uniroma2.dicii.ispw.models.lezione.dao.LezioneDBMS;
import it.uniroma2.dicii.ispw.models.utente.Utente;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class Corso implements Serializable {
    private String name;
    private Date startDate;
    private List<Lezione> lezioneList;
    private List<Utente> enrolledUsers;
    private List<Utente> teachers;

    private UtenteCorsoDAO utenteCorsoDAO;
    private LezioneDAO lezioneDAO;

    public Corso() {
        if(App.getPersistenceLayer().equals(TypersOfPersistenceLayer.JDBC)){
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
                e.printStackTrace();
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
                e.printStackTrace();
                return null;
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
                e.printStackTrace();
                return null;
            }
        }
        return teachers;
    }

    public void setTeachers(List<Utente> teachers) {
        this.teachers = teachers;
    }
}
