package uz.alexits.cargostar.database.cache;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import uz.alexits.cargostar.database.converters.DateConverter;
import uz.alexits.cargostar.database.converters.PaymentStatusConverter;
import uz.alexits.cargostar.database.converters.PointConverter;
import uz.alexits.cargostar.database.converters.TransportationStatusConverter;
import uz.alexits.cargostar.database.dao.ActorDao;
import uz.alexits.cargostar.database.dao.InvoiceDao;
import uz.alexits.cargostar.database.dao.LocationDao;
import uz.alexits.cargostar.database.dao.PackagingDao;
import uz.alexits.cargostar.database.dao.ParcelDao;
import uz.alexits.cargostar.database.dao.RequestDao;
import uz.alexits.cargostar.model.actor.Customer;
import uz.alexits.cargostar.model.calculation.Zone;
import uz.alexits.cargostar.model.calculation.ZoneSettings;
import uz.alexits.cargostar.model.location.Branche;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.model.location.TransitPoint;
import uz.alexits.cargostar.model.shipping.Cargo;
import uz.alexits.cargostar.model.shipping.Consolidation;
import uz.alexits.cargostar.model.shipping.Invoice;
import uz.alexits.cargostar.model.shipping.ReceiptTransitPointCrossRef;
import uz.alexits.cargostar.model.shipping.Request;
import uz.alexits.cargostar.model.Notification;
import uz.alexits.cargostar.model.actor.AddressBook;
import uz.alexits.cargostar.model.actor.Courier;
import uz.alexits.cargostar.model.actor.User;
import uz.alexits.cargostar.model.calculation.Packaging;
import uz.alexits.cargostar.model.calculation.PackagingType;
import uz.alexits.cargostar.model.calculation.Provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        ReceiptTransitPointCrossRef.class,
        Cargo.class,
        Consolidation.class,
        Notification.class,
        Request.class,
        Provider.class,
        Packaging.class,
        PackagingType.class,
        Zone.class,
        ZoneSettings.class}, version = 61, exportSchema = false)
@TypeConverters({ PointConverter.class, TransportationStatusConverter.class, PaymentStatusConverter.class, DateConverter.class })
public abstract class LocalCache extends RoomDatabase {
    private static final String DB_NAME = "cargo_cache.db";
    private static volatile LocalCache instance;

    public abstract LocationDao locationDao();
    public abstract PackagingDao packagingDao();
    public abstract ActorDao actorDao();

    public abstract RequestDao requestDao();
    public abstract ParcelDao parcelDao();
    public abstract InvoiceDao invoiceDao();

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

                                    new Thread(() -> {
                                        populateProviders(context);
                                    }).start();
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

    private static void populateProviders(@NonNull final Context context) {
        final Provider fedex = new Provider(
                4,
                "FedEx",
                "FedEx",
                "FedEx",
                true,
                false,
                "FedEx Express — американская грузовая авиакомпания, базирующаяся в городе Мемфис, штат Теннесси.",
                "FedEx Express — американская грузовая авиакомпания, базирующаяся в городе Мемфис, штат Теннесси.",
                "FedEx Express — американская грузовая авиакомпания, базирующаяся в городе Мемфис, штат Теннесси.",
                -1,
                1,
                new Date(1590523358),
                new Date(1602670058));
        final Provider tnt = new Provider(
                5,
                "TNT",
                "TNT",
                "TNT",
                true,
                true,
                "ТНТ Экспресс — одна из крупнейших компаний экспресс-доставки. Штаб-квартира находится в Нидерландах, в городе Хофддорп.",
                "ТНТ Экспресс — одна из крупнейших компаний экспресс-доставки. Штаб-квартира находится в Нидерландах, в городе Хофддорп.",
                "ТНТ Экспресс — одна из крупнейших компаний экспресс-доставки. Штаб-квартира находится в Нидерландах, в городе Хофддорп.",
                12,
                1,
                new Date(1590525452),
                new Date(1602680091));
        final Provider cargostar = new Provider(
                6,
                "Cargostar",
                "Cargostar",
                "Cargostar",
                false,
                false,
                "ООО \"Cargo Star\" - официальный агент НАК \"Узбекистон Хаво Йуллари\" по продаже грузовых перевозок, сертифицированный транспортный экспедитор с застрахованной ответственностью, зарегистрированный в ГТК таможенный брокер и лицензированный перевозчик.",
                "ООО \"Cargo Star\" - официальный агент НАК \"Узбекистон Хаво Йуллари\" по продаже грузовых перевозок, сертифицированный транспортный экспедитор с застрахованной ответственностью, зарегистрированный в ГТК таможенный брокер и лицензированный перевозчик.",
                "ООО \"Cargo Star\" - официальный агент НАК \"Узбекистон Хаво Йуллари\" по продаже грузовых перевозок, сертифицированный транспортный экспедитор с застрахованной ответственностью, зарегистрированный в ГТК таможенный брокер и лицензированный перевозчик.",
                -1,
                1,
                new Date(1598679402),
                new Date(1601460641));
        final List<Provider> providerList = new ArrayList<>();
        providerList.add(fedex);
        providerList.add(tnt);
        providerList.add(cargostar);
        getInstance(context).packagingDao().insertProviders(providerList);
    }

    private static final String TAG = LocalCache.class.toString();
}
