package uz.alexits.cargostar.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import uz.alexits.cargostar.entities.transportation.Request;

import java.util.List;
import java.util.UUID;

public class RequestsViewModel extends HeaderViewModel {
    private final MutableLiveData<UUID> bindRequestUUID;
    private final MutableLiveData<UUID> fetchRequestListUUID;

    public RequestsViewModel(final Context context) {
        super(context);
        this.bindRequestUUID = new MutableLiveData<>();
        this.fetchRequestListUUID = new MutableLiveData<>();
    }

    public LiveData<List<Request>> getPublicRequests() {
        return requestRepository.selectPublicRequests();
    }

    public LiveData<List<Request>> getMyRequests() {
        return requestRepository.selectRequestsByCourierId();
    }

    /* public request */
    public void readRequest(final long requestId) {
        requestRepository.readRequest(requestId);
    }

    public void bindRequest(final long requestId) {
        bindRequestUUID.setValue(requestRepository.bindRequest(requestId));
    }

    public LiveData<WorkInfo> getBindRequestResult(final Context context) {
        return Transformations.switchMap(bindRequestUUID, input -> WorkManager.getInstance(context).getWorkInfoByIdLiveData(input));
    }

    public void fetchRequestList() {
        fetchRequestListUUID.setValue(requestRepository.fetchRequestList());
    }

    public LiveData<WorkInfo> getFetchRequestListResult(final Context context) {
        return Transformations.switchMap(fetchRequestListUUID, input -> WorkManager.getInstance(context).getWorkInfoByIdLiveData(input));
    }

    public void fetchRequestData(final long requestId, final int consignmentQuantity) {
        requestRepository.fetchRequestData(requestId, consignmentQuantity);
    }
}
