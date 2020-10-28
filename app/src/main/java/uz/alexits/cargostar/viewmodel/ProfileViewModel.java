package uz.alexits.cargostar.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import uz.alexits.cargostar.model.actor.Courier;
import uz.alexits.cargostar.database.cache.Repository;

public class ProfileViewModel extends AndroidViewModel {
    private final Repository repository;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);
    }

//    public LiveData<Country> selectCountryByCourierId(final long courierId) {
//        return repository.selectCountryByCourierId(courierId);
//    }
//
//    public LiveData<Region> selectRegionByCourierId(final long courierId) {
//        return repository.selectRegionByCourierId(courierId);
//    }
//
//    public LiveData<City> selectCityByCourierId(final long courierId) {
//        return repository.selectCityByCourierId(courierId);
//    }

    public void updateCourier(final Courier courier) {
        repository.updateCourier(courier);
    }
}
