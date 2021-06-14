package uz.alexits.cargostar.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.OverwritingInputMerger;
import androidx.work.WorkManager;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.database.dao.ImportDao;
import uz.alexits.cargostar.entities.transportation.Import;
import uz.alexits.cargostar.viewmodel.ImportViewModel;
import uz.alexits.cargostar.workers.invoice.FetchInvoiceListWorker;
import uz.alexits.cargostar.workers.transportation.FetchTransportationsWorker;

public class ImportRepository {
    final Context context;
    final ImportDao importDao;

    public ImportRepository(final Context context) {
        this.context = context;
        this.importDao = LocalCache.getInstance(context).importDao();
    }

    public LiveData<List<Import>> selectImportListByStartDate(final long startDate) {
        return importDao.selectImportListByStartDate(SharedPrefs.getInstance(context).getLong(SharedPrefs.ID), startDate);
    }

    public LiveData<List<Import>> selectImportListByEndDate(final long endDate) {
        return importDao.selectImportListByEndDate(SharedPrefs.getInstance(context).getLong(SharedPrefs.ID), endDate);
    }

    public LiveData<List<Import>> selectImportListByBothDates(final long startDate, final long endDate) {
        return importDao.selectImportListByBothDates(SharedPrefs.getInstance(context).getLong(SharedPrefs.ID), startDate, endDate);
    }

    public LiveData<List<Import>> selectImportList() {
        return importDao.selectImportList(SharedPrefs.getInstance(context).getLong(SharedPrefs.ID));
    }

    public UUID fetchImportList() {
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
        final OneTimeWorkRequest fetchInvoicesRequest = new OneTimeWorkRequest.Builder(FetchInvoiceListWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        final OneTimeWorkRequest fetchTransportationsRequest = new OneTimeWorkRequest.Builder(FetchTransportationsWorker.class)
                .setConstraints(constraints)
                .setInputMerger(OverwritingInputMerger.class)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 5*1000L, TimeUnit.MILLISECONDS)
                .build();
        WorkManager.getInstance(context)
                .beginWith(fetchInvoicesRequest)
                .then(fetchTransportationsRequest)
                .enqueue();
        return fetchTransportationsRequest.getId();
    }
}
