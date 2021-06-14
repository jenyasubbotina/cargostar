package uz.alexits.cargostar.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.OverwritingInputMerger;
import androidx.work.WorkManager;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.dao.LocationDao;
import uz.alexits.cargostar.database.dao.TransportationDao;
import uz.alexits.cargostar.database.dao.TransportationStatusDao;
import uz.alexits.cargostar.entities.location.TransitPoint;
import uz.alexits.cargostar.entities.transportation.Transportation;
import uz.alexits.cargostar.entities.transportation.TransportationStatus;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.location.FetchTransitPointsWorker;
import uz.alexits.cargostar.workers.requests.BindRequestWorker;
import uz.alexits.cargostar.workers.transportation.FetchTransportationStatusesWorker;
import uz.alexits.cargostar.workers.transportation.FetchTransportationsWorker;
import uz.alexits.cargostar.workers.transportation.SearchTransportationWorker;

public class TransportationRepository {
    private final Context context;
    private final TransportationDao transportationDao;
    private final TransportationStatusDao transportationStatusDao;
    private final LocationDao locationDao;

    public TransportationRepository(final Context context) {
        this.context = context;
        this.transportationDao = LocalCache.getInstance(context).transportationDao();
        this.locationDao = LocalCache.getInstance(context).locationDao();
        this.transportationStatusDao = LocalCache.getInstance(context).transportationStatusDao();
    }

    public LiveData<List<TransitPoint>> selectTransitPoints() {
        return locationDao.selectAllTransitPoints();
    }

    public LiveData<List<TransportationStatus>> selectTransportationStatuses() {
        return transportationStatusDao.selectAllTransportationStatuses();
    }

    public LiveData<Transportation> selectTransportationById(final long transportationId) {
        return transportationDao.selectTransportationById(transportationId);
    }

    public LiveData<List<Transportation>> selectTransportationListByTransitPointAndStatusId(final long transitPointId, final long statusId) {
        return transportationDao.selectTransportationListByTransitPointAndStatusId(transitPointId, statusId);
    }

    public LiveData<Transportation> selectTransportationByInvoiceId(final long invoiceId) {
        return transportationDao.selectTransportationByInvoiceId(invoiceId);
    }

    public LiveData<List<Transportation>> selectTransportationsByPartialId(final long partialId) {
        return transportationDao.selectTransportationsByPartialId(partialId);
    }

    public UUID fetchTransportationList() {
        final Constraints dbConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final OneTimeWorkRequest fetchTransportationsRequest = new OneTimeWorkRequest.Builder(FetchTransportationsWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).enqueue(fetchTransportationsRequest);
        return fetchTransportationsRequest.getId();
    }

    public UUID fetchTransitPoints() {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final OneTimeWorkRequest fetchTransportationStatusListRequest = new OneTimeWorkRequest.Builder(FetchTransitPointsWorker.class)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).enqueue(fetchTransportationStatusListRequest);
        return fetchTransportationStatusListRequest.getId();
    }

    public UUID fetchTransportationStatuses() {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final OneTimeWorkRequest fetchTransportationStatusListRequest = new OneTimeWorkRequest.Builder(FetchTransportationStatusesWorker.class)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).enqueue(fetchTransportationStatusListRequest);
        return fetchTransportationStatusListRequest.getId();
    }

    public UUID searchTransportationByQrAndBindRequest(final String qr) {
        final Constraints onlineConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final Constraints offlineConstraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final Data inputData = new Data.Builder()
                .putString(Constants.KEY_TRANSPORTATION_QR, qr)
                .build();
        final OneTimeWorkRequest searchTransportationRequest = new OneTimeWorkRequest.Builder(SearchTransportationWorker.class)
                .setConstraints(offlineConstraints)
                .setInputData(inputData)
                .build();
        final OneTimeWorkRequest bindRequest = new OneTimeWorkRequest.Builder(BindRequestWorker.class)
                .setConstraints(onlineConstraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).beginWith(searchTransportationRequest).then(bindRequest).enqueue();
        return bindRequest.getId();
    }

//    public UUID fetchTransportationByTrackingCode(final String trackingCode) {
//        final Constraints constraints = new Constraints.Builder()
//                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
//                .setRequiresCharging(false)
//                .setRequiresStorageNotLow(false)
//                .setRequiresDeviceIdle(false)
//                .build();
//        final Data inputData = new Data.Builder()
//                .putString(Constants.KEY_TRACKING_CODE, trackingCode)
//                .build();
//        final OneTimeWorkRequest fetchTransportationRequest = new OneTimeWorkRequest.Builder(FetchTransportationByTrackingCodeWorker.class)
//                .setConstraints(constraints)
//                .setInputData(inputData)
//                .build();
//        WorkManager.getInstance(context).enqueue(fetchTransportationRequest);
//        return fetchTransportationRequest.getId();
//    }
}
