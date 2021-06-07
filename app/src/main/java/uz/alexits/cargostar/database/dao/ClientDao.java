package uz.alexits.cargostar.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import uz.alexits.cargostar.entities.actor.Client;

import java.util.List;

@Dao
public interface ClientDao {
    /*Customer data*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long createClient(final Client newClient);

    @Query("SELECT * FROM client WHERE id == :userId")
    LiveData<Client> selectClientByUserId(final long userId);

    @Query("SELECT * FROM client ORDER BY email DESC")
    LiveData<List<Client>> selectAllClients();

    @Query("SELECT user_id FROM client WHERE email LIKE :senderEmail LIMIT 1")
    LiveData<Long> selectClientUserIdByEmail(final String senderEmail);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertClientList(final List<Client> senderList);

    @Query("SELECT * FROM client WHERE email LIKE :email LIMIT 1")
    LiveData<Client> selectClientByEmail(final String email);

    @Query("SELECT id FROM client WHERE email LIKE :email LIMIT 1")
    LiveData<Long> selectClientIdByEmail(final String email);

    @Query("SELECT id FROM client ORDER BY id DESC LIMIT 1")
    long getLastClientId();
}
