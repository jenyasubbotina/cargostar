package uz.alexits.cargostar.utils;

import java.util.regex.Pattern;

public class Regex {
    private static final String NAME_PATTERN = "";
    private static final String PHONE_NUMBER_PATTERN = "";
    private static final String EMAIL_PATTERN = "";
    private static final String PASSWORD_PATTERN = "";
    private static final String POST_INDEX_PATTERN = "";
    private static final String GEOLOCATION_PATTERN = "\\d{2}.\\d{2}";

    public static boolean isPhoneNumber(final String phoneNumber) {
        return android.util.Patterns.PHONE.matcher(phoneNumber).matches();
    }

    public static boolean isEmail(final String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isGeolocation(final String geolocation) {
        return Pattern.compile(GEOLOCATION_PATTERN).matcher(geolocation).matches();
    }
}
