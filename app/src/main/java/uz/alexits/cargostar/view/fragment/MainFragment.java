
package uz.alexits.cargostar.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.WorkInfo;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import uz.alexits.cargostar.BuildConfig;
import uz.alexits.cargostar.R;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.view.UiUtils;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.dialog.ScanQrDialog;
import uz.alexits.cargostar.viewmodel.MainViewModel;
import uz.alexits.cargostar.viewmodel.factory.MainViewModelFactory;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class MainFragment extends Fragment {
    //header views
    private ImageView profileImageView;
    private TextView fullNameTextView;
    private TextView branchTextView;
    private TextView courierIdTextView;
    private EditText requestSearchEditText;
    private ImageView requestSearchImageView;
    private ImageView editImageView;
    private ImageView createUserImageView;
    private ImageView calculatorImageView;
    private ImageView notificationsImageView;
    private TextView badgeCounterTextView;

    //main content views
    private ImageView publicRequestsImageView;
    private ImageView myRequestsImageView;
    private ImageView createParcelImageView;
    private ImageView currentTransportationListImageView;
    private ImageView scanParcelsImageView;
    private ImageView transportationDeliveryImageView;
    private TextView appVersionTextView;

    private SwipeRefreshLayout swipeRefreshLayout;

    private MainViewModel mainViewModel;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final MainViewModelFactory mainFactory = new MainViewModelFactory(requireContext());
        mainViewModel = new ViewModelProvider(getViewModelStore(), mainFactory).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_main, container, false);

        //header views
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

        //main content views
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh_layout);
        publicRequestsImageView = root.findViewById(R.id.public_requests_image_view);
        myRequestsImageView = root.findViewById(R.id.my_requests_image_view);
        createParcelImageView = root.findViewById(R.id.create_parcel_image_view);
        currentTransportationListImageView = root.findViewById(R.id.current_parcels_image_view);
        scanParcelsImageView = root.findViewById(R.id.scan_parcels_image_view);
        transportationDeliveryImageView = root.findViewById(R.id.parcel_delivery_image_view);
        appVersionTextView = root.findViewById(R.id.app_version_text_view);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //header views
        editImageView.setOnClickListener(v -> {
            UiUtils.getNavController(requireActivity(), R.id.main_fragment_container).navigate(R.id.profileFragment);
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

        profileImageView.setOnClickListener(null);

        //main content views
        appVersionTextView.setText(getString(R.string.app_version, BuildConfig.VERSION_NAME));

        publicRequestsImageView.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(MainFragmentDirections.actionMainFragmentToPublicRequestsFragment());
        });

        myRequestsImageView.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(MainFragmentDirections.actionMainFragmentToMyRequestsFragment());
        });

        createParcelImageView.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(MainFragmentDirections.actionMainFragmentToCreateInvoiceFragment());
        });

        scanParcelsImageView.setOnClickListener(v -> {
            ScanQrDialog dialogFragment = ScanQrDialog.newInstance(0, IntentConstants.REQUEST_SCAN_QR_MENU);
            dialogFragment.setTargetFragment(MainFragment.this, IntentConstants.REQUEST_SCAN_QR_MENU);
            dialogFragment.show(getParentFragmentManager().beginTransaction(), TAG);
        });

        currentTransportationListImageView.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(MainFragmentDirections.actionMainFragmentToTransportationsFragment());
        });

        transportationDeliveryImageView.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(MainFragmentDirections.actionMainFragmentToImportFragment());
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
            mainViewModel.searchRequest(Long.parseLong(requestId));
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            mainViewModel.fetchLocationData();
            mainViewModel.fetchClientList();
            mainViewModel.fetchAddressBook();
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* header */
        mainViewModel.getCourierData(requireContext()).observe(getViewLifecycleOwner(), courier -> {
            if (courier != null) {
                fullNameTextView.setText(getString(R.string.header_courier_full_name, courier.getFirstName(), courier.getLastName()));
                courierIdTextView.setText(getString(R.string.courier_id_placeholder, courier.getId()));
            }
        });

        mainViewModel.getBrancheData(requireContext()).observe(getViewLifecycleOwner(), branch -> {
            if (branch != null) {
                branchTextView.setText(getString(R.string.header_branch_name, branch.getName()));
            }
        });

        mainViewModel.selectNewNotificationsCount().observe(getViewLifecycleOwner(), newNotificationsCount -> {
            if (newNotificationsCount != null) {
                badgeCounterTextView.setText(String.valueOf(newNotificationsCount));
            }
        });

        mainViewModel.getSearchRequestResult(requireContext()).observe(getViewLifecycleOwner(), workInfo -> {
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

        mainViewModel.getFetchLocationDataResult(requireContext()).observe(getViewLifecycleOwner(), workInfo -> {
            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                swipeRefreshLayout.setRefreshing(false);
                return;
            }
            if (workInfo.getState() == WorkInfo.State.FAILED) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(requireContext(), R.string.error_sync_failed, Toast.LENGTH_SHORT).show();
                return;
            }
            if (workInfo.getState() == WorkInfo.State.CANCELLED) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(requireContext(), R.string.error_internet, Toast.LENGTH_SHORT).show();
                return;
            }
            swipeRefreshLayout.setRefreshing(true);
        });

        //todo: review
        mainViewModel.getScanQrResult(requireContext()).observe(getViewLifecycleOwner(), workInfo -> {
            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                swipeRefreshLayout.setRefreshing(false);
                NavHostFragment.findNavController(this).navigate(
                        MainFragmentDirections.actionMainFragmentToStatusFragment()
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
            if (workInfo.getState() == WorkInfo.State.CANCELLED || workInfo.getState() == WorkInfo.State.FAILED) {
                Toast.makeText(requireContext(), "QR код не найден", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
                return;
            }
            swipeRefreshLayout.setRefreshing(true);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == IntentConstants.REQUEST_SCAN_QR_MENU) {
                mainViewModel.searchTransportationByQr(data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE));
            }
        }
    }

    private static final String TAG = MainFragment.class.toString();
}