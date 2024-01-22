module multifitclub {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires atlantafx.base;
    requires de.jensd.fx.glyphs.fontawesome;



    exports it.uniroma2.dicii.ispw;
    exports it.uniroma2.dicii.ispw.exception;
    exports it.uniroma2.dicii.ispw.bean;
    exports it.uniroma2.dicii.ispw.controller;
    exports it.uniroma2.dicii.ispw.view;
    exports it.uniroma2.dicii.ispw.model.utente.dao;
    exports it.uniroma2.dicii.ispw.model.utente;
    exports it.uniroma2.dicii.ispw.model.lezione;
    exports it.uniroma2.dicii.ispw.model.corso;
    exports it.uniroma2.dicii.ispw.enums;
    exports it.uniroma2.dicii.ispw.utils;
    exports it.uniroma2.dicii.ispw.view.segreteria;

    opens it.uniroma2.dicii.ispw.view to javafx.fxml;
    opens it.uniroma2.dicii.ispw.view.segreteria to javafx.fxml;
    opens it.uniroma2.dicii.ispw.view.utente to javafx.fxml;
}