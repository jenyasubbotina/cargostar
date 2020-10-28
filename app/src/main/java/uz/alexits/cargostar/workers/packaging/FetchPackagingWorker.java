package uz.alexits.cargostar.workers.packaging;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.model.packaging.Packaging;

import java.io.IOException;
import java.util.List;
import retrofit2.Response;

public class FetchPackagingWorker extends Worker {
    private static final String TAG = FetchPackagingWorker.class.toString();

    public FetchPackagingWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        try {
            final Response<List<Packaging>> response = RetrofitClient.getInstance(getApplicationContext()).getPackaging();

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchAllPackaging(): response=" + response.body());
                    final List<Packaging> packagingList = response.body();
                    LocalCache.getInstance(getApplicationContext()).packagingDao().insertPackaging(packagingList);
                    return ListenableWorker.Result.success();
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
}
