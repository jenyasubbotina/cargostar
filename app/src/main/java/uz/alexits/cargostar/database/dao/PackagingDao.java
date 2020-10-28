package uz.alexits.cargostar.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import uz.alexits.cargostar.model.packaging.Packaging;
import uz.alexits.cargostar.model.packaging.PackagingType;
import uz.alexits.cargostar.model.packaging.Provider;
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

    /* packaging */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertPackaging(final List<Packaging> packagingTypeList);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertPackaging(final Packaging packagingType);

    @Query("SELECT * FROM packaging ORDER BY id ASC")
    LiveData<List<Packaging>> selectAllPackaging();

    /* packaging-types */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertPackagingTypes(final List<PackagingType> packagingTypeList);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertPackagingType(final PackagingType packagingType);

    @Query("SELECT * FROM packagingType ORDER BY id ASC")
    LiveData<List<PackagingType>> selectAllPackagingTypes();

}
