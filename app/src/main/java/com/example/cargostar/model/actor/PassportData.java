package com.example.cargostar.model.actor;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "passport_data",
        inheritSuperIndices = true,
        foreignKeys = {@ForeignKey(entity = Customer.class,
        parentColumns = "id",
        childColumns = "user_id",
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = {"user_id"}, unique = true)})
public class PassportData {
    @PrimaryKey
    @ColumnInfo(name = "user_id") private long userId;
    @ColumnInfo(name = "passport_serial") @NonNull  private String passportSerial;

    public PassportData(@NonNull final String passportSerial) {
        this.passportSerial = passportSerial;
    }

    public void setPassportSerial(@NonNull final String passportSerial) {
        this.passportSerial = passportSerial;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @NonNull
    public String getPassportSerial() {
        return passportSerial;
    }

    public long getUserId() {
        return userId;
    }

    @NonNull
    @Override
    public String toString() {
        return "PassportData{" +
                "userId='" + userId + '\'' +
                ", passportSerial='" + passportSerial + '\'' +
                '}';
    }
}