package it.uniroma2.dicii.ispw.view.graphicalcontroller.segreteria;

import it.uniroma2.dicii.ispw.utils.LoggerManager;
import it.uniroma2.dicii.ispw.view.graphicalcontroller.AuthenticatedUser;
import it.uniroma2.dicii.ispw.view.graphicalcontroller.PageHelper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;


public class StructuralController extends AuthenticatedUser {

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

    @FXML
    private AnnouncementController announcementController;

    @Override
    public void initUserData() {
        nameLbl.setText(AuthenticatedUser.utenteBean.getName());
        //land on dashboard
        switchView(new ActionEvent(dashboardBtn, null));
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
    private static void onLogoutBtnClick (ActionEvent event) {
        AuthenticatedUser.utenteBean = null;
        PageHelper.logout(event);
    }


}