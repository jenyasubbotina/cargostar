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
import uz.alexits.cargostar.entities.calculation.Zone;
import uz.alexits.cargostar.entities.calculation.ZoneCountry;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class FetchZoneCountriesWorker extends Worker {
    private String login;
    private String password;
    private final String token;

    private final Integer perPage;
    private Integer pageCount;

    public FetchZoneCountriesWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);

        this.login = SharedPrefs.getInstance(context).getString(Constants.KEY_LOGIN);
        this.password = SharedPrefs.getInstance(context).getString(Constants.KEY_PASSWORD);
        this.token = getInputData().getString(Constants.KEY_TOKEN);

        if (login == null || password == null) {
            this.login = getInputData().getString(Constants.KEY_LOGIN);
            this.password = getInputData().getString(Constants.KEY_PASSWORD);
        }
        this.perPage = 50;
        this.pageCount = 1;
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(login, password);

            for (int i = 1; i <= pageCount; i++) {
                Response<List<ZoneCountry>> response = RetrofitClient.getInstance(getApplicationContext()).getZoneCountries(i, perPage);

                String pageCountStr = response.headers().get(RetrofitClient.PAGINATION_PAGE_COUNT);

                if (pageCountStr != null) {
                    pageCount = Integer.parseInt(pageCountStr);
                }

                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        final List<ZoneCountry> zoneCountryList = response.body();

                        LocalCache.getInstance(getApplicationContext()).zoneDao().insertZoneCountries(zoneCountryList);
                    }
                }
                else {
                    Log.e(TAG, "fetchZoneCountries(): " + response.errorBody());
                    return Result.failure();
                }
            }
            return Result.success(new Data.Builder()
                    .putString(Constants.KEY_LOGIN, login)
                    .putString(Constants.KEY_PASSWORD, password)
                    .putString(Constants.KEY_TOKEN, token)
                    .build());
        }
        catch (IOException e) {
            Log.e(TAG, "fetchZoneCountries(): ", e);
            return Result.failure();
        }
    }

    private static final String TAG = FetchZoneCountriesWorker.class.toString();
}
