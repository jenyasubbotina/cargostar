package uz.alexits.cargostar.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import uz.alexits.cargostar.database.cache.Repository;
import uz.alexits.cargostar.model.location.Branche;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.model.location.TransitPoint;

import java.util.List;

public class LocationDataViewModel extends AndroidViewModel {
    private final Repository repository;

    private final LiveData<List<Country>> countryList;
    private final LiveData<List<Branche>> brancheList;
    private final LiveData<List<TransitPoint>> transitPointList;
    private final MutableLiveData<Long> countryId;

    public LocationDataViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);
        this.countryList = repository.selectAllCountries();
        this.brancheList = repository.selectAllBranches();
        this.transitPointList = repository.selectAllTransitPoints();
        this.countryId = new MutableLiveData<>();
    }

    public LiveData<List<Country>> getCountryList() {
        return countryList;
    }

    public void setCountryId(final long countryId) {
        this.countryId.setValue(countryId);
    }

    public LiveData<List<City>> getCityList() {
        return Transformations.switchMap(countryId, repository::selectCitiesByCountryId);
    }

    public LiveData<List<TransitPoint>> getTransitPointList() {
        return transitPointList;
    }

    public LiveData<List<City>> getCitiesByCountryId(final long countryId) {
        return repository.selectCitiesByCountryId(countryId);
    }

    public LiveData<List<City>> getCitiesByRegionId(final long regionId) {
        return repository.selectCitiesByRegionId(regionId);
    }

    public LiveData<List<Branche>> getBrancheList() {
        return brancheList;
    }

    public LiveData<Country> getCountryById(final long countryId) {
        return repository.selectCountryById(countryId);
    }

    public LiveData<Region> getRegionById(final long regionId) {
        return repository.selectRegionById(regionId);
    }

    public LiveData<List<Region>> getRegionsByCountryId(final long countryId) {
        return repository.selectRegionsByCountryId(countryId);
    }

    public LiveData<City> getCityById(final long cityId) {
        return repository.selectCityById(cityId);
    }
}
