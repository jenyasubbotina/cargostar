package com.example.cargostar.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.cargostar.model.database.Repository;
import com.example.cargostar.model.location.TransitPoint;
import com.example.cargostar.model.shipping.Parcel;

public class ParcelStatusViewModel extends AndroidViewModel {
    private final Repository repository;

    public ParcelStatusViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);
    }

    public LiveData<TransitPoint> selectTransitPoint(final long pointId) {
        return repository.selectTransitPoint(pointId);
    }

    public LiveData<Parcel> selectParcel(final long receiptId) {
        return repository.selectParcel(receiptId);
    }
}
