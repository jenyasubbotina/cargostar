package uz.alexits.cargostar.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import uz.alexits.cargostar.R;

import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.actor.Account;
import uz.alexits.cargostar.model.location.Address;
import uz.alexits.cargostar.model.location.Point;
import uz.alexits.cargostar.utils.Regex;
import uz.alexits.cargostar.viewmodel.CreateUserViewModel;
import uz.alexits.cargostar.viewmodel.HeaderViewModel;
import uz.alexits.cargostar.view.Constants;
import uz.alexits.cargostar.view.UiUtils;

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
    private EditText loginEditText;
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
    private EditText countryEditText;
    private EditText regionEditText;
    private EditText cityEditText;
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
    //ViewModel
    private HeaderViewModel headerViewModel;
    private CreateUserViewModel createUserViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        initUI();
        context = this;

        headerViewModel = new ViewModelProvider(this).get(HeaderViewModel.class);
        createUserViewModel = new ViewModelProvider(this).get(CreateUserViewModel.class);

        headerViewModel.selectCourierByLogin(SharedPrefs.getInstance(context).getString(SharedPrefs.LOGIN)).observe(this, courier -> {
            if (courier != null) {
                fullNameTextView.setText(courier.getFirstName() + " " + courier.getLastName());
            }
        });
        headerViewModel.selectBrancheById(SharedPrefs.getInstance(context).getLong(SharedPrefs.ID)).observe(this, branch -> {
            if (branch != null) {
                branchTextView.setText(getString(R.string.branch) + " \"" + branch.getName() + "\"");
            }
        });
        headerViewModel.selectNewNotificationsCount().observe(this, newNotificationsCount -> {
            if (newNotificationsCount != null) {
                badgeCounterTextView.setText(String.valueOf(newNotificationsCount));
            }
        });

        parcelSearchImageView.setOnClickListener(v -> {
            final String parcelIdStr = parcelSearchEditText.getText().toString();
            if (TextUtils.isEmpty(parcelIdStr)) {
                Toast.makeText(context, "Введите ID перевозки или номер накладной", Toast.LENGTH_SHORT).show();
                return;
            }

            final long parcelId = Long.parseLong(parcelIdStr);

            headerViewModel.selectRequest(parcelId).observe(this, receiptWithCargoList -> {
                if (receiptWithCargoList == null) {
                    Toast.makeText(context, "Накладной не существует", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (receiptWithCargoList.getReceipt() == null) {
//                    Toast.makeText(context, "Накладной не существует", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                final Intent mainIntent = new Intent(context, MainActivity.class);
                mainIntent.putExtra(Constants.INTENT_REQUEST_KEY, Constants.REQUEST_FIND_PARCEL);
                mainIntent.putExtra(Constants.INTENT_REQUEST_VALUE, parcelId);
                startActivity(mainIntent);
            });
        });
        //listeners
        photoImageView.setOnClickListener(v -> {
            final Intent openGallery = new Intent();
            openGallery.setType("image/*");
            openGallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(openGallery, "Выберите фото"), Constants.REQUEST_UPLOAD_PHOTO);
        });

        signatureImageView.setOnClickListener(v -> {
            startActivityForResult(new Intent(context, SignatureActivity.class), Constants.REQUEST_SENDER_SIGNATURE);
        });

        contractImageView.setOnClickListener(v -> {
            final Intent openFileManager = new Intent();
            openFileManager.addCategory(Intent.CATEGORY_OPENABLE);
            openFileManager.setType("*/*");
            openFileManager.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(openFileManager, "Выберите файл"), Constants.REQUEST_UPLOAD_PHOTO);
        });

        createBtn.setOnClickListener(v -> {
            final String login = loginEditText.getText().toString();
            final String password = passwordEditText.getText().toString();
            final String email = emailEditText.getText().toString();

            final String firstName = firstNameEditText.getText().toString();
            final String middleName = middleNameEditText.getText().toString();
            final String lastName = lastNameEditText.getText().toString();
            final String phone = phoneEditText.getText().toString();

            final String cargostarAccountNumber = cargostarAccountNumberEditText.getText().toString();
            final String tntAccountNumber = tntAccountNumberEditText.getText().toString();
            final String fedexAccountNumber = fedexAccountNumberEditText.getText().toString();

            final String country = countryEditText.getText().toString();
            final String region = regionEditText.getText().toString();
            final String city = cityEditText.getText().toString();
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

            if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(middleName) || TextUtils.isEmpty(lastName)) {
                Toast.makeText(context, "Имя не указано или указано не полностью", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(context, "Номер телефона не указан", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(country)) {
                Toast.makeText(context, "Страна не указана", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(region)) {
                Toast.makeText(context, "Регион/территория не указаны", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(city)) {
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

            final Address newAddress = new Address(country, region, city, address);
            newAddress.setZip(zip);

            if (!TextUtils.isEmpty(geolocation)) {
                if (!Regex.isGeolocation(geolocation)) {
                    Toast.makeText(context, "Геолокация должна быть указана в формате ХХ.ХХ", Toast.LENGTH_SHORT).show();
                    return;
                }
                newAddress.setGeolocation(new Point(geolocation));
            }
            final Account newAccount = new Account(login, password, email);

            //todo: create Customer, Passport and Payment data
//            final Customer customer = new Customer(firstName, middleName, lastName, phone, newAccount, newAddress, cargostarAccountNumber);
//            customer.setTntAccountNumber(tntAccountNumber);
//            customer.setFedexAccountNumber(fedexAccountNumber);
//
//            if (!TextUtils.isEmpty(discount)) {
//                customer.setDiscount(Integer.parseInt(discount));
//            }
//            if (!TextUtils.isEmpty(signature)) {
//                customer.setSignature(new Document(signature, signature));
//            }
//            if (!TextUtils.isEmpty(photo)) {
//                customer.setPhoto(new Document(photo, photo));
//            }

//            PassportData passportData = null;
//            PaymentData paymentData = null;
//
//            if (!TextUtils.isEmpty(passportSerial)) {
//                passportData = new PassportData(passportSerial);
//            }
//            else {
//                if (!TextUtils.isEmpty(inn) && !TextUtils.isEmpty(company)) {
//                    paymentData = new PaymentData(inn, company);
//                    if (!TextUtils.isEmpty(checkingAccount)) {
//                        paymentData.setCheckingAccount(checkingAccount);
//                    }
//                    if (!TextUtils.isEmpty(bank)) {
//                        paymentData.setBank(bank);
//                    }
//                    if (!TextUtils.isEmpty(vat)) {
//                        paymentData.setVat(vat);
//                    }
//                    if (!TextUtils.isEmpty(mfo)) {
//                        paymentData.setMfo(mfo);
//                    }
//                    if (!TextUtils.isEmpty(oked)) {
//                        paymentData.setOked(oked);
//                    }
//                    if (!TextUtils.isEmpty(contract)) {
//                        paymentData.setContract(new Document(contract, contract));
//                    }
//                }
//            }
//            if (passportData == null && paymentData == null) {
//                Toast.makeText(context, "Заполните физ. или юр. данные", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            final long userId = createUserViewModel.createCustomer(customer);

//            if (passportData != null) {
//                passportData.setUserId(userId);
//                createUserViewModel.createPassportData(passportData);
//                Toast.makeText(context, "Аккаунт " + userId + " был успешно создан для физ. лица " + firstName, Toast.LENGTH_SHORT).show();
//            }
//            else {
//                paymentData.setUserId(userId);
//                createUserViewModel.createPaymentData(paymentData);
//                Toast.makeText(context, "Аккаунт " + userId + " был успешно создан для юр. лица " + firstName, Toast.LENGTH_SHORT).show();
//            }
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == Constants.REQUEST_UPLOAD_PHOTO) {
                final Uri selectedImage = data.getData();
                if (selectedImage != null) {
                    photoResultImageView.setImageResource(R.drawable.ic_image_green);
                    photoResultImageView.setVisibility(View.VISIBLE);
                    photoEditText.setBackgroundResource(R.drawable.edit_text_active);
                    return;
                }
            }
            if (requestCode == Constants.REQUEST_UPLOAD_DOCUMENT) {
                final Uri selectedDoc = data.getData();
                if (selectedDoc != null) {
                    contractResultImageView.setImageResource(R.drawable.ic_doc_green);
                    contractResultImageView.setVisibility(View.VISIBLE);
                    contractEditText.setBackgroundResource(R.drawable.edit_text_active);
                    return;
                }
            }
            if (requestCode == Constants.REQUEST_SENDER_SIGNATURE) {
                signatureResultImageView.setImageResource(R.drawable.ic_image_green);
                signatureResultImageView.setVisibility(View.VISIBLE);
                signatureEditText.setBackgroundResource(R.drawable.edit_text_active);
                signatureEditText.setText(data.getStringExtra(Constants.INTENT_RESULT_VALUE));
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
        loginEditText = findViewById(R.id.login_edit_text);
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
        countryEditText = findViewById(R.id.country_edit_text);
        regionEditText = findViewById(R.id.region_edit_text);
        cityEditText = findViewById(R.id.city_edit_text);
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

        loginEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(loginEditText, hasFocus);
        });

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

        countryEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(countryEditText, hasFocus);
        });

        regionEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(regionEditText, hasFocus);
        });

        cityEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(cityEditText, hasFocus);
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
    }
}