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
import uz.alexits.cargostar.entities.location.Branche;
import uz.alexits.cargostar.entities.transportation.Request;

import java.io.IOException;
import java.util.List;
import retrofit2.Response;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class FetchRequestsWorker extends Worker {
    private String login;
    private String password;
    private final String token;

    private final Integer perPage;
    private Integer pageCount;

    public FetchRequestsWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);

        this.login = SharedPrefs.getInstance(context).getString(Constants.KEY_LOGIN);
        this.password = SharedPrefs.getInstance(context).getString(Constants.KEY_PASSWORD);
        this.token = getInputData().getString(Constants.KEY_TOKEN);

        if (login == null || password == null) {
            this.login = getInputData().getString(Constants.KEY_LOGIN);
            this.password = getInputData().getString(Constants.KEY_PASSWORD);
        }
        this.perPage = 50;
        this.pageCount = 1;
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(login, password);

            for (int i = 1; i <= pageCount; i++) {
                Response<List<Request>> response = RetrofitClient.getInstance(getApplicationContext()).getPublicRequests(i, perPage);

                String pageCountStr = response.headers().get(RetrofitClient.PAGINATION_PAGE_COUNT);

                if (pageCountStr != null) {
                    pageCount = Integer.parseInt(pageCountStr);
                }

                if (response.code() == 200) {
                    if (response.isSuccessful()) {
                        final List<Request> requestList = response.body();

                        LocalCache.getInstance(getApplicationContext()).requestDao().insertRequests(requestList);
                    }
                }
                else {
                    Log.e(TAG, "fetchRequestList(): " + response.errorBody());
                    return Result.failure();
                }
            }
            return Result.success(new Data.Builder()
                    .putString(Constants.KEY_LOGIN, login)
                    .putString(Constants.KEY_PASSWORD, password)
                    .putString(Constants.KEY_TOKEN, token)
                    .build());
        }
        catch (IOException e) {
            Log.e(TAG, "fetchRequestList(): ", e);
            return Result.failure();
        }
    }

    private static final String TAG = FetchRequestsWorker.class.toString();
}
