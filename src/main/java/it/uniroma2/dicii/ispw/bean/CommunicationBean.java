package it.uniroma2.dicii.ispw.bean;

import it.uniroma2.dicii.ispw.enums.RoomRequestStatus;

import java.sql.Timestamp;

public class CommunicationBean {
    private String title;
    private String text;
    private Timestamp date;
    private String sender;
    private Timestamp when;
    private RoomRequestStatus status;
    private String room;
    private int id = -1;

    public CommunicationBean() {
    }

    public CommunicationBean(String title, String text, Timestamp date, String sender) {
        this.title = title;
        this.text = text;
        this.date = date;
        this.sender = sender;
    }

    public CommunicationBean(String title, String text, Timestamp date, String sender, Timestamp when, RoomRequestStatus status, String room) {
        this.title = title;
        this.text = text;
        this.date = date;
        this.sender = sender;
        this.when = when;
        this.status = status;
        this.room = room;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
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

    public Timestamp getWhen() {
        return when;
    }

    public void setWhen(Timestamp when) {
        this.when = when;
    }

    public RoomRequestStatus getStatus() {
        return status;
    }

    public void setStatus(RoomRequestStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
