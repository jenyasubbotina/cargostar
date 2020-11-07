package uz.alexits.cargostar.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uz.alexits.cargostar.R;

import uz.alexits.cargostar.api.RetrofitClient;
import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.actor.AddressBook;
import uz.alexits.cargostar.model.calculation.Packaging;
import uz.alexits.cargostar.model.calculation.Provider;
import uz.alexits.cargostar.model.calculation.Zone;
import uz.alexits.cargostar.model.calculation.ZoneSettings;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.model.shipping.Cargo;
import uz.alexits.cargostar.model.shipping.Invoice;
import uz.alexits.cargostar.utils.ImageSerializer;
import uz.alexits.cargostar.view.callback.CreateInvoiceCallback;
import uz.alexits.cargostar.viewmodel.CalculatorViewModel;
import uz.alexits.cargostar.viewmodel.CreateInvoiceViewModel;
import uz.alexits.cargostar.viewmodel.CourierViewModel;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.adapter.CreateInvoiceAdapter;
import uz.alexits.cargostar.view.adapter.CreateInvoiceData;
import java.util.ArrayList;
import java.util.List;

import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.INPUT_TYPE_PHONE;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_BUTTON;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_CALCULATOR_ITEM;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_EDIT_TEXT_SPINNER;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_HEADING;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_ONE_IMAGE_EDIT_TEXT;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_SINGLE_IMAGE_EDIT_TEXT;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_SINGLE_SPINNER;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_TWO_CARD_VIEWS;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_TWO_EDIT_TEXTS;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_SINGLE_EDIT_TEXT;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_STROKE;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_TWO_IMAGE_EDIT_TEXTS;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.INPUT_TYPE_NUMBER;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.INPUT_TYPE_TEXT;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.INPUT_TYPE_EMAIL;
import static uz.alexits.cargostar.view.adapter.CreateInvoiceData.TYPE_TWO_RADIO_BTNS;
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
    private RecyclerView contentRecyclerView;

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
    private Button saveReceiptBtn;
    private Button createReceiptBtn;
    private CreateInvoiceAdapter adapter;

    //EditText watcher values
    private String courierId;
    private String operatorId;
    private String accountantId;
    private String senderSignature;
    private String recipientSignature;
    private String senderLogin;
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
    private String recipientLogin;
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
    private String payerLogin;
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
    private String qr;
    private String instructions;
    private String description;
    private String packageType;
    private String weight;
    private String length;
    private String width;
    private String height;
    private String cargoQr;

    /* view model */
    private CourierViewModel courierViewModel;
    private CreateInvoiceViewModel createInvoiceViewModel;
    private CalculatorViewModel calculatorViewModel;

    /* selected items for calculations */
    private static Long selectedCountryId = null;
    private static Integer selectedPackageType = null;
    private static Packaging selectedPackaging = null;
    private static List<Long> selectedPackagingIdList = null;
    private static List<ZoneSettings> selectedZoneSettingsList = null;

    private static final List<Cargo> cargoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invoice);
        context = this;

        itemList = new ArrayList<>();

        courierViewModel = new ViewModelProvider(this).get(CourierViewModel.class);
        createInvoiceViewModel = new ViewModelProvider(this).get(CreateInvoiceViewModel.class);
        calculatorViewModel = new ViewModelProvider(this).get(CalculatorViewModel.class);

        if (getIntent() != null) {
            final int requestKey = getIntent().getIntExtra(IntentConstants.INTENT_REQUEST_KEY, -1);
            final long requestId = getIntent().getLongExtra(IntentConstants.INTENT_REQUEST_VALUE, -1);
            final long requestOrParcel =  getIntent().getLongExtra(IntentConstants.INTENT_REQUEST_OR_PARCEL, -1);

            initUI();

            /* header data view model */
            courierViewModel.selectRequest(requestId).observe(this, receipt -> {
                if (receipt != null) {
                    if (requestOrParcel == IntentConstants.INTENT_REQUEST) {
                        saveReceiptBtn.setVisibility(View.VISIBLE);
                        createReceiptBtn.setVisibility(View.VISIBLE);
                    }
                    else if (requestOrParcel == IntentConstants.INTENT_PARCEL) {
                        saveReceiptBtn.setVisibility(View.VISIBLE);
                        createReceiptBtn.setVisibility(View.INVISIBLE);
                    }
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
                }
                if (requestKey != IntentConstants.REQUEST_EDIT_PARCEL) {
                    saveReceiptBtn.setVisibility(View.INVISIBLE);
                    createReceiptBtn.setVisibility(View.VISIBLE);
                }
            });

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
        calculatorViewModel.getProvider().observe(this, provider -> {
            Log.i(TAG, "provider: " + provider);
            serviceProvider = provider;
        });

        calculatorViewModel.getType().observe(this, type -> {
            Log.i(TAG, "type: " + type);
            calculatorViewModel.setTypePackageIdList(type, selectedPackagingIdList);
        });

        calculatorViewModel.getPackagingIds().observe(this, packagingIds -> {
            Log.i(TAG, "packagingIds: " + packagingIds);
            selectedPackagingIdList = packagingIds;
            calculatorViewModel.setTypePackageIdList(selectedPackageType, packagingIds);
        });

        calculatorViewModel.getPackagingTypeList().observe(this, packagingTypeList -> {
            Log.i(TAG, "packagingTypeList: " + packagingTypeList);
            adapter.setPackagingTypeList(packagingTypeList);
        });

        calculatorViewModel.getPackaging().observe(this, packaging -> {
            Log.i(TAG, "packaging: " + packaging);
            selectedPackaging = packaging;
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
            Log.i(TAG, "zoneSettingsList: " + zoneSettingsList);
            selectedZoneSettingsList = zoneSettingsList;
        });

        parcelSearchImageView.setOnClickListener(v -> {
            final String parcelIdStr = parcelSearchEditText.getText().toString();
            if (TextUtils.isEmpty(parcelIdStr)) {
                Toast.makeText(context, "Введите ID перевозки или номер накладной", Toast.LENGTH_SHORT).show();
                return;
            }

            final long parcelId = Long.parseLong(parcelIdStr);

            //todo: refactor this
            courierViewModel.selectRequest(parcelId).observe(this, receiptWithCargoList -> {
                if (receiptWithCargoList == null) {
                    Toast.makeText(context, "Накладной не существует", Toast.LENGTH_SHORT).show();
                    return;
                }
                //todo: handle invoice by request
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

        saveReceiptBtn.setOnClickListener(v -> {
            saveReceipt();
        });

        createReceiptBtn.setOnClickListener(v -> {
            sendInvoice();
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
        itemList.add(new CreateInvoiceData(getString(R.string.login_email), getString(R.string.cargostar_account_number), null, null,
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
        itemList.add(new CreateInvoiceData(getString(R.string.phone_number), getString(R.string.email), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_PHONE, INPUT_TYPE_EMAIL, true, true));
        itemList.add(new CreateInvoiceData(null, null, null, null, TYPE_STROKE));

        /* recipient data */
        itemList.add(new CreateInvoiceData(getString(R.string.receiver_data), null, null, null, TYPE_HEADING));
        itemList.add(new CreateInvoiceData(getString(R.string.login_email), getString(R.string.cargostar_account_number), null, null,
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
        itemList.add(new CreateInvoiceData(getString(R.string.phone_number), getString(R.string.email), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_PHONE, INPUT_TYPE_EMAIL, true, true));
        itemList.add(new CreateInvoiceData(null, null, null, null, TYPE_STROKE));

        /* payer data */
        itemList.add(new CreateInvoiceData(getString(R.string.payer_data), null, null, null, TYPE_HEADING));
        itemList.add(new CreateInvoiceData(getString(R.string.address_book), null, null, null, TYPE_SINGLE_SPINNER));
        itemList.add(new CreateInvoiceData(getString(R.string.login_email), getString(R.string.country), null, null,
                TYPE_EDIT_TEXT_SPINNER, INPUT_TYPE_EMAIL, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.region), getString(R.string.city), null, null,
                TYPE_TWO_SPINNERS, INPUT_TYPE_TEXT, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.delivery_address), getString(R.string.post_index), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_EMAIL, INPUT_TYPE_NUMBER, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.first_name), getString(R.string.middle_name), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_TEXT, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.last_name), getString(R.string.phone_number), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_TEXT, INPUT_TYPE_PHONE, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.email), getString(R.string.discount), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_EMAIL, INPUT_TYPE_NUMBER, true, false));
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

        /* parcel data */
        itemList.add(new CreateInvoiceData(getString(R.string.parcel_data), null, null, null, TYPE_HEADING));
        itemList.add(new CreateInvoiceData(getString(R.string.qr_code), getString(R.string.courier_guidelines),null, null,
                TYPE_ONE_IMAGE_EDIT_TEXT, -1, INPUT_TYPE_TEXT, false, true));
        itemList.add(new CreateInvoiceData(null, null,null, null,
                TYPE_TWO_CARD_VIEWS, -1, -1, false, true));
        itemList.add(new CreateInvoiceData(null, null, null, null, TYPE_TWO_RADIO_BTNS));
        /* for each cargo */
        itemList.add(new CreateInvoiceData(getString(R.string.for_each_cargo), null, null, null, TYPE_HEADING));
        itemList.add(new CreateInvoiceData(getString(R.string.cargo_description), getString(R.string.package_type), null, null,
                TYPE_EDIT_TEXT_SPINNER, INPUT_TYPE_TEXT, -1, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.weight), getString(R.string.length), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_NUMBER, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.width), getString(R.string.height), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_NUMBER, true, true));
        itemList.add(new CreateInvoiceData(getString(R.string.qr_code), null, null, null, TYPE_SINGLE_IMAGE_EDIT_TEXT));
        itemList.add(new CreateInvoiceData(getString(R.string.add), null, null, null, TYPE_BUTTON));
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

        tariffRadioGroup = findViewById(R.id.tariff_radio_group);
        paymentMethodRadioGroup = findViewById(R.id.payment_method_radio_group);
        expressRadioBtn = findViewById(R.id.express_radio_btn);
        economyRadioBtn = findViewById(R.id.economy_radio_btn);
        cashRadioBtn = findViewById(R.id.cash_radio_btn);
        terminalRadioBtn = findViewById(R.id.terminal_radio_btn);
        saveReceiptBtn = findViewById(R.id.save_btn);
        createReceiptBtn = findViewById(R.id.create_receipt_btn);
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

    private void addItem() {
        if (TextUtils.isEmpty(description)) {
            Toast.makeText(context, "Добавьте описание", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(packageType)) {
            Toast.makeText(context, "Укажите тип упаковки", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(weight)) {
            Toast.makeText(context, "Укажите вес груза", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(length)) {
            Toast.makeText(context, "Укажите длину груза", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(width)) {
            Toast.makeText(context, "Укажите ширину груза", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(height)) {
            Toast.makeText(context, "Укажите высоту груза", Toast.LENGTH_SHORT).show();
            return;
        }

        final Cargo newItem = new Cargo(description, packageType,
                Integer.parseInt(length), Integer.parseInt(width), Integer.parseInt(height), Integer.parseInt(weight), cargoQr);
        final CreateInvoiceData calcItem = new CreateInvoiceData(TYPE_CALCULATOR_ITEM);
        calcItem.packageType = packageType;
        calcItem.index = String.valueOf(cargoList.size());
        calcItem.dimensions = length + "x" + width + "x" + height;
        calcItem.weight = weight;

        cargoList.add(newItem);
        itemList.add(calcItem);
        adapter.notifyDataSetChanged();
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

    private void sendInvoice() {
//        //sender data
//        if (TextUtils.isEmpty(senderAddress)) {
//            Log.i(TAG, "senderAddress empty");
//            Toast.makeText(context, "Для создания заявки укажите адрес отправителя", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(senderCountry)) {
//            Log.i(TAG, "senderCountry empty");
//            Toast.makeText(context, "Для создания заявки укажите страну отправителя", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(senderRegion)) {
//            Log.i(TAG, "senderRegion empty");
//            Toast.makeText(context, "Для создания заявки укажите регион отправителя", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(senderCity)) {
//            Log.i(TAG, "senderCity empty");
//            Toast.makeText(context, "Для создания заявки укажите город отправителя", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(senderZip)) {
//            Log.i(TAG, "senderZip empty");
//            Toast.makeText(context, "Для создания заявки укажите почтовый индекс отправителя", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(senderFirstName)) {
//            Log.i(TAG, "senderFirstName empty");
//            Toast.makeText(context, "Для создания заявки укажите имя отправителя", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(senderMiddleName)) {
//            Log.i(TAG, "senderMiddleName empty");
//            Toast.makeText(context, "Для создания заявки укажите отчество отправителя", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(senderLastName)) {
//            Log.i(TAG, "senderLastName empty");
//            Toast.makeText(context, "Для создания заявки укажите фамилию отправителя", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(senderPhone)) {
//            Toast.makeText(context, "Для создания заявки укажите номер телефона отправителя", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(senderEmail)) {
//            Toast.makeText(context, "Для создания заявки укажите email отправителя", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(senderSignature)) {
//            Toast.makeText(context, "Для создания заявки добавьте подпись отправителя", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(senderCargostar)) {
//            Toast.makeText(context, "Для создания заявки укажите номер акканута CargoStar отправителя", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        //recipient data
//        if (TextUtils.isEmpty(recipientAddress)) {
//            Toast.makeText(context, "Для создания заявки укажите адрес получателя", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(recipientCountry)) {
//            Toast.makeText(context, "Для создания заявки укажите страну получателя", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(recipientRegion)) {
//            Toast.makeText(context, "Для создания заявки укажите регион получателя", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(recipientCity)) {
//            Toast.makeText(context, "Для создания заявки укажите город получателя", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(recipientZip)) {
//            Toast.makeText(context, "Для создания заявки укажите почтовый индекс получателя", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(recipientFirstName)) {
//            Toast.makeText(context, "Для создания заявки укажите имя получателя", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(recipientMiddleName)) {
//            Toast.makeText(context, "Для создания заявки укажите отчество получателя", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(recipientLastName)) {
//            Toast.makeText(context, "Для создания заявки укажите фамилию получателя", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(recipientPhone)) {
//            Toast.makeText(context, "Для создания заявки укажите номер телефона получателя", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(recipientEmail)) {
//            Toast.makeText(context, "Для создания заявки укажите email получателя", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        //payer data
//        if (TextUtils.isEmpty(payerAddress)) {
//            Toast.makeText(context, "Для создания заявки укажите адрес плательщика", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(payerCountry)) {
//            Toast.makeText(context, "Для создания заявки укажите страну плательщика", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(payerRegion)) {
//            Toast.makeText(context, "Для создания заявки укажите регион плательщика", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(payerCity)) {
//            Toast.makeText(context, "Для создания заявки укажите город плательщика", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(payerZip)) {
//            Toast.makeText(context, "Для создания заявки укажите почтовый индекс плательщика", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(payerFirstName)) {
//            Toast.makeText(context, "Для создания заявки укажите имя плательщика", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(payerMiddleName)) {
//            Toast.makeText(context, "Для создания заявки укажите отчество плательщика", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(payerLastName)) {
//            Toast.makeText(context, "Для создания заявки укажите фамилию плательщика", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(payerPhone)) {
//            Toast.makeText(context, "Для создания заявки укажите номер телефона плательщика", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (TextUtils.isEmpty(payerEmail)) {
//            Toast.makeText(context, "Для создания заявки укажите email плательщика", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        //parcel data
//        if (TextUtils.isEmpty(qr)) {
//            Toast.makeText(context, "Для создания заявки отсканируйте QR-код", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (cargoList.isEmpty()) {
//            Toast.makeText(context, "Для создания заявки добавьте хотя бы 1 груз", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (!senderCountry.equals(getString(R.string.uzbekistan)) && !recipientCountry.equals(getString(R.string.uzbekistan))) {
//            Toast.makeText(context, "Страна отправителя или получателя должна быть Узбекистан", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (!firstCardRadioBtn.isChecked() && !secondCardRadioBtn.isChecked()) {
//            Toast.makeText(context, "Для создания заявки выберите поставщика услуг", Toast.LENGTH_SHORT).show();
//            return;
//        }

//        final Invoice newRequest = new Invoice(senderFirstName, senderMiddleName, senderLastName, newSenderAddress, senderPhone, TransportationStatus.IN_TRANSIT, PaymentStatus.WAITING_PAYMENT);
//        newRequest.setCourierId(SharedPrefs.getInstance(context).getLong(SharedPrefs.ID));
//        newRequest.setServiceProvider(serviceProvider);
//        newRequest.setSenderSignature(senderSignature);
//        newRequest.setSenderEmail(senderEmail);
//        newRequest.setSenderCargostarAccountNumber(senderCargostar);
//        newRequest.setRecipientAddress(newRecipientAddress);
//        newRequest.setRecipientFirstName(recipientFirstName);
//        newRequest.setRecipientMiddleName(recipientMiddleName);
//        newRequest.setRecipientLastName(recipientLastName);
//        newRequest.setRecipientPhone(recipientPhone);
//        newRequest.setRecipientEmail(recipientEmail);
//        newRequest.setPayerAddress(newPayerAddress);
//        newRequest.setPayerFirstName(payerFirstName);
//        newRequest.setPayerMiddleName(payerMiddleName);
//        newRequest.setPayerLastName(payerLastName);
//        newRequest.setPayerPhone(payerPhone);
//        newRequest.setPayerEmail(payerEmail);
//        newRequest.setQr(qr);
//        //sender data
//        newRequest.setSenderLogin(senderLogin);
//        newRequest.setSenderTntAccountNumber(senderTnt);
//        newRequest.setSenderFedexAccountNumber(senderFedex);
//        newRequest.setRecipientLogin(recipientLogin);
//        newRequest.setRecipientCargostarAccountNumber(recipientCargo);
//        newRequest.setRecipientTntAccountNumber(recipientTnt);
//        newRequest.setRecipientFedexAccountNumber(recipientFedex);
//        //payer data
//        newRequest.setPayerLogin(payerLogin);
//        newRequest.setPayerCargostarAccountNumber(payerCargostar);
//        newRequest.setPayerTntAccountNumber(payerTnt);
//        newRequest.setPayerFedexAccountNumber(payerFedex);
//
//        if (!TextUtils.isEmpty(discount)) {
//            newRequest.setDiscount(Integer.parseInt(discount));
//        }
//        if (!TextUtils.isEmpty(recipientSignature)) {
//            newRequest.setRecipientSignature(recipientSignature);
//        }
//
//        newRequest.setCheckingAccount(checkingAccount);
//        newRequest.setBank(bank);
//        newRequest.setRegistrationCode(registrationCode);
//        newRequest.setMfo(mfo);
//        newRequest.setOked(oked);
//        newRequest.setInstructions(instructions);
//        newRequest.setPaymentStatus(PaymentStatus.WAITING_PAYMENT);
//
//        if (tariffRadioGroup.getCheckedRadioButtonId() == expressRadioBtn.getId()) {
//            newRequest.setTariff(getString(R.string.express));
//        }
//        else if (tariffRadioGroup.getCheckedRadioButtonId() == economyRadioBtn.getId()) {
//            newRequest.setTariff(getString(R.string.economy_express));
//        }
//        if (firstCardRadioBtn.isChecked()) {
//            serviceProvider = firstCardValue;
//        }
//        else if (secondCardRadioBtn.isChecked()) {
//            serviceProvider = secondCardValue;
//        }
//        newRequest.setServiceProvider(serviceProvider);

//        final CreateInvoiceParams createInvoiceParams = new CreateInvoiceParams(
//                );

        final List<Cargo> tempCargoList = new ArrayList<>();
        tempCargoList.add(new Cargo("Cargo1", 4.5, 4, 4, 10));
        tempCargoList.add(new Cargo("Cargo2", 4.5, 4, 4, 5));
        tempCargoList.add(new Cargo("Cargo3", 4.5, 4, 4, 14));

        RetrofitClient.getInstance(context).setServerData(SharedPrefs.getInstance(context).getString(SharedPrefs.LOGIN),
                SharedPrefs.getInstance(context).getString(SharedPrefs.PASSWORD_HASH));
        RetrofitClient.getInstance(context)
                .createInvoice(11L,
                        null,
                        null,
                        ImageSerializer.fileToBase64(senderSignature),
                        "android@gmail.com",
                        "12345677890",
                        "12345677890",
                        "12345677890",
                        "Uzbekistan",
                        "",
                        "Tashkent",
                        "Yunusabad 8",
                        "100170",
                        "Android",
                        "Valerevich",
                        "Kim",
                        "+998991231232",
                        ImageSerializer.fileToBase64(recipientSignature),
                        "sk@gmail.com",
                        "12345677890",
                        "12345677890",
                        "12345677890",
                        "Uzbekistan",
                        "",
                        "Tashkent",
                        "Chilonzor 1",
                        "100100",
                        "Sergey",
                        "",
                        "Kadushkin",
                        "+998901671213",
                        "sergey.kim@mail.ru",
                        "12345677890",
                        "12345677890",
                        "12345677890",
                        "Uzbekistan",
                        "",
                        "Tashkent",
                        "Yunusabad 8",
                        "100190",
                        "Sergey",
                        "",
                        "Kim",
                        "+998990116824",
                        "-10",
                        "12345677890",
                        "NBU bank",
                        "12345677890",
                        "12345677890",
                        "12345677890",
                        qr,
                        "Инструкции для курьера",
                        tempCargoList,
                        2607500,
                        6L,
                        5L,
                        1, new Callback<JsonElement>() {
                            @Override
                            public void onResponse(@NonNull final Call<JsonElement> call, @NonNull final Response<JsonElement> response) {
                                Log.i(TAG, "body: " + response.body());
                                Log.e(TAG, "errorBody: " + response.errorBody());
                            }

                            @Override
                            public void onFailure(@NonNull final Call<JsonElement> call, @NonNull final Throwable t) {
                                Log.e(TAG, "onFailure: ", t);
                            }
                        });

        Toast.makeText(context, "Накладная создана успешно!", Toast.LENGTH_SHORT).show();

//        startActivity(new Intent(this, MainActivity.class));
//        finish();
    }

    private static final String TAG = CreateInvoiceActivity.class.toString();

    @Override
    public void onAddBtnClicked() {
        addItem();
    }

    @Override
    public void onDeleteItemClicked(final int position) {
        //todo: refactor
        cargoList.remove(position - 51);
        itemList.remove(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCameraImageClicked(final int position) {
        //todo: refactor
        final Intent scanQrIntent = new Intent(context, ScanQrActivity.class);
        if (position == 42) {
            startActivityForResult(scanQrIntent, IntentConstants.REQUEST_SCAN_QR_PARCEL);
        }
        else if (position == 48) {
            startActivityForResult(scanQrIntent, IntentConstants.REQUEST_SCAN_QR_CARGO);
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
    public void afterFirstEditTextChanged(int position, Editable editable) {
        switch (position) {
            case 1: {
                //courierId
                courierId = editable.toString();
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
                //senderLogin
                senderLogin = editable.toString();
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
                //recipientLogin
                recipientLogin = editable.toString();
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
                //payerLogin
                payerLogin = editable.toString();
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
                //payerEmail
                payerEmail = editable.toString();
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
            case 42: {
                //qr
                qr = editable.toString();
                break;
            }
            case 45: {
                //desc
                description = editable.toString();
                break;
            }
            case 46: {
                //weight
                weight = editable.toString();
                break;
            }
            case 47: {
                //width
                width = editable.toString();
                break;
            }
            case 48: {
                //cargoQr
                cargoQr = editable.toString();
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
            case 12: {
                //senderEmail
                senderEmail = editable.toString();
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
            case 21: {
                //recipientEmail
                recipientEmail = editable.toString();
                break;
            }
            case 25: {
                //payerAddress
                payerAddress = editable.toString();
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
            case 30: {
                //discount
                discount = editable.toString();
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
            case 42: {
                //instructions
                instructions = editable.toString();
                break;
            }
            case 45: {
                //package type
                packageType = editable.toString();
                break;
            }
            case 46: {
                //length
                length = editable.toString();
                break;
            }
            case 47: {
                //height
                height = editable.toString();
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == IntentConstants.REQUEST_UPLOAD_PHOTO) {
                final Uri selectedImage = data.getData();
                if (selectedImage != null) {
//                    photoResultImageView.setVisibility(View.VISIBLE);
//                    photoEditText.setBackgroundResource(R.drawable.edit_text_active);
                    return;
                }
            }
            if (requestCode == IntentConstants.REQUEST_UPLOAD_DOCUMENT) {
                final Uri selectedDoc = data.getData();
                if (selectedDoc != null) {
//                    contractResultImageView.setImageResource(R.drawable.ic_doc_green);
//                    contractResultImageView.setVisibility(View.VISIBLE);
//                    contractEditText.setBackgroundResource(R.drawable.edit_text_active);
                    return;
                }
            }
            if (requestCode == IntentConstants.REQUEST_SCAN_QR_PARCEL) {
                Log.i(TAG, "onActivityResult: " + data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE));
                itemList.get(42).firstValue = data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE);
                qr = data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE);
                adapter.notifyItemChanged(42);
                return;
            }
            if (requestCode == IntentConstants.REQUEST_SCAN_QR_CARGO) {
                Log.i(TAG, "onActivityResult: " + data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE));
                itemList.get(48).firstValue = data.getStringExtra(IntentConstants.INTENT_RESULT_VALUE);
                adapter.notifyItemChanged(48);
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
                Log.i(TAG, "selected sender country: " + senderCountry);
                createInvoiceViewModel.setSenderCountryId(senderCountry.getId());
                calculatorViewModel.setSrcCountryId(senderCountry.getId());

                //country & null = hide all
                if (recipientCountry == null) {
                    selectedCountryId = null;
                    calculatorViewModel.setCountryIdProviderId(selectedCountryId, serviceProvider != null ? serviceProvider.getId() : null);

//                    firstCardRadioBtn.setChecked(false);
//                    secondCardRadioBtn.setChecked(false);
//
//                    firstCardRadioBtn.setText(null);
//                    secondCardRadioBtn.setText(null);
//
//                    firstCardRadioBtn.setVisibility(View.INVISIBLE);
//                    firstCardImageView.setVisibility(View.INVISIBLE);
//                    firstCard.setVisibility(View.INVISIBLE);
//
//                    secondCardRadioBtn.setVisibility(View.INVISIBLE);
//                    secondCardImageView.setVisibility(View.INVISIBLE);
//                    secondCard.setVisibility(View.INVISIBLE);

                    return;
                }
                if (!TextUtils.isEmpty(senderCountry.getNameEn()) && senderCountry.getNameEn().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                    //uzbekistan -> uzbekistan = cargo only
                    if (!TextUtils.isEmpty(recipientCountry.getNameEn()) && recipientCountry.getNameEn().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                        selectedCountryId = senderCountry.getId();
                        calculatorViewModel.setCountryIdProviderId(selectedCountryId, serviceProvider != null ? serviceProvider.getId() : null);

//                        firstCardRadioBtn.setChecked(false);
//
//                        firstCardImageView.setImageResource(R.drawable.logo_cargo_calc);
//                        firstCardRadioBtn.setText(R.string.cargostar);
//                        secondCardRadioBtn.setText(null);
//
//                        firstCardRadioBtn.setVisibility(View.VISIBLE);
//                        firstCardImageView.setVisibility(View.VISIBLE);
//                        firstCard.setVisibility(View.VISIBLE);
//
//                        secondCardRadioBtn.setVisibility(View.INVISIBLE);
//                        secondCardImageView.setVisibility(View.INVISIBLE);
//                        secondCard.setVisibility(View.INVISIBLE);

                        return;
                    }
                    //uzbekistan -> other = fedex & tnt (export)
                    selectedCountryId = recipientCountry.getId();
                    calculatorViewModel.setCountryIdProviderId(selectedCountryId, serviceProvider != null ? serviceProvider.getId() : null);

//                    firstCardRadioBtn.setChecked(false);
//                    secondCardRadioBtn.setChecked(false);
//
//                    firstCardRadioBtn.setText(R.string.tnt);
//                    secondCardRadioBtn.setText(R.string.fedex);
//
//                    firstCardImageView.setImageResource(R.drawable.logo_tnt_cacl);
//                    firstCardRadioBtn.setVisibility(View.VISIBLE);
//                    firstCardImageView.setVisibility(View.VISIBLE);
//                    firstCard.setVisibility(View.VISIBLE);
//
//                    secondCardImageView.setImageResource(R.drawable.logo_fedex_calc);
//                    secondCardRadioBtn.setVisibility(View.VISIBLE);
//                    secondCardImageView.setVisibility(View.VISIBLE);
//                    secondCard.setVisibility(View.VISIBLE);
                    return;
                }
                //other -> uzbekistan = tnt only
                if (!TextUtils.isEmpty(recipientCountry.getNameEn()) && recipientCountry.getNameEn().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                    selectedCountryId = senderCountry.getId();
                    calculatorViewModel.setCountryIdProviderId(selectedCountryId, serviceProvider != null ? serviceProvider.getId() : null);

//                    firstCardRadioBtn.setChecked(false);
//                    firstCardImageView.setImageResource(R.drawable.logo_tnt_cacl);
//
//                    firstCardRadioBtn.setText(R.string.tnt);
//                    secondCardRadioBtn.setText(null);
//
//                    firstCardRadioBtn.setVisibility(View.VISIBLE);
//                    firstCardImageView.setVisibility(View.VISIBLE);
//                    firstCard.setVisibility(View.VISIBLE);
//
//                    secondCardRadioBtn.setVisibility(View.INVISIBLE);
//                    secondCardImageView.setVisibility(View.INVISIBLE);
//                    secondCard.setVisibility(View.INVISIBLE);
                    return;
                }
                selectedCountryId = null;
                calculatorViewModel.setCountryIdProviderId(selectedCountryId, serviceProvider != null ? serviceProvider.getId() : null);

//                firstCardRadioBtn.setChecked(false);
//                secondCardRadioBtn.setChecked(false);
//
//                firstCardRadioBtn.setText(null);
//                secondCardRadioBtn.setText(null);
//
//                firstCardRadioBtn.setVisibility(View.INVISIBLE);
//                firstCardImageView.setVisibility(View.INVISIBLE);
//                firstCard.setVisibility(View.INVISIBLE);
//
//                secondCardRadioBtn.setVisibility(View.INVISIBLE);
//                secondCardImageView.setVisibility(View.INVISIBLE);
//                secondCard.setVisibility(View.INVISIBLE);
            }
                break;
            case 17: {
                //recipient country
                recipientCountry = (Country) selectedObject;
                Log.i(TAG, "selected recipient country: " + recipientCountry);
                createInvoiceViewModel.setRecipientCountryId(recipientCountry.getId());
                calculatorViewModel.setDestCountryId(recipientCountry.getId());

                //country & null = hide all
                if (senderCountry == null) {
                    selectedCountryId = null;
                    calculatorViewModel.setCountryIdProviderId(selectedCountryId, serviceProvider != null ? serviceProvider.getId() : null);

//                    firstCardRadioBtn.setChecked(false);
//                    secondCardRadioBtn.setChecked(false);
//
//                    firstCardRadioBtn.setText(null);
//                    secondCardRadioBtn.setText(null);
//
//                    firstCardRadioBtn.setVisibility(View.INVISIBLE);
//                    firstCardImageView.setVisibility(View.INVISIBLE);
//                    firstCard.setVisibility(View.INVISIBLE);
//
//                    secondCardRadioBtn.setVisibility(View.INVISIBLE);
//                    secondCardImageView.setVisibility(View.INVISIBLE);
//                    secondCard.setVisibility(View.INVISIBLE);
                    return;
                }

                if (!TextUtils.isEmpty(senderCountry.getNameEn()) && senderCountry.getNameEn().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                    //uzbekistan -> uzbekistan = cargo only
                    if (!TextUtils.isEmpty(recipientCountry.getNameEn()) && recipientCountry.getNameEn().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                        selectedCountryId = recipientCountry.getId();
                        calculatorViewModel.setCountryIdProviderId(selectedCountryId, serviceProvider != null ? serviceProvider.getId() : null);

//                        firstCardRadioBtn.setChecked(false);
//
//                        firstCardImageView.setImageResource(R.drawable.logo_cargo_calc);
//
//                        firstCardRadioBtn.setText(R.string.cargostar);
//                        secondCardRadioBtn.setText(null);
//
//                        firstCardRadioBtn.setVisibility(View.VISIBLE);
//                        firstCardImageView.setVisibility(View.VISIBLE);
//                        firstCard.setVisibility(View.VISIBLE);
//
//                        secondCardRadioBtn.setVisibility(View.INVISIBLE);
//                        secondCardImageView.setVisibility(View.INVISIBLE);
//                        secondCard.setVisibility(View.INVISIBLE);
                        return;
                    }
                    //uzbekistan -> other = fedex & tnt (export)
                    selectedCountryId = recipientCountry.getId();
                    calculatorViewModel.setCountryIdProviderId(selectedCountryId, serviceProvider != null ? serviceProvider.getId() : null);

//                    firstCardImageView.setImageResource(R.drawable.logo_tnt_cacl);
//                    firstCardRadioBtn.setChecked(false);
//                    secondCardRadioBtn.setChecked(false);
//
//                    firstCardRadioBtn.setText(R.string.tnt);
//                    secondCardRadioBtn.setText(R.string.fedex);
//
//                    firstCardRadioBtn.setVisibility(View.VISIBLE);
//                    firstCard.setVisibility(View.VISIBLE);
//                    firstCardImageView.setVisibility(View.VISIBLE);
//
//                    secondCardImageView.setImageResource(R.drawable.logo_fedex_calc);
//                    secondCardRadioBtn.setVisibility(View.VISIBLE);
//                    secondCardImageView.setVisibility(View.VISIBLE);
//                    secondCard.setVisibility(View.VISIBLE);

                    return;
                }
                //other -> uzbekistan = tnt only
                if (!TextUtils.isEmpty(recipientCountry.getNameEn()) && recipientCountry.getNameEn().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                    selectedCountryId = senderCountry.getId();
                    calculatorViewModel.setCountryIdProviderId(selectedCountryId, serviceProvider != null ? serviceProvider.getId() : null);

//                    firstCardRadioBtn.setChecked(false);
//
//                    firstCardImageView.setImageResource(R.drawable.logo_tnt_cacl);
//
//                    firstCardRadioBtn.setText(R.string.tnt);
//                    secondCardRadioBtn.setText(null);
//
//                    firstCardRadioBtn.setVisibility(View.VISIBLE);
//                    firstCardImageView.setVisibility(View.VISIBLE);
//                    firstCard.setVisibility(View.VISIBLE);
//
//                    secondCardRadioBtn.setVisibility(View.INVISIBLE);
//                    secondCardImageView.setVisibility(View.INVISIBLE);
//                    secondCard.setVisibility(View.INVISIBLE);
                    return;
                }
                selectedCountryId = null;
                calculatorViewModel.setCountryIdProviderId(selectedCountryId, serviceProvider != null ? serviceProvider.getId() : null);

//                firstCardRadioBtn.setChecked(false);
//                secondCardRadioBtn.setChecked(false);
//
//                firstCardRadioBtn.setText(null);
//                secondCardRadioBtn.setText(null);
//
//                firstCardRadioBtn.setVisibility(View.INVISIBLE);
//                firstCardImageView.setVisibility(View.INVISIBLE);
//                firstCard.setVisibility(View.INVISIBLE);
//
//                secondCardRadioBtn.setVisibility(View.INVISIBLE);
//                secondCardImageView.setVisibility(View.INVISIBLE);
//                secondCard.setVisibility(View.INVISIBLE);
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
                Log.i(TAG, "selected senderCity: " + senderCity);
                createInvoiceViewModel.setSenderCityId(city.getId());
                break;
            }
            case 18: {
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
    public void onRadioBtnsSelected(View checkView, boolean checkedBtn) {
        if (checkView instanceof RadioButton) {
            if (checkedBtn) {
                if (((RadioButton) checkView).getText().equals(getString(R.string.cargostar))) {
                    calculatorViewModel.setProviderId(6L);
                    Log.i(TAG, "onRadioBtnsSelected: ");
                    return;
                }
                if (((RadioButton) checkView).getText().equals(getString(R.string.tnt))) {
                    calculatorViewModel.setProviderId(5L);
                    Log.i(TAG, "onRadioBtnsSelected: ");
                    return;
                }
                if (((RadioButton) checkView).getText().equals(getString(R.string.fedex))) {
                    calculatorViewModel.setProviderId(4L);
                    Log.i(TAG, "onRadioBtnsSelected: ");
                }
            }
        }
    }

    @Override
    public void onRadioGroupSelected(final RadioGroup group, final int docTypeId, final int boxTypeId) {
        if (group.getCheckedRadioButtonId() == docTypeId) {
            selectedPackageType = 1;
            Log.i(TAG, "onRadioGroupSelected: ");
            return;
        }
        if (group.getCheckedRadioButtonId() == boxTypeId) {
            selectedPackageType = 2;
            Log.i(TAG, "onRadioGroupSelected: ");
        }
    }

    private void initCitySpinner() {

    }
}