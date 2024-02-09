package it.uniroma2.dicii.ispw.model.lezione;

import it.uniroma2.dicii.ispw.App;
import it.uniroma2.dicii.ispw.enums.TypesOfPersistenceLayer;
import it.uniroma2.dicii.ispw.model.communication.RoomRequest;
import it.uniroma2.dicii.ispw.model.lezione.dao.LezioneDAO;
import it.uniroma2.dicii.ispw.model.lezione.dao.LezioneDBMS;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class Lezione implements Serializable {
    private  String day;
    private Time startTime;
    private String sala;
    private String courseName;
    private String cfUtente;

    private transient LezioneDAO lezioneDAO;

    public Lezione(){
        if(App.getPersistenceLayer().equals(TypesOfPersistenceLayer.JDBC)) {
            lezioneDAO = new LezioneDBMS();
        }
//       else {
//            corsoDAO = new CorsoFS();
//            utenteCorsoDAO = new UtenteCorsoFS();
//        }
    }
    public Lezione(String day, Time startTime, String courseName, String cfUtente) {
        this();
        this.day = day;
        this.startTime = startTime;
        this.courseName = courseName;
        this.cfUtente = cfUtente;
    }

    public boolean isThereOverlap() {
        List<Lezione> lezioniList = lezioneDAO.getAllLezioniForDay(this.day);
        for (Lezione l: lezioniList) {
            //check for overlapping
            Time fineLezione = Time.valueOf(this.getStartTime().toLocalTime().plusHours(1));
            Time fineLezionetemp = Time.valueOf(l.getStartTime().toLocalTime().plusHours(1));

            if((this.getStartTime().before(l.getStartTime()) && fineLezione.after(l.getStartTime()) ) ||
                    (this.getStartTime().after(l.getStartTime()) && this.getStartTime().before(fineLezionetemp)))
                return true;
        }
        return false;
    }

    public void transformReservationIntoLesson(RoomRequest rr) {
        LocalDateTime localDateTime = rr.getWhen().toLocalDateTime();
        DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();
        this.day = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ITALIAN);
        this.cfUtente = rr.getSender();
        this.sala = rr.getRoom();
    }


    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public String getCfUtente() {
        return cfUtente;
    }
    public void setCfUtente(String cfUtente) {
        this.cfUtente = cfUtente;
    }
    public Time getStartTime() {
        return startTime;
    }
    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }
    public String getSala() {
        return sala;
    }
    public void setSala(String sala) {
        this.sala = sala;
    }
}
