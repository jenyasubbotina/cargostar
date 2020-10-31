package uz.alexits.cargostar.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import uz.alexits.cargostar.database.cache.Repository;
import uz.alexits.cargostar.model.calculation.Packaging;
import uz.alexits.cargostar.model.calculation.PackagingType;
import uz.alexits.cargostar.model.calculation.Provider;
import java.util.List;

public class CalculatorViewModel extends AndroidViewModel {
    private final Repository repository;
    private final LiveData<List<Provider>> providerList;
    private final LiveData<List<Packaging>> packagingList;
    private final LiveData<List<PackagingType>> packagingTypeList;

//    private final MutableLiveData<List<Long>> selectedPackagingList;
    private final MutableLiveData<Long> selectedProviderId;
    private final MutableLiveData<Integer> selectedType;

    public CalculatorViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);
        this.providerList = repository.selectAllProviders();
        this.packagingList = repository.selectAllPackaging();
        this.packagingTypeList = repository.selectAllPackagingTypes();

//        this.selectedPackagingList = new MutableLiveData<>();
        this.selectedProviderId = new MutableLiveData<>();
        this.selectedType = new MutableLiveData<>();
        this.selectedProviderId.setValue(0L);
        this.selectedType.setValue(0);
    }

    public LiveData<List<Provider>> getProviderList() {
        return providerList;
    }

    public LiveData<List<Packaging>> getPackagingList() {
        return packagingList;
    }

    public LiveData<List<PackagingType>> getPackagingTypeList() {
        return packagingTypeList;
    }

    public LiveData<List<Packaging>> getPackagingsByProviderId(final long providerId) {
        return repository.selectPackagingsByProviderId(providerId);
    }

    public LiveData<List<Long>> getPackagingIdsByProviderId(final long providerId) {
        return repository.selectPackagingIdsByProviderId(providerId);
    }

    public LiveData<List<PackagingType>> getPackagingTypesByTypeAndPackagingIds(final long type, final long[] packagingIds) {
        return repository.selectPackagingTypesByTypeAndPackagingIds(type, packagingIds);
    }

    public LiveData<List<PackagingType>> getPackagingTypesByProviderId(final long providerId, final long type) {
        return repository.selectPackagingTypesByProviderId(providerId, type);
    }

    public LiveData<List<PackagingType>> getPackagingTypesByProviderId() {
        return repository.selectPackagingTypesByProviderId(getSelectedProviderId().getValue(), getSelectedType().getValue());
    }

    /* getters & setters */

    public void setSelectedProviderId(final long selectedProviderId) {
        this.selectedProviderId.setValue(selectedProviderId);
    }

    public void setSelectedType(final int selectedType) {
        this.selectedType.setValue(selectedType);
    }

    public LiveData<Long> getSelectedProviderId() {
        return selectedProviderId;
    }

    public LiveData<Integer> getSelectedType() {
        return selectedType;
    }

    private static final String TAG = CalculatorViewModel.class.toString();
}

