package com.example.cargostar.model.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.cargostar.model.Notification;
import com.example.cargostar.model.actor.AddressBook;
import com.example.cargostar.model.actor.Courier;
import com.example.cargostar.model.actor.Customer;
import com.example.cargostar.model.actor.PassportData;
import com.example.cargostar.model.actor.PaymentData;
import com.example.cargostar.model.actor.User;
import com.example.cargostar.model.database.converters.DateConverter;
import com.example.cargostar.model.database.converters.PaymentStatusConverter;
import com.example.cargostar.model.database.converters.PointConverter;
import com.example.cargostar.model.database.converters.TransportationStatusConverter;
import com.example.cargostar.model.database.dao.ActorDao;
import com.example.cargostar.model.database.dao.LocationDao;
import com.example.cargostar.model.database.dao.ParcelDao;
import com.example.cargostar.model.location.Branche;
import com.example.cargostar.model.location.City;
import com.example.cargostar.model.location.Country;
import com.example.cargostar.model.location.Region;
import com.example.cargostar.model.location.TransitPoint;
import com.example.cargostar.model.shipping.Cargo;
import com.example.cargostar.model.shipping.Consolidation;
import com.example.cargostar.model.shipping.Receipt;
import com.example.cargostar.model.shipping.ReceiptTransitPointCrossRef;

@Database(entities = {
        User.class,
        Courier.class,
        Customer.class,
        PassportData.class,
        PaymentData.class,
        AddressBook.class,
        Country.class,
        Region.class,
        City.class,
        Branche.class,
        TransitPoint.class,
        Receipt.class,
        ReceiptTransitPointCrossRef.class,
        Cargo.class,
        Consolidation.class,
        Notification.class}, version = 37, exportSchema = false)
@TypeConverters({ PointConverter.class, TransportationStatusConverter.class, PaymentStatusConverter.class, DateConverter.class })
public abstract class LocalCache extends RoomDatabase {
    private static final String DB_NAME = "cargo_cache.db";
    private static volatile LocalCache instance;

    public abstract ActorDao actorDao();
    public abstract LocationDao locationDao();
    public abstract ParcelDao parcelDao();

    public static LocalCache getInstance(final Context context) {
        if (instance == null) {
            synchronized (LocalCache.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), LocalCache.class, DB_NAME)
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}
