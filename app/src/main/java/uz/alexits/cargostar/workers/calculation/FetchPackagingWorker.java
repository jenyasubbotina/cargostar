package uz.alexits.cargostar.workers.calculation;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.calculation.Packaging;

import java.io.IOException;
import java.util.List;
import retrofit2.Response;
import uz.alexits.cargostar.utils.Constants;

public class FetchPackagingWorker extends Worker {
    private String login;
    private String password;

    public FetchPackagingWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);
        this.login = SharedPrefs.getInstance(context).getString(SharedPrefs.LOGIN);
        this.password = SharedPrefs.getInstance(context).getString(SharedPrefs.PASSWORD_HASH);

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
            final Response<List<Packaging>> response = RetrofitClient.getInstance(getApplicationContext()).getPackaging();

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchAllPackaging(): response=" + response.body());
                    final List<Packaging> packagingList = response.body();
                    LocalCache.getInstance(getApplicationContext()).packagingDao().insertPackagingTransaction(packagingList);
                    return ListenableWorker.Result.success(new Data.Builder()
                            .putString(Constants.KEY_LOGIN, login)
                            .putString(Constants.KEY_PASSWORD, password).build());
                }
            }
            else {
                Log.e(TAG, "doWork(): " + response.errorBody());
            }
            return ListenableWorker.Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "doWork(): ", e);
            return ListenableWorker.Result.failure();
        }
    }

    private static final String TAG = FetchPackagingWorker.class.toString();
}
