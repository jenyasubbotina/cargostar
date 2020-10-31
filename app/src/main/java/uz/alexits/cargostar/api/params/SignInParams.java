package uz.alexits.cargostar.api.params;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class SignInParams {
    @SerializedName("token")
    @NonNull private final String token;

    public SignInParams(@NonNull final String token) {
        this.token = token;
    }

    @NonNull
    public String getToken() {
        return token;
    }
}
