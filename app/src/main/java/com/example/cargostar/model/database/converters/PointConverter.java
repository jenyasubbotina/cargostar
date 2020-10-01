package com.example.cargostar.model.database.converters;

import androidx.room.TypeConverter;

import com.example.cargostar.model.location.Point;

public class PointConverter {
    @TypeConverter
    public static String pointToString(final Point point) {
        return point == null ? null : point.getLongitude() + "." + point.getLatitude();
    }

    @TypeConverter
    public static Point stringToPoint(final String value) {
        if (value == null || !value.contains(".")) {
            return null;
        }
        final int longitude = Integer.parseInt(value.substring(0, value.indexOf(".")));
        final int latitude = Integer.parseInt(value.substring(value.indexOf(".") + 1));
        return new Point(longitude, latitude);
    }
}
