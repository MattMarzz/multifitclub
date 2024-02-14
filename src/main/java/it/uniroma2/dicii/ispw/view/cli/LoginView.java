package it.uniroma2.dicii.ispw.view.cli;

import it.uniroma2.dicii.ispw.bean.LoginBean;
import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.controller.LoginController;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.utils.LoggerManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LoginView extends TemplateView{

    @Override
    public void control() {
        LoginBean loginBean = new LoginBean();
        try {
            loginBean = this.show();
        } catch (IOException e) {
            LoggerManager.logSevereException(e.getMessage(), e);
            System.out.println("Impossibile leggere dati immessi");
        }

        try {
            usrBean = new LoginController().login(loginBean);
        } catch (ItemNotFoundException | InvalidDataException e) {
            LoggerManager.logSevereException(e.getMessage(), e);
            System.out.println("Impossibile accedere!");
        }

    }

    public LoginBean show() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        printHeader("Benvenuto nel sistema di login");

        System.out.print("Inserisci email: ");
        String email = reader.readLine();

        System.out.print("Inserisci password: ");
        String pwd = reader.readLine();

        return new LoginBean(email, pwd);
    }

    @Override
    public int userChoice() {
        List<String> options = new ArrayList<>();
        options.add("Riprova");
        options.add("Esci");
        return operationMenu("Come vuoi procedere?", options);
    }

    public UtenteBean getUsrBean() {
        return usrBean;
    }
}
