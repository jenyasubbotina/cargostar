package uz.alexits.cargostar.entities.calculation;

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

@Entity(tableName = "zone_settings",
        foreignKeys = {
                @ForeignKey(entity = Zone.class, parentColumns = "id", childColumns = "zone_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = Packaging.class, parentColumns = "id", childColumns = "packaging_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),},
        indices = {@Index(value = "zone_id"), @Index(value = "packaging_id")})
public class ZoneSettings {
    @Expose
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    private final long id;

    @Expose
    @SerializedName("zone_id")
    @ColumnInfo(name = "zone_id")
    private final long zoneId;

    @SerializedName("packaging_id")
    @Expose
    @ColumnInfo(name = "packaging_id")
    private final long packagingId;

    @SerializedName("packaging_type_id")
    @Expose
    @ColumnInfo(name = "packaging_type_id")
    private final long packagingTypeId;

    @SerializedName("provider_id")
    @Expose
    @ColumnInfo(name = "provider_id")
    private final long providerId;

    @SerializedName("weight_from")
    @Expose
    @ColumnInfo(name = "weight_from")
    private final double weightFrom;

    @SerializedName("weight_to")
    @Expose
    @ColumnInfo(name = "weight_to")
    private final double weightTo;

    @SerializedName("weight_step")
    @Expose
    @ColumnInfo(name = "weight_step")
    private final double weightStep;

    @SerializedName("price_from")
    @Expose
    @ColumnInfo(name = "price_from")
    private final long priceFrom;

    @SerializedName("price_step")
    @Expose
    @ColumnInfo(name = "price_step")
    private final long priceStep;

    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name")
    @NonNull
    private final String name;

    @SerializedName("description")
    @Expose
    @ColumnInfo(name = "description")
    @Nullable
    private String description;

    @SerializedName("fuel")
    @Expose
    @ColumnInfo(name = "fuel")
    @Nullable
    private String fuel;

    @Ignore
    public ZoneSettings(final long id,
                        final long zoneId,
                        final long packagingId,
                        final long packagingTypeId,
                        final long providerId,
                        final double weightFrom,
                        final double weightTo,
                        final double weightStep,
                        final long priceFrom,
                        final long priceStep,
                        @NonNull final String name) {
        this.id = id;
        this.zoneId = zoneId;
        this.packagingId = packagingId;
        this.packagingTypeId = packagingTypeId;
        this.providerId = providerId;
        this.weightFrom = weightFrom;
        this.weightTo = weightTo;
        this.weightStep = weightStep;
        this.priceFrom = priceFrom;
        this.priceStep = priceStep;
        this.name = name;
    }

    public ZoneSettings(final long id,
                        final long zoneId,
                        final long packagingId,
                        final long packagingTypeId,
                        final long providerId,
                        final double weightFrom,
                        final double weightTo,
                        final double weightStep,
                        final long priceFrom,
                        final long priceStep,
                        @NonNull final String name,
                        @Nullable final String description,
                        @Nullable final String fuel) {
        this.id = id;
        this.zoneId = zoneId;
        this.packagingId = packagingId;
        this.packagingTypeId = packagingTypeId;
        this.providerId = providerId;
        this.weightFrom = weightFrom;
        this.weightTo = weightTo;
        this.weightStep = weightStep;
        this.priceFrom = priceFrom;
        this.priceStep = priceStep;
        this.name = name;
        this.description = description;
        this.fuel = fuel;
    }

    public long getId() {
        return id;
    }

    public long getZoneId() {
        return zoneId;
    }

    public long getPackagingId() {
        return packagingId;
    }

    public long getPackagingTypeId() {
        return packagingTypeId;
    }

    public long getProviderId() {
        return providerId;
    }

    public double getWeightFrom() {
        return weightFrom;
    }

    public double getWeightTo() {
        return weightTo;
    }

    public double getWeightStep() {
        return weightStep;
    }

    public long getPriceFrom() {
        return priceFrom;
    }

    public long getPriceStep() {
        return priceStep;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Nullable
    public String getFuel() {
        return fuel;
    }

    public void setFuel(@Nullable String fuel) {
        this.fuel = fuel;
    }

    @NonNull
    @Override
    public String toString() {
        return "ZoneSettings{" +
                "id=" + id +
                ", zoneId=" + zoneId +
                ", packagingId=" + packagingId +
                ", packagingTypeId=" + packagingTypeId +
                ", providerId=" + providerId +
                ", weightFrom=" + weightFrom +
                ", weightTo=" + weightTo +
                ", weightStep=" + weightStep +
                ", priceFrom=" + priceFrom +
                ", priceStep=" + priceStep +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", fuel='" + fuel + '\'' +
                '}';
    }
}
