package uz.alexits.cargostar.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.Repository;
import uz.alexits.cargostar.model.calculation.CountryIdProviderId;
import uz.alexits.cargostar.model.calculation.Packaging;
import uz.alexits.cargostar.model.calculation.PackagingType;
import uz.alexits.cargostar.model.calculation.Provider;
import uz.alexits.cargostar.model.calculation.TypePackageIdList;
import uz.alexits.cargostar.model.calculation.Vat;
import uz.alexits.cargostar.model.calculation.Zone;
import uz.alexits.cargostar.model.calculation.ZoneSettings;
import uz.alexits.cargostar.model.location.City;

import java.util.List;

public class CalculatorViewModel extends AndroidViewModel {
    private final Repository repository;

    private final MutableLiveData<Long> srcCountryId;
    private final MutableLiveData<Long> destCountryId;

    private final MutableLiveData<Long> providerId;
    private final MutableLiveData<Integer> type;

    private final MutableLiveData<TypePackageIdList> typePackageIdList;
    private final MutableLiveData<CountryIdProviderId> countryIdProviderId;

    private final MutableLiveData<List<Long>> zoneIds;
    private final MutableLiveData<List<Packaging>> packagingList;

    private final LiveData<Vat> vat;

    public CalculatorViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);

        this.srcCountryId = new MutableLiveData<>();
        this.destCountryId = new MutableLiveData<>();

        this.providerId = new MutableLiveData<>();
        this.type = new MutableLiveData<>();

        this.typePackageIdList = new MutableLiveData<>();
        this.countryIdProviderId = new MutableLiveData<>();

        this.zoneIds = new MutableLiveData<>();
        this.packagingList = new MutableLiveData<>();

        this.vat = repository.selectVat();
    }

    /* setters */
    public void setSrcCountryId(final Long srcCountryId) {
        if (srcCountryId == null) {
            this.srcCountryId.setValue(-1L);
            return;
        }
        this.srcCountryId.setValue(srcCountryId);
    }

    public void setDestCountryId(final Long destCountryId) {
        if (destCountryId == null) {
            this.destCountryId.setValue(-1L);
            return;
        }
        this.destCountryId.setValue(destCountryId);
    }

    public void setProviderId(final Long providerId) {
        if (providerId == null) {
            this.setProviderId(-1L);
            return;
        }
        this.providerId.setValue(providerId);
    }

    public void setType(final Integer type) {
        if (type == null) {
            this.type.setValue(-1);
            return;
        }
        this.type.setValue(type);
    }

    public void setTypePackageIdList(final Integer type, final List<Long> packagingIds) {
        this.typePackageIdList.setValue(new TypePackageIdList(type, packagingIds));
    }

    public void setCountryIdProviderId(final Long countryId, final Long providerId) {
        this.countryIdProviderId.setValue(new CountryIdProviderId(countryId, providerId));
        Log.i(TAG, "countryId=" + countryId + " providerId=" + providerId);
    }

    public void setZoneIds(final List<Long> zoneIds) {
        this.zoneIds.setValue(zoneIds);
    }

    /* getters */
    public LiveData<List<City>> getSrcCities() {
        return Transformations.switchMap(srcCountryId, repository::selectCitiesByCountryId);
    }

    public LiveData<List<City>> getDestCities() {
        return Transformations.switchMap(destCountryId, repository::selectCitiesByCountryId);
    }

    public LiveData<Provider> getProvider() {
        return Transformations.switchMap(providerId, repository::selectProviderById);
    }

    public LiveData<Integer> getType() {
        return type;
    }

    public LiveData<List<Long>> getPackagingIds() {
        return Transformations.switchMap(providerId, repository::selectPackagingIdsByProviderId);
    }

    public LiveData<List<PackagingType>> getPackagingTypeList() {
        return Transformations.switchMap(typePackageIdList, input ->
                repository.selectPackagingTypesByTypeAndPackagingIds(input.getType(), input.getPackagingIdList()));
    }

    public LiveData<List<ZoneSettings>> getZoneSettingsList() {
        return Transformations.switchMap(zoneIds, repository::selectZoneSettingsByZoneIds);
    }

    public LiveData<List<Zone>> getZoneList() {
        return Transformations.switchMap(countryIdProviderId, input ->
                repository.selectZoneListByCountryIdAndProviderId(input.getCountryId(), input.getProviderId()));
    }

//    public void setZoneSettingsIds(final List<Long> zoneSettingsIds) {
//        this.zoneSettingsIds.setValue(zoneSettingsIds);
//    }
//
    public LiveData<List<Packaging>> getTariffList() {
        return Transformations.switchMap(providerId, repository::selectPackagingsByProviderId);
    }

    public LiveData<Vat> getVat() {
        return vat;
    }

    private static final String TAG = CalculatorViewModel.class.toString();
}

