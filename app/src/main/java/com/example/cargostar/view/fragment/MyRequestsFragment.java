package com.example.cargostar.view.fragment;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cargostar.R;
import com.example.cargostar.model.database.SharedPrefs;
import com.example.cargostar.model.shipping.Receipt;
import com.example.cargostar.model.shipping.ReceiptWithCargoList;
import com.example.cargostar.view.Constants;
import com.example.cargostar.view.activity.CalculatorActivity;
import com.example.cargostar.view.activity.CreateUserActivity;
import com.example.cargostar.view.activity.MainActivity;
import com.example.cargostar.view.activity.NotificationsActivity;
import com.example.cargostar.view.activity.ProfileActivity;
import com.example.cargostar.view.adapter.MyRequestAdapter;
import com.example.cargostar.view.callback.RequestCallback;
import com.example.cargostar.viewmodel.HeaderViewModel;
import com.example.cargostar.viewmodel.PopulateViewModel;
import com.example.cargostar.viewmodel.RequestsViewModel;

import java.util.List;

public class MyRequestsFragment extends Fragment implements RequestCallback {
    private Context context;
    private FragmentActivity activity;
    //viewModel
    private HeaderViewModel headerViewModel;
    private RequestsViewModel requestsViewModel;
    private RecyclerView myRequestsRecyclerView;
    private MyRequestAdapter adapter;
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

    public MyRequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        activity = getActivity();
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
       headerViewModel = new ViewModelProvider(this).get(HeaderViewModel.class);
       requestsViewModel = new ViewModelProvider(this).get(RequestsViewModel.class);
        //header views
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

        requestsViewModel.selectMyRequests(SharedPrefs.getInstance(context).getLong(SharedPrefs.ID)).observe(getViewLifecycleOwner(), myRequests -> {
            adapter.setMyRequestList(myRequests);
            adapter.notifyDataSetChanged();
        });

    }

    @Override
    public void onRequestSelected(ReceiptWithCargoList currentItem, RecyclerView.ViewHolder holder) {
        currentItem.getReceipt().setRead(true);
        requestsViewModel.readReceipt(currentItem.getReceipt().getId());

        final MyRequestsFragmentDirections.ActionMyBidsFragmentToParcelDataFragment action = MyRequestsFragmentDirections.actionMyBidsFragmentToParcelDataFragment();
        action.setParcelId(currentItem.getReceipt().getId());
        action.setRequestOrParcel(Constants.INTENT_REQUEST);
        NavHostFragment.findNavController(this).navigate(action);
    }

    @Override
    public void onPlusClicked(Receipt currentItem) {
        //do nothing here
    }
}