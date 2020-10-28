package uz.alexits.cargostar.model.location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "branche",
        foreignKeys = {
        @ForeignKey(entity = Country.class, parentColumns = "id", childColumns = "country_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
        @ForeignKey(entity = Region.class, parentColumns = "id", childColumns = "region_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
        @ForeignKey(entity = City.class, parentColumns = "id", childColumns = "city_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "country_id"), @Index(value = "region_id"), @Index(value = "city_id")})
public class Branche {
    @Expose
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    private final long id;

    @Expose
    @SerializedName("country_id")
    @ColumnInfo(name = "country_id")
    private final long countryId;

    @Expose
    @SerializedName("region_id")
    @ColumnInfo(name = "region_id")
    private final long regionId;

    @Expose
    @SerializedName("city_id")
    @ColumnInfo(name = "city_id")
    private final long cityId;

    @Expose
    @SerializedName("name")
    @ColumnInfo(name = "name")
    @NonNull private final String name;

    @Expose
    @SerializedName("address")
    @ColumnInfo(name = "address")
    @Nullable private String address;

    @Expose
    @SerializedName("zip")
    @ColumnInfo(name = "zip")
    @Nullable private String zip;

    @Expose
    @SerializedName("geolocation")
    @ColumnInfo(name = "geolocation")
    @Nullable private String geolocation;

    @Expose
    @SerializedName("telephone")
    @ColumnInfo(name = "telephone")
    @Nullable private String phone;

    @Expose
    @SerializedName("status")
    @ColumnInfo(name = "status")
    private int status;

    @Expose
    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    @Nullable private Date createdAt;

    @Expose
    @SerializedName("updated_at")
    @ColumnInfo(name = "updated_at")
    @Nullable private Date updatedAt;

    public Branche(final long id,
                   final long countryId,
                   final long regionId,
                   final long cityId,
                   @NonNull final String name,
                   @Nullable final String address,
                   @Nullable final String zip,
                   @Nullable final String geolocation,
                   @Nullable final String phone,
                   final int status,
                   @Nullable final Date createdAt,
                   @Nullable final Date updatedAt) {
        this.id = id;
        this.countryId = countryId;
        this.regionId = regionId;
        this.cityId = cityId;
        this.name = name;
        this.address = address;
        this.zip = zip;
        this.geolocation = geolocation;
        this.phone = phone;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() {
        return id;
    }

    public long getCountryId() {
        return countryId;
    }

    public long getRegionId() {
        return regionId;
    }

    public long getCityId() {
        return cityId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public String getAddress() {
        return address;
    }

    public void setAddress(@Nullable String address) {
        this.address = address;
    }

    @Nullable
    public String getZip() {
        return zip;
    }

    public void setZip(@Nullable String zip) {
        this.zip = zip;
    }

    @Nullable
    public String getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(@Nullable String geolocation) {
        this.geolocation = geolocation;
    }

    @Nullable
    public String getPhone() {
        return phone;
    }

    public void setPhone(@Nullable String phone) {
        this.phone = phone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
        return name;
    }
}
