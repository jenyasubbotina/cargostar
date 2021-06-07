package uz.alexits.cargostar.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

import uz.alexits.cargostar.R;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.utils.Constants;

public class InitializationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialization);

        createNotificationChannels(this);

        obtainFcmToken(this);

        final boolean isLoggedIn = SharedPrefs.getInstance(this).isLoggedIn(this);

        if (isLoggedIn) {
            startActivity(new Intent(this, MainActivity.class));
        }
        else {
            final Intent loginIntent = new Intent(this, SignInActivity.class);

            if (getIntent() != null) {
                loginIntent.putExtra(Constants.KEY_LOGIN, getIntent().getStringExtra(Constants.KEY_LOGIN));
                loginIntent.putExtra(Constants.KEY_PASSWORD, getIntent().getStringExtra(Constants.KEY_PASSWORD));
            }
            startActivity(loginIntent);
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