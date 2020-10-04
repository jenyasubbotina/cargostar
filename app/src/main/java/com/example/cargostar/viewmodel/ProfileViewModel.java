package com.example.cargostar.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.cargostar.model.actor.Courier;
import com.example.cargostar.model.database.Repository;
import com.example.cargostar.model.location.City;
import com.example.cargostar.model.location.Country;
import com.example.cargostar.model.location.Region;

public class ProfileViewModel extends AndroidViewModel {
    private final Repository repository;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);
    }

    public LiveData<Country> selectCountryByCourierId(final long courierId) {
        return repository.selectCountryByCourierId(courierId);
    }

    public LiveData<Region> selectRegionByCourierId(final long courierId) {
        return repository.selectRegionByCourierId(courierId);
    }

    public LiveData<City> selectCityByCourierId(final long courierId) {
        return repository.selectCityByCourierId(courierId);
    }

    public void updateCourier(final Courier courier) {
        repository.updateCourier(courier);
    }
}
