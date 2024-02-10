package it.uniroma2.dicii.ispw.model.utente.dao;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
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
        String[] record;
        CSVWriter csvWriter = null;
        try {
            u = getUtenteByCf(utente.getCf());
            duplicatedRecordId = u != null;
        } catch (ItemNotFoundException e) {
            duplicatedRecordId = false;
        }

        if(duplicatedRecordId) throw new ItemAlreadyExistsException("Utente esistente!");

        try {
             csvWriter = new CSVWriter(new BufferedWriter(new FileWriter(this.file, true)),  CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.RFC4180_LINE_END);

            record = setRecordFromUtente(utente);

            csvWriter.writeNext(record);
            csvWriter.flush();
            CSVManager.closeCsvWriter(csvWriter);

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
            String[] record;
            int emIndex = UtenteAttributesOrder.getIndex_Email();
            int pwdIndex = UtenteAttributesOrder.getIndex_Pwd();

            while ((record = csvReader.readNext()) != null) {
                //check if the user exists
                if(record[emIndex].equals(loginBean.getEmail()) &&
                    record[pwdIndex].equals(loginBean.getPassword())) {
                    u = setUtenteFromRecord(record);
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
            String[] record = {};
            int cfIndex = UtenteAttributesOrder.getIndex_Cf();

            while ((record = csvReader.readNext()) != null) {
                //check if the user exists
                if(record[cfIndex].equals(cf)) {
                    u = setUtenteFromRecord(record);
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
            String[] record = {};

            while ((record = csvReader.readNext()) != null) {
                Utente u = setUtenteFromRecord(record);
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
        Utente u = null;
        boolean isExistingUser = false;
        CSVReader csvReader = null;
        CSVWriter csvWriter = null;
        try {
            csvReader = new CSVReader(new BufferedReader(new FileReader(this.file)));
            String[] record;
            int cfIndex = UtenteAttributesOrder.getIndex_Cf();

            List<String[]> updatedRecords = new ArrayList<>();

            while ((record = csvReader.readNext()) != null) {
                //check if the user exists
                if(record[cfIndex].equals(utente.getCf())) {
                    isExistingUser = true;
                    record = setRecordFromUtente(utente);
                }
                updatedRecords.add(record);
            }

            csvWriter = new CSVWriter(new BufferedWriter(new FileWriter(this.file, false)),  CSVWriter.DEFAULT_SEPARATOR,
                    CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.RFC4180_LINE_END);
            csvWriter.writeAll(updatedRecords);
            csvWriter.flush();

        } catch (Exception e) {
            LoggerManager.logSevereException(ConstantMsg.ERROR_OPENING_FILE, e);
            return "Modifica impossibile";
        } finally {
            CSVManager.closeCsvReader(csvReader);
            CSVManager.closeCsvWriter(csvWriter);
        }

        return  isExistingUser ? "Modifica effettuata!" : "Utente non trovato.";
    }

    private Utente setUtenteFromRecord(String[] record) {
        String cf = record[UtenteAttributesOrder.getIndex_Cf()];
        String name = record[UtenteAttributesOrder.getIndex_Nome()];
        String surname = record[UtenteAttributesOrder.getIndex_Cognome()];
        int roleId = Integer.parseInt(record[UtenteAttributesOrder.getIndex_Ruolo()]);
        String email = record[UtenteAttributesOrder.getIndex_Email()];
        String pwd = record[UtenteAttributesOrder.getIndex_Pwd()];
        String dateStr = record[UtenteAttributesOrder.getIndex_Data()];

        Date date = null;
        try {
            date = DateParser.parseStringToDateUtil(dateStr);
        } catch (InvalidDataException e) {
            LoggerManager.logSevereException("Errore di conversione data: ", e);
        }


        return new Utente(name, surname, cf, date, email, pwd, Ruolo.getRuolo(roleId));
    }

    private synchronized String[] setRecordFromUtente(Utente u) throws IOException {
        String[] record = new String[7];

        record[UtenteAttributesOrder.getIndex_Cf()] = u.getCf();
        record[UtenteAttributesOrder.getIndex_Nome()] = u.getName();
        record[UtenteAttributesOrder.getIndex_Cognome()] = u.getSurname();
        record[UtenteAttributesOrder.getIndex_Email()] = u.getEmail();
        record[UtenteAttributesOrder.getIndex_Pwd()] = u.getPassword();
        record[UtenteAttributesOrder.getIndex_Data()] = DateParser.parseDateToString(u.getBirthDate());
        record[UtenteAttributesOrder.getIndex_Ruolo()] = String.valueOf(u.getRuolo().ordinal());

        return record;
    }



    private static class UtenteAttributesOrder {
        public static int getIndex_Cf() {
            return 0;
        }
        public static int getIndex_Nome() {
            return 1;
        }
        public static int getIndex_Cognome() {
            return 2;
        }
        public static int getIndex_Data() {
            return 3;
        }
        public static int getIndex_Ruolo() {
            return 4;
        }
        public static int getIndex_Email() {
            return 5;
        }
        public static int getIndex_Pwd() {
            return 6;
        }
    }


}
