package uz.alexits.cargostar.workers.requests;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.transportation.Request;
import uz.alexits.cargostar.utils.Constants;

public class FetchRequestDataWorker extends Worker {
    private final long requestId;

    public FetchRequestDataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.requestId = getInputData().getLong(Constants.KEY_REQUEST_ID, -1);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        if (requestId <= 0) {
            Log.e(TAG, "fetchRequestData(): request <= 0");
            return Result.failure();
        }
        try {
            RetrofitClient.getInstance(getApplicationContext())
                    .setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_LOGIN),
                            SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_PASSWORD));
            final Response<Request> response = RetrofitClient.getInstance(getApplicationContext()).getRequest(requestId);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchRequestData(): response=" + response.body());
                    final Request request = response.body();

                    if (request == null) {
                        Log.e(TAG, "fetchRequestData(): sender is NULL");
                        return Result.failure();
                    }
                    request.setNew(false);
                    final long rowInserted = LocalCache.getInstance(getApplicationContext()).requestDao().insertRequest(request);

                    if (rowInserted == -1) {
                        Log.e(TAG, "fetchRequestData(): couldn't insert entry " + request);
                        return Result.failure();
                    }
                    Log.i(TAG, "fetchRequestData(): successfully inserted entry " + request);

                    final Data outputData = new Data.Builder()
                            .putLong(Constants.KEY_REQUEST_ID, request.getId())
                            .putLong(Constants.KEY_SENDER_ID, request.getClientId() != null ? request.getClientId() : -1L)
                            .putLong(Constants.KEY_COURIER_ID, request.getCourierId() != null ? request.getCourierId() : -1L)
                            .putLong(Constants.KEY_PROVIDER_ID, request.getProviderId() != null ? request.getProviderId() : -1L)
                            .putLong(Constants.KEY_INVOICE_ID, request.getInvoiceId() != null ? request.getInvoiceId() : -1L)
                            .putLong(Constants.KEY_USER_ID, request.getUserId() != null ? request.getUserId() : -1L)
                            .putLong(Constants.KEY_SENDER_COUNTRY_ID, request.getSenderCountryId() != null ? request.getSenderCountryId() : -1L)
                            .putLong(Constants.KEY_SENDER_REGION_ID, request.getSenderRegionId() != null ? request.getSenderRegionId() : -1L)
                            .putLong(Constants.KEY_SENDER_CITY_ID, request.getSenderCityId() != null ? request.getSenderCityId() : -1L)
                            .putLong(Constants.KEY_RECIPIENT_COUNTRY_ID, request.getRecipientCountryId() != null ? request.getRecipientCountryId() : -1L)
                            .putLong(Constants.KEY_RECIPIENT_CITY_ID, request.getRecipientCityId() != null ? request.getRecipientCityId() : -1L)
                            .putString(Constants.KEY_SENDER_EMAIL, request.getSenderEmail())
                            .putString(Constants.KEY_SENDER_FIRST_NAME, request.getSenderFirstName())
                            .putString(Constants.KEY_SENDER_LAST_NAME, request.getSenderLastName())
                            .putString(Constants.KEY_SENDER_MIDDLE_NAME, request.getSenderMiddleName())
                            .putString(Constants.KEY_SENDER_PHONE, request.getSenderPhone())
                            .putString(Constants.KEY_SENDER_CITY, request.getSenderCity())
                            .putString(Constants.KEY_SENDER_ADDRESS, request.getSenderAddress())
                            .putString(Constants.KEY_RECIPIENT_CITY, request.getRecipientCity())
                            .putInt(Constants.KEY_DELIVERY_TYPE, request.getDeliveryType())
                            .putString(Constants.KEY_PAYMENT_STATUS, request.getPaymentStatus())
                            .putInt(Constants.KEY_CONSIGNMENT_QUANTITY, request.getConsignmentQuantity())
                            .putString(Constants.KEY_COMMENT, request.getComment())
                            .build();

                    Log.i(TAG, "fetchRequestDataWorker(): count=" + request.getConsignmentQuantity());

                    return Result.success(outputData);
                }
            }
            else {
                Log.e(TAG, "fetchRequestData(): " + response.errorBody());
            }
            return ListenableWorker.Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "fetchRequestData(): ", e);
            return ListenableWorker.Result.failure();
        }
        catch (JsonSyntaxException ex) {
            Log.e(TAG, "fetchRequestData(): request " + requestId + " was deleted from server");
            LocalCache.getInstance(getApplicationContext()).requestDao().deleteRequest(requestId);
            return Result.failure();
        }
    }

    private static final String TAG = FetchRequestDataWorker.class.toString();
}
