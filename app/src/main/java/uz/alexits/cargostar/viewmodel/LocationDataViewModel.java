package uz.alexits.cargostar.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

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
    private final LiveData<List<Region>> regionList;
    private final LiveData<List<City>> cityList;
    private final LiveData<List<Branche>> brancheList;
    private final LiveData<List<TransitPoint>> transitPointList;

    public LocationDataViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);
        this.countryList = repository.selectAllCountries();
        this.regionList = repository.selectAllRegions();
        this.cityList = repository.selectAllCities();
        this.brancheList = repository.selectAllBranches();
        this.transitPointList = repository.selectAllTransitPoints();
    }

    public LiveData<List<Country>> getCountryList() {
        return countryList;
    }

    public LiveData<List<Region>> getRegionList() {
        return regionList;
    }

    public LiveData<List<City>> getCityList() {
        return cityList;
    }

    public LiveData<List<TransitPoint>> getTransitPointList() {
        return transitPointList;
    }

    public LiveData<List<City>> getCitiesByCountryId(final long countryId) {
        return repository.selectCitiesByCountryId(countryId);
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

    public LiveData<City> getCityById(final long cityId) {
        return repository.selectCityById(cityId);
    }
}
