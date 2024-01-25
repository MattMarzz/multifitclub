package it.uniroma2.dicii.ispw.model;

import it.uniroma2.dicii.ispw.controller.Subject;

public class AnnouncementManager extends Subject {

    private static AnnouncementManager instance = null;

    private AnnouncementManager() {}

    public synchronized static AnnouncementManager getInstance(){
        if(instance == null) {
            instance = new AnnouncementManager();
        }
        return instance;
    }

}
