package uz.alexits.cargostar.workers.calculation;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.calculation.Vat;
import uz.alexits.cargostar.model.calculation.ZoneSettings;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class FetchVatWorker extends Worker {
    public FetchVatWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.LOGIN),
                    SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.PASSWORD_HASH));
            final Response<Vat> response = RetrofitClient.getInstance(getApplicationContext()).getVat();

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchVat(): response=" + response.body());
                    final Vat vat = response.body();
                    LocalCache.getInstance(getApplicationContext()).packagingDao().insertVat(vat);
                    return ListenableWorker.Result.success();
                }
            }
            else {
                Log.e(TAG, "fetchVat(): " + response.errorBody());
            }
            return ListenableWorker.Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "fetchVat(): ", e);
            return ListenableWorker.Result.failure();
        }
    }

    private static final String TAG = FetchVatWorker.class.toString();
}
