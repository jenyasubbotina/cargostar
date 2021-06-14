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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;

import uz.alexits.cargostar.R;
import uz.alexits.cargostar.database.cache.LocalCache;
import uz.alexits.cargostar.entities.transportation.Import;
import uz.alexits.cargostar.entities.transportation.Invoice;
import uz.alexits.cargostar.entities.transportation.Transportation;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.UiUtils;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.adapter.ImportAdapter;
import uz.alexits.cargostar.view.callback.ImportCallback;
import uz.alexits.cargostar.viewmodel.ImportViewModel;
import uz.alexits.cargostar.viewmodel.factory.ImportViewModelFactory;

public class ImportFragment extends Fragment implements ImportCallback {
    //header views
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

    private EditText startDateEditText;
    private EditText endDateEditText;
    private Button submitBtn;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ImportAdapter importAdapter;

    private ImportViewModel importViewModel;

    public ImportFragment() { }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ImportViewModelFactory importFactory = new ImportViewModelFactory(requireContext());
        importViewModel = new ViewModelProvider(getViewModelStore(), importFactory).get(ImportViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_import, container, false);

        /* header views */
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

        startDateEditText = root.findViewById(R.id.start_date_edit_text);
        endDateEditText = root.findViewById(R.id.end_date_edit_text);
        submitBtn = root.findViewById(R.id.submit_btn);

        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh_layout);
        final RecyclerView importListRecyclerView = root.findViewById(R.id.delivery_list_recycler_view);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        importListRecyclerView.setLayoutManager(layoutManager);
        importAdapter = new ImportAdapter(requireContext(), this);
        importListRecyclerView.setAdapter(importAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //header views
        profileImageView.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), MainActivity.class));
        });

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

        startDateEditText.addTextChangedListener(UiUtils.getOnTextChanged(startDateEditText));
        endDateEditText.addTextChangedListener(UiUtils.getOnTextChanged(endDateEditText));

        submitBtn.setOnClickListener(v -> {
            final String startDate = startDateEditText.getText().toString().trim();
            final String endDate = endDateEditText.getText().toString().trim();

            try {
                importViewModel.setDates(startDate, endDate);
            }
            catch (ParseException e) {
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            importViewModel.fetchImportList();
        });
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        importViewModel.removeGetRequestDataUUID();

        /* header */
        importViewModel.getCourierData(requireContext()).observe(getViewLifecycleOwner(), courier -> {
            if (courier != null) {
                fullNameTextView.setText(getString(R.string.header_courier_full_name, courier.getFirstName(), courier.getLastName()));
                courierIdTextView.setText(getString(R.string.courier_id_placeholder, courier.getId()));
            }
        });

        importViewModel.getBrancheData(requireContext()).observe(getViewLifecycleOwner(), branch -> {
            if (branch != null) {
                branchTextView.setText(getString(R.string.header_branch_name, branch.getName()));
            }
        });

        importViewModel.selectNewNotificationsCount().observe(getViewLifecycleOwner(), newNotificationsCount -> {
            if (newNotificationsCount != null) {
                badgeCounterTextView.setText(String.valueOf(newNotificationsCount));
            }
        });

        importViewModel.getSearchRequestResult(requireContext()).observe(getViewLifecycleOwner(), workInfo -> {
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

        importViewModel.getImportList().observe(getViewLifecycleOwner(), importList -> {
            importAdapter.setImportList(importList);
        });

        importViewModel.getFetchImportListResult(requireContext()).observe(getViewLifecycleOwner(), workInfo -> {
            if (workInfo.getState() == WorkInfo.State.SUCCEEDED || workInfo.getState() == WorkInfo.State.FAILED || workInfo.getState() == WorkInfo.State.CANCELLED) {
                swipeRefreshLayout.setRefreshing(false);
                return;
            }
            swipeRefreshLayout.setRefreshing(true);
        });

        importViewModel.getRequestDataFromCacheResult(requireContext()).observe(getViewLifecycleOwner(), workInfo -> {
            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                swipeRefreshLayout.setRefreshing(false);

                NavHostFragment.findNavController(this).navigate(ImportFragmentDirections.actionImportFragmentToInvoiceFragment()
                        .setIsPublic(false)
                        .setIsRequest(false)
                        .setRequestId(workInfo.getOutputData().getLong(Constants.KEY_REQUEST_ID, 0))
                        .setInvoiceId(workInfo.getOutputData().getLong(Constants.KEY_INVOICE_ID, 0))
                        .setCourierId(workInfo.getOutputData().getLong(Constants.KEY_COURIER_ID, 0))
                        .setProviderId(workInfo.getOutputData().getLong(Constants.KEY_PROVIDER_ID, 0))
                        .setClientId(workInfo.getOutputData().getLong(Constants.KEY_CLIENT_ID, 0))
                        .setSenderFirstName(workInfo.getOutputData().getString(Constants.KEY_SENDER_FIRST_NAME))
                        .setSenderLastName(workInfo.getOutputData().getString(Constants.KEY_SENDER_LAST_NAME))
                        .setSenderMiddleName(workInfo.getOutputData().getString(Constants.KEY_SENDER_MIDDLE_NAME))
                        .setSenderEmail(workInfo.getOutputData().getString(Constants.KEY_SENDER_EMAIL))
                        .setSenderPhone(workInfo.getOutputData().getString(Constants.KEY_SENDER_PHONE))
                        .setSenderAddress(workInfo.getOutputData().getString(Constants.KEY_SENDER_ADDRESS))
                        .setSenderCountryId(workInfo.getOutputData().getLong(Constants.KEY_SENDER_COUNTRY_ID, 0))
                        .setSenderCityName(workInfo.getOutputData().getString(Constants.KEY_SENDER_CITY_NAME))
                        .setRecipientCountryId(workInfo.getOutputData().getLong(Constants.KEY_RECIPIENT_COUNTRY_ID, 0))
                        .setRecipientCityName(workInfo.getOutputData().getString(Constants.KEY_RECIPIENT_CITY_NAME))
                        .setDeliveryType(workInfo.getOutputData().getInt(Constants.KEY_DELIVERY_TYPE, 0))
                        .setComment(workInfo.getOutputData().getString(Constants.KEY_COMMENT))
                        .setConsignmentQuantity(workInfo.getOutputData().getInt(Constants.KEY_CONSIGNMENT_QUANTITY, 0)));
                return;
            }
            if (workInfo.getState() == WorkInfo.State.FAILED || workInfo.getState() == WorkInfo.State.CANCELLED) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(requireContext(), "Доставка не найдена...обновите список доставок", Toast.LENGTH_SHORT).show();
                return;
            }
            swipeRefreshLayout.setRefreshing(true);
        });
    }

    @Override
    public void onImportItemClicked(int position, Import item, ImportCallback callback) {
        importViewModel.getRequestDataFromCache(item.getInvoiceId());
    }

    private static final String TAG = ImportFragment.class.toString();
}