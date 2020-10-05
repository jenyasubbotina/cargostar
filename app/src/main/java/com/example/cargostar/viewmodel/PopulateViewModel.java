package com.example.cargostar.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.cargostar.model.Notification;
import com.example.cargostar.model.TransportationStatus;
import com.example.cargostar.model.actor.AddressBook;
import com.example.cargostar.model.actor.Courier;
import com.example.cargostar.model.actor.Customer;
import com.example.cargostar.model.actor.PassportData;
import com.example.cargostar.model.actor.PaymentData;
import com.example.cargostar.model.database.Repository;
import com.example.cargostar.model.location.Branch;
import com.example.cargostar.model.location.City;
import com.example.cargostar.model.location.Country;
import com.example.cargostar.model.location.Region;
import com.example.cargostar.model.location.TransitPoint;
import com.example.cargostar.model.shipping.Cargo;
import com.example.cargostar.model.shipping.Consolidation;
import com.example.cargostar.model.shipping.Parcel;
import com.example.cargostar.model.shipping.Receipt;
import com.example.cargostar.model.shipping.ReceiptTransitPointCrossRef;
import com.example.cargostar.model.shipping.ReceiptWithCargoList;

import java.util.List;

public class PopulateViewModel extends AndroidViewModel {
    private final Repository repository;
    private LiveData<List<Courier>> courierList;

    public PopulateViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);
        this.courierList = repository.selectAllCouriers();
    }

    /* Courier queries */
    public long createCourier(final Courier newCourier) {
        return repository.createCourier(newCourier);
    }

    public LiveData<Courier> selectCourier(final String userId) {
        return repository.selectCourier(userId);
    }

    public LiveData<List<Courier>> selectAllCouriers() {
        return courierList;
    }

    public void dropCouriers() {
        repository.dropCouriers();
    }

    public LiveData<Courier> selectCourierByLogin(final String login) {
        return repository.selectCourierByLogin(login);
    }

    /*Request queries*/
    public long createRequest(final Receipt newRequest) {
        return repository.createRequest(newRequest);
    }

    public void createRequests(final List<Receipt> newRequestList) {
        repository.createRequests(newRequestList);
    }

    public long createParcelTransitPointCrossRef(final ReceiptTransitPointCrossRef receiptTransitPointCrossRef) {
        return repository.createParcelTransitPointCrossRef(receiptTransitPointCrossRef);
    }

    public void dropRequests() {
        repository.dropReceipts();
    }

    public void deleteRequest(final long requestId) {
        repository.deleteReceipt(requestId);
    }

    public LiveData<Receipt> selectReceipt(final long receiptId) {
        return repository.selectReceipt(receiptId);
    }

    public LiveData<List<Parcel>> selectCurrentParcels(final long courierId) {
        return repository.selectCurrentParcels(courierId);
    }

    public LiveData<List<Parcel>> selectAllParcels() {
        return repository.selectAllParcels();
    }

    public LiveData<List<Receipt>> selectAllReceipts() {
        return repository.selectAllReceipts();
    }

    /*Cargo queries*/
    public void createCargo(final List<Cargo> cargoList) {
        repository.createCargo(cargoList);
    }

    public void createCargo(final Cargo cargo) {
        repository.createCargo(cargo);
    }

    public void updateCargo(final Cargo updatedCargo) {
        repository.updateCargo(updatedCargo);
    }

    /* Location Queries */
    public long createCountry(final Country newCountry) {
        return repository.createCountry(newCountry);
    }

    public long[] createCountries(final List<Country> countryList) {
        return repository.createCountries(countryList);
    }

    public LiveData<List<Country>> selectAllCountries() {
        return repository.selectAllCountries();
    }

    public void dropCountries() {
        repository.dropCountries();
    }

    public long createRegion(final Region newRegion) {
        return repository.createRegion(newRegion);
    }

    public long[] createRegions(final List<Region> regionList) {
        return repository.createRegions(regionList);
    }

    public LiveData<List<Region>> selectAllRegions() {
        return repository.selectAllRegions();
    }

    public LiveData<List<Region>> selectRegionsByCountry(final long countryId) {
        return repository.selectRegionsByCountry(countryId);
    }

    public void dropRegions() {
        repository.dropRegions();
    }

    public long createCity(final City newCity) {
        return repository.createCity(newCity);
    }

    public long[] createCities(final List<City> cityList) {
        return repository.createCities(cityList);
    }

    public LiveData<List<City>> selectAllCities() {
        return repository.selectAllCities();
    }

    public LiveData<List<City>> selectCitiesByRegion(final long regionId) {
        return repository.selectAllCitiesByRegion(regionId);
    }

    public LiveData<List<City>> selectCitiesByCountry(final long countryId) {
        return repository.selectAllCitiesByCountry(countryId);
    }

    public void dropCities() {
        repository.dropCities();
    }

    /*branches*/
    public long createBranch(final Branch newBranch) {
        return repository.createBranch(newBranch);
    }

    public long[] createBranches(final List<Branch> branchList) {
        return repository.createBranches(branchList);
    }

    public LiveData<List<Branch>> selectAllBranches() {
        return repository.selectAllBranches();
    }

    public LiveData<List<Branch>> selectBranchesByCity(final long cityId) {
        return repository.selectAllBranchesByCity(cityId);
    }

    public LiveData<List<Branch>> selectBranchesByRegion(final long regionId) {
        return repository.selectAllBranchesByRegion(regionId);
    }

    public LiveData<List<Branch>> selectBranchesByCountry(final long countryId) {
        return repository.selectAllBranchesByCountry(countryId);
    }

    public void dropBranches() {
        repository.dropBranches();
    }

    /*transit points*/
    public long createTransitPoint(final TransitPoint transitPoint) {
        return repository.createTransitPoint(transitPoint);
    }

    public long[] createTransitPoint(final List<TransitPoint> transitPointList) {
        return repository.createTransitPoint(transitPointList);
    }

    public void updateTransitPoint(final TransitPoint updatedTransitPoint) {
        repository.updateTransitPoint(updatedTransitPoint);
    }

    public void dropTransitPoints() {
        repository.dropTransitPoints();
    }

    /*address book*/
    public long createAddressBookEntry(final AddressBook addressBook) {
        return repository.createAddressBookEntry(addressBook);
    }

    public void updateAddressBookEntry(final AddressBook updatedAddressBook) {
        repository.updateAddressBookEntry(updatedAddressBook);
    }

    /*Consolidation queries*/
    public long[] createConsolidation(final List<Consolidation> consolidationList) {
        return repository.createConsolidation(consolidationList);
    }

    public long createConsolidation(final Consolidation consolidation) {
        return repository.createConsolidation(consolidation);
    }

    public void updateConsolidation(final Consolidation updatedConsolidation) {
        repository.updateConsolidation(updatedConsolidation);
    }

    public LiveData<List<Parcel>> selectParcelsByConsolidationNumber(final long consolidationNumber) {
        return repository.selectParcelsByConsolidationNumber(consolidationNumber);
    }

    public LiveData<Parcel> selectParcelByConsolidationNumber(final long consolidationNumber, final long parcelId) {
        return repository.selectParcelByConsolidationNumber(consolidationNumber, parcelId);
    }

    //Notification queries
    public void createNotification(final List<Notification> notificationList) {
        repository.createNotification(notificationList);
    }

    public void createNotification(final Notification notification) {
        repository.createNotification(notification);
    }

    public void updateNotification(final Notification updatedNotification) {
        repository.updateNotification(updatedNotification);
    }
}
