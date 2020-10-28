package uz.alexits.cargostar.workers.location;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.workers.SyncWorkRequest;

import java.io.IOException;
import java.util.List;
import retrofit2.Response;

public class FetchCountriesWorker extends Worker {
    private final int perPage;

    public FetchCountriesWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);
        this.perPage = getInputData().getInt(SyncWorkRequest.KEY_PER_PAGE, -1);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            Log.i(TAG, "perPage=" + perPage);
            final Response<List<Country>> response = RetrofitClient.getInstance(getApplicationContext()).getCountries(perPage);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchAllCountries(): response=" + response.body());
                    final List<Country> countryList = response.body();
                    LocalCache.getInstance(getApplicationContext()).locationDao().insertCountries(countryList);
                    return Result.success();
                }
            }
            else {
                Log.e(TAG, "doWork(): " + response.errorBody());
            }
            return Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "doWork(): ", e);
            return Result.failure();
        }
    }

    private static final String TAG = FetchCountriesWorker.class.toString();
}
