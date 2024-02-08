package it.uniroma2.dicii.ispw.controller;

import it.uniroma2.dicii.ispw.App;
import it.uniroma2.dicii.ispw.bean.LezioneBean;
import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.enums.Ruolo;
import it.uniroma2.dicii.ispw.enums.TypesOfPersistenceLayer;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.exception.ItemAlreadyExistsException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.model.corso.Corso;
import it.uniroma2.dicii.ispw.model.lezione.Lezione;
import it.uniroma2.dicii.ispw.model.lezione.dao.LezioneDAO;
import it.uniroma2.dicii.ispw.model.lezione.dao.LezioneDBMS;
import it.uniroma2.dicii.ispw.model.utente.Utente;
import it.uniroma2.dicii.ispw.model.utente.dao.UtenteDAO;
import it.uniroma2.dicii.ispw.model.utente.dao.UtenteDBMS;
import it.uniroma2.dicii.ispw.notification.Client;
import it.uniroma2.dicii.ispw.utils.LoginManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProgrammazioneController {

    private LezioneDAO lezioneDAO;
    private UtenteDAO utenteDAO;


    public ProgrammazioneController() {
        if(App.getPersistenceLayer().equals(TypesOfPersistenceLayer.JDBC)) {
            lezioneDAO = new LezioneDBMS();
            utenteDAO = new UtenteDBMS();
        }
//       else {
//            corsoDAO = new CorsoFS();
//            utenteCorsoDAO = new UtenteCorsoFS();
//        }
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

}
