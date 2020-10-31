package uz.alexits.cargostar.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;

import uz.alexits.cargostar.R;

import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.utils.ImageSerializer;
import uz.alexits.cargostar.utils.Regex;
import uz.alexits.cargostar.viewmodel.CustomerViewModel;
import uz.alexits.cargostar.viewmodel.CourierViewModel;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.utils.UiUtils;
import uz.alexits.cargostar.viewmodel.LocationDataViewModel;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class CreateUserActivity extends AppCompatActivity {
    private static final String TAG = CreateUserActivity.class.toString();
    private Context context;
    //header views
    private TextView fullNameTextView;
    private TextView branchTextView;
    private EditText parcelSearchEditText;
    private ImageView parcelSearchImageView;
    private ImageView editImageView;
    private ImageView profileImageView;
    private ImageView notificationsImageView;
    private ImageView calculatorImageView;
    private TextView badgeCounterTextView;
    //user data
    private EditText passwordEditText;
    private EditText emailEditText;
    //client data
    private EditText cargostarAccountNumberEditText;
    private EditText tntAccountNumberEditText;
    private EditText fedexAccountNumberEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText middleNameEditText;
    private EditText phoneEditText;
    private EditText addressEditText;
    private EditText geolocationEditText;

    //country spinner item
    private ArrayAdapter<Country> countryArrayAdapter;
    private Spinner countrySpinner;
    private RelativeLayout countryField;

    //city spinner item
    private ArrayAdapter<Region> regionArrayAdapter;
    private RelativeLayout regionField;
    private Spinner regionSpinner;

    //city spinner item
    private ArrayAdapter<City> cityArrayAdapter;
    private RelativeLayout cityField;
    private Spinner citySpinner;

    private EditText zipEditText;
    private EditText photoEditText;
    private EditText discountEditText;
    private EditText signatureEditText;
    //payment data
    private TextView passportSerialTextView;
    private EditText passportSerialEditText;
    private TextView innTextView;
    private EditText innEditText;
    private TextView companyTextView;
    private EditText companyEditText;
    private TextView checkingAccountTextView;
    private EditText checkingAccountEditText;
    private TextView bankTextView;
    private EditText bankEditText;
    private TextView vatTextView;
    private EditText vatEditText;
    private TextView mfoTextView;
    private EditText mfoEditText;
    private TextView okedTextView;
    private EditText okedEditText;
    private TextView contractTextView;
    private EditText contractEditText;
    private Button createBtn;

    private ImageView photoImageView;
    private ImageView photoResultImageView;
    private ImageView signatureImageView;
    private ImageView signatureResultImageView;
    private ImageView contractImageView;
    private ImageView contractResultImageView;

    private ProgressBar progressBar;

    //ViewModel
    private CourierViewModel courierViewModel;
    private CustomerViewModel customerViewModel;
    private LocationDataViewModel locationDataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        initUI();

        context = this;

        //header view model
        courierViewModel = new ViewModelProvider(this).get(CourierViewModel.class);
        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);

        courierViewModel.selectCourierByLogin(SharedPrefs.getInstance(context).getString(SharedPrefs.LOGIN)).observe(this, courier -> {
            if (courier != null) {
                fullNameTextView.setText(courier.getFirstName() + " " + courier.getLastName());
            }
        });
        courierViewModel.selectBrancheById(SharedPrefs.getInstance(context).getLong(SharedPrefs.BRANCH_ID)).observe(this, branch -> {
            if (branch != null) {
                branchTextView.setText(getString(R.string.branch) + " \"" + branch.getName() + "\"");
            }
        });
        courierViewModel.selectNewNotificationsCount().observe(this, newNotificationsCount -> {
            if (newNotificationsCount != null) {
                badgeCounterTextView.setText(String.valueOf(newNotificationsCount));
            }
        });

        //location data view model
        locationDataViewModel = new ViewModelProvider(this).get(LocationDataViewModel.class);

        locationDataViewModel.getCountryList().observe(this, countryList -> {
            for (final Country country : countryList) {
                countryArrayAdapter.add(country);
            }
            countryArrayAdapter.notifyDataSetChanged();
        });

        locationDataViewModel.getRegionList().observe(this, regionList -> {
            for (final Region region : regionList) {
                regionArrayAdapter.add(region);
            }
            regionArrayAdapter.notifyDataSetChanged();
        });

        locationDataViewModel.getCityList().observe(this, cityList -> {
            for (final City city : cityList) {
                cityArrayAdapter.add(city);
            }
            cityArrayAdapter.notifyDataSetChanged();
        });

        //listeners
        parcelSearchImageView.setOnClickListener(v -> {
            final String parcelIdStr = parcelSearchEditText.getText().toString();
            if (TextUtils.isEmpty(parcelIdStr)) {
                Toast.makeText(context, "Введите ID перевозки или номер накладной", Toast.LENGTH_SHORT).show();
                return;
            }

            final long parcelId = Long.parseLong(parcelIdStr);

            courierViewModel.selectRequest(parcelId).observe(this, receiptWithCargoList -> {
                if (receiptWithCargoList == null) {
                    Toast.makeText(context, "Накладной не существует", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (receiptWithCargoList.getReceipt() == null) {
//                    Toast.makeText(context, "Накладной не существует", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                final Intent mainIntent = new Intent(context, MainActivity.class);
                mainIntent.putExtra(IntentConstants.INTENT_REQUEST_KEY, IntentConstants.REQUEST_FIND_PARCEL);
                mainIntent.putExtra(IntentConstants.INTENT_REQUEST_VALUE, parcelId);
                startActivity(mainIntent);
            });
        });
        //listeners
        photoImageView.setOnClickListener(v -> {
            final Intent openGallery = new Intent();
            openGallery.setType("image/*");
            openGallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(openGallery, "Выберите фото"), IntentConstants.REQUEST_UPLOAD_PHOTO);
        });

        signatureImageView.setOnClickListener(v -> {
            startActivityForResult(new Intent(context, SignatureActivity.class), IntentConstants.REQUEST_SENDER_SIGNATURE);
        });

        contractImageView.setOnClickListener(v -> {
            final Intent openFileManager = new Intent();
            openFileManager.addCategory(Intent.CATEGORY_OPENABLE);
            openFileManager.setType("*/*");
            openFileManager.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(openFileManager, "Выберите файл"), IntentConstants.REQUEST_UPLOAD_PHOTO);
        });

        createBtn.setOnClickListener(v -> {
            final String password = passwordEditText.getText().toString();
            final String email = emailEditText.getText().toString();

            final String firstName = firstNameEditText.getText().toString();
            final String middleName = middleNameEditText.getText().toString();
            final String lastName = lastNameEditText.getText().toString();
            final String phone = phoneEditText.getText().toString();

            final String cargostarAccountNumber = cargostarAccountNumberEditText.getText().toString();
            final String tntAccountNumber = tntAccountNumberEditText.getText().toString();
            final String fedexAccountNumber = fedexAccountNumberEditText.getText().toString();

            final Country country = (Country) countrySpinner.getSelectedItem();
            final Region region = (Region) regionSpinner.getSelectedItem();
            final City city = (City) citySpinner.getSelectedItem();

            final String zip = zipEditText.getText().toString();
            final String address = addressEditText.getText().toString();
            final String geolocation = geolocationEditText.getText().toString();

            final String photo = photoEditText.getText().toString();
            final String discount = discountEditText.getText().toString();
            final String signature = signatureEditText.getText().toString();

            //PassportData
            final String passportSerial = passportSerialEditText.getText().toString();
            //PaymentData
            final String inn = innEditText.getText().toString();
            final String company = companyEditText.getText().toString();
            final String checkingAccount = checkingAccountEditText.getText().toString();
            final String bank = bankEditText.getText().toString();
            final String vat = vatEditText.getText().toString();
            final String mfo = mfoEditText.getText().toString();
            final String oked = okedEditText.getText().toString();
            final String contract = contractEditText.getText().toString();

            /* check for empty fields */
            if (password.length() < 6) {
                Toast.makeText(context, "Пароль должен сожержать минимум 6 символов", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(middleName) || TextUtils.isEmpty(lastName)) {
                Toast.makeText(context, "Имя не указано или указано не полностью", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(context, "Номер телефона не указан", Toast.LENGTH_SHORT).show();
                return;
            }
            if (country == null) {
                Toast.makeText(context, "Страна не указана", Toast.LENGTH_SHORT).show();
                return;
            }
            if (region == null) {
                Toast.makeText(context, "Регион/территория не указаны", Toast.LENGTH_SHORT).show();
                return;
            }
            if (city == null) {
                Toast.makeText(context, "Город не указан", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(address)) {
                Toast.makeText(context, "Адрес не указан", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(cargostarAccountNumber)) {
                Toast.makeText(context, "Укажите номер аккаунта CargoStar", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(passportSerial) && TextUtils.isEmpty(inn) && TextUtils.isEmpty(company)) {
                Toast.makeText(context, "Заполните физ. или юр. данные", Toast.LENGTH_SHORT).show();
                return;
            }

            /* check for regular expressions */
            if (!Regex.isEmail(email)) {
                Toast.makeText(context, "Email указан неверно", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Regex.isName(firstName)) {
                Toast.makeText(context, "Имя указано неверно", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Regex.isName(middleName)) {
                Toast.makeText(context, "Отчество указано неверно", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Regex.isName(lastName)) {
                Toast.makeText(context, "Фамилия указана неверно", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Regex.isPhoneNumber(phone)) {
                Toast.makeText(context, "Номер телефона указан неверно", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Regex.isAccountNumber(cargostarAccountNumber)) {
                Toast.makeText(context, "Номер аккаунта CargoStar указан неверно", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(tntAccountNumber)) {
                if (!Regex.isAccountNumber(tntAccountNumber)) {
                Toast.makeText(context, "Номер аккаунта TNT указан неверно", Toast.LENGTH_SHORT).show();
                return;
            }
            }
            if (!TextUtils.isEmpty(fedexAccountNumber)) {
                if (!Regex.isAccountNumber(fedexAccountNumber)) {
                Toast.makeText(context, "Номер аккаунта Fedex указан неверно", Toast.LENGTH_SHORT).show();
                return;
            }
            }
            if (!TextUtils.isEmpty(zip)) {
                if (!Regex.isZip(zip)) {
                Toast.makeText(context, "Почтовый индекс должен содержать только цифры", Toast.LENGTH_SHORT).show();
                return;
            }
            }

            double discountDouble = 0;

            if (!TextUtils.isEmpty(discount)) {
                if (!Regex.isFloatOrInt(discount)) {
                    Toast.makeText(context, "Скидка указана неверно", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    discountDouble = Double.parseDouble(discount);
                }
                catch (NumberFormatException e) {
                    Log.e(TAG, "createUser(): ", e);
                }
            }
            if (TextUtils.isEmpty(passportSerial) && (TextUtils.isEmpty(inn) || TextUtils.isEmpty(company))) {
                Toast.makeText(context, "Для юр. лица укажите ИНН и название компании", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(inn)) {
                if (!Regex.isAccountNumber(inn)) {
                    Toast.makeText(context, "ИНН указан неверно", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
//            if (checkingAccount) {
//
//            }
//            if (bank) {
//
//            }
//            if (mfo) {
//
//            }
//            if (oked) {
//
//            }
//            if (vat) {
//
//            }

            String photoBytesStr = null;

            if (!TextUtils.isEmpty(photo)) {
                photoBytesStr = ImageSerializer.bitmapToBase64(context, photo);
            }

            String signatureBytesStr = null;

            if (!TextUtils.isEmpty(signature)) {
                signatureBytesStr = ImageSerializer.fileToBase64(signature);
            }
            if (!TextUtils.isEmpty(geolocation)) {
                if (!Regex.isGeolocation(geolocation)) {
                    Toast.makeText(context, "Геолокация должна быть указана в формате ХХ.ХХ", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            int userType = 0;

            if (!TextUtils.isEmpty(passportSerial)) {
                userType = 1;
            }
            else {
                userType = 2;
            }

            final UUID createUserWorkerId = SyncWorkRequest.createUser(
                    this,
                    email,
                    password,
                    cargostarAccountNumber,
                    tntAccountNumber,
                    fedexAccountNumber,
                    firstName,
                    middleName,
                    lastName,
                    phone,
                    country.getNameEn(),
                    region.getNameEn(),
                    city.getNameEn(),
                    address,
                    geolocation,
                    zip,
                    discountDouble,
                    userType,
                    passportSerial,
                    inn,
                    company,
                    bank,
                    mfo,
                    oked,
                    checkingAccount,
                    vat,
                    photoBytesStr,
                    signatureBytesStr);
            WorkManager.getInstance(this).getWorkInfoByIdLiveData(createUserWorkerId).observe(this, workInfo -> {
                if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                    progressBar.setVisibility(View.INVISIBLE);
                    createBtn.setEnabled(true);
                    Toast.makeText(context, "Пользователь " + email + " был успешно создан!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(context, MainActivity.class));
                    finish();
                    return;
                }
                if (workInfo.getState() == WorkInfo.State.FAILED || workInfo.getState() == WorkInfo.State.CANCELLED) {
                    progressBar.setVisibility(View.INVISIBLE);
                    createBtn.setEnabled(true);
                    Log.e(TAG, "createUser(): failed to create user");
                    Toast.makeText(this, "Ошибка создания пользователя", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                createBtn.setEnabled(false);
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == IntentConstants.REQUEST_UPLOAD_PHOTO) {
                final Uri selectedImage = data.getData();
                if (selectedImage != null) {
                    photoResultImageView.setImageResource(R.drawable.ic_image_green);
                    photoResultImageView.setVisibility(View.VISIBLE);
                    photoEditText.setBackgroundResource(R.drawable.edit_text_active);

                    Log.i(TAG, "selected photo: " + selectedImage);
                    photoEditText.setText(selectedImage.toString());

                    return;
                }
            }
            if (requestCode == IntentConstants.REQUEST_UPLOAD_DOCUMENT) {
                final Uri selectedDoc = data.getData();
                if (selectedDoc != null) {
                    contractResultImageView.setImageResource(R.drawable.ic_doc_green);
                    contractResultImageView.setVisibility(View.VISIBLE);
                    contractEditText.setBackgroundResource(R.drawable.edit_text_active);
                    return;
                }
            }
            if (requestCode == IntentConstants.REQUEST_SENDER_SIGNATURE) {
                signatureResultImageView.setImageResource(R.drawable.ic_image_green);
                signatureResultImageView.setVisibility(View.VISIBLE);
                signatureEditText.setBackgroundResource(R.drawable.edit_text_active);

                final String signatureFilePath = data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE);

                Log.i(TAG, "signature filepath: " + signatureFilePath);
                signatureEditText.setText(signatureFilePath);
            }
        }
    }

    private void initUI() {
        //search data
        fullNameTextView = findViewById(R.id.full_name_text_view);
        branchTextView = findViewById(R.id.branch_text_view);
        parcelSearchEditText = findViewById(R.id.search_edit_text);
        parcelSearchImageView = findViewById(R.id.search_btn);
        editImageView = findViewById(R.id.edit_image_view);
        profileImageView = findViewById(R.id.profile_image_view);
        notificationsImageView = findViewById(R.id.notifications_image_view);
        calculatorImageView = findViewById(R.id.calculator_image_view);
        badgeCounterTextView = findViewById(R.id.badge_counter_text_view);
        //user data
        passwordEditText = findViewById(R.id.password_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        //client data
        cargostarAccountNumberEditText = findViewById(R.id.cargostar_account_number_edit_text);
        tntAccountNumberEditText = findViewById(R.id.tnt_account_number_edit_text);
        fedexAccountNumberEditText = findViewById(R.id.fedex_account_number_edit_text);
        firstNameEditText = findViewById(R.id.first_name_edit_text);
        lastNameEditText = findViewById(R.id.last_name_edit_text);
        middleNameEditText = findViewById(R.id.middle_name_edit_text);
        phoneEditText = findViewById(R.id.phone_number_edit_text);
        addressEditText = findViewById(R.id.address_edit_text);
        geolocationEditText = findViewById(R.id.geolocation_edit_text);

        //country, region, city
        countryField = findViewById(R.id.country_field);
        regionField = findViewById(R.id.region_field);
        cityField = findViewById(R.id.city_field);

        countrySpinner = findViewById(R.id.country_spinner);
        regionSpinner = findViewById(R.id.region_spinner);
        citySpinner = findViewById(R.id.city_spinner);

        progressBar = findViewById(R.id.progress_bar);

        countryArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countryArrayAdapter);

        regionArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        regionArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionSpinner.setAdapter(regionArrayAdapter);

        cityArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new ArrayList<>());
        cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityArrayAdapter);

        zipEditText = findViewById(R.id.zip_edit_text);
        photoEditText = findViewById(R.id.photo_edit_text);
        discountEditText = findViewById(R.id.discount_edit_text);
        signatureEditText = findViewById(R.id.signature_edit_text);
        //payment data
        passportSerialTextView = findViewById(R.id.passport_serial_text_view);
        passportSerialEditText = findViewById(R.id.passport_serial_edit_text);
        innTextView = findViewById(R.id.inn_text_view);
        innEditText = findViewById(R.id.inn_edit_text);
        companyTextView = findViewById(R.id.company_text_view);
        companyEditText = findViewById(R.id.company_edit_text);
        checkingAccountTextView = findViewById(R.id.checking_account_text_view);
        checkingAccountEditText = findViewById(R.id.checking_account_edit_text);
        bankTextView = findViewById(R.id.bank_text_view);
        bankEditText = findViewById(R.id.bank_edit_text);
        vatTextView = findViewById(R.id.vat_text_view);
        vatEditText = findViewById(R.id.vat_edit_text);
        mfoTextView = findViewById(R.id.mfo_text_view);
        mfoEditText = findViewById(R.id.mfo_edit_text);
        okedTextView = findViewById(R.id.oked_text_view);
        okedEditText = findViewById(R.id.oked_edit_text);
        contractTextView = findViewById(R.id.contract_text_view);
        contractEditText = findViewById(R.id.contract_edit_text);
        contractImageView = findViewById(R.id.contract_image_view);
        createBtn = findViewById(R.id.create_btn);

        photoImageView = findViewById(R.id.photo_image_view);
        photoResultImageView = findViewById(R.id.photo_result_image_view);
        signatureImageView = findViewById(R.id.signature_image_view);
        signatureResultImageView = findViewById(R.id.success_signature_image_view);
        contractImageView = findViewById(R.id.contract_image_view);
        contractResultImageView = findViewById(R.id.contract_success_image_view);

        passwordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(passwordEditText, hasFocus);
        });

        emailEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(emailEditText, hasFocus);
        });

        cargostarAccountNumberEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(cargostarAccountNumberEditText, hasFocus);
        });

        tntAccountNumberEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(tntAccountNumberEditText, hasFocus);
        });

        fedexAccountNumberEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(fedexAccountNumberEditText, hasFocus);
        });

        firstNameEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(firstNameEditText, hasFocus);
        });

        lastNameEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(lastNameEditText, hasFocus);
        });

        middleNameEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(middleNameEditText, hasFocus);
        });

        phoneEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(phoneEditText, hasFocus);
        });

        addressEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(addressEditText, hasFocus);
        });

        zipEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(zipEditText, hasFocus);
        });

        geolocationEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(geolocationEditText, hasFocus);
        });

        photoEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(photoEditText, hasFocus);
        });

        discountEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(discountEditText, hasFocus);
        });

        signatureEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(signatureEditText, hasFocus);
        });

        passportSerialEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(passportSerialEditText, hasFocus);
        });

        companyEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(companyEditText, hasFocus);
        });

        innEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(innEditText, hasFocus);
        });

        checkingAccountEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(checkingAccountEditText, hasFocus);
        });

        mfoEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(mfoEditText, hasFocus);
        });

        okedEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(okedEditText, hasFocus);
        });

        vatEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(vatEditText, hasFocus);
        });

        bankEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(bankEditText, hasFocus);
        });

        contractEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(contractEditText, hasFocus);
        });

        passportSerialEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.i(TAG, "passportSerial: " + editable.length());
                if (editable.length() > 0) {
                    innTextView.setVisibility(View.INVISIBLE);
                    innEditText.setVisibility(View.INVISIBLE);
                    companyTextView.setVisibility(View.INVISIBLE);
                    companyEditText.setVisibility(View.INVISIBLE);
                    checkingAccountTextView.setVisibility(View.INVISIBLE);
                    checkingAccountEditText.setVisibility(View.INVISIBLE);
                    bankTextView.setVisibility(View.INVISIBLE);
                    bankEditText.setVisibility(View.INVISIBLE);
                    vatTextView.setVisibility(View.INVISIBLE);
                    vatEditText.setVisibility(View.INVISIBLE);
                    mfoTextView.setVisibility(View.INVISIBLE);
                    mfoEditText.setVisibility(View.INVISIBLE);
                    okedTextView.setVisibility(View.INVISIBLE);
                    okedEditText.setVisibility(View.INVISIBLE);
                    contractTextView.setVisibility(View.INVISIBLE);
                    contractEditText.setVisibility(View.INVISIBLE);
                    contractImageView.setVisibility(View.INVISIBLE);
                    return;
                }
                innTextView.setVisibility(View.VISIBLE);
                innEditText.setVisibility(View.VISIBLE);
                companyTextView.setVisibility(View.VISIBLE);
                companyEditText.setVisibility(View.VISIBLE);
                checkingAccountTextView.setVisibility(View.VISIBLE);
                checkingAccountEditText.setVisibility(View.VISIBLE);
                bankTextView.setVisibility(View.VISIBLE);
                bankEditText.setVisibility(View.VISIBLE);
                vatTextView.setVisibility(View.VISIBLE);
                vatEditText.setVisibility(View.VISIBLE);
                mfoTextView.setVisibility(View.VISIBLE);
                mfoEditText.setVisibility(View.VISIBLE);
                okedTextView.setVisibility(View.VISIBLE);
                okedEditText.setVisibility(View.VISIBLE);
                contractTextView.setVisibility(View.VISIBLE);
                contractEditText.setVisibility(View.VISIBLE);
                contractImageView.setVisibility(View.VISIBLE);
            }
        });

        innEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    passportSerialTextView.setVisibility(View.INVISIBLE);
                    passportSerialEditText.setVisibility(View.INVISIBLE);
                    return;
                }
                if (TextUtils.isEmpty(companyEditText.getText())) {
                    passportSerialTextView.setVisibility(View.VISIBLE);
                    passportSerialEditText.setVisibility(View.VISIBLE);
                }
            }
        });

        companyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    passportSerialTextView.setVisibility(View.INVISIBLE);
                    passportSerialEditText.setVisibility(View.INVISIBLE);
                    return;
                }
                if (TextUtils.isEmpty(innEditText.getText())) {
                    passportSerialTextView.setVisibility(View.VISIBLE);
                    passportSerialEditText.setVisibility(View.VISIBLE);
                }
            }
        });
        editImageView.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });
        profileImageView.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });
        notificationsImageView.setOnClickListener(v -> {
            startActivity(new Intent(this, NotificationsActivity.class));
        });
        calculatorImageView.setOnClickListener(v -> {
            startActivity(new Intent(this, CalculatorActivity.class));
        });

        //country, region, city spinners
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final TextView itemTextView = (TextView) view;
                final Country selectedCountry = (Country) parent.getSelectedItem();

                if (itemTextView != null) {
                    if (position < parent.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        countryField.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
                final int selectedPosition = countrySpinner.getSelectedItemPosition();

                populateRegionAdapter(regionArrayAdapter, selectedCountry.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final TextView itemTextView = (TextView) view;
                final Region selectedRegion = (Region) parent.getSelectedItem();

                if (itemTextView != null) {
                    if (position < parent.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        regionField.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
                final int selectedPosition = regionSpinner.getSelectedItemPosition();

                populateCityAdapter(cityArrayAdapter, selectedRegion.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final TextView itemTextView = (TextView) view;
                final City selectedCity = (City) parent.getSelectedItem();

                if (itemTextView != null) {
                    if (position < parent.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        cityField.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
                final int selectedPosition = citySpinner.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void populateRegionAdapter(final ArrayAdapter<Region> arrayAdapter, final long countryId) {
        arrayAdapter.clear();

        locationDataViewModel.getRegionsByCountryId(countryId).observe(this, regionList -> {
            for (final Region region : regionList) {
                arrayAdapter.add(region);
            }
            arrayAdapter.notifyDataSetChanged();
        });
    }

    private void populateCityAdapter(final ArrayAdapter<City> arrayAdapter, final long regionId) {
        arrayAdapter.clear();

        locationDataViewModel.getCitiesByRegionId(regionId).observe(this, cityList -> {
            for (final City city : cityList) {
                arrayAdapter.add(city);
            }
            arrayAdapter.notifyDataSetChanged();
        });
    }
}