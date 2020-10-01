package com.example.cargostar.model.database;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.cargostar.model.Notification;
import com.example.cargostar.model.TransportationStatus;
import com.example.cargostar.model.actor.AddressBook;
import com.example.cargostar.model.actor.Courier;
import com.example.cargostar.model.actor.Customer;
import com.example.cargostar.model.actor.PassportData;
import com.example.cargostar.model.actor.PaymentData;
import com.example.cargostar.model.database.dao.ActorDao;
import com.example.cargostar.model.database.dao.LocationDao;
import com.example.cargostar.model.database.dao.ParcelDao;
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

public class Repository {
    private static Repository instance;
    private ActorDao actorDao;
    private LocationDao locationDao;
    private ParcelDao parcelDao;

    private LiveData<List<Courier>> courierList;
    private LiveData<List<Customer>> customerList;
    private LiveData<List<PassportData>> passportDataList;
    private LiveData<List<PaymentData>> paymentDataList;

    private LiveData<List<Country>> countryList;
    private LiveData<List<Region>> regionList;
    private LiveData<List<City>> cityList;

    private LiveData<List<ReceiptWithCargoList>> requestList;
    private LiveData<List<ReceiptWithCargoList>> publicRequestList;

    private Repository(final Application application) {
        final LocalCache localCache= LocalCache.getInstance(application);
        this.actorDao = localCache.actorDao();
        this.locationDao = localCache.locationDao();
        this.parcelDao = localCache.parcelDao();

        this.countryList = localCache.locationDao().selectAllCountries();
        this.regionList = localCache.locationDao().selectAllRegions();
        this.cityList = localCache.locationDao().selectAllCities();

        this.courierList = localCache.actorDao().selectAllCouriers();
        this.customerList = localCache.actorDao().selectAllCustomers();
        this.passportDataList = localCache.actorDao().selectAllPassportData();
        this.paymentDataList = localCache.actorDao().selectAllPaymentData();

        this.requestList = localCache.parcelDao().selectAllRequests();
        this.publicRequestList = localCache.parcelDao().selectPublicRequests();
    }

    public static Repository getInstance(final Application application) {
        if (instance == null) {
            synchronized (Repository.class) {
                if (instance == null) {
                    instance = new Repository(application);
                }
            }
        }
        return instance;
    }

    /* Location Queries */
    public long createCountry(final Country newCountry) {
        return locationDao.createCountry(newCountry);
    }

    public long[] createCountries(final List<Country> countryList) {
        return locationDao.createCountries(countryList);
    }

    public LiveData<List<Country>> selectAllCountries() {
        return locationDao.selectAllCountries();
    }

    public void dropCountries() {
        locationDao.dropCountries();
    }

    public long createRegion(final Region newRegion) {
        return locationDao.createRegion(newRegion);
    }

    public long[] createRegions(final List<Region> regionList) {
        return locationDao.createRegions(regionList);
    }

    public LiveData<List<Region>> selectAllRegions() {
        return locationDao.selectAllRegions();
    }

    public LiveData<List<Region>> selectRegionsByCountry(final long countryId) {
        return locationDao.selectRegionsByCountry(countryId);
    }

    public void dropRegions() {
        locationDao.dropRegions();
    }

    public long createCity(final City newCity) {
        return locationDao.createCity(newCity);
    }

    public long[] createCities(final List<City> cityList) {
        return locationDao.createCities(cityList);
    }

    public LiveData<List<City>> selectAllCities() {
        return locationDao.selectAllCities();
    }

    public LiveData<List<City>> selectAllCitiesByRegion(final long regionId) {
        return locationDao.selectAllCitiesByRegion(regionId);
    }

    public LiveData<List<City>> selectAllCitiesByCountry(final long countryId) {
        return locationDao.selectAllCitiesByCountry(countryId);
    }

    public void dropCities() {
        locationDao.dropCities();
    }

    /*branches*/
    public long createBranch(final Branch newBranch) {
        return locationDao.createBranch(newBranch);
    }

    public long[] createBranches(final List<Branch> branchList) {
        return locationDao.createBranches(branchList);
    }

    public LiveData<Branch> selectBranchByCourierId(final long courierId) {
        return locationDao.selectBranchByCourierId(courierId);
    }

    public LiveData<Country> selectCountryByCourierId(final long courierId) {
        return locationDao.selectCountryByCourierId(courierId);
    }

    public LiveData<Region> selectRegionByCourierId(final long courierId) {
        return locationDao.selectRegionByCourierId(courierId);
    }

    public LiveData<City> selectCityByCourierId(final long courierId) {
        return locationDao.selectCityByCourierId(courierId);
    }

    public LiveData<List<Branch>> selectAllBranches() {
        return locationDao.selectAllBranches();
    }

    public LiveData<List<Branch>> selectAllBranchesByCity(final long cityId) {
        return locationDao.selectAllBranchesByCity(cityId);
    }

    public LiveData<List<Branch>> selectAllBranchesByRegion(final long regionId) {
        return locationDao.selectAllBranchesByRegion(regionId);
    }

    public LiveData<List<Branch>> selectAllBranchesByCountry(final long countryId) {
        return locationDao.selectAllBranchesByCountry(countryId);
    }

    public void dropBranches() {
        locationDao.dropBranches();
    }

    /*transit points*/
    public long createTransitPoint(final TransitPoint transitPoint) {
        return locationDao.createTransitPoint(transitPoint);
    }

    public long[] createTransitPoint(final List<TransitPoint> transitPointList) {
        return locationDao.createTransitPoint(transitPointList);
    }

    public void updateTransitPoint(final TransitPoint updatedTransitPoint) {
        locationDao.updateTransitPoint(updatedTransitPoint);
    }

    public void dropTransitPoints() {
        locationDao.dropTransitPoints();
    }

    public LiveData<List<TransitPoint>> selectAllTransitPoints() {
        return locationDao.selectAllTransitPoints();
    }

    public LiveData<TransitPoint> selectTransitPoint(final long pointId) {
        return locationDao.selectTransitPoint(pointId);
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

    /* Courier queries */
    public long createCourier(final Courier newCourier) {
        return actorDao.createCourier(newCourier);
    }

    public void updateCourier(final Courier courier) {
        actorDao.updateCourier(courier);
    }

    public LiveData<Courier> selectCourier(final String userId) {
        return actorDao.selectCourier(userId);
    }

    public LiveData<Courier> selectCourierByLogin(final String login) {
        return actorDao.selectCourierByLogin(login);
    }

    public LiveData<List<Courier>> selectAllCouriers() {
        return courierList;
    }

    public void dropCouriers() {
        actorDao.dropCouriers();
    }

    /* Customer queries */
    public long createCustomer(final Customer newCustomer) {
        return actorDao.createCustomer(newCustomer);
    }

    public LiveData<Customer> selectCustomer(final String userId) {
        return actorDao.selectCustomer(userId);
    }

    public LiveData<List<Customer>> selectAllCustomers() {
        return customerList;
    }

    public void dropCustomers() {
        actorDao.dropCustomers();
    }

    public LiveData<Customer> selectCustomerByLogin(final String senderLogin) {
        return actorDao.selectCustomerByLogin(senderLogin);
    }

    /* Passport data queries */
    public void createPassportData(final PassportData passportData) {
        actorDao.createPassportData(passportData);
    }

    public LiveData<PassportData> selectPassportData(final String userId) {
        return actorDao.selectPassportData(userId);
    }

    public LiveData<List<PassportData>> selectAllPassportData() {
        return passportDataList;
    }

    public void dropPassportData() {
        actorDao.dropPassportData();
    }

    /* Payment data queries */
    public void createPaymentData(final PaymentData paymentData) {
        actorDao.createPaymentData(paymentData);
    }

    public LiveData<PaymentData> selectPaymentData(final String userId) {
        return actorDao.selectPaymentData(userId);
    }

    public LiveData<List<PaymentData>> selectAllPaymentData() {
        return paymentDataList;
    }

    public void dropPaymentData() {
        actorDao.dropPaymentData();
    }

    /*Request queries*/
    public LiveData<ReceiptWithCargoList> selectRequest(final long requestId) {
        return parcelDao.selectRequest(requestId);
    }

    public LiveData<List<ReceiptWithCargoList>> selectAllRequests() {
        return requestList;
    }

    public LiveData<List<ReceiptWithCargoList>> selectPublicRequests() {
        return publicRequestList;
    }

    public LiveData<List<ReceiptWithCargoList>> selectMyRequests(final long courierId) {
        return parcelDao.selectMyRequests(courierId);
    }

    public LiveData<Parcel> selectParcel(final long receiptId) {
        return parcelDao.selectParcel(receiptId);
    }

    public LiveData<Receipt> selectReceipt(final long receiptId) {
        return parcelDao.selectReceipt(receiptId);
    }

    public LiveData<List<Parcel>> selectCurrentParcels(final long courierId) {
        return parcelDao.selectCurrentParcels(courierId);
    }

    public LiveData<List<Parcel>> selectAllParcels() {
        return parcelDao.selectAllParcels();
    }

    public LiveData<List<Receipt>> selectAllReceipts() {
        return parcelDao.selectAllReceipts();
    }

    public long createRequest(final Receipt newRequest) {
        return parcelDao.createRequest(newRequest);
    }

    public void createRequests(final List<Receipt> newRequestList) {
        parcelDao.createRequests(newRequestList);
    }

    public void updateReceipt(final Receipt updatedReceipt) {
        parcelDao.updateReceipt(updatedReceipt);
    }

    public long createParcelTransitPointCrossRef(final ReceiptTransitPointCrossRef receiptTransitPointCrossRef) {
        return parcelDao.createParcelTransitPointCrossRef(receiptTransitPointCrossRef);
    }

    public LiveData<List<Parcel>> selectParcelsByStatus(final long courierId, final TransportationStatus transportationStatus) {
        return parcelDao.selectParcelsByStatus(courierId, transportationStatus);
    }

    public LiveData<List<Parcel>> selectParcelsByStatus(final long courierId, final TransportationStatus[] statusArray) {
        return parcelDao.selectParcelsByStatus(courierId, statusArray);
    }

    public LiveData<List<Parcel>> selectParcelsByLocation(final long courierId,
                                                          final TransportationStatus transportationStatus,
                                                          final long locationId) {
        return parcelDao.selectParcelsByLocation(courierId, transportationStatus, locationId);
    }

    public LiveData<List<Parcel>> selectParcelsByLocationAndStatus(final long courierId, final TransportationStatus[] statusList, final long locationId) {
        return parcelDao.selectParcelsByLocationAndStatus(courierId, TransportationStatus.IN_TRANSIT, statusList, locationId);
    }

    public void dropReceipts() {
        parcelDao.dropReceipts();
    }

    public void deleteReceipt(final long requestId) {
        parcelDao.deleteReceipt(requestId);
    }

    /*Cargo queries*/
    public void createCargo(final List<Cargo> cargoList) {
        parcelDao.createCargo(cargoList);
    }

    public void createCargo(final Cargo cargo) {
        parcelDao.createCargo(cargo);
    }

    public void updateCargo(final Cargo updatedCargo) {
        parcelDao.updateCargo(updatedCargo);
    }

    public void readReceipt(final long receiptId) {
        parcelDao.readReceipt(receiptId, true);
    }

    /*Notification queries*/
    public void createNotification(final List<Notification> notificationList) {
        parcelDao.createNotification(notificationList);
    }

    public void createNotification(final Notification notification) {
        parcelDao.createNotification(notification);
    }

    public void updateNotification(final Notification updatedNotification) {
        parcelDao.updateNotification(updatedNotification);
    }

    public LiveData<Notification> selectNotification(final long receiptId) {
        return parcelDao.selectNotification(receiptId);
    }

    public LiveData<List<Notification>> selectAllNotifications() {
        return parcelDao.selectAllNotifications();
    }

    public LiveData<List<Notification>> selectNewNotifications() {
        return parcelDao.selectNewNotifications(false);
    }

    public void readNotification(final long receiptId) {
        parcelDao.readNotification(receiptId, true);
    }

    /*address book*/
    public long createAddressBookEntry(final AddressBook addressBook) {
        return actorDao.createAddressBookEntry(addressBook);
    }

    public void updateAddressBookEntry(final AddressBook updatedAddressBook) {
        actorDao.updateAddressBookEntry(updatedAddressBook);
    }

    public LiveData<List<AddressBook>> selectAddressBookEntriesBySenderLogin(final String senderLogin) {
        return actorDao.selectAddressBookEntriesBySenderLogin(senderLogin);
    }

    public LiveData<AddressBook> selectAddressBookEntryById(final long id) {
        return actorDao.selectAddressBookEntryById(id);
    }

    public LiveData<List<AddressBook>> selectAddressBookEntries() {
        return actorDao.selectAddressBookEntries();
    }

    /*Consolidation queries*/
    public long[] createConsolidation(final List<Consolidation> consolidationList) {
        return parcelDao.createConsolidation(consolidationList);
    }

    public long createConsolidation(final Consolidation consolidation) {
        return parcelDao.createConsolidation(consolidation);
    }

    public void updateConsolidation(final Consolidation updatedConsolidation) {
        parcelDao.updateConsolidation(updatedConsolidation);
    }

    public LiveData<List<Parcel>> selectParcelsByConsolidationNumber(final long consolidationNumber) {
        return parcelDao.selectParcelsByConsolidationNumber(consolidationNumber);
    }

    public LiveData<Parcel> selectParcelByConsolidationNumber(final long consolidationNumber, final long parcelId) {
        return parcelDao.selectParcelByConsolidationNumber(consolidationNumber, parcelId);
    }
}