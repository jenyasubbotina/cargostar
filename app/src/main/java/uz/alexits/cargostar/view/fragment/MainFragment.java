package uz.alexits.cargostar.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import uz.alexits.cargostar.R;

import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.viewmodel.HeaderViewModel;
import uz.alexits.cargostar.view.Constants;
import uz.alexits.cargostar.view.activity.CalculatorActivity;
import uz.alexits.cargostar.view.activity.CreateParcelActivity;
import uz.alexits.cargostar.view.activity.CreateUserActivity;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.activity.NotificationsActivity;
import uz.alexits.cargostar.view.activity.ProfileActivity;
import uz.alexits.cargostar.view.activity.ScanQrActivity;
import uz.alexits.cargostar.view.fragment.MainFragmentDirections;
import uz.alexits.cargostar.viewmodel.LocationDataViewModel;
import uz.alexits.cargostar.workers.SyncWorkRequest;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class MainFragment extends Fragment {
    private FragmentActivity activity;
    private Context context;
    //header views
    private TextView fullNameTextView;
    private TextView branchTextView;
    private EditText parcelSearchEditText;
    private ImageView parcelSearchImageView;
    private ImageView editImageView;
    private ImageView createUserImageView;
    private ImageView calculatorImageView;
    private ImageView notificationsImageView;
    private TextView badgeCounterTextView;
    //main content views
    private ImageView publicBidsImageView;
    private ImageView myBidsImageView;
    private ImageView createParcelImageView;
    private ImageView currentParcelsImageView;
    private ImageView scanParcelsImageView;
    private ImageView parcelDeliveryImageView;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        context = getContext();

        //todo: fetch countries, regions, cities
//        SyncWorkRequest.fetchLocationData(getContext(), 20000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_main, container, false);

        //header views
        fullNameTextView = activity.findViewById(R.id.full_name_text_view);
        branchTextView = activity.findViewById(R.id.branch_text_view);
        parcelSearchEditText = activity.findViewById(R.id.search_edit_text);
        parcelSearchImageView = activity.findViewById(R.id.search_btn);
        editImageView = activity.findViewById(R.id.edit_image_view);
        createUserImageView = activity.findViewById(R.id.create_user_image_view);
        calculatorImageView = activity.findViewById(R.id.calculator_image_view);
        notificationsImageView = activity.findViewById(R.id.notifications_image_view);
        badgeCounterTextView = activity.findViewById(R.id.badge_counter_text_view);
        //main content views
        publicBidsImageView = root.findViewById(R.id.public_bids_image_view);
        myBidsImageView = root.findViewById(R.id.my_bids_image_view);
        createParcelImageView = root.findViewById(R.id.create_parcel_image_view);
        currentParcelsImageView = root.findViewById(R.id.current_parcels_image_view);
        scanParcelsImageView = root.findViewById(R.id.scan_parcels_image_view);
        parcelDeliveryImageView = root.findViewById(R.id.parcel_delivery_image_view);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //header views
        editImageView.setOnClickListener(v -> {
            startActivity(new Intent(context, ProfileActivity.class));
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
        publicBidsImageView.setOnClickListener(v -> {
            NavHostFragment.findNavController(MainFragment.this).navigate(R.id.action_mainFragment_to_publicBidsFragment);
        });

        myBidsImageView.setOnClickListener(v -> {
            NavHostFragment.findNavController(MainFragment.this).navigate(R.id.action_mainFragment_to_myBidsFragment);
        });

        createParcelImageView.setOnClickListener(v -> {
            startActivity(new Intent(context, CreateParcelActivity.class));
        });

        currentParcelsImageView.setOnClickListener(v -> {
            NavHostFragment.findNavController(MainFragment.this).navigate(R.id.action_mainFragment_to_currentParcelsFragment);
        });

        scanParcelsImageView.setOnClickListener(v -> {
//            NavHostFragment.findNavController(MainFragment.this).navigate(R.id.action_mainFragment_to_scanQrActivity);
            final Intent scanQrIntent = new Intent(context, ScanQrActivity.class);
            startActivityForResult(scanQrIntent, Constants.REQUEST_SCAN_QR_MENU);
        });

        parcelDeliveryImageView.setOnClickListener(v -> {
            //TODO: select those current parcels with final destination equals to courier's transit point & status equala IN_TRANSIT
            final MainFragmentDirections.ActionMainFragmentToCurrentParcelsFragment action =
                    MainFragmentDirections.actionMainFragmentToCurrentParcelsFragment();
            action.setStatusFlag(Constants.IN_TRANSIT);
            NavHostFragment.findNavController(this).navigate(action);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //header views
        final HeaderViewModel headerViewModel = new ViewModelProvider(this).get(HeaderViewModel.class);

        headerViewModel.selectCourierByLogin(SharedPrefs.getInstance(context).getString(SharedPrefs.LOGIN)).observe(getViewLifecycleOwner(), courier -> {
            if (courier != null) {
                fullNameTextView.setText(courier.getFirstName() + " " + courier.getLastName());
            }
        });
        headerViewModel.selectBrancheById(SharedPrefs.getInstance(context).getLong(SharedPrefs.ID)).observe(getViewLifecycleOwner(), branch -> {
            if (branch != null) {
                branchTextView.setText(getString(R.string.branch) + " \"" + branch.getName() + "\"");
            }
        });
        headerViewModel.selectNewNotificationsCount().observe(getViewLifecycleOwner(), newNotificationsCount -> {
            if (newNotificationsCount != null) {
                badgeCounterTextView.setText(String.valueOf(newNotificationsCount));
            }
        });
        //location data view model
        final LocationDataViewModel locationDataViewModel = new ViewModelProvider(this).get(LocationDataViewModel.class);

        locationDataViewModel.getCountryList().observe(getViewLifecycleOwner(), countryList -> {
            Log.i(TAG, "countryList: " + countryList.size());
        });

        locationDataViewModel.getRegionList().observe(getViewLifecycleOwner(), regionList -> {
            Log.i(TAG, "regionList: " + regionList.size());
        });

        locationDataViewModel.getCityList().observe(getViewLifecycleOwner(), cityList -> {
            Log.i(TAG, "cityList: " + cityList.size());
        });

        locationDataViewModel.getBrancheList().observe(getViewLifecycleOwner(), brancheList -> {
            Log.i(TAG, "brancheList: " + brancheList.size());
        });

        parcelSearchImageView.setOnClickListener(v -> {
            final String parcelIdStr = parcelSearchEditText.getText().toString();

            if (TextUtils.isEmpty(parcelIdStr)) {
                Toast.makeText(context, "Введите ID перевозки или номер накладной", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isDigitsOnly(parcelIdStr)) {
                Toast.makeText(context, "Неверный формат", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                final long parcelId = Long.parseLong(parcelIdStr);
                headerViewModel.selectRequest(parcelId).observe(getViewLifecycleOwner(), receiptWithCargoList -> {
                    if (receiptWithCargoList == null) {
                        Toast.makeText(context, "Накладной не существует", Toast.LENGTH_SHORT).show();
                        return;
                    }
//                    if (receiptWithCargoList.getReceipt() == null) {
//                        Toast.makeText(context, "Накладной не существует", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                    final Intent mainIntent = new Intent(context, MainActivity.class);
                    mainIntent.putExtra(Constants.INTENT_REQUEST_KEY, Constants.REQUEST_FIND_PARCEL);
                    mainIntent.putExtra(Constants.INTENT_REQUEST_VALUE, parcelId);
                    startActivity(mainIntent);
                });
            }
            catch (Exception e) {
                Log.e(TAG, "parseLong(): ", e);
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == Constants.REQUEST_SCAN_QR_MENU) {
                final MainFragmentDirections.ActionMainFragmentToParcelDataFragment action =
                        MainFragmentDirections.actionMainFragmentToParcelDataFragment();
                action.setParcelId(1);
                action.setParcelId(Constants.INTENT_PARCEL);
                NavHostFragment.findNavController(this).navigate(action);
            }
        }
    }

    private static final String TAG = MainFragment.class.toString();
}