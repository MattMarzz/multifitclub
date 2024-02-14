package it.uniroma2.dicii.ispw.model.lezione;

import it.uniroma2.dicii.ispw.App;
import it.uniroma2.dicii.ispw.utils.DateParser;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;


/*
 * Attention: Before running the tests, ensure that the directory for the CSV files
 * has been correctly set and the appropriate level of persistence is configured
 * in the application.properties file.
 */
class TestLezione {

    @Test
    void testAffirmativeIsThereOverlap() throws Exception{
        startTheAppForPersistenceLayer();
        Lezione lezione = new Lezione();
        lezione.setSala("A");
        lezione.setCourseName("Nuovo Corso");
        lezione.setStartTime(DateParser.parseStringToTime("17:00:00"));
        lezione.setDay("Lunedì");

        boolean expected = true;
        boolean actual = lezione.isThereOverlap();

        assertEquals(expected, actual);
    }

    @Test
    void testNegativeIsThereOverlap() throws Exception{
        startTheAppForPersistenceLayer();
        Lezione lezione = new Lezione();
        lezione.setSala("A");
        lezione.setCourseName("Nuovo Corso");
        lezione.setStartTime(DateParser.parseStringToTime("17:30:00"));
        lezione.setDay("Lunedì");

        boolean expected = false;
        boolean actual = lezione.isThereOverlap();

        assertEquals(expected, actual);
    }

    void startTheAppForPersistenceLayer() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        App application = new App();
        Method prvPersistenceLayer = App.class.getDeclaredMethod("setPersistenceLayerAndUi");
        prvPersistenceLayer.setAccessible(true);
        prvPersistenceLayer.invoke(application);
    }
}
