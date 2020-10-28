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
import uz.alexits.cargostar.viewmodel.HeaderViewModel;
import uz.alexits.cargostar.viewmodel.RequestsViewModel;
import uz.alexits.cargostar.view.Constants;
import uz.alexits.cargostar.view.activity.CalculatorActivity;
import uz.alexits.cargostar.view.activity.CreateUserActivity;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.activity.NotificationsActivity;
import uz.alexits.cargostar.view.activity.ProfileActivity;
import uz.alexits.cargostar.view.adapter.PublicRequestAdapter;
import uz.alexits.cargostar.view.callback.RequestCallback;
import uz.alexits.cargostar.view.fragment.PublicRequestsFragmentDirections;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class PublicRequestsFragment extends Fragment implements RequestCallback {
    private static final String TAG = PublicRequestsFragment.class.toString();
    private Context context;
    private FragmentActivity activity;
    private HeaderViewModel headerViewModel;
    private RequestsViewModel requestsViewModel;
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
    //main content views
    private PublicRequestAdapter adapter;
    private RecyclerView publicBidsRecyclerView;

    public PublicRequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        activity = getActivity();

        SyncWorkRequest.fetchRequestData(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_public_requests, container, false);
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
        publicBidsRecyclerView = root.findViewById(R.id.public_bids_recycler_view);

        adapter = new PublicRequestAdapter(context, this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        publicBidsRecyclerView.setLayoutManager(layoutManager);
        publicBidsRecyclerView.setAdapter(adapter);

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

        requestsViewModel.getPublicRequests().observe(getViewLifecycleOwner(), requestList -> {
            Log.i(TAG, "requestList: " + requestList.size());
            adapter.setRequestList(requestList);
            adapter.notifyDataSetChanged();
        });

        //header views
        headerViewModel.selectCourierByLogin(SharedPrefs.getInstance(context).getString(SharedPrefs.LOGIN)).observe(getViewLifecycleOwner(), courier -> {
            if (courier != null) {
                fullNameTextView.setText(courier.getFirstName() + " " + courier.getLastName());
            }
        });
        headerViewModel.selectBrancheById(SharedPrefs.getInstance(context).getLong(SharedPrefs.ID)).observe(getViewLifecycleOwner(), branch -> {
            if (branch != null) {
                branchTextView.setText(getString(R.string.branch) + " \"" + branch.getName() + "\"");
            }
        });
        headerViewModel.selectNewNotificationsCount().observe(getViewLifecycleOwner(), newNotificationsCount -> {
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

            final long parcelId = Long.parseLong(parcelIdStr);

            headerViewModel.selectRequest(parcelId).observe(getViewLifecycleOwner(), receiptWithCargoList -> {
                if (receiptWithCargoList == null) {
                    Toast.makeText(context, "Накладной не существует", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (receiptWithCargoList.getReceipt() == null) {
//                    Toast.makeText(context, "Накладной не существует", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                final Intent mainIntent = new Intent(context, MainActivity.class);
                mainIntent.putExtra(Constants.INTENT_REQUEST_KEY, Constants.REQUEST_FIND_PARCEL);
                mainIntent.putExtra(Constants.INTENT_REQUEST_VALUE, parcelId);
                startActivity(mainIntent);
            });
        });
    }

    @Override
    public void onRequestSelected(Request currentItem, RecyclerView.ViewHolder holder) {
//        currentItem.setRead(true);
        requestsViewModel.readReceipt(currentItem.getId());
        final PublicRequestsFragmentDirections.ActionPublicBidsFragmentToParcelDataFragment action = PublicRequestsFragmentDirections.actionPublicBidsFragmentToParcelDataFragment();
        action.setParcelId(currentItem.getId());
//        action.setSenderCountryId(currentItem.getSenderCountryId());
//        action.setSenderRegionId(currentItem.getSenderRegionId());
//        action.setSenderCityId(currentItem.getSenderCityId());
//        action.setRecipientCountryId(currentItem.getRecipientCountryId());
//        action.setRecipientCityId(currentItem.getRecipientCityId());
//        action.setProviderId(currentItem.getProviderId());
//        action.setInvoiceId(currentItem.getInvoiceId());
//        action.setUserId(currentItem.getUserId());
//        action.setClientId(currentItem.getClientId());
        action.setRequestOrParcel(Constants.INTENT_REQUEST);
        NavHostFragment.findNavController(this).navigate(action);

    }

    @Override
    public void onPlusClicked(Request currentItem) {
        if (currentItem.getCourierId() <= 0) {
            currentItem.setCourierId(SharedPrefs.getInstance(context).getLong(SharedPrefs.ID));
            requestsViewModel.updateReceipt(currentItem);
            adapter.notifyDataSetChanged();
            Toast.makeText(context, "Заявка " + currentItem.getId() + " успешно добавлена в Мои Заявки", Toast.LENGTH_SHORT).show();
        }
    }
}