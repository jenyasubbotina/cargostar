package uz.alexits.cargostar.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import uz.alexits.cargostar.entities.actor.Courier;

@Dao
public interface CourierDao {
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

    @Query("SELECT id FROM country WHERE name_en == :countryName LIMIT 1")
    LiveData<Long> selectCountryByCountryName(final String countryName);
}
