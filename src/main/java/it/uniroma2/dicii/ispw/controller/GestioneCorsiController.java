package it.uniroma2.dicii.ispw.controller;

import it.uniroma2.dicii.ispw.App;
import it.uniroma2.dicii.ispw.bean.CorsoBean;
import it.uniroma2.dicii.ispw.enums.TypesOfPersistenceLayer;
import it.uniroma2.dicii.ispw.model.corso.Corso;
import it.uniroma2.dicii.ispw.model.corso.dao.CorsoDAO;
import it.uniroma2.dicii.ispw.model.corso.dao.CorsoDBMS;
import it.uniroma2.dicii.ispw.model.corso.dao.UtenteCorsoDAO;
import it.uniroma2.dicii.ispw.model.corso.dao.UtenteCorsoDBMS;

import java.util.ArrayList;
import java.util.List;

public class GestioneCorsiController {

    private CorsoDAO corsoDAO;
    private UtenteCorsoDAO utenteCorsoDAO;

    public GestioneCorsiController() {
        if(App.getPersistenceLayer().equals(TypesOfPersistenceLayer.JDBC)) {
            corsoDAO = new CorsoDBMS();
            utenteCorsoDAO = new UtenteCorsoDBMS();
        }
//       else {
//            corsoDAO = new CorsoFS();
//            utenteCorsoDAO = new UtenteCorsoFS();
//        }
    }

    public List<CorsoBean> getAllCorsi() throws Exception {
        List<CorsoBean> corsoBeanList = new ArrayList<>();
        for (Corso c: corsoDAO.getAllCorsi()) {
            CorsoBean cb = new CorsoBean(c.getName(), c.getStartDate());
            corsoBeanList.add(cb);
        }
        return corsoBeanList;
    }

}
