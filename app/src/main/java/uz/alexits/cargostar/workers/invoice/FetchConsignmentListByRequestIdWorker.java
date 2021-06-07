package uz.alexits.cargostar.workers.invoice;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.entities.transportation.Consignment;
import uz.alexits.cargostar.utils.Constants;

public class FetchConsignmentListByRequestIdWorker extends Worker {
    private final long requestId;
    private final int consignmentQuantity;

    public FetchConsignmentListByRequestIdWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.requestId = getInputData().getLong(Constants.KEY_REQUEST_ID, 0L);
        this.consignmentQuantity = getInputData().getInt(Constants.KEY_CONSIGNMENT_QUANTITY, 0);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        if (requestId <= 0) {
            Log.e(TAG, "fetchCargoList(): requestId <= 0");
            return ListenableWorker.Result.failure();
        }
        final Data.Builder outputDataBuilder = new Data.Builder()
                .putLong(Constants.KEY_REQUEST_ID, requestId)
                .putInt(Constants.KEY_CONSIGNMENT_QUANTITY, consignmentQuantity);

        if (consignmentQuantity <= 0) {
            Log.e(TAG, "fetchCargoList(): consignmentQuantity <= 0");
            return ListenableWorker.Result.success(outputDataBuilder.build());
        }

        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(
                    SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_LOGIN),
                    SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_PASSWORD));
            final Response<List<Consignment>> response = RetrofitClient.getInstance(getApplicationContext()).getCargoListByRequestId(requestId);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    final long[] rowIds = LocalCache.getInstance(getApplicationContext()).consignmentDao().insertConsignmentList(response.body());
                    if (rowIds.length > 0) {
                        return Result.success();
                    }
                    return Result.failure();
                }
            }
            else {
                Log.e(TAG, "fetchCargoList(): " + response.errorBody());
            }
            return Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "fetchCargoList(): ", e);
            return Result.failure();
        }
    }

    private static final String TAG = FetchConsignmentListByRequestIdWorker.class.toString();
}
