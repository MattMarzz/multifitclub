package it.uniroma2.dicii.ispw;

import it.uniroma2.dicii.ispw.bean.IscrittoBean;
import it.uniroma2.dicii.ispw.controller.GestioneIscrittiController;
import it.uniroma2.dicii.ispw.iscritto.Iscritto;

public class Main {
    public static void main(String[] args) {

        //example: add new subscriber
        GestioneIscrittiController gestioneIscrittiController = new GestioneIscrittiController();
        //IscrittoBean iscritto = new IscrittoBean();
        //iscritto.setName("Mario");
        //iscritto.setSurname("Rossi");
        //iscritto.setCf("BNCMRA70A20H501B");
        //iscritto.setBirthDate("20/01/1970");
        //gestioneIscrittiController.addIscritto(iscritto);
        gestioneIscrittiController.getAllIscritti();

    }
}