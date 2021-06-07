package uz.alexits.cargostar.api;

import retrofit2.http.Body;
import uz.alexits.cargostar.entities.params.AddresseeParams;
import uz.alexits.cargostar.entities.params.BindRequestParams;
import uz.alexits.cargostar.entities.params.CreateClientParams;
import uz.alexits.cargostar.entities.params.CreateInvoiceParams;
import uz.alexits.cargostar.entities.params.CreateInvoiceResponse;
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
import uz.alexits.cargostar.entities.transportation.Addressee;
import uz.alexits.cargostar.entities.transportation.Consignment;
import uz.alexits.cargostar.entities.transportation.Import;
import uz.alexits.cargostar.entities.transportation.Invoice;
import uz.alexits.cargostar.entities.transportation.Partial;
import uz.alexits.cargostar.entities.transportation.Request;
import uz.alexits.cargostar.entities.calculation.Provider;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import uz.alexits.cargostar.entities.transportation.Route;
import uz.alexits.cargostar.entities.transportation.Transportation;
import uz.alexits.cargostar.entities.transportation.TransportationData;
import uz.alexits.cargostar.entities.transportation.TransportationStatus;

public interface ApiService {
    /* Location data */
    @GET("country")
    Call<List<Country>> getCountries(@Query("per-page") final int perPage);

    @GET("country/new/")
    Call<List<Country>> getCountries(@Query("per-page") final int perPage, @Query("id") final long lastId);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("region")
    Call<List<Region>> getRegions(@Query("per-page") final int perPage);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("region/new/")
    Call<List<Region>> getRegions(@Query("per-page") final int perPage, @Query("id") final long lastId);

    @Headers({"Content-Type: application/json; charset=utf-8;"})
    @GET("city")
    Call<List<City>> getCities(@Query("per-page") final int perPage);

    @Headers({"Content-Type: application/json; charset=utf-8;"})
    @GET("city/new/")
    Call<List<City>> getCities(@Query("per-page") final int perPage, @Query("id") final long lastId);

    @Headers({"Content-Type: application/json; charset=utf-8;"})
    @GET("branche")
    Call<List<Branche>> getBranches(@Query("per-page") final int perPage);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("transit-point")
    Call<List<TransitPoint>> getTransitPoints(@Query("per-page") final int perPage);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("zone")
    Call<List<Zone>> getZones(@Query("per-page") final int perPage);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("zone-country")
    Call<List<ZoneCountry>> getZoneCountries(@Query("per-page") final int perPage);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("zone-setting")
    Call<List<ZoneSettings>> getZoneSettings(@Query("per-page") final int perPage);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("setting")
    Call<Vat> getVat();

    /* Providers / packaging */
    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("provider")
    Call<List<Provider>> getProviders();

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("packaging-type")
    Call<List<PackagingType>> getPackagingTypes();

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("packaging")
    Call<List<Packaging>> getPackaging();

    /* Courier */
    @Headers("Content-type: application/json; charset=utf-8;")
    @POST("employee/auth")
    Call<Courier> signIn(@Body SignInParams signInParams);

    @Headers("Content-type: application/json; charset=utf-8;")
    @PUT("employee/update")
    Call<Courier> updateCourierData(@Query("id") final long courierId, @Body UpdateCourierParams updatedCourierParams);

    /* Client */
    @Headers("Content-type: application/json; charset=utf-8;")
    @POST("client/create-client")
    Call<Client> createClient(@Body CreateClientParams createClientParams);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("client/view")
    Call<Client> getClient(@Query("id") final long clientId);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("client/new")
    Call<List<Client>> getClients(@Query("per-page") final int perPage, @Query("id") final long lastId);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("client")
    Call<List<Client>> getClients(@Query("per-page") final int perPage);

    /* Address Book */
    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("address-book/view")
    Call<AddressBook> getAddressBookData(@Query("id") final long recipientPayerId);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("address-book")
    Call<List<AddressBook>> getAddressBook(@Query("per-page") final int perPage);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("address-book/new")
    Call<List<AddressBook>> getAddressBook(@Query("per-page") final int perPage, @Query("id") final long lastId);

    /* Requests */
    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("request")
    Call<List<Request>> getPublicRequests(@Query("per-page") final int perPage);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("request/new")
    Call<List<Request>> getPublicRequests(@Query("per-page") final int perPage, @Query("id") final long lastId);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("request/employee")
    Call<List<Request>> getMyRequests(@Query("per-page") final int perPage, @Query("id") final long courierId);

    @Headers("Content-type: application/json; charset=utf-8;")
    @PUT("request/update")
    Call<Request> bindRequest(@Query("id") final long requestId, @Body BindRequestParams bindRequestParams);

    @Headers("Content-type: application/json; charset=utf-8;")
    @GET("request/view")
    Call<Request> getRequest(@Query("id") final long requestId);

    /* Invoice */
    @Headers("Content-type: application/json; charset=utf-8;")
    @POST("invoice/full-create")
    Call<CreateInvoiceResponse> createInvoice(@Body CreateInvoiceParams createInvoiceParams);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("invoice/view")
    Call<Invoice> getInvoice(@Query("id") final long invoiceId);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("invoice")
    Call<List<Invoice>> getInvoiceList(@Query("per-page") final int perPage);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("invoice/new")
    Call<List<Invoice>> getInvoiceList(@Query("per-page") final int perPage, @Query("id") final long lastId);

    /* Transportation */
    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("transportation/view")
    Call<Transportation> getTransportation(@Query("id") final long transportationId);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("transportation/view")
    Call<Transportation> getTransportation(@Query("trackingCode") final String trackingCode);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("transportation")
    Call<List<Transportation>> getCurrentTransportations(@Query("per-page") final int perPage);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("transportation/new")
    Call<List<Transportation>> getCurrentTransportations(@Query("per-page") final int perPage, @Query("id") final long lastId);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("partial")
    Call<List<Partial>> getPartialData(@Query("per-page") final int perPage);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("status-transport")
    Call<List<TransportationStatus>> getTransportationStatusList(@Query("per-page") final int perPage);

    /* Transportation History */
    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("transportation-status/transportation")
    Call<List<TransportationData>> getTransportationData(@Query("id") final long transportationId);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("transportation/points")
    Call<List<Route>> getTransportationRoute(@Query("id") final long transportationId);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @POST("transportation-status/create")
    Call<Transportation> updateTransportationStatus(@Body TransportationStatusParams transportationStatusParams);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @POST("partial/status")
    Call<Transportation> updatePartialStatus(@Body TransportationStatusParams transportationStatusParams);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @POST("invoice/recipient")
    Call<Invoice> sendRecipientSignature(@Body AddresseeParams params);

    /* Cargo */
    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("consignment/invoice")
    Call<List<Consignment>> getCargoListByInvoiceId(@Query("id") final long invoiceId);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("request/partial")
    Call<List<Consignment>> getCargoListByRequestId(@Query("id") final long requestId);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("client")
    Call<List<Client>> getAllCustomers(@Query("per-page") final int perPage);
}
