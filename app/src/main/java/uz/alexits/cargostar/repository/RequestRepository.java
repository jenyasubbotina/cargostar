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
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.database.dao.RequestDao;
import uz.alexits.cargostar.entities.transportation.Request;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.invoice.FetchConsignmentListByRequestIdWorker;
import uz.alexits.cargostar.workers.invoice.SearchRequestWorker;
import uz.alexits.cargostar.workers.requests.BindRequestWorker;
import uz.alexits.cargostar.workers.requests.FetchRequestDataWorker;
import uz.alexits.cargostar.workers.requests.FetchRequestsWorker;
import uz.alexits.cargostar.workers.requests.GetLastRequestId;
import uz.alexits.cargostar.workers.requests.GetRequestDataWorker;
import uz.alexits.cargostar.workers.requests.ReadRequestWorker;

public class RequestRepository {
    private final Context context;
    private final RequestDao requestDao;

    public RequestRepository(final Context context) {
        this.context = context;
        this.requestDao = LocalCache.getInstance(context).requestDao();
    }

    public LiveData<Request> selectRequest(final long requestId) {
        return requestDao.selectRequestById(requestId);
    }

    public LiveData<List<Request>> selectAllRequests() {
        return requestDao.selectAllRequests();
    }

    public LiveData<List<Request>> selectPublicRequests() {
        return requestDao.selectPublicRequests();
    }

    public LiveData<List<Request>> selectRequestsByCourierId() {
        return requestDao.selectRequestsByCourierId(SharedPrefs.getInstance(context).getLong(SharedPrefs.ID));
    }

    public void readRequest(final long requestId) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_REQUEST_ID, requestId)
                .build();
        final OneTimeWorkRequest readRequestRequest = new OneTimeWorkRequest.Builder(ReadRequestWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build();
        WorkManager.getInstance(context).enqueue(readRequestRequest);
    }

    public UUID bindRequest(final long requestId) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_REQUEST_ID, requestId)
                .putLong(Constants.KEY_COURIER_ID, SharedPrefs.getInstance(context).getLong(SharedPrefs.ID))
                .build();
        final OneTimeWorkRequest bindRequest = new OneTimeWorkRequest.Builder(BindRequestWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).enqueue(bindRequest);
        return bindRequest.getId();
    }

    public UUID searchRequest(final long requestId) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_REQUEST_ID, requestId)
                .build();
        final OneTimeWorkRequest getRequestRequest = new OneTimeWorkRequest.Builder(SearchRequestWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build();
        WorkManager.getInstance(context).enqueue(getRequestRequest);
        return getRequestRequest.getId();
    }

    public UUID fetchRequestData(final long requestId, final int consignmentQuantity) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_REQUEST_ID, requestId)
                .putInt(Constants.KEY_CONSIGNMENT_QUANTITY, consignmentQuantity)
                .build();

        final OneTimeWorkRequest fetchRequestDataRequest = new OneTimeWorkRequest.Builder(FetchRequestDataWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchConsignmentListByRequestId = new OneTimeWorkRequest.Builder(FetchConsignmentListByRequestIdWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).beginWith(fetchRequestDataRequest).then(fetchConsignmentListByRequestId).enqueue();
        return fetchConsignmentListByRequestId.getId();
    }

    public UUID fetchRequestList() {
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
        final OneTimeWorkRequest getLastIdRequest = new OneTimeWorkRequest.Builder(GetLastRequestId.class)
                .setConstraints(dbConstraints)
                .build();
        final OneTimeWorkRequest fetchRequestsRequest = new OneTimeWorkRequest.Builder(FetchRequestsWorker.class)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context)
                .beginWith(getLastIdRequest)
                .then(fetchRequestsRequest)
                .enqueue();
        return fetchRequestsRequest.getId();
    }

    public UUID fetchRequestDataFromCache(final long requestId) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_REQUEST_ID, requestId)
                .build();
        final OneTimeWorkRequest getRequestRequest = new OneTimeWorkRequest.Builder(GetRequestDataWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build();
        WorkManager.getInstance(context).enqueue(getRequestRequest);
        return getRequestRequest.getId();
    }
}
