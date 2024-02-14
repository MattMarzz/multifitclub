package it.uniroma2.dicii.ispw.view.cli;

import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.view.cli.segreteria.SegreteriaView;
import it.uniroma2.dicii.ispw.view.cli.utente.IstruttoreView;

public class CliController {

    public void start() {
        UtenteBean authUsr;
        boolean loop_cond= false;
        LoginView loginView = new LoginView();

        do {
            loginView.control();
            authUsr = loginView.getUsrBean();
            if(authUsr != null && authUsr.getRuolo() != null)
                loop_cond = false;
            else if (loginView.userChoice() == 1) {
                loop_cond = true;
            } else {
                System.exit(0);
            }
        } while (loop_cond);

        switch (authUsr.getRuolo()) {
            case SEGRETERIA -> new SegreteriaView(authUsr).control();
            case ISTRUTTORE -> new IstruttoreView(authUsr).control();
        }

    }
}
