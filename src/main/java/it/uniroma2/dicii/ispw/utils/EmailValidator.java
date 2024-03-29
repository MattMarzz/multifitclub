package it.uniroma2.dicii.ispw.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
    private static final String EMAIL_PATTERN =
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);

    private EmailValidator() {}

    public static boolean isNotValid(String email) {
        Matcher matcher = pattern.matcher(email);
        return !matcher.matches();
    }
}
