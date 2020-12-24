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

public class GetLastRequestId extends Worker {
    private final int perPage;
    private final String login;
    private final String password;

    public GetLastRequestId(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.perPage = getInputData().getInt(SyncWorkRequest.KEY_PER_PAGE, SyncWorkRequest.DEFAULT_PER_PAGE);
        this.login = SharedPrefs.getInstance(context).getString(Constants.KEY_LOGIN);
        this.password = SharedPrefs.getInstance(context).getString(Constants.KEY_PASSWORD);
    }

    @NonNull
    @Override
    public Result doWork() {
        final long lastId = LocalCache.getInstance(getApplicationContext()).requestDao().getLastRequestId();
        return Result.success(
                new Data.Builder()
                        .putLong(SyncWorkRequest.KEY_PER_PAGE, perPage)
                        .putString(Constants.KEY_LOGIN, login)
                        .putString(Constants.KEY_PASSWORD, password)
                        .putLong(Constants.LAST_REQUEST_ID, lastId)
                        .build());
    }
}
