package it.uniroma2.dicii.ispw.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerManager {
    //specific logger for LoggerManager class
    private static final Logger logger = Logger.getLogger(LoggerManager.class.getName());
    private static final int LEN_BYTE = 10 * 1024 * 1024;
    private LoggerManager(){}

    //need to execute only the very first time
    static {
        try {
            FileHandler fileHandler = new FileHandler("logs/application.log", LEN_BYTE, 0, true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            System.err.println("Errore durante l'inizializzazione del logger.");
            e.printStackTrace(System.err);
        }
    }

    public static void logSevereException(String message, Exception e){
        logger.log(Level.SEVERE, message, e);
    }
    public static void logSevereException(String message){
        logger.log(Level.SEVERE, message);
    }
    public static void logInfoException(String message, Exception e){
        logger.log(Level.INFO, message, e);
    }
}
