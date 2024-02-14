package it.uniroma2.dicii.ispw.view.cli;

import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.view.cli.segreteria.SegreteriaView;
import it.uniroma2.dicii.ispw.view.cli.utente.IstruttoreView;

public class CliController {

    public void start() {
        UtenteBean authUsr;
        boolean loopCond= false;
        LoginView loginView = new LoginView();

        do {
            loginView.control();
            authUsr = loginView.getUsrBean();
            if(authUsr != null && authUsr.getRuolo() != null)
                loopCond = false;
            else if (loginView.userChoice() == 1) {
                loopCond = true;
            } else {
                System.exit(0);
            }
        } while (loopCond);

        switch (authUsr.getRuolo()) {
            case SEGRETERIA -> new SegreteriaView(authUsr).control();
            case ISTRUTTORE -> new IstruttoreView(authUsr).control();
            case UTENTE -> {
                System.out.println("Utente work in progress...");
                System.exit(0);
            }
        }

    }
}
