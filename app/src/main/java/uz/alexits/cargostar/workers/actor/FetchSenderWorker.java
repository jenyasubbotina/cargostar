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
import uz.alexits.cargostar.model.actor.Customer;
import uz.alexits.cargostar.utils.Constants;

public class FetchSenderWorker extends Worker {
    private final long senderId;
    private final long requestId;
    private final long courierId;
    private final long providerId;
    private final long invoiceId;
    private final long userId;
    private final long recipientCountryId;
    private final long recipientCityId;
    private final String recipientCity;
    private final int deliveryType;
    private final String paymentStatus;
    private final int consignmentQuantity;
    private final String comment;

    public FetchSenderWorker(@NonNull final Context context, @NonNull final WorkerParameters workerParams) {
        super(context, workerParams);
        this.senderId = getInputData().getLong(Constants.KEY_SENDER_ID, -1);
        this.requestId = getInputData().getLong(Constants.KEY_REQUEST_ID, -1L);
        this.courierId = getInputData().getLong(Constants.KEY_COURIER_ID, -1L);
        this.providerId = getInputData().getLong(Constants.KEY_PROVIDER_ID,-1L);
        this.invoiceId = getInputData().getLong(Constants.KEY_INVOICE_ID, -1L);
        this.userId = getInputData().getLong(Constants.KEY_USER_ID, -1L);
        this.recipientCountryId = getInputData().getLong(Constants.KEY_RECIPIENT_COUNTRY_ID, -1L);
        this.recipientCityId = getInputData().getLong(Constants.KEY_RECIPIENT_CITY_ID, -1L);
        this.recipientCity = getInputData().getString(Constants.KEY_RECIPIENT_CITY);
        this.deliveryType = getInputData().getInt(Constants.KEY_DELIVERY_TYPE, 0);
        this.paymentStatus = getInputData().getString(Constants.KEY_PAYMENT_STATUS);
        this.consignmentQuantity = getInputData().getInt(Constants.KEY_CONSIGNMENT_QUANTITY, 0);
        this.comment = getInputData().getString(Constants.KEY_COMMENT);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        if (senderId <= 0) {
            Log.e(TAG, "fetchSenderData(): senderId <= 0");
            return Result.failure();
        }
        try {
            RetrofitClient.getInstance(getApplicationContext())
                    .setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_LOGIN),
                            SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_PASSWORD));
            final Response<Customer> response = RetrofitClient.getInstance(getApplicationContext()).getClient(senderId);

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

                    final Data outputData = new Data.Builder()
                            .putLong(Constants.KEY_REQUEST_ID, requestId)
                            .putLong(Constants.KEY_COURIER_ID, courierId)
                            .putLong(Constants.KEY_PROVIDER_ID, providerId)
                            .putLong(Constants.KEY_INVOICE_ID, invoiceId)
                            .putLong(Constants.KEY_USER_ID, userId)
                            .putLong(Constants.KEY_RECIPIENT_COUNTRY_ID, recipientCountryId)
                            .putLong(Constants.KEY_RECIPIENT_CITY_ID, recipientCityId)
                            .putString(Constants.KEY_RECIPIENT_CITY, recipientCity)
                            .putInt(Constants.KEY_DELIVERY_TYPE, deliveryType)
                            .putString(Constants.KEY_PAYMENT_STATUS, paymentStatus)
                            .putInt(Constants.KEY_CONSIGNMENT_QUANTITY, consignmentQuantity)
                            .putString(Constants.KEY_COMMENT, comment)
                            .putLong(Constants.KEY_SENDER_ID, sender.getId())
                            .putString(Constants.KEY_SENDER_EMAIL, sender.getEmail())
                            .putString(Constants.KEY_SENDER_SIGNATURE, sender.getSignatureUrl())
                            .putString(Constants.KEY_SENDER_FIRST_NAME, sender.getFirstName())
                            .putString(Constants.KEY_SENDER_LAST_NAME, sender.getLastName())
                            .putString(Constants.KEY_SENDER_MIDDLE_NAME, sender.getMiddleName())
                            .putString(Constants.KEY_SENDER_PHONE, sender.getPhone())
                            .putString(Constants.KEY_SENDER_ADDRESS, sender.getAddress())
                            .putLong(Constants.KEY_SENDER_COUNTRY_ID, sender.getCountryId() != null ? sender.getCountryId() : -1L)
                            .putLong(Constants.KEY_SENDER_REGION_ID, sender.getRegionId() != null ? sender.getRegionId() : -1L)
                            .putLong(Constants.KEY_SENDER_CITY_ID, sender.getCityId() != null ? sender.getCityId() : -1L)
                            .putString(Constants.KEY_SENDER_ZIP, sender.getZip())
                            .putString(Constants.KEY_SENDER_COMPANY_NAME, sender.getCompany())
                            .putString(Constants.KEY_SENDER_CARGOSTAR, sender.getCargostarAccountNumber())
                            .putString(Constants.KEY_SENDER_TNT, sender.getTntAccountNumber())
                            .putString(Constants.KEY_SENDER_FEDEX, sender.getFedexAccountNumber())
                            .putInt(Constants.KEY_DISCOUNT, sender.getDiscount())
                            .build();
                    return Result.success(outputData);
                }
            }
            else {
                Log.e(TAG, "fetchSenderData(): " + response.errorBody());
            }
            return ListenableWorker.Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "fetchSenderData(): ", e);
            return ListenableWorker.Result.failure();
        }
    }

    private static final String TAG = FetchSenderWorker.class.toString();
}
