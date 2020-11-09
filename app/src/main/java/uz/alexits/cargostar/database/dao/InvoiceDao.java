package uz.alexits.cargostar.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import uz.alexits.cargostar.model.actor.AddressBook;
import uz.alexits.cargostar.model.shipping.Consignment;
import uz.alexits.cargostar.model.shipping.Invoice;
import uz.alexits.cargostar.model.shipping.Request;

@Dao
public interface InvoiceDao {
    /* invoice */
    @Query("SELECT * FROM request WHERE invoice_id == :invoiceId LIMIT 1")
    LiveData<Request> selectRequestByInvoiceId(final long invoiceId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertInvoice(final Invoice invoice);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertInvoiceList(final List<Invoice> invoiceList);

    @Query("SELECT * FROM invoice ORDER BY id ASC")
    LiveData<List<Invoice>> selectAllInvoices();

    @Query("SELECT * FROM invoice WHERE id == :invoiceId LIMIT 1")
    LiveData<Invoice> selectInvoiceById(final long invoiceId);

    @Update
    int updateInvoice(final Invoice invoice);

    /* address book */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAddressBookEntry(final AddressBook addressBookEntry);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertAddressBookEntries(final List<AddressBook> addressBookList);

    @Query("SELECT * FROM address_book ORDER BY id ASC")
    LiveData<List<AddressBook>> selectAllAddressBookEntries();

    @Query("SELECT * FROM address_book WHERE id == :addressBookId")
    LiveData<AddressBook> selectAddressBookEntryById(final long addressBookId);

    @Update
    int updateAddressBookEntry(final AddressBook addressBook);

    /* cargo */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertCargoList(final List<Consignment> consignmentList);

    @Query("SELECT * FROM consignment WHERE request_id == :requestId ORDER BY id DESC")
    LiveData<List<Consignment>> selectConsignmentListByRequestId(final Long requestId);
}
