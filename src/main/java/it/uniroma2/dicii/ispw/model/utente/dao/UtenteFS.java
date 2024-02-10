package it.uniroma2.dicii.ispw.model.utente.dao;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import it.uniroma2.dicii.ispw.bean.LoginBean;
import it.uniroma2.dicii.ispw.enums.Ruolo;
import it.uniroma2.dicii.ispw.exception.InvalidDataException;
import it.uniroma2.dicii.ispw.exception.ItemAlreadyExistsException;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.model.utente.Utente;
import it.uniroma2.dicii.ispw.utils.CSVManager;
import it.uniroma2.dicii.ispw.utils.ConstantMsg;
import it.uniroma2.dicii.ispw.utils.DateParser;
import it.uniroma2.dicii.ispw.utils.LoggerManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UtenteFS implements UtenteDAO {

    private static final String CSV_FILE_NAME = CSVManager.getCsvDir() + "utente.csv";
    private final File file;
    private static final int INDEX_CF = 0;
    private static final int INDEX_NOME = 1;
    private static final int INDEX_COGNOME = 2;
    private static final int INDEX_DATA = 3;
    private static final int INDEX_RUOLO = 4;
    private static final int INDEX_EMAIL = 5;
    private static final int INDEX_PWD = 6;

    public UtenteFS() throws IOException {
        this.file = new File(CSV_FILE_NAME);

        if(!file.exists()) {
            boolean isFileCreated = file.createNewFile();
            if(!isFileCreated) throw new IOException("Impossibile dialogare con il file");
        }
    }

    @Override
    public String insertUtente(Utente utente) throws ItemAlreadyExistsException {
        boolean duplicatedRecordId;
        Utente u = null;
        String[] rcrd;
        CSVWriter csvWriter = null;
        try {
            u = getUtenteByCf(utente.getCf());
            duplicatedRecordId = u != null;
        } catch (ItemNotFoundException e) {
            duplicatedRecordId = false;
        }

        if(duplicatedRecordId) throw new ItemAlreadyExistsException("Utente esistente!");

        try {
             csvWriter = new CSVWriter(new BufferedWriter(new FileWriter(this.file, true)),  ICSVWriter.DEFAULT_SEPARATOR,
                    ICSVWriter.NO_QUOTE_CHARACTER, ICSVWriter.DEFAULT_ESCAPE_CHARACTER, ICSVWriter.RFC4180_LINE_END);

            rcrd = setRecordFromUtente(utente);

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
    public Utente auth(LoginBean loginBean) throws ItemNotFoundException {
        Utente u = null;
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new BufferedReader(new FileReader(this.file)));
            String[] rcrd;
            int emIndex = INDEX_EMAIL;
            int pwdIndex = INDEX_PWD;

            while ((rcrd = csvReader.readNext()) != null) {
                //check if the user exists
                if(rcrd[emIndex].equals(loginBean.getEmail()) &&
                    rcrd[pwdIndex].equals(loginBean.getPassword())) {
                    u = setUtenteFromRecord(rcrd);
                    break;
                }
            }

        } catch (Exception e) {
            LoggerManager.logSevereException(ConstantMsg.ERROR_OPENING_FILE, e);
            throw new ItemNotFoundException("Credenziali errate");
        } finally {
            CSVManager.closeCsvReader(csvReader);
        }

        if(u == null) throw new ItemNotFoundException("Credenziali errate");

        return u;
    }

    @Override
    public Utente getUtenteByCf(String cf) throws ItemNotFoundException {
        Utente u = null;
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new BufferedReader(new FileReader(this.file)));
            String[] rcrd = {};
            int cfIndex = INDEX_CF;

            while ((rcrd = csvReader.readNext()) != null) {
                //check if the user exists
                if(rcrd[cfIndex].equals(cf)) {
                    u = setUtenteFromRecord(rcrd);
                }
            }

        } catch (Exception e) {
            LoggerManager.logSevereException(ConstantMsg.ERROR_OPENING_FILE, e);
            throw new ItemNotFoundException("Utente non esistente.");
        } finally {
            CSVManager.closeCsvReader(csvReader);
        }

        if(u == null) throw new ItemNotFoundException("Utente non esistente");

        return u;
    }

    @Override
    public List<Utente> getAllUtenti() {
        List<Utente> utenteList = new ArrayList<>();
        CSVReader csvReader = null;

        try {
            csvReader = new CSVReader(new BufferedReader(new FileReader(this.file)));
            String[] rcrd = {};

            while ((rcrd = csvReader.readNext()) != null) {
                Utente u = setUtenteFromRecord(rcrd);
                utenteList.add(u);
            }

        } catch (Exception e) {
            LoggerManager.logSevereException(ConstantMsg.ERROR_OPENING_FILE, e);
            return utenteList;
        } finally {
            CSVManager.closeCsvReader(csvReader);
        }

        return utenteList;
    }

    @Override
    public String editUtente(Utente utente) {
        boolean isExistingUser = false;
        CSVReader csvReader = null;
        CSVWriter csvWriter = null;
        try {
            csvReader = new CSVReader(new BufferedReader(new FileReader(this.file)));
            String[] rcrd;
            int cfIndex = INDEX_CF;

            List<String[]> updatedRecords = new ArrayList<>();

            while ((rcrd = csvReader.readNext()) != null) {
                //check if the user exists
                if(rcrd[cfIndex].equals(utente.getCf())) {
                    isExistingUser = true;
                    rcrd = setRecordFromUtente(utente);
                }
                updatedRecords.add(rcrd);
            }

            if(isExistingUser) {
                csvWriter = new CSVWriter(new BufferedWriter(new FileWriter(this.file, false)),  ICSVWriter.DEFAULT_SEPARATOR,
                        ICSVWriter.NO_QUOTE_CHARACTER, ICSVWriter.DEFAULT_ESCAPE_CHARACTER, ICSVWriter.RFC4180_LINE_END);
                csvWriter.writeAll(updatedRecords);
                csvWriter.flush();
                return "Modifica effettuata!";
            } else
                return "Utente non trovato.";

        } catch (Exception e) {
            LoggerManager.logSevereException(ConstantMsg.ERROR_OPENING_FILE, e);
            return "Modifica impossibile";
        } finally {
            CSVManager.closeCsvReader(csvReader);
            CSVManager.closeCsvWriter(csvWriter);
        }
    }

    private Utente setUtenteFromRecord(String[] rcrd) {
        String cf = rcrd[INDEX_CF];
        String name = rcrd[INDEX_NOME];
        String surname = rcrd[INDEX_COGNOME];
        int roleId = Integer.parseInt(rcrd[INDEX_RUOLO]);
        String email = rcrd[INDEX_EMAIL];
        String pwd = rcrd[INDEX_PWD];
        String dateStr = rcrd[INDEX_DATA];

        Date date = null;
        try {
            date = DateParser.parseStringToDateUtil(dateStr);
        } catch (InvalidDataException e) {
            LoggerManager.logSevereException("Errore di conversione data: ", e);
        }


        return new Utente(name, surname, cf, date, email, pwd, Ruolo.getRuolo(roleId));
    }

    private String[] setRecordFromUtente(Utente u) {
        String[] rcrd = new String[7];

        rcrd[INDEX_CF] = u.getCf();
        rcrd[INDEX_NOME] = u.getName();
        rcrd[INDEX_COGNOME] = u.getSurname();
        rcrd[INDEX_EMAIL] = u.getEmail();
        rcrd[INDEX_PWD] = u.getPassword();
        rcrd[INDEX_DATA] = DateParser.parseDateToString(u.getBirthDate());
        rcrd[INDEX_RUOLO] = String.valueOf(u.getRuolo().ordinal());

        return rcrd;
    }
    
}
