package uz.alexits.cargostar.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import uz.alexits.cargostar.model.location.Branche;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.model.location.TransitPoint;

import java.util.List;

@Dao
public interface LocationDao {
    /* countries */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertCountries(final List<Country> countryList);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertCountry(final Country country);

    @Query("SELECT * FROM country ORDER BY id ASC")
    LiveData<List<Country>> selectAllCountries();

    @Query("SELECT COUNT(id) FROM country")
    int getCountriesCount();

    @Query("SELECT * FROM country WHERE id == :countryId")
    LiveData<Country> selectCountryById(final long countryId);

    /* regions */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertRegions(final List<Region> regionList);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertRegion(final Region region);

    @Query("SELECT * FROM region ORDER BY country_id ASC")
    LiveData<List<Region>> selectAllRegions();

    @Query("SELECT * FROM region WHERE country_id == :countryId ORDER BY country_id DESC")
    LiveData<List<Region>> selectRegionsByCountry(final long countryId);

    @Query("SELECT COUNT(id) FROM region")
    int getRegionsCount();

    @Query("SELECT * FROM region WHERE id == :regionId")
    LiveData<Region> selectRegionById(final long regionId);

    @Query("SELECT * FROM region WHERE country_id == :countryId")
    LiveData<List<Region>> selectRegionsByCountryId(final long countryId);

    /* cities */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertCities(final List<City> cityList);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertCity(final City city);

    @Query("SELECT * FROM city ORDER BY region_id ASC")
    LiveData<List<City>> selectAllCities();

    @Query("SELECT * FROM city WHERE region_id == :regionId ORDER BY region_id")
    LiveData<List<City>> selectAllCitiesByRegion(final long regionId);

    @Query("SELECT * FROM city WHERE region_id IN (SELECT id FROM region WHERE country_id == :countryId) ORDER BY region_id")
    LiveData<List<City>> selectCitiesByCountryId(final long countryId);

    @Query("SELECT COUNT(id) FROM city")
    int getCitiesCount();

    @Query("SELECT * FROM city WHERE id == :cityId")
    LiveData<City> selectCityById(final long cityId);

    @Query("SELECT * FROM city WHERE region_id == :regionId")
    LiveData<List<City>> selectCitiesByRegionId(final long regionId);

    /*branches*/
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertBranches(final List<Branche> brancheList);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertBranche(final Branche branche);

    @Query("SELECT * FROM branche ORDER BY id ASC")
    LiveData<List<Branche>> selectAllBranches();

    @Query("SELECT * FROM branche WHERE id == :brancheId")
    LiveData<Branche> selectBrancheById(final long brancheId);

    @Query("DELETE FROM Branche")
    void dropBranches();

    /*transit points*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertTransitPoints(final List<TransitPoint> transitPointList);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertTransitPoint(final TransitPoint transitPoint);

    @Query("SELECT * FROM transitPoint ORDER BY id")
    LiveData<List<TransitPoint>> selectAllTransitPoints();

    @Query("SELECT * FROM transitPoint WHERE id == :transitPointId ORDER BY id")
    LiveData<TransitPoint> selectTransitPoint(final long transitPointId);

    @Query("SELECT * FROM transitPoint WHERE branche_id == :branchId ORDER BY branche_id")
    LiveData<TransitPoint> selectTransitPointByBranch(final long branchId);

    @Query("SELECT * FROM transitPoint WHERE branche_id IN (SELECT id FROM Branche WHERE city_id == :cityId) ORDER BY id")
    LiveData<List<TransitPoint>> selectAllTransitPointsByCity(final long cityId);

    @Query("SELECT * FROM transitPoint WHERE branche_id IN (SELECT id FROM Branche WHERE city_id IN " +
            "(SELECT id FROM city WHERE region_id IN (SELECT id FROM region WHERE country_id == :countryId))) ORDER BY id")
    LiveData<List<TransitPoint>> selectAllTransitPointsByCountry(final long countryId);

    @Query("SELECT COUNT(id) FROM transitPoint")
    int getTransitPointsCount();

//    /* zones */
//    @Insert(onConflict = OnConflictStrategy.ABORT)
//    long insertZone(final Zone zone);
//
//    /* zone settings */
//    @Insert(onConflict = OnConflictStrategy.ABORT)
//    long insertZoneSettings(final ZoneSettings zoneSettings);
}