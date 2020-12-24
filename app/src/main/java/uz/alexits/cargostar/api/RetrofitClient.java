package uz.alexits.cargostar.api;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import uz.alexits.cargostar.R;
import uz.alexits.cargostar.api.params.BindRequestParams;
import uz.alexits.cargostar.api.params.CreateClientParams;
import uz.alexits.cargostar.api.params.CreateInvoiceParams;
import uz.alexits.cargostar.api.params.CreateInvoiceResponse;
import uz.alexits.cargostar.api.params.RecipientSignatureParams;
import uz.alexits.cargostar.api.params.SignInParams;
import uz.alexits.cargostar.api.params.TransportationStatusParams;
import uz.alexits.cargostar.api.params.UpdateCourierParams;
import uz.alexits.cargostar.model.actor.AddressBook;
import uz.alexits.cargostar.model.actor.Courier;
import uz.alexits.cargostar.model.actor.Customer;
import uz.alexits.cargostar.model.calculation.Vat;
import uz.alexits.cargostar.model.calculation.ZoneCountry;
import uz.alexits.cargostar.model.location.Branche;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.model.location.TransitPoint;
import uz.alexits.cargostar.model.calculation.Zone;
import uz.alexits.cargostar.model.calculation.ZoneSettings;
import uz.alexits.cargostar.model.calculation.Packaging;
import uz.alexits.cargostar.model.calculation.PackagingType;
import uz.alexits.cargostar.model.transportation.Consignment;
import uz.alexits.cargostar.model.transportation.Invoice;
import uz.alexits.cargostar.model.transportation.Request;
import uz.alexits.cargostar.model.calculation.Provider;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uz.alexits.cargostar.model.transportation.Route;
import uz.alexits.cargostar.model.transportation.Transportation;
import uz.alexits.cargostar.model.transportation.TransportationData;
import uz.alexits.cargostar.model.transportation.TransportationStatus;

public class RetrofitClient {
    private static Retrofit.Builder retrofitBuilder;
    private static OkHttpClient.Builder httpBuilder;
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
        final BasicAuthInterceptor basicAuthInterceptor = new BasicAuthInterceptor(login, password);
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

    public Response<List<AddressBook>> getAddressBook(final int perPage) throws IOException {
        return apiService.getAddressBook(perPage).execute();
    }

    public Response<List<AddressBook>> getAddressBook(final int perPage, final long lastId) throws IOException {
        return apiService.getAddressBook(perPage, lastId).execute();
    }

    /*Requests requests */
    public Response<List<Request>> getPublicRequests(final int perPage) throws IOException {
        return apiService.getPublicRequests(perPage).execute();
    }

    public Response<List<Request>> getPublicRequests(final int perPage, final long lastId) throws IOException {
        return apiService.getPublicRequests(perPage, lastId).execute();
    }

    public Response<Request> getRequest(final long requestId) throws IOException {
        return apiService.getRequest(requestId).execute();
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

    public Response<List<ZoneCountry>> getZoneCountries(final int perPage) throws IOException {
        return apiService.getZoneCountries(perPage).execute();
    }

    public Response<List<ZoneSettings>> getZoneSettings(final int perPage) throws IOException {
        return apiService.getZoneSettings(perPage).execute();
    }

    public Response<Vat> getVat() throws IOException {
        return apiService.getVat().execute();
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
                signatureUrl);
        return apiService.createClient(clientParams).execute();
    }

    public Response<Customer> getClient(final long clientId) throws IOException {
        return apiService.getClient(clientId).execute();
    }

    public Response<List<Customer>> getClients(final int perPage) throws IOException {
        return apiService.getClients(perPage).execute();
    }

    public Response<List<Customer>> getClients(final int perPage, final long lastId) throws IOException {
        return apiService.getClients(perPage, lastId).execute();
    }

    /* Courier */
    public Response<Courier> signIn(@NonNull final String fcmToken) throws IOException {
        return apiService.signIn(new SignInParams(fcmToken)).execute();
    }

    public Response<Request> bindRequest(final long requestId, final long courierId) throws IOException {
        return apiService.bindRequest(requestId, new BindRequestParams(courierId)).execute();
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
        return apiService.updateCourierData(courierId, updateCourierParams).execute();
    }

    /* Address Book */
    public Response<AddressBook> getAddressBookEntry(final long entryId) throws IOException {
        return apiService.getAddressBookData(entryId).execute();
    }

    /* Invoice */
    public Response<CreateInvoiceResponse> createInvoice(@NonNull final CreateInvoiceParams createInvoiceParams) throws IOException {
        Log.i(TAG, "createInvoice: " + createInvoiceParams);
        return apiService.createInvoice(createInvoiceParams).execute();
    }

    public Response<Invoice> getInvoice(final long invoiceId) throws IOException {
        return apiService.getInvoice(invoiceId).execute();
    }

    public Response<List<Invoice>> getInvoiceList(final int perPage) throws IOException {
        return apiService.getInvoiceList(perPage).execute();
    }

    public Response<List<Invoice>> getInvoiceList(final int perPage, final long lastId) throws IOException {
        return apiService.getInvoiceList(perPage, lastId).execute();
    }

    public Response<RecipientSignatureParams> sendRecipientSignature(final long invoiceId, final String recipientSignature) throws IOException {
        return apiService.sendRecipientSignature(new RecipientSignatureParams(invoiceId, recipientSignature)).execute();
    }

    /* Transportation */
    public Response<Transportation> getTransportation(final long transportationId) throws IOException {
        return apiService.getTransportation(transportationId).execute();
    }

    public Response<List<Transportation>> getCurrentTransportations(final int perPage) throws IOException {
        return apiService.getCurrentTransportations(perPage).execute();
    }

    public Response<List<Transportation>> getCurrentTransportations(final int perPage, final long lastId) throws IOException {
        return apiService.getCurrentTransportations(perPage, lastId).execute();
    }

    public Response<List<TransportationStatus>> getTransportationStatusList(final int perPage) throws IOException {
        return apiService.getTransportationStatusList(perPage).execute();
    }

    public Response<List<TransportationData>> getTransportationData(final Long transportationId) throws IOException {
        return apiService.getTransportationData(transportationId).execute();
    }

    public Response<List<Route>> getTransportationRoute(final Long transportationId) throws IOException {
        return apiService.getTransportationRoute(transportationId).execute();
    }

    public Response<Transportation> updateTransportationStatus(final Long transportationId,
                                                                           final Long transitPointId,
                                                                           final Long transportationStatusId) throws IOException {
        return apiService.updateTransportationStatus(new TransportationStatusParams(transportationId, transitPointId, transportationStatusId)).execute();
    }

    public Response<Transportation> updatePartialStatus(final Long transportationId,
                                                               final Long transitPointId,
                                                               final Long transportationStatusId) throws IOException {
        return apiService.updatePartialStatus(new TransportationStatusParams(transportationId, transitPointId, transportationStatusId)).execute();
    }

    /* Cargo */
    public Response<List<Consignment>> getCargoListByInvoiceId(@Query("id") final Long invoiceId) throws IOException {
        return apiService.getCargoListByInvoiceId(invoiceId).execute();
    }

    public Response<List<Consignment>> getCargoListByRequestId(@Query("id") final Long requestId) throws IOException {
        return apiService.getCargoListByRequestId(requestId).execute();
    }

    private static final String TAG = RetrofitClient.class.toString();

    public Response<List<Customer>> getAllCustomers(final int perPage) throws IOException {
        return apiService.getAllCustomers(perPage).execute();
    }
}
