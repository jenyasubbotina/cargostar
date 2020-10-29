package uz.alexits.cargostar.model.actor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "courier", inheritSuperIndices = true,
        foreignKeys = {
//        @ForeignKey(entity = Branche.class, parentColumns = "id", childColumns = "branche_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
        }
//        indices = {@Index(value = "branche_id")}
        )
public class Courier extends User {
    @Expose
    @SerializedName("branche_id")
    @ColumnInfo(name = "branche_id")
    private long brancheId;

    @ColumnInfo(name = "position")
    @NonNull private String position;

    public Courier(final long id,
                   final long countryId,
                   final long regionId,
                   final long cityId,
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
                   @NonNull final Account account,
                   final long brancheId) {
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
                account);
        this.brancheId = brancheId;
        this.position = "Курьер";
    }

//    @Expose
//    @SerializedName("photo")
//    @Embedded(prefix = "photo_")
//    @Nullable protected Document photo;

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

    @NonNull
    @Override
    public String toString() {
        return "Courier{" +
                "brancheId=" + brancheId +
                ", position='" + position + '\'' +
                '}';
    }
}
