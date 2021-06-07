package uz.alexits.cargostar.entities.location;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "city",
        foreignKeys = {@ForeignKey(entity = Region.class,
                parentColumns = "id",
                childColumns = "region_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "region_id")})
public class City {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @ColumnInfo(name = "id")
    private long id;

    @SerializedName("region_id")
    @Expose
    @ColumnInfo(name = "region_id")
    private final long regionId;

    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name")
    @NonNull private final String name;

    @SerializedName("name_en")
    @Expose
    @ColumnInfo(name = "name_en")
    @NonNull private final String nameEn;

    public City(final long id,
                final long regionId,
                @NonNull final String name,
                @NonNull final String nameEn) {
        this.id = id;
        this.regionId = regionId;
        this.name = name;
        this.nameEn = nameEn;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRegionId() {
        return regionId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getNameEn() {
        return nameEn;
    }

    @NonNull
    @Override
    public String toString() {
        return nameEn;
    }
}
