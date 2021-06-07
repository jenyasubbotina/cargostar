package uz.alexits.cargostar.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.dao.NotificationDao;
import uz.alexits.cargostar.push.Notification;

public class NotificationRepository {
    private final NotificationDao notificationDao;

    public NotificationRepository(final Context context) {
        this.notificationDao = LocalCache.getInstance(context).notificationDao();
    }

    public void createNotification(final List<Notification> notificationList) {
        new Thread(() -> notificationDao.createNotification(notificationList)).start();
    }

    public void createNotification(final Notification notification) {
        new Thread(() -> notificationDao.createNotification(notification)).start();
    }

    public void updateNotification(final Notification updatedNotification) {
        new Thread(() -> notificationDao.updateNotification(updatedNotification)).start();
    }

    public LiveData<Notification> selectNotification(final long id) {
        return notificationDao.selectNotification(id);
    }

    public LiveData<List<Notification>> selectAllNotifications() {
        return notificationDao.selectAllNotifications();
    }

    public LiveData<List<Notification>> selectNewNotifications() {
        return notificationDao.selectNewNotifications(false);
    }

    public LiveData<Integer> selectNewNotificationsCount() {
        return notificationDao.selectNewNotificationsCount(false);
    }

    public void readNotification(final long notificationId) {
        new Thread(() -> notificationDao.readNotification(notificationId, true));
    }
}
