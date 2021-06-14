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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import uz.alexits.cargostar.R;
import uz.alexits.cargostar.entities.transportation.Consignment;
import uz.alexits.cargostar.entities.transportation.Request;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.view.UiUtils;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.adapter.InvoiceData;
import uz.alexits.cargostar.view.adapter.InvoiceDataAdapter;
import uz.alexits.cargostar.view.callback.InvoiceDataCallback;
import uz.alexits.cargostar.viewmodel.CreateInvoiceViewModel;
import uz.alexits.cargostar.viewmodel.InvoiceViewModel;
import uz.alexits.cargostar.viewmodel.factory.InvoiceViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class InvoiceFragment extends Fragment implements InvoiceDataCallback {
    private static boolean publicDataHidden;
    private static boolean senderDataHidden;
    private static boolean recipientDataHidden;
    private static boolean payerDataHidden;
    private static boolean accountDataHidden;
    private static boolean paymentDataHidden;
    private static boolean transportationDataHidden;

    private static InvoiceData senderDataHeading;
    private static InvoiceData publicDataHeading;
    private static InvoiceData recipientDataHeading;
    private static InvoiceData payerDataHeading;
    private static InvoiceData accountDataHeading;
    private static InvoiceData paymentDataHeading;
    private static InvoiceData transportationDataHeading;
    private static InvoiceData forEachCargoHeading;

    private static List<InvoiceData> itemList;
    private static List<InvoiceData> publicDataList;
    private static List<InvoiceData> senderDataList;
    private static List<InvoiceData> recipientDataList;
    private static List<InvoiceData> payerDataList;
    private static List<InvoiceData> accountDataList;
    private static List<InvoiceData> paymentDataList;
    private static List<InvoiceData> transportationDataList;
    private static List<InvoiceData> forEachCargoList;

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

    /* main content views */
    private RecyclerView dataRecyclerView;
    private InvoiceDataAdapter adapter;
    private ImageView editInvoiceImageView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private InvoiceViewModel invoiceViewModel;

    public InvoiceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        publicDataHidden = false;
        senderDataHidden = false;
        recipientDataHidden = false;
        payerDataHidden = false;
        accountDataHidden = false;
        paymentDataHidden = false;
        transportationDataHidden = false;

        publicDataHeading = new InvoiceData(getString(R.string.public_data), null, InvoiceData.TYPE_HEADING);
        senderDataHeading = new InvoiceData(getString(R.string.sender_data), null, InvoiceData.TYPE_HEADING);
        recipientDataHeading = new InvoiceData(getString(R.string.receiver_data), null, InvoiceData.TYPE_HEADING);
        payerDataHeading = new InvoiceData(getString(R.string.payer_data), null, InvoiceData.TYPE_HEADING);
        accountDataHeading = new InvoiceData(getString(R.string.account_numbers), null, InvoiceData.TYPE_HEADING);
        transportationDataHeading = new InvoiceData(getString(R.string.parcel_data), null, InvoiceData.TYPE_HEADING);
        forEachCargoHeading = new InvoiceData(getString(R.string.for_each_cargo), null, InvoiceData.TYPE_HEADING);
        paymentDataHeading = new InvoiceData(getString(R.string.payment_data), null, InvoiceData.TYPE_HEADING);

        itemList = new ArrayList<>();
        publicDataList = new ArrayList<>();
        senderDataList = new ArrayList<>();
        recipientDataList = new ArrayList<>();
        payerDataList = new ArrayList<>();
        accountDataList = new ArrayList<>();
        paymentDataList = new ArrayList<>();
        transportationDataList = new ArrayList<>();
        forEachCargoList = new ArrayList<>();

        //public data
        itemList.add(publicDataHeading);
        itemList.add(new InvoiceData(getString(R.string.invoice_id), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.courier_id), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.service_provider), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(null, null, InvoiceData.TYPE_STROKE));

        //public data
        publicDataList.add(new InvoiceData(getString(R.string.invoice_id), null, InvoiceData.TYPE_ITEM));
        publicDataList.add(new InvoiceData(getString(R.string.courier_id), null, InvoiceData.TYPE_ITEM));
        publicDataList.add(new InvoiceData(getString(R.string.service_provider), null, InvoiceData.TYPE_ITEM));

        //sender data
        itemList.add(senderDataHeading);
        itemList.add(new InvoiceData(getString(R.string.email), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.first_name), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.middle_name), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.last_name), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.phone_number), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.country), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.city), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.take_address), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.post_index), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.cargostar_account_number), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.tnt_account_number), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.fedex_account_number), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.sender_signature), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(null, null, InvoiceData.TYPE_STROKE));

        //sender data
        senderDataList.add(new InvoiceData(getString(R.string.email), null, InvoiceData.TYPE_ITEM));
        senderDataList.add(new InvoiceData(getString(R.string.first_name), null, InvoiceData.TYPE_ITEM));
        senderDataList.add(new InvoiceData(getString(R.string.middle_name), null, InvoiceData.TYPE_ITEM));
        senderDataList.add(new InvoiceData(getString(R.string.last_name), null, InvoiceData.TYPE_ITEM));
        senderDataList.add(new InvoiceData(getString(R.string.phone_number), null, InvoiceData.TYPE_ITEM));
        senderDataList.add(new InvoiceData(getString(R.string.country), null, InvoiceData.TYPE_ITEM));
        senderDataList.add(new InvoiceData(getString(R.string.city), null, InvoiceData.TYPE_ITEM));
        senderDataList.add(new InvoiceData(getString(R.string.take_address), null, InvoiceData.TYPE_ITEM));
        senderDataList.add(new InvoiceData(getString(R.string.post_index), null, InvoiceData.TYPE_ITEM));
        senderDataList.add(new InvoiceData(getString(R.string.cargostar_account_number), null, InvoiceData.TYPE_ITEM));
        senderDataList.add(new InvoiceData(getString(R.string.tnt_account_number), null, InvoiceData.TYPE_ITEM));
        senderDataList.add(new InvoiceData(getString(R.string.fedex_account_number), null, InvoiceData.TYPE_ITEM));
        senderDataList.add(new InvoiceData(getString(R.string.sender_signature), null, InvoiceData.TYPE_ITEM));

        //recipient data
        itemList.add(recipientDataHeading);
        itemList.add(new InvoiceData(getString(R.string.email), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.first_name), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.middle_name), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.last_name), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.phone_number), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.country), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.city), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.delivery_address), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.post_index), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.cargostar_account_number), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.tnt_account_number), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.fedex_account_number), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.receiver_signature), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(null, null, InvoiceData.TYPE_STROKE));

        //recipient data
        recipientDataList.add(new InvoiceData(getString(R.string.email), null, InvoiceData.TYPE_ITEM));
        recipientDataList.add(new InvoiceData(getString(R.string.first_name), null, InvoiceData.TYPE_ITEM));
        recipientDataList.add(new InvoiceData(getString(R.string.middle_name), null, InvoiceData.TYPE_ITEM));
        recipientDataList.add(new InvoiceData(getString(R.string.last_name), null, InvoiceData.TYPE_ITEM));
        recipientDataList.add(new InvoiceData(getString(R.string.phone_number), null, InvoiceData.TYPE_ITEM));
        recipientDataList.add(new InvoiceData(getString(R.string.country), null, InvoiceData.TYPE_ITEM));
        recipientDataList.add(new InvoiceData(getString(R.string.city), null, InvoiceData.TYPE_ITEM));
        recipientDataList.add(new InvoiceData(getString(R.string.delivery_address), null, InvoiceData.TYPE_ITEM));
        recipientDataList.add(new InvoiceData(getString(R.string.post_index), null, InvoiceData.TYPE_ITEM));
        recipientDataList.add(new InvoiceData(getString(R.string.cargostar_account_number), null, InvoiceData.TYPE_ITEM));
        recipientDataList.add(new InvoiceData(getString(R.string.tnt_account_number), null, InvoiceData.TYPE_ITEM));
        recipientDataList.add(new InvoiceData(getString(R.string.fedex_account_number), null, InvoiceData.TYPE_ITEM));
        recipientDataList.add(new InvoiceData(getString(R.string.receiver_signature), null, InvoiceData.TYPE_ITEM));

        //payer data
        itemList.add(payerDataHeading);
        itemList.add(new InvoiceData(getString(R.string.email), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.first_name), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.middle_name), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.last_name), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.phone_number), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.country), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.city), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.address), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.post_index), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.cargostar_account_number), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.tnt_account_number), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.fedex_account_number), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(null, null, InvoiceData.TYPE_STROKE));

        //payer data
        payerDataList.add(new InvoiceData(getString(R.string.email), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.first_name), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.middle_name), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.last_name), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.phone_number), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.country), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.city), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.address), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.post_index), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.cargostar_account_number), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.tnt_account_number), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.fedex_account_number), null, InvoiceData.TYPE_ITEM));

        //accounts data
        itemList.add(accountDataHeading);
        itemList.add(new InvoiceData(getString(R.string.contract_number), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(null, null, InvoiceData.TYPE_STROKE));

        //accounts data
        accountDataList.add(new InvoiceData(getString(R.string.contract_number), null, InvoiceData.TYPE_ITEM));

        //payment data
        itemList.add(paymentDataHeading);
        itemList.add(new InvoiceData(getString(R.string.cost), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.fuel_tax), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.tariff), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(null, null, InvoiceData.TYPE_STROKE));

        //payment data
        paymentDataList.add(new InvoiceData(getString(R.string.cost), null, InvoiceData.TYPE_ITEM));
        paymentDataList.add(new InvoiceData(getString(R.string.fuel_tax), null, InvoiceData.TYPE_ITEM));
        paymentDataList.add(new InvoiceData(getString(R.string.tariff), null, InvoiceData.TYPE_ITEM));

        //transportation data
        itemList.add(transportationDataHeading);
        itemList.add(new InvoiceData(getString(R.string.tracking_code_main), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.qr_code), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.destination_quantity), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.arrival_date), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.courier_guidelines), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(null, null, InvoiceData.TYPE_STROKE));

        //transportation data
        transportationDataList.add(new InvoiceData(getString(R.string.tracking_code_main), null, InvoiceData.TYPE_ITEM));
        transportationDataList.add(new InvoiceData(getString(R.string.qr_code), null, InvoiceData.TYPE_ITEM));
        transportationDataList.add(new InvoiceData(getString(R.string.destination_quantity), null, InvoiceData.TYPE_ITEM));
        transportationDataList.add(new InvoiceData(getString(R.string.arrival_date), null, InvoiceData.TYPE_ITEM));
        transportationDataList.add(new InvoiceData(getString(R.string.courier_guidelines), null, InvoiceData.TYPE_ITEM));

        final InvoiceViewModelFactory invoiceFactory = new InvoiceViewModelFactory(requireContext());
        invoiceViewModel = new ViewModelProvider(getViewModelStore(), invoiceFactory).get(InvoiceViewModel.class);

        if (getArguments() != null) {
            invoiceViewModel.setIsRequest(InvoiceFragmentArgs.fromBundle(getArguments()).getIsRequest());
            invoiceViewModel.setIsRequestPublic(InvoiceFragmentArgs.fromBundle(getArguments()).getIsPublic());
            invoiceViewModel.setInvoiceId(InvoiceFragmentArgs.fromBundle(getArguments()).getInvoiceId());
            invoiceViewModel.setClientId(InvoiceFragmentArgs.fromBundle(getArguments()).getClientId());
            invoiceViewModel.setCurrentConsignmentQuantity(InvoiceFragmentArgs.fromBundle(getArguments()).getConsignmentQuantity());

            invoiceViewModel.setCurrentRequest(new Request(
                    InvoiceFragmentArgs.fromBundle(getArguments()).getRequestId(),
                    InvoiceFragmentArgs.fromBundle(getArguments()).getSenderFirstName(),
                    InvoiceFragmentArgs.fromBundle(getArguments()).getSenderMiddleName(),
                    InvoiceFragmentArgs.fromBundle(getArguments()).getSenderLastName(),
                    InvoiceFragmentArgs.fromBundle(getArguments()).getSenderEmail(),
                    InvoiceFragmentArgs.fromBundle(getArguments()).getSenderPhone(),
                    InvoiceFragmentArgs.fromBundle(getArguments()).getSenderAddress(),
                    InvoiceFragmentArgs.fromBundle(getArguments()).getSenderCountryId(),
                    InvoiceFragmentArgs.fromBundle(getArguments()).getSenderCityName(),
                    InvoiceFragmentArgs.fromBundle(getArguments()).getRecipientCountryId(),
                    InvoiceFragmentArgs.fromBundle(getArguments()).getRecipientCityName(),
                    InvoiceFragmentArgs.fromBundle(getArguments()).getComment(),
                    InvoiceFragmentArgs.fromBundle(getArguments()).getSenderUserId(),
                    InvoiceFragmentArgs.fromBundle(getArguments()).getClientId(),
                    InvoiceFragmentArgs.fromBundle(getArguments()).getCourierId(),
                    InvoiceFragmentArgs.fromBundle(getArguments()).getProviderId(),
                    InvoiceFragmentArgs.fromBundle(getArguments()).getInvoiceId(),
                    InvoiceFragmentArgs.fromBundle(getArguments()).getSenderCityName(),
                    InvoiceFragmentArgs.fromBundle(getArguments()).getRecipientCityName(),
                    InvoiceFragmentArgs.fromBundle(getArguments()).getConsignmentQuantity(),
                    InvoiceFragmentArgs.fromBundle(getArguments()).getPaymentStatus(),
                    InvoiceFragmentArgs.fromBundle(getArguments()).getDeliveryType()));

            //consignment
            itemList.add(forEachCargoHeading);

            for (int i = 0; i < InvoiceFragmentArgs.fromBundle(getArguments()).getConsignmentQuantity(); i++) {
                itemList.add(new InvoiceData(getString(R.string.cargo_name), null, InvoiceData.TYPE_ITEM));
                itemList.add(new InvoiceData(getString(R.string.cargo_description), null, InvoiceData.TYPE_ITEM));
                itemList.add(new InvoiceData(getString(R.string.cargo_price), null, InvoiceData.TYPE_ITEM));
                itemList.add(new InvoiceData(getString(R.string.package_type), null, InvoiceData.TYPE_ITEM));
                itemList.add(new InvoiceData(getString(R.string.dimensions), null, InvoiceData.TYPE_ITEM));
                itemList.add(new InvoiceData(getString(R.string.weight), null, InvoiceData.TYPE_ITEM));
                itemList.add(new InvoiceData(getString(R.string.qr_code), null, InvoiceData.TYPE_ITEM));
            }

            itemList.add(new InvoiceData(null, null, InvoiceData.TYPE_STROKE));

            invoiceViewModel.fetchInvoiceData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_invoice, container, false);

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
        editInvoiceImageView = root.findViewById(R.id.edit_parcel_image_view);
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh_layout);

        dataRecyclerView = root.findViewById(R.id.data_recycler_view);
        adapter = new InvoiceDataAdapter(requireContext(), this);
        dataRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        dataRecyclerView.setAdapter(adapter);

        adapter.setItemList(itemList);
        adapter.notifyDataSetChanged();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!invoiceViewModel.isPublic() && invoiceViewModel.isRequest()) {
            editInvoiceImageView.setVisibility(View.VISIBLE);
        }
        else {
            editInvoiceImageView.setVisibility(View.INVISIBLE);
        }

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

        editInvoiceImageView.setOnClickListener(v -> {
            final InvoiceFragmentDirections.ActionInvoiceDataFragmentToCreateInvoiceFragment action = InvoiceFragmentDirections.actionInvoiceDataFragmentToCreateInvoiceFragment();
            action.setAction(CreateInvoiceViewModel.CREATE_INVOICE_ACTION_EDIT);

            if (invoiceViewModel.getCurrentRequest().getValue() != null) {
                action.setRequestId(invoiceViewModel.getCurrentRequest().getValue().getId())
                        .setInvoiceId(invoiceViewModel.getCurrentRequest().getValue().getInvoiceId())
                        .setCourierId(invoiceViewModel.getCurrentRequest().getValue().getCourierId())
                        .setSenderId(invoiceViewModel.getClientId())
                        .setRecipientId(invoiceViewModel.getRecipientId())
                        .setPayerId(invoiceViewModel.getPayerId())
                        .setPackagingId(invoiceViewModel.getPackagingId())
                        .setTotalPrice((float) invoiceViewModel.getTotalPrice())
                        .setProviderId(invoiceViewModel.getCurrentRequest().getValue().getProviderId())
                        .setPaymentMethod(invoiceViewModel.getPaymentMethod())
                        .setDeliveryType(invoiceViewModel.getCurrentRequest().getValue().getDeliveryType())
                        .setSenderEmail(invoiceViewModel.getCurrentRequest().getValue().getSenderEmail())
                        .setSenderFirstName(invoiceViewModel.getCurrentRequest().getValue().getSenderFirstName())
                        .setSenderLastName(invoiceViewModel.getCurrentRequest().getValue().getSenderLastName())
                        .setSenderMiddleName(invoiceViewModel.getCurrentRequest().getValue().getSenderMiddleName())
                        .setSenderPhone(invoiceViewModel.getCurrentRequest().getValue().getSenderPhone())
                        .setSenderAddress(invoiceViewModel.getCurrentRequest().getValue().getSenderAddress())
                        .setSenderCountryId(invoiceViewModel.getCurrentRequest().getValue().getSenderCountryId())
                        .setSenderCityName(invoiceViewModel.getCurrentRequest().getValue().getSenderCity())
                        .setRecipientCountryId(invoiceViewModel.getCurrentRequest().getValue().getRecipientCountryId())
                        .setRecipientCityName(invoiceViewModel.getCurrentRequest().getValue().getRecipientCity())
                        .setComment(invoiceViewModel.getCurrentRequest().getValue().getComment())
                        .setConsignmentQuantity(invoiceViewModel.getCurrentRequest().getValue().getConsignmentQuantity());
            }
            NavHostFragment.findNavController(this).navigate(action);
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
            invoiceViewModel.searchRequest(Long.parseLong(requestId));
        });

        swipeRefreshLayout.setOnRefreshListener(() -> invoiceViewModel.fetchInvoiceData());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* header */
        invoiceViewModel.getCourierData(requireContext()).observe(getViewLifecycleOwner(), courier -> {
            if (courier != null) {
                fullNameTextView.setText(getString(R.string.header_courier_full_name, courier.getFirstName(), courier.getLastName()));
                courierIdTextView.setText(getString(R.string.courier_id_placeholder, courier.getId()));
            }
        });

        invoiceViewModel.getBrancheData(requireContext()).observe(getViewLifecycleOwner(), branch -> {
            if (branch != null) {
                branchTextView.setText(getString(R.string.header_branch_name, branch.getName()));
            }
        });

        invoiceViewModel.selectNewNotificationsCount().observe(getViewLifecycleOwner(), newNotificationsCount -> {
            if (newNotificationsCount != null) {
                badgeCounterTextView.setText(String.valueOf(newNotificationsCount));
            }
        });

        invoiceViewModel.getSearchRequestResult(requireContext()).observe(getViewLifecycleOwner(), workInfo -> {
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

        /* public & payment data */
        invoiceViewModel.getCurrentRequest().observe(getViewLifecycleOwner(), requestData -> {
            if (requestData != null) {
                invoiceViewModel.setSenderCountryId(requestData.getSenderCountryId());
                invoiceViewModel.setRecipientCountryId(requestData.getRecipientCountryId());
                invoiceViewModel.setProviderId(requestData.getProviderId());

                itemList.set(1, new InvoiceData(getString(R.string.invoice_id), requestData.getInvoiceId() > 0 ? String.valueOf(requestData.getInvoiceId()) : null, InvoiceData.TYPE_ITEM));
                itemList.set(2, new InvoiceData(getString(R.string.courier_id), requestData.getCourierId() > 0 ? String.valueOf(requestData.getCourierId()) : null, InvoiceData.TYPE_ITEM));

                itemList.set(6, new InvoiceData(getString(R.string.email), requestData.getSenderEmail(), InvoiceData.TYPE_ITEM));
                itemList.set(7, new InvoiceData(getString(R.string.first_name), requestData.getSenderFirstName(), InvoiceData.TYPE_ITEM));
                itemList.set(8, new InvoiceData(getString(R.string.middle_name), requestData.getSenderMiddleName(), InvoiceData.TYPE_ITEM));
                itemList.set(9, new InvoiceData(getString(R.string.last_name), requestData.getSenderLastName(), InvoiceData.TYPE_ITEM));
                itemList.set(10, new InvoiceData(getString(R.string.phone_number), requestData.getSenderPhone(), InvoiceData.TYPE_ITEM));

                itemList.set(12, new InvoiceData(getString(R.string.city), requestData.getSenderCity(), InvoiceData.TYPE_ITEM));
                itemList.set(13, new InvoiceData(getString(R.string.take_address), requestData.getSenderAddress(), InvoiceData.TYPE_ITEM));

                itemList.set(27, new InvoiceData(getString(R.string.city), requestData.getRecipientCity(), InvoiceData.TYPE_ITEM));

                itemList.set(60, new InvoiceData(getString(R.string.destination_quantity), String.valueOf(requestData.getConsignmentQuantity()), InvoiceData.TYPE_ITEM));
                itemList.set(62, new InvoiceData(getString(R.string.courier_guidelines), requestData.getComment(), InvoiceData.TYPE_ITEM));

                publicDataList.set(0, new InvoiceData(getString(R.string.invoice_id), requestData.getInvoiceId() > 0 ? String.valueOf(requestData.getInvoiceId()) : null, InvoiceData.TYPE_ITEM));
                publicDataList.set(1, new InvoiceData(getString(R.string.courier_id), requestData.getCourierId() > 0 ? String.valueOf(requestData.getCourierId()) : null, InvoiceData.TYPE_ITEM));

                senderDataList.set(0, new InvoiceData(getString(R.string.email), requestData.getSenderEmail(), InvoiceData.TYPE_ITEM));
                senderDataList.set(1, new InvoiceData(getString(R.string.first_name), requestData.getSenderFirstName(), InvoiceData.TYPE_ITEM));
                senderDataList.set(2, new InvoiceData(getString(R.string.middle_name), requestData.getSenderMiddleName(), InvoiceData.TYPE_ITEM));
                senderDataList.set(3, new InvoiceData(getString(R.string.last_name), requestData.getSenderLastName(), InvoiceData.TYPE_ITEM));
                senderDataList.set(4, new InvoiceData(getString(R.string.phone_number), requestData.getSenderPhone(), InvoiceData.TYPE_ITEM));
                senderDataList.set(6, new InvoiceData(getString(R.string.city), requestData.getSenderCityName(), InvoiceData.TYPE_ITEM));
                senderDataList.set(7, new InvoiceData(getString(R.string.take_address), requestData.getSenderAddress(), InvoiceData.TYPE_ITEM));

                recipientDataList.set(6, new InvoiceData(getString(R.string.city), requestData.getRecipientCityName(), InvoiceData.TYPE_ITEM));

                transportationDataList.set(2, new InvoiceData(getString(R.string.destination_quantity), String.valueOf(requestData.getConsignmentQuantity()), InvoiceData.TYPE_ITEM));
                transportationDataList.set(4, new InvoiceData(getString(R.string.courier_guidelines), requestData.getComment(), InvoiceData.TYPE_ITEM));

                adapter.notifyItemChanged(1, 2);
                adapter.notifyItemRangeChanged(6, 5);
                adapter.notifyItemRangeChanged(12, 2);
                adapter.notifyItemChanged(27);
                adapter.notifyItemChanged(60);
                adapter.notifyItemChanged(62);
            }
        });

        invoiceViewModel.getProvider().observe(getViewLifecycleOwner(), provider -> {
            if (provider != null) {
                itemList.set(3, new InvoiceData(getString(R.string.service_provider), provider.getNameEn(), InvoiceData.TYPE_ITEM));
                itemList.set(54, new InvoiceData(getString(R.string.fuel_tax), String.valueOf(provider.getFuel()), InvoiceData.TYPE_ITEM));
                publicDataList.set(2, new InvoiceData(getString(R.string.service_provider), null, InvoiceData.TYPE_ITEM));
                paymentDataList.set(1, new InvoiceData(getString(R.string.fuel_tax), String.valueOf(provider.getFuel()), InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(3);
                adapter.notifyItemChanged(54);
            }
        });

        /* payment data */
        invoiceViewModel.getPackagingData().observe(getViewLifecycleOwner(), tariff -> {
            if (tariff != null) {
                itemList.set(55, new InvoiceData(getString(R.string.tariff), tariff.getName(), InvoiceData.TYPE_ITEM));
                paymentDataList.set(2, new InvoiceData(getString(R.string.tariff), tariff.getName(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(55);
            }
        });

        invoiceViewModel.getSenderData().observe(getViewLifecycleOwner(), sender -> {
            if (sender != null) {
                itemList.set(14, new InvoiceData(getString(R.string.post_index), sender.getZip(), InvoiceData.TYPE_ITEM));
                itemList.set(15, new InvoiceData(getString(R.string.cargostar_account_number), sender.getCargostarAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(16, new InvoiceData(getString(R.string.tnt_account_number), sender.getTntAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(17, new InvoiceData(getString(R.string.fedex_account_number), sender.getFedexAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(18, new InvoiceData(getString(R.string.sender_signature), sender.getSignatureUrl(), InvoiceData.TYPE_ITEM));

                senderDataList.set(8, new InvoiceData(getString(R.string.post_index), sender.getZip(), InvoiceData.TYPE_ITEM));
                senderDataList.set(9, new InvoiceData(getString(R.string.cargostar_account_number), sender.getCargostarAccountNumber(), InvoiceData.TYPE_ITEM));
                senderDataList.set(10, new InvoiceData(getString(R.string.tnt_account_number), sender.getTntAccountNumber(), InvoiceData.TYPE_ITEM));
                senderDataList.set(11, new InvoiceData(getString(R.string.fedex_account_number), sender.getFedexAccountNumber(), InvoiceData.TYPE_ITEM));
                senderDataList.set(12, new InvoiceData(getString(R.string.sender_signature), sender.getSignatureUrl(), InvoiceData.TYPE_ITEM));

                adapter.notifyItemRangeChanged(14, 5);
            }
        });

        invoiceViewModel.getSenderCountry().observe(getViewLifecycleOwner(), country -> {
            if (country != null) {
                itemList.set(11, new InvoiceData(getString(R.string.country), country.getNameEn(), InvoiceData.TYPE_ITEM));
                senderDataList.set(5, new InvoiceData(getString(R.string.country), country.getNameEn(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(11);
            }
        });

        /* recipient data */
        invoiceViewModel.getRecipientData().observe(getViewLifecycleOwner(), recipient -> {
            if (recipient != null) {
                itemList.set(21, new InvoiceData(getString(R.string.email), recipient.getEmail(), InvoiceData.TYPE_ITEM));
                itemList.set(22, new InvoiceData(getString(R.string.first_name), recipient.getFirstName(), InvoiceData.TYPE_ITEM));
                itemList.set(23, new InvoiceData(getString(R.string.middle_name), recipient.getMiddleName(), InvoiceData.TYPE_ITEM));
                itemList.set(24, new InvoiceData(getString(R.string.last_name), recipient.getLastName(), InvoiceData.TYPE_ITEM));
                itemList.set(25, new InvoiceData(getString(R.string.phone_number), recipient.getPhone(), InvoiceData.TYPE_ITEM));

                itemList.set(28, new InvoiceData(getString(R.string.delivery_address), recipient.getAddress(), InvoiceData.TYPE_ITEM));
                itemList.set(29, new InvoiceData(getString(R.string.post_index), recipient.getZip(), InvoiceData.TYPE_ITEM));
                itemList.set(30, new InvoiceData(getString(R.string.cargostar_account_number), recipient.getCargostarAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(31, new InvoiceData(getString(R.string.tnt_account_number), recipient.getTntAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(32, new InvoiceData(getString(R.string.fedex_account_number), recipient.getFedexAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(33, new InvoiceData(getString(R.string.receiver_signature), null, InvoiceData.TYPE_ITEM));

                recipientDataList.set(0, new InvoiceData(getString(R.string.email), recipient.getEmail(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(1, new InvoiceData(getString(R.string.first_name), recipient.getFirstName(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(2, new InvoiceData(getString(R.string.middle_name),  recipient.getMiddleName(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(3, new InvoiceData(getString(R.string.last_name), recipient.getLastName(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(4, new InvoiceData(getString(R.string.phone_number), recipient.getPhone(), InvoiceData.TYPE_ITEM));

                recipientDataList.set(7, new InvoiceData(getString(R.string.delivery_address), recipient.getAddress(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(8, new InvoiceData(getString(R.string.post_index), recipient.getZip(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(9, new InvoiceData(getString(R.string.cargostar_account_number), recipient.getCargostarAccountNumber(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(10, new InvoiceData(getString(R.string.tnt_account_number), recipient.getTntAccountNumber(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(11, new InvoiceData(getString(R.string.fedex_account_number), recipient.getFedexAccountNumber(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(12, new InvoiceData(getString(R.string.receiver_signature), null, InvoiceData.TYPE_ITEM));

                adapter.notifyItemRangeChanged(21, 5);
                adapter.notifyItemRangeChanged(28, 6);
            }
        });

        invoiceViewModel.getRecipientCountry().observe(getViewLifecycleOwner(), country -> {
            if (country != null) {
                itemList.set(26, new InvoiceData(getString(R.string.country), country.getNameEn(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(5, new InvoiceData(getString(R.string.country), country.getNameEn(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(26);
            }
        });

        /* payer data */
        invoiceViewModel.getPayerData().observe(getViewLifecycleOwner(), payer -> {
            if (payer != null) {
                invoiceViewModel.setPayerCountryId(payer.getCountryId());

                itemList.set(36, new InvoiceData(getString(R.string.email), payer.getEmail(), InvoiceData.TYPE_ITEM));
                itemList.set(37, new InvoiceData(getString(R.string.first_name), payer.getFirstName(), InvoiceData.TYPE_ITEM));
                itemList.set(38, new InvoiceData(getString(R.string.middle_name), payer.getMiddleName(), InvoiceData.TYPE_ITEM));
                itemList.set(39, new InvoiceData(getString(R.string.last_name), payer.getLastName(), InvoiceData.TYPE_ITEM));
                itemList.set(40, new InvoiceData(getString(R.string.phone_number), payer.getPhone(), InvoiceData.TYPE_ITEM));
                itemList.set(42, new InvoiceData(getString(R.string.city), payer.getCityName(), InvoiceData.TYPE_ITEM));
                itemList.set(43, new InvoiceData(getString(R.string.address), payer.getAddress(), InvoiceData.TYPE_ITEM));
                itemList.set(44, new InvoiceData(getString(R.string.post_index), payer.getZip(), InvoiceData.TYPE_ITEM));
                itemList.set(45, new InvoiceData(getString(R.string.cargostar_account_number), payer.getCargostarAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(46, new InvoiceData(getString(R.string.tnt_account_number), payer.getTntAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(47, new InvoiceData(getString(R.string.fedex_account_number), payer.getFedexAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(50, new InvoiceData(getString(R.string.contract_number), payer.getContractNumber(), InvoiceData.TYPE_ITEM));

                payerDataList.set(0, new InvoiceData(getString(R.string.email), payer.getEmail(), InvoiceData.TYPE_ITEM));
                payerDataList.set(1, new InvoiceData(getString(R.string.first_name), payer.getFirstName(), InvoiceData.TYPE_ITEM));
                payerDataList.set(2, new InvoiceData(getString(R.string.middle_name), payer.getMiddleName(), InvoiceData.TYPE_ITEM));
                payerDataList.set(3, new InvoiceData(getString(R.string.last_name), payer.getLastName(), InvoiceData.TYPE_ITEM));
                payerDataList.set(4, new InvoiceData(getString(R.string.phone_number), payer.getPhone(), InvoiceData.TYPE_ITEM));
                payerDataList.set(6, new InvoiceData(getString(R.string.city), payer.getCityName(), InvoiceData.TYPE_ITEM));
                payerDataList.set(7, new InvoiceData(getString(R.string.address), payer.getAddress(), InvoiceData.TYPE_ITEM));
                payerDataList.set(8, new InvoiceData(getString(R.string.post_index), payer.getZip(), InvoiceData.TYPE_ITEM));
                payerDataList.set(9, new InvoiceData(getString(R.string.cargostar_account_number), payer.getCargostarAccountNumber(), InvoiceData.TYPE_ITEM));
                payerDataList.set(10, new InvoiceData(getString(R.string.tnt_account_number), payer.getTntAccountNumber(), InvoiceData.TYPE_ITEM));
                payerDataList.set(11, new InvoiceData(getString(R.string.fedex_account_number), payer.getFedexAccountNumber(), InvoiceData.TYPE_ITEM));
                accountDataList.set(0, new InvoiceData(getString(R.string.checking_account), payer.getContractNumber(), InvoiceData.TYPE_ITEM));

                //account data
                adapter.notifyItemRangeChanged(36, 5);
                adapter.notifyItemRangeChanged(42, 6);
                adapter.notifyItemChanged(50);
            }
        });

        invoiceViewModel.getPayerCountry().observe(getViewLifecycleOwner(), country -> {
            if (country != null) {
                itemList.set(41, new InvoiceData(getString(R.string.country), country.getNameEn(), InvoiceData.TYPE_ITEM));
                payerDataList.set(5, new InvoiceData(getString(R.string.country), country.getNameEn(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(41);
            }
        });

        /* invoice data */
        invoiceViewModel.getInvoiceData().observe(getViewLifecycleOwner(), invoice -> {
            if (invoice != null) {
                invoiceViewModel.setCurrentRecipientId(invoice.getRecipientId());
                invoiceViewModel.setCurrentPayerId(invoice.getPayerId());
                invoiceViewModel.setCurrentPackagingId(invoice.getTariffId());
                invoiceViewModel.setPackagingId(invoice.getTariffId());
                invoiceViewModel.setPaymentMethod(invoice.getPaymentMethod());
                invoiceViewModel.setTotalPrice(invoice.getPrice());
                itemList.set(53, new InvoiceData(getString(R.string.cost), String.valueOf(invoice.getPrice()), InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(53);

                paymentDataList.set(0, new InvoiceData(getString(R.string.cost), String.valueOf(invoice.getPrice()), InvoiceData.TYPE_ITEM));
            }
        });

        /* transportation data */
        invoiceViewModel.getTransportation().observe(getViewLifecycleOwner(), transportation -> {
            if (transportation != null) {
                itemList.set(58, new InvoiceData(getString(R.string.tracking_code_main), transportation.getTrackingCode(), InvoiceData.TYPE_ITEM));
                itemList.set(59, new InvoiceData(getString(R.string.qr_code), transportation.getQrCode(), InvoiceData.TYPE_ITEM));
                itemList.set(61, new InvoiceData(getString(R.string.arrival_date), transportation.getArrivalDate(), InvoiceData.TYPE_ITEM));
                itemList.set(62, new InvoiceData(getString(R.string.courier_guidelines), transportation.getInstructions(), InvoiceData.TYPE_ITEM));

                transportationDataList.set(0, new InvoiceData(getString(R.string.tracking_code_main), transportation.getTrackingCode(), InvoiceData.TYPE_ITEM));
                transportationDataList.set(1, new InvoiceData(getString(R.string.qr_code), transportation.getQrCode(), InvoiceData.TYPE_ITEM));
                transportationDataList.set(3, new InvoiceData(getString(R.string.arrival_date), transportation.getArrivalDate(), InvoiceData.TYPE_ITEM));
                transportationDataList.set(4, new InvoiceData(getString(R.string.courier_guidelines), transportation.getInstructions(), InvoiceData.TYPE_ITEM));

                adapter.notifyItemRangeChanged(58, 2);
                adapter.notifyItemRangeChanged(61, 2);
            }
        });

        /* consignment data */
        invoiceViewModel.getReceivedConsignmentList().observe(getViewLifecycleOwner(), consignmentList -> {
            //for each cargo
            if (consignmentList != null && !consignmentList.isEmpty()) {
                int i = 0;
                int j = 65;

                for (final Consignment consignment : consignmentList) {
                    itemList.set(j + i, new InvoiceData(getString(R.string.cargo_name), consignment.getName(), InvoiceData.TYPE_ITEM));
                    j++;
                    itemList.set(j + i, new InvoiceData(getString(R.string.cargo_description), consignment.getDescription(), InvoiceData.TYPE_ITEM));
                    j++;
                    itemList.set(j + i, new InvoiceData(getString(R.string.cargo_price), consignment.getCost(), InvoiceData.TYPE_ITEM));
                    j++;
                    itemList.set(j + i, new InvoiceData(getString(R.string.package_type), String.valueOf(consignment.getPackagingId()), InvoiceData.TYPE_ITEM));
                    j++;
                    itemList.set(j + i, new InvoiceData(getString(R.string.dimensions), String.valueOf(consignment.getDimensions()), InvoiceData.TYPE_ITEM));
                    j++;
                    itemList.set(j + i, new InvoiceData(getString(R.string.weight), String.valueOf(consignment.getWeight()), InvoiceData.TYPE_ITEM));
                    j++;
                    itemList.set(j + i, new InvoiceData(getString(R.string.qr_code), consignment.getQr(), InvoiceData.TYPE_ITEM));
                    i++;
                }
                dataRecyclerView.post(() -> {
                    adapter.notifyItemRangeChanged(64, forEachCargoList.size());
                });
            }
        });

        invoiceViewModel.getFetchInvoiceDataResult(requireContext()).observe(getViewLifecycleOwner(), workInfo -> {
            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                editInvoiceImageView.setEnabled(true);
                swipeRefreshLayout.setRefreshing(false);

                if (!invoiceViewModel.isPublic() && invoiceViewModel.isRequest()) {
                    editInvoiceImageView.setVisibility(View.VISIBLE);
                }
                return;
            }
            if (workInfo.getState() == WorkInfo.State.FAILED || workInfo.getState() == WorkInfo.State.CANCELLED) {
                Toast.makeText(requireContext(), "Не удалось обновить данные с сервера", Toast.LENGTH_SHORT).show();
                editInvoiceImageView.setEnabled(false);
                swipeRefreshLayout.setRefreshing(false);

                if (!invoiceViewModel.isPublic() && invoiceViewModel.isRequest()) {
                    editInvoiceImageView.setVisibility(View.GONE);
                }
                return;
            }
            editInvoiceImageView.setEnabled(false);
            swipeRefreshLayout.setRefreshing(true);

            if (!invoiceViewModel.isPublic() && invoiceViewModel.isRequest()) {
                editInvoiceImageView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onArrowTapped(final int position) {
        final InvoiceData currentItem = itemList.get(position);
        currentItem.isHidden = !currentItem.isHidden;

        /* public data */
        if (currentItem.equals(publicDataHeading)) {
            if (publicDataHidden) {
                itemList.addAll(position + 1, publicDataList);
            }
            else {
                for (int i = 0; i < 3; i++) {
                    itemList.remove(position + 1);
                }
            }
            publicDataHidden = !publicDataHidden;
        }
        /* sender data */
        else if (currentItem.equals(senderDataHeading)) {
            if (senderDataHidden) {
                itemList.addAll(position + 1, senderDataList);
            }
            else {
                for (int i = 0; i < 13; i++) {
                    itemList.remove(position + 1);
                }
            }
            senderDataHidden = !senderDataHidden;
        }
        /* recipient data */
        else if (currentItem.equals(recipientDataHeading)) {
            if (recipientDataHidden) {
                itemList.addAll(position + 1, recipientDataList);
            }
            else {
                for (int i = 0; i < 13; i++) {
                    itemList.remove(position + 1);
                }
            }
            recipientDataHidden = !recipientDataHidden;
        }
        /* payer data */
        else if (currentItem.equals(payerDataHeading)) {
            if (payerDataHidden) {
                itemList.addAll(position + 1, payerDataList);
            }
            else {
                for (int i = 0; i < 12; i++) {
                    itemList.remove(position + 1);
                }
            }
            payerDataHidden = !payerDataHidden;
        }
        /* account data */
        else if (currentItem.equals(accountDataHeading)) {
            if (accountDataHidden) {
                itemList.addAll(position + 1, accountDataList);
            }
            else {
                itemList.remove(position + 1);
            }
            accountDataHidden = !accountDataHidden;
        }
        else if (currentItem.equals(paymentDataHeading)) {
            if (paymentDataHidden) {
                itemList.addAll(position + 1, paymentDataList);
            }
            else {
                for (int i = 0; i < 3; i++) {
                    itemList.remove(position + 1);
                }
            }
            paymentDataHidden = !paymentDataHidden;
        }
        else if (currentItem.equals(transportationDataHeading)) {
            if (transportationDataHidden) {
                itemList.addAll(position + 1, transportationDataList);
            }
            else {
                for (int i = 0; i < 5; i++) {
                    itemList.remove(position + 1);
                }
            }
            transportationDataHidden = !transportationDataHidden;
        }
//        else if (currentItem.equals(forEachCargoHeading)) {
//            if (!consignmentListSet) {
//                return;
//            }
//
//            if (consignmentQuantity > 0) {
//                if (forEachCargoHidden) {
//                    itemList.addAll(position + 1, forEachCargoList);
//                }
//                else {
//                    for (int i = 0; i < (7 * consignmentQuantity); i++) {
//                        itemList.remove(position + 1);
//                    }
//                }
//                forEachCargoHidden = !forEachCargoHidden;
//            }
//        }
        adapter.notifyDataSetChanged();
    }

    private static final String TAG = InvoiceFragment.class.toString();
}