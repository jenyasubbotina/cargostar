package uz.alexits.cargostar.viewmodel;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.push.Notification;
import uz.alexits.cargostar.model.actor.Courier;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.Repository;

import com.google.gson.JsonElement;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopulateViewModel extends AndroidViewModel {
    private final Repository repository;

    private LiveData<List<Courier>> courierList;

    public PopulateViewModel(@NonNull final Application application) {
        super(application);
        this.repository = Repository.getInstance(application);
    }

    public void initAddressBook() {
        RetrofitClient.getInstance(getApplication().getApplicationContext()).setServerData(
                SharedPrefs.getInstance(getApplication().getApplicationContext()).getString(SharedPrefs.LOGIN),
                SharedPrefs.getInstance(getApplication().getApplicationContext()).getString(SharedPrefs.PASSWORD_HASH));
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
    }

    /*Request queries*/
//    public long createRequest(final Receipt newRequest) {
//        return repository.createRequest(newRequest);
//    }
//
//    public void createRequests(final List<Receipt> newRequestList) {
//        repository.createRequests(newRequestList);
//    }

//    public long createParcelTransitPointCrossRef(final ReceiptTransitPointCrossRef receiptTransitPointCrossRef) {
//        return repository.createParcelTransitPointCrossRef(receiptTransitPointCrossRef);
//    }
//
//    public LiveData<Invoice> selectReceipt(final long receiptId) {
//        return repository.selectReceipt(receiptId);
//    }
//
//    public LiveData<List<Parcel>> selectAllParcels() {
//        return repository.selectAllParcels();
//    }
//
//    public LiveData<List<Invoice>> selectAllReceipts() {
//        return repository.selectAllReceipts();
//    }
//
//    /*Cargo queries*/
//    public void createCargo(final List<Cargo> cargoList) {
//        repository.createCargo(cargoList);
//    }
//
//    public void createCargo(final Cargo cargo) {
//        repository.createCargo(cargo);
//    }
//
//    public void updateCargo(final Cargo updatedCargo) {
//        repository.updateCargo(updatedCargo);
//    }
//
//    public long createAddressBookEntry(final AddressBook addressBook) {
//        return repository.createAddressBookEntry(addressBook);
//    }
//
//    /*Consolidation queries*/
//    public long[] createConsolidation(final List<Consolidation> consolidationList) {
//        return repository.createConsolidation(consolidationList);
//    }
//
//    public long createConsolidation(final Consolidation consolidation) {
//        return repository.createConsolidation(consolidation);
//    }
//
//    public void updateConsolidation(final Consolidation updatedConsolidation) {
//        repository.updateConsolidation(updatedConsolidation);
//    }
//
//    public LiveData<List<Parcel>> selectParcelsByConsolidationNumber(final long consolidationNumber) {
//        return repository.selectParcelsByConsolidationNumber(consolidationNumber);
//    }
//
//    public LiveData<Parcel> selectParcelByConsolidationNumber(final long consolidationNumber, final long parcelId) {
//        return repository.selectParcelByConsolidationNumber(consolidationNumber, parcelId);
//    }

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

    private static final String TAG = PopulateViewModel.class.toString();
}
