package uz.alexits.cargostar.api;

import uz.alexits.cargostar.model.location.Branche;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.model.location.TransitPoint;
import uz.alexits.cargostar.model.packaging.Packaging;
import uz.alexits.cargostar.model.packaging.PackagingType;
import uz.alexits.cargostar.model.shipping.Request;
import uz.alexits.cargostar.model.packaging.Provider;

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
    Call<List<Branche>> getBranches();

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("transit-point")
    Call<List<TransitPoint>> getTransitPoints(@Query("per-page") final int perPage);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("zone")
    Call<JsonElement> getZones();

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("zone-setting")
    Call<JsonElement> getZoneSettings();

    /*Requests requests */
    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("request")
    Call<List<Request>> getPublicRequests();

    @Headers("Content-Type: application/json; charset=utf-8;")
    @PUT("request/update")
    Call<JsonElement> updateRequest(@Query("id") final long id);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @POST("request-additional/create")
    Call<JsonElement> bindRequest(@Query("request_id") final long requestId, @Query("employee)id") final long courierId);

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("address-book")
    Call<JsonElement> getAddressBook();

    /*Providers / packaging */
    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("provider")
    Call<List<Provider>> getProviders();

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("packaging-type")
    Call<List<PackagingType>> getPackagingTypes();

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("packaging")
    Call<List<Packaging>> getPackaging();
}
