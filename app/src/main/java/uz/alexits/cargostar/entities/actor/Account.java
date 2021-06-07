package uz.alexits.cargostar.entities.actor;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Account {
    @Expose
    @SerializedName("username")
    @NonNull private String login;

    @Expose
    @SerializedName("password")
    @NonNull private String passwordHash;

    public Account(@NonNull final String login, @NonNull final String passwordHash) {
        this.login = login;
        this.passwordHash = passwordHash;
    }

    @NonNull
    public String getLogin() {
        return login;
    }

    @NonNull
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setLogin(@NonNull final String login) {
        this.login = login;
    }

    public void setPasswordHash(@NonNull final String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @NonNull
    @Override
    public String toString() {
        return "Account{" +
                "login='" + login + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                '}';
    }
}
