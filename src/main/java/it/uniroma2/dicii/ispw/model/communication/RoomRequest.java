package it.uniroma2.dicii.ispw.model.communication;

import it.uniroma2.dicii.ispw.App;
import it.uniroma2.dicii.ispw.enums.RoomRequestStatus;
import it.uniroma2.dicii.ispw.enums.TypesOfPersistenceLayer;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.model.communication.dao.RoomRequestDAO;
import it.uniroma2.dicii.ispw.model.communication.dao.RoomRequestDBMS;
import it.uniroma2.dicii.ispw.model.communication.dao.RoomRequestFS;
import it.uniroma2.dicii.ispw.notification.Client;
import it.uniroma2.dicii.ispw.utils.LoggerManager;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;

public class RoomRequest extends CommunicationBase implements Serializable {
    private int reqId = -1;
    private Timestamp when;
    private RoomRequestStatus status;
    private String room;

    private transient RoomRequestDAO roomRequestDAO;

    public RoomRequest() {
        setPersistenceLayer();
    }

    public RoomRequest(String sender, int id, String title, String msg, Timestamp date, String room, Timestamp when, RoomRequestStatus status) {
        super(sender, title, msg, date);
        this.reqId = id;
        this.status = status;
        this.when = when;
        this.room = room;
        setPersistenceLayer();
    }

    private void setPersistenceLayer() {
        if(App.getPersistenceLayer().equals(TypesOfPersistenceLayer.JDBC)) {
            roomRequestDAO = new RoomRequestDBMS();
        }
        else {
            try {
                roomRequestDAO = new RoomRequestFS();
            } catch (IOException e) {
                LoggerManager.logSevereException("Impossibile dialogare con il file system", e);
            }
        }
    }

    @Override
    public void sendCommunication(Client client) {
        //check if it's a request response from secretary then set status
        if(this.reqId != -1) {
            try {
                this.setSender(roomRequestDAO.requestResponse(this));
            } catch (ItemNotFoundException e) {
                LoggerManager.logSevere(e.getMessage());
            }

        } else {
            //save the new room request into persistence layer
            try {
                roomRequestDAO.insertRoomRequest(this);
            } catch (Exception e) {
                LoggerManager.logSevereException("Connessione in persistenza fallita", e);
            }
        }

        if(client != null)
            client.sendRoomRequestNotification(this);
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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getReqId() {
        return reqId;
    }

    public void setReqId(int reqId) {
        this.reqId = reqId;
    }
}
