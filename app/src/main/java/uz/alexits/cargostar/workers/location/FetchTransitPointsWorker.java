package uz.alexits.cargostar.workers.location;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.location.TransitPoint;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.SyncWorkRequest;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class FetchTransitPointsWorker extends Worker {
    private final int perPage;
    private String login;
    private String password;

    public FetchTransitPointsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.perPage = getInputData().getInt(SyncWorkRequest.KEY_PER_PAGE, SyncWorkRequest.DEFAULT_PER_PAGE);
        this.login = SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.LOGIN);
        this.password = SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.PASSWORD_HASH);

        if (login == null || password == null) {
            this.login = getInputData().getString(Constants.KEY_LOGIN);
            this.password = getInputData().getString(Constants.KEY_PASSWORD);
        }
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.i(TAG, "login: " + login + " password: " + password);
        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(login, password);
            final Response<List<TransitPoint>> response = RetrofitClient.getInstance(getApplicationContext()).getTransitPoints(perPage);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    final List<TransitPoint> transitPointList = response.body();

                    Log.i(TAG, "fetchTransitPointList(): " + transitPointList);

                    LocalCache.getInstance(getApplicationContext()).locationDao().insertTransitPoints(transitPointList);
                    return Result.success(new Data.Builder()
                            .putString(Constants.KEY_LOGIN, login)
                            .putString(Constants.KEY_PASSWORD, password).build());
                }
            }
            else {
                Log.e(TAG, "fetchTransitPointList(): " + response.errorBody());
            }
            return Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "fetchTransitPointList(): ", e);
            return Result.failure();
        }
    }

    private static final String TAG = FetchTransitPointsWorker.class.toString();
}
