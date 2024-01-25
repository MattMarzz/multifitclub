package it.uniroma2.dicii.ispw.view;

import it.uniroma2.dicii.ispw.bean.UtenteBean;

public abstract class AuthenticatedUser {
    protected static UtenteBean utenteBean;
    public void setUtenteBean(UtenteBean utenteBean) {
        AuthenticatedUser.utenteBean = utenteBean;
    }

    public static UtenteBean getUtenteBean() {
        return AuthenticatedUser.utenteBean;
    }
    public abstract void initUserData();
}
