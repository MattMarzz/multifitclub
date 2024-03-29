package it.uniroma2.dicii.ispw.enums;

public enum Ruolo {
    UTENTE(0),
    ISTRUTTORE(1),
    SEGRETERIA(2);

    private final int id;

    private Ruolo(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static Ruolo getRuolo(int id) {
        for (Ruolo r: values()) {
            if(r.getId() == id) return r;
        }
        return null;
    }

}