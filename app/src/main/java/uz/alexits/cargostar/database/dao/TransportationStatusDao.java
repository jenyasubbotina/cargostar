package uz.alexits.cargostar.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import uz.alexits.cargostar.entities.transportation.Route;
import uz.alexits.cargostar.entities.transportation.TransportationStatus;

@Dao
public interface TransportationStatusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTransportationStatus(final TransportationStatus status);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertTransportationStatusList(final List<TransportationStatus> transportationList);

    @Query("SELECT * FROM transportationStatus WHERE id == :statusId LIMIT 1")
    LiveData<TransportationStatus> selectTransportationStatusById(final long statusId);

    @Query("SELECT * FROM transportationStatus ORDER BY id DESC")
    LiveData<List<TransportationStatus>> selectAllTransportationStatuses();

    /* route */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertTransportationRoute(final List<Route> transportationRouteList);

    @Query("SELECT * FROM route WHERE transportation_id == :transportationId ORDER BY id ASC")
    LiveData<List<Route>> selectRouteByTransportationId(final long transportationId);
}
