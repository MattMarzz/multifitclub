package it.uniroma2.dicii.ispw.controllers;

import it.uniroma2.dicii.ispw.App;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestLoginController {
    @Test
    public void TestValidateWrongEmail() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String notValidEmail = "casualstring@";

        //need to specify the persistence layer choose
        startTheAppForPersistenceLayer();

        LoginController loginController = new LoginController();
        Method privateMethod = LoginController.class.getDeclaredMethod("validate", String.class);
        privateMethod.setAccessible(true);

        boolean out = (boolean) privateMethod.invoke(loginController, notValidEmail);
        assertEquals(false, out);
    }

    @Test
    public void TestValidateRightEmail() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        String validEmail = "pippo@gmail.com";

        //need to specify the persistence layer choose
        startTheAppForPersistenceLayer();

        LoginController loginController = new LoginController();
        Method privateMethod = LoginController.class.getDeclaredMethod("validate", String.class);
        privateMethod.setAccessible(true);

        boolean out = (boolean) privateMethod.invoke(loginController, validEmail);
        assertEquals(true, out);

    }

    public void startTheAppForPersistenceLayer() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        App application = new App();
        Method prvPersistenceLayer = App.class.getDeclaredMethod("setPersistenceLayer");
        prvPersistenceLayer.setAccessible(true);
        prvPersistenceLayer.invoke(application);
    }

}