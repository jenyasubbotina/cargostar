package uz.alexits.cargostar.view.adapter;

import androidx.annotation.NonNull;

public class ParcelData {
    public static final int TYPE_HEADING = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_STROKE = 2;

    public String key;
    public String value;
    public int type;
    public boolean isHidden;

    public ParcelData(String key, String value, int type) {
        this.key = key;
        this.value = value;
        this.type = type;
        this.isHidden = false;
    }

    @NonNull
    @Override
    public String toString() {
        return "ParcelData{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", type=" + type +
                ", isHidden=" + isHidden +
                '}';
    }
}