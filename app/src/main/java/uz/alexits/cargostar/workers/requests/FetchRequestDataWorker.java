package uz.alexits.cargostar.workers.requests;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.entities.transportation.Request;
import uz.alexits.cargostar.utils.Constants;

public class FetchRequestDataWorker extends Worker {
    private final long requestId;

    public FetchRequestDataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.requestId = getInputData().getLong(Constants.KEY_REQUEST_ID, 0);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        if (requestId <= 0) {
            Log.e(TAG, "fetchRequestData(): request <= 0");
            return Result.failure();
        }
        try {
            RetrofitClient.getInstance(getApplicationContext())
                    .setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_LOGIN),
                            SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_PASSWORD));
            final Response<Request> response = RetrofitClient.getInstance(getApplicationContext()).getRequest(requestId);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchRequestData(): response=" + response.body());
                    final Request request = response.body();

                    if (request == null) {
                        Log.e(TAG, "fetchRequestData(): sender is NULL");
                        return Result.failure();
                    }
                    request.setNew(false);
                    final long rowInserted = LocalCache.getInstance(getApplicationContext()).requestDao().insertRequest(request);

                    if (rowInserted > 0) {
                        return Result.success(new Data.Builder()
                                .putLong(Constants.KEY_REQUEST_ID, request.getId())
                                .putLong(Constants.KEY_SENDER_ID, request.getClientId())
                                .putLong(Constants.KEY_INVOICE_ID, request.getInvoiceId())
                                .putInt(Constants.KEY_CONSIGNMENT_QUANTITY, request.getConsignmentQuantity())
                                .build());

                    }
                    return Result.failure();
                }
            }
            else {
                Log.e(TAG, "fetchRequestData(): " + response.errorBody());
            }
            return ListenableWorker.Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "fetchRequestData(): ", e);
            return ListenableWorker.Result.failure();
        }
        catch (JsonSyntaxException ex) {
            Log.e(TAG, "fetchRequestData(): request " + requestId + " was deleted from server");
            LocalCache.getInstance(getApplicationContext()).requestDao().deleteRequest(requestId);
            return Result.failure();
        }
    }

    private static final String TAG = FetchRequestDataWorker.class.toString();
}
