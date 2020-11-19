package uz.alexits.cargostar.workers.transportation;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.io.IOException;
import java.util.List;
import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.transportation.Transportation;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class FetchTransportationsWorker extends Worker {
    private final int perPage;
    private String login;
    private String password;

    public FetchTransportationsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.perPage = getInputData().getInt(SyncWorkRequest.KEY_PER_PAGE, SyncWorkRequest.DEFAULT_PER_PAGE);
        this.login = SharedPrefs.getInstance(context).getString(SharedPrefs.LOGIN);
        this.password = SharedPrefs.getInstance(context).getString(SharedPrefs.PASSWORD_HASH);

        if (login == null || password == null) {
            this.login = getInputData().getString(Constants.KEY_LOGIN);
            this.password = getInputData().getString(Constants.KEY_PASSWORD);
        }
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(login, password);
            final Response<List<Transportation>> response = RetrofitClient.getInstance(getApplicationContext()).getCurrentTransportations(perPage);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    final List<Transportation> currentTransportations = response.body();

                    final long[] rowsInserted = LocalCache.getInstance(getApplicationContext()).transportationDao().insertTransportationList(currentTransportations);

                    return ListenableWorker.Result.success(new Data.Builder()
                            .putString(Constants.KEY_LOGIN, login)
                            .putString(Constants.KEY_PASSWORD, password).build());
                }
            }
            else {
                Log.e(TAG, "fetchAllTransportations(): " + response.errorBody());
            }
            return ListenableWorker.Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "doWork(): ", e);
            return ListenableWorker.Result.failure();
        }
    }

    private static final String TAG = FetchTransportationsWorker.class.toString();
}
