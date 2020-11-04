package uz.alexits.cargostar.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import uz.alexits.cargostar.model.calculation.Zone;
import uz.alexits.cargostar.model.calculation.ZoneCountry;
import uz.alexits.cargostar.model.calculation.ZoneSettings;
import uz.alexits.cargostar.model.calculation.Packaging;
import uz.alexits.cargostar.model.calculation.PackagingType;
import uz.alexits.cargostar.model.calculation.Provider;
import java.util.List;

@Dao
public interface PackagingDao {
    /* providers */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertProviders(final List<Provider> providerList);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertPackagingTypes(final Provider provider);

    @Query("SELECT * FROM provider ORDER BY id ASC")
    LiveData<List<Provider>> selectAllProviders();

    @Query("SELECT * FROM provider WHERE id == :providerId ORDER BY id ASC")
    LiveData<Provider> selectProviderById(final long providerId);

    /* packaging */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertPackaging(final List<Packaging> packagingTypeList);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertPackaging(final Packaging packagingType);

    @Query("SELECT * FROM packaging ORDER BY id ASC")
    LiveData<List<Packaging>> selectAllPackaging();

    @Query("SELECT * FROM packaging WHERE provider_id == :providerId ORDER BY id ASC")
    LiveData<List<Packaging>> selectPackagingsByProviderId(final long providerId);

    @Query("SELECT id FROM packaging WHERE provider_id == :providerId ORDER BY id ASC")
    LiveData<List<Long>> selectPackagingIdsByProviderId(final Long providerId);

    @Query("SELECT * FROM packaging WHERE id == :packagingId ORDER BY id ASC")
    LiveData<Packaging> selectPackagingById(final Long packagingId);

    /* packaging-types */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertPackagingTypes(final List<PackagingType> packagingTypeList);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertPackagingType(final PackagingType packagingType);

    @Query("SELECT * FROM packagingType ORDER BY id ASC")
    LiveData<List<PackagingType>> selectAllPackagingTypes();

    @Query("SELECT * FROM packagingType WHERE type == :type AND packaging_id IN (:packagingIds) ORDER BY id ASC")
    LiveData<List<PackagingType>> selectPackagingTypesByTypeAndPackagingIds(final long type, final List<Long> packagingIds);

    @Query("SELECT * FROM packagingType WHERE type == :type AND packaging_id IN " +
            "(SELECT id FROM packaging WHERE provider_id == :providerId ORDER BY id ASC) ORDER BY id ASC")
    LiveData<List<PackagingType>> selectPackagingTypesByProviderId(final long providerId, final long type);

    /* zones */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertZones(final List<Zone> zoneList);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertZone(final Zone zone);

    @Query("SELECT * FROM zone ORDER BY id ASC")
    LiveData<List<Zone>> selectAllZones();

    @Query("SELECT * FROM zone WHERE id == :zoneId ORDER BY id ASC")
    LiveData<Zone> selectZoneById(final long zoneId);

    @Query("SELECT * FROM zone WHERE provider_id == :providerId AND id IN " +
            "(SELECT zone_id FROM zoneCountry WHERE country_id == :countryId AND zone_id IS NOT NULL)" +
            " ORDER BY id ASC")
    LiveData<List<Zone>> selectZoneListByCountryIdAndProviderId(final Long countryId, final Long providerId);

    /* zone settings */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertZoneSettingsList(final List<ZoneSettings> zoneSettingsList);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertZoneSettings(final ZoneSettings zoneSettings);

    @Query("SELECT * FROM zone_settings ORDER BY id ASC")
    LiveData<List<ZoneSettings>> selectAllZoneSettings();

    @Query("SELECT * FROM zone_settings WHERE id == :zoneSettingsId ORDER BY id ASC")
    LiveData<ZoneSettings> selectZoneSettingsById(final long zoneSettingsId);

    @Query("SELECT * FROM zone_settings WHERE provider_id == :providerId ORDER BY id ASC")
    LiveData<List<ZoneSettings>> selectZoneSettingsByProviderId(final long providerId);

    @Query("SELECT * FROM zone_settings WHERE zone_id IN (:zoneIdList) ORDER BY id ASC")
    LiveData<List<ZoneSettings>> selectZoneSettingsByZoneIds(final List<Long> zoneIdList);

    @Query("SELECT * FROM zone_settings WHERE packaging_id == :packagingId ORDER BY id ASC")
    LiveData<List<ZoneSettings>> selectZoneSettingsByPackagingId(final long packagingId);

    @Query("SELECT * FROM zone_settings WHERE packaging_type_id == :packagingTypeId ORDER BY id ASC")
    LiveData<List<ZoneSettings>> selectZoneSettingsByPackagingTypeId(final long packagingTypeId);

    /* zone countries */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertZoneCountries(final List<ZoneCountry> zoneCountries);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertZoneCountry(final ZoneCountry zoneCountry);

    @Query("SELECT * FROM zoneCountry ORDER BY id ASC")
    LiveData<List<ZoneCountry>> selectAllZoneCountries();

//    @Query("SELECT * FROM zoneCountry WHERE country_id == :countryId AND zone_id IS NOT NULL")
//    LiveData<List<ZoneCountry>> selectZoneCountryByCountryId(final long countryId);
}
