package uz.alexits.cargostar.entities.actor;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "address_book")
public class AddressBook {
    @Expose
    @SerializedName("id")
    @PrimaryKey
    @ColumnInfo(name = "id") private final long id;

    @Expose
    @SerializedName("user_id")
    @ColumnInfo(name = "user_id") private final long userId;

    @Expose
    @SerializedName("country_id")
    @ColumnInfo(name = "country_id") private final long countryId;

    @Expose
    @SerializedName("city_name")
    @ColumnInfo(name = "city_name") private final String cityName;

    @Expose
    @SerializedName("address")
    @ColumnInfo(name = "address") private final String address;

    @Expose
    @SerializedName("zip")
    @ColumnInfo(name = "zip") private final String zip;

    @Expose
    @SerializedName("firstname")
    @ColumnInfo(name = "first_name") private final String firstName;

    @Expose
    @SerializedName("middlename")
    @ColumnInfo(name = "middle_name") private final String middleName;

    @Expose
    @SerializedName("lastname")
    @ColumnInfo(name = "last_name") private final String lastName;

    @Expose
    @SerializedName("email")
    @ColumnInfo(name = "email") private final String email;

    @Expose
    @SerializedName("telephone")
    @ColumnInfo(name = "phone") private final String phone;

    @Expose
    @SerializedName("cargo")
    @ColumnInfo(name = "cargostar_account_number") private final String cargostarAccountNumber;

    @Expose
    @SerializedName("fedex")
    @ColumnInfo(name = "fedex_account_number") private final String tntAccountNumber;

    @Expose
    @SerializedName("tnt")
    @ColumnInfo(name = "tnt_account_number") private final String fedexAccountNumber;

    @Expose
    @SerializedName("company")
    @ColumnInfo(name = "company") private final String company;

    @Expose
    @SerializedName("inn")
    @ColumnInfo(name = "inn") private final String inn;

    @Expose
    @SerializedName("contract_number")
    @ColumnInfo(name = "contract_number") private final String contractNumber;

    @Expose
    @SerializedName("passport")
    @ColumnInfo(name = "passport_serial") private final String passportSerial;

    @Expose
    @SerializedName("type")
    @ColumnInfo(name = "type") private final int type;

    public AddressBook(final long id,
                       final long userId,
                       final long countryId,
                       final String cityName,
                       final String address,
                       final String zip,
                       final String firstName,
                       final String middleName,
                       final String lastName,
                       final String email,
                       final String phone,
                       final String cargostarAccountNumber,
                       final String tntAccountNumber,
                       final String fedexAccountNumber,
                       final String company,
                       final String inn,
                       final String contractNumber,
                       final String passportSerial,
                       final int type) {
        this.id = id;
        this.userId = userId;
        this.countryId = countryId;
        this.cityName = cityName;
        this.address = address;
        this.zip = zip;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.cargostarAccountNumber = cargostarAccountNumber;
        this.tntAccountNumber = tntAccountNumber;
        this.fedexAccountNumber = fedexAccountNumber;
        this.company = company;
        this.inn = inn;
        this.contractNumber = contractNumber;
        this.passportSerial = passportSerial;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public long getCountryId() {
        return countryId;
    }

    public String getCityName() {
        return cityName;
    }

    public String getAddress() {
        return address;
    }

    public String getZip() {
        return zip;
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

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
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

    public String getCompany() {
        return company;
    }

    public String getInn() {
        return inn;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public String getPassportSerial() {
        return passportSerial;
    }

    public int getType() {
        return type;
    }

    public long getUserId() {
        return userId;
    }

    @NonNull
    @Override
    public String toString() {
        return firstName + " " + lastName + ", " + address;
    }
}
