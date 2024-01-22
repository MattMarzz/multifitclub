package it.uniroma2.dicii.ispw.bean;

public class LezioneBean {
    private String giorno;
    private String ora;
    private String sala;

    public LezioneBean() {
    }

    public LezioneBean(String giorno, String ora, String sala) {
        this.giorno = giorno;
        this.ora = ora;
        this.sala = sala;
    }

    public String getGiorno() {
        return giorno;
    }

    public void setGiorno(String giorno) {
        this.giorno = giorno;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }
}
