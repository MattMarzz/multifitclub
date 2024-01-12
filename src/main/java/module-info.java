module multifitclub {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens it.uniroma2.dicii.ispw.graphics_controllers to javafx.fxml;
    exports it.uniroma2.dicii.ispw;
    exports it.uniroma2.dicii.ispw.exceptions;
    exports it.uniroma2.dicii.ispw.beans;
    exports it.uniroma2.dicii.ispw.controllers;
    exports it.uniroma2.dicii.ispw.graphics_controllers;
    exports it.uniroma2.dicii.ispw.models.utente.dao;
    exports it.uniroma2.dicii.ispw.models.utente;
    exports it.uniroma2.dicii.ispw.models.lezione;
    exports it.uniroma2.dicii.ispw.models.corso;
    exports it.uniroma2.dicii.ispw.enums;
    exports it.uniroma2.dicii.ispw.utils;
}