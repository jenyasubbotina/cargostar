package uz.alexits.cargostar.database.converters;

import androidx.room.TypeConverter;

import java.util.Date;

public class DateConverter {
    @TypeConverter
    public static Long dateToLong(final Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Date longToDate(final Long value) {
        return value == null ? null : new Date(value);
    }
}
