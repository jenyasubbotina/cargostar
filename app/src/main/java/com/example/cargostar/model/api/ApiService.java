package com.example.cargostar.model.api;

import com.google.gson.JsonElement;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ApiService {
    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("transit-point")
    Call<JsonElement> getTransitPoints();

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("packaging-type")
    Call<JsonElement> getPackagingTypes();

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("packaging")
    Call<JsonElement> getPackaging();

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("zone")
    Call<JsonElement> getZones();

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("zone-setting")
    Call<JsonElement> getZoneSettings();

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("country")
    Call<JsonElement> getCountries();

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("region")
    Call<JsonElement> getRegions();

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("city")
    Call<JsonElement> getCities();

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("address-book")
    Call<JsonElement> getAddressBook();
}
