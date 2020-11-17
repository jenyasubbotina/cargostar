package uz.alexits.cargostar.api;

import retrofit2.http.Body;
import uz.alexits.cargostar.api.params.BindRequestParams;
import uz.alexits.cargostar.api.params.CreateClientParams;
import uz.alexits.cargostar.api.params.CreateInvoiceParams;
import uz.alexits.cargostar.api.params.CreateInvoiceResponse;
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
import uz.alexits.cargostar.model.shipping.Consignment;
import uz.alexits.cargostar.model.shipping.Invoice;
import uz.alexits.cargostar.model.shipping.Request;
import uz.alexits.cargostar.model.calculation.Provider;

import com.google.gson.JsonElement;

import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import uz.alexits.cargostar.model.transportation.Route;
import uz.alexits.cargostar.model.transportation.Transportation;
import uz.alexits.cargostar.model.transportation.TransportationData;
import uz.alexits.cargostar.model.transportation.TransportationStatus;

public interface ApiService {
    /* Location data */
    @GET("country")
    Call<List<Country>> getCountries(@Query("per-page") final int perPage);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("region")
    Call<List<Region>> getRegions(@Query("per-page") final int perPage);

    @Headers({"Content-Type: application/json; charset=utf-8;"})
    @GET("city")
    Call<List<City>> getCities(@Query("per-page") final int perPage);

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

    /*Requests requests */
    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("request")
    Call<List<Request>> getPublicRequests(@Query("per-page") final int perPage);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("request/employee")
    Call<List<Request>> getMyRequests(@Query("per-page") final int perPage, @Query("id") final long courierId);

    @Headers("Content-type: application/json; charset=utf-8;")
    @PUT("request/update")
    Call<Request> bindRequest(@Query("id") final long requestId, @Body BindRequestParams bindRequestParams);

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

    /* Client */
    @Headers("Content-type: application/json; charset=utf-8;")
    @POST("client/create-client")
    Call<Customer> createClient(@Body CreateClientParams createClientParams);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("client/view")
    Call<Customer> getClient(@Query("id") final long clientId);

    /* Courier */
    @Headers("Content-type: application/json; charset=utf-8;")
    @POST("employee/auth")
    Call<Courier> signIn(@Body SignInParams signInParams);

    @Headers("Content-type: application/json; charset=utf-8;")
    @PUT("employee/update")
    Call<Courier> updateCourierData(@Query("id") final long courierId, @Body UpdateCourierParams updatedCourierParams);

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

    /* Address Book */
    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("address-book/view")
    Call<AddressBook> getAddressBookData(@Query("id") final long recipientPayerId);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("address-book")
    Call<List<AddressBook>> getAddressBook(@Query("per-page") final int perPage);

    /* Transportation */
    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("transportation")
    Call<List<Transportation>> getCurrentTransportations(@Query("per-page") final int perPage);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("status-transport")
    Call<List<TransportationStatus>> getTransportationStatusList(@Query("per-page") final int perPage);

    //transportation history
    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("transportation-status/transportation")
    Call<List<TransportationData>> getTransportationData(@Query("id") final Long transportationId);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("transportation/points")
    Call<List<Route>> getTransportationRoute(@Query("id") final Long transportationId);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @POST("transportation-status/create")
    Call<Transportation> updateTransportationStatus(@Body TransportationStatusParams transportationStatusParams);

    /* Cargo */
    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("consignment/invoice")
    Call<List<Consignment>> getCargoListByInvoiceId(@Query("id") final Long invoiceId);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("client")
    Call<List<Customer>> getAllCustomers(@Query("per-page") final int perPage);
}
