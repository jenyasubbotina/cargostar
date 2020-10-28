package uz.alexits.cargostar.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import uz.alexits.cargostar.model.shipping.Request;
import uz.alexits.cargostar.database.cache.Repository;

import java.util.List;

public class RequestsViewModel extends AndroidViewModel {
    private final Repository repository;
    private final LiveData<List<Request>> requestList;

    public RequestsViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);
        this.requestList = repository.selectAllRequests();
    }

    public LiveData<List<Request>> getPublicRequests() {
        return requestList;
    }

    public LiveData<List<Request>> getMyRequests(final long courierId) {
        return repository.selectRequestsByCourierId(courierId);
    }

    public void readReceipt(final long requestId) {
        repository.readReceipt(requestId);
    }

    public void updateReceipt(final Request updatedRequest) {
        repository.updateRequest(updatedRequest);
    }
}
