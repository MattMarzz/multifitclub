package it.uniroma2.dicii.ispw.graphics_controllers;

import it.uniroma2.dicii.ispw.beans.UtenteBean;
import it.uniroma2.dicii.ispw.controllers.LoginController;
import it.uniroma2.dicii.ispw.models.utente.Utente;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginViewController {
    private PasswordField password;
    private Button loginBtn;
    private TextField email;
    private Label errorMsg;

    protected void onLoginClick() {
        try {
            if (!email.getText().isBlank() && !password.getText().isBlank()) {
                UtenteBean utenteBean = new UtenteBean(email.getText(), password.getText());
                LoginController loginController = new LoginController();
                Utente utente = null;

                utente = loginController.login(utenteBean);

                if (utente != null) {
                    //navigate new page
                    System.out.println("login successful");
                } else {
                    handleError("Credenziali errate!");
                }
            } else handleError("E-mail e password necessarie!");
        } catch (Exception e) {
            handleError(e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleError(String msg){
        this.errorMsg.setText(msg);
    }
}
