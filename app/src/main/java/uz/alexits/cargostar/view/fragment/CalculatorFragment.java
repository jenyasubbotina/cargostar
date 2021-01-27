package uz.alexits.cargostar.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import uz.alexits.cargostar.R;

import uz.alexits.cargostar.database.cache.SharedPrefs;
import uz.alexits.cargostar.model.TariffPrice;
import uz.alexits.cargostar.model.calculation.Packaging;
import uz.alexits.cargostar.model.calculation.Provider;
import uz.alexits.cargostar.model.calculation.Vat;
import uz.alexits.cargostar.model.calculation.Zone;
import uz.alexits.cargostar.model.calculation.ZoneSettings;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.calculation.PackagingType;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.model.transportation.Consignment;
import uz.alexits.cargostar.utils.Constants;
import uz.alexits.cargostar.utils.Regex;
import uz.alexits.cargostar.view.adapter.CalculatorAdapter;
import uz.alexits.cargostar.view.adapter.TariffPriceAdapter;
import uz.alexits.cargostar.viewmodel.CourierViewModel;
import uz.alexits.cargostar.viewmodel.CalculatorViewModel;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.view.UiUtils;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.adapter.ConsignmentAdapter;
import uz.alexits.cargostar.view.callback.CreateInvoiceCallback;
import uz.alexits.cargostar.viewmodel.LocationDataViewModel;
import uz.alexits.cargostar.workers.SyncWorkRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CalculatorFragment extends Fragment implements CreateInvoiceCallback {
    private Context context;
    private FragmentActivity activity;
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

    /* country spinner items */
    private ArrayAdapter<Country> countryArrayAdapter;
    private Spinner srcCountrySpinner;
    private Spinner destCountrySpinner;
    private RelativeLayout srcCountryField;
    private RelativeLayout destCountryField;

    /* city spinner items */
    private ArrayAdapter<City> srcCityArrayAdapter;
    private ArrayAdapter<City> destCityArrayAdapter;
    private RelativeLayout srcCityField;
    private RelativeLayout destCityField;
    private Spinner srcCitySpinner;
    private Spinner destCitySpinner;

    /* courier view model */
    private CourierViewModel courierViewModel;
    /* location view model */
    private LocationDataViewModel locationDataViewModel;
    /* packaging view model */
    private CalculatorViewModel calculatorViewModel;

    /* packagingType items */
    private ArrayAdapter<PackagingType> packagingTypeArrayAdapter;
    private Spinner packagingTypeSpinner;
    private RelativeLayout packagingTypeField;

    /* packageType items */
    private RadioGroup packageTypeRadioGroup;
    private RadioButton docTypeRadioBtn;
    private RadioButton boxTypeRadioBtn;

    /* provider */
    private CardView firstCard;
    private CardView secondCard;
    private RadioButton firstCardRadioBtn;
    private RadioButton secondCardRadioBtn;
    private ImageView firstCardImageView;
    private ImageView secondCardImageView;

    /* cargo list */
    private EditText weightEditText;
    private EditText lengthEditText;
    private EditText widthEditText;
    private EditText heightEditText;
    private Button addBtn;

    /* calculations */
    private TextView totalQuantityTextView;
    private TextView totalWeightTextView;
    private TextView totalDimensionsTextView;
    private RecyclerView tariffPriceRecyclerView;
    private TariffPriceAdapter tariffPriceAdapter;
    private Button calculateBtn;

    /* show current cargoList */
    private CalculatorAdapter calculatorAdapter;
    private RecyclerView itemRecyclerView;

    /* selected items */
    private Long selectedCountryId = null;
    private Provider selectedProvider = null;
    private List<Packaging> tariffList = null;
    private List<Long> selectedPackagingIdList = new ArrayList<>();
    private List<ZoneSettings> selectedZoneSettingsList = null;
    private Vat selectedVat = null;

    private static final List<Consignment> consignmentList = new ArrayList<>();
    private static final List<TariffPrice> tariffPriceList = new ArrayList<>();

    public CalculatorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();
        activity = getActivity();

        consignmentList.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_calculator, container, false);
        //header views
        fullNameTextView = activity.findViewById(R.id.full_name_text_view);
        branchTextView = activity.findViewById(R.id.branch_text_view);
        courierIdTextView = activity.findViewById(R.id.courier_id_text_view);
        requestSearchEditText = activity.findViewById(R.id.search_edit_text);
        requestSearchImageView = activity.findViewById(R.id.search_btn);
        profileImageView = activity.findViewById(R.id.profile_image_view);
        editImageView = activity.findViewById(R.id.edit_image_view);
        createUserImageView = activity.findViewById(R.id.create_user_image_view);
        notificationsImageView = activity.findViewById(R.id.notifications_image_view);
        badgeCounterTextView = activity.findViewById(R.id.badge_counter_text_view);
        calculatorImageView = activity.findViewById(R.id.calculator_image_view);

        //main content views
        srcCountryField = root.findViewById(R.id.source_country_field);
        destCountryField = root.findViewById(R.id.destination_country_field);

        srcCityField = root.findViewById(R.id.source_city_field);
        destCityField = root.findViewById(R.id.destination_city_field);

        srcCountrySpinner = root.findViewById(R.id.source_country_spinner);
        destCountrySpinner = root.findViewById(R.id.destination_country_spinner);

        srcCitySpinner = root.findViewById(R.id.source_city_spinner);
        destCitySpinner = root.findViewById(R.id.destination_city_spinner);

        packagingTypeSpinner = root.findViewById(R.id.package_type_spinner);
        packagingTypeField = root.findViewById(R.id.package_type_field);

        weightEditText = root.findViewById(R.id.weight_edit_text);
        lengthEditText = root.findViewById(R.id.length_edit_text);
        widthEditText = root.findViewById(R.id.width_edit_text);
        heightEditText = root.findViewById(R.id.height_edit_text);
        addBtn = root.findViewById(R.id.add_item_btn);

        totalQuantityTextView = root.findViewById(R.id.total_quantity_value_text_view);
        totalWeightTextView = root.findViewById(R.id.total_weight_value_text_view);
        totalDimensionsTextView = root.findViewById(R.id.total_dimensions_value_text_view);
        calculateBtn = root.findViewById(R.id.calculate_btn);

        packageTypeRadioGroup = root.findViewById(R.id.package_type_radio_group);
        docTypeRadioBtn = root.findViewById(R.id.doc_type_radio_btn);
        boxTypeRadioBtn = root.findViewById(R.id.box_type_radio_btn);

        countryArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new ArrayList<>());
        countryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        srcCountrySpinner.setAdapter(countryArrayAdapter);
        destCountrySpinner.setAdapter(countryArrayAdapter);

        srcCityArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new ArrayList<>());
        destCityArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new ArrayList<>());
        srcCityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destCityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        srcCitySpinner.setAdapter(srcCityArrayAdapter);
        destCitySpinner.setAdapter(destCityArrayAdapter);

        packagingTypeArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, new ArrayList<>());
        packagingTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        packagingTypeSpinner.setAdapter(packagingTypeArrayAdapter);

        firstCard = root.findViewById(R.id.first_card);
        secondCard = root.findViewById(R.id.second_card);
        firstCardImageView = root.findViewById(R.id.first_card_logo);
        secondCardImageView = root.findViewById(R.id.second_card_logo);
        firstCardRadioBtn = root.findViewById(R.id.first_card_radio_btn);
        secondCardRadioBtn = root.findViewById(R.id.second_card_radio_btn);

        final LinearLayoutManager itemLayoutManager = new LinearLayoutManager(context);
        itemLayoutManager.setOrientation(RecyclerView.VERTICAL);
        final LinearLayoutManager tariffPriceLayoutManager = new LinearLayoutManager(context);
        tariffPriceLayoutManager.setOrientation(RecyclerView.VERTICAL);

        itemRecyclerView = root.findViewById(R.id.calculationsRecyclerView);
        calculatorAdapter = new CalculatorAdapter(context, consignmentList, this);
        itemRecyclerView.setLayoutManager(itemLayoutManager);
        itemRecyclerView.setAdapter(calculatorAdapter);

        tariffPriceRecyclerView = root.findViewById(R.id.tariff_price_recycler_view);
        tariffPriceAdapter = new TariffPriceAdapter(context);
        tariffPriceRecyclerView.setLayoutManager(tariffPriceLayoutManager);
        tariffPriceRecyclerView.setAdapter(tariffPriceAdapter);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //header views
        editImageView.setOnClickListener(v -> {
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(R.id.profileFragment);
        });

        createUserImageView.setOnClickListener(v -> {
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(R.id.createUserFragment);
        });

        notificationsImageView.setOnClickListener(v -> {
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(R.id.notificationsFragment);
        });

        profileImageView.setOnClickListener(v -> {
            UiUtils.getNavController(activity, R.id.main_fragment_container).navigate(R.id.mainFragment);
        });

        calculatorImageView.setOnClickListener(null);

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

        /* spinner items */
        srcCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView itemTextView = (TextView) view;
                final Country srcCountry = (Country) adapterView.getSelectedItem();

                calculatorViewModel.setSrcCountryId(srcCountry.getId());

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        srcCountryField.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }

                final Country destCountry = (Country) destCountrySpinner.getSelectedItem();

                if (!TextUtils.isEmpty(srcCountry.getNameEn()) && srcCountry.getNameEn().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                    //uzbekistan -> uzbekistan = cargo only
                    if (!TextUtils.isEmpty(destCountry.getNameEn()) && destCountry.getNameEn().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                        selectedCountryId = srcCountry.getId();
                        calculatorViewModel.setCountryIdProviderId(selectedCountryId, selectedProvider != null ? selectedProvider.getId() : null);

                        firstCardRadioBtn.setChecked(false);

                        firstCardImageView.setImageResource(R.drawable.logo_cargo_calc);
                        firstCardRadioBtn.setText(R.string.cargostar);
                        secondCardRadioBtn.setText(null);

                        firstCardRadioBtn.setVisibility(View.VISIBLE);
                        firstCardImageView.setVisibility(View.VISIBLE);
                        firstCard.setVisibility(View.VISIBLE);

                        secondCardRadioBtn.setVisibility(View.INVISIBLE);
                        secondCardImageView.setVisibility(View.INVISIBLE);
                        secondCard.setVisibility(View.INVISIBLE);

                        return;
                    }
                    //uzbekistan -> other = fedex & tnt (export)
                    selectedCountryId = destCountry.getId();
                    calculatorViewModel.setCountryIdProviderId(selectedCountryId, selectedProvider != null ? selectedProvider.getId() : null);

                    firstCardRadioBtn.setChecked(false);
                    secondCardRadioBtn.setChecked(false);

                    firstCardRadioBtn.setText(R.string.tnt);
                    secondCardRadioBtn.setText(R.string.fedex);

                    firstCardImageView.setImageResource(R.drawable.logo_tnt_cacl);
                    firstCardRadioBtn.setVisibility(View.VISIBLE);
                    firstCardImageView.setVisibility(View.VISIBLE);
                    firstCard.setVisibility(View.VISIBLE);

                    secondCardImageView.setImageResource(R.drawable.logo_fedex_calc);
                    secondCardRadioBtn.setVisibility(View.VISIBLE);
                    secondCardImageView.setVisibility(View.VISIBLE);
                    secondCard.setVisibility(View.VISIBLE);
                    return;
                }
                //other -> uzbekistan = tnt only
                if (!TextUtils.isEmpty(destCountry.getNameEn()) && destCountry.getNameEn().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                    selectedCountryId = srcCountry.getId();
                    calculatorViewModel.setCountryIdProviderId(selectedCountryId, selectedProvider != null ? selectedProvider.getId() : null);

                    firstCardRadioBtn.setChecked(false);
                    firstCardImageView.setImageResource(R.drawable.logo_tnt_cacl);

                    firstCardRadioBtn.setText(R.string.tnt);
                    secondCardRadioBtn.setText(null);

                    firstCardRadioBtn.setVisibility(View.VISIBLE);
                    firstCardImageView.setVisibility(View.VISIBLE);
                    firstCard.setVisibility(View.VISIBLE);

                    secondCardRadioBtn.setVisibility(View.INVISIBLE);
                    secondCardImageView.setVisibility(View.INVISIBLE);
                    secondCard.setVisibility(View.INVISIBLE);
                    return;
                }
                selectedCountryId = null;
                calculatorViewModel.setCountryIdProviderId(selectedCountryId, selectedProvider != null ? selectedProvider.getId() : null);

                firstCardRadioBtn.setChecked(false);
                secondCardRadioBtn.setChecked(false);

                firstCardRadioBtn.setText(null);
                secondCardRadioBtn.setText(null);

                firstCardRadioBtn.setVisibility(View.INVISIBLE);
                firstCardImageView.setVisibility(View.INVISIBLE);
                firstCard.setVisibility(View.INVISIBLE);

                secondCardRadioBtn.setVisibility(View.INVISIBLE);
                secondCardImageView.setVisibility(View.INVISIBLE);
                secondCard.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        destCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView itemTextView = (TextView) view;
                final Country destCountry = (Country) adapterView.getSelectedItem();

                calculatorViewModel.setDestCountryId(destCountry.getId());

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        destCountryField.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }

                final Country srcCountry = (Country) srcCountrySpinner.getSelectedItem();

                if (!TextUtils.isEmpty(srcCountry.getNameEn()) && srcCountry.getNameEn().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                    //uzbekistan -> uzbekistan = cargo only
                    if (!TextUtils.isEmpty(destCountry.getNameEn()) && destCountry.getNameEn().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                        selectedCountryId = destCountry.getId();
                        calculatorViewModel.setCountryIdProviderId(selectedCountryId, selectedProvider != null ? selectedProvider.getId() : null);

                        firstCardRadioBtn.setChecked(false);

                        firstCardImageView.setImageResource(R.drawable.logo_cargo_calc);

                        firstCardRadioBtn.setText(R.string.cargostar);
                        secondCardRadioBtn.setText(null);

                        firstCardRadioBtn.setVisibility(View.VISIBLE);
                        firstCardImageView.setVisibility(View.VISIBLE);
                        firstCard.setVisibility(View.VISIBLE);

                        secondCardRadioBtn.setVisibility(View.INVISIBLE);
                        secondCardImageView.setVisibility(View.INVISIBLE);
                        secondCard.setVisibility(View.INVISIBLE);
                        return;
                    }
                    //uzbekistan -> other = fedex & tnt (export)
                    selectedCountryId = destCountry.getId();
                    calculatorViewModel.setCountryIdProviderId(selectedCountryId, selectedProvider != null ? selectedProvider.getId() : null);

                    firstCardImageView.setImageResource(R.drawable.logo_tnt_cacl);
                    firstCardRadioBtn.setChecked(false);
                    secondCardRadioBtn.setChecked(false);

                    firstCardRadioBtn.setText(R.string.tnt);
                    secondCardRadioBtn.setText(R.string.fedex);

                    firstCardRadioBtn.setVisibility(View.VISIBLE);
                    firstCard.setVisibility(View.VISIBLE);
                    firstCardImageView.setVisibility(View.VISIBLE);

                    secondCardImageView.setImageResource(R.drawable.logo_fedex_calc);
                    secondCardRadioBtn.setVisibility(View.VISIBLE);
                    secondCardImageView.setVisibility(View.VISIBLE);
                    secondCard.setVisibility(View.VISIBLE);

                    return;
                }
                //other -> uzbekistan = tnt only
                if (!TextUtils.isEmpty(destCountry.getNameEn()) && destCountry.getNameEn().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                    selectedCountryId = srcCountry.getId();
                    calculatorViewModel.setCountryIdProviderId(selectedCountryId, selectedProvider != null ? selectedProvider.getId() : null);

                    firstCardRadioBtn.setChecked(false);

                    firstCardImageView.setImageResource(R.drawable.logo_tnt_cacl);

                    firstCardRadioBtn.setText(R.string.tnt);
                    secondCardRadioBtn.setText(null);

                    firstCardRadioBtn.setVisibility(View.VISIBLE);
                    firstCardImageView.setVisibility(View.VISIBLE);
                    firstCard.setVisibility(View.VISIBLE);

                    secondCardRadioBtn.setVisibility(View.INVISIBLE);
                    secondCardImageView.setVisibility(View.INVISIBLE);
                    secondCard.setVisibility(View.INVISIBLE);
                    return;
                }
                selectedCountryId = null;
                calculatorViewModel.setCountryIdProviderId(selectedCountryId, selectedProvider != null ? selectedProvider.getId() : null);

                firstCardRadioBtn.setChecked(false);
                secondCardRadioBtn.setChecked(false);

                firstCardRadioBtn.setText(null);
                secondCardRadioBtn.setText(null);

                firstCardRadioBtn.setVisibility(View.INVISIBLE);
                firstCardImageView.setVisibility(View.INVISIBLE);
                firstCard.setVisibility(View.INVISIBLE);

                secondCardRadioBtn.setVisibility(View.INVISIBLE);
                secondCardImageView.setVisibility(View.INVISIBLE);
                secondCard.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        srcCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView itemTextView = (TextView) view;

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        srcCityField.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        destCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView itemTextView = (TextView) view;

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        destCityField.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        /* providers */
        firstCard.setOnClickListener(v -> {
            firstCardRadioBtn.setChecked(true);
        });

        secondCard.setOnClickListener(v -> {
            secondCardRadioBtn.setChecked(true);
        });

        /* choose provider */
        firstCardRadioBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                secondCardRadioBtn.setChecked(false);

                if (firstCardRadioBtn.getText().equals(getString(R.string.cargostar))) {
                    calculatorViewModel.setProviderId(6L);
                    calculatorViewModel.setCountryIdProviderId(selectedCountryId, 6L);
                    return;
                }
                if (firstCardRadioBtn.getText().equals(getString(R.string.tnt))) {
                    calculatorViewModel.setProviderId(5L);
                    calculatorViewModel.setCountryIdProviderId(selectedCountryId, 5L);
                }
            }
        });

        secondCardRadioBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                firstCardRadioBtn.setChecked(false);
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        courierViewModel = new ViewModelProvider(this).get(CourierViewModel.class);
        locationDataViewModel = new ViewModelProvider(this).get(LocationDataViewModel.class);
        calculatorViewModel = new ViewModelProvider(this).get(CalculatorViewModel.class);

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

                        final long requestId = outputData.getLong(Constants.KEY_REQUEST_ID, -1L);
                        final long invoiceId = outputData.getLong(Constants.KEY_INVOICE_ID, -1L);
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

        /* countries */
        locationDataViewModel.getCountryList().observe(getViewLifecycleOwner(), countryList -> {
            if (countryList != null) {
                countryArrayAdapter.clear();
                countryArrayAdapter.addAll(countryList);

                for (int i = 0; i < countryList.size(); i++) {
                    if (countryList.get(i).getNameEn().equalsIgnoreCase(getString(R.string.uzbekistan))) {
                        int finalI = i;
                        srcCountrySpinner.post(() -> srcCountrySpinner.setSelection(finalI, false));
                        int finalI1 = i;
                        destCountrySpinner.post(() -> destCountrySpinner.setSelection(finalI1, false));
                        return;
                    }
                }
            }
        });

        /* cities */
        calculatorViewModel.getSrcCities().observe(getViewLifecycleOwner(), srcCityList -> {
            if (srcCityList != null) {
                srcCityArrayAdapter.clear();
                srcCityArrayAdapter.addAll(srcCityList);

                for (int i = 0; i < srcCityList.size(); i++) {
                    if (srcCityList.get(i).getNameEn().equalsIgnoreCase(getString(R.string.tashkent))) {
                        int finalI = i;
                        srcCitySpinner.post(() -> srcCitySpinner.setSelection(finalI, false));
                        return;
                    }
                }
            }
        });

        calculatorViewModel.getDestCities().observe(getViewLifecycleOwner(), destCityList -> {
            if (destCityList != null) {
                destCityArrayAdapter.clear();
                destCityArrayAdapter.addAll(destCityList);

                for (int i = 0; i < destCityList.size(); i++) {
                    if (destCityList.get(i).getNameEn().equalsIgnoreCase(getString(R.string.samarkand))) {
                        int finalI = i;
                        destCitySpinner.post(() -> destCitySpinner.setSelection(finalI, false));
                        return;
                    }
                }
            }
        });

        /* calculation */
        calculatorViewModel.getProvider().observe(getViewLifecycleOwner(), provider -> {
            selectedProvider = provider;
        });

        calculatorViewModel.getType().observe(getViewLifecycleOwner(), type -> {
            if (type != null && !selectedPackagingIdList.isEmpty()) {
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
            packagingTypeArrayAdapter.notifyDataSetChanged();
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
    public void afterFirstEditTextChanged(final int position, final CharSequence editable) {
        //do nothing
    }

    @Override
    public void afterSecondEditTextChanged(final int position, final CharSequence editable) {
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
    public void onBigSpinnerItemSelected(final int position, final Object entry) {
        //do nothing
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
    public void onScanItemClicked(int position) {
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

    private void addCargoToInvoice() {
        final PackagingType packagingType = (PackagingType) packagingTypeSpinner.getSelectedItem();
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
        consignmentList.add(new Consignment(
                -1,
                -1L,
                packagingType.getId(),
                packagingType.getName(),
                null,
                null,
                null,
                !TextUtils.isEmpty(length) ? Double.parseDouble(length) : 0,
                !TextUtils.isEmpty(width) ? Double.parseDouble(width) : 0,
                !TextUtils.isEmpty(height) ? Double.parseDouble(height) : 0,
                !TextUtils.isEmpty(weight) ? Double.parseDouble(weight) : 0,
                dimensions,
                null));
        calculatorAdapter.notifyItemInserted(consignmentList.size() - 1);
    }

    private void calculateTotalPrice() {
        if (srcCountrySpinner.getSelectedItem() == null) {
            Toast.makeText(context, "Укажите страну отправки", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destCountrySpinner.getSelectedItem() == null) {
            Toast.makeText(context, "Укажите страну прибытия", Toast.LENGTH_SHORT).show();
            return;
        }
        if (packagingTypeSpinner.getSelectedItem() == null) {
            Toast.makeText(context, "Выберите упаковку", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!firstCardRadioBtn.isChecked() && !secondCardRadioBtn.isChecked()) {
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
            if (correspondingTariff != null) {
                tariffPriceList.add(new TariffPrice(correspondingTariff.getName(), String.valueOf((int) totalPrice + 1), correspondingTariff.getId()));
            }
        }
        totalQuantityTextView.setText(String.valueOf(totalQuantity));
        totalWeightTextView.setText(String.valueOf(new BigDecimal(Double.toString(totalWeight)).setScale(2, RoundingMode.HALF_UP).doubleValue()));
        totalDimensionsTextView.setText(String.valueOf(new BigDecimal(Double.toString(totalVolume)).setScale(2, RoundingMode.HALF_UP).doubleValue()));

        tariffPriceAdapter.setItemList(tariffPriceList);
    }

    private static final String TAG = CalculatorFragment.class.toString();
}