package uz.alexits.cargostar.workers.requests;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.shipping.Request;

import java.io.IOException;
import java.util.List;
import retrofit2.Response;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class FetchRequestsWorker extends Worker {
    private final int perPage;

    public FetchRequestsWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);
        this.perPage = getInputData().getInt(SyncWorkRequest.KEY_PER_PAGE, SyncWorkRequest.DEFAULT_PER_PAGE);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.LOGIN),
                    SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.PASSWORD_HASH));
            final Response<List<Request>> response = RetrofitClient.getInstance(getApplicationContext()).getPublicRequests(perPage);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchAllRequests(): response=" + response.body());
                    final List<Request> publicRequestList = response.body();

                    LocalCache.getInstance(getApplicationContext()).requestDao().dropAndInsertRequestList(publicRequestList);
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

    private static final String TAG = FetchRequestsWorker.class.toString();
}
