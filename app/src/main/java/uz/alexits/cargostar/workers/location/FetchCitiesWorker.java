package uz.alexits.cargostar.workers.location;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.entities.location.City;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class FetchCitiesWorker extends Worker {
    @Nullable private final String login;
    @Nullable private final String password;
    private final String token;
    private final long lastId;

    public FetchCitiesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.login = getInputData().getString(Constants.KEY_LOGIN);
        this.password = getInputData().getString(Constants.KEY_PASSWORD);
        this.token = getInputData().getString(Constants.KEY_TOKEN);
        this.lastId = getInputData().getLong(Constants.LAST_CITY_ID, 0L);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        if (login == null || password == null) {
            Log.e(TAG, "fetchCities(): login or password is empty");
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            return Result.failure();
        }

        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(login, password);
            Response<List<City>> response = null;

            if (lastId > 0) {
                response = RetrofitClient.getInstance(getApplicationContext()).getCities(SyncWorkRequest.DEFAULT_PER_PAGE, lastId);
            }
            else {
                response = RetrofitClient.getInstance(getApplicationContext()).getCities(SyncWorkRequest.DEFAULT_PER_PAGE);
            }

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    final List<City> cityList = response.body();

                    LocalCache.getInstance(getApplicationContext()).locationDao().insertCities(cityList);

                    final Data outputData = new Data.Builder()
                            .putString(Constants.KEY_LOGIN, login)
                            .putString(Constants.KEY_PASSWORD, password)
                            .putString(Constants.KEY_TOKEN, token)
                            .putInt(Constants.KEY_PROGRESS, 20).build();
                    return ListenableWorker.Result.success(outputData);
                }
            }
            else {
                Log.e(TAG, "fetchCities(): " + response.errorBody());
            }
            return ListenableWorker.Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "fetchCities(): ", e);
            return ListenableWorker.Result.failure();
        }
    }

    private static final String TAG = FetchCitiesWorker.class.toString();
}
