package com.example.cargostar.model.location;

import androidx.annotation.NonNull;

public class Address {
    @NonNull private String country;
    @NonNull private String region;
    @NonNull private String city;
    @NonNull private String address;
    private String zip;
    private Point geolocation;

    public Address(@NonNull final String country, @NonNull final String region, @NonNull final String city, @NonNull final String address) {
        this.country = country;
        this.region = region;
        this.city = city;
        this.address = address;
    }

    @NonNull
    public String getCountry() {
        return country;
    }

    public void setCountry(@NonNull final String country) {
        this.country = country;
    }

    @NonNull
    public String getRegion() {
        return region;
    }

    public void setRegion(@NonNull final String region) {
        this.region = region;
    }

    @NonNull
    public String getCity() {
        return city;
    }

    public void setCity(@NonNull final String city) {
        this.city = city;
    }

    @NonNull
    public String getAddress() {
        return address;
    }

    public void setAddress(@NonNull final String address) {
        this.address = address;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Point getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(Point geolocation) {
        this.geolocation = geolocation;
    }

    @NonNull
    @Override
    public String toString() {
        return "Address{" +
                "country='" + country + '\'' +
                ", region='" + region + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", zip='" + zip + '\'' +
                ", geolocation=" + geolocation +
                '}';
    }
}