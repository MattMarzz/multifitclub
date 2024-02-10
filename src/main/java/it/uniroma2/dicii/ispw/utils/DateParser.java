package it.uniroma2.dicii.ispw.utils;

import it.uniroma2.dicii.ispw.exception.InvalidDataException;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser {
    private DateParser() {}

    public static Date parseStringToDateUtil(String str) throws InvalidDataException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date  = sdf.parse(str);
        } catch (ParseException e) {
            LoggerManager.logSevereException("Errore di conversione da stringa a Date: ", e);
            throw new InvalidDataException("Formato data non valida.");
        }
        return date;
    }

    public static String parseDateToString(Date date)  {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static Time parseStringToTime(String str) throws InvalidDataException {
        Time time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            Date date = sdf.parse(str);
            long milliseconds = date.getTime();
            time = new Time(milliseconds);
        } catch (ParseException e) {
            throw new InvalidDataException("Formato orario non valido.");
        }
        return time;
    }

    public static String parseTimeToString(Time time)  {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(time);
    }
}
