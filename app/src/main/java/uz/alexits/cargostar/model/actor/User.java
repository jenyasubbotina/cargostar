package uz.alexits.cargostar.model.actor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "user",
        foreignKeys = {
//        @ForeignKey(entity = Country.class, parentColumns = "id", childColumns = "country_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
//        @ForeignKey(entity = Region.class, parentColumns = "id", childColumns = "region_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
//        @ForeignKey(entity = City.class, parentColumns = "id", childColumns = "city_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
        },
        indices = {
//        @Index(value = {"country_id"}), @Index(value = {"region_id"}), @Index(value = {"city_id"}),
                @Index(value = {"login"}, unique = true)})
public abstract class User {
    @Expose
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    protected final long id;

    @Expose
    @SerializedName("country_id")
    @ColumnInfo(name = "country_id")
    protected final long countryId;

    @Expose
    @SerializedName("region_id")
    @ColumnInfo(name = "region_id")
    protected final long regionId;

    @Expose
    @SerializedName("city_id")
    @ColumnInfo(name = "city_id")
    protected final long cityId;

    @Expose
    @SerializedName("firstname")
    @ColumnInfo(name = "first_name")
    @NonNull protected final String firstName;

    @Expose
    @SerializedName("middlename")
    @ColumnInfo(name = "middle_name")
    @Nullable protected final String middleName;

    @Expose
    @SerializedName("lastname")
    @ColumnInfo(name = "last_name")
    @NonNull protected final String lastName;

    @Expose
    @SerializedName("telephone")
    @ColumnInfo(name = "phone")
    @NonNull protected final String phone;

    @Expose
    @SerializedName("address")
    @ColumnInfo(name = "address")
    @Nullable protected String address;

    @Expose
    @SerializedName("geo")
    @ColumnInfo(name = "geo")
    @Nullable protected String geo;

    @Expose
    @SerializedName("zip")
    @ColumnInfo(name = "zip")
    @Nullable protected String zip;

    @Expose
    @SerializedName("status")
    @ColumnInfo(name = "status")
    protected int status;

    @Expose
    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    @Nullable protected Date createdAt;

    @Expose
    @SerializedName("updated_at")
    @ColumnInfo(name = "updated_at")
    @Nullable protected Date updatedAt;

    @Embedded
    @NonNull protected final Account account;

    public User(final long id,
                final long countryId,
                final long regionId,
                final long cityId,
                @NonNull final String firstName,
                @Nullable final String middleName,
                @NonNull final String lastName,
                @NonNull final String phone,
                @Nullable final String address,
                @Nullable final String geo,
                @Nullable final String zip,
                final int status,
                @Nullable final Date createdAt,
                @Nullable final Date updatedAt,
                @NonNull final Account account) {
        this.id = id;
        this.countryId = countryId;
        this.regionId = regionId;
        this.cityId = cityId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.geo = geo;
        this.zip = zip;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.account = account;
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
    public String getFirstName() {
        return firstName;
    }

    @Nullable
    public String getMiddleName() {
        return middleName;
    }

    @NonNull
    public String getLastName() {
        return lastName;
    }

    @NonNull
    public String getPhone() {
        return phone;
    }

    @Nullable
    public String getAddress() {
        return address;
    }

    public void setAddress(@Nullable final String address) {
        this.address = address;
    }

    @Nullable
    public String getGeo() {
        return geo;
    }

    public void setGeo(@Nullable final String geo) {
        this.geo = geo;
    }

    @Nullable
    public String getZip() {
        return zip;
    }

    public void setZip(@Nullable final String zip) {
        this.zip = zip;
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

    public void setCreatedAt(@Nullable final Date createdAt) {
        this.createdAt = createdAt;
    }

    @Nullable
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(@Nullable final Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @NonNull
    public Account getAccount() {
        return account;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", countryId=" + countryId +
                ", regionId=" + regionId +
                ", cityId=" + cityId +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", geo='" + geo + '\'' +
                ", zip='" + zip + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", account=" + account +
                '}';
    }
}
