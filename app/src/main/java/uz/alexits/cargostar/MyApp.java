package uz.alexits.cargostar;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import uz.alexits.cargostar.database.cache.LocalCache;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LocalCache.getInstance(this);
        createNotificationChannels(this);
    }

    private void createNotificationChannels(@NonNull final Context context) {
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final NotificationChannel notificationChannel = new NotificationChannel(
                    getString(R.string.implicit_notification_channel_id), getString(R.string.implicit_notification_channel_name), NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setShowBadge(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            if (notificationManager == null) {
                Log.e(TAG, "notificationManager is NULL");
                return;
            }
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private static final String TAG = MyApp.class.toString();
}
