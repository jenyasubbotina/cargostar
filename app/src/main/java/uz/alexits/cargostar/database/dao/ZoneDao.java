package uz.alexits.cargostar.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import uz.alexits.cargostar.entities.calculation.Zone;
import uz.alexits.cargostar.entities.calculation.ZoneCountry;
import uz.alexits.cargostar.entities.calculation.ZoneSettings;

@Dao
public abstract class ZoneDao {
    /* zones */
    @Query("DELETE FROM zone")
    abstract void dropZones();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long[] insertZones(final List<Zone> zoneList);

    @Transaction
    public long[] insertZonesTransaction(final List<Zone> zoneList) {
        dropZones();
        return insertZones(zoneList);
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertZone(final Zone zone);

    @Query("SELECT * FROM zone ORDER BY id ASC")
    public abstract LiveData<List<Zone>> selectAllZones();

    @Query("SELECT * FROM zone WHERE id == :zoneId ORDER BY id ASC")
    public abstract LiveData<Zone> selectZoneById(final long zoneId);

    /* zone settings */
    @Query("DELETE FROM zone_settings")
    abstract void dropZoneSettings();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long[] insertZoneSettingsList(final List<ZoneSettings> zoneSettingsList);

    @Transaction
    public long[] insertZoneSettingsTransaction(final List<ZoneSettings> zoneSettingsList) {
        dropZoneSettings();
        return insertZoneSettingsList(zoneSettingsList);
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertZoneSettings(final ZoneSettings zoneSettings);

    @Query("SELECT * FROM zone_settings ORDER BY id ASC")
    public abstract LiveData<List<ZoneSettings>> selectAllZoneSettings();

    @Query("SELECT * FROM zone_settings WHERE id == :zoneSettingsId ORDER BY id ASC")
    public abstract LiveData<ZoneSettings> selectZoneSettingsById(final long zoneSettingsId);

    @Query("SELECT * FROM zone_settings WHERE provider_id == :providerId ORDER BY id ASC")
    public abstract LiveData<List<ZoneSettings>> selectZoneSettingsByProviderId(final long providerId);

    @Query("SELECT * FROM zone_settings WHERE packaging_id == :packagingId ORDER BY id ASC")
    public abstract LiveData<List<ZoneSettings>> selectZoneSettingsByPackagingId(final long packagingId);

    @Query("SELECT * FROM zone_settings WHERE packaging_type_id == :packagingTypeId ORDER BY id ASC")
    public abstract LiveData<List<ZoneSettings>> selectZoneSettingsByPackagingTypeId(final long packagingTypeId);

    /* zone country */
    @Query("DELETE FROM zoneCountry")
    abstract void dropZoneCountries();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long[] insertZoneCountries(final List<ZoneCountry> zoneCountries);

    @Transaction
    public long[] insertZoneCountriesTransaction(final List<ZoneCountry> zoneCountries) {
        dropZoneCountries();
        return insertZoneCountries(zoneCountries);
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertZoneCountry(final ZoneCountry zoneCountry);

    @Query("SELECT * FROM zoneCountry ORDER BY id ASC")
    public abstract LiveData<List<ZoneCountry>> selectAllZoneCountries();

    @Query("SELECT * FROM zoneCountry WHERE country_id == :countryId AND zone_id IS NOT NULL")
    public abstract LiveData<List<ZoneCountry>> selectZoneCountryByCountryId(final long countryId);

    @Query("SELECT * FROM zone_settings WHERE zone_id IN " +
            "(SELECT id FROM zone WHERE id IN " +
            "(SELECT zoneCountry.zone_id FROM zoneCountry WHERE country_id == :countryId AND zone_id IS NOT NULL ORDER BY zone_id DESC) " +
            "AND provider_id == :providerId ORDER BY id DESC)")
    public abstract LiveData<List<ZoneSettings>> selectZoneSettingsListByCountryIdAndProviderId(final long countryId, final long providerId);
}
