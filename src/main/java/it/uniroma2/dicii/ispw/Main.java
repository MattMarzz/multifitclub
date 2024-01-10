package it.uniroma2.dicii.ispw;

import it.uniroma2.dicii.ispw.controllers.GestioneIscrittiController;

public class Main {
    public static void main(String[] args) {

        //example: add new subscriber
        GestioneIscrittiController gestioneIscrittiController = new GestioneIscrittiController();
        //IscrittoBean iscritto = new IscrittoBean();
        //iscritto.setName("Mattep");
        //iscritto.setSurname("Bianchi");
        //iscritto.setCf("BNCMRA70A20H501B");
        //iscritto.setBirthDate("20/01/1970");
        //gestioneIscrittiController.addIscritto(iscritto);
        gestioneIscrittiController.getAllIscritti();

    }
}