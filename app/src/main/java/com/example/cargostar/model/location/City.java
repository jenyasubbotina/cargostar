package com.example.cargostar.model.location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

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
    @Nullable private String nameEn;

    @SerializedName("status")
    @Expose
    @ColumnInfo(name = "status") private int status;

    @SerializedName("created_at")
    @Expose
    @ColumnInfo(name = "created_at")
    @Nullable private Date createdAt;

    @SerializedName("updated_at")
    @Expose
    @ColumnInfo(name = "updated_at")
    @Nullable private Date updatedAt;

    public City(final long id,
                final long regionId,
                @NonNull final String name,
                @Nullable final String nameEn,
                final int status,
                @Nullable final Date createdAt,
                @Nullable final Date updatedAt) {
        this.id = id;
        this.regionId = regionId;
        this.name = name;
        this.nameEn = nameEn;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Ignore
    public City(final long regionId, @NonNull final String name) {
        this.regionId = regionId;
        this.name = name;
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

    @Nullable
    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(@Nullable String nameEn) {
        this.nameEn = nameEn;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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
        return "City{" +
                "id=" + id +
                ", regionId=" + regionId +
                ", name='" + name + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
