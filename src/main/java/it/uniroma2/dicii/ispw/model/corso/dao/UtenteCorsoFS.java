package it.uniroma2.dicii.ispw.model.corso.dao;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.ICSVWriter;
import it.uniroma2.dicii.ispw.enums.UserRoleInCourse;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;
import it.uniroma2.dicii.ispw.model.corso.Corso;
import it.uniroma2.dicii.ispw.model.utente.Utente;
import it.uniroma2.dicii.ispw.model.utente.dao.UtenteFS;
import it.uniroma2.dicii.ispw.utils.CSVManager;
import it.uniroma2.dicii.ispw.utils.ConstantMsg;
import it.uniroma2.dicii.ispw.utils.LoggerManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UtenteCorsoFS implements UtenteCorsoDAO{

    private static final String CSV_FILE_NAME_INSEGNATO = CSVManager.getCsvDir() + "insegnato.csv";
    private static final String CSV_FILE_NAME_ISCRIZIONE = CSVManager.getCsvDir() + "iscrizione.csv";
    private final File fileInsegnato;
    private final File fileIscrizione;
    public UtenteCorsoFS() throws IOException {
        this.fileInsegnato = new File(CSV_FILE_NAME_INSEGNATO);
        this.fileIscrizione = new File(CSV_FILE_NAME_ISCRIZIONE);

        if(!fileInsegnato.exists()) {
            boolean isFileCreated = fileInsegnato.createNewFile();
            if(!isFileCreated) throw new IOException("Impossibile dialogare con il file");
        }

        if(!fileIscrizione.exists()) {
            boolean isFileCreated = fileIscrizione.createNewFile();
            if(!isFileCreated) throw new IOException("Impossibile dialogare con il file");
        }
    }

    @Override
    public List<Corso> getCoursesByUserId(String cf, UserRoleInCourse userRoleInCourse) {
        List<String> coursesNames = getRelationships(RelationshipAttributesOrder.getIndexUtente(),
                    RelationshipAttributesOrder.getIndexCorso(), cf, userRoleInCourse);

        List<Corso> corsoList = new ArrayList<>();
        try {
            CorsoFS corsoFS = new CorsoFS();
            for (String courseName: coursesNames) {
                corsoList.add(corsoFS.getCorsoByNome(courseName));
            }
        } catch (IOException | ItemNotFoundException e) {
            LoggerManager.logSevereException(e.getMessage(), e);
            return corsoList;
        }
        return corsoList;
    }

    @Override
    public List<Utente> getUsersByCourseId(String nomeCorso, UserRoleInCourse userRoleInCourse)  {
        List<String> cfs = getRelationships(RelationshipAttributesOrder.getIndexCorso(),
                RelationshipAttributesOrder.getIndexUtente(), nomeCorso, userRoleInCourse);

        List<Utente> utenteList = new ArrayList<>();
        try {
            UtenteFS utenteFS = new UtenteFS();
            for (String cf: cfs) {
                utenteList.add(utenteFS.getUtenteByCf(cf));
            }
        } catch (IOException | ItemNotFoundException e) {
            LoggerManager.logSevereException(e.getMessage(), e);
            return utenteList;
        }
        return utenteList;
    }

    @Override
    public void removeEnrollmentByUtente(Utente utente, Corso corso) {
        boolean isExistingEnrollment = false;
        CSVReader csvReader = null;
        CSVWriter csvWriter = null;
        try {
            csvReader = new CSVReader(new BufferedReader(new FileReader(this.fileIscrizione)));
            String[] rcrd;

            int cfIndex = RelationshipAttributesOrder.getIndexUtente();
            int courseIndex = RelationshipAttributesOrder.getIndexCorso();

            List<String[]> updatedRecords = new ArrayList<>();

            while ((rcrd = csvReader.readNext()) != null) {
                if(rcrd[cfIndex].equals(utente.getCf()) && rcrd[courseIndex].equals(corso.getName())) {
                    isExistingEnrollment = true;
                } else {
                    updatedRecords.add(rcrd);
                }

            }

            if(isExistingEnrollment) {
                csvWriter = new CSVWriter(new BufferedWriter(new FileWriter(this.fileIscrizione, false)),  ICSVWriter.DEFAULT_SEPARATOR,
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

    @Override
    public void addEnrollmentToUtente(Utente utente, Corso corso) {
        CSVWriter csvWriter = null;
        boolean isExistingEnrollment = false;

        List<String> coursesNames = getRelationships(RelationshipAttributesOrder.getIndexUtente(),
                RelationshipAttributesOrder.getIndexCorso(), utente.getCf(), UserRoleInCourse.ENROLLMENT);

        for (String courseName: coursesNames) {
            if(courseName.equals(corso.getName())) {
                isExistingEnrollment = true;
                break;
            }
        }
        if (!isExistingEnrollment) {
            String[] rcrd;
            try {
                csvWriter = new CSVWriter(new BufferedWriter(new FileWriter(this.fileIscrizione, true)), ICSVWriter.DEFAULT_SEPARATOR,
                        ICSVWriter.NO_QUOTE_CHARACTER, ICSVWriter.DEFAULT_ESCAPE_CHARACTER, ICSVWriter.RFC4180_LINE_END);

                rcrd = setRecordFromEnrollment(utente, corso);

                csvWriter.writeNext(rcrd);
                csvWriter.flush();

            } catch (IOException e) {
                LoggerManager.logSevereException("Impossibile scrivere file!", e);
            } finally {
                CSVManager.closeCsvWriter(csvWriter);
            }
        }

    }

    private List<String> getRelationships(int searchingIndex, int resultIndex, String str, UserRoleInCourse uric) {
        List<String> results = new ArrayList<>();
        CSVReader csvReader = null;
        try {
            if(uric.equals(UserRoleInCourse.ENROLLMENT))
                csvReader = new CSVReader(new BufferedReader(new FileReader(this.fileIscrizione)));
            else
                csvReader = new CSVReader(new BufferedReader(new FileReader(this.fileInsegnato)));

            String[] rcrd = {};

            while ((rcrd = csvReader.readNext()) != null) {
                //check if the user exists
                if(rcrd[searchingIndex].equals(str)) {
                    results.add(rcrd[resultIndex]);
                }
            }

        } catch (Exception e) {
            LoggerManager.logSevereException(ConstantMsg.ERROR_OPENING_FILE, e);
            return results;
        } finally {
            CSVManager.closeCsvReader(csvReader);
        }

        return results;
    }

    private String[] setRecordFromEnrollment(Utente utente, Corso corso) {
        String[] rcrd = new String[2];

        rcrd[RelationshipAttributesOrder.getIndexCorso()] = corso.getName();
        rcrd[RelationshipAttributesOrder.getIndexUtente()] = utente.getCf();

        return rcrd;
    }

    private static class RelationshipAttributesOrder {
        public static int getIndexCorso() {
            return 0;
        }
        public static int getIndexUtente() {
            return 1;
        }
    }

}
