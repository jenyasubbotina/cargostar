package uz.alexits.cargostar.workers.transportation;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import java.io.IOException;
import java.util.List;
import retrofit2.Response;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.transportation.Transportation;
import uz.alexits.cargostar.model.transportation.TransportationData;
import uz.alexits.cargostar.utils.Constants;

public class FetchTransportationDataWorker extends Worker {
    private final Long transportationId;

    public FetchTransportationDataWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.transportationId = getInputData().getLong(Constants.KEY_TRANSPORTATION_ID, -1L);
    }

    @NonNull
    @Override
    public Result doWork() {
        if (transportationId == -1L) {
            Log.e(TAG, "fetchTransportationData(): empty transportation id");
            return Result.failure();
        }
        try {
            RetrofitClient.getInstance(getApplicationContext())
                    .setServerData(SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_LOGIN),
                            SharedPrefs.getInstance(getApplicationContext()).getString(Constants.KEY_PASSWORD));
            final Response<Transportation> response = RetrofitClient.getInstance(getApplicationContext()).getTransportation(transportationId);

            if (response.code() == 200) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "fetchTransportationData(): response=" + response.body());

                    final Transportation transportation = response.body();

                    if (transportation == null) {
                        return Result.failure();
                    }

                    final long rowId = LocalCache.getInstance(getApplicationContext()).transportationDao().insertTransportation(transportation);

                    if (rowId > 0) {
                        final Data outputData = new Data.Builder()
                                .putLong(Constants.KEY_TRANSPORTATION_ID, transportationId)
                                .putLong(Constants.KEY_PROVIDER_ID, transportation.getProviderId() != null ? transportation.getProviderId() : -1L)
                                .putLong(Constants.KEY_COURIER_ID, transportation.getCourierId() != null ? transportation.getCourierId() : -1L)
                                .putLong(Constants.KEY_REQUEST_ID, transportation.getRequestId() != null ? transportation.getRequestId() : -1L)
                                .putLong(Constants.KEY_INVOICE_ID, transportation.getInvoiceId() != null ? transportation.getInvoiceId() : -1L)
                                .putLong(Constants.KEY_BRANCHE_ID, transportation.getBrancheId() != null ? transportation.getBrancheId() : -1L)
                                .putLong(Constants.KEY_PARTIAL_ID, transportation.getPartialId() != null ? transportation.getPartialId() : -1L)
                                .putLong(Constants.KEY_CURRENT_TRANSIT_POINT_ID, transportation.getCurrentTransitionPointId() != null ? transportation.getCurrentTransitionPointId() : -1L)
                                .putLong(Constants.KEY_TRANSPORTATION_STATUS_ID, transportation.getTransportationStatusId() != null ? transportation.getTransportationStatusId() : -1L)
                                .putString(Constants.KEY_CURRENT_STATUS_NAME, transportation.getTransportationStatusName())
                                .putLong(Constants.KEY_PAYMENT_STATUS_ID, transportation.getPaymentStatusId() != null ? transportation.getPaymentStatusId() : -1L)
                                .putString(Constants.KEY_CITY_FROM, transportation.getCityFrom())
                                .putString(Constants.KEY_CITY_TO, transportation.getCityTo())
                                .putString(Constants.KEY_ARRIVAL_DATE, transportation.getArrivalDate())
                                .putString(Constants.KEY_DIRECTION, transportation.getDirection())
                                .putString(Constants.KEY_TRACKING_CODE, transportation.getTrackingCode())
                                .putString(Constants.KEY_QR_CODE, transportation.getQrCode())
                                .putString(Constants.KEY_PARTY_QR_CODE, transportation.getPartyQrCode())
                                .putString(Constants.KEY_INSTRUCTIONS, transportation.getInstructions())
                                .build();

                        return Result.success(outputData);
                    }
                    return Result.failure();
                }
            }
            else {
                Log.e(TAG, "fetchTransportationData(): " + response.errorBody());
            }
            return Result.failure();
        }
        catch (IOException e) {
            Log.e(TAG, "fetchTransportationData(): ", e);
            return Result.failure();
        }
    }

    private static final String TAG = FetchTransportationDataWorker.class.toString();
}
