package uz.alexits.cargostar.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.UUID;

import uz.alexits.cargostar.R;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.view.UiUtils;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class SignInActivity extends AppCompatActivity {
    private EditText loginEditText;
    private EditText passwordEditText;
    private Button signInBtn;
    private CheckBox keepLoggingCheckBox;
    private ImageView passwordEyeImageView;

    private ProgressBar progressBar;

    private static volatile boolean showPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        loginEditText = findViewById(R.id.login_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        signInBtn = findViewById(R.id.sign_in_btn);
        keepLoggingCheckBox = findViewById(R.id.keep_logging_check_box);
        passwordEyeImageView = findViewById(R.id.password_eye_image_view);
        progressBar = findViewById(R.id.progress_bar);

        if (getIntent() != null) {
            loginEditText.setText(getIntent().getStringExtra(Constants.KEY_LOGIN));
            passwordEditText.setText(getIntent().getStringExtra(Constants.KEY_PASSWORD));
        }

        showPassword = false;

        signInBtn.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);

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

            final String token = SharedPrefs.getInstance(this).getString(Constants.KEY_TOKEN);

            if (TextUtils.isEmpty(token)) {
                Log.e(TAG, "onCreate(): empty token");
                Toast.makeText(this, "TOKEN iS EMPTY!!!", Toast.LENGTH_SHORT).show();
            }

            final UUID synchronizeFirstTime = SyncWorkRequest.synchronizeFirstTime(this, login, password, token);

            WorkManager.getInstance(this).getWorkInfoByIdLiveData(synchronizeFirstTime).observe(this, workInfo -> {
                if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                    progressBar.setVisibility(View.INVISIBLE);
                    signInBtn.setEnabled(true);

                    final Data outputData = workInfo.getOutputData();
                    final long courierId = outputData.getLong(SharedPrefs.ID, 0);
                    final long brancheId = outputData.getLong(SharedPrefs.BRANCH_ID, 0);

                    if (courierId <= 0) {
                        Log.e(TAG, "Ошибка: ID курьера пустой");
                        Toast.makeText(this, "Ошибка: ID курьера пустой", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SharedPrefs.getInstance(this).putLong(SharedPrefs.ID, courierId);
                    SharedPrefs.getInstance(this).putLong(SharedPrefs.BRANCH_ID, brancheId);
                    SharedPrefs.getInstance(this).putString(Constants.KEY_LOGIN, login);
                    SharedPrefs.getInstance(this).putString(Constants.KEY_PASSWORD, password);
                    SharedPrefs.getInstance(this).putString(Constants.KEY_TOKEN, token);
                    SharedPrefs.getInstance(this).putBoolean(SharedPrefs.KEEP_LOGGED, keepLoggingCheckBox.isChecked());
                    SharedPrefs.getInstance(this).putBoolean(SharedPrefs.IS_LOGGED, true);


                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                    return;
                }
                if (workInfo.getState() == WorkInfo.State.FAILED || workInfo.getState() == WorkInfo.State.CANCELLED) {
                    Log.e(TAG, "synchronizeFirstTime(): chain work failed");
                    Toast.makeText(this, "Ошибка инициализации", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    signInBtn.setEnabled(true);
                    return;
                }
                if (workInfo.getState() == WorkInfo.State.BLOCKED) {
                    Log.i(TAG, "progress: " + workInfo.getProgress().getInt(Constants.KEY_PROGRESS, 0));
                }
                Log.i(TAG, "workInfo: " + workInfo.getState() + " attempt=" + workInfo.getRunAttemptCount());
                progressBar.setVisibility(View.VISIBLE);
                signInBtn.setEnabled(false);
            });
        });

        loginEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(loginEditText, hasFocus);
        });

        passwordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(passwordEditText, hasFocus);
        });

        passwordEyeImageView.setOnClickListener(v -> {
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

    private static final String TAG = SignInActivity.class.toString();
}