package uz.alexits.cargostar.entities.calculation;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "packaging",
        foreignKeys = {
                @ForeignKey(entity = Provider.class, parentColumns = "id", childColumns = "provider_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "provider_id")})
public class Packaging {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @ColumnInfo(name = "id")
    private long id;

    @SerializedName("provider_id")
    @Expose
    @ColumnInfo(name = "provider_id")
    private long providerId;

    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name")
    @NonNull
    private final String name;

    @SerializedName("parcel_fee")
    @Expose
    @ColumnInfo(name = "parcel_fee")
    private final long parcelFee;

    @SerializedName("volumex")
    @Expose
    @ColumnInfo(name = "volumex")
    private final int volumex;

    public Packaging(final long id,
                     final long providerId,
                     @NonNull final String name,
                     final long parcelFee,
                     final int volumex) {
        this.id = id;
        this.providerId = providerId;
        this.name = name;
        this.parcelFee = parcelFee;
        this.volumex = volumex;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProviderId() {
        return providerId;
    }

    public void setProviderId(long providerId) {
        this.providerId = providerId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public long isParcelFree() {
        return parcelFee;
    }

    public int getVolumex() {
        return volumex;
    }

    public long getParcelFee() {
        return parcelFee;
    }

    @NonNull
    @Override
    public String toString() {
        return "Packaging{" +
                "id=" + id +
                ", providerId=" + providerId +
                ", name='" + name + '\'' +
                ", parcelFee=" + parcelFee +
                ", volumex='" + volumex + '\'' +
                '}';
    }
}