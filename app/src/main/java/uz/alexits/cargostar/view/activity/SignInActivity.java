package uz.alexits.cargostar.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.messaging.FirebaseMessaging;
import uz.alexits.cargostar.R;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.viewmodel.LocationDataViewModel;
import uz.alexits.cargostar.viewmodel.SingInViewModel;
import uz.alexits.cargostar.view.UiUtils;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = SignInActivity.class.toString();
    private static boolean showPassword = false;

    private EditText loginEditText;
    private EditText passwordEditText;
    private Button signInBtn;
    private CheckBox keepLoggingCheckBox;
    private TextView forgotPasswordTextView;
    private ImageView passwordEyeImageView;

    private SingInViewModel signInViewModel;
    private LocationDataViewModel locationDataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initUI();

        createNotificationChannels(this);

        obtainFcmToken(this);

        signInViewModel = new ViewModelProvider(this).get(SingInViewModel.class);

        LocalCache.getInstance(this).actorDao().selectAllCouriers().observe(this, couriers -> {
            Log.i(TAG, "couriers: " + couriers);
        });

        signInBtn.setOnClickListener(v -> {
            final String login = loginEditText.getText().toString();
            final String password = passwordEditText.getText().toString();

            if (TextUtils.isEmpty(login)) {
                Toast.makeText(this, "Логин не может быть пустым", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Пароль не может быть пустым", Toast.LENGTH_SHORT).show();
                return;
            }
            signInViewModel.selectCourierByLogin(login).observe(this, employee -> {
                if (employee == null) {
                    Toast.makeText(this, "Пользователь не зарегистрирован", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(employee.getAccount().getPasswordHash())) {
                    Toast.makeText(this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPrefs.getInstance(this).putString(SharedPrefs.LOGIN, login);
                SharedPrefs.getInstance(this).putString(SharedPrefs.PASSWORD_HASH, password);
                SharedPrefs.getInstance(this).putLong(SharedPrefs.ID, employee.getId());

                if (keepLoggingCheckBox.isChecked()) {
                    SharedPrefs.getInstance(this).putBoolean(SharedPrefs.KEEP_LOGGED, true);
                }
                else {
                    SharedPrefs.getInstance(this).putBoolean(SharedPrefs.KEEP_LOGGED, false);
                }
                startActivity(new Intent(this, MainActivity.class));
                finish();
            });
        });

        if (SharedPrefs.getInstance(this).getBoolean(SharedPrefs.KEEP_LOGGED) &&
                SharedPrefs.getInstance(this).getString(SharedPrefs.LOGIN) != null &&
                SharedPrefs.getInstance(this).getString(SharedPrefs.PASSWORD_HASH) != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void initUI() {
        loginEditText = findViewById(R.id.login_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        signInBtn = findViewById(R.id.sign_in_btn);
        keepLoggingCheckBox = findViewById(R.id.keep_logging_check_box);
        forgotPasswordTextView = findViewById(R.id.forgot_password_text_view);
        passwordEyeImageView = findViewById(R.id.password_eye_image_view);

        loginEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(loginEditText, hasFocus);
        });

        passwordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(passwordEditText, hasFocus);
        });

        passwordEyeImageView.setOnClickListener(v -> {
            Log.i(TAG, "passwordEye clicked()" + showPassword);
            if (showPassword) {
                passwordEyeImageView.setImageResource(R.drawable.ic_hide_password);
                passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                showPassword = false;
            }
            else {
                passwordEyeImageView.setImageResource(R.drawable.ic_show_password);
                passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                showPassword = true;
            }
        });
    }

    private void obtainFcmToken(@NonNull final Context context) {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Fetching FCM registration token failed", task.getException());
                return;
            }
            final String token = task.getResult();
            SharedPrefs.getInstance(context).putString(SharedPrefs.TOKEN, token);


            Log.i(TAG, "obtainFcmToken(): " + token);
            Toast.makeText(context, token, Toast.LENGTH_SHORT).show();
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
}