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
import uz.alexits.cargostar.model.shipping.Invoice;
import uz.alexits.cargostar.utils.Constants;

public class FetchInvoiceWorker extends Worker {
    private final long invoiceId;

    public FetchInvoiceWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);
        this.invoiceId = getInputData().getLong(Constants.KEY_INVOICE_ID, -1);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        if (invoiceId < 0) {
            Log.e(TAG, "fetchInvoice(): invoiceId < 0");
            return Result.failure();
        }

        final Data.Builder outputDataBuilder = new Data.Builder();

        try {
            RetrofitClient.getInstance(getApplicationContext())
                    .setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.LOGIN),
                            SharedPrefs.getInstance(getApplicationContext()).getString(SharedPrefs.PASSWORD_HASH));
            final Response<Invoice> response = RetrofitClient.getInstance(getApplicationContext()).getInvoice(invoiceId);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchInvoice(): response=" + response.body());
                    final Invoice invoice = response.body();

                    if (invoice == null) {
                        Log.e(TAG, "fetchInvoice(): invoice is NULL");
                        return Result.failure();
                    }
                    Log.i(TAG, "fetchInvoice(): successfully received invoice " + invoice);

                    outputDataBuilder.putLong(Constants.KEY_INVOICE_ID, invoice.getId());
                    outputDataBuilder.putString(Constants.KEY_NUMBER, invoice.getNumber());
                    outputDataBuilder.putLong(Constants.KEY_PROVIDER_ID, invoice.getProviderId() != null ? invoice.getProviderId() : -1L);
                    outputDataBuilder.putLong(Constants.KEY_REQUEST_ID, invoice.getRequestId() != null ? invoice.getRequestId() : -1L);
                    outputDataBuilder.putLong(Constants.KEY_TARIFF_ID, invoice.getTariffId() != null ? invoice.getTariffId() : -1L);

                    outputDataBuilder.putLong(Constants.KEY_SENDER_ID, invoice.getSenderId() != null ? invoice.getSenderId() : -1L);
                    outputDataBuilder.putLong(Constants.KEY_RECIPIENT_ID, invoice.getRecipientId() != null ? invoice.getRecipientId() : -1L);
                    outputDataBuilder.putLong(Constants.KEY_PAYER_ID, invoice.getPayerId() != null ? invoice.getPayerId() : -1L);

                    outputDataBuilder.putDouble(Constants.KEY_PRICE, invoice.getPrice());

                    outputDataBuilder.putLong(Constants.KEY_STATUS, invoice.getStatus());
                    outputDataBuilder.putLong(Constants.KEY_CREATED_AT, invoice.getCreatedAt() != null ? invoice.getCreatedAt().getTime() : -1L);
                    outputDataBuilder.putLong(Constants.KEY_UPDATED_AT, invoice.getUpdatedAt() != null ? invoice.getUpdatedAt().getTime() : -1L);

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

    private static final String TAG = FetchInvoiceWorker.class.toString();
}
