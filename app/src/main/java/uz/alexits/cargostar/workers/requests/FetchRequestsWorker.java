package uz.alexits.cargostar.workers.requests;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.transportation.Request;

import java.io.IOException;
import java.util.List;
import retrofit2.Response;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class FetchRequestsWorker extends Worker {
    private final int perPage;
    private String login;
    private String password;
    private final String token;
    private final long lastId;

    public FetchRequestsWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);
        this.perPage = getInputData().getInt(SyncWorkRequest.KEY_PER_PAGE, SyncWorkRequest.DEFAULT_PER_PAGE);
        this.login = SharedPrefs.getInstance(context).getString(Constants.KEY_LOGIN);
        this.password = SharedPrefs.getInstance(context).getString(Constants.KEY_PASSWORD);
        this.token = getInputData().getString(Constants.KEY_TOKEN);
        this.lastId = getInputData().getLong(Constants.LAST_REQUEST_ID, 0L);

        if (login == null || password == null) {
            this.login = getInputData().getString(Constants.KEY_LOGIN);
            this.password = getInputData().getString(Constants.KEY_PASSWORD);
        }
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(login, password);
            Response<List<Request>> response = null;

            if (lastId > 0) {
                response = RetrofitClient.getInstance(getApplicationContext()).getPublicRequests(perPage, lastId);
            }
            else {
                response = RetrofitClient.getInstance(getApplicationContext()).getPublicRequests(perPage);
            }

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchAllRequests(): response=" + response.body());
                    final List<Request> publicRequestList = response.body();

                    if (publicRequestList != null) {
                        LocalCache.getInstance(getApplicationContext()).requestDao().insertRequests(publicRequestList);
                    }
                    final Data outputData = new Data.Builder()
                            .putString(Constants.KEY_LOGIN, login)
                            .putString(Constants.KEY_PASSWORD, password)
                            .putString(Constants.KEY_TOKEN, token)
                            .putInt(Constants.KEY_PROGRESS, 70)
                            .build();
                    return ListenableWorker.Result.success(outputData);
                }
            }
            else {
                Log.e(TAG, "fetchRequests(): " + response.errorBody());
            }
            return ListenableWorker.Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "fetchRequests(): ", e);
            return ListenableWorker.Result.failure();
        }
    }

    private static final String TAG = FetchRequestsWorker.class.toString();
}
