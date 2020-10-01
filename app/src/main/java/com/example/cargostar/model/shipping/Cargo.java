package com.example.cargostar.model.shipping;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cargo")
public class Cargo {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") private long id;
    @ColumnInfo(name = "receipt_id") private long receiptId;
    @ColumnInfo(name = "description") @NonNull private String description;
    @ColumnInfo(name = "package_type") @NonNull private String packageType;
    @ColumnInfo(name = "length", defaultValue = "-1") private int length;
    @ColumnInfo(name = "width", defaultValue = "-1") private int width;
    @ColumnInfo(name = "height", defaultValue = "-1") private int height;
    @ColumnInfo(name = "weight", defaultValue = "-1") private int weight;
    @ColumnInfo(name = "qr") private String qr;

    public Cargo(@NonNull final String description,
                 @NonNull final String packageType,
                 final int length,
                 final int width,
                 final int height,
                 final int weight,
                 final String qr) {
        this.description = description;
        this.packageType = packageType;
        this.length = length;
        this.width = width;
        this.height = height;
        this.weight = weight;
        this.qr = qr;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setReceiptId(final long receiptId) {
        this.receiptId = receiptId;
    }

    public long getReceiptId() {
        return receiptId;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull final String description) {
        this.description = description;
    }

    @NonNull
    public String getPackageType() {
        return packageType;
    }

    public void setPackageType(@NonNull final String packageType) {
        this.packageType = packageType;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
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
                ", receiptId='" + receiptId + '\'' +
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
