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
import uz.alexits.cargostar.model.location.Branche;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.model.transportation.Request;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class FetchCountriesWorker extends Worker {
    private final int perPage;
    @Nullable private final String login;
    @Nullable private final String password;
    private final String token;
    private final long lastId;

    public FetchCountriesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.perPage = getInputData().getInt(SyncWorkRequest.KEY_PER_PAGE, SyncWorkRequest.DEFAULT_PER_PAGE);
        this.login = getInputData().getString(Constants.KEY_LOGIN);
        this.password = getInputData().getString(Constants.KEY_PASSWORD);
        this.token = getInputData().getString(Constants.KEY_TOKEN);
        this.lastId = getInputData().getLong(Constants.LAST_COUNTRY_ID, 0L);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        if (login == null || password == null) {
            Log.e(TAG, "fetchCountries(): login or password is empty");
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            return Result.failure();
        }

        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(login, password);
            Response<List<Country>> response = null;

            if (lastId > 0) {
                response = RetrofitClient.getInstance(getApplicationContext()).getCountries(perPage, lastId);
            }
            else {
                response = RetrofitClient.getInstance(getApplicationContext()).getCountries(perPage);
            }

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    final List<Country> countryList = response.body();

                    LocalCache.getInstance(getApplicationContext()).locationDao().insertCountries(countryList);

                    final Data outputData = new Data.Builder()
                            .putString(Constants.KEY_LOGIN, login)
                            .putString(Constants.KEY_PASSWORD, password)
                            .putString(Constants.KEY_TOKEN, token)
                            .putInt(Constants.KEY_PROGRESS, 10)
                            .build();
                    return ListenableWorker.Result.success(outputData);
                }
            }
            else {
                Log.e(TAG, "fetchCountries(): " + response.errorBody());
            }
            return ListenableWorker.Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "fetchCountries(): ", e);
            return ListenableWorker.Result.failure();
        }
    }

    private static final String TAG = FetchCountriesWorker.class.toString();
}

