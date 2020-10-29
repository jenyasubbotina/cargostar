package uz.alexits.cargostar.api;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import retrofit2.http.Body;
import uz.alexits.cargostar.R;

import uz.alexits.cargostar.api.params.BindRequestParams;
import uz.alexits.cargostar.api.params.CreateClientParams;
import uz.alexits.cargostar.api.params.UpdateCourierParams;
import uz.alexits.cargostar.model.location.Branche;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.model.location.TransitPoint;
import uz.alexits.cargostar.model.packaging.Packaging;
import uz.alexits.cargostar.model.packaging.PackagingType;
import uz.alexits.cargostar.model.shipping.Request;
import uz.alexits.cargostar.model.packaging.Provider;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private final ApiService apiService;
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
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder()
                                .setLenient()
                                .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context1) ->
                                        new Date(json.getAsJsonPrimitive().getAsLong()))
                                .create()))
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

    public Response<List<Country>> getCountries(final int perPage) throws IOException {
        return apiService.getCountries(perPage).execute();
    }

    public Response<List<Region>> getRegions(final int perPage) throws IOException {
        return apiService.getRegions(perPage).execute();
    }

    public Response<List<City>> getCities(final int perPage) throws IOException {
        return apiService.getCities(perPage).execute();
    }

    public Response<List<Branche>> getBranches() throws IOException {
        return apiService.getBranches().execute();
    }

    public Response<List<TransitPoint>> getTransitPoints(final int perPage) throws IOException {
        return apiService.getTransitPoints(perPage).execute();
    }

    public void getAddressBook(final Callback<JsonElement> callback) {
        final Call<JsonElement> call = apiService.getAddressBook();
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

    /*Requests requests */
    public Response<List<Request>> getPublicRequests() throws IOException {
        return apiService.getPublicRequests().execute();
    }

//    public void updateRequest(final long requestId, final Callback<JsonElement> callback) {
//        final Call<JsonElement> call = apiService.updateRequest(requestId);
//        call.enqueue(callback);
//    }

    public void bindRequest(final long requestId, final long courierId, final Callback<JsonElement> callback) {
        final Call<JsonElement> call = apiService.bindRequest(new BindRequestParams(requestId, courierId));
        call.enqueue(callback);
    }

    /* Provider / packaging */
    public Response<List<Provider>> getProviders() throws IOException {
        return apiService.getProviders().execute();
    }

    public Response<List<Packaging>> getPackaging() throws IOException {
        return apiService.getPackaging().execute();
    }

    public Response<List<PackagingType>> getPackagingTypes() throws IOException {
        return apiService.getPackagingTypes().execute();
    }

    /* Client */
    public void createClient(final String login,
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
                                              final String signatureUrl,
                                              final Callback<JsonElement> callback) {
        final CreateClientParams clientParams = new CreateClientParams(
                login,
                password,
                email,
                cargostarAccountNumber,
                tntAccountNumber,
                fedexAccountNumber,
                firstName,
                middleName,
                lastName,
                phone,
                country,
                region,
                city,
                address,
                geolocation,
                zip,
                discount,
                userType,
                passportSerial,
                inn,
                company,
                bank,
                mfo,
                oked,
                checkingAccount,
                registrationCode,
                photoUrl,
                signatureUrl);
        Log.i(TAG, "createClient(): " + clientParams);
        apiService.createClient(clientParams).enqueue(callback);
    }

    private static final String TAG = RetrofitClient.class.toString();
}
