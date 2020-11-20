package uz.alexits.cargostar.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import uz.alexits.cargostar.model.shipping.Consignment;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.utils.Regex;
import uz.alexits.cargostar.utils.UiUtils;
import uz.alexits.cargostar.view.adapter.CalculatorAdapter;
import uz.alexits.cargostar.view.adapter.TariffPriceRadioAdapter;
import uz.alexits.cargostar.view.callback.CreateInvoiceCallback;
import uz.alexits.cargostar.view.callback.TariffCallback;
import uz.alexits.cargostar.viewmodel.CalculatorViewModel;
import uz.alexits.cargostar.viewmodel.CreateInvoiceViewModel;
import uz.alexits.cargostar.viewmodel.CourierViewModel;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.adapter.CreateInvoiceAdapter;
import uz.alexits.cargostar.view.adapter.CreateInvoiceData;
import uz.alexits.cargostar.viewmodel.RequestsViewModel;
import uz.alexits.cargostar.workers.SyncWorkRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.INPUT_TYPE_PHONE;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_EDIT_TEXT_IMAGE_VIEW;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_EDIT_TEXT_SPINNER;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_HEADING;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_SINGLE_SPINNER;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_TWO_EDIT_TEXTS;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_SINGLE_EDIT_TEXT;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_STROKE;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.INPUT_TYPE_NUMBER;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.INPUT_TYPE_TEXT;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.INPUT_TYPE_EMAIL;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_TWO_SPINNERS;

public class CreateInvoiceActivity extends AppCompatActivity implements CreateInvoiceCallback, TariffCallback {
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
    private RelativeLayout packagingTypeField;

    private EditText weightEditText;
    private EditText lengthEditText;
    private EditText widthEditText;
    private EditText heightEditText;
    private EditText cargoQrEditText;
    private ImageView cargoQrImageView;
    private ImageView cargoResultImageView;
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

    /* sender data */
    private Country senderCountry;
    private Region senderRegion;
    private City senderCity;

    /* recipient data */
    private Country recipientCountry;
    private Region recipientRegion;
    private City recipientCity;

    /* payer data */
    private Country payerCountry;
    private Region payerRegion;
    private City payerCity;

    /* show current cargoList */
    private CalculatorAdapter calculatorAdapter;
    private RecyclerView itemRecyclerView;

    /* view model */
    private CourierViewModel courierViewModel;
    private RequestsViewModel requestsViewModel;
    private CreateInvoiceViewModel createInvoiceViewModel;
    private CalculatorViewModel calculatorViewModel;

    /* if invoice was created before */
    private long requestId;
    private long invoiceId;

    /* selected items */
    private Long selectedCountryId = null;
    private Provider selectedProvider = null;
    private List<Packaging> tariffList = null;
    private List<Long> selectedPackagingIdList = new ArrayList<>();
    private List<ZoneSettings> selectedZoneSettingsList = null;
    private Vat selectedVat = null;

    private static List<Consignment> consignmentList;
    private static final List<TariffPrice> tariffPriceList = new ArrayList<>();

    private long existingTariffId;
    private String existingTotalPrice;

    private EditText courierIdEditText;

    /* sender data */
    private EditText senderEmailEditText;
    private EditText senderSignatureEditText;
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

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (getIntent() != null && getIntent().getExtras() != null) {
            final long requestId = getIntent().getLongExtra(Constants.KEY_REQUEST_ID, -1L);
            final long invoiceId = getIntent().getLongExtra(Constants.KEY_INVOICE_ID, -1L);
            final long senderId = getIntent().getLongExtra(Constants.KEY_SENDER_ID, -1L);
            final long courierId = getIntent().getLongExtra(Constants.KEY_COURIER_ID, -1L);
            final long providerId = getIntent().getLongExtra(Constants.KEY_PROVIDER_ID, -1L);
            final long senderCountryId = getIntent().getLongExtra(Constants.KEY_SENDER_COUNTRY_ID, -1L);
            final long senderRegionId = getIntent().getLongExtra(Constants.KEY_SENDER_REGION_ID, -1L);
            final long senderCityId = getIntent().getLongExtra(Constants.KEY_SENDER_CITY_ID, -1L);
            final long recipientCountryId = getIntent().getLongExtra(Constants.KEY_RECIPIENT_COUNTRY_ID, -1L);
            final long recipientCityId = getIntent().getLongExtra(Constants.KEY_RECIPIENT_CITY_ID, -1L);
            final int deliveryType = getIntent().getIntExtra(Constants.KEY_DELIVERY_TYPE, 0);
            final String comment = getIntent().getStringExtra(Constants.KEY_COMMENT);
            final int consignmentQuantity = getIntent().getIntExtra(Constants.KEY_CONSIGNMENT_QUANTITY, -1);

            this.requestId = requestId;
            this.invoiceId = invoiceId;

            updateUI(requestId,
                    invoiceId,
                    senderId,
                    courierId,
                    providerId,
                    senderCountryId,
                    senderRegionId,
                    senderCityId,
                    recipientCountryId,
                    recipientCityId,
                    deliveryType,
                    comment,
                    consignmentQuantity);

            createInvoiceBtn.setText(R.string.update_invoice);
        }
        else {
            createInvoiceBtn.setText(R.string.create_invoice);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invoice);

        context = this;
        itemList = new ArrayList<>();
        consignmentList = new ArrayList<>();

        initUI();

        courierViewModel = new ViewModelProvider(this).get(CourierViewModel.class);
        requestsViewModel = new ViewModelProvider(this).get(RequestsViewModel.class);
        createInvoiceViewModel = new ViewModelProvider(this).get(CreateInvoiceViewModel.class);
        calculatorViewModel = new ViewModelProvider(this).get(CalculatorViewModel.class);

        /* header view model */
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

        createInvoiceViewModel.getSenderAddressBook().observe(this, senderAddressBook -> {
            if (senderAddressBook != null) {
                initAddressBook(senderAddressBook);
            }
        });

        createInvoiceViewModel.getSenderData().observe(this, sender -> {
            if (sender != null) {
                this.itemList.get(4).firstValue = sender.getEmail();
                this.itemList.get(4).secondValue = sender.getSignatureUrl();
                this.itemList.get(5).firstValue = sender.getFirstName();
                this.itemList.get(5).secondValue = sender.getLastName();
                this.itemList.get(6).firstValue = sender.getMiddleName();
                this.itemList.get(6).secondValue = sender.getPhone();
                this.itemList.get(7).firstValue = sender.getAddress();

                this.itemList.get(9).firstValue = sender.getZip();
                this.itemList.get(9).secondValue = sender.getCompany();
                this.itemList.get(10).firstValue = sender.getTntAccountNumber();
                this.itemList.get(10).secondValue = sender.getFedexAccountNumber();

                this.adapter.notifyItemRangeChanged(4, 4);
                this.adapter.notifyItemRangeChanged(9, 2);
            }
        });

        createInvoiceViewModel.getSenderUserId().observe(this, senderId -> {
            if (senderId != null) {
                createInvoiceViewModel.setSenderUserId(senderId);
            }
        });

        /* calculator data view model */
        calculatorViewModel.getProvider().observe(this, provider -> {
            selectedProvider = provider;
        });

        calculatorViewModel.getType().observe(this, type -> {
            if (type != null) {
                calculatorViewModel.setTypePackageIdList(type, selectedPackagingIdList);
            }
        });

        calculatorViewModel.getPackagingIds().observe(this, packagingIds -> {
            selectedPackagingIdList = packagingIds;
            if (packageTypeRadioGroup.getCheckedRadioButtonId() == docTypeRadioBtn.getId()) {
                calculatorViewModel.setTypePackageIdList(1, packagingIds);
            }
            else if (packageTypeRadioGroup.getCheckedRadioButtonId() == boxTypeRadioBtn.getId()) {
                calculatorViewModel.setTypePackageIdList(2, packagingIds);
            }
        });

        calculatorViewModel.getPackagingTypeList().observe(this, packagingTypeList -> {
            packagingTypeArrayAdapter.clear();
            packagingTypeArrayAdapter.addAll(packagingTypeList);
            packagingTypeArrayAdapter.notifyDataSetChanged();
        });

        calculatorViewModel.getZoneList().observe(this, zoneList -> {
            if (zoneList != null && !zoneList.isEmpty()) {

                final List<Long> zoneIds = new ArrayList<>();

                for (final Zone zone : zoneList) {
                    zoneIds.add(zone.getId());
                }
                calculatorViewModel.setZoneIds(zoneIds);
            }
        });

        calculatorViewModel.getZoneSettingsList().observe(this, zoneSettingsList -> {
            selectedZoneSettingsList = zoneSettingsList;
        });

        calculatorViewModel.getVat().observe(this, vat -> {
            selectedVat = vat;
        });

        calculatorViewModel.getTariffList().observe(this, packagingList -> {
            tariffList = packagingList;
        });

        /* listeners */
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

                if (firstProviderRadioBtn.getText().equals(getString(R.string.cargostar))) {
                    Log.i(TAG, "checked(): cargostar");

                    calculatorViewModel.setProviderId(6L);
                    calculatorViewModel.setCountryIdProviderId(selectedCountryId, 6L);
                    return;
                }
                if (firstProviderRadioBtn.getText().equals(getString(R.string.tnt))) {
                    Log.i(TAG, "checked(): tnt");

                    calculatorViewModel.setProviderId(5L);
                    calculatorViewModel.setCountryIdProviderId(selectedCountryId, 5L);
                }
            }
        });

        secondProviderRadioBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                firstProviderRadioBtn.setChecked(false);
                //only fedex case
                Log.i(TAG, "checked(): fedex");
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
                final PackagingType selectedPackagingType = (PackagingType) adapterView.getSelectedItem();

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
        itemList.add(new CreateInvoiceData(getString(R.string.courier_id), null, String.valueOf(SharedPrefs.getInstance(context).getLong(SharedPrefs.ID)), null,
                TYPE_SINGLE_EDIT_TEXT, INPUT_TYPE_TEXT, -1, false, false));
        itemList.add(new CreateInvoiceData(null, null, null, null, TYPE_STROKE));

        /* sender data */
        itemList.add(new CreateInvoiceData(getString(R.string.sender_data), null, null, null, TYPE_HEADING));
        itemList.add(new CreateInvoiceData(getString(R.string.email), getString(R.string.sender_signature), null, null,
                TYPE_EDIT_TEXT_IMAGE_VIEW, INPUT_TYPE_EMAIL, -1, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.first_name), getString(R.string.last_name), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_TEXT, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.middle_name), getString(R.string.phone_number), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_TEXT, INPUT_TYPE_PHONE, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.take_address), getString(R.string.country), null, null,
                TYPE_EDIT_TEXT_SPINNER, INPUT_TYPE_TEXT, -1, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.region), getString(R.string.city), null, null,
                TYPE_TWO_SPINNERS, -1, -1, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.post_index), getString(R.string.company_name), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.tnt_account_number), getString(R.string.fedex_account_number), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_NUMBER, true, true));
        itemList.add(new CreateInvoiceData(null, null, null, null, TYPE_STROKE));

        /* recipient data */
        itemList.add(new CreateInvoiceData(getString(R.string.receiver_data), null, null, null, TYPE_HEADING));
        itemList.add(new CreateInvoiceData(getString(R.string.address_book), null, null, null, TYPE_SINGLE_SPINNER));
        itemList.add(new CreateInvoiceData(getString(R.string.email), getString(R.string.first_name), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_EMAIL, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.last_name), getString(R.string.middle_name), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_TEXT, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.delivery_address), getString(R.string.country), null, null,
                TYPE_EDIT_TEXT_SPINNER, INPUT_TYPE_TEXT, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.region), getString(R.string.city), null, null,
                TYPE_TWO_SPINNERS, INPUT_TYPE_TEXT, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.post_index), getString(R.string.phone_number), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_PHONE, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.cargostar_account_number), getString(R.string.tnt_account_number), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_NUMBER, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.fedex_account_number), getString(R.string.company_name), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateInvoiceData(null, null, null, null, TYPE_STROKE));

        /* payer data */
        itemList.add(new CreateInvoiceData(getString(R.string.payer_data), null, null, null, TYPE_HEADING));
        itemList.add(new CreateInvoiceData(getString(R.string.address_book), null, null, null, TYPE_SINGLE_SPINNER));
        itemList.add(new CreateInvoiceData(getString(R.string.email), getString(R.string.first_name), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_EMAIL, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.last_name), getString(R.string.middle_name), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_TEXT, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.payer_address), getString(R.string.country), null, null,
                TYPE_EDIT_TEXT_SPINNER, INPUT_TYPE_TEXT, -1, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.region), getString(R.string.city), null, null,
                TYPE_TWO_SPINNERS, INPUT_TYPE_TEXT, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.post_index), getString(R.string.phone_number), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_PHONE, true, true));
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
        itemList.add(new CreateInvoiceData(getString(R.string.INN), getString(R.string.company_name),null,null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.checking_account), getString(R.string.bank),null,null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.payer_registration_code), getString(R.string.mfo), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_NUMBER, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.oked), null, null, null,
                TYPE_SINGLE_EDIT_TEXT, INPUT_TYPE_NUMBER, -1, true, false));
        itemList.add(new CreateInvoiceData(null, null, null, null, TYPE_STROKE));

        contentRecyclerView = findViewById(R.id.content_recycler_view);
        adapter = new CreateInvoiceAdapter(context, this);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        contentRecyclerView.setHasFixedSize(true);

        contentRecyclerView.setLayoutManager(linearLayoutManager);
        contentRecyclerView.setAdapter(adapter);

        adapter.setItemList(itemList);

        //below recycler view
        totalQuantityTextView = findViewById(R.id.total_quantity_value_text_view);
        totalWeightTextView = findViewById(R.id.total_weight_value_text_view);
        totalDimensionsTextView = findViewById(R.id.total_dimensions_value_text_view);
        calculateBtn = findViewById(R.id.calculate_btn);

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
        cargoNameEditText = findViewById(R.id.cargo_name_edit_text);
        cargoPriceEditText = findViewById(R.id.cargo_price_edit_text);
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
        paymentMethodRadioGroup = findViewById(R.id.payment_method_radio_group);
        cashRadioBtn = findViewById(R.id.cash_radio_btn);
        terminalRadioBtn = findViewById(R.id.terminal_radio_btn);
        transferRadioBtn = findViewById(R.id.transfer_radio_btn);
        corporateRadioBtn = findViewById(R.id.corporate_radio_btn);

        createInvoiceBtn = findViewById(R.id.create_receipt_btn);

        packagingTypeArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new ArrayList<>());
        packagingTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        packagingTypeSpinner.setAdapter(packagingTypeArrayAdapter);

        final LinearLayoutManager tariffPriceLayoutManager = new LinearLayoutManager(context);
        tariffPriceLayoutManager.setOrientation(RecyclerView.VERTICAL);

        tariffPriceRecyclerView = findViewById(R.id.tariff_price_recycler_view);
        tariffPriceRadioAdapter = new TariffPriceRadioAdapter(context, this);
        tariffPriceRecyclerView.setLayoutManager(tariffPriceLayoutManager);
        tariffPriceRecyclerView.setAdapter(tariffPriceRadioAdapter);

        instructionsEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(instructionsEditText, hasFocus);
        });

        weightEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(weightEditText, hasFocus);
        });

        lengthEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(lengthEditText, hasFocus);
        });

        widthEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(widthEditText, hasFocus);
        });

        heightEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(heightEditText, hasFocus);
        });

        cargoNameEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(cargoNameEditText, hasFocus);
        });

        cargoDescriptionEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(cargoDescriptionEditText, hasFocus);
        });

        cargoPriceEditText.setOnFocusChangeListener((v, hasFocus) -> {
            UiUtils.onFocusChanged(cargoPriceEditText, hasFocus);
        });
    }

    private void updateUI(final long requestId,
                          final long invoiceId,
                          final long senderId,
                          final long courierId,
                          final long providerId,
                          final long senderCountryId,
                          final long senderRegionId,
                          final long senderCityId,
                          final long recipientCountryId,
                          final long recipientCityId,
                          final int deliveryType,
                          final String comment,
                          final int consignmentQuantity) {
        requestsViewModel.setRequestId(requestId);
        requestsViewModel.setInvoiceId(invoiceId);
        requestsViewModel.setSenderId(senderId);
        requestsViewModel.setCourierId(courierId);
        requestsViewModel.setProviderId(providerId);
        requestsViewModel.setSenderCountryId(senderCountryId);
        requestsViewModel.setSenderRegionId(senderRegionId);
        requestsViewModel.setSenderCityId(senderCityId);
        requestsViewModel.setRecipientCountryId(recipientCountryId);
        requestsViewModel.setRecipientCityId(recipientCityId);

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

        calculatorViewModel.setProviderId(providerId);

        if (deliveryType == 1) {
            docTypeRadioBtn.setChecked(true);
            calculatorViewModel.setType(1);
        }
        else if (deliveryType == 2) {
            boxTypeRadioBtn.setChecked(true);
            calculatorViewModel.setType(2);
        }

        requestsViewModel.getProvider().observe(this, provider -> {
            if (provider != null) {
                this.selectedProvider = provider;
            }
        });

        requestsViewModel.getInvoice().observe(this, invoice -> {
            if (invoice != null) {
                requestsViewModel.setTariffId(invoice.getTariffId());
                requestsViewModel.setRecipientId(invoice.getRecipientId());
                requestsViewModel.setPayerId(invoice.getPayerId());
                existingTariffId = invoice.getTariffId();
                existingTotalPrice = String.valueOf(invoice.getPrice());
            }
        });

        requestsViewModel.getSender().observe(this, this::updateSenderData);

        requestsViewModel.getRecipient().observe(this, this::updateRecipientData);

        requestsViewModel.getPayer().observe(this, this::updatePayerData);

        requestsViewModel.getTransportation().observe(this, transportation -> {
            if (transportation != null) {
                instructionsEditText.setText(transportation.getInstructions());
            }
        });

        requestsViewModel.getConsignmentList().observe(this, consignments -> {
            if (consignments != null) {
                for (final Consignment consignment : consignments) {
                    consignment.setPackagingType(consignment.getPackagingId() != null ? String.valueOf(consignment.getPackagingId()) : null);
                }
                consignmentList = consignments;
                calculatorAdapter.setItemList(consignmentList);
                calculatorAdapter.notifyDataSetChanged();
            }
        });
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
        if (TextUtils.isEmpty(consignmentQr)) {
            Toast.makeText(context, "Отсканируйте груз", Toast.LENGTH_SHORT).show();
            return;
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
                consignmentQr));
        calculatorAdapter.notifyItemInserted(consignmentList.size() - 1);
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
        double totalLength = 0.0;
        double totalWidth = 0.0;
        double totalHeight = 0.0;

        for (final Consignment item : consignmentList) {
            totalWeight += item.getWeight();
            totalLength += item.getLength();
            totalWidth += item.getWidth();
            totalHeight += item.getHeight();
        }
        if (packageTypeRadioGroup.getCheckedRadioButtonId() == boxTypeRadioBtn.getId()) {
            totalVolume = totalLength * totalWidth * totalHeight;
        }

        if (selectedZoneSettingsList.isEmpty()) {
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
            tariffPriceList.add(new TariffPrice(correspondingTariff.getName(), String.valueOf((int) totalPrice), correspondingTariff.getId()));
        }
        totalQuantityTextView.setText(String.valueOf(totalQuantity));
        totalWeightTextView.setText(String.valueOf(new BigDecimal(Double.toString(totalWeight)).setScale(2, RoundingMode.HALF_UP).doubleValue()));
        totalDimensionsTextView.setText(String.valueOf(new BigDecimal(Double.toString(totalVolume)).setScale(2, RoundingMode.HALF_UP).doubleValue()));

        tariffPriceRadioAdapter.setItemList(tariffPriceList);
    }

    private void createInvoice() {
        final String courierId = courierIdEditText.getText().toString();

        /* sender data */
        final String senderEmail = senderEmailEditText.getText().toString().trim();
        final String senderSignature = senderSignatureEditText.getText().toString().trim();
        final String senderFirstName = senderFirstNameEditText.getText().toString().trim();
        final String senderLastName = senderLastNameEditText.getText().toString().trim();
        final String senderMiddleName = senderMiddleNameEditText.getText().toString().trim();
        final String senderPhone = senderPhoneEditText.getText().toString().trim();
        final String senderAddress = senderAddressEditText.getText().toString().trim();
        final String senderCountryId = senderCountry != null ? String.valueOf(senderCountry.getId()) : null;
        final String senderRegionId = senderRegion != null ? String.valueOf(senderRegion.getId()) : null;
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
        final String recipientRegionId = recipientRegion != null ? String.valueOf(recipientRegion.getId()) : null;
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
        final String payerRegionId = payerRegion != null ? String.valueOf(payerRegion.getId()) : null;
        final String payerCityId = payerCity != null ? String.valueOf(payerCity.getId()) : null;
        final String payerZip = payerZipEditText.getText().toString().trim();
        final String payerPhone = payerPhoneEditText.getText().toString().trim();
        final String payerDiscount = discountEditText.getText().toString().trim();
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

        if (TextUtils.isEmpty(courierId)) {
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
        if (TextUtils.isEmpty(senderCityId)) {
            Log.e(TAG, "senderCity empty");
            Toast.makeText(context, "Для создания заявки укажите город отправителя", Toast.LENGTH_SHORT).show();
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
        if (TextUtils.isEmpty(recipientEmail)) {
            Log.e(TAG, "sendInvoice(): recipientEmail is empty");
            Toast.makeText(context, "Для создания заявки укажите email получателя", Toast.LENGTH_SHORT).show();
            return;
        }
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
        if (TextUtils.isEmpty(recipientCityId)) {
            Log.e(TAG, "sendInvoice(): recipientCity is empty");
            Toast.makeText(context, "Для создания заявки укажите город получателя", Toast.LENGTH_SHORT).show();
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
        if (TextUtils.isEmpty(payerEmail)) {
            Log.e(TAG, "sendInvoice(): payerEmail is empty");
            Toast.makeText(context, "Для создания заявки укажите email плательщика", Toast.LENGTH_SHORT).show();
            return;
        }
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
        if (TextUtils.isEmpty(payerCityId)) {
            Log.e(TAG, "sendInvoice(): payerCity is empty");
            Toast.makeText(context, "Для создания заявки укажите город плательщика", Toast.LENGTH_SHORT).show();
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

        for (final CreateInvoiceData invoiceData : itemList) {
            Log.i(TAG, "createInvoice(): invoiceData=" + invoiceData);
        }

        final UUID sendInvoiceWorkUUID = SyncWorkRequest.sendInvoice(
                context,
                requestId,
                invoiceId,
                !TextUtils.isEmpty(courierId) ? Long.parseLong(courierId) : -1L,
                !TextUtils.isEmpty(senderSignature) ? senderSignature : null,
                invoiceId > 0,
                senderEmail,
                senderTnt,
                senderFedex,
                senderCountryId,
                senderRegionId,
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
                recipientRegionId,
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
                payerRegionId,
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
                !TextUtils.isEmpty(payerDiscount) ? Double.parseDouble(payerDiscount) : -1,
                payerInn,
                checkingAccount,
                bank,
                mfo,
                oked,
                registrationCode,
                instructions,
                selectedProvider.getId(),
                tariffPriceRadioAdapter.getSelectedPackagingId() <= 0 ? existingTariffId : tariffPriceRadioAdapter.getSelectedPackagingId(),
                deliveryType,
                paymentMethod,
                totalWeight,
                totalVolume,
                tariffPriceRadioAdapter.getSelectedPrice() == null ? existingTotalPrice : tariffPriceRadioAdapter.getSelectedPrice(),
                serializedConsignmentList);

        WorkManager.getInstance(context).getWorkInfoByIdLiveData(sendInvoiceWorkUUID).observe(this, workInfo -> {
            if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                createInvoiceBtn.setEnabled(true);
                progressBar.setVisibility(View.INVISIBLE);

                final String successMsg = invoiceId > 0 ? "Накладная успешно изменена" : "Накладная создана успешно";

                Toast.makeText(context, successMsg, Toast.LENGTH_SHORT).show();

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
        consignmentList.remove(position);
        calculatorAdapter.notifyDataSetChanged();
    }

    @Override
    public void bindEditTextSpinner(int position, EditText editText) {
        if (position == 7) {
            //senderAddress
            senderAddressEditText = editText;
            return;
        }
        if (position == 16) {
            //recipientAddress
            recipientAddressEditText = editText;
            return;
        }
        if (position == 26) {
            //payerAddress
            payerAddressEditText = editText;
        }
    }

    @Override
    public void bindEditTextImageView(int position, final EditText firstEditText, final EditText secondEditText) {
        if (position == 4) {
            senderEmailEditText = firstEditText;
            senderSignatureEditText = secondEditText;
        }
    }

    @Override
    public void bindTwoEditTexts(int position, final EditText firstEditText, final EditText secondEditText) {
        if (position == 1) {
            courierIdEditText = firstEditText;
            return;
        }
        if (position == 5) {
            senderFirstNameEditText = firstEditText;
            senderLastNameEditText = secondEditText;
            return;
        }
        if (position == 6) {
            senderMiddleNameEditText = firstEditText;
            senderPhoneEditText = secondEditText;
            return;
        }
        if (position == 9) {
            senderZipEditText = firstEditText;
            senderCompanyEditText = secondEditText;
            return;
        }
        if (position == 10) {
            senderTntEditText = firstEditText;
            senderFedexEditText = secondEditText;
            return;
        }
        if (position == 14) {
            recipientEmailEditText = firstEditText;
            recipientFirstNameEditText = secondEditText;
            return;
        }
        if (position == 15) {
            recipientLastNameEditText = firstEditText;
            recipientMiddleNameEditText = secondEditText;
            return;
        }
        if (position == 18) {
            recipientZipEditText = firstEditText;
            recipientPhoneEditText = secondEditText;
            return;
        }
        if (position == 19) {
            recipientCargoEditText = firstEditText;
            recipientTntEditText = secondEditText;
            return;
        }
        if (position == 20) {
            recipientFedexEditText = firstEditText;
            recipientCompanyEditText = secondEditText;
            return;
        }
        if (position == 24) {
            payerEmailEditText = firstEditText;
            payerFirstNameEditText = secondEditText;
            return;
        }
        if (position == 25) {
            payerLastNameEditText = firstEditText;
            payerMiddleNameEditText = secondEditText;
            return;
        }
        if (position == 28) {
            payerZipEditText = firstEditText;
            payerPhoneEditText = secondEditText;
            return;
        }
        if (position == 29) {
            //discount
            discountEditText = firstEditText;
            return;
        }
        if (position == 31) {
            //payerCargo
            payerCargoEditText = firstEditText;
            return;
        }
        if (position == 32) {
            payerTntEditText = firstEditText;
            payerTntTaxIdEditText = secondEditText;
            return;
        }
        if (position == 33) {
            payerFedexEditText = firstEditText;
            payerFedexTaxIdEditText = secondEditText;
            return;
        }
        if (position == 36) {
            payerInnEditText = firstEditText;
            payerCompanyEditText = secondEditText;
            return;
        }
        if (position == 37) {
            checkingAccountEditText = firstEditText;
            bankEditText = secondEditText;
            return;
        }
        if (position == 38) {
            registrationCodeEditText = firstEditText;
            mfoEditText = secondEditText;
        }
        if (position == 39) {
            //payer oked
            okedEditText = firstEditText;
        }
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
    public void afterFirstEditTextChanged(final int position, final CharSequence editable) {
        if (position == 4) {
            //senderEmail
            final String senderEmail = editable.toString();
            itemList.get(4).firstValue = senderEmail;

            if (createInvoiceViewModel != null) {
                createInvoiceViewModel.setSenderEmail(senderEmail);
            }
        }
    }

    @Override
    public void afterSecondEditTextChanged(final int position, final CharSequence editable) {
        if (position == 36) {
            //payerCompanyName
            itemList.get(36).secondValue = editable.toString();
            if (TextUtils.isEmpty(itemList.get(36).secondValue)) {
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IntentConstants.REQUEST_SCAN_QR_CARGO) {
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
            if (resultCode == RESULT_CANCELED) {
                return;
            }
            if (resultCode == RESULT_OK && data != null) {
                itemList.get(4).secondValue = data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE);
                adapter.notifyItemChanged(4);
            }
        }
    }

    private void initAddressBook(final List<AddressBook> addressBook) {
        adapter.setAddressBookEntries(addressBook);
        adapter.notifyItemChanged(13);
        adapter.notifyItemChanged(23);
    }

    @Override
    public void onBigSpinnerItemSelected(final int position, final Object selectedObject) {
        final AddressBook entry = (AddressBook) selectedObject;

        if (position == 13) {
            updateRecipientData(entry);
            return;
        }
        if (position == 23) {
            updatePayerData(entry);
        }
    }

    private void updateSenderData(final Customer sender) {
        if (sender != null) {
            requestsViewModel.setSenderCountryId(sender.getCountryId());
            requestsViewModel.setSenderRegionId(sender.getRegionId());
            requestsViewModel.setSenderCityId(sender.getCityId());

            itemList.get(4).firstValue = sender.getEmail();
            itemList.get(4).secondValue = sender.getSignatureUrl();
            itemList.get(5).firstValue = sender.getFirstName();
            itemList.get(5).secondValue = sender.getLastName();
            itemList.get(6).firstValue = sender.getMiddleName();
            itemList.get(6).secondValue = sender.getPhone();
            itemList.get(7).firstValue = sender.getAddress();
            itemList.get(7).secondValue = String.valueOf(sender.getCountryId());
            itemList.get(8).firstValue = String.valueOf(sender.getRegionId());
            itemList.get(8).secondValue = String.valueOf(sender.getCityId());
            itemList.get(9).firstValue = sender.getZip();
            itemList.get(9).secondValue = sender.getCompany();
            itemList.get(10).firstValue = sender.getTntAccountNumber();
            itemList.get(10).secondValue = sender.getFedexAccountNumber();

            adapter.notifyItemRangeChanged(4, 7);
        }
    }

    private void updateRecipientData(final AddressBook recipient) {
        if (recipient != null) {
            requestsViewModel.setRecipientCountryId(recipient.getCountryId());
            requestsViewModel.setRecipientRegionId(recipient.getRegionId());
            requestsViewModel.setRecipientCityId(recipient.getCityId());

            itemList.get(14).firstValue = recipient.getEmail();
            itemList.get(14).secondValue = recipient.getFirstName();
            itemList.get(15).firstValue = recipient.getLastName();
            itemList.get(15).secondValue = recipient.getMiddleName();
            itemList.get(16).firstValue = recipient.getAddress();
            itemList.get(16).secondValue = String.valueOf(recipient.getCountryId());
            itemList.get(17).firstValue = String.valueOf(recipient.getRegionId());
            itemList.get(17).secondValue = String.valueOf(recipient.getCityId());
            itemList.get(18).firstValue = recipient.getZip();
            itemList.get(18).secondValue = recipient.getPhone();
            itemList.get(19).firstValue = recipient.getCargostarAccountNumber();
            itemList.get(19).secondValue = recipient.getTntAccountNumber();
            itemList.get(20).firstValue = recipient.getFedexAccountNumber();
            itemList.get(20).secondValue = recipient.getCompany();

            adapter.notifyItemRangeChanged(14, 7);
        }
    }

    private void updatePayerData(final AddressBook payer) {
        if (payer != null) {
            requestsViewModel.setPayerCountryId(payer.getCountryId());
            requestsViewModel.setPayerRegionId(payer.getRegionId());
            requestsViewModel.setPayerCityId(payer.getCityId());

            itemList.get(24).firstValue = payer.getEmail();
            itemList.get(24).secondValue = payer.getFirstName();
            itemList.get(25).firstValue = payer.getLastName();
            itemList.get(25).secondValue = payer.getMiddleName();
            itemList.get(26).firstValue = payer.getAddress();
            itemList.get(26).secondValue = String.valueOf(payer.getCountryId());
            itemList.get(27).firstValue = String.valueOf(payer.getRegionId());
            itemList.get(27).secondValue = String.valueOf(payer.getCityId());
            itemList.get(28).firstValue = payer.getZip();
            itemList.get(28).secondValue = payer.getPhone();
            itemList.get(31).firstValue = payer.getCargostarAccountNumber();
            itemList.get(32).firstValue = payer.getTntAccountNumber();
            itemList.get(33).firstValue = payer.getFedexAccountNumber();
            itemList.get(36).firstValue = payer.getInn();
            itemList.get(36).secondValue = payer.getCompany();
            itemList.get(37).firstValue = payer.getCheckingAccount();
            itemList.get(37).secondValue = payer.getBank();
            itemList.get(38).firstValue = payer.getRegistrationCode();
            itemList.get(38).secondValue = payer.getMfo();
            itemList.get(39).firstValue = payer.getOked();

            this.adapter.notifyItemRangeChanged(24, 16);
        }
    }

    @Override
    public void onSpinnerEditTextItemSelected(final int position, final Object selectedObject) {
        switch (position) {
            case 7: {
                //sender country
                senderCountry = (Country) selectedObject;
                createInvoiceViewModel.setSenderCountryId(senderCountry.getId());
                calculatorViewModel.setSrcCountryId(senderCountry.getId());

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
                break;
            case 16: {
                //recipient country
                recipientCountry = (Country) selectedObject;
                createInvoiceViewModel.setRecipientCountryId(recipientCountry.getId());
                calculatorViewModel.setDestCountryId(recipientCountry.getId());

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
                break;
            }
            case 26: {
                //payer country
                payerCountry = (Country) selectedObject;
                createInvoiceViewModel.setPayerCountryId(payerCountry.getId());
                break;
            }
        }
    }

    @Override
    public void onFirstSpinnerItemSelected(final int position, final Region region) {
        switch (position) {
            case 8: {
                //sender region
                senderRegion = region;
                createInvoiceViewModel.setSenderRegionId(region.getId());
                break;
            }
            case 17: {
                //recipient region
                recipientRegion = region;
                createInvoiceViewModel.setRecipientRegionId(region.getId());
                break;
            }
            case 27: {
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
            case 8: {
                //sender city
                senderCity = city;
                createInvoiceViewModel.setSenderCityId(city.getId());
                break;
            }
            case 17: {
                //recipient city
                recipientCity = city;
                createInvoiceViewModel.setRecipientCityId(city.getId());
                break;
            }
            case 27: {
                //payer city
                payerCity = city;
                createInvoiceViewModel.setPayerCityId(city.getId());
                break;
            }
        }
    }

    @Override
    public void onTariffSelected(int position, int lastCheckedPosition) {
        tariffPriceRadioAdapter.setLastCheckedPosition(position);
        tariffPriceRadioAdapter.notifyDataSetChanged();
    }

    private static final String TAG = CreateInvoiceActivity.class.toString();
}