package uz.alexits.cargostar.workers.requests;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.entities.transportation.Request;
import uz.alexits.cargostar.utils.Constants;

public class InsertRequestWorker extends Worker {
    private final long requestId;
    private final long invoiceId;
    private final long senderCountryId;
    private final long senderRegionId;
    private final long senderCityId;
    private final long userId;
    private final long senderId;
    private final long courierId;
    private final long providerId;
    private final String senderFirstName;
    private final String senderMiddleName;
    private final String senderLastName;
    private final String senderEmail;
    private final String senderPhone;
    private final String senderAddress;
    private final long recipientCountryId;
    private final long recipientCityId;
    private final String cityFrom;
    private final String cityTo;
    private final int consigmentQuantity;
    private final String paymentStatus;
    private final String comment;
    private final int deliveryType;

    private final String senderCityName;
    private final String recipientCityName;

    public InsertRequestWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.requestId = getInputData().getLong(Constants.KEY_REQUEST_ID, 0L);
        this.invoiceId = getInputData().getLong(Constants.KEY_INVOICE_ID, 0L);
        this.senderCountryId = getInputData().getLong(Constants.KEY_SENDER_COUNTRY_ID, 0L);
        this.senderRegionId = getInputData().getLong(Constants.KEY_SENDER_REGION_ID, 0L);
        this.senderCityId = getInputData().getLong(Constants.KEY_SENDER_CITY_ID, 0L);
        this.userId = getInputData().getLong(Constants.KEY_USER_ID, 0L);
        this.senderId = getInputData().getLong(Constants.KEY_SENDER_ID, 0L);
        this.courierId = getInputData().getLong(Constants.KEY_COURIER_ID, 0L);
        this.providerId = getInputData().getLong(Constants.KEY_PROVIDER_ID, 0L);
        this.senderFirstName = getInputData().getString(Constants.KEY_SENDER_FIRST_NAME);
        this.senderMiddleName = getInputData().getString(Constants.KEY_SENDER_MIDDLE_NAME);
        this.senderLastName = getInputData().getString(Constants.KEY_SENDER_LAST_NAME);
        this.senderEmail = getInputData().getString(Constants.KEY_EMAIL);
        this.senderPhone = getInputData().getString(Constants.KEY_SENDER_PHONE);
        this.senderAddress = getInputData().getString(Constants.KEY_SENDER_ADDRESS);
        this.recipientCountryId = getInputData().getLong(Constants.KEY_RECIPIENT_COUNTRY_ID, 0L);
        this.recipientCityId = getInputData().getLong(Constants.KEY_RECIPIENT_CITY_ID, 0L);
        this.cityFrom = getInputData().getString(Constants.KEY_CITY_FROM);
        this.cityTo = getInputData().getString(Constants.KEY_CITY_TO);
        this.consigmentQuantity = getInputData().getInt(Constants.KEY_CONSIGNMENT_QUANTITY, 0);
        this.paymentStatus = getInputData().getString(Constants.KEY_PAYMENT_STATUS);
        this.comment = getInputData().getString(Constants.KEY_COMMENT);
         this.deliveryType = getInputData().getInt(Constants.KEY_DELIVERY_TYPE, 0);

        this.senderCityName = getInputData().getString(Constants.KEY_SENDER_CITY_NAME);
        this.recipientCityName = getInputData().getString(Constants.KEY_RECIPIENT_CITY_NAME);
    }

    @NonNull
    @Override
    public Result doWork() {
        if (requestId > 0L) {
            Log.e(TAG, "requestId is NULL");
            return Result.failure();
        }

        try {
            final Request request = new Request(
                    requestId,
                    senderFirstName,
                    senderMiddleName,
                    senderLastName,
                    senderEmail,
                    senderPhone,
                    senderAddress,
                    senderCountryId > 0L ? senderCountryId : null,
                    senderCityName,
                    recipientCountryId > 0L ? recipientCountryId : null,
                    recipientCityName,
                    comment,
                    userId > 0L ? userId : null,
                    senderId > 0L ? senderId : null,
                    courierId > 0L ? courierId : null,
                    providerId > 0L ? providerId : null,
                    invoiceId > 0L ? invoiceId : null,
                    cityFrom,
                    cityTo,
                    consigmentQuantity,
                    paymentStatus,
                    deliveryType);
            final long rowInserted = LocalCache.getInstance(getApplicationContext()).requestDao().insertRequest(request);

            if (rowInserted > 0) {
                Log.i(TAG, "insertRequest(): successfully inserted request " + request);

                return Result.success();
            }
            Log.e(TAG, "insertRequest(): couldn't insert request " + request);
            return Result.failure();

        }
        catch (Exception e) {
            Log.e(TAG, "insertRequest(): ", e);
            return Result.failure();
        }
    }

    private static final String TAG = InsertRequestWorker.class.toString();
}
