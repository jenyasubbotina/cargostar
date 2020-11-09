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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import uz.alexits.cargostar.R;

import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.location.TransitPoint;
import uz.alexits.cargostar.model.transportation.Transportation;
import uz.alexits.cargostar.viewmodel.TransportationViewModel;
import uz.alexits.cargostar.viewmodel.CourierViewModel;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.activity.CalculatorActivity;
import uz.alexits.cargostar.view.activity.CreateUserActivity;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.activity.NotificationsActivity;
import uz.alexits.cargostar.view.activity.ProfileActivity;
import uz.alexits.cargostar.view.activity.ScanQrActivity;
import uz.alexits.cargostar.view.adapter.TransportationAdapter;
import uz.alexits.cargostar.view.adapter.SpinnerAdapter;
import uz.alexits.cargostar.view.callback.TransportationCallback;
import uz.alexits.cargostar.workers.SyncWorkRequest;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class CurrentTransportationsFragment extends Fragment implements TransportationCallback {
    private Context context;
    private FragmentActivity activity;
    //viewModel
    private CourierViewModel courierViewModel;
    private TransportationViewModel transportationViewModel;
    //transportation statuses -> all
//    private static TransportationStatus[] statusArray;
    //header views
    private TextView fullNameTextView;
    private TextView branchTextView;
    private EditText parcelSearchEditText;
    private ImageView parcelSearchImageView;
    private ImageView profileImageView;
    private ImageView editImageView;
    private ImageView createUserImageView;
    private ImageView calculatorImageView;
    private ImageView notificationsImageView;
    private TextView badgeCounterTextView;
    //main content views
    private int statusFlag;

    private CheckBox inTransitCheckBox;
    private CheckBox onTheWayCheckBox;
    private CheckBox deliveredCheckBox;
    private CheckBox lostCheckBox;
    private CheckBox allCheckBox;

    private RelativeLayout transitPointField;
    private Spinner transitPointSpinner;
    private List<TransitPoint> transitPointList;

    private EditText qrCodeEditText;
    private ImageView scanQrImageView;

    private RecyclerView currentParcelsRecyclerView;
    private TransportationAdapter transportationAdapter;

    private static final List<Long> selectedStatusArray = new ArrayList<>();
    private static Long selectedTransitPointId = -1L;

    public CurrentTransportationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        activity = getActivity();

        if (getArguments() != null) {
            statusFlag = getArguments().getInt(IntentConstants.STATUS_FLAG);
        }

        SyncWorkRequest.fetchTransportationList(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_current_parcels, container, false);

        //header views
        fullNameTextView = activity.findViewById(R.id.full_name_text_view);
        branchTextView = activity.findViewById(R.id.branch_text_view);
        parcelSearchEditText = activity.findViewById(R.id.search_edit_text);
        parcelSearchImageView = activity.findViewById(R.id.search_btn);
        profileImageView = activity.findViewById(R.id.profile_image_view);
        editImageView = activity.findViewById(R.id.edit_image_view);
        createUserImageView = activity.findViewById(R.id.create_user_image_view);
        calculatorImageView = activity.findViewById(R.id.calculator_image_view);
        notificationsImageView = activity.findViewById(R.id.notifications_image_view);
        badgeCounterTextView = activity.findViewById(R.id.badge_counter_text_view);
        //main content views
        inTransitCheckBox = root.findViewById(R.id.in_transit_check_box);
        onTheWayCheckBox = root.findViewById(R.id.on_the_way_check_box);
        deliveredCheckBox = root.findViewById(R.id.delivered_check_box);
        lostCheckBox = root.findViewById(R.id.lost_check_box);
        allCheckBox = root.findViewById(R.id.all_check_box);

        qrCodeEditText = root.findViewById(R.id.qr_code_edit_text);
        scanQrImageView = root.findViewById(R.id.camera_image_view);
        transitPointField = root.findViewById(R.id.city_field);
        transitPointSpinner = root.findViewById(R.id.city_spinner);
        currentParcelsRecyclerView = root.findViewById(R.id.current_parcels_recycler_view);

        transportationAdapter = new TransportationAdapter(context, this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        currentParcelsRecyclerView.setLayoutManager(layoutManager);
        currentParcelsRecyclerView.setAdapter(transportationAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //header views
        profileImageView.setOnClickListener(v -> {
            startActivity(new Intent(context, MainActivity.class));
        });
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

        /* main content views */
        inTransitCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            initParcels(b, onTheWayCheckBox.isChecked(), deliveredCheckBox.isChecked(), lostCheckBox.isChecked(), allCheckBox.isChecked());
        });

        onTheWayCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            initParcels(inTransitCheckBox.isChecked(), b, deliveredCheckBox.isChecked(), lostCheckBox.isChecked(), allCheckBox.isChecked());
        });

        deliveredCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            initParcels(inTransitCheckBox.isChecked(), onTheWayCheckBox.isChecked(), b, lostCheckBox.isChecked(), allCheckBox.isChecked());
        });

        lostCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            initParcels(inTransitCheckBox.isChecked(), onTheWayCheckBox.isChecked(), deliveredCheckBox.isChecked(), b, allCheckBox.isChecked());
        });

        allCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            initParcels(inTransitCheckBox.isChecked(), onTheWayCheckBox.isChecked(), deliveredCheckBox.isChecked(), lostCheckBox.isChecked(), b);
        });

        //default checkBox values
//        inTransitCheckBox.setChecked(true);
//        onTheWayCheckBox.setChecked(true);

        //main content views
        transitPointSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView itemTextView = (TextView) view;
                final TransitPoint currentItem = (TransitPoint) adapterView.getItemAtPosition(i);

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        transitPointField.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
                selectedTransitPointId = currentItem.getId();
                transportationViewModel.setStatusArrayTransitPoint(selectedStatusArray, selectedTransitPointId);
//                if (inTransitCheckBox.isChecked()) {
//                    parcelsViewModel.selectParcelsByLocationAndStatus(SharedPrefs.getInstance(context).getLong(SharedPrefs.ID), statusArray, currentItem.getId()).observe(getViewLifecycleOwner(), parcelList -> {
//                        Log.i(TAG, "parcelList: " + parcelList);
//                        if (parcelList != null) {
//                            parcelAdapter.setParcelList(parcelList);
//                            parcelAdapter.notifyDataSetChanged();
//                        }
//                    });
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        scanQrImageView.setOnClickListener(v -> {
            final Intent scanQrIntent = new Intent(context, ScanQrActivity.class);
            startActivityForResult(scanQrIntent, IntentConstants.REQUEST_SCAN_QR_MENU);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        courierViewModel = new ViewModelProvider(this).get(CourierViewModel.class);
        transportationViewModel = new ViewModelProvider(this).get(TransportationViewModel.class);

        courierViewModel.selectCourierByLogin(SharedPrefs.getInstance(context).getString(SharedPrefs.LOGIN)).observe(getViewLifecycleOwner(), courier -> {
            if (courier != null) {
                fullNameTextView.setText(courier.getFirstName() + " " + courier.getLastName());
            }
        });
        courierViewModel.selectBrancheById(SharedPrefs.getInstance(context).getLong(SharedPrefs.BRANCH_ID)).observe(getViewLifecycleOwner(), branch -> {
            if (branch != null) {
                branchTextView.setText(getString(R.string.branch) + " \"" + branch.getName() + "\"");
            }
        });
        courierViewModel.selectNewNotificationsCount().observe(getViewLifecycleOwner(), newNotificationsCount -> {
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
        });

        /* transportation view model */
        transportationViewModel.selectAllTransitPoints().observe(getViewLifecycleOwner(), transitPointList -> {
            Log.i(TAG, "onActivityCreated(): " + transitPointList);
            if (transitPointList != null) {
                this.transitPointList = transitPointList;
                initCitySpinner(transitPointList);
            }
        });

        transportationViewModel.getCurrentTransportationList().observe(getViewLifecycleOwner(), transportationList -> {
            transportationAdapter.setTransportationList(transportationList);
            transportationAdapter.notifyDataSetChanged();

            for (final Transportation transportation : transportationList) {
                Log.i(TAG, "transportation: " + transportation);
            }
        });
    }

    private void initParcels(final boolean inTransit, final boolean onTheWay, final boolean delivered, final boolean lost, final boolean all) {
        if (all || (inTransit && onTheWay && delivered && lost)) {
            //in transit + on the way + delivered + lost
            Log.i(TAG, "statusList: in transit + on the way + delivered + lost");
            selectedStatusArray.clear();
            selectedStatusArray.add(3L);
            selectedStatusArray.add(4L);
            selectedStatusArray.add(5L);
            selectedStatusArray.add(6L);
        }
        else if (inTransit && !onTheWay && !delivered && !lost) {
            //in transit
            Log.i(TAG, "statusList: in transit");
            selectedStatusArray.clear();
            selectedStatusArray.add(3L);
        }
        else if (inTransit && onTheWay && !delivered && !lost) {
            //in transit + on the way
            Log.i(TAG, "statusList: in transit + on the way");
            selectedStatusArray.clear();
            selectedStatusArray.add(3L);
            selectedStatusArray.add(4L);
        }
        else if (inTransit && onTheWay && delivered) {
            //in transit + on the way + delivered
            Log.i(TAG, "statusList: in transit + on the way + delivered");
            selectedStatusArray.clear();
            selectedStatusArray.add(3L);
            selectedStatusArray.add(4L);
            selectedStatusArray.add(6L);
        }
        else if (inTransit && onTheWay) {
            //in transit + on the way + lost
            Log.i(TAG, "statusList: in transit + on the way + lost");
            selectedStatusArray.clear();
            selectedStatusArray.add(3L);
            selectedStatusArray.add(4L);
            selectedStatusArray.add(5L);
        }
        else if (inTransit && delivered && !lost) {
            //in transit + delivered
            Log.i(TAG, "statusList: in transit + delivered");
            selectedStatusArray.clear();
            selectedStatusArray.add(3L);
            selectedStatusArray.add(6L);
        }
        else if (inTransit && !delivered) {
            //in transit + lost
            Log.i(TAG, "statusList: in transit + lost");
            selectedStatusArray.clear();
            selectedStatusArray.add(3L);
            selectedStatusArray.add(5L);
       }
        else if (inTransit) {
            //in transit + delivered + lost
            Log.i(TAG, "statusList: in transit + delivered + lost");
            selectedStatusArray.clear();
            selectedStatusArray.add(3L);
            selectedStatusArray.add(5L);
            selectedStatusArray.add(6L);        }
        else if (onTheWay && !delivered && !lost) {
            //on the way
            Log.i(TAG, "statusList: on the way");
            selectedStatusArray.clear();
            selectedStatusArray.add(4L);        }
        else if (onTheWay && delivered && !lost) {
            //on the way + delivered
            Log.i(TAG, "statusList: on the way + delivered");
            selectedStatusArray.clear();
            selectedStatusArray.add(4L);
            selectedStatusArray.add(6L);        }
        else if (onTheWay && delivered) {
            //on the way + delivered + lost
            Log.i(TAG, "statusList: on the way + delivered + lost");
            selectedStatusArray.clear();
            selectedStatusArray.add(4L);
            selectedStatusArray.add(5L);
            selectedStatusArray.add(6L);
        }
        else if (onTheWay) {
            //on the way + lost
            Log.i(TAG, "statusList: on the way + lost");
            selectedStatusArray.clear();
            selectedStatusArray.add(4L);
            selectedStatusArray.add(5L);
        }
        else if (delivered && !lost) {
            //delivered
            Log.i(TAG, "statusList: delivered");
            selectedStatusArray.clear();
            selectedStatusArray.add(6L);
        }
        else if (delivered) {
            //delivered + lost
            Log.i(TAG, "statusList: delivered + lost");
            selectedStatusArray.clear();
            selectedStatusArray.add(5L);
            selectedStatusArray.add(6L);        }
        else if (lost) {
            //lost
            Log.i(TAG, "statusList: lost");
            selectedStatusArray.clear();
            selectedStatusArray.add(5L);
        }
        else {
            //nothing
            Log.i(TAG, "statusList: nothing");
            selectedStatusArray.clear();
        }
        transportationViewModel.setStatusArrayTransitPoint(selectedStatusArray, selectedTransitPointId);
    }

    @Override
    public void onTransportationSelected(Transportation currentItem) {
        Log.i(TAG, "currentItem: " + currentItem);
        final CurrentTransportationsFragmentDirections.ActionCurrentParcelsFragmentToParcelStatusFragment action =
                CurrentTransportationsFragmentDirections.actionCurrentParcelsFragmentToParcelStatusFragment(
                        currentItem.getQrCode(),
                        currentItem.getTrackingCode(),
                        currentItem.getTransportationStatusName(),
                        currentItem.getCityFrom(),
                        currentItem.getCityTo(),
                        currentItem.getPartyQrCode(),
                        currentItem.getInstructions(),
                        currentItem.getDirection(),
                        currentItem.getArrivalDate());

        action.setTransportationId(currentItem.getId());
        action.setInvoiceId(currentItem.getInvoiceId() != null ? currentItem.getInvoiceId() : -1L);
        action.setTransportationStatusId(currentItem.getTransportationStatusId() != null ? currentItem.getTransportationStatusId() : -1L);
        action.setTransportationStatusName(currentItem.getTransportationStatusName());
        action.setPaymentStatusId(currentItem.getPaymentStatusId() != null ? currentItem.getPaymentStatusId() : -1L);
        action.setTrackingCode(currentItem.getTrackingCode());
        action.setQrCode(currentItem.getQrCode());
        action.setPartyQrCode(currentItem.getPartyQrCode());
        action.setCurrentTransitPointId(currentItem.getCurrentTransitionPointId() != null ? currentItem.getCurrentTransitionPointId() : -1L);
        action.setCityFrom(currentItem.getCityFrom());
        action.setCityTo(currentItem.getCityTo());
        action.setCourierId(currentItem.getCourierId() != null ? currentItem.getCourierId() : -1L);
        action.setProviderId(currentItem.getProviderId() != null ? currentItem.getProviderId() : -1L);
        action.setInstructions(currentItem.getInstructions());
        action.setArrivalDate(currentItem.getArrivalDate());
        action.setDirection(currentItem.getDirection());

        NavHostFragment.findNavController(this).navigate(action);
    }

    private void initCitySpinner(final List<TransitPoint> transitPointList) {
        final SpinnerAdapter<TransitPoint> cityAdapter = new SpinnerAdapter<>(context, R.layout.spinner_item, transitPointList);
        cityAdapter.setDropDownViewResource(R.layout.spinner_item);
        transitPointSpinner.setAdapter(cityAdapter);
        cityAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == IntentConstants.REQUEST_SCAN_QR_MENU) {
                final CurrentTransportationsFragmentDirections.ActionCurrentParcelsFragmentToParcelDataFragment action =
                        CurrentTransportationsFragmentDirections.actionCurrentParcelsFragmentToParcelDataFragment();
                action.setRequestId(1);
                action.setRequestOrParcel(IntentConstants.INTENT_PARCEL);
                NavHostFragment.findNavController(this).navigate(action);
            }
        }
    }

    private static final String TAG = CurrentTransportationsFragment.class.toString();
}