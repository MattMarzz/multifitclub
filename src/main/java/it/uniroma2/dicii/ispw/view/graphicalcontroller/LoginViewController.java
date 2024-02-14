package it.uniroma2.dicii.ispw.view.graphicalcontroller;


import it.uniroma2.dicii.ispw.bean.LezioneBean;
import it.uniroma2.dicii.ispw.bean.LoginBean;
import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.controller.LoginController;
import it.uniroma2.dicii.ispw.controller.ProgrammazioneController;
import it.uniroma2.dicii.ispw.utils.LoggerManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Time;
import java.util.List;
import java.util.ResourceBundle;

public class LoginViewController implements Initializable {
    @FXML
    private TextField email;
    @FXML
    private Label errorMsg;
    @FXML
    private PasswordField password;
    @FXML
    private TableColumn<LezioneBean, String> courseCol;
    @FXML
    private TableColumn<LezioneBean, String> dayCol;
    @FXML
    private TableColumn<LezioneBean, Time> hourCol;
    @FXML
    private TableColumn<LezioneBean, String> istCol;
    @FXML
    private TableColumn<LezioneBean, String> roomCol;
    @FXML
    private TableView<LezioneBean> schedulingTable;


    @FXML
    private void onAccediBtnClick(ActionEvent event) {
        try {
            if (!email.getText().isBlank() && !password.getText().isBlank()) {
                LoginBean loginBean = new LoginBean(email.getText(), password.getText());
                LoginController loginController = new LoginController();
                UtenteBean utenteBean = loginController.login(loginBean);

                if (utenteBean != null) {
                    //navigate to new page
                    switch (utenteBean.getRuolo()) {
                        case SEGRETERIA -> PageHelper.changeScene(event, "views/segreteria/structure.fxml", "Dashboard", utenteBean);
                        case ISTRUTTORE -> PageHelper.changeScene(event, "views/utente/homepage.fxml", "Homepage", utenteBean);
                        case UTENTE -> handleError("utente work in progress...");
                    }
                } else {
                    handleError("Credenziali errate!");
                }
            } else handleError("E-mail e password necessarie!");
        } catch (Exception e) {
            handleError(e.getMessage());
            LoggerManager.logInfoException("Errore di autenticazione: " + e.getMessage(), e);
        }
    }

    private void handleError(String msg){
        this.errorMsg.setText(msg);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadAllLessons();
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