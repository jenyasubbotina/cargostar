package uz.alexits.cargostar.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

import uz.alexits.cargostar.entities.transportation.Import;

@Dao
public interface ImportDao {
    @Query("SELECT * FROM import ORDER BY invoice_id DESC")
    LiveData<List<Import>> selectImportList();
}
