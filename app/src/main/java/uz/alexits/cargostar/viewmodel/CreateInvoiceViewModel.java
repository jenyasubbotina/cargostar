package uz.alexits.cargostar.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import uz.alexits.cargostar.model.actor.AddressBook;
import uz.alexits.cargostar.database.cache.Repository;
import uz.alexits.cargostar.model.calculation.CountryIdProviderId;
import uz.alexits.cargostar.model.calculation.Packaging;
import uz.alexits.cargostar.model.calculation.PackagingType;
import uz.alexits.cargostar.model.calculation.Provider;
import uz.alexits.cargostar.model.calculation.TypePackageIdList;
import uz.alexits.cargostar.model.calculation.Zone;
import uz.alexits.cargostar.model.calculation.ZoneSettings;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.Region;

import java.util.List;

public class CreateInvoiceViewModel extends AndroidViewModel {
    private final Repository repository;

    private final LiveData<List<Country>> countryList;

    /* sender data */
    private final MutableLiveData<Long> senderCountryId;
    private final MutableLiveData<Long> senderRegionId;
    private final MutableLiveData<Long> senderCityId;

    /* recipient data */
    private final MutableLiveData<Long> recipientCountryId;
    private final MutableLiveData<Long> recipientRegionId;
    private final MutableLiveData<Long> recipientCityId;

    /* payer data */
    private final MutableLiveData<Long> payerCountryId;
    private final MutableLiveData<Long> payerRegionId;
    private final MutableLiveData<Long> payerCityId;

//    /* calculation data */
//    private final MutableLiveData<Long> providerId;
//    private final MutableLiveData<Long> packagingId;
//    private final MutableLiveData<Integer> type;
//
//    private final MutableLiveData<TypePackageIdList> typePackageIdList;
//    private final MutableLiveData<CountryIdProviderId> countryIdProviderId;
//
//    private final MutableLiveData<List<Long>> zoneIds;

    public CreateInvoiceViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);

        this.countryList = repository.selectAllCountries();

        this.senderCountryId = new MutableLiveData<>();
        this.senderRegionId = new MutableLiveData<>();
        this.senderCityId = new MutableLiveData<>();

        this.recipientCountryId = new MutableLiveData<>();
        this.recipientRegionId = new MutableLiveData<>();
        this.recipientCityId = new MutableLiveData<>();

        this.payerCountryId = new MutableLiveData<>();
        this.payerRegionId = new MutableLiveData<>();
        this.payerCityId = new MutableLiveData<>();

//        this.providerId = new MutableLiveData<>();
//        this.packagingId = new MutableLiveData<>();
//        this.type = new MutableLiveData<>();
//
//        this.typePackageIdList = new MutableLiveData<>();
//        this.countryIdProviderId = new MutableLiveData<>();
//
//        this.zoneIds = new MutableLiveData<>();
    }

    /* location data */
    public LiveData<List<Country>> getCountryList() {
        return countryList;
    }

    public LiveData<List<Region>> getSenderRegionList() {
        return Transformations.switchMap(senderCountryId, repository::selectRegionsByCountryId);
    }

    public LiveData<List<Region>> getRecipientRegionList() {
        return Transformations.switchMap(recipientCountryId, repository::selectRegionsByCountryId);
    }

    public LiveData<List<Region>> getPayerRegionList() {
        return Transformations.switchMap(payerCountryId, repository::selectRegionsByCountryId);
    }

    public LiveData<List<City>> getSenderCityList() {
        return Transformations.switchMap(senderRegionId, repository::selectCitiesByRegionId);
    }

    public LiveData<List<City>> getRecipientCityList() {
        return Transformations.switchMap(recipientRegionId, repository::selectCitiesByRegionId);
    }

    public LiveData<List<City>> getPayerCityList() {
        return Transformations.switchMap(payerRegionId, repository::selectCitiesByRegionId);
    }

    public void setSenderCountryId(final long senderCountryId) {
        this.senderCountryId.setValue(senderCountryId);
    }

    public void setRecipientCountryId(final long recipientCountryId) {
        this.recipientCountryId.setValue(recipientCountryId);
    }

    public void setPayerCountryId(final long payerCountryId) {
        this.payerCountryId.setValue(payerCountryId);
    }

    public void setSenderRegionId(final long senderRegionId) {
        this.senderRegionId.setValue(senderRegionId);
    }

    public void setRecipientRegionId(final long recipientRegionId) {
        this.recipientRegionId.setValue(recipientRegionId);
    }

    public void setPayerRegionId(final long payerRegionId) {
        this.payerRegionId.setValue(payerRegionId);
    }

    public void setSenderCityId(final long senderCityId) {
        this.senderCityId.setValue(senderCityId);
    }

    public void setRecipientCityId(final long recipientCityId) {
        this.recipientCityId.setValue(recipientCityId);
    }

    public void setPayerCityId(final long payerCityId) {
        this.payerCityId.setValue(payerCityId);
    }

    /* address book data*/
    public LiveData<AddressBook> selectAddressBookEntryById(final long id) {
        return repository.selectAddressBookEntryById(id);
    }

    public LiveData<List<AddressBook>> selectAddressBookEntries() {
        return repository.selectAddressBookEntries();
    }

//    /* calculation data */
//    public void setProviderId(final Long providerId) {
//        this.providerId.setValue(providerId);
//    }
//
//    public void setPackagingId(final Long packagingId) {
//        this.packagingId.setValue(packagingId);
//    }
//
//    public void setType(final Integer type) {
//        this.type.setValue(type);
//    }
//
//    public void setTypePackageIdList(final Integer type, final List<Long> packagingIds) {
//        this.typePackageIdList.setValue(new TypePackageIdList(type, packagingIds));
//    }
//
//    public void setCountryIdProviderId(final Long countryId, final Long providerId) {
//        this.countryIdProviderId.setValue(new CountryIdProviderId(countryId, providerId));
//    }
//
//    public void setZoneIds(final List<Long> zoneIds) {
//        this.zoneIds.setValue(zoneIds);
//    }
//
//    public LiveData<Provider> getProvider() {
//        return Transformations.switchMap(providerId, repository::selectProviderById);
//    }
//
//    public LiveData<Integer> getType() {
//        return type;
//    }
//
//    public LiveData<List<Long>> getPackagingIds() {
//        Log.i(TAG, "providerId: " + providerId.getValue());
//        return Transformations.switchMap(providerId, repository::selectPackagingIdsByProviderId);
//    }
//
//    public LiveData<List<PackagingType>> getPackagingTypeList() {
//        return Transformations.switchMap(typePackageIdList, input ->
//                repository.selectPackagingTypesByTypeAndPackagingIds(input.getType(), input.getPackagingIdList()));
//    }
//
//    public LiveData<Packaging> getPackaging() {
//        return Transformations.switchMap(packagingId, repository::selectPackagingById);
//    }
//
//    public LiveData<List<ZoneSettings>> getZoneSettingsList() {
//        return Transformations.switchMap(zoneIds, repository::selectZoneSettingsByZoneIds);
//    }
//
//    public LiveData<List<Zone>> getZoneList() {
//        return Transformations.switchMap(countryIdProviderId, input ->
//                repository.selectZoneListByCountryIdAndProviderId(input.getCountryId(), input.getProviderId()));
//    }

//    public void updateReceipt(final Receipt updatedReceipt) {
//        repository.updateReceipt(updatedReceipt);
//    }

//    public LiveData<Customer> selectCustomer(final String userId) {
//        return repository.selectCustomer(userId);
//    }
//
//    public LiveData<Customer> selectCustomerByLogin(final String senderLogin) {
//        return repository.selectCustomerByLogin(senderLogin);
//    }

    private static final String TAG = CreateInvoiceViewModel.class.toString();
}
