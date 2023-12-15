package it.uniroma2.dicii.ispw.iscritto.dao;

import it.uniroma2.dicii.ispw.iscritto.Iscritto;

import java.io.*;
import java.util.List;

public class FileSystemPersistence implements IscrittoDAO {

    private static final String FILE_PATH = "iscritti.dat";

    @Override
    public void insertIscritto(Iscritto iscritto) {
        try {
            List<Iscritto> iscrittoList = this.getAllIscritti();
            iscrittoList.add(iscritto);

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH));
            oos.writeObject(iscrittoList);
            oos.close();
            System.out.println("New Iscritto added correctly!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Iscritto> getAllIscritti() {
        List<Iscritto> iscrittoList;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH));
            iscrittoList = (List<Iscritto>) ois.readObject();
            ois.close();
        }
        //TODO: in case of empty file we have an EOF exception to handle correctly
        catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return iscrittoList;
    }
}
