package com.example.cargostar.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.cargostar.model.actor.Courier;
import com.example.cargostar.model.database.Repository;
import com.example.cargostar.model.location.Branch;
import com.example.cargostar.model.shipping.ReceiptWithCargoList;

public class HeaderViewModel extends AndroidViewModel {
    private final Repository repository;

    public HeaderViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);
    }

    public LiveData<Courier> selectCourierByLogin(final String login) {
        return repository.selectCourierByLogin(login);
    }

    public LiveData<Branch> selectBranchByCourierId(final long courierId) {
        return repository.selectBranchByCourierId(courierId);
    }

    public LiveData<ReceiptWithCargoList> selectRequest(final long requestId) {
        return repository.selectRequest(requestId);
    }

    public LiveData<Integer> selectNewNotificationsCount() {
        return repository.selectNewNotificationsCount();
    }
}
