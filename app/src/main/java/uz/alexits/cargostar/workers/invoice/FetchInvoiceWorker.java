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
import uz.alexits.cargostar.entities.transportation.Invoice;
import uz.alexits.cargostar.utils.Constants;

public class FetchInvoiceWorker extends Worker {
    private final long requestId;
    private final long invoiceId;
    private final int consignmentQuantity;

    public FetchInvoiceWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);

        this.requestId = getInputData().getLong(Constants.KEY_REQUEST_ID, 0L);
        this.invoiceId = getInputData().getLong(Constants.KEY_INVOICE_ID, 0);
        this.consignmentQuantity = getInputData().getInt(Constants.KEY_CONSIGNMENT_QUANTITY, 0);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        if (invoiceId <= 0) {
            Log.e(TAG, "fetchInvoice(): invoiceId <= 0");
            return Result.failure();
        }
        try {
            RetrofitClient.getInstance(getApplicationContext())
                    .setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_LOGIN),
                            SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_PASSWORD));
            final Response<Invoice> response = RetrofitClient.getInstance(getApplicationContext()).getInvoice(invoiceId);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchInvoice(): response=" + response.body());
                    final Invoice invoice = response.body();

                    if (invoice == null) {
                        Log.e(TAG, "fetchInvoice(): invoice is NULL");
                        return Result.failure();
                    }
                    final long rowId = LocalCache.getInstance(getApplicationContext()).invoiceDao().insertInvoice(invoice);

                    if (rowId > 0) {
                        return Result.success(new Data.Builder()
                                .putLong(Constants.KEY_REQUEST_ID, requestId)
                                .putLong(Constants.KEY_SENDER_ID, invoice.getSenderId())
                                .putLong(Constants.KEY_RECIPIENT_ID, invoice.getRecipientId())
                                .putLong(Constants.KEY_PAYER_ID, invoice.getPayerId())
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

    private static final String TAG = FetchInvoiceWorker.class.toString();
}
