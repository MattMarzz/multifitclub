package it.uniroma2.dicii.ispw.controllers;


import it.uniroma2.dicii.ispw.beans.UtenteBean;
import it.uniroma2.dicii.ispw.utente.Utente;
import it.uniroma2.dicii.ispw.utente.dao.UtenteDAO;
import it.uniroma2.dicii.ispw.utente.dao.UtenteDBMS;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GestioneUtentiController {

    public void insertUtente(UtenteBean utenteBean){
        try {
            int CF_LENGTH = 16;

            if(utenteBean.getCf().length() != CF_LENGTH){
                System.out.println("Codice Fiscale not valid!");
                return;
            }


            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date  = sdf.parse(utenteBean.getBirthDate());
            Utente utente = new Utente(utenteBean.getName(), utenteBean.getSurname(), utenteBean.getCf(), date);
            UtenteDAO utenteDAO = new UtenteDBMS();
            utenteDAO.insertUtente(utente);

        } catch (ParseException e) {
            System.out.println("Date is invalid!");
            throw new RuntimeException(e);
        }
    }
}
