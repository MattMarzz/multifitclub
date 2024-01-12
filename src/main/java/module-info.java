module multifitclub {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens it.uniroma2.dicii.ispw.graphicsControllers to javafx.fxml;
    exports it.uniroma2.dicii.ispw;
    exports it.uniroma2.dicii.ispw.exceptions;
    exports it.uniroma2.dicii.ispw.beans;
    exports it.uniroma2.dicii.ispw.controllers;
    exports it.uniroma2.dicii.ispw.graphicsControllers;
    exports it.uniroma2.dicii.ispw.utente.dao;
    exports it.uniroma2.dicii.ispw.utente;
    exports it.uniroma2.dicii.ispw.enums;
}