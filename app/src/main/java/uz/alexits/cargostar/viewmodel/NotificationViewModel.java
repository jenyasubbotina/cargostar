package uz.alexits.cargostar.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import uz.alexits.cargostar.push.Notification;
import uz.alexits.cargostar.repository.NotificationRepository;

import java.util.List;

public class NotificationViewModel extends HeaderViewModel {

    public NotificationViewModel(final Context context) {
        super(context);
    }

    public void createNotification(final List<Notification> notificationList) {
        notificationRepository.createNotification(notificationList);
    }

    public void createNotification(final Notification notification) {
        notificationRepository.createNotification(notification);
    }

    public LiveData<List<Notification>> selectAllNotifications() {
        return notificationRepository.selectAllNotifications();
    }

    public LiveData<List<Notification>> selectNewNotifications() {
        return notificationRepository.selectNewNotifications();
    }

    public LiveData<Integer> selectNewNotificationsCount() {
        return notificationRepository.selectNewNotificationsCount();
    }

    public void readNotification(final long notificationId) {
        notificationRepository.readNotification(notificationId);
    }
}
