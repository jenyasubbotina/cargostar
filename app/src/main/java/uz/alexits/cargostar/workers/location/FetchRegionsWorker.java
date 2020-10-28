package uz.alexits.cargostar.workers.location;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.workers.SyncWorkRequest;

import java.io.IOException;
import java.util.List;
import retrofit2.Response;

public class FetchRegionsWorker extends Worker {
    private final int perPage;

    public FetchRegionsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.perPage = getInputData().getInt(SyncWorkRequest.KEY_PER_PAGE, -1);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            final Response<List<Region>> response = RetrofitClient.getInstance(getApplicationContext()).getRegions(perPage);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchAllRegions(): response=" + response.body());
                    final List<Region> regionList = response.body();
                    LocalCache.getInstance(getApplicationContext()).locationDao().insertRegions(regionList);
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

    private static final String TAG = FetchRegionsWorker.class.toString();
}
