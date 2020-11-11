package uz.alexits.cargostar.model.calculation;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "vat")
public class Vat {
    @Expose
    @SerializedName("nds")
    @PrimaryKey
    @ColumnInfo(name = "vat")
    private final double vat;

    public Vat(double vat) {
        this.vat = vat;
    }

    public double getVat() {
        return vat;
    }

    @NonNull
    @Override
    public String toString() {
        return "Vat{" +
                "vat=" + vat +
                '}';
    }
}
