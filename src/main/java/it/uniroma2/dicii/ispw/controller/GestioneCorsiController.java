package it.uniroma2.dicii.ispw.controller;

import it.uniroma2.dicii.ispw.App;
import it.uniroma2.dicii.ispw.bean.CorsoBean;
import it.uniroma2.dicii.ispw.bean.LezioneBean;
import it.uniroma2.dicii.ispw.enums.TypesOfPersistenceLayer;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.exception.ItemAlreadyExistsException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.model.corso.Corso;
import it.uniroma2.dicii.ispw.model.corso.dao.CorsoDAO;
import it.uniroma2.dicii.ispw.model.corso.dao.CorsoDBMS;
import it.uniroma2.dicii.ispw.model.lezione.Lezione;

import java.util.ArrayList;
import java.util.List;

public class GestioneCorsiController {
    private CorsoDAO corsoDAO;

    public GestioneCorsiController() {
        if(App.getPersistenceLayer().equals(TypesOfPersistenceLayer.JDBC)) {
            corsoDAO = new CorsoDBMS();
        }
//       else {
//            corsoDAO = new CorsoFS();
//            utenteCorsoDAO = new UtenteCorsoFS();
//        }
    }

    public List<CorsoBean> getAllCorsi() {
        List<CorsoBean> corsoBeanList = new ArrayList<>();
        for (Corso c: corsoDAO.getAllCorsi()) {
            CorsoBean cb = new CorsoBean(c.getName(), c.getStartDate());
            corsoBeanList.add(cb);
        }
        return corsoBeanList;
    }

    public void insertCourse(CorsoBean corsoBean) throws InvalidDataException, ItemAlreadyExistsException {
        Corso corso = new Corso();
        if (corsoBean.getName().isBlank())
            throw new InvalidDataException("Il nome non può essere vuoto.");
        if(corsoBean.getStartDate() == null)
            throw new InvalidDataException("La data non può essere vuota.");

        corso.setName(corsoBean.getName());
        corso.setStartDate(corsoBean.getStartDate());
        corsoDAO.insertCorso(corso);
    }

    public void removeCorso(CorsoBean corsoBean) throws InvalidDataException, ItemNotFoundException {
        if (corsoBean.getName().isBlank())
            throw new InvalidDataException("Il nome non può essere vuoto.");
        Corso corso = corsoDAO.getCorsoByNome(corsoBean.getName());
        if(corso == null) throw new ItemNotFoundException("Corso non trovato.");
        corsoDAO.removeCorso(corso);
    }

    public List<LezioneBean> getLezioniByCorsoId(CorsoBean corsoBean) throws InvalidDataException, ItemNotFoundException {
        if (corsoBean.getName().isBlank())
            throw new InvalidDataException("Il nome non può essere vuoto.");
        Corso corso = corsoDAO.getCorsoByNome(corsoBean.getName());
        if(corso == null) throw new ItemNotFoundException("Corso non trovato.");
        List<LezioneBean> lezioneBeanList = new ArrayList<>();
        for (Lezione l: corso.getLezioneList()) {
            LezioneBean lb = new LezioneBean();
            lb.setGiorno(l.getDay());
            lb.setOra(l.getStarTime().toString());
            lb.setSala(l.getSala());
            lezioneBeanList.add(lb);
        }
        return lezioneBeanList;
    }

}
