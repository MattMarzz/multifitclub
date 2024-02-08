package it.uniroma2.dicii.ispw.view.graphicalcontroller;

import it.uniroma2.dicii.ispw.bean.UtenteBean;
import it.uniroma2.dicii.ispw.utils.Observer;

public abstract class AuthenticatedUser implements Observer {
    protected static UtenteBean utenteBean;
    public static void setUtenteBean(UtenteBean utenteBean) {
        AuthenticatedUser.utenteBean = utenteBean;
    }
    public static UtenteBean getUtenteBean() {
        return AuthenticatedUser.utenteBean;
    }
    public abstract void initUserData();
}
