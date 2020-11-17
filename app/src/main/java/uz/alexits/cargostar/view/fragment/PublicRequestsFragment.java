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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import uz.alexits.cargostar.R;

import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.shipping.Request;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.viewmodel.CourierViewModel;
import uz.alexits.cargostar.viewmodel.RequestsViewModel;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.activity.CalculatorActivity;
import uz.alexits.cargostar.view.activity.CreateUserActivity;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.activity.NotificationsActivity;
import uz.alexits.cargostar.view.activity.ProfileActivity;
import uz.alexits.cargostar.view.adapter.PublicRequestAdapter;
import uz.alexits.cargostar.view.callback.RequestCallback;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class PublicRequestsFragment extends Fragment implements RequestCallback {
    private Context context;
    private FragmentActivity activity;
    private CourierViewModel courierViewModel;
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

    private LinearLayout lockLayout;
    private ProgressBar progressBar;

    private static long courierId = -1;
    private static UUID bindRequestUUID = null;

    public PublicRequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        activity = getActivity();

        if (getArguments() != null) {
            courierId = PublicRequestsFragmentArgs.fromBundle(getArguments()).getCourierId();
        }

        SyncWorkRequest.fetchRequestData(context);
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

        lockLayout = root.findViewById(R.id.lock_layout);
        progressBar = root.findViewById(R.id.progress_bar);

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
        courierViewModel = new ViewModelProvider(this).get(CourierViewModel.class);
        requestsViewModel = new ViewModelProvider(this).get(RequestsViewModel.class);

        requestsViewModel.getPublicRequests().observe(getViewLifecycleOwner(), requestList -> {
            adapter.setRequestList(requestList);
            adapter.notifyDataSetChanged();
        });

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

        if (bindRequestUUID != null) {

        }
    }

    @Override
    public void onRequestSelected(Request currentItem, RecyclerView.ViewHolder holder) {
        currentItem.setNew(false);

        new Thread(() -> requestsViewModel.readReceipt(currentItem.getId())).start();

        final PublicRequestsFragmentDirections.ActionPublicBidsFragmentToParcelDataFragment action = PublicRequestsFragmentDirections.actionPublicBidsFragmentToParcelDataFragment();
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
        action.setDeliveryType(currentItem.getDeliveryType());
        action.setComment(currentItem.getComment());
        action.setConsignmentQuantity(currentItem.getConsignmentQuantity());
        NavHostFragment.findNavController(this).navigate(action);
    }

    @Override
    public void onPlusClicked(Request currentItem) {
        if (currentItem.getCourierId() == null) {
            Log.i(TAG, "requestId=" + currentItem.getId() + " courierId=" + courierId);
            bindRequestUUID = SyncWorkRequest.bindRequest(context, currentItem.getId(), courierId);

            WorkManager.getInstance(context).getWorkInfoByIdLiveData(bindRequestUUID).observe(getViewLifecycleOwner(), workInfo -> {
                if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {

                    if (workInfo.getOutputData() != null) {
                        final long requestId = workInfo.getOutputData().getLong(Constants.KEY_REQUEST_ID, -1L);

                        Log.i(TAG, "bindRequest: successfully bound request" + requestId);
                        Toast.makeText(context, "Заявка " + requestId + " успешно добавлена в Мои Заявки", Toast.LENGTH_SHORT).show();
                        lockLayout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                    return;
                }
                if (workInfo.getState() == WorkInfo.State.CANCELLED || workInfo.getState() == WorkInfo.State.FAILED) {
                    Log.e(TAG, "bindRequest(): couldn't bind request");
                    Toast.makeText(context, "Ошибка. Не удалось привязать заявку к курьеру", Toast.LENGTH_SHORT).show();

                    lockLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);

                    return;
                }
                lockLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            });
        }
    }

    private static final String TAG = PublicRequestsFragment.class.toString();
}