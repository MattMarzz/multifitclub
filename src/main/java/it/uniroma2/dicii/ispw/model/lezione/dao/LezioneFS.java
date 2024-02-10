package it.uniroma2.dicii.ispw.model.lezione.dao;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.exception.ItemAlreadyExistsException;
import it.uniroma2.dicii.ispw.model.lezione.Lezione;
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
        return getParametricLessons(LezioneAttributesOrder.getIndexCorso(), nomeCorso);
    }

    @Override
    public List<Lezione> getAllLezioniForDay(String giorno) {
        return getParametricLessons(LezioneAttributesOrder.getIndexGiorno(), giorno);
    }

    @Override
    public String insertLezioni(List<Lezione> lezioneList) throws ItemAlreadyExistsException {
        String[] rcrd;
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
            csvWriter = new CSVWriter(new BufferedWriter(new FileWriter(this.file, true)),  ICSVWriter.DEFAULT_SEPARATOR,
                    ICSVWriter.NO_QUOTE_CHARACTER, ICSVWriter.DEFAULT_ESCAPE_CHARACTER, ICSVWriter.RFC4180_LINE_END);
            for (Lezione l : lezioneList) {
                rcrd = setRecordFromLezione(l);

                csvWriter.writeNext(rcrd);
                csvWriter.flush();
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
            String[] rcrd = {};

            while ((rcrd = csvReader.readNext()) != null) {
                Lezione l = setLezioneFromRecord(rcrd);
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

    private Lezione setLezioneFromRecord(String[] rcrd) {
        String giorno = rcrd[LezioneAttributesOrder.getIndexGiorno()];
        String oraStr = rcrd[LezioneAttributesOrder.getIndexOra()];
        String sala = rcrd[LezioneAttributesOrder.getIndexSala()];
        String corso = rcrd[LezioneAttributesOrder.getIndexCorso()];
        String istruttore = rcrd[LezioneAttributesOrder.getIndexIstruttore()];

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
            String[] rcrd = {};

            while ((rcrd = csvReader.readNext()) != null) {
                //check if the user exists
                if(rcrd[index].equals(str)) {
                    lezioneList.add(setLezioneFromRecord(rcrd));
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
        String[] rcrd = new String[5];

        rcrd[LezioneAttributesOrder.getIndexGiorno()] = l.getDay();
        rcrd[LezioneAttributesOrder.getIndexOra()] = DateParser.parseTimeToString(l.getStartTime());
        rcrd[LezioneAttributesOrder.getIndexSala()] = l.getSala();
        rcrd[LezioneAttributesOrder.getIndexCorso()] = l.getCourseName();
        rcrd[LezioneAttributesOrder.getIndexIstruttore()] = l.getCfUtente();

        return rcrd;
    }



    private static class LezioneAttributesOrder {
        public static int getIndexGiorno() {
            return 0;
        }
        public static int getIndexOra() {
            return 1;
        }
        public static int getIndexSala() {
            return 2;
        }
        public static int getIndexCorso() {
            return 3;
        }
        public static int getIndexIstruttore() {
            return 4;
        }
    }
}
