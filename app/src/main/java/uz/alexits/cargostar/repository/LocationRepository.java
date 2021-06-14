package uz.alexits.cargostar.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.OverwritingInputMerger;
import androidx.work.WorkManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.dao.LocationDao;
import uz.alexits.cargostar.entities.location.Branche;
import uz.alexits.cargostar.entities.location.City;
import uz.alexits.cargostar.entities.location.Country;
import uz.alexits.cargostar.entities.location.Region;
import uz.alexits.cargostar.entities.location.TransitPoint;
import uz.alexits.cargostar.workers.location.FetchCitiesWorker;
import uz.alexits.cargostar.workers.location.FetchCountriesWorker;
import uz.alexits.cargostar.workers.location.FetchRegionsWorker;

public class LocationRepository {
    private final Context context;
    private final LocationDao locationDao;

    public LocationRepository(final Context context) {
        this.context = context;
        this.locationDao = LocalCache.getInstance(context).locationDao();
    }

    public LiveData<List<Country>> selectAllCountries() {
        return locationDao.selectAllCountries();
    }

    public LiveData<List<Region>> selectAllRegions() {
        return locationDao.selectAllRegions();
    }

    public LiveData<List<City>> selectAllCities() {
        return locationDao.selectAllCities();
    }

    public LiveData<List<City>> selectCitiesByCountryId(final long countryId) {
        return locationDao.selectCitiesByCountryId(countryId);
    }

    public LiveData<List<City>> selectCitiesByRegionId(final long regionId) {
        return locationDao.selectCitiesByRegionId(regionId);
    }

    public LiveData<List<Branche>> selectAllBranches() {
        return locationDao.selectAllBranches();
    }

    public LiveData<Branche> selectBrancheById(final long brancheId) {
        return locationDao.selectBrancheById(brancheId);
    }

    public LiveData<List<TransitPoint>> selectAllTransitPoints() {
        return locationDao.selectAllTransitPoints();
    }

    public LiveData<TransitPoint> selectTransitPointById(final long transitPointId) {
        return locationDao.selectTransitPoint(transitPointId);
    }

    public LiveData<TransitPoint> selectTransitPointByBranch(final long branchId) {
        return locationDao.selectTransitPointByBranch(branchId);
    }

    public LiveData<List<TransitPoint>> selectAllTransitPointsByCity(final long cityId) {
        return locationDao.selectAllTransitPointsByCity(cityId);
    }

    public LiveData<List<TransitPoint>> selectAllTransitPointsByCountry(final long countryId) {
        return locationDao.selectAllTransitPointsByCountry(countryId);
    }

    public LiveData<Country> selectCountryById(final long countryId) {
        return locationDao.selectCountryById(countryId);
    }

    public LiveData<Region> selectRegionById(final long regionId) {
        return locationDao.selectRegionById(regionId);
    }

    public LiveData<List<Region>> selectRegionsByCountryId(final long countryId) {
        return locationDao.selectRegionsByCountryId(countryId);
    }

    public LiveData<City> selectCityById(final long cityId) {
        return locationDao.selectCityById(cityId);
    }

    public LiveData<City> selectCityByTransitPointId(final long transitPointId) {
        return locationDao.selectCityByTransitPointId(transitPointId);
    }

    public LiveData<Long> selectDestinationCountryIdByRequestId(final long requestId) {
        return locationDao.selectDestinationCountryIdByRequestId(requestId);
    }

    /* Location Data */
    public UUID fetchLocationData() {
        final Constraints dbConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final OneTimeWorkRequest fetchCountryDataRequest = new OneTimeWorkRequest.Builder(FetchCountriesWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchRegionDataRequest = new OneTimeWorkRequest.Builder(FetchRegionsWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchCityDataRequest = new OneTimeWorkRequest.Builder(FetchCitiesWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context)
                .beginWith(fetchCountryDataRequest)
                .then(fetchRegionDataRequest)
                .then(fetchCityDataRequest)
                .enqueue();
        return fetchCityDataRequest.getId();
    }
}
