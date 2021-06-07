package uz.alexits.cargostar.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.dao.VatDao;
import uz.alexits.cargostar.entities.calculation.Vat;

public class VatRepository {
    private final VatDao vatDao;

    public VatRepository(final Context context) {
        this.vatDao = LocalCache.getInstance(context).vatDao();
    }

    public LiveData<Vat> selectVat() {
        return vatDao.selectVat();
    }

}
