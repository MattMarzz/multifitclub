package it.uniroma2.dicii.ispw.controllers;


import it.uniroma2.dicii.ispw.beans.UtenteBean;
import it.uniroma2.dicii.ispw.utente.Utente;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GestioneUtentiController {

    public void addIscritto(UtenteBean utenteBean){
        //TODO: check data input
        int CF_LENGTH = 16;
        if(utenteBean.getCf().length() != CF_LENGTH){
            System.out.println("Codice Fiscale not valid!");
            return;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date  = sdf.parse(utenteBean.getBirthDate());
            Utente utente = new Utente(utenteBean.getName(), utenteBean.getSurname(), utenteBean.getCf(), date);
            utente.storeIscritto();
        } catch (ParseException e) {
            System.out.println("Date is invalid!");
            throw new RuntimeException(e);
        }
    }

    public void getAllIscritti(){
        Utente utente = new Utente();
        List<Utente> utenteList = utente.getAllIscritti();
        //TODO: here we have the list we need but probaby we need to covert it in a bean list to put it back on the view
    }

    public void deleteIscritto(/*bean con i dati dell'iscritto*/){
        //check data input
        //call the model to store new iscritto
        //eventually message the view the state of the operation
    }

    public void editIscritto(/*bean con i dati dell'iscritto*/){
        //check data input
        //call the model to store new iscritto
        //eventually message the view the state of the operation
    }


}
