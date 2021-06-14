package uz.alexits.cargostar.entities.calculation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "packagingType")
public class PackagingType {
    @Expose
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    private final long id;

    @Expose
    @SerializedName("packaging_id")
    @ColumnInfo(name = "packaging_id")
    private final long packagingId;

    @Expose
    @SerializedName("name")
    @ColumnInfo(name = "name")
    @NonNull private final String name;

    @Expose
    @SerializedName("name_en")
    @ColumnInfo(name = "name_en")
    @Nullable private final String nameEn;

    @Expose
    @SerializedName("name_uz")
    @ColumnInfo(name = "name_uz")
    @Nullable private final String nameUz;

    @Expose
    @SerializedName("description")
    @ColumnInfo(name = "description")
    @Nullable private final String description;

    @Expose
    @SerializedName("type")
    @ColumnInfo(name = "type")
    private final int type;

    public PackagingType(final long id,
                         final long packagingId,
                         @NonNull final String name,
                         @Nullable final String nameEn,
                         @Nullable final String nameUz,
                         @Nullable final String description,
                         final int type) {
        this.id = id;
        this.packagingId = packagingId;
        this.name = name;
        this.nameEn = nameEn;
        this.nameUz = nameUz;
        this.description = description;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public long getPackagingId() {
        return packagingId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public String getNameEn() {
        return nameEn;
    }

    @Nullable
    public String getNameUz() {
        return nameUz;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public int getType() {
        return type;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
