package it.uniroma2.dicii.ispw.model.lezione.dao;

import it.uniroma2.dicii.ispw.App;
import it.uniroma2.dicii.ispw.model.lezione.Lezione;
import it.uniroma2.dicii.ispw.utils.DateParser;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


/*
 * Attention: Before running the tests, ensure that the directory for the CSV files
 * has been correctly set and the appropriate level of persistence is configured
 * in the application.properties file.
 */
class TestLezioneFS {

    @Test
    void testRightGetLezioniByCourseId() throws Exception {
        //need to specify the persistence layer choose
        startTheAppForPersistenceLayer();

        LezioneFS lezioneFS = new LezioneFS();
        String inputCourse = "Pilates";
        List<Lezione> expectedList = new ArrayList<>();
        Lezione l1 = new Lezione("Lunedì", DateParser.parseStringToTime("16:30:00"), "A", "Pilates", "");
        Lezione l2 = new Lezione("Mercoledì", DateParser.parseStringToTime("16:30:00"), "A", "Pilates", "");
        Lezione l3 = new Lezione("Venerdì", DateParser.parseStringToTime("16:30:00"), "A", "Pilates", "");
        expectedList.add(l1);
        expectedList.add(l2);
        expectedList.add(l3);

        List<Lezione> actualList = lezioneFS.getLezioniByCourseId(inputCourse);
        actualList = removeDaoAddress(actualList);

        assertEquals(expectedList, actualList);
    }

    @Test
    void testWrongGetLezioniByCourseId() throws Exception {
        //need to specify the persistence layer choose
        startTheAppForPersistenceLayer();

        LezioneFS lezioneFS = new LezioneFS();
        String inputCourse = "Pilates";
        List<Lezione> wrongList = new ArrayList<>();
        Lezione l1 = new Lezione("Mercoledì", DateParser.parseStringToTime("16:30:00"), "A", "Pilates", "");
        Lezione l2 = new Lezione("Venerdì", DateParser.parseStringToTime("16:30:00"), "A", "Pilates", "");
        wrongList.add(l1);
        wrongList.add(l2);

        List<Lezione> actualList = lezioneFS.getLezioniByCourseId(inputCourse);
        actualList = removeDaoAddress(actualList);

        assertNotEquals(wrongList, actualList);
    }

    @Test
    void testEmptyGetLezioniByCourseId() throws Exception {
        //need to specify the persistence layer choose
        startTheAppForPersistenceLayer();

        LezioneFS lezioneFS = new LezioneFS();
        String inputCourse = "Corso Non Esistente";
        List<Lezione> emptyList = new ArrayList<>();
        List<Lezione> actualList = lezioneFS.getLezioniByCourseId(inputCourse);
        actualList = removeDaoAddress(actualList);

        assertEquals(emptyList, actualList);
    }

    @Test
    void testRightGetAllLezioni() throws Exception {
        //need to specify the persistence layer choose
        startTheAppForPersistenceLayer();
        LezioneFS lezioneFS = new LezioneFS();
        List<Lezione> expectedList = new ArrayList<>();
        Lezione l1 = new Lezione("Lunedì", DateParser.parseStringToTime("16:30:00"), "A", "Pilates", "");
        Lezione l2 = new Lezione("Mercoledì", DateParser.parseStringToTime("16:30:00"), "A", "Pilates", "");
        Lezione l3 = new Lezione("Venerdì", DateParser.parseStringToTime("16:30:00"), "A", "Pilates", "");
        Lezione l4 = new Lezione("Martedì", DateParser.parseStringToTime("18:00:00"), "B", "Karate", "");
        Lezione l5 = new Lezione("Giovedì", DateParser.parseStringToTime("18:00:00"), "B", "Karate", "");
        expectedList.add(l1);
        expectedList.add(l2);
        expectedList.add(l3);
        expectedList.add(l4);
        expectedList.add(l5);

        List<Lezione> actualList = lezioneFS.getAllLezioni();
        actualList = removeDaoAddress(actualList);

        assertEquals(expectedList, actualList);
    }

    List<Lezione> removeDaoAddress(List<Lezione> actualList) throws IllegalAccessException, NoSuchFieldException {
        for (Lezione l: actualList) {
            //remove the dao address

            Field field = Lezione.class.getDeclaredField("lezioneDAO");
            field.setAccessible(true);
            field.set(l, null);
        }

        return actualList;
    }

    void startTheAppForPersistenceLayer() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        App application = new App();
        Method prvPersistenceLayer = App.class.getDeclaredMethod("setPersistenceLayer");
        prvPersistenceLayer.setAccessible(true);
        prvPersistenceLayer.invoke(application);
    }
}
