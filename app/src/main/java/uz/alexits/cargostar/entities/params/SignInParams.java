package uz.alexits.cargostar.entities.params;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class SignInParams {
    @SerializedName("token")
    @NonNull private final String token;

    public SignInParams(final String token) {
        this.token = token;
    }

    @NonNull
    public String getToken() {
        return token;
    }
}
