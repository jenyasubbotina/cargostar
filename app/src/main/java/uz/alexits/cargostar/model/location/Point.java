package uz.alexits.cargostar.model.location;

import androidx.annotation.NonNull;

public class Point {
    private final int latitude;
    private final int longitude;

    public Point(final int longitude, final int latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Point(final String value) {
        this.longitude = Integer.parseInt(value.substring(0, value.indexOf(".")));
        this.latitude = Integer.parseInt(value.substring(value.indexOf(".") + 1));
    }

    public int getLatitude() {
        return latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    @NonNull
    @Override
    public String toString() {
        return longitude + "." + latitude;
    }
}
