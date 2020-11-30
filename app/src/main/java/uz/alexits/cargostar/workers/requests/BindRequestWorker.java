package uz.alexits.cargostar.workers.requests;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;

import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.transportation.Request;
import uz.alexits.cargostar.utils.Constants;

public class BindRequestWorker extends Worker {
    private final long courierId;
    private final long requestId;

    public BindRequestWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.courierId = getInputData().getLong(Constants.KEY_COURIER_ID, -1L);
        this.requestId = getInputData().getLong(Constants.KEY_REQUEST_ID, -1L);
    }

    @NonNull
    @Override
    public Result doWork() {
        if (courierId <= 0 || requestId <= 0) {
            Log.e(TAG, "bindRequest(): empty fields");
            return Result.failure();
        }
        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(
                    SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.LOGIN),
                    SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.PASSWORD_HASH));

            final Response<Request> response = RetrofitClient.getInstance(getApplicationContext()).bindRequest(requestId, courierId);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    final Request request = response.body();

                    if (request == null) {
                        Log.e(TAG, "bindRequest(): courier is NULL");
                        return Result.failure();
                    }

                    Log.i(TAG, "bindRequest(): " + request);

                    final int rowUpdated = LocalCache.getInstance(getApplicationContext()).requestDao().updateRequest(request);

                    if (rowUpdated == -1) {
                        Log.e(TAG, "bindRequest(): couldn't insert courier " + request);
                        return Result.failure();
                    }

                    final Data outputData = new Data.Builder()
                            .putLong(Constants.KEY_REQUEST_ID, request.getId())
                            .build();

                    return Result.success(outputData);
                }
            }
        }
        catch (IOException e) {
            Log.e(TAG, "bindRequest(): ", e);
            return Result.failure();
        }
        Log.e(TAG, "bindRequest(): unknown error");
        return Result.failure();
    }

    private static final String TAG = BindRequestWorker.class.toString();
}
