package com.example.cargostar.view.fragment;

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

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cargostar.R;
import com.example.cargostar.model.Notification;
import com.example.cargostar.model.database.SharedPrefs;
import com.example.cargostar.view.Constants;
import com.example.cargostar.view.activity.CalculatorActivity;
import com.example.cargostar.view.activity.CreateUserActivity;
import com.example.cargostar.view.activity.MainActivity;
import com.example.cargostar.view.activity.NotificationsActivity;
import com.example.cargostar.view.activity.ProfileActivity;
import com.example.cargostar.view.adapter.NotificationAdapter;
import com.example.cargostar.view.callback.NotificationCallback;
import com.example.cargostar.view.viewholder.NotificationViewHolder;
import com.example.cargostar.viewmodel.HeaderViewModel;
import com.example.cargostar.viewmodel.NotificationsViewModel;
import com.example.cargostar.viewmodel.PopulateViewModel;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationsFragment extends Fragment implements NotificationCallback {
    private FragmentActivity activity;
    private Context context;
    //viewModel
    private HeaderViewModel headerViewModel;
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
        headerViewModel = new ViewModelProvider(this).get(HeaderViewModel.class);
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        headerViewModel.selectCourierByLogin(SharedPrefs.getInstance(context).getString(SharedPrefs.LOGIN)).observe(getViewLifecycleOwner(), courier -> {
            if (courier != null) {
                fullNameTextView.setText(courier.getFirstName() + " " + courier.getLastName());
            }
        });
        headerViewModel.selectBranchByCourierId(SharedPrefs.getInstance(context).getLong(SharedPrefs.ID)).observe(getViewLifecycleOwner(), branch -> {
            if (branch != null) {
                branchTextView.setText(getString(R.string.branch) + " \"" + branch.getName() + "\"");
            }
        });

        parcelSearchImageView.setOnClickListener(v -> {
            final String parcelIdStr = parcelSearchEditText.getText().toString();
            if (TextUtils.isEmpty(parcelIdStr)) {
                Toast.makeText(context, "Введите ID перевозки или номер накладной", Toast.LENGTH_SHORT).show();
                return;
            }

            final long parcelId = Long.parseLong(parcelIdStr);

            headerViewModel.selectRequest(parcelId).observe(getViewLifecycleOwner(), receiptWithCargoList -> {
                if (receiptWithCargoList == null) {
                    Toast.makeText(context, "Накладной не существует", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (receiptWithCargoList.getReceipt() == null) {
                    Toast.makeText(context, "Накладной не существует", Toast.LENGTH_SHORT).show();
                    return;
                }
                final Intent mainIntent = new Intent(context, MainActivity.class);
                mainIntent.putExtra(Constants.INTENT_REQUEST_KEY, Constants.REQUEST_FIND_PARCEL);
                mainIntent.putExtra(Constants.INTENT_REQUEST_VALUE, parcelId);
                startActivity(mainIntent);
            });
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

        currentItem.setRead(true);
        notificationsViewModel.readNotification(currentItem.getReceiptId());

        final Intent requestsIntent = new Intent(getContext(), MainActivity.class);
        if (currentItem.getLink().equalsIgnoreCase(publicBids)) {
            requestsIntent.putExtra(Constants.INTENT_REQUEST_KEY, Constants.REQUEST_PUBLIC_REQUESTS);
        }
        else if (currentItem.getLink().equalsIgnoreCase(myBids)) {
            requestsIntent.putExtra(Constants.INTENT_REQUEST_KEY, Constants.REQUEST_MY_REQUESTS);
        }
        startActivity(requestsIntent);
    }
}