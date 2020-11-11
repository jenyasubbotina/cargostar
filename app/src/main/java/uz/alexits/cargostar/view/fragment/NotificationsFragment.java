package uz.alexits.cargostar.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
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
import uz.alexits.cargostar.view.viewholder.NotificationViewHolder;
import uz.alexits.cargostar.viewmodel.CourierViewModel;
import uz.alexits.cargostar.viewmodel.NotificationsViewModel;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.activity.CalculatorActivity;
import uz.alexits.cargostar.view.activity.CreateUserActivity;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.activity.ProfileActivity;
import uz.alexits.cargostar.view.adapter.NotificationAdapter;
import uz.alexits.cargostar.view.callback.NotificationCallback;
import uz.alexits.cargostar.workers.SyncWorkRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NotificationsFragment extends Fragment implements NotificationCallback {
    private static final String TAG = NotificationsFragment.class.toString();
    private FragmentActivity activity;
    private Context context;
    //viewModel
    private CourierViewModel courierViewModel;
    private NotificationsViewModel notificationsViewModel;
    //header views
    private TextView fullNameTextView;
    private TextView branchTextView;
    private EditText parcelSearchEditText;
    private ImageView parcelSearchImageView;
    private ImageView editImageView;
    private ImageView createUserImageView;
    private ImageView calculatorImageView;
    private ImageView profileImageView;
    private TextView badgeCounterTextView;
    //main content views
    private final List<Notification> notificationList = new ArrayList<>();
    private RecyclerView notificationRecyclerView;
    private NotificationAdapter notificationAdapter;

    public NotificationsFragment() {
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
        final View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        //header views
        fullNameTextView = activity.findViewById(R.id.full_name_text_view);
        branchTextView = activity.findViewById(R.id.branch_text_view);
        parcelSearchEditText = activity.findViewById(R.id.search_edit_text);
        parcelSearchImageView = activity.findViewById(R.id.search_btn);
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
        notificationRecyclerView.setNestedScrollingEnabled(false);
        notificationRecyclerView.setLayoutManager(layoutManager);
        notificationRecyclerView.setAdapter(notificationAdapter);

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
        profileImageView.setOnClickListener(v -> {
            startActivity(new Intent(context, MainActivity.class));
        });
        calculatorImageView.setOnClickListener(v -> {
            startActivity(new Intent(context, CalculatorActivity.class));
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //header views
        courierViewModel = new ViewModelProvider(this).get(CourierViewModel.class);
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

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
                        mainIntent.putExtra(IntentConstants.INTENT_REQUEST_KEY, IntentConstants.REQUEST_FIND_INVOICE);
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
        courierViewModel.selectNewNotificationsCount().observe(getViewLifecycleOwner(), newNotificationsCount -> {
            if (newNotificationsCount != null) {
                badgeCounterTextView.setText(String.valueOf(newNotificationsCount));
            }
        });
        //init notificationList
        notificationsViewModel.selectAllNotifications().observe(getViewLifecycleOwner(), notificationList -> {
            Log.i(NotificationsFragment.class.toString(), "notificationList=" + notificationList);
            notificationAdapter.setNotificationList(notificationList);
            notificationAdapter.notifyDataSetChanged();
        });

    }

    @Override
    public void onNotificationSelected(Notification currentItem, NotificationViewHolder holder) {
        final String publicBids = getString(R.string.public_bids_one_line);
        final String myBids = getString(R.string.my_bids_one_line);

//        currentItem.setRead(true);
//        notificationsViewModel.readNotification(currentItem.getId());

        final Intent requestsIntent = new Intent(getContext(), MainActivity.class);
        if (currentItem.getLink().equalsIgnoreCase(publicBids)) {
            requestsIntent.putExtra(IntentConstants.INTENT_REQUEST_KEY, IntentConstants.REQUEST_PUBLIC_REQUESTS);
        }
        else if (currentItem.getLink().equalsIgnoreCase(myBids)) {
            requestsIntent.putExtra(IntentConstants.INTENT_REQUEST_KEY, IntentConstants.REQUEST_MY_REQUESTS);
        }
        startActivity(requestsIntent);
    }
}