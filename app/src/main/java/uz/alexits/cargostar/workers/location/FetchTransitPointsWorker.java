package uz.alexits.cargostar.workers.location;

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
import uz.alexits.cargostar.entities.location.TransitPoint;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.SyncWorkRequest;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

public class FetchTransitPointsWorker extends Worker {
    private String login;
    private String password;
    private final String token;

    public FetchTransitPointsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.login = SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_LOGIN);
        this.password = SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_PASSWORD);
        this.token = getInputData().getString(Constants.KEY_TOKEN);

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
            final Response<List<TransitPoint>> response = RetrofitClient.getInstance(getApplicationContext()).getTransitPoints(SyncWorkRequest.DEFAULT_PER_PAGE);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    final List<TransitPoint> transitPointList = response.body();

                    Log.i(TAG, "fetchTransitPointList(): " + transitPointList);

                    LocalCache.getInstance(getApplicationContext()).locationDao().insertTransitPoints(transitPointList);

                    final Data outputData = new Data.Builder()
                            .putString(Constants.KEY_LOGIN, login)
                            .putString(Constants.KEY_PASSWORD, password)
                            .putString(Constants.KEY_TOKEN, token)
                            .putInt(Constants.KEY_PROGRESS, 30).build();
                    return ListenableWorker.Result.success(outputData);
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
