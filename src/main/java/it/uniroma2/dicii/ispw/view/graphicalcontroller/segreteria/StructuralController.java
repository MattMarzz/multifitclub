package it.uniroma2.dicii.ispw.view.graphicalcontroller.segreteria;

import it.uniroma2.dicii.ispw.controller.GestioneUtentiController;
import it.uniroma2.dicii.ispw.controller.LoginController;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.model.utente.Utente;
import it.uniroma2.dicii.ispw.utils.LoggerManager;
import it.uniroma2.dicii.ispw.utils.LoginManager;
import it.uniroma2.dicii.ispw.notification.Client;
import it.uniroma2.dicii.ispw.utils.Observer;
import it.uniroma2.dicii.ispw.view.graphicalcontroller.AuthenticatedUser;
import it.uniroma2.dicii.ispw.view.graphicalcontroller.PageHelper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;


public class StructuralController extends AuthenticatedUser implements Observer {

    @FXML
    private Button coursesListBtn;
    @FXML
    private Button addUserBtn;
    @FXML
    private AnchorPane addUserView;
    @FXML
    private Button announceBtn;
    @FXML
    private AnchorPane announceView;
    @FXML
    private AnchorPane coursesListView;
    @FXML
    private Button dashboardBtn;
    @FXML
    private AnchorPane dashboardView;
    @FXML
    private Label nameLbl;
    @FXML
    private Button reservationBtn;
    @FXML
    private AnchorPane reservationView;
    @FXML
    private Button schedulingBtn;
    @FXML
    private AnchorPane schedulingView;
    @FXML
    private Button userListBtn;
    @FXML
    private AnchorPane usersListView;

    @Override
    public void initUserData() {
        nameLbl.setText(AuthenticatedUser.utenteBean.getName());
        //land on dashboard
        switchView(new ActionEvent(dashboardBtn, null));
        Utente u = null;
        try {
            u = new GestioneUtentiController().getUtenteByCf(utenteBean.getCf());
        } catch (ItemNotFoundException e) {
            LoggerManager.logSevereException(e.getMessage());
        }
        Client c = LoginManager.getInstance().getHashMap().get(u);
        if(c != null)
            c.attach(this);
    }


    public void switchView(ActionEvent event) {

        if(event.getSource() == dashboardBtn) {
            setRightView(true, false, false, false, false, false, false);
        } else if (event.getSource() == reservationBtn) {
            setRightView(false, true, false, false, false, false, false);
        } else if (event.getSource() == announceBtn) {
            setRightView(false, false, true, false, false, false, false);
        } else if (event.getSource() == userListBtn) {
            setRightView(false, false, false, true, false, false, false);
        } else if (event.getSource() == addUserBtn) {
            setRightView(false, false, false, false, true, false, false);
        } else if (event.getSource() == coursesListBtn) {
            setRightView(false, false, false, false, false, true, false);
        } else if (event.getSource() == schedulingBtn) {
            setRightView(false, false, false, false, false, false, true);
        }
    }

    private void setRightView(boolean dash, boolean res, boolean ann, boolean u, boolean addu, boolean c, boolean sch) {
        dashboardView.setVisible(dash);
        reservationView.setVisible(res);
        announceView.setVisible(ann);
        usersListView.setVisible(u);
        addUserView.setVisible(addu);
        coursesListView.setVisible(c);
        schedulingView.setVisible(sch);
    }

    @FXML
    private void onLogoutBtnClick (ActionEvent event) {
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
            // Aggiorna la view con la notifica (ad esempio, mostra una snack bar)
            // Puoi accedere direttamente agli elementi della UI da qui
            if(msg.length != 0)
                PageHelper.launchAlert(Alert.AlertType.INFORMATION, "Notifica", msg[0]);
        });

    }
}