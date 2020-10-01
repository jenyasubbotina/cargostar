package com.example.cargostar.model.database.converters;

import androidx.room.TypeConverter;
import com.example.cargostar.model.location.Point;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
