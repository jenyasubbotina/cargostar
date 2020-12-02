package uz.alexits.cargostar.workers.calculation;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.calculation.Vat;
import uz.alexits.cargostar.model.calculation.ZoneSettings;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class FetchVatWorker extends Worker {
    private String login;
    private String password;
    private final String token;

    public FetchVatWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);
        this.login = SharedPrefs.getInstance(context).getString(SharedPrefs.LOGIN);
        this.password = SharedPrefs.getInstance(context).getString(SharedPrefs.PASSWORD_HASH);
        this.token = getInputData().getString(Constants.KEY_TOKEN);

        if (login == null || password == null) {
            this.login = getInputData().getString(Constants.KEY_LOGIN);
            this.password = getInputData().getString(Constants.KEY_PASSWORD);
        }
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(login, password);
            final Response<Vat> response = RetrofitClient.getInstance(getApplicationContext()).getVat();

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchVat(): response=" + response.body());
                    final Vat vat = response.body();

                    LocalCache.getInstance(getApplicationContext()).packagingDao().insertVat(vat);

                    final Data outputData = new Data.Builder()
                            .putString(Constants.KEY_LOGIN, login)
                            .putString(Constants.KEY_PASSWORD, password)
                            .putString(Constants.KEY_TOKEN, token)
                            .putInt(Constants.KEY_PROGRESS, 65)
                            .build();
                    return ListenableWorker.Result.success(outputData);
                }
            }
            else {
                Log.e(TAG, "fetchVat(): " + response.errorBody());
            }
            return ListenableWorker.Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "fetchVat(): ", e);
            return ListenableWorker.Result.failure();
        }
    }

    private static final String TAG = FetchVatWorker.class.toString();
}
