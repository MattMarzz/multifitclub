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
    exports it.uniroma2.dicii.ispw.view.graphicalcontroller;
    exports it.uniroma2.dicii.ispw.model.utente.dao;
    exports it.uniroma2.dicii.ispw.model.utente;
    exports it.uniroma2.dicii.ispw.model.lezione;
    exports it.uniroma2.dicii.ispw.model.corso;
    exports it.uniroma2.dicii.ispw.model.announcement;
    exports it.uniroma2.dicii.ispw.model.announcement.dao;
    exports it.uniroma2.dicii.ispw.enums;
    exports it.uniroma2.dicii.ispw.utils;
    exports it.uniroma2.dicii.ispw.view.graphicalcontroller.segreteria;

    opens it.uniroma2.dicii.ispw.view.graphicalcontroller to javafx.fxml;
    opens it.uniroma2.dicii.ispw.view.graphicalcontroller.segreteria to javafx.fxml;
    opens it.uniroma2.dicii.ispw.view.graphicalcontroller.utente to javafx.fxml;
}