package uz.alexits.cargostar.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
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
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.view.activity.InitializationActivity;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.UiUtils;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.viewmodel.ProfileViewModel;
import uz.alexits.cargostar.viewmodel.factory.ProfileViewModelFactory;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class ProfileFragment extends Fragment {
    /* header views */
    private TextView fullNameTextView;
    private TextView branchTextView;
    private TextView courierIdTextView;
    private EditText requestSearchEditText;
    private ImageView requestSearchImageView;
    private ImageView profileImageView;
    private ImageView createUserImageView;
    private ImageView calculatorImageView;
    private ImageView editProfileImageView;
    private ImageView notificationsImageView;
    private TextView badgeCounterTextView;

    /* content views */
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
    private EditText cityEditText;
    private EditText zipEditText;
    private EditText photoEditText;
    private ImageView uploadPhotoImageView;
    private ImageView uploadResultImageView;
    private Button saveBtn;
    private TextView logoutTextView;

    private ProgressBar progressBar;

    private ProfileViewModel profileViewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ProfileViewModelFactory profileFactory = new ProfileViewModelFactory(requireContext());
        profileViewModel = new ViewModelProvider(getViewModelStore(), profileFactory).get(ProfileViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_profile, container, false);

        /* header views */
        fullNameTextView = requireActivity().findViewById(R.id.full_name_text_view);
        branchTextView = requireActivity().findViewById(R.id.branch_text_view);
        courierIdTextView = requireActivity().findViewById(R.id.courier_id_text_view);
        requestSearchEditText = requireActivity().findViewById(R.id.search_edit_text);
        requestSearchImageView = requireActivity().findViewById(R.id.search_btn);
        profileImageView = requireActivity().findViewById(R.id.profile_image_view);
        createUserImageView = requireActivity().findViewById(R.id.create_user_image_view);
        calculatorImageView = requireActivity().findViewById(R.id.calculator_image_view);
        editProfileImageView = requireActivity().findViewById(R.id.edit_image_view);
        notificationsImageView = requireActivity().findViewById(R.id.notifications_image_view);
        badgeCounterTextView = requireActivity().findViewById(R.id.badge_counter_text_view);

        /* content views */
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
        cityEditText = root.findViewById(R.id.city_edit_text);
        zipEditText = root.findViewById(R.id.zip_edit_text);
        photoEditText = root.findViewById(R.id.photo_edit_text);
        uploadPhotoImageView = root.findViewById(R.id.photo_image_view);
        uploadResultImageView = root.findViewById(R.id.photo_result_image_view);
        saveBtn = root.findViewById(R.id.save_btn);
        logoutTextView = root.findViewById(R.id.logout_text_view);

        progressBar = root.findViewById(R.id.progress_bar);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //header views
        profileImageView.setOnClickListener(v -> {
            UiUtils.getNavController(requireActivity(), R.id.main_fragment_container).navigate(R.id.mainFragment);
        });

        createUserImageView.setOnClickListener(v -> {
            UiUtils.getNavController(requireActivity(), R.id.main_fragment_container).navigate(R.id.createUserFragment);
        });

        notificationsImageView.setOnClickListener(v -> {
            UiUtils.getNavController(requireActivity(), R.id.main_fragment_container).navigate(R.id.notificationsFragment);
        });

        calculatorImageView.setOnClickListener(v -> {
            UiUtils.getNavController(requireActivity(), R.id.main_fragment_container).navigate(R.id.calculatorFragment);
        });

        editProfileImageView.setOnClickListener(null);

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
        cityEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(cityEditText, hasFocus);
        });
        zipEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(zipEditText, hasFocus);
        });

        uploadPhotoImageView.setOnClickListener(v -> {
            final Intent pickFromGallery = new Intent();
            pickFromGallery.setType("image/*");
            pickFromGallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(pickFromGallery, "Выберите файл"), IntentConstants.REQUEST_UPLOAD_PHOTO);
        });

        saveBtn.setOnClickListener(v -> {
            final String login = loginEditText.getText().toString().trim();
            final String email = emailEditText.getText().toString().trim();
            final String password = passwordEditText.getText().toString().trim();
            final String firstName = firstNameEditText.getText().toString().trim();
            final String middleName = middleNameEditText.getText().toString().trim();
            final String lastName = lastNameEditText.getText().toString().trim();
            final String phone = phoneEditText.getText().toString().trim();
            final String photo = photoEditText.getText().toString().trim();

            if (TextUtils.isEmpty(login)) {
                Toast.makeText(getContext(), "Пароль не может быть пустым", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getContext(), "Имя не указано или указано не полностью", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Пароль не может быть пустым", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
                Toast.makeText(getContext(), "Имя не указано или указано не полностью", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(phone)) {
                Toast.makeText(getContext(), "Номер телефона не указан", Toast.LENGTH_SHORT).show();
                return;
            }
            profileViewModel.editProfileData(login, email, password, firstName, middleName, lastName, phone, photo);
        });

        logoutTextView.setOnClickListener(v -> {
            final String login = SharedPrefs.getInstance(requireContext()).getString(Constants.KEY_LOGIN);
            final String password = SharedPrefs.getInstance(requireContext()).getString(Constants.KEY_PASSWORD);

            SharedPrefs.getInstance(requireContext()).putLong(SharedPrefs.ID, 0L);
            SharedPrefs.getInstance(requireContext()).putLong(SharedPrefs.BRANCH_ID, 0L);
            SharedPrefs.getInstance(requireContext()).putString(Constants.KEY_LOGIN, null);
            SharedPrefs.getInstance(requireContext()).putString(Constants.KEY_PASSWORD, null);
            SharedPrefs.getInstance(requireContext()).putString(Constants.KEY_TOKEN, null);
            SharedPrefs.getInstance(requireContext()).putBoolean(SharedPrefs.KEEP_LOGGED, false);

            final Intent logoutIntent = new Intent(requireContext(), InitializationActivity.class);
            logoutIntent.putExtra(Constants.KEY_LOGIN, login);
            logoutIntent.putExtra(Constants.KEY_PASSWORD, password);
            logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(logoutIntent);
            requireActivity().setResult(Activity.RESULT_OK, null);
            requireActivity().finish();
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* header */
        profileViewModel.getCourierData(requireContext()).observe(getViewLifecycleOwner(), courier -> {
            if (courier != null) {
                profileViewModel.setCourierCountryId(courier.getCountryId());

                fullNameTextView.setText(getString(R.string.header_courier_full_name, courier.getFirstName(), courier.getLastName()));
                courierIdTextView.setText(getString(R.string.courier_id_placeholder, courier.getId()));

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
                if (!TextUtils.isEmpty(courier.getCityName())) {
                    cityEditText.setText(courier.getCityName());
                    cityEditText.setBackgroundResource(R.drawable.edit_text_active);
                }
                else {
                    cityEditText.setBackgroundResource(R.drawable.edit_text_locked);
                }
            }
        });

        profileViewModel.getBrancheData(requireContext()).observe(getViewLifecycleOwner(), branch -> {
            if (branch != null) {
                branchTextView.setText(getString(R.string.header_branch_name, branch.getName()));

                addressEditText.setText(branch.getAddress());
                addressEditText.setBackgroundResource(R.drawable.edit_text_active);
                zipEditText.setText(branch.getZip());
                zipEditText.setBackgroundResource(R.drawable.edit_text_active);
                geolocationEditText.setText(branch.getGeolocation());
                geolocationEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
        });

        profileViewModel.selectNewNotificationsCount().observe(getViewLifecycleOwner(), newNotificationsCount -> {
            if (newNotificationsCount != null) {
                badgeCounterTextView.setText(String.valueOf(newNotificationsCount));
            }
        });

        profileViewModel.getSearchRequestResult(requireContext()).observe(getViewLifecycleOwner(), workInfo -> {
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

        /* Location Data View Model */
        profileViewModel.getProfileCountry().observe(getViewLifecycleOwner(), country -> {
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

        profileViewModel.getEditProfileResult(requireContext()).observe(getViewLifecycleOwner(), workInfo -> {
            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                progressBar.setVisibility(View.INVISIBLE);
                saveBtn.setEnabled(true);
                Toast.makeText(requireContext(), "Изменения приняты", Toast.LENGTH_SHORT).show();

                NavHostFragment.findNavController(this).navigate(R.id.action_profileFragment_to_mainFragment);
                NavHostFragment.findNavController(this).popBackStack();
                return;
            }
            if (workInfo.getState() == WorkInfo.State.FAILED || workInfo.getState() == WorkInfo.State.CANCELLED) {
                Log.e(TAG, "insertLocationData(): failed to insert location data");
                progressBar.setVisibility(View.INVISIBLE);
                saveBtn.setEnabled(true);

                Toast.makeText(requireContext(), "Не удалось обновить данные", Toast.LENGTH_SHORT).show();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            saveBtn.setEnabled(false);
        });
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