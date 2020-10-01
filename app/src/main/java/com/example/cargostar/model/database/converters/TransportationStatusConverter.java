package com.example.cargostar.model.database.converters;

import androidx.room.TypeConverter;

import com.example.cargostar.model.TransportationStatus;
import com.example.cargostar.model.location.Point;

public class TransportationStatusConverter {
    @TypeConverter
    public static int transportationStatusToInt(final TransportationStatus status) {
        if (status == TransportationStatus.IN_TRANSIT) {
            return 0;
        }
        if (status == TransportationStatus.ON_THE_WAY) {
            return 1;
        }
        if (status == TransportationStatus.DELIVERED) {
            return 2;
        }
        if (status == TransportationStatus.LOST) {
            return 3;
        }
        return -1;
    }

    @TypeConverter
    public static TransportationStatus intToTransportationStatus(final int value) {
        if (value == 0) {
            return TransportationStatus.IN_TRANSIT;
        }
        if (value == 1) {
            return TransportationStatus.ON_THE_WAY;
        }
        if (value == 2) {
            return TransportationStatus.DELIVERED;
        }
        if (value == 3) {
            return TransportationStatus.LOST;
        }
        return null;
    }
}
