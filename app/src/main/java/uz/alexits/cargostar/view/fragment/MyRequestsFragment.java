package uz.alexits.cargostar.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import uz.alexits.cargostar.R;
import uz.alexits.cargostar.entities.transportation.Request;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.view.UiUtils;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.adapter.MyRequestAdapter;
import uz.alexits.cargostar.view.callback.RequestCallback;
import uz.alexits.cargostar.viewmodel.RequestsViewModel;
import uz.alexits.cargostar.viewmodel.factory.RequestsViewModelFactory;

public class MyRequestsFragment extends Fragment implements RequestCallback {
    /* header views */
    private TextView fullNameTextView;
    private TextView branchTextView;
    private TextView courierIdTextView;
    private EditText requestSearchEditText;
    private ImageView requestSearchImageView;
    private ImageView profileImageView;
    private ImageView editImageView;
    private ImageView createUserImageView;
    private ImageView calculatorImageView;
    private ImageView notificationsImageView;
    private TextView badgeCounterTextView;

    /* swipe to refresh */
    private SwipeRefreshLayout swipeRefreshLayout;

    private MyRequestAdapter adapter;

    private RequestsViewModel requestsViewModel;

    public MyRequestsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final RequestsViewModelFactory requestFactory = new RequestsViewModelFactory(requireContext());
        requestsViewModel = new ViewModelProvider(getViewModelStore(), requestFactory).get(RequestsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_my_requests, container, false);

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
        final RecyclerView myRequestsRecyclerView = root.findViewById(R.id.my_bids_recycler_view);
        adapter = new MyRequestAdapter(requireContext(), this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
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
            UiUtils.getNavController(requireActivity(), R.id.main_fragment_container).navigate(R.id.mainFragment);
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

        editImageView.setOnClickListener(v -> {
            UiUtils.getNavController(requireActivity(), R.id.main_fragment_container).navigate(R.id.profileFragment);
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
            requestsViewModel.searchRequest(Long.parseLong(requestId));
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            requestsViewModel.fetchRequestList();
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* header */
        requestsViewModel.getCourierData(requireContext()).observe(getViewLifecycleOwner(), courier -> {
            if (courier != null) {
                fullNameTextView.setText(getString(R.string.header_courier_full_name, courier.getFirstName(), courier.getLastName()));
                courierIdTextView.setText(getString(R.string.courier_id_placeholder, courier.getId()));
            }
        });

        requestsViewModel.getBrancheData(requireContext()).observe(getViewLifecycleOwner(), branch -> {
            if (branch != null) {
                branchTextView.setText(getString(R.string.header_branch_name, branch.getName()));
            }
        });

        requestsViewModel.selectNewNotificationsCount().observe(getViewLifecycleOwner(), newNotificationsCount -> {
            if (newNotificationsCount != null) {
                badgeCounterTextView.setText(String.valueOf(newNotificationsCount));
            }
        });

        requestsViewModel.getSearchRequestResult(requireContext()).observe(getViewLifecycleOwner(), workInfo -> {
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

        requestsViewModel.getMyRequests().observe(getViewLifecycleOwner(), requestList -> {
            for (final Request request : requestList) {
                Log.i(TAG, "->" + request);
            }
            adapter.setMyRequestList(requestList);
        });



        requestsViewModel.getFetchRequestListResult(requireContext()).observe(getViewLifecycleOwner(), workInfo -> {
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
    }

    @Override
    public void onRequestSelected(final int position, final Request currentItem) {
        currentItem.setNew(false);
        requestsViewModel.readRequest(currentItem.getId());
        adapter.notifyItemChanged(position);
        NavHostFragment.findNavController(this).navigate(
                MyRequestsFragmentDirections.actionMyRequestsFragmentToInvoiceFragment()
                        .setIsPublic(false)
                        .setIsRequest(true)
                        .setRequestId(currentItem.getId())
                        .setInvoiceId(currentItem.getInvoiceId())
                        .setCourierId(currentItem.getCourierId())
                        .setProviderId(currentItem.getProviderId())
                        .setClientId(currentItem.getClientId())
                        .setSenderFirstName(currentItem.getSenderFirstName())
                        .setSenderLastName(currentItem.getSenderLastName())
                        .setSenderMiddleName(currentItem.getSenderMiddleName())
                        .setSenderEmail(currentItem.getSenderEmail())
                        .setSenderPhone(currentItem.getSenderPhone())
                        .setSenderAddress(currentItem.getSenderAddress())
                        .setSenderCountryId(currentItem.getSenderCountryId())
                        .setSenderCityName(currentItem.getSenderCity())
                        .setRecipientCountryId(currentItem.getRecipientCountryId())
                        .setRecipientCityName(currentItem.getRecipientCity())
                        .setDeliveryType(currentItem.getDeliveryType())
                        .setComment(currentItem.getComment())
                        .setConsignmentQuantity(currentItem.getConsignmentQuantity())
        );
    }

    private static final String TAG = MyRequestsFragment.class.toString();
}