package uz.alexits.cargostar.view.fragment;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.work.WorkInfo;

import java.util.ArrayList;

import uz.alexits.cargostar.R;
import uz.alexits.cargostar.entities.location.Country;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.utils.Regex;
import uz.alexits.cargostar.view.UiUtils;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.activity.SignatureActivity;
import uz.alexits.cargostar.viewmodel.CreateUserViewModel;
import uz.alexits.cargostar.viewmodel.factory.CreateUserViewModelFactory;

public class CreateUserFragment extends Fragment {
    /* header views */
    private TextView fullNameTextView;
    private TextView branchTextView;
    private TextView courierIdTextView;
    private EditText requestSearchEditText;
    private ImageView requestSearchImageView;
    private ImageView editImageView;
    private ImageView profileImageView;
    private ImageView calculatorImageView;
    private ImageView createUserImageView;
    private ImageView notificationsImageView;
    private TextView badgeCounterTextView;

    /* content views */
    private EditText passwordEditText;
    private EditText emailEditText;
    private EditText tntAccountNumberEditText;
    private EditText fedexAccountNumberEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText middleNameEditText;
    private EditText phoneEditText;
    private EditText addressEditText;
    private EditText cityEditText;
    private EditText geolocationEditText;
    private ArrayAdapter<Country> countryArrayAdapter;
    private Spinner countrySpinner;
    private RelativeLayout countryField;
    private EditText zipEditText;
    private EditText signatureEditText;
    private TextView passportSerialTextView;
    private EditText passportSerialEditText;
    private TextView innTextView;
    private EditText innEditText;
    private TextView companyTextView;
    private EditText companyEditText;
    private TextView contractNumberTextView;
    private EditText contractNumberEditText;
    private Button createBtn;
    private ImageView signatureImageView;
    private ImageView signatureResultImageView;
    private ProgressBar progressBar;

    private CreateUserViewModel createUserViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final CreateUserViewModelFactory createUserFactory = new CreateUserViewModelFactory(requireContext());
        createUserViewModel = new ViewModelProvider(getViewModelStore(), createUserFactory).get(CreateUserViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_create_user, container, false);

        /* header views */
        fullNameTextView = requireActivity().findViewById(R.id.full_name_text_view);
        branchTextView = requireActivity().findViewById(R.id.branch_text_view);
        courierIdTextView = requireActivity().findViewById(R.id.courier_id_text_view);
        requestSearchEditText = requireActivity().findViewById(R.id.search_edit_text);
        requestSearchImageView = requireActivity().findViewById(R.id.search_btn);
        editImageView = requireActivity().findViewById(R.id.edit_image_view);
        profileImageView = requireActivity().findViewById(R.id.profile_image_view);
        notificationsImageView = requireActivity().findViewById(R.id.notifications_image_view);
        calculatorImageView = requireActivity().findViewById(R.id.calculator_image_view);
        createUserImageView = requireActivity().findViewById(R.id.create_user_image_view);
        badgeCounterTextView = requireActivity().findViewById(R.id.badge_counter_text_view);

        /* content views */
        passwordEditText = root.findViewById(R.id.password_edit_text);
        emailEditText = root.findViewById(R.id.email_edit_text);
        tntAccountNumberEditText = root.findViewById(R.id.tnt_account_number_edit_text);
        fedexAccountNumberEditText = root.findViewById(R.id.fedex_account_number_edit_text);
        firstNameEditText = root.findViewById(R.id.first_name_edit_text);
        lastNameEditText = root.findViewById(R.id.last_name_edit_text);
        middleNameEditText = root.findViewById(R.id.middle_name_edit_text);
        phoneEditText = root.findViewById(R.id.phone_number_edit_text);
        addressEditText = root.findViewById(R.id.address_edit_text);
        geolocationEditText = root.findViewById(R.id.geolocation_edit_text);
        countryField = root.findViewById(R.id.country_field);
        cityEditText = root.findViewById(R.id.city_edit_text);
        countrySpinner = root.findViewById(R.id.country_spinner);
        progressBar = root.findViewById(R.id.progress_bar);
        zipEditText = root.findViewById(R.id.zip_edit_text);
        signatureEditText = root.findViewById(R.id.signature_edit_text);
        passportSerialTextView = root.findViewById(R.id.passport_serial_text_view);
        passportSerialEditText = root.findViewById(R.id.passport_serial_edit_text);
        innTextView = root.findViewById(R.id.inn_text_view);
        innEditText = root.findViewById(R.id.inn_edit_text);
        companyTextView = root.findViewById(R.id.company_text_view);
        companyEditText = root.findViewById(R.id.company_edit_text);
        contractNumberTextView = root.findViewById(R.id.contract_number_text_view);
        contractNumberEditText = root.findViewById(R.id.contract_number_edit_text);
        createBtn = root.findViewById(R.id.create_btn);

        signatureImageView = root.findViewById(R.id.signature_image_view);
        signatureResultImageView = root.findViewById(R.id.success_signature_image_view);

        countryArrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countryArrayAdapter);

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

        contractNumberEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(contractNumberEditText, hasFocus);
        });

        cityEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(cityEditText, hasFocus);
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
                    contractNumberTextView.setVisibility(View.INVISIBLE);
                    contractNumberEditText.setVisibility(View.INVISIBLE);
                    return;
                }
                innTextView.setVisibility(View.VISIBLE);
                innEditText.setVisibility(View.VISIBLE);
                companyTextView.setVisibility(View.VISIBLE);
                companyEditText.setVisibility(View.VISIBLE);
                contractNumberTextView.setVisibility(View.VISIBLE);
                contractNumberEditText.setVisibility(View.VISIBLE);
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
            UiUtils.getNavController(requireActivity(), R.id.main_fragment_container).navigate(R.id.profileFragment);
        });

        notificationsImageView.setOnClickListener(v -> {
            UiUtils.getNavController(requireActivity(), R.id.main_fragment_container).navigate(R.id.notificationsFragment);
        });

        profileImageView.setOnClickListener(v -> {
            UiUtils.getNavController(requireActivity(), R.id.main_fragment_container).navigate(R.id.mainFragment);
        });

        calculatorImageView.setOnClickListener(v -> {
            UiUtils.getNavController(requireActivity(), R.id.main_fragment_container).navigate(R.id.calculatorFragment);
        });

        createUserImageView.setOnClickListener(null);

        //country, region, city spinners
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                createUserViewModel.setCountryId(((Country) parent.getSelectedItem()).getId());

                final TextView itemTextView = (TextView) view;

                if (itemTextView != null) {
                    if (position < parent.getCount()) {
                        itemTextView.setTextColor(requireContext().getColor(R.color.colorBlack));
                        countryField.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        requestSearchImageView.setOnClickListener(v -> {
            final String requestId = requestSearchEditText.getText().toString().trim();

            if (TextUtils.isEmpty(requestId)) {
                Toast.makeText(requireContext(), "Введите ID заявки", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isDigitsOnly(requestId)) {
                Toast.makeText(requireContext(), "Неверный формат", Toast.LENGTH_SHORT).show();
                return;
            }
            createUserViewModel.searchRequest(Long.parseLong(requestId));
        });

        signatureImageView.setOnClickListener(v -> {
            startActivityForResult(new Intent(requireContext(), SignatureActivity.class), IntentConstants.REQUEST_SENDER_SIGNATURE);
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
            final String city = cityEditText.getText().toString();

            final String zip = zipEditText.getText().toString();
            final String address = addressEditText.getText().toString();
            final String geolocation = geolocationEditText.getText().toString();

            final String signature = signatureEditText.getText().toString();

            //PassportData
            final String passportSerial = passportSerialEditText.getText().toString();
            //PaymentData
            final String inn = innEditText.getText().toString();
            final String company = companyEditText.getText().toString();
            final String contractNumber = contractNumberEditText.getText().toString();

            /* check for empty fields */
            if (password.length() < 6) {
                Toast.makeText(requireContext(), "Пароль должен сожержать минимум 6 символов", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
                Toast.makeText(requireContext(), "Имя не указано или указано не полностью", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(requireContext(), "Номер телефона не указан", Toast.LENGTH_SHORT).show();
                return;
            }
            if (country == null) {
                Toast.makeText(requireContext(), "Страна не указана", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(address)) {
                Toast.makeText(requireContext(), "Адрес не указан", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(passportSerial) && TextUtils.isEmpty(inn) && TextUtils.isEmpty(company)) {
                Toast.makeText(requireContext(), "Заполните физ. или юр. данные", Toast.LENGTH_SHORT).show();
                return;
            }

            /* check for regular expressions */
            if (!Regex.isEmail(email)) {
                Toast.makeText(requireContext(), "Email указан неверно", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Regex.isName(firstName)) {
                Toast.makeText(requireContext(), "Имя указано неверно", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Regex.isName(lastName)) {
                Toast.makeText(requireContext(), "Фамилия указана неверно", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Regex.isPhoneNumber(phone)) {
                Toast.makeText(requireContext(), "Номер телефона указан неверно", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(tntAccountNumber)) {
                if (!Regex.isAccountNumber(tntAccountNumber)) {
                    Toast.makeText(requireContext(), "Номер аккаунта TNT указан неверно", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (!TextUtils.isEmpty(fedexAccountNumber)) {
                if (!Regex.isAccountNumber(fedexAccountNumber)) {
                    Toast.makeText(requireContext(), "Номер аккаунта Fedex указан неверно", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (!TextUtils.isEmpty(zip)) {
                if (!Regex.isZip(zip)) {
                    Toast.makeText(requireContext(), "Почтовый индекс должен содержать только цифры", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (TextUtils.isEmpty(passportSerial) && (TextUtils.isEmpty(inn) || TextUtils.isEmpty(company))) {
                Toast.makeText(requireContext(), "Для юр. лица укажите ИНН и название компании", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isEmpty(inn)) {
                if (!Regex.isAccountNumber(inn)) {
                    Toast.makeText(requireContext(), "ИНН указан неверно", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (!TextUtils.isEmpty(geolocation)) {
                if (!Regex.isGeolocation(geolocation)) {
                    Toast.makeText(requireContext(), "Геолокация должна быть указана в формате ХХ.ХХ", Toast.LENGTH_SHORT).show();
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
            createUserViewModel.createUser(
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
                    city,
                    address,
                    geolocation,
                    zip,
                    0.0,
                    userType,
                    passportSerial,
                    inn,
                    company,
                    contractNumber,
                    !TextUtils.isEmpty(signature) ? signature : null);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* header */
        createUserViewModel.getCourierData(requireContext()).observe(getViewLifecycleOwner(), courier -> {
            if (courier != null) {
                fullNameTextView.setText(getString(R.string.header_courier_full_name, courier.getFirstName(), courier.getLastName()));
                courierIdTextView.setText(getString(R.string.courier_id_placeholder, courier.getId()));
            }
        });

        createUserViewModel.getBrancheData(requireContext()).observe(getViewLifecycleOwner(), branch -> {
            if (branch != null) {
                branchTextView.setText(getString(R.string.header_branch_name, branch.getName()));
            }
        });

        createUserViewModel.selectNewNotificationsCount().observe(getViewLifecycleOwner(), newNotificationsCount -> {
            if (newNotificationsCount != null) {
                badgeCounterTextView.setText(String.valueOf(newNotificationsCount));
            }
        });

        createUserViewModel.getSearchRequestResult(requireContext()).observe(getViewLifecycleOwner(), workInfo -> {
            if (workInfo.getState() == WorkInfo.State.FAILED || workInfo.getState() == WorkInfo.State.CANCELLED) {
                Toast.makeText(requireContext(), "Заявки не существует", Toast.LENGTH_SHORT).show();
                requestSearchEditText.setEnabled(true);
                return;
            }
            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                requestSearchEditText.setEnabled(true);
                startActivity(new Intent(requireContext(), MainActivity.class)
                        .putExtra(IntentConstants.INTENT_REQUEST_KEY, IntentConstants.REQUEST_FIND_REQUEST)
                        .putExtra(Constants.KEY_REQUEST_ID, workInfo.getOutputData().getLong(Constants.KEY_REQUEST_ID, 0L))
                        .putExtra(Constants.KEY_INVOICE_ID, workInfo.getOutputData().getLong(Constants.KEY_INVOICE_ID, 0L))
                        .putExtra(Constants.KEY_CLIENT_ID, workInfo.getOutputData().getLong(Constants.KEY_CLIENT_ID, 0L))
                        .putExtra(Constants.KEY_COURIER_ID, workInfo.getOutputData().getLong(Constants.KEY_COURIER_ID, 0L))
                        .putExtra(Constants.KEY_SENDER_COUNTRY_ID, workInfo.getOutputData().getLong(Constants.KEY_SENDER_COUNTRY_ID, 0L))
                        .putExtra(Constants.KEY_SENDER_REGION_ID, workInfo.getOutputData().getLong(Constants.KEY_SENDER_REGION_ID, 0L))
                        .putExtra(Constants.KEY_SENDER_CITY_ID, workInfo.getOutputData().getLong(Constants.KEY_SENDER_CITY_ID, 0L))
                        .putExtra(Constants.KEY_RECIPIENT_COUNTRY_ID, workInfo.getOutputData().getLong(Constants.KEY_RECIPIENT_COUNTRY_ID, 0L))
                        .putExtra(Constants.KEY_RECIPIENT_CITY_ID, workInfo.getOutputData().getLong(Constants.KEY_RECIPIENT_CITY_ID, 0L))
                        .putExtra(Constants.KEY_PROVIDER_ID, workInfo.getOutputData().getLong(Constants.KEY_PROVIDER_ID, 0L)));
                return;
            }
            requestSearchEditText.setEnabled(false);
        });

        createUserViewModel.getCountryList().observe(getViewLifecycleOwner(), countryList -> {
            if (countryList != null) {
                countryArrayAdapter.clear();
                countryArrayAdapter.addAll(countryList);

                for (int i = 0; i < countryList.size(); i++) {
                    if (countryList.get(i).getNameEn().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                        int finalI = i;
                        countrySpinner.post(() -> countrySpinner.setSelection(finalI, false));
                        return;
                    }
                }
            }
        });

        createUserViewModel.getCreateClientResult(requireContext()).observe(getViewLifecycleOwner(), workInfo -> {
            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                progressBar.setVisibility(View.INVISIBLE);
                createBtn.setEnabled(true);
                Toast.makeText(requireContext(), "Пользователь " + " был успешно создан!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(requireContext(), MainActivity.class));
                requireActivity().finish();
                return;
            }
            if (workInfo.getState() == WorkInfo.State.FAILED || workInfo.getState() == WorkInfo.State.CANCELLED) {
                progressBar.setVisibility(View.INVISIBLE);
                createBtn.setEnabled(true);
                Log.e(TAG, "createUser(): failed to create user");
                Toast.makeText(requireContext(), "Ошибка создания пользователя", Toast.LENGTH_SHORT).show();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            createBtn.setEnabled(false);
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

    private static final String TAG = CreateUserFragment.class.toString();
}
