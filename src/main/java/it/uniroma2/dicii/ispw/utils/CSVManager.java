package it.uniroma2.dicii.ispw.utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import it.uniroma2.dicii.ispw.exception.ItemNotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CSVManager {
    private CSVManager(){}

    public static String getCsvDir() {
        try (InputStream input = DbConnection.class.getClassLoader().getResourceAsStream("application.properties")) {
            if(input == null) throw new ItemNotFoundException("Directory base non trovata");

            Properties properties = new Properties();
            properties.load(input);
            return properties.getProperty("csv.dir");

        } catch (IOException | ItemNotFoundException e) {
            LoggerManager.logSevereException("Impossibile trovare directory csv", e);
        }
        return null;
    }

    public static void closeCsvReader(CSVReader csvReader) {
        if (csvReader != null) {
            try {
                csvReader.close();
            } catch(IOException e) {
                LoggerManager.logSevereException(ConstantMsg.ERROR_CLOSING_FILE, e);
            }
        }
    }

    public static void closeCsvWriter(CSVWriter csvWriter) {
        if (csvWriter != null) {
            try {
                csvWriter.close();
            } catch(IOException e) {
                LoggerManager.logSevereException(ConstantMsg.ERROR_CLOSING_FILE, e);
            }
        }
    }
}
