package com.example.cargostar.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.cargostar.model.database.Repository;
import com.example.cargostar.model.shipping.Receipt;
import com.example.cargostar.model.shipping.ReceiptWithCargoList;

import java.util.List;

public class RequestsViewModel extends AndroidViewModel {
    private final Repository repository;
    private LiveData<List<ReceiptWithCargoList>> requestList;
    private LiveData<List<ReceiptWithCargoList>> publicRequestList;

    public RequestsViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);
        this.requestList = repository.selectAllRequests();
        this.publicRequestList = repository.selectPublicRequests();
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

    public void readReceipt(final long receiptId) {
        repository.readReceipt(receiptId);
    }

    public void updateReceipt(final Receipt updatedReceipt) {
        repository.updateReceipt(updatedReceipt);
    }
}
