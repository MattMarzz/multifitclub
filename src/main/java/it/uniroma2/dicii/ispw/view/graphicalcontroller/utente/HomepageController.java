package it.uniroma2.dicii.ispw.view.graphicalcontroller.utente;

import it.uniroma2.dicii.ispw.view.graphicalcontroller.AuthenticatedUser;
import it.uniroma2.dicii.ispw.view.graphicalcontroller.PageHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HomepageController extends AuthenticatedUser {


    @FXML
    private Label topNameLbl;

    @Override
    public void initUserData() {
        topNameLbl.setText(AuthenticatedUser.utenteBean.getName());
    }

    @FXML
    private static void onLogoutBtnClick(ActionEvent event) {
        AuthenticatedUser.utenteBean = null;
        PageHelper.logout(event);
    }


}
