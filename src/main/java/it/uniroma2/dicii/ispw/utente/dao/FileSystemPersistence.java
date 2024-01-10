package it.uniroma2.dicii.ispw.utente.dao;

import it.uniroma2.dicii.ispw.utente.Utente;

import java.io.*;
import java.util.List;

public class FileSystemPersistence implements UtenteDAO {

    private static final String FILE_PATH = "iscritti.dat";

    @Override
    public void insertIscritto(Utente utente) {
        try {
            List<Utente> utenteList = this.getAllIscritti();
            utenteList.add(utente);

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH));
            oos.writeObject(utenteList);
            oos.close();
            System.out.println("New Iscritto added correctly!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Utente> getAllIscritti() {
        List<Utente> utenteList;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH));
            utenteList = (List<Utente>) ois.readObject();
            ois.close();
        }
        //TODO: in case of empty file we have an EOF exception to handle correctly
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return utenteList;
    }
}
