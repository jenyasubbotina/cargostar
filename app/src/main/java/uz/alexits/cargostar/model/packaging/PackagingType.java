package uz.alexits.cargostar.model.packaging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

@Entity(tableName = "packagingType",
        foreignKeys = {@ForeignKey(entity = Packaging.class,
                parentColumns = "id",
                childColumns = "packaging_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "packaging_id")})
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
    @SerializedName("description_en")
    @ColumnInfo(name = "description_en")
    @Nullable private final String descriptionEn;

    @Expose
    @SerializedName("description_uz")
    @ColumnInfo(name = "description_uz")
    @Nullable private final String descriptionUz;

    @Expose
    @SerializedName("status")
    @ColumnInfo(name = "status")
    private final int status;

    @Expose
    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    @Nullable private final Date createdAt;

    @Expose
    @SerializedName("updated_at")
    @ColumnInfo(name = "updated_at")
    @Nullable private final Date updatedAt;

    public PackagingType(final long id,
                         final long packagingId,
                         @NonNull final String name,
                         @Nullable final String nameEn,
                         @Nullable final String nameUz,
                         @Nullable final String description,
                         @Nullable final String descriptionEn,
                         @Nullable final String descriptionUz,
                         final int status,
                         @Nullable final Date createdAt,
                         @Nullable final Date updatedAt) {
        this.id = id;
        this.packagingId = packagingId;
        this.name = name;
        this.nameEn = nameEn;
        this.nameUz = nameUz;
        this.description = description;
        this.descriptionEn = descriptionEn;
        this.descriptionUz = descriptionUz;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    @Nullable
    public String getDescriptionEn() {
        return descriptionEn;
    }

    @Nullable
    public String getDescriptionUz() {
        return descriptionUz;
    }

    public int getStatus() {
        return status;
    }

    @Nullable
    public Date getCreatedAt() {
        return createdAt;
    }

    @Nullable
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
