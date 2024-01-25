package it.uniroma2.dicii.ispw.model.announcement;

import it.uniroma2.dicii.ispw.controller.Subject;

import java.io.Serializable;
import java.sql.Timestamp;

public class Announcement extends Subject implements Serializable {

    private int id;
    private String title;
    private String text;
    private Timestamp date;
    private String sender;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
