package uz.alexits.cargostar.workers.calculation;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.calculation.Provider;

import java.io.IOException;
import java.util.List;
import retrofit2.Response;

public class FetchProvidersWorker extends Worker {
    private static final String TAG = FetchProvidersWorker.class.toString();

    public FetchProvidersWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.LOGIN),
                    SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.PASSWORD_HASH));
            final Response<List<Provider>> response = RetrofitClient.getInstance(getApplicationContext()).getProviders();

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchAllProviders(): response=" + response.body());
                    final List<Provider> providerList = response.body();
                    LocalCache.getInstance(getApplicationContext()).packagingDao().insertProviders(providerList);
                    return ListenableWorker.Result.success();
                }
            }
            else {
                Log.e(TAG, "fetchAllProviders(): " + response.errorBody());
            }
            return ListenableWorker.Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "fetchAllProviders(): ", e);
            return ListenableWorker.Result.failure();
        }
    }
}
