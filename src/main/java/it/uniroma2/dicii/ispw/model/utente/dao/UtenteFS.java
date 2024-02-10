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
            int emIndex = UtenteAttributesOrder.getIndexEmail();
            int pwdIndex = UtenteAttributesOrder.getIndexPwd();

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
            int cfIndex = UtenteAttributesOrder.getIndexCf();

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
            int cfIndex = UtenteAttributesOrder.getIndexCf();

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
        String cf = rcrd[UtenteAttributesOrder.getIndexCf()];
        String name = rcrd[UtenteAttributesOrder.getIndexNome()];
        String surname = rcrd[UtenteAttributesOrder.getIndexCognome()];
        int roleId = Integer.parseInt(rcrd[UtenteAttributesOrder.getIndexRuolo()]);
        String email = rcrd[UtenteAttributesOrder.getIndexEmail()];
        String pwd = rcrd[UtenteAttributesOrder.getIndexPwd()];
        String dateStr = rcrd[UtenteAttributesOrder.getIndexData()];

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

        rcrd[UtenteAttributesOrder.getIndexCf()] = u.getCf();
        rcrd[UtenteAttributesOrder.getIndexNome()] = u.getName();
        rcrd[UtenteAttributesOrder.getIndexCognome()] = u.getSurname();
        rcrd[UtenteAttributesOrder.getIndexEmail()] = u.getEmail();
        rcrd[UtenteAttributesOrder.getIndexPwd()] = u.getPassword();
        rcrd[UtenteAttributesOrder.getIndexData()] = DateParser.parseDateToString(u.getBirthDate());
        rcrd[UtenteAttributesOrder.getIndexRuolo()] = String.valueOf(u.getRuolo().ordinal());

        return rcrd;
    }



    private static class UtenteAttributesOrder {
        public static int getIndexCf() {
            return 0;
        }
        public static int getIndexNome() {
            return 1;
        }
        public static int getIndexCognome() {
            return 2;
        }
        public static int getIndexData() {
            return 3;
        }
        public static int getIndexRuolo() {
            return 4;
        }
        public static int getIndexEmail() {
            return 5;
        }
        public static int getIndexPwd() {
            return 6;
        }
    }


}
