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

        if (transportation == null) {
            Log.e(TAG, "searchTransportation(): transportationQr is NULL " + transportationQr);
            return Result.failure();
        }

        final Data.Builder outputData = new Data.Builder();

        outputData.putLong(Constants.KEY_TRANSPORTATION_ID, transportation.getId());
        outputData.putLong(Constants.KEY_INVOICE_ID, transportation.getInvoiceId());
        outputData.putString(Constants.KEY_CITY_FROM, transportation.getCityFrom());
        outputData.putString(Constants.KEY_CITY_TO, transportation.getCityTo());
        outputData.putLong(Constants.KEY_COURIER_ID, transportation.getCourierId());
        outputData.putLong(Constants.KEY_PROVIDER_ID, transportation.getProviderId());
        outputData.putString(Constants.KEY_DIRECTION, transportation.getDirection());
        outputData.putLong(Constants.KEY_TRANSPORTATION_STATUS_ID, transportation.getTransportationStatusId());
        outputData.putString(Constants.KEY_TRANSPORTATION_STATUS, transportation.getTransportationStatusName());
        outputData.putLong(Constants.KEY_CURRENT_TRANSIT_POINT_ID, transportation.getCurrentTransitionPointId());
        outputData.putString(Constants.KEY_INSTRUCTIONS, transportation.getInstructions());
        outputData.putString(Constants.KEY_ARRIVAL_DATE, transportation.getArrivalDate());
        outputData.putString(Constants.KEY_TRACKING_CODE, transportation.getTrackingCode());
        outputData.putString(Constants.KEY_QR_CODE, transportation.getQrCode());
        outputData.putString(Constants.KEY_PARTY_QR_CODE, transportation.getPartyQrCode());
        outputData.putLong(Constants.KEY_PAYMENT_STATUS_ID, transportation.getPaymentStatusId());

        return Result.success(outputData.build());
    }

    private static final String TAG = SearchTransportationWorker.class.toString();
}
