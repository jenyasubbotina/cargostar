package uz.alexits.cargostar.model.calculation;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import uz.alexits.cargostar.model.location.Country;

@Entity(tableName = "zoneCountry",
        foreignKeys = {
        @ForeignKey(entity = Country.class, parentColumns = "id", childColumns = "country_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
        @ForeignKey(entity = Zone.class, parentColumns = "id", childColumns = "zone_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "country_id"), @Index(value = "zone_id")})
public class ZoneCountry {
    @Expose
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    private final long id;

    @Expose
    @SerializedName("country_id")
    @ColumnInfo(name = "country_id")
    private final Long countryId;

    @Expose
    @SerializedName("zone_id")
    @ColumnInfo(name = "zone_id")
    private final Long zoneId;

    public ZoneCountry(final long id,
                       final Long countryId,
                       final Long zoneId) {
        this.id = id;
        this.countryId = countryId;
        this.zoneId = zoneId;
    }

    public long getId() {
        return id;
    }

    public Long getCountryId() {
        return countryId;
    }

    public Long getZoneId() {
        return zoneId;
    }

    @NonNull
    @Override
    public String toString() {
        return "ZoneCountry{" +
                "id=" + id +
                ", countryId=" + countryId +
                ", zoneId=" + zoneId +
                '}';
    }
}