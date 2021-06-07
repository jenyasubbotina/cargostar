package uz.alexits.cargostar.workers.actor;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.io.IOException;
import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.entities.actor.Client;
import uz.alexits.cargostar.utils.Constants;

public class FetchSenderWorker extends Worker {
    private final long requestId;
    private final long invoiceId;
    private final long senderId;
    private final int consignmentQuantity;

    public FetchSenderWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);

        this.senderId = getInputData().getLong(Constants.KEY_SENDER_ID, 0);
        this.requestId = getInputData().getLong(Constants.KEY_REQUEST_ID, 0L);
        this.invoiceId = getInputData().getLong(Constants.KEY_INVOICE_ID, 0L);
        this.consignmentQuantity = getInputData().getInt(Constants.KEY_CONSIGNMENT_QUANTITY, 0);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        if (senderId <= 0) {
            Log.e(TAG, "fetchSenderData(): senderId <= 0");
            return Result.failure();
        }
        try {
            RetrofitClient.getInstance(getApplicationContext())
                    .setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_LOGIN),
                            SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_PASSWORD));
            final Response<Client> response = RetrofitClient.getInstance(getApplicationContext()).getClient(senderId);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchSenderData(): response=" + response.body());
                    final Client sender = response.body();

                    if (sender == null) {
                        Log.e(TAG, "fetchSenderData(): sender is NULL");
                        return Result.failure();
                    }

                    final long rowInserted = LocalCache.getInstance(getApplicationContext()).clientDao().createClient(sender);

                    if (rowInserted > 0) {
                        return Result.success(new Data.Builder()
                                .putLong(Constants.KEY_REQUEST_ID, requestId)
                                .putLong(Constants.KEY_INVOICE_ID, invoiceId)
                                .putInt(Constants.KEY_CONSIGNMENT_QUANTITY, consignmentQuantity)
                                .build());
                    }
                    return Result.failure();
                }
            }
            else {
                Log.e(TAG, "fetchSenderData(): " + response.errorBody());
            }
            return ListenableWorker.Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "fetchSenderData(): ", e);
            return ListenableWorker.Result.failure();
        }
    }

    private static final String TAG = FetchSenderWorker.class.toString();
}
