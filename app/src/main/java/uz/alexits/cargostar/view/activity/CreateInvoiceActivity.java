package uz.alexits.cargostar.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uz.alexits.cargostar.R;
import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.api.params.CreateInvoiceParams;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.actor.AddressBook;
import uz.alexits.cargostar.model.calculation.Packaging;
import uz.alexits.cargostar.model.calculation.PackagingType;
import uz.alexits.cargostar.model.calculation.Provider;
import uz.alexits.cargostar.model.calculation.Zone;
import uz.alexits.cargostar.model.calculation.ZoneSettings;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.model.shipping.Cargo;
import uz.alexits.cargostar.model.shipping.Consignment;
import uz.alexits.cargostar.model.shipping.Invoice;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.utils.Regex;
import uz.alexits.cargostar.view.adapter.CalculatorAdapter;
import uz.alexits.cargostar.view.callback.CreateInvoiceCallback;
import uz.alexits.cargostar.viewmodel.CreateInvoiceViewModel;
import uz.alexits.cargostar.viewmodel.CourierViewModel;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.adapter.CreateInvoiceAdapter;
import uz.alexits.cargostar.view.adapter.CreateInvoiceData;
import uz.alexits.cargostar.workers.SyncWorkRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.INPUT_TYPE_PHONE;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_EDIT_TEXT_SPINNER;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_HEADING;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_SINGLE_SPINNER;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_TWO_EDIT_TEXTS;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_SINGLE_EDIT_TEXT;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_STROKE;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_TWO_IMAGE_EDIT_TEXTS;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.INPUT_TYPE_NUMBER;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.INPUT_TYPE_TEXT;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.INPUT_TYPE_EMAIL;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_TWO_SPINNERS;

public class CreateInvoiceActivity extends AppCompatActivity implements CreateInvoiceCallback {
    private Context context;
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

    private List<CreateInvoiceData> itemList;
    private CreateInvoiceAdapter adapter;
    private RecyclerView contentRecyclerView;

    /* content views */
    //transportation
    private EditText transportationQrEditText;
    private ImageView transportationQrImageView;
    private ImageView transportationQrResultImageView;
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

    private Spinner packagingTypeSpinner;
    private ArrayAdapter<PackagingType> packagingTypeArrayAdapter;
    private RelativeLayout packagingTypeField;

    private EditText weightEditText;
    private EditText lengthEditText;
    private EditText widthEditText;
    private EditText heightEditText;
    private EditText cargoQrEditText;
    private ImageView cargoQrImageView;
    private ImageView cargoResultImageView;
    private Button addBtn;

    //calculation
    private TextView totalQuantityTextView;
    private TextView totalWeightTextView;
    private TextView totalDimensionsTextView;

    private Button calculateBtn;

    private RadioGroup paymentMethodRadioGroup;

    private RadioGroup tariffRadioGroup;
    private RadioButton cashRadioBtn;
    private RadioButton terminalRadioBtn;
    private RadioButton expressRadioBtn;
    private RadioButton economyRadioBtn;

    private TextView economyPriceTextView;
    private TextView expressPriceTextView;

    private Button saveInvoiceBtn;
    private Button createInvoiceBtn;

    //EditText watcher values
    private String courierId;
    private String operatorId;
    private String accountantId;
    private String senderSignature;
    private String recipientSignature;
    private String senderCargostar;
    private String senderTnt;
    private String senderFedex;
    private String senderAddress;

    private Country senderCountry;
    private Region senderRegion;
    private City senderCity;

    private Provider serviceProvider;

    private String senderZip;
    private String senderFirstName;
    private String senderMiddleName;
    private String senderLastName;
    private String senderPhone;
    private String senderEmail;

    private String recipientCargo;
    private String recipientTnt;
    private String recipientFedex;
    private String recipientAddress;

    private Country recipientCountry;
    private Region recipientRegion;
    private City recipientCity;

    private String recipientZip;
    private String recipientFirstName;
    private String recipientMiddleName;
    private String recipientLastName;
    private String recipientPhone;
    private String recipientEmail;
    private String payerAddress;

    private Country payerCountry;
    private Region payerRegion;
    private City payerCity;

    private String payerZip;
    private String payerFirstName;
    private String payerMiddleName;
    private String payerLastName;
    private String payerPhone;
    private String payerEmail;
    private String discount;
    private String payerCargostar;
    private String payerTnt;
    private String payerFedex;
    private String payerTntTaxId;
    private String payerFedexTaxId;
    private String checkingAccount;
    private String bank;
    private String registrationCode;
    private String mfo;
    private String oked;

    /* show current cargoList */
    private CalculatorAdapter calculatorAdapter;
    private RecyclerView itemRecyclerView;

    /* view model */
    private CourierViewModel courierViewModel;
    private CreateInvoiceViewModel createInvoiceViewModel;

    /* selected items for calculations */
    private static Integer selectedPackageType = null;

    /* selected items */
    private Long selectedCountryId = null;
    private Packaging selectedPackaging = null;
    private List<Long> selectedPackagingIdList = null;
    private List<ZoneSettings> selectedZoneSettingsList = null;

    private static final List<Consignment> consignmentList = new ArrayList<>();
    private double totalPrice = 0.0;
    private ProgressBar progressBar;
//    private static final List<Cargo> cargoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invoice);
        context = this;

        itemList = new ArrayList<>();

        courierViewModel = new ViewModelProvider(this).get(CourierViewModel.class);
        createInvoiceViewModel = new ViewModelProvider(this).get(CreateInvoiceViewModel.class);

        if (getIntent() != null) {
            final int requestKey = getIntent().getIntExtra(IntentConstants.INTENT_REQUEST_KEY, -1);
            final long requestId = getIntent().getLongExtra(IntentConstants.INTENT_REQUEST_VALUE, -1);
            final long requestOrParcel =  getIntent().getLongExtra(IntentConstants.INTENT_REQUEST_OR_PARCEL, -1);

            initUI();

//            /* header data view model */
//            courierViewModel.selectRequest(requestId).observe(this, receipt -> {
//                if (receipt != null) {
//                    if (requestOrParcel == IntentConstants.INTENT_REQUEST) {
//                        saveReceiptBtn.setVisibility(View.VISIBLE);
//                        createReceiptBtn.setVisibility(View.VISIBLE);
//                    }
//                    else if (requestOrParcel == IntentConstants.INTENT_PARCEL) {
//                        saveReceiptBtn.setVisibility(View.VISIBLE);
//                        createReceiptBtn.setVisibility(View.INVISIBLE);
//                    }
                    //todo: handle payment status
//                    if (receipt.getReceipt().getPaymentStatus() == PaymentStatus.PAID ||
//                            receipt.getReceipt().getPaymentStatus() == PaymentStatus.PAID_MORE ||
//                            receipt.getReceipt().getPaymentStatus() == PaymentStatus.PAID_PARTIALLY) {
//                        cashRadioBtn.setVisibility(View.INVISIBLE);
//                        terminalRadioBtn.setVisibility(View.INVISIBLE);
//                        paymentMethodRadioGroup.setVisibility(View.INVISIBLE);
//                        firstCardRadioBtn.setVisibility(View.INVISIBLE);
//                        secondCardRadioBtn.setVisibility(View.INVISIBLE);
//                        firstCardImageView.setVisibility(View.INVISIBLE);
//                        secondCardImageView.setVisibility(View.INVISIBLE);
//                        firstCard.setVisibility(View.INVISIBLE);
//                        secondCard.setVisibility(View.INVISIBLE);
//                        expressRadioBtn.setVisibility(View.INVISIBLE);
//                        economyRadioBtn.setVisibility(View.INVISIBLE);
//                        tariffRadioGroup.setVisibility(View.INVISIBLE);
//                    }
//                    else if (receipt.getReceipt().getPaymentStatus() == PaymentStatus.WAITING_CHECK || receipt.getReceipt().getPaymentStatus() == PaymentStatus.WAITING_PAYMENT) {
//                        cashRadioBtn.setVisibility(View.VISIBLE);
//                        terminalRadioBtn.setVisibility(View.VISIBLE);
//                        paymentMethodRadioGroup.setVisibility(View.VISIBLE);
//                        firstCardRadioBtn.setVisibility(View.VISIBLE);
//                        secondCardRadioBtn.setVisibility(View.VISIBLE);
//                        firstCardImageView.setVisibility(View.VISIBLE);
//                        secondCardImageView.setVisibility(View.VISIBLE);
//                        firstCard.setVisibility(View.VISIBLE);
//                        secondCard.setVisibility(View.VISIBLE);
//                        expressRadioBtn.setVisibility(View.VISIBLE);
//                        economyRadioBtn.setVisibility(View.VISIBLE);
//                        tariffRadioGroup.setVisibility(View.VISIBLE);
//                    }
//                    currentReceipt = receipt;
//                    updateUI();
//                }
//                if (requestKey != IntentConstants.REQUEST_EDIT_PARCEL) {
//                    saveReceiptBtn.setVisibility(View.INVISIBLE);
//                    createReceiptBtn.setVisibility(View.VISIBLE);
//                }
//            });

        }
        courierViewModel.selectCourierByLogin(SharedPrefs.getInstance(context).getString(SharedPrefs.LOGIN)).observe(this, courier -> {
            if (courier != null) {
                fullNameTextView.setText(courier.getFirstName() + " " + courier.getLastName());
            }
        });
        courierViewModel.selectBrancheById(SharedPrefs.getInstance(context).getLong(SharedPrefs.BRANCH_ID)).observe(this, branch -> {
            if (branch != null) {
                branchTextView.setText(getString(R.string.branch) + " \"" + branch.getName() + "\"");
            }
        });
        courierViewModel.selectNewNotificationsCount().observe(this, newNotificationsCount -> {
            if (newNotificationsCount != null) {
                badgeCounterTextView.setText(String.valueOf(newNotificationsCount));
            }
        });

        /* location data view model */
        createInvoiceViewModel.getCountryList().observe(this, countryList -> {
            adapter.setCountryList(countryList);
//            adapter.notifyItemChanged(8);
//            adapter.notifyItemChanged(17);
//            adapter.notifyItemChanged(25);
        });

        createInvoiceViewModel.getSenderRegionList().observe(this, senderRegionList ->  {
            adapter.setSenderRegionList(senderRegionList);
        });

        createInvoiceViewModel.getRecipientRegionList().observe(this, recipientRegionList -> {
            adapter.setRecipientRegionList(recipientRegionList);
        });

        createInvoiceViewModel.getPayerRegionList().observe(this, payerRegionList -> {
            adapter.setPayerRegionList(payerRegionList);
        });

        createInvoiceViewModel.getSenderCityList().observe(this, senderCityList ->  {
            adapter.setSenderCityList(senderCityList);
        });

        createInvoiceViewModel.getRecipientCityList().observe(this, recipientCityList -> {
            adapter.setRecipientCityList(recipientCityList);
        });

        createInvoiceViewModel.getPayerCityList().observe(this, payerCityList -> {
            adapter.setPayerCityList(payerCityList);
        });

        /* address book data view model */
        //todo: handle addressBook population
//        createParcelViewModel.selectAddressBookEntriesBySenderLogin(senderLogin).observe(this, addressBookEntries -> {
//            Log.i(TAG, "entries=" + addressBookEntries);
//            adapter.setAddressBookEntries(addressBookEntries);
//            adapter.notifyDataSetChanged();
//        });

        /* calculator data view model */
        createInvoiceViewModel.getProvider().observe(this, provider -> {
            Log.i(TAG, "provider: " + provider);
            serviceProvider = provider;
        });

        createInvoiceViewModel.getType().observe(this, type -> {
            Log.i(TAG, "type: " + type);
            if (type != null && selectedPackagingIdList != null) {
                createInvoiceViewModel.setTypePackageIdList(type, selectedPackagingIdList);
            }
        });

        createInvoiceViewModel.getPackagingIds().observe(this, packagingIds -> {
            Log.i(TAG, "packagingIds: " + packagingIds);
            selectedPackagingIdList = packagingIds;
            if (packageTypeRadioGroup.getCheckedRadioButtonId() == docTypeRadioBtn.getId()) {
                createInvoiceViewModel.setTypePackageIdList(1, packagingIds);
            }
            else if (packageTypeRadioGroup.getCheckedRadioButtonId() == boxTypeRadioBtn.getId()) {
                createInvoiceViewModel.setTypePackageIdList(2, packagingIds);
            }
        });

        createInvoiceViewModel.getPackagingTypeList().observe(this, packagingTypeList -> {
            Log.i(TAG, "packagingTypeList: " + packagingTypeList);
            packagingTypeArrayAdapter.clear();
            packagingTypeArrayAdapter.addAll(packagingTypeList);
            packagingTypeArrayAdapter.notifyDataSetChanged();
        });

        createInvoiceViewModel.getPackaging().observe(this, packaging -> {
            Log.i(TAG, "packaging: " + packaging);
            selectedPackaging = packaging;
        });

        createInvoiceViewModel.getZoneList().observe(this, zoneList -> {
            if (zoneList != null && !zoneList.isEmpty()) {

                final List<Long> zoneIds = new ArrayList<>();

                for (final Zone zone : zoneList) {
                    zoneIds.add(zone.getId());
                }
                createInvoiceViewModel.setZoneIds(zoneIds);
            }
        });

        createInvoiceViewModel.getZoneSettingsList().observe(this, zoneSettingsList -> {
            Log.i(TAG, "zoneSettingsList: " + zoneSettingsList);
            selectedZoneSettingsList = zoneSettingsList;
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
                WorkManager.getInstance(context).getWorkInfoByIdLiveData(searchInvoiceUUID).observe(this, workInfo -> {
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
                        mainIntent.putExtra(IntentConstants.INTENT_REQUEST_KEY, IntentConstants.REQUEST_FIND_PARCEL);
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

        transportationQrImageView.setOnClickListener(v -> {
            startActivityForResult(new Intent(context, ScanQrActivity.class), IntentConstants.REQUEST_SCAN_QR_PARCEL);

        });

        cargoQrImageView.setOnClickListener(v -> {
            startActivityForResult(new Intent(context, ScanQrActivity.class), IntentConstants.REQUEST_SCAN_QR_CARGO);
        });

        /* providers */
        firstProviderCard.setOnClickListener(v -> {
            firstProviderRadioBtn.setChecked(true);
        });

        secondProviderCard.setOnClickListener(v -> {
            secondProviderRadioBtn.setChecked(true);
        });

        /* choose provider */
        firstProviderRadioBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                secondProviderRadioBtn.setChecked(false);

                Log.i(TAG, "selectedCountryId: " + selectedCountryId);

                if (firstProviderRadioBtn.getText().equals(getString(R.string.cargostar))) {
                    Log.i(TAG, "selectProvider(): cargostar");
                    createInvoiceViewModel.setProviderId(6L);
                    createInvoiceViewModel.setCountryIdProviderId(selectedCountryId, 6L);
                    return;
                }
                if (firstProviderRadioBtn.getText().equals(getString(R.string.tnt))) {
                    Log.i(TAG, "selectProvider(): tnt");
                    createInvoiceViewModel.setProviderId(5L);
                    createInvoiceViewModel.setCountryIdProviderId(selectedCountryId, 5L);
                }
                return;
            }
//            if (!secondProviderRadioBtn.isChecked()) {
//                selectedProvider = null;
//                createInvoiceViewModel.setProviderId(null);
//                createInvoiceViewModel.setCountryIdProviderId(selectedCountryId, null);
//            }
        });

        secondProviderRadioBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                Log.i(TAG, "selectProvider(): fedex");
                firstProviderRadioBtn.setChecked(false);
                //only fedex case
                createInvoiceViewModel.setProviderId(4L);
                createInvoiceViewModel.setCountryIdProviderId(selectedCountryId, 4L);
                return;
            }
//            if (!firstProviderRadioBtn.isChecked()) {
//                selectedProvider = null;
//                createInvoiceViewModel.setProviderId(null);
//                createInvoiceViewModel.setCountryIdProviderId(selectedCountryId, null);
//            }
        });

        /* choose packaging type (1 / 2) */
        packageTypeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == docTypeRadioBtn.getId()) {
                //docs
                createInvoiceViewModel.setType(1);
            }
            else if (checkedId == boxTypeRadioBtn.getId()) {
                //boxes
                createInvoiceViewModel.setType(2);
            }
            else {
                createInvoiceViewModel.setType(null);
            }
        });

        packagingTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView itemTextView = (TextView) view;
                final PackagingType selectedPackagingType = (PackagingType) adapterView.getSelectedItem();

                createInvoiceViewModel.setPackagingId(selectedPackagingType.getPackagingId());

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        packagingTypeField.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addBtn.setOnClickListener(v -> {
            addCargoToInvoice();
        });

        calculateBtn.setOnClickListener(v -> {
            calculateTotalPrice();
        });

        saveInvoiceBtn.setOnClickListener(v -> {
            saveReceipt();
        });

        createInvoiceBtn.setOnClickListener(v -> {
            createInvoice();
        });
    }

    private void initUI() {
        //header views
        fullNameTextView = findViewById(R.id.full_name_text_view);
        branchTextView = findViewById(R.id.branch_text_view);
        parcelSearchEditText = findViewById(R.id.search_edit_text);
        parcelSearchImageView = findViewById(R.id.search_btn);
        profileImageView = findViewById(R.id.profile_image_view);
        editImageView = findViewById(R.id.edit_image_view);
        createUserImageView = findViewById(R.id.create_user_image_view);
        calculatorImageView = findViewById(R.id.calculator_image_view);
        notificationsImageView = findViewById(R.id.notifications_image_view);
        badgeCounterTextView = findViewById(R.id.badge_counter_text_view);
        progressBar = findViewById(R.id.progress_bar);

        itemRecyclerView = findViewById(R.id.calculations_recycler_view);
        calculatorAdapter = new CalculatorAdapter(context, consignmentList, this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        itemRecyclerView.setLayoutManager(layoutManager);
        itemRecyclerView.setAdapter(calculatorAdapter);

        //content views
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

        /* public data */
        itemList.add(new CreateInvoiceData(getString(R.string.public_data), null, null, null, TYPE_HEADING));
        itemList.add(new CreateInvoiceData(getString(R.string.courier_id), getString(R.string.operator_id), String.valueOf(SharedPrefs.getInstance(context).getLong(SharedPrefs.ID)), null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_TEXT, INPUT_TYPE_TEXT, false, false));
        itemList.add(new CreateInvoiceData(getString(R.string.accountant_id), null, null, null,
                TYPE_SINGLE_EDIT_TEXT, INPUT_TYPE_TEXT, -1, false, false));
        itemList.add(new CreateInvoiceData(null, null, null, null, TYPE_STROKE));

        /* sender data */
        itemList.add(new CreateInvoiceData(getString(R.string.sender_data), null, null, null, TYPE_HEADING));
        itemList.add(new CreateInvoiceData(getString(R.string.sender_signature), getString(R.string.receiver_signature), null, null,
                TYPE_TWO_IMAGE_EDIT_TEXTS));
        itemList.add(new CreateInvoiceData(getString(R.string.email), getString(R.string.cargostar_account_number), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_EMAIL, INPUT_TYPE_NUMBER, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.tnt_account_number), getString(R.string.fedex_account_number), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_NUMBER, true, true));

        itemList.add(new CreateInvoiceData(getString(R.string.take_address), getString(R.string.country), null, null,
                TYPE_EDIT_TEXT_SPINNER, INPUT_TYPE_TEXT, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.region), getString(R.string.city), null, null,
                TYPE_TWO_SPINNERS, -1, -1, true, true));

        itemList.add(new CreateInvoiceData(getString(R.string.post_index), getString(R.string.first_name), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.middle_name), getString(R.string.last_name), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_TEXT, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.phone_number), null, null, null,
                TYPE_SINGLE_EDIT_TEXT, INPUT_TYPE_PHONE, -1, true, true));
        itemList.add(new CreateInvoiceData(null, null, null, null, TYPE_STROKE));

        /* recipient data */
        itemList.add(new CreateInvoiceData(getString(R.string.receiver_data), null, null, null, TYPE_HEADING));
        itemList.add(new CreateInvoiceData(getString(R.string.email), getString(R.string.cargostar_account_number), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_EMAIL, INPUT_TYPE_NUMBER, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.tnt_account_number), getString(R.string.fedex_account_number), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_NUMBER, true, true));

        itemList.add(new CreateInvoiceData(getString(R.string.delivery_address), getString(R.string.country), null, null,
                TYPE_EDIT_TEXT_SPINNER, INPUT_TYPE_TEXT, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.region), getString(R.string.city), null, null,
                TYPE_TWO_SPINNERS, INPUT_TYPE_TEXT, INPUT_TYPE_TEXT, true, true));

        itemList.add(new CreateInvoiceData(getString(R.string.post_index), getString(R.string.first_name), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.middle_name), getString(R.string.last_name), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_TEXT, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.phone_number), null, null, null,
                TYPE_SINGLE_EDIT_TEXT, INPUT_TYPE_PHONE, -1, true, true));
        itemList.add(new CreateInvoiceData(null, null, null, null, TYPE_STROKE));

        /* payer data */
        itemList.add(new CreateInvoiceData(getString(R.string.payer_data), null, null, null, TYPE_HEADING));
        itemList.add(new CreateInvoiceData(getString(R.string.address_book), null, null, null, TYPE_SINGLE_SPINNER));
        itemList.add(new CreateInvoiceData(getString(R.string.email), getString(R.string.country), null, null,
                TYPE_EDIT_TEXT_SPINNER, INPUT_TYPE_EMAIL, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.region), getString(R.string.city), null, null,
                TYPE_TWO_SPINNERS, INPUT_TYPE_TEXT, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.delivery_address), getString(R.string.post_index), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_EMAIL, INPUT_TYPE_NUMBER, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.first_name), getString(R.string.middle_name), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_TEXT, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.last_name), getString(R.string.phone_number), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_TEXT, INPUT_TYPE_PHONE, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.discount), null, null, null,
                TYPE_SINGLE_EDIT_TEXT, INPUT_TYPE_NUMBER, -1, false, false));
        itemList.add(new CreateInvoiceData(null, null, null, null, TYPE_STROKE));

        /* account numbers */
        itemList.add(new CreateInvoiceData(getString(R.string.cargostar_account_number), null, null, null,
                TYPE_SINGLE_EDIT_TEXT, INPUT_TYPE_NUMBER, -1, true, false));
        itemList.add(new CreateInvoiceData(getString(R.string.tnt_account_number), getString(R.string.tax_id),null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_NUMBER, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.fedex_account_number), getString(R.string.tax_id), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_NUMBER, true, true));
        itemList.add(new CreateInvoiceData(null, null, null, null, TYPE_STROKE));

        /* payment data */
        itemList.add(new CreateInvoiceData(getString(R.string.payment_data), null, null, null, TYPE_HEADING));
        itemList.add(new CreateInvoiceData(getString(R.string.checking_account), getString(R.string.bank),null,null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.payer_registration_code), getString(R.string.mfo), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_NUMBER, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.oked), null, null, null,
                TYPE_SINGLE_EDIT_TEXT, INPUT_TYPE_NUMBER, -1, true, false));
        itemList.add(new CreateInvoiceData(null, null, null, null, TYPE_STROKE));

        contentRecyclerView = findViewById(R.id.content_recycler_view);
        adapter = new CreateInvoiceAdapter(context, this);
        contentRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        contentRecyclerView.setAdapter(adapter);
        adapter.setItemList(itemList);
        adapter.notifyDataSetChanged();
        //below recycler view
        totalQuantityTextView = findViewById(R.id.total_quantity_value_text_view);
        totalWeightTextView = findViewById(R.id.total_weight_value_text_view);
        totalDimensionsTextView = findViewById(R.id.total_dimensions_value_text_view);
        calculateBtn = findViewById(R.id.calculate_btn);

        transportationQrEditText = findViewById(R.id.transportation_qr_edit_text);
        transportationQrImageView = findViewById(R.id.transportation_qr_image_view);
        transportationQrResultImageView = findViewById(R.id.transportation_qr_result_image_view);
        instructionsEditText = findViewById(R.id.instructions_edit_text);

        //provider
        firstProviderCard = findViewById(R.id.first_card);
        secondProviderCard = findViewById(R.id.second_card);
        firstProviderRadioBtn = findViewById(R.id.first_card_radio_btn);
        secondProviderRadioBtn = findViewById(R.id.second_card_radio_btn);
        firstProviderImageView = findViewById(R.id.first_card_logo);
        secondProviderImageView = findViewById(R.id.second_card_logo);

        //package type
        packageTypeRadioGroup = findViewById(R.id.package_type_radio_group);
        docTypeRadioBtn = findViewById(R.id.doc_type_radio_btn);
        boxTypeRadioBtn = findViewById(R.id.box_type_radio_btn);

        //cargo
        cargoDescriptionEditText = findViewById(R.id.cargo_description_edit_text);
        packagingTypeSpinner = findViewById(R.id.packaging_type_spinner);
        packagingTypeField = findViewById(R.id.packaging_type_field);
        weightEditText = findViewById(R.id.weight_edit_text);
        lengthEditText = findViewById(R.id.length_edit_text);
        widthEditText = findViewById(R.id.width_edit_text);
        heightEditText = findViewById(R.id.height_edit_text);
        cargoQrEditText = findViewById(R.id.cargo_qr_edit_text);
        cargoQrImageView = findViewById(R.id.cargo_qr_image_view);
        cargoResultImageView = findViewById(R.id.cargo_qr_result_image_view);
        addBtn = findViewById(R.id.add_item_btn);

        //calculations
        tariffRadioGroup = findViewById(R.id.tariff_radio_group);
        paymentMethodRadioGroup = findViewById(R.id.payment_method_radio_group);
        expressRadioBtn = findViewById(R.id.express_radio_btn);
        economyRadioBtn = findViewById(R.id.economy_radio_btn);
        cashRadioBtn = findViewById(R.id.cash_radio_btn);
        terminalRadioBtn = findViewById(R.id.terminal_radio_btn);
        economyPriceTextView = findViewById(R.id.economy_price_text_view);
        expressPriceTextView = findViewById(R.id.express_price_text_view);
        saveInvoiceBtn = findViewById(R.id.save_btn);
        createInvoiceBtn = findViewById(R.id.create_receipt_btn);

        packagingTypeArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new ArrayList<>());
        packagingTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        packagingTypeSpinner.setAdapter(packagingTypeArrayAdapter);
    }

    private void updateUI(final Invoice invoice) {
        //public data
//        (1).firstValue = receipt != null ? String.valueOf(receipt.getInvoice().getCourierId()) : null;
//        itemList.get(1).secondValue = receipt != null ? receipt.getInvoice().getOperatorId() : null;
//        itemList.get(2).firstValue = receipt != null ? receipt.getInvoice().getAccountantId() : null;
//        itemList.get(2).secondValue = receipt != null ? receipt.getInvoice().getServiceProvider() : null;
//        courierId = receipt != null ? String.valueOf(receipt.getInvoice().getCourierId()) : null;
//        operatorId = receipt != null ? receipt.getInvoice().getOperatorId() : null;
//        accountantId = receipt != null ? receipt.getInvoice().getAccountantId() : null;
//        serviceProvider = receipt != null ? receipt.getInvoice().getServiceProvider() : null;
//        //sender data
//        itemList.get(5).firstValue = receipt != null ? receipt.getInvoice().getSenderSignature() : null;
//        itemList.get(5).secondValue = receipt != null ? receipt.getInvoice().getRecipientSignature() : null;
//        itemList.get(6).firstValue = receipt != null ? receipt.getInvoice().getSenderLogin() : null;
//        itemList.get(6).secondValue = receipt != null ? receipt.getInvoice().getSenderCargostarAccountNumber() : null;
//        itemList.get(7).firstValue = receipt != null ? receipt.getInvoice().getSenderTntAccountNumber() : null;
//        itemList.get(7).secondValue = receipt != null ? receipt.getInvoice().getSenderFedexAccountNumber(): null;
//        itemList.get(8).firstValue = receipt != null ? receipt.getInvoice().getSenderAddress().getAddress() : null;
//        itemList.get(8).secondValue = receipt != null ? receipt.getInvoice().getSenderAddress().getCountry() : null;
//        itemList.get(9).firstValue = receipt != null ? receipt.getInvoice().getSenderAddress().getCity() : null;
//        itemList.get(9).secondValue = receipt != null ? receipt.getInvoice().getSenderAddress().getRegion() : null;
//        itemList.get(10).firstValue = receipt != null ? receipt.getInvoice().getSenderAddress().getZip() : null;
//        itemList.get(10).secondValue = receipt != null ? receipt.getInvoice().getSenderFirstName() : null;
//        itemList.get(11).firstValue = receipt != null ? receipt.getInvoice().getSenderMiddleName() : null;
//        itemList.get(11).secondValue = receipt != null ? receipt.getInvoice().getSenderLastName() : null;
//        itemList.get(12).firstValue = receipt != null ? receipt.getInvoice().getSenderPhone() : null;
//        itemList.get(12).secondValue = receipt != null ? receipt.getInvoice().getSenderEmail() : null;
//        senderSignature = receipt != null ? receipt.getInvoice().getSenderSignature() : null;
//        recipientSignature = receipt != null ? receipt.getInvoice().getRecipientSignature() : null;
//        senderLogin = receipt != null ? receipt.getInvoice().getSenderLogin() : null;
//        senderCargostar = receipt != null ? receipt.getInvoice().getSenderCargostarAccountNumber() : null;
//        senderTnt = receipt != null ? receipt.getInvoice().getSenderTntAccountNumber() : null;
//        senderFedex = receipt != null ? receipt.getInvoice().getSenderFedexAccountNumber(): null;
//        senderAddress = receipt != null ? receipt.getInvoice().getSenderAddress().getAddress() : null;
//        senderCountry = receipt != null ? receipt.getInvoice().getSenderAddress().getCountry() : null;
//        senderCity = receipt != null ? receipt.getInvoice().getSenderAddress().getCity() : null;
//        senderRegion = receipt != null ? receipt.getInvoice().getSenderAddress().getRegion() : null;
//        senderZip = receipt != null ? receipt.getInvoice().getSenderAddress().getZip() : null;
//        senderFirstName = receipt != null ? receipt.getInvoice().getSenderFirstName() : null;
//        senderMiddleName = receipt != null ? receipt.getInvoice().getSenderMiddleName() : null;
//        senderLastName = receipt != null ? receipt.getInvoice().getSenderLastName() : null;
//        senderPhone = receipt != null ? receipt.getInvoice().getSenderPhone() : null;
//        senderEmail = receipt != null ? receipt.getInvoice().getSenderEmail() : null;
//        //recipient data
//        itemList.get(15).firstValue = receipt != null ? receipt.getInvoice().getRecipientLogin() : null;
//        itemList.get(15).secondValue = receipt != null ? receipt.getInvoice().getRecipientCargostarAccountNumber() : null;
//        itemList.get(16).firstValue = receipt != null ? receipt.getInvoice().getRecipientTntAccountNumber() : null;
//        itemList.get(16).secondValue = receipt != null ? receipt.getInvoice().getRecipientFedexAccountNumber() : null;
//        itemList.get(17).firstValue = receipt != null && receipt.getInvoice().getRecipientAddress() != null ? receipt.getInvoice().getRecipientAddress().getAddress() : null;
//        itemList.get(17).secondValue = receipt != null && receipt.getInvoice().getRecipientAddress() != null ? receipt.getInvoice().getRecipientAddress().getCountry() : null;
//        itemList.get(18).firstValue = receipt != null  && receipt.getInvoice().getRecipientAddress() != null ? receipt.getInvoice().getRecipientAddress().getCity() : null;
//        itemList.get(18).secondValue = receipt != null && receipt.getInvoice().getRecipientAddress() != null ? receipt.getInvoice().getRecipientAddress().getRegion(): null;
//        itemList.get(19).firstValue = receipt != null && receipt.getInvoice().getRecipientAddress() != null ? receipt.getInvoice().getRecipientAddress().getZip() : null;
//        itemList.get(19).secondValue = receipt != null ? receipt.getInvoice().getRecipientFirstName() : null;
//        itemList.get(20).firstValue = receipt != null ? receipt.getInvoice().getRecipientMiddleName() : null;
//        itemList.get(20).secondValue = receipt != null ? receipt.getInvoice().getRecipientLastName() : null;
//        itemList.get(21).firstValue = receipt != null ? receipt.getInvoice().getRecipientPhone() : null;
//        itemList.get(21).secondValue = receipt != null ? receipt.getInvoice().getRecipientEmail() : null;
//        recipientLogin = receipt != null ? receipt.getInvoice().getRecipientLogin() : null;
//        recipientCargo = receipt != null ? receipt.getInvoice().getRecipientCargostarAccountNumber() : null;
//        recipientTnt = receipt != null ? receipt.getInvoice().getRecipientTntAccountNumber() : null;
//        recipientFedex = receipt != null ? receipt.getInvoice().getRecipientFedexAccountNumber() : null;
//        recipientAddress = receipt != null && receipt.getInvoice().getRecipientAddress() != null ? receipt.getInvoice().getRecipientAddress().getAddress() : null;
//        recipientCountry = receipt != null && receipt.getInvoice().getRecipientAddress() != null ? receipt.getInvoice().getRecipientAddress().getCountry() : null;
//        recipientCity = receipt != null  && receipt.getInvoice().getRecipientAddress() != null ? receipt.getInvoice().getRecipientAddress().getCity() : null;
//        recipientRegion = receipt != null && receipt.getInvoice().getRecipientAddress() != null ? receipt.getInvoice().getRecipientAddress().getRegion(): null;
//        recipientZip = receipt != null && receipt.getInvoice().getRecipientAddress() != null ? receipt.getInvoice().getRecipientAddress().getZip() : null;
//        recipientFirstName = receipt != null ? receipt.getInvoice().getRecipientFirstName() : null;
//        recipientMiddleName = receipt != null ? receipt.getInvoice().getRecipientMiddleName() : null;
//        recipientLastName = receipt != null ? receipt.getInvoice().getRecipientLastName() : null;
//        recipientPhone = receipt != null ? receipt.getInvoice().getRecipientPhone() : null;
//        recipientEmail = receipt != null ? receipt.getInvoice().getRecipientEmail() : null;
//        //payer data
//        itemList.get(25).firstValue = receipt != null ? receipt.getInvoice().getPayerLogin() : null;
//        itemList.get(25).secondValue = receipt != null && receipt.getInvoice().getPayerAddress() != null ? receipt.getInvoice().getPayerAddress().getAddress() : null;
//        itemList.get(26).firstValue = receipt != null && receipt.getInvoice().getPayerAddress() != null ? receipt.getInvoice().getPayerAddress().getCountry() : null;
//        itemList.get(26).secondValue = receipt != null && receipt.getInvoice().getPayerAddress() != null ? receipt.getInvoice().getPayerAddress().getCity() : null;
//        itemList.get(27).firstValue = receipt != null && receipt.getInvoice().getPayerAddress() != null ? receipt.getInvoice().getPayerAddress().getRegion() : null;
//        itemList.get(27).secondValue = receipt != null && receipt.getInvoice().getPayerAddress() != null ? receipt.getInvoice().getPayerAddress().getZip() : null;
//        itemList.get(28).firstValue = receipt != null ? receipt.getInvoice().getPayerFirstName() : null;
//        itemList.get(28).secondValue = receipt != null ? receipt.getInvoice().getPayerMiddleName() : null;
//        itemList.get(29).firstValue = receipt != null ? receipt.getInvoice().getPayerLastName() : null;
//        itemList.get(29).secondValue = receipt != null ? receipt.getInvoice().getPayerPhone() : null;
//        itemList.get(30).firstValue = receipt != null ? receipt.getInvoice().getPayerEmail() : null;
//        itemList.get(30).secondValue = receipt != null && receipt.getInvoice().getDiscount() > 0 ? String.valueOf(receipt.getInvoice().getDiscount()) : null;
//        payerLogin = receipt != null ? receipt.getInvoice().getPayerLogin() : null;
//        payerAddress = receipt != null && receipt.getInvoice().getPayerAddress() != null ? receipt.getInvoice().getPayerAddress().getAddress() : null;
//        payerCountry = receipt != null && receipt.getInvoice().getPayerAddress() != null ? receipt.getInvoice().getPayerAddress().getCountry() : null;
//        payerCity = receipt != null && receipt.getInvoice().getPayerAddress() != null ? receipt.getInvoice().getPayerAddress().getCity() : null;
//        payerRegion = receipt != null && receipt.getInvoice().getPayerAddress() != null ? receipt.getInvoice().getPayerAddress().getRegion() : null;
//        payerZip = receipt != null && receipt.getInvoice().getPayerAddress() != null ? receipt.getInvoice().getPayerAddress().getZip() : null;
//        payerFirstName = receipt != null ? receipt.getInvoice().getPayerFirstName() : null;
//        payerMiddleName = receipt != null ? receipt.getInvoice().getPayerMiddleName() : null;
//        payerLastName = receipt != null ? receipt.getInvoice().getPayerLastName() : null;
//        payerPhone = receipt != null ? receipt.getInvoice().getPayerPhone() : null;
//        payerEmail = receipt != null ? receipt.getInvoice().getPayerEmail() : null;
//        discount = receipt != null && receipt.getInvoice().getDiscount() > 0 ? String.valueOf(receipt.getInvoice().getDiscount()) : null;
//        //account numbers
//        itemList.get(33).firstValue = receipt != null ? receipt.getInvoice().getPayerTntAccountNumber() : null;
//        itemList.get(34).firstValue = receipt != null ? receipt.getInvoice().getPayerFedexAccountNumber() : null;
//        payerTnt = receipt != null ? receipt.getInvoice().getPayerTntAccountNumber() : null;
//        payerFedex = receipt != null ? receipt.getInvoice().getPayerFedexAccountNumber() : null;
//        //payment data
//        itemList.get(37).firstValue = receipt != null ? receipt.getInvoice().getCheckingAccount() : null;
//        itemList.get(37).secondValue = receipt != null ? receipt.getInvoice().getBank() : null;
//        itemList.get(38).firstValue = receipt != null ? receipt.getInvoice().getRegistrationCode() : null;
//        itemList.get(38).secondValue = receipt != null ? receipt.getInvoice().getMfo() : null;
//        itemList.get(39).firstValue = receipt != null ? receipt.getInvoice().getOked() : null;
//        checkingAccount = receipt != null ? receipt.getInvoice().getCheckingAccount() : null;
//        bank = receipt != null ? receipt.getInvoice().getBank() : null;
//        registrationCode = receipt != null ? receipt.getInvoice().getRegistrationCode() : null;
//        mfo = receipt != null ? receipt.getInvoice().getMfo() : null;
//        oked = receipt != null ? receipt.getInvoice().getOked() : null;
//        //parcel data
//        itemList.get(42).firstValue = receipt != null ? receipt.getInvoice().getQr() : null;
//        itemList.get(42).secondValue = receipt != null ? receipt.getInvoice().getInstructions() : null;
//        qr = receipt != null ? receipt.getInvoice().getQr() : null;
//        instructions = receipt != null ? receipt.getInvoice().getInstructions() : null;
//        //cargo data 50
//        int i = 1;
//
//        if (receipt != null) {
//            cargoList = receipt.getCargoList();
//            for (final Cargo cargo : receipt.getCargoList()) {
//                final CreateParcelData cargoData = new CreateParcelData(CreateParcelData.TYPE_CALCULATOR_ITEM);
//                cargoData.index = String.valueOf(i++);
//                cargoData.packageType = cargo.getPackageType();
//                cargoData.source = receipt.getInvoice().getSenderAddress().getCity();
//                cargoData.weight = String.valueOf(cargo.getWeight());
//                cargoData.dimensions = cargo.getLength() + "x" + cargo.getWidth() + "x" + cargo.getHeight();
//                itemList.add(cargoData);
//            }
//        }
//        adapter.notifyDataSetChanged();
    }

    /* btns */
    private void addCargoToInvoice() {
        final PackagingType packagingType = (PackagingType) packagingTypeSpinner.getSelectedItem();
        final String description = cargoDescriptionEditText.getText().toString().trim();
        final String weight = weightEditText.getText().toString().trim();
        final String length = lengthEditText.getText().toString().trim();
        final String width = widthEditText.getText().toString().trim();
        final String height = heightEditText.getText().toString().trim();
        final String consignmentQr = cargoQrEditText.getText().toString().trim();

        /* check for empty fields */
        if (packagingType == null) {
            Toast.makeText(context, "Выберите упаковку", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(weight)) {
            Toast.makeText(context, "Укажите вес", Toast.LENGTH_SHORT).show();
            return;
        }
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
        /* check for regex */
        if (!Regex.isFloatOrInt(weight)) {
            Toast.makeText(context, "Вес указан неверно", Toast.LENGTH_SHORT).show();
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
        //todo: packagingId & deliveryType
        consignmentList.add(new Consignment(
                -1,
                -1L,
                String.valueOf(packagingType.getId()),
                description,
                Double.parseDouble(length),
                Double.parseDouble(width),
                Double.parseDouble(height),
                Double.parseDouble(weight),
                -1,
                consignmentQr));
        calculatorAdapter.notifyItemInserted(consignmentList.size() - 1);
    }

    private void calculateTotalPrice() {
        if (senderCountry == null) {
            Toast.makeText(context, "Укажите страну отправки", Toast.LENGTH_SHORT).show();
            return;
        }
        if (recipientCountry == null) {
            Toast.makeText(context, "Укажите страну прибытия", Toast.LENGTH_SHORT).show();
            return;
        }
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
        int totalQuantity = consignmentList.size();
        double totalVolume = 0.0;
        double totalWeight = 0.0;
        double totalLength = 0.0;
        double totalWidth = 0.0;
        double totalHeight = 0.0;

        double totalPrice = 0.0;

        for (final Consignment item : consignmentList) {
            totalWeight += item.getWeight();
            totalLength += item.getLength();
            totalWidth += item.getWidth();
            totalHeight += item.getHeight();
        }
        totalVolume = totalLength * totalWidth * totalHeight;

        if (selectedPackaging != null) {
            final int volumex = selectedPackaging.getVolumex();

            if (volumex > 0) {
                final double volumexWeight = totalVolume / volumex;

                if (volumexWeight > totalWeight) {
                    totalWeight = volumexWeight;
                }
            }
        }
        ZoneSettings actualZoneSettings = null;

        for (final ZoneSettings zoneSettings : selectedZoneSettingsList) {
            if (totalWeight >= zoneSettings.getWeightFrom() && totalWeight < zoneSettings.getWeightTo()) {
                actualZoneSettings = zoneSettings;
                Log.i(TAG, "selectedZoneSettings=" + actualZoneSettings);
                break;
            }
        }
        if (actualZoneSettings != null) {
            totalPrice = actualZoneSettings.getPriceFrom();
            Log.i(TAG, "calculating: from " + actualZoneSettings.getWeightFrom() + " to " + actualZoneSettings.getWeightTo() + " with " + actualZoneSettings.getWeightStep());
            for (double i = actualZoneSettings.getWeightFrom(); i < totalWeight; i += actualZoneSettings.getWeightStep()) {
                totalPrice += actualZoneSettings.getPriceStep();
            }
        }
        if (selectedPackaging != null) {
            if (packageTypeRadioGroup.getCheckedRadioButtonId() == boxTypeRadioBtn.getId()) {
                totalPrice += selectedPackaging.getParcelFee();
            }
        }
        if (serviceProvider != null) {
            totalPrice = totalPrice * (serviceProvider.getFuel() + 100) / 100;

        }
        totalPrice *= 1.15;
        this.totalPrice = totalPrice;

        totalQuantityTextView.setText(String.valueOf(totalQuantity));
        totalWeightTextView.setText(String.valueOf(totalWeight));
        totalDimensionsTextView.setText(String.valueOf(totalVolume));
        expressPriceTextView.setText(String.valueOf(totalPrice));
        economyPriceTextView.setText(String.valueOf(totalPrice));
    }

    private void saveReceipt() {
        //sender data
        if (TextUtils.isEmpty(senderAddress)) {
            Log.i(TAG, "senderAddress empty");
            Toast.makeText(context, "Для создания заявки укажите адрес отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (senderCountry == null) {
            Log.i(TAG, "senderCountry empty");
            Toast.makeText(context, "Для создания заявки укажите страну отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (senderRegion == null) {
            Log.i(TAG, "senderRegion empty");
            Toast.makeText(context, "Для создания заявки укажите регион отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (senderCity == null) {
            Log.i(TAG, "senderCity empty");
            Toast.makeText(context, "Для создания заявки укажите город отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senderFirstName)) {
            Log.i(TAG, "senderFirstName empty");
            Toast.makeText(context, "Для создания заявки укажите имя отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senderMiddleName)) {
            Log.i(TAG, "senderMiddleName empty");
            Toast.makeText(context, "Для создания заявки укажите отчество отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senderLastName)) {
            Log.i(TAG, "senderLastName empty");
            Toast.makeText(context, "Для создания заявки укажите фамилию отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senderPhone)) {
            Toast.makeText(context, "Для создания заявки укажите номер телефона отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senderEmail)) {
            Toast.makeText(context, "Для создания заявки укажите email отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!senderCountry.equals(getString(R.string.uzbekistan)) && !recipientCountry.equals(getString(R.string.uzbekistan))) {
            Toast.makeText(context, "Страна отправителя или получателя должна быть Узбекистан", Toast.LENGTH_SHORT).show();
            return;
        }
        //todo: handle service provider
//        if (!firstCardRadioBtn.isChecked() && !secondCardRadioBtn.isChecked()) {
//            Toast.makeText(context, "Для создания заявки выберите поставщика услуг", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (firstCardRadioBtn.isChecked()) {
//            serviceProvider = firstCardValue;
//        }
//        else if (secondCardRadioBtn.isChecked()) {
//            serviceProvider = secondCardValue;
//        }

//        currentReceipt.getInvoice().setServiceProvider(serviceProvider);
//        currentReceipt.getInvoice().setSenderSignature(senderSignature);
//        currentReceipt.getInvoice().setSenderEmail(senderEmail);
//        currentReceipt.getInvoice().setSenderCargostarAccountNumber(senderCargostar);
//        currentReceipt.getInvoice().setRecipientFirstName(recipientFirstName);
//        currentReceipt.getInvoice().setRecipientMiddleName(recipientMiddleName);
//        currentReceipt.getInvoice().setRecipientLastName(recipientLastName);
//        currentReceipt.getInvoice().setRecipientPhone(recipientPhone);
//        currentReceipt.getInvoice().setRecipientEmail(recipientEmail);
//        currentReceipt.getInvoice().setRecipientSignature(recipientSignature);
//        currentReceipt.getInvoice().setPayerFirstName(payerFirstName);
//        currentReceipt.getInvoice().setPayerMiddleName(payerMiddleName);
//        currentReceipt.getInvoice().setPayerLastName(payerLastName);
//        currentReceipt.getInvoice().setPayerPhone(payerPhone);
//        currentReceipt.getInvoice().setPayerEmail(payerEmail);
//        currentReceipt.getInvoice().setQr(qr);
//        //sender data
//        currentReceipt.getInvoice().setSenderLogin(senderLogin);
//        currentReceipt.getInvoice().setSenderTntAccountNumber(senderTnt);
//        currentReceipt.getInvoice().setSenderFedexAccountNumber(senderFedex);
//        currentReceipt.getInvoice().setRecipientLogin(recipientLogin);
//        currentReceipt.getInvoice().setRecipientCargostarAccountNumber(recipientCargo);
//        currentReceipt.getInvoice().setRecipientTntAccountNumber(recipientTnt);
//        currentReceipt.getInvoice().setRecipientFedexAccountNumber(recipientFedex);
//        //payer data
//        currentReceipt.getInvoice().setPayerLogin(payerLogin);
//        currentReceipt.getInvoice().setPayerCargostarAccountNumber(payerCargostar);
//        currentReceipt.getInvoice().setPayerTntAccountNumber(payerTnt);
//        currentReceipt.getInvoice().setPayerFedexAccountNumber(payerFedex);
//        currentReceipt.getInvoice().setCheckingAccount(checkingAccount);
//        currentReceipt.getInvoice().setBank(bank);
//        currentReceipt.getInvoice().setRegistrationCode(registrationCode);
//        currentReceipt.getInvoice().setMfo(mfo);
//        currentReceipt.getInvoice().setOked(oked);
//        currentReceipt.getInvoice().setInstructions(instructions);
//        currentReceipt.getInvoice().setPaymentStatus(PaymentStatus.WAITING_PAYMENT);

//        if (!TextUtils.isEmpty(discount)) {
//            currentReceipt.getInvoice().setDiscount(Integer.parseInt(discount));
//        }
//        if (newSenderAddress != null) {
//            newSenderAddress.setZip(senderZip);
//            currentReceipt.getInvoice().setSenderAddress(newSenderAddress);
//        }
//        if (newRecipientAddress != null) {
//            newRecipientAddress.setZip(recipientZip);
//            currentReceipt.getInvoice().setRecipientAddress(newRecipientAddress);
//        }
//        if (newPayerAddress != null) {
//            newPayerAddress.setZip(payerZip);
//            currentReceipt.getInvoice().setPayerAddress(newPayerAddress);
//        }
//        createParcelViewModel.updateReceipt(currentReceipt.getReceipt());
        Toast.makeText(context, "Данные сохранены успешно", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void createInvoice() {
        final String transportationQr = transportationQrEditText.getText().toString().trim();
        final String instructions = instructionsEditText.getText().toString().trim();

        courierId = String.valueOf(SharedPrefs.getInstance(context).getLong(SharedPrefs.ID));

        //sender data
        if (TextUtils.isEmpty(courierId)) {
            Log.e(TAG, "courierId empty");
            Toast.makeText(context, "Пустой ID курьера", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senderAddress)) {
            Log.e(TAG, "senderAddress empty");
            Toast.makeText(context, "Для создания заявки укажите адрес отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (senderCountry == null) {
            Log.e(TAG, "senderCountry empty");
            Toast.makeText(context, "Для создания заявки укажите страну отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (senderRegion == null) {
            Log.e(TAG, "senderRegion empty");
            Toast.makeText(context, "Для создания заявки укажите регион отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (senderCity == null) {
            Log.e(TAG, "senderCity empty");
            Toast.makeText(context, "Для создания заявки укажите город отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senderZip)) {
            Log.e(TAG, "senderZip empty");
            Toast.makeText(context, "Для создания заявки укажите почтовый индекс отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senderFirstName)) {
            Log.e(TAG, "senderFirstName empty");
            Toast.makeText(context, "Для создания заявки укажите имя отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senderMiddleName)) {
            Log.e(TAG, "senderMiddleName empty");
            Toast.makeText(context, "Для создания заявки укажите отчество отправителя", Toast.LENGTH_SHORT).show();
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
        if (TextUtils.isEmpty(senderCargostar)) {
            Log.e(TAG, "sendInvoice(): senderCargostar is empty");
            Toast.makeText(context, "Для создания заявки укажите номер акканута CargoStar отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        //recipient data
        if (TextUtils.isEmpty(recipientAddress)) {
            Log.e(TAG, "sendInvoice(): recipientAddress is empty");
            Toast.makeText(context, "Для создания заявки укажите адрес получателя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (recipientCountry == null) {
            Log.e(TAG, "sendInvoice(): recipientCountry is empty");
            Toast.makeText(context, "Для создания заявки укажите страну получателя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (recipientRegion == null) {
            Log.e(TAG, "sendInvoice(): recipientRegion is empty");
            Toast.makeText(context, "Для создания заявки укажите регион получателя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (recipientCity == null) {
            Log.e(TAG, "sendInvoice(): recipientCity is empty");
            Toast.makeText(context, "Для создания заявки укажите город получателя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(recipientZip)) {
            Log.e(TAG, "sendInvoice(): recipientZip is empty");
            Toast.makeText(context, "Для создания заявки укажите почтовый индекс получателя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(recipientFirstName)) {
            Log.e(TAG, "sendInvoice(): recipientFirstName is empty");
            Toast.makeText(context, "Для создания заявки укажите имя получателя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(recipientMiddleName)) {
            Log.e(TAG, "sendInvoice(): recipientMiddleName is empty");
            Toast.makeText(context, "Для создания заявки укажите отчество получателя", Toast.LENGTH_SHORT).show();
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
        if (TextUtils.isEmpty(recipientEmail)) {
            Log.e(TAG, "sendInvoice(): recipientEmail is empty");
            Toast.makeText(context, "Для создания заявки укажите email получателя", Toast.LENGTH_SHORT).show();
            return;
        }
        //payer data
        if (TextUtils.isEmpty(payerAddress)) {
            Log.e(TAG, "sendInvoice(): payerAddress is empty");
            Toast.makeText(context, "Для создания заявки укажите адрес плательщика", Toast.LENGTH_SHORT).show();
            return;
        }
        if (payerCountry == null) {
            Log.e(TAG, "sendInvoice(): payerCountry is empty");
            Toast.makeText(context, "Для создания заявки укажите страну плательщика", Toast.LENGTH_SHORT).show();
            return;
        }
        if (payerRegion == null) {
            Log.e(TAG, "sendInvoice(): payerRegion is empty");
            Toast.makeText(context, "Для создания заявки укажите регион плательщика", Toast.LENGTH_SHORT).show();
            return;
        }
        if (payerCity == null) {
            Log.e(TAG, "sendInvoice(): payerCity is empty");
            Toast.makeText(context, "Для создания заявки укажите город плательщика", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(payerZip)) {
            Log.e(TAG, "sendInvoice(): payerZip is empty");
            Toast.makeText(context, "Для создания заявки укажите почтовый индекс плательщика", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(payerFirstName)) {
            Log.e(TAG, "sendInvoice(): payerFirstName is empty");
            Toast.makeText(context, "Для создания заявки укажите имя плательщика", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(payerMiddleName)) {
            Log.e(TAG, "sendInvoice(): payerMiddleName is empty");
            Toast.makeText(context, "Для создания заявки укажите отчество плательщика", Toast.LENGTH_SHORT).show();
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
        if (TextUtils.isEmpty(payerEmail)) {
            Log.e(TAG, "sendInvoice(): payerEmail is empty");
            Toast.makeText(context, "Для создания заявки укажите email плательщика", Toast.LENGTH_SHORT).show();
            return;
        }
        //parcel data
        if (TextUtils.isEmpty(transportationQr)) {
            Toast.makeText(context, "Для создания заявки отсканируйте QR-код", Toast.LENGTH_SHORT).show();
            return;
        }
        if (consignmentList.isEmpty()) {
            Toast.makeText(context, "Для создания заявки добавьте хотя бы 1 груз", Toast.LENGTH_SHORT).show();
            return;
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

        if (paymentMethodRadioGroup.getCheckedRadioButtonId() != cashRadioBtn.getId()
                && paymentMethodRadioGroup.getCheckedRadioButtonId() != terminalRadioBtn.getId()) {
            Toast.makeText(context, "Для создания заявки выберите способ оплаты", Toast.LENGTH_SHORT).show();
            return;
        }
        if (paymentMethodRadioGroup.getCheckedRadioButtonId() == cashRadioBtn.getId()) {
            paymentMethod = 1;
        }
        if (paymentMethodRadioGroup.getCheckedRadioButtonId() == terminalRadioBtn.getId()) {
            paymentMethod = 2;
        }
        final String totalWeightText = totalWeightTextView.getText().toString().trim();
        final String totalVolumeText = totalDimensionsTextView.getText().toString().trim();

        try {
            totalWeight = Double.parseDouble(totalWeightText);
            totalVolume = Double.parseDouble(totalVolumeText);
        }
        catch (Exception e) {
            Log.e(TAG, "createInvoice(): ", e);
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

        final String serializedConsignmentList = new Gson().toJson(consignmentList);

        final UUID sendInvoiceWorkUUID = SyncWorkRequest.sendInvoice(
                context,
                courierId != null && !TextUtils.isEmpty(courierId) ? Long.parseLong(courierId) : -1L,
                operatorId != null && !TextUtils.isEmpty(operatorId) ? Long.parseLong(operatorId) : -1L,
                accountantId != null && !TextUtils.isEmpty(accountantId) ? Long.parseLong(accountantId) : -1L,
                senderSignature,
                senderEmail,
                senderCargostar,
                senderTnt,
                senderFedex,
                senderCountry.getId(),
                senderRegion.getId(),
                senderCity.getId(),
                senderAddress,
                senderZip,
                senderFirstName,
                senderMiddleName,
                senderLastName,
                senderPhone,
                recipientSignature,
                recipientEmail,
                recipientCargo,
                recipientTnt,
                recipientFedex,
                recipientCountry.getId(),
                recipientRegion.getId(),
                recipientCity.getId(),
                recipientAddress,
                recipientZip,
                recipientFirstName,
                recipientMiddleName,
                recipientLastName,
                recipientPhone,
                payerEmail,
                payerCountry.getId(),
                payerRegion.getId(),
                payerCity.getId(),
                payerAddress,
                payerZip,
                payerFirstName,
                payerMiddleName,
                payerLastName,
                payerPhone,
                payerCargostar,
                payerTnt,
                payerFedex,
                discount != null && !TextUtils.isEmpty(discount) ? Double.parseDouble(discount) : -1,
                checkingAccount,
                bank,
                mfo,
                oked,
                registrationCode,
                transportationQr,
                instructions,
                serviceProvider.getId(),
                selectedPackaging.getId(),
                deliveryType,
                paymentMethod,
                totalWeight,
                totalVolume,
                totalPrice,
                serializedConsignmentList);
        WorkManager.getInstance(context).getWorkInfoByIdLiveData(sendInvoiceWorkUUID).observe(this, workInfo -> {
            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                createInvoiceBtn.setEnabled(true);
                progressBar.setVisibility(View.INVISIBLE);

                Log.i(TAG, "createInvoice(): successfully ");
                Toast.makeText(context, "Накладная создана успешно", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(this, MainActivity.class));
                finish();

                return;
            }
            if (workInfo.getState() == WorkInfo.State.CANCELLED || workInfo.getState() == WorkInfo.State.FAILED) {
                createInvoiceBtn.setEnabled(true);
                progressBar.setVisibility(View.INVISIBLE);

                Log.e(TAG, "createInvoice(): ");
                Toast.makeText(context, "Произошла ошибка при создании накладной", Toast.LENGTH_SHORT).show();
                return;
            }
            createInvoiceBtn.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void onDeleteItemClicked(final int position) {
        if (consignmentList.size() <= 0) {
            return;
        }
        Log.i(TAG, "position=" + position + " size" + consignmentList.size());
        consignmentList.remove(position);
        calculatorAdapter.notifyDataSetChanged();

        Log.i(TAG, "itemCount=" + calculatorAdapter.getItemCount());
    }

    @Override
    public void onSenderSignatureClicked() {
        startActivityForResult(new Intent(context, SignatureActivity.class), IntentConstants.REQUEST_SENDER_SIGNATURE);
    }

    @Override
    public void onRecipientSignatureClicked() {
        startActivityForResult(new Intent(context, SignatureActivity.class), IntentConstants.REQUEST_RECIPIENT_SIGNATURE);
    }

    @Override
    public void afterFirstEditTextChanged(int position, Editable editable) {
        switch (position) {
            case 1: {
                //courierId
                courierId = editable.toString();
                Log.i(TAG, "courierId: " + courierId);
                break;
            }
            case 2: {
                //accountantId
                accountantId = editable.toString();
                break;
            }
            case 5: {
                //senderSignature
                senderSignature = editable.toString();
                break;
            }
            case 6: {
                //senderEmail
                senderEmail = editable.toString();
//                createParcelViewModel.selectAddressBookEntriesBySenderLogin(senderLogin).observe(this, this::initPayerAddressBook);

                //todo: fill in Spinner through AddressBook
//                createParcelViewModel.selectCustomerByLogin(senderLogin).observe(this, customer -> {
//                    if (customer != null) {
//                        if (customer.getSignature() != null) {
//                            itemList.get(5).firstValue =  customer.getSignature().getName();
//                            adapter.notifyItemChanged(5);
//                        }
//                        itemList.get(6).firstValue = customer.getAccount().getLogin();
//                        itemList.get(6).secondValue = customer.getCargostarAccountNumber();
//                        adapter.notifyItemChanged(6);
//                        itemList.get(7).firstValue = customer.getTntAccountNumber();
//                        itemList.get(7).secondValue = customer.getFedexAccountNumber();
//                        adapter.notifyItemChanged(7);
//                        itemList.get(8).firstValue = customer.getAddress().getAddress();
//                        itemList.get(8).secondValue = customer.getAddress().getCountry();
//                        adapter.notifyItemChanged(8);
//                        itemList.get(9).firstValue = customer.getAddress().getCity();
//                        itemList.get(9).secondValue = customer.getAddress().getRegion();
//                        adapter.notifyItemChanged(9);
//                        itemList.get(10).firstValue = customer.getAddress().getZip();
//                        itemList.get(10).secondValue = customer.getFirstName();
//                        adapter.notifyItemChanged(10);
//                        itemList.get(11).firstValue = customer.getMiddleName();
//                        itemList.get(11).secondValue = customer.getLastName();
//                        adapter.notifyItemChanged(11);
//                        itemList.get(12).firstValue = customer.getPhone();
//                        itemList.get(12).secondValue = customer.getAccount().getEmail();
//                        adapter.notifyItemChanged(12);
//                    }
//                });
                break;
            }
            case 7: {
                //senderTnt
                senderTnt = editable.toString();
                break;
            }
            case 8: {
                //senderAddress
                senderAddress = editable.toString();
                break;
            }
            case 10: {
                //senderZip
                senderZip = editable.toString();
                break;
            }
            case 11: {
                //senderMiddleName
                senderMiddleName = editable.toString();
                break;
            }
            case 12: {
                //senderPhone
                senderPhone = editable.toString();
                break;
            }
            case 15: {
                //recipientEmail
                recipientEmail = editable.toString();
                break;
            }
            case 16: {
                //recipientTnt
                recipientTnt = editable.toString();
                break;
            }
            case 17: {
                //recipientAddress
                recipientAddress = editable.toString();
                break;
            }
            case 19: {
                //recipientZip
                recipientZip = editable.toString();
                break;
            }
            case 20: {
                //recipientMiddleName
                recipientMiddleName = editable.toString();
                break;
            }
            case 21: {
                //recipientPhone
                recipientPhone = editable.toString();
                break;
            }
            case 25: {
                //payerEmail
                payerEmail = editable.toString();
                break;
            }
            case 27: {
                //payerEmail
                payerAddress = editable.toString();
                break;
            }
            case 28: {
                //payerFirstName
                payerFirstName = editable.toString();
                break;
            }
            case 29: {
                //payerLastName
                payerLastName = editable.toString();
                break;
            }
            case 30: {
                //payerLastName
                discount = editable.toString();
                break;
            }
            case 32: {
                //payerCargo
                payerCargostar = editable.toString();
                break;
            }
            case 33: {
                //payerTnt
                payerTnt = editable.toString();
                break;
            }
            case 34: {
                //payerFedex
                payerFedex = editable.toString();
                break;
            }
            case 37: {
                //checkingAccount
                checkingAccount = editable.toString();
                break;
            }
            case 38: {
                //registrationCode
                registrationCode = editable.toString();
                break;
            }
            case 39: {
                //oked
                oked = editable.toString();
                break;
            }
        }
    }

    @Override
    public void afterSecondEditTextChanged(int position, Editable editable) {
        switch (position) {
            case 1: {
                //operatorId
                operatorId = editable.toString();
                break;
            }
            case 5: {
                //recipientSignature
                recipientSignature = editable.toString();
                break;
            }
            case 6: {
                //senderCargo
                senderCargostar = editable.toString();
                break;
            }
            case 7: {
                //senderFedex
                senderFedex = editable.toString();
                break;
            }
            case 10: {
                //senderFirstName
                senderFirstName = editable.toString();
                break;
            }
            case 11: {
                //senderLastName
                senderLastName = editable.toString();
                break;
            }
            case 15: {
                //recipientCargo
                recipientCargo = editable.toString();
                break;
            }
            case 16: {
                //recipientFedex
                recipientFedex = editable.toString();
                break;
            }
            case 19: {
                //recipientFirstName
                recipientFirstName = editable.toString();
                break;
            }
            case 20: {
                //recipientLastName
                recipientLastName = editable.toString();
                break;
            }
            case 27: {
                //payerZip
                payerZip = editable.toString();
                break;
            }
            case 28: {
                //payerMiddleName
                payerMiddleName = editable.toString();
                break;
            }
            case 29: {
                //payerPhone
                payerPhone = editable.toString();
                break;
            }
            case 33: {
                //retntTaxId
                payerTntTaxId = editable.toString();
                break;
            }
            case 34: {
                //reFedexTaxId
                payerFedexTaxId = editable.toString();
                break;
            }
            case 37: {
                //bank
                bank = editable.toString();
                break;
            }
            case 38: {
                //mfo
                mfo = editable.toString();
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IntentConstants.REQUEST_SCAN_QR_PARCEL) {
            if (resultCode == RESULT_CANCELED) {
                transportationQrEditText.setBackgroundResource(R.drawable.edit_text_locked);
                transportationQrResultImageView.setImageResource(R.drawable.ic_image_red);
                transportationQrResultImageView.setVisibility(View.VISIBLE);
                return;
            }
            if (resultCode == RESULT_OK && data != null) {
                Log.i(TAG, "onActivityResult: " + data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE));
                transportationQrEditText.setText(data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE));
                transportationQrEditText.setBackgroundResource(R.drawable.edit_text_active);
                transportationQrResultImageView.setImageResource(R.drawable.ic_image_green);
                transportationQrResultImageView.setVisibility(View.VISIBLE);
            }
            return;
        }
        if (requestCode == IntentConstants.REQUEST_SCAN_QR_CARGO) {
            Log.i(TAG, "onActivityResult: " + data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE));
            cargoQrEditText.setText(data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE));

            if (resultCode == RESULT_CANCELED) {
                cargoQrEditText.setBackgroundResource(R.drawable.edit_text_locked);
                cargoResultImageView.setImageResource(R.drawable.ic_image_red);
                cargoResultImageView.setVisibility(View.VISIBLE);
                return;
            }
            if (resultCode == RESULT_OK && data != null) {
                Log.i(TAG, "onActivityResult: " + data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE));
                cargoQrEditText.setText(data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE));
                cargoQrEditText.setBackgroundResource(R.drawable.edit_text_active);
                cargoResultImageView.setImageResource(R.drawable.ic_image_green);
                cargoResultImageView.setVisibility(View.VISIBLE);
            }
            return;
        }
        if (requestCode == IntentConstants.REQUEST_SENDER_SIGNATURE) {
            itemList.get(5).firstValue = data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE);
            senderSignature = data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE);
            adapter.notifyItemChanged(5);
            return;
        }
        if (requestCode == IntentConstants.REQUEST_RECIPIENT_SIGNATURE) {
            itemList.get(5).secondValue = data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE);
            recipientSignature = data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE);
            adapter.notifyItemChanged(5);
        }
    }

    private void initPayerAddressBook(final List<AddressBook> addressBook) {
        adapter.setAddressBookEntries(addressBook);
        adapter.notifyItemChanged(24);
    }

    @Override
    public void onSpinnerItemChanged(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i(TAG, "onSpinnerItemChanged: ");
        final List<AddressBook> addressBook = adapter.getAddressBookEntries();

        Log.i(TAG, "onSpinnerItemChanged(): " + addressBook);

        if (addressBook != null && addressBook.get(i) != null) {
//            payerFirstName = addressBook.get(i).getPayerFirstName();
//            payerMiddleName = addressBook.get(i).getPayerMiddleName();
//            payerLastName = addressBook.get(i).getPayerLastName();
//            payerCountry = addressBook.get(i).getPayerAddress().getCountry();
//            payerRegion = addressBook.get(i).getPayerAddress().getRegion();
//            payerCity = addressBook.get(i).getPayerAddress().getCity();
//            payerAddress = addressBook.get(i).getPayerAddress().getAddress();
//            payerEmail = addressBook.get(i).getPayerEmail();
//            payerPhone = addressBook.get(i).getPayerPhone();
//            payerLogin = addressBook.get(i).getPayerLogin();
//            checkingAccount = addressBook.get(i).getPayerCheckingAccount();
//            bank = addressBook.get(i).getPayerBank();
//            mfo = addressBook.get(i).getPayerMfo();
//            oked = addressBook.get(i).getPayerOked();
//            registrationCode = addressBook.get(i).getPayerRegistrationCode();
//            payerCargostar = addressBook.get(i).getCargostarAccountNumber();
//            payerTnt = addressBook.get(i).getTntAccountNumber();
//            payerFedex = addressBook.get(i).getFedexAccountNumber();
//            itemList.get(25).firstValue = payerLogin;
//            adapter.notifyItemChanged(25);
//            itemList.get(26).firstValue = payerAddress;
//            adapter.notifyItemChanged(26);
//            itemList.get(28).firstValue = payerFirstName;
//            itemList.get(28).secondValue = payerMiddleName;
//            adapter.notifyItemChanged(28);
//            itemList.get(29).firstValue = payerLastName;
//            itemList.get(29).secondValue = payerPhone;
//            adapter.notifyItemChanged(29);
//            itemList.get(30).firstValue = payerEmail;
//            adapter.notifyItemChanged(30);
//            itemList.get(31).firstValue = payerCargostar;
//            adapter.notifyItemChanged(31);
//            itemList.get(32).firstValue = payerTnt;
//            adapter.notifyItemChanged(32);
//            itemList.get(33).firstValue = payerFedex;
//            adapter.notifyItemChanged(33);
//            itemList.get(34).firstValue = checkingAccount;
//            itemList.get(34).secondValue = bank;
//            adapter.notifyItemChanged(34);
//            itemList.get(35).firstValue = registrationCode;
//            itemList.get(35).secondValue = mfo;
//            adapter.notifyItemChanged(35);
//            itemList.get(36).firstValue = oked;
//            adapter.notifyItemChanged(36);
        }
    }

    @Override
    public void onSpinnerEditTextItemSelected(final int position, final Object selectedObject) {
        switch (position) {
            case 8: {
                //sender country
                senderCountry = (Country) selectedObject;
                createInvoiceViewModel.setSenderCountryId(senderCountry.getId());
//                calculatorViewModel.setSrcCountryId(senderCountry.getId());

                //country & null = hide all
                if (recipientCountry == null) {
                    selectedCountryId = null;
                    createInvoiceViewModel.setCountryIdProviderId(selectedCountryId, serviceProvider != null ? serviceProvider.getId() : null);

                    firstProviderRadioBtn.setChecked(false);
                    secondProviderRadioBtn.setChecked(false);

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
                        createInvoiceViewModel.setCountryIdProviderId(selectedCountryId, serviceProvider != null ? serviceProvider.getId() : null);

                        firstProviderRadioBtn.setChecked(false);

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
                    createInvoiceViewModel.setCountryIdProviderId(selectedCountryId, serviceProvider != null ? serviceProvider.getId() : null);

                    firstProviderRadioBtn.setChecked(false);
                    secondProviderRadioBtn.setChecked(false);

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
                    createInvoiceViewModel.setCountryIdProviderId(selectedCountryId, serviceProvider != null ? serviceProvider.getId() : null);

                    firstProviderRadioBtn.setChecked(false);
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
                createInvoiceViewModel.setCountryIdProviderId(selectedCountryId, serviceProvider != null ? serviceProvider.getId() : null);

                firstProviderRadioBtn.setChecked(false);
                secondProviderRadioBtn.setChecked(false);

                firstProviderRadioBtn.setText(null);
                secondProviderRadioBtn.setText(null);

                firstProviderRadioBtn.setVisibility(View.INVISIBLE);
                firstProviderImageView.setVisibility(View.INVISIBLE);
                firstProviderCard.setVisibility(View.INVISIBLE);

                secondProviderRadioBtn.setVisibility(View.INVISIBLE);
                secondProviderImageView.setVisibility(View.INVISIBLE);
                secondProviderCard.setVisibility(View.INVISIBLE);
            }
                break;
            case 17: {
                //recipient country
                recipientCountry = (Country) selectedObject;
                createInvoiceViewModel.setRecipientCountryId(recipientCountry.getId());
//                calculatorViewModel.setDestCountryId(recipientCountry.getId());

                //country & null = hide all
                if (senderCountry == null) {
                    selectedCountryId = null;
                    createInvoiceViewModel.setCountryIdProviderId(selectedCountryId, serviceProvider != null ? serviceProvider.getId() : null);

                    firstProviderRadioBtn.setChecked(false);
                    secondProviderRadioBtn.setChecked(false);

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
                        createInvoiceViewModel.setCountryIdProviderId(selectedCountryId, serviceProvider != null ? serviceProvider.getId() : null);

                        firstProviderRadioBtn.setChecked(false);

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
                    createInvoiceViewModel.setCountryIdProviderId(selectedCountryId, serviceProvider != null ? serviceProvider.getId() : null);

                    firstProviderImageView.setImageResource(R.drawable.logo_tnt_cacl);
                    firstProviderRadioBtn.setChecked(false);
                    secondProviderRadioBtn.setChecked(false);

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
                    createInvoiceViewModel.setCountryIdProviderId(selectedCountryId, serviceProvider != null ? serviceProvider.getId() : null);

                    firstProviderRadioBtn.setChecked(false);

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
                createInvoiceViewModel.setCountryIdProviderId(selectedCountryId, serviceProvider != null ? serviceProvider.getId() : null);

                firstProviderRadioBtn.setChecked(false);
                secondProviderRadioBtn.setChecked(false);

                firstProviderRadioBtn.setText(null);
                secondProviderRadioBtn.setText(null);

                firstProviderRadioBtn.setVisibility(View.INVISIBLE);
                firstProviderImageView.setVisibility(View.INVISIBLE);
                firstProviderCard.setVisibility(View.INVISIBLE);

                secondProviderRadioBtn.setVisibility(View.INVISIBLE);
                secondProviderImageView.setVisibility(View.INVISIBLE);
                secondProviderCard.setVisibility(View.INVISIBLE);
                break;
            }
            case 25: {
                //payer country
                payerCountry = (Country) selectedObject;
                createInvoiceViewModel.setPayerCountryId(payerCountry.getId());
                break;
            }
            case 46: {
                //packaging type
//                packagingType = (PackagingType) selectedObject;
//                Log.i(TAG, "selected packaging type: " + packagingType);
//                calculatorViewModel.setPackagingId();
            }
        }
    }

    @Override
    public void onFirstSpinnerItemSelected(final int position, final Region region) {
        switch (position) {
            case 9: {
                //sender region
                senderRegion = region;
                createInvoiceViewModel.setSenderRegionId(region.getId());
                break;
            }
            case 18: {
                //recipient region
                recipientRegion = region;
                createInvoiceViewModel.setRecipientRegionId(region.getId());
                break;
            }
            case 26: {
                //payer region
                payerRegion = region;
                createInvoiceViewModel.setPayerRegionId(region.getId());
                break;
            }
        }
    }

    @Override
    public void onSecondSpinnerItemSelected(final int position, final City city) {
        switch (position) {
            case 9: {
                //sender city
                senderCity = city;
                createInvoiceViewModel.setSenderCityId(city.getId());
                break;
            }
            case 18: {
                //recipient city
                recipientCity = city;
                createInvoiceViewModel.setRecipientCityId(city.getId());
                break;
            }
            case 26: {
                //payer city
                payerCity = city;
                createInvoiceViewModel.setPayerCityId(city.getId());
                break;
            }
        }
    }

    private void initCitySpinner() {

    }

    private static final String TAG = CreateInvoiceActivity.class.toString();
}