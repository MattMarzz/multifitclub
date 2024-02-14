package it.uniroma2.dicii.ispw.view.graphicalcontroller.segreteria;

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
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class ReservationController implements Initializable {
    @FXML
    private TableColumn<CommunicationBean, Timestamp> dateCol;
    @FXML
    private DatePicker dateIn;
    @FXML
    private TableView<CommunicationBean> resListView;
    @FXML
    private TextField roomIn;
    @FXML
    private TableColumn<CommunicationBean, String> senderCol;
    @FXML
    private TextField senderIn;
    @FXML
    private TableColumn<CommunicationBean, String> stateCol;
    @FXML
    private TextArea textIn;
    @FXML
    private TableColumn<CommunicationBean, String> titleCol;
    @FXML
    private TextField titleIn;
    @FXML
    private DatePicker whenIn;
    @FXML
    private Button acceptBtn;
    @FXML
    private Button rejectBtn;

    private int id;


    @FXML
    void onAcceptBtnClick(ActionEvent event) {
        if(!titleIn.getText().isBlank()) {
            CommunicationBean cb = new CommunicationBean();
            cb.setId(id);
            cb.setStatus(RoomRequestStatus.ACCEPTED);
            CommunicationController communicationController = new CommunicationController();
            try {
                communicationController.forwardCommunication(AuthenticatedUser.getUtenteBean(), cb, TypesOfCommunications.ROOM_REQUEST);
            } catch (ItemNotFoundException | InvalidDataException e) {
                PageHelper.launchAlert(null, Alert.AlertType.ERROR, PageHelper.ERROR, e.getMessage());
            }

            loadReservationRequest();
        }
    }

    @FXML
    void onRefreshBtnClick(ActionEvent event) {
        loadReservationRequest();
    }

    @FXML
    void onRejectBtnClick(ActionEvent event) {
        if(!titleIn.getText().isBlank()) {
            CommunicationBean cb = new CommunicationBean();
            cb.setId(id);
            cb.setStatus(RoomRequestStatus.REJECTED);
            CommunicationController communicationController = new CommunicationController();
            try {
                communicationController.forwardCommunication(AuthenticatedUser.getUtenteBean(), cb, TypesOfCommunications.ROOM_REQUEST);
            } catch (ItemNotFoundException | InvalidDataException e) {
                PageHelper.launchAlert(null, Alert.AlertType.ERROR, PageHelper.ERROR, e.getMessage());
            }

            loadReservationRequest();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       loadReservationRequest();
    }

    private void loadReservationRequest() {
        CommunicationController communicationController = new CommunicationController();
        ObservableList<CommunicationBean> communicationBeanObservableList = FXCollections.observableArrayList();
        communicationBeanObservableList.addAll(communicationController.getAllRoomRequestBean());

        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        senderCol.setCellValueFactory(new PropertyValueFactory<>("sender"));
        stateCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        resListView.setItems(communicationBeanObservableList);

        //make rows selectable
        resListView.setRowFactory(tv -> {
            TableRow<CommunicationBean> row = new TableRow<>();
            row.setOnMouseEntered(event -> row.setCursor(Cursor.HAND));
            row.setOnMouseExited(event -> row.setCursor(Cursor.DEFAULT));

            row.setOnMouseClicked(event -> {
                CommunicationBean selectedAnnouncement = row.getItem();
                if (selectedAnnouncement != null) {
                    showReservationReq(selectedAnnouncement);
                }
            });
            return row;
        });
    }

    private void showReservationReq(CommunicationBean cb) {
        this.id = cb.getId();
        textIn.setText(cb.getText());
        titleIn.setText(cb.getTitle());
        dateIn.setValue(cb.getDate().toLocalDateTime().toLocalDate());
        senderIn.setText(cb.getSender());
        roomIn.setText(cb.getRoom());
        whenIn.setValue(cb.getWhen().toLocalDateTime().toLocalDate());

        if(!cb.getStatus().equals(RoomRequestStatus.PENDING)) {
            acceptBtn.setDisable(true);
            rejectBtn.setDisable(true);
        } else {
            acceptBtn.setDisable(false);
            rejectBtn.setDisable(false);
        }
    }
}
