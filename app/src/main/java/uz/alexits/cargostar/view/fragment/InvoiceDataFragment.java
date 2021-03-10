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
import uz.alexits.cargostar.model.actor.AddressBook;
import uz.alexits.cargostar.model.actor.Customer;
import uz.alexits.cargostar.model.transportation.Consignment;
import uz.alexits.cargostar.model.transportation.Invoice;
import uz.alexits.cargostar.model.transportation.Request;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    private Customer sender;
    private AddressBook recipient;
    private AddressBook payer;

    private Request request;
    private Invoice invoice;

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
            isPublic = InvoiceDataFragmentArgs.fromBundle(getArguments()).getIsPublic();

            request = new Request(
                    InvoiceDataFragmentArgs.fromBundle(getArguments()).getRequestId(),
                    InvoiceDataFragmentArgs.fromBundle(getArguments()).getSenderFirstName(),
                    InvoiceDataFragmentArgs.fromBundle(getArguments()).getSenderMiddleName(),
                    InvoiceDataFragmentArgs.fromBundle(getArguments()).getSenderLastName(),
                    InvoiceDataFragmentArgs.fromBundle(getArguments()).getSenderEmail(),
                    InvoiceDataFragmentArgs.fromBundle(getArguments()).getSenderPhone(),
                    InvoiceDataFragmentArgs.fromBundle(getArguments()).getSenderAddress(),
                    InvoiceDataFragmentArgs.fromBundle(getArguments()).getSenderCountryId(),
                    InvoiceDataFragmentArgs.fromBundle(getArguments()).getSenderCityName(),
                    InvoiceDataFragmentArgs.fromBundle(getArguments()).getRecipientCountryId(),
                    InvoiceDataFragmentArgs.fromBundle(getArguments()).getRecipientCityName(),
                    InvoiceDataFragmentArgs.fromBundle(getArguments()).getComment(),
                    InvoiceDataFragmentArgs.fromBundle(getArguments()).getSenderUserId(),
                    InvoiceDataFragmentArgs.fromBundle(getArguments()).getClientId(),
                    InvoiceDataFragmentArgs.fromBundle(getArguments()).getCourierId(),
                    InvoiceDataFragmentArgs.fromBundle(getArguments()).getProviderId(),
                    InvoiceDataFragmentArgs.fromBundle(getArguments()).getInvoiceId(),
                    1, new Date(), new Date(),
                    InvoiceDataFragmentArgs.fromBundle(getArguments()).getSenderCityName(),
                    InvoiceDataFragmentArgs.fromBundle(getArguments()).getRecipientCityName(),
                    InvoiceDataFragmentArgs.fromBundle(getArguments()).getConsignmentQuantity(),
                    InvoiceDataFragmentArgs.fromBundle(getArguments()).getPaymentStatus(),
                    InvoiceDataFragmentArgs.fromBundle(getArguments()).getDeliveryType());

            if (request.getInvoiceId() > 0 && request.getClientId() > 0) {
                fetchInvoiceRequestUUID = SyncWorkRequest.fetchInvoiceData(context, request.getId(), request.getInvoiceId(), request.getClientId(), request.getConsignmentQuantity());
            }
            else if (request.getClientId() > 0) {
                fetchInvoiceRequestUUID = SyncWorkRequest.fetchSenderData(context, request.getId(), request.getClientId(), request.getConsignmentQuantity());
            }
            else {
                fetchInvoiceRequestUUID = SyncWorkRequest.fetchRequestData(context, request.getId(), request.getConsignmentQuantity());
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
            action.setRequestId(request.getId());
            action.setInvoiceId(request.getInvoiceId());
            action.setInvoiceNumber(invoice.getNumber());
            action.setTariffId(invoice.getTariffId());
            action.setCourierId(request.getCourierId());
            action.setProviderId(request.getProviderId());
            action.setPrice((float) invoice.getPrice());
            action.setDeliveryType(request.getDeliveryType());
            action.setComment(request.getComment());
            action.setConsignmentQuantity(request.getConsignmentQuantity());

            action.setSenderId(sender.getId());
            action.setSenderUserId(sender.getUserId());
            action.setSenderEmail(sender.getEmail());
            action.setSenderFirstName(sender.getFirstName());
            action.setSenderLastName(sender.getLastName());
            action.setSenderMiddleName(sender.getMiddleName());
            action.setSenderPhone(sender.getPhone());
            action.setSenderAddress(sender.getAddress());
            action.setSenderZip(sender.getZip());
            action.setSenderCountryId(sender.getCountryId());
            action.setSenderCityName(sender.getCityName());
            action.setSenderCargo(sender.getCargostarAccountNumber());
            action.setSenderTnt(sender.getTntAccountNumber());
            action.setSenderFedex(sender.getFedexAccountNumber());
            action.setSenderPassport(sender.getPassportSerial());
            action.setSenderInn(sender.getInn());
            action.setSenderCompany(sender.getCompany());
            action.setSenderPhoto(sender.getPhotoUrl());
            action.setSenderSignature(sender.getSignatureUrl());
            action.setSenderDiscount(sender.getDiscount());

            action.setRecipientId(recipient.getId());
            action.setRecipientUserId(recipient.getUserId());
            action.setRecipientEmail(recipient.getEmail());
            action.setRecipientFirstName(recipient.getFirstName());
            action.setRecipientLastName(recipient.getLastName());
            action.setRecipientMiddleName(recipient.getMiddleName());
            action.setRecipientPhone(recipient.getPhone());
            action.setRecipientAddress(recipient.getAddress());
            action.setRecipientZip(recipient.getZip());
            action.setRecipientCountryId(recipient.getCountryId());
            action.setRecipientCityName(recipient.getCityName());
            action.setRecipientCargo(recipient.getCargostarAccountNumber());
            action.setRecipientTnt(recipient.getTntAccountNumber());
            action.setRecipientFedex(recipient.getFedexAccountNumber());
            action.setRecipientPassport(recipient.getPassportSerial());
            action.setRecipientInn(recipient.getInn());
            action.setRecipientCompany(recipient.getCompany());
            action.setRecipientUserType(recipient.getType());

            action.setPayerId(payer.getId());
            action.setPayerUserId(payer.getUserId());
            action.setPayerEmail(payer.getEmail());
            action.setPayerFirstName(payer.getFirstName());
            action.setPayerLastName(payer.getLastName());
            action.setPayerMiddleName(payer.getMiddleName());
            action.setPayerPhone(payer.getPhone());
            action.setPayerAddress(payer.getAddress());
            action.setPayerCountryId(payer.getCountryId());
            action.setPayerCityName(payer.getCityName());
            action.setPayerZip(payer.getZip());
            action.setPayerCargo(payer.getCargostarAccountNumber());
            action.setPayerTnt(payer.getTntAccountNumber());
            action.setPayerFedex(payer.getFedexAccountNumber());
            action.setPayerUserType(payer.getType());
            action.setPayerPassport(payer.getPassportSerial());
            action.setPayerInn(payer.getInn());
            action.setPayerCompany(payer.getCompany());
            action.setPayerContractNumber(payer.getContractNumber());

            action.setSerializedConsignmentList(serializedConsignmentList);

            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(action);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final CourierViewModel courierViewModel = new ViewModelProvider(this).get(CourierViewModel.class);
        final RequestsViewModel requestsViewModel = new ViewModelProvider(this).get(RequestsViewModel.class);

        requestsViewModel.setRequestId(request.getId());
        requestsViewModel.setProviderId(request.getProviderId());
        requestsViewModel.setSenderId(request.getClientId());
        requestsViewModel.setInvoiceId(request.getInvoiceId());
        requestsViewModel.setSenderCountryId(request.getSenderCountryId());
        requestsViewModel.setRecipientCountryId(request.getRecipientCountryId());

        itemList.set(1, new InvoiceData(getString(R.string.invoice_id), request.getInvoiceId() > 0 ? String.valueOf(request.getInvoiceId()) : null, InvoiceData.TYPE_ITEM));
        itemList.set(2, new InvoiceData(getString(R.string.courier_id), request.getCourierId() > 0 ? String.valueOf(request.getCourierId()) : null, InvoiceData.TYPE_ITEM));
        adapter.notifyItemRangeChanged(1, 2);

        publicDataList.set(0, new InvoiceData(getString(R.string.invoice_id), request.getInvoiceId() > 0 ? String.valueOf(request.getInvoiceId()) : null, InvoiceData.TYPE_ITEM));
        publicDataList.set(1, new InvoiceData(getString(R.string.courier_id), request.getCourierId() > 0 ? String.valueOf(request.getCourierId()) : null, InvoiceData.TYPE_ITEM));

        itemList.set(60, new InvoiceData(getString(R.string.destination_quantity), String.valueOf(request.getConsignmentQuantity()), InvoiceData.TYPE_ITEM));
        adapter.notifyItemChanged(60);

        transportationDataList.set(2, new InvoiceData(getString(R.string.destination_quantity), String.valueOf(request.getConsignmentQuantity()), InvoiceData.TYPE_ITEM));

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

                        final Intent mainIntent = new Intent(context, MainActivity.class);
                        mainIntent.putExtra(IntentConstants.INTENT_REQUEST_KEY, IntentConstants.REQUEST_FIND_REQUEST);
                        mainIntent.putExtra(Constants.KEY_REQUEST_ID, outputData.getLong(Constants.KEY_REQUEST_ID, -1L));
                        mainIntent.putExtra(Constants.KEY_INVOICE_ID, outputData.getLong(Constants.KEY_INVOICE_ID, -1L));
                        mainIntent.putExtra(Constants.KEY_CLIENT_ID, outputData.getLong(Constants.KEY_CLIENT_ID, -1L));
                        mainIntent.putExtra(Constants.KEY_COURIER_ID, outputData.getLong(Constants.KEY_COURIER_ID, -1L));
                        mainIntent.putExtra(Constants.KEY_SENDER_COUNTRY_ID, outputData.getLong(Constants.KEY_SENDER_COUNTRY_ID, -1L));
                        mainIntent.putExtra(Constants.KEY_SENDER_REGION_ID, outputData.getLong(Constants.KEY_SENDER_REGION_ID, -1L));
                        mainIntent.putExtra(Constants.KEY_SENDER_CITY_ID, outputData.getLong(Constants.KEY_SENDER_CITY_ID, -1L));
                        mainIntent.putExtra(Constants.KEY_RECIPIENT_COUNTRY_ID, outputData.getLong(Constants.KEY_RECIPIENT_COUNTRY_ID, -1L));
                        mainIntent.putExtra(Constants.KEY_RECIPIENT_CITY_ID, outputData.getLong(Constants.KEY_RECIPIENT_CITY_ID, -1L));
                        mainIntent.putExtra(Constants.KEY_PROVIDER_ID, outputData.getLong(Constants.KEY_PROVIDER_ID, -1L));
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
                itemList.set(54, new InvoiceData(getString(R.string.fuel_tax), String.valueOf(provider.getFuel()), InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(3);
                adapter.notifyItemChanged(54);

                publicDataList.set(2, new InvoiceData(getString(R.string.service_provider), null, InvoiceData.TYPE_ITEM));
                paymentDataList.set(1, new InvoiceData(getString(R.string.fuel_tax), String.valueOf(provider.getFuel()), InvoiceData.TYPE_ITEM));
            }
        });

        /* payment data */
        requestsViewModel.getTariff().observe(getViewLifecycleOwner(), tariff -> {
            if (tariff != null) {
                itemList.set(55, new InvoiceData(getString(R.string.tariff), tariff.getName(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(55);

                paymentDataList.set(2, new InvoiceData(getString(R.string.tariff), tariff.getName(), InvoiceData.TYPE_ITEM));
            }
        });

        requestsViewModel.getSender().observe(getViewLifecycleOwner(), sender -> {
            if (sender != null) {
                requestsViewModel.setSenderCountryId(sender.getCountryId());

                itemList.set(6, new InvoiceData(getString(R.string.email), sender.getEmail(), InvoiceData.TYPE_ITEM));
                itemList.set(7, new InvoiceData(getString(R.string.first_name), sender.getFirstName(), InvoiceData.TYPE_ITEM));
                itemList.set(8, new InvoiceData(getString(R.string.middle_name), sender.getMiddleName(), InvoiceData.TYPE_ITEM));
                itemList.set(9, new InvoiceData(getString(R.string.last_name), sender.getLastName(), InvoiceData.TYPE_ITEM));
                itemList.set(10, new InvoiceData(getString(R.string.phone_number), sender.getPhone(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemRangeChanged(6, 5);

                itemList.set(12, new InvoiceData(getString(R.string.city), sender.getCityName(), InvoiceData.TYPE_ITEM));
                itemList.set(13, new InvoiceData(getString(R.string.take_address), sender.getAddress(), InvoiceData.TYPE_ITEM));
                itemList.set(14, new InvoiceData(getString(R.string.post_index), sender.getZip(), InvoiceData.TYPE_ITEM));
                itemList.set(15, new InvoiceData(getString(R.string.cargostar_account_number), sender.getCargostarAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(16, new InvoiceData(getString(R.string.tnt_account_number), sender.getTntAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(17, new InvoiceData(getString(R.string.fedex_account_number), sender.getFedexAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(18, new InvoiceData(getString(R.string.sender_signature), sender.getSignatureUrl(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemRangeChanged(12, 7);

                senderDataList.set(0, new InvoiceData(getString(R.string.email), sender.getEmail(), InvoiceData.TYPE_ITEM));
                senderDataList.set(1, new InvoiceData(getString(R.string.first_name), sender.getFirstName(), InvoiceData.TYPE_ITEM));
                senderDataList.set(2, new InvoiceData(getString(R.string.middle_name), sender.getMiddleName(), InvoiceData.TYPE_ITEM));
                senderDataList.set(3, new InvoiceData(getString(R.string.last_name), sender.getLastName(), InvoiceData.TYPE_ITEM));
                senderDataList.set(4, new InvoiceData(getString(R.string.phone_number), sender.getPhone(), InvoiceData.TYPE_ITEM));
                senderDataList.set(6, new InvoiceData(getString(R.string.city), sender.getCityName(), InvoiceData.TYPE_ITEM));
                senderDataList.set(7, new InvoiceData(getString(R.string.take_address), sender.getAddress(), InvoiceData.TYPE_ITEM));
                senderDataList.set(8, new InvoiceData(getString(R.string.post_index), sender.getZip(), InvoiceData.TYPE_ITEM));
                senderDataList.set(9, new InvoiceData(getString(R.string.cargostar_account_number), sender.getCargostarAccountNumber(), InvoiceData.TYPE_ITEM));
                senderDataList.set(10, new InvoiceData(getString(R.string.tnt_account_number), sender.getTntAccountNumber(), InvoiceData.TYPE_ITEM));
                senderDataList.set(11, new InvoiceData(getString(R.string.fedex_account_number), sender.getFedexAccountNumber(), InvoiceData.TYPE_ITEM));
                senderDataList.set(12, new InvoiceData(getString(R.string.sender_signature), sender.getSignatureUrl(), InvoiceData.TYPE_ITEM));
            }
        });

        requestsViewModel.getSenderCountry().observe(getViewLifecycleOwner(), country -> {
            if (country != null) {
                itemList.set(11, new InvoiceData(getString(R.string.country), country.getNameEn(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(11);

                senderDataList.set(5, new InvoiceData(getString(R.string.country), country.getNameEn(), InvoiceData.TYPE_ITEM));
            }
        });

        /* recipient data */
        requestsViewModel.getRecipient().observe(getViewLifecycleOwner(), recipient -> {
            if (recipient != null) {
                requestsViewModel.setRecipientCountryId(recipient.getCountryId());

                itemList.set(21, new InvoiceData(getString(R.string.email), recipient.getEmail(), InvoiceData.TYPE_ITEM));
                itemList.set(22, new InvoiceData(getString(R.string.first_name), recipient.getFirstName(), InvoiceData.TYPE_ITEM));
                itemList.set(23, new InvoiceData(getString(R.string.middle_name), recipient.getMiddleName(), InvoiceData.TYPE_ITEM));
                itemList.set(24, new InvoiceData(getString(R.string.last_name), recipient.getLastName(), InvoiceData.TYPE_ITEM));
                itemList.set(25, new InvoiceData(getString(R.string.phone_number), recipient.getPhone(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemRangeChanged(21, 5);

                itemList.set(27, new InvoiceData(getString(R.string.city), recipient.getCityName(), InvoiceData.TYPE_ITEM));
                itemList.set(28, new InvoiceData(getString(R.string.delivery_address), recipient.getAddress(), InvoiceData.TYPE_ITEM));
                itemList.set(29, new InvoiceData(getString(R.string.post_index), recipient.getZip(), InvoiceData.TYPE_ITEM));
                itemList.set(30, new InvoiceData(getString(R.string.cargostar_account_number), recipient.getCargostarAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(31, new InvoiceData(getString(R.string.tnt_account_number), recipient.getTntAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(32, new InvoiceData(getString(R.string.fedex_account_number), recipient.getFedexAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(33, new InvoiceData(getString(R.string.receiver_signature), null, InvoiceData.TYPE_ITEM));
                adapter.notifyItemRangeChanged(27, 7);

                recipientDataList.set(0, new InvoiceData(getString(R.string.email), recipient.getEmail(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(1, new InvoiceData(getString(R.string.first_name), recipient.getFirstName(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(2, new InvoiceData(getString(R.string.middle_name),  recipient.getMiddleName(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(3, new InvoiceData(getString(R.string.last_name), recipient.getLastName(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(4, new InvoiceData(getString(R.string.phone_number), recipient.getPhone(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(6, new InvoiceData(getString(R.string.city), recipient.getCityName(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(7, new InvoiceData(getString(R.string.delivery_address), recipient.getAddress(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(8, new InvoiceData(getString(R.string.post_index), recipient.getZip(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(9, new InvoiceData(getString(R.string.cargostar_account_number), recipient.getCargostarAccountNumber(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(10, new InvoiceData(getString(R.string.tnt_account_number), recipient.getTntAccountNumber(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(11, new InvoiceData(getString(R.string.fedex_account_number), recipient.getFedexAccountNumber(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(12, new InvoiceData(getString(R.string.receiver_signature), null, InvoiceData.TYPE_ITEM));
            }
        });

        requestsViewModel.getRecipientCountry().observe(getViewLifecycleOwner(), country -> {
            if (country != null) {
                itemList.set(26, new InvoiceData(getString(R.string.country), country.getNameEn(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(26);

                recipientDataList.set(5, new InvoiceData(getString(R.string.country), country.getNameEn(), InvoiceData.TYPE_ITEM));
            }
        });

        /* payer data */
        requestsViewModel.getPayer().observe(getViewLifecycleOwner(), payer -> {
            if (payer != null) {
                requestsViewModel.setPayerCountryId(payer.getCountryId());

                itemList.set(36, new InvoiceData(getString(R.string.email), payer.getEmail(), InvoiceData.TYPE_ITEM));
                itemList.set(37, new InvoiceData(getString(R.string.first_name), payer.getFirstName(), InvoiceData.TYPE_ITEM));
                itemList.set(38, new InvoiceData(getString(R.string.middle_name), payer.getMiddleName(), InvoiceData.TYPE_ITEM));
                itemList.set(39, new InvoiceData(getString(R.string.last_name), payer.getLastName(), InvoiceData.TYPE_ITEM));
                itemList.set(40, new InvoiceData(getString(R.string.phone_number), payer.getPhone(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemRangeChanged(36, 5);

                itemList.set(42, new InvoiceData(getString(R.string.city), payer.getCityName(), InvoiceData.TYPE_ITEM));
                itemList.set(43, new InvoiceData(getString(R.string.address), payer.getAddress(), InvoiceData.TYPE_ITEM));
                itemList.set(44, new InvoiceData(getString(R.string.post_index), payer.getZip(), InvoiceData.TYPE_ITEM));
                itemList.set(45, new InvoiceData(getString(R.string.cargostar_account_number), payer.getCargostarAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(46, new InvoiceData(getString(R.string.tnt_account_number), payer.getTntAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(47, new InvoiceData(getString(R.string.fedex_account_number), payer.getFedexAccountNumber(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemRangeChanged(42, 6);

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

                //account data
                itemList.set(50, new InvoiceData(getString(R.string.contract_number), payer.getContractNumber(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(50);

                accountDataList.set(0, new InvoiceData(getString(R.string.checking_account), payer.getContractNumber(), InvoiceData.TYPE_ITEM));
            }
        });

        requestsViewModel.getPayerCountry().observe(getViewLifecycleOwner(), country -> {
            if (country != null) {
                itemList.set(41, new InvoiceData(getString(R.string.country), country.getNameEn(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(41);

                payerDataList.set(5, new InvoiceData(getString(R.string.country), country.getNameEn(), InvoiceData.TYPE_ITEM));
            }
        });

        /* invoice data */
        requestsViewModel.getInvoice().observe(getViewLifecycleOwner(), invoice -> {
            if (invoice != null) {
                requestsViewModel.setRecipientId(invoice.getRecipientId());
                requestsViewModel.setPayerId(invoice.getPayerId());
                requestsViewModel.setTariffId(invoice.getTariffId());

                itemList.set(53, new InvoiceData(getString(R.string.cost), String.valueOf(invoice.getPrice()), InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(53);

                paymentDataList.set(0, new InvoiceData(getString(R.string.cost), String.valueOf(invoice.getPrice()), InvoiceData.TYPE_ITEM));
            }
        });

        /* transportation data */
        requestsViewModel.getTransportation().observe(getViewLifecycleOwner(), transportation -> {
            if (transportation != null) {
                itemList.set(58, new InvoiceData(getString(R.string.tracking_code_main), transportation.getTrackingCode(), InvoiceData.TYPE_ITEM));
                itemList.set(59, new InvoiceData(getString(R.string.qr_code), transportation.getQrCode(), InvoiceData.TYPE_ITEM));
                itemList.set(61, new InvoiceData(getString(R.string.arrival_date), transportation.getArrivalDate(), InvoiceData.TYPE_ITEM));
                itemList.set(62, new InvoiceData(getString(R.string.courier_guidelines), transportation.getInstructions(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemRangeChanged(58, 2);
                adapter.notifyItemRangeChanged(61, 2);

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
                int j = 65;

                if (request.getConsignmentQuantity() > 0) {
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
                        adapter.notifyItemRangeChanged(64, forEachCargoList.size());
                    });
                }
            }
        });

        if (fetchInvoiceRequestUUID != null) {
            WorkManager.getInstance(context).getWorkInfoByIdLiveData(fetchInvoiceRequestUUID).observe(getViewLifecycleOwner(), workInfo -> {
                if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                    final Data outputData = workInfo.getOutputData();

                    if (request == null) {
                        request = new Request();
                    }
                    request.setId(outputData.getLong(Constants.KEY_REQUEST_ID, -1L));
                    request.setSenderCountryId(outputData.getLong(Constants.KEY_SENDER_COUNTRY_ID, -1L));
                    request.setSenderCity(outputData.getString(Constants.KEY_SENDER_CITY_NAME));
                    request.setSenderCityName(outputData.getString(Constants.KEY_SENDER_CITY_NAME));
                    request.setUserId(outputData.getLong(Constants.KEY_SENDER_USER_ID, -1L));
                    request.setClientId(outputData.getLong(Constants.KEY_SENDER_ID, -1L));
                    request.setCourierId(outputData.getLong(Constants.KEY_COURIER_ID, -1L));
                    request.setProviderId(outputData.getLong(Constants.KEY_PROVIDER_ID, -1L));
                    request.setInvoiceId(outputData.getLong(Constants.KEY_INVOICE_ID, -1L));
                    request.setSenderFirstName(outputData.getString(Constants.KEY_SENDER_FIRST_NAME));
                    request.setSenderMiddleName(outputData.getString(Constants.KEY_SENDER_MIDDLE_NAME));
                    request.setSenderLastName(outputData.getString(Constants.KEY_SENDER_LAST_NAME));
                    request.setSenderEmail(outputData.getString(Constants.KEY_SENDER_EMAIL));
                    request.setSenderPhone(outputData.getString(Constants.KEY_SENDER_PHONE));
                    request.setSenderAddress(outputData.getString(Constants.KEY_SENDER_ADDRESS));
                    request.setRecipientCountryId(outputData.getLong(Constants.KEY_RECIPIENT_COUNTRY_ID, -1L));
                    request.setRecipientCityName(outputData.getString(Constants.KEY_RECIPIENT_CITY_NAME));
                    request.setDeliveryType(outputData.getInt(Constants.KEY_DELIVERY_TYPE, 0));
                    request.setComment(outputData.getString(Constants.KEY_COMMENT));
                    request.setConsignmentQuantity(outputData.getInt(Constants.KEY_CONSIGNMENT_QUANTITY, 0));
                    request.setPaymentStatus(outputData.getString(Constants.KEY_PAYMENT_STATUS));

                    invoice = new Invoice(
                            outputData.getLong(Constants.KEY_INVOICE_ID, -1L),
                            outputData.getString(Constants.KEY_NUMBER),
                            outputData.getLong(Constants.KEY_SENDER_ID, -1L),
                            outputData.getLong(Constants.KEY_RECIPIENT_ID, -1L),
                            outputData.getString(Constants.KEY_RECIPIENT_SIGNATURE),
                            outputData.getLong(Constants.KEY_PAYER_ID, -1L),
                            outputData.getLong(Constants.KEY_PROVIDER_ID, -1L),
                            outputData.getLong(Constants.KEY_REQUEST_ID, -1L),
                            outputData.getLong(Constants.KEY_TARIFF_ID, -1L),
                            outputData.getDouble(Constants.KEY_PRICE, -1),
                            1, new Date(), new Date());

                    sender = new Customer(
                            outputData.getLong(Constants.KEY_SENDER_ID, -1L),
                            outputData.getLong(Constants.KEY_SENDER_USER_ID, -1L),
                            outputData.getLong(Constants.KEY_SENDER_COUNTRY_ID, -1L),
                            outputData.getString(Constants.KEY_SENDER_CITY_NAME),
                            outputData.getString(Constants.KEY_SENDER_FIRST_NAME),
                            outputData.getString(Constants.KEY_SENDER_MIDDLE_NAME),
                            outputData.getString(Constants.KEY_SENDER_LAST_NAME),
                            outputData.getString(Constants.KEY_SENDER_PHONE),
                            outputData.getString(Constants.KEY_SENDER_EMAIL),
                            outputData.getString(Constants.KEY_SENDER_ADDRESS),
                            null,
                            outputData.getString(Constants.KEY_SENDER_ZIP),
                            1, new Date(), new Date(),
                            outputData.getString(Constants.KEY_SENDER_CARGOSTAR),
                            outputData.getString(Constants.KEY_SENDER_TNT),
                            outputData.getString(Constants.KEY_SENDER_FEDEX),
                            outputData.getInt(Constants.KEY_DISCOUNT, 0),
                            outputData.getString(Constants.KEY_SENDER_PHOTO),
                            outputData.getString(Constants.KEY_SENDER_SIGNATURE),
                            outputData.getInt(Constants.KEY_SENDER_TYPE, 0),
                            outputData.getString(Constants.KEY_SENDER_PASSPORT),
                            outputData.getString(Constants.KEY_SENDER_INN),
                            outputData.getString(Constants.KEY_SENDER_COMPANY_NAME));

                    recipient = new AddressBook(
                            outputData.getLong(Constants.KEY_RECIPIENT_ID, -1L),
                            outputData.getLong(Constants.KEY_RECIPIENT_USER_ID, -1L),
                            outputData.getLong(Constants.KEY_RECIPIENT_COUNTRY_ID, -1L),
                            outputData.getString(Constants.KEY_RECIPIENT_CITY_NAME),
                            outputData.getString(Constants.KEY_RECIPIENT_ADDRESS),
                            outputData.getString(Constants.KEY_RECIPIENT_ZIP),
                            outputData.getString(Constants.KEY_RECIPIENT_FIRST_NAME),
                            outputData.getString(Constants.KEY_RECIPIENT_MIDDLE_NAME),
                            outputData.getString(Constants.KEY_RECIPIENT_LAST_NAME),
                            outputData.getString(Constants.KEY_RECIPIENT_EMAIL),
                            outputData.getString(Constants.KEY_RECIPIENT_PHONE),
                            outputData.getString(Constants.KEY_RECIPIENT_CARGOSTAR),
                            outputData.getString(Constants.KEY_RECIPIENT_TNT),
                            outputData.getString(Constants.KEY_RECIPIENT_FEDEX),
                            outputData.getString(Constants.KEY_RECIPIENT_COMPANY_NAME),
                            outputData.getString(Constants.KEY_RECIPIENT_INN),
                            outputData.getString(Constants.KEY_RECIPIENT_CONTRACT_NUMBER),
                            outputData.getString(Constants.KEY_RECIPIENT_PASSPORT),
                            outputData.getInt(Constants.KEY_RECIPIENT_TYPE, 0),
                            1, new Date(), new Date());

                    payer = new AddressBook(
                            outputData.getLong(Constants.KEY_PAYER_ID, -1L),
                            outputData.getLong(Constants.KEY_PAYER_USER_ID, -1L),
                            outputData.getLong(Constants.KEY_PAYER_COUNTRY_ID, -1L),
                            outputData.getString(Constants.KEY_PAYER_CITY_NAME),
                            outputData.getString(Constants.KEY_PAYER_ADDRESS),
                            outputData.getString(Constants.KEY_PAYER_ZIP),
                            outputData.getString(Constants.KEY_PAYER_FIRST_NAME),
                            outputData.getString(Constants.KEY_PAYER_MIDDLE_NAME),
                            outputData.getString(Constants.KEY_PAYER_LAST_NAME),
                            outputData.getString(Constants.KEY_PAYER_EMAIL),
                            outputData.getString(Constants.KEY_PAYER_PHONE),
                            outputData.getString(Constants.KEY_PAYER_CARGOSTAR),
                            outputData.getString(Constants.KEY_PAYER_TNT),
                            outputData.getString(Constants.KEY_PAYER_FEDEX),
                            outputData.getString(Constants.KEY_PAYER_COMPANY_NAME),
                            outputData.getString(Constants.KEY_PAYER_INN),
                            outputData.getString(Constants.KEY_PAYER_CONTRACT_NUMBER),
                            outputData.getString(Constants.KEY_PAYER_PASSPORT),
                            outputData.getInt(Constants.KEY_PAYER_TYPE, 0),
                            1, new Date(), new Date());

                    serializedConsignmentList = outputData.getString(Constants.KEY_SERIALIZED_CONSIGNMENT_LIST);

                    progressBar.setVisibility(View.GONE);

                    if (request.getCourierId() > 0) {
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

        //consignment
        itemList.add(forEachCargoHeading);

        for (int i = 0; i < request.getConsignmentQuantity(); i++) {
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
                for (int i = 0; i < 1; i++) {
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

    private static final String TAG = InvoiceDataFragment.class.toString();
}