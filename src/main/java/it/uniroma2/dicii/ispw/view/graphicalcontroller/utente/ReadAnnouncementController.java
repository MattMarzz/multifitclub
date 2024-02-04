package it.uniroma2.dicii.ispw.view.graphicalcontroller.utente;

import it.uniroma2.dicii.ispw.bean.CommunicationBean;
import it.uniroma2.dicii.ispw.controller.CommunicationController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;

public class ReadAnnouncementController implements Initializable {

    @FXML
    private TableView<CommunicationBean> annListView;
    @FXML
    private TableColumn<CommunicationBean, Timestamp> dateCol;
    @FXML
    private TableColumn<CommunicationBean, String> senderCol;
    @FXML
    private TableColumn<CommunicationBean, String> titleCol;
    @FXML
    private DatePicker dateIn;
    @FXML
    private TextField senderIn;
    @FXML
    private TextArea textIn;
    @FXML
    private TextField titleIn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadAnnunceTable();
    }

    private void loadAnnunceTable() {
        CommunicationController communicationController = new CommunicationController();
        ObservableList<CommunicationBean> communicationBeanObservableList = FXCollections.observableArrayList();
        communicationBeanObservableList.addAll(communicationController.getAllAnnouncementBean());

        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        senderCol.setCellValueFactory(new PropertyValueFactory<>("sender"));

        annListView.setItems(communicationBeanObservableList);

        //make rows selectable
        annListView.setRowFactory(tv -> {
            TableRow<CommunicationBean> row = new TableRow<>();
            row.setOnMouseEntered(event -> row.setCursor(Cursor.HAND));
            row.setOnMouseExited(event -> row.setCursor(Cursor.DEFAULT));

            row.setOnMouseClicked(event -> {
                CommunicationBean selectedAnnouncement = row.getItem();
                if (selectedAnnouncement != null) {
                   showAnnouncement(selectedAnnouncement);
                }
            });
            return row;
        });
    }

    @FXML
    private void onRefreshBtnClick() {
        loadAnnunceTable();
    }

    public void showAnnouncement(CommunicationBean cb) {
        textIn.setText(cb.getText());
        titleIn.setText(cb.getTitle());
        dateIn.setValue(cb.getDate().toLocalDateTime().toLocalDate());
        senderIn.setText(cb.getSender());
    }

}