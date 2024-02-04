package it.uniroma2.dicii.ispw.model.communication;


import it.uniroma2.dicii.ispw.App;
import it.uniroma2.dicii.ispw.enums.TypesOfPersistenceLayer;
import it.uniroma2.dicii.ispw.model.communication.dao.AnnouncementDAO;
import it.uniroma2.dicii.ispw.model.communication.dao.AnnouncementDBMS;
import it.uniroma2.dicii.ispw.notification.Client;
import it.uniroma2.dicii.ispw.utils.LoggerManager;

import java.io.Serializable;
import java.sql.Timestamp;

public class Announcement extends CommunicationBase implements Serializable {
    private int AnnId;
    private transient AnnouncementDAO announcementDAO;

    public Announcement(){
        setPersistenceLayer();
    }
    public Announcement(String sender, String title, String msg, Timestamp date) {
        super(sender, title, msg, date);
        setPersistenceLayer();
    }

    private void setPersistenceLayer() {
        if(App.getPersistenceLayer().equals(TypesOfPersistenceLayer.JDBC)) {
            announcementDAO = new AnnouncementDBMS();
        }
//        else {
//            announcementDAO = new AnnouncementFS();
//        }
    }

    @Override
    public void sendCommunication(Client client) {
        //save the announcement into persistence layer
        try {
            announcementDAO.insertAnnouncement(this);
        } catch (Exception e) {
            LoggerManager.logSevereException("Connessione al db fallita", e);
        }

        if(client != null)
            client.sendAnnouncementNotification();
    }

    public void setAnnId(int annId) {
        AnnId = annId;
    }
}
