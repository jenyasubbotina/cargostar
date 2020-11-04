package uz.alexits.cargostar.model.shipping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "cargo")
public class Cargo {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") private long id;
    @ColumnInfo(name = "invoice_id") private long invoiceId;

    @SerializedName("description")
    @ColumnInfo(name = "description")
    @Nullable private String description;

    @SerializedName("packaging_type")
    @ColumnInfo(name = "package_type")
    @Nullable private String packageType;

    @SerializedName("length")
    @ColumnInfo(name = "length", defaultValue = "-1")
    private double length;

    @SerializedName("width")
    @ColumnInfo(name = "width", defaultValue = "-1")
    private double width;

    @SerializedName("height")
    @ColumnInfo(name = "height", defaultValue = "-1")
    private double height;

    @SerializedName("weight")
    @ColumnInfo(name = "weight", defaultValue = "-1")
    private double weight;

    @SerializedName("qr")
    @ColumnInfo(name = "qr")
    @Nullable private String qr;

    public Cargo(@Nullable final String description,
                 @NonNull final String packageType,
                 final double length,
                 final double width,
                 final double height,
                 final double weight,
                 @Nullable final String qr) {
        this.description = description;
        this.packageType = packageType;
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
        this.qr = qr;
    }

    @Ignore
    public Cargo(@Nullable final String description,
                 final double length,
                 final double width,
                 final double height,
                 final double weight) {
        this.description = description;
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
    }

    @Ignore
    public Cargo(final double length,
                 final double width,
                 final double height,
                 final double weight) {
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setInvoiceId(final long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public long getInvoiceId() {
        return invoiceId;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull final String description) {
        this.description = description;
    }

    @Nullable
    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(@NonNull final String packageType) {
        this.packageType = packageType;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(final String qr) {
        this.qr = qr;
    }

    @NonNull
    @Override
    public String toString() {
        return "Cargo{" +
                "id=" + id +
                ", receiptId='" + invoiceId + '\'' +
                ", description='" + description + '\'' +
                ", packageType='" + packageType + '\'' +
                ", length=" + length +
                ", width=" + width +
                ", height=" + height +
                ", weight=" + weight +
                ", qr='" + qr + '\'' +
                '}';
    }
}
