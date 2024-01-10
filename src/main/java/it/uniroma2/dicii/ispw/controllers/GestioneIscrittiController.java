package it.uniroma2.dicii.ispw.controllers;


import it.uniroma2.dicii.ispw.beans.IscrittoBean;
import it.uniroma2.dicii.ispw.iscritto.Iscritto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GestioneIscrittiController {

    public void addIscritto(IscrittoBean iscrittoBean){
        //TODO: check data input
        int CF_LENGTH = 16;
        if(iscrittoBean.getCf().length() != CF_LENGTH){
            System.out.println("Codice Fiscale not valid!");
            return;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date  = sdf.parse(iscrittoBean.getBirthDate());
            Iscritto iscritto = new Iscritto(iscrittoBean.getName(), iscrittoBean.getSurname(), iscrittoBean.getCf(), date);
            iscritto.storeIscritto();
        } catch (ParseException e) {
            System.out.println("Date is invalid!");
            throw new RuntimeException(e);
        }
    }

    public void getAllIscritti(){
        Iscritto iscritto = new Iscritto();
        List<Iscritto> iscrittoList = iscritto.getAllIscritti();
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
