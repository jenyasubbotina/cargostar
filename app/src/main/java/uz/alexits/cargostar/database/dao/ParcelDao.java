package uz.alexits.cargostar.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import uz.alexits.cargostar.model.shipping.Cargo;
import uz.alexits.cargostar.model.shipping.Consolidation;
import uz.alexits.cargostar.model.shipping.Invoice;
import uz.alexits.cargostar.model.shipping.Parcel;
import uz.alexits.cargostar.model.shipping.ReceiptTransitPointCrossRef;
import uz.alexits.cargostar.model.shipping.ReceiptWithCargoList;
import uz.alexits.cargostar.model.shipping.Request;
import uz.alexits.cargostar.model.Notification;
import uz.alexits.cargostar.model.TransportationStatus;

import java.util.List;

@Dao
public interface ParcelDao {
    /*Receipt queries*/
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long createParcelTransitPointCrossRef(final ReceiptTransitPointCrossRef receiptTransitPointCrossRef);

    @Transaction
    @Query("DELETE FROM invoice")
    void dropReceipts();

    @Transaction
    @Query("DELETE FROM invoice WHERE id == :requestId")
    void deleteReceipt(final long requestId);

    @Update
    void updateRequest(final Request updatedRequest);

    @Transaction
    @Query("SELECT * FROM " +
            "(SELECT * FROM invoice WHERE " +
            "service_provider IS NULL OR " +
            "sender_signature IS NULL OR " +
            "recipient_signature IS NULL OR " +
            "sender_login IS NULL OR " +
            "sender_cargostar_acc_num IS NULL OR " +
            "sender_zip IS NULL OR " +
            "sender_email IS NULL OR " +
            "recipient_country IS NULL OR recipient_region IS NULL OR recipient_city IS NULL OR recipient_address IS NULL OR recipient_zip IS NULL OR " +
            "recipient_first_name IS NULL OR " +
            "recipient_middle_name IS NULL OR " +
            "recipient_last_name IS NULL OR " +
            "recipient_phone IS NULL OR " +
            "recipient_email IS NULL OR " +
            "payer_login IS NULL OR " +
            "payer_country IS NULL OR payer_region IS NULL OR payer_city IS NULL OR payer_address IS NULL OR payer_zip IS NULL OR " +
            "payer_first_name IS NULL OR " +
            "payer_middle_name IS NULL OR " +
            "payer_last_name IS NULL OR " +
            "payer_phone IS NULL OR " +
            "payer_email IS NULL OR " +
            "tracking_code IS NULL OR " +
            "qr IS NULL OR " +
            "tariff IS NULL OR " +
            "current_location_id IS NULL OR " +
            "transportation_status IS NULL OR " +
            "cost IS NULL OR " +
            "fuel_charge IS NULL OR " +
            "vat IS NULL OR " +
            "dispatch_date IS NULL OR " +
            "arrival_date IS NULL OR " +
            "receipt_name IS NULL OR receipt_link IS NULL OR " +
            "invoice_name IS NULL OR invoice_link IS NULL) " +
            "WHERE " +
            "courier_id <= 0 AND " +
            "sender_country IS NOT NULL AND " +
            "sender_region IS NOT NULL AND " +
            "sender_city IS NOT NULL AND " +
            "sender_address IS NOT NULL AND " +
            "sender_first_name IS NOT NULL AND " +
            "sender_middle_name IS NOT NULL AND " +
            "sender_last_name IS NOT NULL AND " +
            "sender_phone IS NOT NULL " +
            "ORDER BY id")
    LiveData<List<ReceiptWithCargoList>> selectAllRequests();

    @Transaction
    @Query("SELECT * FROM " +
            "(SELECT * FROM invoice WHERE " +
            "service_provider IS NULL OR " +
            "sender_signature IS NULL OR " +
            "recipient_signature IS NULL OR " +
            "sender_login IS NULL OR " +
            "sender_cargostar_acc_num IS NULL OR " +
            "sender_zip IS NULL OR " +
            "sender_email IS NULL OR " +
            "recipient_country IS NULL OR recipient_region IS NULL OR recipient_city IS NULL OR recipient_address IS NULL OR recipient_zip IS NULL OR " +
            "recipient_first_name IS NULL OR " +
            "recipient_middle_name IS NULL OR " +
            "recipient_last_name IS NULL OR " +
            "recipient_phone IS NULL OR " +
            "recipient_email IS NULL OR " +
            "payer_login IS NULL OR " +
            "payer_country IS NULL OR payer_region IS NULL OR payer_city IS NULL OR payer_address IS NULL OR payer_zip IS NULL OR " +
            "payer_first_name IS NULL OR " +
            "payer_middle_name IS NULL OR " +
            "payer_last_name IS NULL OR " +
            "payer_phone IS NULL OR " +
            "payer_email IS NULL OR " +
            "tracking_code IS NULL OR " +
            "qr IS NULL OR " +
            "current_location_id IS NULL OR " +
            "transportation_status IS NULL OR " +
            "tariff IS NULL OR " +
            "cost IS NULL OR " +
            "fuel_charge IS NULL OR " +
            "vat IS NULL OR " +
            "dispatch_date IS NULL OR " +
            "arrival_date IS NULL OR " +
            "receipt_name IS NULL OR receipt_link IS NULL OR " +
            "invoice_name IS NULL OR invoice_link IS NULL) " +
            "WHERE " +
            "sender_country IS NOT NULL AND " +
            "sender_region IS NOT NULL AND " +
            "sender_city IS NOT NULL AND " +
            "sender_address IS NOT NULL AND " +
            "sender_first_name IS NOT NULL AND " +
            "sender_middle_name IS NOT NULL AND " +
            "sender_last_name IS NOT NULL AND " +
            "sender_phone IS NOT NULL AND " +
            "id == :requestId")
    LiveData<ReceiptWithCargoList> selectRequest(final long requestId);

    @Transaction
    @Query("SELECT * FROM " +
            "(SELECT * FROM invoice WHERE " +
            "service_provider IS NULL OR " +
            "sender_signature IS NULL OR " +
            "recipient_signature IS NULL OR " +
            "sender_login IS NULL OR " +
            "sender_cargostar_acc_num IS NULL OR " +
            "sender_zip IS NULL OR " +
            "sender_email IS NULL OR " +
            "recipient_country IS NULL OR recipient_region IS NULL OR recipient_city IS NULL OR recipient_address IS NULL OR recipient_zip IS NULL OR " +
            "recipient_first_name IS NULL OR " +
            "recipient_middle_name IS NULL OR " +
            "recipient_last_name IS NULL OR " +
            "recipient_phone IS NULL OR " +
            "recipient_email IS NULL OR " +
            "payer_login IS NULL OR " +
            "payer_country IS NULL OR payer_region IS NULL OR payer_city IS NULL OR payer_address IS NULL OR payer_zip IS NULL OR " +
            "payer_first_name IS NULL OR " +
            "payer_middle_name IS NULL OR " +
            "payer_last_name IS NULL OR " +
            "payer_phone IS NULL OR " +
            "payer_email IS NULL OR " +
            "tracking_code IS NULL OR " +
            "current_location_id IS NULL OR " +
            "transportation_status IS NULL OR " +
            "qr IS NULL OR " +
            "tariff IS NULL OR " +
            "cost IS NULL OR " +
            "fuel_charge IS NULL OR " +
            "vat IS NULL OR " +
            "dispatch_date IS NULL OR " +
            "arrival_date IS NULL OR " +
            "receipt_name IS NULL OR receipt_link IS NULL OR " +
            "invoice_name IS NULL OR invoice_link IS NULL) " +
            "WHERE " +
            "courier_id == -1 AND " +
            "sender_country IS NOT NULL AND " +
            "sender_region IS NOT NULL AND " +
            "sender_city IS NOT NULL AND " +
            "sender_address IS NOT NULL AND " +
            "sender_first_name IS NOT NULL AND " +
            "sender_middle_name IS NOT NULL AND " +
            "sender_last_name IS NOT NULL AND " +
            "sender_phone IS NOT NULL " +
            "ORDER BY id")
    LiveData<List<ReceiptWithCargoList>> selectPublicRequests();

    @Transaction
    @Query("SELECT * FROM " +
            "(SELECT * FROM invoice WHERE " +
            "service_provider IS NULL OR " +
            "sender_signature IS NULL OR " +
            "recipient_signature IS NULL OR " +
            "sender_zip IS NULL OR " +
            "sender_email IS NULL OR " +
            "recipient_country IS NULL OR recipient_region IS NULL OR recipient_city IS NULL OR recipient_address IS NULL OR recipient_zip IS NULL OR " +
            "recipient_first_name IS NULL OR " +
            "recipient_middle_name IS NULL OR " +
            "recipient_last_name IS NULL OR " +
            "recipient_phone IS NULL OR " +
            "recipient_email IS NULL OR " +
            "payer_country IS NULL OR payer_region IS NULL OR payer_city IS NULL OR payer_address IS NULL OR payer_zip IS NULL OR " +
            "payer_first_name IS NULL OR " +
            "payer_middle_name IS NULL OR " +
            "payer_last_name IS NULL OR " +
            "payer_phone IS NULL OR " +
            "payer_email IS NULL OR " +
            "tracking_code IS NULL OR " +
            "qr IS NULL OR " +
            "current_location_id IS NULL OR " +
            "transportation_status IS NULL OR " +
            "tariff IS NULL OR " +
            "cost IS NULL OR " +
            "fuel_charge IS NULL OR " +
            "vat IS NULL OR " +
            "dispatch_date IS NULL OR " +
            "arrival_date IS NULL OR " +
            "receipt_name IS NULL OR receipt_link IS NULL OR " +
            "invoice_name IS NULL OR invoice_link IS NULL) " +
            "WHERE " +
            "courier_id == :courierId AND " +
            "sender_country IS NOT NULL AND " +
            "sender_region IS NOT NULL AND " +
            "sender_city IS NOT NULL AND " +
            "sender_address IS NOT NULL AND " +
            "sender_first_name IS NOT NULL AND " +
            "sender_middle_name IS NOT NULL AND " +
            "sender_last_name IS NOT NULL AND " +
            "sender_phone IS NOT NULL " +
            "ORDER BY id")
    LiveData<List<ReceiptWithCargoList>> selectMyRequests(final long courierId);

    /*Cargo queries*/
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void createCargo(final List<Cargo> cargoList);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void createCargo(final Cargo cargo);

    @Update
    void updateCargo(final Cargo updatedCargo);

    @Transaction
    @Query("SELECT * FROM invoice WHERE " +
            "service_provider IS NOT NULL AND " +
            "courier_id > 0 AND " +
            "operator_id IS NOT NULL AND " +
            "sender_signature IS NOT NULL AND " +
            "recipient_signature IS NOT NULL AND " +
            "sender_country IS NOT NULL AND sender_region IS NOT NULL AND sender_city IS NOT NULL AND sender_address IS NOT NULL AND sender_zip IS NOT NULL AND " +
            "sender_first_name IS NOT NULL AND " +
            "sender_middle_name IS NOT NULL AND " +
            "sender_last_name IS NOT NULL AND " +
            "sender_phone IS NOT NULL AND " +
            "sender_email IS NOT NULL AND " +
            "recipient_country IS NOT NULL AND recipient_region IS NOT NULL AND recipient_city IS NOT NULL AND recipient_address IS NOT NULL AND recipient_zip IS NOT NULL AND " +
            "recipient_first_name IS NOT NULL AND " +
            "recipient_middle_name IS NOT NULL AND " +
            "recipient_last_name IS NOT NULL AND " +
            "recipient_phone IS NOT NULL AND " +
            "recipient_email IS NOT NULL AND " +
            "payer_country IS NOT NULL AND payer_region IS NOT NULL AND payer_city IS NOT NULL AND payer_address IS NOT NULL AND payer_zip IS NOT NULL AND " +
            "payer_first_name IS NOT NULL AND " +
            "payer_middle_name IS NOT NULL AND " +
            "payer_last_name IS NOT NULL AND " +
            "payer_phone IS NOT NULL AND " +
            "payer_email IS NOT NULL AND " +
            "tracking_code IS NOT NULL AND " +
            "qr IS NOT NULL AND " +
            "current_location_id IS NOT NULL AND " +
            "transportation_status IS NOT NULL AND " +
            "tariff IS NOT NULL AND " +
            "cost > 0 AND " +
            "fuel_charge > 0 AND " +
            "vat > 0 AND " +
            "dispatch_date IS NOT NULL AND " +
            "arrival_date IS NOT NULL AND " +
            "receipt_name IS NOT NULL AND receipt_link IS NOT NULL AND " +
            "invoice_name IS NOT NULL AND invoice_link IS NOT NULL AND " +
            "id == :receiptId")
    LiveData<Parcel> selectParcel(final long receiptId);

    @Query("SELECT * FROM invoice WHERE id == :receiptId")
    LiveData<Invoice> selectReceipt(final long receiptId);

    @Transaction
    @Query("SELECT * FROM invoice WHERE " +
            "service_provider IS NOT NULL AND " +
            "courier_id == :courierId AND " +
            "operator_id IS NOT NULL AND " +
            "sender_signature IS NOT NULL AND " +
            "recipient_signature IS NOT NULL AND " +
            "sender_country IS NOT NULL AND sender_region IS NOT NULL AND sender_city IS NOT NULL AND sender_address IS NOT NULL AND sender_zip IS NOT NULL AND " +
            "sender_first_name IS NOT NULL AND " +
            "sender_middle_name IS NOT NULL AND " +
            "sender_last_name IS NOT NULL AND " +
            "sender_phone IS NOT NULL AND " +
            "sender_email IS NOT NULL AND " +
            "recipient_country IS NOT NULL AND recipient_region IS NOT NULL AND recipient_city IS NOT NULL AND recipient_address IS NOT NULL AND recipient_zip IS NOT NULL AND " +
            "recipient_first_name IS NOT NULL AND " +
            "recipient_middle_name IS NOT NULL AND " +
            "recipient_last_name IS NOT NULL AND " +
            "recipient_phone IS NOT NULL AND " +
            "recipient_email IS NOT NULL AND " +
            "payer_country IS NOT NULL AND payer_region IS NOT NULL AND payer_city IS NOT NULL AND payer_address IS NOT NULL AND payer_zip IS NOT NULL AND " +
            "payer_first_name IS NOT NULL AND " +
            "payer_middle_name IS NOT NULL AND " +
            "payer_last_name IS NOT NULL AND " +
            "payer_phone IS NOT NULL AND " +
            "payer_email IS NOT NULL AND " +
            "tracking_code IS NOT NULL AND " +
            "qr IS NOT NULL AND " +
            "current_location_id IS NOT NULL AND " +
            "transportation_status == :transportationStatus AND " +
            "tariff IS NOT NULL AND " +
            "cost > 0 AND " +
            "fuel_charge > 0 AND " +
            "vat > 0 AND " +
            "dispatch_date IS NOT NULL AND " +
            "arrival_date IS NOT NULL AND " +
            "receipt_name IS NOT NULL AND receipt_link IS NOT NULL AND " +
            "invoice_name IS NOT NULL AND invoice_link IS NOT NULL")
    LiveData<List<Parcel>> selectParcelsByStatus(final long courierId, final TransportationStatus transportationStatus);

    @Transaction
    @Query("SELECT * FROM invoice WHERE " +
            "service_provider IS NOT NULL AND " +
            "courier_id == :courierId AND " +
            "operator_id IS NOT NULL AND " +
            "sender_signature IS NOT NULL AND " +
            "recipient_signature IS NOT NULL AND " +
            "sender_country IS NOT NULL AND sender_region IS NOT NULL AND sender_city IS NOT NULL AND sender_address IS NOT NULL AND sender_zip IS NOT NULL AND " +
            "sender_first_name IS NOT NULL AND " +
            "sender_middle_name IS NOT NULL AND " +
            "sender_last_name IS NOT NULL AND " +
            "sender_phone IS NOT NULL AND " +
            "sender_email IS NOT NULL AND " +
            "recipient_country IS NOT NULL AND recipient_region IS NOT NULL AND recipient_city IS NOT NULL AND recipient_address IS NOT NULL AND recipient_zip IS NOT NULL AND " +
            "recipient_first_name IS NOT NULL AND " +
            "recipient_middle_name IS NOT NULL AND " +
            "recipient_last_name IS NOT NULL AND " +
            "recipient_phone IS NOT NULL AND " +
            "recipient_email IS NOT NULL AND " +
            "payer_country IS NOT NULL AND payer_region IS NOT NULL AND payer_city IS NOT NULL AND payer_address IS NOT NULL AND payer_zip IS NOT NULL AND " +
            "payer_first_name IS NOT NULL AND " +
            "payer_middle_name IS NOT NULL AND " +
            "payer_last_name IS NOT NULL AND " +
            "payer_phone IS NOT NULL AND " +
            "payer_email IS NOT NULL AND " +
            "tracking_code IS NOT NULL AND " +
            "qr IS NOT NULL AND " +
            "current_location_id IS NOT NULL AND " +
            "transportation_status IN (:statusArray) AND " +
            "tariff IS NOT NULL AND " +
            "cost > 0 AND " +
            "fuel_charge > 0 AND " +
            "vat > 0 AND " +
            "dispatch_date IS NOT NULL AND " +
            "arrival_date IS NOT NULL AND " +
            "receipt_name IS NOT NULL AND receipt_link IS NOT NULL AND " +
            "invoice_name IS NOT NULL AND invoice_link IS NOT NULL")
    LiveData<List<Parcel>> selectParcelsByStatus(final long courierId, final TransportationStatus[] statusArray);

    @Transaction
    @Query("SELECT * FROM invoice WHERE " +
            "service_provider IS NOT NULL AND " +
            "courier_id == :courierId AND " +
            "operator_id IS NOT NULL AND " +
            "sender_signature IS NOT NULL AND " +
            "recipient_signature IS NOT NULL AND " +
            "sender_country IS NOT NULL AND sender_region IS NOT NULL AND sender_city IS NOT NULL AND sender_address IS NOT NULL AND sender_zip IS NOT NULL AND " +
            "sender_first_name IS NOT NULL AND " +
            "sender_middle_name IS NOT NULL AND " +
            "sender_last_name IS NOT NULL AND " +
            "sender_phone IS NOT NULL AND " +
            "sender_email IS NOT NULL AND " +
            "recipient_country IS NOT NULL AND recipient_region IS NOT NULL AND recipient_city IS NOT NULL AND recipient_address IS NOT NULL AND recipient_zip IS NOT NULL AND " +
            "recipient_first_name IS NOT NULL AND " +
            "recipient_middle_name IS NOT NULL AND " +
            "recipient_last_name IS NOT NULL AND " +
            "recipient_phone IS NOT NULL AND " +
            "recipient_email IS NOT NULL AND " +
            "payer_country IS NOT NULL AND payer_region IS NOT NULL AND payer_city IS NOT NULL AND payer_address IS NOT NULL AND payer_zip IS NOT NULL AND " +
            "payer_first_name IS NOT NULL AND " +
            "payer_middle_name IS NOT NULL AND " +
            "payer_last_name IS NOT NULL AND " +
            "payer_phone IS NOT NULL AND " +
            "payer_email IS NOT NULL AND " +
            "tracking_code IS NOT NULL AND " +
            "qr IS NOT NULL AND " +
            "current_location_id == :locationId AND " +
            "transportation_status == :transportationStatus AND " +
            "tariff IS NOT NULL AND " +
            "cost > 0 AND " +
            "fuel_charge > 0 AND " +
            "vat > 0 AND " +
            "dispatch_date IS NOT NULL AND " +
            "arrival_date IS NOT NULL AND " +
            "receipt_name IS NOT NULL AND receipt_link IS NOT NULL AND " +
            "invoice_name IS NOT NULL AND invoice_link IS NOT NULL")
    LiveData<List<Parcel>> selectParcelsByLocation(final long courierId, final TransportationStatus transportationStatus, final long locationId);

    @Transaction
    @Query("SELECT * FROM invoice WHERE " +
            "service_provider IS NOT NULL AND " +
            "courier_id == :courierId AND " +
            "operator_id IS NOT NULL AND " +
            "sender_signature IS NOT NULL AND " +
            "recipient_signature IS NOT NULL AND " +
            "sender_country IS NOT NULL AND sender_region IS NOT NULL AND sender_city IS NOT NULL AND sender_address IS NOT NULL AND sender_zip IS NOT NULL AND " +
            "sender_first_name IS NOT NULL AND " +
            "sender_middle_name IS NOT NULL AND " +
            "sender_last_name IS NOT NULL AND " +
            "sender_phone IS NOT NULL AND " +
            "sender_email IS NOT NULL AND " +
            "recipient_country IS NOT NULL AND recipient_region IS NOT NULL AND recipient_city IS NOT NULL AND recipient_address IS NOT NULL AND recipient_zip IS NOT NULL AND " +
            "recipient_first_name IS NOT NULL AND " +
            "recipient_middle_name IS NOT NULL AND " +
            "recipient_last_name IS NOT NULL AND " +
            "recipient_phone IS NOT NULL AND " +
            "recipient_email IS NOT NULL AND " +
            "payer_country IS NOT NULL AND payer_region IS NOT NULL AND payer_city IS NOT NULL AND payer_address IS NOT NULL AND payer_zip IS NOT NULL AND " +
            "payer_first_name IS NOT NULL AND " +
            "payer_middle_name IS NOT NULL AND " +
            "payer_last_name IS NOT NULL AND " +
            "payer_phone IS NOT NULL AND " +
            "payer_email IS NOT NULL AND " +
            "tracking_code IS NOT NULL AND " +
            "qr IS NOT NULL AND " +
            "(current_location_id == :locationId AND " +
            "transportation_status == :inTransitStatus) OR " +
//            "(current_location_id == :locationId AND " +
//            "transportation_status == :onTheWayStatus) OR " +
            "(transportation_status IN (:statusList) AND " +
            "transportation_status != :inTransitStatus) AND " +
            "tariff IS NOT NULL AND " +
            "cost > 0 AND " +
            "fuel_charge > 0 AND " +
            "vat > 0 AND " +
            "dispatch_date IS NOT NULL AND " +
            "arrival_date IS NOT NULL AND " +
            "receipt_name IS NOT NULL AND receipt_link IS NOT NULL AND " +
            "invoice_name IS NOT NULL AND invoice_link IS NOT NULL")
    LiveData<List<Parcel>> selectParcelsByLocationAndStatus(final long courierId, final TransportationStatus inTransitStatus, final long locationId,  final TransportationStatus[] statusList);

    @Transaction
    @Query("SELECT * FROM invoice WHERE " +
            "service_provider IS NOT NULL AND " +
            "operator_id IS NOT NULL AND " +
            "sender_signature IS NOT NULL AND " +
            "recipient_signature IS NOT NULL AND " +
            "sender_country IS NOT NULL AND sender_region IS NOT NULL AND sender_city IS NOT NULL AND sender_address IS NOT NULL AND sender_zip IS NOT NULL AND " +
            "sender_first_name IS NOT NULL AND " +
            "sender_middle_name IS NOT NULL AND " +
            "sender_last_name IS NOT NULL AND " +
            "sender_phone IS NOT NULL AND " +
            "sender_email IS NOT NULL AND " +
            "recipient_country IS NOT NULL AND recipient_region IS NOT NULL AND recipient_city IS NOT NULL AND recipient_address IS NOT NULL AND recipient_zip IS NOT NULL AND " +
            "recipient_first_name IS NOT NULL AND " +
            "recipient_middle_name IS NOT NULL AND " +
            "recipient_last_name IS NOT NULL AND " +
            "recipient_phone IS NOT NULL AND " +
            "recipient_email IS NOT NULL AND " +
            "payer_country IS NOT NULL AND payer_region IS NOT NULL AND payer_city IS NOT NULL AND payer_address IS NOT NULL AND payer_zip IS NOT NULL AND " +
            "payer_first_name IS NOT NULL AND " +
            "payer_middle_name IS NOT NULL AND " +
            "payer_last_name IS NOT NULL AND " +
            "payer_phone IS NOT NULL AND " +
            "payer_email IS NOT NULL AND " +
            "tracking_code IS NOT NULL AND " +
            "qr IS NOT NULL AND " +
            "current_location_id IS NOT NULL AND " +
            "transportation_status IS NOT NULL AND " +
            "tariff IS NOT NULL AND " +
            "cost > 0 AND " +
            "fuel_charge > 0 AND " +
            "vat > 0 AND " +
            "dispatch_date IS NOT NULL AND " +
            "arrival_date IS NOT NULL AND " +
            "receipt_name IS NOT NULL AND receipt_link IS NOT NULL AND " +
            "invoice_name IS NOT NULL AND invoice_link IS NOT NULL")
    LiveData<List<Parcel>> selectAllParcels();

    @Query("SELECT * FROM invoice ORDER BY id ASC")
    LiveData<List<Invoice>> selectAllReceipts();

    @Query("UPDATE request SET is_new = :isNew WHERE id == :receiptId")
    void readReceipt(final long receiptId, final boolean isNew);

    /*Notification queries*/
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void createNotification(final List<Notification> notificationList);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void createNotification(final Notification notification);

    @Update
    void updateNotification(final Notification updatedNotification);

    @Query("UPDATE notification SET is_read = :isRead WHERE receipt_id == :receiptId")
    void readNotification(final long receiptId, final boolean isRead);

    @Query("SELECT * FROM notification WHERE receipt_id == :receiptId")
    LiveData<Notification> selectNotification(final long receiptId);

    @Query("SELECT * FROM notification ORDER BY receive_date DESC")
    LiveData<List<Notification>> selectAllNotifications();

    @Query("SELECT * FROM notification WHERE is_read == :isRead ORDER BY receive_date DESC")
    LiveData<List<Notification>> selectNewNotifications(final boolean isRead);

    @Query("SELECT COUNT() FROM notification WHERE is_read == :isRead")
    LiveData<Integer> selectNewNotificationsCount(final boolean isRead);

    /*Consolidation queries*/
    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long[] createConsolidation(final List<Consolidation> consolidationList);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long createConsolidation(final Consolidation consolidation);

    @Transaction
    @Update
    void updateConsolidation(final Consolidation updatedConsolidation);

    @Transaction
    @Query("SELECT * FROM Invoice WHERE " +
            "service_provider IS NOT NULL AND " +
            "operator_id IS NOT NULL AND " +
            "sender_signature IS NOT NULL AND " +
            "recipient_signature IS NOT NULL AND " +
            "sender_country IS NOT NULL AND sender_region IS NOT NULL AND sender_city IS NOT NULL AND sender_address IS NOT NULL AND sender_zip IS NOT NULL AND " +
            "sender_first_name IS NOT NULL AND " +
            "sender_middle_name IS NOT NULL AND " +
            "sender_last_name IS NOT NULL AND " +
            "sender_phone IS NOT NULL AND " +
            "sender_email IS NOT NULL AND " +
            "recipient_country IS NOT NULL AND recipient_region IS NOT NULL AND recipient_city IS NOT NULL AND recipient_address IS NOT NULL AND recipient_zip IS NOT NULL AND " +
            "recipient_first_name IS NOT NULL AND " +
            "recipient_middle_name IS NOT NULL AND " +
            "recipient_last_name IS NOT NULL AND " +
            "recipient_phone IS NOT NULL AND " +
            "recipient_email IS NOT NULL AND " +
            "payer_country IS NOT NULL AND payer_region IS NOT NULL AND payer_city IS NOT NULL AND payer_address IS NOT NULL AND payer_zip IS NOT NULL AND " +
            "payer_first_name IS NOT NULL AND " +
            "payer_middle_name IS NOT NULL AND " +
            "payer_last_name IS NOT NULL AND " +
            "payer_phone IS NOT NULL AND " +
            "payer_email IS NOT NULL AND " +
            "tracking_code IS NOT NULL AND " +
            "qr IS NOT NULL AND " +
            "current_location_id IS NOT NULL AND " +
            "transportation_status IS NOT NULL AND " +
            "tariff IS NOT NULL AND " +
            "cost > 0 AND " +
            "fuel_charge > 0 AND " +
            "vat > 0 AND " +
            "dispatch_date IS NOT NULL AND " +
            "arrival_date IS NOT NULL AND " +
            "receipt_name IS NOT NULL AND receipt_link IS NOT NULL AND " +
            "invoice_name IS NOT NULL AND invoice_link IS NOT NULL AND " +
            "consolidation_number == :consolidationNumber")
    LiveData<List<Parcel>> selectParcelsByConsolidationNumber(final long consolidationNumber);

    @Transaction
    @Query("SELECT * FROM Invoice WHERE " +
            "service_provider IS NOT NULL AND " +
            "operator_id IS NOT NULL AND " +
            "sender_signature IS NOT NULL AND " +
            "recipient_signature IS NOT NULL AND " +
            "sender_country IS NOT NULL AND sender_region IS NOT NULL AND sender_city IS NOT NULL AND sender_address IS NOT NULL AND sender_zip IS NOT NULL AND " +
            "sender_first_name IS NOT NULL AND " +
            "sender_middle_name IS NOT NULL AND " +
            "sender_last_name IS NOT NULL AND " +
            "sender_phone IS NOT NULL AND " +
            "sender_email IS NOT NULL AND " +
            "recipient_country IS NOT NULL AND recipient_region IS NOT NULL AND recipient_city IS NOT NULL AND recipient_address IS NOT NULL AND recipient_zip IS NOT NULL AND " +
            "recipient_first_name IS NOT NULL AND " +
            "recipient_middle_name IS NOT NULL AND " +
            "recipient_last_name IS NOT NULL AND " +
            "recipient_phone IS NOT NULL AND " +
            "recipient_email IS NOT NULL AND " +
            "payer_country IS NOT NULL AND payer_region IS NOT NULL AND payer_city IS NOT NULL AND payer_address IS NOT NULL AND payer_zip IS NOT NULL AND " +
            "payer_first_name IS NOT NULL AND " +
            "payer_middle_name IS NOT NULL AND " +
            "payer_last_name IS NOT NULL AND " +
            "payer_phone IS NOT NULL AND " +
            "payer_email IS NOT NULL AND " +
            "tracking_code IS NOT NULL AND " +
            "qr IS NOT NULL AND " +
            "current_location_id IS NOT NULL AND " +
            "transportation_status IS NOT NULL AND " +
            "tariff IS NOT NULL AND " +
            "cost > 0 AND " +
            "fuel_charge > 0 AND " +
            "vat > 0 AND " +
            "dispatch_date IS NOT NULL AND " +
            "arrival_date IS NOT NULL AND " +
            "receipt_name IS NOT NULL AND receipt_link IS NOT NULL AND " +
            "invoice_name IS NOT NULL AND invoice_link IS NOT NULL AND " +
            "consolidation_number == :consolidationNumber AND " +
            "id == :parcelId")
    LiveData<Parcel> selectParcelByConsolidationNumber(final long consolidationNumber, final long parcelId);
}
