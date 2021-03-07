package uz.alexits.cargostar.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static String dateToStr(final Date date) {
        if (date == null) {
            return null;
        }
        return dateFormatter.format(date);
    }

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
}
