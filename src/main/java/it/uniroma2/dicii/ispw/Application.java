package it.uniroma2.dicii.ispw;

import it.uniroma2.dicii.ispw.beans.UtenteBean;
import it.uniroma2.dicii.ispw.controllers.GestioneUtentiController;

import java.io.BufferedReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Application {
    public static void main(String[] args) {

        //example: add new user
        GestioneUtentiController gestioneUtentiController = new GestioneUtentiController();
        UtenteBean utente = new UtenteBean();
        utente.setName("Matteo");
        utente.setSurname("Bianchi");
        utente.setCf("BNCMRA70A20H501B");
        utente.setBirthDate("20/01/1970");
        gestioneUtentiController.insertUtente(utente);
    }
}