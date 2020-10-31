package uz.alexits.cargostar.model.calculation;

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
import java.util.Date;

@Entity(tableName = "zone_settings",
        foreignKeys = {
                @ForeignKey(entity = Zone.class, parentColumns = "id", childColumns = "zone_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = PackagingType.class, parentColumns = "id", childColumns = "packaging_type_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = Packaging.class, parentColumns = "id", childColumns = "packaging_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = Provider.class, parentColumns = "id", childColumns = "provider_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "zone_id"), @Index(value = "packaging_type_id"), @Index(value = "packaging_id"), @Index(value = "provider_id")})
public class ZoneSettings {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @ColumnInfo(name = "id")
    private final long id;

    @SerializedName("zone_id")
    @Expose
    @ColumnInfo(name = "zone_id")
    private final Long zoneId;

    @SerializedName("packaging_id")
    @Expose
    @ColumnInfo(name = "packaging_id")
    private final Long packagingId;

    @SerializedName("packaging_type_id")
    @Expose
    @ColumnInfo(name = "packaging_type_id")
    private final Long packagingTypeId;

    @SerializedName("provider_id")
    @Expose
    @ColumnInfo(name = "provider_id")
    private final Long providerId;

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

    @SerializedName("status")
    @Expose
    @ColumnInfo(name = "status")
    private final int status;

    @SerializedName("created_at")
    @Expose
    @ColumnInfo(name = "created_at")
    @Nullable
    private Date createdAt;

    @SerializedName("updated_at")
    @Expose
    @ColumnInfo(name = "updated_at")
    @Nullable
    private Date updatedAt;

    @Ignore
    public ZoneSettings(final long id,
                        final Long zoneId,
                        final Long packagingId,
                        final Long packagingTypeId,
                        final Long providerId,
                        final double weightFrom,
                        final double weightTo,
                        final double weightStep,
                        final long priceFrom,
                        final long priceStep,
                        @NonNull final String name,
                        final int status) {
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
        this.status = status;
    }

    public ZoneSettings(final long id,
                        final Long zoneId,
                        final Long packagingId,
                        final Long packagingTypeId,
                        final Long providerId,
                        final double weightFrom,
                        final double weightTo,
                        final double weightStep,
                        final long priceFrom,
                        final long priceStep,
                        @NonNull final String name,
                        @Nullable final String description,
                        @Nullable final String fuel,
                        final int status,
                        @Nullable final Date createdAt,
                        @Nullable final Date updatedAt) {
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
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() {
        return id;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public Long getPackagingId() {
        return packagingId;
    }

    public Long getPackagingTypeId() {
        return packagingTypeId;
    }

    public Long getProviderId() {
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

    public int getStatus() {
        return status;
    }

    @Nullable
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@Nullable Date createdAt) {
        this.createdAt = createdAt;
    }

    @Nullable
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(@Nullable Date updatedAt) {
        this.updatedAt = updatedAt;
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
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
