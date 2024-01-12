package it.uniroma2.dicii.ispw.graphicsControllers;

import it.uniroma2.dicii.ispw.beans.UtenteBean;
import it.uniroma2.dicii.ispw.controllers.LoginController;
import it.uniroma2.dicii.ispw.exceptions.InvalidDataException;
import it.uniroma2.dicii.ispw.exceptions.ItemNotFoundException;
import it.uniroma2.dicii.ispw.utente.Utente;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginViewController {
    public PasswordField password;
    public Button loginBtn;
    public TextField email;
    public Label errorMsg;

    public void onLoginClick() {
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

    public void handleError(String msg){
        this.errorMsg.setText(msg);
    }
}
