package uz.alexits.cargostar.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.gson.Gson;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import uz.alexits.cargostar.R;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.TariffPrice;
import uz.alexits.cargostar.model.actor.AddressBook;
import uz.alexits.cargostar.model.actor.Customer;
import uz.alexits.cargostar.model.calculation.Packaging;
import uz.alexits.cargostar.model.calculation.PackagingType;
import uz.alexits.cargostar.model.calculation.Provider;
import uz.alexits.cargostar.model.calculation.Vat;
import uz.alexits.cargostar.model.calculation.Zone;
import uz.alexits.cargostar.model.calculation.ZoneSettings;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.model.transportation.Consignment;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.utils.Regex;
import uz.alexits.cargostar.utils.Serializer;
import uz.alexits.cargostar.view.UiUtils;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.activity.ScanQrActivity;
import uz.alexits.cargostar.view.activity.SignatureActivity;
import uz.alexits.cargostar.view.adapter.ConsignmentAdapter;
import uz.alexits.cargostar.view.adapter.CustomArrayAdapter;
import uz.alexits.cargostar.view.adapter.TariffPriceRadioAdapter;
import uz.alexits.cargostar.view.callback.CreateInvoiceCallback;
import uz.alexits.cargostar.view.callback.TariffCallback;
import uz.alexits.cargostar.viewmodel.CalculatorViewModel;
import uz.alexits.cargostar.viewmodel.CourierViewModel;
import uz.alexits.cargostar.viewmodel.CreateInvoiceViewModel;
import uz.alexits.cargostar.workers.SyncWorkRequest;

public class CreateInvoiceFragment extends Fragment implements CreateInvoiceCallback, TariffCallback {
    private Context context;
    private Activity activity;
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

    /* content views */
    private EditText instructionsEditText;

    //provider
    private CardView firstProviderCard;
    private CardView secondProviderCard;
    private RadioButton firstProviderRadioBtn;
    private RadioButton secondProviderRadioBtn;
    private ImageView firstProviderImageView;
    private ImageView secondProviderImageView;

    //package type
    private RadioGroup packageTypeRadioGroup;
    private RadioButton docTypeRadioBtn;
    private RadioButton boxTypeRadioBtn;

    //cargo
    private EditText cargoDescriptionEditText;
    private EditText cargoNameEditText;
    private EditText cargoPriceEditText;

    private Spinner packagingTypeSpinner;
    private ArrayAdapter<PackagingType> packagingTypeArrayAdapter;

    private TextView lengthTextView;
    private TextView widthTextView;
    private TextView heightTextView;
    private EditText weightEditText;
    private EditText lengthEditText;
    private EditText widthEditText;
    private EditText heightEditText;
    private Button addBtn;

    private ProgressBar progressBar;

    //calculation
    private TextView totalQuantityTextView;
    private TextView totalWeightTextView;
    private TextView totalDimensionsTextView;

    private Button calculateBtn;

    private RadioGroup paymentMethodRadioGroup;
    private RadioButton cashRadioBtn;
    private RadioButton terminalRadioBtn;
    private RadioButton transferRadioBtn;
    private RadioButton corporateRadioBtn;

    private RecyclerView tariffPriceRecyclerView;
    private TariffPriceRadioAdapter tariffPriceRadioAdapter;

    private Button createInvoiceBtn;

    /* location data */
    private Country senderCountry;
    private Country recipientCountry;
    private Country payerCountry;

    private City senderCity;
    private City recipientCity;
    private City payerCity;

    /* show current cargoList */
    private ConsignmentAdapter consignmentAdapter;
    private RecyclerView itemRecyclerView;

    /* view model */
    private CourierViewModel courierViewModel;
    private CreateInvoiceViewModel createInvoiceViewModel;
    private CalculatorViewModel calculatorViewModel;

    /* main content views */

    /* sender data */
    private EditText senderEmailEditText;
    private EditText senderSignatureEditText;
    private ImageView senderSignatureResultImageView;
    private ImageView senderSignatureImageView;
    private EditText senderFirstNameEditText;
    private EditText senderLastNameEditText;
    private EditText senderMiddleNameEditText;
    private EditText senderPhoneEditText;
    private EditText senderAddressEditText;
    private EditText senderZipEditText;
    private EditText senderCompanyEditText;
    private EditText senderTntEditText;
    private EditText senderFedexEditText;

    /* recipient data */
    private EditText recipientEmailEditText;
    private EditText recipientFirstNameEditText;
    private EditText recipientLastNameEditText;
    private EditText recipientMiddleNameEditText;
    private EditText recipientAddressEditText;
    private EditText recipientZipEditText;
    private EditText recipientPhoneEditText;
    private EditText recipientCargoEditText;
    private EditText recipientTntEditText;
    private EditText recipientFedexEditText;
    private EditText recipientCompanyEditText;

    /* payer data */
    private EditText payerEmailEditText;
    private EditText payerFirstNameEditText;
    private EditText payerLastNameEditText;
    private EditText payerMiddleNameEditText;
    private EditText payerAddressEditText;
    private EditText payerZipEditText;
    private EditText payerPhoneEditText;
    private EditText discountEditText;

    private EditText payerCargoEditText;
    private EditText payerTntEditText;
    private EditText payerTntTaxIdEditText;
    private EditText payerFedexEditText;
    private EditText payerFedexTaxIdEditText;
    private EditText payerInnEditText;
    private EditText payerCompanyEditText;
    private EditText checkingAccountEditText;
    private EditText bankEditText;
    private EditText registrationCodeEditText;
    private EditText mfoEditText;
    private EditText okedEditText;

    private Spinner senderCountrySpinner;
    private Spinner recipientCountrySpinner;
    private Spinner payerCountrySpinner;

    private Spinner senderCitySpinner;
    private Spinner recipientCitySpinner;
    private Spinner payerCitySpinner;

    private Spinner recipientAddressBookSpinner;
    private Spinner payerAddressBookSpinner;

    private RadioGroup payerIsSomeoneRadioGroup;
    private RadioButton senderIsPayerRadioBtn;
    private RadioButton recipientIsPayerRadioBtn;

    private ArrayAdapter<Country> countryArrayAdapter;
    private ArrayAdapter<City> senderCityArrayAdapter;
    private ArrayAdapter<City> recipientCityArrayAdapter;
    private ArrayAdapter<City> payerCityArrayAdapter;
    private ArrayAdapter<AddressBook> recipientAddressBookArrayAdapter;
    private ArrayAdapter<AddressBook> payerAddressBookArrayAdapter;
    private List<AddressBook> addressBookEntries;

    /* flags to handle UI initialization */
    private static volatile boolean isSenderInitialPick;
    private static volatile boolean addressBookInitialized;
    private static volatile boolean isRecipientInitialPick;
    private static volatile boolean isPayerInitialPick;

    private static volatile boolean isSenderCityInitialPick;
    private static volatile boolean isRecipientCityInitialPick;
    private static volatile boolean isPayerCityInitialPick;

    private static volatile boolean senderCountryIsSet;
    private static volatile boolean recipientCountryIsSet;
    private static volatile boolean payerCountryIsSet;

    /* if invoice was created before */
    private static volatile int requestKey = -1;
    private static volatile long requestId = -1L;
    private static volatile long invoiceId = -1L;
    private static volatile long providerId = -1L;
    private static volatile long courierId = -1L;
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

    /* selected items */
    private Long selectedCountryId = null;
    private Provider selectedProvider = null;
    private List<Packaging> tariffList = null;
    private List<Long> selectedPackagingIdList = new ArrayList<>();
    private List<ZoneSettings> selectedZoneSettingsList = null;
    private Vat selectedVat = null;

    private static List<Consignment> consignmentList;
    private static final List<TariffPrice> tariffPriceList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestKey = -1;
        requestId = -1;
        invoiceId = -1;
        invoiceNumber = null;
        tariffId = -1;
        courierId = -1;
        providerId = -1;
        senderId = -1;
        recipientId = -1;
        payerId = -1;
        senderCountryId = -1;
        recipientCountryId = -1;
        payerCountryId = -1;

        price = 0;
        deliveryType = 0;
        comment = null;
        consignmentQuantity = 0;

        senderEmail = null;
        senderSignature = null;
        senderFirstName = null;
        senderLastName = null;
        senderMiddleName = null;
        senderPhone = null;
        senderAddress = null;
        senderZip = null;
        senderCompany = null;
        senderCargo = null;
        senderTnt = null;
        senderFedex = null;
        discount = 0;

        recipientEmail = null;
        recipientFirstName = null;
        recipientLastName = null;
        recipientMiddleName = null;
        recipientPhone = null;
        recipientAddress = null;
        recipientZip = null;
        recipientCompany = null;
        recipientCargo = null;
        recipientTnt = null;
        recipientFedex = null;

        payerEmail = null;
        payerFirstName = null;
        payerLastName = null;
        payerMiddleName = null;
        payerPhone = null;
        payerAddress = null;
        payerZip = null;
        payerCompany = null;
        payerCargo = null;
        payerTnt = null;
        payerFedex = null;
        payerInn = null;
        payerBank = null;
        payerCheckingAccount = null;
        payerRegistrationCode = null;
        payerMfo = null;
        payerOked = null;

        context = getContext();
        activity = getActivity();

        addressBookEntries = new ArrayList<>();
        consignmentList = new ArrayList<>();

        if (getArguments() != null) {
            requestKey = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getRequestKey();
            requestId = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getRequestId();
            invoiceId = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getInvoiceId();
            invoiceNumber = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getInvoiceNumber();
            tariffId = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getTariffId();
            courierId = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getCourierId();
            providerId = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getProviderId();
            price = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getPrice();

            deliveryType = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getDeliveryType();
            comment = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getComment();

            senderId = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderId();
            senderEmail = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderEmail();
            senderSignature = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderSignature();
            senderFirstName = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderFirstName();
            senderLastName = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderLastName();
            senderMiddleName = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderMiddleName();
            senderPhone = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderPhone();
            senderAddress = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderAddress();
            senderCountryId = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderCountryId();
            senderCityId = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderCityId();
            senderZip = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderZip();
            senderCompany = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderCompany();
            senderCargo = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderCargo();
            senderTnt = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderTnt();
            senderFedex = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSenderFedex();
            discount = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getDiscount();

            recipientId = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getRecipientId();
            recipientEmail = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getRecipientEmail();
            recipientFirstName = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getRecipientFirstName();
            recipientLastName = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getRecipientLastName();
            recipientMiddleName = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getRecipientMiddleName();
            recipientPhone = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getRecipientPhone();
            recipientAddress = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getRecipientAddress();
            recipientCountryId = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getRecipientCountryId();
            recipientCityId = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getRecipientCityId();
            recipientZip = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getRecipientZip();
            recipientCompany = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getRecipientCompany();
            recipientCargo = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getRecipientCargo();
            recipientTnt = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getRecipientTnt();
            recipientFedex = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getRecipientFedex();

            payerId = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getPayerId();
            payerEmail = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getPayerEmail();
            payerFirstName = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getPayerFirstName();
            payerLastName = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getPayerLastName();
            payerMiddleName = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getPayerMiddleName();
            payerPhone = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getPayerPhone();
            payerAddress = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getPayerAddress();
            payerCountryId = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getPayerCountryId();
            payerCityId = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getPayerCityId();
            payerZip = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getPayerZip();
            payerCompany = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getPayerCompany();
            payerCargo = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getPayerCargo();
            payerTnt = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getPayerTnt();
            payerFedex = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getPayerFedex();
            payerInn = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getPayerInn();
            payerBank = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getPayerBank();
            payerCheckingAccount = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getPayerCheckingAccount();
            payerRegistrationCode = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getPayerRegistrationCode();
            payerMfo = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getPayerMfo();
            payerOked = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getPayerOked();
            consignmentQuantity = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getConsignmentQuantity();

            if (consignmentQuantity > 0) {
                final String serializedConsignmentList = CreateInvoiceFragmentArgs.fromBundle(getArguments()).getSerializedConsignmentList();
                final List<Consignment> consignments = Serializer.deserializeConsignmentList(serializedConsignmentList);

                if (consignments != null) {
                    for (final Consignment consignment : consignments) {
                        consignment.setPackagingType(consignment.getPackagingId() != null ? String.valueOf(consignment.getPackagingId()) : null);
                    }
                    consignmentList = consignments;
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_create_invoice, container, false);

        isSenderInitialPick = true;
        isRecipientInitialPick = true;
        isPayerInitialPick = true;
        addressBookInitialized = false;

        initUI(root);

        if (requestKey == IntentConstants.REQUEST_EDIT_INVOICE) {
            createInvoiceBtn.setText(R.string.update_invoice);
            updateUI();
        }
        else {
            createInvoiceBtn.setText(R.string.create_invoice);
        }

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (requestKey == IntentConstants.REQUEST_EDIT_INVOICE) {
            isSenderCityInitialPick = false;
            isRecipientCityInitialPick = false;
            isPayerCityInitialPick = false;
        }
        else {
            isSenderCityInitialPick = true;
            isRecipientCityInitialPick = true;
            isPayerCityInitialPick = true;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        courierViewModel = new ViewModelProvider(this).get(CourierViewModel.class);
        createInvoiceViewModel = new ViewModelProvider(this).get(CreateInvoiceViewModel.class);
        calculatorViewModel = new ViewModelProvider(this).get(CalculatorViewModel.class);

        /* header view model */
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

        /* location data view model */
        createInvoiceViewModel.getCountryList().observe(getViewLifecycleOwner(), countryList -> {
            Log.i(TAG, "countryListSize: " + countryList.size());
            setCountryList(countryList);
        });

        createInvoiceViewModel.getSenderCityList().observe(getViewLifecycleOwner(), this::setSenderCityList);

        createInvoiceViewModel.getRecipientCityList().observe(getViewLifecycleOwner(), this::setRecipientCityList);

        createInvoiceViewModel.getPayerCityList().observe(getViewLifecycleOwner(), this::setPayerCityList);

        createInvoiceViewModel.getSenderAddressBook().observe(getViewLifecycleOwner(), senderAddressBook -> {
            if (senderAddressBook != null) {
                initAddressBook(senderAddressBook);
            }
        });

        createInvoiceViewModel.getSender().observe(getViewLifecycleOwner(), sender -> {
            if (sender != null) {
                addressBookInitialized = true;
                createInvoiceViewModel.setSenderUserId(sender.getUserId());
                updateSenderData(sender);
                return;
            }
            addressBookInitialized = false;
        });

        /* calculator data view model */

        if (requestKey == IntentConstants.REQUEST_EDIT_INVOICE) {
            calculatorViewModel.setProviderId(providerId);
        }

        calculatorViewModel.getProvider().observe(getViewLifecycleOwner(), provider -> {
            if (provider != null) {
                selectedProvider = provider;
            }
        });

        calculatorViewModel.getType().observe(getViewLifecycleOwner(), type -> {
            if (type != null) {
                calculatorViewModel.setTypePackageIdList(type, selectedPackagingIdList);
            }
        });

        calculatorViewModel.getPackagingIds().observe(getViewLifecycleOwner(), packagingIds -> {
            selectedPackagingIdList = packagingIds;

            if (packageTypeRadioGroup.getCheckedRadioButtonId() == docTypeRadioBtn.getId()) {
                calculatorViewModel.setTypePackageIdList(1, packagingIds);
            }
            else if (packageTypeRadioGroup.getCheckedRadioButtonId() == boxTypeRadioBtn.getId()) {
                calculatorViewModel.setTypePackageIdList(2, packagingIds);
            }
        });

        calculatorViewModel.getPackagingTypeList().observe(getViewLifecycleOwner(), packagingTypeList -> {
            packagingTypeArrayAdapter.clear();
            packagingTypeArrayAdapter.addAll(packagingTypeList);
        });

        calculatorViewModel.getZoneList().observe(getViewLifecycleOwner(), zoneList -> {
            if (zoneList != null && !zoneList.isEmpty()) {

                final List<Long> zoneIds = new ArrayList<>();

                for (final Zone zone : zoneList) {
                    zoneIds.add(zone.getId());
                }
                calculatorViewModel.setZoneIds(zoneIds);
            }
        });

        calculatorViewModel.getZoneSettingsList().observe(getViewLifecycleOwner(), zoneSettingsList -> {
            selectedZoneSettingsList = zoneSettingsList;
        });

        calculatorViewModel.getVat().observe(getViewLifecycleOwner(), vat -> {
            selectedVat = vat;
        });

        calculatorViewModel.getTariffList().observe(getViewLifecycleOwner(), packagingList -> {
            tariffList = packagingList;
        });

        //update UI
        createInvoiceViewModel.getInvoice().observe(getViewLifecycleOwner(), invoice -> {
            if (invoice != null) {
                createInvoiceViewModel.setTariffId(invoice.getTariffId());
                createInvoiceViewModel.setRecipientId(invoice.getRecipientId());
                createInvoiceViewModel.setPayerId(invoice.getPayerId());
            }
        });

        createInvoiceViewModel.getTransportation().observe(getViewLifecycleOwner(), transportation -> {
            if (transportation != null) {
                instructionsEditText.setText(transportation.getInstructions());
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* listeners */
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

        //content views
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

        senderSignatureImageView.setOnClickListener(v -> {
            startActivityForResult(new Intent(context, SignatureActivity.class), IntentConstants.REQUEST_SENDER_SIGNATURE);
        });

        addBtn.setOnClickListener(v -> {
            addCargoToInvoice();
        });

        calculateBtn.setOnClickListener(v -> {
            calculateTotalPrice();
        });

        createInvoiceBtn.setOnClickListener(v -> {
            createInvoice();
        });

        /* providers */
        firstProviderCard.setOnClickListener(v -> {
            firstProviderRadioBtn.setChecked(true);
        });

        secondProviderCard.setOnClickListener(v -> {
            secondProviderRadioBtn.setChecked(true);
        });

        /* spinners */
        senderCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //sender country
                senderCountry = (Country) parent.getSelectedItem();
                createInvoiceViewModel.setSenderCountryId(senderCountry.getId());
                calculatorViewModel.setSrcCountryId(senderCountry.getId());

                final TextView itemTextView = (TextView) view;
                if (itemTextView != null) {
                    if (position < parent.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        senderCountrySpinner.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
                //country & null = hide all
                if (recipientCountry == null) {
                    selectedCountryId = null;
                    calculatorViewModel.setCountryIdProviderId(selectedCountryId, selectedProvider != null ? selectedProvider.getId() : null);

                    firstProviderRadioBtn.setText(null);
                    secondProviderRadioBtn.setText(null);

                    firstProviderRadioBtn.setVisibility(View.INVISIBLE);
                    firstProviderImageView.setVisibility(View.INVISIBLE);
                    firstProviderCard.setVisibility(View.INVISIBLE);

                    secondProviderRadioBtn.setVisibility(View.INVISIBLE);
                    secondProviderImageView.setVisibility(View.INVISIBLE);
                    secondProviderCard.setVisibility(View.INVISIBLE);

                    return;
                }
                if (!TextUtils.isEmpty(senderCountry.getNameEn()) && senderCountry.getNameEn().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                    //uzbekistan -> uzbekistan = cargo only
                    if (!TextUtils.isEmpty(recipientCountry.getNameEn()) && recipientCountry.getNameEn().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                        selectedCountryId = senderCountry.getId();
                        calculatorViewModel.setCountryIdProviderId(selectedCountryId, selectedProvider != null ? selectedProvider.getId() : null);

                        firstProviderImageView.setImageResource(R.drawable.logo_cargo_calc);
                        firstProviderRadioBtn.setText(R.string.cargostar);
                        secondProviderRadioBtn.setText(null);

                        firstProviderRadioBtn.setVisibility(View.VISIBLE);
                        firstProviderImageView.setVisibility(View.VISIBLE);
                        firstProviderCard.setVisibility(View.VISIBLE);

                        secondProviderRadioBtn.setVisibility(View.INVISIBLE);
                        secondProviderImageView.setVisibility(View.INVISIBLE);
                        secondProviderCard.setVisibility(View.INVISIBLE);

                        return;
                    }
                    //uzbekistan -> other = fedex & tnt (export)
                    selectedCountryId = recipientCountry.getId();
                    calculatorViewModel.setCountryIdProviderId(selectedCountryId, selectedProvider != null ? selectedProvider.getId() : null);

                    firstProviderRadioBtn.setText(R.string.tnt);
                    secondProviderRadioBtn.setText(R.string.fedex);

                    firstProviderImageView.setImageResource(R.drawable.logo_tnt_cacl);
                    firstProviderRadioBtn.setVisibility(View.VISIBLE);
                    firstProviderImageView.setVisibility(View.VISIBLE);
                    firstProviderCard.setVisibility(View.VISIBLE);

                    secondProviderImageView.setImageResource(R.drawable.logo_fedex_calc);
                    secondProviderRadioBtn.setVisibility(View.VISIBLE);
                    secondProviderImageView.setVisibility(View.VISIBLE);
                    secondProviderCard.setVisibility(View.VISIBLE);
                    return;
                }
                //other -> uzbekistan = tnt only
                if (!TextUtils.isEmpty(recipientCountry.getNameEn()) && recipientCountry.getNameEn().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                    selectedCountryId = senderCountry.getId();
                    calculatorViewModel.setCountryIdProviderId(selectedCountryId, selectedProvider != null ? selectedProvider.getId() : null);

                    firstProviderImageView.setImageResource(R.drawable.logo_tnt_cacl);

                    firstProviderRadioBtn.setText(R.string.tnt);
                    secondProviderRadioBtn.setText(null);

                    firstProviderRadioBtn.setVisibility(View.VISIBLE);
                    firstProviderImageView.setVisibility(View.VISIBLE);
                    firstProviderCard.setVisibility(View.VISIBLE);

                    secondProviderRadioBtn.setVisibility(View.INVISIBLE);
                    secondProviderImageView.setVisibility(View.INVISIBLE);
                    secondProviderCard.setVisibility(View.INVISIBLE);
                    return;
                }
                selectedCountryId = null;
                calculatorViewModel.setCountryIdProviderId(selectedCountryId, selectedProvider != null ? selectedProvider.getId() : null);

                firstProviderRadioBtn.setText(null);
                secondProviderRadioBtn.setText(null);

                firstProviderRadioBtn.setVisibility(View.INVISIBLE);
                firstProviderImageView.setVisibility(View.INVISIBLE);
                firstProviderCard.setVisibility(View.INVISIBLE);

                secondProviderRadioBtn.setVisibility(View.INVISIBLE);
                secondProviderImageView.setVisibility(View.INVISIBLE);
                secondProviderCard.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        recipientCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //recipient country
                recipientCountry = (Country) parent.getSelectedItem();
                createInvoiceViewModel.setRecipientCountryId(recipientCountry.getId());
                calculatorViewModel.setDestCountryId(recipientCountry.getId());

                final TextView itemTextView = (TextView) view;
                if (itemTextView != null) {
                    if (position < parent.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        recipientCountrySpinner.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
                //country & null = hide all
                if (senderCountry == null) {
                    selectedCountryId = null;
                    calculatorViewModel.setCountryIdProviderId(selectedCountryId, selectedProvider != null ? selectedProvider.getId() : null);

                    firstProviderRadioBtn.setText(null);
                    secondProviderRadioBtn.setText(null);

                    firstProviderRadioBtn.setVisibility(View.INVISIBLE);
                    firstProviderImageView.setVisibility(View.INVISIBLE);
                    firstProviderCard.setVisibility(View.INVISIBLE);

                    secondProviderRadioBtn.setVisibility(View.INVISIBLE);
                    secondProviderImageView.setVisibility(View.INVISIBLE);
                    secondProviderCard.setVisibility(View.INVISIBLE);
                    return;
                }

                if (!TextUtils.isEmpty(senderCountry.getNameEn()) && senderCountry.getNameEn().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                    //uzbekistan -> uzbekistan = cargo only
                    if (!TextUtils.isEmpty(recipientCountry.getNameEn()) && recipientCountry.getNameEn().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                        selectedCountryId = recipientCountry.getId();
                        calculatorViewModel.setCountryIdProviderId(selectedCountryId, selectedProvider != null ? selectedProvider.getId() : null);

                        firstProviderImageView.setImageResource(R.drawable.logo_cargo_calc);

                        firstProviderRadioBtn.setText(R.string.cargostar);
                        secondProviderRadioBtn.setText(null);

                        firstProviderRadioBtn.setVisibility(View.VISIBLE);
                        firstProviderImageView.setVisibility(View.VISIBLE);
                        firstProviderCard.setVisibility(View.VISIBLE);

                        secondProviderRadioBtn.setVisibility(View.INVISIBLE);
                        secondProviderImageView.setVisibility(View.INVISIBLE);
                        secondProviderCard.setVisibility(View.INVISIBLE);
                        return;
                    }
                    //uzbekistan -> other = fedex & tnt (export)
                    selectedCountryId = recipientCountry.getId();
                    calculatorViewModel.setCountryIdProviderId(selectedCountryId, selectedProvider != null ? selectedProvider.getId() : null);

                    firstProviderImageView.setImageResource(R.drawable.logo_tnt_cacl);

                    firstProviderRadioBtn.setText(R.string.tnt);
                    secondProviderRadioBtn.setText(R.string.fedex);

                    firstProviderRadioBtn.setVisibility(View.VISIBLE);
                    firstProviderCard.setVisibility(View.VISIBLE);
                    firstProviderImageView.setVisibility(View.VISIBLE);

                    secondProviderImageView.setImageResource(R.drawable.logo_fedex_calc);
                    secondProviderRadioBtn.setVisibility(View.VISIBLE);
                    secondProviderImageView.setVisibility(View.VISIBLE);
                    secondProviderCard.setVisibility(View.VISIBLE);

                    return;
                }
                //other -> uzbekistan = tnt only
                if (!TextUtils.isEmpty(recipientCountry.getNameEn()) && recipientCountry.getNameEn().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                    selectedCountryId = senderCountry.getId();
                    calculatorViewModel.setCountryIdProviderId(selectedCountryId, selectedProvider != null ? selectedProvider.getId() : null);

                    firstProviderImageView.setImageResource(R.drawable.logo_tnt_cacl);

                    firstProviderRadioBtn.setText(R.string.tnt);
                    secondProviderRadioBtn.setText(null);

                    firstProviderRadioBtn.setVisibility(View.VISIBLE);
                    firstProviderImageView.setVisibility(View.VISIBLE);
                    firstProviderCard.setVisibility(View.VISIBLE);

                    secondProviderRadioBtn.setVisibility(View.INVISIBLE);
                    secondProviderImageView.setVisibility(View.INVISIBLE);
                    secondProviderCard.setVisibility(View.INVISIBLE);
                    return;
                }
                selectedCountryId = null;
                calculatorViewModel.setCountryIdProviderId(selectedCountryId, selectedProvider != null ? selectedProvider.getId() : null);

                firstProviderRadioBtn.setText(null);
                secondProviderRadioBtn.setText(null);

                firstProviderRadioBtn.setVisibility(View.INVISIBLE);
                firstProviderImageView.setVisibility(View.INVISIBLE);
                firstProviderCard.setVisibility(View.INVISIBLE);

                secondProviderRadioBtn.setVisibility(View.INVISIBLE);
                secondProviderImageView.setVisibility(View.INVISIBLE);
                secondProviderCard.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        payerCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //payer
                final Country country = (Country) parent.getSelectedItem();
                payerCountry = (Country) country;
                createInvoiceViewModel.setPayerCountryId(payerCountry.getId());

                final TextView itemTextView = (TextView) view;
                if (itemTextView != null) {
                    if (position < parent.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        payerCountrySpinner.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        senderCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                senderCity = (City) parent.getSelectedItem();

                final TextView itemTextView = (TextView) view;
                if (itemTextView != null) {
                    if (position < parent.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        senderCitySpinner.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        recipientCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                recipientCity = (City) parent.getSelectedItem();

                final TextView itemTextView = (TextView) view;
                if (itemTextView != null) {
                    if (position < parent.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        recipientCitySpinner.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        payerCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                payerCity = (City) parent.getSelectedItem();

                final TextView itemTextView = (TextView) view;
                if (itemTextView != null) {
                    if (position < parent.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        payerCitySpinner.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        recipientAddressBookSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isRecipientInitialPick) {
                    isRecipientInitialPick = false;
                    return;
                }

                final AddressBook entry = (AddressBook) parent.getSelectedItem();
                updateRecipientData(entry);

                final TextView itemTextView = (TextView) view;
                if (itemTextView != null) {
                    if (position < parent.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        recipientAddressBookSpinner.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        payerAddressBookSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isPayerInitialPick) {
                    isPayerInitialPick = false;
                    return;
                }

                final AddressBook entry = (AddressBook) parent.getSelectedItem();
                updatePayerData(entry);

                final TextView itemTextView = (TextView) view;
                if (itemTextView != null) {
                    if (position < parent.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        payerAddressBookSpinner.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /* choose provider */
        firstProviderRadioBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                secondProviderRadioBtn.setChecked(false);

                if (firstProviderRadioBtn.getText().equals(getString(R.string.cargostar))) {
                    calculatorViewModel.setProviderId(6L);
                    calculatorViewModel.setCountryIdProviderId(selectedCountryId, 6L);
                    return;
                }
                if (firstProviderRadioBtn.getText().equals(getString(R.string.tnt))) {
                    calculatorViewModel.setProviderId(5L);
                    calculatorViewModel.setCountryIdProviderId(selectedCountryId, 5L);
                }
            }
        });

        secondProviderRadioBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                firstProviderRadioBtn.setChecked(false);
                //only fedex case
                calculatorViewModel.setProviderId(4L);
                calculatorViewModel.setCountryIdProviderId(selectedCountryId, 4L);
            }
        });

        /* choose packaging type (1 / 2) */
        packageTypeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == docTypeRadioBtn.getId()) {
                //docs
                calculatorViewModel.setType(1);
                //make consignment fields invisible
                lengthTextView.setVisibility(View.INVISIBLE);
                widthTextView.setVisibility(View.INVISIBLE);
                heightTextView.setVisibility(View.INVISIBLE);
                lengthEditText.setVisibility(View.INVISIBLE);
                widthEditText.setVisibility(View.INVISIBLE);
                heightEditText.setVisibility(View.INVISIBLE);

                if (selectedPackagingIdList != null) {
                    calculatorViewModel.setTypePackageIdList(1, selectedPackagingIdList);
                }
            }
            else if (checkedId == boxTypeRadioBtn.getId()) {
                //boxes
                calculatorViewModel.setType(2);
                //make consignment fields visible
                lengthTextView.setVisibility(View.VISIBLE);
                widthTextView.setVisibility(View.VISIBLE);
                heightTextView.setVisibility(View.VISIBLE);
                lengthEditText.setVisibility(View.VISIBLE);
                widthEditText.setVisibility(View.VISIBLE);
                heightEditText.setVisibility(View.VISIBLE);

                if (selectedPackagingIdList != null) {
                    calculatorViewModel.setTypePackageIdList(2, selectedPackagingIdList);
                }
            }
            else {
                calculatorViewModel.setType(null);
            }
        });

        packagingTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView itemTextView = (TextView) view;

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        senderCountrySpinner.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final TextWatcher payerCompanyWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    cashRadioBtn.setVisibility(View.VISIBLE);
                    terminalRadioBtn.setVisibility(View.VISIBLE);
                    transferRadioBtn.setVisibility(View.INVISIBLE);
                    corporateRadioBtn.setVisibility(View.INVISIBLE);
                }
                else {
                    cashRadioBtn.setVisibility(View.INVISIBLE);
                    terminalRadioBtn.setVisibility(View.INVISIBLE);
                    transferRadioBtn.setVisibility(View.VISIBLE);
                    corporateRadioBtn.setVisibility(View.VISIBLE);
                }
            }
        };

        final TextWatcher senderEmailWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                if (s.length() >= 10) {
                    createInvoiceViewModel.setSenderEmail(s.toString());
                }
            }
        };

        payerCompanyEditText.addTextChangedListener(payerCompanyWatcher);
        senderEmailEditText.addTextChangedListener(senderEmailWatcher);

        payerIsSomeoneRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == senderIsPayerRadioBtn.getId()) {
                payerEmailEditText.setText(senderEmailEditText.getText().toString().trim());
                payerFirstNameEditText.setText(senderFirstNameEditText.getText().toString().trim());
                payerLastNameEditText.setText(senderLastNameEditText.getText().toString().trim());
                payerMiddleNameEditText.setText(senderMiddleNameEditText.getText().toString().trim());
                payerAddressEditText.setText(senderAddressEditText.getText().toString().trim());
                payerZipEditText.setText(senderZipEditText.getText().toString().trim());
                payerPhoneEditText.setText(senderPhoneEditText.getText().toString().trim());
                payerCargoEditText.setText(null);
                payerTntEditText.setText(senderTntEditText.getText().toString().trim());
                payerFedexEditText.setText(senderFedexEditText.getText().toString().trim());
                payerCompanyEditText.setText(senderCompanyEditText.getText().toString().trim());

                payerCountrySpinner.setSelection(senderCountrySpinner.getSelectedItemPosition());
                payerCityId = ((City) senderCitySpinner.getSelectedItem()).getId();
                return;
            }
            if (checkedId == recipientIsPayerRadioBtn.getId()) {
                payerEmailEditText.setText(recipientEmailEditText.getText().toString().trim());
                payerFirstNameEditText.setText(recipientFirstNameEditText.getText().toString().trim());
                payerLastNameEditText.setText(recipientLastNameEditText.getText().toString().trim());
                payerMiddleNameEditText.setText(recipientMiddleNameEditText.getText().toString().trim());
                payerAddressEditText.setText(recipientAddressEditText.getText().toString().trim());
                payerZipEditText.setText(recipientZipEditText.getText().toString().trim());
                payerPhoneEditText.setText(recipientPhoneEditText.getText().toString().trim());

                payerCargoEditText.setText(recipientCargoEditText.getText().toString().trim());
                payerTntEditText.setText(recipientTntEditText.getText().toString().trim());
                payerFedexEditText.setText(recipientFedexEditText.getText().toString().trim());
                payerCompanyEditText.setText(recipientCompanyEditText.getText().toString().trim());

                payerCountrySpinner.setSelection(recipientCountrySpinner.getSelectedItemPosition());
                payerCityId = ((City) recipientCitySpinner.getSelectedItem()).getId();
            }
        });
    }

    private void bindOnFocusChangeListeners() {
        senderEmailEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        senderSignatureEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        senderFirstNameEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        senderLastNameEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        senderMiddleNameEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        senderPhoneEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        senderAddressEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        senderCitySpinner.setOnFocusChangeListener(UiUtils::onFocusChanged);

        senderZipEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        senderCompanyEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        senderTntEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        senderFedexEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        /* recipient data */
        recipientEmailEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        recipientFirstNameEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        recipientLastNameEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        recipientMiddleNameEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        recipientCitySpinner.setOnFocusChangeListener(UiUtils::onFocusChanged);

        recipientAddressEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        recipientZipEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        recipientPhoneEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        recipientCargoEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        recipientTntEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        recipientFedexEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        recipientCompanyEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        /* payer data */
        payerEmailEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        payerFirstNameEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        payerLastNameEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        payerMiddleNameEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        payerAddressEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        payerZipEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        payerCitySpinner.setOnFocusChangeListener(UiUtils::onFocusChanged);

        payerPhoneEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        discountEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        payerCargoEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        payerTntEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        payerTntTaxIdEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        payerFedexEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        payerFedexTaxIdEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        payerInnEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        payerCompanyEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        checkingAccountEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        bankEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        registrationCodeEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        mfoEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        okedEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        instructionsEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        weightEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        lengthEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        widthEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        heightEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        cargoNameEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        cargoDescriptionEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);

        cargoPriceEditText.setOnFocusChangeListener(UiUtils::onFocusChanged);
    }

    private void initUI(final View root) {
        //header views
        profileImageView = activity.findViewById(R.id.profile_image_view);
        fullNameTextView = activity.findViewById(R.id.full_name_text_view);
        branchTextView = activity.findViewById(R.id.branch_text_view);
        courierIdTextView = activity.findViewById(R.id.courier_id_text_view);
        requestSearchEditText = activity.findViewById(R.id.search_edit_text);
        requestSearchImageView = activity.findViewById(R.id.search_btn);
        editImageView = activity.findViewById(R.id.edit_image_view);
        createUserImageView = activity.findViewById(R.id.create_user_image_view);
        calculatorImageView = activity.findViewById(R.id.calculator_image_view);
        notificationsImageView = activity.findViewById(R.id.notifications_image_view);
        badgeCounterTextView = activity.findViewById(R.id.badge_counter_text_view);

        //main content views
        progressBar = root.findViewById(R.id.progress_bar);

        /* sender data */
        senderEmailEditText = root.findViewById(R.id.sender_email_edit_text);
        senderSignatureEditText = root.findViewById(R.id.sender_signature_edit_text);
        senderSignatureResultImageView = root.findViewById(R.id.sender_signature_result_image_view);
        senderSignatureImageView = root.findViewById(R.id.sender_signature_image_view);
        senderFirstNameEditText = root.findViewById(R.id.sender_first_name_edit_text);
        senderLastNameEditText = root.findViewById(R.id.sender_last_name_edit_text);
        senderMiddleNameEditText = root.findViewById(R.id.sender_middle_name_edit_text);
        senderPhoneEditText = root.findViewById(R.id.sender_phone_edit_text);
        senderAddressEditText = root.findViewById(R.id.sender_address_edit_text);
        senderZipEditText = root.findViewById(R.id.sender_zip_edit_text);
        senderCompanyEditText = root.findViewById(R.id.sender_company_edit_text);
        senderTntEditText = root.findViewById(R.id.sender_tnt_edit_text);
        senderFedexEditText = root.findViewById(R.id.sender_fedex_edit_text);
        discountEditText = root.findViewById(R.id.sender_discount_edit_text);

        /* recipient data */
        recipientEmailEditText = root.findViewById(R.id.recipient_email_edit_text);
        recipientFirstNameEditText = root.findViewById(R.id.recipient_first_name_edit_text);
        recipientLastNameEditText = root.findViewById(R.id.recipient_last_name_edit_text);
        recipientMiddleNameEditText = root.findViewById(R.id.recipient_middle_name_edit_text);
        recipientAddressEditText = root.findViewById(R.id.recipient_address_edit_text);
        recipientZipEditText = root.findViewById(R.id.recipient_zip_edit_text);
        recipientPhoneEditText = root.findViewById(R.id.recipient_phone_edit_text);
        recipientCargoEditText = root.findViewById(R.id.recipient_cargo_edit_text);
        recipientTntEditText = root.findViewById(R.id.recipient_tnt_edit_text);
        recipientFedexEditText = root.findViewById(R.id.recipient_fedex_edit_text);
        recipientCompanyEditText = root.findViewById(R.id.recipient_company_edit_text);

        /* payer data */
        payerIsSomeoneRadioGroup = root.findViewById(R.id.payer_is_someone_radio_group);
        senderIsPayerRadioBtn = root.findViewById(R.id.payer_is_sender_radio_btn);
        recipientIsPayerRadioBtn = root.findViewById(R.id.payer_is_recipient_radio_btn);
        payerEmailEditText = root.findViewById(R.id.payer_email_edit_text);
        payerFirstNameEditText = root.findViewById(R.id.payer_first_name_edit_text);
        payerLastNameEditText = root.findViewById(R.id.payer_last_name_edit_text);
        payerMiddleNameEditText = root.findViewById(R.id.payer_middle_name_edit_text);
        payerAddressEditText = root.findViewById(R.id.payer_address_edit_text);
        payerZipEditText = root.findViewById(R.id.payer_zip_edit_text);
        payerPhoneEditText = root.findViewById(R.id.payer_phone_edit_text);

        payerCargoEditText = root.findViewById(R.id.payer_cargo_edit_text);
        payerTntEditText = root.findViewById(R.id.payer_tnt_edit_text);
        payerTntTaxIdEditText = root.findViewById(R.id.payer_tnt_tax_id_edit_text);
        payerFedexEditText = root.findViewById(R.id.payer_fedex_edit_text);
        payerFedexTaxIdEditText = root.findViewById(R.id.payer_fedex_tax_id_edit_text);
        payerInnEditText = root.findViewById(R.id.payer_inn_edit_text);
        payerCompanyEditText = root.findViewById(R.id.payer_company_edit_text);
        checkingAccountEditText = root.findViewById(R.id.checking_account_edit_text);
        bankEditText = root.findViewById(R.id.bank_edit_text);
        registrationCodeEditText = root.findViewById(R.id.registration_code_edit_text);
        mfoEditText = root.findViewById(R.id.mfo_edit_text);
        okedEditText = root.findViewById(R.id.oked_edit_text);

        //below recycler view
        totalQuantityTextView = root.findViewById(R.id.total_quantity_value_text_view);
        totalWeightTextView = root.findViewById(R.id.total_weight_value_text_view);
        totalDimensionsTextView = root.findViewById(R.id.total_dimensions_value_text_view);
        calculateBtn = root.findViewById(R.id.calculate_btn);

        instructionsEditText = root.findViewById(R.id.instructions_edit_text);

        //provider
        firstProviderCard = root.findViewById(R.id.first_card);
        secondProviderCard = root.findViewById(R.id.second_card);
        firstProviderRadioBtn = root.findViewById(R.id.first_card_radio_btn);
        secondProviderRadioBtn = root.findViewById(R.id.second_card_radio_btn);
        firstProviderImageView = root.findViewById(R.id.first_card_logo);
        secondProviderImageView = root.findViewById(R.id.second_card_logo);

        //package type
        packageTypeRadioGroup = root.findViewById(R.id.package_type_radio_group);
        docTypeRadioBtn = root.findViewById(R.id.doc_type_radio_btn);
        boxTypeRadioBtn = root.findViewById(R.id.box_type_radio_btn);

        //cargo
        cargoNameEditText = root.findViewById(R.id.cargo_name_edit_text);
        cargoPriceEditText = root.findViewById(R.id.cargo_price_edit_text);
        cargoDescriptionEditText = root.findViewById(R.id.cargo_description_edit_text);
        packagingTypeSpinner = root.findViewById(R.id.packaging_type_spinner);

        lengthTextView = root.findViewById(R.id.length_text_view);
        widthTextView = root.findViewById(R.id.width_text_view);
        heightTextView = root.findViewById(R.id.height_text_view);
        weightEditText = root.findViewById(R.id.weight_edit_text);
        lengthEditText = root.findViewById(R.id.length_edit_text);
        widthEditText = root.findViewById(R.id.width_edit_text);
        heightEditText = root.findViewById(R.id.height_edit_text);
        addBtn = root.findViewById(R.id.add_item_btn);

        //calculations
        paymentMethodRadioGroup = root.findViewById(R.id.payment_method_radio_group);
        cashRadioBtn = root.findViewById(R.id.cash_radio_btn);
        terminalRadioBtn = root.findViewById(R.id.terminal_radio_btn);
        transferRadioBtn = root.findViewById(R.id.transfer_radio_btn);
        corporateRadioBtn = root.findViewById(R.id.corporate_radio_btn);

        createInvoiceBtn = root.findViewById(R.id.create_receipt_btn);

        tariffPriceRecyclerView = root.findViewById(R.id.tariff_price_recycler_view);

        itemRecyclerView = root.findViewById(R.id.calculations_recycler_view);

        packagingTypeArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new ArrayList<>());
        packagingTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        packagingTypeSpinner.setAdapter(packagingTypeArrayAdapter);

        consignmentAdapter = new ConsignmentAdapter(context, consignmentList, this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        itemRecyclerView.setLayoutManager(layoutManager);
        itemRecyclerView.setAdapter(consignmentAdapter);
        consignmentAdapter.setItemList(consignmentList);
        consignmentAdapter.notifyDataSetChanged();

        tariffPriceRadioAdapter = new TariffPriceRadioAdapter(context, this);
        final LinearLayoutManager tariffPriceLayoutManager = new LinearLayoutManager(context);
        tariffPriceLayoutManager.setOrientation(RecyclerView.VERTICAL);
        tariffPriceRecyclerView.setLayoutManager(tariffPriceLayoutManager);
        tariffPriceRecyclerView.setAdapter(tariffPriceRadioAdapter);

        senderCountrySpinner = root.findViewById(R.id.sender_country_spinner);
        recipientCountrySpinner = root.findViewById(R.id.recipient_country_spinner);
        payerCountrySpinner = root.findViewById(R.id.payer_country_spinner);

        senderCitySpinner = root.findViewById(R.id.sender_city_spinner);
        recipientCitySpinner = root.findViewById(R.id.recipient_city_spinner);
        payerCitySpinner = root.findViewById(R.id.payer_city_spinner);

        recipientAddressBookSpinner = root.findViewById(R.id.recipient_address_book_spinner);
        payerAddressBookSpinner = root.findViewById(R.id.payer_address_book_spinner);

        countryArrayAdapter = new CustomArrayAdapter<>(context, android.R.layout.simple_spinner_item, new ArrayList<>());
        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        senderCityArrayAdapter = new CustomArrayAdapter<>(context, android.R.layout.simple_spinner_item, new ArrayList<>());
        senderCityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        recipientCityArrayAdapter = new CustomArrayAdapter<>(context, android.R.layout.simple_spinner_item, new ArrayList<>());
        recipientCityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        payerCityArrayAdapter = new CustomArrayAdapter<>(context, android.R.layout.simple_spinner_item, new ArrayList<>());
        payerCityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        recipientAddressBookArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, addressBookEntries);
        recipientAddressBookArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        payerAddressBookArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, addressBookEntries);
        payerAddressBookArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        senderCountrySpinner.setAdapter(countryArrayAdapter);
        recipientCountrySpinner.setAdapter(countryArrayAdapter);
        payerCountrySpinner.setAdapter(countryArrayAdapter);

        senderCitySpinner.setAdapter(senderCityArrayAdapter);
        recipientCitySpinner.setAdapter(recipientCityArrayAdapter);
        payerCitySpinner.setAdapter(payerCityArrayAdapter);

        recipientAddressBookSpinner.setAdapter(recipientAddressBookArrayAdapter);
        payerAddressBookSpinner.setAdapter(payerAddressBookArrayAdapter);

        bindOnFocusChangeListeners();
    }

    private void updateUI() {
        /* update sender data */
        if (!TextUtils.isEmpty(senderEmail)) {
            senderEmailEditText.setText(senderEmail);
            senderEmailEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(senderSignature)) {
            senderSignatureEditText.setText(senderSignature);
            senderSignatureEditText.setBackgroundResource(R.drawable.edit_text_active);
            senderSignatureResultImageView.setImageResource(R.drawable.ic_image_green);
        }
        else {
            senderSignatureResultImageView.setImageResource(R.drawable.ic_image_red);
        }
        senderSignatureResultImageView.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(senderFirstName)) {
            senderFirstNameEditText.setText(senderFirstName);
            senderFirstNameEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(senderLastName)) {
            senderLastNameEditText.setText(senderLastName);
            senderLastNameEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(senderMiddleName)) {
            senderMiddleNameEditText.setText(senderMiddleName);
            senderMiddleNameEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(senderPhone)) {
            senderPhoneEditText.setText(senderPhone);
            senderPhoneEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(senderAddress)) {
            senderAddressEditText.setText(senderAddress);
            senderAddressEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(senderZip)) {
            senderZipEditText.setText(senderZip);
            senderZipEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(senderCompany)) {
            senderCompanyEditText.setText(senderCompany);
            senderCompanyEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(senderTnt)) {
            senderTntEditText.setText(senderTnt);
            senderTntEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(senderFedex)) {
            senderFedexEditText.setText(senderFedex);
            senderFedexEditText.setBackgroundResource(R.drawable.edit_text_active);
        }

        /* update recipient data */
        if (!TextUtils.isEmpty(recipientEmail)) {
            recipientEmailEditText.setText(recipientEmail);
            recipientEmailEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(recipientFirstName)) {
            recipientFirstNameEditText.setText(recipientFirstName);
            recipientFirstNameEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(recipientLastName)) {
            recipientLastNameEditText.setText(recipientLastName);
            recipientLastNameEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(recipientMiddleName)) {
            recipientMiddleNameEditText.setText(recipientMiddleName);
            recipientMiddleNameEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(recipientAddress)) {
            recipientAddressEditText.setText(recipientAddress);
            recipientAddressEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(recipientZip)) {
            recipientZipEditText.setText(recipientZip);
            recipientZipEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(recipientPhone)) {
            recipientPhoneEditText.setText(recipientPhone);
            recipientPhoneEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(recipientCargo)) {
            recipientCargoEditText.setText(recipientCargo);
            recipientCargoEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(recipientTnt)) {
            recipientTntEditText.setText(recipientTnt);
            recipientTntEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(recipientFedex)) {
            recipientFedexEditText.setText(recipientFedex);
            recipientFedexEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(recipientCompany)) {
            recipientCompanyEditText.setText(recipientCompany);
            recipientCompanyEditText.setBackgroundResource(R.drawable.edit_text_active);
        }

        /* update payer data */
        if (!TextUtils.isEmpty(payerEmail)) {
            payerEmailEditText.setText(payerEmail);
            payerEmailEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(payerFirstName)) {
            payerFirstNameEditText.setText(payerFirstName);
            payerFirstNameEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(payerLastName)) {
            payerLastNameEditText.setText(payerLastName);
            payerLastNameEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(payerMiddleName)) {
            payerMiddleNameEditText.setText(payerMiddleName);
            payerMiddleNameEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(payerAddress)) {
            payerAddressEditText.setText(payerAddress);
            payerAddressEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(payerZip)) {
            payerZipEditText.setText(payerZip);
            payerZipEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(payerPhone)) {
            payerPhoneEditText.setText(payerPhone);
            payerPhoneEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(payerCargo)) {
            payerCargoEditText.setText(payerCargo);
            payerCargoEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(payerTnt)) {
            payerTntEditText.setText(payerTnt);
            payerTntEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(payerFedex)) {
            payerFedexEditText.setText(payerFedex);
            payerFedexEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(payerInn)) {
            payerInnEditText.setText(payerInn);
            payerInnEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(payerCompany)) {
            payerCompanyEditText.setText(payerCompany);
            payerCompanyEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(payerCheckingAccount)) {
            checkingAccountEditText.setText(payerCheckingAccount);
            checkingAccountEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(payerBank)) {
            bankEditText.setText(payerBank);
            bankEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(payerRegistrationCode)) {
            registrationCodeEditText.setText(payerRegistrationCode);
            registrationCodeEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(payerMfo)) {
            mfoEditText.setText(payerMfo);
            mfoEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (!TextUtils.isEmpty(payerOked)) {
            okedEditText.setText(payerOked);
            okedEditText.setBackgroundResource(R.drawable.edit_text_active);
        }

        /* update invoice data */
        if (!TextUtils.isEmpty(comment)) {
            instructionsEditText.setText(comment);
            instructionsEditText.setBackgroundResource(R.drawable.edit_text_active);
        }
        if (providerId == 4) {
            firstProviderRadioBtn.setChecked(false);
            secondProviderRadioBtn.setChecked(true);

            firstProviderRadioBtn.setText(R.string.tnt);
            secondProviderRadioBtn.setText(R.string.fedex);

            firstProviderImageView.setImageResource(R.drawable.logo_tnt_cacl);
            firstProviderRadioBtn.setVisibility(View.INVISIBLE);
            firstProviderImageView.setVisibility(View.INVISIBLE);
            firstProviderCard.setVisibility(View.INVISIBLE);

            secondProviderImageView.setImageResource(R.drawable.logo_fedex_calc);
            secondProviderRadioBtn.setVisibility(View.VISIBLE);
            secondProviderImageView.setVisibility(View.VISIBLE);
            secondProviderCard.setVisibility(View.VISIBLE);
        }
        else if (providerId == 5) {
            firstProviderRadioBtn.setChecked(true);
            secondProviderRadioBtn.setChecked(false);

            firstProviderRadioBtn.setText(R.string.tnt);
            secondProviderRadioBtn.setText(R.string.fedex);

            firstProviderImageView.setImageResource(R.drawable.logo_tnt_cacl);
            firstProviderRadioBtn.setVisibility(View.VISIBLE);
            firstProviderImageView.setVisibility(View.VISIBLE);
            firstProviderCard.setVisibility(View.VISIBLE);

            secondProviderImageView.setImageResource(R.drawable.logo_fedex_calc);
            secondProviderRadioBtn.setVisibility(View.INVISIBLE);
            secondProviderImageView.setVisibility(View.INVISIBLE);
            secondProviderCard.setVisibility(View.INVISIBLE);
        }
        else if (providerId == 6) {
            firstProviderRadioBtn.setChecked(true);
            secondProviderRadioBtn.setChecked(false);

            firstProviderImageView.setImageResource(R.drawable.logo_cargo_calc);
            firstProviderRadioBtn.setText(R.string.cargostar);
            secondProviderRadioBtn.setText(null);

            firstProviderRadioBtn.setVisibility(View.VISIBLE);
            firstProviderImageView.setVisibility(View.VISIBLE);
            firstProviderCard.setVisibility(View.VISIBLE);

            secondProviderRadioBtn.setVisibility(View.INVISIBLE);
            secondProviderImageView.setVisibility(View.INVISIBLE);
            secondProviderCard.setVisibility(View.INVISIBLE);
        }

        if (deliveryType == 1) {
            docTypeRadioBtn.setChecked(true);
        }
        else if (deliveryType == 2) {
            boxTypeRadioBtn.setChecked(true);
        }

        if (!TextUtils.isEmpty(payerCompany)) {
            cashRadioBtn.setVisibility(View.GONE);
            terminalRadioBtn.setVisibility(View.GONE);
            transferRadioBtn.setVisibility(View.VISIBLE);
            corporateRadioBtn.setVisibility(View.VISIBLE);
        }
        else {
            cashRadioBtn.setVisibility(View.VISIBLE);
            terminalRadioBtn.setVisibility(View.VISIBLE);
            transferRadioBtn.setVisibility(View.GONE);
            corporateRadioBtn.setVisibility(View.GONE);
        }
    }

    /* btns */
    private void addCargoToInvoice() {
        final PackagingType packagingType = (PackagingType) packagingTypeSpinner.getSelectedItem();
        final String name = cargoNameEditText.getText().toString().trim();
        final String description = cargoDescriptionEditText.getText().toString().trim();
        final String price = cargoPriceEditText.getText().toString().trim();
        final String weight = weightEditText.getText().toString().trim();
        final String length = lengthEditText.getText().toString().trim();
        final String width = widthEditText.getText().toString().trim();
        final String height = heightEditText.getText().toString().trim();
        final String dimensions = length + "x" + width + "x" + height;

        /* check for empty fields */
        if (packagingType == null) {
            Toast.makeText(context, "Выберите упаковку", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(weight)) {
            Toast.makeText(context, "Укажите вес", Toast.LENGTH_SHORT).show();
            return;
        }
        /* check for regex */
        if (!Regex.isFloatOrInt(weight)) {
            Toast.makeText(context, "Вес указан неверно", Toast.LENGTH_SHORT).show();
            return;
        }
        if (packageTypeRadioGroup.getCheckedRadioButtonId() == boxTypeRadioBtn.getId()) {
            if (TextUtils.isEmpty(length)) {
                Toast.makeText(context, "Укажите длину", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(width)) {
                Toast.makeText(context, "Укажите ширину", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(height)) {
                Toast.makeText(context, "Укажите высоту", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Regex.isFloatOrInt(length)) {
                Toast.makeText(context, "Длина указана неверно", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Regex.isFloatOrInt(width)) {
                Toast.makeText(context, "Ширина указана неверно", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Regex.isFloatOrInt(height)) {
                Toast.makeText(context, "Высота указана неверно", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(context, "Добавьте наименование вложения", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(description)) {
            Toast.makeText(context, "Добавьте описание вложения", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (packageTypeRadioGroup.getCheckedRadioButtonId() == boxTypeRadioBtn.getId()) {
            if (TextUtils.isEmpty(price)) {
                Toast.makeText(context, "Добавьте стоимость вложения", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        consignmentList.add(new Consignment(
                -1,
                requestId,
                packagingType.getId(),
                String.valueOf(packagingType.getId()),
                name,
                description,
                price,
                !TextUtils.isEmpty(length) ? Double.parseDouble(length) : 0,
                !TextUtils.isEmpty(width) ? Double.parseDouble(width) : 0,
                !TextUtils.isEmpty(height) ? Double.parseDouble(height) : 0,
                !TextUtils.isEmpty(weight) ? Double.parseDouble(weight) : 0,
                dimensions,
                null));
        consignmentAdapter.notifyItemInserted(consignmentList.size() - 1);
    }

    private void calculateTotalPrice() {
        if (packagingTypeSpinner.getSelectedItem() == null) {
            Toast.makeText(context, "Выберите упаковку", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!firstProviderRadioBtn.isChecked() && !secondProviderRadioBtn.isChecked()) {
            Toast.makeText(context, "Укажите поставщика услуг", Toast.LENGTH_SHORT).show();
            return;
        }
        if (packageTypeRadioGroup.getCheckedRadioButtonId() != docTypeRadioBtn.getId() && packageTypeRadioGroup.getCheckedRadioButtonId() != boxTypeRadioBtn.getId()) {
            Toast.makeText(context, "Укажите тип упаковки", Toast.LENGTH_SHORT).show();
            return;
        }
        if (consignmentList.isEmpty()) {
            Toast.makeText(context, "Добавьте хотя бы одну позицию", Toast.LENGTH_SHORT).show();
            return;
        }
        tariffPriceList.clear();

        int totalQuantity = consignmentList.size();
        double totalVolume = 0.0;
        double totalWeight = 0.0;

        for (final Consignment item : consignmentList) {
            totalWeight += item.getWeight();
            totalVolume += item.getLength() * item.getWidth() * item.getHeight();
        }

        if (selectedZoneSettingsList == null || selectedZoneSettingsList.isEmpty()) {
            Toast.makeText(context, "Зоны для подсчета не найдены", Toast.LENGTH_SHORT).show();
            return;
        }

        final Map<ZoneSettings, Packaging> zoneSettingsTariffMap = new HashMap<>();

        for (final ZoneSettings zoneSettings : selectedZoneSettingsList) {
            for (final Packaging packaging : tariffList) {
                if (packaging.getId() == zoneSettings.getPackagingId()) {
                    final int volumex = packaging.getVolumex();

                    if (volumex > 0) {
                        final double volumexWeight = totalVolume / volumex;

                        if (volumexWeight > totalWeight) {
                            totalWeight = volumexWeight;
                        }
                    }
                    if (totalWeight > zoneSettings.getWeightFrom() && totalWeight <= zoneSettings.getWeightTo()) {
                        zoneSettingsTariffMap.put(zoneSettings, packaging);
                    }
                }
            }
        }

        double totalPrice = 0.0;

        for (final ZoneSettings actualZoneSettings : zoneSettingsTariffMap.keySet()) {
            totalPrice = actualZoneSettings.getPriceFrom();
            final Packaging correspondingTariff = zoneSettingsTariffMap.get(actualZoneSettings);

            for (double i = actualZoneSettings.getWeightFrom(); i < totalWeight; i += actualZoneSettings.getWeightStep()) {
                if (actualZoneSettings.getWeightStep() <= 0) {
                    break;
                }
                totalPrice += actualZoneSettings.getPriceStep();
            }
            if (totalPrice > 0) {
                if (correspondingTariff != null) {
                    if (discount > 0 && discount < 100) {
                        totalPrice = totalPrice * (100 - discount) / 100;
                    }
                    
                    if (packageTypeRadioGroup.getCheckedRadioButtonId() == boxTypeRadioBtn.getId()) {
                        totalPrice += correspondingTariff.getParcelFee();
                    }
                }
                if (selectedProvider != null) {
                    totalPrice = totalPrice * (selectedProvider.getFuel() + 100) / 100;
                }
                if (selectedVat != null) {
                    totalPrice *= (selectedVat.getVat() + 100) / 100;
                }
            }
            if (correspondingTariff != null) {
                tariffPriceList.add(new TariffPrice(correspondingTariff.getName(), String.valueOf((int) totalPrice + 1), correspondingTariff.getId()));
            }
        }
        totalQuantityTextView.setText(String.valueOf(totalQuantity));
        totalWeightTextView.setText(String.valueOf(new BigDecimal(Double.toString(totalWeight)).setScale(2, RoundingMode.HALF_UP).doubleValue()));
        totalDimensionsTextView.setText(String.valueOf((int) totalVolume));

        tariffPriceRadioAdapter.setItemList(tariffPriceList);
    }

    private void createInvoice() {
        /* sender data */
        final String senderEmail = senderEmailEditText.getText().toString().trim();
        final String senderSignature = senderSignatureEditText.getText().toString().trim();
        final String senderFirstName = senderFirstNameEditText.getText().toString().trim();
        final String senderLastName = senderLastNameEditText.getText().toString().trim();
        final String senderMiddleName = senderMiddleNameEditText.getText().toString().trim();
        final String senderPhone = senderPhoneEditText.getText().toString().trim();
        final String senderAddress = senderAddressEditText.getText().toString().trim();
        final String senderCountryId = senderCountry != null ? String.valueOf(senderCountry.getId()) : null;
        final String senderCityId = senderCity != null ? String.valueOf(senderCity.getId()) : null;
        final String senderZip = senderZipEditText.getText().toString().trim();
        final String senderCompanyName = senderCompanyEditText.getText().toString().trim();
        final String senderTnt = senderTntEditText.getText().toString().trim();
        final String senderFedex = senderFedexEditText.getText().toString().trim();
        int senderType = 0;

        /* recipient data */
        final String recipientEmail = recipientEmailEditText.getText().toString().trim();
        final String recipientFirstName = recipientFirstNameEditText.getText().toString().trim();
        final String recipientLastName = recipientLastNameEditText.getText().toString().trim();
        final String recipientMiddleName = recipientMiddleNameEditText.getText().toString().trim();
        final String recipientAddress = recipientAddressEditText.getText().toString().trim();
        final String recipientCountryId = recipientCountry != null ? String.valueOf(recipientCountry.getId()) : null;
        final String recipientCityId = recipientCity != null ? String.valueOf(recipientCity.getId()) : null;
        final String recipientZip = recipientZipEditText.getText().toString().trim();
        final String recipientPhone = recipientPhoneEditText.getText().toString().trim();
        final String recipientCargo = recipientCargoEditText.getText().toString().trim();
        final String recipientTnt = recipientTntEditText.getText().toString().trim();
        final String recipientFedex = recipientFedexEditText.getText().toString().trim();
        final String recipientCompanyName = recipientCompanyEditText.getText().toString().trim();
        int recipientType = 0;

        /* payer data */
        final String payerEmail = payerEmailEditText.getText().toString().trim();
        final String payerFirstName = payerFirstNameEditText.getText().toString().trim();
        final String payerLastName = payerLastNameEditText.getText().toString().trim();
        final String payerMiddleName = payerMiddleNameEditText.getText().toString().trim();
        final String payerAddress = payerAddressEditText.getText().toString().trim();
        final String payerCountryId = payerCountry != null ? String.valueOf(payerCountry.getId()) : null;
        final String payerCityId = payerCity != null ? String.valueOf(payerCity.getId()) : null;
        final String payerZip = payerZipEditText.getText().toString().trim();
        final String payerPhone = payerPhoneEditText.getText().toString().trim();
        final String discount = discountEditText.getText().toString().trim();
        int payerType = 0;

        final String payerCargo = payerCargoEditText.getText().toString().trim();
        final String payerTnt = payerTntEditText.getText().toString().trim();
        final String payerTntTax = payerTntTaxIdEditText.getText().toString().trim();
        final String payerFedex = payerFedexEditText.getText().toString().trim();
        final String payerFedexTax = payerFedexTaxIdEditText.getText().toString().trim();
        final String payerInn = payerInnEditText.getText().toString().trim();
        final String payerCompany = payerCompanyEditText.getText().toString().trim();
        final String checkingAccount = checkingAccountEditText.getText().toString().trim();
        final String bank = bankEditText.getText().toString().trim();
        final String registrationCode = registrationCodeEditText.getText().toString().trim();
        final String mfo = mfoEditText.getText().toString().trim();
        final String oked = okedEditText.getText().toString().trim();

        final String instructions = instructionsEditText.getText().toString().trim();

        if (courierId <= 0) {
            Log.e(TAG, "courierId empty");
            Toast.makeText(context, "Пустой ID курьера", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senderEmail)) {
            Log.e(TAG, "sendInvoice(): senderEmail is empty");
            Toast.makeText(context, "Для создания заявки укажите email отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senderSignature)) {
            Log.e(TAG, "sendInvoice(): senderSignature is empty");
            Toast.makeText(context, "Для создания заявки добавьте подпись отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senderFirstName)) {
            Log.e(TAG, "senderFirstName empty");
            Toast.makeText(context, "Для создания заявки укажите имя отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senderLastName)) {
            Log.e(TAG, "senderLastName empty");
            Toast.makeText(context, "Для создания заявки укажите фамилию отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senderPhone)) {
            Log.e(TAG, "sendInvoice(): senderPhone is empty");
            Toast.makeText(context, "Для создания заявки укажите номер телефона отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senderCountryId)) {
            Log.e(TAG, "senderCountry empty");
            Toast.makeText(context, "Для создания заявки укажите страну отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senderAddress)) {
            Log.e(TAG, "senderAddress empty");
            Toast.makeText(context, "Для создания заявки укажите адрес отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senderZip)) {
            Log.e(TAG, "senderZip empty");
            Toast.makeText(context, "Для создания заявки укажите почтовый индекс отправителя", Toast.LENGTH_SHORT).show();
            return;
        }

        //recipient data
        if (TextUtils.isEmpty(recipientFirstName)) {
            Log.e(TAG, "sendInvoice(): recipientFirstName is empty");
            Toast.makeText(context, "Для создания заявки укажите имя получателя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(recipientLastName)) {
            Log.e(TAG, "sendInvoice(): recipientLastName is empty");
            Toast.makeText(context, "Для создания заявки укажите фамилию получателя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(recipientPhone)) {
            Log.e(TAG, "sendInvoice(): recipientPhone is empty");
            Toast.makeText(context, "Для создания заявки укажите номер телефона получателя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(recipientCountryId)) {
            Log.e(TAG, "sendInvoice(): recipientCountry is empty");
            Toast.makeText(context, "Для создания заявки укажите страну получателя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(recipientAddress)) {
            Log.e(TAG, "sendInvoice(): recipientAddress is empty");
            Toast.makeText(context, "Для создания заявки укажите адрес получателя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(recipientZip)) {
            Log.e(TAG, "sendInvoice(): recipientZip is empty");
            Toast.makeText(context, "Для создания заявки укажите почтовый индекс получателя", Toast.LENGTH_SHORT).show();
            return;
        }

        //payer data
        if (TextUtils.isEmpty(payerFirstName)) {
            Log.e(TAG, "sendInvoice(): payerFirstName is empty");
            Toast.makeText(context, "Для создания заявки укажите имя плательщика", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(payerLastName)) {
            Log.e(TAG, "sendInvoice(): payerLastName is empty");
            Toast.makeText(context, "Для создания заявки укажите фамилию плательщика", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(payerPhone)) {
            Log.e(TAG, "sendInvoice(): payerPhone is empty");
            Toast.makeText(context, "Для создания заявки укажите номер телефона плательщика", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(payerCountryId)) {
            Log.e(TAG, "sendInvoice(): payerCountry is empty");
            Toast.makeText(context, "Для создания заявки укажите страну плательщика", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(payerAddress)) {
            Log.e(TAG, "sendInvoice(): payerAddress is empty");
            Toast.makeText(context, "Для создания заявки укажите адрес плательщика", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(payerZip)) {
            Log.e(TAG, "sendInvoice(): payerZip is empty");
            Toast.makeText(context, "Для создания заявки укажите почтовый индекс плательщика", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(senderCompanyName)) {
            senderType = 2;
        }
        else {
            senderType = 1;
        }
        if (!TextUtils.isEmpty(recipientCompanyName)) {
            recipientType = 2;
        }
        else {
            recipientType = 1;
        }
        if (!TextUtils.isEmpty(payerCompany)) {
            payerType = 2;
        }
        else {
            payerType = 1;
        }
        if (!firstProviderRadioBtn.isChecked() && !secondProviderRadioBtn.isChecked()) {
            Toast.makeText(context, "Для создания заявки выберите поставщика услуг", Toast.LENGTH_SHORT).show();
            return;
        }

        int deliveryType = 0;
        int paymentMethod = 0;
        double totalWeight = 0.0;
        double totalVolume = 0.0;

        if (packageTypeRadioGroup.getCheckedRadioButtonId() != docTypeRadioBtn.getId()
                && packageTypeRadioGroup.getCheckedRadioButtonId() != boxTypeRadioBtn.getId()) {
            Toast.makeText(context, "Для создания заявки выберите тип посылки (Документы/Коробка)", Toast.LENGTH_SHORT).show();
            return;
        }
        if (packageTypeRadioGroup.getCheckedRadioButtonId() == docTypeRadioBtn.getId()) {
            deliveryType = 1;
        }
        if (packageTypeRadioGroup.getCheckedRadioButtonId() == boxTypeRadioBtn.getId()) {
            deliveryType = 2;
        }
        if (consignmentList.isEmpty()) {
            Toast.makeText(context, "Для создания заявки добавьте хотя бы 1 груз", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < consignmentList.size(); i++) {
            if (TextUtils.isEmpty(consignmentList.get(i).getQr())) {
                Toast.makeText(context, "Отсканируйте QR для " + i + " груза", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if (paymentMethodRadioGroup.getCheckedRadioButtonId() != cashRadioBtn.getId()
                && paymentMethodRadioGroup.getCheckedRadioButtonId() != terminalRadioBtn.getId()
                && paymentMethodRadioGroup.getCheckedRadioButtonId() != transferRadioBtn.getId()
                && paymentMethodRadioGroup.getCheckedRadioButtonId() != corporateRadioBtn.getId()) {
            Toast.makeText(context, "Для создания заявки выберите способ оплаты", Toast.LENGTH_SHORT).show();
            return;
        }
        if (paymentMethodRadioGroup.getCheckedRadioButtonId() == cashRadioBtn.getId()) {
            paymentMethod = 2;
        }
        else if (paymentMethodRadioGroup.getCheckedRadioButtonId() == terminalRadioBtn.getId()) {
            paymentMethod = 3;
        }
        else if (paymentMethodRadioGroup.getCheckedRadioButtonId() == transferRadioBtn.getId()) {
            paymentMethod = 4;
        }
        else if (paymentMethodRadioGroup.getCheckedRadioButtonId() == corporateRadioBtn.getId()) {
            paymentMethod = 5;
        }

        final String totalWeightText = totalWeightTextView.getText().toString().trim();
        final String totalVolumeText = totalDimensionsTextView.getText().toString().trim();

        try {
            if (!TextUtils.isEmpty(totalWeightText)) {
                totalWeight = Double.parseDouble(totalWeightText);
            }
            if (!TextUtils.isEmpty(totalVolumeText)) {
                totalVolume = Double.parseDouble(totalVolumeText);
            }
        }
        catch (Exception e) {
            Log.e(TAG, "createInvoice(): ", e);
            totalWeight = -1;
            totalVolume = -1;
        }

        if (totalWeight <= 0) {
            for (final Consignment consignment : consignmentList) {
                totalWeight += consignment.getWeight();
            }
        }
        if (totalVolume < 0) {
            for (final Consignment consignment : consignmentList) {
                totalVolume += consignment.getLength() * consignment.getHeight() * consignment.getWidth();
            }
        }

        final String serializedConsignmentList = new Gson().toJson(consignmentList);

        final UUID sendInvoiceWorkUUID = SyncWorkRequest.sendInvoice(
                context,
                requestId,
                invoiceId,
                courierId,
                !TextUtils.isEmpty(senderSignature) ? senderSignature : null,
                invoiceId > 0,
                senderEmail,
                senderTnt,
                senderFedex,
                senderCountryId,
                senderCityId,
                senderAddress,
                senderZip,
                senderFirstName,
                senderMiddleName,
                senderLastName,
                senderPhone,
                senderCompanyName,
                senderType,
                recipientEmail,
                recipientCargo,
                recipientTnt,
                recipientFedex,
                recipientCountryId,
                recipientCityId,
                recipientAddress,
                recipientZip,
                recipientFirstName,
                recipientMiddleName,
                recipientLastName,
                recipientPhone,
                recipientCompanyName,
                recipientType,
                payerEmail,
                payerCountryId,
                payerCityId,
                payerAddress,
                payerZip,
                payerFirstName,
                payerMiddleName,
                payerLastName,
                payerPhone,
                payerCargo,
                payerTnt,
                payerFedex,
                payerTntTax,
                payerFedexTax,
                payerCompany,
                payerType,
                !TextUtils.isEmpty(discount) ? Double.parseDouble(discount) : -1,
                payerInn,
                checkingAccount,
                bank,
                mfo,
                oked,
                registrationCode,
                instructions,
                selectedProvider.getId(),
                tariffPriceRadioAdapter.getSelectedPackagingId() <= 0 ? tariffId : tariffPriceRadioAdapter.getSelectedPackagingId(),
                deliveryType,
                paymentMethod,
                totalWeight,
                totalVolume,
                tariffPriceRadioAdapter.getSelectedPrice() == null ? String.valueOf(price) : tariffPriceRadioAdapter.getSelectedPrice(),
                serializedConsignmentList);

        WorkManager.getInstance(context).getWorkInfoByIdLiveData(sendInvoiceWorkUUID).observe(getViewLifecycleOwner(), workInfo -> {
            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                createInvoiceBtn.setEnabled(true);
                createInvoiceBtn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

                final String successMsg = invoiceId > 0 ? "Накладная успешно изменена" : "Накладная создана успешно";

                Toast.makeText(context, successMsg, Toast.LENGTH_SHORT).show();

                UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(R.id.action_createInvoiceFragment_to_mainFragment);

                return;
            }
            if (workInfo.getState() == WorkInfo.State.CANCELLED || workInfo.getState() == WorkInfo.State.FAILED) {
                createInvoiceBtn.setEnabled(true);
                createInvoiceBtn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);

                Log.e(TAG, "createInvoice(): ");
                Toast.makeText(context, "Произошла ошибка при создании накладной", Toast.LENGTH_SHORT).show();
                return;
            }
            if (workInfo.getState() == WorkInfo.State.BLOCKED || workInfo.getState() == WorkInfo.State.ENQUEUED) {
                Toast.makeText(context, "Кэширование накладной...", Toast.LENGTH_SHORT).show();
                createInvoiceBtn.setEnabled(true);
                createInvoiceBtn.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                return;
            }
            if (workInfo.getState() == WorkInfo.State.RUNNING) {
                createInvoiceBtn.setEnabled(false);
                createInvoiceBtn.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onDeleteItemClicked(final int position) {
        if (consignmentList.size() <= 0) {
            return;
        }
        consignmentList.remove(position);
        consignmentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onScanItemClicked(int position) {
        final Intent scanQrIntent = new Intent(context, ScanQrActivity.class);
        scanQrIntent.putExtra(Constants.KEY_QR_POSITION, position);
        startActivityForResult(scanQrIntent, IntentConstants.REQUEST_SCAN_QR_CARGO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IntentConstants.REQUEST_SCAN_QR_CARGO) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                final String qr = data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE);
                final int position = data.getIntExtra(Constants.KEY_QR_POSITION, -1);

                if (position >= 0) {
                    consignmentList.get(position).setQr(qr);
                    consignmentAdapter.notifyItemChanged(position);
                }
            }
            return;
        }
        if (requestCode == IntentConstants.REQUEST_SENDER_SIGNATURE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                senderSignatureEditText.setBackgroundResource(R.drawable.edit_text_locked);
                senderSignatureResultImageView.setImageResource(R.drawable.ic_image_red);
                senderSignatureResultImageView.setVisibility(View.VISIBLE);
                return;
            }
            if (resultCode == Activity.RESULT_OK && data != null) {
                senderSignatureEditText.setText(data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE));
                senderSignatureEditText.setBackgroundResource(R.drawable.edit_text_active);
                senderSignatureResultImageView.setImageResource(R.drawable.ic_image_green);
                senderSignatureResultImageView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initAddressBook(final List<AddressBook> addressBook) {
        if (addressBook != null) {
            recipientAddressBookArrayAdapter.clear();
            payerAddressBookArrayAdapter.clear();

            for (final AddressBook entry : addressBook) {
                recipientAddressBookArrayAdapter.add(entry);
                payerAddressBookArrayAdapter.add(entry);
            }
        }
    }

    private void updateSenderData(final Customer sender) {
        if (sender != null) {
            if (!TextUtils.isEmpty(sender.getSignatureUrl())) {
                senderSignatureEditText.setText(sender.getSignatureUrl());
                senderSignatureEditText.setBackgroundResource(R.drawable.edit_text_active);
                senderSignatureResultImageView.setImageResource(R.drawable.ic_image_green);
            }
            else {
                senderSignatureEditText.setBackgroundResource(R.drawable.edit_text_locked);
                senderSignatureResultImageView.setImageResource(R.drawable.ic_image_red);
            }
            senderSignatureResultImageView.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(sender.getFirstName())) {
                senderFirstNameEditText.setText(sender.getFirstName());
                senderFirstNameEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                senderFirstNameEditText.setText(null);
                senderFirstNameEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(sender.getLastName())) {
                senderLastNameEditText.setText(sender.getLastName());
                senderLastNameEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                senderLastNameEditText.setText(null);
                senderLastNameEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(sender.getMiddleName())) {
                senderMiddleNameEditText.setText(sender.getMiddleName());
                senderMiddleNameEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                senderMiddleNameEditText.setText(null);
                senderMiddleNameEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(sender.getPhone())) {
                senderPhoneEditText.setText(sender.getPhone());
                senderPhoneEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                senderPhoneEditText.setText(null);
                senderPhoneEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(sender.getAddress())) {
                senderAddressEditText.setText(sender.getAddress());
                senderAddressEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                senderAddressEditText.setText(null);
                senderAddressEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(sender.getZip())) {
                senderZipEditText.setText(sender.getZip());
                senderZipEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                senderZipEditText.setText(null);
                senderZipEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(sender.getCompany())) {
                senderCompanyEditText.setText(sender.getCompany());
                senderCompanyEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                senderCompanyEditText.setText(null);
                senderCompanyEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(sender.getTntAccountNumber())) {
                senderTntEditText.setText(sender.getTntAccountNumber());
                senderTntEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                senderTntEditText.setText(null);
                senderTntEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(sender.getFedexAccountNumber())) {
                senderFedexEditText.setText(sender.getFedexAccountNumber());
                senderFedexEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                senderFedexEditText.setText(null);
                senderFedexEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (sender.getDiscount() > 0) {
                discountEditText.setText(String.valueOf(sender.getDiscount()));
                discountEditText.setBackgroundResource(R.drawable.edit_text_active);
                discount = sender.getDiscount();
            }
            else {
                discountEditText.setText(null);
                discountEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (sender.getCountryId() != null) {
                senderCountryId = sender.getCountryId();
            }
            if (sender.getCityId() != null) {
                senderCityId = sender.getCityId();
            }
//                for (int i = 0; i < countryList.size(); i++) {
//                    if (countryList.get(i).getId() == senderCountryId) {
//                        senderCountrySpinner.setSelection(i);
//                        break;
//                    }
//                }
        }
    }

    private void updateRecipientData(final AddressBook recipient) {
        if (recipient != null) {
            if (!TextUtils.isEmpty(recipient.getEmail())) {
                recipientEmailEditText.setText(recipient.getEmail());
                recipientEmailEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                recipientEmailEditText.setText(null);
                recipientEmailEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(recipient.getFirstName())) {
                recipientFirstNameEditText.setText(recipient.getFirstName());
                recipientFirstNameEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                recipientFirstNameEditText.setText(null);
                recipientFirstNameEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(recipient.getLastName())) {
                recipientLastNameEditText.setText(recipient.getLastName());
                recipientLastNameEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                recipientLastNameEditText.setText(null);
                recipientLastNameEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(recipient.getMiddleName())) {
                recipientMiddleNameEditText.setText(recipient.getMiddleName());
                recipientMiddleNameEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                recipientMiddleNameEditText.setText(null);
                recipientMiddleNameEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(recipient.getAddress())) {
                recipientAddressEditText.setText(recipient.getAddress());
                recipientAddressEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                recipientAddressEditText.setText(null);
                recipientAddressEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(recipient.getZip())) {
                recipientZipEditText.setText(recipient.getZip());
                recipientZipEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                recipientZipEditText.setText(null);
                recipientZipEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(recipient.getPhone())) {
                recipientPhoneEditText.setText(recipient.getPhone());
                recipientPhoneEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                recipientPhoneEditText.setText(null);
                recipientPhoneEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(recipient.getCargostarAccountNumber())) {
                recipientCargoEditText.setText(recipient.getCargostarAccountNumber());
                recipientCargoEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                recipientCargoEditText.setText(null);
                recipientCargoEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(recipient.getTntAccountNumber())) {
                recipientTntEditText.setText(recipient.getTntAccountNumber());
                recipientTntEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                recipientTntEditText.setText(null);
                recipientTntEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(recipient.getFedexAccountNumber())) {
                recipientFedexEditText.setText(recipient.getFedexAccountNumber());
                recipientFedexEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                recipientFedexEditText.setText(null);
                recipientFedexEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(recipient.getCompany())) {
                recipientCompanyEditText.setText(recipient.getCompany());
                recipientCompanyEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                recipientCompanyEditText.setText(null);
                recipientCompanyEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (recipient.getCountryId() != null) {
                recipientCountryId = recipient.getCountryId();
            }
            if (recipient.getCityId() != null) {
                recipientCityId = recipient.getCityId();
            }
//                for (int i = 0; i < countryList.size(); i++) {
//                    if (countryList.get(i).getId() == recipientCountryId) {
//                        recipientCountrySpinner.setSelection(i);
//                        break;
//                    }
//                }
        }
    }

    private void updatePayerData(final AddressBook payer) {
        if (payer != null) {
            if (!TextUtils.isEmpty(payer.getEmail())) {
                payerEmailEditText.setText(payer.getEmail());
                payerEmailEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                payerEmailEditText.setText(null);
                payerEmailEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(payer.getFirstName())) {
                payerFirstNameEditText.setText(payer.getFirstName());
                payerFirstNameEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                payerFirstNameEditText.setText(null);
                payerFirstNameEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(payer.getLastName())) {
                payerLastNameEditText.setText(payer.getLastName());
                payerLastNameEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                payerLastNameEditText.setText(null);
                payerLastNameEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(payer.getMiddleName())) {
                payerMiddleNameEditText.setText(payer.getMiddleName());
                payerMiddleNameEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                payerMiddleNameEditText.setText(null);
                payerMiddleNameEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(payer.getAddress())) {
                payerAddressEditText.setText(payer.getAddress());
                payerAddressEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                payerAddressEditText.setText(null);
                payerAddressEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(payer.getZip())) {
                payerZipEditText.setText(payer.getZip());
                payerZipEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                payerZipEditText.setText(null);
                payerZipEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(payer.getPhone())) {
                payerPhoneEditText.setText(payer.getPhone());
                payerPhoneEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                payerPhoneEditText.setText(null);
                payerPhoneEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(payer.getCargostarAccountNumber())) {
                payerCargoEditText.setText(payer.getCargostarAccountNumber());
                payerCargoEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                payerCargoEditText.setText(null);
                payerCargoEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(payer.getTntAccountNumber())) {
                payerTntEditText.setText(payer.getTntAccountNumber());
                payerTntEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                payerTntEditText.setText(null);
                payerTntEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(payer.getFedexAccountNumber())) {
                payerFedexEditText.setText(payer.getFedexAccountNumber());
                payerFedexEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                payerFedexEditText.setText(null);
                payerFedexEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(payer.getInn())) {
                payerInnEditText.setText(payer.getInn());
                payerInnEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                payerInnEditText.setText(null);
                payerInnEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(payer.getCompany())) {
                payerCompanyEditText.setText(payer.getCompany());
                payerCompanyEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                payerCompanyEditText.setText(null);
                payerCompanyEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(payer.getCheckingAccount())) {
                checkingAccountEditText.setText(payer.getCheckingAccount());
                checkingAccountEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                checkingAccountEditText.setText(null);
                checkingAccountEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(payer.getBank())) {
                bankEditText.setText(payer.getBank());
                bankEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                bankEditText.setText(null);
                bankEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(payer.getRegistrationCode())) {
                registrationCodeEditText.setText(payer.getRegistrationCode());
                registrationCodeEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                registrationCodeEditText.setText(null);
                registrationCodeEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(payer.getMfo())) {
                mfoEditText.setText(payer.getMfo());
                mfoEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                mfoEditText.setText(null);
                mfoEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (!TextUtils.isEmpty(payer.getOked())) {
                okedEditText.setText(payer.getOked());
                okedEditText.setBackgroundResource(R.drawable.edit_text_active);
            }
            else {
                okedEditText.setText(null);
                okedEditText.setBackgroundResource(R.drawable.edit_text_locked);
            }
            if (payer.getCountryId() != null) {
                payerCountryId = payer.getCountryId();
            }
            if (payer.getCityId() != null) {
                payerCityId = payer.getCityId();
            }
//                for (int i = 0; i < countryList.size(); i++) {
//                    if (countryList.get(i).getId() == payerCountryId) {
//                        payerCountrySpinner.setSelection(i);
//                        break;
//                    }
//                }
        }
    }

    @Override
    public void onTariffSelected(int position, int lastCheckedPosition) {
        tariffPriceRadioAdapter.setLastCheckedPosition(position);
        tariffPriceRecyclerView.post(() -> {
            tariffPriceRadioAdapter.notifyDataSetChanged();
        });
    }

    private void setCountryList(final List<Country> countryList) {
        if (countryList != null) {
            countryArrayAdapter.clear();

            for (final Country country : countryList) {
                countryArrayAdapter.add(country);
            }
            if (senderCountryId >= 0) {
                for (int i = 0; i < countryList.size(); i++) {
                    if (countryList.get(i).getId() == senderCountryId) {
                        senderCountrySpinner.setSelection(i);
                        break;
                    }
                }
            }
            else {
                for (int i = 0; i < countryList.size(); i++) {
                    if (countryList.get(i).getNameEn().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                        int finalI = i;
                        senderCountrySpinner.post(() -> senderCountrySpinner.setSelection(finalI));
                        break;
                    }
                }
            }
            senderCountryIsSet = true;

            if (recipientCountryId >= 0) {
                for (int i = 0; i < countryList.size(); i++) {
                    if (countryList.get(i).getId() == recipientCountryId) {
                        recipientCountrySpinner.setSelection(i);
                        break;
                    }
                }
            }
            else {
                for (int i = 0; i < countryList.size(); i++) {
                    if (countryList.get(i).getNameEn().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                        int finalI = i;
                        recipientCountrySpinner.post(() -> recipientCountrySpinner.setSelection(finalI));
                        break;
                    }
                }
            }
            recipientCountryIsSet = true;

            if (payerCountryId >= 0) {
                for (int i = 0; i < countryList.size(); i++) {
                    if (countryList.get(i).getId() == payerCountryId) {
                        payerCountrySpinner.setSelection(i);
                        break;
                    }
                }
            }
            else {
                for (int i = 0; i < countryList.size(); i++) {
                    if (countryList.get(i).getNameEn().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                        int finalI = i;
                        payerCountrySpinner.post(() -> payerCountrySpinner.setSelection(finalI));
                        break;
                    }
                }
            }
            payerCountryIsSet = true;
        }
    }

    private void setSenderCityList(final List<City> senderCityList) {
        if (senderCityList != null) {
            senderCityArrayAdapter.clear();

            for (final City city : senderCityList) {
                senderCityArrayAdapter.add(city);
            }
            if (!senderCountryIsSet) {
                return;
            }
            if (isSenderCityInitialPick) {
                for (int i = 0; i < senderCityList.size(); i++) {
                    if (senderCityList.get(i).getNameEn().equalsIgnoreCase(getString(R.string.tashkent))) {
                        int finalI = i;
                        senderCitySpinner.post(() -> senderCitySpinner.setSelection(finalI));
                        isSenderCityInitialPick = false;
                        return;
                    }
                }
            }
            if (senderCityId >= 0) {
                for (int i = 0; i < senderCityList.size(); i++) {
                    if (senderCityList.get(i).getId() == senderCityId) {
                        int finalI = i;
                        senderCitySpinner.post(() -> senderCitySpinner.setSelection(finalI));
                        return;
                    }
                }
            }
        }
    }

    private void setRecipientCityList(final List<City> recipientCityList) {
        if (recipientCityList != null) {
            recipientCityArrayAdapter.clear();

            for (final City city : recipientCityList) {
                recipientCityArrayAdapter.add(city);
            }
            if (!recipientCountryIsSet) {
                return;
            }
            if (isRecipientCityInitialPick) {
                for (int i = 0; i < recipientCityList.size(); i++) {
                    if (recipientCityList.get(i).getNameEn().equalsIgnoreCase(getString(R.string.tashkent))) {
                        int finalI = i;
                        recipientCitySpinner.post(() -> recipientCitySpinner.setSelection(finalI));
                        isRecipientCityInitialPick = false;
                        return;
                    }
                }
            }
            if (recipientCityId >= 0) {
                for (int i = 0; i < recipientCityList.size(); i++) {
                    if (recipientCityList.get(i).getId() == recipientCityId) {
                        int finalI = i;
                        recipientCitySpinner.post(() -> recipientCitySpinner.setSelection(finalI));
                        return;
                    }
                }
            }
        }
    }

    private void setPayerCityList(final List<City> payerCityList) {
        if (payerCityList != null) {
            payerCityArrayAdapter.clear();

            for (final City city : payerCityList) {
                payerCityArrayAdapter.add(city);
            }
            if (!payerCountryIsSet) {
                return;
            }
            if (isPayerCityInitialPick) {
                for (int i = 0; i < payerCityList.size(); i++) {
                    if (payerCityList.get(i).getNameEn().equalsIgnoreCase(getString(R.string.tashkent))) {
                        int finalI = i;
                        payerCitySpinner.post(() -> payerCitySpinner.setSelection(finalI));
                        isPayerCityInitialPick = false;
                        return;
                    }
                }
            }
            if (payerCityId >= 0) {
                for (int i = 0; i < payerCityList.size(); i++) {
                    if (payerCityList.get(i).getId() == payerCityId) {
                        int finalI = i;
                        payerCitySpinner.post(() -> payerCitySpinner.setSelection(finalI));
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void onBigSpinnerItemSelected(final int position, final Object selectedObject) {
        //do nothing
    }

    @Override
    public void onSpinnerEditTextItemSelected(final int position, final Object selectedObject) {
        //do nothing
    }

    @Override
    public void onFirstSpinnerItemSelected(final int position, final Region region) {
        //do nothing
    }

    @Override
    public void onSecondSpinnerItemSelected(final int position, final City city) {
        //do nothing
    }

    @Override
    public void bindEditTextSpinner(int position, EditText editText) {
        //do nothing
    }

    @Override
    public void bindEditTextImageView(int position, EditText firstEditText, EditText secondEditText) {
        //do nothing
    }

    @Override
    public void bindTwoEditTexts(int position, EditText firstEditText, EditText secondEditText) {
        //do nothing
    }

    @Override
    public void onSenderSignatureClicked() {
        //do nothing
    }

    @Override
    public void onRecipientSignatureClicked() {
        //do nothing
    }

    @Override
    public void afterFirstEditTextChanged(int position, CharSequence text) {
        //do nothing
    }

    @Override
    public void afterSecondEditTextChanged(int position, CharSequence text) {
        //do nothing
    }

    private static final String TAG = CreateInvoiceFragment.class.toString();
}
