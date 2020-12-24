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

import uz.alexits.cargostar.R;

import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.push.Notification;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.view.UiUtils;
import uz.alexits.cargostar.view.viewholder.NotificationViewHolder;
import uz.alexits.cargostar.viewmodel.CourierViewModel;
import uz.alexits.cargostar.viewmodel.NotificationsViewModel;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.adapter.NotificationAdapter;
import uz.alexits.cargostar.view.callback.NotificationCallback;
import uz.alexits.cargostar.workers.SyncWorkRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificationsFragment extends Fragment implements NotificationCallback {
    private FragmentActivity activity;
    private Context context;
    //viewModel
    private CourierViewModel courierViewModel;
    private NotificationsViewModel notificationsViewModel;
    //header views
    private TextView fullNameTextView;
    private TextView branchTextView;
    private TextView courierIdTextView;
    private EditText requestSearchEditText;
    private ImageView requestSearchImageView;
    private ImageView editImageView;
    private ImageView createUserImageView;
    private ImageView calculatorImageView;
    private ImageView profileImageView;
    private TextView badgeCounterTextView;
    //main content views
    private final List<Notification> notificationList = new ArrayList<>();
    private RecyclerView notificationRecyclerView;
    private NotificationAdapter notificationAdapter;

    private static volatile long courierId = -1;
    private static volatile long courierBranchId = -1;

    public NotificationsFragment() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        //header views
        fullNameTextView = activity.findViewById(R.id.full_name_text_view);
        branchTextView = activity.findViewById(R.id.branch_text_view);
        courierIdTextView = activity.findViewById(R.id.courier_id_text_view);
        requestSearchEditText = activity.findViewById(R.id.search_edit_text);
        requestSearchImageView = activity.findViewById(R.id.search_btn);
        editImageView = activity.findViewById(R.id.edit_image_view);
        createUserImageView = activity.findViewById(R.id.create_user_image_view);
        calculatorImageView = activity.findViewById(R.id.calculator_image_view);
        profileImageView = activity.findViewById(R.id.profile_image_view);
        badgeCounterTextView = activity.findViewById(R.id.badge_counter_text_view);
        //main content views
        notificationRecyclerView = root.findViewById(R.id.notificationRecyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        notificationAdapter = new NotificationAdapter(this.getContext(), notificationList, this);
        notificationRecyclerView.setHasFixedSize(false);
        notificationRecyclerView.setLayoutManager(layoutManager);
        notificationRecyclerView.setAdapter(notificationAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //header views

        profileImageView.setOnClickListener(v -> {
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(R.id.mainFragment);
        });

        createUserImageView.setOnClickListener(v -> {
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(R.id.createUserFragment);
        });

        calculatorImageView.setOnClickListener(v -> {
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(R.id.calculatorFragment);
        });

        editImageView.setOnClickListener(v -> {
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(R.id.profileFragment);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //header views
        courierViewModel = new ViewModelProvider(this).get(CourierViewModel.class);
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        courierViewModel.selectCourierByLogin(SharedPrefs.getInstance(context).getString(Constants.KEY_LOGIN)).observe(getViewLifecycleOwner(), courier -> {
            if (courier != null) {
                fullNameTextView.setText(getString(R.string.header_courier_full_name, courier.getFirstName(), courier.getLastName()));
                courierIdTextView.setText(getString(R.string.courier_id_placeholder, courier.getId()));
                courierBranchId = courier.getBrancheId();
            }
        });

        courierViewModel.selectBrancheById(SharedPrefs.getInstance(context).getLong(SharedPrefs.BRANCH_ID)).observe(getViewLifecycleOwner(), branch -> {
            if (branch != null) {
                branchTextView.setText(getString(R.string.header_branch_name, branch.getName()));
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

        courierViewModel.selectNewNotificationsCount().observe(getViewLifecycleOwner(), newNotificationsCount -> {
            if (newNotificationsCount != null) {
                badgeCounterTextView.setText(String.valueOf(newNotificationsCount));
            }
        });
        //init notificationList
        notificationsViewModel.selectAllNotifications().observe(getViewLifecycleOwner(), notificationList -> {
            notificationAdapter.setNotificationList(notificationList);
            notificationAdapter.notifyDataSetChanged();
        });

    }

    @Override
    public void onNotificationSelected(Notification currentItem, NotificationViewHolder holder) {
        currentItem.setRead(true);
        notificationsViewModel.readNotification(currentItem.getId());

        if (currentItem.getLink().equalsIgnoreCase(IntentConstants.REQUEST_PUBLIC_REQUESTS)) {
            final NotificationsFragmentDirections.ActionNotificationsFragmentToPublicBidsFragment action = NotificationsFragmentDirections.actionNotificationsFragmentToPublicBidsFragment();
            action.setCourierId(courierId);
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(action);
            return;
        }
        if (currentItem.getLink().equalsIgnoreCase(IntentConstants.REQUEST_MY_REQUESTS)) {
            final NotificationsFragmentDirections.ActionNotificationsFragmentToMyRequestsFragment action = NotificationsFragmentDirections.actionNotificationsFragmentToMyRequestsFragment();
            action.setCourierId(courierId);
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(action);
            return;
        }
        if (currentItem.getLink().equalsIgnoreCase(IntentConstants.REQUEST_CURRENT_TRANSPORTATIONS)) {
            if (courierBranchId <= 0) {
                Toast.makeText(activity, "Подождите...", Toast.LENGTH_SHORT).show();
                return;
            }
            final NotificationsFragmentDirections.ActionNotificationsFragmentToCurrentTransportationsFragment action = NotificationsFragmentDirections.actionNotificationsFragmentToCurrentTransportationsFragment();
            action.setStatusFlag(IntentConstants.FRAGMENT_CURRENT_TRANSPORT);
            action.setCourierBranchId(courierBranchId);
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(action);
        }
    }

    private static final String TAG = NotificationsFragment.class.toString();
}