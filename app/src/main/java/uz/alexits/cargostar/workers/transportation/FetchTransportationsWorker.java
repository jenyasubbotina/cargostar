package uz.alexits.cargostar.workers.transportation;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.io.IOException;
import java.util.List;
import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.entities.transportation.Transportation;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class FetchTransportationsWorker extends Worker {
    private String login;
    private String password;
    private final String token;
    private final long lastId;

    public FetchTransportationsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.login = SharedPrefs.getInstance(context).getString(Constants.KEY_LOGIN);
        this.password = SharedPrefs.getInstance(context).getString(Constants.KEY_PASSWORD);
        this.token = getInputData().getString(Constants.KEY_TOKEN);
        this.lastId = getInputData().getLong(Constants.LAST_TRANSPORTATION_ID, 0L);

        if (login == null || password == null) {
            this.login = getInputData().getString(Constants.KEY_LOGIN);
            this.password = getInputData().getString(Constants.KEY_PASSWORD);
        }
    }

    @NonNull
    @Override
    public Result doWork() {
        final Data outputData = new Data.Builder()
                .putString(Constants.KEY_LOGIN, login)
                .putString(Constants.KEY_PASSWORD, password)
                .putString(Constants.KEY_TOKEN, token)
                .build();

        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(login, password);

            Response<List<Transportation>> response = null;

            if (lastId > 0) {
                response = RetrofitClient.getInstance(getApplicationContext()).getCurrentTransportations(SyncWorkRequest.DEFAULT_PER_PAGE, lastId);
            }
            else {
                response = RetrofitClient.getInstance(getApplicationContext()).getCurrentTransportations(SyncWorkRequest.DEFAULT_PER_PAGE);
            }
            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    final List<Transportation> currentTransportations = response.body();

                    if (currentTransportations == null || currentTransportations.isEmpty()) {
                        return Result.success(outputData);
                    }
                    LocalCache.getInstance(getApplicationContext()).transportationDao().insertTransportationList(currentTransportations);;
                    return Result.success(outputData);
                }
            }
            else {
                Log.e(TAG, "fetchAllTransportations(): " + response.errorBody());
            }
            return Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "doWork(): ", e);
            return Result.failure();
        }
    }

    private static final String TAG = FetchTransportationsWorker.class.toString();
}
