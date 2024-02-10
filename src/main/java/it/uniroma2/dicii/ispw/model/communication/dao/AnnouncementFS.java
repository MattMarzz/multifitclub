package it.uniroma2.dicii.ispw.model.communication.dao;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.model.communication.Announcement;
import it.uniroma2.dicii.ispw.utils.CSVManager;
import it.uniroma2.dicii.ispw.utils.ConstantMsg;
import it.uniroma2.dicii.ispw.utils.DateParser;
import it.uniroma2.dicii.ispw.utils.LoggerManager;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementFS implements AnnouncementDAO{

    private static final String CSV_FILE_NAME = CSVManager.getCsvDir() + "annuncio.csv";
    private final File file;
    private static final int INDEX_ID = 0;
    private static final int INDEX_TITOLO = 1;
    private static final int INDEX_TESTO = 2;
    private static final int INDEX_DATA = 3;
    private static final int INDEX_UTENTE = 4;

    public AnnouncementFS() throws IOException {
        this.file = new File(CSV_FILE_NAME);

        if(!file.exists()) {
            boolean isFileCreated = file.createNewFile();
            if(!isFileCreated) throw new IOException("Impossibile dialogare con il file");
        }
    }

    @Override
    public String insertAnnouncement(Announcement announcement) {
        CSVWriter csvWriter = null;
        String[] rcrd;
        try {
            csvWriter = new CSVWriter(new BufferedWriter(new FileWriter(this.file, true)),  ICSVWriter.DEFAULT_SEPARATOR,
                    ICSVWriter.NO_QUOTE_CHARACTER, ICSVWriter.DEFAULT_ESCAPE_CHARACTER, ICSVWriter.RFC4180_LINE_END);

            List<Announcement> allAnns = getAllAnnouncement();
            int lastId = 0;
            if(!allAnns.isEmpty())
                lastId = allAnns.getLast().getAnnId();

            announcement.setAnnId(lastId + 1);

            rcrd = setRecordFromAnnouncement(announcement);

            csvWriter.writeNext(rcrd);
            csvWriter.flush();

        } catch (IOException e) {
            LoggerManager.logSevereException("Impossibile scrivere file!", e);
            return "Errore in inserimento";

        }finally {
            CSVManager.closeCsvWriter(csvWriter);
        }

        return "Inserimento effettuato";
    }

    @Override
    public List<Announcement> getAllAnnouncement() {
        List<Announcement> announcementList = new ArrayList<>();
        CSVReader csvReader = null;

        try {
            csvReader = new CSVReader(new BufferedReader(new FileReader(this.file)));
            String[] rcrd = {};

            while ((rcrd = csvReader.readNext()) != null) {
                Announcement a = setAnnouncementFromRecord(rcrd);
                announcementList.add(a);
            }

        } catch (Exception e) {
            LoggerManager.logSevereException(ConstantMsg.ERROR_OPENING_FILE, e);
            return announcementList;
        } finally {
            CSVManager.closeCsvReader(csvReader);
        }

        return announcementList;
    }

    private Announcement setAnnouncementFromRecord(String[] rcrd) {
        int id = Integer.parseInt(rcrd[INDEX_ID]);
        String titolo = rcrd[INDEX_TITOLO];
        String testo = rcrd[INDEX_TESTO];
        String dataStr = rcrd[INDEX_DATA];
        String utente = rcrd[INDEX_UTENTE];

        Timestamp date = null;
        try {
            date = DateParser.parseStringToTimestamp(dataStr);
        } catch (InvalidDataException e) {
            LoggerManager.logSevereException("Errore di conversione data: ", e);
        }

        return new Announcement(utente, titolo , testo, date, id);
    }

    private String[] setRecordFromAnnouncement(Announcement a) {
        String[] rcrd = new String[5];

        rcrd[INDEX_ID] = String.valueOf(a.getAnnId());
        rcrd[INDEX_TITOLO] = a.getTitle();
        rcrd[INDEX_TESTO] = a.getMsg();
        rcrd[INDEX_UTENTE] = a.getSender();
        rcrd[INDEX_DATA] = DateParser.parseTimestampToString(a.getDate());

        return rcrd;
    }

}
