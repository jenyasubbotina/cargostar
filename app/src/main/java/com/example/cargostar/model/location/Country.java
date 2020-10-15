package com.example.cargostar.model.location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "country")
public class Country {
    @SerializedName("id")
    @Expose
    @PrimaryKey
    @ColumnInfo(name = "id")
    private long id;

    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name")
    @NonNull private final String name;

    @SerializedName("code")
    @Expose
    @ColumnInfo(name = "code")
    @NonNull private final String code;

    @SerializedName("status")
    @Expose
    @ColumnInfo(name = "status")
    private int status;

    @SerializedName("code2")
    @Expose
    @ColumnInfo(name = "code2")
    private String code2;

    @SerializedName("name_en")
    @Expose
    @ColumnInfo(name = "name_en")
    private String nameEn;

    @SerializedName("create_at")
    @Expose
    @ColumnInfo(name = "created_at")
    @Nullable private Date createdAt;

    @SerializedName("updated_at")
    @Expose
    @ColumnInfo(name = "updated_at")
    @Nullable private Date updatedAt;


    public Country(final long id,
                   @NonNull final String name,
                   @NonNull final String code,
                   final int status,
                   @Nullable final String code2,
                   @Nullable final String nameEn,
                   @Nullable final Date createdAt,
                   @Nullable final Date updatedAt) {
        this.name = name;
        this.code = code;
        this.status = status;
        this.code2 = code2;
        this.nameEn = nameEn;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Ignore
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode2() {
        return code2;
    }

    public void setCode2(String code2) {
        this.code2 = code2;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
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
        return "Country{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", status=" + status +
                ", code2='" + code2 + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
