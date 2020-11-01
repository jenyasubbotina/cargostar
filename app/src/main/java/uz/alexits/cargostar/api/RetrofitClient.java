package uz.alexits.cargostar.api;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import uz.alexits.cargostar.R;
import uz.alexits.cargostar.api.params.BindRequestParams;
import uz.alexits.cargostar.api.params.CreateClientParams;
import uz.alexits.cargostar.api.params.CreateInvoiceParams;
import uz.alexits.cargostar.api.params.SignInParams;
import uz.alexits.cargostar.api.params.UpdateCourierParams;
import uz.alexits.cargostar.model.actor.AddressBook;
import uz.alexits.cargostar.model.actor.Courier;
import uz.alexits.cargostar.model.actor.Customer;
import uz.alexits.cargostar.model.location.Branche;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.model.location.TransitPoint;
import uz.alexits.cargostar.model.calculation.Zone;
import uz.alexits.cargostar.model.calculation.ZoneSettings;
import uz.alexits.cargostar.model.calculation.Packaging;
import uz.alexits.cargostar.model.calculation.PackagingType;
import uz.alexits.cargostar.model.shipping.Cargo;
import uz.alexits.cargostar.model.shipping.Invoice;
import uz.alexits.cargostar.model.shipping.Request;
import uz.alexits.cargostar.model.calculation.Provider;
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
    private static Retrofit.Builder retrofitBuilder;
    private static OkHttpClient.Builder httpBuilder;
    private static BasicAuthInterceptor basicAuthInterceptor;
    private ApiService apiService;
    private static RetrofitClient INSTANCE;

    private RetrofitClient(@NonNull final Context context) {
        httpBuilder = new OkHttpClient.Builder()
                .connectTimeout(60L, TimeUnit.SECONDS)
                .readTimeout(60L, TimeUnit.SECONDS)
                .writeTimeout(60L, TimeUnit.SECONDS)
                .callTimeout(60L, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        retrofitBuilder = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.default_server_url))
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder()
                                .setLenient()
                                .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context1) -> new Date(json.getAsJsonPrimitive().getAsLong()))
                                .create()));
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

    public void setServerData(@NonNull final String login, @NonNull final String password) {
        basicAuthInterceptor = new BasicAuthInterceptor(login, password);
        apiService = retrofitBuilder.client(httpBuilder.addInterceptor(basicAuthInterceptor).build()).build().create(ApiService.class);
    }

    /* Location data */
    public Response<List<Country>> getCountries(final int perPage) throws IOException {
        return apiService.getCountries(perPage).execute();
    }

    public Response<List<Region>> getRegions(final int perPage) throws IOException {
        return apiService.getRegions(perPage).execute();
    }

    public Response<List<City>> getCities(final int perPage) throws IOException {
        return apiService.getCities(perPage).execute();
    }

    public Response<List<Branche>> getBranches(final int perPage) throws IOException {
        return apiService.getBranches(perPage).execute();
    }

    public Response<List<TransitPoint>> getTransitPoints(final int perPage) throws IOException {
        return apiService.getTransitPoints(perPage).execute();
    }

    public void getAddressBook(final Callback<JsonElement> callback) {
        final Call<JsonElement> call = apiService.getAddressBook();
        call.enqueue(callback);
    }

    /*Requests requests */
    public Response<List<Request>> getPublicRequests() throws IOException {
        return apiService.getPublicRequests().execute();
    }

    public Response<List<Request>> getMyRequests(final long courierId) throws IOException {
        return apiService.getMyRequests(courierId).execute();
    }

    /* Provider / packaging for calculations */
    public Response<List<Provider>> getProviders() throws IOException {
        return apiService.getProviders().execute();
    }

    public Response<List<Packaging>> getPackaging() throws IOException {
        return apiService.getPackaging().execute();
    }

    public Response<List<PackagingType>> getPackagingTypes() throws IOException {
        return apiService.getPackagingTypes().execute();
    }

    public Response<List<Zone>> getZones(final int perPage) throws IOException {
        return apiService.getZones(perPage).execute();
    }

    public Response<List<ZoneSettings>> getZoneSettings(final int perPage) throws IOException {
        return apiService.getZoneSettings(perPage).execute();
    }

    /* Client */
    public Response<Customer> createClient(final String login,
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
                                           final double discount,
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
                                           final String signatureUrl) throws IOException {
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
        return apiService.createClient(clientParams).execute();
    }

    public Response<Customer> getCustomer(final long clientId) throws IOException {
        return apiService.getClient(clientId).execute();
    }

    /* Courier */
    public Response<Courier> signIn(@NonNull final String fcmToken) throws IOException {
        return apiService.signIn(new SignInParams(fcmToken)).execute();
    }

    public void bindRequest(final long requestId, final long courierId, final Callback<JsonElement> callback) {
        final Call<JsonElement> call = apiService.bindRequest(requestId, new BindRequestParams(courierId));
        call.enqueue(callback);
    }

    public Response<Courier> updateCourierData(final long courierId,
                                               @NonNull final String password,
                                               @Nullable final String firstName,
                                               @Nullable final String middleName,
                                               @Nullable final String lastName,
                                               @Nullable final String phone,
                                               @Nullable final String photo) throws IOException {
        final UpdateCourierParams updateCourierParams = new UpdateCourierParams(
                password, firstName, middleName, lastName, phone, photo);
        Log.i(TAG, "updateCourierData(): " + updateCourierParams);
        return apiService.updateCourierData(courierId, updateCourierParams).execute();
    }

    /* Address Book */
    public Response<AddressBook> getAddressBookEntry(final long entryId) throws IOException {
        return apiService.getAddressBookData(entryId).execute();
    }

    /* Invoice */
    public void createInvoice(final Long courierId,
                              final Long operatorId,
                              final Long accountantId,
                              final String senderSignature,
                              final String senderEmail,
                              final String senderCargostarAccountNumber,
                              final String senderTntAccountNumber,
                              final String senderFedexAccountNumber,
                              final String senderCountry,
                              final String senderRegion,
                              final String senderCity,
                              final String senderAddress,
                              final String senderZip,
                              final String senderFirstName,
                              final String senderMiddleName,
                              final String senderLastName,
                              final String senderPhone,
                              final String recipientSignature,
                              final String recipientEmail,
                              final String recipientCargostarAccountNumber,
                              final String recipientTntAccountNumber,
                              final String recipientFedexAccountNumber,
                              final String recipientCountry,
                              final String recipientRegion,
                              final String recipientCity,
                              final String recipientAddress,
                              final String recipientZip,
                              final String recipientFirstName,
                              final String recipientMiddleName,
                              final String recipientLastName,
                              final String recipientPhone,
                              final String payerEmail,
                              final String payerCargostarAccountNumber,
                              final String payerTntAccountNumber,
                              final String payerFedexAccountNumber,
                              final String payerCountry,
                              final String payerRegion,
                              final String payerCity,
                              final String payerAddress,
                              final String payerZip,
                              final String payerFirstName,
                              final String payerMiddleName,
                              final String payerLastName,
                              final String payerPhone,
                              final String discount,
                              final String checkingAccount,
                              final String bank,
                              final String registrationCode,
                              final String mfo,
                              final String oked,
                              final String qr,
                              final String courierGuidelines,
                              final List<Cargo> cargoList,
                              final double price,
                              final Long tariffId,
                              final Long providerId,
                              final int paymentMethod,
                              @NonNull final Callback<JsonElement> callback) {
        final CreateInvoiceParams createInvoiceParams = new CreateInvoiceParams(
        courierId,
        operatorId,
        accountantId,
        senderSignature,
        senderEmail,
        senderCargostarAccountNumber,
        senderTntAccountNumber,
        senderFedexAccountNumber,
        senderCountry,
        senderRegion,
        senderCity,
        senderAddress,
        senderZip,
        senderFirstName,
        senderMiddleName,
        senderLastName,
        senderPhone,
        recipientSignature,
        recipientEmail,
        recipientCargostarAccountNumber,
        recipientTntAccountNumber,
        recipientFedexAccountNumber,
        recipientCountry,
        recipientRegion,
        recipientCity,
        recipientAddress,
        recipientZip,
        recipientFirstName,
        recipientMiddleName,
        recipientLastName,
        recipientPhone,
        payerEmail,
        payerCargostarAccountNumber,
        payerTntAccountNumber,
        payerFedexAccountNumber,
        payerCountry,
        payerRegion,
        payerCity,
        payerAddress,
        payerZip,
        payerFirstName,
        payerMiddleName,
        payerLastName,
        payerPhone,
        discount,
        checkingAccount,
        bank,
        registrationCode,
        mfo,
        oked,
        qr,
        courierGuidelines,
        cargoList,
        price,
        tariffId,
        providerId,
        paymentMethod);

        Log.i(TAG, "createInvoice: " + createInvoiceParams);

        apiService.createInvoice(createInvoiceParams).enqueue(callback);
    }

    public Response<Invoice> getInvoice(final long invoiceId) throws IOException {
        return apiService.getInvoice(invoiceId).execute();
    }

    public void updateRequest(final long requestId, final Callback<JsonElement> callback) {
//        final Call<JsonElement> call = apiService.updateRequest(requestId);
//        call.enqueue(callback);
    }

    /* Transportation */
    public Response<List<JsonElement>> getCurrentTransportations() throws IOException {
        return apiService.getCurrentTransportations().execute();
    }

    private static final String TAG = RetrofitClient.class.toString();
}
