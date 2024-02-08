package it.uniroma2.dicii.ispw.utils;

import java.util.ArrayList;
import java.util.List;

public class Subject {
    private List<Observer> observers = new ArrayList<>();

    public void attach(Observer observer) {
        this.observers.add(observer);
    }

    public void detach(Observer observer) {
        this.observers.remove(observer);
    }

    public void notifyChanges(String... msg) {
        for (Observer o: this.observers) {
            if(msg[0].equals("Reload"))
                o.update();
            else
                o.update(msg);
        }
    }
}
