package com.example.cargostar.model.location;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "branch",
        foreignKeys = {@ForeignKey(entity = City.class,
        parentColumns = "id",
        childColumns = "city_id",
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "city_id")})
public class Branch {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") private long id;
    @ColumnInfo(name = "city_id") private final long cityId;
    @ColumnInfo(name = "name") @NonNull private final String name;
    @ColumnInfo(name = "address") @NonNull private final String address;
    @ColumnInfo(name = "zip")@NonNull private final String zip;
    @Embedded @NonNull private final Point geolocation;
    @ColumnInfo(name = "phone") @NonNull private final String phone;

    public Branch( long cityId, @NonNull String name, @NonNull String address, @NonNull final String zip, @NonNull final Point geolocation, @NonNull String phone) {
        this.cityId = cityId;
        this.name = name;
        this.address = address;
        this.zip = zip;
        this.geolocation = geolocation;
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCityId() {
        return cityId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    @NonNull
    public String getPhone() {
        return phone;
    }

    @NonNull
    public String getZip() {
        return zip;
    }

    @NonNull
    public Point getGeolocation() {
        return geolocation;
    }

    @NonNull
    @Override
    public String toString() {
        return "Branch{" +
                "id=" + id +
                ", cityId=" + cityId +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", zip='" + zip + '\'' +
                ", geolocation=" + geolocation +
                '}';
    }
}
