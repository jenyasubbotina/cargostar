package uz.alexits.cargostar.entities.actor;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "client", inheritSuperIndices = true)
public class Client extends User {
    @Expose
    @SerializedName("cargo")
    @ColumnInfo(name = "cargostar_account_number")
    private String cargostarAccountNumber;

    @Expose
    @SerializedName("tnt")
    @ColumnInfo(name = "tnt_account_number")
    private String tntAccountNumber;

    @Expose
    @SerializedName("fedex")
    @ColumnInfo(name = "fedex_account_number")
    private String fedexAccountNumber;

    @Expose
    @SerializedName("discount")
    @ColumnInfo(name = "discount")
    private int discount;

    @Expose
    @SerializedName("photo")
    @ColumnInfo(name = "photo_url")
    private String photoUrl;

    @Expose
    @SerializedName("signature")
    @ColumnInfo(name = "signature_url")
    private String signatureUrl;

    //1 - physical, 2 - juridical
    @Expose
    @SerializedName("type")
    @ColumnInfo(name = "user_type")
    private int userType;

    @Expose
    @SerializedName("passport")
    @ColumnInfo(name = "passport_serial")
    private String passportSerial;

    @Expose
    @SerializedName("inn")
    @ColumnInfo(name = "inn")
    private String inn;

    @Expose
    @SerializedName("company")
    @ColumnInfo(name = "company")
    private String company;

    @Ignore
    public Client(final long id,
                  final long userId,
                  final Long countryId,
                  final String cityName,
                  final String firstName,
                  final String middleName,
                  final String lastName,
                  final String phone,
                  final String email,
                  final String address,
                  final String geo,
                  final String zip) {
        super(id, userId, countryId, cityName, firstName, middleName, lastName, phone, email, address, geo, zip);
    }

    public Client(final long id,
                  final long userId,
                  final Long countryId,
                  final String cityName,
                  final String firstName,
                  final String middleName,
                  final String lastName,
                  final String phone,
                  final String email,
                  final String address,
                  final String geo,
                  final String zip,
                  final String cargostarAccountNumber,
                  final String tntAccountNumber,
                  final String fedexAccountNumber,
                  final int discount,
                  final String photoUrl,
                  final String signatureUrl,
                  final int userType,
                  final String passportSerial,
                  final String inn,
                  final String company) {
        super(id, userId, countryId, cityName, firstName, middleName, lastName, phone, email, address, geo, zip);
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
    }

    public String getCargostarAccountNumber() {
        return cargostarAccountNumber;
    }

    public void setCargostarAccountNumber(final String cargostarAccountNumber) {
        this.cargostarAccountNumber = cargostarAccountNumber;
    }

    public String getTntAccountNumber() {
        return tntAccountNumber;
    }

    public void setTntAccountNumber(final String tntAccountNumber) {
        this.tntAccountNumber = tntAccountNumber;
    }

    public String getFedexAccountNumber() {
        return fedexAccountNumber;
    }

    public void setFedexAccountNumber(final String fedexAccountNumber) {
        this.fedexAccountNumber = fedexAccountNumber;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(final String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getSignatureUrl() {
        return signatureUrl;
    }

    public void setSignatureUrl(final String signatureUrl) {
        this.signatureUrl = signatureUrl;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getPassportSerial() {
        return passportSerial;
    }

    public void setPassportSerial(final String passportSerial) {
        this.passportSerial = passportSerial;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(final String inn) {
        this.inn = inn;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(final String company) {
        this.company = company;
    }

    @NonNull
    @Override
    public String toString() {
        return "Client{" +
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
                ", id=" + id +
                ", countryId=" + countryId +
                ", cityId=" + cityName +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", geo='" + geo + '\'' +
                ", zip='" + zip + '\'' +
                '}';
    }
}
