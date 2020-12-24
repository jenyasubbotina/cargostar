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
    private final int perPage;
    private final String login;
    private final String password;
    private final String token;

    public GetLastTransportationId(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.perPage = getInputData().getInt(SyncWorkRequest.KEY_PER_PAGE, SyncWorkRequest.DEFAULT_PER_PAGE);
        this.login = SharedPrefs.getInstance(context).getString(Constants.KEY_LOGIN);
        this.password = SharedPrefs.getInstance(context).getString(Constants.KEY_PASSWORD);
        this.token = getInputData().getString(Constants.KEY_TOKEN);
    }

    @NonNull
    @Override
    public Result doWork() {
        final long lastId = LocalCache.getInstance(getApplicationContext()).transportationDao().getLastTransportationId();
        return Result.success(
                new Data.Builder()
                        .putInt(SyncWorkRequest.KEY_PER_PAGE, SyncWorkRequest.DEFAULT_PER_PAGE)
                        .putString(Constants.KEY_LOGIN, login)
                        .putString(Constants.KEY_PASSWORD, password)
                        .putString(Constants.KEY_TOKEN, token)
                        .putLong(Constants.LAST_TRANSPORTATION_ID, lastId)
                        .build());
    }
}
