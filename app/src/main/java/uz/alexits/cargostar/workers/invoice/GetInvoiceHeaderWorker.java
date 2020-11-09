package uz.alexits.cargostar.workers.invoice;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.model.shipping.Invoice;
import uz.alexits.cargostar.model.shipping.Request;
import uz.alexits.cargostar.utils.Constants;

public class GetInvoiceHeaderWorker extends Worker {
    private final Long invoiceId;

    public GetInvoiceHeaderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.invoiceId = getInputData().getLong(Constants.KEY_INVOICE_ID, -1L);
    }

    @NonNull
    @Override
    public Result doWork() {
        if (invoiceId == -1L) {
            Log.e(TAG, "selectInvoiceById(): invoiceId is empty");
            return Result.failure();
        }
        final Request request = LocalCache.getInstance(getApplicationContext()).invoiceDao().selectRequestByInvoiceId(invoiceId).getValue();

        if (request == null) {
            Log.e(TAG, "selectInvoiceById(): invoice is NULL ??? " + invoiceId + " " + request);
            return Result.failure();
        }

        final Data.Builder outputData = new Data.Builder();
        outputData.putLong(Constants.KEY_INVOICE_ID, request.getId());
//        outputData.putString(Constants.KEY_NUMBER, request.getNumber());
//        outputData.putLong(Constants.KEY_SENDER_ID, request.getSenderId());
//        outputData.putLong(Constants.KEY_RECIPIENT_ID, request.getRecipientId());
//        outputData.putLong(Constants.KEY_PAYER_ID, request.getPayerId());
//        outputData.putLong(Constants.KEY_REQUEST_ID, request.getRequestId());
        outputData.putLong(Constants.KEY_PROVIDER_ID, request.getProviderId());
//        outputData.putDouble(Constants.KEY_PRICE, request.getPrice());
//        outputData.putLong(Constants.KEY_TARIFF_ID, request.getTariffId());
        outputData.putInt(Constants.KEY_STATUS, request.getStatus());
        outputData.putLong(Constants.KEY_CREATED_AT, request.getCreatedAt() != null ? request.getCreatedAt().getTime() : -1L);
        outputData.putLong(Constants.KEY_UPDATED_AT, request.getUpdatedAt() != null ? request.getUpdatedAt().getTime() : -1L);

        return Result.success(outputData.build());
    }

    private static final String TAG = GetInvoiceHeaderWorker.class.toString();
}
