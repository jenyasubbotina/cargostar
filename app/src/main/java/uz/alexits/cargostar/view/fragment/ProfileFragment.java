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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import uz.alexits.cargostar.R;

import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.actor.Courier;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.utils.ImageSerializer;
import uz.alexits.cargostar.viewmodel.CourierViewModel;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.utils.UiUtils;
import uz.alexits.cargostar.view.activity.CalculatorActivity;
import uz.alexits.cargostar.view.activity.CreateUserActivity;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.activity.NotificationsActivity;
import uz.alexits.cargostar.view.activity.SignInActivity;
import uz.alexits.cargostar.viewmodel.LocationDataViewModel;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class ProfileFragment extends Fragment {
    private FragmentActivity activity;
    private Context context;
    private CourierViewModel courierViewModel;
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

    private ProgressBar progressBar;

    private static Courier currentCourier = null;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        context = getContext();
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
            startActivityForResult(Intent.createChooser(pickFromGallery, "Выберите файл"), IntentConstants.REQUEST_UPLOAD_PHOTO);
        });

        saveBtn.setOnClickListener(v -> {
            final String password = passwordEditText.getText().toString();
            final String firstName = firstNameEditText.getText().toString();
            final String middleName = middleNameEditText.getText().toString();
            final String lastName = lastNameEditText.getText().toString();
            final String phone = phoneEditText.getText().toString();
            final String photo = photoEditText.getText().toString();

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Пароль не может быть пустым", Toast.LENGTH_SHORT).show();
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

            final String pwd = !TextUtils.isEmpty(password) ? password : currentCourier.getPassword();

            final UUID updateCourierRequestId = SyncWorkRequest.updateCourierData(
                    context,
                    currentCourier.getId(),
                    currentCourier.getLogin(),
                    currentCourier.getEmail(),
                    pwd,
                    !TextUtils.isEmpty(firstName) ? firstName : currentCourier.getFirstName(),
                    !TextUtils.isEmpty(middleName) ? middleName : currentCourier.getMiddleName(),
                    !TextUtils.isEmpty(lastName) ? lastName : currentCourier.getLastName(),
                    !TextUtils.isEmpty(phone) ? phone : currentCourier.getPhone(),
                    !TextUtils.isEmpty(photo) ? photo : currentCourier.getPhotoUrl());

            WorkManager.getInstance(context).getWorkInfoByIdLiveData(updateCourierRequestId).observe(getViewLifecycleOwner(), workInfo -> {
                if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                    progressBar.setVisibility(View.INVISIBLE);
                    saveBtn.setEnabled(true);

                    SharedPrefs.getInstance(context).putString(SharedPrefs.PASSWORD_HASH, pwd);

                    startActivity(new Intent(context, MainActivity.class));
                    activity.finish();

                    Toast.makeText(context, "Изменения приняты", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (workInfo.getState() == WorkInfo.State.FAILED || workInfo.getState() == WorkInfo.State.CANCELLED) {
                    Log.e(TAG, "insertLocationData(): failed to insert location data");
                    Toast.makeText(context, "Не удалось обновить данные", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    saveBtn.setEnabled(true);
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                saveBtn.setEnabled(false);
            });
        });

        logoutTextView.setOnClickListener(v -> {
            SharedPrefs.getInstance(activity).putString(SharedPrefs.LOGIN, null);
            SharedPrefs.getInstance(activity).putLong(SharedPrefs.ID, 0);
            SharedPrefs.getInstance(activity).putLong(SharedPrefs.BRANCH_ID, 0);
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

        courierViewModel = new ViewModelProvider(this).get(CourierViewModel.class);
        final LocationDataViewModel locationDataViewModel = new ViewModelProvider(this).get(LocationDataViewModel.class);

        final String login = SharedPrefs.getInstance(getContext()).getString(SharedPrefs.LOGIN);

        courierViewModel.selectCourierByLogin(login).observe(getViewLifecycleOwner(), courier -> {
            if (courier != null) {
                currentCourier = courier;
                courierViewModel.setCourierCountryId(courier.getCountryId());
                courierViewModel.setCourierRegionId(courier.getRegionId());
                courierViewModel.setCourierCityId(courier.getCityId());

                userIdEditText.setText(String.valueOf(courier.getId()));
                userIdEditText.setBackgroundResource(R.drawable.edit_text_active);
                if (!TextUtils.isEmpty(courier.getLogin())) {
                    loginEditText.setText(courier.getLogin());
                    loginEditText.setBackgroundResource(R.drawable.edit_text_active);
                }
                else {
                    loginEditText.setBackgroundResource(R.drawable.edit_text_locked);
                }
                if (!TextUtils.isEmpty(courier.getPassword())) {
                    passwordEditText.setText(courier.getPassword());
                    passwordEditText.setBackgroundResource(R.drawable.edit_text_active);
                }
                else {
                    passwordEditText.setBackgroundResource(R.drawable.edit_text_locked);
                }
                if (!TextUtils.isEmpty(courier.getEmail())) {
                    emailEditText.setText(courier.getEmail());
                    emailEditText.setBackgroundResource(R.drawable.edit_text_active);
                }
                else {
                    emailEditText.setBackgroundResource(R.drawable.edit_text_locked);
                }
                if (!TextUtils.isEmpty(courier.getPosition())) {
                    positionEditText.setText(courier.getPosition());
                    positionEditText.setBackgroundResource(R.drawable.edit_text_active);
                }
                else {
                    positionEditText.setBackgroundResource(R.drawable.edit_text_locked);
                }
                if (!TextUtils.isEmpty(courier.getFirstName())) {
                    firstNameEditText.setText(courier.getFirstName());
                    firstNameEditText.setBackgroundResource(R.drawable.edit_text_active);
                }
                else {
                    firstNameEditText.setBackgroundResource(R.drawable.edit_text_locked);
                }
                if (!TextUtils.isEmpty(courier.getMiddleName())) {
                    middleNameEditText.setText(courier.getMiddleName());
                    middleNameEditText.setBackgroundResource(R.drawable.edit_text_active);
                }
                else {
                    middleNameEditText.setBackgroundResource(R.drawable.edit_text_locked);
                }
                if (!TextUtils.isEmpty(courier.getLastName())) {
                    lastNameEditText.setText(courier.getLastName());
                    lastNameEditText.setBackgroundResource(R.drawable.edit_text_active);
                }
                else {
                    lastNameEditText.setBackgroundResource(R.drawable.edit_text_locked);
                }
                if (!TextUtils.isEmpty(courier.getPhone())) {
                    phoneEditText.setText(courier.getPhone());
                    phoneEditText.setBackgroundResource(R.drawable.edit_text_active);
                }
                else {
                    phoneEditText.setBackgroundResource(R.drawable.edit_text_locked);
                }
                if (courier.getPhotoUrl() != null) {
                    photoEditText.setText(courier.getPhotoUrl());
                    photoEditText.setBackgroundResource(R.drawable.edit_text_active);
                }
                else {
                    photoEditText.setBackgroundResource(R.drawable.edit_text_locked);
                }
                fullNameTextView.setText(courier.getFirstName() + " " + courier.getLastName());
            }
        });

        courierViewModel.selectBrancheById(SharedPrefs.getInstance(context).getLong(SharedPrefs.BRANCH_ID)).observe(getViewLifecycleOwner(), branch -> {
            Log.i(ProfileFragment.class.toString(), "branch" + branch);
            if (branch != null) {
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
        courierViewModel.selectNewNotificationsCount().observe(getViewLifecycleOwner(), newNotificationsCount -> {
            if (newNotificationsCount != null) {
                badgeCounterTextView.setText(String.valueOf(newNotificationsCount));
            }
        });

        /* Location Data View Model */
        courierViewModel.getCourierCountry().observe(getViewLifecycleOwner(), country -> {
            if (country != null) {
                if (!TextUtils.isEmpty(country.getName())) {
                    countryEditText.setText(country.getName());
                    countryEditText.setBackgroundResource(R.drawable.edit_text_active);
                }
                else {
                    countryEditText.setBackgroundResource(R.drawable.edit_text_locked);
                }
            }
        });
        courierViewModel.getCourierRegion().observe(getViewLifecycleOwner(), region -> {
            if (region != null) {
                if (!TextUtils.isEmpty(region.getName())) {
                    regionEditText.setText(region.getName());
                    regionEditText.setBackgroundResource(R.drawable.edit_text_active);
                }
                else {
                    regionEditText.setBackgroundResource(R.drawable.edit_text_locked);
                }
            }
        });
        courierViewModel.getCourierCity().observe(getViewLifecycleOwner(), city ->  {
            if (city != null) {
                if (!TextUtils.isEmpty(city.getName())) {
                    cityEditText.setText(city.getName());
                    cityEditText.setBackgroundResource(R.drawable.edit_text_active);
                }
                else {
                    cityEditText.setBackgroundResource(R.drawable.edit_text_locked);
                }
            }
        });

        parcelSearchImageView.setOnClickListener(v -> {
            final String invoiceIdStr = parcelSearchEditText.getText().toString();

            if (TextUtils.isEmpty(invoiceIdStr)) {
                Toast.makeText(context, "Введите ID перевозки или номер накладной", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isDigitsOnly(invoiceIdStr)) {
                Toast.makeText(context, "Неверный формат", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                final UUID searchInvoiceUUID = SyncWorkRequest.searchInvoice(context, Long.parseLong(invoiceIdStr));
                WorkManager.getInstance(context).getWorkInfoByIdLiveData(searchInvoiceUUID).observe(getViewLifecycleOwner(), workInfo -> {
                    if (workInfo.getState() == WorkInfo.State.FAILED || workInfo.getState() == WorkInfo.State.CANCELLED) {
                        Toast.makeText(context, "Накладной не существует", Toast.LENGTH_SHORT).show();

                        parcelSearchEditText.setEnabled(true);

                        return;
                    }
                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        final Data outputData = workInfo.getOutputData();

                        final long requestId = outputData.getLong(Constants.KEY_REQUEST_ID, -1L);
                        final long invoiceId = outputData.getLong(Constants.KEY_INVOICE_ID, -1L);
                        final long clientId = outputData.getLong(Constants.KEY_CLIENT_ID, -1L);
                        final long senderCountryId = outputData.getLong(Constants.KEY_SENDER_COUNTRY_ID, -1L);
                        final long senderRegionId = outputData.getLong(Constants.KEY_SENDER_REGION_ID, -1L);
                        final long senderCityId = outputData.getLong(Constants.KEY_SENDER_CITY_ID, -1L);
                        final long recipientCountryId = outputData.getLong(Constants.KEY_RECIPIENT_COUNTRY_ID, -1L);
                        final long recipientCityId = outputData.getLong(Constants.KEY_RECIPIENT_CITY_ID, -1L);
                        final long providerId = outputData.getLong(Constants.KEY_PROVIDER_ID, -1L);

                        final Intent mainIntent = new Intent(context, MainActivity.class);
                        mainIntent.putExtra(IntentConstants.INTENT_REQUEST_KEY, IntentConstants.REQUEST_FIND_PARCEL);
                        mainIntent.putExtra(IntentConstants.INTENT_REQUEST_VALUE, requestId);
                        mainIntent.putExtra(Constants.KEY_REQUEST_ID, requestId);
                        mainIntent.putExtra(Constants.KEY_INVOICE_ID, invoiceId);
                        mainIntent.putExtra(Constants.KEY_COURIER_ID, requestId);
                        mainIntent.putExtra(Constants.KEY_CLIENT_ID, clientId);
                        mainIntent.putExtra(Constants.KEY_SENDER_COUNTRY_ID, senderCountryId);
                        mainIntent.putExtra(Constants.KEY_SENDER_REGION_ID, senderRegionId);
                        mainIntent.putExtra(Constants.KEY_SENDER_CITY_ID, senderCityId);
                        mainIntent.putExtra(Constants.KEY_RECIPIENT_COUNTRY_ID, recipientCountryId);
                        mainIntent.putExtra(Constants.KEY_RECIPIENT_CITY_ID, recipientCityId);
                        mainIntent.putExtra(Constants.KEY_PROVIDER_ID, providerId);
                        startActivity(mainIntent);

                        parcelSearchEditText.setEnabled(true);

                        return;
                    }
                    parcelSearchEditText.setEnabled(false);
                });
            }
            catch (Exception e) {
                Log.e(TAG, "getInvoiceById(): ", e);
                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
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

        progressBar = root.findViewById(R.id.progress_bar);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == -1) {
            if (requestCode == IntentConstants.REQUEST_UPLOAD_PHOTO) {
                if (data != null) {
                    final Uri selectedImage = data.getData();

                    if (selectedImage != null) {
                        uploadResultImageView.setImageResource(R.drawable.ic_image_green);
                        uploadResultImageView.setVisibility(View.VISIBLE);
                        photoEditText.setBackgroundResource(R.drawable.edit_text_active);

                        Log.i(TAG, "selected photo: " + selectedImage);
                        photoEditText.setText(selectedImage.toString());
                    }
                }
            }
        }
    }

    private static final String TAG = ProfileFragment.class.toString();
}