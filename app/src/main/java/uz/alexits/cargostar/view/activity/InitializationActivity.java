package uz.alexits.cargostar.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.UUID;

import uz.alexits.cargostar.R;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.Repository;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class InitializationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialization);

        final ProgressBar progressBar = findViewById(R.id.progress_bar);

        createNotificationChannels(this);

        obtainFcmToken(this);

        if (SharedPrefs.getInstance(this).getBoolean(SharedPrefs.KEEP_LOGGED) &&
                SharedPrefs.getInstance(this).getString(Constants.KEY_LOGIN) != null &&
                SharedPrefs.getInstance(this).getString(Constants.KEY_PASSWORD) != null &&
                SharedPrefs.getInstance(this).getString(Constants.KEY_TOKEN) != null) {
            startActivity(new Intent(this, MainActivity.class));
        }
        else {
            startActivity(new Intent(this, SignInActivity.class));
        }
        finish();
    }

    private void obtainFcmToken(@NonNull final Context context) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Fetching FCM registration token failed", task.getException());
                return;
            }
            final String token = task.getResult();

            Log.i(TAG, "obtainFcmToken(): " + token);

            SharedPrefs.getInstance(context).putString(Constants.KEY_TOKEN, token);
        });
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

    private static final String TAG = InitializationActivity.class.toString();
}