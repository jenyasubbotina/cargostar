package uz.alexits.cargostar.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.WorkInfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import uz.alexits.cargostar.R;
import uz.alexits.cargostar.entities.location.TransitPoint;
import uz.alexits.cargostar.entities.transportation.Transportation;
import uz.alexits.cargostar.entities.transportation.TransportationStatus;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.view.UiUtils;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.adapter.TransportationAdapter;
import uz.alexits.cargostar.view.adapter.CustomArrayAdapter;
import uz.alexits.cargostar.view.callback.TransportationCallback;
import uz.alexits.cargostar.view.dialog.ScanQrDialog;
import uz.alexits.cargostar.viewmodel.TransportationViewModel;
import uz.alexits.cargostar.viewmodel.factory.TransportationViewModelFactory;

import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class TransportationsFragment extends Fragment implements TransportationCallback {
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
    private SwipeRefreshLayout swipeRefreshLayout;
    private Spinner transportationStatusSpinner;
    private Spinner transitPointSpinner;
    private ArrayAdapter<TransitPoint> transitPointArrayAdapter;
    private ArrayAdapter<TransportationStatus> transportationStatusArrayAdapter;
    private EditText qrCodeEditText;
    private ImageView scanQrImageView;

    private TransportationAdapter transportationAdapter;

    private TransportationViewModel transportationViewModel;

    public TransportationsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TransportationViewModelFactory transportationFactory = new TransportationViewModelFactory(requireContext());
        transportationViewModel = new ViewModelProvider(getViewModelStore(), transportationFactory).get(TransportationViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_transportations, container, false);

        /* header views */
        fullNameTextView = requireActivity().findViewById(R.id.full_name_text_view);
        branchTextView = requireActivity().findViewById(R.id.branch_text_view);
        courierIdTextView = requireActivity().findViewById(R.id.courier_id_text_view);
        requestSearchEditText = requireActivity().findViewById(R.id.search_edit_text);
        requestSearchImageView = requireActivity().findViewById(R.id.search_btn);
        profileImageView = requireActivity().findViewById(R.id.profile_image_view);
        editImageView = requireActivity().findViewById(R.id.edit_image_view);
        createUserImageView = requireActivity().findViewById(R.id.create_user_image_view);
        calculatorImageView = requireActivity().findViewById(R.id.calculator_image_view);
        notificationsImageView = requireActivity().findViewById(R.id.notifications_image_view);
        badgeCounterTextView = requireActivity().findViewById(R.id.badge_counter_text_view);

        /* main content views */
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh_layout);
        qrCodeEditText = root.findViewById(R.id.qr_code_edit_text);
        scanQrImageView = root.findViewById(R.id.camera_image_view);

        transportationStatusSpinner = root.findViewById(R.id.status_spinner);
        transitPointSpinner = root.findViewById(R.id.city_spinner);

        transitPointArrayAdapter = new CustomArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<>());
        transportationStatusArrayAdapter = new CustomArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, new ArrayList<>());

        transitPointArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transportationStatusArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transitPointSpinner.setAdapter(transitPointArrayAdapter);
        transportationStatusSpinner.setAdapter(transportationStatusArrayAdapter);

        final RecyclerView currentTransportationListRecyclerView = root.findViewById(R.id.current_parcels_recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        currentTransportationListRecyclerView.setLayoutManager(layoutManager);
        currentTransportationListRecyclerView.setHasFixedSize(true);
        transportationAdapter = new TransportationAdapter(requireContext(), this);
        currentTransportationListRecyclerView.setAdapter(transportationAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //header views
        profileImageView.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), MainActivity.class));
        });

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

        editImageView.setOnClickListener(v -> {
            UiUtils.getNavController(requireActivity(), R.id.main_fragment_container).navigate(R.id.profileFragment);
        });

        /* main content views */
        transitPointSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int i, final long l) {
                final TextView itemTextView = (TextView) view;

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack, null));
                        transitPointSpinner.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
                transportationViewModel.setSelectedTransitPoint((TransitPoint) adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        transportationStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int position, final long id) {
                final TextView itemTextView = (TextView) view;

                if (itemTextView != null) {
                    if (position < adapterView.getCount()) {
                        itemTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorBlack, null));
                        transportationStatusSpinner.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
                transportationViewModel.setSelectedTransportationStatus((TransportationStatus) adapterView.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        scanQrImageView.setOnClickListener(v -> {
            ScanQrDialog dialogFragment = ScanQrDialog.newInstance(0, IntentConstants.REQUEST_SCAN_QR_MENU);
            dialogFragment.setTargetFragment(TransportationsFragment.this, IntentConstants.REQUEST_SCAN_QR_MENU);
            dialogFragment.show(getParentFragmentManager().beginTransaction(), TAG);
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            transportationViewModel.fetchTransportationList();
            transportationViewModel.fetchTransportationStatuses();
            transportationViewModel.fetchTransitPoints();
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final boolean[] activityCreated = {true, true};
        transportationViewModel.removeSearchTransportationByQrUUID();

        /* header */
        transportationViewModel.getCourierData(requireContext()).observe(getViewLifecycleOwner(), courier -> {
            if (courier != null) {
                fullNameTextView.setText(getString(R.string.header_courier_full_name, courier.getFirstName(), courier.getLastName()));
                courierIdTextView.setText(getString(R.string.courier_id_placeholder, courier.getId()));
            }
        });

        transportationViewModel.getBrancheData(requireContext()).observe(getViewLifecycleOwner(), branch -> {
            if (branch != null) {
                branchTextView.setText(getString(R.string.header_branch_name, branch.getName()));
            }
        });

        transportationViewModel.selectNewNotificationsCount().observe(getViewLifecycleOwner(), newNotificationsCount -> {
            if (newNotificationsCount != null) {
                badgeCounterTextView.setText(String.valueOf(newNotificationsCount));
            }
        });

        transportationViewModel.getSearchRequestResult(requireContext()).observe(getViewLifecycleOwner(), workInfo -> {
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

        /* transportation view model */
        transportationViewModel.getSearchTransportationByQrResult(requireContext()).observe(getViewLifecycleOwner(), workInfo -> {
            if (workInfo.getState() == WorkInfo.State.CANCELLED || workInfo.getState() == WorkInfo.State.FAILED) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(requireContext(), "Перевозка уже была привязана" + workInfo.getOutputData().getString(Constants.KEY_TRANSPORTATION_QR), Toast.LENGTH_SHORT).show();
                return;
            }
            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                swipeRefreshLayout.setRefreshing(false);
                NavHostFragment.findNavController(this).navigate(
                        TransportationsFragmentDirections.actionTransportationsFragmentToStatusFragment()
                                .setTransportationId(workInfo.getOutputData().getLong(Constants.KEY_TRANSPORTATION_ID, 0))
                                .setInvoiceId(workInfo.getOutputData().getLong(Constants.KEY_INVOICE_ID, 0))
                                .setRequestId(workInfo.getOutputData().getLong(Constants.KEY_REQUEST_ID, 0))
                                .setTransportationStatusId(workInfo.getOutputData().getLong(Constants.KEY_TRANSPORTATION_STATUS_ID, 0))
                                .setTransportationStatusName(workInfo.getOutputData().getString(Constants.KEY_TRANSPORTATION_STATUS))
                                .setPaymentStatusId(workInfo.getOutputData().getLong(Constants.KEY_PAYMENT_STATUS_ID, 0))
                                .setTrackingCode(workInfo.getOutputData().getString(Constants.KEY_TRACKING_CODE))
                                .setQrCode(workInfo.getOutputData().getString(Constants.KEY_TRANSPORTATION_QR))
                                .setPartyQrCode(workInfo.getOutputData().getString(Constants.KEY_PARTY_QR_CODE))
                                .setCurrentTransitPointId(workInfo.getOutputData().getLong(Constants.KEY_CURRENT_TRANSIT_POINT_ID, 0))
                                .setCityFrom(workInfo.getOutputData().getString(Constants.KEY_CITY_FROM))
                                .setCityTo(workInfo.getOutputData().getString(Constants.KEY_CITY_TO))
                                .setCourierId(workInfo.getOutputData().getLong(Constants.KEY_COURIER_ID, 0))
                                .setProviderId(workInfo.getOutputData().getLong(Constants.KEY_PROVIDER_ID, 0))
                                .setInstructions(workInfo.getOutputData().getString(Constants.KEY_INSTRUCTIONS))
                                .setArrivalDate(workInfo.getOutputData().getString(Constants.KEY_ARRIVAL_DATE))
                                .setDirection(workInfo.getOutputData().getString(Constants.KEY_DIRECTION)));
                return;
            }
            swipeRefreshLayout.setRefreshing(true);
        });

        transportationViewModel.getTransportationStatuses().observe(getViewLifecycleOwner(), transportationStatusList -> {
            if (transportationStatusList != null) {
                transportationStatusArrayAdapter.clear();
                transportationStatusArrayAdapter.addAll(transportationStatusList);

                if (activityCreated[0]) {
                    for (int i = 0; i < transportationStatusList.size(); i++) {
                        if (transportationStatusList.get(i).getName().equalsIgnoreCase(getString(R.string.in_transit))) {
                            transportationStatusSpinner.setSelection(i);
                            activityCreated[0] = false;
                            return;
                        }
                    }
                }
            }
        });

        transportationViewModel.getTransitPoints().observe(getViewLifecycleOwner(), transitPointList -> {
            if (transitPointList != null) {
                transitPointArrayAdapter.clear();
                transitPointArrayAdapter.addAll(transitPointList);

                if (activityCreated[1]) {
                    for (int i = 0; i < transitPointList.size(); i++) {
                        if (transitPointList.get(i).getName().equalsIgnoreCase(getString(R.string.tashkent))) {
                            transitPointSpinner.setSelection(i);
                            activityCreated[1] = false;
                            return;
                        }
                    }
                }
            }
        });

        transportationViewModel.getTransportationList().observe(getViewLifecycleOwner(), transportationList -> {
            if (transportationList != null) {
                transportationAdapter.setTransportationList(transportationList);
                transportationAdapter.notifyDataSetChanged();
            }
        });

        transportationViewModel.getFetchTransportationsResult(requireContext()).observe(getViewLifecycleOwner(), workInfo -> {
            if (workInfo.getState() == WorkInfo.State.SUCCEEDED || workInfo.getState() == WorkInfo.State.FAILED || workInfo.getState() == WorkInfo.State.CANCELLED) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onTransportationSelected(Transportation currentItem) {
        NavHostFragment.findNavController(this).navigate(
                TransportationsFragmentDirections.actionTransportationsFragmentToStatusFragment()
                        .setRequestId(currentItem.getRequestId())
                        .setTransportationId(currentItem.getId())
                        .setInvoiceId(currentItem.getInvoiceId())
                        .setTransportationStatusId(currentItem.getTransportationStatusId())
                        .setTransportationStatusName(currentItem.getTransportationStatusName())
                        .setPaymentStatusId(currentItem.getPaymentStatusId())
                        .setPartialId(currentItem.getPartialId())
                        .setTrackingCode(currentItem.getTrackingCode())
                        .setQrCode(currentItem.getQrCode())
                        .setPartyQrCode(currentItem.getPartyQrCode())
                        .setCurrentTransitPointId(currentItem.getCurrentTransitionPointId())
                        .setCityFrom(currentItem.getCityFrom())
                        .setCityTo(currentItem.getCityTo())
                        .setCourierId(currentItem.getCourierId())
                        .setProviderId(currentItem.getProviderId())
                        .setInstructions(currentItem.getInstructions())
                        .setArrivalDate(currentItem.getArrivalDate())
                        .setDirection(currentItem.getDirection()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == IntentConstants.REQUEST_SCAN_QR_MENU) {
                transportationViewModel.searchTransportationByQrAndBindRequest(data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE));
            }
        }
    }

    private static final String TAG = TransportationsFragment.class.toString();
}