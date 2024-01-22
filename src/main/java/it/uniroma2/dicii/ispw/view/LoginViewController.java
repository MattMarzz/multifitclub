package it.uniroma2.dicii.ispw.view;


import it.uniroma2.dicii.ispw.bean.LoginBean;
import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.controller.LoginController;
import it.uniroma2.dicii.ispw.utils.LoggerManager;
import it.uniroma2.dicii.ispw.view.segreteria.DashboardController;
import it.uniroma2.dicii.ispw.view.utente.HomepageController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginViewController {
    @FXML
    private TextField email;
    @FXML
    private Label errorMsg;
    @FXML
    private PasswordField password;

    @FXML
    private void onAccediBtnClick(ActionEvent event) {
        try {
            if (!email.getText().isBlank() && !password.getText().isBlank()) {
                LoginBean loginBean = new LoginBean(email.getText(), password.getText());
                LoginController loginController = new LoginController();
                UtenteBean utenteBean = loginController.login(loginBean);

                if (utenteBean != null) {
                    //navigate to new page
                    switch (utenteBean.getRuolo()){
                        case SEGRETERIA ->  PageHelper.changeScene(event, "views/segreteria/dashboard.fxml", "Dashboard", utenteBean, new DashboardController());
                        case ISTRUTTORE ->  PageHelper.changeScene(event, "views/utente/homepage.fxml", "Home", utenteBean, new HomepageController());
                        case UTENTE -> PageHelper.changeScene(event, "views/utente/homepage.fxml", "Home", utenteBean, new HomepageController());
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

}