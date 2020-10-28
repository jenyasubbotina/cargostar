package uz.alexits.cargostar.model.location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import uz.alexits.cargostar.model.packaging.Packaging;
import uz.alexits.cargostar.model.packaging.PackagingType;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

@Entity(tableName = "zone_settings",
        foreignKeys = {
                @ForeignKey(entity = Zone.class, parentColumns = "id", childColumns = "zone_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = PackagingType.class, parentColumns = "id", childColumns = "packaging_type_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = Packaging.class, parentColumns = "id", childColumns = "packaging_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "zone_id"), @Index(value = "packaging_type"), @Index(value = "packaging_id")})
public class ZoneSettings {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @ColumnInfo(name = "id")
    private final long id;

    @SerializedName("zone_id")
    @Expose
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

    @SerializedName("svg_id")
    @Expose
    @ColumnInfo(name = "svg_id")
    private final long svg_id;

    @SerializedName("weight_from")
    @Expose
    @ColumnInfo(name = "weight_from")
    private final int weightFrom;

    @SerializedName("weight_to")
    @Expose
    @ColumnInfo(name = "weight_to")
    private final int weightTo;

    @SerializedName("weight_step")
    @Expose
    @ColumnInfo(name = "weight_step")
    private final int weightStep;

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

    @SerializedName("description_uz")
    @Expose
    @ColumnInfo(name = "description_uz")
    @Nullable
    private String descriptionUz;

    @SerializedName("description_en")
    @Expose
    @ColumnInfo(name = "description_en")
    @Nullable
    private String descriptionEn;

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
                        final long zoneId,
                        final long packagingId,
                        final long packagingTypeId,
                        final long providerId,
                        final long svg_id,
                        final int weightFrom,
                        final int weightTo,
                        final int weightStep,
                        final long priceFrom,
                        final long priceStep,
                        @NonNull final String name,
                        final int status) {
        this.id = id;
        this.zoneId = zoneId;
        this.packagingId = packagingId;
        this.packagingTypeId = packagingTypeId;
        this.providerId = providerId;
        this.svg_id = svg_id;
        this.weightFrom = weightFrom;
        this.weightTo = weightTo;
        this.weightStep = weightStep;
        this.priceFrom = priceFrom;
        this.priceStep = priceStep;
        this.name = name;
        this.status = status;
    }

    public ZoneSettings(final long id,
                        final long zoneId,
                        final long packagingId,
                        final long packagingTypeId,
                        final long providerId,
                        final long svg_id,
                        final int weightFrom,
                        final int weightTo,
                        final int weightStep,
                        final long priceFrom,
                        final long priceStep,
                        @NonNull final String name,
                        @Nullable final String description,
                        @Nullable final String descriptionUz,
                        @Nullable final String descriptionEn,
                        @Nullable final String fuel,
                        final int status,
                        @Nullable final Date createdAt,
                        @Nullable final Date updatedAt) {
        this.id = id;
        this.zoneId = zoneId;
        this.packagingId = packagingId;
        this.packagingTypeId = packagingTypeId;
        this.providerId = providerId;
        this.svg_id = svg_id;
        this.weightFrom = weightFrom;
        this.weightTo = weightTo;
        this.weightStep = weightStep;
        this.priceFrom = priceFrom;
        this.priceStep = priceStep;
        this.name = name;
        this.description = description;
        this.descriptionUz = descriptionUz;
        this.descriptionEn = descriptionEn;
        this.fuel = fuel;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public long getSvg_id() {
        return svg_id;
    }

    public int getWeightFrom() {
        return weightFrom;
    }

    public int getWeightTo() {
        return weightTo;
    }

    public int getWeightStep() {
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
    public String getDescriptionUz() {
        return descriptionUz;
    }

    public void setDescriptionUz(@Nullable String descriptionUz) {
        this.descriptionUz = descriptionUz;
    }

    @Nullable
    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(@Nullable String descriptionEn) {
        this.descriptionEn = descriptionEn;
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

    @Override
    public String toString() {
        return "ZoneSettings{" +
                "id=" + id +
                ", zoneId=" + zoneId +
                ", packagingId=" + packagingId +
                ", packagingTypeId=" + packagingTypeId +
                ", providerId=" + providerId +
                ", svg_id=" + svg_id +
                ", weightFrom=" + weightFrom +
                ", weightTo=" + weightTo +
                ", weightStep=" + weightStep +
                ", priceFrom=" + priceFrom +
                ", priceStep=" + priceStep +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", descriptionUz='" + descriptionUz + '\'' +
                ", descriptionEn='" + descriptionEn + '\'' +
                ", fuel='" + fuel + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
