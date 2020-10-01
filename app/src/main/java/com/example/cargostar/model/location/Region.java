package com.example.cargostar.model.location;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "region",
        foreignKeys = {@ForeignKey(entity = Country.class,
                parentColumns = "id",
                childColumns = "country_id",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "country_id")})
public class Region {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") private long id;
    @ColumnInfo(name = "country_id") private final long countryId;
    @ColumnInfo (name = "name") @NonNull private final String name;

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

    @NonNull
    @Override
    public String toString() {
        return "Region{" +
                "id=" + id +
                ", countryId=" + countryId +
                ", name='" + name + '\'' +
                '}';
    }
}
