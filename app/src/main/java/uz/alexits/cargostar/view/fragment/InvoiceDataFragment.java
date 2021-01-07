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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import uz.alexits.cargostar.R;

import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.transportation.Consignment;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.view.UiUtils;
import uz.alexits.cargostar.viewmodel.CourierViewModel;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.adapter.InvoiceData;
import uz.alexits.cargostar.view.adapter.InvoiceDataAdapter;
import uz.alexits.cargostar.view.callback.InvoiceDataCallback;
import uz.alexits.cargostar.viewmodel.RequestsViewModel;
import uz.alexits.cargostar.workers.SyncWorkRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class InvoiceDataFragment extends Fragment implements InvoiceDataCallback {
    private Context context;
    private FragmentActivity activity;
    private List<InvoiceData> itemList;

    private boolean publicDataHidden = false;
    private InvoiceData publicDataHeading;
    private final List<InvoiceData> publicDataList = new ArrayList<>();

    private boolean senderDataHidden = false;
    private InvoiceData senderDataHeading;
    private final List<InvoiceData> senderDataList = new ArrayList<>();

    private boolean recipientDataHidden = false;
    private InvoiceData recipientDataHeading;
    private final List<InvoiceData> recipientDataList = new ArrayList<>();

    private boolean payerDataHidden = false;
    private InvoiceData payerDataHeading;
    private final List<InvoiceData> payerDataList = new ArrayList<>();

    private boolean accountDataHidden = false;
    private InvoiceData accountDataHeading;
    private final List<InvoiceData> accountDataList = new ArrayList<>();

    private boolean paymentDataHidden = false;
    private InvoiceData paymentDataHeading;
    private final List<InvoiceData> paymentDataList = new ArrayList<>();

    private boolean transportationDataHidden = false;
    private InvoiceData transportationDataHeading;
    private final List<InvoiceData> transportationDataList = new ArrayList<>();

    private InvoiceData forEachCargoHeading;
    private final List<InvoiceData> forEachCargoList = new ArrayList<>();

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
    //main content views
    private RecyclerView dataRecyclerView;
    private InvoiceDataAdapter adapter;
    private ImageView editInvoiceImageView;
    private ProgressBar progressBar;

    private static boolean isPublic = true;

    private static volatile long requestId = -1L;
    private static volatile long invoiceId = -1L;
    private static volatile long providerId = -1L;
    private static volatile long invoiceCourierId = -1L;
    private static volatile long tariffId = -1L;
    private static volatile int deliveryType = 0;
    private static volatile String invoiceNumber = null;

    private static volatile String comment = null;
    private static volatile int consignmentQuantity = 0;
    private static volatile double price = 0;

    private static volatile long senderId = -1L;
    private static volatile String senderEmail = null;
    private static volatile String senderSignature = null;
    private static volatile String senderFirstName = null;
    private static volatile String senderLastName = null;
    private static volatile String senderMiddleName = null;
    private static volatile String senderPhone = null;
    private static volatile String senderAddress = null;
    private static volatile long senderCountryId = -1L;
    private static volatile long senderRegionId = -1L;
    private static volatile long senderCityId = -1L;
    private static volatile String senderZip = null;
    private static volatile String senderCompany = null;
    private static volatile String senderCargo = null;
    private static volatile String senderTnt = null;
    private static volatile String senderFedex = null;
    private static volatile int discount = 0;

    private static volatile long recipientId = -1L;
    private static volatile String recipientEmail = null;
    private static volatile String recipientFirstName = null;
    private static volatile String recipientLastName = null;
    private static volatile String recipientMiddleName = null;
    private static volatile String recipientPhone = null;
    private static volatile String recipientAddress = null;
    private static volatile long recipientCountryId = -1L;
    private static volatile long recipientRegionId = -1L;
    private static volatile long recipientCityId = -1L;
    private static volatile String recipientZip = null;
    private static volatile String recipientCompany = null;
    private static volatile String recipientCargo = null;
    private static volatile String recipientTnt = null;
    private static volatile String recipientFedex = null;

    private static volatile long payerId = -1L;
    private static volatile String payerEmail = null;
    private static volatile String payerFirstName = null;
    private static volatile String payerLastName = null;
    private static volatile String payerMiddleName = null;
    private static volatile String payerPhone = null;
    private static volatile String payerAddress = null;
    private static volatile long payerCountryId = -1L;
    private static volatile long payerRegionId = -1L;
    private static volatile long payerCityId = -1L;
    private static volatile String payerZip = null;
    private static volatile String payerCompany = null;
    private static volatile String payerCargo = null;
    private static volatile String payerTnt = null;
    private static volatile String payerFedex = null;
    private static volatile String payerInn = null;
    private static volatile String payerBank = null;
    private static volatile String payerCheckingAccount = null;
    private static volatile String payerRegistrationCode = null;
    private static volatile String payerMfo = null;
    private static volatile String payerOked = null;

    private static volatile String serializedConsignmentList = null;

    private static volatile UUID fetchInvoiceRequestUUID;

    public InvoiceDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        context = getContext();
        itemList = new ArrayList<>();

        if (getArguments() != null) {
            //parcelId can be requestId
            requestId = InvoiceDataFragmentArgs.fromBundle(getArguments()).getRequestId();
            isPublic = InvoiceDataFragmentArgs.fromBundle(getArguments()).getIsPublic();
            invoiceId = InvoiceDataFragmentArgs.fromBundle(getArguments()).getInvoiceId();
            invoiceCourierId = InvoiceDataFragmentArgs.fromBundle(getArguments()).getCourierId();
            providerId = InvoiceDataFragmentArgs.fromBundle(getArguments()).getProviderId();

            senderId = InvoiceDataFragmentArgs.fromBundle(getArguments()).getClientId();

            senderEmail = InvoiceDataFragmentArgs.fromBundle(getArguments()).getSenderEmail();
            senderFirstName = InvoiceDataFragmentArgs.fromBundle(getArguments()).getSenderFirstName();
            senderLastName = InvoiceDataFragmentArgs.fromBundle(getArguments()).getSenderLastName();
            senderMiddleName = InvoiceDataFragmentArgs.fromBundle(getArguments()).getSenderMiddleName();
            senderPhone = InvoiceDataFragmentArgs.fromBundle(getArguments()).getSenderPhone();
            senderAddress = InvoiceDataFragmentArgs.fromBundle(getArguments()).getSenderAddress();
            senderCountryId = InvoiceDataFragmentArgs.fromBundle(getArguments()).getSenderCountryId();
            senderRegionId = InvoiceDataFragmentArgs.fromBundle(getArguments()).getSenderRegionId();
            senderCityId = InvoiceDataFragmentArgs.fromBundle(getArguments()).getSenderCityId();

            recipientCountryId = InvoiceDataFragmentArgs.fromBundle(getArguments()).getRecipientCountryId();
            recipientCityId = InvoiceDataFragmentArgs.fromBundle(getArguments()).getRecipientCityId();

            deliveryType = InvoiceDataFragmentArgs.fromBundle(getArguments()).getDeliveryType();
            comment = InvoiceDataFragmentArgs.fromBundle(getArguments()).getComment();
            consignmentQuantity = InvoiceDataFragmentArgs.fromBundle(getArguments()).getConsignmentQuantity();

            if (invoiceId > 0 && senderId > 0) {
                fetchInvoiceRequestUUID = SyncWorkRequest.fetchInvoiceData(context, requestId, invoiceId, senderId, consignmentQuantity);
            }
            else if (senderId > 0) {
                fetchInvoiceRequestUUID = SyncWorkRequest.fetchSenderData(context, requestId, senderId, consignmentQuantity);
            }
            else {
                fetchInvoiceRequestUUID = SyncWorkRequest.fetchRequestData(context, requestId, consignmentQuantity);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_invoice_data, container, false);
        //header views
        fullNameTextView = activity.findViewById(R.id.full_name_text_view);
        branchTextView = activity.findViewById(R.id.branch_text_view);
        courierIdTextView = activity.findViewById(R.id.courier_id_text_view);
        requestSearchEditText = activity.findViewById(R.id.search_edit_text);
        requestSearchImageView = activity.findViewById(R.id.search_btn);
        profileImageView = activity.findViewById(R.id.profile_image_view);
        editImageView = activity.findViewById(R.id.edit_image_view);
        createUserImageView = activity.findViewById(R.id.create_user_image_view);
        calculatorImageView = activity.findViewById(R.id.calculator_image_view);
        notificationsImageView = activity.findViewById(R.id.notifications_image_view);
        badgeCounterTextView = activity.findViewById(R.id.badge_counter_text_view);
        //main content views
        dataRecyclerView = root.findViewById(R.id.data_recycler_view);
        adapter = new InvoiceDataAdapter(context, this);
        dataRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        dataRecyclerView.setAdapter(adapter);
        editInvoiceImageView = root.findViewById(R.id.edit_parcel_image_view);
        progressBar = root.findViewById(R.id.progress_bar);

        initItemList();

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

        notificationsImageView.setOnClickListener(v -> {
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(R.id.notificationsFragment);
        });

        calculatorImageView.setOnClickListener(v -> {
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(R.id.calculatorFragment);
        });

        editImageView.setOnClickListener(v -> {
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(R.id.profileFragment);
        });

        if (!isPublic) {
            editInvoiceImageView.setVisibility(View.VISIBLE);
        }

        editInvoiceImageView.setOnClickListener(v -> {
            final InvoiceDataFragmentDirections.ActionInvoiceDataFragmentToCreateInvoiceFragment action = InvoiceDataFragmentDirections.actionInvoiceDataFragmentToCreateInvoiceFragment();
            action.setRequestKey(IntentConstants.REQUEST_EDIT_INVOICE);
            action.setRequestId(requestId);
            action.setInvoiceId(invoiceId);
            action.setInvoiceNumber(invoiceNumber);
            action.setTariffId(tariffId);
            action.setCourierId(invoiceCourierId);
            action.setProviderId(providerId);
            action.setPrice((float) price);
            action.setDeliveryType(deliveryType);
            action.setComment(comment);
            action.setConsignmentQuantity(consignmentQuantity);

            action.setSenderId(senderId);
            action.setSenderEmail(senderEmail);
            action.setSenderSignature(senderSignature);
            action.setSenderFirstName(senderFirstName);
            action.setSenderLastName(senderLastName);
            action.setSenderMiddleName(senderMiddleName);
            action.setSenderPhone(senderPhone);
            action.setSenderAddress(senderAddress);
            action.setSenderCountryId(senderCountryId);
            action.setSenderRegionId(senderRegionId);
            action.setSenderCityId(senderCityId);
            action.setSenderZip(senderZip);
            action.setSenderCompany(senderCompany);
            action.setSenderCargo(senderCargo);
            action.setSenderTnt(senderTnt);
            action.setSenderFedex(senderFedex);
            action.setDiscount(discount);

            action.setRecipientId(recipientId);
            action.setRecipientEmail(recipientEmail);
            action.setRecipientFirstName(recipientFirstName);
            action.setRecipientLastName(recipientLastName);
            action.setRecipientMiddleName(recipientMiddleName);
            action.setRecipientPhone(recipientPhone);
            action.setRecipientAddress(recipientAddress);
            action.setRecipientCountryId(recipientCountryId);
            action.setRecipientRegionId(recipientRegionId);
            action.setRecipientCityId(recipientCityId);
            action.setRecipientZip(recipientZip);
            action.setRecipientCompany(recipientCompany);
            action.setRecipientCargo(recipientCargo);
            action.setRecipientTnt(recipientTnt);
            action.setRecipientFedex(recipientFedex);

            action.setPayerId(payerId);
            action.setPayerEmail(payerEmail);
            action.setPayerFirstName(payerFirstName);
            action.setPayerLastName(payerLastName);
            action.setPayerMiddleName(payerMiddleName);
            action.setPayerPhone(payerPhone);
            action.setPayerAddress(payerAddress);
            action.setPayerCountryId(payerCountryId);
            action.setPayerRegionId(payerRegionId);
            action.setPayerCityId(payerCityId);
            action.setPayerZip(payerZip);
            action.setPayerCompany(payerCompany);
            action.setPayerCargo(payerCargo);
            action.setPayerTnt(payerTnt);
            action.setPayerFedex(payerFedex);
            action.setPayerInn(payerInn);
            action.setPayerBank(payerBank);
            action.setPayerCheckingAccount(payerCheckingAccount);
            action.setPayerRegistrationCode(payerRegistrationCode);
            action.setPayerMfo(payerMfo);
            action.setPayerOked(payerOked);

            action.setSerializedConsignmentList(serializedConsignmentList);

            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(action);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final CourierViewModel courierViewModel = new ViewModelProvider(this).get(CourierViewModel.class);
        final RequestsViewModel requestsViewModel = new ViewModelProvider(this).get(RequestsViewModel.class);

        requestsViewModel.setRequestId(requestId);
        requestsViewModel.setProviderId(providerId);
        requestsViewModel.setSenderId(senderId);
        requestsViewModel.setInvoiceId(invoiceId);
        requestsViewModel.setSenderCountryId(senderCountryId);
        requestsViewModel.setSenderRegionId(senderRegionId);
        requestsViewModel.setSenderCityId(senderCityId);
        requestsViewModel.setRecipientCountryId(recipientCountryId);
        requestsViewModel.setRecipientCityId(recipientCityId);

        itemList.set(1, new InvoiceData(getString(R.string.invoice_id), invoiceId > 0 ? String.valueOf(invoiceId) : null, InvoiceData.TYPE_ITEM));
        itemList.set(2, new InvoiceData(getString(R.string.courier_id), invoiceCourierId > 0 ? String.valueOf(invoiceCourierId) : null, InvoiceData.TYPE_ITEM));
        adapter.notifyItemRangeChanged(1, 2);

        publicDataList.set(0, new InvoiceData(getString(R.string.invoice_id), invoiceId > 0 ? String.valueOf(invoiceId) : null, InvoiceData.TYPE_ITEM));
        publicDataList.set(1, new InvoiceData(getString(R.string.courier_id), invoiceCourierId > 0 ? String.valueOf(invoiceCourierId) : null, InvoiceData.TYPE_ITEM));

        itemList.set(68, new InvoiceData(getString(R.string.destination_quantity), String.valueOf(consignmentQuantity), InvoiceData.TYPE_ITEM));
        adapter.notifyItemChanged(68);

        transportationDataList.set(2, new InvoiceData(getString(R.string.destination_quantity), String.valueOf(consignmentQuantity), InvoiceData.TYPE_ITEM));


        //header views
        courierViewModel.selectCourierByLogin(SharedPrefs.getInstance(context).getString(Constants.KEY_LOGIN)).observe(getViewLifecycleOwner(), courier -> {
            if (courier != null) {
                fullNameTextView.setText(getString(R.string.header_courier_full_name, courier.getFirstName(), courier.getLastName()));
                courierIdTextView.setText(getString(R.string.courier_id_placeholder, courier.getId()));
            }
        });

        courierViewModel.selectBrancheById(SharedPrefs.getInstance(context).getLong(SharedPrefs.BRANCH_ID)).observe(getViewLifecycleOwner(), branch -> {
            if (branch != null) {
                branchTextView.setText(getString(R.string.header_branch_name, branch.getName()));
            }
        });

        courierViewModel.selectNewNotificationsCount().observe(getViewLifecycleOwner(), newNotificationsCount -> {
            if (newNotificationsCount != null) {
                badgeCounterTextView.setText(String.valueOf(newNotificationsCount));
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

                        requestId = outputData.getLong(Constants.KEY_REQUEST_ID, -1L);
                        invoiceId = outputData.getLong(Constants.KEY_INVOICE_ID, -1L);
                        final long courierId = outputData.getLong(Constants.KEY_COURIER_ID, -1L);
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
                        mainIntent.putExtra(Constants.KEY_COURIER_ID, courierId);
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

        /* public & payment data */
        requestsViewModel.getProvider().observe(getViewLifecycleOwner(), provider -> {
            if (provider != null) {
                itemList.set(3, new InvoiceData(getString(R.string.service_provider), provider.getNameEn(), InvoiceData.TYPE_ITEM));
                itemList.set(61, new InvoiceData(getString(R.string.fuel_tax), String.valueOf(provider.getFuel()), InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(3);

                publicDataList.set(2, new InvoiceData(getString(R.string.service_provider), null, InvoiceData.TYPE_ITEM));
                paymentDataList.set(1, new InvoiceData(getString(R.string.fuel_tax), String.valueOf(provider.getFuel()), InvoiceData.TYPE_ITEM));
            }
        });

        /* payment data */
        requestsViewModel.getTariff().observe(getViewLifecycleOwner(), tariff -> {
            if (tariff != null) {
                itemList.set(63, new InvoiceData(getString(R.string.tariff), tariff.getName(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(3);

                paymentDataList.set(3, new InvoiceData(getString(R.string.tariff), tariff.getName(), InvoiceData.TYPE_ITEM));
            }
        });

        /* sender data */
        itemList.set(6, new InvoiceData(getString(R.string.email), senderEmail, InvoiceData.TYPE_ITEM));
        itemList.set(7, new InvoiceData(getString(R.string.first_name), senderFirstName, InvoiceData.TYPE_ITEM));
        itemList.set(8, new InvoiceData(getString(R.string.middle_name), senderMiddleName, InvoiceData.TYPE_ITEM));
        itemList.set(9, new InvoiceData(getString(R.string.last_name), senderLastName, InvoiceData.TYPE_ITEM));
        itemList.set(10, new InvoiceData(getString(R.string.phone_number), senderPhone, InvoiceData.TYPE_ITEM));
        adapter.notifyItemRangeChanged(6, 5);

        itemList.set(14, new InvoiceData(getString(R.string.take_address), senderAddress, InvoiceData.TYPE_ITEM));

        senderDataList.set(0, new InvoiceData(getString(R.string.email), senderEmail, InvoiceData.TYPE_ITEM));
        senderDataList.set(1, new InvoiceData(getString(R.string.first_name), senderFirstName, InvoiceData.TYPE_ITEM));
        senderDataList.set(2, new InvoiceData(getString(R.string.middle_name), senderMiddleName, InvoiceData.TYPE_ITEM));
        senderDataList.set(3, new InvoiceData(getString(R.string.last_name), senderLastName, InvoiceData.TYPE_ITEM));
        senderDataList.set(4, new InvoiceData(getString(R.string.phone_number), senderPhone, InvoiceData.TYPE_ITEM));
        senderDataList.set(8, new InvoiceData(getString(R.string.take_address), senderAddress, InvoiceData.TYPE_ITEM));

        requestsViewModel.getSender().observe(getViewLifecycleOwner(), sender -> {
            if (sender != null) {
                requestsViewModel.setSenderCountryId(sender.getCountryId());
                requestsViewModel.setSenderRegionId(sender.getRegionId());
                requestsViewModel.setSenderCityId(sender.getCityId());

                itemList.set(6, new InvoiceData(getString(R.string.email), sender.getEmail(), InvoiceData.TYPE_ITEM));
                itemList.set(7, new InvoiceData(getString(R.string.first_name), sender.getFirstName(), InvoiceData.TYPE_ITEM));
                itemList.set(8, new InvoiceData(getString(R.string.middle_name), sender.getMiddleName(), InvoiceData.TYPE_ITEM));
                itemList.set(9, new InvoiceData(getString(R.string.last_name), sender.getLastName(), InvoiceData.TYPE_ITEM));
                itemList.set(10, new InvoiceData(getString(R.string.phone_number), sender.getPhone(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemRangeChanged(6, 5);

                itemList.set(14, new InvoiceData(getString(R.string.take_address), sender.getAddress(), InvoiceData.TYPE_ITEM));
                itemList.set(15, new InvoiceData(getString(R.string.post_index), sender.getZip(), InvoiceData.TYPE_ITEM));
                itemList.set(16, new InvoiceData(getString(R.string.cargostar_account_number), sender.getCargostarAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(17, new InvoiceData(getString(R.string.tnt_account_number), sender.getTntAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(18, new InvoiceData(getString(R.string.fedex_account_number), sender.getFedexAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(19, new InvoiceData(getString(R.string.sender_signature), null, InvoiceData.TYPE_ITEM));
                adapter.notifyItemRangeChanged(14, 6);

                senderDataList.set(0, new InvoiceData(getString(R.string.email), sender.getEmail(), InvoiceData.TYPE_ITEM));
                senderDataList.set(1, new InvoiceData(getString(R.string.first_name), sender.getFirstName(), InvoiceData.TYPE_ITEM));
                senderDataList.set(2, new InvoiceData(getString(R.string.middle_name), sender.getMiddleName(), InvoiceData.TYPE_ITEM));
                senderDataList.set(3, new InvoiceData(getString(R.string.last_name), sender.getLastName(), InvoiceData.TYPE_ITEM));
                senderDataList.set(4, new InvoiceData(getString(R.string.phone_number), sender.getPhone(), InvoiceData.TYPE_ITEM));
                senderDataList.set(8, new InvoiceData(getString(R.string.take_address), sender.getAddress(), InvoiceData.TYPE_ITEM));
                senderDataList.set(9, new InvoiceData(getString(R.string.post_index), sender.getZip(), InvoiceData.TYPE_ITEM));
                senderDataList.set(10, new InvoiceData(getString(R.string.cargostar_account_number), sender.getCargostarAccountNumber(), InvoiceData.TYPE_ITEM));
                senderDataList.set(11, new InvoiceData(getString(R.string.tnt_account_number), sender.getTntAccountNumber(), InvoiceData.TYPE_ITEM));
                senderDataList.set(12, new InvoiceData(getString(R.string.fedex_account_number), sender.getFedexAccountNumber(), InvoiceData.TYPE_ITEM));
                senderDataList.set(13, new InvoiceData(getString(R.string.sender_signature), null, InvoiceData.TYPE_ITEM));

            }
        });

        requestsViewModel.getSenderCountry().observe(getViewLifecycleOwner(), country -> {
            if (country != null) {
                itemList.set(11, new InvoiceData(getString(R.string.country), country.getNameEn(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(11);

                senderDataList.set(5, new InvoiceData(getString(R.string.country), country.getNameEn(), InvoiceData.TYPE_ITEM));
            }
        });

        requestsViewModel.getSenderRegion().observe(getViewLifecycleOwner(), region -> {
            if (region != null) {
                itemList.set(12, new InvoiceData(getString(R.string.region), region.getNameEn(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(12);

                senderDataList.set(6, new InvoiceData(getString(R.string.region), region.getNameEn(), InvoiceData.TYPE_ITEM));
            }
        });

        requestsViewModel.getSenderCity().observe(getViewLifecycleOwner(), city -> {
            if (city != null) {
                itemList.set(13, new InvoiceData(getString(R.string.city), city.getNameEn(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(13);

                senderDataList.set(7, new InvoiceData(getString(R.string.city), city.getNameEn(), InvoiceData.TYPE_ITEM));
            }
        });

        /* recipient data */
        requestsViewModel.getRecipient().observe(getViewLifecycleOwner(), recipient -> {
            if (recipient != null) {
                requestsViewModel.setRecipientCountryId(recipient.getCountryId());
                requestsViewModel.setRecipientRegionId(recipient.getRegionId());
                requestsViewModel.setRecipientCityId(recipient.getCityId());

                itemList.set(22, new InvoiceData(getString(R.string.email), recipient.getEmail(), InvoiceData.TYPE_ITEM));
                itemList.set(23, new InvoiceData(getString(R.string.first_name), recipient.getFirstName(), InvoiceData.TYPE_ITEM));
                itemList.set(24, new InvoiceData(getString(R.string.middle_name), recipient.getMiddleName(), InvoiceData.TYPE_ITEM));
                itemList.set(25, new InvoiceData(getString(R.string.last_name), recipient.getLastName(), InvoiceData.TYPE_ITEM));
                itemList.set(26, new InvoiceData(getString(R.string.phone_number), recipient.getPhone(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemRangeChanged(22, 5);

                itemList.set(30, new InvoiceData(getString(R.string.delivery_address), recipient.getAddress(), InvoiceData.TYPE_ITEM));
                itemList.set(31, new InvoiceData(getString(R.string.post_index), recipient.getZip(), InvoiceData.TYPE_ITEM));
                itemList.set(32, new InvoiceData(getString(R.string.cargostar_account_number), recipient.getCargostarAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(33, new InvoiceData(getString(R.string.tnt_account_number), recipient.getTntAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(34, new InvoiceData(getString(R.string.fedex_account_number), recipient.getFedexAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(35, new InvoiceData(getString(R.string.receiver_signature), null, InvoiceData.TYPE_ITEM));
                adapter.notifyItemRangeChanged(30, 6);

                recipientDataList.set(0, new InvoiceData(getString(R.string.email), recipient.getEmail(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(1, new InvoiceData(getString(R.string.first_name), recipient.getFirstName(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(2, new InvoiceData(getString(R.string.middle_name),  recipient.getMiddleName(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(3, new InvoiceData(getString(R.string.last_name), recipient.getLastName(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(4, new InvoiceData(getString(R.string.phone_number), recipient.getPhone(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(8, new InvoiceData(getString(R.string.delivery_address), recipient.getAddress(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(9, new InvoiceData(getString(R.string.post_index), recipient.getZip(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(10, new InvoiceData(getString(R.string.cargostar_account_number), recipient.getCargostarAccountNumber(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(11, new InvoiceData(getString(R.string.tnt_account_number), recipient.getTntAccountNumber(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(12, new InvoiceData(getString(R.string.fedex_account_number), recipient.getFedexAccountNumber(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(13, new InvoiceData(getString(R.string.receiver_signature), null, InvoiceData.TYPE_ITEM));
            }
        });

        requestsViewModel.getRecipientCountry().observe(getViewLifecycleOwner(), country -> {
            if (country != null) {
                itemList.set(27, new InvoiceData(getString(R.string.country), country.getNameEn(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(27);

                recipientDataList.set(5, new InvoiceData(getString(R.string.country), country.getNameEn(), InvoiceData.TYPE_ITEM));
            }
        });

        requestsViewModel.getRecipientRegion().observe(getViewLifecycleOwner(), region -> {
            if (region != null) {
                itemList.set(28, new InvoiceData(getString(R.string.region), region.getNameEn(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(28);

                recipientDataList.set(6, new InvoiceData(getString(R.string.region), region.getNameEn(), InvoiceData.TYPE_ITEM));
            }

        });

        requestsViewModel.getRecipientCity().observe(getViewLifecycleOwner(), city -> {
            if (city != null) {
                itemList.set(29, new InvoiceData(getString(R.string.city), city.getNameEn(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(29);

                recipientDataList.set(7, new InvoiceData(getString(R.string.city), city.getNameEn(), InvoiceData.TYPE_ITEM));
            }
        });

        /* payer data */
        requestsViewModel.getPayer().observe(getViewLifecycleOwner(), payer -> {
            if (payer != null) {
                requestsViewModel.setPayerCountryId(payer.getCountryId());
                requestsViewModel.setPayerRegionId(payer.getRegionId());
                requestsViewModel.setPayerCityId(payer.getCityId());

                itemList.set(38, new InvoiceData(getString(R.string.email), payer.getEmail(), InvoiceData.TYPE_ITEM));
                itemList.set(39, new InvoiceData(getString(R.string.first_name), payer.getFirstName(), InvoiceData.TYPE_ITEM));
                itemList.set(40, new InvoiceData(getString(R.string.middle_name), payer.getMiddleName(), InvoiceData.TYPE_ITEM));
                itemList.set(41, new InvoiceData(getString(R.string.last_name), payer.getLastName(), InvoiceData.TYPE_ITEM));
                itemList.set(42, new InvoiceData(getString(R.string.phone_number), payer.getPhone(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemRangeChanged(38, 5);

                itemList.set(46, new InvoiceData(getString(R.string.address), payer.getAddress(), InvoiceData.TYPE_ITEM));
                itemList.set(47, new InvoiceData(getString(R.string.post_index), payer.getZip(), InvoiceData.TYPE_ITEM));
                itemList.set(48, new InvoiceData(getString(R.string.cargostar_account_number), payer.getCargostarAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(49, new InvoiceData(getString(R.string.tnt_account_number), payer.getTntAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(50, new InvoiceData(getString(R.string.fedex_account_number), payer.getFedexAccountNumber(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemRangeChanged(46, 5);

                payerDataList.set(0, new InvoiceData(getString(R.string.email), payer.getEmail(), InvoiceData.TYPE_ITEM));
                payerDataList.set(1, new InvoiceData(getString(R.string.first_name), payer.getFirstName(), InvoiceData.TYPE_ITEM));
                payerDataList.set(2, new InvoiceData(getString(R.string.middle_name), payer.getMiddleName(), InvoiceData.TYPE_ITEM));
                payerDataList.set(3, new InvoiceData(getString(R.string.last_name), payer.getLastName(), InvoiceData.TYPE_ITEM));
                payerDataList.set(4, new InvoiceData(getString(R.string.phone_number), payer.getPhone(), InvoiceData.TYPE_ITEM));
                payerDataList.set(8, new InvoiceData(getString(R.string.address), payer.getAddress(), InvoiceData.TYPE_ITEM));
                payerDataList.set(9, new InvoiceData(getString(R.string.post_index), payer.getZip(), InvoiceData.TYPE_ITEM));
                payerDataList.set(10, new InvoiceData(getString(R.string.cargostar_account_number), payer.getCargostarAccountNumber(), InvoiceData.TYPE_ITEM));
                payerDataList.set(11, new InvoiceData(getString(R.string.tnt_account_number), payer.getTntAccountNumber(), InvoiceData.TYPE_ITEM));
                payerDataList.set(12, new InvoiceData(getString(R.string.fedex_account_number), payer.getFedexAccountNumber(), InvoiceData.TYPE_ITEM));

                //account data
                itemList.set(53, new InvoiceData(getString(R.string.checking_account), payer.getCheckingAccount(), InvoiceData.TYPE_ITEM));
                itemList.set(54, new InvoiceData(getString(R.string.bank), payer.getBank(), InvoiceData.TYPE_ITEM));
                itemList.set(55, new InvoiceData(getString(R.string.payer_registration_code), payer.getRegistrationCode(), InvoiceData.TYPE_ITEM));
                itemList.set(56, new InvoiceData(getString(R.string.mfo), payer.getMfo(), InvoiceData.TYPE_ITEM));
                itemList.set(57, new InvoiceData(getString(R.string.oked), payer.getOked(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemRangeChanged(53, 5);

                accountDataList.set(0, new InvoiceData(getString(R.string.checking_account), payer.getCheckingAccount(), InvoiceData.TYPE_ITEM));
                accountDataList.set(1, new InvoiceData(getString(R.string.bank), payer.getBank(), InvoiceData.TYPE_ITEM));
                accountDataList.set(2, new InvoiceData(getString(R.string.payer_registration_code), payer.getRegistrationCode(), InvoiceData.TYPE_ITEM));
                accountDataList.set(3, new InvoiceData(getString(R.string.mfo), payer.getMfo(), InvoiceData.TYPE_ITEM));
                accountDataList.set(4, new InvoiceData(getString(R.string.oked), payer.getOked(), InvoiceData.TYPE_ITEM));
            }
        });

        requestsViewModel.getPayerCountry().observe(getViewLifecycleOwner(), country -> {
            if (country != null) {
                itemList.set(43, new InvoiceData(getString(R.string.country), country.getNameEn(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(43);

                payerDataList.set(5, new InvoiceData(getString(R.string.country), country.getNameEn(), InvoiceData.TYPE_ITEM));
            }
        });

        requestsViewModel.getPayerRegion().observe(getViewLifecycleOwner(), region -> {
            if (region != null) {
                itemList.set(44, new InvoiceData(getString(R.string.region), region.getNameEn(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(44);

                payerDataList.set(6, new InvoiceData(getString(R.string.region), region.getNameEn(), InvoiceData.TYPE_ITEM));
            }
        });

        requestsViewModel.getPayerCity().observe(getViewLifecycleOwner(), city -> {
            if (city != null) {
                itemList.set(45, new InvoiceData(getString(R.string.city), city.getNameEn(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(45);

                payerDataList.set(7, new InvoiceData(getString(R.string.city), city.getNameEn(), InvoiceData.TYPE_ITEM));
            }
        });

        /* invoice data */
        requestsViewModel.getInvoice().observe(getViewLifecycleOwner(), invoice -> {
            if (invoice != null) {
                requestsViewModel.setRecipientId(invoice.getRecipientId());
                requestsViewModel.setPayerId(invoice.getPayerId());
                requestsViewModel.setTariffId(invoice.getTariffId());

                itemList.set(60, new InvoiceData(getString(R.string.cost), String.valueOf(invoice.getPrice()), InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(60);

                paymentDataList.set(0, new InvoiceData(getString(R.string.cost), String.valueOf(invoice.getPrice()), InvoiceData.TYPE_ITEM));
            }
        });

        /* transportation data */
        requestsViewModel.getTransportation().observe(getViewLifecycleOwner(), transportation -> {
            if (transportation != null) {
                itemList.set(66, new InvoiceData(getString(R.string.tracking_code_main), transportation.getTrackingCode(), InvoiceData.TYPE_ITEM));
                itemList.set(67, new InvoiceData(getString(R.string.qr_code), transportation.getQrCode(), InvoiceData.TYPE_ITEM));
                itemList.set(69, new InvoiceData(getString(R.string.arrival_date), transportation.getArrivalDate(), InvoiceData.TYPE_ITEM));
                itemList.set(70, new InvoiceData(getString(R.string.courier_guidelines), transportation.getInstructions(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemRangeChanged(66, 5);

                transportationDataList.set(0, new InvoiceData(getString(R.string.tracking_code_main), transportation.getTrackingCode(), InvoiceData.TYPE_ITEM));
                transportationDataList.set(1, new InvoiceData(getString(R.string.qr_code), transportation.getQrCode(), InvoiceData.TYPE_ITEM));
                transportationDataList.set(3, new InvoiceData(getString(R.string.arrival_date), transportation.getArrivalDate(), InvoiceData.TYPE_ITEM));
                transportationDataList.set(4, new InvoiceData(getString(R.string.courier_guidelines), transportation.getInstructions(), InvoiceData.TYPE_ITEM));
            }
        });

        /* consignment data */
        requestsViewModel.getConsignmentList().observe(getViewLifecycleOwner(), consignmentList -> {
            //for each cargo
            if (consignmentList != null && !consignmentList.isEmpty()) {
                int i = 0;
                int j = 73;

                if (consignmentQuantity > 0) {
                    for (final Consignment consignment : consignmentList) {
                        consignment.setPackagingType(consignment.getPackagingId() != null ? String.valueOf(consignment.getPackagingId()) : null);
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
                        adapter.notifyItemRangeChanged(72, forEachCargoList.size());
                    });
                }
            }
        });

        if (fetchInvoiceRequestUUID != null) {
            WorkManager.getInstance(context).getWorkInfoByIdLiveData(fetchInvoiceRequestUUID).observe(getViewLifecycleOwner(), workInfo -> {
                if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                    final Data outputData = workInfo.getOutputData();

                    requestId = outputData.getLong(Constants.KEY_REQUEST_ID, -1L);
                    invoiceId = outputData.getLong(Constants.KEY_INVOICE_ID, -1L);
                    invoiceNumber = outputData.getString(Constants.KEY_NUMBER);
                    providerId = outputData.getLong(Constants.KEY_PROVIDER_ID, -1L);
                    tariffId = outputData.getLong(Constants.KEY_TARIFF_ID, -1L);
                    senderId = outputData.getLong(Constants.KEY_SENDER_ID, -1L);
                    recipientId = outputData.getLong(Constants.KEY_RECIPIENT_ID, -1L);
                    payerId = outputData.getLong(Constants.KEY_PAYER_ID, -1L);
                    price = outputData.getDouble(Constants.KEY_PRICE, -1);
                    invoiceCourierId = outputData.getLong(Constants.KEY_COURIER_ID, -1L);

                    senderEmail = outputData.getString(Constants.KEY_SENDER_EMAIL);
                    senderSignature = outputData.getString(Constants.KEY_SENDER_SIGNATURE);
                    senderFirstName = outputData.getString(Constants.KEY_SENDER_FIRST_NAME);
                    senderLastName = outputData.getString(Constants.KEY_SENDER_LAST_NAME);
                    senderMiddleName = outputData.getString(Constants.KEY_SENDER_MIDDLE_NAME);
                    senderPhone = outputData.getString(Constants.KEY_SENDER_PHONE);
                    senderAddress = outputData.getString(Constants.KEY_SENDER_ADDRESS);
                    senderCountryId = outputData.getLong(Constants.KEY_SENDER_COUNTRY_ID, -1L);
                    senderRegionId = outputData.getLong(Constants.KEY_SENDER_REGION_ID, -1L);
                    senderCityId = outputData.getLong(Constants.KEY_SENDER_CITY_ID, -1L);
                    senderZip = outputData.getString(Constants.KEY_SENDER_ZIP);
                    senderCompany = outputData.getString(Constants.KEY_SENDER_COMPANY_NAME);
                    senderCargo = outputData.getString(Constants.KEY_SENDER_CARGOSTAR);
                    senderTnt = outputData.getString(Constants.KEY_SENDER_TNT);
                    senderFedex = outputData.getString(Constants.KEY_SENDER_FEDEX);
                    discount = outputData.getInt(Constants.KEY_DISCOUNT, 0);

                    recipientEmail = outputData.getString(Constants.KEY_RECIPIENT_EMAIL);
                    recipientFirstName = outputData.getString(Constants.KEY_RECIPIENT_FIRST_NAME);
                    recipientLastName = outputData.getString(Constants.KEY_RECIPIENT_LAST_NAME);
                    recipientMiddleName = outputData.getString(Constants.KEY_RECIPIENT_MIDDLE_NAME);
                    recipientPhone = outputData.getString(Constants.KEY_RECIPIENT_PHONE);
                    recipientAddress = outputData.getString(Constants.KEY_RECIPIENT_ADDRESS);
                    recipientCountryId = outputData.getLong(Constants.KEY_RECIPIENT_COUNTRY_ID, -1L);
                    recipientRegionId = outputData.getLong(Constants.KEY_RECIPIENT_REGION_ID, -1L);
                    recipientCityId = outputData.getLong(Constants.KEY_RECIPIENT_CITY_ID, -1L);
                    recipientZip = outputData.getString(Constants.KEY_RECIPIENT_ZIP);
                    recipientCompany = outputData.getString(Constants.KEY_RECIPIENT_COMPANY_NAME);
                    recipientCargo = outputData.getString(Constants.KEY_RECIPIENT_CARGOSTAR);
                    recipientTnt = outputData.getString(Constants.KEY_RECIPIENT_TNT);
                    recipientFedex = outputData.getString(Constants.KEY_RECIPIENT_FEDEX);

                    payerEmail = outputData.getString(Constants.KEY_PAYER_EMAIL);
                    payerFirstName = outputData.getString(Constants.KEY_PAYER_FIRST_NAME);
                    payerLastName = outputData.getString(Constants.KEY_PAYER_LAST_NAME);
                    payerMiddleName = outputData.getString(Constants.KEY_PAYER_MIDDLE_NAME);
                    payerPhone = outputData.getString(Constants.KEY_PAYER_PHONE);
                    payerAddress = outputData.getString(Constants.KEY_PAYER_ADDRESS);
                    payerCountryId = outputData.getLong(Constants.KEY_PAYER_COUNTRY_ID, -1L);
                    payerRegionId = outputData.getLong(Constants.KEY_PAYER_REGION_ID, -1L);
                    payerCityId = outputData.getLong(Constants.KEY_PAYER_CITY_ID, -1L);
                    payerZip = outputData.getString(Constants.KEY_PAYER_ZIP);
                    payerCompany = outputData.getString(Constants.KEY_PAYER_COMPANY_NAME);
                    payerCargo = outputData.getString(Constants.KEY_PAYER_CARGOSTAR);
                    payerTnt = outputData.getString(Constants.KEY_PAYER_TNT);
                    payerFedex = outputData.getString(Constants.KEY_PAYER_FEDEX);

                    payerInn = outputData.getString(Constants.KEY_PAYER_INN);
                    payerBank = outputData.getString(Constants.KEY_PAYER_BANK);
                    payerCheckingAccount = outputData.getString(Constants.KEY_PAYER_CHECKING_ACCOUNT);
                    payerRegistrationCode = outputData.getString(Constants.KEY_PAYER_REGISTRATION_CODE);
                    payerMfo = outputData.getString(Constants.KEY_PAYER_MFO);
                    payerOked = outputData.getString(Constants.KEY_PAYER_OKED);

                    serializedConsignmentList = outputData.getString(Constants.KEY_SERIALIZED_CONSIGNMENT_LIST);

                    progressBar.setVisibility(View.GONE);

                    if (invoiceCourierId > 0) {
                        editInvoiceImageView.setVisibility(View.VISIBLE);
                    }
                    else {
                        editInvoiceImageView.setVisibility(View.GONE);
                    }

                    return;
                }
                if (workInfo.getState() == WorkInfo.State.FAILED || workInfo.getState() == WorkInfo.State.CANCELLED) {
                    Toast.makeText(context, "Не удалось обновить данные с сервера", Toast.LENGTH_SHORT).show();
                    editInvoiceImageView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                editInvoiceImageView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            });
        }
    }

    private void initItemList() {
        itemList.clear();

        publicDataHeading = new InvoiceData(getString(R.string.public_data), null, InvoiceData.TYPE_HEADING);
        senderDataHeading = new InvoiceData(getString(R.string.sender_data), null, InvoiceData.TYPE_HEADING);
        recipientDataHeading = new InvoiceData(getString(R.string.receiver_data), null, InvoiceData.TYPE_HEADING);
        payerDataHeading = new InvoiceData(getString(R.string.payer_data), null, InvoiceData.TYPE_HEADING);
        accountDataHeading = new InvoiceData(getString(R.string.account_numbers), null, InvoiceData.TYPE_HEADING);
        transportationDataHeading = new InvoiceData(getString(R.string.parcel_data), null, InvoiceData.TYPE_HEADING);
        forEachCargoHeading = new InvoiceData(getString(R.string.for_each_cargo), null, InvoiceData.TYPE_HEADING);
        paymentDataHeading = new InvoiceData(getString(R.string.payment_data), null, InvoiceData.TYPE_HEADING);

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
        itemList.add(new InvoiceData(getString(R.string.region), null, InvoiceData.TYPE_ITEM));
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
        senderDataList.add(new InvoiceData(getString(R.string.region), null, InvoiceData.TYPE_ITEM));
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
        itemList.add(new InvoiceData(getString(R.string.region), null, InvoiceData.TYPE_ITEM));
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
        recipientDataList.add(new InvoiceData(getString(R.string.region), null, InvoiceData.TYPE_ITEM));
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
        itemList.add(new InvoiceData(getString(R.string.region), null, InvoiceData.TYPE_ITEM));
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
        payerDataList.add(new InvoiceData(getString(R.string.region), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.city), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.address), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.post_index), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.cargostar_account_number), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.tnt_account_number), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.fedex_account_number), null, InvoiceData.TYPE_ITEM));

        //accounts data
        itemList.add(accountDataHeading);
        itemList.add(new InvoiceData(getString(R.string.checking_account), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.bank), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.payer_registration_code), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.mfo), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.oked), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(null, null, InvoiceData.TYPE_STROKE));

        //accounts data
        accountDataList.add(new InvoiceData(getString(R.string.checking_account), null, InvoiceData.TYPE_ITEM));
        accountDataList.add(new InvoiceData(getString(R.string.bank), null, InvoiceData.TYPE_ITEM));
        accountDataList.add(new InvoiceData(getString(R.string.payer_registration_code), null, InvoiceData.TYPE_ITEM));
        accountDataList.add(new InvoiceData(getString(R.string.mfo), null, InvoiceData.TYPE_ITEM));
        accountDataList.add(new InvoiceData(getString(R.string.oked), null, InvoiceData.TYPE_ITEM));

        //payment data
        itemList.add(paymentDataHeading);
        itemList.add(new InvoiceData(getString(R.string.cost), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.fuel_tax), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.NDC), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.tariff), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(null, null, InvoiceData.TYPE_STROKE));

        //payment data
        paymentDataList.add(new InvoiceData(getString(R.string.cost), null, InvoiceData.TYPE_ITEM));
        paymentDataList.add(new InvoiceData(getString(R.string.fuel_tax), null, InvoiceData.TYPE_ITEM));
        paymentDataList.add(new InvoiceData(getString(R.string.NDC), null, InvoiceData.TYPE_ITEM));
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

        //consignment
        itemList.add(forEachCargoHeading);

        for (int i = 0; i < consignmentQuantity; i++) {
            itemList.add(new InvoiceData(getString(R.string.cargo_name), null, InvoiceData.TYPE_ITEM));
            itemList.add(new InvoiceData(getString(R.string.cargo_description), null, InvoiceData.TYPE_ITEM));
            itemList.add(new InvoiceData(getString(R.string.cargo_price), null, InvoiceData.TYPE_ITEM));
            itemList.add(new InvoiceData(getString(R.string.package_type), null, InvoiceData.TYPE_ITEM));
            itemList.add(new InvoiceData(getString(R.string.dimensions), null, InvoiceData.TYPE_ITEM));
            itemList.add(new InvoiceData(getString(R.string.weight), null, InvoiceData.TYPE_ITEM));
            itemList.add(new InvoiceData(getString(R.string.qr_code), null, InvoiceData.TYPE_ITEM));
        }

        itemList.add(new InvoiceData(null, null, InvoiceData.TYPE_STROKE));

        adapter.setItemList(itemList);
        adapter.notifyDataSetChanged();
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
                for (int i = 1; i < 4; i++) {
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
                for (int i = 0; i < 14; i++) {
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
                for (int i = 0; i < 14; i++) {
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
                for (int i = 0; i < 13; i++) {
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
                for (int i = 0; i < 5; i++) {
                    itemList.remove(position + 1);
                }
            }
            accountDataHidden = !accountDataHidden;
        }
        else if (currentItem.equals(paymentDataHeading)) {
            if (paymentDataHidden) {
                itemList.addAll(position + 1, paymentDataList);
            }
            else {
                for (int i = 0; i < 4; i++) {
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

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
    private static final String TAG = InvoiceDataFragment.class.toString();
}