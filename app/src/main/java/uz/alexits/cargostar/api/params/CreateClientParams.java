package uz.alexits.cargostar.api.params;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class CreateClientParams {
    @SerializedName("username")
    private final String login;

    @SerializedName("password")
    private final String password;

    @SerializedName("email")
    private final String email;

    @SerializedName("cargo")
    private final String cargostarAccountNumber;

    @SerializedName("tnt")
    private final String tntAccountNumber;

    @SerializedName("fedex")
    private final String fedexAccountNumber;

    @SerializedName("firstname")
    private final String firstName;

    @SerializedName("middlename")
    private final String middleName;

    @SerializedName("lastname")
    private final String lastName;

    @SerializedName("telephone")
    private final String phone;

    @SerializedName("country")
    private final String country;

    @SerializedName("region")
    private final String region;

    @SerializedName("city")
    private final String city;

    @SerializedName("address")
    private final String address;

    @SerializedName("geo")
    private final String geolocation;

    @SerializedName("zip")
    private final String zip;

    @SerializedName("discount")
    private final int discount;

    @SerializedName("photo")
    private final String photoUrl;

    @SerializedName("signature")
    private final String signatureUrl;

    //1 - physical, 2 - juridical
    @SerializedName("type")
    private final int userType;

    @SerializedName("passport")
    private final String passportSerial;

    @SerializedName("inn")
    private final String inn;

    @SerializedName("company")
    private final String company;

    @SerializedName("bank")
    private final String bank;

    @SerializedName("mfo")
    private final String mfo;

    @SerializedName("oked")
    private final String oked;

    @SerializedName("account")
    private final String checkingAccount;

    @SerializedName("code")
    private final String registrationCode;
//    private final String contractUrl;


    public CreateClientParams(final String login,
                              final String password,
                              final String email,
                              final String cargostarAccountNumber,
                              final String tntAccountNumber,
                              final String fedexAccountNumber,
                              final String firstName,
                              final String middleName,
                              final String lastName,
                              final String phone,
                              final String country,
                              final String region,
                              final String city,
                              final String address,
                              final String geolocation,
                              final String zip,
                              final int discount,
                              final int userType,
                              final String passportSerial,
                              final String inn,
                              final String company,
                              final String bank,
                              final String mfo,
                              final String oked,
                              final String checkingAccount,
                              final String registrationCode,
                              final String photoUrl,
                              final String signatureUrl) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.cargostarAccountNumber = cargostarAccountNumber;
        this.tntAccountNumber = tntAccountNumber;
        this.fedexAccountNumber = fedexAccountNumber;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phone = phone;
        this.country = country;
        this.region = region;
        this.city = city;
        this.address = address;
        this.geolocation = geolocation;
        this.zip = zip;
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

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getCargostarAccountNumber() {
        return cargostarAccountNumber;
    }

    public String getTntAccountNumber() {
        return tntAccountNumber;
    }

    public String getFedexAccountNumber() {
        return fedexAccountNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getGeolocation() {
        return geolocation;
    }

    public String getZip() {
        return zip;
    }

    public int getDiscount() {
        return discount;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getSignatureUrl() {
        return signatureUrl;
    }

    public int getUserType() {
        return userType;
    }

    public String getPassportSerial() {
        return passportSerial;
    }

    public String getInn() {
        return inn;
    }

    public String getCompany() {
        return company;
    }

    public String getBank() {
        return bank;
    }

    public String getMfo() {
        return mfo;
    }

    public String getOked() {
        return oked;
    }

    public String getCheckingAccount() {
        return checkingAccount;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    @NonNull
    @Override
    public String toString() {
        return "CreateClientParams{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", cargostarAccountNumber='" + cargostarAccountNumber + '\'' +
                ", tntAccountNumber='" + tntAccountNumber + '\'' +
                ", fedexAccountNumber='" + fedexAccountNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", country='" + country + '\'' +
                ", region='" + region + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", geolocation='" + geolocation + '\'' +
                ", zip='" + zip + '\'' +
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
                '}';
    }
}
