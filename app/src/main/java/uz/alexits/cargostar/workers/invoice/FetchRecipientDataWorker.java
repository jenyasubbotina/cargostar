package uz.alexits.cargostar.workers.invoice;

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
import uz.alexits.cargostar.model.actor.AddressBook;
import uz.alexits.cargostar.utils.Constants;

public class FetchRecipientDataWorker extends Worker {
    private final long invoiceId;
    private final String number;
    private final long providerId;
    private final long requestId;
    private final long tariffId;

    private final long senderId;
    private final long recipientId;
    private final long payerId;

    private final double price;
    private final int status;
    private final long createdAtTime;
    private final long updatedAtTime;

    public FetchRecipientDataWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);
        this.invoiceId = getInputData().getLong(Constants.KEY_INVOICE_ID, -1L);
        this.number = getInputData().getString(Constants.KEY_NUMBER);
        this.providerId = getInputData().getLong(Constants.KEY_PROVIDER_ID, -1L);
        this.requestId = getInputData().getLong(Constants.KEY_REQUEST_ID, -1L);
        this.tariffId = getInputData().getLong(Constants.KEY_TARIFF_ID, -1L);

        this.senderId = getInputData().getLong(Constants.KEY_SENDER_ID, -1L);
        this.recipientId = getInputData().getLong(Constants.KEY_RECIPIENT_ID, -1L);
        this.payerId = getInputData().getLong(Constants.KEY_PAYER_ID, -1L);

        this.price = getInputData().getLong(Constants.KEY_PRICE, -1L);
        this.status = getInputData().getInt(Constants.KEY_STATUS, -1);
        this.createdAtTime = getInputData().getLong(Constants.KEY_CREATED_AT, -1L);
        this.updatedAtTime = getInputData().getLong(Constants.KEY_UPDATED_AT, -1L);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        if (invoiceId <= 0) {
            Log.e(TAG, "fetchRecipientData(): invoiceId <= 0");
            return Result.failure();
        }
        if (providerId <= 0) {
            Log.e(TAG, "fetchRecipientData(): providerId <= 0");
            return Result.failure();
        }
        if (requestId <= 0) {
            Log.e(TAG, "fetchRecipientData(): requestId <= 0");
            return Result.failure();
        }
        if (tariffId <= 0) {
            Log.e(TAG, "fetchRecipientData(): tariffId <= 0");
            return Result.failure();
        }
        if (recipientId <= 0) {
            Log.e(TAG, "fetchRecipientData(): recipientId <= 0");
            return Result.failure();
        }

        try {
            RetrofitClient.getInstance(getApplicationContext())
                    .setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.LOGIN),
                            SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.PASSWORD_HASH));
            final Response<AddressBook> response = RetrofitClient.getInstance(getApplicationContext()).getAddressBookEntry(recipientId);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchRecipientDataWorker(): response=" + response.body());
                    final AddressBook recipient = response.body();

                    if (recipient == null) {
                        Log.e(TAG, "fetchRecipientDataWorker(): recipient is NULL");
                        return Result.failure();
                    }

                    final long rowInserted = LocalCache.getInstance(getApplicationContext()).invoiceDao().insertAddressBookEntry(recipient);

                    if (rowInserted == -1) {
                        Log.e(TAG, "fetchRecipientDataWorker(): couldn't insert entry " + recipient);
                        return Result.failure();
                    }

                    final Data.Builder outputDataBuilder = new Data.Builder();

                    outputDataBuilder.putLong(Constants.KEY_INVOICE_ID, invoiceId);
                    outputDataBuilder.putString(Constants.KEY_NUMBER, number);
                    outputDataBuilder.putLong(Constants.KEY_PROVIDER_ID, providerId);
                    outputDataBuilder.putLong(Constants.KEY_REQUEST_ID, requestId);
                    outputDataBuilder.putLong(Constants.KEY_TARIFF_ID, tariffId);

                    outputDataBuilder.putLong(Constants.KEY_SENDER_ID, senderId);
                    outputDataBuilder.putLong(Constants.KEY_RECIPIENT_ID, recipientId);
                    outputDataBuilder.putLong(Constants.KEY_PAYER_ID, payerId);

                    outputDataBuilder.putDouble(Constants.KEY_PRICE, price);

                    outputDataBuilder.putLong(Constants.KEY_STATUS, status);
                    outputDataBuilder.putLong(Constants.KEY_CREATED_AT, createdAtTime);
                    outputDataBuilder.putLong(Constants.KEY_UPDATED_AT, updatedAtTime);

                    Log.i(TAG, "fetchRecipientDataWorker(): successfully inserted entry " + recipient);
                    return Result.success(outputDataBuilder.build());
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

    private static final String TAG = FetchRecipientDataWorker.class.toString();
}
