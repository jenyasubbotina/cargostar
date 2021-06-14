package uz.alexits.cargostar.viewmodel;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import uz.alexits.cargostar.entities.transportation.Import;
import uz.alexits.cargostar.repository.ImportRepository;
import uz.alexits.cargostar.repository.InvoiceRepository;

public class ImportViewModel extends HeaderViewModel {
    private final ImportRepository importRepository;

    private MutableLiveData<UUID> getRequestFromCacheUUID;
    private final MutableLiveData<UUID> fetchImportListUUID;

    private final MutableLiveData<Pair<Long, Long>> datePair;

    public ImportViewModel(final Context context) {
        super(context);

        this.importRepository = new ImportRepository(context);
        this.fetchImportListUUID = new MutableLiveData<>();
        this.getRequestFromCacheUUID = new MutableLiveData<>();
        this.datePair = new MutableLiveData<>();
        this.datePair.setValue(new Pair<>(0L, 0L));
    }

    public LiveData<List<Import>> getImportList() {
        return Transformations.switchMap(datePair, input -> {
           if (input.first <= 0 && input.second <= 0) {
               Log.i(TAG, "getImportList(): both are 0");
               return importRepository.selectImportList();
           }
           if (input.second <= 0) {
               Log.i(TAG, "getImportList(): endDate is 0, startDate=" + input.first);
               return importRepository.selectImportListByStartDate(input.first);
           }
           if (input.first <= 0) {
               Log.i(TAG, "getImportList(): startDate is 0, endDate=" + input.second);
               return importRepository.selectImportListByEndDate(input.second);
           }
            Log.i(TAG, "getImportList(): trigger both dates, startDate=" + input.first + " endDate=" + input.second);
           return importRepository.selectImportListByBothDates(input.first, input.second);
        });
    }

    public void fetchImportList() {
        fetchImportListUUID.setValue(importRepository.fetchImportList());
    }

    public LiveData<WorkInfo> getFetchImportListResult(final Context context) {
        return Transformations.switchMap(fetchImportListUUID, input -> WorkManager.getInstance(context).getWorkInfoByIdLiveData(input));
    }

    public void removeGetRequestDataUUID() {
        this.getRequestFromCacheUUID = new MutableLiveData<>();
    }

    public void getRequestDataFromCache(final long invoiceId) {
        getRequestFromCacheUUID.setValue(requestRepository.selectRequestByInvoiceId(invoiceId));
    }

    public LiveData<WorkInfo> getRequestDataFromCacheResult(final Context context) {
        return Transformations.switchMap(getRequestFromCacheUUID, input -> WorkManager.getInstance(context).getWorkInfoByIdLiveData(input));
    }

    public void setDates(final String startDateStr, final String endDateStr) throws ParseException {
        Date startDate = null;
        Date endDate = null;

        if (!TextUtils.isEmpty(startDateStr)) {
            startDate = dateFormat.parse(startDateStr);
        }
        if (!TextUtils.isEmpty(endDateStr)) {
            endDate = dateFormat.parse(endDateStr);
        }
        if (startDate == null && endDate == null) {
            Log.e(TAG, "setDates(): both dates are incorrect");
            return;
        }
        if (startDate == null) {
            this.datePair.setValue(new Pair<>(0L, endDate.getTime()/1000));
            return;
        }
        if (endDate == null) {
            this.datePair.setValue(new Pair<>(startDate.getTime()/1000, 0L));
            return;
        }
        if (startDate.getTime() == endDate.getTime()) {
            startDate = dateFormatWithTime.parse(startDateStr + ":00-00-00");
            endDate = dateFormatWithTime.parse(endDateStr + ":23-59-59");
        }
        if (startDate != null && endDate != null) {
            this.datePair.setValue(new Pair<>(startDate.getTime()/1000, endDate.getTime()/1000));
        }
    }

    private static final String TAG = ImportViewModel.class.toString();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final SimpleDateFormat dateFormatWithTime = new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss", Locale.US);
}
