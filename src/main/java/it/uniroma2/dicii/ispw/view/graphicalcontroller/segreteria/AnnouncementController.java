package it.uniroma2.dicii.ispw.view.graphicalcontroller.segreteria;


import it.uniroma2.dicii.ispw.bean.AnnouncementBean;
import it.uniroma2.dicii.ispw.controller.ManageAnnouncementController;
import it.uniroma2.dicii.ispw.controller.Observer;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.model.AnnouncementManager;
import it.uniroma2.dicii.ispw.view.graphicalcontroller.AuthenticatedUser;
import it.uniroma2.dicii.ispw.view.graphicalcontroller.PageHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.Timestamp;
import java.time.LocalDateTime;


public class AnnouncementController implements Observer {

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
            titleLbl.setText(PageHelper.EMPTY_FIELDS);
            textLbl.setText(PageHelper.EMPTY_FIELDS);
        } else if(titleIn.getText().isBlank() && !textIn.getText().isBlank()) {
            titleLbl.setText(PageHelper.EMPTY_FIELDS);
        } else if (textIn.getText().isBlank() && !titleIn.getText().isBlank()) {
            textLbl.setText(PageHelper.EMPTY_FIELDS);
        } else if (!textIn.getText().isBlank() && !titleIn.getText().isBlank()){
            AnnouncementBean announcementBean = new AnnouncementBean();
            announcementBean.setTitle(titleIn.getText());
            announcementBean.setText(textIn.getText());
            announcementBean.setDate(Timestamp.valueOf(LocalDateTime.now()));
            announcementBean.setSender(AuthenticatedUser.getUtenteBean().getCf());
            ManageAnnouncementController manageAnnouncementController = new ManageAnnouncementController();
            AnnouncementManager.getInstance().attach(this);
            try {
                res = manageAnnouncementController.publishNewAnnouncement(announcementBean);
            } catch (InvalidDataException e) {
                AnnouncementManager.getInstance().detach(this);
                PageHelper.launchAlert(Alert.AlertType.ERROR, PageHelper.ERROR, e.getMessage());
            }
            PageHelper.launchAlert(Alert.AlertType.INFORMATION, PageHelper.SUCCESS, res);
            onClearBtnClick(new ActionEvent());
        }

    }

    @Override
    public void update() {
        System.out.println("bene ma non benissimo");
    }
}
