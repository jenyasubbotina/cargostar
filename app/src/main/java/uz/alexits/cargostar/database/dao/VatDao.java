package uz.alexits.cargostar.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import uz.alexits.cargostar.entities.calculation.Vat;

@Dao
public interface VatDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertVat(final List<Vat> vat);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertVat(final Vat vat);

    @Query("SELECT * FROM vat LIMIT 1")
    LiveData<Vat> selectVat();
}
