package uz.alexits.cargostar.api.params;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateCourierParams {
    @Expose
    @SerializedName("password")
    private final String password;

    @Expose
    @SerializedName("password_repeat")
    private final String passwordRepeat;

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
    @SerializedName("photo")
    private final String photo;

    public UpdateCourierParams(final String password,
                               final String firstName,
                               final String middleName,
                               final String lastName,
                               final String phone,
                               final String photo) {
        this.password = password;
        this.passwordRepeat = password;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.phone = phone;
        this.photo = photo;
    }

    public String getPassword() {
        return password;
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

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    @NonNull
    @Override
    public String toString() {
        return "UpdateCourierParams{" +
                ", passwordRepeat='" + passwordRepeat + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
