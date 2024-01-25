package it.uniroma2.dicii.ispw.view.segreteria;


import it.uniroma2.dicii.ispw.bean.AnnouncementBean;
import it.uniroma2.dicii.ispw.controller.ManageAnnouncementController;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.view.AuthenticatedUser;
import it.uniroma2.dicii.ispw.view.PageHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.Timestamp;
import java.time.LocalDateTime;


public class AnnouncementController {

    @FXML
    private TextArea textIn;
    @FXML
    private TextField titleIn;
    @FXML
    private Label textLbl;
    @FXML
    private Label titleLbl;

    @FXML
    void onClearBtnClick(ActionEvent event) {
        textIn.setText("");
        titleIn.setText("");
        textLbl.setText("");
        titleLbl.setText("");
    }

    @FXML
    void onSendBtnClick(ActionEvent event) {
        String res  = "";
        if(titleIn.getText().isBlank() && textIn.getText().isBlank()) {
            titleLbl.setText("Il campo non può essere vuoto.");
            textLbl.setText("Il campo non può essere vuoto.");
        } else if(titleIn.getText().isBlank() && !textIn.getText().isBlank()) {
            titleLbl.setText("Il campo non può essere vuoto.");
        } else if (textIn.getText().isBlank() && !titleIn.getText().isBlank()) {
            textLbl.setText("Il campo non può essere vuoto.");
        } else if (!textIn.getText().isBlank() && !titleIn.getText().isBlank()){
            AnnouncementBean announcementBean = new AnnouncementBean();
            announcementBean.setTitle(titleIn.getText());
            announcementBean.setText(textIn.getText());
            announcementBean.setDate(Timestamp.valueOf(LocalDateTime.now()));
            announcementBean.setSender(AuthenticatedUser.getUtenteBean().getCf());
            ManageAnnouncementController manageAnnouncementController = new ManageAnnouncementController();
            try {
                res = manageAnnouncementController.publishNewAnnouncement(announcementBean);
            } catch (InvalidDataException e) {
                PageHelper.launchAlert(Alert.AlertType.ERROR, "Errore", e.getMessage());
            }
            PageHelper.launchAlert(Alert.AlertType.INFORMATION, "Successo", res);
            onClearBtnClick(new ActionEvent());
        }

    }

}
