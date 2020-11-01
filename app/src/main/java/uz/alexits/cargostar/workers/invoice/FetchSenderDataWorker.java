package uz.alexits.cargostar.workers.invoice;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.io.IOException;
import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.actor.Customer;
import uz.alexits.cargostar.utils.Constants;

public class FetchSenderDataWorker extends Worker {
    private final long senderId;

    public FetchSenderDataWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);
        this.senderId = getInputData().getLong(Constants.KEY_SENDER_ID, -1);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        if (senderId <= 0) {
            Log.e(TAG, "fetchRecipientDataWorker(): invoiceId <= 0");
            return Result.failure();
        }
        try {
            RetrofitClient.getInstance(getApplicationContext())
                    .setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.LOGIN),
                            SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.PASSWORD_HASH));
            final Response<Customer> response = RetrofitClient.getInstance(getApplicationContext()).getCustomer(senderId);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchSenderData(): response=" + response.body());
                    final Customer sender = response.body();

                    if (sender == null) {
                        Log.e(TAG, "fetchSenderData(): sender is NULL");
                        return Result.failure();
                    }

                    final long rowInserted = LocalCache.getInstance(getApplicationContext()).actorDao().createCustomer(sender);

                    if (rowInserted == -1) {
                        Log.e(TAG, "fetchSenderData(): couldn't insert entry " + sender);
                        return Result.failure();
                    }
                    Log.i(TAG, "fetchSenderData(): successfully inserted entry " + sender);
                    return Result.success();
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

    private static final String TAG = FetchSenderDataWorker.class.toString();
}
