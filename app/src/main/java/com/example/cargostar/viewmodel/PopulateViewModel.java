package com.example.cargostar.viewmodel;

import android.app.Application;
import android.util.Log;
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
import com.example.cargostar.model.api.RetrofitClient;
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
import com.google.gson.JsonElement;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopulateViewModel extends AndroidViewModel {
    private static final String TAG = PopulateViewModel.class.toString();
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

    public LiveData<Receipt> selectReceipt(final long receiptId) {
        return repository.selectReceipt(receiptId);
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
    public long[] createCountries(final List<Country> countryList) {
        return repository.createCountries(countryList);
    }

    //TODO: check query here
    public LiveData<List<Country>> selectAllCountries() {
        RetrofitClient.getInstance(getApplication().getApplicationContext()).getCountries(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                Log.i(TAG, "selectAllCountries(): code=" + response.code());
                if (response.isSuccessful()) {
                    Log.i(TAG, "selectAllCountries(): response=" + response.body());
                    return;
                }
                Log.e(TAG, "selectAllCountries(): error=" + response.errorBody());
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                Log.e(TAG, "selectAllCountries(): ", t);
            }
        });
        return repository.selectAllCountries();
    }

    public LiveData<List<Region>> selectAllRegions() {
        RetrofitClient.getInstance(getApplication().getApplicationContext()).getRegions(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                Log.i(TAG, "selectAllRegions(): code=" + response.code());
                if (response.isSuccessful()) {
                    Log.i(TAG, "selectAllRegions(): response=" + response.body());
                    return;
                }
                Log.e(TAG, "selectAllRegions(): error=" + response.errorBody());
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                Log.e(TAG, "selectAllRegions(): ", t);
            }
        });
        return repository.selectAllRegions();
    }

    public LiveData<List<City>> selectAllCities() {
        RetrofitClient.getInstance(getApplication().getApplicationContext()).getCities(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                Log.i(TAG, "selectAllCities(): code=" + response.code());
                if (response.isSuccessful()) {
                    Log.i(TAG, "selectAllCities(): response=" + response.body());
                    return;
                }
                Log.e(TAG, "selectAllCities(): error=" + response.errorBody());
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                Log.e(TAG, "selectAllCities(): ", t);
            }
        });
        return repository.selectAllCities();
    }

    public LiveData<List<Branch>> selectAllTransitPoints() {
        RetrofitClient.getInstance(getApplication().getApplicationContext()).getTransitPoints(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                Log.i(TAG, "selectAllTransitPoints(): code=" + response.code());
                if (response.isSuccessful()) {
                    Log.i(TAG, "selectAllTransitPoints(): response=" + response.body());
                    return;
                }
                Log.e(TAG, "selectAllTransitPoints(): error=" + response.errorBody());
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                Log.e(TAG, "selectAllTransitPoints(): ", t);
            }
        });
        return repository.selectAllBranches();
    }

    public LiveData<List<Branch>> selectPackagingTypes() {
        RetrofitClient.getInstance(getApplication().getApplicationContext()).getPackageTypes(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                Log.i(TAG, "selectPackagingTypes(): code=" + response.code());
                if (response.isSuccessful()) {
                    Log.i(TAG, "selectPackagingTypes(): response=" + response.body());
                    return;
                }
                Log.e(TAG, "selectPackagingTypes(): error=" + response.errorBody());
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                Log.e(TAG, "selectPackagingTypes(): ", t);
            }
        });
        return null;
    }

    public LiveData<List<Branch>> selectPackaging() {
        RetrofitClient.getInstance(getApplication().getApplicationContext()).getPackaging(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                Log.i(TAG, "selectPackaging(): code=" + response.code());
                if (response.isSuccessful()) {
                    Log.i(TAG, "selectPackaging(): response=" + response.body());
                    return;
                }
                Log.e(TAG, "selectPackaging(): error=" + response.errorBody());
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                Log.e(TAG, "selectPackaging(): ", t);
            }
        });
        return null;
    }

    public LiveData<List<Branch>> selectZones() {
        RetrofitClient.getInstance(getApplication().getApplicationContext()).getZones(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                Log.i(TAG, "selectZones(): code=" + response.code());
                if (response.isSuccessful()) {
                    Log.i(TAG, "selectZones(): response=" + response.body());
                    return;
                }
                Log.e(TAG, "selectZones(): error=" + response.errorBody());
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                Log.e(TAG, "selectZones(): ", t);
            }
        });
        return null;
    }

    public LiveData<List<Branch>> selectZoneSettings() {
        RetrofitClient.getInstance(getApplication().getApplicationContext()).getZoneSettings(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                Log.i(TAG, "selectZoneSettings(): code=" + response.code());
                if (response.isSuccessful()) {
                    Log.i(TAG, "selectZoneSettings(): response=" + response.body());
                    return;
                }
                Log.e(TAG, "selectZoneSettings(): error=" + response.errorBody());
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                Log.e(TAG, "selectZoneSettings(): ", t);
            }
        });
        return null;
    }

    public LiveData<List<Branch>> selectAddressBook() {
        RetrofitClient.getInstance(getApplication().getApplicationContext()).getAddressBook(new Callback<JsonElement>() {
            @Override
            public void onResponse(@NonNull Call<JsonElement> call, @NonNull Response<JsonElement> response) {
                Log.i(TAG, "getAddressBook(): code=" + response.code());
                if (response.isSuccessful()) {
                    Log.i(TAG, "getAddressBook(): response=" + response.body());
                    return;
                }
                Log.e(TAG, "getAddressBook(): error=" + response.errorBody());
            }

            @Override
            public void onFailure(@NonNull Call<JsonElement> call, @NonNull Throwable t) {
                Log.e(TAG, "getAddressBook(): ", t);
            }
        });
        return null;
    }

    //TODO:
    public long[] createRegions(final List<Region> regionList) {
        return repository.createRegions(regionList);
    }

    public long[] createCities(final List<City> cityList) {
        return repository.createCities(cityList);
    }

    public long[] createBranches(final List<Branch> branchList) {
        return repository.createBranches(branchList);
    }

    public long[] createTransitPoint(final List<TransitPoint> transitPointList) {
        return repository.createTransitPoint(transitPointList);
    }

    public long createAddressBookEntry(final AddressBook addressBook) {
        return repository.createAddressBookEntry(addressBook);
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
