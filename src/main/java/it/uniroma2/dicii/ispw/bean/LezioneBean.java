package it.uniroma2.dicii.ispw.bean;

import java.sql.Time;
import java.util.List;

public class LezioneBean {
    private String giorno;
    private Time ora;
    private String sala;
    private String corso;
    private List<String> giornoList;
    private String cf;

    public LezioneBean() {
    }


    public String getGiorno() {
        return giorno;
    }

    public void setGiorno(String giorno) {
        this.giorno = giorno;
    }

    public Time getOra() {
        return ora;
    }

    public void setOra(Time ora) {
        this.ora = ora;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public String getCorso() {
        return corso;
    }

    public void setCorso(String corso) {
        this.corso = corso;
    }

    public List<String> getGiornoList() {
        return giornoList;
    }

    public void setGiornoList(List<String> giornoList) {
        this.giornoList = giornoList;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }
}
