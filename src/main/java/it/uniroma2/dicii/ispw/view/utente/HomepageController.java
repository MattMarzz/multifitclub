package it.uniroma2.dicii.ispw.view.utente;

import it.uniroma2.dicii.ispw.view.AuthenticatedUser;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HomepageController extends AuthenticatedUser {

    @FXML
    private Label tryLbl;
    @Override
    public void initUserData() {
        tryLbl.setText(this.utenteBean.getName());
    }
}
