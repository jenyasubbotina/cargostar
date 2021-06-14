package uz.alexits.cargostar.workers.transportation;

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
import uz.alexits.cargostar.entities.transportation.TransportationStatus;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class FetchTransportationStatusesWorker extends Worker {
    private String login;
    private String password;
    private final String token;

    public FetchTransportationStatusesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.login = SharedPrefs.getInstance(context).getString(Constants.KEY_LOGIN);
        this.password = SharedPrefs.getInstance(context).getString(Constants.KEY_PASSWORD);

        this.token = getInputData().getString(Constants.KEY_TOKEN);

        if (login == null || password == null) {
            this.login = getInputData().getString(Constants.KEY_LOGIN);
            this.password = getInputData().getString(Constants.KEY_PASSWORD);
        }
        Log.i(TAG, "FetchTransportationStatusesWorker(): login=" + login + " password=" + password);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(login, password);
            final Response<List<TransportationStatus>> response = RetrofitClient.getInstance(getApplicationContext()).getTransportationStatusList(SyncWorkRequest.DEFAULT_PER_PAGE);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    final List<TransportationStatus> transportationStatusList = response.body();

                    LocalCache.getInstance(getApplicationContext()).transportationStatusDao().insertTransportationStatusList(transportationStatusList);

                    final Data outputData = new Data.Builder()
                            .putString(Constants.KEY_LOGIN, login)
                            .putString(Constants.KEY_PASSWORD, password)
                            .putString(Constants.KEY_TOKEN, token)
                            .putInt(Constants.KEY_PROGRESS, 90)
                            .build();
                    return ListenableWorker.Result.success(outputData);
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

    private static final String TAG = FetchTransportationStatusesWorker.class.toString();
}
