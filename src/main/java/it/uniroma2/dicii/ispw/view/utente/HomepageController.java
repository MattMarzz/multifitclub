package it.uniroma2.dicii.ispw.view.utente;

import it.uniroma2.dicii.ispw.view.AuthenticatedUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class HomepageController extends AuthenticatedUser {


    @FXML
    private Label topNameLbl;

    @Override
    public void initUserData() {
        topNameLbl.setText(AuthenticatedUser.utenteBean.getName());
    }

    @FXML
    void onLogoutBtnClick(ActionEvent event) {
    }


}
