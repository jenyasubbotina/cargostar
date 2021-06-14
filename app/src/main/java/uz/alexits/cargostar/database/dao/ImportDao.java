package uz.alexits.cargostar.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import uz.alexits.cargostar.entities.transportation.Import;

@Dao
public interface ImportDao {
    @Query("SELECT * FROM import WHERE courier_id == :courierId ORDER BY invoice_id DESC")
    LiveData<List<Import>> selectImportList(final long courierId);

    @Query("SELECT * FROM import WHERE courier_id == :courierId AND created_at >= :startDate ORDER BY invoice_id DESC")
    LiveData<List<Import>> selectImportListByStartDate(final long courierId, final long startDate);

    @Query("SELECT * FROM import WHERE courier_id == :courierId AND created_at <= :endDate ORDER BY invoice_id DESC")
    LiveData<List<Import>> selectImportListByEndDate(final long courierId, final long endDate);

    @Query("SELECT * FROM import WHERE courier_id == :courierId AND created_at >= :startDate AND created_at <= :endDate ORDER BY invoice_id DESC")
    LiveData<List<Import>> selectImportListByBothDates(final long courierId, final long startDate, final long endDate);
}
