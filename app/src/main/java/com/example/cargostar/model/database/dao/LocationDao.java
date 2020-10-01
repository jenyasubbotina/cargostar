package com.example.cargostar.model.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cargostar.model.location.Branch;
import com.example.cargostar.model.location.City;
import com.example.cargostar.model.location.Country;
import com.example.cargostar.model.location.Region;
import com.example.cargostar.model.location.TransitPoint;

import java.util.List;

@Dao
public interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long createCountry(final Country newCountry);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long[] createCountries(final List<Country> countryList);

    @Query("SELECT * FROM country ORDER BY id ASC")
    LiveData<List<Country>> selectAllCountries();

    @Query("DELETE FROM country")
    void dropCountries();

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long createRegion(final Region newRegion);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long[] createRegions(final List<Region> regionList);

    @Query("SELECT * FROM region ORDER BY country_id ASC")
    LiveData<List<Region>> selectAllRegions();

    @Query("SELECT * FROM region WHERE country_id == :countryId ORDER BY country_id DESC")
    LiveData<List<Region>> selectRegionsByCountry(final long countryId);

    @Query("DELETE FROM region")
    void dropRegions();

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long createCity(final City newCity);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long[] createCities(final List<City> cityList);

    @Query("SELECT * FROM city ORDER BY region_id ASC")
    LiveData<List<City>> selectAllCities();

    @Query("SELECT * FROM city WHERE region_id == :regionId ORDER BY region_id")
    LiveData<List<City>> selectAllCitiesByRegion(final long regionId);

    @Query("SELECT * FROM city WHERE region_id IN (SELECT id FROM region WHERE country_id == :countryId) ORDER BY region_id")
    LiveData<List<City>> selectAllCitiesByCountry(final long countryId);

    @Query("DELETE FROM city")
    void dropCities();

    /*branches*/
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long createBranch(final Branch newBranch);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long[] createBranches(final List<Branch> branchList);

    @Query("SELECT * FROM branch WHERE id == (SELECT id FROM courier WHERE id == :courierId)")
    LiveData<Branch> selectBranchByCourierId(final long courierId);

    @Query("SELECT * FROM branch ORDER BY city_id ASC")
    LiveData<List<Branch>> selectAllBranches();

    @Query("SELECT * FROM country WHERE id == " +
            "(SELECT country_id FROM region WHERE id == " +
            "(SELECT region_id FROM city WHERE id == " +
            "(SELECT city_id FROM branch WHERE id == " +
            "(SELECT branch_id FROM courier WHERE id == :courierId))))")
    LiveData<Country> selectCountryByCourierId(final long courierId);

    @Query("SELECT * FROM region WHERE id == " +
            "(SELECT region_id FROM city WHERE id == " +
            "(SELECT city_id FROM branch WHERE id == " +
            "(SELECT branch_id FROM courier WHERE id == :courierId)))")
    LiveData<Region> selectRegionByCourierId(final long courierId);

    @Query("SELECT * FROM city WHERE id == " +
            "(SELECT city_id FROM branch WHERE id == " +
            "(SELECT branch_id FROM courier WHERE id == :courierId))")
    LiveData<City> selectCityByCourierId(final long courierId);

    @Query("SELECT * FROM branch WHERE city_id == :cityId ORDER BY city_id")
    LiveData<List<Branch>> selectAllBranchesByCity(final long cityId);

    @Query("SELECT * FROM branch WHERE city_id IN (SELECT id FROM region WHERE id == :regionId) ORDER BY city_id")
    LiveData<List<Branch>> selectAllBranchesByRegion(final long regionId);

    @Query("SELECT * FROM branch WHERE city_id IN (SELECT id FROM city WHERE region_id IN " +
            "(SELECT id FROM region WHERE country_id == :countryId)) ORDER BY city_id")
    LiveData<List<Branch>> selectAllBranchesByCountry(final long countryId);

    @Query("DELETE FROM branch")
    void dropBranches();

    /*transit points*/
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long createTransitPoint(final TransitPoint transitPoint);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long[] createTransitPoint(final List<TransitPoint> transitPointList);

    @Update
    void updateTransitPoint(final TransitPoint updatedTransitPoint);

    @Query("DELETE FROM transit_point")
    void dropTransitPoints();

    @Query("SELECT * FROM transit_point ORDER BY transit_point_id")
    LiveData<List<TransitPoint>> selectAllTransitPoints();

    @Query("SELECT * FROM transit_point WHERE transit_point_id == :transitPointId ORDER BY transit_point_id")
    LiveData<TransitPoint> selectTransitPoint(final long transitPointId);

    @Query("SELECT * FROM transit_point WHERE branch_id == :branchId ORDER BY branch_id")
    LiveData<TransitPoint> selectTransitPointByBranch(final long branchId);

    @Query("SELECT * FROM transit_point WHERE branch_id IN (SELECT id FROM branch WHERE city_id == :cityId) ORDER BY transit_point_id")
    LiveData<List<TransitPoint>> selectAllTransitPointsByCity(final long cityId);

    @Query("SELECT * FROM transit_point WHERE branch_id IN (SELECT id FROM branch WHERE city_id IN " +
            "(SELECT id FROM city WHERE region_id IN (SELECT id FROM region WHERE country_id == :countryId))) ORDER BY transit_point_id")
    LiveData<List<TransitPoint>> selectAllTransitPointsByCountry(final long countryId);
}