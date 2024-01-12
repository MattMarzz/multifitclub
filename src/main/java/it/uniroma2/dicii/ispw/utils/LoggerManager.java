package it.uniroma2.dicii.ispw.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerManager {
    //specific logger for LoggerManager class
    private static final Logger logger = Logger.getLogger(LoggerManager.class.getName());
    private LoggerManager(){}

    //need to execute only the very first time
    static {
        try {
            FileHandler fileHandler = new FileHandler("logs/application.log");
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void logSevereException(String message, Exception e){
        logger.log(Level.SEVERE, message, e);
    }

    public static void logInfoException(String message, Exception e){
        logger.log(Level.INFO, message, e);
    }
}
