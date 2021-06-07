package uz.alexits.cargostar.api;

import android.content.Context;

import androidx.annotation.NonNull;

import uz.alexits.cargostar.R;
import uz.alexits.cargostar.entities.params.AddresseeParams;
import uz.alexits.cargostar.entities.params.BindRequestParams;
import uz.alexits.cargostar.entities.params.CreateClientParams;
import uz.alexits.cargostar.entities.params.CreateInvoiceParams;
import uz.alexits.cargostar.entities.params.CreateInvoiceResponse;
import uz.alexits.cargostar.entities.transportation.Addressee;
import uz.alexits.cargostar.entities.params.SignInParams;
import uz.alexits.cargostar.entities.params.TransportationStatusParams;
import uz.alexits.cargostar.entities.params.UpdateCourierParams;
import uz.alexits.cargostar.entities.actor.AddressBook;
import uz.alexits.cargostar.entities.actor.Client;
import uz.alexits.cargostar.entities.actor.Courier;
import uz.alexits.cargostar.entities.calculation.Vat;
import uz.alexits.cargostar.entities.calculation.ZoneCountry;
import uz.alexits.cargostar.entities.location.Branche;
import uz.alexits.cargostar.entities.location.City;
import uz.alexits.cargostar.entities.location.Country;
import uz.alexits.cargostar.entities.location.Region;
import uz.alexits.cargostar.entities.location.TransitPoint;
import uz.alexits.cargostar.entities.calculation.Zone;
import uz.alexits.cargostar.entities.calculation.ZoneSettings;
import uz.alexits.cargostar.entities.calculation.Packaging;
import uz.alexits.cargostar.entities.calculation.PackagingType;
import uz.alexits.cargostar.entities.transportation.Consignment;
import uz.alexits.cargostar.entities.transportation.Invoice;
import uz.alexits.cargostar.entities.transportation.Request;
import uz.alexits.cargostar.entities.calculation.Provider;
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
import uz.alexits.cargostar.entities.transportation.Route;
import uz.alexits.cargostar.entities.transportation.Transportation;
import uz.alexits.cargostar.entities.transportation.TransportationData;
import uz.alexits.cargostar.entities.transportation.TransportationStatus;

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

    public Response<List<Country>> getCountries(final int perPage, final long lastId) throws IOException {
        return apiService.getCountries(perPage, lastId).execute();
    }

    public Response<List<Region>> getRegions(final int perPage) throws IOException {
        return apiService.getRegions(perPage).execute();
    }

    public Response<List<Region>> getRegions(final int perPage, final long lastId) throws IOException {
        return apiService.getRegions(perPage, lastId).execute();
    }

    public Response<List<City>> getCities(final int perPage) throws IOException {
        return apiService.getCities(perPage).execute();
    }

    public Response<List<City>> getCities(final int perPage, final long lastId) throws IOException {
        return apiService.getCities(perPage, lastId).execute();
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
    public Response<Client> createClient(final CreateClientParams params) throws IOException {
        return apiService.createClient(params).execute();
    }

    public Response<Client> getClient(final long clientId) throws IOException {
        return apiService.getClient(clientId).execute();
    }

    public Response<List<Client>> getClients(final int perPage) throws IOException {
        return apiService.getClients(perPage).execute();
    }

    public Response<List<Client>> getClients(final int perPage, final long lastId) throws IOException {
        return apiService.getClients(perPage, lastId).execute();
    }

    /* Courier */
    public Response<Courier> signIn(final SignInParams params) throws IOException {
        return apiService.signIn(params).execute();
    }

    public Response<Request> bindRequest(final long requestId, final BindRequestParams params) throws IOException {
        return apiService.bindRequest(requestId, params).execute();
    }

    public Response<Courier> updateCourierData(final long courierId, final UpdateCourierParams params) throws IOException {
        return apiService.updateCourierData(courierId, params).execute();
    }

    /* Address Book */
    public Response<AddressBook> getAddressBookEntry(final long entryId) throws IOException {
        return apiService.getAddressBookData(entryId).execute();
    }

    /* Invoice */
    public Response<CreateInvoiceResponse> createInvoice(@NonNull final CreateInvoiceParams createInvoiceParams) throws IOException {
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

    public Response<Invoice> sendRecipientSignature(final AddresseeParams params) throws IOException {
        return apiService.sendRecipientSignature(params).execute();
    }

    /* Transportation */
    public Response<Transportation> getTransportation(final long transportationId) throws IOException {
        return apiService.getTransportation(transportationId).execute();
    }

    public Response<Transportation> getTransportation(final String trackingCode) throws IOException {
        return apiService.getTransportation(trackingCode).execute();
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

    public Response<List<TransportationData>> getTransportationData(final long transportationId) throws IOException {
        return apiService.getTransportationData(transportationId).execute();
    }

    public Response<List<Route>> getTransportationRoute(final long transportationId) throws IOException {
        return apiService.getTransportationRoute(transportationId).execute();
    }

    public Response<Transportation> updateTransportationStatus(final TransportationStatusParams params) throws IOException {
        return apiService.updateTransportationStatus(params).execute();
    }

    public Response<Transportation> updatePartialStatus(final TransportationStatusParams params) throws IOException {
        return apiService.updatePartialStatus(params).execute();
    }

    /* Cargo */
    public Response<List<Consignment>> getCargoListByInvoiceId(final long invoiceId) throws IOException {
        return apiService.getCargoListByInvoiceId(invoiceId).execute();
    }

    public Response<List<Consignment>> getCargoListByRequestId(final long requestId) throws IOException {
        return apiService.getCargoListByRequestId(requestId).execute();
    }

    public Response<List<Client>> getAllCustomers(final int perPage) throws IOException {
        return apiService.getAllCustomers(perPage).execute();
    }

    private static final String TAG = RetrofitClient.class.toString();
}
