package uz.alexits.cargostar.entities.params;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateClientParams {
    @Expose
    @SerializedName("username")
    private final String login;

    @Expose
    @SerializedName("password")
    private final String password;

    @Expose
    @SerializedName("password_repeat")
    private final String passwordRepeat;

    @Expose
    @SerializedName("email")
    private final String email;

    @Expose
    @SerializedName("cargo")
    private final String cargostarAccountNumber;

    @Expose
    @SerializedName("tnt")
    private final String tntAccountNumber;

    @Expose
    @SerializedName("fedex")
    private final String fedexAccountNumber;

    @Expose
    @SerializedName("firstname")
    private final String firstName;

    @Expose
    @SerializedName("middlename")
    private final String middleName;

    @Expose
    @SerializedName("lastname")
    private final String lastName;

    @Expose
    @SerializedName("telephone")
    private final String phone;

    @Expose
    @SerializedName("country")
    private final String country;

    @Expose
    @SerializedName("city_name")
    private final String cityName;

    @Expose
    @SerializedName("address")
    private final String address;

    @Expose
    @SerializedName("geo")
    private final String geolocation;

    @Expose
    @SerializedName("zip")
    private final String zip;

    @Expose
    @SerializedName("discount")
    private final double discount;

    @Expose
    @SerializedName("signature")
    private final String signatureUrl;

    @Expose
    //1 - physical, 2 - juridical
    @SerializedName("type")
    private final int userType;

    @Expose
    @SerializedName("passport")
    private final String passportSerial;

    @Expose
    @SerializedName("inn")
    private final String inn;

    @Expose
    @SerializedName("company")
    private final String company;

    @Expose
    @SerializedName("contract_number")
    private final String contractNumber;


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
                              final String cityName,
                              final String address,
                              final String geolocation,
                              final String zip,
                              final double discount,
                              final int userType,
                              final String passportSerial,
                              final String inn,
                              final String company,
                              final String contractNumber,
                              final String signatureUrl) {
        this.login = login;
        this.password = password;
        this.passwordRepeat = password;
        this.email = email;
        this.cargostarAccountNumber = cargostarAccountNumber;
        this.tntAccountNumber = tntAccountNumber;
        this.fedexAccountNumber = fedexAccountNumber;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phone = phone;
        this.country = country;
        this.cityName = cityName;
        this.address = address;
        this.geolocation = geolocation;
        this.zip = zip;
        this.discount = discount;
        this.signatureUrl = signatureUrl;
        this.userType = userType;
        this.passportSerial = passportSerial;
        this.inn = inn;
        this.company = company;
        this.contractNumber = contractNumber;
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

    public String getCityName() {
        return cityName;
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

    public double getDiscount() {
        return discount;
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

    public String getContractNumber() {
        return contractNumber;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
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
                ", city='" + cityName + '\'' +
                ", address='" + address + '\'' +
                ", geolocation='" + geolocation + '\'' +
                ", zip='" + zip + '\'' +
                ", discount=" + discount +
                ", signatureUrl='" + signatureUrl + '\'' +
                ", userType=" + userType +
                ", passportSerial='" + passportSerial + '\'' +
                ", inn='" + inn + '\'' +
                ", company='" + company + '\'' +
                ", bank='" + contractNumber + '\'' +
                '}';
    }
}
