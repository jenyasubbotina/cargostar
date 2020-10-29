package uz.alexits.cargostar.utils;

import java.util.regex.Pattern;

public class Regex {
    //1 word, letters only
    private static final String NAME_PATTERN = "[A-Za-z]+";
    //first +, digits only
    private static final String PHONE_NUMBER_PATTERN = "^(+??)\\d";
    //email pattern from Inet
    private static final String EMAIL_PATTERN = "\"^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$";
    //todo
    private static final String PASSWORD_PATTERN = "";
    //digits only
    private static final String ZIP_PATTERN = "\\d+";
    //todo: change pattern
    private static final String GEOLOCATION_PATTERN = "\\d{2}.\\d{2}";
    //1 word, digits and letters
    private static final String ACCOUNT_NUMBER_PATTERN = "\\w+";
    //1 word, letters only in English
    private static final String LOCATION_PATTERN = "[A-Za-z]+";

    public static boolean isPhoneNumber(final String phoneNumber) {
        return android.util.Patterns.PHONE.matcher(phoneNumber).matches();
    }

    public static boolean isEmail(final String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isGeolocation(final String geolocation) {
        return Pattern.compile(GEOLOCATION_PATTERN).matcher(geolocation).matches();
    }

    public static boolean isAccountNumber(final String accountNumber) {
        return Pattern.compile(ACCOUNT_NUMBER_PATTERN).matcher(accountNumber).matches();
    }

    public static boolean isName(final String name) {
        return Pattern.compile(NAME_PATTERN).matcher(name).matches();
    }

    public static boolean isLocation(final String location) {
        return Pattern.compile(LOCATION_PATTERN).matcher(location).matches();
    }

    public static boolean isZip(final String zip) {
        return Pattern.compile(ZIP_PATTERN).matcher(zip).matches();
    }
}
