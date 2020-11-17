package uz.alexits.cargostar.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import uz.alexits.cargostar.push.Notification;

@Dao
public interface NotificationDao {
    /*Notification queries*/
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void createNotification(final List<Notification> notificationList);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void createNotification(final Notification notification);

    @Update
    void updateNotification(final Notification updatedNotification);

    @Query("UPDATE notification SET is_read = :isRead WHERE id == :notificationId")
    void readNotification(final long notificationId, final boolean isRead);
//
//    @Query("SELECT * FROM notification WHERE receipt_id == :receiptId")
//    LiveData<Notification> selectNotification(final long receiptId);

    @Query("SELECT * FROM notification ORDER BY receive_date DESC")
    LiveData<List<Notification>> selectAllNotifications();

    @Query("SELECT * FROM notification WHERE is_read == :isRead ORDER BY receive_date DESC")
    LiveData<List<Notification>> selectNewNotifications(final boolean isRead);

    @Query("SELECT COUNT() FROM notification WHERE is_read == :isRead")
    LiveData<Integer> selectNewNotificationsCount(final boolean isRead);
}
