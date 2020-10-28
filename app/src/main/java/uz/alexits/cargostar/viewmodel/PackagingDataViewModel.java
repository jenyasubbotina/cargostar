package uz.alexits.cargostar.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import uz.alexits.cargostar.database.cache.Repository;
import uz.alexits.cargostar.model.packaging.Packaging;
import uz.alexits.cargostar.model.packaging.PackagingType;
import uz.alexits.cargostar.model.packaging.Provider;
import java.util.List;

public class PackagingDataViewModel extends AndroidViewModel {
    private final Repository repository;
    private final LiveData<List<Provider>> providerList;
    private final LiveData<List<Packaging>> packagingList;
    private final LiveData<List<PackagingType>> packagingTypeList;

    public PackagingDataViewModel(@NonNull Application application) {
        super(application);
        this.repository = Repository.getInstance(application);
        this.providerList = repository.selectAllProviders();
        this.packagingList = repository.selectAllPackaging();
        this.packagingTypeList = repository.selectAllPackagingTypes();
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
}

