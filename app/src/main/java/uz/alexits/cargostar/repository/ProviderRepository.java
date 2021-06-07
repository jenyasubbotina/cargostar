package uz.alexits.cargostar.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.dao.ProviderDao;
import uz.alexits.cargostar.entities.calculation.Provider;

public class ProviderRepository {
    private final ProviderDao providerDao;

    public ProviderRepository(final Context context) {
        this.providerDao = LocalCache.getInstance(context).providerDao();
    }

    public LiveData<List<Provider>> selectAllProviders() {
        return providerDao.selectAllProviders();
    }

    public LiveData<Provider> selectProviderById(final long providerId) {
        return providerDao.selectProviderById(providerId);
    }
}
