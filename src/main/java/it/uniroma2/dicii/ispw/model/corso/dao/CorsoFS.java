package it.uniroma2.dicii.ispw.model.corso.dao;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.exception.ItemAlreadyExistsException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.model.corso.Corso;
import it.uniroma2.dicii.ispw.utils.CSVManager;
import it.uniroma2.dicii.ispw.utils.ConstantMsg;
import it.uniroma2.dicii.ispw.utils.DateParser;
import it.uniroma2.dicii.ispw.utils.LoggerManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CorsoFS implements CorsoDAO {

    private static final String CSV_FILE_NAME = CSVManager.getCsvDir() + "corso.csv";
    private final File file;

    public CorsoFS() throws IOException {
        this.file = new File(CSV_FILE_NAME);

        if(!file.exists()) {
            boolean isFileCreated = file.createNewFile();
            if(!isFileCreated) throw new IOException("Impossibile dialogare con il file");
        }
    }

    @Override
    public Corso getCorsoByNome(String nome) throws ItemNotFoundException {
        Corso c = null;
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new BufferedReader(new FileReader(this.file)));
            String[] rcrd = {};
            int nameIndex = CorsoAttributesOrder.getIndexNome();

            while ((rcrd = csvReader.readNext()) != null) {
                //check if the user exists
                if(rcrd[nameIndex].equals(nome)) {
                    c = setCorsoFromRecord(rcrd);
                }
            }

        } catch (Exception e) {
            LoggerManager.logSevereException(ConstantMsg.ERROR_OPENING_FILE, e);
            throw new ItemNotFoundException("Utente non esistente.");
        } finally {
            CSVManager.closeCsvReader(csvReader);
        }

        if(c == null) throw new ItemNotFoundException("Utente non esistente");

        return c;
    }

    @Override
    public List<Corso> getAllCorsi() {
        List<Corso> corsoList = new ArrayList<>();
        CSVReader csvReader = null;

        try {
            csvReader = new CSVReader(new BufferedReader(new FileReader(this.file)));
            String[] rcrd = {};

            while ((rcrd = csvReader.readNext()) != null) {
                Corso c = setCorsoFromRecord(rcrd);
                corsoList.add(c);
            }

        } catch (Exception e) {
            LoggerManager.logSevereException(ConstantMsg.ERROR_OPENING_FILE, e);
            return corsoList;
        } finally {
            CSVManager.closeCsvReader(csvReader);
        }

        return corsoList;
    }

    @Override
    public void insertCorso(Corso corso) throws ItemAlreadyExistsException {
        boolean duplicatedRecordId;
        Corso c = null;
        String[] rcrd;
        CSVWriter csvWriter = null;

        try {
            c = getCorsoByNome(corso.getName());
            duplicatedRecordId = c != null;
        } catch (ItemNotFoundException e) {
            duplicatedRecordId = false;
        }

        if(duplicatedRecordId) throw new ItemAlreadyExistsException("Corso esistente!");

        try {
            csvWriter = new CSVWriter(new BufferedWriter(new FileWriter(this.file, true)),  ICSVWriter.DEFAULT_SEPARATOR,
                    ICSVWriter.NO_QUOTE_CHARACTER, ICSVWriter.DEFAULT_ESCAPE_CHARACTER, ICSVWriter.RFC4180_LINE_END);

            rcrd = setRecordFromCorso(corso);

            csvWriter.writeNext(rcrd);
            csvWriter.flush();

        } catch (IOException e) {
            LoggerManager.logSevereException("Impossibile scrivere file!", e);
        }finally {
            CSVManager.closeCsvWriter(csvWriter);
        }

    }

    @Override
    public void removeCorso(Corso corso) {
        boolean isExistingCourse = false;
        CSVReader csvReader = null;
        CSVWriter csvWriter = null;
        try {
            csvReader = new CSVReader(new BufferedReader(new FileReader(this.file)));
            String[] rcrd;
            int nameIndex = CorsoAttributesOrder.getIndexNome();

            List<String[]> updatedRecords = new ArrayList<>();

            while ((rcrd = csvReader.readNext()) != null) {
                //check if the user exists
                if(!rcrd[nameIndex].equals(corso.getName())) {
                    updatedRecords.add(rcrd);
                } else {
                    isExistingCourse = true;
                    break;
                }

            }

            if(isExistingCourse) {
                csvWriter = new CSVWriter(new BufferedWriter(new FileWriter(this.file, false)),  ICSVWriter.DEFAULT_SEPARATOR,
                        ICSVWriter.NO_QUOTE_CHARACTER, ICSVWriter.DEFAULT_ESCAPE_CHARACTER, ICSVWriter.RFC4180_LINE_END);
                csvWriter.writeAll(updatedRecords);
                csvWriter.flush();
            }

        } catch (Exception e) {
            LoggerManager.logSevereException(ConstantMsg.ERROR_OPENING_FILE, e);
        } finally {
            CSVManager.closeCsvReader(csvReader);
            CSVManager.closeCsvWriter(csvWriter);
        }

    }

    private Corso setCorsoFromRecord(String[] rcrd) {
        String name = rcrd[CorsoAttributesOrder.getIndexNome()];
        String dateStr = rcrd[CorsoAttributesOrder.getIndexData()];

        Date date = null;
        try {
            date = DateParser.parseStringToDateUtil(dateStr);
        } catch (InvalidDataException e) {
            LoggerManager.logSevereException("Errore di conversione data: ", e);
        }

        return new Corso(name, date);
    }

    private String[] setRecordFromCorso(Corso c) {
        String[] rcrd = new String[2];

        rcrd[CorsoAttributesOrder.getIndexNome()] = c.getName();
        rcrd[CorsoAttributesOrder.getIndexData()] = DateParser.parseDateToString(c.getStartDate());

        return rcrd;
    }


    private static class CorsoAttributesOrder {
        public static int getIndexNome() {
            return 0;
        }
        public static int getIndexData() {
            return 1;
        }
    }
}
