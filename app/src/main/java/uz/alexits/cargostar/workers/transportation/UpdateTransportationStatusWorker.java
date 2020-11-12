package uz.alexits.cargostar.workers.transportation;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.api.params.TransportationStatusParams;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.transportation.Route;
import uz.alexits.cargostar.model.transportation.Transportation;
import uz.alexits.cargostar.utils.Constants;

public class UpdateTransportationStatusWorker extends Worker {
    private final long transportationId;
    private final long transportationStatusId;
    private final long transitPointId;

    public UpdateTransportationStatusWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.transportationId = getInputData().getLong(Constants.KEY_TRANSPORTATION_ID, -1L);
        this.transportationStatusId = getInputData().getLong(Constants.KEY_TRANSPORTATION_STATUS_ID, -1L);
        this.transitPointId = getInputData().getLong(Constants.KEY_TRANSIT_POINT_ID, -1L);
    }

    @NonNull
    @Override
    public Result doWork() {
        if (transportationId == -1L) {
            Log.e(TAG, "updateTransportationStatus(): empty transportation id");
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            return Result.failure();
        }
        if (transportationStatusId == -1L) {
            Log.e(TAG, "updateTransportationStatus(): empty status id");
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            return Result.failure();
        }
        if (transitPointId == -1L) {
            Log.e(TAG, "updateTransportationStatus(): empty transit point id");
            Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            return Result.failure();
        }
        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.LOGIN),
                    SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.PASSWORD_HASH));
            final Response<Transportation> response = RetrofitClient.getInstance(getApplicationContext())
                    .updateTransportationStatus(transportationId, transitPointId, transportationStatusId);

            if (response.code() == 200 || response.code() == 201) {
                if (response.isSuccessful()) {
                    final Transportation transportation = response.body();

                    Log.i(TAG, "updateTransportationStatus(): response=" + transportation);

                    final int rowId = LocalCache.getInstance(getApplicationContext()).transportationDao().updateTransportation(transportation);

                    if (rowId <= 0) {
                        Log.e(TAG, "updateTransportationStatus(): couldn't insert " + transportation);
                        return Result.failure();
                    }

                    final Data outputData = new Data.Builder()
                            .putLong(Constants.KEY_TRANSPORTATION_ID, transportation.getId())
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
