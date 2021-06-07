package uz.alexits.cargostar.workers.transportation;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;

import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.entities.params.TransportationStatusParams;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.entities.transportation.Transportation;
import uz.alexits.cargostar.utils.Constants;

public class UpdateTransportationStatusWorker extends Worker {
    private final TransportationStatusParams params;

    public UpdateTransportationStatusWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.params = new TransportationStatusParams(
                getInputData().getLong(Constants.KEY_TRANSPORTATION_ID, 0L),
                getInputData().getLong(Constants.KEY_TRANSPORTATION_STATUS_ID, 0L),
                getInputData().getLong(Constants.KEY_TRANSIT_POINT_ID, 0L));
    }

    @NonNull
    @Override
    public Result doWork() {
        if (params.getTransportationId() <= 0L) {
            Log.e(TAG, "updateTransportationStatus(): empty transportation id");
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            return Result.failure();
        }
        if (params.getTransportationStatusId() <= 0L) {
            Log.e(TAG, "updateTransportationStatus(): empty status id");
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            return Result.failure();
        }
        if (params.getTransitPointId() <= 0L) {
            Log.e(TAG, "updateTransportationStatus(): empty transit point id");
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            return Result.failure();
        }
        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(
                    SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_LOGIN),
                    SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_PASSWORD));
            final Response<Transportation> response = RetrofitClient.getInstance(getApplicationContext())
                    .updateTransportationStatus(params);

            if (response.code() == 200 || response.code() == 201) {
                if (response.isSuccessful()) {
                    final Transportation transportation = response.body();

                    if (transportation == null) {
                        return Result.success(new Data.Builder().putLong(Constants.KEY_TRANSPORTATION_ID, params.getTransportationId()).build());
                    }
                    final int rowId = LocalCache.getInstance(getApplicationContext()).transportationDao().updateTransportation(transportation);

                    Log.i(TAG, "updateTransportationStatus(): response=" + transportation);

                    if (rowId <= 0) {
                        Log.e(TAG, "updateTransportationStatus(): couldn't insert " + transportation);
                        return Result.failure();
                    }
                    final Data outputData = new Data.Builder()
                            .putLong(Constants.KEY_TRANSPORTATION_ID, params.getTransportationId())
                            .putLong(Constants.KEY_CURRENT_TRANSIT_POINT_ID, transportation.getCurrentTransitionPointId())
                            .putLong(Constants.KEY_CURRENT_STATUS_ID, transportation.getTransportationStatusId())
                            .putString(Constants.KEY_CURRENT_STATUS_NAME, transportation.getTransportationStatusName())
                            .build();

                    return Result.success(outputData);
                }
            }
            else {
                Log.e(TAG, "doWork(): " + response.errorBody());
            }
            return Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "doWork(): ", e);
            return Result.failure();
        }
    }

    private static final String TAG = UpdateTransportationStatusWorker.class.toString();
}
