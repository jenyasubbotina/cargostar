package com.example.cargostar.model.api;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BasicAuthInterceptor implements Interceptor {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private final String apiLogin;
    private final String apiPassword;

    public BasicAuthInterceptor(@NonNull final String apiLogin, @NonNull final String apiPassword) {
        this.apiLogin = apiLogin;
        this.apiPassword = apiPassword;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        return chain.proceed(chain.request().newBuilder()
                .header(AUTHORIZATION_HEADER, Credentials.basic(apiLogin, apiPassword))
                .build());
    }
}
