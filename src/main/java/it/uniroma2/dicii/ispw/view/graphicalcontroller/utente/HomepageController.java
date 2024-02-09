package it.uniroma2.dicii.ispw.view.graphicalcontroller.utente;


import it.uniroma2.dicii.ispw.bean.LezioneBean;
import it.uniroma2.dicii.ispw.controller.LoginController;
import it.uniroma2.dicii.ispw.controller.ProgrammazioneController;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.utils.LoggerManager;
import it.uniroma2.dicii.ispw.view.graphicalcontroller.AuthenticatedUser;
import it.uniroma2.dicii.ispw.view.graphicalcontroller.PageHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HomepageController extends AuthenticatedUser {

    @FXML
    private Label topNameLbl;
    @FXML
    private TextField cfIn;
    @FXML
    private TableColumn<LezioneBean, String> courseCol;
    @FXML
    private DatePicker dateIn;
    @FXML
    private TableColumn<LezioneBean, String> dayCol;
    @FXML
    private TextField emailIn;
    @FXML
    private Label formNameLbl;
    @FXML
    private TableColumn<LezioneBean, Time> hourCol;
    @FXML
    private TextField nameIn;
    @FXML
    private PasswordField pwdIn;
    @FXML
    private TextField roleIn;
    @FXML
    private TableColumn<LezioneBean, String> roomCol;
    @FXML
    private TableView<LezioneBean> schedulingTable;
    @FXML
    private TextField surnameIn;
    @FXML
    private TableColumn<LezioneBean, String> urCourseCol;
    @FXML
    private TableColumn<LezioneBean, String> urDayCol;
    @FXML
    private TableColumn<LezioneBean, Time> urHourCol;
    @FXML
    private TableColumn<LezioneBean, String> urRoomCol;
    @FXML
    private TableColumn<LezioneBean, String> istCol;
    @FXML
    private TableView<LezioneBean> urTable;

    @Override
    public void initUserData() {
        topNameLbl.setText(AuthenticatedUser.utenteBean.getName());

        new LoginController().attachObserver(AuthenticatedUser.getUtenteBean(), this);

        setUserInfo();
        loadAllLessons();
        loadYourLessons();
    }

    private void setUserInfo() {
        formNameLbl.setText(AuthenticatedUser.getUtenteBean().getName());

        nameIn.setText(AuthenticatedUser.utenteBean.getName());
        surnameIn.setText(AuthenticatedUser.utenteBean.getSurname());
        cfIn.setText(AuthenticatedUser.utenteBean.getCf());
        emailIn.setText(AuthenticatedUser.utenteBean.getEmail());
        pwdIn.setText(AuthenticatedUser.utenteBean.getPassword());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dateIn.setValue(LocalDate.parse(AuthenticatedUser.utenteBean.getBirthDate(), formatter));

        roleIn.setText(AuthenticatedUser.getUtenteBean().getRuolo().name());
    }

    @FXML
    private void onLogoutBtnClick(ActionEvent event) {
        LoginController loginController = new LoginController();
        loginController.detachObserver(AuthenticatedUser.getUtenteBean(), this);
        loginController.logout(AuthenticatedUser.getUtenteBean());

        AuthenticatedUser.setUtenteBean(null);
        PageHelper.logout(event);
    }


    @Override
    public void update(String... msg) {
        Platform.runLater(() -> {
            if(msg.length != 0) {
                PageHelper.launchAlert(Alert.AlertType.INFORMATION, "Notifica", msg[0]);

                //load lessons
                loadAllLessons();
                loadYourLessons();
            }
        });
    }

    public void loadYourLessons() {
        try {
            List<LezioneBean> lezioneBeanList = new ProgrammazioneController().getLezioniByUtente(AuthenticatedUser.getUtenteBean().getCf());
            ObservableList<LezioneBean> lezioneBeanObservableList = FXCollections.observableArrayList();
            lezioneBeanObservableList.addAll(lezioneBeanList);

            urCourseCol.setCellValueFactory(new PropertyValueFactory<>("corso"));
            urDayCol.setCellValueFactory(new PropertyValueFactory<>("giorno"));
            urHourCol.setCellValueFactory(new PropertyValueFactory<>("ora"));
            urRoomCol.setCellValueFactory(new PropertyValueFactory<>("sala"));

            urTable.setItems(lezioneBeanObservableList);
        } catch (ItemNotFoundException e) {
            LoggerManager.logSevereException(e.getMessage(), e);
        }
    }

    public void loadAllLessons() {
        List<LezioneBean> lezioneBeanList = new ProgrammazioneController().getAllLezioni();
        ObservableList<LezioneBean> lezioneBeanObservableList = FXCollections.observableArrayList();
        lezioneBeanObservableList.addAll(lezioneBeanList);

        courseCol.setCellValueFactory(new PropertyValueFactory<>("corso"));
        dayCol.setCellValueFactory(new PropertyValueFactory<>("giorno"));
        hourCol.setCellValueFactory(new PropertyValueFactory<>("ora"));
        istCol.setCellValueFactory(new PropertyValueFactory<>("cf"));
        roomCol.setCellValueFactory(new PropertyValueFactory<>("sala"));

        schedulingTable.setItems(lezioneBeanObservableList);
    }
}
