package uz.alexits.cargostar.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SyncRequest;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import uz.alexits.cargostar.R;

import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.viewmodel.CourierViewModel;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.activity.CalculatorActivity;
import uz.alexits.cargostar.view.activity.CreateInvoiceActivity;
import uz.alexits.cargostar.view.activity.CreateUserActivity;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.activity.NotificationsActivity;
import uz.alexits.cargostar.view.activity.ProfileActivity;
import uz.alexits.cargostar.view.activity.ScanQrActivity;
import uz.alexits.cargostar.viewmodel.RequestsViewModel;
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
    private ImageView publicRequestsImageView;
    private ImageView myRequestsImageView;
    private ImageView createParcelImageView;
    private ImageView currentParcelsImageView;
    private ImageView scanParcelsImageView;
    private ImageView parcelDeliveryImageView;

    private static long courierId = -1;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        context = getContext();
        courierId = SharedPrefs.getInstance(context).getLong(SharedPrefs.ID);

        SyncWorkRequest.fetchTransitPoints(context);
        SyncWorkRequest.fetchTransportationStatuses(context);
        SyncWorkRequest.fetchRequestData(context);
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
        publicRequestsImageView = root.findViewById(R.id.public_bids_image_view);
        myRequestsImageView = root.findViewById(R.id.my_bids_image_view);
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
        publicRequestsImageView.setOnClickListener(v -> {
            final MainFragmentDirections.ActionMainFragmentToPublicBidsFragment action = MainFragmentDirections.actionMainFragmentToPublicBidsFragment();
            action.setCourierId(courierId);
            NavHostFragment.findNavController(this).navigate(action);
        });
        myRequestsImageView.setOnClickListener(v -> {
            final MainFragmentDirections.ActionMainFragmentToMyBidsFragment action = MainFragmentDirections.actionMainFragmentToMyBidsFragment();
            action.setCourierId(courierId);
            NavHostFragment.findNavController(this).navigate(action);
        });

        createParcelImageView.setOnClickListener(v -> {
            startActivity(new Intent(context, CreateInvoiceActivity.class));
        });

        currentParcelsImageView.setOnClickListener(v -> {
            NavHostFragment.findNavController(MainFragment.this).navigate(R.id.action_mainFragment_to_currentParcelsFragment);
        });

        scanParcelsImageView.setOnClickListener(v -> {
//            NavHostFragment.findNavController(MainFragment.this).navigate(R.id.action_mainFragment_to_scanQrActivity);
            final Intent scanQrIntent = new Intent(context, ScanQrActivity.class);
            startActivityForResult(scanQrIntent, IntentConstants.REQUEST_SCAN_QR_MENU);
        });

        parcelDeliveryImageView.setOnClickListener(v -> {
            //TODO: select those current parcels with final destination equals to courier's transit point & status equala IN_TRANSIT
            final MainFragmentDirections.ActionMainFragmentToCurrentParcelsFragment action =
                    MainFragmentDirections.actionMainFragmentToCurrentParcelsFragment();
            action.setStatusFlag(IntentConstants.IN_TRANSIT);
            NavHostFragment.findNavController(this).navigate(action);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //header views
        final CourierViewModel courierViewModel = new ViewModelProvider(this).get(CourierViewModel.class);

        courierViewModel.selectCourierByLogin(SharedPrefs.getInstance(context).getString(SharedPrefs.LOGIN)).observe(getViewLifecycleOwner(), courier -> {
            if (courier != null) {
                fullNameTextView.setText(courier.getFirstName() + " " + courier.getLastName());
                Log.i(TAG, "courier: " + courier);
            }
        });
        courierViewModel.selectBrancheById(SharedPrefs.getInstance(context).getLong(SharedPrefs.BRANCH_ID)).observe(getViewLifecycleOwner(), branche -> {
            if (branche != null) {
                branchTextView.setText(getString(R.string.branch) + " \"" + branche.getName() + "\"");
            }
        });
        courierViewModel.selectNewNotificationsCount().observe(getViewLifecycleOwner(), newNotificationsCount -> {
            if (newNotificationsCount != null) {
                badgeCounterTextView.setText(String.valueOf(newNotificationsCount));
            }
        });

        LocalCache.getInstance(context).invoiceDao().selectAllInvoices().observe(getViewLifecycleOwner(), invoiceList -> {
            Log.i(TAG, "invoiceList: " + invoiceList);
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
                final UUID searchInvoiceUUID = SyncWorkRequest.getInvoiceById(context, Long.parseLong(invoiceIdStr));
                WorkManager.getInstance(context).getWorkInfoByIdLiveData(searchInvoiceUUID).observe(getViewLifecycleOwner(), workInfo -> {
                    if (workInfo.getState() == WorkInfo.State.FAILED || workInfo.getState() == WorkInfo.State.CANCELLED) {
                        Toast.makeText(context, "Накладной не существует", Toast.LENGTH_SHORT).show();

                        parcelSearchEditText.setEnabled(true);

                        return;
                    }
                    if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        final Data outputData = workInfo.getOutputData();
                        final long invoiceId = outputData.getLong(Constants.KEY_INVOICE_ID, -1L);
                        final String number = outputData.getString(Constants.KEY_NUMBER);
                        final long senderId = outputData.getLong(Constants.KEY_SENDER_ID, -1L);
                        final long recipientId = outputData.getLong(Constants.KEY_RECIPIENT_ID, -1L);
                        final long payerId = outputData.getLong(Constants.KEY_PAYER_ID, -1L);
                        final long requestId = outputData.getLong(Constants.KEY_REQUEST_ID, -1L);
                        final long providerId = outputData.getLong(Constants.KEY_PROVIDER_ID, -1L);
                        final double price = outputData.getDouble(Constants.KEY_PRICE, -1);
                        final long tariffId = outputData.getLong(Constants.KEY_TARIFF_ID, -1L);
                        final long status = outputData.getInt(Constants.KEY_STATUS, -1);
                        final long createdAt = outputData.getLong(Constants.KEY_CREATED_AT, -1L);
                        final long updatedAt = outputData.getLong(Constants.KEY_UPDATED_AT, -1L);

                        final Intent mainIntent = new Intent(context, MainActivity.class);
                        mainIntent.putExtra(IntentConstants.INTENT_REQUEST_KEY, IntentConstants.REQUEST_FIND_PARCEL);
                        mainIntent.putExtra(IntentConstants.INTENT_REQUEST_VALUE, invoiceId);
                        mainIntent.putExtra(Constants.KEY_INVOICE_ID, invoiceId);
                        mainIntent.putExtra(Constants.KEY_NUMBER, number);
                        mainIntent.putExtra(Constants.KEY_SENDER_ID, senderId);
                        mainIntent.putExtra(Constants.KEY_RECIPIENT_ID, recipientId);
                        mainIntent.putExtra(Constants.KEY_PAYER_ID, payerId);
                        mainIntent.putExtra(Constants.KEY_REQUEST_ID, requestId);
                        mainIntent.putExtra(Constants.KEY_PROVIDER_ID, providerId);
                        mainIntent.putExtra(Constants.KEY_PRICE, price);
                        mainIntent.putExtra(Constants.KEY_TARIFF_ID, tariffId);
                        mainIntent.putExtra(Constants.KEY_STATUS, status);
                        mainIntent.putExtra(Constants.KEY_CREATED_AT, createdAt);
                        mainIntent.putExtra(Constants.KEY_UPDATED_AT, updatedAt);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == IntentConstants.REQUEST_SCAN_QR_MENU) {
                final MainFragmentDirections.ActionMainFragmentToParcelDataFragment action =
                        MainFragmentDirections.actionMainFragmentToParcelDataFragment();
                action.setParcelId(1);
                action.setParcelId(IntentConstants.INTENT_PARCEL);
                NavHostFragment.findNavController(this).navigate(action);
            }
        }
    }

    private static final String TAG = MainFragment.class.toString();
}