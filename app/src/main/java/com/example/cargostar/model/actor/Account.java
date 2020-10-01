package com.example.cargostar.model.actor;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

public class Account {
    @NonNull private String login;
    @NonNull private String passwordHash;
    @NonNull private String email;

    public Account(@NonNull final String login, @NonNull final String passwordHash, @NonNull final String email) {
        this.login = login;
        this.passwordHash = passwordHash;
        this.email = email;
    }

    @NonNull
    public String getLogin() {
        return login;
    }

    @NonNull
    public String getPasswordHash() {
        return passwordHash;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setLogin(@NonNull final String login) {
        this.login = login;
    }

    public void setPasswordHash(@NonNull final String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setEmail(@NonNull final String email) {
        this.email = email;
    }

    @NonNull
    @Override
    public String toString() {
        return "Account{" +
                "login='" + login + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
