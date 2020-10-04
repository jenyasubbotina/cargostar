package com.example.cargostar.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.cargostar.model.Notification;
import com.example.cargostar.model.database.Repository;

import java.util.List;

public class NotificationsViewModel extends AndroidViewModel {
    private final Repository repository;

    public NotificationsViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);
    }

    /*Notification queries*/
    public LiveData<Notification> selectNotification(final long receiptId) {
        return repository.selectNotification(receiptId);
    }

    public LiveData<List<Notification>> selectNewNotifications() {
        return repository.selectNewNotifications();
    }

    public void readNotification(final long receiptId) {
        repository.readNotification(receiptId);
    }

    public LiveData<List<Notification>> selectAllNotifications() {
        return repository.selectAllNotifications();
    }
}
