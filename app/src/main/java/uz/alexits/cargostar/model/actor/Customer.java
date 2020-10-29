package uz.alexits.cargostar.model.actor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "customer", inheritSuperIndices = true)
public class Customer extends User {
    @Expose
    @SerializedName("cargo")
    @ColumnInfo(name = "cargostar_account_number")
    @Nullable private String cargostarAccountNumber;

    @Expose
    @SerializedName("tnt")
    @ColumnInfo(name = "tnt_account_number")
    @Nullable private String tntAccountNumber;

    @Expose
    @SerializedName("fedex")
    @ColumnInfo(name = "fedex_account_number")
    @Nullable private String fedexAccountNumber;

    @Expose
    @SerializedName("discount")
    @ColumnInfo(name = "discount")
    private int discount;

    @Expose
    @SerializedName("photo")
    @ColumnInfo(name = "photo_url")
    @Nullable private String photoUrl;

    @Expose
    @SerializedName("signature")
    @ColumnInfo(name = "signature_url")
    @Nullable private String signatureUrl;

    //1 - physical, 2 - juridical
    @Expose
    @SerializedName("type")
    @ColumnInfo(name = "user_type")
    private int userType;

    @Expose
    @SerializedName("passport")
    @ColumnInfo(name = "passport_serial")
    @Nullable private String passportSerial;

    @Expose
    @SerializedName("inn")
    @ColumnInfo(name = "inn")
    @Nullable private String inn;

    @Expose
    @SerializedName("company")
    @ColumnInfo(name = "company")
    @Nullable private String company;

    @Expose
    @SerializedName("bank")
    @ColumnInfo(name = "bank")
    @Nullable private String bank;

    @Expose
    @SerializedName("mfo")
    @ColumnInfo(name = "mfo")
    @Nullable private String mfo;

    @Expose
    @SerializedName("oked")
    @ColumnInfo(name = "oked")
    @Nullable private String oked;

    @Expose
    @SerializedName("account")
    @ColumnInfo(name = "checking_account")
    @Nullable private String checkingAccount;

    @Expose
    @SerializedName("code")
    @ColumnInfo(name = "registration_code")
    @Nullable private String registrationCode;

    @Ignore
    public Customer(final long id,
                    final long countryId,
                    final long regionId,
                    final long cityId,
                    @NonNull final String firstName,
                    @Nullable final String middleName,
                    @NonNull final String lastName,
                    @NonNull final String phone,
                    @NonNull final String email,
                    @Nullable final String address,
                    @Nullable final String geo,
                    @Nullable final String zip,
                    final int status,
                    @Nullable final Date createdAt,
                    @Nullable final Date updatedAt,
                    @NonNull final Account account) {
        super(id, countryId, regionId, cityId, firstName, middleName, lastName, phone, email, address, geo, zip, status, createdAt, updatedAt, account);
    }

    public Customer(final long id,
                    final long countryId,
                    final long regionId,
                    final long cityId,
                    @NonNull final String firstName,
                    @Nullable final String middleName,
                    @NonNull final String lastName,
                    @NonNull final String phone,
                    @NonNull final String email,
                    @Nullable final String address,
                    @Nullable final String geo,
                    @Nullable final String zip,
                    final int status,
                    @Nullable final Date createdAt,
                    @Nullable final Date updatedAt,
                    @NonNull final Account account,
                    @Nullable final String cargostarAccountNumber,
                    @Nullable final String tntAccountNumber,
                    @Nullable final String fedexAccountNumber,
                    final int discount,
                    @Nullable final String photoUrl,
                    @Nullable final String signatureUrl,
                    final int userType,
                    @Nullable final String passportSerial,
                    @Nullable final String inn,
                    @Nullable final String company,
                    @Nullable final String bank,
                    @Nullable final String mfo,
                    @Nullable final String oked,
                    @Nullable final String checkingAccount,
                    @Nullable final String registrationCode) {
        super(id, countryId, regionId, cityId, firstName, middleName, lastName, phone, email, address, geo, zip, status, createdAt, updatedAt, account);
        this.cargostarAccountNumber = cargostarAccountNumber;
        this.tntAccountNumber = tntAccountNumber;
        this.fedexAccountNumber = fedexAccountNumber;
        this.discount = discount;
        this.photoUrl = photoUrl;
        this.signatureUrl = signatureUrl;
        this.userType = userType;
        this.passportSerial = passportSerial;
        this.inn = inn;
        this.company = company;
        this.bank = bank;
        this.mfo = mfo;
        this.oked = oked;
        this.checkingAccount = checkingAccount;
        this.registrationCode = registrationCode;
    }

    @Nullable
    public String getCargostarAccountNumber() {
        return cargostarAccountNumber;
    }

    public void setCargostarAccountNumber(@Nullable String cargostarAccountNumber) {
        this.cargostarAccountNumber = cargostarAccountNumber;
    }

    @Nullable
    public String getTntAccountNumber() {
        return tntAccountNumber;
    }

    public void setTntAccountNumber(@Nullable String tntAccountNumber) {
        this.tntAccountNumber = tntAccountNumber;
    }

    @Nullable
    public String getFedexAccountNumber() {
        return fedexAccountNumber;
    }

    public void setFedexAccountNumber(@Nullable String fedexAccountNumber) {
        this.fedexAccountNumber = fedexAccountNumber;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    @Nullable
    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(@Nullable String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Nullable
    public String getSignatureUrl() {
        return signatureUrl;
    }

    public void setSignatureUrl(@Nullable String signatureUrl) {
        this.signatureUrl = signatureUrl;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    @Nullable
    public String getPassportSerial() {
        return passportSerial;
    }

    public void setPassportSerial(@Nullable String passportSerial) {
        this.passportSerial = passportSerial;
    }

    @Nullable
    public String getInn() {
        return inn;
    }

    public void setInn(@Nullable String inn) {
        this.inn = inn;
    }

    @Nullable
    public String getCompany() {
        return company;
    }

    public void setCompany(@Nullable String company) {
        this.company = company;
    }

    @Nullable
    public String getBank() {
        return bank;
    }

    public void setBank(@Nullable String bank) {
        this.bank = bank;
    }

    @Nullable
    public String getMfo() {
        return mfo;
    }

    public void setMfo(@Nullable String mfo) {
        this.mfo = mfo;
    }

    @Nullable
    public String getOked() {
        return oked;
    }

    public void setOked(@Nullable String oked) {
        this.oked = oked;
    }

    @Nullable
    public String getCheckingAccount() {
        return checkingAccount;
    }

    public void setCheckingAccount(@Nullable String checkingAccount) {
        this.checkingAccount = checkingAccount;
    }

    @Nullable
    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(@Nullable String registrationCode) {
        this.registrationCode = registrationCode;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "cargostarAccountNumber='" + cargostarAccountNumber + '\'' +
                ", tntAccountNumber='" + tntAccountNumber + '\'' +
                ", fedexAccountNumber='" + fedexAccountNumber + '\'' +
                ", discount=" + discount +
                ", photoUrl='" + photoUrl + '\'' +
                ", signatureUrl='" + signatureUrl + '\'' +
                ", userType=" + userType +
                ", passportSerial='" + passportSerial + '\'' +
                ", inn='" + inn + '\'' +
                ", company='" + company + '\'' +
                ", bank='" + bank + '\'' +
                ", mfo='" + mfo + '\'' +
                ", oked='" + oked + '\'' +
                ", checkingAccount='" + checkingAccount + '\'' +
                ", registrationCode='" + registrationCode + '\'' +
                ", id=" + id +
                ", countryId=" + countryId +
                ", regionId=" + regionId +
                ", cityId=" + cityId +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", geo='" + geo + '\'' +
                ", zip='" + zip + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", account=" + account +
                '}';
    }
}
