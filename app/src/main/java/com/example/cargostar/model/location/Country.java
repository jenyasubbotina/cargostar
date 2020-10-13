package com.example.cargostar.model.location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "country")
public class Country {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") private long id;
    @ColumnInfo(name = "name") @NonNull private final String name;
    @ColumnInfo(name = "code") @NonNull private final String code;
    @ColumnInfo(name = "status1") private int status1;
    @ColumnInfo(name = "code2") private String code2;
    @ColumnInfo(name = "status2") private int status2;
    @ColumnInfo(name = "name_en") private String nameEn;
    @ColumnInfo(name = "created_at") @Nullable private Date createAt;
    @ColumnInfo(name = "updated_at") @Nullable private Date updatedAt;


    public Country(@NonNull final String name, @NonNull final String code) {
        this.name = name;
        this.code = code;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getCode() {
        return code;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus1() {
        return status1;
    }

    public void setStatus1(int status1) {
        this.status1 = status1;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public int getStatus2() {
        return status2;
    }

    public void setStatus2(int status2) {
        this.status2 = status2;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    @Nullable
    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(@Nullable Date createAt) {
        this.createAt = createAt;
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
        return "Country{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
