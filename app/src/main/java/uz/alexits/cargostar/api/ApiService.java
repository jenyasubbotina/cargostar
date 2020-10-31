package uz.alexits.cargostar.api;

import retrofit2.http.Body;
import uz.alexits.cargostar.api.params.BindRequestParams;
import uz.alexits.cargostar.api.params.CreateClientParams;
import uz.alexits.cargostar.api.params.CreateInvoiceParams;
import uz.alexits.cargostar.api.params.SignInParams;
import uz.alexits.cargostar.api.params.UpdateCourierParams;
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
import uz.alexits.cargostar.model.shipping.Request;
import uz.alexits.cargostar.model.calculation.Provider;

import com.google.gson.JsonElement;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

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
    @GET("zone-setting")
    Call<List<ZoneSettings>> getZoneSettings(@Query("per-page") final int perPage);

    /*Requests requests */
    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("request/empty-employee")
    Call<List<Request>> getPublicRequests();

//    @Headers("Content-Type: application/json; charset=utf-8;")
//    @PUT("request/update")
//    Call<JsonElement> updateRequest(@Query("id") final long id, @Body UpdateRequestParams updateRequestParams);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("request/employee")
    Call<List<Request>> getMyRequests(@Query("id") final long courierId);

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
    @GET("address-book")
    Call<JsonElement> getAddressBook();

    /* Courier */
    @Headers("Content-type: application/json; charset=utf-8;")
    @POST("employee/auth")
    //login: testc1
    //password: qweqwe
    Call<Courier> signIn(@Body SignInParams signInParams);

    @Headers("Content-type: application/json; charset=utf-8;")
    @PUT("request/update")
    Call<JsonElement> bindRequest(@Query("id") final long requestId, @Body BindRequestParams bindRequestParams);

    @Headers("Content-type: application/json; charset=utf-8;")
    @PUT("employee/update")
    Call<Courier> updateCourierData(@Query("id") final long courierId, @Body UpdateCourierParams updatedCourierParams);

    /* Invoice */
    @Headers("Content-type: application/json; charset=utf-8;")
    @POST("invoice/full-create")
    Call<JsonElement> createInvoice(@Body CreateInvoiceParams createInvoiceParams);

    /* Transportation */
//    https://cargo.alex-its.uz/api/transportation-status/transportation?id=10
    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("transportation")
    Call<List<JsonElement>> getCurrentTransportations();
}