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
import uz.alexits.cargostar.workers.requests.GetRequestDataWorker;

public class GetInvoiceDataWorker  extends Worker {
    private final long invoiceId;

    public GetInvoiceDataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.invoiceId = getInputData().getLong(Constants.KEY_INVOICE_ID, 0);
    }

    @NonNull
    @Override
    public Result doWork() {
        final Request request = LocalCache.getInstance(getApplicationContext()).requestDao().getRequestByInvoiceId(invoiceId);

        if (request == null) {
            Log.e(TAG, "getRequest() returned null");
            return Result.failure();
        }
        return Result.success(new Data.Builder()
                .putLong(Constants.KEY_REQUEST_ID, request.getId())
                .putLong(Constants.KEY_INVOICE_ID, request.getInvoiceId())
                .putLong(Constants.KEY_CLIENT_ID, request.getClientId())
                .putLong(Constants.KEY_COURIER_ID, request.getCourierId())
                .putLong(Constants.KEY_SENDER_USER_ID, request.getUserId())
                .putLong(Constants.KEY_PROVIDER_ID, request.getProviderId())
                .putLong(Constants.KEY_SENDER_COUNTRY_ID, request.getSenderCountryId())
                .putLong(Constants.KEY_RECIPIENT_COUNTRY_ID, request.getRecipientCountryId())
                .putString(Constants.KEY_SENDER_FIRST_NAME, request.getSenderFirstName())
                .putString(Constants.KEY_SENDER_MIDDLE_NAME, request.getSenderMiddleName())
                .putString(Constants.KEY_SENDER_LAST_NAME, request.getSenderLastName())
                .putString(Constants.KEY_SENDER_EMAIL, request.getSenderEmail())
                .putString(Constants.KEY_SENDER_PHONE, request.getSenderPhone())
                .putString(Constants.KEY_SENDER_ADDRESS, request.getSenderAddress())
                .putString(Constants.KEY_SENDER_CITY_NAME, request.getSenderCityName())
                .putString(Constants.KEY_RECIPIENT_CITY_NAME, request.getRecipientCityName())
                .putString(Constants.KEY_COMMENT, request.getComment())
                .putInt(Constants.KEY_CONSIGNMENT_QUANTITY, request.getConsignmentQuantity())
                .putString(Constants.KEY_PAYMENT_STATUS, request.getPaymentStatus())
                .putInt(Constants.KEY_DELIVERY_TYPE, request.getDeliveryType())
                .build());
    }

    private static final String TAG = GetInvoiceDataWorker.class.toString();
}
