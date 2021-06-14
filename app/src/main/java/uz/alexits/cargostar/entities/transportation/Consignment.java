package uz.alexits.cargostar.entities.transportation;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "consignment")
public class Consignment {
    @Expose
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    private final long id;

    @Expose
    @SerializedName("request_id")
    @ColumnInfo(name = "request_id")
    private final long requestId;

    @Expose
    @SerializedName("packaging_id")
    @ColumnInfo(name = "packaging_id")
    private final long packagingId;

    @Expose
    @SerializedName("pack")
    @ColumnInfo(name = "dimensions")
    private final String dimensions;

    @Expose
    @SerializedName("name")
    @ColumnInfo(name = "name")
    private final String name;

    @Expose
    @SerializedName("description")
    @ColumnInfo(name = "description")
    private final String description;

    @Expose
    @SerializedName("cost")
    @ColumnInfo(name = "cost")
    private final String cost;

    @Expose
    @SerializedName("length")
    @ColumnInfo(name = "length", defaultValue = "0")
    private final double length;

    @Expose
    @SerializedName("width")
    @ColumnInfo(name = "width", defaultValue = "0")
    private final double width;

    @Expose
    @SerializedName("height")
    @ColumnInfo(name = "height", defaultValue = "0")
    private final double height;

    @Expose
    @SerializedName("weight")
    @ColumnInfo(name = "weight", defaultValue = "0")
    private final double weight;

    @SerializedName("qr")
    @ColumnInfo(name = "qr")
    private String qr;

    public Consignment(final long id,
                       final long requestId,
                       final long packagingId,
                       final String name,
                       final String description,
                       final String cost,
                       final double length,
                       final double width,
                       final double height,
                       final double weight,
                       final String dimensions,
                       final String qr) {
        this.id = id;
        this.requestId = requestId;
        this.packagingId = packagingId;
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
        this.dimensions = dimensions;
        this.qr = qr;
    }

    public long getId() {
        return id;
    }

    public long getRequestId() {
        return requestId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCost() {
        return cost;
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

    public String getDimensions() {
        return dimensions;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    public long getPackagingId() {
        return packagingId;
    }

    @NonNull
    @Override
    public String toString() {
        return "Consignment{" +
                "id=" + id +
                ", requestId=" + requestId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", cost=" + cost +
                ", length=" + length +
                ", width=" + width +
                ", height=" + height +
                ", weight=" + weight +
                ", dimensions=" + dimensions +
                ", qr='" + qr + '\'' +
                '}';
    }
}
