package com.example.cargostar.model.actor;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import com.example.cargostar.model.Document;
import com.example.cargostar.model.location.Address;

@Entity(tableName = "user", indices = {@Index(value = {"login"}, unique = true)})
public abstract class User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") protected long id;
    @ColumnInfo(name = "first_name") @NonNull protected String firstName;
    @ColumnInfo(name = "middle_name") @NonNull protected String middleName;
    @ColumnInfo(name = "last_name") @NonNull protected String lastName;
    @ColumnInfo(name = "phone") @NonNull protected String phone;
    @Embedded @NonNull protected Account account;
    @Embedded(prefix = "photo_") protected Document photo;

    public User(@NonNull final String firstName,
                @NonNull final String middleName,
                @NonNull final String lastName,
                @NonNull final String phone,
                @NonNull final Account account) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phone = phone;
        this.account = account;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @NonNull
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NonNull final String firstName) {
        this.firstName = firstName;
    }

    @NonNull
    public String getLastName() {
        return lastName;
    }

    public void setLastName(@NonNull final String lastName) {
        this.lastName = lastName;
    }

    @NonNull
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(@NonNull final String middleName) {
        this.middleName = middleName;
    }

    @NonNull
    public String getPhone() {
        return phone;
    }

    public void setPhone(@NonNull final String phone) {
        this.phone = phone;
    }

    public Document getPhoto() {
        return photo;
    }

    public void setPhoto(Document photo) {
        this.photo = photo;
    }

    @NonNull
    public Account getAccount() {
        return account;
    }

    public void setAccount(@NonNull final Account account) {
        this.account = account;
    }

    @NonNull

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", account=" + account +
                ", photo=" + photo +
                '}';
    }
}
