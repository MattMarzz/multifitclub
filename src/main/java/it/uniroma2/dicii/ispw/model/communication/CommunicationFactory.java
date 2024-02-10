package it.uniroma2.dicii.ispw.model.communication;

import it.uniroma2.dicii.ispw.enums.RoomRequestStatus;

import java.sql.Timestamp;

public class CommunicationFactory {
    public Announcement createAnnouncement(String cf, String title, String msg, Timestamp date) {
        return new Announcement(cf, title, msg, date);
    }

    public RoomRequest createRoomRequest(String cf, int id, String title, String msg, Timestamp date, ReservationInfo resInfo, RoomRequestStatus status) {
        return new RoomRequest(cf, id, title, msg, date, resInfo, status);
    }
}
