package uz.alexits.cargostar.model.actor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import uz.alexits.cargostar.model.location.Branche;

@Entity(tableName = "courier", inheritSuperIndices = true,
        foreignKeys = {
        @ForeignKey(entity = Branche.class, parentColumns = "id", childColumns = "branche_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "branche_id")})
public class Courier extends User {
    @Expose
    @SerializedName("branche_id")
    @ColumnInfo(name = "branche_id")
    private Long brancheId;

    @ColumnInfo(name = "position")
    @NonNull private String position;

    @Expose
    @SerializedName("photo")
    @ColumnInfo(name = "photo_url")
    @Nullable private String photoUrl;

    public Courier(final long id,
                   final Long countryId,
                   final Long regionId,
                   final Long cityId,
                   @NonNull final String firstName,
                   @NonNull final String middleName,
                   @NonNull final String lastName,
                   @NonNull final String phone,
                   @NonNull final String email,
                   @Nullable final String address,
                   @Nullable final String geo,
                   @Nullable final String zip,
                   final int status,
                   @Nullable final Date createdAt,
                   @Nullable final Date updatedAt,
                   final Long brancheId,
                   @NonNull final String login,
                   @Nullable final String photoUrl) {
        super(id,
                countryId,
                regionId,
                cityId,
                firstName,
                middleName,
                lastName,
                phone,
                email,
                address,
                geo,
                zip,
                status,
                createdAt,
                updatedAt,
                login);
        this.brancheId = brancheId;
        this.position = "Курьер";
        this.photoUrl = photoUrl;
    }

    public void setPhotoUrl(@Nullable String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Nullable
    public String getPhotoUrl() {
        return photoUrl;
    }

    public Long getBrancheId() {
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

    @NonNull
    @Override
    public String toString() {
        return "Courier{" +
                "brancheId=" + brancheId +
                ", position='" + position + '\'' +
                ", id=" + id +
                ", countryId=" + countryId +
                ", regionId=" + regionId +
                ", cityId=" + cityId +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", geo='" + geo + '\'' +
                ", zip='" + zip + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", login=" + login +
                ", password=" + password +
                '}';
    }
}
