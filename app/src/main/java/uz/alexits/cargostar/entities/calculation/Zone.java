package uz.alexits.cargostar.entities.calculation;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import uz.alexits.cargostar.entities.location.Country;

@Entity(tableName = "zone",
        foreignKeys = {
        @ForeignKey(entity = Country.class, parentColumns = "id", childColumns = "country_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = Provider.class, parentColumns = "id", childColumns = "provider_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "country_id"), @Index(value = "provider_id")})
public class Zone {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @ColumnInfo(name = "id")
    private final long id;

    @SerializedName("country_id")
    @Expose
    @ColumnInfo(name = "country_id")
    private final long countryId;

    @SerializedName("provider_id")
    @Expose
    @ColumnInfo(name = "provider_id")
    private final long providerId;

    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name")
    @NonNull
    private final String name;

    public Zone(final long id,
                final long countryId,
                final long providerId,
                @NonNull final String name) {
        this.id = id;
        this.countryId = countryId;
        this.providerId = providerId;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public long getCountryId() {
        return countryId;
    }

    public long getProviderId() {
        return providerId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return "Zone{" +
                "id=" + id +
                ", countryId=" + countryId +
                ", providerId=" + providerId +
                ", name='" + name + '\'' +
                '}';
    }
}
