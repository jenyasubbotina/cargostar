package uz.alexits.cargostar.workers.invoice;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.entities.transportation.Request;
import uz.alexits.cargostar.utils.Constants;

public class SearchRequestWorker extends Worker {
    private final long requestId;

    public SearchRequestWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.requestId = getInputData().getLong(Constants.KEY_REQUEST_ID, 0L);
    }

    @NonNull
    @Override
    public Result doWork() {
        if (requestId <= 0L) {
            Log.e(TAG, "searchRequestById(): requestId is empty");
            return Result.failure();
        }
        final Request request = LocalCache.getInstance(getApplicationContext()).requestDao().searchRequestById(requestId);

        if (request == null) {
            Log.e(TAG, "searchRequestById(): request is NULL " + requestId);
            return Result.failure();
        }

        final Data.Builder outputData = new Data.Builder();
        outputData.putLong(Constants.KEY_REQUEST_ID, request.getId());
        outputData.putLong(Constants.KEY_INVOICE_ID, request.getInvoiceId());
        outputData.putLong(Constants.KEY_COURIER_ID, request.getCourierId());
        outputData.putLong(Constants.KEY_CLIENT_ID, request.getClientId());
        outputData.putLong(Constants.KEY_SENDER_COUNTRY_ID, request.getSenderCountryId());
        outputData.putLong(Constants.KEY_RECIPIENT_COUNTRY_ID, request.getRecipientCountryId());
        outputData.putLong(Constants.KEY_PROVIDER_ID, request.getProviderId());
        outputData.putInt(Constants.KEY_DELIVERY_TYPE, request.getDeliveryType());
        outputData.putString(Constants.KEY_COMMENT, request.getComment());
        outputData.putInt(Constants.KEY_CONSIGNMENT_QUANTITY, request.getConsignmentQuantity());

        return Result.success(outputData.build());
    }

    private static final String TAG = SearchRequestWorker.class.toString();
}
