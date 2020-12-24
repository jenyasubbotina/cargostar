package uz.alexits.cargostar.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.ArrayList;
import java.util.UUID;

import uz.alexits.cargostar.R;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.utils.Regex;
import uz.alexits.cargostar.view.UiUtils;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.activity.SignatureActivity;
import uz.alexits.cargostar.viewmodel.CourierViewModel;
import uz.alexits.cargostar.viewmodel.CustomerViewModel;
import uz.alexits.cargostar.viewmodel.LocationDataViewModel;
import uz.alexits.cargostar.viewmodel.RequestsViewModel;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class CreateUserFragment extends Fragment {
    private FragmentActivity activity;
    private Context context;
    //header views
    private TextView fullNameTextView;
    private TextView branchTextView;
    private TextView courierIdTextView;
    private EditText requestSearchEditText;
    private ImageView requestSearchImageView;
    private ImageView editImageView;
    private ImageView profileImageView;
    private ImageView notificationsImageView;
    private ImageView calculatorImageView;
    private TextView badgeCounterTextView;
    //user data
    private EditText passwordEditText;
    private EditText emailEditText;
    //client data
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
    private ArrayAdapter<City> cityArrayAdapter;
    private RelativeLayout cityField;
    private Spinner citySpinner;

    private EditText zipEditText;
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
    private Button createBtn;

    private ImageView signatureImageView;
    private ImageView signatureResultImageView;

    private ProgressBar progressBar;

    //ViewModel
    private CourierViewModel courierViewModel;
    private CustomerViewModel customerViewModel;
    private LocationDataViewModel locationDataViewModel;
    private RequestsViewModel requestsViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = getContext();
        this.activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_create_user, container, false);

        //search data
        fullNameTextView = activity.findViewById(R.id.full_name_text_view);
        branchTextView = activity.findViewById(R.id.branch_text_view);
        courierIdTextView = activity.findViewById(R.id.courier_id_text_view);
        requestSearchEditText = activity.findViewById(R.id.search_edit_text);
        requestSearchImageView = activity.findViewById(R.id.search_btn);
        editImageView = activity.findViewById(R.id.edit_image_view);
        profileImageView = activity.findViewById(R.id.profile_image_view);
        notificationsImageView = activity.findViewById(R.id.notifications_image_view);
        calculatorImageView = activity.findViewById(R.id.calculator_image_view);
        badgeCounterTextView = activity.findViewById(R.id.badge_counter_text_view);
        //user data
        passwordEditText = root.findViewById(R.id.password_edit_text);
        emailEditText = root.findViewById(R.id.email_edit_text);
        //client data
        tntAccountNumberEditText = root.findViewById(R.id.tnt_account_number_edit_text);
        fedexAccountNumberEditText = root.findViewById(R.id.fedex_account_number_edit_text);
        firstNameEditText = root.findViewById(R.id.first_name_edit_text);
        lastNameEditText = root.findViewById(R.id.last_name_edit_text);
        middleNameEditText = root.findViewById(R.id.middle_name_edit_text);
        phoneEditText = root.findViewById(R.id.phone_number_edit_text);
        addressEditText = root.findViewById(R.id.address_edit_text);
        geolocationEditText = root.findViewById(R.id.geolocation_edit_text);

        //country, region, city
        countryField = root.findViewById(R.id.country_field);
        cityField = root.findViewById(R.id.city_field);

        countrySpinner = root.findViewById(R.id.country_spinner);
        citySpinner = root.findViewById(R.id.city_spinner);

        progressBar = root.findViewById(R.id.progress_bar);

        zipEditText = root.findViewById(R.id.zip_edit_text);
        signatureEditText = root.findViewById(R.id.signature_edit_text);
        //payment data
        passportSerialTextView = root.findViewById(R.id.passport_serial_text_view);
        passportSerialEditText = root.findViewById(R.id.passport_serial_edit_text);
        innTextView = root.findViewById(R.id.inn_text_view);
        innEditText = root.findViewById(R.id.inn_edit_text);
        companyTextView = root.findViewById(R.id.company_text_view);
        companyEditText = root.findViewById(R.id.company_edit_text);
        checkingAccountTextView = root.findViewById(R.id.checking_account_text_view);
        checkingAccountEditText = root.findViewById(R.id.checking_account_edit_text);
        bankTextView = root.findViewById(R.id.bank_text_view);
        bankEditText = root.findViewById(R.id.bank_edit_text);
        vatTextView = root.findViewById(R.id.vat_text_view);
        vatEditText = root.findViewById(R.id.vat_edit_text);
        mfoTextView = root.findViewById(R.id.mfo_text_view);
        mfoEditText = root.findViewById(R.id.mfo_edit_text);
        okedTextView = root.findViewById(R.id.oked_text_view);
        okedEditText = root.findViewById(R.id.oked_edit_text);
        createBtn = root.findViewById(R.id.create_btn);

        signatureImageView = root.findViewById(R.id.signature_image_view);
        signatureResultImageView = root.findViewById(R.id.success_signature_image_view);

        countryArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new ArrayList<>());
        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countryArrayAdapter);

        cityArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new ArrayList<>());
        cityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityArrayAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        passwordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(passwordEditText, hasFocus);
        });

        emailEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(emailEditText, hasFocus);
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
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(R.id.profileFragment);
        });

        notificationsImageView.setOnClickListener(v -> {
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(R.id.notificationsFragment);
        });

        profileImageView.setOnClickListener(v -> {
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(R.id.mainFragment);
        });

        calculatorImageView.setOnClickListener(v -> {
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(R.id.calculatorFragment);
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

        requestSearchImageView.setOnClickListener(v -> {
            final String invoiceIdStr = requestSearchEditText.getText().toString();

            if (TextUtils.isEmpty(invoiceIdStr)) {
                Toast.makeText(context, "Введите ID заявки", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isDigitsOnly(invoiceIdStr)) {
                Toast.makeText(context, "Неверный формат", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                final UUID searchInvoiceUUID = SyncWorkRequest.searchRequest(context, Long.parseLong(invoiceIdStr));

                WorkManager.getInstance(context).getWorkInfoByIdLiveData(searchInvoiceUUID).observe(getViewLifecycleOwner(), workInfo -> {
                    if (workInfo.getState() == WorkInfo.State.FAILED || workInfo.getState() == WorkInfo.State.CANCELLED) {
                        Toast.makeText(context, "Заявки не существует", Toast.LENGTH_SHORT).show();
                        requestSearchEditText.setEnabled(true);
                        return;
                    }
                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        final Data outputData = workInfo.getOutputData();

                        final long requestId = outputData.getLong(Constants.KEY_REQUEST_ID, -1L);
                        final long invoiceId = outputData.getLong(Constants.KEY_INVOICE_ID, -1L);
                        final long courierId = outputData.getLong(Constants.KEY_COURIER_ID, -1L);
                        final long clientId = outputData.getLong(Constants.KEY_CLIENT_ID, -1L);
                        final long senderCountryId = outputData.getLong(Constants.KEY_SENDER_COUNTRY_ID, -1L);
                        final long senderRegionId = outputData.getLong(Constants.KEY_SENDER_REGION_ID, -1L);
                        final long senderCityId = outputData.getLong(Constants.KEY_SENDER_CITY_ID, -1L);
                        final long recipientCountryId = outputData.getLong(Constants.KEY_RECIPIENT_COUNTRY_ID, -1L);
                        final long recipientCityId = outputData.getLong(Constants.KEY_RECIPIENT_CITY_ID, -1L);
                        final long providerId = outputData.getLong(Constants.KEY_PROVIDER_ID, -1L);

                        final Intent mainIntent = new Intent(context, MainActivity.class);
                        mainIntent.putExtra(IntentConstants.INTENT_REQUEST_KEY, IntentConstants.REQUEST_FIND_REQUEST);
                        mainIntent.putExtra(IntentConstants.INTENT_REQUEST_VALUE, requestId);
                        mainIntent.putExtra(Constants.KEY_REQUEST_ID, requestId);
                        mainIntent.putExtra(Constants.KEY_INVOICE_ID, invoiceId);
                        mainIntent.putExtra(Constants.KEY_CLIENT_ID, clientId);
                        mainIntent.putExtra(Constants.KEY_COURIER_ID, courierId);
                        mainIntent.putExtra(Constants.KEY_SENDER_COUNTRY_ID, senderCountryId);
                        mainIntent.putExtra(Constants.KEY_SENDER_REGION_ID, senderRegionId);
                        mainIntent.putExtra(Constants.KEY_SENDER_CITY_ID, senderCityId);
                        mainIntent.putExtra(Constants.KEY_RECIPIENT_COUNTRY_ID, recipientCountryId);
                        mainIntent.putExtra(Constants.KEY_RECIPIENT_CITY_ID, recipientCityId);
                        mainIntent.putExtra(Constants.KEY_PROVIDER_ID, providerId);
                        startActivity(mainIntent);

                        requestSearchEditText.setEnabled(true);

                        return;
                    }
                    requestSearchEditText.setEnabled(false);
                });
            }
            catch (Exception e) {
                Log.e(TAG, "getInvoiceById(): ", e);
                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        signatureImageView.setOnClickListener(v -> {
            startActivityForResult(new Intent(context, SignatureActivity.class), IntentConstants.REQUEST_SENDER_SIGNATURE);
        });

        createBtn.setOnClickListener(v -> {
            final String password = passwordEditText.getText().toString();
            final String email = emailEditText.getText().toString();

            final String firstName = firstNameEditText.getText().toString();
            final String middleName = middleNameEditText.getText().toString();
            final String lastName = lastNameEditText.getText().toString();
            final String phone = phoneEditText.getText().toString();

            final String tntAccountNumber = tntAccountNumberEditText.getText().toString();
            final String fedexAccountNumber = fedexAccountNumberEditText.getText().toString();

            final Country country = (Country) countrySpinner.getSelectedItem();
            final City city = (City) citySpinner.getSelectedItem();

            final String zip = zipEditText.getText().toString();
            final String address = addressEditText.getText().toString();
            final String geolocation = geolocationEditText.getText().toString();

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
            if (city == null) {
                Toast.makeText(context, "Город не указан", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(address)) {
                Toast.makeText(context, "Адрес не указан", Toast.LENGTH_SHORT).show();
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
                    context,
                    email,
                    password,
                    null,
                    tntAccountNumber,
                    fedexAccountNumber,
                    firstName,
                    middleName,
                    lastName,
                    phone,
                    country.getNameEn(),
                    city.getNameEn(),
                    address,
                    geolocation,
                    zip,
                    0.0,
                    userType,
                    passportSerial,
                    inn,
                    company,
                    bank,
                    mfo,
                    oked,
                    checkingAccount,
                    vat,
                    !TextUtils.isEmpty(signature) ? signature : null);
            WorkManager.getInstance(context).getWorkInfoByIdLiveData(createUserWorkerId).observe(getViewLifecycleOwner(), workInfo -> {
                if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                    progressBar.setVisibility(View.INVISIBLE);
                    createBtn.setEnabled(true);
                    Toast.makeText(context, "Пользователь " + email + " был успешно создан!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(context, MainActivity.class));
                    activity.finish();
                    return;
                }
                if (workInfo.getState() == WorkInfo.State.FAILED || workInfo.getState() == WorkInfo.State.CANCELLED) {
                    progressBar.setVisibility(View.INVISIBLE);
                    createBtn.setEnabled(true);
                    Log.e(TAG, "createUser(): failed to create user");
                    Toast.makeText(context, "Ошибка создания пользователя", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                createBtn.setEnabled(false);
            });
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //header view model
        courierViewModel = new ViewModelProvider(this).get(CourierViewModel.class);
        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
        locationDataViewModel = new ViewModelProvider(this).get(LocationDataViewModel.class);
        requestsViewModel = new ViewModelProvider(this).get(RequestsViewModel.class);

        courierViewModel.selectCourierByLogin(SharedPrefs.getInstance(context).getString(Constants.KEY_LOGIN)).observe(getViewLifecycleOwner(), courier -> {
            if (courier != null) {
                fullNameTextView.setText(getString(R.string.header_courier_full_name, courier.getFirstName(), courier.getLastName()));
                courierIdTextView.setText(getString(R.string.courier_id_placeholder, courier.getId()));
            }
        });
        courierViewModel.selectBrancheById(SharedPrefs.getInstance(context).getLong(SharedPrefs.BRANCH_ID)).observe(getViewLifecycleOwner(), branch -> {
            if (branch != null) {
                branchTextView.setText(getString(R.string.header_branch_name, branch.getName()));
            }
        });
        courierViewModel.selectNewNotificationsCount().observe(getViewLifecycleOwner(), newNotificationsCount -> {
            if (newNotificationsCount != null) {
                badgeCounterTextView.setText(String.valueOf(newNotificationsCount));
            }
        });

        //location data view model
        locationDataViewModel.getCountryList().observe(getViewLifecycleOwner(), countryList -> {
            for (final Country country : countryList) {
                countryArrayAdapter.add(country);
            }
            countryArrayAdapter.notifyDataSetChanged();
        });

        locationDataViewModel.getCityList().observe(getViewLifecycleOwner(), cityList -> {
            for (final City city : cityList) {
                cityArrayAdapter.add(city);
            }
            cityArrayAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AppCompatActivity.RESULT_CANCELED) {
            return;
        }
        if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
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

    private static final String TAG = CreateUserFragment.class.toString();
}
