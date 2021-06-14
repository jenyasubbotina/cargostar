package uz.alexits.cargostar.workers.requests;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.IOException;

import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.entities.params.BindRequestParams;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.entities.transportation.Request;
import uz.alexits.cargostar.entities.transportation.Transportation;
import uz.alexits.cargostar.utils.Constants;

public class BindRequestWorker extends Worker {
    private final long requestId;
    private final BindRequestParams params;
    private final Transportation transportation;

    public BindRequestWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.requestId = getInputData().getLong(Constants.KEY_REQUEST_ID, 0L);
        this.params = new BindRequestParams(SharedPrefs.getInstance(context).getLong(SharedPrefs.ID));
        transportation = new Transportation(
                getInputData().getLong(Constants.KEY_TRANSPORTATION_ID, 0),
                getInputData().getLong(Constants.KEY_PROVIDER_ID, 0),
                getInputData().getLong(Constants.KEY_COURIER_ID, 0),
                getInputData().getLong(Constants.KEY_INVOICE_ID, 0),
                getInputData().getLong(Constants.KEY_REQUEST_ID, 0),
                getInputData().getLong(Constants.KEY_BRANCHE_ID, 0),
                getInputData().getLong(Constants.KEY_TRANSPORTATION_STATUS_ID, 0),
                getInputData().getLong(Constants.KEY_PAYMENT_STATUS_ID, 0),
                getInputData().getLong(Constants.KEY_CURRENT_TRANSIT_POINT_ID, 0),
                getInputData().getLong(Constants.KEY_PARTIAL_ID, 0),
                getInputData().getString(Constants.KEY_ARRIVAL_DATE),
                getInputData().getString(Constants.KEY_TRACKING_CODE),
                getInputData().getString(Constants.KEY_QR_CODE),
                getInputData().getString(Constants.KEY_PARTY_QR_CODE),
                getInputData().getString(Constants.KEY_INSTRUCTIONS),
                getInputData().getString(Constants.KEY_DIRECTION),
                getInputData().getString(Constants.KEY_CITY_FROM),
                getInputData().getString(Constants.KEY_CITY_TO),
                getInputData().getString(Constants.KEY_TRANSPORTATION_STATUS),
                getInputData().getInt(Constants.TRANSPORTATION_TYPE, 0),
                getInputData().getString(Constants.IMPORT_STATUS)
        );
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            RetrofitClient.getInstance(getApplicationContext()).setServerData(
                    SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_LOGIN),
                    SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_PASSWORD));

            final Response<Request> response = RetrofitClient.getInstance(getApplicationContext()).bindRequest(requestId, params);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    final Request request = response.body();

                    if (request == null) {
                        Log.e(TAG, "bindRequest(): courier is NULL");
                        return Result.failure();
                    }

                    Log.i(TAG, "bindRequest(): " + request);

                    final int rowUpdated = LocalCache.getInstance(getApplicationContext()).requestDao().updateRequest(request);

                    if (rowUpdated == -1) {
                        Log.e(TAG, "bindRequest(): couldn't insert courier " + request);
                        return Result.failure();
                    }
                    return Result.success(
                            new Data.Builder()
                                    .putLong(Constants.KEY_REQUEST_ID, request.getId())
                                    .putLong(Constants.KEY_INVOICE_ID, transportation.getInvoiceId())
                                    .putLong(Constants.KEY_TRANSPORTATION_ID, transportation.getId())
                                    .putLong(Constants.KEY_COURIER_ID, transportation.getCourierId())
                                    .putLong(Constants.KEY_PROVIDER_ID, transportation.getProviderId())
                                    .putLong(Constants.KEY_BRANCHE_ID, transportation.getBrancheId())
                                    .putLong(Constants.KEY_TRANSPORTATION_STATUS_ID, transportation.getTransportationStatusId())
                                    .putLong(Constants.KEY_PAYMENT_STATUS_ID, transportation.getPaymentStatusId())
                                    .putLong(Constants.KEY_CURRENT_TRANSIT_POINT_ID, transportation.getCurrentTransitionPointId())
                                    .putLong(Constants.KEY_PARTIAL_ID, transportation.getPartialId())
                                    .putString(Constants.KEY_TRACKING_CODE, transportation.getTrackingCode())
                                    .putString(Constants.KEY_TRANSPORTATION_QR, transportation.getQrCode())
                                    .putString(Constants.KEY_PARTY_QR_CODE, transportation.getPartyQrCode())
                                    .putString(Constants.KEY_TRANSPORTATION_STATUS, transportation.getTransportationStatusName())
                                    .putString(Constants.KEY_CITY_FROM, transportation.getCityFrom())
                                    .putString(Constants.KEY_CITY_TO, transportation.getCityTo())
                                    .putString(Constants.KEY_INSTRUCTIONS, transportation.getInstructions())
                                    .putString(Constants.KEY_ARRIVAL_DATE, transportation.getArrivalDate())
                                    .putString(Constants.KEY_DIRECTION, transportation.getDirection())
                                    .putInt(Constants.TRANSPORTATION_TYPE, transportation.getTransportationType())
                                    .putString(Constants.IMPORT_STATUS, transportation.getImportStatus())
                                    .build());
                }
            }
        }
        catch (IOException e) {
            Log.e(TAG, "bindRequest(): ", e);
            return Result.failure();
        }
        Log.e(TAG, "bindRequest(): unknown error");
        return Result.failure();
    }

    private static final String TAG = BindRequestWorker.class.toString();
}
