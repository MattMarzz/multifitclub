package it.uniroma2.dicii.ispw.view.graphicalcontroller.segreteria;

import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.controller.GestioneUtentiController;
import it.uniroma2.dicii.ispw.enums.Ruolo;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.view.graphicalcontroller.PageHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AddUserController implements Initializable {
    @FXML
    private Button addBtn;
    @FXML
    private AnchorPane addUserView;
    @FXML
    private TextField cfIn;
    @FXML
    private DatePicker dateIn;
    @FXML
    private TextField emailIn;
    @FXML
    private TextField nameIn;
    @FXML
    private TextField pwdIn;
    @FXML
    private ComboBox<Ruolo> roleIn;
    @FXML
    private TextField surnameIn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Ruolo[] ruoli = Ruolo.values();
        roleIn.getItems().addAll(ruoli);
        //set default pwd
        pwdIn.setText("dafult1");
    }

    @FXML
    void onAddBtnClick(ActionEvent event) {
        UtenteBean utenteBean = new UtenteBean();
        String res = "Ops... Qualcosa è andato storto!";
        if (cfIn.getText().isBlank() || nameIn.getText().isBlank() || surnameIn.getText().isBlank() || dateIn.getValue() == null ||
            emailIn.getText().isBlank() || roleIn.getValue() == null) {
            PageHelper.launchAlert(Alert.AlertType.ERROR, "Errore", "Riempire tutti i campi.");
        } else {
            utenteBean.setCf(cfIn.getText());
            utenteBean.setName(nameIn.getText());
            utenteBean.setSurname(surnameIn.getText());
            utenteBean.setEmail(emailIn.getText());
            utenteBean.setPassword(pwdIn.getText());
            utenteBean.setRuolo(roleIn.getValue());
            utenteBean.setBirthDate(dateIn.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            GestioneUtentiController gestioneUtentiController = new GestioneUtentiController();
            try {
                res = gestioneUtentiController.insertUtente(utenteBean);
            } catch (InvalidDataException e) {
                PageHelper.launchAlert(Alert.AlertType.ERROR, "Errore", e.getMessage());
            }
        }
        PageHelper.launchAlert(Alert.AlertType.INFORMATION, "Info", res);
        cleanFields();
    }

    @FXML
    private void onClearBtnClick() {
        cleanFields();
    }

    private void cleanFields() {
        cfIn.setText("");
        nameIn.setText("");
        surnameIn.setText("");
        emailIn.setText("");
        roleIn.getSelectionModel().clearSelection();
        dateIn.setValue(null);
    }

}
