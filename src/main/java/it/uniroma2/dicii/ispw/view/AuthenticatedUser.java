package it.uniroma2.dicii.ispw.view;

import it.uniroma2.dicii.ispw.bean.UtenteBean;

public abstract class AuthenticatedUser {
    protected UtenteBean utenteBean;

    public void setUtenteBean(UtenteBean utenteBean) {
        this.utenteBean = utenteBean;
    }
    public abstract void initUserData();
}
