package uz.alexits.cargostar.database.cache;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import uz.alexits.cargostar.database.converters.DateConverter;
import uz.alexits.cargostar.database.converters.PaymentStatusConverter;
import uz.alexits.cargostar.database.dao.ActorDao;
import uz.alexits.cargostar.database.dao.InvoiceDao;
import uz.alexits.cargostar.database.dao.LocationDao;
import uz.alexits.cargostar.database.dao.NotificationDao;
import uz.alexits.cargostar.database.dao.PackagingDao;
import uz.alexits.cargostar.database.dao.TransportationDao;
import uz.alexits.cargostar.database.dao.RequestDao;
import uz.alexits.cargostar.model.actor.Customer;
import uz.alexits.cargostar.model.calculation.Vat;
import uz.alexits.cargostar.model.calculation.Zone;
import uz.alexits.cargostar.model.calculation.ZoneCountry;
import uz.alexits.cargostar.model.calculation.ZoneSettings;
import uz.alexits.cargostar.model.location.Branche;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.model.location.TransitPoint;
import uz.alexits.cargostar.model.transportation.Consignment;
import uz.alexits.cargostar.model.transportation.Invoice;
import uz.alexits.cargostar.model.transportation.Request;
import uz.alexits.cargostar.model.transportation.Partial;
import uz.alexits.cargostar.model.transportation.Route;
import uz.alexits.cargostar.push.Notification;
import uz.alexits.cargostar.model.actor.AddressBook;
import uz.alexits.cargostar.model.actor.Courier;
import uz.alexits.cargostar.model.actor.User;
import uz.alexits.cargostar.model.calculation.Packaging;
import uz.alexits.cargostar.model.calculation.PackagingType;
import uz.alexits.cargostar.model.calculation.Provider;
import uz.alexits.cargostar.model.transportation.Transportation;
import uz.alexits.cargostar.model.transportation.TransportationData;
import uz.alexits.cargostar.model.transportation.TransportationStatus;

@Database(entities = {
        User.class,
        Courier.class,
        Customer.class,
        AddressBook.class,
        Country.class,
        Region.class,
        City.class,
        Branche.class,
        TransitPoint.class,
        Invoice.class,
        Consignment.class,
        Notification.class,
        Request.class,
        Provider.class,
        Packaging.class,
        PackagingType.class,
        Zone.class,
        ZoneSettings.class,
        ZoneCountry.class,
        Transportation.class,
        TransportationStatus.class,
        TransportationData.class,
        Route.class,
        Vat.class}, version = 90, exportSchema = false)
@TypeConverters({ PaymentStatusConverter.class, DateConverter.class })
public abstract class LocalCache extends RoomDatabase {
    private static final String DB_NAME = "cargo_cache.db";
    private static volatile LocalCache instance;

    public abstract LocationDao locationDao();

    public abstract PackagingDao packagingDao();

    public abstract ActorDao actorDao();

    public abstract RequestDao requestDao();
    public abstract InvoiceDao invoiceDao();
    public abstract TransportationDao transportationDao();

    public abstract NotificationDao notificationDao();

    public static LocalCache getInstance(final Context context) {
        if (instance == null) {
            synchronized (LocalCache.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), LocalCache.class, DB_NAME)
                            //todo: remove allow Main Thread Queries
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                }

                                @Override
                                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                    super.onOpen(db);
                                }
                            }).build();
                }
            }
        }
        return instance;
    }

    private static final String TAG = LocalCache.class.toString();
}
