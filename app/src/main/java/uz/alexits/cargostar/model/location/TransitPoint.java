package uz.alexits.cargostar.model.location;

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

@Entity(tableName = "transitPoint",
        foreignKeys = {
        @ForeignKey(entity = Branche.class, parentColumns = "id", childColumns = "branche_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = Country.class, parentColumns = "id", childColumns = "country_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = Region.class, parentColumns = "id", childColumns = "region_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = City.class, parentColumns = "id", childColumns = "city_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "branche_id"), @Index(value = "country_id"), @Index(value = "region_id"), @Index(value = "city_id")})
public class TransitPoint {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @ColumnInfo(name = "id")
    private long id;

    @SerializedName("country_id")
    @Expose
    @ColumnInfo(name = "country_id")
    private final Long countryId;

    @SerializedName("region_id")
    @Expose
    @ColumnInfo(name = "region_id")
    private final Long regionId;

    @SerializedName("city_id")
    @Expose
    @ColumnInfo(name = "city_id")
    private final Long cityId;

    @SerializedName("branche_id")
    @Expose
    @ColumnInfo(name = "branche_id")
    private final Long brancheId;

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
    @Nullable private String geolocation;

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

    public TransitPoint(final long id,
                        final Long countryId,
                        final Long regionId,
                        final Long cityId,
                        final Long brancheId,
                        @NonNull final String name,
                        @NonNull final String address,
                        @Nullable final String geolocation,
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

    public Long getCountryId() {
        return countryId;
    }


    public Long getRegionId() {
        return regionId;
    }


    public Long getCityId() {
        return cityId;
    }


    public Long getBrancheId() {
        return brancheId;
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
    public String getGeolocation() {
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
        return name;
    }
}
