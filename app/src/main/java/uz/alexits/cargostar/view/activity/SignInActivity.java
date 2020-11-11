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
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import uz.alexits.cargostar.R;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.utils.UiUtils;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = SignInActivity.class.toString();
    private static boolean showPassword = false;

    private EditText loginEditText;
    private EditText passwordEditText;
    private Button signInBtn;
    private CheckBox keepLoggingCheckBox;
    private TextView forgotPasswordTextView;
    private ImageView passwordEyeImageView;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        initUI();

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

            final String token = SharedPrefs.getInstance(this).getString(SharedPrefs.TOKEN);

            if (TextUtils.isEmpty(token)) {
                Log.e(TAG, "onCreate(): empty token");
                Toast.makeText(this, "TOKEN iS EMPTY!!!", Toast.LENGTH_SHORT).show();
            }

            final UUID fetchBranchesAndSignInWorkerId = SyncWorkRequest.fetchBranchesAndSignIn(this, 10000, login, password, token);

            Log.i(TAG, "login=" + login + " password=" + password);

            WorkManager.getInstance(this).getWorkInfoByIdLiveData(fetchBranchesAndSignInWorkerId).observe(this, workInfo -> {
                if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                    progressBar.setVisibility(View.INVISIBLE);
                    signInBtn.setEnabled(true);

                    final Data outputData = workInfo.getOutputData();
                    final long courierId = outputData.getLong(SharedPrefs.ID, -1);
                    final long brancheId = outputData.getLong(SharedPrefs.BRANCH_ID, -1);

                    if (courierId == -1) {
                        Log.e(TAG, "Ошибка: ID курьера пустой");
                        Toast.makeText(this, "Ошибка: ID курьера пустой", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SharedPrefs.getInstance(this).putBoolean(SharedPrefs.KEEP_LOGGED, keepLoggingCheckBox.isChecked());
                    SharedPrefs.getInstance(this).putString(SharedPrefs.LOGIN, login);
                    SharedPrefs.getInstance(this).putString(SharedPrefs.PASSWORD_HASH, password);
                    SharedPrefs.getInstance(this).putString(SharedPrefs.TOKEN, token);
                    SharedPrefs.getInstance(this).putLong(SharedPrefs.ID, courierId);
                    SharedPrefs.getInstance(this).putLong(SharedPrefs.BRANCH_ID, brancheId);

                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                    return;
                }
                if (workInfo.getState() == WorkInfo.State.FAILED || workInfo.getState() == WorkInfo.State.CANCELLED) {
                    Log.e(TAG, "insertLocationData(): failed to insert location data");
                    Toast.makeText(this, "Ошибка инициализации", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    signInBtn.setEnabled(true);
                    return;
                }
                Log.i(TAG, "workInfo: " + workInfo.getState() + " attempt=" + workInfo.getRunAttemptCount());
                progressBar.setVisibility(View.VISIBLE);
                signInBtn.setEnabled(false);
            });
        });
    }

    private void initUI() {
        loginEditText = findViewById(R.id.login_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        signInBtn = findViewById(R.id.sign_in_btn);
        keepLoggingCheckBox = findViewById(R.id.keep_logging_check_box);
        forgotPasswordTextView = findViewById(R.id.forgot_password_text_view);
        passwordEyeImageView = findViewById(R.id.password_eye_image_view);
        progressBar = findViewById(R.id.progress_bar);

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
}