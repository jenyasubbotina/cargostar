package com.example.cargostar.model.actor;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Index;

import com.example.cargostar.model.Document;
import com.example.cargostar.model.location.Address;

@Entity(tableName = "customer", inheritSuperIndices = true)
public class Customer extends User {
    @Embedded @NonNull protected Address address;
    @ColumnInfo(name = "cargo_acc_num") @NonNull protected String cargostarAccountNumber;
    @ColumnInfo(name = "tnt_acc_num") protected String tntAccountNumber;
    @ColumnInfo(name = "fedex_acc_num") protected String fedexAccountNumber;
    @ColumnInfo(name = "discount") protected int discount;
    @Embedded(prefix = "signature_") protected Document signature;

    public Customer(@NonNull final String firstName,
                    @NonNull final String middleName,
                    @NonNull final String lastName,
                    @NonNull final String phone,
                    @NonNull final Account account,
                    @NonNull final Address address,
                    @NonNull final String cargostarAccountNumber) {
        super(firstName, middleName, lastName, phone, account);
        this.address = address;
        this.cargostarAccountNumber = cargostarAccountNumber;
    }

    @NonNull
    public Address getAddress() {
        return address;
    }

    public void setAddress(@NonNull Address address) {
        this.address = address;
    }

    @NonNull
    public String getCargostarAccountNumber() {
        return cargostarAccountNumber;
    }

    public void setCargostarAccountNumber(@NonNull final String cargostarAccountNumber) {
        this.cargostarAccountNumber = cargostarAccountNumber;
    }

    public String getTntAccountNumber() {
        return tntAccountNumber;
    }

    public void setTntAccountNumber(String tntAccountNumber) {
        this.tntAccountNumber = tntAccountNumber;
    }

    public String getFedexAccountNumber() {
        return fedexAccountNumber;
    }

    public void setFedexAccountNumber(String fedexAccountNumber) {
        this.fedexAccountNumber = fedexAccountNumber;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public Document getSignature() {
        return signature;
    }

    public void setSignature(Document signature) {
        this.signature = signature;
    }

    @NonNull
    @Override
    public String toString() {
        return "Customer{" +
                "cargostarAccountNumber='" + cargostarAccountNumber + '\'' +
                ", tntAccountNumber='" + tntAccountNumber + '\'' +
                ", fedexAccountNumber='" + fedexAccountNumber + '\'' +
                ", discount=" + discount +
                ", signature='" + signature + '\'' +
                ", id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", address=" + address +
                ", photo='" + photo + '\'' +
                ", account='" + account + '\'' +
                '}';
    }
}
