package uz.alexits.cargostar.repository;

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
import uz.alexits.cargostar.entities.transportation.Request;
import uz.alexits.cargostar.entities.transportation.Transportation;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.requests.FetchRequestDataWorker;

public class FetchTransportationByTrackingCodeWorker extends Worker {
    private final String trackingCode;

    public FetchTransportationByTrackingCodeWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.trackingCode = getInputData().getString(Constants.KEY_TRACKING_CODE);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {
        try {
            RetrofitClient.getInstance(getApplicationContext())
                    .setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_LOGIN),
                            SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_PASSWORD));
            final Response<Transportation> response = RetrofitClient.getInstance(getApplicationContext()).getTransportation(trackingCode);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchTransportationByTrackingCode(): response=" + response.body());
                    final Transportation transportation = response.body();

                    if (transportation == null) {
                        Log.e(TAG, "fetchTransportationByTrackingCode(): sender is NULL");
                        return Result.failure();
                    }
                    final long rowInserted = LocalCache.getInstance(getApplicationContext()).transportationDao().insertTransportation(transportation);

                    if (rowInserted > 0) {
                        return Result.success(new Data.Builder()
                                .putLong(Constants.KEY_REQUEST_ID, transportation.getId())
                                .putLong(Constants.KEY_INVOICE_ID, transportation.getInvoiceId())
                                .putLong(Constants.KEY_REQUEST_ID, transportation.getRequestId())
                                .putLong(Constants.KEY_PAYMENT_STATUS_ID, transportation.getPaymentStatusId())
                                .putString(Constants.KEY_TRACKING_CODE, transportation.getTrackingCode())
                                .putString(Constants.KEY_QR_CODE, transportation.getQrCode())
                                .putString(Constants.KEY_PARTY_QR_CODE, transportation.getPartyQrCode())
                                .putLong(Constants.KEY_CURRENT_TRANSIT_POINT_ID, transportation.getCurrentTransitionPointId())
                                .putString(Constants.KEY_CITY_FROM, transportation.getCityFrom())
                                .putString(Constants.KEY_CITY_TO, transportation.getCityTo())
                                .putLong(Constants.KEY_COURIER_ID, transportation.getCourierId())
                                .putLong(Constants.KEY_PROVIDER_ID, transportation.getProviderId())
                                .putString(Constants.KEY_INSTRUCTIONS, transportation.getInstructions())
                                .putString(Constants.KEY_ARRIVAL_DATE, transportation.getArrivalDate())
                                .putString(Constants.KEY_DIRECTION, transportation.getDirection())
                                .build());
                    }
                    return Result.failure();
                }
            }
            else {
                Log.e(TAG, "fetchTransportationByTrackingCode(): " + response.errorBody());
            }
            return ListenableWorker.Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "fetchTransportationByTrackingCode(): ", e);
            return ListenableWorker.Result.failure();
        }
        catch (JsonSyntaxException ex) {
            Log.e(TAG, "fetchTransportationByTrackingCode(): request " + trackingCode + " was deleted from server");
            return Result.failure();
        }
    }

    private static final String TAG = FetchTransportationByTrackingCodeWorker.class.toString();
}
