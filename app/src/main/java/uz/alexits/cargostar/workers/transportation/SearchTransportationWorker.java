package uz.alexits.cargostar.workers.transportation;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.entities.transportation.Transportation;
import uz.alexits.cargostar.utils.Constants;

public class SearchTransportationWorker extends Worker {
    private final String transportationQr;

    public SearchTransportationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.transportationQr = getInputData().getString(Constants.KEY_TRANSPORTATION_QR);
    }

    @NonNull
    @Override
    public Result doWork() {
        if (transportationQr == null) {
            Log.e(TAG, "searchTransportation(): transportationQr is empty");
            return Result.failure();
        }
        final Transportation transportation = LocalCache.getInstance(getApplicationContext()).transportationDao().selectTransportationByQr(transportationQr);

        final Data.Builder outputDataBuilder = new Data.Builder();

        if (transportation == null) {
            Log.e(TAG, "searchTransportation(): transportationQr is NULL " + transportationQr);
            return Result.failure(outputDataBuilder
                    .putString(Constants.KEY_TRANSPORTATION_QR, transportationQr)
                    .build());
        }
        return Result.success(outputDataBuilder
                .putLong(Constants.KEY_TRANSPORTATION_ID, transportation.getId())
                .putLong(Constants.KEY_INVOICE_ID, transportation.getInvoiceId())
                .putLong(Constants.KEY_REQUEST_ID, transportation.getRequestId())
                .putLong(Constants.KEY_COURIER_ID, transportation.getCourierId())
                .putLong(Constants.KEY_BRANCHE_ID, transportation.getBrancheId())
                .putLong(Constants.KEY_PROVIDER_ID, transportation.getProviderId())
                .putLong(Constants.KEY_TRANSPORTATION_STATUS_ID, transportation.getTransportationStatusId())
                .putLong(Constants.KEY_CURRENT_TRANSIT_POINT_ID, transportation.getCurrentTransitionPointId())
                .putLong(Constants.KEY_PAYMENT_STATUS_ID, transportation.getPaymentStatusId())
                .putLong(Constants.KEY_PARTIAL_ID, transportation.getPartialId())
                .putString(Constants.KEY_TRANSPORTATION_QR, transportation.getQrCode())
                .putString(Constants.KEY_TRACKING_CODE, transportation.getTrackingCode())
                .putString(Constants.KEY_PARTY_QR_CODE, transportation.getPartyQrCode())
                .putString(Constants.KEY_CITY_FROM, transportation.getCityFrom())
                .putString(Constants.KEY_CITY_TO, transportation.getCityTo())
                .putString(Constants.KEY_INSTRUCTIONS, transportation.getInstructions())
                .putString(Constants.KEY_DIRECTION, transportation.getDirection())
                .putString(Constants.KEY_ARRIVAL_DATE, transportation.getArrivalDate())
                .putString(Constants.KEY_TRANSPORTATION_STATUS, transportation.getTransportationStatusName())
                .putInt(Constants.TRANSPORTATION_TYPE, transportation.getTransportationType())
                .putString(Constants.IMPORT_STATUS, transportation.getImportStatus())
                .build());
    }

    private static final String TAG = SearchTransportationWorker.class.toString();
}
