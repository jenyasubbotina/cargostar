package uz.alexits.cargostar.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import uz.alexits.cargostar.entities.calculation.Provider;

@Dao
public abstract class ProviderDao {
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

}
