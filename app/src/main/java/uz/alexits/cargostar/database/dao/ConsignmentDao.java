package uz.alexits.cargostar.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import uz.alexits.cargostar.entities.transportation.Consignment;

@Dao
public interface ConsignmentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertConsignmentList(final List<Consignment> consignmentList);

    @Query("SELECT * FROM consignment WHERE request_id == :requestId ORDER BY id DESC")
    LiveData<List<Consignment>> selectConsignmentListByRequestId(final Long requestId);
}
