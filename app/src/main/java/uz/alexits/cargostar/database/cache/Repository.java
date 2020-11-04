package uz.alexits.cargostar.database.cache;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import uz.alexits.cargostar.database.dao.ActorDao;
import uz.alexits.cargostar.database.dao.InvoiceDao;
import uz.alexits.cargostar.database.dao.LocationDao;
import uz.alexits.cargostar.database.dao.PackagingDao;
import uz.alexits.cargostar.database.dao.ParcelDao;
import uz.alexits.cargostar.database.dao.RequestDao;
import uz.alexits.cargostar.model.actor.Customer;
import uz.alexits.cargostar.model.calculation.Zone;
import uz.alexits.cargostar.model.calculation.ZoneCountry;
import uz.alexits.cargostar.model.calculation.ZoneSettings;
import uz.alexits.cargostar.model.location.Branche;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.model.location.TransitPoint;
import uz.alexits.cargostar.model.shipping.Invoice;
import uz.alexits.cargostar.model.shipping.Request;
import uz.alexits.cargostar.model.Notification;
import uz.alexits.cargostar.model.actor.AddressBook;
import uz.alexits.cargostar.model.actor.Courier;
import uz.alexits.cargostar.model.calculation.Packaging;
import uz.alexits.cargostar.model.calculation.PackagingType;
import uz.alexits.cargostar.model.calculation.Provider;

import java.util.List;

public class Repository {
    /* database */
    private static Repository instance;

    private final LocationDao locationDao;
    private final PackagingDao packagingDao;

    private final ActorDao actorDao;
    private final InvoiceDao invoiceDao;

    private final ParcelDao parcelDao;
    private final RequestDao requestDao;

    /*location data*/
    private final LiveData<List<Country>> countryList;
    private final LiveData<List<Region>> regionList;
    private final LiveData<List<City>> cityList;
    private final LiveData<List<Branche>> brancheList;
    private final LiveData<List<TransitPoint>> transitPointList;

    /*provider / packaging data*/
    private final LiveData<List<Provider>> providerList;
    private final LiveData<List<Packaging>> packagingList;
    private final LiveData<List<PackagingType>> packagingTypeList;

    /*shipping data*/
    private LiveData<List<Request>> requestList;

    private Repository(final Application application) {
        final LocalCache localCache = LocalCache.getInstance(application);
        this.locationDao = localCache.locationDao();
        this.actorDao = localCache.actorDao();
        this.invoiceDao = localCache.invoiceDao();
        this.parcelDao = localCache.parcelDao();
        this.requestDao = localCache.requestDao();
        this.packagingDao = localCache.packagingDao();
        /* location data */
        this.countryList = locationDao.selectAllCountries();
        this.regionList = locationDao.selectAllRegions();
        this.cityList = locationDao.selectAllCities();
        brancheList = locationDao.selectAllBranches();
        this.transitPointList = locationDao.selectAllTransitPoints();
        /* location data */
        this.providerList = packagingDao.selectAllProviders();
        this.packagingList = packagingDao.selectAllPackaging();
        this.packagingTypeList = packagingDao.selectAllPackagingTypes();
        /* shipping data */
        this.requestList = requestDao.selectPublicRequests();
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
    public LiveData<List<Country>> selectAllCountries() {
        return countryList;
    }

    public LiveData<List<Region>> selectAllRegions() {
        return regionList;
    }

    public LiveData<List<City>> selectAllCities() {
        return cityList;
    }

    public LiveData<List<City>> selectCitiesByCountryId(final long countryId) {
        return locationDao.selectCitiesByCountryId(countryId);
    }

    public LiveData<List<City>> selectCitiesByRegionId(final long regionId) {
        return locationDao.selectCitiesByRegionId(regionId);
    }

    public LiveData<List<Branche>> selectAllBranches() {
        return brancheList;
    }

    public LiveData<Branche> selectBrancheById(final long brancheId) {
        return locationDao.selectBrancheById(brancheId);
    }

    public LiveData<List<TransitPoint>> selectAllTransitPoints() {
        return transitPointList;
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

    /* Provider / Packaging Queries*/
    public LiveData<List<Provider>> selectAllProviders() {
        return providerList;
    }

    public LiveData<Provider> selectProviderById(final long providerId) {
        return packagingDao.selectProviderById(providerId);
    }

    public LiveData<List<Packaging>> selectAllPackaging() {
        return packagingList;
    }

    public LiveData<List<PackagingType>> selectAllPackagingTypes() {
        return packagingTypeList;
    }

    public LiveData<List<Packaging>> selectPackagingsByProviderId(final long providerId) {
        return packagingDao.selectPackagingsByProviderId(providerId);
    }

    public LiveData<List<PackagingType>> selectPackagingTypesByTypeAndPackagingIds(final long type, final List<Long> packagingIds) {
        return packagingDao.selectPackagingTypesByTypeAndPackagingIds(type, packagingIds);
    }

    public LiveData<List<Long>> selectPackagingIdsByProviderId(final Long providerId) {
        return packagingDao.selectPackagingIdsByProviderId(providerId);
    }

    public LiveData<Packaging> selectPackagingById(final Long packagingId) {
        return packagingDao.selectPackagingById(packagingId);
    }

    public LiveData<List<PackagingType>> selectPackagingTypesByProviderId(final long providerId, final long type) {
        return packagingDao.selectPackagingTypesByProviderId(providerId, type);
    }

    public LiveData<List<ZoneSettings>> selectZoneSettingsByZoneIds(final List<Long> zoneIds) {
        return packagingDao.selectZoneSettingsByZoneIds(zoneIds);
    }

    public LiveData<List<Zone>> selectZoneListByCountryIdAndProviderId(final Long countryId, final Long providerId) {
        return packagingDao.selectZoneListByCountryIdAndProviderId(countryId, providerId);
    }

    /* zone countries */
    public LiveData<List<ZoneCountry>> selectAllZoneCountries() {
        return packagingDao.selectAllZoneCountries();
    }

    /* Courier queries */
    public long createCourier(final Courier newCourier) {
        return actorDao.createCourier(newCourier);
    }

    public void updateCourier(final long courierId,
                              final String password,
                              final String firstName,
                              final String middleName,
                              final String lastName,
                              final String phone) {
        actorDao.updateCourier(courierId, password, firstName, middleName, lastName, phone);
    }

    public LiveData<Courier> selectCourier(final long userId) {
        return actorDao.selectCourier(userId);
    }

    public LiveData<Courier> selectCourierByLogin(final String login) {
        return actorDao.selectCourierByLogin(login);
    }

    public void dropCouriers() {
        actorDao.dropCouriers();
    }

    /* Customer queries */
    public LiveData<Customer> selectCustomerById(final long clientId) {
        return actorDao.selectCustomer(clientId);
    }

    /* request data*/
    public LiveData<Request> selectRequest(final long requestId) {
        return requestDao.selectRequestById(requestId);
    }

    public LiveData<List<Request>> selectAllRequests() {
        return requestDao.selectPublicRequests();
    }

    public LiveData<List<Request>> selectRequestsByCourierId(final long courierId) {
        return requestDao.selectRequestsByCourierId(courierId);
    }

    /* invoice data */
    public LiveData<Invoice> selectInvoiceById(final long invoiceId) {
        return invoiceDao.selectInvoiceById(invoiceId);
    }

    public void updateRequest(final Request updateRequest) {
        requestDao.updateRequest(updateRequest);
    }

//    public long createParcelTransitPointCrossRef(final ReceiptTransitPointCrossRef receiptTransitPointCrossRef) {
//        return parcelDao.createParcelTransitPointCrossRef(receiptTransitPointCrossRef);
//    }
//
//    public LiveData<List<Parcel>> selectParcelsByStatus(final long courierId, final TransportationStatus transportationStatus) {
//        return parcelDao.selectParcelsByStatus(courierId, transportationStatus);
//    }
//
//    public LiveData<List<Parcel>> selectParcelsByStatus(final long courierId, final TransportationStatus[] statusArray) {
//        return parcelDao.selectParcelsByStatus(courierId, statusArray);
//    }
//
//    public LiveData<List<Parcel>> selectParcelsByLocation(final long courierId,
//                                                          final TransportationStatus transportationStatus,
//                                                          final long locationId) {
//        return parcelDao.selectParcelsByLocation(courierId, transportationStatus, locationId);
//    }
//
//    public LiveData<List<Parcel>> selectParcelsByLocationAndStatus(final long courierId, final TransportationStatus[] statusList, final long locationId) {
//        return parcelDao.selectParcelsByLocationAndStatus(courierId, TransportationStatus.IN_TRANSIT, locationId, statusList);
//    }
//
//    public void dropReceipts() {
//        parcelDao.dropReceipts();
//    }
//
//    public void deleteReceipt(final long requestId) {
//        parcelDao.deleteReceipt(requestId);
//    }
//
//    /*Cargo queries*/
//    public void createCargo(final List<Cargo> cargoList) {
//        parcelDao.createCargo(cargoList);
//    }
//
//    public void createCargo(final Cargo cargo) {
//        parcelDao.createCargo(cargo);
//    }
//
//    public void updateCargo(final Cargo updatedCargo) {
//        parcelDao.updateCargo(updatedCargo);
//    }

    public void readReceipt(final long receiptId) {
        parcelDao.readReceipt(receiptId, false);
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

    public LiveData<Integer> selectNewNotificationsCount() {
        return parcelDao.selectNewNotificationsCount(false);
    }

    public void readNotification(final long receiptId) {
        parcelDao.readNotification(receiptId, true);
    }

//    /*Consolidation queries*/
//    public long[] createConsolidation(final List<Consolidation> consolidationList) {
//        return parcelDao.createConsolidation(consolidationList);
//    }
//
//    public long createConsolidation(final Consolidation consolidation) {
//        return parcelDao.createConsolidation(consolidation);
//    }
//
//    public void updateConsolidation(final Consolidation updatedConsolidation) {
//        parcelDao.updateConsolidation(updatedConsolidation);
//    }
//
//    public LiveData<List<Parcel>> selectParcelsByConsolidationNumber(final long consolidationNumber) {
//        return parcelDao.selectParcelsByConsolidationNumber(consolidationNumber);
//    }
//
//    public LiveData<Parcel> selectParcelByConsolidationNumber(final long consolidationNumber, final long parcelId) {
//        return parcelDao.selectParcelByConsolidationNumber(consolidationNumber, parcelId);
//    }

    /*address book*/
    public long createAddressBookEntry(final AddressBook addressBook) {
        return invoiceDao.insertAddressBookEntry(addressBook);
    }

    public void updateAddressBookEntry(final AddressBook updatedAddressBook) {
        invoiceDao.updateAddressBookEntry(updatedAddressBook);
    }

    public LiveData<AddressBook> selectAddressBookEntryById(final long id) {
        return invoiceDao.selectAddressBookEntryById(id);
    }

    public LiveData<List<AddressBook>> selectAddressBookEntries() {
        return invoiceDao.selectAllAddressBookEntries();
    }

    private static final String TAG = Repository.class.toString();
}