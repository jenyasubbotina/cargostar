package uz.alexits.cargostar.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import uz.alexits.cargostar.model.location.Branche;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.model.location.TransitPoint;
import uz.alexits.cargostar.model.transportation.Request;

import java.util.List;

@Dao
public abstract class LocationDao {
    /* countries */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract long[] insertCountries(final List<Country> countryList);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract long insertCountry(final Country country);

    @Query("DELETE FROM country")
    abstract void dropCountries();

    @Transaction
    public long[] dropAndInsertCountries(final List<Country> countryList) {
        dropCountries();
        return insertCountries(countryList);
    }

    @Query("SELECT * FROM country ORDER BY name_en ASC")
    public abstract LiveData<List<Country>> selectAllCountries();

    @Query("SELECT COUNT(id) FROM country")
    public abstract int getCountriesCount();

    @Query("SELECT * FROM country WHERE id == :countryId")
    public abstract LiveData<Country> selectCountryById(final long countryId);

    @Query("SELECT * FROM country WHERE id == (SELECT recipient_country_id FROM request WHERE id == :requestId LIMIT 1)")
    public abstract LiveData<Country> selectDestinationCountryByRequestId(final long requestId);

    /* regions */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract long[] insertRegions(final List<Region> regionList);

    @Query("DELETE FROM region")
    abstract void dropRegions();

    @Transaction
    public long[] dropAndInsertRegions(final List<Region> regionList) {
        dropRegions();
        return insertRegions(regionList);
    }

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract long insertRegion(final Region region);

    @Query("SELECT * FROM region ORDER BY id ASC")
    public abstract LiveData<List<Region>> selectAllRegions();

    @Query("SELECT * FROM region WHERE country_id == :countryId ORDER BY country_id DESC")
    public abstract LiveData<List<Region>> selectRegionsByCountry(final long countryId);

    @Query("SELECT COUNT(id) FROM region")
    public abstract int getRegionsCount();

    @Query("SELECT * FROM region WHERE id == :regionId")
    public abstract LiveData<Region> selectRegionById(final long regionId);

    @Query("SELECT * FROM region WHERE country_id == :countryId")
    public abstract LiveData<List<Region>> selectRegionsByCountryId(final long countryId);

    /* cities */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long[] insertCities(final List<City> cityList);

    @Query("DELETE FROM city")
    abstract void dropCities();

    @Transaction
    public long[] dropAndInsertCities(final List<City> cityList) {
        dropCities();
        return insertCities(cityList);
    }

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract long insertCity(final City city);

    @Query("SELECT * FROM city ORDER BY name_en ASC")
    public abstract LiveData<List<City>> selectAllCities();

    @Query("SELECT * FROM city WHERE region_id == :regionId ORDER BY region_id")
    public abstract LiveData<List<City>> selectAllCitiesByRegion(final long regionId);

    @Query("SELECT * FROM city WHERE region_id IN (SELECT id FROM region WHERE country_id == :countryId) ORDER BY name_en")
    public abstract LiveData<List<City>> selectCitiesByCountryId(final long countryId);

    @Query("SELECT COUNT(id) FROM city")
    public abstract int getCitiesCount();

    @Query("SELECT * FROM city WHERE id == :cityId")
    public abstract LiveData<City> selectCityById(final long cityId);

    @Query("SELECT * FROM city WHERE region_id == :regionId ORDER BY name_en")
    public abstract LiveData<List<City>> selectCitiesByRegionId(final long regionId);

    /*branches*/
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long[] insertBranches(final List<Branche> brancheList);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract long insertBranche(final Branche branche);

    @Query("SELECT * FROM branche ORDER BY id ASC")
    public abstract LiveData<List<Branche>> selectAllBranches();

    @Query("SELECT * FROM branche WHERE id == :brancheId")
    public abstract LiveData<Branche> selectBrancheById(final long brancheId);

    @Query("DELETE FROM Branche")
    public abstract void dropBranches();

    /*transit points*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long[] insertTransitPoints(final List<TransitPoint> transitPointList);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract long insertTransitPoint(final TransitPoint transitPoint);

    @Query("SELECT * FROM transitPoint ORDER BY id")
    public abstract LiveData<List<TransitPoint>> selectAllTransitPoints();

    @Query("SELECT * FROM transitPoint WHERE id == :transitPointId ORDER BY id")
    public abstract LiveData<TransitPoint> selectTransitPoint(final long transitPointId);

    @Query("SELECT * FROM transitPoint WHERE branche_id == :branchId ORDER BY branche_id")
    public abstract LiveData<TransitPoint> selectTransitPointByBranch(final long branchId);

    @Query("SELECT * FROM transitPoint WHERE branche_id IN (SELECT id FROM Branche WHERE city_id == :cityId) ORDER BY id")
    public abstract LiveData<List<TransitPoint>> selectAllTransitPointsByCity(final long cityId);

    @Query("SELECT * FROM transitPoint WHERE branche_id IN (SELECT id FROM Branche WHERE city_id IN " +
            "(SELECT id FROM city WHERE region_id IN (SELECT id FROM region WHERE country_id == :countryId))) ORDER BY id")
    public abstract LiveData<List<TransitPoint>> selectAllTransitPointsByCountry(final long countryId);

    @Query("SELECT COUNT(id) FROM transitPoint")
    public abstract int getTransitPointsCount();
}