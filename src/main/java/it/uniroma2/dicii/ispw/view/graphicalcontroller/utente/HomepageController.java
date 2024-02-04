package it.uniroma2.dicii.ispw.view.graphicalcontroller.utente;

import it.uniroma2.dicii.ispw.controller.GestioneUtentiController;
import it.uniroma2.dicii.ispw.controller.LoginController;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.notification.Client;
import it.uniroma2.dicii.ispw.model.utente.Utente;
import it.uniroma2.dicii.ispw.utils.LoggerManager;
import it.uniroma2.dicii.ispw.utils.LoginManager;
import it.uniroma2.dicii.ispw.utils.Observer;
import it.uniroma2.dicii.ispw.view.graphicalcontroller.AuthenticatedUser;
import it.uniroma2.dicii.ispw.view.graphicalcontroller.PageHelper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;

public class HomepageController extends AuthenticatedUser implements Observer {


    @FXML
    private Label topNameLbl;

    @Override
    public void initUserData() {
        topNameLbl.setText(AuthenticatedUser.utenteBean.getName());
        Utente u = null;
        try {
            u = new GestioneUtentiController().getUtenteByCf(utenteBean.getCf());
        } catch (ItemNotFoundException e) {
            LoggerManager.logSevere(e.getMessage());
        }
        Client c = LoginManager.getInstance().getHashMap().get(u);
        if(c != null)
            c.attach(this);

        //listener for each tab

    }

    @FXML
    private void onLogoutBtnClick(ActionEvent event) {
        Utente u = null;
        try {
            u = new GestioneUtentiController().getUtenteByCf(AuthenticatedUser.utenteBean.getCf());
        } catch (ItemNotFoundException e) {
            LoggerManager.logSevereException("Errore di login", e);
        }
        Client c = LoginManager.getInstance().getHashMap().get(u);
        if(c != null)
            c.detach(this);

        new LoginController().logout(u);
        AuthenticatedUser.utenteBean = null;

        PageHelper.logout(event);
    }


    @Override
    public void update(String... msg) {
        Platform.runLater(() -> {
            PageHelper.launchAlert(Alert.AlertType.INFORMATION, "Notifica", msg[0]);
        });
    }
}
