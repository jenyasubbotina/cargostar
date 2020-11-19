package uz.alexits.cargostar.workers.transportation;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.model.shipping.Request;
import uz.alexits.cargostar.model.transportation.Transportation;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.invoice.GetInvoiceHeaderWorker;

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

        final Data.Builder outputDataBuilder = new Data.Builder()
                .putString(Constants.KEY_TRANSPORTATION_QR, transportationQr);

        if (transportation == null) {
            Log.e(TAG, "searchTransportation(): transportationQr is NULL " + transportationQr);
            return Result.failure(outputDataBuilder.build());
        }

        outputDataBuilder.putLong(Constants.KEY_TRANSPORTATION_ID, transportation.getId());
        outputDataBuilder.putLong(Constants.KEY_INVOICE_ID, transportation.getInvoiceId() != null ? transportation.getInvoiceId() : 0);
        outputDataBuilder.putLong(Constants.KEY_REQUEST_ID, transportation.getRequestId() != null ? transportation.getRequestId() : 0);
        outputDataBuilder.putLong(Constants.KEY_COURIER_ID, transportation.getCourierId() != null ? transportation.getCourierId() : 0);
        outputDataBuilder.putLong(Constants.KEY_PROVIDER_ID, transportation.getProviderId() != null ? transportation.getProviderId() : 0);
        outputDataBuilder.putLong(Constants.KEY_TRANSPORTATION_STATUS_ID, transportation.getTransportationStatusId() != null ? transportation.getTransportationStatusId() : 0);
        outputDataBuilder.putLong(Constants.KEY_CURRENT_TRANSIT_POINT_ID, transportation.getCurrentTransitionPointId() != null ? transportation.getCurrentTransitionPointId() : 0);
        outputDataBuilder.putLong(Constants.KEY_PAYMENT_STATUS_ID, transportation.getPaymentStatusId() != null ? transportation.getPaymentStatusId() : 0);

        outputDataBuilder.putString(Constants.KEY_TRACKING_CODE, transportation.getTrackingCode());
        outputDataBuilder.putString(Constants.KEY_CITY_FROM, transportation.getCityFrom());
        outputDataBuilder.putString(Constants.KEY_CITY_TO, transportation.getCityTo());
        outputDataBuilder.putString(Constants.KEY_PARTY_QR_CODE, transportation.getPartyQrCode());
        outputDataBuilder.putString(Constants.KEY_INSTRUCTIONS, transportation.getInstructions());
        outputDataBuilder.putString(Constants.KEY_DIRECTION, transportation.getDirection());
        outputDataBuilder.putString(Constants.KEY_ARRIVAL_DATE, transportation.getArrivalDate());
        outputDataBuilder.putString(Constants.KEY_TRANSPORTATION_STATUS, transportation.getTransportationStatusName());

        return Result.success(outputDataBuilder.build());
    }

    private static final String TAG = SearchTransportationWorker.class.toString();
}
