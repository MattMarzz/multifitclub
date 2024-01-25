package it.uniroma2.dicii.ispw.view.graphicalcontroller.segreteria;


import it.uniroma2.dicii.ispw.bean.AnnouncementBean;
import it.uniroma2.dicii.ispw.controller.ManageAnnouncementController;
import it.uniroma2.dicii.ispw.controller.Observer;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.model.AnnouncementManager;
import it.uniroma2.dicii.ispw.view.graphicalcontroller.AuthenticatedUser;
import it.uniroma2.dicii.ispw.view.graphicalcontroller.PageHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;


public class AnnouncementController implements Observer, Initializable {

    @FXML
    private TextArea textIn;
    @FXML
    private TextField titleIn;
    @FXML
    private Label textLbl;
    @FXML
    private Label titleLbl;
    @FXML
    private TableView<AnnouncementBean> announceTable;
    @FXML
    private TableColumn<AnnouncementBean, Timestamp> dateCol;
    @FXML
    private TableColumn<AnnouncementBean, String> textCol;
    @FXML
    private TableColumn<AnnouncementBean, String> titleCol;


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
        loadTable();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
    }

    private void loadTable() {
        ManageAnnouncementController manageAnnouncementController = new ManageAnnouncementController();
        ObservableList<AnnouncementBean> announcementBeanObservableList = FXCollections.observableArrayList();
        announcementBeanObservableList.addAll(manageAnnouncementController.getAllAnnouncementBean());

        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        textCol.setCellValueFactory(new PropertyValueFactory<>("text"));

        announceTable.setItems(announcementBeanObservableList);
    }
}
