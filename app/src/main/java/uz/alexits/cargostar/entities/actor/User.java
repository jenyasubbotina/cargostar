package uz.alexits.cargostar.entities.actor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import uz.alexits.cargostar.entities.location.Country;

@Entity(tableName = "user",
        foreignKeys = {
        @ForeignKey(entity = Country.class, parentColumns = "id", childColumns = "country_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)},
        indices = {
        @Index(value = {"country_id"}), @Index(value = "email")})
public abstract class User {
    @Expose
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    protected final long id;

    @Expose
    @SerializedName("user_id")
    @ColumnInfo(name = "user_id")
    protected final long userId;

    @Expose
    @SerializedName("country_id")
    @ColumnInfo(name = "country_id")
    protected final long countryId;

    @Expose
    @SerializedName("city_name")
    @ColumnInfo(name = "city_name")
    protected final String cityName;

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
    @SerializedName("email")
    @ColumnInfo(name = "email")
    @NonNull protected String email;

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

    public User(final long id,
                final long userId,
                final long countryId,
                final String cityName,
                @NonNull final String firstName,
                @Nullable final String middleName,
                @NonNull final String lastName,
                @NonNull final String phone,
                @NonNull final String email,
                @Nullable final String address,
                @Nullable final String geo,
                @Nullable final String zip) {
        this.id = id;
        this.userId = userId;
        this.countryId = countryId;
        this.cityName = cityName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.geo = geo;
        this.zip = zip;
    }


    @NonNull
    public String getEmail() {
        return email;
    }

    public long getId() {
        return id;
    }

    public long getCountryId() {
        return countryId;
    }

    public String getCityName() {
        return cityName;
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

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public long getUserId() {
        return userId;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userId=" + userId +
                ", countryId=" + countryId +
                ", cityId=" + cityName +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", geo='" + geo + '\'' +
                ", zip='" + zip + '\'' +
                '}';
    }
}
