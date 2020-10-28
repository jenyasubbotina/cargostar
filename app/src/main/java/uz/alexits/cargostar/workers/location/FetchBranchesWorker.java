package uz.alexits.cargostar.workers.location;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.model.location.Branche;
import uz.alexits.cargostar.api.RetrofitClient;

import java.io.IOException;
import java.util.List;
import retrofit2.Response;

public class FetchBranchesWorker extends Worker {

    public FetchBranchesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        try {
            final Response<List<Branche>> response = RetrofitClient.getInstance(getApplicationContext()).getBranches();

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchAllBranches(): response=" + response.body());
                    final List<Branche> brancheList = response.body();
                    LocalCache.getInstance(getApplicationContext()).locationDao().insertBranches(brancheList);
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

    private static final String TAG = FetchBranchesWorker.class.toString();
}
