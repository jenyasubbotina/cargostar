package uz.alexits.cargostar.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.dao.PackagingDao;
import uz.alexits.cargostar.entities.calculation.Packaging;
import uz.alexits.cargostar.entities.calculation.PackagingType;
import uz.alexits.cargostar.workers.calculation.FetchPackagingTypesWorker;
import uz.alexits.cargostar.workers.calculation.FetchPackagingWorker;
import uz.alexits.cargostar.workers.calculation.FetchProvidersWorker;
import uz.alexits.cargostar.workers.calculation.FetchVatWorker;
import uz.alexits.cargostar.workers.calculation.FetchZoneCountriesWorker;
import uz.alexits.cargostar.workers.calculation.FetchZoneSettingsWorker;
import uz.alexits.cargostar.workers.calculation.FetchZonesWorker;

public class PackagingRepository {
    private final Context context;
    private final PackagingDao packagingDao;

    public PackagingRepository(final Context context) {
        this.context = context;
        this.packagingDao = LocalCache.getInstance(context).packagingDao();
    }

    public LiveData<List<Packaging>> selectPackagingListByProviderId(final long providerId) {
        return packagingDao.selectPackagingListByProviderId(providerId);
    }

    public LiveData<Packaging> selectPackagingById(final long packagingId) {
        return packagingDao.selectPackagingById(packagingId);
    }

    public LiveData<List<PackagingType>> selectDocPackagingTypesByProviderId(final long providerId) {
        return packagingDao.selectDocPackagingTypesByPackagingIds(providerId);
    }

    public LiveData<List<PackagingType>> selectBoxPackagingTypesByProviderId(final long providerId) {
        return packagingDao.selectBoxPackagingTypesByPackagingIds(providerId);
    }

    /* Calculation Data */
    public UUID fetchPackagingData() {
        final Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresCharging(false)
                .setRequiresStorageNotLow(false)
                .setRequiresDeviceIdle(false)
                .build();
        final OneTimeWorkRequest fetchProviderListRequest = new OneTimeWorkRequest.Builder(FetchProvidersWorker.class)
                .setConstraints(constraints)
                .build();
        final OneTimeWorkRequest fetchVatRequest = new OneTimeWorkRequest.Builder(FetchVatWorker.class)
                .setConstraints(constraints)
                .build();
        final OneTimeWorkRequest fetchPackagingRequest = new OneTimeWorkRequest.Builder(FetchPackagingWorker.class)
                .setConstraints(constraints)
                .build();
        final OneTimeWorkRequest fetchZonesRequest = new OneTimeWorkRequest.Builder(FetchZonesWorker.class)
                .setConstraints(constraints)
                .build();
        final OneTimeWorkRequest fetchPackagingTypesRequest = new OneTimeWorkRequest.Builder(FetchPackagingTypesWorker.class)
                .setConstraints(constraints)
                .build();
        final OneTimeWorkRequest fetchZoneCountriesRequest = new OneTimeWorkRequest.Builder(FetchZoneCountriesWorker.class)
                .setConstraints(constraints)
                .build();
        final OneTimeWorkRequest fetchZoneSettingsRequest = new OneTimeWorkRequest.Builder(FetchZoneSettingsWorker.class)
                .setConstraints(constraints)
                .build();

        final List<OneTimeWorkRequest> firstChainList = new ArrayList<>();
        firstChainList.add(fetchProviderListRequest);
        firstChainList.add(fetchVatRequest);

        final List<OneTimeWorkRequest> secondChainList = new ArrayList<>();
        secondChainList.add(fetchPackagingRequest);
        secondChainList.add(fetchZonesRequest);

        final List<OneTimeWorkRequest> thirdChainList = new ArrayList<>();
        thirdChainList.add(fetchPackagingTypesRequest);
        thirdChainList.add(fetchZoneCountriesRequest);

        WorkManager.getInstance(context)
                .beginWith(firstChainList)
                .then(secondChainList)
                .then(thirdChainList)
                .then(fetchZoneSettingsRequest)
                .enqueue();
        return fetchZoneSettingsRequest.getId();
    }
}
