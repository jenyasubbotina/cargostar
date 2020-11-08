package uz.alexits.cargostar.model.shipping;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cargo {
    @Expose
    @SerializedName("packaging-type")
    private final String packagingType;
    @Expose
    @SerializedName("length")
    private final double length;

    @Expose
    @SerializedName("width")
    private final double width;

    @Expose
    @SerializedName("height")
    private final double height;

    @Expose
    @SerializedName("weight")
    private final double weight;

    public Cargo(String packagingType, double length, double width, double height, double weight) {
        this.packagingType = packagingType;
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
    }

    public String getPackagingType() {
        return packagingType;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }
}
