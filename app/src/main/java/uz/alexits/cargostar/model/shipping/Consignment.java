package uz.alexits.cargostar.model.shipping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import uz.alexits.cargostar.model.location.Region;

@Entity(tableName = "consignment", foreignKeys = {
        @ForeignKey(entity = Request.class, parentColumns = "id", childColumns = "request_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "request_id")})
public class Consignment {
    @Expose
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    private final long id;

    @Expose
    @SerializedName("request_id")
    @ColumnInfo(name = "request_id")
    private final Long requestId;

    @Expose
    @SerializedName("packaging-type")
    @ColumnInfo(name = "packaging_type")
    private final String packagingType;

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
    @ColumnInfo(name = "length", defaultValue = "-1")
    private final double length;

    @Expose
    @SerializedName("width")
    @ColumnInfo(name = "width", defaultValue = "-1")
    private final double width;

    @Expose
    @SerializedName("height")
    @ColumnInfo(name = "height", defaultValue = "-1")
    private final double height;

    @Expose
    @SerializedName("weight")
    @ColumnInfo(name = "weight", defaultValue = "-1")
    private final double weight;

    @Expose
    @SerializedName("dimensions")
    @ColumnInfo(name = "dimensions")
    private final double dimensions;

    @SerializedName("qr")
    @ColumnInfo(name = "qr")
    private String qr;

    public Consignment(final long id,
                       final Long requestId,
                       final String packagingType,
                       final String name,
                       final String description,
                       final String cost,
                       final double length,
                       final double width,
                       final double height,
                       final double weight,
                       final double dimensions,
                       final String qr) {
        this.id = id;
        this.requestId = requestId;
        this.packagingType = packagingType;
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

    public Long getRequestId() {
        return requestId;
    }

    public String getPackagingType() {
        return packagingType;
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

    public double getDimensions() {
        return dimensions;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    @NonNull
    @Override
    public String toString() {
        return "Consignment{" +
                "id=" + id +
                ", requestId=" + requestId +
                ", packagingType='" + packagingType + '\'' +
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
