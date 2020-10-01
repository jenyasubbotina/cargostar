package com.example.cargostar.model.actor;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import com.example.cargostar.model.location.Address;
import com.example.cargostar.model.location.Branch;
import com.example.cargostar.model.location.Country;

@Entity(tableName = "courier", inheritSuperIndices = true,
        foreignKeys = {@ForeignKey(entity = Branch.class,
        parentColumns = "id",
        childColumns = "branch_id",
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE)},
        indices = {@Index(value = "branch_id")})
public class Courier extends User {
    @ColumnInfo(name = "branch_id") private final long branchId;
    @ColumnInfo(name = "position") @NonNull private String position;

    public Courier(@NonNull final String firstName,
                   @NonNull final String middleName,
                   @NonNull final String lastName,
                   @NonNull final String phone,
                   @NonNull final Account account,
                   final long branchId) {
        super(firstName, middleName, lastName, phone, account);
        this.branchId = branchId;
        this.position = "курьер";
    }

    public long getBranchId() {
        return branchId;
    }

    public void setPosition(@NonNull final String position) {
        this.position = position;
    }

    @NonNull
    public String getPosition() {
        return position;
    }

    @NonNull
    @Override
    public String toString() {
        return "Courier{" +
                "position='" + position + '\'' +
                ", id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", account=" + account +
                ", photo=" + photo +
                ", branchId=" + branchId +
                '}';
    }
}
