package com.example.cargostar.model.location;

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

@Entity(tableName = "transit_point",
        foreignKeys = {@ForeignKey(entity = Branche.class,
                parentColumns = "id",
                childColumns = "branche_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "branche_id")})
public class TransitPoint {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @ColumnInfo(name = "id")
    private long id;

    @SerializedName("country_id")
    @Expose
    @ColumnInfo(name = "country_id")
    private long countryId;

    @SerializedName("region_id")
    @Expose
    @ColumnInfo(name = "region_id")
    private long regionId;

    @SerializedName("city_id")
    @Expose
    @ColumnInfo(name = "city_id")
    private long cityId;

    @SerializedName("branche_id")
    @Expose
    @ColumnInfo(name = "branche_id")
    private long brancheId;

    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name")
    @NonNull private final String name;

    @SerializedName("address")
    @Expose
    @ColumnInfo(name = "address")
    @NonNull private final String address;

    @SerializedName("geo")
    @Expose
    @ColumnInfo(name = "geolocation")
    @Nullable private Point geolocation;

    @SerializedName("status")
    @Expose
    @ColumnInfo(name = "status")
    private final int status;

    @SerializedName("create_at")
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
    public TransitPoint(final long id,
                        final long countryId,
                        final long regionId,
                        final long cityId,
                        final long brancheId,
                        @NonNull final String name,
                        @NonNull final String address,
                        final int status) {
        this.id = id;
        this.countryId = countryId;
        this.regionId = regionId;
        this.cityId = cityId;
        this.brancheId = brancheId;
        this.name = name;
        this.address = address;
        this.status = status;
    }

    public TransitPoint(final long id,
                        final long countryId,
                        final long regionId,
                        final long cityId,
                        final long brancheId,
                        @NonNull final String name,
                        @NonNull final String address,
                        @Nullable final Point geolocation,
                        final int status,
                        @Nullable final Date createdAt,
                        @Nullable final Date updatedAt) {
        this.id = id;
        this.countryId = countryId;
        this.regionId = regionId;
        this.cityId = cityId;
        this.brancheId = brancheId;
        this.name = name;
        this.address = address;
        this.geolocation = geolocation;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCountryId() {
        return countryId;
    }

    public void setCountryId(long countryId) {
        this.countryId = countryId;
    }

    public long getRegionId() {
        return regionId;
    }

    public void setRegionId(long regionId) {
        this.regionId = regionId;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public long getBrancheId() {
        return brancheId;
    }

    public void setBrancheId(long brancheId) {
        this.brancheId = brancheId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    @Nullable
    public Point getGeolocation() {
        return geolocation;
    }

    public int getStatus() {
        return status;
    }

    @Nullable
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@NonNull Date createdAt) {
        this.createdAt = createdAt;
    }

    @Nullable
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(@NonNull Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
