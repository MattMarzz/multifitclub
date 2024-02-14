package it.uniroma2.dicii.ispw.view.graphicalcontroller.utente;

import it.uniroma2.dicii.ispw.bean.CommunicationBean;
import it.uniroma2.dicii.ispw.controller.CommunicationController;
import it.uniroma2.dicii.ispw.enums.RoomRequestStatus;
import it.uniroma2.dicii.ispw.enums.TypesOfCommunications;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.view.graphicalcontroller.AuthenticatedUser;
import it.uniroma2.dicii.ispw.view.graphicalcontroller.PageHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;


import java.sql.Timestamp;
import java.time.LocalDateTime;

public class RoomRequestController {

    @FXML
    private TableColumn<CommunicationBean, String> dateCol;
    @FXML
    private TableColumn<CommunicationBean, String> dayCol;
    @FXML
    private Label dayLbl;
    @FXML
    private TableView<CommunicationBean> requestTable;
    @FXML
    private TableColumn<CommunicationBean, String> roomCol;
    @FXML
    private Label roomLbl;
    @FXML
    private TableColumn<CommunicationBean, String> statusCol;
    @FXML
    private TextArea textIn;
    @FXML
    private Label textLbl;
    @FXML
    private TableColumn<CommunicationBean, String> titleCol;
    @FXML
    private TextField titleIn;
    @FXML
    private TextField roomIn;
    @FXML
    private DatePicker whenIn;
    @FXML
    private Label titleLbl;
    @FXML
    private AnchorPane tableView;

    @FXML
    void onToggleBtnClick(ActionEvent event) {
        tableView.setVisible(!tableView.isVisible());
        loadTable();
    }

    @FXML
    void onClearBtnClick(ActionEvent event) {
        textIn.setText("");
        titleIn.setText("");
        roomIn.setText("");
        whenIn.setValue(null);
        textLbl.setText("");
        titleLbl.setText("");
        roomLbl.setText("");
        dayLbl.setText("");
    }

    @FXML
    void onSendBtnClick(ActionEvent event) {
        if(titleIn.getText().isBlank())
            titleLbl.setText(PageHelper.EMPTY_FIELDS);
        else if(textIn.getText().isBlank())
            textLbl.setText(PageHelper.EMPTY_FIELDS);
        else if(roomIn.getText().isBlank())
            dayLbl.setText(PageHelper.EMPTY_FIELDS);
        else if(whenIn.getValue() == null)
            dayLbl.setText(PageHelper.EMPTY_FIELDS);
        else {
            CommunicationBean cb = new CommunicationBean();
            cb.setTitle(titleIn.getText());
            cb.setText(textIn.getText());
            cb.setDate(Timestamp.valueOf(LocalDateTime.now()));
            cb.setSender(AuthenticatedUser.getUtenteBean().getCf());
            cb.setRoom(roomIn.getText());
            cb.setStatus(RoomRequestStatus.PENDING);
            cb.setWhen(Timestamp.valueOf(whenIn.getValue().atStartOfDay()));
            CommunicationController communicationController = new CommunicationController();

            try {
                communicationController.forwardCommunication(AuthenticatedUser.getUtenteBean(), cb, TypesOfCommunications.ROOM_REQUEST);
            } catch (ItemNotFoundException | InvalidDataException e) {
                PageHelper.launchAlert(null, Alert.AlertType.ERROR, PageHelper.ERROR, e.getMessage());
            }

            loadTable();
            onClearBtnClick(new ActionEvent());
        }

    }

    public void loadTable() {
        CommunicationController communicationController = new CommunicationController();
        ObservableList<CommunicationBean> communicationBeanObservableList = FXCollections.observableArrayList();
        communicationBeanObservableList.addAll(communicationController.getRoomRequestByUtente(AuthenticatedUser.getUtenteBean().getCf()));

        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        roomCol.setCellValueFactory(new PropertyValueFactory<>("room"));
        dayCol.setCellValueFactory(new PropertyValueFactory<>("when"));

        requestTable.setItems(communicationBeanObservableList);
    }

    @FXML
    void onRefreshBtnClick(ActionEvent event) {
        loadTable();
    }
}
