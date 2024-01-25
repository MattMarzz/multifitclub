package it.uniroma2.dicii.ispw.utils;

import it.uniroma2.dicii.ispw.exception.InvalidDataException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser {
    public static Date parseStringToDateUtil(String str) throws InvalidDataException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date  = sdf.parse(str);
        } catch (ParseException e) {
            LoggerManager.logSevereException("Errore di conversione da stringa a Date: ", e);
            throw new InvalidDataException("Formato data non valida.");
        }
        return date;
    }
}
