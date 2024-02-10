package it.uniroma2.dicii.ispw.view.graphicalcontroller.segreteria;

import it.uniroma2.dicii.ispw.bean.CommunicationBean;
import it.uniroma2.dicii.ispw.controller.CommunicationController;
import it.uniroma2.dicii.ispw.enums.TypesOfCommunications;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.view.graphicalcontroller.AuthenticatedUser;
import it.uniroma2.dicii.ispw.view.graphicalcontroller.PageHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ResourceBundle;


public class AnnouncementController implements Initializable {

    @FXML
    private TextArea textIn;
    @FXML
    private TextField titleIn;
    @FXML
    private Label textLbl;
    @FXML
    private Label titleLbl;
    @FXML
    private TableView<CommunicationBean> announceTable;
    @FXML
    private TableColumn<CommunicationBean, Timestamp> dateCol;
    @FXML
    private TableColumn<CommunicationBean, String> textCol;
    @FXML
    private TableColumn<CommunicationBean, String> titleCol;

    @FXML
    void onClearBtnClick(ActionEvent event) {
        textIn.setText("");
        titleIn.setText("");
        textLbl.setText("");
        titleLbl.setText("");
    }

    @FXML
    void onSendBtnClick(ActionEvent event) {
        if(titleIn.getText().isBlank() && textIn.getText().isBlank()) {
            titleLbl.setText(PageHelper.EMPTY_FIELDS);
            textLbl.setText(PageHelper.EMPTY_FIELDS);
        } else if(titleIn.getText().isBlank() && !textIn.getText().isBlank()) {
            titleLbl.setText(PageHelper.EMPTY_FIELDS);
        } else if (textIn.getText().isBlank() && !titleIn.getText().isBlank()) {
            textLbl.setText(PageHelper.EMPTY_FIELDS);
        } else if (!textIn.getText().isBlank() && !titleIn.getText().isBlank()){
            CommunicationBean cb = new CommunicationBean();
            cb.setTitle(titleIn.getText());
            cb.setText(textIn.getText());
            cb.setDate(Timestamp.valueOf(LocalDateTime.now()));
            cb.setSender(AuthenticatedUser.getUtenteBean().getCf());
            CommunicationController communicationController = new CommunicationController();

            try {
                communicationController.forwardCommunication(AuthenticatedUser.getUtenteBean(), cb, TypesOfCommunications.ANNOUNCEMENT);
            } catch (ItemNotFoundException | InvalidDataException e) {
                PageHelper.launchAlert(Alert.AlertType.ERROR, PageHelper.ERROR, e.getMessage());
            }

            loadTable();
            onClearBtnClick(new ActionEvent());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

//        loadTable();
    }

    private void loadTable() {
        CommunicationController communicationController = new CommunicationController();
        ObservableList<CommunicationBean> announcementBeanObservableList = FXCollections.observableArrayList();
        announcementBeanObservableList.addAll(communicationController.getAllAnnouncementBean());

        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        textCol.setCellValueFactory(new PropertyValueFactory<>("text"));

        announceTable.setItems(announcementBeanObservableList);
    }

}
