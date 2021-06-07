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
import uz.alexits.cargostar.database.dao.TransportationStatusDao;
import uz.alexits.cargostar.entities.transportation.Route;
import uz.alexits.cargostar.entities.transportation.TransportationStatus;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.SendRecipientSignatureWorker;
import uz.alexits.cargostar.workers.transportation.FetchTransportationDataWorker;
import uz.alexits.cargostar.workers.transportation.FetchTransportationRouteWorker;
import uz.alexits.cargostar.workers.transportation.UpdateTransportationStatusWorker;

public class TransportationStatusRepository {
    private final Context context;
    private final TransportationStatusDao transportationStatusDao;

    public TransportationStatusRepository(final Context context) {
        this.context = context;
        this.transportationStatusDao = LocalCache.getInstance(context).transportationStatusDao();
    }

    public LiveData<TransportationStatus> selectTransportationStatusById(final long statusId) {
        return transportationStatusDao.selectTransportationStatusById(statusId);
    }

    public LiveData<List<Route>> selectRoute(final long transportationId) {
        return transportationStatusDao.selectRouteByTransportationId(transportationId);
    }

    public UUID fetchTransportationData(final long transportationId) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_TRANSPORTATION_ID, transportationId)
                .build();
        final OneTimeWorkRequest fetchTransportationDataRequest = new OneTimeWorkRequest.Builder(FetchTransportationDataWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).enqueue(fetchTransportationDataRequest);
        return fetchTransportationDataRequest.getId();
    }

    public UUID fetchTransportationRoute(final long transportationId) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_TRANSPORTATION_ID, transportationId)
                .build();
        final OneTimeWorkRequest fetchTransportationRouteRequest = new OneTimeWorkRequest.Builder(FetchTransportationRouteWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).enqueue(fetchTransportationRouteRequest);
        return fetchTransportationRouteRequest.getId();
    }

    public UUID updateTransportationStatus(final long transportationId,
                                           final long transportationStatusId,
                                           final long transitPointId) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_TRANSPORTATION_ID, transportationId)
                .putLong(Constants.KEY_TRANSPORTATION_STATUS_ID, transportationStatusId)
                .putLong(Constants.KEY_TRANSIT_POINT_ID, transitPointId)
                .build();
        final OneTimeWorkRequest updateTransportationStatusRequest = new OneTimeWorkRequest.Builder(UpdateTransportationStatusWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchTransportationRouteRequest = new OneTimeWorkRequest.Builder(FetchTransportationRouteWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context)
                .beginWith(updateTransportationStatusRequest)
                .then(fetchTransportationRouteRequest)
                .enqueue();

        return fetchTransportationRouteRequest.getId();
    }
    public UUID sendRecipientSignatureAndUpdateStatusDelivered(final long invoiceId,
                                                               final String fullName,
                                                               final String phone,
                                                               final String address,
                                                               final String company,
                                                               final String recipientSignature,
                                                               final String recipientSignatureDate,
                                                               final String comment,
                                                               final boolean addresseeResult,
                                                               final long transportationId,
                                                               final long transportationStatusId,
                                                               final long transitPointId) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();

        final Data sendSignatureData = new Data.Builder()
                .putLong(Constants.KEY_INVOICE_ID, invoiceId)
                .putString(Constants.ADDRESSEE_FULL_NAME, fullName)
                .putString(Constants.ADDRESSEE_PHONE, phone)
                .putString(Constants.ADDRESSEE_ADDRESS, address)
                .putString(Constants.ADDRESSEE_ORGANIZATION, company)
                .putString(Constants.ADDRESSEE_COMMENT, comment)
                .putString(Constants.ADDRESSEE_SIGNATURE, recipientSignature)
                .putString(Constants.ADDRESSEE_SIGNATURE_DATE, recipientSignatureDate)
                .putBoolean(Constants.ADDRESSEE_IS_ACCEPTED, addresseeResult)
                .build();
        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_TRANSPORTATION_ID, transportationId)
                .putLong(Constants.KEY_TRANSPORTATION_STATUS_ID, transportationStatusId)
                .putLong(Constants.KEY_TRANSIT_POINT_ID, transitPointId)
                .build();

        final OneTimeWorkRequest sendRecipientSignatureRequest = new OneTimeWorkRequest.Builder(SendRecipientSignatureWorker.class)
                .setConstraints(constraints)
                .setInputData(sendSignatureData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest updateTransportationStatusRequest = new OneTimeWorkRequest.Builder(UpdateTransportationStatusWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchTransportationRouteRequest = new OneTimeWorkRequest.Builder(FetchTransportationRouteWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context)
                .beginWith(sendRecipientSignatureRequest)
                .then(updateTransportationStatusRequest)
                .then(fetchTransportationRouteRequest)
                .enqueue();
        return fetchTransportationRouteRequest.getId();
    }
}
