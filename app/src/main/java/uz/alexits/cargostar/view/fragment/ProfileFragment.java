package uz.alexits.cargostar.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import uz.alexits.cargostar.viewmodel.HeaderViewModel;
import uz.alexits.cargostar.viewmodel.ProfileViewModel;
import uz.alexits.cargostar.view.Constants;
import uz.alexits.cargostar.view.UiUtils;
import uz.alexits.cargostar.view.activity.CalculatorActivity;
import uz.alexits.cargostar.view.activity.CreateUserActivity;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.activity.NotificationsActivity;
import uz.alexits.cargostar.view.activity.SignInActivity;

public class ProfileFragment extends Fragment {
    private FragmentActivity activity;
    private Context context;
    private HeaderViewModel headerViewModel;
    private ProfileViewModel profileViewModel;
    //header views
    private TextView fullNameTextView;
    private TextView branchTextView;
    private EditText parcelSearchEditText;
    private ImageView parcelSearchImageView;
    private ImageView profileImageView;
    private ImageView createUserImageView;
    private ImageView calculatorImageView;
    private ImageView notificationsImageView;
    private TextView badgeCounterTextView;
    //main content views
    private EditText userIdEditText;
    private EditText loginEditText;
    private EditText passwordEditText;
    private EditText emailEditText;
    private EditText positionEditText;
    private EditText firstNameEditText;
    private EditText middleNameEditText;
    private EditText lastNameEditText;
    private EditText phoneEditText;
    private EditText addressEditText;
    private EditText geolocationEditText;
    private EditText countryEditText;
    private EditText regionEditText;
    private EditText cityEditText;
    private EditText zipEditText;
    private EditText photoEditText;
    private ImageView uploadPhotoImageView;
    private ImageView uploadResultImageView;
    private Button saveBtn;
    private TextView logoutTextView;

    private long courierId;
    private long branchId;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        context = getContext();
        courierId = SharedPrefs.getInstance(context).getLong(SharedPrefs.ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_profile, container, false);
        initUI(activity, root);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //header views
        profileImageView.setOnClickListener(v -> {
            startActivity(new Intent(context, MainActivity.class));
        });
        createUserImageView.setOnClickListener(v -> {
            startActivity(new Intent(context, CreateUserActivity.class));
        });
        notificationsImageView.setOnClickListener(v -> {
            startActivity(new Intent(context, NotificationsActivity.class));
        });
        calculatorImageView.setOnClickListener(v -> {
            startActivity(new Intent(context, CalculatorActivity.class));
        });
        //main content views
        userIdEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(userIdEditText, hasFocus);
        });
        loginEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(loginEditText, hasFocus);
        });
        passwordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(passwordEditText, hasFocus);
        });
        emailEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(emailEditText, hasFocus);
        });
        positionEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(positionEditText, hasFocus);
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
        geolocationEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(geolocationEditText, hasFocus);
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

        final Intent pickFromGallery = new Intent();
        pickFromGallery.setType("image/*");
        pickFromGallery.setAction(Intent.ACTION_GET_CONTENT);

        uploadPhotoImageView.setOnClickListener(v -> {
            startActivityForResult(Intent.createChooser(pickFromGallery, "Выберите файл"), Constants.REQUEST_UPLOAD_PHOTO);
        });

        saveBtn.setOnClickListener(v -> {
            final String userId = userIdEditText.getText().toString();
            final String login = loginEditText.getText().toString();
            final String password = passwordEditText.getText().toString();
            final String email = emailEditText.getText().toString();

            final String firstName = firstNameEditText.getText().toString();
            final String middleName = middleNameEditText.getText().toString();
            final String lastName = lastNameEditText.getText().toString();
            final String phone = phoneEditText.getText().toString();

            final String country = countryEditText.getText().toString();
            final String region = regionEditText.getText().toString();
            final String city = cityEditText.getText().toString();
            final String zip = zipEditText.getText().toString();
            final String address = addressEditText.getText().toString();
            final String geolocation = geolocationEditText.getText().toString();

            final String photo = photoEditText.getText().toString();

            if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(login) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
                Toast.makeText(getContext(), "Данные авторизации не могут быть пустыми", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(middleName) || TextUtils.isEmpty(lastName)) {
                Toast.makeText(getContext(), "Имя не указано или указано не полностью", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(getContext(), "Номер телефона не указан", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(country)) {
                Toast.makeText(getContext(), "Страна не указана", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(region)) {
                Toast.makeText(getContext(), "Регион/территория не указаны", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(city)) {
                Toast.makeText(getContext(), "Город не указан", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(address)) {
                Toast.makeText(getContext(), "Адрес не указан", Toast.LENGTH_SHORT).show();
                return;
            }

            final Address newAddress = new Address(country, region, city, address);
            newAddress.setZip(zip);

            if (!TextUtils.isEmpty(geolocation)) {
                newAddress.setGeolocation(new Point(geolocation));
            }
            final Account newAccount = new Account(login, password, email);
            //todo: create Customer with Ids
//            final Courier courier = new Courier(
//                    id,
//                    countryId,
//                    regionId,
//                    cityId,
//                    firstName,
//                    middleName,
//                    lastName,
//                    phone,
//                    address,
//                    geolocation,
//                    zip,
//                    1,
//                    createdAt,
//                    new Date(),
//                    newAccount,
//                    1);

            //todo: add photo to Courier
            if (!TextUtils.isEmpty(photo)) {
//                courier.setPhoto(new Document(photo, photo));
            }

//            profileViewModel.updateCourier(courier);
            Toast.makeText(activity, "Изменения сохранены", Toast.LENGTH_SHORT).show();
        });

        logoutTextView.setOnClickListener(v -> {
            SharedPrefs.getInstance(activity).putString(SharedPrefs.LOGIN, null);
            SharedPrefs.getInstance(activity).putString(SharedPrefs.PASSWORD_HASH, null);
            SharedPrefs.getInstance(activity).putBoolean(SharedPrefs.KEEP_LOGGED, false);
            final Intent logoutIntent = new Intent(activity.getApplicationContext(), SignInActivity.class);
            logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(logoutIntent);
            activity.setResult(Activity.RESULT_OK, null);
            activity.finish();
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        headerViewModel = new ViewModelProvider(this).get(HeaderViewModel.class);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        final String login = SharedPrefs.getInstance(getContext()).getString(SharedPrefs.LOGIN);

        headerViewModel.selectCourierByLogin(login).observe(getViewLifecycleOwner(), employee -> {
            userIdEditText.setText(String.valueOf(employee.getId()));
            userIdEditText.setBackgroundResource(R.drawable.edit_text_active);
            if (!TextUtils.isEmpty(employee.getAccount().getLogin())) {
                loginEditText.setText(employee.getAccount().getLogin());
                loginEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                loginEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(employee.getAccount().getPasswordHash())) {
                passwordEditText.setText(employee.getAccount().getPasswordHash());
                passwordEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                passwordEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(employee.getAccount().getEmail())) {
                emailEditText.setText(employee.getAccount().getEmail());
                emailEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                emailEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(employee.getPosition())) {
                positionEditText.setText(employee.getPosition());
                positionEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                positionEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(employee.getFirstName())) {
                firstNameEditText.setText(employee.getFirstName());
                firstNameEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                firstNameEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(employee.getMiddleName())) {
                middleNameEditText.setText(employee.getMiddleName());
                middleNameEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                middleNameEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(employee.getLastName())) {
                lastNameEditText.setText(employee.getLastName());
                lastNameEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                lastNameEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(employee.getPhone())) {
                phoneEditText.setText(employee.getPhone());
                phoneEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                phoneEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            //todo: add photo field
//            if (employee.getPhoto() != null) {
//                photoEditText.setText(employee.getPhoto().getName());
//                photoEditText.setBackgroundResource(R.drawable.edit_text_active);
//            }
//            else {
//                photoEditText.setBackgroundResource(R.drawable.edit_text_locked);
//            }
            fullNameTextView.setText(employee.getFirstName() + " " + employee.getLastName());

        });

        headerViewModel.selectBrancheById(courierId).observe(getViewLifecycleOwner(), branch -> {
            Log.i(ProfileFragment.class.toString(), "branch" + branch);
            if (branch != null) {
                branchId = branch.getId();
                branchTextView.setText(getString(R.string.branch) + " \"" + branch.getName() + "\"");
                //branch data
                addressEditText.setText(branch.getAddress());
                addressEditText.setBackgroundResource(R.drawable.edit_text_active);
                zipEditText.setText(branch.getZip());
                zipEditText.setBackgroundResource(R.drawable.edit_text_active);
                geolocationEditText.setText(branch.getGeolocation().toString());
                geolocationEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
        });
        headerViewModel.selectNewNotificationsCount().observe(getViewLifecycleOwner(), newNotificationsCount -> {
            if (newNotificationsCount != null) {
                badgeCounterTextView.setText(String.valueOf(newNotificationsCount));
            }
        });

//        profileViewModel.selectCountryByCourierId(courierId).observe(getViewLifecycleOwner(), country -> {
//            Log.i(ProfileFragment.class.toString(), "branchId=" + branchId + "country=" + country);
//            if (country != null) {
//                if (!TextUtils.isEmpty(country.getName())) {
//                    countryEditText.setText(country.getName());
//                    countryEditText.setBackgroundResource(R.drawable.edit_text_active);
//                }
//                else {
//                    countryEditText.setBackgroundResource(R.drawable.edit_text_locked);
//                }
//            }
//        });
//        profileViewModel.selectRegionByCourierId(courierId).observe(getViewLifecycleOwner(), region -> {
//            Log.i(ProfileFragment.class.toString(), "branchId=" + branchId + "region=" + region);
//            if (region != null) {
//                if (!TextUtils.isEmpty(region.getName())) {
//                    regionEditText.setText(region.getName());
//                    regionEditText.setBackgroundResource(R.drawable.edit_text_active);
//                }
//                else {
//                    regionEditText.setBackgroundResource(R.drawable.edit_text_locked);
//                }
//            }
//        });
//        profileViewModel.selectCityByCourierId(courierId).observe(getViewLifecycleOwner(), city -> {
//            Log.i(ProfileFragment.class.toString(), "branchId=" + branchId + "city=" + city);
//            if (city != null) {
//                if (!TextUtils.isEmpty(city.getName())) {
//                    cityEditText.setText(city.getName());
//                    cityEditText.setBackgroundResource(R.drawable.edit_text_active);
//                }
//                else {
//                    cityEditText.setBackgroundResource(R.drawable.edit_text_locked);
//                }
//            }
//        });

        parcelSearchImageView.setOnClickListener(v -> {
            final String parcelIdStr = parcelSearchEditText.getText().toString();
            if (TextUtils.isEmpty(parcelIdStr)) {
                Toast.makeText(context, "Введите ID перевозки или номер накладной", Toast.LENGTH_SHORT).show();
                return;
            }

            final long parcelId = Long.parseLong(parcelIdStr);

            headerViewModel.selectRequest(parcelId).observe(getViewLifecycleOwner(), receiptWithCargoList -> {
                Toast.makeText(context, "Накладной не существует", Toast.LENGTH_SHORT).show();
                if (receiptWithCargoList == null) {
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
    }

    private void initUI(final FragmentActivity activity, final View root) {
        //header views
        fullNameTextView = activity.findViewById(R.id.full_name_text_view);
        branchTextView = activity.findViewById(R.id.branch_text_view);
        parcelSearchEditText = activity.findViewById(R.id.search_edit_text);
        parcelSearchImageView = activity.findViewById(R.id.search_btn);
        profileImageView = activity.findViewById(R.id.profile_image_view);
        createUserImageView = activity.findViewById(R.id.create_user_image_view);
        calculatorImageView = activity.findViewById(R.id.calculator_image_view);
        notificationsImageView = activity.findViewById(R.id.notifications_image_view);
        badgeCounterTextView = activity.findViewById(R.id.badge_counter_text_view);
        //main content views
        userIdEditText = root.findViewById(R.id.user_id_edit_text);
        loginEditText = root.findViewById(R.id.login_edit_text);
        passwordEditText = root.findViewById(R.id.password_edit_text);
        emailEditText = root.findViewById(R.id.email_edit_text);
        positionEditText = root.findViewById(R.id.position_edit_text);
        firstNameEditText = root.findViewById(R.id.first_name_edit_text);
        middleNameEditText = root.findViewById(R.id.middle_name_edit_text);
        lastNameEditText = root.findViewById(R.id.last_name_edit_text);
        phoneEditText = root.findViewById(R.id.phone_number_edit_text);
        addressEditText = root.findViewById(R.id.address_edit_text);
        geolocationEditText = root.findViewById(R.id.geolocation_edit_text);
        countryEditText = root.findViewById(R.id.country_edit_text);
        regionEditText = root.findViewById(R.id.region_edit_text);
        cityEditText = root.findViewById(R.id.city_edit_text);
        zipEditText = root.findViewById(R.id.zip_edit_text);
        photoEditText = root.findViewById(R.id.photo_edit_text);
        uploadPhotoImageView = root.findViewById(R.id.photo_image_view);
        uploadResultImageView = root.findViewById(R.id.photo_result_image_view);
        saveBtn = root.findViewById(R.id.save_btn);
        logoutTextView = root.findViewById(R.id.logout_text_view);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            if (requestCode == Constants.REQUEST_UPLOAD_PHOTO && data != null) {
                final Uri selectedImage = data.getData();
                if (selectedImage != null) {
                    uploadResultImageView.setImageResource(R.drawable.ic_image_green);
                    uploadResultImageView.setVisibility(View.VISIBLE);
                    photoEditText.setBackgroundResource(R.drawable.edit_text_active);
                }
            }
        }
    }
}