package it.uniroma2.dicii.ispw.model.communication;

import it.uniroma2.dicii.ispw.notification.Client;

import java.io.Serializable;
import java.sql.Timestamp;

public abstract class CommunicationBase implements Serializable {
    protected String sender;
    protected String title;
    protected String msg;
    protected Timestamp date;

    protected CommunicationBase() {
    }

    protected CommunicationBase(String sender, String title, String msg, Timestamp date) {
        this.sender = sender;
        this.title = title;
        this. msg = msg;
        this.date = date;
    }

    public abstract void sendCommunication(Client client);

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}
