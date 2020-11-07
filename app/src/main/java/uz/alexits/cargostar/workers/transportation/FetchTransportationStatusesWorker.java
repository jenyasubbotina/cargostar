package uz.alexits.cargostar.workers.transportation;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import uz.alexits.cargostar.model.transportation.TransportationStatus;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class FetchTransportationStatusesWorker extends Worker {
    private final int perPage;

    public FetchTransportationStatusesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.perPage = getInputData().getInt(SyncWorkRequest.KEY_PER_PAGE, SyncWorkRequest.DEFAULT_PER_PAGE);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.LOGIN),
                    SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.PASSWORD_HASH));
            final Response<List<TransportationStatus>> response = RetrofitClient.getInstance(getApplicationContext()).getTransportationStatusList(perPage);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    final List<TransportationStatus> transportationStatusList = response.body();
                    LocalCache.getInstance(getApplicationContext()).transportationDao().insertTransportationStatusList(transportationStatusList);
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

    private static final String TAG = FetchTransportationStatusesWorker.class.toString();
}
