package uz.alexits.cargostar.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import uz.alexits.cargostar.model.transportation.Request;

import java.util.List;

@Dao
public abstract class RequestDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long insertRequest(final Request newRequest);

    @Query("DELETE FROM request")
    public abstract void dropRequests();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long[] insertRequests(final List<Request> newRequestList);

    @Transaction
    public long[] dropAndInsertRequestList(final List<Request> newRequestList) {
        dropRequests();
        return insertRequests(newRequestList);
    }

    @Query("SELECT * FROM request WHERE id NOT IN (SELECT request_id FROM transportation ORDER BY request_id DESC) AND courier_id IS NULL ORDER BY id DESC")
    public abstract LiveData<List<Request>> selectPublicRequests();

    @Query("SELECT * FROM request WHERE id NOT IN (SELECT request_id FROM transportation ORDER BY request_id DESC) AND courier_id == :courierId ORDER BY id DESC")
    public abstract LiveData<List<Request>> selectRequestsByCourierId(final long courierId);

    @Query("SELECT * FROM request WHERE id == :requestId")
    public abstract LiveData<Request> selectRequestById(final long requestId);

    @Query("SELECT * FROM request WHERE id == :requestId")
    public abstract Request searchRequestById(final long requestId);

    @Update
    public abstract int updateRequest(final Request updatedRequest);

    @Query("UPDATE request SET is_new = :isNew WHERE id == :requestId")
    public abstract void readNewRequest(final long requestId, final boolean isNew);
}
