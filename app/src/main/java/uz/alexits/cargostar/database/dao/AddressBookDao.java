package uz.alexits.cargostar.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import uz.alexits.cargostar.entities.actor.AddressBook;

@Dao
public interface AddressBookDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAddressBookEntry(final AddressBook addressBookEntry);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAddressBookEntries(final List<AddressBook> addressBookList);

    @Query("SELECT * FROM address_book ORDER BY id ASC")
    LiveData<List<AddressBook>> selectAllAddressBookEntries();

    @Query("SELECT * FROM address_book WHERE id == :addressBookId")
    LiveData<AddressBook> selectAddressBookEntryById(final long addressBookId);

    @Query("SELECT * FROM address_book WHERE user_id == :senderUserId")
    LiveData<List<AddressBook>> selectAddressBookBySenderUserId(final long senderUserId);

    @Update
    int updateAddressBookEntry(final AddressBook addressBook);

    @Query("SELECT id FROM address_book ORDER BY id DESC LIMIT 1")
    long getLastAddressBookId();

    @Query("SELECT * FROM address_book WHERE user_id == (SELECT user_id FROM client WHERE email == :senderEmail) ORDER BY country_id DESC")
    LiveData<List<AddressBook>> selectAddressBookBySenderEmail(final String senderEmail);
}
