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
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.viewmodel.HeaderViewModel;
import uz.alexits.cargostar.view.Constants;
import uz.alexits.cargostar.view.activity.CalculatorActivity;
import uz.alexits.cargostar.view.activity.CreateParcelActivity;
import uz.alexits.cargostar.view.activity.CreateUserActivity;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.activity.NotificationsActivity;
import uz.alexits.cargostar.view.activity.ProfileActivity;
import uz.alexits.cargostar.view.adapter.ParcelData;
import uz.alexits.cargostar.view.adapter.ParcelDataAdapter;
import uz.alexits.cargostar.view.callback.ParcelDataCallback;
import uz.alexits.cargostar.view.fragment.ParcelDataFragmentArgs;
import uz.alexits.cargostar.viewmodel.LocationDataViewModel;

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

            Log.i(TAG, "requestId=" + requestId + " requestOrParcel=" + requestOrParcel);
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
        headerViewModel.selectBrancheById(SharedPrefs.getInstance(context).getLong(SharedPrefs.ID)).observe(getViewLifecycleOwner(), branch -> {
            if (branch != null) {
                branchTextView.setText(getString(R.string.branch) + " \"" + branch.getName() + "\"");
            }
        });
        headerViewModel.selectNewNotificationsCount().observe(getViewLifecycleOwner(), newNotificationsCount -> {
            if (newNotificationsCount != null) {
                badgeCounterTextView.setText(String.valueOf(newNotificationsCount));
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
//                if (receiptWithCargoList.getReceipt() == null) {
//                    Toast.makeText(context, "Накладной не существует", Toast.LENGTH_SHORT).show();
//                    return;
//                }
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
            itemList.add(new ParcelData(getString(R.string.parcel_id), String.valueOf(request.getId()), ParcelData.TYPE_ITEM));
            //todo: get service provider through providerId in Provider table
            itemList.add(new ParcelData(getString(R.string.service_provider), null, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.courier_id), request.getCourierId() > 0 ? String.valueOf(request.getCourierId()) : null, ParcelData.TYPE_ITEM));
            //todo: get operatorId
            itemList.add(new ParcelData(getString(R.string.operator_id), null, ParcelData.TYPE_ITEM));
            //todo: get accountantId
            itemList.add(new ParcelData(getString(R.string.accountant_id), null, ParcelData.TYPE_ITEM));
            //todo: get senderSignature
            itemList.add(new ParcelData(getString(R.string.sender_signature), null, ParcelData.TYPE_ITEM));
            //todo: get recipientSignature
            itemList.add(new ParcelData(getString(R.string.receiver_signature), null, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(null, null, ParcelData.TYPE_STROKE));
            publicDataList.add(new ParcelData(getString(R.string.parcel_id), String.valueOf(request.getId()), ParcelData.TYPE_ITEM));
            publicDataList.add(new ParcelData(getString(R.string.service_provider), null, ParcelData.TYPE_ITEM));
            publicDataList.add(new ParcelData(getString(R.string.courier_id), request.getCourierId() > 0 ? String.valueOf(request.getCourierId()) : null, ParcelData.TYPE_ITEM));
            publicDataList.add(new ParcelData(getString(R.string.operator_id), null, ParcelData.TYPE_ITEM));
            publicDataList.add(new ParcelData(getString(R.string.accountant_id), null, ParcelData.TYPE_ITEM));
            publicDataList.add(new ParcelData(getString(R.string.sender_signature), null, ParcelData.TYPE_ITEM));
            publicDataList.add(new ParcelData(getString(R.string.receiver_signature), null, ParcelData.TYPE_ITEM));
            //sender data
            itemList.add(senderDataHeading);
            //todo: get sender login through senderId in Sender table
            itemList.add(new ParcelData(getString(R.string.login_email), null, ParcelData.TYPE_ITEM));
            //todo: get sender cargostar number through senderId in Sender table
            itemList.add(new ParcelData(getString(R.string.cargostar_account_number), null, ParcelData.TYPE_ITEM));
            //todo: get sender tnt number through senderId in Sender table
            itemList.add(new ParcelData(getString(R.string.tnt_account_number), null, ParcelData.TYPE_ITEM));
            //todo: get sender fedex number through senderId in Sender table
            itemList.add(new ParcelData(getString(R.string.fedex_account_number), null, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.take_address), request.getSenderAddress(), ParcelData.TYPE_ITEM));
            //todo get zip from AddressBook
            itemList.add(new ParcelData(getString(R.string.post_index), null, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.first_name), request.getSenderFirstName(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.middle_name), request.getSenderMiddleName(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.last_name), request.getSenderLastName(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.phone_number), request.getSenderPhone(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.email), request.getSenderEmail(), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(null, null, ParcelData.TYPE_STROKE));
            senderDataList.add(new ParcelData(getString(R.string.login_email), null, ParcelData.TYPE_ITEM));
            senderDataList.add(new ParcelData(getString(R.string.cargostar_account_number), null, ParcelData.TYPE_ITEM));
            senderDataList.add(new ParcelData(getString(R.string.tnt_account_number), null, ParcelData.TYPE_ITEM));
            senderDataList.add(new ParcelData(getString(R.string.fedex_account_number), null, ParcelData.TYPE_ITEM));
            senderDataList.add(new ParcelData(getString(R.string.take_address), request.getSenderAddress(), ParcelData.TYPE_ITEM));
            senderDataList.add(new ParcelData(getString(R.string.post_index), null, ParcelData.TYPE_ITEM));
            senderDataList.add(new ParcelData(getString(R.string.first_name), request.getSenderFirstName(), ParcelData.TYPE_ITEM));
            senderDataList.add(new ParcelData(getString(R.string.middle_name), request.getSenderMiddleName(), ParcelData.TYPE_ITEM));
            senderDataList.add(new ParcelData(getString(R.string.last_name), request.getSenderLastName(), ParcelData.TYPE_ITEM));
            senderDataList.add(new ParcelData(getString(R.string.phone_number), request.getSenderPhone(), ParcelData.TYPE_ITEM));
            senderDataList.add(new ParcelData(getString(R.string.email), request.getSenderEmail(), ParcelData.TYPE_ITEM));
            //recipient data
            itemList.add(recipientDataHeading);
            //todo: get recipient login through recipientId in Recipient table
            itemList.add(new ParcelData(getString(R.string.login_email), null, ParcelData.TYPE_ITEM));
            //todo: get recipient cargostar number through senderId in Recipient table
            itemList.add(new ParcelData(getString(R.string.cargostar_account_number), null, ParcelData.TYPE_ITEM));
            //todo: get recipient tnt number through senderId in Recipient table
            itemList.add(new ParcelData(getString(R.string.tnt_account_number), null, ParcelData.TYPE_ITEM));
            //todo: get recipient fedex number senderId in Recipient table
            itemList.add(new ParcelData(getString(R.string.fedex_account_number), null, ParcelData.TYPE_ITEM));
            //todo: get recipientAddress from AddressBook
            itemList.add(new ParcelData(getString(R.string.take_address), null, ParcelData.TYPE_ITEM));
            //todo: get recipientCountry from AddressBook
            itemList.add(new ParcelData(getString(R.string.country), null, ParcelData.TYPE_ITEM));
            //todo: get recipientRegion from AddressBook
            itemList.add(new ParcelData(getString(R.string.region), null, ParcelData.TYPE_ITEM));
            //todo: get recipientCity from AddressBook
            itemList.add(new ParcelData(getString(R.string.city), null, ParcelData.TYPE_ITEM));
            //todo: get recipientZip from AddressBook
            itemList.add(new ParcelData(getString(R.string.post_index), null, ParcelData.TYPE_ITEM));
            //todo: get recipientFirstName from AddressBook
            itemList.add(new ParcelData(getString(R.string.first_name), null, ParcelData.TYPE_ITEM));
            //todo: get recipientMiddleName from AddressBook
            itemList.add(new ParcelData(getString(R.string.middle_name), null, ParcelData.TYPE_ITEM));
            //todo: get recipientLastName from AddressBook
            itemList.add(new ParcelData(getString(R.string.last_name), null, ParcelData.TYPE_ITEM));
            //todo: get recipientPhone from AddressBook
            itemList.add(new ParcelData(getString(R.string.phone_number), null, ParcelData.TYPE_ITEM));
            //todo: get recipientEmail from AddressBook
            itemList.add(new ParcelData(getString(R.string.email), null, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(null, null, ParcelData.TYPE_STROKE));
            recipientDataList.add(new ParcelData(getString(R.string.login_email), null, ParcelData.TYPE_ITEM));
            recipientDataList.add(new ParcelData(getString(R.string.cargostar_account_number), null, ParcelData.TYPE_ITEM));
            recipientDataList.add(new ParcelData(getString(R.string.tnt_account_number), null, ParcelData.TYPE_ITEM));
            recipientDataList.add(new ParcelData(getString(R.string.fedex_account_number), null, ParcelData.TYPE_ITEM));
            recipientDataList.add(new ParcelData(getString(R.string.take_address), null, ParcelData.TYPE_ITEM));
            recipientDataList.add(new ParcelData(getString(R.string.country), null, ParcelData.TYPE_ITEM));
            recipientDataList.add(new ParcelData(getString(R.string.region), null, ParcelData.TYPE_ITEM));
            recipientDataList.add(new ParcelData(getString(R.string.city), null, ParcelData.TYPE_ITEM));
            recipientDataList.add(new ParcelData(getString(R.string.post_index), null, ParcelData.TYPE_ITEM));
            recipientDataList.add(new ParcelData(getString(R.string.first_name), null, ParcelData.TYPE_ITEM));
            recipientDataList.add(new ParcelData(getString(R.string.middle_name),  null, ParcelData.TYPE_ITEM));
            recipientDataList.add(new ParcelData(getString(R.string.last_name), null, ParcelData.TYPE_ITEM));
            recipientDataList.add(new ParcelData(getString(R.string.phone_number), null, ParcelData.TYPE_ITEM));
            recipientDataList.add(new ParcelData(getString(R.string.email), null, ParcelData.TYPE_ITEM));
            //payer data
            itemList.add(payerDataHeading);
            //todo: get payerLogin from AddressBook
            itemList.add(new ParcelData(getString(R.string.login_email), null, ParcelData.TYPE_ITEM));
            //todo: get payerCargo from AddressBook
            itemList.add(new ParcelData(getString(R.string.cargostar_account_number), null, ParcelData.TYPE_ITEM));
            //todo: get payerTnt from AddressBook
            itemList.add(new ParcelData(getString(R.string.tnt_account_number), null, ParcelData.TYPE_ITEM));
            //todo: get payerFedex from AddressBook
            itemList.add(new ParcelData(getString(R.string.fedex_account_number), null, ParcelData.TYPE_ITEM));
            //todo: get payerAddress from AddressBook
            itemList.add(new ParcelData(getString(R.string.take_address), null, ParcelData.TYPE_ITEM));
            //todo: get payerCountry from AddressBook
            itemList.add(new ParcelData(getString(R.string.country), null, ParcelData.TYPE_ITEM));
            //todo: get payerRegion from AddressBook
            itemList.add(new ParcelData(getString(R.string.region), null, ParcelData.TYPE_ITEM));
            //todo: get payerCity from AddressBook
            itemList.add(new ParcelData(getString(R.string.city), null, ParcelData.TYPE_ITEM));
            //todo: get payerZip from AddressBook
            itemList.add(new ParcelData(getString(R.string.post_index), null, ParcelData.TYPE_ITEM));
            //todo: get payerFirstName from AddressBook
            itemList.add(new ParcelData(getString(R.string.first_name), null, ParcelData.TYPE_ITEM));
            //todo: get payerMiddleName from AddressBook
            itemList.add(new ParcelData(getString(R.string.middle_name), null, ParcelData.TYPE_ITEM));
            //todo: get payerLastName from AddressBook
            itemList.add(new ParcelData(getString(R.string.last_name), null, ParcelData.TYPE_ITEM));
            //todo: get payerPhone from AddressBook
            itemList.add(new ParcelData(getString(R.string.phone_number), null, ParcelData.TYPE_ITEM));
            //todo: get payerEmail from AddressBook
            itemList.add(new ParcelData(getString(R.string.email), null, ParcelData.TYPE_ITEM));
            //todo: get discount from AddressBook
            itemList.add(new ParcelData(getString(R.string.discount), null, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(null, null, ParcelData.TYPE_STROKE));
            payerDataList.add(new ParcelData(getString(R.string.login_email), null, ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.cargostar_account_number), null, ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.tnt_account_number), null, ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.fedex_account_number), null, ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.take_address), null, ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.country), null, ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.region), null, ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.city), null, ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.post_index), null, ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.first_name), null, ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.middle_name), null, ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.last_name), null, ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.phone_number), null, ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.email), null, ParcelData.TYPE_ITEM));
            payerDataList.add(new ParcelData(getString(R.string.discount), null, ParcelData.TYPE_ITEM));
            //accounts data
            itemList.add(accountDataHeading);
            //todo: get checkingAccount from AddressBook
            itemList.add(new ParcelData(getString(R.string.checking_account), null, ParcelData.TYPE_ITEM));
            //todo: get bank from AddressBook
            itemList.add(new ParcelData(getString(R.string.bank), null, ParcelData.TYPE_ITEM));
            //todo: get registrationCode from AddressBook
            itemList.add(new ParcelData(getString(R.string.payer_registration_code), null, ParcelData.TYPE_ITEM));
            //todo: get mfo from AddressBook
            itemList.add(new ParcelData(getString(R.string.mfo), null, ParcelData.TYPE_ITEM));
            //todo: get oked from AddressBook
            itemList.add(new ParcelData(getString(R.string.oked), null, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(null, null, ParcelData.TYPE_STROKE));
            accountDataList.add(new ParcelData(getString(R.string.checking_account), null, ParcelData.TYPE_ITEM));
            accountDataList.add(new ParcelData(getString(R.string.bank), null, ParcelData.TYPE_ITEM));
            accountDataList.add(new ParcelData(getString(R.string.payer_registration_code), null, ParcelData.TYPE_ITEM));
            accountDataList.add(new ParcelData(getString(R.string.mfo), null, ParcelData.TYPE_ITEM));
            accountDataList.add(new ParcelData(getString(R.string.oked), null, ParcelData.TYPE_ITEM));
            //parcel data
            itemList.add(parcelDataHeading);
            //todo: get trackingCode from Parcel
            itemList.add(new ParcelData(getString(R.string.tracking_code_main), null, ParcelData.TYPE_ITEM));
            //todo: get qr from Parcel
            itemList.add(new ParcelData(getString(R.string.qr_code), null, ParcelData.TYPE_ITEM));
            //todo: get instructions from Parcel
            itemList.add(new ParcelData(getString(R.string.courier_guidelines), null, ParcelData.TYPE_ITEM));
            //todo: get tariff from Provider
            itemList.add(new ParcelData(getString(R.string.tariff), null, ParcelData.TYPE_ITEM));
            //todo: get destinationQuantity from size of CargoList
            itemList.add(new ParcelData(getString(R.string.destination_quantity), null, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(null, null, ParcelData.TYPE_STROKE));
            parcelDataList.add(new ParcelData(getString(R.string.tracking_code_main), null, ParcelData.TYPE_ITEM));
            parcelDataList.add(new ParcelData(getString(R.string.qr_code), null, ParcelData.TYPE_ITEM));
            parcelDataList.add(new ParcelData(getString(R.string.courier_guidelines), null, ParcelData.TYPE_ITEM));
            parcelDataList.add(new ParcelData(getString(R.string.tariff), null, ParcelData.TYPE_ITEM));
            parcelDataList.add(new ParcelData(getString(R.string.destination_quantity), null, ParcelData.TYPE_ITEM));
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
            itemList.add(new ParcelData(getString(R.string.cost), null, ParcelData.TYPE_ITEM));
            //todo: get fuel from Provider
            itemList.add(new ParcelData(getString(R.string.fuel_tax), null, ParcelData.TYPE_ITEM));
            //todo: get vat from ZoneSettings
            itemList.add(new ParcelData(getString(R.string.NDC), null, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(null, null, ParcelData.TYPE_STROKE));
            paymentDataList.add(new ParcelData(getString(R.string.cost), null, ParcelData.TYPE_ITEM));
            paymentDataList.add(new ParcelData(getString(R.string.fuel_tax), null, ParcelData.TYPE_ITEM));
            paymentDataList.add(new ParcelData(getString(R.string.NDC), null, ParcelData.TYPE_ITEM));
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
            itemList.add(new ParcelData(getString(R.string.dispatch_date), dispatchDate, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(getString(R.string.arrival_date), arrivalDate, ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(null, null, ParcelData.TYPE_STROKE));
            logisticsDataList.add(new ParcelData(getString(R.string.dispatch_date), dispatchDate, ParcelData.TYPE_ITEM));
            logisticsDataList.add(new ParcelData(getString(R.string.arrival_date), arrivalDate, ParcelData.TYPE_ITEM));
            //documents data
            itemList.add(documentsDataHeading);
            //todo: requestDoc from ???
            itemList.add(new ParcelData(getString(R.string.e_bid), getString(R.string.absent), ParcelData.TYPE_ITEM));
            //todo: receiptDoc from ???
            itemList.add(new ParcelData(getString(R.string.e_waybill), getString(R.string.absent), ParcelData.TYPE_ITEM));
            //todo: guaranteeLetterDoc from ???
            itemList.add(new ParcelData(getString(R.string.payer_guarantee_letter), getString(R.string.absent), ParcelData.TYPE_ITEM));
            //todo: paybillDoc from ???
            itemList.add(new ParcelData(getString(R.string.pay_bill), getString(R.string.absent), ParcelData.TYPE_ITEM));
            //todo: invoiceDoc from ???
            itemList.add(new ParcelData(getString(R.string.invoice_document), getString(R.string.absent), ParcelData.TYPE_ITEM));
            //todo: invoiceDoc from ???
            itemList.add(new ParcelData(getString(R.string.invoice), getString(R.string.absent), ParcelData.TYPE_ITEM));
            itemList.add(new ParcelData(null, null, ParcelData.TYPE_STROKE));
            documentsDataList.add(new ParcelData(getString(R.string.e_bid), getString(R.string.absent), ParcelData.TYPE_ITEM));
            documentsDataList.add(new ParcelData(getString(R.string.e_waybill), getString(R.string.absent), ParcelData.TYPE_ITEM));
            documentsDataList.add(new ParcelData(getString(R.string.payer_guarantee_letter), getString(R.string.absent), ParcelData.TYPE_ITEM));
            documentsDataList.add(new ParcelData(getString(R.string.pay_bill), getString(R.string.absent), ParcelData.TYPE_ITEM));
            documentsDataList.add(new ParcelData(getString(R.string.invoice_document), getString(R.string.absent), ParcelData.TYPE_ITEM));
            documentsDataList.add(new ParcelData(getString(R.string.invoice), getString(R.string.absent), ParcelData.TYPE_ITEM));

            adapter.setItemList(itemList);
            adapter.notifyDataSetChanged();
        });

        /* init Location Data */
        final LocationDataViewModel locationDataViewModel = new ViewModelProvider(this).get(LocationDataViewModel.class);

//        locationDataViewModel.getCountryById(countryId).observe(getViewLifecycleOwner(), country -> {
//            initCountryData(country);
//        });
//
//        locationDataViewModel.getRegionById(regionId).observe(getViewLifecycleOwner(), region ->  {
//            initRegionData(region);
//        });
//
//        locationDataViewModel.getCityById(cityId).observe(getViewLifecycleOwner(), city -> {
//            initCityData(city);
//        });
    }

    private void initCountryData(final Country country) {
        //get country through senderCountryId in Country table
        itemList.add(new ParcelData(getString(R.string.country), country.getName(), ParcelData.TYPE_ITEM));
        senderDataList.add(new ParcelData(getString(R.string.country), country.getName(), ParcelData.TYPE_ITEM));
    }

    private void initRegionData(final Region region) {
        //get region through senderRegionId in Region table
        itemList.add(new ParcelData(getString(R.string.region), region.getName(), ParcelData.TYPE_ITEM));
        senderDataList.add(new ParcelData(getString(R.string.region), region.getName(), ParcelData.TYPE_ITEM));
    }

    private void initCityData(final City city) {
        //get city through senderCityId in City table
        itemList.add(new ParcelData(getString(R.string.city), city.getName(), ParcelData.TYPE_ITEM));
        senderDataList.add(new ParcelData(getString(R.string.city), city.getName(), ParcelData.TYPE_ITEM));
    }

    private void initRequestData() {

    }

    private void initAddressBookData() {

    }

    private void initClientData() {

    }

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

    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
    private static final String TAG = ParcelDataFragment.class.toString();
}