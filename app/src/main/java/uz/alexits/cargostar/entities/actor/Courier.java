package uz.alexits.cargostar.entities.actor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import uz.alexits.cargostar.entities.location.Branche;

@Entity(tableName = "courier", inheritSuperIndices = true,
        foreignKeys = {
        @ForeignKey(entity = Branche.class, parentColumns = "id", childColumns = "branche_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "branche_id")})
public class Courier extends User {
    @Expose
    @SerializedName("username")
    @ColumnInfo(name = "login")
    @NonNull protected String login;

    @Expose
    @SerializedName("password")
    @ColumnInfo(name = "password")
    @Nullable protected String password;

    @Expose
    @SerializedName("branche_id")
    @ColumnInfo(name = "branche_id")
    private long brancheId;

    @ColumnInfo(name = "position")
    @NonNull private String position;

    @ColumnInfo(name = "photo_url")
    @Nullable private String photoUrl;

    public Courier(final long id,
                   final long userId,
                   final long countryId,
                   final String cityName,
                   @NonNull final String firstName,
                   @NonNull final String middleName,
                   @NonNull final String lastName,
                   @NonNull final String phone,
                   @NonNull final String email,
                   @Nullable final String address,
                   @Nullable final String geo,
                   @Nullable final String zip,
                   final long brancheId,
                   @NonNull final String login,
                   @Nullable final String photoUrl) {
        super(id,
                userId,
                countryId,
                cityName,
                firstName,
                middleName,
                lastName,
                phone,
                email,
                address,
                geo,
                zip);
        this.brancheId = brancheId;
        this.position = "Курьер";
        this.photoUrl = photoUrl;
        this.login = login;
    }

    public void setPhotoUrl(@Nullable String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Nullable
    public String getPhotoUrl() {
        return photoUrl;
    }

    public long getBrancheId() {
        return brancheId;
    }

    @NonNull
    public String getPosition() {
        return position;
    }

    public void setBrancheId(long brancheId) {
        this.brancheId = brancheId;
    }

    public void setPosition(@NonNull String position) {
        this.position = position;
    }

    public void setPassword(@Nullable String password) {
        this.password = password;
    }

    public void setLogin(@NonNull String login) {
        this.login = login;
    }

    @NonNull
    public String getLogin() {
        return login;
    }

    @Nullable
    public String getPassword() {
        return password;
    }

    @NonNull
    @Override
    public String toString() {
        return "Courier{" +
                "brancheId=" + brancheId +
                ", position='" + position + '\'' +
                ", id=" + id +
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
                ", login=" + login +
                ", password=" + password +
                ", photoUrl=" + photoUrl +
                '}';
    }
}
