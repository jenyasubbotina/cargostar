package uz.alexits.cargostar.workers.transportation;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class GetLastTransportationId extends Worker {
    public GetLastTransportationId(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        final long lastId = LocalCache.getInstance(getApplicationContext()).transportationDao().getLastTransportationId();
        return Result.success(
                new Data.Builder()
                        .putLong(Constants.LAST_TRANSPORTATION_ID, lastId)
                        .build());
    }
}
