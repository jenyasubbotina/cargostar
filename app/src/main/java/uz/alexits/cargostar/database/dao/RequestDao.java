package uz.alexits.cargostar.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import uz.alexits.cargostar.model.shipping.Request;

import java.util.List;

@Dao
public interface RequestDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertRequest(final Request newRequest);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertRequests(final List<Request> newRequestList);

    @Query("SELECT * FROM request WHERE courier_id IS NULL ORDER BY id DESC")
    LiveData<List<Request>> selectPublicRequests();

    @Query("SELECT * FROM request WHERE courier_id == :courierId ORDER BY id DESC")
    LiveData<List<Request>> selectRequestsByCourierId(final long courierId);

    @Query("SELECT * FROM request WHERE id == :requestId")
    LiveData<Request> selectRequestById(final long requestId);

    @Update
    int updateRequest(final Request updatedRequest);

    @Query("UPDATE request SET is_new = :isNew WHERE id == :requestId")
    void readNewRequest(final long requestId, final boolean isNew);
}
