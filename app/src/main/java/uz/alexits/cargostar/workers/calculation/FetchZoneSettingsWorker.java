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
import uz.alexits.cargostar.entities.calculation.ZoneSettings;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class FetchZoneSettingsWorker extends Worker {
    private String login;
    private String password;
    private final String token;

    public FetchZoneSettingsWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);
        this.login = SharedPrefs.getInstance(context).getString(Constants.KEY_LOGIN);
        this.password = SharedPrefs.getInstance(context).getString(Constants.KEY_PASSWORD);
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
            final Response<List<ZoneSettings>> response = RetrofitClient.getInstance(getApplicationContext()).getZoneSettings(SyncWorkRequest.DEFAULT_PER_PAGE);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    final List<ZoneSettings> zoneSettingsList = response.body();

                    //foreign might be null (:facepalm)
//                    for (final ZoneSettings zoneSettings : zoneSettingsList) {
//                        Log.i(TAG, "zoneSetting: " + zoneSettings);
//                        LocalCache.getInstance(getApplicationContext()).zoneDao().insertZoneSettings(zoneSettings);
//                    }

                    LocalCache.getInstance(getApplicationContext()).zoneDao().insertZoneSettingsList(zoneSettingsList);

                    final Data outputData = new Data.Builder()
                            .putString(Constants.KEY_LOGIN, login)
                            .putString(Constants.KEY_PASSWORD, password)
                            .putString(Constants.KEY_TOKEN, token)
                            .putInt(Constants.KEY_PROGRESS, 60).build();
                    return ListenableWorker.Result.success(outputData);
                }
            }
            else {
                Log.e(TAG, "fetchAllZoneSettings(): " + response.errorBody());
            }
            return ListenableWorker.Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "fetchAllZoneSettings(): ", e);
            return ListenableWorker.Result.failure();
        }
    }

    private static final String TAG = FetchZoneSettingsWorker.class.toString();
}
