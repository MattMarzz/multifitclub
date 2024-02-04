package it.uniroma2.dicii.ispw.enums;

public enum RoomRequestStatus {
    PENDING(0),
    ACCEPTED(1),
    REJECTED(2);

    private final int id;

    private RoomRequestStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static RoomRequestStatus getStatus(int id) {
        for (RoomRequestStatus r: values()) {
            if(r.getId() == id) return r;
        }
        return null;
    }
}
