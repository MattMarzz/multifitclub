package it.uniroma2.dicii.ispw.model.communication;

import java.sql.Timestamp;

public class ReservationInfo {
    private Timestamp when;
    private String room;

    public ReservationInfo(Timestamp when, String room) {
        this.when = when;
        this.room = room;
    }

    public Timestamp getWhen() {
        return when;
    }

    public void setWhen(Timestamp when) {
        this.when = when;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
