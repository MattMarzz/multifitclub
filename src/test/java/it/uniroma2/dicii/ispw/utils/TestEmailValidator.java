package it.uniroma2.dicii.ispw.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestEmailValidator {

    @Test
    void testValidateWrongEmail() {
        String notValidEmail = "casualstring@";
        boolean expected = true;
        boolean actual = EmailValidator.isNotValid(notValidEmail);

        assertEquals(expected, actual);
    }


    @Test
    void testValidateRightEmail() {
        String validEmail = "pippo@gmail.com";
        boolean expected = false;
        boolean actual = EmailValidator.isNotValid(validEmail);

        assertEquals(expected, actual);
    }

}
