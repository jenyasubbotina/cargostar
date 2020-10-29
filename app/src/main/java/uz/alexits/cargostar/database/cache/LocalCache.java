package uz.alexits.cargostar.database.cache;

import android.content.Context;
import android.content.res.AssetManager;
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
import uz.alexits.cargostar.database.dao.LocationDao;
import uz.alexits.cargostar.database.dao.PackagingDao;
import uz.alexits.cargostar.database.dao.ParcelDao;
import uz.alexits.cargostar.database.dao.RequestDao;
import uz.alexits.cargostar.model.actor.Customer;
import uz.alexits.cargostar.model.location.Branche;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.model.location.TransitPoint;
import uz.alexits.cargostar.model.shipping.Cargo;
import uz.alexits.cargostar.model.shipping.Consolidation;
import uz.alexits.cargostar.model.shipping.Receipt;
import uz.alexits.cargostar.model.shipping.ReceiptTransitPointCrossRef;
import uz.alexits.cargostar.model.shipping.Request;
import uz.alexits.cargostar.model.Notification;
import uz.alexits.cargostar.model.actor.Account;
import uz.alexits.cargostar.model.actor.AddressBook;
import uz.alexits.cargostar.model.actor.Courier;
import uz.alexits.cargostar.model.actor.User;
import uz.alexits.cargostar.model.packaging.Packaging;
import uz.alexits.cargostar.model.packaging.PackagingType;
import uz.alexits.cargostar.model.packaging.Provider;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.CheckedOutputStream;

@Database(entities = {
        User.class,
        Courier.class,
        Customer.class,
//        PassportData.class,
//        PaymentData.class,
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
        Notification.class,
        Request.class,
        Provider.class,
        Packaging.class,
        PackagingType.class}, version = 49, exportSchema = false)
@TypeConverters({ PointConverter.class, TransportationStatusConverter.class, PaymentStatusConverter.class, DateConverter.class })
public abstract class LocalCache extends RoomDatabase {
    private static final String DB_NAME = "cargo_cache.db";
    private static volatile LocalCache instance;

    public abstract LocationDao locationDao();
    public abstract PackagingDao packagingDao();
    public abstract ActorDao actorDao();
    public abstract ParcelDao parcelDao();
    public abstract RequestDao requestDao();

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
                                        populateLocationData(context);
                                        populateProviders(context);
                                        populateDefaultCourier(context);
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

    private static void populateDefaultCourier(@NonNull final Context context) {
        getInstance(context).actorDao().createCourier(new Courier(
                8,
                191,
                0,
                1,
                "Sergey",
                "",
                "Kadushkin",
                "+998935977577",
                "android.kim@gmail.com",
                "Chilonzor 24",
                "",
                "111111",
                1,
                null,
                null,
                new Account("android", "12345"),
                1));
    }

    private static void populateLocationData(@NonNull final Context context) {
        final Gson gson = new Gson();

        InputStream countriesIs = null;
        InputStream regionsIs = null;
        InputStream citiesIs = null;

        try {
            countriesIs = context.getAssets().open("location/countries.json");
            regionsIs = context.getAssets().open("location/regions.json");
            citiesIs = context.getAssets().open("location/cities.json");

            Log.i(TAG, "IS: " + countriesIs);

            final Type countryType = new TypeToken<List<Country>>(){}.getType();
            final List<Country> countryList = gson.fromJson(new JsonReader(new InputStreamReader(countriesIs)), countryType);

            final Type regionType = new TypeToken<List<Region>>(){}.getType();
            final List<Region> regionList = gson.fromJson(new JsonReader(new InputStreamReader(regionsIs)), regionType);

            final Type cityType = new TypeToken<List<City>>(){}.getType();
            final List<City> cityList = gson.fromJson(new JsonReader(new InputStreamReader(citiesIs)), cityType);

            Log.i(TAG, "countryList=" + countryList.size());
            Log.i(TAG, "regionList=" + regionList.size());
            Log.i(TAG, "cityList=" + cityList.size());

            getInstance(context).locationDao().insertCountries(countryList);
            getInstance(context).locationDao().insertRegions(regionList);
            getInstance(context).locationDao().insertCities(cityList);
        }
        catch (IOException e) {
            Log.e(TAG, "populateLocationData(): ", e);
        }
        finally {
            try {
                if (countriesIs != null) {
                    countriesIs.close();
                }
                if (regionsIs != null) {
                    regionsIs.close();
                }
                if (citiesIs != null) {
                    citiesIs.close();
                }
            }
            catch (IOException e) {
                Log.e(TAG, "populateLocationData(): ", e);
            }
        }
    }

    private static final String TAG = LocalCache.class.toString();
}
