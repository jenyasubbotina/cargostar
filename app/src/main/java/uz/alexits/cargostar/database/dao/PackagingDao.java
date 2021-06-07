package uz.alexits.cargostar.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import uz.alexits.cargostar.entities.calculation.Packaging;
import uz.alexits.cargostar.entities.calculation.PackagingType;

import java.util.List;

@Dao
public abstract class PackagingDao {
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

    @Query("SELECT * FROM packaging WHERE provider_id == :providerId ORDER BY id ASC")
    public abstract LiveData<List<Packaging>> selectPackagingListByProviderId(final long providerId);

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

    @Query("SELECT * FROM packagingType WHERE type == 1 AND packaging_id IN (" +
            "SELECT id FROM packaging WHERE provider_id == :providerId ORDER BY id ASC) ORDER BY id ASC")
    public abstract LiveData<List<PackagingType>> selectDocPackagingTypesByPackagingIds(final long providerId);

    @Query("SELECT * FROM packagingType WHERE type == 2 AND packaging_id IN (" +
            "SELECT id FROM packaging WHERE provider_id == :providerId ORDER BY id ASC) ORDER BY id ASC")
    public abstract LiveData<List<PackagingType>> selectBoxPackagingTypesByPackagingIds(final long providerId);

    @Query("SELECT * FROM packaging WHERE id == :packagingId LIMIT 1")
    public abstract LiveData<Packaging> selectPackagingById(long packagingId);
}
