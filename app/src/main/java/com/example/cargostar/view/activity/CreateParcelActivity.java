package com.example.cargostar.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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

import com.example.cargostar.R;
import com.example.cargostar.model.PaymentStatus;
import com.example.cargostar.model.TransportationStatus;
import com.example.cargostar.model.actor.AddressBook;
import com.example.cargostar.model.database.SharedPrefs;
import com.example.cargostar.model.location.Address;
import com.example.cargostar.model.shipping.Cargo;
import com.example.cargostar.model.shipping.Receipt;
import com.example.cargostar.model.shipping.ReceiptWithCargoList;
import com.example.cargostar.view.Constants;
import com.example.cargostar.view.adapter.CreateParcelAdapter;
import com.example.cargostar.view.adapter.CreateParcelData;
import com.example.cargostar.view.callback.CreateParcelCallback;
import com.example.cargostar.viewmodel.HeaderViewModel;
import com.example.cargostar.viewmodel.PopulateViewModel;

import java.util.ArrayList;
import java.util.List;
import static com.example.cargostar.view.adapter.CreateParcelData.TYPE_BUTTON;
import static com.example.cargostar.view.adapter.CreateParcelData.TYPE_CALCULATOR_ITEM;
import static com.example.cargostar.view.adapter.CreateParcelData.TYPE_HEADING;
import static com.example.cargostar.view.adapter.CreateParcelData.TYPE_ONE_IMAGE_EDIT_TEXT;
import static com.example.cargostar.view.adapter.CreateParcelData.TYPE_SINGLE_IMAGE_EDIT_TEXT;
import static com.example.cargostar.view.adapter.CreateParcelData.TYPE_SINGLE_SPINNER;
import static com.example.cargostar.view.adapter.CreateParcelData.TYPE_TWO_EDIT_TEXTS;
import static com.example.cargostar.view.adapter.CreateParcelData.TYPE_SINGLE_EDIT_TEXT;
import static com.example.cargostar.view.adapter.CreateParcelData.TYPE_STROKE;
import static com.example.cargostar.view.adapter.CreateParcelData.TYPE_TWO_IMAGE_EDIT_TEXTS;
import static com.example.cargostar.view.adapter.CreateParcelData.INPUT_TYPE_NUMBER;
import static com.example.cargostar.view.adapter.CreateParcelData.INPUT_TYPE_TEXT;
import static com.example.cargostar.view.adapter.CreateParcelData.INPUT_TYPE_EMAIL;
import static com.example.cargostar.view.adapter.CreateParcelData.INPUT_TYPE_PHONE;

public class CreateParcelActivity extends AppCompatActivity implements CreateParcelCallback {
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

    private HeaderViewModel headerViewModel;
    private ReceiptWithCargoList currentReceipt;
    private List<CreateParcelData> itemList;
    private List<Cargo> cargoList = new ArrayList<>();
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
    private CreateParcelAdapter adapter;

    //service provider
    private CardView firstCard;
    private CardView secondCard;
    private RadioButton firstCardRadioBtn;
    private RadioButton secondCardRadioBtn;
    private ImageView firstCardImageView;
    private ImageView secondCardImageView;
    private String firstCardValue;
    private String secondCardValue;

    //EditText watcher values
    private String courierId;
    private String operatorId;
    private String accountantId;
    private String serviceProvider;
    private String senderSignature;
    private String recipientSignature;
    private String senderLogin;
    private String senderCargostar;
    private String senderTnt;
    private String senderFedex;
    private String senderAddress;
    private String senderCountry;
    private String senderCity;
    private String senderRegion;
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
    private String recipientCountry;
    private String recipientCity;
    private String recipientRegion;
    private String recipientZip;
    private String recipientFirstName;
    private String recipientMiddleName;
    private String recipientLastName;
    private String recipientPhone;
    private String recipientEmail;
    private String payerLogin;
    private String payerAddress;
    private String payerCountry;
    private String payerCity;
    private String payerRegion;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_parcel);
        context = this;

        itemList = new ArrayList<>();
        headerViewModel = new ViewModelProvider(this).get(HeaderViewModel.class);

        if (getIntent() != null) {
            final int requestKey = getIntent().getIntExtra(Constants.INTENT_REQUEST_KEY, -1);
            final long requestId = getIntent().getLongExtra(Constants.INTENT_REQUEST_VALUE, -1);
            final long requestOrParcel =  getIntent().getLongExtra(Constants.INTENT_REQUEST_OR_PARCEL, -1);

            initUI();

            if (requestKey == Constants.REQUEST_EDIT_PARCEL) {
                headerViewModel.selectRequest(requestId).observe(this, receipt -> {
                    if (receipt != null) {
                        if (requestOrParcel == Constants.INTENT_REQUEST) {
                            saveReceiptBtn.setVisibility(View.VISIBLE);
                            createReceiptBtn.setVisibility(View.VISIBLE);
                        }
                        else if (requestOrParcel == Constants.INTENT_PARCEL) {
                            saveReceiptBtn.setVisibility(View.VISIBLE);
                            createReceiptBtn.setVisibility(View.INVISIBLE);
                        }
                        if (receipt.getReceipt().getPaymentStatus() == PaymentStatus.PAID ||
                                receipt.getReceipt().getPaymentStatus() == PaymentStatus.PAID_MORE ||
                                receipt.getReceipt().getPaymentStatus() == PaymentStatus.PAID_PARTIALLY) {
                            cashRadioBtn.setVisibility(View.INVISIBLE);
                            terminalRadioBtn.setVisibility(View.INVISIBLE);
                        }
                        else if (receipt.getReceipt().getPaymentStatus() == PaymentStatus.WAITING_CHECK || receipt.getReceipt().getPaymentStatus() == PaymentStatus.WAITING_PAYMENT) {
                            cashRadioBtn.setVisibility(View.VISIBLE);
                            terminalRadioBtn.setVisibility(View.VISIBLE);
                        }
                        currentReceipt = receipt;
                        updateUI(currentReceipt);
                    }
                });
            }
            else {
                saveReceiptBtn.setVisibility(View.INVISIBLE);
                createReceiptBtn.setVisibility(View.VISIBLE);
            }
        }
        headerViewModel.selectCourierByLogin(SharedPrefs.getInstance(context).getString(SharedPrefs.LOGIN)).observe(this, courier -> {
            if (courier != null) {
                fullNameTextView.setText(courier.getFirstName() + " " + courier.getLastName());
            }
        });

        headerViewModel.selectBranchByCourierId(SharedPrefs.getInstance(context).getLong(SharedPrefs.ID)).observe(this, branch -> {
            if (branch != null) {
                branchTextView.setText(getString(R.string.branch) + " \"" + branch.getName() + "\"");
            }
        });

        parcelSearchImageView.setOnClickListener(v -> {
            final String parcelIdStr = parcelSearchEditText.getText().toString();
            if (TextUtils.isEmpty(parcelIdStr)) {
                Toast.makeText(context, "Введите ID перевозки или номер накладной", Toast.LENGTH_SHORT).show();
                return;
            }

            final long parcelId = Long.parseLong(parcelIdStr);

            headerViewModel.selectRequest(parcelId).observe(this, receiptWithCargoList -> {
                if (receiptWithCargoList == null) {
                    Toast.makeText(context, "Накладной не существует", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (receiptWithCargoList.getReceipt() == null) {
                    Toast.makeText(context, "Накладной не существует", Toast.LENGTH_SHORT).show();
                    return;
                }
                final Intent mainIntent = new Intent(context, MainActivity.class);
                mainIntent.putExtra(Constants.INTENT_REQUEST_KEY, Constants.REQUEST_FIND_PARCEL);
                mainIntent.putExtra(Constants.INTENT_REQUEST_VALUE, parcelId);
                startActivity(mainIntent);
            });
        });

        saveReceiptBtn.setOnClickListener(v -> {
            saveReceipt();
        });

        createReceiptBtn.setOnClickListener(v -> {
            createReceipt();
        });
        //todo: cretea viewModel for CreateParcelAcitivity
//        model.selectAddressBookEntriesBySenderLogin(senderLogin).observe(this, addressBookEntries -> {
//            Log.i(TAG, "entries=" + addressBookEntries);
//            adapter.setAddressBookEntries(addressBookEntries);
//            adapter.notifyDataSetChanged();
//        });
    }

    @Override
    public void onDeleteItemClicked(final int position) {
        Log.i(TAG, "onDeleteItemClicked(): ");
        cargoList.remove(position);
        itemList.remove(position);
        adapter.notifyItemRemoved(position);
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
        //service provider
        firstCard = findViewById(R.id.first_card);
        secondCard = findViewById(R.id.second_card);
        firstCardImageView = findViewById(R.id.first_card_logo);
        secondCardImageView = findViewById(R.id.second_card_logo);
        firstCardRadioBtn = findViewById(R.id.first_card_radio_btn);
        secondCardRadioBtn = findViewById(R.id.second_card_radio_btn);

        firstCard.setOnClickListener(v -> {
            firstCardRadioBtn.setChecked(true);
        });

        secondCard.setOnClickListener(v -> {
            secondCardRadioBtn.setChecked(true);
        });

        firstCardRadioBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                secondCardRadioBtn.setChecked(false);
            }
        });

        secondCardRadioBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                firstCardRadioBtn.setChecked(false);
            }
        });

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
        //public data
        itemList.add(new CreateParcelData(getString(R.string.public_data), null, null, null, TYPE_HEADING));
        itemList.add(new CreateParcelData(getString(R.string.courier_id), getString(R.string.operator_id), String.valueOf(SharedPrefs.getInstance(context).getLong(SharedPrefs.ID)), null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_TEXT, INPUT_TYPE_TEXT, false, false));
        itemList.add(new CreateParcelData(getString(R.string.accountant_id), null, null, null,
                TYPE_SINGLE_EDIT_TEXT, INPUT_TYPE_TEXT, -1, false, false));
        itemList.add(new CreateParcelData(null, null, null, null, TYPE_STROKE));
        //sender data
        itemList.add(new CreateParcelData(getString(R.string.sender_data), null, null, null, TYPE_HEADING));
        itemList.add(new CreateParcelData(getString(R.string.sender_signature), getString(R.string.receiver_signature), null, null,
                TYPE_TWO_IMAGE_EDIT_TEXTS));
        itemList.add(new CreateParcelData(getString(R.string.login_email), getString(R.string.cargostar_account_number), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_EMAIL, INPUT_TYPE_NUMBER, true, true));
        itemList.add(new CreateParcelData(getString(R.string.tnt_account_number), getString(R.string.fedex_account_number), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_NUMBER, true, true));
        itemList.add(new CreateParcelData(getString(R.string.take_address), getString(R.string.country), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_TEXT, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateParcelData(getString(R.string.city), getString(R.string.region), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_TEXT, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateParcelData(getString(R.string.post_index), getString(R.string.first_name), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateParcelData(getString(R.string.middle_name), getString(R.string.last_name), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_TEXT, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateParcelData(getString(R.string.phone_number), getString(R.string.email), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_PHONE, INPUT_TYPE_EMAIL, true, true));
        itemList.add(new CreateParcelData(null, null, null, null, TYPE_STROKE));
        //recipient data
        itemList.add(new CreateParcelData(getString(R.string.receiver_data), null, null, null, TYPE_HEADING));
        itemList.add(new CreateParcelData(getString(R.string.login_email), getString(R.string.cargostar_account_number), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_EMAIL, INPUT_TYPE_NUMBER, true, true));
        itemList.add(new CreateParcelData(getString(R.string.tnt_account_number), getString(R.string.fedex_account_number), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_NUMBER, true, true));
        itemList.add(new CreateParcelData(getString(R.string.delivery_address), getString(R.string.country), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_TEXT, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateParcelData(getString(R.string.city), getString(R.string.region), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_TEXT, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateParcelData(getString(R.string.post_index), getString(R.string.first_name), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateParcelData(getString(R.string.middle_name), getString(R.string.last_name), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_TEXT, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateParcelData(getString(R.string.phone_number), getString(R.string.email), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_PHONE, INPUT_TYPE_EMAIL, true, true));
        itemList.add(new CreateParcelData(null, null, null, null, TYPE_STROKE));
        //payer data
        itemList.add(new CreateParcelData(getString(R.string.payer_data), null, null, null, TYPE_HEADING));
        itemList.add(new CreateParcelData(getString(R.string.address_book), null, null, null, TYPE_SINGLE_SPINNER));
        //todo: add adapter to the spinner
        itemList.add(new CreateParcelData(getString(R.string.login_email), getString(R.string.delivery_address), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_EMAIL, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateParcelData(getString(R.string.country), getString(R.string.city), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_TEXT, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateParcelData(getString(R.string.region), getString(R.string.post_index), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_EMAIL, INPUT_TYPE_NUMBER, true, true));
        itemList.add(new CreateParcelData(getString(R.string.first_name), getString(R.string.middle_name), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_TEXT, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateParcelData(getString(R.string.last_name), getString(R.string.phone_number), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_TEXT, INPUT_TYPE_PHONE, true, true));
        itemList.add(new CreateParcelData(getString(R.string.email), getString(R.string.discount), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_EMAIL, INPUT_TYPE_NUMBER, true, false));
        itemList.add(new CreateParcelData(null, null, null, null, TYPE_STROKE));
        //account numbers
        itemList.add(new CreateParcelData(getString(R.string.cargostar_account_number), null, null, null,
                TYPE_SINGLE_EDIT_TEXT, INPUT_TYPE_NUMBER, -1, true, false));
        itemList.add(new CreateParcelData(getString(R.string.tnt_account_number), getString(R.string.tax_id),null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_NUMBER, true, true));
        itemList.add(new CreateParcelData(getString(R.string.fedex_account_number), getString(R.string.tax_id), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_NUMBER, true, true));
        itemList.add(new CreateParcelData(null, null, null, null, TYPE_STROKE));
        //payment data
        itemList.add(new CreateParcelData(getString(R.string.payment_data), null, null, null, TYPE_HEADING));
        itemList.add(new CreateParcelData(getString(R.string.checking_account), getString(R.string.bank),null,null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateParcelData(getString(R.string.payer_registration_code), getString(R.string.mfo), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_NUMBER, true, true));
        itemList.add(new CreateParcelData(getString(R.string.oked), null, null, null,
                TYPE_SINGLE_EDIT_TEXT, INPUT_TYPE_NUMBER, -1, true, false));
        itemList.add(new CreateParcelData(null, null, null, null, TYPE_STROKE));
        //parcel data
        itemList.add(new CreateParcelData(getString(R.string.parcel_data), null, null, null, TYPE_HEADING));
        itemList.add(new CreateParcelData(getString(R.string.qr_code), getString(R.string.courier_guidelines),null, null,
                TYPE_ONE_IMAGE_EDIT_TEXT, -1, INPUT_TYPE_TEXT, false, true));
        itemList.add(new CreateParcelData(null, null, null, null, TYPE_STROKE));
        //for each cargo
        itemList.add(new CreateParcelData(getString(R.string.for_each_cargo), null, null, null, TYPE_HEADING));
        itemList.add(new CreateParcelData(getString(R.string.cargo_description), getString(R.string.package_type), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_TEXT, INPUT_TYPE_TEXT, true, true));
        itemList.add(new CreateParcelData(getString(R.string.weight), getString(R.string.length), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_NUMBER, true, true));
        itemList.add(new CreateParcelData(getString(R.string.width), getString(R.string.height), null, null,
                TYPE_TWO_EDIT_TEXTS, INPUT_TYPE_NUMBER, INPUT_TYPE_NUMBER, true, true));
        itemList.add(new CreateParcelData(getString(R.string.qr_code), null, null, null, TYPE_SINGLE_IMAGE_EDIT_TEXT));
        itemList.add(new CreateParcelData(getString(R.string.add), null, null, null, TYPE_BUTTON));
        itemList.add(new CreateParcelData(null, null, null, null, TYPE_STROKE));

        contentRecyclerView = findViewById(R.id.content_recycler_view);
        adapter = new CreateParcelAdapter(context, this);
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

    private void updateUI(final ReceiptWithCargoList receipt) {
        //public data
        itemList.get(1).firstValue = receipt != null ? String.valueOf(receipt.getReceipt().getCourierId()) : null;
        itemList.get(1).secondValue = receipt != null ? receipt.getReceipt().getOperatorId() : null;
        itemList.get(2).firstValue = receipt != null ? receipt.getReceipt().getAccountantId() : null;
        itemList.get(2).secondValue = receipt != null ? receipt.getReceipt().getServiceProvider() : null;
        courierId = receipt != null ? String.valueOf(receipt.getReceipt().getCourierId()) : null;
        operatorId = receipt != null ? receipt.getReceipt().getOperatorId() : null;
        accountantId = receipt != null ? receipt.getReceipt().getAccountantId() : null;
        serviceProvider = receipt != null ? receipt.getReceipt().getServiceProvider() : null;
        //sender data
        itemList.get(5).firstValue = receipt != null ? receipt.getReceipt().getSenderSignature() : null;
        itemList.get(5).secondValue = receipt != null ? receipt.getReceipt().getRecipientSignature() : null;
        itemList.get(6).firstValue = receipt != null ? receipt.getReceipt().getSenderLogin() : null;
        itemList.get(6).secondValue = receipt != null ? receipt.getReceipt().getSenderCargostarAccountNumber() : null;
        itemList.get(7).firstValue = receipt != null ? receipt.getReceipt().getSenderTntAccountNumber() : null;
        itemList.get(7).secondValue = receipt != null ? receipt.getReceipt().getSenderFedexAccountNumber(): null;
        itemList.get(8).firstValue = receipt != null ? receipt.getReceipt().getSenderAddress().getAddress() : null;
        itemList.get(8).secondValue = receipt != null ? receipt.getReceipt().getSenderAddress().getCountry() : null;
        itemList.get(9).firstValue = receipt != null ? receipt.getReceipt().getSenderAddress().getCity() : null;
        itemList.get(9).secondValue = receipt != null ? receipt.getReceipt().getSenderAddress().getRegion() : null;
        itemList.get(10).firstValue = receipt != null ? receipt.getReceipt().getSenderAddress().getZip() : null;
        itemList.get(10).secondValue = receipt != null ? receipt.getReceipt().getSenderFirstName() : null;
        itemList.get(11).firstValue = receipt != null ? receipt.getReceipt().getSenderMiddleName() : null;
        itemList.get(11).secondValue = receipt != null ? receipt.getReceipt().getSenderLastName() : null;
        itemList.get(12).firstValue = receipt != null ? receipt.getReceipt().getSenderPhone() : null;
        itemList.get(12).secondValue = receipt != null ? receipt.getReceipt().getSenderEmail() : null;
        senderSignature = receipt != null ? receipt.getReceipt().getSenderSignature() : null;
        recipientSignature = receipt != null ? receipt.getReceipt().getRecipientSignature() : null;
        senderLogin = receipt != null ? receipt.getReceipt().getSenderLogin() : null;
        senderCargostar = receipt != null ? receipt.getReceipt().getSenderCargostarAccountNumber() : null;
        senderTnt = receipt != null ? receipt.getReceipt().getSenderTntAccountNumber() : null;
        senderFedex = receipt != null ? receipt.getReceipt().getSenderFedexAccountNumber(): null;
        senderAddress = receipt != null ? receipt.getReceipt().getSenderAddress().getAddress() : null;
        senderCountry = receipt != null ? receipt.getReceipt().getSenderAddress().getCountry() : null;
        senderCity = receipt != null ? receipt.getReceipt().getSenderAddress().getCity() : null;
        senderRegion = receipt != null ? receipt.getReceipt().getSenderAddress().getRegion() : null;
        senderZip = receipt != null ? receipt.getReceipt().getSenderAddress().getZip() : null;
        senderFirstName = receipt != null ? receipt.getReceipt().getSenderFirstName() : null;
        senderMiddleName = receipt != null ? receipt.getReceipt().getSenderMiddleName() : null;
        senderLastName = receipt != null ? receipt.getReceipt().getSenderLastName() : null;
        senderPhone = receipt != null ? receipt.getReceipt().getSenderPhone() : null;
        senderEmail = receipt != null ? receipt.getReceipt().getSenderEmail() : null;
        //recipient data
        itemList.get(15).firstValue = receipt != null ? receipt.getReceipt().getRecipientLogin() : null;
        itemList.get(15).secondValue = receipt != null ? receipt.getReceipt().getRecipientCargostarAccountNumber() : null;
        itemList.get(16).firstValue = receipt != null ? receipt.getReceipt().getRecipientTntAccountNumber() : null;
        itemList.get(16).secondValue = receipt != null ? receipt.getReceipt().getRecipientFedexAccountNumber() : null;
        itemList.get(17).firstValue = receipt != null && receipt.getReceipt().getRecipientAddress() != null ? receipt.getReceipt().getRecipientAddress().getAddress() : null;
        itemList.get(17).secondValue = receipt != null && receipt.getReceipt().getRecipientAddress() != null ? receipt.getReceipt().getRecipientAddress().getCountry() : null;
        itemList.get(18).firstValue = receipt != null  && receipt.getReceipt().getRecipientAddress() != null ? receipt.getReceipt().getRecipientAddress().getCity() : null;
        itemList.get(18).secondValue = receipt != null && receipt.getReceipt().getRecipientAddress() != null ? receipt.getReceipt().getRecipientAddress().getRegion(): null;
        itemList.get(19).firstValue = receipt != null && receipt.getReceipt().getRecipientAddress() != null ? receipt.getReceipt().getRecipientAddress().getZip() : null;
        itemList.get(19).secondValue = receipt != null ? receipt.getReceipt().getRecipientFirstName() : null;
        itemList.get(20).firstValue = receipt != null ? receipt.getReceipt().getRecipientMiddleName() : null;
        itemList.get(20).secondValue = receipt != null ? receipt.getReceipt().getRecipientLastName() : null;
        itemList.get(21).firstValue = receipt != null ? receipt.getReceipt().getRecipientPhone() : null;
        itemList.get(21).secondValue = receipt != null ? receipt.getReceipt().getRecipientEmail() : null;
        recipientLogin = receipt != null ? receipt.getReceipt().getRecipientLogin() : null;
        recipientCargo = receipt != null ? receipt.getReceipt().getRecipientCargostarAccountNumber() : null;
        recipientTnt = receipt != null ? receipt.getReceipt().getRecipientTntAccountNumber() : null;
        recipientFedex = receipt != null ? receipt.getReceipt().getRecipientFedexAccountNumber() : null;
        recipientAddress = receipt != null && receipt.getReceipt().getRecipientAddress() != null ? receipt.getReceipt().getRecipientAddress().getAddress() : null;
        recipientCountry = receipt != null && receipt.getReceipt().getRecipientAddress() != null ? receipt.getReceipt().getRecipientAddress().getCountry() : null;
        recipientCity = receipt != null  && receipt.getReceipt().getRecipientAddress() != null ? receipt.getReceipt().getRecipientAddress().getCity() : null;
        recipientRegion = receipt != null && receipt.getReceipt().getRecipientAddress() != null ? receipt.getReceipt().getRecipientAddress().getRegion(): null;
        recipientZip = receipt != null && receipt.getReceipt().getRecipientAddress() != null ? receipt.getReceipt().getRecipientAddress().getZip() : null;
        recipientFirstName = receipt != null ? receipt.getReceipt().getRecipientFirstName() : null;
        recipientMiddleName = receipt != null ? receipt.getReceipt().getRecipientMiddleName() : null;
        recipientLastName = receipt != null ? receipt.getReceipt().getRecipientLastName() : null;
        recipientPhone = receipt != null ? receipt.getReceipt().getRecipientPhone() : null;
        recipientEmail = receipt != null ? receipt.getReceipt().getRecipientEmail() : null;
        //payer data
        //todo: insert address book spinner (payerFirstName + payerLastName + payerAddress)
//        itemList.get(24).firstValue = receipt != null ? receipt.getReceipt().get
        itemList.get(25).firstValue = receipt != null ? receipt.getReceipt().getPayerLogin() : null;
        itemList.get(25).secondValue = receipt != null && receipt.getReceipt().getPayerAddress() != null ? receipt.getReceipt().getPayerAddress().getAddress() : null;
        itemList.get(26).firstValue = receipt != null && receipt.getReceipt().getPayerAddress() != null ? receipt.getReceipt().getPayerAddress().getCountry() : null;
        itemList.get(26).secondValue = receipt != null && receipt.getReceipt().getPayerAddress() != null ? receipt.getReceipt().getPayerAddress().getCity() : null;
        itemList.get(27).firstValue = receipt != null && receipt.getReceipt().getPayerAddress() != null ? receipt.getReceipt().getPayerAddress().getRegion() : null;
        itemList.get(27).secondValue = receipt != null && receipt.getReceipt().getPayerAddress() != null ? receipt.getReceipt().getPayerAddress().getZip() : null;
        itemList.get(28).firstValue = receipt != null ? receipt.getReceipt().getPayerFirstName() : null;
        itemList.get(28).secondValue = receipt != null ? receipt.getReceipt().getPayerMiddleName() : null;
        itemList.get(29).firstValue = receipt != null ? receipt.getReceipt().getPayerLastName() : null;
        itemList.get(29).secondValue = receipt != null ? receipt.getReceipt().getPayerPhone() : null;
        itemList.get(30).firstValue = receipt != null ? receipt.getReceipt().getPayerEmail() : null;
        itemList.get(30).secondValue = receipt != null && receipt.getReceipt().getDiscount() > 0 ? String.valueOf(receipt.getReceipt().getDiscount()) : null;
        //todo: insert address book spinner
//        addressBook =
        payerLogin = receipt != null ? receipt.getReceipt().getPayerLogin() : null;
        payerAddress = receipt != null && receipt.getReceipt().getPayerAddress() != null ? receipt.getReceipt().getPayerAddress().getAddress() : null;
        payerCountry = receipt != null && receipt.getReceipt().getPayerAddress() != null ? receipt.getReceipt().getPayerAddress().getCountry() : null;
        payerCity = receipt != null && receipt.getReceipt().getPayerAddress() != null ? receipt.getReceipt().getPayerAddress().getCity() : null;
        payerRegion = receipt != null && receipt.getReceipt().getPayerAddress() != null ? receipt.getReceipt().getPayerAddress().getRegion() : null;
        payerZip = receipt != null && receipt.getReceipt().getPayerAddress() != null ? receipt.getReceipt().getPayerAddress().getZip() : null;
        payerFirstName = receipt != null ? receipt.getReceipt().getPayerFirstName() : null;
        payerMiddleName = receipt != null ? receipt.getReceipt().getPayerMiddleName() : null;
        payerLastName = receipt != null ? receipt.getReceipt().getPayerLastName() : null;
        payerPhone = receipt != null ? receipt.getReceipt().getPayerPhone() : null;
        payerEmail = receipt != null ? receipt.getReceipt().getPayerEmail() : null;
        discount = receipt != null && receipt.getReceipt().getDiscount() > 0 ? String.valueOf(receipt.getReceipt().getDiscount()) : null;
        //account numbers
        itemList.get(33).firstValue = receipt != null ? receipt.getReceipt().getPayerTntAccountNumber() : null;
        itemList.get(34).firstValue = receipt != null ? receipt.getReceipt().getPayerFedexAccountNumber() : null;
        payerTnt = receipt != null ? receipt.getReceipt().getPayerTntAccountNumber() : null;
        payerFedex = receipt != null ? receipt.getReceipt().getPayerFedexAccountNumber() : null;
        //payment data
        itemList.get(37).firstValue = receipt != null ? receipt.getReceipt().getCheckingAccount() : null;
        itemList.get(37).secondValue = receipt != null ? receipt.getReceipt().getBank() : null;
        itemList.get(38).firstValue = receipt != null ? receipt.getReceipt().getRegistrationCode() : null;
        itemList.get(38).secondValue = receipt != null ? receipt.getReceipt().getMfo() : null;
        itemList.get(39).firstValue = receipt != null ? receipt.getReceipt().getOked() : null;
        checkingAccount = receipt != null ? receipt.getReceipt().getCheckingAccount() : null;
        bank = receipt != null ? receipt.getReceipt().getBank() : null;
        registrationCode = receipt != null ? receipt.getReceipt().getRegistrationCode() : null;
        mfo = receipt != null ? receipt.getReceipt().getMfo() : null;
        oked = receipt != null ? receipt.getReceipt().getOked() : null;
        //parcel data
        itemList.get(42).firstValue = receipt != null ? receipt.getReceipt().getQr() : null;
        itemList.get(42).secondValue = receipt != null ? receipt.getReceipt().getInstructions() : null;
        qr = receipt != null ? receipt.getReceipt().getQr() : null;
        instructions = receipt != null ? receipt.getReceipt().getInstructions() : null;
        //cargo data 50
        int i = 1;

        if (receipt != null) {
            cargoList = receipt.getCargoList();
            for (final Cargo cargo : receipt.getCargoList()) {
                final CreateParcelData cargoData = new CreateParcelData(CreateParcelData.TYPE_CALCULATOR_ITEM);
                cargoData.index = String.valueOf(i++);
                cargoData.packageType = cargo.getPackageType();
                cargoData.source = receipt.getReceipt().getSenderAddress().getCity();
                cargoData.weight = String.valueOf(cargo.getWeight());
                cargoData.dimensions = cargo.getLength() + "x" + cargo.getWidth() + "x" + cargo.getHeight();
                itemList.add(cargoData);
            }
        }
        adapter.notifyDataSetChanged();
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
        final CreateParcelData calcItem = new CreateParcelData(TYPE_CALCULATOR_ITEM);
        calcItem.packageType = packageType;
        calcItem.index = String.valueOf(cargoList.size());
        calcItem.destination = recipientCity;
        calcItem.source = senderCity;
        calcItem.dimensions = length + "x" + width + "x" + height;
        calcItem.weight = weight;

        cargoList.add(newItem);
        itemList.add(calcItem);
        adapter.notifyItemInserted(itemList.size() - 1);
    }

    private void saveReceipt() {
        //sender data
        if (TextUtils.isEmpty(senderAddress)) {
            Log.i(TAG, "senderAddress empty");
            Toast.makeText(context, "Для создания заявки укажите адрес отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senderCountry)) {
            Log.i(TAG, "senderCountry empty");
            Toast.makeText(context, "Для создания заявки укажите страну отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senderRegion)) {
            Log.i(TAG, "senderRegion empty");
            Toast.makeText(context, "Для создания заявки укажите регион отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senderCity)) {
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
        if (!firstCardRadioBtn.isChecked() && !secondCardRadioBtn.isChecked()) {
            Toast.makeText(context, "Для создания заявки выберите поставщика услуг", Toast.LENGTH_SHORT).show();
            return;
        }
        if (firstCardRadioBtn.isChecked()) {
            serviceProvider = firstCardValue;
        }
        else if (secondCardRadioBtn.isChecked()) {
            serviceProvider = secondCardValue;
        }
        currentReceipt.getReceipt().setServiceProvider(serviceProvider);
        currentReceipt.getReceipt().setSenderSignature(senderSignature);
        currentReceipt.getReceipt().setSenderEmail(senderEmail);
        currentReceipt.getReceipt().setSenderCargostarAccountNumber(senderCargostar);
        currentReceipt.getReceipt().setRecipientFirstName(recipientFirstName);
        currentReceipt.getReceipt().setRecipientMiddleName(recipientMiddleName);
        currentReceipt.getReceipt().setRecipientLastName(recipientLastName);
        currentReceipt.getReceipt().setRecipientPhone(recipientPhone);
        currentReceipt.getReceipt().setRecipientEmail(recipientEmail);
        currentReceipt.getReceipt().setRecipientSignature(recipientSignature);
        currentReceipt.getReceipt().setPayerFirstName(payerFirstName);
        currentReceipt.getReceipt().setPayerMiddleName(payerMiddleName);
        currentReceipt.getReceipt().setPayerLastName(payerLastName);
        currentReceipt.getReceipt().setPayerPhone(payerPhone);
        currentReceipt.getReceipt().setPayerEmail(payerEmail);
        currentReceipt.getReceipt().setQr(qr);
        //sender data
        currentReceipt.getReceipt().setSenderLogin(senderLogin);
        currentReceipt.getReceipt().setSenderTntAccountNumber(senderTnt);
        currentReceipt.getReceipt().setSenderFedexAccountNumber(senderFedex);
        currentReceipt.getReceipt().setRecipientLogin(recipientLogin);
        currentReceipt.getReceipt().setRecipientCargostarAccountNumber(recipientCargo);
        currentReceipt.getReceipt().setRecipientTntAccountNumber(recipientTnt);
        currentReceipt.getReceipt().setRecipientFedexAccountNumber(recipientFedex);
        //payer data
        currentReceipt.getReceipt().setPayerLogin(payerLogin);
        currentReceipt.getReceipt().setPayerCargostarAccountNumber(payerCargostar);
        currentReceipt.getReceipt().setPayerTntAccountNumber(payerTnt);
        currentReceipt.getReceipt().setPayerFedexAccountNumber(payerFedex);
        currentReceipt.getReceipt().setCheckingAccount(checkingAccount);
        currentReceipt.getReceipt().setBank(bank);
        currentReceipt.getReceipt().setRegistrationCode(registrationCode);
        currentReceipt.getReceipt().setMfo(mfo);
        currentReceipt.getReceipt().setOked(oked);
        currentReceipt.getReceipt().setInstructions(instructions);
        currentReceipt.getReceipt().setPaymentStatus(PaymentStatus.WAITING_PAYMENT);

        Address newSenderAddress = null;
        Address newRecipientAddress = null;
        Address newPayerAddress = null;

        if (!TextUtils.isEmpty(senderCountry) && !TextUtils.isEmpty(senderRegion) && !TextUtils.isEmpty(senderCity) && !TextUtils.isEmpty(senderAddress)) {
            newSenderAddress = new Address(senderCountry, senderRegion, senderCity, senderAddress);
        }
        if (!TextUtils.isEmpty(recipientCountry) && !TextUtils.isEmpty(recipientRegion) && !TextUtils.isEmpty(recipientCity) && !TextUtils.isEmpty(recipientAddress)) {
            newRecipientAddress = new Address(recipientCountry, recipientRegion, recipientCity, recipientAddress);
        }
        if (!TextUtils.isEmpty(payerCountry) && !TextUtils.isEmpty(payerRegion) && !TextUtils.isEmpty(payerCity) && !TextUtils.isEmpty(payerAddress)) {
            newPayerAddress = new Address(payerCountry, payerRegion, payerCity, payerAddress);
        }
        if (!TextUtils.isEmpty(discount)) {
            currentReceipt.getReceipt().setDiscount(Integer.parseInt(discount));
        }
        if (newSenderAddress != null) {
            newSenderAddress.setZip(senderZip);
            currentReceipt.getReceipt().setSenderAddress(newSenderAddress);
        }
        if (newRecipientAddress != null) {
            newRecipientAddress.setZip(recipientZip);
            currentReceipt.getReceipt().setRecipientAddress(newRecipientAddress);
        }
        if (newPayerAddress != null) {
            newPayerAddress.setZip(payerZip);
            currentReceipt.getReceipt().setPayerAddress(newPayerAddress);
        }
        //todo: create viewModel for CreateParcelAcitivity
//        model.updateReceipt(currentReceipt.getReceipt());
        Toast.makeText(context, "Данные сохранены успешно", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void createReceipt() {
        Log.i(TAG, "createReceipt(): " +
                "\nsenderCountry=" + senderCountry + " senderRegion=" + senderRegion + " senderCity=" + senderCity + " senderAddress=" + senderAddress +
                "\nrecipientCountry=" + recipientCountry + " recipientRegion=" + recipientRegion + " recipientCity=" + recipientCity + " recipientAddress=" + recipientAddress +
                "\npayerCountry=" + payerCountry + " payerRegion=" + payerRegion + " payerCity=" + payerCity + " payerAddress=" + payerAddress +
                "\nsenderFirstName=" + senderFirstName +
                "\nsenderMiddleName=" + senderMiddleName +
                "\nsenderLastName=" + senderLastName +
                "\nsenderPhone=" + senderPhone +
                "\ncourierId=" + courierId +
                "\nserviceProvider=" + serviceProvider +
                "\nsenderSignature=" + senderSignature +
                "\nsenderEmail=" + senderEmail +
                "\nsenderCargostar=" + senderCargostar +
                "\nrecipientFirstName=" + recipientFirstName +
                "\nrecipientMiddleName=" + recipientMiddleName +
                "\nrecipientLastName=" + recipientLastName +
                "\nrecipientPhone=" + recipientPhone +
                "\nrecipientEmail=" + recipientEmail +
                "\nrecipientSignature=" + recipientSignature +
                "\npayerFirstName=" + payerFirstName +
                "\npayerMiddleName=" + payerMiddleName +
                "\npayerLastName=" + payerLastName +
                "\npayerPhone=" + payerPhone +
                "\npayerEmail=" + payerEmail +
                "\nqr=" + qr +
                "\nsenderLogin=" + senderLogin +
                "\nsenderTnt=" + senderTnt +
                "\nsenderFedex=" + senderFedex +
                "\nrecipientLogin=" + recipientLogin +
                "\nrecipientCargo=" + recipientCargo +
                "\nrecipientTnt=" + recipientTnt +
                "\nrecipientFedex=" + recipientFedex +
                "\npayerLogin=" + payerLogin +
                "\npayerCargostar=" + payerCargostar +
                "\npayerTnt=" + payerTnt +
                "\npayerFedex=" + payerFedex +
                "\ndiscount=" + discount +
                "\ncheckingAccount=" + checkingAccount +
                "\nbank=" + bank+
                "\nregistrationCode=" + registrationCode +
                "\nmfo=" + mfo +
                "\noked=" + oked +
                "\ninstructions=" + instructions +
                "\ninstructions=" + instructions +
                "\ninstructions=" + instructions);
        //sender data
        if (TextUtils.isEmpty(senderAddress)) {
            Log.i(TAG, "senderAddress empty");
            Toast.makeText(context, "Для создания заявки укажите адрес отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senderCountry)) {
            Log.i(TAG, "senderCountry empty");
            Toast.makeText(context, "Для создания заявки укажите страну отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senderRegion)) {
            Log.i(TAG, "senderRegion empty");
            Toast.makeText(context, "Для создания заявки укажите регион отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senderCity)) {
            Log.i(TAG, "senderCity empty");
            Toast.makeText(context, "Для создания заявки укажите город отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senderZip)) {
            Log.i(TAG, "senderZip empty");
            Toast.makeText(context, "Для создания заявки укажите почтовый индекс отправителя", Toast.LENGTH_SHORT).show();
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
        if (TextUtils.isEmpty(senderSignature)) {
            Toast.makeText(context, "Для создания заявки добавьте подпись отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(senderCargostar)) {
            Toast.makeText(context, "Для создания заявки укажите номер акканута CargoStar отправителя", Toast.LENGTH_SHORT).show();
            return;
        }
        //recipient data
        if (TextUtils.isEmpty(recipientAddress)) {
            Toast.makeText(context, "Для создания заявки укажите адрес получателя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(recipientCountry)) {
            Toast.makeText(context, "Для создания заявки укажите страну получателя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(recipientRegion)) {
            Toast.makeText(context, "Для создания заявки укажите регион получателя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(recipientCity)) {
            Toast.makeText(context, "Для создания заявки укажите город получателя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(recipientZip)) {
            Toast.makeText(context, "Для создания заявки укажите почтовый индекс получателя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(recipientFirstName)) {
            Toast.makeText(context, "Для создания заявки укажите имя получателя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(recipientMiddleName)) {
            Toast.makeText(context, "Для создания заявки укажите отчество получателя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(recipientLastName)) {
            Toast.makeText(context, "Для создания заявки укажите фамилию получателя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(recipientPhone)) {
            Toast.makeText(context, "Для создания заявки укажите номер телефона получателя", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(recipientEmail)) {
            Toast.makeText(context, "Для создания заявки укажите email получателя", Toast.LENGTH_SHORT).show();
            return;
        }
        //payer data
        if (TextUtils.isEmpty(payerAddress)) {
            Toast.makeText(context, "Для создания заявки укажите адрес плательщика", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(payerCountry)) {
            Toast.makeText(context, "Для создания заявки укажите страну плательщика", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(payerRegion)) {
            Toast.makeText(context, "Для создания заявки укажите регион плательщика", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(payerCity)) {
            Toast.makeText(context, "Для создания заявки укажите город плательщика", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(payerZip)) {
            Toast.makeText(context, "Для создания заявки укажите почтовый индекс плательщика", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(payerFirstName)) {
            Toast.makeText(context, "Для создания заявки укажите имя плательщика", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(payerMiddleName)) {
            Toast.makeText(context, "Для создания заявки укажите отчество плательщика", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(payerLastName)) {
            Toast.makeText(context, "Для создания заявки укажите фамилию плательщика", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(payerPhone)) {
            Toast.makeText(context, "Для создания заявки укажите номер телефона плательщика", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(payerEmail)) {
            Toast.makeText(context, "Для создания заявки укажите email плательщика", Toast.LENGTH_SHORT).show();
            return;
        }
        //parcel data
        if (TextUtils.isEmpty(qr)) {
            Toast.makeText(context, "Для создания заявки отсканируйте QR-код", Toast.LENGTH_SHORT).show();
            return;
        }
        if (cargoList.isEmpty()) {
            Toast.makeText(context, "Для создания заявки добавьте хотя бы 1 груз", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!senderCountry.equals(getString(R.string.uzbekistan)) && !recipientCountry.equals(getString(R.string.uzbekistan))) {
            Toast.makeText(context, "Страна отправителя или получателя должна быть Узбекистан", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!firstCardRadioBtn.isChecked() && !secondCardRadioBtn.isChecked()) {
            Toast.makeText(context, "Для создания заявки выберите поставщика услуг", Toast.LENGTH_SHORT).show();
            return;
        }

        final Address newSenderAddress = new Address(senderCountry, senderRegion, senderCity, senderAddress);
        final Address newRecipientAddress = new Address(recipientCountry, recipientRegion, recipientCity, recipientAddress);
        final Address newPayerAddress = new Address(payerCountry, payerRegion, payerCity, payerAddress);
        newSenderAddress.setZip(senderZip);
        newRecipientAddress.setZip(recipientZip);
        newPayerAddress.setZip(payerZip);

        final Receipt newRequest = new Receipt(senderFirstName, senderMiddleName, senderLastName, newSenderAddress, senderPhone, TransportationStatus.IN_TRANSIT, PaymentStatus.WAITING_PAYMENT);
        newRequest.setCourierId(SharedPrefs.getInstance(context).getLong(SharedPrefs.ID));
        newRequest.setServiceProvider(serviceProvider);
        newRequest.setSenderSignature(senderSignature);
        newRequest.setSenderEmail(senderEmail);
        newRequest.setSenderCargostarAccountNumber(senderCargostar);
        newRequest.setRecipientAddress(newRecipientAddress);
        newRequest.setRecipientFirstName(recipientFirstName);
        newRequest.setRecipientMiddleName(recipientMiddleName);
        newRequest.setRecipientLastName(recipientLastName);
        newRequest.setRecipientPhone(recipientPhone);
        newRequest.setRecipientEmail(recipientEmail);
        newRequest.setPayerAddress(newPayerAddress);
        newRequest.setPayerFirstName(payerFirstName);
        newRequest.setPayerMiddleName(payerMiddleName);
        newRequest.setPayerLastName(payerLastName);
        newRequest.setPayerPhone(payerPhone);
        newRequest.setPayerEmail(payerEmail);
        newRequest.setQr(qr);
        //sender data
        newRequest.setSenderLogin(senderLogin);
        newRequest.setSenderTntAccountNumber(senderTnt);
        newRequest.setSenderFedexAccountNumber(senderFedex);
        newRequest.setRecipientLogin(recipientLogin);
        newRequest.setRecipientCargostarAccountNumber(recipientCargo);
        newRequest.setRecipientTntAccountNumber(recipientTnt);
        newRequest.setRecipientFedexAccountNumber(recipientFedex);
        //payer data
        newRequest.setPayerLogin(payerLogin);
        newRequest.setPayerCargostarAccountNumber(payerCargostar);
        newRequest.setPayerTntAccountNumber(payerTnt);
        newRequest.setPayerFedexAccountNumber(payerFedex);

        if (!TextUtils.isEmpty(discount)) {
            newRequest.setDiscount(Integer.parseInt(discount));
        }
        if (!TextUtils.isEmpty(recipientSignature)) {
            newRequest.setRecipientSignature(recipientSignature);
        }

        newRequest.setCheckingAccount(checkingAccount);
        newRequest.setBank(bank);
        newRequest.setRegistrationCode(registrationCode);
        newRequest.setMfo(mfo);
        newRequest.setOked(oked);
        newRequest.setInstructions(instructions);
        newRequest.setPaymentStatus(PaymentStatus.WAITING_PAYMENT);

        if (tariffRadioGroup.getCheckedRadioButtonId() == expressRadioBtn.getId()) {
            newRequest.setTariff(getString(R.string.express));
        }
        else if (tariffRadioGroup.getCheckedRadioButtonId() == economyRadioBtn.getId()) {
            newRequest.setTariff(getString(R.string.economy_express));
        }
        if (firstCardRadioBtn.isChecked()) {
            serviceProvider = firstCardValue;
        }
        else if (secondCardRadioBtn.isChecked()) {
            serviceProvider = secondCardValue;
        }
        newRequest.setServiceProvider(serviceProvider);

        Toast.makeText(context, "Накладная создана успешно!", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private static final String TAG = CreateParcelActivity.class.toString();

    @Override
    public void onAddBtnClicked() {
        addItem();
    }

    @Override
    public void onCameraImageClicked(final int position) {
        final Intent scanQrIntent = new Intent(context, ScanQrActivity.class);
        if (position == 42) {
            startActivityForResult(scanQrIntent, Constants.REQUEST_SCAN_QR_PARCEL);
        }
        else if (position == 48) {
            startActivityForResult(scanQrIntent, Constants.REQUEST_SCAN_QR_CARGO);
        }
    }

    @Override
    public void onSenderSignatureClicked() {
        startActivityForResult(new Intent(context, SignatureActivity.class), Constants.REQUEST_SENDER_SIGNATURE);
    }

    @Override
    public void onRecipientSignatureClicked() {
        startActivityForResult(new Intent(context, SignatureActivity.class), Constants.REQUEST_RECIPIENT_SIGNATURE);
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
                //todo: create viewModel for CreateParcelActivity
//                model.selectAddressBookEntriesBySenderLogin(senderLogin).observe(this, this::initPayerAddressBook);
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
            case 9: {
                //senderCity
                senderCity = editable.toString();
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
            case 18: {
                //recipientCity
                recipientCity = editable.toString();
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
            case 26: {
                //payerCountry
                payerCountry = editable.toString();
                break;
            }
            case 27: {
                //payerRegion
                payerRegion = editable.toString();
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
            case 2: {
                //serviceProvider
                serviceProvider = editable.toString();
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
            case 8: {
                //senderCountry
                senderCountry = editable.toString();

                if (!TextUtils.isEmpty(recipientCountry)) {
                    if (senderCountry.equals(getString(R.string.uzbekistan)) && recipientCountry.equals(getString(R.string.uzbekistan))) {
                        firstCardRadioBtn.setChecked(true);
                        firstCardImageView.setImageResource(R.drawable.logo_cargo_calc);
                        firstCardValue = getString(R.string.cargostar);
                        secondCardRadioBtn.setVisibility(View.INVISIBLE);
                        secondCardImageView.setVisibility(View.INVISIBLE);
                        secondCard.setVisibility(View.INVISIBLE);
                        return;
                    }
                    else if (senderCountry.equals(getString(R.string.uzbekistan)) && !recipientCountry.equals(getString(R.string.uzbekistan))) {
                        firstCardRadioBtn.setChecked(true);
                        firstCardImageView.setImageResource(R.drawable.logo_tnt_cacl);
                        firstCardValue = getString(R.string.tnt);
                        secondCardImageView.setImageResource(R.drawable.logo_fedex_calc);
                        secondCardRadioBtn.setVisibility(View.VISIBLE);
                        secondCardImageView.setVisibility(View.VISIBLE);
                        secondCard.setVisibility(View.VISIBLE);
                        secondCardValue = getString(R.string.fedex);
                    }
                    else if (!senderCountry.equals(getString(R.string.uzbekistan)) && recipientCountry.equals(getString(R.string.uzbekistan))) {
                        firstCardRadioBtn.setChecked(true);
                        firstCardImageView.setImageResource(R.drawable.logo_tnt_cacl);
                        firstCardValue = getString(R.string.tnt);
                        secondCardRadioBtn.setVisibility(View.INVISIBLE);
                        secondCardImageView.setVisibility(View.INVISIBLE);
                        secondCard.setVisibility(View.INVISIBLE);
                    }
                }
                break;
            }
            case 9: {
                //senderRegion
                senderRegion = editable.toString();
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
            case 17: {
                //recipientCountry
                recipientCountry = editable.toString();

                if (!TextUtils.isEmpty(senderCountry)) {
                    if (senderCountry.equals(getString(R.string.uzbekistan)) && recipientCountry.equals(getString(R.string.uzbekistan))) {
                        firstCardRadioBtn.setChecked(true);
                        firstCardImageView.setImageResource(R.drawable.logo_cargo_calc);
                        firstCardValue = getString(R.string.cargostar);
                        secondCardRadioBtn.setVisibility(View.INVISIBLE);
                        secondCardImageView.setVisibility(View.INVISIBLE);
                        secondCard.setVisibility(View.INVISIBLE);
                        return;
                    } else if (senderCountry.equals(getString(R.string.uzbekistan)) && !recipientCountry.equals(getString(R.string.uzbekistan))) {
                        firstCardRadioBtn.setChecked(true);
                        firstCardImageView.setImageResource(R.drawable.logo_tnt_cacl);
                        firstCardValue = getString(R.string.tnt);
                        secondCardImageView.setImageResource(R.drawable.logo_fedex_calc);
                        secondCardRadioBtn.setVisibility(View.VISIBLE);
                        secondCardImageView.setVisibility(View.VISIBLE);
                        secondCard.setVisibility(View.VISIBLE);
                        secondCardValue = getString(R.string.fedex);
                    } else if (!senderCountry.equals(getString(R.string.uzbekistan)) && recipientCountry.equals(getString(R.string.uzbekistan))) {
                        firstCardRadioBtn.setChecked(true);
                        firstCardImageView.setImageResource(R.drawable.logo_tnt_cacl);
                        firstCardValue = getString(R.string.tnt);
                        secondCardRadioBtn.setVisibility(View.INVISIBLE);
                        secondCardImageView.setVisibility(View.INVISIBLE);
                        secondCard.setVisibility(View.INVISIBLE);
                    }
                }
                break;
            }
            case 18: {
                //recipientRegion
                recipientRegion = editable.toString();
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
            case 26: {
                //payerCity
                payerCity = editable.toString();
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
            if (requestCode == Constants.REQUEST_UPLOAD_PHOTO) {
                final Uri selectedImage = data.getData();
                if (selectedImage != null) {
//                    photoResultImageView.setVisibility(View.VISIBLE);
//                    photoEditText.setBackgroundResource(R.drawable.edit_text_active);
                    return;
                }
            }
            if (requestCode == Constants.REQUEST_UPLOAD_DOCUMENT) {
                final Uri selectedDoc = data.getData();
                if (selectedDoc != null) {
//                    contractResultImageView.setImageResource(R.drawable.ic_doc_green);
//                    contractResultImageView.setVisibility(View.VISIBLE);
//                    contractEditText.setBackgroundResource(R.drawable.edit_text_active);
                    return;
                }
            }
            if (requestCode == Constants.REQUEST_SCAN_QR_PARCEL) {
                Log.i(TAG, "onActivityResult: " + data.getStringExtra(Constants.INTENT_RESULT_VALUE));
                itemList.get(42).firstValue = data.getStringExtra(Constants.INTENT_RESULT_VALUE);
                qr = data.getStringExtra(Constants.INTENT_RESULT_VALUE);
                adapter.notifyItemChanged(42);
                return;
            }
            if (requestCode == Constants.REQUEST_SCAN_QR_CARGO) {
                Log.i(TAG, "onActivityResult: " + data.getStringExtra(Constants.INTENT_RESULT_VALUE));
                itemList.get(48).firstValue = data.getStringExtra(Constants.INTENT_RESULT_VALUE);
                adapter.notifyItemChanged(48);
                return;
            }
            if (requestCode == Constants.REQUEST_SENDER_SIGNATURE) {
                itemList.get(5).firstValue = data.getStringExtra(Constants.INTENT_RESULT_VALUE);
                senderSignature = data.getStringExtra(Constants.INTENT_RESULT_VALUE);
                adapter.notifyItemChanged(5);
                return;
            }
            if (requestCode == Constants.REQUEST_RECIPIENT_SIGNATURE) {
                itemList.get(5).secondValue = data.getStringExtra(Constants.INTENT_RESULT_VALUE);
                recipientSignature = data.getStringExtra(Constants.INTENT_RESULT_VALUE);
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
            payerFirstName = addressBook.get(i).getPayerFirstName();
            payerMiddleName = addressBook.get(i).getPayerMiddleName();
            payerLastName = addressBook.get(i).getPayerLastName();
            payerCountry = addressBook.get(i).getPayerAddress().getCountry();
            payerRegion = addressBook.get(i).getPayerAddress().getRegion();
            payerCity = addressBook.get(i).getPayerAddress().getCity();
            payerAddress = addressBook.get(i).getPayerAddress().getAddress();
            payerEmail = addressBook.get(i).getPayerEmail();
            payerPhone = addressBook.get(i).getPayerPhone();
            payerLogin = addressBook.get(i).getPayerLogin();
            checkingAccount = addressBook.get(i).getPayerCheckingAccount();
            bank = addressBook.get(i).getPayerBank();
            mfo = addressBook.get(i).getPayerMfo();
            oked = addressBook.get(i).getPayerOked();
            registrationCode = addressBook.get(i).getPayerRegistrationCode();
            payerCargostar = addressBook.get(i).getCargostarAccountNumber();
            payerTnt = addressBook.get(i).getTntAccountNumber();
            payerFedex = addressBook.get(i).getFedexAccountNumber();
            //25 payerLogin 25 payerAddress
            itemList.get(25).firstValue = payerLogin;
            itemList.get(25).secondValue = payerAddress;
            adapter.notifyItemChanged(25);
            //26 payerCountry 26 payerCity
            itemList.get(26).firstValue = payerCountry;
            itemList.get(26).secondValue = payerCity;
            adapter.notifyItemChanged(26);
            //27 payerRegion 27 payerZip
            itemList.get(27).firstValue = payerRegion;
            itemList.get(27).secondValue = payerZip;
            adapter.notifyItemChanged(27);
            //28 payerFirstName 28 payerMiddleName
            itemList.get(28).firstValue = payerFirstName;
            itemList.get(28).secondValue = payerMiddleName;
            adapter.notifyItemChanged(28);
            //29 payerLastName 29 payerPhone
            itemList.get(29).firstValue = payerLastName;
            itemList.get(29).secondValue = payerPhone;
            adapter.notifyItemChanged(29);
            //30 payerEmail
            itemList.get(30).firstValue = payerEmail;
            adapter.notifyItemChanged(30);
            //31 payerCargo
            itemList.get(31).firstValue = payerCargostar;
            adapter.notifyItemChanged(31);
            //32 payerTnt
            itemList.get(32).firstValue = payerTnt;
            adapter.notifyItemChanged(32);
            //33 payerFedex
            itemList.get(33).firstValue = payerFedex;
            adapter.notifyItemChanged(33);
            //34 checkingAccount 34 bank
            itemList.get(34).firstValue = checkingAccount;
            itemList.get(34).secondValue = bank;
            adapter.notifyItemChanged(34);
            //35 registrationCode 35 mfo
            itemList.get(35).firstValue = registrationCode;
            itemList.get(35).secondValue = mfo;
            adapter.notifyItemChanged(35);
            //36 oked
            itemList.get(36).firstValue = oked;
            adapter.notifyItemChanged(36);
        }
    }
}