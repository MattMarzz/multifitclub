package it.uniroma2.dicii.ispw.controller;

import it.uniroma2.dicii.ispw.App;
import it.uniroma2.dicii.ispw.bean.LezioneBean;
import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.enums.RoomRequestStatus;
import it.uniroma2.dicii.ispw.enums.Ruolo;
import it.uniroma2.dicii.ispw.enums.TypesOfPersistenceLayer;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.exception.ItemAlreadyExistsException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.model.communication.RoomRequest;
import it.uniroma2.dicii.ispw.model.communication.dao.RoomRequestDAO;
import it.uniroma2.dicii.ispw.model.communication.dao.RoomRequestDBMS;
import it.uniroma2.dicii.ispw.model.corso.Corso;
import it.uniroma2.dicii.ispw.model.lezione.Lezione;
import it.uniroma2.dicii.ispw.model.lezione.dao.LezioneDAO;
import it.uniroma2.dicii.ispw.model.lezione.dao.LezioneDBMS;
import it.uniroma2.dicii.ispw.model.lezione.dao.LezioneFS;
import it.uniroma2.dicii.ispw.model.utente.Utente;
import it.uniroma2.dicii.ispw.model.utente.dao.UtenteDAO;
import it.uniroma2.dicii.ispw.model.utente.dao.UtenteDBMS;
import it.uniroma2.dicii.ispw.model.utente.dao.UtenteFS;
import it.uniroma2.dicii.ispw.notification.Client;
import it.uniroma2.dicii.ispw.utils.LoggerManager;
import it.uniroma2.dicii.ispw.utils.LoginManager;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProgrammazioneController {

    private LezioneDAO lezioneDAO;
    private UtenteDAO utenteDAO;
    private RoomRequestDAO roomRequestDAO;


    public ProgrammazioneController() {
        if(App.getPersistenceLayer().equals(TypesOfPersistenceLayer.JDBC)) {
            lezioneDAO = new LezioneDBMS();
            utenteDAO = new UtenteDBMS();
            roomRequestDAO = new RoomRequestDBMS();
        }
       else {
            try {
                lezioneDAO = new LezioneFS();
                utenteDAO = new UtenteFS();

            } catch (IOException e) {
                LoggerManager.logSevereException("Impossibile dialogare con il file system", e);
            }
        }
    }

    public String insertLezioni(LezioneBean lezioneBean, UtenteBean utenteBean) throws ItemNotFoundException, InvalidDataException, ItemAlreadyExistsException {
        //check if course exists
        Corso c = new GestioneCorsiController().getCorsoById(lezioneBean.getCorso());
        if (c.getName() == null) throw new ItemNotFoundException("Corso specificato non esistente.");

        List<Lezione> lezioniList = new ArrayList<>();
        for (String giorno: lezioneBean.getGiornoList()) {
            Lezione l = new Lezione();
            l.setSala(lezioneBean.getSala());
            l.setCourseName(lezioneBean.getCorso());
            l.setStartTime(lezioneBean.getOra());
            l.setDay(giorno);
            if(l.isThereOverlap()) throw new InvalidDataException("La lezione si sovrappone ad una esistente nel giorno: " + giorno);

            lezioniList.add(l);
        }

        //insert lessons into persistence layer
        String res = lezioneDAO.insertLezioni(lezioniList);

        //need to update observers
        Utente u = utenteDAO.getUtenteByCf(utenteBean.getCf());
        Map<Utente, Client> hm = LoginManager.getInstance().getHashMap();
        Client client = hm.get(u);
        if(client != null)
            client.newActivitiesDetected();

        return res;
    }

    public List<LezioneBean> getAllLezioni() {
        List<Lezione> lezioneList = lezioneDAO.getAllLezioni();
        lezioneList.addAll(getConfirmedReservation(null));
        return lezioneToLezioneBean(lezioneList);
    }

    public List<LezioneBean> getLezioniByUtente(String cf) throws ItemNotFoundException {
        List<Corso> courses;
        List<Lezione> lessons = new ArrayList<>();

        Utente u = new GestioneUtentiController().getUtenteByCf(cf);
        if(u.getRuolo().equals(Ruolo.ISTRUTTORE)) {
            courses = u.getTeachings();
        } else {
            courses = u.getEnrollments();
        }

        for (Corso c: courses) {
            List<Lezione> tempList = lezioneDAO.getLezioniByCourseId(c.getName());
            lessons.addAll(tempList);
        }
        lessons.addAll(getConfirmedReservation(cf));
        return lezioneToLezioneBean(lessons);
    }

    public List<LezioneBean> lezioneToLezioneBean(List<Lezione> lezioneList) {
        List<LezioneBean> lezioneBeanList = new ArrayList<>();
        for (Lezione l: lezioneList) {
            LezioneBean lb = new LezioneBean();
            lb.setCorso(l.getCourseName());
            lb.setSala(l.getSala());
            lb.setOra(l.getStartTime());
            lb.setGiorno(l.getDay());
            lb.setCf(l.getCfUtente());
            lezioneBeanList.add(lb);
        }
        return lezioneBeanList;
    }

    public List<Lezione> getConfirmedReservation(String cf) {
        List<RoomRequest> roomRequests;
        List<Lezione> lezioneList = new ArrayList<>();

        if(cf == null) {
            //retrieve all confirmed reservation
            roomRequests = roomRequestDAO.getAllAcceptedRequest();
        } else {
            //retrieve all confirmed reservation for user
            roomRequests = roomRequestDAO.getRoomRequestByUtente(cf);
            roomRequests.removeIf(rr -> !rr.getStatus().equals(RoomRequestStatus.ACCEPTED));
        }

        lezioneList = getLessonFromReservation(roomRequests);
        return lezioneList;
    }

    public List<Lezione> getLessonFromReservation(List<RoomRequest> roomRequests) {
        List<Lezione> lezioneList = new ArrayList<>();
        for (RoomRequest rr: roomRequests) {
            //check if rr is expired or not
            if(rr.getWhen().after(Timestamp.valueOf(LocalDateTime.now()))) {
                Lezione l = new Lezione();
                l.transformReservationIntoLesson(rr);
                lezioneList.add(l);
            }
        }
        return lezioneList;
    }

}
