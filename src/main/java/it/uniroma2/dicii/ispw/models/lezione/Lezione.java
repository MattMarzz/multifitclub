package it.uniroma2.dicii.ispw.models.lezione;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.TimeZone;

public class Lezione implements Serializable {
    private  String day;
    private Time startTime;
    private String courseName;
    private String cfUtente;

    public Lezione(){}
    public Lezione(String day, Time startTime, String courseName, String cfUtente) {
        this.day = day;
        this.startTime = startTime;
        this.courseName = courseName;
        this.cfUtente = cfUtente;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Time getStarTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
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
}
