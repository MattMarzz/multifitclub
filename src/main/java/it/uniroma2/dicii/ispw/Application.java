package it.uniroma2.dicii.ispw;

import java.sql.Connection;

public class Application {
    public static void main(String[] args) {

        //example: add new subscriber
        //GestioneUtentiController gestioneUtentiController = new GestioneUtentiController();
        //IscrittoBean iscritto = new IscrittoBean();
        //iscritto.setName("Mattep");
        //iscritto.setSurname("Bianchi");
        //iscritto.setCf("BNCMRA70A20H501B");
        //iscritto.setBirthDate("20/01/1970");
        //gestioneIscrittiController.addIscritto(iscritto);
        //gestioneUtentiController.getAllIscritti();

        DbConnection dbConnection = DbConnection.getDbConnectionInstance();
        Connection conn = dbConnection.getConn();
    }
}