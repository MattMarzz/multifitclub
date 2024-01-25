package it.uniroma2.dicii.ispw.controller;

import it.uniroma2.dicii.ispw.App;
import it.uniroma2.dicii.ispw.bean.AnnouncementBean;
import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.enums.TypesOfPersistenceLayer;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.model.announcement.Announcement;
import it.uniroma2.dicii.ispw.model.announcement.dao.AnnouncementDAO;
import it.uniroma2.dicii.ispw.model.announcement.dao.AnnouncementDBMS;
import it.uniroma2.dicii.ispw.model.utente.Utente;
import it.uniroma2.dicii.ispw.utils.LoggerManager;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ManageAnnouncementController {

    private AnnouncementDAO announcementDAO;

    public ManageAnnouncementController() {
        if(App.getPersistenceLayer().equals(TypesOfPersistenceLayer.JDBC)) {
            announcementDAO = new AnnouncementDBMS();
        }
//        else {
//            announcementDAO = new AnnouncementFS();
//        }
    }

    public String publishNewAnnouncement(AnnouncementBean announcementBean) throws InvalidDataException {
        String res = "";

        if(announcementBean.getText().isBlank())
            throw new InvalidDataException("Inseire testo dell'annuncio");
        if(announcementBean.getTitle().isBlank())
            throw new InvalidDataException("Inserire titolo dell'annuncio");
        if(announcementBean.getSender() == null)
            throw new InvalidDataException("Mittente non specificato");

        Announcement ann = new Announcement();
        ann.setTitle(announcementBean.getTitle());
        ann.setText(announcementBean.getText());
        ann.setDate(Timestamp.valueOf(LocalDateTime.now()));
        ann.setSender(announcementBean.getSender());

        //save the announcement into persistence layer
        try {
            res = announcementDAO.insertAnnouncement(ann);
        } catch (Exception e) {
            LoggerManager.logSevereException("Connessione al db fallita", e);
            return "Impossibile inserire annuncio";
        }

        //load all observer
//        GestioneUtentiController gestioneUtentiController = new GestioneUtentiController();
//        List<UtenteBean> utenteBeanList = gestioneUtentiController.getAllUtenti();
//        List<Observer> utenteList = new ArrayList<>();
//        try {
//            for (UtenteBean ub: utenteBeanList) {
//                Utente u = new Utente(ub.getName(), ub.getSurname(), ub.getCf(),
//                        new SimpleDateFormat("yyyy-MM-dd").parse(ub.getBirthDate()), ub.getEmail(), null, ub.getRuolo());
//                utenteList.add(u);
//            }
//        } catch (ParseException e) {
//            LoggerManager.logSevereException("Impossibile convertire la data", e);
//            return "annuncio inviato ma non notificato!";
//        }
//        ann.attachFullList(utenteList);
//        ann.notifyChanges();
        ann.appendAnnouncement();

        return res;
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

    public List<AnnouncementBean> getAllAnnouncementBean() {
        List<Announcement> al = getAllAnnouncement();
        List<AnnouncementBean> announcementBeanList = new ArrayList<>();
        for (Announcement a: al ) {
            AnnouncementBean ab = new AnnouncementBean();
            ab.setTitle(a.getTitle());
            ab.setText(a.getText());
            ab.setSender(a.getSender());
            ab.setDate(a.getDate());
            announcementBeanList.add(ab);
        }
        return announcementBeanList;
    }

}
