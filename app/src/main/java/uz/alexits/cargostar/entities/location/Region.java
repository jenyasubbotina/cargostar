package uz.alexits.cargostar.entities.location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "region",
        foreignKeys = {@ForeignKey(entity = Country.class,
                parentColumns = "id",
                childColumns = "country_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "country_id")})
public class Region {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @ColumnInfo(name = "id")
    private long id;

    @SerializedName("country_id")
    @Expose
    @ColumnInfo(name = "country_id")
    private final long countryId;

    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name")
    @NonNull private final String name;

    @SerializedName("status")
    @Expose
    @ColumnInfo(name = "status")
    private int status;

    @SerializedName("name_en")
    @Expose
    @ColumnInfo(name = "name_en")
    @Nullable private String nameEn;

    @SerializedName("created_at")
    @Expose
    @ColumnInfo(name = "created_at")
    @Nullable private Date createdAt;

    @SerializedName("updated_at")
    @Expose
    @ColumnInfo(name = "updated_at")
    @Nullable private Date updatedAt;

    public Region(final long id,
                  final long countryId,
                  @NonNull final String name,
                  @Nullable final String nameEn,
                  int status,
                  @Nullable final Date createdAt,
                  @Nullable final Date updatedAt) {
        this.id = id;
        this.countryId = countryId;
        this.name = name;
        this.status = status;
        this.nameEn = nameEn;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Ignore
    public Region(final long countryId, @NonNull final String name) {
        this.countryId = countryId;
        this.name = name;
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

    @NonNull
    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Nullable
    public String getNameEn() {
        return nameEn;
    }

    @Nullable
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@Nullable Date createdAt) {
        this.createdAt = createdAt;
    }

    @Nullable
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(@Nullable Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

//    @NonNull
//    @Override
//    public String toString() {
//        return "Region{" +
//                "id=" + id +
//                ", countryId=" + countryId +
//                ", nameEn='" + nameEn + '\'' +
//                '}';
//    }
}
