package com.example.cargostar.model.api;

import android.content.Context;
import androidx.annotation.NonNull;
import com.example.cargostar.R;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String TAG = RetrofitClient.class.toString();

    private ApiService apiService;
    private static RetrofitClient INSTANCE;

    private RetrofitClient(@NonNull final Context context) {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60L, TimeUnit.SECONDS)
                .readTimeout(60L, TimeUnit.SECONDS)
                .writeTimeout(60L, TimeUnit.SECONDS)
                .callTimeout(60L, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new BasicAuthInterceptor(context.getString(R.string.default_api_login), context.getString(R.string.default_api_password)))
                .build();
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.default_server_url))
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .client(okHttpClient)
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public static RetrofitClient getInstance(@NonNull final Context context) {
        if (INSTANCE == null) {
            synchronized (RetrofitClient.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RetrofitClient(context);
                }
            }
        }
        return INSTANCE;
    }

    public void getTransitPoints(final Callback<JsonElement> callback) {
        final Call<JsonElement> call = apiService.getTransitPoints();
        call.enqueue(callback);
    }

    public void getPackageTypes(final Callback<JsonElement> callback) {
        final Call<JsonElement> call = apiService.getPackagingTypes();
        call.enqueue(callback);
    }

    public void getCountries(final Callback<JsonElement> callback) {
        final Call<JsonElement> call = apiService.getCountries();
        call.enqueue(callback);
    }

    public void getRegions(final Callback<JsonElement> callback) {
        final Call<JsonElement> call = apiService.getRegions();
        call.enqueue(callback);
    }

    public void getCities(final Callback<JsonElement> callback) {
        final Call<JsonElement> call = apiService.getCities();
        call.enqueue(callback);
    }

    public void getAddressBook(final Callback<JsonElement> callback) {
        final Call<JsonElement> call = apiService.getAddressBook();
        call.enqueue(callback);
    }

    public void getPackaging(final Callback<JsonElement> callback) {
        final Call<JsonElement> call = apiService.getPackaging();
        call.enqueue(callback);
    }

    public void getZones(final Callback<JsonElement> callback) {
        final Call<JsonElement> call = apiService.getZones();
        call.enqueue(callback);
    }

    public void getZoneSettings(final Callback<JsonElement> callback) {
        final Call<JsonElement> call = apiService.getZoneSettings();
        call.enqueue(callback);
    }
}
