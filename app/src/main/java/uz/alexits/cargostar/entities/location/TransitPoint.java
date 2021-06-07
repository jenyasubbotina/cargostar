package uz.alexits.cargostar.entities.location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    private final long countryId;

    @SerializedName("region_id")
    @Expose
    @ColumnInfo(name = "region_id")
    private final long regionId;

    @SerializedName("city_id")
    @Expose
    @ColumnInfo(name = "city_id")
    private final long cityId;

    @SerializedName("branche_id")
    @Expose
    @ColumnInfo(name = "branche_id")
    private final long brancheId;

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
    @Nullable private final String geolocation;

    public TransitPoint(final long id,
                        final long countryId,
                        final long regionId,
                        final long cityId,
                        final long brancheId,
                        @NonNull final String name,
                        @NonNull final String address,
                        @Nullable final String geolocation) {
        this.id = id;
        this.countryId = countryId;
        this.regionId = regionId;
        this.cityId = cityId;
        this.brancheId = brancheId;
        this.name = name;
        this.address = address;
        this.geolocation = geolocation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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


    public long getBrancheId() {
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

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
