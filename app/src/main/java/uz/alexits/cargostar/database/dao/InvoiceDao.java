package uz.alexits.cargostar.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import uz.alexits.cargostar.entities.transportation.Addressee;
import uz.alexits.cargostar.entities.transportation.Invoice;
import uz.alexits.cargostar.entities.transportation.Request;

@Dao
public interface InvoiceDao {
    @Query("SELECT * FROM request WHERE invoice_id == :invoiceId LIMIT 1")
    Request selectRequestByInvoiceId(final long invoiceId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertInvoice(final Invoice invoice);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertInvoiceList(final List<Invoice> invoiceList);

    @Query("SELECT * FROM invoice ORDER BY id ASC")
    LiveData<List<Invoice>> selectAllInvoices();

    @Query("SELECT * FROM invoice WHERE id == :invoiceId LIMIT 1")
    LiveData<Invoice> selectInvoiceById(final long invoiceId);

    @Query("SELECT * FROM invoice WHERE id == :invoiceId LIMIT 1")
    Invoice selectInvoiceByIdSync(final long invoiceId);

    @Query("SELECT id FROM invoice ORDER BY id DESC LIMIT 1")
    long getLastInvoiceId();

    @Update
    int updateInvoice(final Invoice invoice);

    @Query("UPDATE invoice SET " +
            "addressee_full_name = :addresseeFullName, " +
            "addressee_phone = :addresseePhone, " +
            "addressee_address = :addresseeAddress, " +
            "addressee_company = :addresseeCompany, " +
            "addressee_comment = :addresseeComment, " +
            "recipient_signature = :addresseeSignature, " +
            "addressee_signature_date = :addresseeSignatureDate, " +
            "addressee_result = :accepted WHERE id == :invoiceId")
    int insertAddressee(final long invoiceId,
                        final String addresseeFullName,
                        final String addresseePhone,
                        final String addresseeAddress,
                        final String addresseeCompany,
                        final String addresseeComment,
                        final String addresseeSignature,
                        final String addresseeSignatureDate,
                        final int accepted);

    @Query("SELECT addressee_full_name, addressee_phone, addressee_address, addressee_company, addressee_comment, recipient_signature, addressee_signature_date, addressee_result FROM invoice WHERE id == :invoiceId")
    LiveData<Addressee> selectAddresseeByInvoiceId(final long invoiceId);
}
