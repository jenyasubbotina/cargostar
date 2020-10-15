package com.example.cargostar.model.api;

import com.example.cargostar.model.location.City;
import com.example.cargostar.model.location.Country;
import com.example.cargostar.model.location.Region;
import com.example.cargostar.model.location.TransitPoint;
import com.google.gson.JsonElement;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ApiService {
    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("transit-point")
    Call<List<TransitPoint>> getTransitPoints();

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
    Call<List<Country>> getCountries();

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("region")
    Call<List<Region>> getRegions();

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("city")
    Call<List<City>> getCities();

    @Headers("Content-Type: application/json; charset=utf-8;")
    @GET("address-book")
    Call<JsonElement> getAddressBook();
}
