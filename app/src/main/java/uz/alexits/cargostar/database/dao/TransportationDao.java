package uz.alexits.cargostar.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import uz.alexits.cargostar.model.transportation.Route;
import uz.alexits.cargostar.model.transportation.Transportation;
import uz.alexits.cargostar.model.transportation.TransportationData;
import uz.alexits.cargostar.model.transportation.TransportationStatus;

import java.util.List;

@Dao
public abstract class TransportationDao {
    /* Transportation */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertTransportation(final Transportation transportation);

    @Query("DELETE FROM transportation")
    abstract void dropTransportationList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long[] insertTransportationList(final List<Transportation> transportationList);

    @Transaction
    public long[] insertTransportationListTransaction(final List<Transportation> transportationList) {
        dropTransportationList();
        return insertTransportationList(transportationList);
    }

    @Update
    public abstract int updateTransportation(final Transportation transportation);

    @Query("SELECT * FROM transportation WHERE qr_code == :qrCode LIMIT 1")
    public abstract Transportation selectTransportationByQr(final String qrCode);

    @Query("SELECT * FROM transportation ORDER BY id")
    public abstract LiveData<List<Transportation>> selectAllTransportation();

    @Query("SELECT * FROM transportation WHERE " +
            "current_transition_point_id == :transitPointId AND " +
            "transportation_status_id IN (:statusArray) AND " +
            "invoice_id IN (SELECT id FROM invoice WHERE recipient_signature IS NULL) ORDER BY id DESC")
    public abstract LiveData<List<Transportation>> selectCurrentTransportations(final List<Long> statusArray, final Long transitPointId);

    @Query("SELECT id FROM transportation WHERE invoice_id IN (SELECT id FROM invoice WHERE recipient_signature IS NULL ORDER BY id DESC)")
    public abstract LiveData<List<Long>> getEmptySignature();

    @Query("DELETE FROM transportation WHERE id == :transportationId")
    public abstract void deleteTransportation(final long transportationId);

    /* Transportation Data */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long[] insertTransportationData(final List<TransportationData> transportationDataList);

    @Query("SELECT * FROM transportationData WHERE transportation_id == :transportationId")
    public abstract LiveData<List<TransportationData>> selectTransportationDataByTransportationId(final long transportationId);

    @Update
    public abstract int updateTransportationData(final TransportationData transportationData);

    /* Transportation Route */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long[] insertTransportationRoute(final List<Route> transportationRouteList);

    /* Transportation Statuses */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertTransportationStatus(final Transportation transportation);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long[] insertTransportationStatusList(final List<TransportationStatus> transportationList);

    @Query("SELECT * FROM route WHERE transportation_id == :transportationId ORDER BY id ASC")
    public abstract LiveData<List<Route>> selectRouteByTransportationId(final long transportationId);

    @Query("SELECT * FROM transportationStatus WHERE id == :statusId LIMIT 1")
    public abstract LiveData<TransportationStatus> selectTransportationStatusById(final long statusId);

    @Query("SELECT * FROM transportation WHERE id == :transportationId")
    public abstract LiveData<Transportation> selectTransportationById(final long transportationId);

    @Query("SELECT * FROM transportation WHERE invoice_id == :invoiceId LIMIT 1")
    public abstract LiveData<Transportation> selectTransportationByInvoiceId(final long invoiceId);

    @Query("SELECT * FROM transportation WHERE partial_id == :partialId ORDER BY id")
    public abstract LiveData<List<Transportation>> selectTransportationsByPartialId(final long partialId);

    @Query("SELECT id FROM transportation ORDER BY id DESC LIMIT 1")
    public abstract long getLastTransportationId();
}
