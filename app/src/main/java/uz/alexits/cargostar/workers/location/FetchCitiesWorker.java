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
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class FetchCitiesWorker extends Worker {
    private final int perPage;
    @Nullable private final String login;
    @Nullable private final String password;

    public FetchCitiesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.perPage = getInputData().getInt(SyncWorkRequest.KEY_PER_PAGE, SyncWorkRequest.DEFAULT_PER_PAGE);
        this.login = getInputData().getString(Constants.KEY_LOGIN);
        this.password = getInputData().getString(Constants.KEY_PASSWORD);
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
            final Response<List<City>> response = RetrofitClient.getInstance(getApplicationContext()).getCities(perPage);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    final List<City> cityList = response.body();
                    LocalCache.getInstance(getApplicationContext()).locationDao().insertCities(cityList);
                    return ListenableWorker.Result.success(
                            new Data.Builder()
                                    .putString(Constants.KEY_LOGIN, login)
                                    .putString(Constants.KEY_PASSWORD, password).build());
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
