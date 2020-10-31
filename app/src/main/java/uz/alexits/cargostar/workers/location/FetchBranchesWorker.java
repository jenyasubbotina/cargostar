package uz.alexits.cargostar.workers.location;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.location.Branche;
import uz.alexits.cargostar.api.RetrofitClient;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import retrofit2.Response;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class FetchBranchesWorker extends Worker {
    private final int perPage;
    @Nullable private final String login;
    @Nullable private final String password;

    public FetchBranchesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.perPage = getInputData().getInt(SyncWorkRequest.KEY_PER_PAGE, -1);
        this.login = getInputData().getString(SharedPrefs.LOGIN);
        this.password = getInputData().getString(SharedPrefs.PASSWORD_HASH);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        if (login == null || password == null) {
            Log.e(TAG, "getBranches(): login or password is empty");
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            return Result.failure();
        }

        try {
            final Response<List<Branche>> response = RetrofitClient.getInstance(
                    getApplicationContext(), login, password)
                    .getBranches(perPage);

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
