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
import uz.alexits.cargostar.viewmodel.CourierViewModel;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.activity.CalculatorActivity;
import uz.alexits.cargostar.view.activity.CreateInvoiceActivity;
import uz.alexits.cargostar.view.activity.CreateUserActivity;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.activity.NotificationsActivity;
import uz.alexits.cargostar.view.activity.ProfileActivity;
import uz.alexits.cargostar.view.adapter.InvoiceData;
import uz.alexits.cargostar.view.adapter.InvoiceDataAdapter;
import uz.alexits.cargostar.view.callback.ParcelDataCallback;
import uz.alexits.cargostar.viewmodel.RequestsViewModel;
import uz.alexits.cargostar.workers.SyncWorkRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InvoiceDataFragment extends Fragment implements ParcelDataCallback {
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

    private boolean parcelDataHidden = false;
    private InvoiceData invoiceDataHeading;
    private final List<InvoiceData> invoiceDataList = new ArrayList<>();

    private boolean forEachCargoHidden = false;
    private InvoiceData forEachCargoHeading;
    private int cargoListSize = -1;
    private final List<InvoiceData> forEachCargoList = new ArrayList<>();

    private boolean paymentDataHidden = false;
    private InvoiceData paymentDataHeading;
    private final List<InvoiceData> paymentDataList = new ArrayList<>();

    private boolean logisticsDataHidden = false;
    private InvoiceData logisticsDataHeading;
    private final List<InvoiceData> logisticsDataList = new ArrayList<>();

    private boolean documentsDataHidden = false;
    private InvoiceData documentsDataHeading;
    private final List<InvoiceData> documentsDataList = new ArrayList<>();
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
    private RecyclerView dataRecyclerView;
    private InvoiceDataAdapter adapter;
    private ImageView editParcelImageView;

    private static long requestId = -1L;
    private static int requestOrParcel = -1;
    private static long invoiceId = -1L;
    private static long providerId = -1L;
    private static long courierId = -1L;
    private static long senderId = -1L;
    private static long senderCountryId = -1L;
    private static long senderRegionId = -1L;
    private static long senderCityId = -1L;
    private static long recipientCountryId = -1L;
    private static long recipientCityId = -1L;

    public InvoiceDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        context = getContext();
        itemList = new ArrayList<>();

        publicDataHeading = new InvoiceData(getString(R.string.public_data), null, InvoiceData.TYPE_HEADING);
        senderDataHeading = new InvoiceData(getString(R.string.sender_data), null, InvoiceData.TYPE_HEADING);
        recipientDataHeading = new InvoiceData(getString(R.string.receiver_data), null, InvoiceData.TYPE_HEADING);
        payerDataHeading = new InvoiceData(getString(R.string.payer_data), null, InvoiceData.TYPE_HEADING);
        accountDataHeading = new InvoiceData(getString(R.string.account_numbers), null, InvoiceData.TYPE_HEADING);
        invoiceDataHeading = new InvoiceData(getString(R.string.parcel_data), null, InvoiceData.TYPE_HEADING);
        forEachCargoHeading = new InvoiceData(getString(R.string.for_each_cargo), null, InvoiceData.TYPE_HEADING);
        paymentDataHeading = new InvoiceData(getString(R.string.payment_data), null, InvoiceData.TYPE_HEADING);
        logisticsDataHeading = new InvoiceData(getString(R.string.logistics_data), null, InvoiceData.TYPE_HEADING);
        documentsDataHeading = new InvoiceData(getString(R.string.additional_documents), null, InvoiceData.TYPE_HEADING);

        if (getArguments() != null) {
            //parcelId can be requestId
            requestId = ParcelDataFragmentArgs.fromBundle(getArguments()).getRequestId();
            requestOrParcel = ParcelDataFragmentArgs.fromBundle(getArguments()).getRequestOrParcel();
            invoiceId = ParcelDataFragmentArgs.fromBundle(getArguments()).getInvoiceId();
            senderId = ParcelDataFragmentArgs.fromBundle(getArguments()).getClientId();
            courierId = ParcelDataFragmentArgs.fromBundle(getArguments()).getCourierId();
            providerId = ParcelDataFragmentArgs.fromBundle(getArguments()).getProviderId();
            senderCountryId = ParcelDataFragmentArgs.fromBundle(getArguments()).getSenderCountryId();
            senderRegionId = ParcelDataFragmentArgs.fromBundle(getArguments()).getSenderRegionId();
            senderCityId = ParcelDataFragmentArgs.fromBundle(getArguments()).getSenderCityId();
            recipientCountryId = ParcelDataFragmentArgs.fromBundle(getArguments()).getRecipientCountryId();
            recipientCityId = ParcelDataFragmentArgs.fromBundle(getArguments()).getRecipientCityId();

            if (senderId > 0) {
                SyncWorkRequest.fetchInvoiceData(context, invoiceId, senderId);
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R    .layout.fragment_parcel_data, container, false);
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
        dataRecyclerView = root.findViewById(R.id.data_recycler_view);
        adapter = new InvoiceDataAdapter(context, this);
        dataRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        dataRecyclerView.setAdapter(adapter);
        editParcelImageView = root.findViewById(R.id.edit_parcel_image_view);

        initItemList();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //header views
        profileImageView.setOnClickListener(v -> {
            startActivity(new Intent(context, MainActivity.class));
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
        editImageView.setOnClickListener(v -> {
            startActivity(new Intent(context, ProfileActivity.class));
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final CourierViewModel courierViewModel = new ViewModelProvider(this).get(CourierViewModel.class);
        final RequestsViewModel requestsViewModel = new ViewModelProvider(this).get(RequestsViewModel.class);

        requestsViewModel.setProviderId(providerId);
        requestsViewModel.setSenderId(senderId);
        requestsViewModel.setInvoiceId(invoiceId);
        requestsViewModel.setCourierId(courierId);
        requestsViewModel.setSenderCountryId(senderCountryId);
        requestsViewModel.setSenderRegionId(senderRegionId);
        requestsViewModel.setSenderCityId(senderCityId);
        requestsViewModel.setRecipientCountryId(recipientCountryId);
        requestsViewModel.setRecipientCityId(recipientCityId);

        //todo: request data
        itemList.set(1, new InvoiceData(getString(R.string.invoice_id), invoiceId > 0 ? String.valueOf(invoiceId) : null, InvoiceData.TYPE_ITEM));
        itemList.set(2, new InvoiceData(getString(R.string.courier_id), courierId > 0 ? String.valueOf(courierId) : null, InvoiceData.TYPE_ITEM));
//                itemList.set(3, new ParcelData(getString(R.string.operator_id), request.getOperatorId() > 0 ? String.valueOf(request.getOperatorId()) : null, ParcelData.TYPE_ITEM));
//                itemList.set(4, new ParcelData(getString(R.string.accountant_id), request.getAccountantId() > 0 ? String.valueOf(request.getAccountantId()) : null, ParcelData.TYPE_ITEM));
        adapter.notifyItemRangeChanged(1, 2);

        publicDataList.set(0, new InvoiceData(getString(R.string.invoice_id), invoiceId > 0 ? String.valueOf(invoiceId) : null, InvoiceData.TYPE_ITEM));
        publicDataList.set(1, new InvoiceData(getString(R.string.courier_id), courierId > 0 ? String.valueOf(courierId) : null, InvoiceData.TYPE_ITEM));
//                publicDataList.set(2, new ParcelData(getString(R.string.operator_id), request.getOperatorId() > 0 ? String.valueOf(request.getOperatorId()) : null, ParcelData.TYPE_ITEM));
//                publicDataList.set(3, new ParcelData(getString(R.string.accountant_id), request.getAccountantId() > 0 ? String.valueOf(request.getAccountantId()) : null, ParcelData.TYPE_ITEM));


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

        requestsViewModel.getProvider().observe(getViewLifecycleOwner(), provider -> {
            Log.i(TAG, "getProvider(): " + provider);

            if (provider != null) {
                itemList.set(5, new InvoiceData(getString(R.string.service_provider), provider.getNameEn(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(5);

                publicDataList.set(4, new InvoiceData(getString(R.string.service_provider), null, InvoiceData.TYPE_ITEM));
            }
        });


        /* sender data */
        requestsViewModel.getSender().observe(getViewLifecycleOwner(), sender -> {
            Log.i(TAG, "getSender(): " + sender);

            if (sender != null) {
                requestsViewModel.setSenderCountryId(sender.getCountryId());
                requestsViewModel.setSenderRegionId(sender.getRegionId());
                requestsViewModel.setSenderCityId(sender.getCityId());

                itemList.set(6, new InvoiceData(getString(R.string.sender_signature), null, InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(6);

                publicDataList.set(5, new InvoiceData(getString(R.string.sender_signature), null, InvoiceData.TYPE_ITEM));

                itemList.set(10, new InvoiceData(getString(R.string.login_email), sender.getEmail(), InvoiceData.TYPE_ITEM));
                itemList.set(11, new InvoiceData(getString(R.string.cargostar_account_number), sender.getCargostarAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(12, new InvoiceData(getString(R.string.tnt_account_number), sender.getTntAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(13, new InvoiceData(getString(R.string.fedex_account_number), sender.getFedexAccountNumber(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemRangeChanged(10, 13);

                itemList.set(17, new InvoiceData(getString(R.string.take_address), sender.getAddress(), InvoiceData.TYPE_ITEM));
                itemList.set(18, new InvoiceData(getString(R.string.post_index), sender.getZip(), InvoiceData.TYPE_ITEM));
                itemList.set(19, new InvoiceData(getString(R.string.first_name), sender.getFirstName(), InvoiceData.TYPE_ITEM));
                itemList.set(20, new InvoiceData(getString(R.string.middle_name), sender.getMiddleName(), InvoiceData.TYPE_ITEM));
                itemList.set(21, new InvoiceData(getString(R.string.last_name), sender.getLastName(), InvoiceData.TYPE_ITEM));
                itemList.set(22, new InvoiceData(getString(R.string.phone_number), sender.getPhone(), InvoiceData.TYPE_ITEM));
                itemList.set(23, new InvoiceData(getString(R.string.email), sender.getEmail(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemRangeChanged(17, 23);

                senderDataList.set(0, new InvoiceData(getString(R.string.login_email), sender.getEmail(), InvoiceData.TYPE_ITEM));
                senderDataList.set(1, new InvoiceData(getString(R.string.cargostar_account_number), sender.getCargostarAccountNumber(), InvoiceData.TYPE_ITEM));
                senderDataList.set(2, new InvoiceData(getString(R.string.tnt_account_number), sender.getTntAccountNumber(), InvoiceData.TYPE_ITEM));
                senderDataList.set(3, new InvoiceData(getString(R.string.fedex_account_number), sender.getFedexAccountNumber(), InvoiceData.TYPE_ITEM));
                senderDataList.set(7, new InvoiceData(getString(R.string.take_address), sender.getAddress(), InvoiceData.TYPE_ITEM));
                senderDataList.set(8, new InvoiceData(getString(R.string.post_index), sender.getZip(), InvoiceData.TYPE_ITEM));
                senderDataList.set(9, new InvoiceData(getString(R.string.first_name), sender.getFirstName(), InvoiceData.TYPE_ITEM));
                senderDataList.set(10, new InvoiceData(getString(R.string.middle_name), sender.getMiddleName(), InvoiceData.TYPE_ITEM));
                senderDataList.set(11, new InvoiceData(getString(R.string.last_name), sender.getLastName(), InvoiceData.TYPE_ITEM));
                senderDataList.set(12, new InvoiceData(getString(R.string.phone_number), sender.getPhone(), InvoiceData.TYPE_ITEM));
                senderDataList.set(13, new InvoiceData(getString(R.string.email), sender.getEmail(), InvoiceData.TYPE_ITEM));
            }
        });

        requestsViewModel.getSenderCountry().observe(getViewLifecycleOwner(), country -> {
            Log.i(TAG, "getSenderCountry(): " + country);

            itemList.set(14, new InvoiceData(getString(R.string.country), country != null ? country.getName() : null, InvoiceData.TYPE_ITEM));
            adapter.notifyItemChanged(14);

            senderDataList.set(4, new InvoiceData(getString(R.string.country), country != null ? country.getName() : null, InvoiceData.TYPE_ITEM));
        });

        requestsViewModel.getSenderRegion().observe(getViewLifecycleOwner(), region -> {
            Log.i(TAG, "getSenderRegion(): " + region);

            itemList.set(15, new InvoiceData(getString(R.string.city), region != null ? region.getName() : null, InvoiceData.TYPE_ITEM));
            adapter.notifyItemChanged(15);

            senderDataList.set(5, new InvoiceData(getString(R.string.city), region != null ? region.getName() : null, InvoiceData.TYPE_ITEM));
        });

        requestsViewModel.getSenderCity().observe(getViewLifecycleOwner(), city -> {
            Log.i(TAG, "getSenderCity(): " + city);

            itemList.set(16, new InvoiceData(getString(R.string.region), city != null ? city.getName() : null, InvoiceData.TYPE_ITEM));
            adapter.notifyItemChanged(16);

            senderDataList.set(6, new InvoiceData(getString(R.string.region), city != null ? city.getName() : null, InvoiceData.TYPE_ITEM));
        });

        /* invoice data */
        requestsViewModel.getInvoice().observe(getViewLifecycleOwner(), invoice -> {
            Log.i(TAG, "getInvoice(): " + invoice);

            if (invoice != null) {
                Log.i(TAG, "invoiceData(): " + "recipientId=" + invoice.getRecipientId() + " payerId=" + invoice.getPayerId());
                requestsViewModel.setRecipientId(invoice.getRecipientId());
                requestsViewModel.setPayerId(invoice.getPayerId());
            }
        });

        /* recipient data */
        requestsViewModel.getRecipient().observe(getViewLifecycleOwner(), recipient -> {
            Log.i(TAG, "getRecipient(): " + recipient);

            if (recipient != null) {
                requestsViewModel.setRecipientCountryId(recipient.getCountryId());
                requestsViewModel.setRecipientRegionId(recipient.getRegionId());
                requestsViewModel.setRecipientCityId(recipient.getCityId());

                itemList.set(7, new InvoiceData(getString(R.string.receiver_signature), null, InvoiceData.TYPE_ITEM));
                adapter.notifyItemChanged(7);

                publicDataList.set(6, new InvoiceData(getString(R.string.receiver_signature), null, InvoiceData.TYPE_ITEM));

                itemList.set(26, new InvoiceData(getString(R.string.login_email), recipient.getEmail(), InvoiceData.TYPE_ITEM));
                itemList.set(27, new InvoiceData(getString(R.string.cargostar_account_number), recipient.getCargostarAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(28, new InvoiceData(getString(R.string.tnt_account_number), recipient.getTntAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(29, new InvoiceData(getString(R.string.fedex_account_number), recipient.getFedexAccountNumber(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemRangeChanged(26, 29);

                itemList.set(33, new InvoiceData(getString(R.string.take_address), recipient.getAddress(), InvoiceData.TYPE_ITEM));
                itemList.set(34, new InvoiceData(getString(R.string.post_index), recipient.getZip(), InvoiceData.TYPE_ITEM));
                itemList.set(35, new InvoiceData(getString(R.string.first_name), recipient.getFirstName(), InvoiceData.TYPE_ITEM));
                itemList.set(36, new InvoiceData(getString(R.string.middle_name), recipient.getMiddleName(), InvoiceData.TYPE_ITEM));
                itemList.set(37, new InvoiceData(getString(R.string.last_name), recipient.getLastName(), InvoiceData.TYPE_ITEM));
                itemList.set(38, new InvoiceData(getString(R.string.phone_number), recipient.getPhone(), InvoiceData.TYPE_ITEM));
                itemList.set(39, new InvoiceData(getString(R.string.email), recipient.getEmail(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemRangeChanged(33, 39);

                recipientDataList.set(0, new InvoiceData(getString(R.string.login_email), recipient.getEmail(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(1, new InvoiceData(getString(R.string.cargostar_account_number), recipient.getCargostarAccountNumber(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(2, new InvoiceData(getString(R.string.tnt_account_number), recipient.getTntAccountNumber(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(3, new InvoiceData(getString(R.string.fedex_account_number), recipient.getFedexAccountNumber(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(7, new InvoiceData(getString(R.string.take_address), recipient.getAddress(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(8, new InvoiceData(getString(R.string.post_index), recipient.getZip(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(9, new InvoiceData(getString(R.string.first_name), recipient.getFirstName(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(10, new InvoiceData(getString(R.string.middle_name),  recipient.getMiddleName(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(11, new InvoiceData(getString(R.string.last_name), recipient.getLastName(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(12, new InvoiceData(getString(R.string.phone_number), recipient.getPhone(), InvoiceData.TYPE_ITEM));
                recipientDataList.set(13, new InvoiceData(getString(R.string.email), recipient.getEmail(), InvoiceData.TYPE_ITEM));
            }
        });

        requestsViewModel.getRecipientCountry().observe(getViewLifecycleOwner(), country -> {
            Log.i(TAG, "getRecipientCountry(): " + country);

            itemList.set(30, new InvoiceData(getString(R.string.country), country != null ? country.getName() : null, InvoiceData.TYPE_ITEM));
            adapter.notifyItemChanged(30);

            recipientDataList.set(4, new InvoiceData(getString(R.string.country), country != null ? country.getName() : null, InvoiceData.TYPE_ITEM));
        });

        requestsViewModel.getRecipientRegion().observe(getViewLifecycleOwner(), region -> {
            Log.i(TAG, "getRecipientRegion(): " + region);

            itemList.set(31, new InvoiceData(getString(R.string.region), region != null ? region.getName() : null, InvoiceData.TYPE_ITEM));
            adapter.notifyItemChanged(31);

            recipientDataList.set(5, new InvoiceData(getString(R.string.region), region != null ? region.getName() : null, InvoiceData.TYPE_ITEM));

        });

        requestsViewModel.getRecipientCity().observe(getViewLifecycleOwner(), city -> {
            Log.i(TAG, "getRecipientCity(): " + city);

            itemList.set(32, new InvoiceData(getString(R.string.city), city != null ? city.getName() : null, InvoiceData.TYPE_ITEM));
            adapter.notifyItemChanged(32);

            recipientDataList.set(6, new InvoiceData(getString(R.string.city), city != null ? city.getName() : null, InvoiceData.TYPE_ITEM));
        });

        /* payer data */
        requestsViewModel.getPayer().observe(getViewLifecycleOwner(), payer -> {
            Log.i(TAG, "getPayer(): " + payer);

            if (payer != null) {
                requestsViewModel.setPayerCountryId(payer.getCountryId());
                requestsViewModel.setPayerRegionId(payer.getRegionId());
                requestsViewModel.setPayerCityId(payer.getCityId());

                itemList.set(42, new InvoiceData(getString(R.string.login_email), payer.getEmail(), InvoiceData.TYPE_ITEM));
                itemList.set(43, new InvoiceData(getString(R.string.cargostar_account_number), payer.getCargostarAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(44, new InvoiceData(getString(R.string.tnt_account_number), payer.getTntAccountNumber(), InvoiceData.TYPE_ITEM));
                itemList.set(45, new InvoiceData(getString(R.string.fedex_account_number), payer.getFedexAccountNumber(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemRangeChanged(42, 45);

                itemList.set(49, new InvoiceData(getString(R.string.take_address), payer.getAddress(), InvoiceData.TYPE_ITEM));
                itemList.set(50, new InvoiceData(getString(R.string.post_index), payer.getZip(), InvoiceData.TYPE_ITEM));
                itemList.set(51, new InvoiceData(getString(R.string.first_name), payer.getFirstName(), InvoiceData.TYPE_ITEM));
                itemList.set(52, new InvoiceData(getString(R.string.middle_name), payer.getMiddleName(), InvoiceData.TYPE_ITEM));
                itemList.set(53, new InvoiceData(getString(R.string.last_name), payer.getLastName(), InvoiceData.TYPE_ITEM));
                itemList.set(54, new InvoiceData(getString(R.string.phone_number), payer.getPhone(), InvoiceData.TYPE_ITEM));
                itemList.set(55, new InvoiceData(getString(R.string.email), payer.getEmail(), InvoiceData.TYPE_ITEM));
//                itemList.set(56, new ParcelData(getString(R.string.discount), payer.getDiscount() >= 0 ? String.valueOf(payer.getDiscount()) : null, ParcelData.TYPE_ITEM));
                adapter.notifyItemRangeChanged(49, 56);

                payerDataList.set(0, new InvoiceData(getString(R.string.login_email), payer.getEmail(), InvoiceData.TYPE_ITEM));
                payerDataList.set(1, new InvoiceData(getString(R.string.cargostar_account_number), payer.getCargostarAccountNumber(), InvoiceData.TYPE_ITEM));
                payerDataList.set(2, new InvoiceData(getString(R.string.tnt_account_number), payer.getTntAccountNumber(), InvoiceData.TYPE_ITEM));
                payerDataList.set(3, new InvoiceData(getString(R.string.fedex_account_number), payer.getFedexAccountNumber(), InvoiceData.TYPE_ITEM));
                payerDataList.set(7, new InvoiceData(getString(R.string.take_address), payer.getAddress(), InvoiceData.TYPE_ITEM));
                payerDataList.set(8, new InvoiceData(getString(R.string.post_index), payer.getZip(), InvoiceData.TYPE_ITEM));
                payerDataList.set(9, new InvoiceData(getString(R.string.first_name), payer.getFirstName(), InvoiceData.TYPE_ITEM));
                payerDataList.set(10, new InvoiceData(getString(R.string.middle_name), payer.getMiddleName(), InvoiceData.TYPE_ITEM));
                payerDataList.set(11, new InvoiceData(getString(R.string.last_name), payer.getLastName(), InvoiceData.TYPE_ITEM));
                payerDataList.set(12, new InvoiceData(getString(R.string.phone_number), payer.getPhone(), InvoiceData.TYPE_ITEM));
                payerDataList.set(13, new InvoiceData(getString(R.string.email), payer.getEmail(), InvoiceData.TYPE_ITEM));
//                payerDataList.set(14, new ParcelData(getString(R.string.discount), payer.getDiscount() >= 0 ? String.valueOf(payer.getDiscount()) : null, ParcelData.TYPE_ITEM));

                //account data
                itemList.set(59, new InvoiceData(getString(R.string.checking_account), payer.getCheckingAccount(), InvoiceData.TYPE_ITEM));
                itemList.set(60, new InvoiceData(getString(R.string.bank), payer.getBank(), InvoiceData.TYPE_ITEM));
                itemList.set(61, new InvoiceData(getString(R.string.payer_registration_code), payer.getRegistrationCode(), InvoiceData.TYPE_ITEM));
                itemList.set(62, new InvoiceData(getString(R.string.mfo), payer.getMfo(), InvoiceData.TYPE_ITEM));
                itemList.set(63, new InvoiceData(getString(R.string.oked), payer.getOked(), InvoiceData.TYPE_ITEM));
                adapter.notifyItemRangeChanged(59, 63);

                accountDataList.set(0, new InvoiceData(getString(R.string.checking_account), payer.getCheckingAccount(), InvoiceData.TYPE_ITEM));
                accountDataList.set(1, new InvoiceData(getString(R.string.bank), payer.getBank(), InvoiceData.TYPE_ITEM));
                accountDataList.set(2, new InvoiceData(getString(R.string.payer_registration_code), payer.getRegistrationCode(), InvoiceData.TYPE_ITEM));
                accountDataList.set(3, new InvoiceData(getString(R.string.mfo), payer.getMfo(), InvoiceData.TYPE_ITEM));
                accountDataList.set(4, new InvoiceData(getString(R.string.oked), payer.getOked(), InvoiceData.TYPE_ITEM));
            }
        });

        requestsViewModel.getPayerCountry().observe(getViewLifecycleOwner(), country -> {
            Log.i(TAG, "getPayerCountry(): " + country);

            itemList.set(46, new InvoiceData(getString(R.string.country), country != null ? country.getName() : null, InvoiceData.TYPE_ITEM));
            adapter.notifyItemChanged(46);

            payerDataList.set(4, new InvoiceData(getString(R.string.country), country != null ? country.getName() : null, InvoiceData.TYPE_ITEM));
        });

        requestsViewModel.getPayerRegion().observe(getViewLifecycleOwner(), region -> {
            Log.i(TAG, "getPayerRegion(): " + region);

            itemList.set(47, new InvoiceData(getString(R.string.region), region != null ? region.getName() : null, InvoiceData.TYPE_ITEM));
            adapter.notifyItemChanged(47);

            payerDataList.set(5, new InvoiceData(getString(R.string.region), region != null ? region.getName() : null, InvoiceData.TYPE_ITEM));

        });

        requestsViewModel.getPayerCity().observe(getViewLifecycleOwner(), city -> {
            Log.i(TAG, "getPayerCity(): " + city);

            itemList.set(48, new InvoiceData(getString(R.string.city), city != null ? city.getName() : null, InvoiceData.TYPE_ITEM));
            adapter.notifyItemChanged(48);

            payerDataList.set(6, new InvoiceData(getString(R.string.city), city != null ? city.getName() : null, InvoiceData.TYPE_ITEM));
        });

        if (invoiceId <= 0) {
            editParcelImageView.setVisibility(View.GONE);
            editParcelImageView.setOnClickListener(null);
        }
        else {
            editParcelImageView.setVisibility(View.VISIBLE);
            editParcelImageView.setOnClickListener(v -> {
                //todo: pass all data here
                final Intent createParcelIntent = new Intent(getContext(), CreateInvoiceActivity.class);
                createParcelIntent.putExtra(IntentConstants.INTENT_REQUEST_KEY, IntentConstants.REQUEST_EDIT_PARCEL);
                createParcelIntent.putExtra(IntentConstants.INTENT_REQUEST_VALUE, requestId);
                createParcelIntent.putExtra(IntentConstants.INTENT_REQUEST_OR_PARCEL, requestOrParcel);
                startActivity(createParcelIntent);
            });
        }



        parcelSearchImageView.setOnClickListener(v -> {
            final String parcelIdStr = parcelSearchEditText.getText().toString();
            if (TextUtils.isEmpty(parcelIdStr)) {
                Toast.makeText(context, "Введите ID перевозки или номер накладной", Toast.LENGTH_SHORT).show();
                return;
            }

            final long parcelId = Long.parseLong(parcelIdStr);

            courierViewModel.selectRequest(parcelId).observe(getViewLifecycleOwner(), receiptWithCargoList -> {
                if (receiptWithCargoList == null) {
                    Toast.makeText(context, "Накладной не существует", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (receiptWithCargoList.getReceipt() == null) {
//                    Toast.makeText(context, "Накладной не существует", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                final Intent mainIntent = new Intent(context, MainActivity.class);
                mainIntent.putExtra(IntentConstants.INTENT_REQUEST_KEY, IntentConstants.REQUEST_FIND_PARCEL);
                mainIntent.putExtra(IntentConstants.INTENT_REQUEST_VALUE, parcelId);
                startActivity(mainIntent);
            });
        });
    }

    private void initItemList() {
        itemList.clear();

        //public data
        itemList.add(publicDataHeading);
        itemList.add(new InvoiceData(getString(R.string.invoice_id), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.courier_id), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.operator_id), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.accountant_id), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.service_provider), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.sender_signature), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.receiver_signature), null, InvoiceData.TYPE_ITEM));

        itemList.add(new InvoiceData(null, null, InvoiceData.TYPE_STROKE));
        //public data
        publicDataList.add(new InvoiceData(getString(R.string.invoice_id), null, InvoiceData.TYPE_ITEM));
        publicDataList.add(new InvoiceData(getString(R.string.courier_id), null, InvoiceData.TYPE_ITEM));
        publicDataList.add(new InvoiceData(getString(R.string.operator_id), null, InvoiceData.TYPE_ITEM));
        publicDataList.add(new InvoiceData(getString(R.string.accountant_id), null, InvoiceData.TYPE_ITEM));
        publicDataList.add(new InvoiceData(getString(R.string.service_provider), null, InvoiceData.TYPE_ITEM));
        publicDataList.add(new InvoiceData(getString(R.string.sender_signature), null, InvoiceData.TYPE_ITEM));
        publicDataList.add(new InvoiceData(getString(R.string.receiver_signature), null, InvoiceData.TYPE_ITEM));

        //sender data
        itemList.add(senderDataHeading);
        itemList.add(new InvoiceData(getString(R.string.login_email), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.cargostar_account_number), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.tnt_account_number), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.fedex_account_number), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.country), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.region), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.city), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.take_address), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.post_index), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.first_name), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.middle_name), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.last_name), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.phone_number), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.email), null, InvoiceData.TYPE_ITEM));

        itemList.add(new InvoiceData(null, null, InvoiceData.TYPE_STROKE));
        //sender data
        senderDataList.add(new InvoiceData(getString(R.string.login_email), null, InvoiceData.TYPE_ITEM));
        senderDataList.add(new InvoiceData(getString(R.string.cargostar_account_number), null, InvoiceData.TYPE_ITEM));
        senderDataList.add(new InvoiceData(getString(R.string.tnt_account_number), null, InvoiceData.TYPE_ITEM));
        senderDataList.add(new InvoiceData(getString(R.string.fedex_account_number), null, InvoiceData.TYPE_ITEM));
        senderDataList.add(new InvoiceData(getString(R.string.country), null, InvoiceData.TYPE_ITEM));
        senderDataList.add(new InvoiceData(getString(R.string.region), null, InvoiceData.TYPE_ITEM));
        senderDataList.add(new InvoiceData(getString(R.string.city), null, InvoiceData.TYPE_ITEM));
        senderDataList.add(new InvoiceData(getString(R.string.take_address), null, InvoiceData.TYPE_ITEM));
        senderDataList.add(new InvoiceData(getString(R.string.post_index), null, InvoiceData.TYPE_ITEM));
        senderDataList.add(new InvoiceData(getString(R.string.first_name), null, InvoiceData.TYPE_ITEM));
        senderDataList.add(new InvoiceData(getString(R.string.middle_name), null, InvoiceData.TYPE_ITEM));
        senderDataList.add(new InvoiceData(getString(R.string.last_name), null, InvoiceData.TYPE_ITEM));
        senderDataList.add(new InvoiceData(getString(R.string.phone_number), null, InvoiceData.TYPE_ITEM));
        senderDataList.add(new InvoiceData(getString(R.string.email), null, InvoiceData.TYPE_ITEM));
        //recipient data
        itemList.add(recipientDataHeading);
        itemList.add(new InvoiceData(getString(R.string.login_email), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.cargostar_account_number), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.tnt_account_number), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.fedex_account_number), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.country), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.region), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.city), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.take_address), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.post_index), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.first_name), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.middle_name), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.last_name), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.phone_number), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.email), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(null, null, InvoiceData.TYPE_STROKE));

        //recipient data
        recipientDataList.add(new InvoiceData(getString(R.string.login_email), null, InvoiceData.TYPE_ITEM));
        recipientDataList.add(new InvoiceData(getString(R.string.cargostar_account_number), null, InvoiceData.TYPE_ITEM));
        recipientDataList.add(new InvoiceData(getString(R.string.tnt_account_number), null, InvoiceData.TYPE_ITEM));
        recipientDataList.add(new InvoiceData(getString(R.string.fedex_account_number), null, InvoiceData.TYPE_ITEM));
        recipientDataList.add(new InvoiceData(getString(R.string.country), null, InvoiceData.TYPE_ITEM));
        recipientDataList.add(new InvoiceData(getString(R.string.region), null, InvoiceData.TYPE_ITEM));
        recipientDataList.add(new InvoiceData(getString(R.string.city), null, InvoiceData.TYPE_ITEM));
        recipientDataList.add(new InvoiceData(getString(R.string.take_address), null, InvoiceData.TYPE_ITEM));
        recipientDataList.add(new InvoiceData(getString(R.string.post_index), null, InvoiceData.TYPE_ITEM));
        recipientDataList.add(new InvoiceData(getString(R.string.first_name), null, InvoiceData.TYPE_ITEM));
        recipientDataList.add(new InvoiceData(getString(R.string.middle_name), null, InvoiceData.TYPE_ITEM));
        recipientDataList.add(new InvoiceData(getString(R.string.last_name), null, InvoiceData.TYPE_ITEM));
        recipientDataList.add(new InvoiceData(getString(R.string.phone_number), null, InvoiceData.TYPE_ITEM));
        recipientDataList.add(new InvoiceData(getString(R.string.email), null, InvoiceData.TYPE_ITEM));

        //payer data
        itemList.add(payerDataHeading);
        itemList.add(new InvoiceData(getString(R.string.login_email), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.cargostar_account_number), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.tnt_account_number), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.fedex_account_number), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.country), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.region), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.city), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.take_address), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.post_index), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.first_name), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.middle_name), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.last_name), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.phone_number), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.email), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.discount), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(null, null, InvoiceData.TYPE_STROKE));

        //payer data
        payerDataList.add(new InvoiceData(getString(R.string.login_email), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.cargostar_account_number), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.tnt_account_number), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.fedex_account_number), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.country), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.region), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.city), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.take_address), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.post_index), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.first_name), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.middle_name), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.last_name), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.phone_number), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.email), null, InvoiceData.TYPE_ITEM));
        payerDataList.add(new InvoiceData(getString(R.string.discount), null, InvoiceData.TYPE_ITEM));

        //accounts data
        itemList.add(accountDataHeading);
        itemList.add(new InvoiceData(getString(R.string.checking_account), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.bank), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.payer_registration_code), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.mfo), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.oked), null, InvoiceData.TYPE_ITEM));

        itemList.add(new InvoiceData(null, null, InvoiceData.TYPE_STROKE));
        //account data
        accountDataList.add(new InvoiceData(getString(R.string.checking_account), null, InvoiceData.TYPE_ITEM));
        accountDataList.add(new InvoiceData(getString(R.string.bank), null, InvoiceData.TYPE_ITEM));
        accountDataList.add(new InvoiceData(getString(R.string.payer_registration_code), null, InvoiceData.TYPE_ITEM));
        accountDataList.add(new InvoiceData(getString(R.string.mfo), null, InvoiceData.TYPE_ITEM));
        accountDataList.add(new InvoiceData(getString(R.string.oked), null, InvoiceData.TYPE_ITEM));

        //parcel data
        itemList.add(invoiceDataHeading);
        //todo: get trackingCode from Parcel
        itemList.add(new InvoiceData(getString(R.string.tracking_code_main), null, InvoiceData.TYPE_ITEM));
        //todo: get qr from Parcel
        itemList.add(new InvoiceData(getString(R.string.qr_code), null, InvoiceData.TYPE_ITEM));
        //todo: get instructions from Parcel
        itemList.add(new InvoiceData(getString(R.string.courier_guidelines), null, InvoiceData.TYPE_ITEM));
        //todo: get tariff from Provider
        itemList.add(new InvoiceData(getString(R.string.tariff), null, InvoiceData.TYPE_ITEM));
        //todo: get destinationQuantity from size of CargoList
        itemList.add(new InvoiceData(getString(R.string.destination_quantity), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(null, null, InvoiceData.TYPE_STROKE));
        invoiceDataList.add(new InvoiceData(getString(R.string.tracking_code_main), null, InvoiceData.TYPE_ITEM));
        invoiceDataList.add(new InvoiceData(getString(R.string.qr_code), null, InvoiceData.TYPE_ITEM));
        invoiceDataList.add(new InvoiceData(getString(R.string.courier_guidelines), null, InvoiceData.TYPE_ITEM));
        invoiceDataList.add(new InvoiceData(getString(R.string.tariff), null, InvoiceData.TYPE_ITEM));
        invoiceDataList.add(new InvoiceData(getString(R.string.destination_quantity), null, InvoiceData.TYPE_ITEM));
        //for each cargo
        itemList.add(forEachCargoHeading);
//            cargoListSize = request.getCargoList().size();
//            for (final Cargo cargo : request.getCargoList()) {
//                itemList.add(new ParcelData(getString(R.string.cargo_description), cargo.getDescription(), ParcelData.TYPE_ITEM));
//                itemList.add(new ParcelData(getString(R.string.package_type), cargo.getPackageType(), ParcelData.TYPE_ITEM));
//                itemList.add(new ParcelData(getString(R.string.length), String.valueOf(cargo.getLength()), ParcelData.TYPE_ITEM));
//                itemList.add(new ParcelData(getString(R.string.width), String.valueOf(cargo.getWidth()), ParcelData.TYPE_ITEM));
//                itemList.add(new ParcelData(getString(R.string.height), String.valueOf(cargo.getHeight()), ParcelData.TYPE_ITEM));
//                itemList.add(new ParcelData(getString(R.string.weight), String.valueOf(cargo.getWeight()), ParcelData.TYPE_ITEM));
//                itemList.add(new ParcelData(getString(R.string.qr_code), cargo.getQr(), ParcelData.TYPE_ITEM));
//                itemList.add(new ParcelData(null, null, ParcelData.TYPE_STROKE));
//                forEachCargoList.add(new ParcelData(getString(R.string.cargo_description), cargo.getDescription(), ParcelData.TYPE_ITEM));
//                forEachCargoList.add(new ParcelData(getString(R.string.package_type), cargo.getPackageType(), ParcelData.TYPE_ITEM));
//                forEachCargoList.add(new ParcelData(getString(R.string.length), String.valueOf(cargo.getLength()), ParcelData.TYPE_ITEM));
//                forEachCargoList.add(new ParcelData(getString(R.string.width), String.valueOf(cargo.getWidth()), ParcelData.TYPE_ITEM));
//                forEachCargoList.add(new ParcelData(getString(R.string.height), String.valueOf(cargo.getHeight()), ParcelData.TYPE_ITEM));
//                forEachCargoList.add(new ParcelData(getString(R.string.weight), String.valueOf(cargo.getWeight()), ParcelData.TYPE_ITEM));
//                forEachCargoList.add(new ParcelData(getString(R.string.qr_code), cargo.getQr(), ParcelData.TYPE_ITEM));
//                forEachCargoList.add(new ParcelData(null, null, ParcelData.TYPE_STROKE));
//            }
//            forEachCargoList.remove(forEachCargoList.size() - 1);
        //payment data
        itemList.add(paymentDataHeading);
        //todo: get overall cost from Invoice
        itemList.add(new InvoiceData(getString(R.string.cost), null, InvoiceData.TYPE_ITEM));
        //todo: get fuel from Provider
        itemList.add(new InvoiceData(getString(R.string.fuel_tax), null, InvoiceData.TYPE_ITEM));
        //todo: get vat from ZoneSettings
        itemList.add(new InvoiceData(getString(R.string.NDC), null, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(null, null, InvoiceData.TYPE_STROKE));
        paymentDataList.add(new InvoiceData(getString(R.string.cost), null, InvoiceData.TYPE_ITEM));
        paymentDataList.add(new InvoiceData(getString(R.string.fuel_tax), null, InvoiceData.TYPE_ITEM));
        paymentDataList.add(new InvoiceData(getString(R.string.NDC), null, InvoiceData.TYPE_ITEM));
        //transportation data
        itemList.add(logisticsDataHeading);

        String dispatchDate = null;
        String arrivalDate = null;

//            if (request.getDispatchDate() != null) {
//                dispatchDate = dateFormatter.format(request.getDispatchDate());
//            }
//            if (request.getArrivalDate() != null) {
//                arrivalDate = dateFormatter.format(request.getArrivalDate());
//            }
        itemList.add(new InvoiceData(getString(R.string.dispatch_date), dispatchDate, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(getString(R.string.arrival_date), arrivalDate, InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(null, null, InvoiceData.TYPE_STROKE));
        logisticsDataList.add(new InvoiceData(getString(R.string.dispatch_date), dispatchDate, InvoiceData.TYPE_ITEM));
        logisticsDataList.add(new InvoiceData(getString(R.string.arrival_date), arrivalDate, InvoiceData.TYPE_ITEM));
        //documents data
        itemList.add(documentsDataHeading);
        //todo: requestDoc from ???
        itemList.add(new InvoiceData(getString(R.string.e_bid), getString(R.string.absent), InvoiceData.TYPE_ITEM));
        //todo: receiptDoc from ???
        itemList.add(new InvoiceData(getString(R.string.e_waybill), getString(R.string.absent), InvoiceData.TYPE_ITEM));
        //todo: guaranteeLetterDoc from ???
        itemList.add(new InvoiceData(getString(R.string.payer_guarantee_letter), getString(R.string.absent), InvoiceData.TYPE_ITEM));
        //todo: paybillDoc from ???
        itemList.add(new InvoiceData(getString(R.string.pay_bill), getString(R.string.absent), InvoiceData.TYPE_ITEM));
        //todo: invoiceDoc from ???
        itemList.add(new InvoiceData(getString(R.string.invoice_document), getString(R.string.absent), InvoiceData.TYPE_ITEM));
        //todo: invoiceDoc from ???
        itemList.add(new InvoiceData(getString(R.string.invoice), getString(R.string.absent), InvoiceData.TYPE_ITEM));
        itemList.add(new InvoiceData(null, null, InvoiceData.TYPE_STROKE));
        documentsDataList.add(new InvoiceData(getString(R.string.e_bid), getString(R.string.absent), InvoiceData.TYPE_ITEM));
        documentsDataList.add(new InvoiceData(getString(R.string.e_waybill), getString(R.string.absent), InvoiceData.TYPE_ITEM));
        documentsDataList.add(new InvoiceData(getString(R.string.payer_guarantee_letter), getString(R.string.absent), InvoiceData.TYPE_ITEM));
        documentsDataList.add(new InvoiceData(getString(R.string.pay_bill), getString(R.string.absent), InvoiceData.TYPE_ITEM));
        documentsDataList.add(new InvoiceData(getString(R.string.invoice_document), getString(R.string.absent), InvoiceData.TYPE_ITEM));
        documentsDataList.add(new InvoiceData(getString(R.string.invoice), getString(R.string.absent), InvoiceData.TYPE_ITEM));

        adapter.setItemList(itemList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onArrowTapped(final int position) {
        final InvoiceData currentItem = itemList.get(position);
        currentItem.isHidden = !currentItem.isHidden;

        if (currentItem.equals(publicDataHeading)) {
            if (publicDataHidden) {
                itemList.addAll(position + 1, publicDataList);
            }
            else {
                for (int i = 0; i < 7; i++) {
                    itemList.remove(position + 1);
                }
            }
            publicDataHidden = !publicDataHidden;
        }
        else if (currentItem.equals(invoiceDataHeading)) {
            if (parcelDataHidden) {
                itemList.addAll(position + 1, invoiceDataList);
            }
            else {
                for (int i = 0; i < 5; i++) {
                    itemList.remove(position + 1);
                }
            }
            parcelDataHidden = !parcelDataHidden;
        }
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
        else if (currentItem.equals(payerDataHeading)) {
            if (payerDataHidden) {
                itemList.addAll(position + 1, payerDataList);
            }
            else {
                for (int i = 0; i < 15; i++) {
                    itemList.remove(position + 1);
                }
            }
            payerDataHidden = !payerDataHidden;
        }
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
        else if (currentItem.equals(forEachCargoHeading)) {
            if (cargoListSize > 0) {
                if (forEachCargoHidden) {
                    itemList.addAll(position + 1, forEachCargoList);
                }
                else {
                    for (int i = 0; i < (8 * cargoListSize) - 1; i++) {
                        itemList.remove(position + 1);
                    }
                }
                forEachCargoHidden = !forEachCargoHidden;
            }
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
        else if (currentItem.equals(logisticsDataHeading)) {
            if (logisticsDataHidden) {
                itemList.addAll(position + 1, logisticsDataList);
            }
            else {
                for (int i = 0; i < 2; i++) {
                    itemList.remove(position + 1);
                }
            }
            logisticsDataHidden = !logisticsDataHidden;
        }
        else if (currentItem.equals(documentsDataHeading)) {
            if (documentsDataHidden) {
                itemList.addAll(position + 1, documentsDataList);
            }
            else {
                for (int i = 0; i < 6; i++) {
                    itemList.remove(position + 1);
                }
            }
            documentsDataHidden = !documentsDataHidden;
        }
        adapter.notifyDataSetChanged();
    }

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
    private static final String TAG = InvoiceDataFragment.class.toString();
}