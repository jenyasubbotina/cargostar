package uz.alexits.cargostar.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.List;
import java.util.UUID;

import uz.alexits.cargostar.entities.transportation.Import;
import uz.alexits.cargostar.repository.ImportRepository;
import uz.alexits.cargostar.repository.InvoiceRepository;

public class ImportViewModel extends HeaderViewModel {
    private final ImportRepository importRepository;

    private final MutableLiveData<UUID> fetchImportListUUID;

    public ImportViewModel(final Context context) {
        super(context);

        this.importRepository = new ImportRepository(context);
        this.fetchImportListUUID = new MutableLiveData<>();
    }

    public LiveData<List<Import>> getImportList() {
        return importRepository.selectImportList();
    }

    public void fetchImportList() {
        fetchImportListUUID.setValue(importRepository.fetchImportList());
    }

    public LiveData<WorkInfo> getFetchImportListResult(final Context context) {
        return Transformations.switchMap(fetchImportListUUID, input -> WorkManager.getInstance(context).getWorkInfoByIdLiveData(input));
    }
}
