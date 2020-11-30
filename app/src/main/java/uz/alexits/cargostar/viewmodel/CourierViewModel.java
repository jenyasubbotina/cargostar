package uz.alexits.cargostar.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.model.actor.Courier;
import uz.alexits.cargostar.database.cache.Repository;
import uz.alexits.cargostar.model.location.Branche;

public class CourierViewModel extends AndroidViewModel {
    private final Repository repository;
    private final MutableLiveData<Long> countryIdLiveData;
    private final MutableLiveData<Long> regionIdLiveData;
    private final MutableLiveData<Long> cityIdLiveData;

    public CourierViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);
        this.countryIdLiveData = new MutableLiveData<>();
        this.regionIdLiveData = new MutableLiveData<>();
        this.cityIdLiveData = new MutableLiveData<>();
    }

    public LiveData<Courier> selectCourierByLogin(final String login) {
        return repository.selectCourierByLogin(login);
    }

    public LiveData<Branche> selectBrancheById(final long brancheId) {
        return repository.selectBrancheById(brancheId);
    }

//    public LiveData<Request> getInvoice(final long requestId) {
//        return repository.selectRequest(requestId);
//    }

    public LiveData<Integer> selectNewNotificationsCount() {
        return repository.selectNewNotificationsCount();
    }

    public void createCourier(@NonNull final Courier courier) {
        repository.createCourier(courier);
    }

    public void setCourierCountryId(final long courierCountryId) {
        this.countryIdLiveData.setValue(courierCountryId);
    }

    public void setCourierRegionId(final long courierRegionId) {
        this.regionIdLiveData.setValue(courierRegionId);
    }

    public void setCourierCityId(final long courierCityId) {
        this.cityIdLiveData.setValue(courierCityId);
    }

    public LiveData<Country> getCourierCountry() {
        return Transformations.switchMap(countryIdLiveData, repository::selectCountryById);
    }

    public LiveData<Region> getCourierRegion() {
        return Transformations.switchMap(regionIdLiveData, repository::selectRegionById);
    }

    public LiveData<City> getCourierCity() {
        return Transformations.switchMap(cityIdLiveData, repository::selectCityById);
    }
}
