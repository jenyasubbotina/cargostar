package uz.alexits.cargostar.workers.requests;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class ReadRequestWorker extends Worker {
    private final long requestId;

    public ReadRequestWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.requestId = getInputData().getLong(Constants.KEY_REQUEST_ID, 0);
    }

    @NonNull
    @Override
    public Result doWork() {
        LocalCache.getInstance(getApplicationContext()).requestDao().readNewRequest(requestId, true);
        return Result.success();
    }
}
