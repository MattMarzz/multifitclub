package it.uniroma2.dicii.ispw.view.graphicalcontroller.utente;

import it.uniroma2.dicii.ispw.bean.AnnouncementBean;
import it.uniroma2.dicii.ispw.controller.ManageAnnouncementController;
import it.uniroma2.dicii.ispw.controller.Observer;
import it.uniroma2.dicii.ispw.model.AnnouncementManager;
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

public class ReadAnnouncementController implements Initializable, Observer {

    @FXML
    private TableView<AnnouncementBean> annListView;
    @FXML
    private TableColumn<AnnouncementBean, Timestamp> dateCol;
    @FXML
    private TableColumn<AnnouncementBean, String> senderCol;
    @FXML
    private TableColumn<AnnouncementBean, String> titleCol;
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
        AnnouncementManager.getInstance().attach(this);
        loadAnnunceTable();
    }

    @Override
    public void update() {
        loadAnnunceTable();
    }

    private void loadAnnunceTable() {
        ManageAnnouncementController manageAnnouncementController = new ManageAnnouncementController();
        ObservableList<AnnouncementBean> announcementBeanObservableList = FXCollections.observableArrayList();
        announcementBeanObservableList.addAll(manageAnnouncementController.getAllAnnouncementBean());

        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        senderCol.setCellValueFactory(new PropertyValueFactory<>("sender"));

        annListView.setItems(announcementBeanObservableList);

        //make rows selectable
        annListView.setRowFactory(tv -> {
            TableRow<AnnouncementBean> row = new TableRow<>();
            row.setOnMouseEntered(event -> row.setCursor(Cursor.HAND));
            row.setOnMouseExited(event -> row.setCursor(Cursor.DEFAULT));

            row.setOnMouseClicked(event -> {
                AnnouncementBean selectedAnnouncement = row.getItem();
                if (selectedAnnouncement != null) {
                   showAnnouncement(selectedAnnouncement);
                }
            });
            return row;
        });
    }

    public void showAnnouncement(AnnouncementBean announcementBean) {
        textIn.setText(announcementBean.getText());
        titleIn.setText(announcementBean.getTitle());
        dateIn.setValue(announcementBean.getDate().toLocalDateTime().toLocalDate());
        senderIn.setText(announcementBean.getSender());
    }

}