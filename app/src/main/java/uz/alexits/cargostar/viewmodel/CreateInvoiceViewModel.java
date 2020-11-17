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

    /* address book */
    private final MutableLiveData<String> senderEmail;

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

        this.senderEmail = new MutableLiveData<>();
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

    public LiveData<List<AddressBook>> getSenderAddressBook() {
        return Transformations.switchMap(senderEmail, repository::selectAddressBookBySenderEmail);
    }

    public void setSenderEmail(final String senderEmail) {
        this.senderEmail.setValue(senderEmail);
    }

    private static final String TAG = CreateInvoiceViewModel.class.toString();
}
