package uz.alexits.cargostar.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

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
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.view.UiUtils;
import uz.alexits.cargostar.viewmodel.TransportationViewModel;
import uz.alexits.cargostar.viewmodel.CourierViewModel;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.activity.ScanQrActivity;
import uz.alexits.cargostar.view.adapter.TransportationAdapter;
import uz.alexits.cargostar.view.adapter.CustomArrayAdapter;
import uz.alexits.cargostar.view.callback.TransportationCallback;
import uz.alexits.cargostar.workers.SyncWorkRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class CurrentTransportationsFragment extends Fragment implements TransportationCallback {
    private Context context;
    private FragmentActivity activity;

    //header views
    private TextView fullNameTextView;
    private TextView branchTextView;
    private TextView courierIdTextView;
    private EditText requestSearchEditText;
    private ImageView requestSearchImageView;
    private ImageView profileImageView;
    private ImageView editImageView;
    private ImageView createUserImageView;
    private ImageView calculatorImageView;
    private ImageView notificationsImageView;
    private TextView badgeCounterTextView;

    //main content views
    private CheckBox inTransitCheckBox;
    private CheckBox onTheWayCheckBox;
    private CheckBox deliveredCheckBox;
    private CheckBox lostCheckBox;
    private CheckBox allCheckBox;

    private RelativeLayout transitPointField;
    private Spinner transitPointSpinner;

    private EditText qrCodeEditText;
    private ImageView scanQrImageView;

    private RecyclerView currentTransportationListRecyclerView;
    private TransportationAdapter transportationAdapter;

    //view model
    private CourierViewModel courierViewModel;
    private TransportationViewModel transportationViewModel;

    private static final List<Long> selectedStatusArray = new ArrayList<>();
    private static volatile long selectedTransitPointId = -1;
    private static volatile long courierBrancheId = -1;
    private static volatile int statusFlag = -1;

    private static volatile boolean isFirstCheckBoxPick = true;
    private static volatile boolean isFirstSpinnerPick = true;

    public CurrentTransportationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        activity = getActivity();

        isFirstCheckBoxPick = true;
        isFirstSpinnerPick = true;

        if (getArguments() != null) {
            statusFlag = CurrentTransportationsFragmentArgs.fromBundle(getArguments()).getStatusFlag();
            courierBrancheId = CurrentTransportationsFragmentArgs.fromBundle(getArguments()).getCourierBranchId();
        }

        SyncWorkRequest.fetchTransportationList(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_current_parcels, container, false);

        //header views
        fullNameTextView = activity.findViewById(R.id.full_name_text_view);
        branchTextView = activity.findViewById(R.id.branch_text_view);
        courierIdTextView = activity.findViewById(R.id.courier_id_text_view);
        requestSearchEditText = activity.findViewById(R.id.search_edit_text);
        requestSearchImageView = activity.findViewById(R.id.search_btn);
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
        currentTransportationListRecyclerView = root.findViewById(R.id.current_parcels_recycler_view);

        transportationAdapter = new TransportationAdapter(context, this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        currentTransportationListRecyclerView.setLayoutManager(layoutManager);
        currentTransportationListRecyclerView.setHasFixedSize(true);
        currentTransportationListRecyclerView.setAdapter(transportationAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //header views
        profileImageView.setOnClickListener(v -> {
            startActivity(new Intent(context, MainActivity.class));
        });

        profileImageView.setOnClickListener(v -> {
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(R.id.mainFragment);
        });

        createUserImageView.setOnClickListener(v -> {
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(R.id.createUserFragment);
        });

        notificationsImageView.setOnClickListener(v -> {
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(R.id.notificationsFragment);

        });

        calculatorImageView.setOnClickListener(v -> {
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(R.id.calculatorFragment);
        });

        editImageView.setOnClickListener(v -> {
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(R.id.profileFragment);
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

        /* transportation view model */
        transportationViewModel.selectAllTransitPoints().observe(getViewLifecycleOwner(), transitPointList -> {
            if (transitPointList != null) {
                initCitySpinner(transitPointList);

                if (isFirstSpinnerPick) {
                    isFirstSpinnerPick = false;

                    for (int i = 0; i < transitPointList.size(); i++) {
                        if (transitPointList.get(i).getBrancheId().compareTo(courierBrancheId) == 0) {
                            transitPointSpinner.setSelection(i);
                            return;
                        }
                    }
                }
            }
        });

        transportationViewModel.getCurrentTransportationList().observe(getViewLifecycleOwner(), transportationList -> {
            if (transportationList != null) {
                transportationAdapter.setTransportationList(transportationList);
                transportationAdapter.notifyDataSetChanged();

                if (isFirstCheckBoxPick) {
                    isFirstCheckBoxPick = false;

                    if (statusFlag == IntentConstants.FRAGMENT_CURRENT_TRANSPORT) {
                        inTransitCheckBox.setChecked(true);
                        onTheWayCheckBox.setChecked(true);
                        return;
                    }
                    if (statusFlag == IntentConstants.FRAGMENT_DELIVERY_TRANSPORT) {
                        deliveredCheckBox.setChecked(true);
                    }
                }
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
            selectedStatusArray.add(6L);
        }
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
        final CurrentTransportationsFragmentDirections.ActionCurrentTransportationsFragmentToTransportationStatusFragment action =
                CurrentTransportationsFragmentDirections.actionCurrentTransportationsFragmentToTransportationStatusFragment(
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
        action.setRequestId(currentItem.getRequestId() != null ? currentItem.getRequestId() : -1L);
        action.setTransportationStatusId(currentItem.getTransportationStatusId() != null ? currentItem.getTransportationStatusId() : -1L);
        action.setTransportationStatusName(currentItem.getTransportationStatusName());
        action.setPaymentStatusId(currentItem.getPaymentStatusId() != null ? currentItem.getPaymentStatusId() : -1L);
        action.setPartialId(currentItem.getPartialId() != null ? currentItem.getPartialId() : -1L);
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

        UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(action);
    }

    private void initCitySpinner(final List<TransitPoint> transitPointList) {
        final CustomArrayAdapter<TransitPoint> cityAdapter = new CustomArrayAdapter<>(context, R.layout.spinner_item, transitPointList);
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

                final String qr = data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE);

                WorkManager.getInstance(context).getWorkInfoByIdLiveData(SyncWorkRequest.searchTransportation(context, qr)).observe(getViewLifecycleOwner(), workInfo -> {
                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        Log.i(TAG, "found transportation with: " + qr);

                        final long transportationId = workInfo.getOutputData().getLong(Constants.KEY_TRANSPORTATION_ID, 0);
                        final long invoiceId = workInfo.getOutputData().getLong(Constants.KEY_INVOICE_ID, 0);
                        final long requestId = workInfo.getOutputData().getLong(Constants.KEY_REQUEST_ID, 0);
                        final long courierId = workInfo.getOutputData().getLong(Constants.KEY_COURIER_ID, 0);
                        final long providerId = workInfo.getOutputData().getLong(Constants.KEY_PROVIDER_ID, 0);
                        final long transportationStatusId = workInfo.getOutputData().getLong(Constants.KEY_TRANSPORTATION_STATUS_ID, 0);
                        final long currentTransitPointId = workInfo.getOutputData().getLong(Constants.KEY_CURRENT_TRANSIT_POINT_ID, 0);
                        final long paymentStatusId = workInfo.getOutputData().getLong(Constants.KEY_PAYMENT_STATUS_ID, 0);

                        final String qrCode = workInfo.getOutputData().getString(Constants.KEY_TRANSPORTATION_QR);
                        final String trackingCode = workInfo.getOutputData().getString(Constants.KEY_TRACKING_CODE);
                        final String cityFrom = workInfo.getOutputData().getString(Constants.KEY_CITY_FROM);
                        final String cityTo = workInfo.getOutputData().getString(Constants.KEY_CITY_TO);
                        final String partyQrCode = workInfo.getOutputData().getString(Constants.KEY_PARTY_QR_CODE);
                        final String instruction = workInfo.getOutputData().getString(Constants.KEY_INSTRUCTIONS);
                        final String direction = workInfo.getOutputData().getString(Constants.KEY_DIRECTION);
                        final String arrivalDate = workInfo.getOutputData().getString(Constants.KEY_ARRIVAL_DATE);
                        final String transportationStatusName = workInfo.getOutputData().getString(Constants.KEY_TRANSPORTATION_STATUS);

                        final CurrentTransportationsFragmentDirections.ActionCurrentTransportationsFragmentToTransportationStatusFragment action =
                                CurrentTransportationsFragmentDirections.actionCurrentTransportationsFragmentToTransportationStatusFragment(
                                        qrCode, trackingCode, transportationStatusName, cityFrom, cityTo, partyQrCode, instruction, direction, arrivalDate);

                        action.setTransportationId(transportationId);
                        action.setInvoiceId(invoiceId);
                        action.setRequestId(requestId);
                        action.setTransportationStatusId(transportationStatusId);
                        action.setTransportationStatusName(transportationStatusName);
                        action.setPaymentStatusId(paymentStatusId);
                        action.setTrackingCode(trackingCode);
                        action.setQrCode(qrCode);
                        action.setPartyQrCode(partyQrCode);
                        action.setCurrentTransitPointId(currentTransitPointId);
                        action.setCityFrom(cityFrom);
                        action.setCityTo(cityTo);
                        action.setCourierId(courierId);
                        action.setProviderId(providerId);
                        action.setInstructions(instruction);
                        action.setArrivalDate(arrivalDate);
                        action.setDirection(direction);

                        UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(action);
                    }
                    if (workInfo.getState() == WorkInfo.State.CANCELLED || workInfo.getState() == WorkInfo.State.FAILED) {
                        Toast.makeText(context, "QR код " + qr + " не найден", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private static final String TAG = CurrentTransportationsFragment.class.toString();
}