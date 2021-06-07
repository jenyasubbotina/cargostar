package uz.alexits.cargostar.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.dao.ConsignmentDao;
import uz.alexits.cargostar.entities.transportation.Consignment;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.workers.invoice.FetchConsignmentListByInvoiceIdWorker;
import uz.alexits.cargostar.workers.invoice.FetchConsignmentListByRequestIdWorker;

public class ConsignmentRepository {
    private final Context context;
    private final ConsignmentDao consignmentDao;

    public ConsignmentRepository(final Context context) {
        this.context = context;
        this.consignmentDao = LocalCache.getInstance(context).consignmentDao();
    }

    public LiveData<List<Consignment>> selectConsignmentListByRequestId(final long requestId) {
        return consignmentDao.selectConsignmentListByRequestId(requestId);
    }

    public UUID fetchConsignmentListByRequestId(final long requestId) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_REQUEST_ID, requestId)
                .build();
        final OneTimeWorkRequest fetchCargoListRequest = new OneTimeWorkRequest.Builder(FetchConsignmentListByRequestIdWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).enqueue(fetchCargoListRequest);

        return fetchCargoListRequest.getId();
    }

    public UUID fetchConsignmentListByInvoiceId(final long invoiceId) {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final Data inputData = new Data.Builder()
                .putLong(Constants.KEY_INVOICE_ID, invoiceId)
                .build();
        final OneTimeWorkRequest fetchCargoListRequest = new OneTimeWorkRequest.Builder(FetchConsignmentListByInvoiceIdWorker.class)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context).enqueue(fetchCargoListRequest);

        return fetchCargoListRequest.getId();
    }
}
