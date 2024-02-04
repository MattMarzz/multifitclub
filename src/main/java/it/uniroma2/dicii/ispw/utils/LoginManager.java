package it.uniroma2.dicii.ispw.utils;

import it.uniroma2.dicii.ispw.notification.Client;
import it.uniroma2.dicii.ispw.model.utente.Utente;

import java.util.HashMap;

public class LoginManager {
    private static LoginManager instance = null;
    private HashMap<Utente, Client> hashMap = new HashMap<>();
    private LoginManager(){}

    public static synchronized LoginManager getInstance() {
        if(instance == null) {
            instance =  new LoginManager();
        }
        return instance;
    }

    public void setHashMap(Utente u, Client c) {
        this.hashMap.put(u, c);
    }

    public HashMap<Utente, Client> getHashMap() {
        return hashMap;
    }
}
