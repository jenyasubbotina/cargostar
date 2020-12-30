
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
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import uz.alexits.cargostar.BuildConfig;
import uz.alexits.cargostar.R;

import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.transportation.Request;
import uz.alexits.cargostar.model.transportation.Transportation;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.view.UiUtils;
import uz.alexits.cargostar.viewmodel.CourierViewModel;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.activity.ScanQrActivity;
import uz.alexits.cargostar.viewmodel.RequestsViewModel;
import uz.alexits.cargostar.viewmodel.TransportationViewModel;
import uz.alexits.cargostar.workers.SyncWorkRequest;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class MainFragment extends Fragment {
    private FragmentActivity activity;
    private Context context;
    //header views
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

    private static volatile long courierId = -1;
    private static volatile long courierBranchId = -1;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        context = getContext();

        new Thread(() -> {
            courierId = SharedPrefs.getInstance(context).getLong(SharedPrefs.ID);
        }).start();

        SyncWorkRequest.fetchLocationData(context);
        SyncWorkRequest.fetchSenderList(context);
        SyncWorkRequest.fetchAddressBookList(context);
        SyncWorkRequest.fetchRequests(context);
        SyncWorkRequest.fetchTransportationList(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_main, container, false);

        //header views
        fullNameTextView = activity.findViewById(R.id.full_name_text_view);
        branchTextView = activity.findViewById(R.id.branch_text_view);
        courierIdTextView = activity.findViewById(R.id.courier_id_text_view);
        requestSearchEditText = activity.findViewById(R.id.search_edit_text);
        requestSearchImageView = activity.findViewById(R.id.search_btn);
        editImageView = activity.findViewById(R.id.edit_image_view);
        createUserImageView = activity.findViewById(R.id.create_user_image_view);
        calculatorImageView = activity.findViewById(R.id.calculator_image_view);
        notificationsImageView = activity.findViewById(R.id.notifications_image_view);
        badgeCounterTextView = activity.findViewById(R.id.badge_counter_text_view);
        //main content views
        publicRequestsImageView = root.findViewById(R.id.public_bids_image_view);
        myRequestsImageView = root.findViewById(R.id.my_bids_image_view);
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
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(R.id.profileFragment);
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

        //main content views
        appVersionTextView.setText(getString(R.string.app_version, BuildConfig.VERSION_NAME));

        publicRequestsImageView.setOnClickListener(v -> {
            final MainFragmentDirections.ActionMainFragmentToPublicRequestsFragment action = MainFragmentDirections.actionMainFragmentToPublicRequestsFragment();
            action.setCourierId(courierId);
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(action);
        });

        myRequestsImageView.setOnClickListener(v -> {
            final MainFragmentDirections.ActionMainFragmentToMyRequestsFragment action = MainFragmentDirections.actionMainFragmentToMyRequestsFragment();
            action.setCourierId(courierId);
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(action);
        });

        createParcelImageView.setOnClickListener(v -> {
            final MainFragmentDirections.ActionMainFragmentToCreateInvoiceFragment action = MainFragmentDirections.actionMainFragmentToCreateInvoiceFragment();
            action.setCourierId(courierId);
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(action);
        });

        scanParcelsImageView.setOnClickListener(v -> {
            final Intent scanQrIntent = new Intent(context, ScanQrActivity.class);
            startActivityForResult(scanQrIntent, IntentConstants.REQUEST_SCAN_QR_MENU);
        });

        currentTransportationListImageView.setOnClickListener(v -> {
            if (courierBranchId <= 0) {
                Toast.makeText(activity, "Подождите...", Toast.LENGTH_SHORT).show();
                return;
            }

            final MainFragmentDirections.ActionMainFragmentToCurrentTransportationsFragment action = MainFragmentDirections.actionMainFragmentToCurrentTransportationsFragment();
            action.setStatusFlag(IntentConstants.FRAGMENT_CURRENT_TRANSPORT);
            action.setCourierBranchId(courierBranchId);
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(action);
        });

        transportationDeliveryImageView.setOnClickListener(v -> {
            final MainFragmentDirections.ActionMainFragmentToCurrentTransportationsFragment action = MainFragmentDirections.actionMainFragmentToCurrentTransportationsFragment();
            action.setStatusFlag(IntentConstants.FRAGMENT_DELIVERY_TRANSPORT);
            action.setCourierBranchId(courierBranchId);
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(action);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //header views
        final CourierViewModel courierViewModel = new ViewModelProvider(this).get(CourierViewModel.class);

        courierViewModel.selectCourierByLogin(SharedPrefs.getInstance(context).getString(Constants.KEY_LOGIN)).observe(getViewLifecycleOwner(), courier -> {
            if (courier != null) {
                fullNameTextView.setText(getString(R.string.header_courier_full_name, courier.getFirstName(), courier.getLastName()));
                courierIdTextView.setText(getString(R.string.courier_id_placeholder, courier.getId()));
                courierBranchId = courier.getBrancheId();
            }
        });

        courierViewModel.selectBrancheById(SharedPrefs.getInstance(context).getLong(SharedPrefs.BRANCH_ID)).observe(getViewLifecycleOwner(), branche -> {
            if (branche != null) {
                branchTextView.setText(getString(R.string.header_branch_name, branche.getName()));
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

                        final MainFragmentDirections.ActionMainFragmentToTransportationStatusFragment action =
                                MainFragmentDirections.actionMainFragmentToTransportationStatusFragment(
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

    private static final String TAG = MainFragment.class.toString();
}