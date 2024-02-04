package it.uniroma2.dicii.ispw.controller;

import it.uniroma2.dicii.ispw.App;
import it.uniroma2.dicii.ispw.bean.CommunicationBean;
import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.enums.TypesOfCommunications;
import it.uniroma2.dicii.ispw.enums.TypesOfPersistenceLayer;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.model.communication.Announcement;
import it.uniroma2.dicii.ispw.model.communication.RoomRequest;
import it.uniroma2.dicii.ispw.model.communication.dao.AnnouncementDAO;
import it.uniroma2.dicii.ispw.model.communication.dao.AnnouncementDBMS;
import it.uniroma2.dicii.ispw.model.communication.dao.RoomRequestDAO;
import it.uniroma2.dicii.ispw.model.communication.dao.RoomRequestDBMS;
import it.uniroma2.dicii.ispw.utils.LoggerManager;
import it.uniroma2.dicii.ispw.utils.LoginManager;
import it.uniroma2.dicii.ispw.notification.Client;
import it.uniroma2.dicii.ispw.model.utente.Utente;
import it.uniroma2.dicii.ispw.model.utente.dao.UtenteDAO;
import it.uniroma2.dicii.ispw.model.utente.dao.UtenteDBMS;
import it.uniroma2.dicii.ispw.model.utente.dao.UtenteFS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommunicationController {
    private UtenteDAO utenteDAO;
    private AnnouncementDAO announcementDAO;
    private RoomRequestDAO roomRequestDAO;

    public CommunicationController() {
        if(App.getPersistenceLayer().equals(TypesOfPersistenceLayer.JDBC)) {
            utenteDAO = new UtenteDBMS();
            announcementDAO = new AnnouncementDBMS();
            roomRequestDAO = new RoomRequestDBMS();
        } else {
            utenteDAO = new UtenteFS();
        }
    }

    public void forwardCommunication(UtenteBean utenteBean, CommunicationBean communicationBean, TypesOfCommunications toc) throws ItemNotFoundException, InvalidDataException {
        Utente u = utenteDAO.getUtenteByCf(utenteBean.getCf());
        Map<Utente, Client> hm = LoginManager.getInstance().getHashMap();

        if(communicationBean.getId() == -1) {
            //it means new communication
            if (communicationBean.getText().isBlank())
                throw new InvalidDataException("Inserire testo dell'annuncio");
            if (communicationBean.getTitle().isBlank())
                throw new InvalidDataException("Inserire titolo dell'annuncio");
            if (communicationBean.getSender() == null)
                throw new InvalidDataException("Mittente non specificato");
        }
        u.sendCommunication(toc, communicationBean, hm.get(u));
    }

        public List<Announcement> getAllAnnouncement() {
        List<Announcement> announcementList = new ArrayList<>();
        try {
            announcementList = announcementDAO.getAllAnnouncement();
        } catch (Exception e) {
            LoggerManager.logSevereException("Errore nel reperire la lista degli annunci", e);
            return announcementList;
        }
        return announcementList;
    }

    public List<CommunicationBean> getAllAnnouncementBean() {
        List<Announcement> al = getAllAnnouncement();
        List<CommunicationBean> communicationBeanList = new ArrayList<>();
        for (Announcement a: al ) {
            CommunicationBean cb = new CommunicationBean();
            cb.setTitle(a.getTitle());
            cb.setText(a.getMsg());
            cb.setSender(a.getSender());
            cb.setDate(a.getDate());
            communicationBeanList.add(cb);
        }
        return communicationBeanList;
    }

    public List<RoomRequest> getAllRoomRequest() {
        List<RoomRequest> requestList = new ArrayList<>();
        try {
            requestList = roomRequestDAO.getAllRequest();
        } catch (Exception e) {
            LoggerManager.logSevereException("Errore nel reperire la lista delle richiste pre le sale", e);
            return requestList;
        }
        return requestList;
    }

    public List<CommunicationBean> getAllRoomRequestBean() {
        List<RoomRequest> requestList = getAllRoomRequest();
        List<CommunicationBean> communicationBeanList = new ArrayList<>();
        for (RoomRequest rr: requestList ) {
            CommunicationBean cb = new CommunicationBean();
            cb.setId(rr.getReqId());
            cb.setTitle(rr.getTitle());
            cb.setText(rr.getMsg());
            cb.setSender(rr.getSender());
            cb.setDate(rr.getDate());
            cb.setRoom(rr.getRoom());
            cb.setWhen(rr.getWhen());
            cb.setStatus(rr.getStatus());

            communicationBeanList.add(cb);
        }
        return communicationBeanList;
    }

    public List<CommunicationBean> getRoomRequestByUtente(String cf) {
        List<CommunicationBean> communicationBeanList = new ArrayList<>();
        List<RoomRequest> roomRequests = roomRequestDAO.getRoomRequestByUtente(cf);
        for (RoomRequest r: roomRequests) {
            CommunicationBean cb = new CommunicationBean();
            cb.setId(r.getReqId());
            cb.setTitle(r.getTitle());
            cb.setText(r.getMsg());
            cb.setDate(r.getDate());
            cb.setWhen(r.getWhen());
            cb.setRoom(r.getRoom());
            cb.setSender(r.getSender());
            cb.setStatus(r.getStatus());
            communicationBeanList.add(cb);
        }
        return communicationBeanList;
    }

}
