package uz.alexits.cargostar.api.params;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class CargoListParams {
    @SerializedName("description")
    private final String description;

    @SerializedName("packaging-type")
    private final String packagingType;

    @SerializedName("weight")
    private final String weight;

    @SerializedName("length")
    private final String length;

    @SerializedName("width")
    private final String width;

    @SerializedName("height")
    private final String height;

    @SerializedName("qr")
    private final String qr;

    public CargoListParams(final String description,
                           final String packagingType,
                           final String weight,
                           final String length,
                           final String width,
                           final String height,
                           final String qr) {
        this.description = description;
        this.packagingType = packagingType;
        this.weight = weight;
        this.length = length;
        this.width = width;
        this.height = height;
        this.qr = qr;
    }

    public String getDescription() {
        return description;
    }

    public String getPackagingType() {
        return packagingType;
    }

    public String getWeight() {
        return weight;
    }

    public String getLength() {
        return length;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }

    public String getQr() {
        return qr;
    }

    @NonNull
    @Override
    public String toString() {
        return "CargoListParams{" +
                "description='" + description + '\'' +
                ", packagingType='" + packagingType + '\'' +
                ", weight='" + weight + '\'' +
                ", length='" + length + '\'' +
                ", width='" + width + '\'' +
                ", height='" + height + '\'' +
                ", qr='" + qr + '\'' +
                '}';
    }
}
