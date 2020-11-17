package uz.alexits.cargostar.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import uz.alexits.cargostar.push.Notification;
import uz.alexits.cargostar.database.cache.Repository;

import java.util.List;

public class NotificationsViewModel extends AndroidViewModel {
    private final Repository repository;

    public NotificationsViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);
    }

    /*Notification queries*/
//    public LiveData<Notification> selectNotification(final long receiptId) {
//        return repository.selectNotification(receiptId);
//    }

    public LiveData<List<Notification>> selectNewNotifications() {
        return repository.selectNewNotifications();
    }

    public void readNotification(final long notificationId) {
        repository.readNotification(notificationId);
    }

    public LiveData<List<Notification>> selectAllNotifications() {
        return repository.selectAllNotifications();
    }
}
