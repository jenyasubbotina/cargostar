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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import uz.alexits.cargostar.R;

import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.shipping.Request;
import uz.alexits.cargostar.viewmodel.CourierViewModel;
import uz.alexits.cargostar.viewmodel.RequestsViewModel;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.activity.CalculatorActivity;
import uz.alexits.cargostar.view.activity.CreateUserActivity;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.activity.NotificationsActivity;
import uz.alexits.cargostar.view.activity.ProfileActivity;
import uz.alexits.cargostar.view.adapter.MyRequestAdapter;
import uz.alexits.cargostar.view.callback.RequestCallback;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class MyRequestsFragment extends Fragment implements RequestCallback {
    private Context context;
    private FragmentActivity activity;
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
    //viewModel
    private CourierViewModel courierViewModel;
    private RequestsViewModel requestsViewModel;
    //recycler view
    private RecyclerView myRequestsRecyclerView;
    private MyRequestAdapter adapter;

    private static long courierId = -1;

    public MyRequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        activity = getActivity();

        if (getArguments() != null) {
            courierId = MyRequestsFragmentArgs.fromBundle(getArguments()).getCourierId();
        }
        SyncWorkRequest.fetchRequestData(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_my_requests, container, false);
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
        myRequestsRecyclerView = root.findViewById(R.id.my_bids_recycler_view);
        adapter = new MyRequestAdapter(context, this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        myRequestsRecyclerView.setLayoutManager(layoutManager);
        myRequestsRecyclerView.setAdapter(adapter);

        return  root;
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       courierViewModel = new ViewModelProvider(this).get(CourierViewModel.class);
       requestsViewModel = new ViewModelProvider(this).get(RequestsViewModel.class);
        //header views
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

        requestsViewModel.getMyRequests(courierId).observe(getViewLifecycleOwner(), myRequests -> {
            Log.i(TAG, "onActivityCreated: " + myRequests);

            adapter.setMyRequestList(myRequests);
            adapter.notifyDataSetChanged();
        });

    }

    @Override
    public void onRequestSelected(Request currentItem, RecyclerView.ViewHolder holder) {
        currentItem.setNew(false);

        new Thread(() -> requestsViewModel.readReceipt(currentItem.getId()));

        final MyRequestsFragmentDirections.ActionMyBidsFragmentToParcelDataFragment action = MyRequestsFragmentDirections.actionMyBidsFragmentToParcelDataFragment();
        action.setRequestId(currentItem.getId());
        action.setRequestOrParcel(IntentConstants.INTENT_REQUEST);
        action.setInvoiceId(currentItem.getInvoiceId() != null ? currentItem.getInvoiceId() : -1L);
        action.setCourierId(currentItem.getCourierId() != null ? currentItem.getCourierId() : -1L);
        action.setClientId(currentItem.getClientId() != null ? currentItem.getClientId() : -1L);
        action.setSenderCountryId(currentItem.getSenderCountryId() != null ? currentItem.getSenderCountryId() : -1L);
        action.setSenderRegionId(currentItem.getSenderRegionId() != null ? currentItem.getSenderRegionId() : -1L);
        action.setSenderCityId(currentItem.getSenderCityId() != null ? currentItem.getSenderCityId() : -1L);
        action.setRecipientCountryId(currentItem.getRecipientCountryId() != null ? currentItem.getRecipientCountryId() : -1L);
        action.setRecipientCityId(currentItem.getRecipientCityId() != null ? currentItem.getRecipientCityId() : -1L);
        action.setProviderId(currentItem.getProviderId() != null ? currentItem.getProviderId() : -1L);
        action.setRequestOrParcel(IntentConstants.INTENT_REQUEST);
        NavHostFragment.findNavController(this).navigate(action);
    }

    @Override
    public void onPlusClicked(Request currentItem) {
        //do nothing here
    }

    private static final String TAG = MyRequestsFragment.class.toString();
}