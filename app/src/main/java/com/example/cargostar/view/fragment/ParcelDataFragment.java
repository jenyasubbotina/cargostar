package com.example.cargostar.view.fragment;

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

import com.example.cargostar.R;
import com.example.cargostar.model.database.SharedPrefs;
import com.example.cargostar.model.shipping.Cargo;
import com.example.cargostar.view.Constants;
import com.example.cargostar.view.activity.CalculatorActivity;
import com.example.cargostar.view.activity.CreateParcelActivity;
import com.example.cargostar.view.activity.CreateUserActivity;
import com.example.cargostar.view.activity.MainActivity;
import com.example.cargostar.view.activity.NotificationsActivity;
import com.example.cargostar.view.activity.ProfileActivity;
import com.example.cargostar.view.adapter.ParcelData;
import com.example.cargostar.view.adapter.ParcelDataAdapter;
import com.example.cargostar.view.callback.ParcelDataCallback;
import com.example.cargostar.viewmodel.HeaderViewModel;
import com.example.cargostar.viewmodel.PopulateViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ParcelDataFragment extends Fragment implements ParcelDataCallback {
    private Context context;
    private FragmentActivity activity;
    private List<ParcelData> itemList;

    private boolean publicDataHidden = false;
    private ParcelData publicDataHeading;
    private final List<ParcelData> publicDataList = new ArrayList<>();

    private boolean senderDataHidden = false;
    private ParcelData senderDataHeading;
    private final List<ParcelData> senderDataList = new ArrayList<>();

    private boolean recipientDataHidden = false;
    private ParcelData recipientDataHeading;
    private final List<ParcelData> recipientDataList = new ArrayList<>();

    private boolean payerDataHidden = false;
    private ParcelData payerDataHeading;
    private final List<ParcelData> payerDataList = new ArrayList<>();

    private boolean accountDataHidden = false;
    private ParcelData accountDataHeading;
    private final List<ParcelData> accountDataList = new ArrayList<>();

    private boolean parcelDataHidden = false;
    private ParcelData parcelDataHeading;
    private final List<ParcelData> parcelDataList = new ArrayList<>();

    private boolean forEachCargoHidden = false;
    private ParcelData forEachCargoHeading;
    private int cargoListSize = -1;
    private final List<ParcelData> forEachCargoList = new ArrayList<>();

    private boolean paymentDataHidden = false;
    private ParcelData paymentDataHeading;
    private final List<ParcelData> paymentDataList = new ArrayList<>();

    private boolean logisticsDataHidden = false;
    private ParcelData logisticsDataHeading;
    private final List<ParcelData> logisticsDataList = new ArrayList<>();

    private boolean documentsDataHidden = false;
    private ParcelData documentsDataHeading;
    private final List<ParcelData> documentsDataList = new ArrayList<>();
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
    private ParcelDataAdapter adapter;
    private ImageView editParcelImageView;

    private long requestId;
    private int requestOrParcel;

    public ParcelDataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        context = getContext();
        itemList = new ArrayList<>();

        publicDataHeading = new ParcelData(getString(R.string.public_data), null, ParcelData.TYPE_HEADING);
        senderDataHeading = new ParcelData(getString(R.string.sender_data), null, ParcelData.TYPE_HEADING);
        recipientDataHeading = new ParcelData(getString(R.string.receiver_data), null, ParcelData.TYPE_HEADING);
        payerDataHeading = new ParcelData(getString(R.string.payer_data), null, ParcelData.TYPE_HEADING);
        accountDataHeading = new ParcelData(getString(R.string.account_numbers), null, ParcelData.TYPE_HEADING);
        parcelDataHeading = new ParcelData(getString(R.string.parcel_data), null, ParcelData.TYPE_HEADING);
        forEachCargoHeading = new ParcelData(getString(R.string.for_each_cargo), null, ParcelData.TYPE_HEADING);
        paymentDataHeading = new ParcelData(getString(R.string.payment_data), null, ParcelData.TYPE_HEADING);
        logisticsDataHeading = new ParcelData(getString(R.string.logistics_data), null, ParcelData.TYPE_HEADING);
        documentsDataHeading = new ParcelData(getString(R.string.additional_documents), null, ParcelData.TYPE_HEADING);

        if (getArguments() != null) {
            requestId = ParcelDataFragmentArgs.fromBundle(getArguments()).getParcelId();
            requestOrParcel = ParcelDataFragmentArgs.fromBundle(getArguments()).getRequestOrParcel();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_parcel_data, container, false);
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
        adapter = new ParcelDataAdapter(context, this);
        dataRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        dataRecyclerView.setAdapter(adapter);
        editParcelImageView = root.findViewById(R.id.edit_parcel_image_view);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //header views
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final HeaderViewModel headerViewModel = new ViewModelProvider(this).get(HeaderViewModel.class);
        //header views
        headerViewModel.selectCourierByLogin(SharedPrefs.getInstance(context).getString(SharedPrefs.LOGIN)).observe(getViewLifecycleOwner(), courier -> {
            if (courier != null) {
                fullNameTextView.setText(courier.getFirstName() + " " + courier.getLastName());
            }
        });
        headerViewModel.selectBranchByCourierId(SharedPrefs.getInstance(context).getLong(SharedPrefs.ID)).observe(getViewLifecycleOwner(), branch -> {
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

            headerViewModel.selectRequest(parcelId).observe(getViewLifecycleOwner(), receiptWithCargoList -> {
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

        editParcelImageView.setOnClickListener(v -> {
            final Intent createParcelIntent = new Intent(getContext(), CreateParcelActivity.class);
            createParcelIntent.putExtra(Constants.INTENT_REQUEST_KEY, Constants.REQUEST_EDIT_PARCEL);
            createParcelIntent.putExtra(Constants.INTENT_REQUEST_VALUE, requestId);
            createParcelIntent.putExtra(Constants.INTENT_REQUEST_OR_PARCEL, requestOrParcel);
            startActivity(createParcelIntent);
        });

        headerViewModel.selectRequest(requestId).observe(getViewLifecycleOwner(), request -> {
            itemList.clear();
            //public data
            itemList.add(publicDataHeading);
            itemList.add(new ParcelData(getString(R.string.parcel_id), String.valueOf(request.getReceipt().getId()), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.service_provider), request.getReceipt().getServiceProvider(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.courier_id), request.getReceipt().getCourierId() > 0 ? String.valueOf(request.getReceipt().getCourierId()) : null, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.operator_id), request.getReceipt().getOperatorId(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.accountant_id), request.getReceipt().getAccountantId(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.sender_signature), request.getReceipt().getSenderSignature(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.receiver_signature), request.getReceipt().getRecipientSignature(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(null, null, ParcelData.TYPE_STROKE));
            publicDataList.add(new ParcelData(getString(R.string.parcel_id), String.valueOf(request.getReceipt().getId()), ParcelData.TYPE_ITEM));
            publicDataList.add(new ParcelData(getString(R.string.service_provider), request.getReceipt().getServiceProvider(), ParcelData.TYPE_ITEM));
            publicDataList.add(new ParcelData(getString(R.string.courier_id), request.getReceipt().getCourierId() > 0 ? String.valueOf(request.getReceipt().getCourierId()) : null, ParcelData.TYPE_ITEM));
            publicDataList.add(new ParcelData(getString(R.string.operator_id), request.getReceipt().getOperatorId(), ParcelData.TYPE_ITEM));
            publicDataList.add(new ParcelData(getString(R.string.accountant_id), request.getReceipt().getAccountantId(), ParcelData.TYPE_ITEM));
            publicDataList.add(new ParcelData(getString(R.string.sender_signature), request.getReceipt().getSenderSignature(), ParcelData.TYPE_ITEM));
            publicDataList.add(new ParcelData(getString(R.string.receiver_signature), request.getReceipt().getRecipientSignature(), ParcelData.TYPE_ITEM));
            //sender data
            itemList.add(senderDataHeading);
            itemList.add(new ParcelData(getString(R.string.login_email), request.getReceipt().getSenderLogin(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.cargostar_account_number), request.getReceipt().getSenderCargostarAccountNumber(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.tnt_account_number), request.getReceipt().getSenderTntAccountNumber(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.fedex_account_number), request.getReceipt().getSenderFedexAccountNumber(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.take_address), request.getReceipt().getSenderAddress().getAddress(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.country), request.getReceipt().getSenderAddress().getCountry(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.region), request.getReceipt().getSenderAddress().getRegion(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.city), request.getReceipt().getSenderAddress().getCity(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.post_index), request.getReceipt().getSenderAddress().getZip(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.first_name), request.getReceipt().getSenderFirstName(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.middle_name), request.getReceipt().getSenderMiddleName(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.last_name), request.getReceipt().getSenderLastName(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.phone_number), request.getReceipt().getSenderPhone(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.email), request.getReceipt().getSenderEmail(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(null, null, ParcelData.TYPE_STROKE));
            senderDataList.add(new ParcelData(getString(R.string.login_email), request.getReceipt().getSenderLogin(), ParcelData.TYPE_ITEM));
            senderDataList.add(new ParcelData(getString(R.string.cargostar_account_number), request.getReceipt().getSenderCargostarAccountNumber(), ParcelData.TYPE_ITEM));
            senderDataList.add(new ParcelData(getString(R.string.tnt_account_number), request.getReceipt().getSenderTntAccountNumber(), ParcelData.TYPE_ITEM));
            senderDataList.add(new ParcelData(getString(R.string.fedex_account_number), request.getReceipt().getSenderFedexAccountNumber(), ParcelData.TYPE_ITEM));
            senderDataList.add(new ParcelData(getString(R.string.take_address), request.getReceipt().getSenderAddress().getAddress(), ParcelData.TYPE_ITEM));
            senderDataList.add(new ParcelData(getString(R.string.country), request.getReceipt().getSenderAddress().getCountry(), ParcelData.TYPE_ITEM));
            senderDataList.add(new ParcelData(getString(R.string.region), request.getReceipt().getSenderAddress().getRegion(), ParcelData.TYPE_ITEM));
            senderDataList.add(new ParcelData(getString(R.string.city), request.getReceipt().getSenderAddress().getCity(), ParcelData.TYPE_ITEM));
            senderDataList.add(new ParcelData(getString(R.string.post_index), request.getReceipt().getSenderAddress().getZip(), ParcelData.TYPE_ITEM));
            senderDataList.add(new ParcelData(getString(R.string.first_name), request.getReceipt().getSenderFirstName(), ParcelData.TYPE_ITEM));
            senderDataList.add(new ParcelData(getString(R.string.middle_name), request.getReceipt().getSenderMiddleName(), ParcelData.TYPE_ITEM));
            senderDataList.add(new ParcelData(getString(R.string.last_name), request.getReceipt().getSenderLastName(), ParcelData.TYPE_ITEM));
            senderDataList.add(new ParcelData(getString(R.string.phone_number), request.getReceipt().getSenderPhone(), ParcelData.TYPE_ITEM));
            senderDataList.add(new ParcelData(getString(R.string.email), request.getReceipt().getSenderEmail(), ParcelData.TYPE_ITEM));
            //recipient data
            itemList.add(recipientDataHeading);
            itemList.add(new ParcelData(getString(R.string.login_email), request.getReceipt().getRecipientLogin(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.cargostar_account_number), request.getReceipt().getRecipientCargostarAccountNumber(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.tnt_account_number), request.getReceipt().getRecipientTntAccountNumber(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.fedex_account_number), request.getReceipt().getRecipientFedexAccountNumber(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.take_address), request.getReceipt().getRecipientAddress() != null ? request.getReceipt().getRecipientAddress().getAddress() : null, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.country), request.getReceipt().getRecipientAddress() != null ? request.getReceipt().getRecipientAddress().getCountry() : null, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.region), request.getReceipt().getRecipientAddress() != null ? request.getReceipt().getRecipientAddress().getRegion() : null, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.city), request.getReceipt().getRecipientAddress() != null ? request.getReceipt().getRecipientAddress().getCity() : null, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.post_index), request.getReceipt().getRecipientAddress() != null ? request.getReceipt().getRecipientAddress().getZip() : null, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.first_name), request.getReceipt().getRecipientFirstName(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.middle_name), request.getReceipt().getRecipientMiddleName(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.last_name), request.getReceipt().getRecipientLastName(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.phone_number), request.getReceipt().getRecipientPhone(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.email), request.getReceipt().getRecipientEmail(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(null, null, ParcelData.TYPE_STROKE));
            recipientDataList.add(new ParcelData(getString(R.string.login_email), request.getReceipt().getRecipientLogin(), ParcelData.TYPE_ITEM));
            recipientDataList.add(new ParcelData(getString(R.string.cargostar_account_number), request.getReceipt().getRecipientCargostarAccountNumber(), ParcelData.TYPE_ITEM));
            recipientDataList.add(new ParcelData(getString(R.string.tnt_account_number), request.getReceipt().getRecipientTntAccountNumber(), ParcelData.TYPE_ITEM));
            recipientDataList.add(new ParcelData(getString(R.string.fedex_account_number), request.getReceipt().getRecipientFedexAccountNumber(), ParcelData.TYPE_ITEM));
            recipientDataList.add(new ParcelData(getString(R.string.take_address), request.getReceipt().getRecipientAddress() != null ? request.getReceipt().getRecipientAddress().getAddress() : null, ParcelData.TYPE_ITEM));
            recipientDataList.add(new ParcelData(getString(R.string.country), request.getReceipt().getRecipientAddress() != null ? request.getReceipt().getRecipientAddress().getCountry() : null, ParcelData.TYPE_ITEM));
            recipientDataList.add(new ParcelData(getString(R.string.region), request.getReceipt().getRecipientAddress() != null ? request.getReceipt().getRecipientAddress().getRegion() : null, ParcelData.TYPE_ITEM));
            recipientDataList.add(new ParcelData(getString(R.string.city), request.getReceipt().getRecipientAddress() != null ? request.getReceipt().getRecipientAddress().getCity() : null, ParcelData.TYPE_ITEM));
            recipientDataList.add(new ParcelData(getString(R.string.post_index), request.getReceipt().getRecipientAddress() != null ? request.getReceipt().getRecipientAddress().getZip() : null, ParcelData.TYPE_ITEM));
            recipientDataList.add(new ParcelData(getString(R.string.first_name), request.getReceipt().getRecipientFirstName(), ParcelData.TYPE_ITEM));
            recipientDataList.add(new ParcelData(getString(R.string.middle_name), request.getReceipt().getRecipientMiddleName(), ParcelData.TYPE_ITEM));
            recipientDataList.add(new ParcelData(getString(R.string.last_name), request.getReceipt().getRecipientLastName(), ParcelData.TYPE_ITEM));
            recipientDataList.add(new ParcelData(getString(R.string.phone_number), request.getReceipt().getRecipientPhone(), ParcelData.TYPE_ITEM));
            recipientDataList.add(new ParcelData(getString(R.string.email), request.getReceipt().getRecipientEmail(), ParcelData.TYPE_ITEM));
            //payer data
            itemList.add(payerDataHeading);
            itemList.add(new ParcelData(getString(R.string.login_email), request.getReceipt().getPayerLogin(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.cargostar_account_number), request.getReceipt().getPayerCargostarAccountNumber(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.tnt_account_number), request.getReceipt().getPayerTntAccountNumber(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.fedex_account_number), request.getReceipt().getPayerFedexAccountNumber(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.take_address), request.getReceipt().getPayerAddress() != null ? request.getReceipt().getPayerAddress().getAddress() : null, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.country), request.getReceipt().getPayerAddress() != null ? request.getReceipt().getPayerAddress().getCountry() : null, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.region), request.getReceipt().getPayerAddress() != null ? request.getReceipt().getPayerAddress().getRegion() : null, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.city), request.getReceipt().getPayerAddress() != null ? request.getReceipt().getPayerAddress().getCity() : null, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.post_index), request.getReceipt().getPayerAddress() != null ? request.getReceipt().getPayerAddress().getZip() : null, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.first_name), request.getReceipt().getPayerFirstName(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.middle_name), request.getReceipt().getPayerMiddleName(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.last_name), request.getReceipt().getPayerLastName(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.phone_number), request.getReceipt().getPayerPhone(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.email), request.getReceipt().getPayerEmail(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.discount), request.getReceipt().getDiscount() > 0 ? String.valueOf(request.getReceipt().getDiscount()) : null, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(null, null, ParcelData.TYPE_STROKE));
            payerDataList.add(new ParcelData(getString(R.string.login_email), request.getReceipt().getPayerLogin(), ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.cargostar_account_number), request.getReceipt().getPayerCargostarAccountNumber(), ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.tnt_account_number), request.getReceipt().getPayerTntAccountNumber(), ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.fedex_account_number), request.getReceipt().getPayerFedexAccountNumber(), ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.take_address), request.getReceipt().getPayerAddress() != null ? request.getReceipt().getPayerAddress().getAddress() : null, ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.country), request.getReceipt().getPayerAddress() != null ? request.getReceipt().getPayerAddress().getCountry() : null, ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.region), request.getReceipt().getPayerAddress() != null ? request.getReceipt().getPayerAddress().getRegion() : null, ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.city), request.getReceipt().getPayerAddress() != null ? request.getReceipt().getPayerAddress().getCity() : null, ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.post_index), request.getReceipt().getPayerAddress() != null ? request.getReceipt().getPayerAddress().getZip() : null, ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.first_name), request.getReceipt().getPayerFirstName(), ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.middle_name), request.getReceipt().getPayerMiddleName(), ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.last_name), request.getReceipt().getPayerLastName(), ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.phone_number), request.getReceipt().getPayerPhone(), ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.email), request.getReceipt().getPayerEmail(), ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.discount), request.getReceipt().getDiscount() > 0 ? String.valueOf(request.getReceipt().getDiscount()) : null, ParcelData.TYPE_ITEM));
            //accounts data
            itemList.add(accountDataHeading);
            itemList.add(new ParcelData(getString(R.string.checking_account), request.getReceipt().getCheckingAccount(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.bank), request.getReceipt().getBank(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.payer_registration_code), request.getReceipt().getRegistrationCode(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.mfo), request.getReceipt().getMfo(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.oked), request.getReceipt().getOked(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(null, null, ParcelData.TYPE_STROKE));
            accountDataList.add(new ParcelData(getString(R.string.checking_account), request.getReceipt().getCheckingAccount(), ParcelData.TYPE_ITEM));
            accountDataList.add(new ParcelData(getString(R.string.bank), request.getReceipt().getBank(), ParcelData.TYPE_ITEM));
            accountDataList.add(new ParcelData(getString(R.string.payer_registration_code), request.getReceipt().getRegistrationCode(), ParcelData.TYPE_ITEM));
            accountDataList.add(new ParcelData(getString(R.string.mfo), request.getReceipt().getMfo(), ParcelData.TYPE_ITEM));
            accountDataList.add(new ParcelData(getString(R.string.oked), request.getReceipt().getOked(), ParcelData.TYPE_ITEM));
            //parcel data
            itemList.add(parcelDataHeading);
            itemList.add(new ParcelData(getString(R.string.tracking_code_main), request.getReceipt().getTrackingCode(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.qr_code), request.getReceipt().getQr(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.courier_guidelines), request.getReceipt().getInstructions(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.tariff), request.getReceipt().getTariff(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.destination_quantity), String.valueOf(request.getCargoList().size()), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(null, null, ParcelData.TYPE_STROKE));
            parcelDataList.add(new ParcelData(getString(R.string.tracking_code_main), request.getReceipt().getTrackingCode(), ParcelData.TYPE_ITEM));
            parcelDataList.add(new ParcelData(getString(R.string.qr_code), request.getReceipt().getQr(), ParcelData.TYPE_ITEM));
            parcelDataList.add(new ParcelData(getString(R.string.courier_guidelines), request.getReceipt().getInstructions(), ParcelData.TYPE_ITEM));
            parcelDataList.add(new ParcelData(getString(R.string.tariff), request.getReceipt().getTariff(), ParcelData.TYPE_ITEM));
            parcelDataList.add(new ParcelData(getString(R.string.destination_quantity), String.valueOf(request.getCargoList().size()), ParcelData.TYPE_ITEM));
            //for each cargo
            itemList.add(forEachCargoHeading);
            cargoListSize = request.getCargoList().size();
            for (final Cargo cargo : request.getCargoList()) {
                itemList.add(new ParcelData(getString(R.string.cargo_description), cargo.getDescription(), ParcelData.TYPE_ITEM));
                itemList.add(new ParcelData(getString(R.string.package_type), cargo.getPackageType(), ParcelData.TYPE_ITEM));
                itemList.add(new ParcelData(getString(R.string.length), String.valueOf(cargo.getLength()), ParcelData.TYPE_ITEM));
                itemList.add(new ParcelData(getString(R.string.width), String.valueOf(cargo.getWidth()), ParcelData.TYPE_ITEM));
                itemList.add(new ParcelData(getString(R.string.height), String.valueOf(cargo.getHeight()), ParcelData.TYPE_ITEM));
                itemList.add(new ParcelData(getString(R.string.weight), String.valueOf(cargo.getWeight()), ParcelData.TYPE_ITEM));
                itemList.add(new ParcelData(getString(R.string.qr_code), cargo.getQr(), ParcelData.TYPE_ITEM));
                itemList.add(new ParcelData(null, null, ParcelData.TYPE_STROKE));
                forEachCargoList.add(new ParcelData(getString(R.string.cargo_description), cargo.getDescription(), ParcelData.TYPE_ITEM));
                forEachCargoList.add(new ParcelData(getString(R.string.package_type), cargo.getPackageType(), ParcelData.TYPE_ITEM));
                forEachCargoList.add(new ParcelData(getString(R.string.length), String.valueOf(cargo.getLength()), ParcelData.TYPE_ITEM));
                forEachCargoList.add(new ParcelData(getString(R.string.width), String.valueOf(cargo.getWidth()), ParcelData.TYPE_ITEM));
                forEachCargoList.add(new ParcelData(getString(R.string.height), String.valueOf(cargo.getHeight()), ParcelData.TYPE_ITEM));
                forEachCargoList.add(new ParcelData(getString(R.string.weight), String.valueOf(cargo.getWeight()), ParcelData.TYPE_ITEM));
                forEachCargoList.add(new ParcelData(getString(R.string.qr_code), cargo.getQr(), ParcelData.TYPE_ITEM));
                forEachCargoList.add(new ParcelData(null, null, ParcelData.TYPE_STROKE));
            }
            forEachCargoList.remove(forEachCargoList.size() - 1);
            //payment data
            itemList.add(paymentDataHeading);
            itemList.add(new ParcelData(getString(R.string.cost), request.getReceipt().getCost() > 0 ? String.valueOf(request.getReceipt().getCost()) : null, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.fuel_tax), request.getReceipt().getFuelCharge() > 0 ? String.valueOf(request.getReceipt().getFuelCharge()) : null, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.NDC), request.getReceipt().getVat() > 0 ? String.valueOf(request.getReceipt().getVat()) : null, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(null, null, ParcelData.TYPE_STROKE));
            paymentDataList.add(new ParcelData(getString(R.string.cost), request.getReceipt().getCost() > 0 ? String.valueOf(request.getReceipt().getCost()) : null, ParcelData.TYPE_ITEM));
            paymentDataList.add(new ParcelData(getString(R.string.fuel_tax), request.getReceipt().getFuelCharge() > 0 ? String.valueOf(request.getReceipt().getFuelCharge()) : null, ParcelData.TYPE_ITEM));
            paymentDataList.add(new ParcelData(getString(R.string.NDC), request.getReceipt().getVat() > 0 ? String.valueOf(request.getReceipt().getVat()) : null, ParcelData.TYPE_ITEM));
            //transportation data
            itemList.add(logisticsDataHeading);

            String dispatchDate = null;
            String arrivalDate = null;

            if (request.getReceipt().getDispatchDate() != null) {
                dispatchDate = dateFormatter.format(request.getReceipt().getDispatchDate());
            }
            if (request.getReceipt().getArrivalDate() != null) {
                arrivalDate = dateFormatter.format(request.getReceipt().getArrivalDate());
            }
            itemList.add(new ParcelData(getString(R.string.dispatch_date), dispatchDate, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.arrival_date), arrivalDate, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(null, null, ParcelData.TYPE_STROKE));
            logisticsDataList.add(new ParcelData(getString(R.string.dispatch_date), dispatchDate, ParcelData.TYPE_ITEM));
            logisticsDataList.add(new ParcelData(getString(R.string.arrival_date), arrivalDate, ParcelData.TYPE_ITEM));
            //documents data
            itemList.add(documentsDataHeading);
            itemList.add(new ParcelData(getString(R.string.e_bid), request.getReceipt().getRequest() == null ? getString(R.string.absent) : getString(R.string.download), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.e_waybill), request.getReceipt().getReceipt() == null ? getString(R.string.absent) : getString(R.string.download), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.payer_guarantee_letter), request.getReceipt().getGuaranteeLetter() == null ? getString(R.string.absent) : getString(R.string.download), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.pay_bill), request.getReceipt().getPaybill() == null ? getString(R.string.absent) : getString(R.string.download), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.invoice_document), request.getReceipt().getInvoice() == null ? getString(R.string.absent) : getString(R.string.download), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.invoice), request.getReceipt().getInvoice() == null ? getString(R.string.absent) : getString(R.string.download), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(null, null, ParcelData.TYPE_STROKE));
            documentsDataList.add(new ParcelData(getString(R.string.e_bid), request.getReceipt().getRequest() == null ? getString(R.string.absent) : getString(R.string.download), ParcelData.TYPE_ITEM));
            documentsDataList.add(new ParcelData(getString(R.string.e_waybill), request.getReceipt().getReceipt() == null ? getString(R.string.absent) : getString(R.string.download), ParcelData.TYPE_ITEM));
            documentsDataList.add(new ParcelData(getString(R.string.payer_guarantee_letter), request.getReceipt().getGuaranteeLetter() == null ? getString(R.string.absent) : getString(R.string.download), ParcelData.TYPE_ITEM));
            documentsDataList.add(new ParcelData(getString(R.string.pay_bill), request.getReceipt().getPaybill() == null ? getString(R.string.absent) : getString(R.string.download), ParcelData.TYPE_ITEM));
            documentsDataList.add(new ParcelData(getString(R.string.invoice_document), request.getReceipt().getWaybill() == null ? getString(R.string.absent) : getString(R.string.download), ParcelData.TYPE_ITEM));
            documentsDataList.add(new ParcelData(getString(R.string.invoice), request.getReceipt().getInvoice() == null ? getString(R.string.absent) : getString(R.string.download), ParcelData.TYPE_ITEM));

            adapter.setItemList(itemList);
            adapter.notifyDataSetChanged();
        });
    }

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy", Locale.US);

    @Override
    public void onArrowTapped(final int position) {
        final ParcelData currentItem = itemList.get(position);
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
        else if (currentItem.equals(parcelDataHeading)) {
            if (parcelDataHidden) {
                itemList.addAll(position + 1, parcelDataList);
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

    private static final String TAG = ParcelDataFragment.class.toString();
}