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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createCustomer(final Customer newCustomer);

    @Query("SELECT * FROM customer WHERE id == :userId")
    LiveData<Customer> selectCustomer(final long userId);

    @Query("SELECT * FROM customer ORDER BY email DESC")
    LiveData<List<Customer>> selectAllCustomers();

    @Query("SELECT user_id FROM customer WHERE email LIKE :senderEmail LIMIT 1")
    LiveData<Long> selectSenderUserIdByEmail(final String senderEmail);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertSenderList(final List<Customer> senderList);

    @Query("SELECT * FROM customer WHERE email LIKE :email LIMIT 1")
    LiveData<Customer> selectSenderByEmail(final String email);
}
