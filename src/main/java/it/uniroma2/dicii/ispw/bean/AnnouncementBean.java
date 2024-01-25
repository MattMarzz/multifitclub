package it.uniroma2.dicii.ispw.bean;

import it.uniroma2.dicii.ispw.model.utente.Utente;

import java.sql.Timestamp;

public class AnnouncementBean {
    private String title;
    private String text;
    private Timestamp date;
    private String sender;

    public AnnouncementBean() {
    }

    public AnnouncementBean(String title, String text, Timestamp date, String sender) {
        this.title = title;
        this.text = text;
        this.date = date;
        this.sender = sender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
