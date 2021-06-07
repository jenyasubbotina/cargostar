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
import uz.alexits.cargostar.entities.actor.AddressBook;
import uz.alexits.cargostar.utils.Constants;

public class FetchPayerWorker extends Worker {
    private final long requestId;
    private final long payerId;
    private final int consignmentQuantity;

    public FetchPayerWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);

        this.requestId = getInputData().getLong(Constants.KEY_REQUEST_ID, 0L);
        this.payerId = getInputData().getLong(Constants.KEY_PAYER_ID, 0L);
        this.consignmentQuantity = getInputData().getInt(Constants.KEY_CONSIGNMENT_QUANTITY, 0);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        if (requestId <= 0) {
            Log.e(TAG, "fetchPayerDataWorker(): requestId <= 0");
            return Result.failure();
        }
        if (payerId <= 0) {
            Log.e(TAG, "fetchPayerDataWorker(): payerId <= 0");
            return Result.failure();
        }
        try {
            RetrofitClient.getInstance(getApplicationContext())
                    .setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_LOGIN),
                            SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_PASSWORD));
            final Response<AddressBook> response = RetrofitClient.getInstance(getApplicationContext()).getAddressBookEntry(payerId);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchPayerDataWorker(): response=" + response.body());
                    final AddressBook payer = response.body();

                    if (payer == null) {
                        Log.e(TAG, "fetchPayerDataWorker(): payer is NULL");
                        return Result.failure();
                    }

                    final long rowInserted = LocalCache.getInstance(getApplicationContext()).addressBookDao().insertAddressBookEntry(payer);

                    if (rowInserted > 0) {
                        return Result.success(new Data.Builder()
                                .putLong(Constants.KEY_REQUEST_ID, requestId)
                                .putInt(Constants.KEY_CONSIGNMENT_QUANTITY, consignmentQuantity)
                                .build());
                    }
                    return Result.failure();
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

    private static final String TAG = FetchPayerWorker.class.toString();
}
