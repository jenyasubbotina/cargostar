package com.example.cargostar.model.location;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.OnConflictStrategy;
import androidx.room.PrimaryKey;

@Entity(tableName = "city",
        foreignKeys = {@ForeignKey(entity = Region.class,
                parentColumns = "id",
                childColumns = "region_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "region_id")})
public class City {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") private long id;
    @ColumnInfo(name = "region_id") private final long regionId;
    @ColumnInfo(name = "name") private final String name;

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

    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", regionId=" + regionId +
                ", name='" + name + '\'' +
                '}';
    }
}
