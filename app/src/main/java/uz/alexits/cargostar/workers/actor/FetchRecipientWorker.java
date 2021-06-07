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

public class FetchRecipientWorker extends Worker {
    private final long requestId;
    private final long recipientId;
    private final long payerId;
    private final int consignmentQuantity;

    public FetchRecipientWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);

        this.requestId = getInputData().getLong(Constants.KEY_REQUEST_ID, 0L);
        this.recipientId = getInputData().getLong(Constants.KEY_RECIPIENT_ID, 0L);
        this.payerId = getInputData().getLong(Constants.KEY_PAYER_ID, 0L);
        this.consignmentQuantity = getInputData().getInt(Constants.KEY_CONSIGNMENT_QUANTITY, 0);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        if (requestId <= 0) {
            Log.e(TAG, "fetchRecipientData(): requestId <= 0");
            return Result.failure();
        }
        if (recipientId <= 0) {
            Log.e(TAG, "fetchRecipientData(): recipientId <= 0");
            return Result.failure();
        }

        try {
            RetrofitClient.getInstance(getApplicationContext())
                    .setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_LOGIN),
                            SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_PASSWORD));
            final Response<AddressBook> response = RetrofitClient.getInstance(getApplicationContext()).getAddressBookEntry(recipientId);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchRecipientDataWorker(): response=" + response.body());
                    final AddressBook recipient = response.body();

                    if (recipient == null) {
                        Log.e(TAG, "fetchRecipientDataWorker(): recipient is NULL");
                        return Result.failure();
                    }

                    final long rowInserted = LocalCache.getInstance(getApplicationContext()).addressBookDao().insertAddressBookEntry(recipient);

                    if (rowInserted > 0) {
                        return Result.success(new Data.Builder()
                                .putLong(Constants.KEY_REQUEST_ID, requestId)
                                .putLong(Constants.KEY_PAYER_ID, payerId)
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

    private static final String TAG = FetchRecipientWorker.class.toString();
}
