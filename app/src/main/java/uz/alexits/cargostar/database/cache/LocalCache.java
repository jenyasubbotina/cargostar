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
import uz.alexits.cargostar.database.dao.AddressBookDao;
import uz.alexits.cargostar.database.dao.ConsignmentDao;
import uz.alexits.cargostar.database.dao.CourierDao;
import uz.alexits.cargostar.database.dao.ClientDao;
import uz.alexits.cargostar.database.dao.ImportDao;
import uz.alexits.cargostar.database.dao.InvoiceDao;
import uz.alexits.cargostar.database.dao.LocationDao;
import uz.alexits.cargostar.database.dao.NotificationDao;
import uz.alexits.cargostar.database.dao.PackagingDao;
import uz.alexits.cargostar.database.dao.ProviderDao;
import uz.alexits.cargostar.database.dao.TransportationDao;
import uz.alexits.cargostar.database.dao.RequestDao;
import uz.alexits.cargostar.database.dao.TransportationStatusDao;
import uz.alexits.cargostar.database.dao.VatDao;
import uz.alexits.cargostar.database.dao.ZoneDao;
import uz.alexits.cargostar.entities.actor.Client;
import uz.alexits.cargostar.entities.calculation.Vat;
import uz.alexits.cargostar.entities.calculation.Zone;
import uz.alexits.cargostar.entities.calculation.ZoneCountry;
import uz.alexits.cargostar.entities.calculation.ZoneSettings;
import uz.alexits.cargostar.entities.location.Branche;
import uz.alexits.cargostar.entities.location.City;
import uz.alexits.cargostar.entities.location.Country;
import uz.alexits.cargostar.entities.location.Region;
import uz.alexits.cargostar.entities.location.TransitPoint;
import uz.alexits.cargostar.entities.transportation.Consignment;
import uz.alexits.cargostar.entities.transportation.Import;
import uz.alexits.cargostar.entities.transportation.Invoice;
import uz.alexits.cargostar.entities.transportation.Request;
import uz.alexits.cargostar.entities.transportation.Route;
import uz.alexits.cargostar.push.Notification;
import uz.alexits.cargostar.entities.actor.AddressBook;
import uz.alexits.cargostar.entities.actor.Courier;
import uz.alexits.cargostar.entities.actor.User;
import uz.alexits.cargostar.entities.calculation.Packaging;
import uz.alexits.cargostar.entities.calculation.PackagingType;
import uz.alexits.cargostar.entities.calculation.Provider;
import uz.alexits.cargostar.entities.transportation.Transportation;
import uz.alexits.cargostar.entities.transportation.TransportationData;
import uz.alexits.cargostar.entities.transportation.TransportationStatus;

@Database(entities = {
        User.class,
        Courier.class,
        Client.class,
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
        Vat.class}, views = {Import.class}, version = 108, exportSchema = true)
@TypeConverters({ PaymentStatusConverter.class, DateConverter.class })
public abstract class LocalCache extends RoomDatabase {
    private static final String DB_NAME = "cargo_cache.db";
    private static volatile LocalCache instance;

    public abstract CourierDao courierDao();
    public abstract ClientDao clientDao();
    public abstract AddressBookDao addressBookDao();

    public abstract LocationDao locationDao();

    public abstract ProviderDao providerDao();
    public abstract PackagingDao packagingDao();
    public abstract VatDao vatDao();

    public abstract ZoneDao zoneDao();

    public abstract RequestDao requestDao();
    public abstract InvoiceDao invoiceDao();
    public abstract ImportDao importDao();

    public abstract TransportationDao transportationDao();
    public abstract TransportationStatusDao transportationStatusDao();

    public abstract ConsignmentDao consignmentDao();

    public abstract NotificationDao notificationDao();

    public static LocalCache getInstance(final Context context) {
        if (instance == null) {
            synchronized (LocalCache.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), LocalCache.class, DB_NAME)
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
