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
    private Repository repository;
    private LiveData<List<Customer>> customerList;
    private LiveData<List<Courier>> courierList;

    private LiveData<List<Country>> countryList;
    private LiveData<List<Region>> regionList;
    private LiveData<List<City>> cityList;

    private LiveData<List<ReceiptWithCargoList>> requestList;
    private LiveData<List<ReceiptWithCargoList>> publicRequestList;
//    private LiveData<List<Parcel>> parcelList;

    public PopulateViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);
        this.customerList = repository.selectAllCustomers();
        this.courierList = repository.selectAllCouriers();
        this.countryList = repository.selectAllCountries();
        this.regionList = repository.selectAllRegions();
        this.cityList = repository.selectAllCities();

        this.requestList = repository.selectAllRequests();
        this.publicRequestList = repository.selectPublicRequests();
    }

    /* Courier queries */
    public long createCourier(final Courier newCourier) {
        return repository.createCourier(newCourier);
    }

    public void updateCourier(final Courier courier) {
        repository.updateCourier(courier);
    }

    public LiveData<Courier> selectCourier(final String userId) {
        return repository.selectCourier(userId);
    }

    public LiveData<Courier> selectCourierByLogin(final String login) {
        return repository.selectCourierByLogin(login);
    }

    public LiveData<List<Courier>> selectAllCouriers() {
        return courierList;
    }

    public void dropCouriers() {
        repository.dropCouriers();
    }

    /* Customer queries */
    public long createCustomer(final Customer newCustomer) {
        return repository.createCustomer(newCustomer);
    }

    public LiveData<Customer> selectCustomer(final String userId) {
        return repository.selectCustomer(userId);
    }

    public LiveData<List<Customer>> selectAllCustomers() {
        return customerList;
    }

    public void dropCustomers() {
        repository.dropCustomers();
    }

    public LiveData<Customer> selectCustomerByLogin(final String senderLogin) {
        return repository.selectCustomerByLogin(senderLogin);
    }

    /* Passport data queries */
    public void createPassportData(final PassportData passportData) {
        repository.createPassportData(passportData);
    }

    public LiveData<PassportData> selectPassportData(final String userId) {
        return repository.selectPassportData(userId);
    }

    public LiveData<List<PassportData>> selectAllPassportData() {
        return repository.selectAllPassportData();
    }

    public void dropPassportData() {
        repository.dropPassportData();
    }

    /* Payment data queries */
    public void createPaymentData(final PaymentData paymentData) {
        repository.createPaymentData(paymentData);
    }

    public LiveData<PaymentData> selectPaymentData(final String userId) {
        return repository.selectPaymentData(userId);
    }

    public LiveData<List<PaymentData>> selectAllPaymentData() {
        return repository.selectAllPaymentData();
    }

    public void dropPaymentData() {
        repository.dropPaymentData();
    }

    /*Request queries*/
    public LiveData<ReceiptWithCargoList> selectRequest(final long requestId) {
        return repository.selectRequest(requestId);
    }

    public LiveData<List<ReceiptWithCargoList>> selectAllRequests() {
        return requestList;
    }

    public LiveData<List<ReceiptWithCargoList>> selectPublicRequests() {
        return publicRequestList;
    }

    public LiveData<List<ReceiptWithCargoList>> selectMyRequests(final long courierId) {
        return repository.selectMyRequests(courierId);
    }

    public long createRequest(final Receipt newRequest) {
        return repository.createRequest(newRequest);
    }

    public void createRequests(final List<Receipt> newRequestList) {
        repository.createRequests(newRequestList);
    }

    public long createParcelTransitPointCrossRef(final ReceiptTransitPointCrossRef receiptTransitPointCrossRef) {
        return repository.createParcelTransitPointCrossRef(receiptTransitPointCrossRef);
    }

    public void updateReceipt(final Receipt updatedReceipt) {
        repository.updateReceipt(updatedReceipt);
    }

    public LiveData<List<Parcel>> selectParcelsByStatus(final long courierId, final TransportationStatus transportationStatus) {
        return repository.selectParcelsByStatus(courierId, transportationStatus);
    }

    public LiveData<List<Parcel>> selectParcelsByStatus(final long courierId, final TransportationStatus[] statusArray) {
        return repository.selectParcelsByStatus(courierId, statusArray);
    }

    public LiveData<List<Parcel>> selectParcelsByLocation(final long courierId, final long locationId) {
        return repository.selectParcelsByLocation(courierId, TransportationStatus.IN_TRANSIT, locationId);
    }

    public LiveData<List<Parcel>> selectParcelsByLocationAndStatus(final long courierId, final TransportationStatus[] statusList, final long locationId) {
        return repository.selectParcelsByLocationAndStatus(courierId, statusList, locationId);
    }

    public void dropRequests() {
        repository.dropReceipts();
    }

    public void deleteRequest(final long requestId) {
        repository.deleteReceipt(requestId);
    }

    public LiveData<Parcel> selectParcel(final long receiptId) {
        return repository.selectParcel(receiptId);
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

    public LiveData<Branch> selectBranchByCourierId(final long courierId) {
        return repository.selectBranchByCourierId(courierId);
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

    public LiveData<List<TransitPoint>> selectAllTransitPoints() {
        return repository.selectAllTransitPoints();
    }

    public LiveData<TransitPoint> selectTransitPoint(final long pointId) {
        return repository.selectTransitPoint(pointId);
    }

    public LiveData<TransitPoint> selectTransitPointByBranch(final long branchId) {
        return repository.selectTransitPointByBranch(branchId);
    }

    public LiveData<List<TransitPoint>> selectTransitPointsByCity(final long cityId) {
        return repository.selectAllTransitPointsByCity(cityId);
    }

    public LiveData<List<TransitPoint>> selectTransitPointsByCountry(final long countryId) {
        return repository.selectAllTransitPointsByCountry(countryId);
    }

    public void readReceipt(final long receiptId) {
        repository.readReceipt(receiptId);
    }

    /*Notification queries*/
    public void createNotification(final List<Notification> notificationList) {
        repository.createNotification(notificationList);
    }

    public void createNotification(final Notification notification) {
        repository.createNotification(notification);
    }

    public void updateNotification(final Notification updatedNotification) {
        repository.updateNotification(updatedNotification);
    }

    public LiveData<Notification> selectNotification(final long receiptId) {
        return repository.selectNotification(receiptId);
    }

    public LiveData<List<Notification>> selectAllNotifications() {
        return repository.selectAllNotifications();
    }

    public LiveData<List<Notification>> selectNewNotifications() {
        return repository.selectNewNotifications();
    }

    public void readNotification(final long receiptId) {
        repository.readNotification(receiptId);
    }

    /*address book*/
    public long createAddressBookEntry(final AddressBook addressBook) {
        return repository.createAddressBookEntry(addressBook);
    }

    public void updateAddressBookEntry(final AddressBook updatedAddressBook) {
        repository.updateAddressBookEntry(updatedAddressBook);
    }

    public LiveData<List<AddressBook>> selectAddressBookEntriesBySenderLogin(final String senderLogin) {
        return repository.selectAddressBookEntriesBySenderLogin(senderLogin);
    }

    public LiveData<AddressBook> selectAddressBookEntryById(final long id) {
        return repository.selectAddressBookEntryById(id);
    }

    public LiveData<List<AddressBook>> selectAddressBookEntries() {
        return repository.selectAddressBookEntries();
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
}
