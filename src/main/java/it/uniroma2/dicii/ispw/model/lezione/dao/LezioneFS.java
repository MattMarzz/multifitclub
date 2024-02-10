package it.uniroma2.dicii.ispw.model.lezione.dao;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.exception.ItemAlreadyExistsException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.model.lezione.Lezione;
import it.uniroma2.dicii.ispw.model.utente.Utente;
import it.uniroma2.dicii.ispw.model.utente.dao.UtenteFS;
import it.uniroma2.dicii.ispw.utils.CSVManager;
import it.uniroma2.dicii.ispw.utils.ConstantMsg;
import it.uniroma2.dicii.ispw.utils.DateParser;
import it.uniroma2.dicii.ispw.utils.LoggerManager;

import java.io.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class LezioneFS implements LezioneDAO{

    private static final String CSV_FILE_NAME = CSVManager.getCsvDir() + "lezione.csv";
    private File file;
    public LezioneFS() throws IOException {
        this.file = new File(CSV_FILE_NAME);

        if(!file.exists()) {
            boolean isFileCreated = file.createNewFile();
            if(!isFileCreated) throw new IOException("Impossibile dialogare con il file");
        }
    }

    @Override
    public List<Lezione> getLezioniByCourseId(String nomeCorso) {
        return getParametricLessons(LezioneAttributesOrder.getIndex_Corso(), nomeCorso);
    }

    @Override
    public List<Lezione> getAllLezioniForDay(String giorno) {
        return getParametricLessons(LezioneAttributesOrder.getIndex_Giorno(), giorno);
    }

    @Override
    public String insertLezioni(List<Lezione> lezioneList) throws ItemAlreadyExistsException {
        String[] record;
        CSVWriter csvWriter = null;

        List<Lezione> alreadyInLesson = getAllLezioni();
        for (Lezione l: lezioneList) {
            for (Lezione inL: alreadyInLesson) {
                if(l.getDay().equals(inL.getDay()) && l.getSala().equals(inL.getSala())&&
                    l.getStartTime().equals(inL.getStartTime())) {
                    throw new ItemAlreadyExistsException("Lezione già esistente");
                }
            }
        }

        try {
            csvWriter = new CSVWriter(new BufferedWriter(new FileWriter(this.file, true)),  CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.RFC4180_LINE_END);
            for (Lezione l : lezioneList) {
                record = setRecordFromLezione(l);

                csvWriter.writeNext(record);
                csvWriter.flush();
                CSVManager.closeCsvWriter(csvWriter);
            }

        } catch (IOException e) {
            LoggerManager.logSevereException("Impossibile scrivere file!", e);
            return "Errore in inserimento";

        }finally {
            CSVManager.closeCsvWriter(csvWriter);
        }

        return "Inserimento effettuato";
    }

    @Override
    public List<Lezione> getAllLezioni() {
        List<Lezione> lezioneList = new ArrayList<>();
        CSVReader csvReader = null;

        try {
            csvReader = new CSVReader(new BufferedReader(new FileReader(this.file)));
            String[] record = {};

            while ((record = csvReader.readNext()) != null) {
                Lezione l = setLezioneFromRecord(record);
                lezioneList.add(l);
            }

        } catch (Exception e) {
            LoggerManager.logSevereException(ConstantMsg.ERROR_OPENING_FILE, e);
            return lezioneList;
        } finally {
            CSVManager.closeCsvReader(csvReader);
        }

        return lezioneList;
    }

    private Lezione setLezioneFromRecord(String[] record) {
        String giorno = record[LezioneAttributesOrder.getIndex_Giorno()];
        String oraStr = record[LezioneAttributesOrder.getIndex_Ora()];
        String sala = record[LezioneAttributesOrder.getIndex_Sala()];
        String corso = record[LezioneAttributesOrder.getIndex_Corso()];
        String istruttore = record[LezioneAttributesOrder.getIndex_Istruttore()];

        Time time = null;
        try {
            time = DateParser.parseStringToTime(oraStr);
        } catch (InvalidDataException e) {
            LoggerManager.logSevereException(e.getMessage(), e);
        }

        return new Lezione(giorno, time, sala, corso, istruttore);
    }

    private synchronized List<Lezione> getParametricLessons(int index, String str) {
        List<Lezione> lezioneList = new ArrayList<>();
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new BufferedReader(new FileReader(this.file)));
            String[] record = {};

            while ((record = csvReader.readNext()) != null) {
                //check if the user exists
                if(record[index].equals(str)) {
                    lezioneList.add(setLezioneFromRecord(record));
                }
            }

        } catch (Exception e) {
            LoggerManager.logSevereException(ConstantMsg.ERROR_OPENING_FILE, e);
            return lezioneList;
        } finally {
            CSVManager.closeCsvReader(csvReader);
        }

        return lezioneList;
    }

    private  String[] setRecordFromLezione(Lezione l){
        String[] record = new String[5];

        record[LezioneAttributesOrder.getIndex_Giorno()] = l.getDay();
        record[LezioneAttributesOrder.getIndex_Ora()] = DateParser.parseTimeToString(l.getStartTime());
        record[LezioneAttributesOrder.getIndex_Sala()] = l.getSala();
        record[LezioneAttributesOrder.getIndex_Corso()] = l.getCourseName();
        record[LezioneAttributesOrder.getIndex_Istruttore()] = l.getCfUtente();

        return record;
    }



    private static class LezioneAttributesOrder {
        public static int getIndex_Giorno() {
            return 0;
        }
        public static int getIndex_Ora() {
            return 1;
        }
        public static int getIndex_Sala() {
            return 2;
        }
        public static int getIndex_Corso() {
            return 3;
        }
        public static int getIndex_Istruttore() {
            return 4;
        }
    }
}
