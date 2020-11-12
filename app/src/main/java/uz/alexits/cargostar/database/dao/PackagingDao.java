package uz.alexits.cargostar.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Index;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import uz.alexits.cargostar.model.calculation.Vat;
import uz.alexits.cargostar.model.calculation.Zone;
import uz.alexits.cargostar.model.calculation.ZoneCountry;
import uz.alexits.cargostar.model.calculation.ZoneSettings;
import uz.alexits.cargostar.model.calculation.Packaging;
import uz.alexits.cargostar.model.calculation.PackagingType;
import uz.alexits.cargostar.model.calculation.Provider;
import java.util.List;

@Dao
public abstract class PackagingDao {
    /* providers */
    @Query("DELETE FROM provider")
    abstract void dropProviders();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long[] insertProviders(final List<Provider> providerList);

    @Transaction
    public long[] insertProvidersTransaction(final List<Provider> providerList) {
        dropProviders();
        return insertProviders(providerList);
    }

    @Query("SELECT * FROM provider ORDER BY id ASC")
    public abstract LiveData<List<Provider>> selectAllProviders();

    @Query("SELECT * FROM provider WHERE id == :providerId ORDER BY id ASC")
    public abstract LiveData<Provider> selectProviderById(final long providerId);

    /* packaging */
    @Query("DELETE FROM packaging")
    abstract void dropPackaging();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long[] insertPackaging(final List<Packaging> packagingList);

    @Transaction
    public long[] insertPackagingTransaction(final List<Packaging> packagingList) {
        dropPackaging();
        return insertPackaging(packagingList);
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertPackaging(final Packaging packagingType);

    @Query("SELECT * FROM packaging ORDER BY id ASC")
    public abstract LiveData<List<Packaging>> selectAllPackaging();

    @Query("SELECT * FROM packaging WHERE provider_id == :providerId ORDER BY id ASC")
    public abstract LiveData<List<Packaging>> selectPackagingsByProviderId(final long providerId);

    @Query("SELECT id FROM packaging WHERE provider_id == :providerId ORDER BY id ASC")
    public abstract LiveData<List<Long>> selectPackagingIdsByProviderId(final Long providerId);

    @Query("SELECT * FROM packaging WHERE id == :packagingId ORDER BY id ASC")
    public abstract LiveData<Packaging> selectPackagingById(final Long packagingId);

    /* packaging-types */
    @Query("DELETE FROM packagingType")
    abstract void dropPackagingType();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract long[] insertPackagingTypes(final List<PackagingType> packagingTypeList);

    @Transaction
    public long[] insertPackagingTypeListTransaction(final List<PackagingType> packagingTypeList) {
        dropPackagingType();
        return insertPackagingTypes(packagingTypeList);
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertPackagingType(final PackagingType packagingType);

    @Query("SELECT * FROM packagingType ORDER BY id ASC")
    public abstract LiveData<List<PackagingType>> selectAllPackagingTypes();

    @Query("SELECT * FROM packagingType WHERE type == :type AND packaging_id IN (:packagingIds) ORDER BY id ASC")
    public abstract LiveData<List<PackagingType>> selectPackagingTypesByTypeAndPackagingIds(final long type, final List<Long> packagingIds);

    @Query("SELECT * FROM packagingType WHERE type == :type AND packaging_id IN " +
            "(SELECT id FROM packaging WHERE provider_id == :providerId ORDER BY id ASC) ORDER BY id ASC")
    public abstract LiveData<List<PackagingType>> selectPackagingTypesByProviderId(final long providerId, final long type);

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

    @Query("SELECT * FROM zone WHERE provider_id == :providerId AND id IN " +
            "(SELECT zone_id FROM zoneCountry WHERE country_id == :countryId AND zone_id IS NOT NULL)" +
            " ORDER BY id ASC")
    public abstract LiveData<List<Zone>> selectZoneListByCountryIdAndProviderId(final Long countryId, final Long providerId);

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

    @Query("SELECT * FROM zone_settings WHERE zone_id IN (:zoneIdList) ORDER BY id ASC")
    public abstract LiveData<List<ZoneSettings>> selectZoneSettingsByZoneIds(final List<Long> zoneIdList);

    @Query("SELECT * FROM zone_settings WHERE packaging_id == :packagingId ORDER BY id ASC")
    public abstract LiveData<List<ZoneSettings>> selectZoneSettingsByPackagingId(final long packagingId);

    @Query("SELECT * FROM zone_settings WHERE packaging_type_id == :packagingTypeId ORDER BY id ASC")
    public abstract LiveData<List<ZoneSettings>> selectZoneSettingsByPackagingTypeId(final long packagingTypeId);

    /* zone countries */
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

//    @Query("SELECT * FROM zoneCountry WHERE country_id == :countryId AND zone_id IS NOT NULL")
//    LiveData<List<ZoneCountry>> selectZoneCountryByCountryId(final long countryId);

    /* vat */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long[] insertVat(final List<Vat> vat);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertVat(final Vat vat);

    @Query("SELECT * FROM vat LIMIT 1")
    public abstract LiveData<Vat> selectVat();
}
