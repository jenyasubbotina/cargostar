package uz.alexits.cargostar.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import java.util.List;

import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.dao.ZoneDao;
import uz.alexits.cargostar.entities.calculation.ZoneSettings;

public class ZoneRepository {
    private final ZoneDao zoneDao;

    public ZoneRepository(final Context context) {
        this.zoneDao = LocalCache.getInstance(context).zoneDao();
    }

    public LiveData<List<ZoneSettings>> selectFedexZoneListByCountryId(final long countryId) {
        return zoneDao.selectZoneSettingsListByCountryIdAndProviderId(countryId, 4L);
    }

    public LiveData<List<ZoneSettings>> selectTntZoneListByCountryId(final long countryId) {
        return zoneDao.selectZoneSettingsListByCountryIdAndProviderId(countryId, 5L);
    }

    public LiveData<List<ZoneSettings>> selectCargostarZoneListByCountryId(final long countryId) {
        return zoneDao.selectZoneSettingsListByCountryIdAndProviderId(countryId, 6L);
    }
}
