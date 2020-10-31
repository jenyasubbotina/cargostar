package uz.alexits.cargostar.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import uz.alexits.cargostar.model.actor.AddressBook;
import uz.alexits.cargostar.model.actor.Courier;
import uz.alexits.cargostar.model.actor.Customer;

import java.util.List;

@Dao
public interface ActorDao {
    /*Courier data*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createCourier(final Courier newCourier);

    @Query("UPDATE courier SET password = :password, " +
            "first_name = :firstName, " +
            "middle_name = :middleName, " +
            "last_name = :lastName," +
            " phone = :phone WHERE id == :courierId")
    void updateCourier(final long courierId, final String password, final String firstName, final String middleName, final String lastName, final  String phone);

    @Query("SELECT * FROM courier WHERE id == :userId")
    LiveData<Courier> selectCourier(final long userId);

    @Query("SELECT * FROM courier WHERE login == :login")
    LiveData<Courier> selectCourierByLogin(final String login);

    @Query("SELECT * FROM courier ORDER BY id DESC")
    LiveData<List<Courier>> selectAllCouriers();

    @Query("DELETE FROM courier")
    void dropCouriers();

    /*Customer data*/
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long createCustomer(final Customer newCustomer);
//
//    @Query("SELECT * FROM customer WHERE login == :senderLogin")
//    LiveData<Customer> selectCustomerByLogin(final String senderLogin);
//
//    @Query("SELECT * FROM customer WHERE id == :userId")
//    LiveData<Customer> selectCustomer(final String userId);
//
//    @Query("SELECT * FROM customer ORDER BY id DESC")
//    LiveData<List<Customer>> selectAllCustomers();
//
//    @Query("DELETE FROM customer")
//    void dropCustomers();
//
//    /*Passport data*/
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void createPassportData(final PassportData passportData);
//
//    @Query("SELECT * FROM passport_data WHERE user_id == :userId")
//    LiveData<PassportData> selectPassportData(final String userId);
//
//    @Query("SELECT * FROM passport_data ORDER BY user_id DESC")
//    LiveData<List<PassportData>> selectAllPassportData();
//
//    @Query("DELETE FROM passport_data")
//    void dropPassportData();
//
//    /*Payment data*/
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void createPaymentData(final PaymentData paymentData);
//
//    @Query("SELECT * FROM payment_data WHERE user_id == :userId")
//    LiveData<PaymentData> selectPaymentData(final String userId);
//
//    @Query("SELECT * FROM payment_data ORDER BY user_id DESC")
//    LiveData<List<PaymentData>> selectAllPaymentData();
//
//    @Query("DELETE FROM payment_data")
//    void dropPaymentData();

    /*address book*/
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long createAddressBookEntry(final AddressBook addressBook);

    @Update
    void updateAddressBookEntry(final AddressBook updatedAddressBook);

    @Query("SELECT * FROM address_book WHERE sender_login LIKE :senderLogin")
    LiveData<List<AddressBook>> selectAddressBookEntriesBySenderLogin(final String senderLogin);

    @Query("SELECT * FROM address_book WHERE id == :id")
    LiveData<AddressBook> selectAddressBookEntryById(final long id);

    @Query("SELECT * FROM address_book ORDER BY id DESC")
    LiveData<List<AddressBook>> selectAddressBookEntries();
}
