package com.example.cargostar.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cargostar.R;
import com.example.cargostar.model.Document;
import com.example.cargostar.model.Notification;
import com.example.cargostar.model.PaymentStatus;
import com.example.cargostar.model.TransportationStatus;
import com.example.cargostar.model.actor.Account;
import com.example.cargostar.model.actor.AddressBook;
import com.example.cargostar.model.actor.Courier;
import com.example.cargostar.model.actor.Customer;
import com.example.cargostar.model.actor.PassportData;
import com.example.cargostar.model.actor.PaymentData;
import com.example.cargostar.model.database.SharedPrefs;
import com.example.cargostar.model.location.Address;
import com.example.cargostar.model.location.Branch;
import com.example.cargostar.model.location.City;
import com.example.cargostar.model.location.Country;
import com.example.cargostar.model.location.Point;
import com.example.cargostar.model.location.Region;
import com.example.cargostar.model.location.TransitPoint;
import com.example.cargostar.model.shipping.Cargo;
import com.example.cargostar.model.shipping.Consolidation;
import com.example.cargostar.model.shipping.Receipt;
import com.example.cargostar.model.shipping.ReceiptTransitPointCrossRef;
import com.example.cargostar.view.UiUtils;
import com.example.cargostar.viewmodel.CreateUserViewModel;
import com.example.cargostar.viewmodel.PopulateViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = SignInActivity.class.toString();
    private static boolean showPassword = false;

    private final List<Country> countryList = new ArrayList<>();
    private final List<Region> regionList = new ArrayList<>();
    private final List<City> cityList = new ArrayList<>();
    private final List<Branch> branchList = new ArrayList<>();
    private final List<TransitPoint> transitPointList = new ArrayList<>();

    private EditText loginEditText;
    private EditText passwordEditText;
    private Button signInBtn;
    private CheckBox keepLoggingCheckBox;
    private TextView forgotPasswordTextView;
    private ImageView passwordEyeImageView;

    private PopulateViewModel populateViewModel;
    private CreateUserViewModel createUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initUI();
        populateViewModel = new ViewModelProvider(this).get(PopulateViewModel.class);
        createUserViewModel = new ViewModelProvider(this).get(CreateUserViewModel.class);
//        initPlaces();
//        initCustomers();
//        initRequests();

        populateViewModel.selectAllCountries();
//        populateViewModel.selectAllRegions();
//        populateViewModel.selectAllCities();
//        populateViewModel.selectAllTransitPoints();
//        populateViewModel.selectPackagingTypes();
//        populateViewModel.selectPackaging();
//        populateViewModel.selectZones();
//        populateViewModel.selectZoneSettings();
//        populateViewModel.selectAddressBook();

        signInBtn.setOnClickListener(v -> {
            final String login = loginEditText.getText().toString();
            final String password = passwordEditText.getText().toString();

            if (TextUtils.isEmpty(login)) {
                Toast.makeText(this, "Логин не может быть пустым", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Пароль не может быть пустым", Toast.LENGTH_SHORT).show();
                return;
            }
            populateViewModel.selectCourierByLogin(login).observe(this, employee -> {
                if (employee == null) {
                    Toast.makeText(this, "Пользователь не зарегистрирован", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(employee.getAccount().getPasswordHash())) {
                    Toast.makeText(this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPrefs.getInstance(this).putString(SharedPrefs.LOGIN, login);
                SharedPrefs.getInstance(this).putString(SharedPrefs.PASSWORD_HASH, password);
                SharedPrefs.getInstance(this).putLong(SharedPrefs.ID, employee.getId());

                if (keepLoggingCheckBox.isChecked()) {
                    SharedPrefs.getInstance(this).putBoolean(SharedPrefs.KEEP_LOGGED, true);
                }
                else {
                    SharedPrefs.getInstance(this).putBoolean(SharedPrefs.KEEP_LOGGED, false);
                }
                startActivity(new Intent(this, MainActivity.class));
                finish();
            });
        });

        if (SharedPrefs.getInstance(this).getBoolean(SharedPrefs.KEEP_LOGGED) &&
                SharedPrefs.getInstance(this).getString(SharedPrefs.LOGIN) != null &&
                SharedPrefs.getInstance(this).getString(SharedPrefs.PASSWORD_HASH) != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void initPlaces() {
        countryList.add(new Country("Узбекистан", "UZ"));
        countryList.add(new Country("Казахстан", "KZ"));
        countryList.add(new Country("Россия", "RU"));
        countryList.add(new Country("Китай", "CN"));
        countryList.add(new Country("Южная Корея", "KR"));

        final long[] countryIds = populateViewModel.createCountries(countryList);

        //Uzbekistan regions
        regionList.add(new Region(countryIds[0], "Ташкент"));
        regionList.add(new Region(countryIds[0], "Андижанская область"));
        regionList.add(new Region(countryIds[0], "Бухарская область"));
        regionList.add(new Region(countryIds[0], "Джизакская область"));
        regionList.add(new Region(countryIds[0], "Кашкадарьинская область"));
        regionList.add(new Region(countryIds[0], "Навоийская область"));
        regionList.add(new Region(countryIds[0], "Наманганская область"));
        regionList.add(new Region(countryIds[0], "Самаркандская область"));
        regionList.add(new Region(countryIds[0], "Сурхандарьинская область"));
        regionList.add(new Region(countryIds[0], "Сырдарьинская область"));
        regionList.add(new Region(countryIds[0], "Ташкентаская область"));
        regionList.add(new Region(countryIds[0], "Ферганская область"));
        regionList.add(new Region(countryIds[0], "Хорезмская область"));
        regionList.add(new Region(countryIds[0], "Республика Каракалпакстан"));
        //Kazakhstan regions
        regionList.add(new Region(countryIds[1],"Алматы"));
        regionList.add(new Region(countryIds[1],"Шымкент"));
        regionList.add(new Region(countryIds[1],"Мангистауская область"));
        regionList.add(new Region(countryIds[1],"Астана"));
        //Russia regions
        regionList.add(new Region(countryIds[2],"Москва"));
        regionList.add(new Region(countryIds[2],"Санкт-Петербург"));
        regionList.add(new Region(countryIds[2],"Приморский край"));
        regionList.add(new Region(countryIds[2],"Новосибирская область"));
        //China regions
        regionList.add(new Region(countryIds[3],"Пекин"));
        //Korea regions
        regionList.add(new Region(countryIds[4],"Сеул"));

        final long[] regionIds = populateViewModel.createRegions(regionList);

        //Uzbekistan cities
        cityList.add(new City(regionIds[0], "Ташкент"));
        cityList.add(new City(regionIds[1], "Андижан"));
        cityList.add(new City(regionIds[2], "Бухара"));
        cityList.add(new City(regionIds[3], "Джизак"));
        cityList.add(new City(regionIds[4], "Карши"));
        cityList.add(new City(regionIds[5], "Навои"));
        cityList.add(new City(regionIds[6], "Наманган"));
        cityList.add(new City(regionIds[7], "Самарканд"));
        cityList.add(new City(regionIds[8], "Термез"));
        cityList.add(new City(regionIds[9], "Гулистан"));
        cityList.add(new City(regionIds[11], "Фергана"));
        cityList.add(new City(regionIds[12], "Ургенч"));
        cityList.add(new City(regionIds[13], "Нукус"));
        //Kazakhstan cities
        cityList.add(new City(regionIds[14],"Алматы"));
        cityList.add(new City(regionIds[15],"Шымкент"));
        cityList.add(new City(regionIds[16],"Актау"));
        cityList.add(new City(regionIds[17],"Астана"));
        //Russia cities
        cityList.add(new City(regionIds[18],"Москва"));
        cityList.add(new City(regionIds[19],"Санкт-Петербург"));
        cityList.add(new City(regionIds[20],"Владивосток"));
        cityList.add(new City(regionIds[21],"Новосибирск"));
        //China cities
        cityList.add(new City(regionIds[22],"Пекин"));
        //Korea cities
        cityList.add(new City(regionIds[23],"Сеул"));

        final long[] cityIds = populateViewModel.createCities(cityList);

        branchList.add(new Branch(cityIds[0], "Ташкент", "Метро Ойбек", "100190", new Point(10, 10), "+998712223344"));
        branchList.add(new Branch(cityIds[1], "Андижан", "ул. Андижан", "100110", new Point(10, 10), "+998712223345"));
        branchList.add(new Branch(cityIds[2], "Бухара", "ул. Бухара", "100120", new Point(10, 10), "+998712223346"));
        branchList.add(new Branch(cityIds[3], "Джизак", "ул. Джизак", "100130", new Point(10, 10), "+998712223347"));
        branchList.add(new Branch(cityIds[4], "Карши", "ул. Карши", "100140", new Point(10, 10), "+998712223348"));
        branchList.add(new Branch(cityIds[5], "Навои", "ул. Навои", "100150", new Point(10, 10), "+998712223349"));
        branchList.add(new Branch(cityIds[6], "Наманган", "ул. Наманган", "100160", new Point(10, 10), "+998712223350"));
        branchList.add(new Branch(cityIds[7], "Самарканд", "ул. Самарканд", "100170", new Point(10, 10), "+998712223351"));
        branchList.add(new Branch(cityIds[8], "Термез", "ул. Термез", "100180", new Point(10, 10), "+998712223352"));
        branchList.add(new Branch(cityIds[9], "Гулистан", "ул. Гулистан", "100050", new Point(10, 10), "+998712223353"));
        branchList.add(new Branch(cityIds[10], "Фергана", "ул. Фергана", "100060", new Point(10, 10), "+998712223354"));
        branchList.add(new Branch(cityIds[11], "Ургенч", "ул. Ургенч", "100070", new Point(10, 10), "+998712223355"));
        branchList.add(new Branch(cityIds[12], "Нукус", "ул. Нукус", "100080", new Point(10, 10), "+998712223356"));
        branchList.add(new Branch(cityIds[13], "Астана", "ул. Нурлан", "980760", new Point(10, 10), "+79878887698"));
        branchList.add(new Branch(cityIds[14], "Алматы", "Метро Сабур", "980760", new Point(10, 10), "+79898971432"));
        branchList.add(new Branch(cityIds[17], "Москва", "Парк Сокольники", "3100120", new Point(10, 10), "+74715648733"));
        branchList.add(new Branch(cityIds[18], "Пекин", "Уханьский рынок", "3100120", new Point(10, 10), "+567249081230"));
        branchList.add(new Branch(cityIds[19], "Сеул", "Тондемун", "200120", new Point(10, 10), "+82100116825"));

        final long[] branchIds = populateViewModel.createBranches(branchList);

        initCouriers(branchIds);
        initParcels(branchIds);
        initConsolidations(branchIds);
    }

    private void initCouriers(final long[] branchIds) {
        final Courier defaultCourier = new Courier(
                "Андроид",
                "Валерьевич",
                "Ким",
                "+998909219678",
                new Account("android", "12345", "andrew_the_slayer@gmail.com"),
                branchIds[16]);
        final Courier sergey = new Courier(
                "Сергей",
                "Игоревич",
                "Бя",
                "+998909967891",
                new Account("sereja", "12345", "the_mighty_dwarf@gmail.com"),
                branchIds[0]);
        final Courier navruz = new Courier(
                "Навруз",
                "Жандуллаевич",
                "Жандуллаев",
                "+998912552255",
                new Account("navruz", "12345", "jason_stacy@gmail.com"),
                branchIds[12]);
        final Courier rustam = new Courier(
                "Рустам",
                "Таджибаевич",
                "Таджиев",
                "+998974371039",
                new Account("rustico", "12345", "rustan_gel@gmail.com"),
                branchIds[1]);
        populateViewModel.createCourier(defaultCourier);
        populateViewModel.createCourier(sergey);
        populateViewModel.createCourier(navruz);
        populateViewModel.createCourier(rustam);
    }

    private void initCustomers() {
        final Customer crosby = new Customer(
                "Сидни",
                "",
                "Кросби",
                "+38918149424",
                new Account("crosby", "12345", "crosby@gmail.com"),
                new Address("Канада", "Онтарио", "Торонто", "Penguins str. 217"),
                "1234567890");
        final Customer dud = new Customer(
                "Юрий",
                "Александрович",
                "Дудь",
                "+75427652987",
                new Account("dud", "12345", "vdud@gmail.com"),
                new Address("Россия", "Москва", "Москва", "Парк Сокольники"),
                "0987654321");

        final long crosbyId = createUserViewModel.createCustomer(crosby);
        final long dudId = createUserViewModel.createCustomer(dud);

        final PassportData passportData = new PassportData("AA7776633");
        passportData.setUserId(crosbyId);
        createUserViewModel.createPassportData(passportData);

        final PaymentData paymentData = new PaymentData("65472819122", "Вдудь");
        paymentData.setUserId(dudId);
        paymentData.setOked("666666666");
        paymentData.setMfo("777777777");
        paymentData.setVat("888888888");
        paymentData.setBank("Тинькофф");
        paymentData.setCheckingAccount("999999999");
        createUserViewModel.createPaymentData(paymentData);

        final AddressBook crosbyAddressBook1 = new AddressBook(
                crosby.getAccount().getLogin(),
                "Сергей", "Львович", "Ким", "+998990116824", "the_real_king@gmail.com",
                new Address("Узбекистан", "Ташкент", "Ташкент", "Юнусабад-8"));
        final AddressBook crosbyAddressBook2 = new AddressBook(
                crosby.getAccount().getLogin(),
                "Сергей", "Игоревич", "Бя", "+998909868886", "the_mighty_dwarf@gmail.com",
                new Address("Узбекистан", "Ташкент", "Ташкент", "Карасу-6"));
        final AddressBook crosbyAddressBook3 = new AddressBook(
                crosby.getAccount().getLogin(),
                "Наврус", "Жандуллаевич", "Жандуллаев", "+998912552255", "njay@gmail.com",
                new Address("Узбекистан", "Республика Каракалпакстан", "Нукус", "Улица пустынная 23"));
        final AddressBook crosbyAddressBook4 = new AddressBook(
                crosby.getAccount().getLogin(),
                "Рустан", "Таджиевич", "Таджиев", "+998974213212", "bad_carma@gmail.com",
                new Address("Узбекистан", "Андижанская Область", "Андижан", "Улица пустырей 19"));
        final AddressBook crosbyAddressBook5 = new AddressBook(
                crosby.getAccount().getLogin(),
                "Андроид", "Валерьевич", "Ким", "+998909876541", "android@gmail.com",
                new Address("Узбекистан", "Ташкент", "Ташкент", "Юнусабад-17"));
        populateViewModel.createAddressBookEntry(crosbyAddressBook1);
        populateViewModel.createAddressBookEntry(crosbyAddressBook2);
        populateViewModel.createAddressBookEntry(crosbyAddressBook3);
        populateViewModel.createAddressBookEntry(crosbyAddressBook4);
        populateViewModel.createAddressBookEntry(crosbyAddressBook4);
    }

    private void initRequests() {
        final Receipt sergeyRequest = new Receipt("Сергей", "Львович", "Ким",
                new Address("Узбекистан", "Ташкент", "Ташкент", "Юнусабад-8"), "+998990116824",
                TransportationStatus.IN_TRANSIT, PaymentStatus.WAITING_PAYMENT);
        final Receipt galRequest = new Receipt("Галь", "", "Гадот",
                new Address("США", "Калифорния", "Сан-Хосе", "Justice str. 9"),
                "+987654321098",
                TransportationStatus.IN_TRANSIT, PaymentStatus.WAITING_CHECK);
        final List<Cargo> sergeyCargoList = new ArrayList<>();
        sergeyCargoList.add(new Cargo("Документы", "Конверт", 50, 30, 3, 1, null));
        sergeyCargoList.add(new Cargo("Канцтовары", "Конверт", 25, 12, 3, 1, null));
        sergeyCargoList.add(new Cargo("Кредитные карты", "Конверт", 7, 5, 3, 1, null));
        final long sergeyParcelId = populateViewModel.createRequest(sergeyRequest);

        for (final Cargo cargo : sergeyCargoList) {
            cargo.setReceiptId(sergeyParcelId);
        }
        populateViewModel.createCargo(sergeyCargoList);

        /*amber account & request*/
        final Customer amber = new Customer(
                "Эмбер",
                "",
                "Херд",
                "+75327652987",
                new Account("amber", "12345", "amber@gmail.com"),
                new Address("Франция", "Париж", "Париж", "st. Eclear Str 289 22B"),
                "9567543218");
        final long amberId = createUserViewModel.createCustomer(amber);

        final PaymentData amberPaymentData = new PaymentData("753421869", "Warner Bros. LTD");
        amberPaymentData.setUserId(amberId);
        amberPaymentData.setOked("145362987");
        amberPaymentData.setMfo("748965123");
        amberPaymentData.setVat("654381927");
        amberPaymentData.setBank("Тинькофф");
        amberPaymentData.setCheckingAccount("951951753");
        createUserViewModel.createPaymentData(amberPaymentData);
        /*Amber addressBook*/
        final AddressBook amberAddressBook1 = new AddressBook(
                amber.getAccount().getLogin(),
                "Сергей", "Львович", "Ким", "+998990116824", "the_real_king@gmail.com",
                new Address("Узбекистан", "Ташкент", "Ташкент", "Юнусабад-8"));
        final AddressBook amberAddressBook2 = new AddressBook(
                amber.getAccount().getLogin(),
                "Сергей", "Игоревич", "Бя", "+998909868886", "the_mighty_dwarf@gmail.com",
                new Address("Узбекистан", "Ташкент", "Ташкент", "Карасу-6"));
        final AddressBook amberAddressBook3 = new AddressBook(
                amber.getAccount().getLogin(),
                "Наврус", "Жандуллаевич", "Жандуллаев", "+998912552255", "njay@gmail.com",
                new Address("Узбекистан", "Республика Каракалпакстан", "Нукус", "Улица пустынная 23"));
        final AddressBook amberAddressBook4 = new AddressBook(
                amber.getAccount().getLogin(),
                "Рустан", "Таджиевич", "Таджиев", "+998974213212", "bad_carma@gmail.com",
                new Address("Узбекистан", "Андижанская Область", "Андижан", "Улица пустырей 19"));
        final AddressBook amberAddressBook5 = new AddressBook(
                amber.getAccount().getLogin(),
                "Андроид", "Валерьевич", "Ким", "+998909876541", "android@gmail.com",
                new Address("Узбекистан", "Ташкент", "Ташкент", "Юнусабад-17"));
        populateViewModel.createAddressBookEntry(amberAddressBook1);
        populateViewModel.createAddressBookEntry(amberAddressBook2);
        populateViewModel.createAddressBookEntry(amberAddressBook3);
        populateViewModel.createAddressBookEntry(amberAddressBook4);
        populateViewModel.createAddressBookEntry(amberAddressBook5);

        final Receipt amberRequest = new Receipt(amber.getFirstName(), amber.getMiddleName(), amber.getLastName(),
                new Address(amber.getAddress().getCountry(), amber.getAddress().getRegion(), amber.getAddress().getCity(), amber.getAddress().getAddress()), amber.getPhone(),
                TransportationStatus.IN_TRANSIT, PaymentStatus.PAID);

        amberRequest.setPayerLogin(amber.getAccount().getLogin());
        amberRequest.setPayerAddress(amber.getAddress());
        amberRequest.setPayerFirstName(amber.getFirstName());
        amberRequest.setPayerMiddleName(amber.getMiddleName());
        amberRequest.setPayerLastName(amber.getLastName());
        amberRequest.setPayerPhone(amber.getPhone());
        amberRequest.setPayerEmail(amber.getAccount().getEmail());
        amberRequest.setDiscount(10);
        amberRequest.setPayerCargostarAccountNumber(amber.getCargostarAccountNumber());
        amberRequest.setPayerTntAccountNumber(amber.getTntAccountNumber());
        amberRequest.setPayerFedexAccountNumber(amber.getTntAccountNumber());
        amberRequest.setCheckingAccount(amberPaymentData.getCheckingAccount());
        amberRequest.setBank(amberPaymentData.getBank());
        amberRequest.setRegistrationCode("11433699582");
        amberRequest.setMfo(amberPaymentData.getMfo());
        amberRequest.setOked(amberPaymentData.getOked());
        amberRequest.setTariff(getString(R.string.express));
        amberRequest.setServiceProvider(getString(R.string.fedex));
        amberRequest.setCost(10500600);

        final List<Cargo> amberCargoList = new ArrayList<>();
        amberCargoList.add(new Cargo("Одежда", "Коробка", 70, 70, 40, 5, null));
        amberCargoList.add(new Cargo("Обувь", "Коробка", 70, 40, 40, 6, null));
        amberCargoList.add(new Cargo("Одежда", "Пакет", 50, 80, 50, 4, null));
        final long amberParcelId = populateViewModel.createRequest(amberRequest);

        for (final Cargo cargo : amberCargoList) {
            cargo.setReceiptId(amberParcelId);
        }
        populateViewModel.createCargo(amberCargoList);

        final List<Cargo> galCargoList = new ArrayList<>();
        galCargoList.add(new Cargo("Твердый груз", "Паллета", 450, 380, 70, 40, null));
        galCargoList.add(new Cargo("Детали для сбора механизмов", "Паллета", 450, 380, 70, 25, null));
        galCargoList.add(new Cargo("Стройматериалы", "Паллета", 450, 380, 70, 120, null));
        final long galParcelId = populateViewModel.createRequest(galRequest);

        for (final Cargo cargo : galCargoList) {
            cargo.setReceiptId(galParcelId);
        }
        populateViewModel.createCargo(galCargoList);
        //init notifications
        final Notification galNotification = new Notification(galParcelId, -1, galRequest.getSenderAddress(), galRequest.getRecipientAddress(), new Date(), false);
        final Notification amberNotification = new Notification(amberParcelId, -1, amberRequest.getSenderAddress(), amberRequest.getRecipientAddress(), new Date(), false);
        final Notification dudNotification = new Notification(sergeyParcelId, -1, sergeyRequest.getSenderAddress(), sergeyRequest.getRecipientAddress(), new Date(), false);
        populateViewModel.createNotification(galNotification);
        populateViewModel.createNotification(amberNotification);
        populateViewModel.createNotification(dudNotification);
    }

    private void initParcels(final long[] branchIds) {
        transitPointList.add(new TransitPoint(branchIds[0], "Ташкент"));
        transitPointList.add(new TransitPoint(branchIds[1], "Андижан"));
        transitPointList.add(new TransitPoint(branchIds[2], "Бухара"));
        transitPointList.add(new TransitPoint(branchIds[3], "Джизак"));
        transitPointList.add(new TransitPoint(branchIds[4], "Карши"));
        transitPointList.add(new TransitPoint(branchIds[5], "Навои"));
        transitPointList.add(new TransitPoint(branchIds[6], "Наманган"));
        transitPointList.add(new TransitPoint(branchIds[7], "Самарканд"));
        transitPointList.add(new TransitPoint(branchIds[8], "Термез"));
        transitPointList.add(new TransitPoint(branchIds[9], "Гулистан"));
        transitPointList.add(new TransitPoint(branchIds[10], "Фергана"));
        transitPointList.add(new TransitPoint(branchIds[11], "Ургенч"));
        transitPointList.add(new TransitPoint(branchIds[12], "Нукус"));
        transitPointList.add(new TransitPoint(branchIds[13], "Астана"));
        transitPointList.add(new TransitPoint(branchIds[14], "Алматы"));
        transitPointList.add(new TransitPoint(branchIds[15], "Москва"));
        transitPointList.add(new TransitPoint(branchIds[16], "Пекин"));
        transitPointList.add(new TransitPoint(branchIds[17], "Сеул"));

        final long[] transitPointIds = populateViewModel.createTransitPoint(transitPointList);
        //android parcel
        final Address androidSenderAddress = new Address("Узбекистан", "Ташкент", "Ташкент", "Юнусабад-8");
        final Address androidRecipientAddress = new Address("Узбекистан", "Кашкадарьинская область", "Карши", "Пустырь 1");
        final Address androidPayerAddress = new Address("Узбекистан", "Ташкент", "Ташкент", "Юнусабад-8");
        androidSenderAddress.setZip("100190");
        androidRecipientAddress.setZip("110120");
        androidPayerAddress.setZip("100190");
        final Receipt androidReceipt = new Receipt("Сергей", "Львович", "Ким",
                androidSenderAddress, "+998990116824",
                TransportationStatus.IN_TRANSIT, PaymentStatus.WAITING_PAYMENT);
        androidReceipt.setServiceProvider("CargoStar");
        androidReceipt.setOperatorId("1234567890");
        androidReceipt.setSenderSignature("sergey_signature.png");
        androidReceipt.setRecipientSignature("rustam_signature.png");
        androidReceipt.setSenderEmail("mikyegresl@gmail.com");
        androidReceipt.setRecipientAddress(androidRecipientAddress);
        androidReceipt.setRecipientFirstName("Рустам");
        androidReceipt.setRecipientMiddleName("Таджибаевич");
        androidReceipt.setRecipientLastName("Таджиев");
        androidReceipt.setRecipientPhone("+998909876543");
        androidReceipt.setRecipientEmail("rustico_bad_carma@gmail.com");
        androidReceipt.setPayerAddress(androidPayerAddress);
        androidReceipt.setPayerFirstName("Сергей");
        androidReceipt.setPayerMiddleName("Львович");
        androidReceipt.setPayerLastName("Ким");
        androidReceipt.setPayerPhone("+998990116824");
        androidReceipt.setPayerEmail("mikyegresl@gmail.com");
        androidReceipt.setTrackingCode("1234567890");
        androidReceipt.setQr("12345678980");
        androidReceipt.setTariff("Express");
        androidReceipt.setCost(2400000);
        androidReceipt.setFuelCharge(10);
        androidReceipt.setVat(15);
        androidReceipt.setDispatchDate(new Date());
        androidReceipt.setArrivalDate(new Date());
        androidReceipt.setReceipt(new Document("Накладная", "android/com.example.cargostar.receipt.doc"));
        androidReceipt.setInvoice(new Document("Инвойс", "android/com.example.cargostar.invoice.doc"));
        androidReceipt.setCourierId(1);
        androidReceipt.setCurrentLocation(transitPointIds[0]);
        androidReceipt.setRead(true);
        final long androidParcelId = populateViewModel.createRequest(androidReceipt);
        populateViewModel.createParcelTransitPointCrossRef(new ReceiptTransitPointCrossRef(androidParcelId, transitPointIds[0]));
        populateViewModel.createParcelTransitPointCrossRef(new ReceiptTransitPointCrossRef(androidParcelId, transitPointIds[2]));
        populateViewModel.createParcelTransitPointCrossRef(new ReceiptTransitPointCrossRef(androidParcelId, transitPointIds[3]));
        populateViewModel.createParcelTransitPointCrossRef(new ReceiptTransitPointCrossRef(androidParcelId, transitPointIds[4]));
        populateViewModel.createParcelTransitPointCrossRef(new ReceiptTransitPointCrossRef(androidParcelId, transitPointIds[1]));

        final List<Cargo> androidCargoList = new ArrayList<>();
        androidCargoList.add(new Cargo("Твердый груз", "Паллета", 450, 380, 70, 40, "1234567890"));
        androidCargoList.add(new Cargo("Детали для сбора механизмов", "Паллета", 450, 380, 70, 25, "1234567899"));
        androidCargoList.add(new Cargo("Стройматериалы", "Паллета", 450, 380, 70, 120, "1234567898"));

        for (final Cargo cargo : androidCargoList) {
            cargo.setReceiptId(androidParcelId);
        }
        populateViewModel.createCargo(androidCargoList);
        //navruz parcel
        final Address navruzSenderAddress = new Address("Узбекистан", "Ташкент", "Ташкент", "Юнусабад-8");
        final Address navruzRecipientAddress = new Address("Узбекистан", "Республика Каракалпакстан", "Нукус", "Пустыня 1");
        final Address navruzPayerAddress = new Address("Узбекистан", "Ташкент", "Ташкент", "Юнусабад-8");
        navruzSenderAddress.setZip("100190");
        navruzRecipientAddress.setZip("110160");
        navruzPayerAddress.setZip("100190");
        final Receipt navruzReceipt = new Receipt("Сергей", "Львович", "Ким",
                navruzSenderAddress, "+998990116824",
                TransportationStatus.ON_THE_WAY, PaymentStatus.PAID);
        navruzReceipt.setServiceProvider("CargoStar");
        navruzReceipt.setOperatorId("1234567890");
        navruzReceipt.setSenderSignature("sergey_signature.png");
        navruzReceipt.setRecipientSignature("rustam_signature.png");
        navruzReceipt.setSenderEmail("mikyegresl@gmail.com");
        navruzReceipt.setRecipientAddress(navruzRecipientAddress);
        navruzReceipt.setRecipientFirstName("Наурыс");
        navruzReceipt.setRecipientMiddleName("Жандуллаевич");
        navruzReceipt.setRecipientLastName("Жандуллаев");
        navruzReceipt.setRecipientPhone("+998912552255");
        navruzReceipt.setRecipientEmail("jason_stacy@gmail.com");
        navruzReceipt.setPayerAddress(androidPayerAddress);
        navruzReceipt.setPayerFirstName("Сергей");
        navruzReceipt.setPayerMiddleName("Львович");
        navruzReceipt.setPayerLastName("Ким");
        navruzReceipt.setPayerPhone("+998990116824");
        navruzReceipt.setPayerEmail("mikyegresl@gmail.com");
        navruzReceipt.setTrackingCode("777888999");
        navruzReceipt.setQr("787989090");
        navruzReceipt.setTariff("Express");
        navruzReceipt.setCost(1200000);
        navruzReceipt.setFuelCharge(10);
        navruzReceipt.setVat(15);
        navruzReceipt.setDispatchDate(new Date());
        navruzReceipt.setArrivalDate(new Date());
        navruzReceipt.setReceipt(new Document("Накладная", "android/com.example.cargostar.receipt.doc"));
        navruzReceipt.setInvoice(new Document("Инвойс", "android/com.example.cargostar.invoice.doc"));
        navruzReceipt.setCourierId(1);
        navruzReceipt.setCurrentLocation(transitPointIds[0]);
        navruzReceipt.setRead(true);

        final long navruzParcelId = populateViewModel.createRequest(navruzReceipt);
        populateViewModel.createParcelTransitPointCrossRef(new ReceiptTransitPointCrossRef(navruzParcelId, transitPointIds[0]));
        populateViewModel.createParcelTransitPointCrossRef(new ReceiptTransitPointCrossRef(navruzParcelId, transitPointIds[1]));
        populateViewModel.createParcelTransitPointCrossRef(new ReceiptTransitPointCrossRef(navruzParcelId, transitPointIds[8]));
        populateViewModel.createParcelTransitPointCrossRef(new ReceiptTransitPointCrossRef(navruzParcelId, transitPointIds[12]));

        final List<Cargo> navruzCargoList = new ArrayList<>();
        navruzCargoList.add(new Cargo("Твердый груз", "Паллета", 450, 380, 70, 40, "2345678901"));
        navruzCargoList.add(new Cargo("Детали для сбора механизмов", "Паллета", 450, 380, 70, 25, "3456789012"));
        navruzCargoList.add(new Cargo("Стройматериалы", "Паллета", 450, 380, 70, 120, "4567890123"));

        for (final Cargo cargo : navruzCargoList) {
            cargo.setReceiptId(navruzParcelId);
        }
        populateViewModel.createCargo(navruzCargoList);
        //sergey parcel
        final Address sergeySenderAddress = new Address("Узбекистан", "Ташкент", "Ташкент", "Юнусабад-8");
        final Address sergeyRecipientAddress = new Address("Узбекистан", "Ташкент", "Ташкент", "Карасу-6");
        final Address sergeyPayerAddress = new Address("Узбекистан", "Ташкент", "Ташкент", "Юнусабад-8");
        sergeySenderAddress.setZip("100190");
        sergeyRecipientAddress.setZip("100180");
        sergeyPayerAddress.setZip("100190");
        final Receipt sergeyReceipt = new Receipt("Сергей", "Львович", "Ким",
                sergeySenderAddress, "+998990116824",
                TransportationStatus.DELIVERED, PaymentStatus.WAITING_CHECK);
        sergeyReceipt.setServiceProvider("CargoStar");
        sergeyReceipt.setOperatorId("1234567890");
        sergeyReceipt.setSenderSignature("sergey_signature.png");
        sergeyReceipt.setRecipientSignature("rustam_signature.png");
        sergeyReceipt.setSenderEmail("mikyegresl@gmail.com");
        sergeyReceipt.setRecipientAddress(sergeyRecipientAddress);
        sergeyReceipt.setRecipientFirstName("Сергей");
        sergeyReceipt.setRecipientMiddleName("Игоревич");
        sergeyReceipt.setRecipientLastName("Бя");
        sergeyReceipt.setRecipientPhone("+9989098676788");
        sergeyReceipt.setRecipientEmail("the_mighty_dwarf@gmail.com");
        sergeyReceipt.setPayerAddress(androidPayerAddress);
        sergeyReceipt.setPayerFirstName("Сергей");
        sergeyReceipt.setPayerMiddleName("Львович");
        sergeyReceipt.setPayerLastName("Ким");
        sergeyReceipt.setPayerPhone("+998990116824");
        sergeyReceipt.setPayerEmail("mikyegresl@gmail.com");
        sergeyReceipt.setTrackingCode("777888991");
        sergeyReceipt.setQr("787989091");
        sergeyReceipt.setTariff("Express");
        sergeyReceipt.setCost(1200000);
        sergeyReceipt.setFuelCharge(10);
        sergeyReceipt.setVat(15);
        sergeyReceipt.setDispatchDate(new Date());
        sergeyReceipt.setArrivalDate(new Date());
        sergeyReceipt.setReceipt(new Document("Накладная", "android/com.example.cargostar.receipt.doc"));
        sergeyReceipt.setInvoice(new Document("Инвойс", "android/com.example.cargostar.invoice.doc"));
        sergeyReceipt.setCourierId(1);
        sergeyReceipt.setCurrentLocation(transitPointIds[0]);
        sergeyReceipt.setRead(true);

        final long sergeyParcelId = populateViewModel.createRequest(sergeyReceipt);
        populateViewModel.createParcelTransitPointCrossRef(new ReceiptTransitPointCrossRef(sergeyParcelId, transitPointIds[0]));

        final List<Cargo> sergeyCargoList = new ArrayList<>();
        sergeyCargoList.add(new Cargo("Твердый груз", "Паллета", 450, 380, 70, 40, "2345678904"));
        sergeyCargoList.add(new Cargo("Детали для сбора механизмов", "Паллета", 450, 380, 70, 25, "3456789014"));
        sergeyCargoList.add(new Cargo("Стройматериалы", "Паллета", 450, 380, 70, 120, "4567890125"));

        for (final Cargo cargo : sergeyCargoList) {
            cargo.setReceiptId(sergeyParcelId);
        }
        populateViewModel.createCargo(sergeyCargoList);
        //crosby parcel
        final Address crosbySenderAddress = new Address("Узбекистан", "Ташкент", "Ташкент", "Юнусабад-8");
        final Address crosbyRecipientAddress = new Address("Россия", "Москва", "Москва", "Пингвинс 123");
        final Address crosbyPayerAddress = new Address("Узбекистан", "Ташкент", "Ташкент", "Юнусабад-8");
        crosbySenderAddress.setZip("100190");
        crosbyRecipientAddress.setZip("9807345");
        crosbyPayerAddress.setZip("100190");
        final Receipt crosbyReceipt = new Receipt("Сергей", "Львович", "Ким",
                crosbySenderAddress, "+998990116824",
                TransportationStatus.LOST, PaymentStatus.PAID_PARTIALLY);
        crosbyReceipt.setServiceProvider("Fedex");
        crosbyReceipt.setOperatorId("1234567890");
        crosbyReceipt.setSenderSignature("sergey_signature.png");
        crosbyReceipt.setRecipientSignature("rustam_signature.png");
        crosbyReceipt.setSenderEmail("mikyegresl@gmail.com");
        crosbyReceipt.setRecipientAddress(crosbyRecipientAddress);
        crosbyReceipt.setRecipientFirstName("Сидни");
        crosbyReceipt.setRecipientMiddleName("");
        crosbyReceipt.setRecipientLastName("Кросби");
        crosbyReceipt.setRecipientPhone("+1456876980");
        crosbyReceipt.setRecipientEmail("sid_the_kid@gmail.com");
        crosbyReceipt.setPayerAddress(androidPayerAddress);
        crosbyReceipt.setPayerFirstName("Сергей");
        crosbyReceipt.setPayerMiddleName("Львович");
        crosbyReceipt.setPayerLastName("Ким");
        crosbyReceipt.setPayerPhone("+998990116824");
        crosbyReceipt.setPayerEmail("mikyegresl@gmail.com");
        crosbyReceipt.setTrackingCode("777888992");
        crosbyReceipt.setQr("787989092");
        crosbyReceipt.setTariff("Express");
        crosbyReceipt.setCost(5600000);
        crosbyReceipt.setFuelCharge(40);
        crosbyReceipt.setVat(25);
        crosbyReceipt.setDispatchDate(new Date());
        crosbyReceipt.setArrivalDate(new Date());
        crosbyReceipt.setReceipt(new Document("Накладная", "android/com.example.cargostar.receipt.doc"));
        crosbyReceipt.setInvoice(new Document("Инвойс", "android/com.example.cargostar.invoice.doc"));
        crosbyReceipt.setCourierId(1);
        crosbyReceipt.setCurrentLocation(transitPointIds[0]);
        crosbyReceipt.setRead(true);

        final long crosbyParcelId = populateViewModel.createRequest(crosbyReceipt);
        populateViewModel.createParcelTransitPointCrossRef(new ReceiptTransitPointCrossRef(crosbyParcelId, transitPointIds[0]));
        populateViewModel.createParcelTransitPointCrossRef(new ReceiptTransitPointCrossRef(crosbyParcelId, transitPointIds[2]));
        populateViewModel.createParcelTransitPointCrossRef(new ReceiptTransitPointCrossRef(crosbyParcelId, transitPointIds[7]));
        populateViewModel.createParcelTransitPointCrossRef(new ReceiptTransitPointCrossRef(crosbyParcelId, transitPointIds[13]));
        populateViewModel.createParcelTransitPointCrossRef(new ReceiptTransitPointCrossRef(crosbyParcelId, transitPointIds[14]));
        populateViewModel.createParcelTransitPointCrossRef(new ReceiptTransitPointCrossRef(crosbyParcelId, transitPointIds[15]));

        final List<Cargo> crosbyCargoList = new ArrayList<>();
        crosbyCargoList.add(new Cargo("Твердый груз", "Паллета", 450, 380, 70, 40, "2345678905"));
        crosbyCargoList.add(new Cargo("Детали для сбора механизмов", "Паллета", 450, 380, 70, 25, "3456789015"));
        crosbyCargoList.add(new Cargo("Стройматериалы", "Паллета", 450, 380, 70, 120, "4567890126"));

        for (final Cargo cargo : crosbyCargoList) {
            cargo.setReceiptId(crosbyParcelId);
        }
        populateViewModel.createCargo(crosbyCargoList);
    }

    private void initConsolidations(final long[] transitPointIds) {
        final Consolidation consolidation = new Consolidation("9901116824");
        final long consolidationId = populateViewModel.createConsolidation(consolidation);
        //mikye parcel for consolidation
        final Address mikyeSenderAddress = new Address("Узбекистан", "Наманганская Область", "Наманган", "Юнусабад-8");
        final Address rustamRecipientAddress = new Address("Узбекистан", " Андижанская Область", "Андижан", "Улица Пустырь 1");
        final Address rustamPayerAddress = new Address("Узбекистан", "Андижанская Область", "Андижан", "Улица Пустырь 1");
        mikyeSenderAddress.setZip("100190");
        rustamRecipientAddress.setZip("110120");
        rustamPayerAddress.setZip("110120");
        final Receipt mikyeReceipt = new Receipt("Сергей", "Львович", "Ким",
                mikyeSenderAddress, "+998990116824",
                TransportationStatus.IN_TRANSIT, PaymentStatus.WAITING_PAYMENT);
        mikyeReceipt.setServiceProvider("CargoStar");
        mikyeReceipt.setOperatorId("1234567890");
        mikyeReceipt.setSenderSignature("sergey_signature.png");
        mikyeReceipt.setRecipientSignature("rustam_signature.png");
        mikyeReceipt.setSenderEmail("mikyegresl@gmail.com");
        mikyeReceipt.setRecipientAddress(rustamRecipientAddress);
        mikyeReceipt.setRecipientFirstName("Рустам");
        mikyeReceipt.setRecipientMiddleName("Таджибаевич");
        mikyeReceipt.setRecipientLastName("Таджиев");
        mikyeReceipt.setRecipientPhone("+998909876543");
        mikyeReceipt.setRecipientEmail("rustico_bad_carma@gmail.com");
        mikyeReceipt.setPayerAddress(rustamPayerAddress);
        mikyeReceipt.setPayerFirstName("Сергей");
        mikyeReceipt.setPayerMiddleName("Львович");
        mikyeReceipt.setPayerLastName("Ким");
        mikyeReceipt.setPayerPhone("+998990116824");
        mikyeReceipt.setPayerEmail("mikyegresl@gmail.com");
        mikyeReceipt.setTrackingCode("231564897");
        mikyeReceipt.setQr("321654987");
        mikyeReceipt.setTariff("Express");
        mikyeReceipt.setCost(2400000);
        mikyeReceipt.setFuelCharge(10);
        mikyeReceipt.setVat(15);
        mikyeReceipt.setDispatchDate(new Date());
        mikyeReceipt.setArrivalDate(new Date());
        mikyeReceipt.setReceipt(new Document("Накладная", "android/com.example.cargostar.receipt.doc"));
        mikyeReceipt.setInvoice(new Document("Инвойс", "android/com.example.cargostar.invoice.doc"));
        mikyeReceipt.setCourierId(1);
        mikyeReceipt.setCurrentLocation(transitPointIds[6]);
        mikyeReceipt.setRead(true);
        mikyeReceipt.setConsolidationNumber(consolidationId);
        final long mikyeParcelId = populateViewModel.createRequest(mikyeReceipt);
        populateViewModel.createParcelTransitPointCrossRef(new ReceiptTransitPointCrossRef(mikyeParcelId, transitPointIds[6]));

        final List<Cargo> mikyeCargoList = new ArrayList<>();
        mikyeCargoList.add(new Cargo("Твердый груз", "Паллета", 450, 380, 70, 40, "1234567890"));
        mikyeCargoList.add(new Cargo("Детали для сбора механизмов", "Паллета", 450, 380, 70, 25, "1234567899"));
        mikyeCargoList.add(new Cargo("Стройматериалы", "Паллета", 450, 380, 70, 120, "1234567898"));

        for (final Cargo cargo : mikyeCargoList) {
            cargo.setReceiptId(mikyeParcelId);
        }
        populateViewModel.createCargo(mikyeCargoList);
        populateViewModel.createParcelTransitPointCrossRef(new ReceiptTransitPointCrossRef(mikyeParcelId, transitPointIds[1]));
        //dud parcel for consolidation
        final Address dudSenderAddress = new Address("Россия", "Москва", "Москва", "Ул. Ленина 6");
        final Address navruzRecipientAddress = new Address("Узбекистан", "Республика Каракалпакстан", "Нукус", "Пустыня 1");
        final Address navruzPayerAddress = new Address("Узбекистан", "Республика Каракалпакстан", "Нукус", "Пустыня 1");
        dudSenderAddress.setZip("700100");
        navruzRecipientAddress.setZip("110160");
        navruzPayerAddress.setZip("100190");
        final Receipt dudReceipt = new Receipt("Юрий", "Александрович", "Дудь",
                dudSenderAddress, "+79990116824",
                TransportationStatus.ON_THE_WAY, PaymentStatus.PAID);
        dudReceipt.setServiceProvider("CargoStar");
        dudReceipt.setOperatorId("1234567890");
        dudReceipt.setSenderSignature("dud_signature.png");
        dudReceipt.setRecipientSignature("jason_signatyre.png");
        dudReceipt.setSenderEmail("vdud@gmail.com");
        dudReceipt.setRecipientAddress(navruzRecipientAddress);
        dudReceipt.setRecipientFirstName("Наурыс");
        dudReceipt.setRecipientMiddleName("Жандуллаевич");
        dudReceipt.setRecipientLastName("Жандуллаев");
        dudReceipt.setRecipientPhone("+998912552255");
        dudReceipt.setRecipientEmail("jason_stacy@gmail.com");
        dudReceipt.setPayerAddress(navruzPayerAddress);
        dudReceipt.setPayerFirstName("Наурыс");
        dudReceipt.setPayerMiddleName("Жандуллаевич");
        dudReceipt.setPayerLastName("Жандуллаев");
        dudReceipt.setPayerPhone("+998912552255");
        dudReceipt.setPayerEmail("jason_stacy@gmail.com");
        dudReceipt.setTrackingCode("951374682");
        dudReceipt.setQr("1832465927");
        dudReceipt.setCurrentLocation(transitPointIds[15]);
        dudReceipt.setTariff("Express");
        dudReceipt.setCost(1200000);
        dudReceipt.setFuelCharge(10);
        dudReceipt.setVat(15);
        dudReceipt.setDispatchDate(new Date());
        dudReceipt.setArrivalDate(new Date());
        dudReceipt.setReceipt(new Document("Накладная", "android/com.example.cargostar.receipt.doc"));
        dudReceipt.setInvoice(new Document("Инвойс", "android/com.example.cargostar.invoice.doc"));
        dudReceipt.setCourierId(1);
        dudReceipt.setRead(true);
        dudReceipt.setConsolidationNumber(consolidationId);

        final long dudParcelId = populateViewModel.createRequest(dudReceipt);
        populateViewModel.createParcelTransitPointCrossRef(new ReceiptTransitPointCrossRef(dudParcelId, transitPointIds[15]));
        final List<Cargo> dudCargoList = new ArrayList<>();
        dudCargoList.add(new Cargo("Твердый груз", "Паллета", 450, 380, 70, 40, "2345678901"));
        dudCargoList.add(new Cargo("Детали для сбора механизмов", "Паллета", 450, 380, 70, 25, "3456789012"));
        dudCargoList.add(new Cargo("Стройматериалы", "Паллета", 450, 380, 70, 120, "4567890123"));

        for (final Cargo cargo : dudCargoList) {
            cargo.setReceiptId(dudParcelId);
        }
        populateViewModel.createCargo(dudCargoList);
        populateViewModel.createParcelTransitPointCrossRef(new ReceiptTransitPointCrossRef(dudParcelId, transitPointIds[12]));
    }

    private void initUI() {
        loginEditText = findViewById(R.id.login_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        signInBtn = findViewById(R.id.sign_in_btn);
        keepLoggingCheckBox = findViewById(R.id.keep_logging_check_box);
        forgotPasswordTextView = findViewById(R.id.forgot_password_text_view);
        passwordEyeImageView = findViewById(R.id.password_eye_image_view);

        loginEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(loginEditText, hasFocus);
        });

        passwordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(passwordEditText, hasFocus);
        });

        passwordEyeImageView.setOnClickListener(v -> {
            Log.i(TAG, "passwordEye clicked()" + showPassword);
            if (showPassword) {
                passwordEyeImageView.setImageResource(R.drawable.ic_hide_password);
                passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                showPassword = false;
            }
            else {
                passwordEyeImageView.setImageResource(R.drawable.ic_show_password);
                passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                showPassword = true;
            }
        });
    }
}