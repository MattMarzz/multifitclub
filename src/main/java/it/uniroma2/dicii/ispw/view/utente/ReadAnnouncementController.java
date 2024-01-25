package it.uniroma2.dicii.ispw.view.utente;

import it.uniroma2.dicii.ispw.bean.AnnouncementBean;
import it.uniroma2.dicii.ispw.controller.ManageAnnouncementController;
import it.uniroma2.dicii.ispw.model.announcement.Announcement;
import it.uniroma2.dicii.ispw.model.utente.Utente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class ReadAnnouncementController implements Initializable {

    @FXML
    private TableView<?> annListView;
    @FXML
    private TableColumn<?, ?> dateCol;
    @FXML
    private TableColumn<?, ?> senderCol;
    @FXML
    private TableColumn<?, ?> titleCol;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Announcement announcement = new Announcement();
        loadAnnunceTable();
    }

    private void loadAnnunceTable() {
        //retrieve announcement from user
        Utente ut =

//        ManageAnnouncementController manageAnnouncementController = new ManageAnnouncementController();
//        ObservableList<AnnouncementBean> announcementBeanObservableList = FXCollections.observableArrayList();
//        announcementBeanObservableList.addAll(manageAnnouncementController.getAllAnnouncement());

//        cfCol.setCellValueFactory(new PropertyValueFactory<>("cf"));
//        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
//        surnameCol.setCellValueFactory(new PropertyValueFactory<>("surname"));
//        dataCol.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
//        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
//        ruoloCol.setCellValueFactory(new PropertyValueFactory<>("ruolo"));
//
//        usersTable.setItems(utenteBeanObservableList);
//
//        //make rows selectable
//        usersTable.setRowFactory(tv -> {
//            TableRow<UtenteBean> row = new TableRow<>();
//            row.setOnMouseEntered(event -> row.setCursor(Cursor.HAND));
//            row.setOnMouseExited(event -> row.setCursor(Cursor.DEFAULT));
//
//            row.setOnMouseClicked(event -> {
//                UtenteBean selectedUtente = row.getItem();
//                if (selectedUtente != null) {
//                    editInfoForm(selectedUtente);
//                }
//            });
//
//            return row;
//        });
    }

}
