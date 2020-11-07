package uz.alexits.cargostar.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import uz.alexits.cargostar.model.transportation.Transportation;
import uz.alexits.cargostar.model.transportation.TransportationData;
import uz.alexits.cargostar.model.transportation.TransportationStatus;

import java.util.List;

@Dao
public interface TransportationDao {
    /* Transportation */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertTransportation(final Transportation transportation);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertTransportationList(final List<Transportation> transportationList);

    @Query("SELECT * FROM transportation ORDER BY id DESC")
    LiveData<List<Transportation>> selectCurrentTransportations();

    @Query("DELETE FROM transportation")
    void dropTransportations();

    @Query("DELETE FROM transportation WHERE id == :transportationId")
    void deleteTransportation(final long transportationId);

    /* Transportation Data */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertTransportationData(final List<TransportationData> transportationDataList);

    @Query("SELECT * FROM transportationData WHERE transportation_id == :transportationId")
    LiveData<List<TransportationData>> selectTransportationDataByTransportationId(final long transportationId);

    @Update
    int updateTransportationData(final TransportationData transportationData);

    /* Transportation Statuses */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertTransportationStatus(final Transportation transportation);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertTransportationStatusList(final List<TransportationStatus> transportationList);
}
