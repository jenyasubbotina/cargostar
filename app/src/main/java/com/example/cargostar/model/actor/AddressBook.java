package com.example.cargostar.model.actor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.cargostar.model.location.Address;

@Entity(tableName = "address_book",
        indices = {@Index(value = {"sender_login"}, unique = false)})
public class AddressBook {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") private long id;
    @ColumnInfo(name = "sender_login") private String senderLogin;
    @ColumnInfo(name = "payer_login") private String payerLogin;
    @ColumnInfo(name = "payer_first_name") @NonNull private String payerFirstName;
    @ColumnInfo(name = "payer_middle_name") @NonNull private String payerMiddleName;
    @ColumnInfo(name = "payer_last_name") @NonNull private String payerLastName;
    @ColumnInfo(name = "payer_phone") @NonNull private String payerPhone;
    @ColumnInfo(name = "payer_email") @NonNull private String payerEmail;
    @Embedded @NonNull private Address payerAddress;
    @ColumnInfo(name = "payer_cargo_acc_num") @Nullable private String cargostarAccountNumber;
    @ColumnInfo(name = "payer_fedex_acc_num") @Nullable private String fedexAccountNumber;
    @ColumnInfo(name = "payer_tnt_acc_num") @Nullable private String tntAccountNumber;
    @ColumnInfo(name = "payer_checking_account") @Nullable private String payerCheckingAccount;
    @ColumnInfo(name = "payer_bank") @Nullable private String payerBank;
    @ColumnInfo(name = "payer_registration_code") @Nullable private String payerRegistrationCode;
    @ColumnInfo(name = "payer_mfo") @Nullable private String payerMfo;
    @ColumnInfo(name = "payer_oked") @Nullable private String payerOked;

    public AddressBook(final String senderLogin,
                       final @NonNull String payerFirstName,
                       final @NonNull String payerMiddleName,
                       final @NonNull String payerLastName,
                       final @NonNull String payerPhone,
                       final @NonNull String payerEmail,
                       final @NonNull Address payerAddress) {
        this.senderLogin = senderLogin;
        this.payerFirstName = payerFirstName;
        this.payerMiddleName = payerMiddleName;
        this.payerLastName = payerLastName;
        this.payerPhone = payerPhone;
        this.payerEmail = payerEmail;
        this.payerAddress = payerAddress;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSenderLogin() {
        return senderLogin;
    }

    public void setSenderLogin(String senderLogin) {
        this.senderLogin = senderLogin;
    }

    public String getPayerLogin() {
        return payerLogin;
    }

    public void setPayerLogin(String payerLogin) {
        this.payerLogin = payerLogin;
    }

    @NonNull
    public String getPayerFirstName() {
        return payerFirstName;
    }

    public void setPayerFirstName(@NonNull String payerFirstName) {
        this.payerFirstName = payerFirstName;
    }

    @NonNull
    public String getPayerMiddleName() {
        return payerMiddleName;
    }

    public void setPayerMiddleName(@NonNull String payerMiddleName) {
        this.payerMiddleName = payerMiddleName;
    }

    @NonNull
    public String getPayerLastName() {
        return payerLastName;
    }

    public void setPayerLastName(@NonNull String payerLastName) {
        this.payerLastName = payerLastName;
    }

    @NonNull
    public String getPayerPhone() {
        return payerPhone;
    }

    public void setPayerPhone(@NonNull String payerPhone) {
        this.payerPhone = payerPhone;
    }

    @NonNull
    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(@NonNull String payerEmail) {
        this.payerEmail = payerEmail;
    }

    @NonNull
    public Address getPayerAddress() {
        return payerAddress;
    }

    public void setPayerAddress(@NonNull Address payerAddress) {
        this.payerAddress = payerAddress;
    }

    @Nullable
    public String getCargostarAccountNumber() {
        return cargostarAccountNumber;
    }

    public void setCargostarAccountNumber(@Nullable String cargostarAccountNumber) {
        this.cargostarAccountNumber = cargostarAccountNumber;
    }

    @Nullable
    public String getFedexAccountNumber() {
        return fedexAccountNumber;
    }

    public void setFedexAccountNumber(@Nullable String fedexAccountNumber) {
        this.fedexAccountNumber = fedexAccountNumber;
    }

    @Nullable
    public String getTntAccountNumber() {
        return tntAccountNumber;
    }

    public void setTntAccountNumber(@Nullable String tntAccountNumber) {
        this.tntAccountNumber = tntAccountNumber;
    }

    @Nullable
    public String getPayerCheckingAccount() {
        return payerCheckingAccount;
    }

    public void setPayerCheckingAccount(@Nullable String payerCheckingAccount) {
        this.payerCheckingAccount = payerCheckingAccount;
    }

    @Nullable
    public String getPayerBank() {
        return payerBank;
    }

    public void setPayerBank(@Nullable String payerBank) {
        this.payerBank = payerBank;
    }

    @Nullable
    public String getPayerRegistrationCode() {
        return payerRegistrationCode;
    }

    public void setPayerRegistrationCode(@Nullable String payerRegistrationCode) {
        this.payerRegistrationCode = payerRegistrationCode;
    }

    @Nullable
    public String getPayerMfo() {
        return payerMfo;
    }

    public void setPayerMfo(@Nullable String payerMfo) {
        this.payerMfo = payerMfo;
    }

    @Nullable
    public String getPayerOked() {
        return payerOked;
    }

    public void setPayerOked(@Nullable String payerOked) {
        this.payerOked = payerOked;
    }

    @NonNull
    @Override
    public String toString() {
        return payerFirstName + ' ' + payerLastName + ' ' + ", " + payerAddress.getAddress();
    }
}
