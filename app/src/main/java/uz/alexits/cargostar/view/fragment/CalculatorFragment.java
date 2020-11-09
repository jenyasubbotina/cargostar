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

import android.text.Editable;
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
import uz.alexits.cargostar.model.calculation.Packaging;
import uz.alexits.cargostar.model.calculation.Provider;
import uz.alexits.cargostar.model.calculation.Zone;
import uz.alexits.cargostar.model.calculation.ZoneSettings;
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.calculation.PackagingType;
import uz.alexits.cargostar.model.location.Region;
import uz.alexits.cargostar.model.shipping.Cargo;
import uz.alexits.cargostar.model.shipping.Consignment;
import uz.alexits.cargostar.utils.Regex;
import uz.alexits.cargostar.viewmodel.CourierViewModel;
import uz.alexits.cargostar.viewmodel.CalculatorViewModel;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.utils.UiUtils;
import uz.alexits.cargostar.view.activity.CreateUserActivity;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.activity.NotificationsActivity;
import uz.alexits.cargostar.view.activity.ProfileActivity;
import uz.alexits.cargostar.view.adapter.CalculatorAdapter;
import uz.alexits.cargostar.view.callback.CreateInvoiceCallback;
import uz.alexits.cargostar.viewmodel.LocationDataViewModel;
import uz.alexits.cargostar.workers.SyncWorkRequest;

import java.util.ArrayList;
import java.util.List;

public class CalculatorFragment extends Fragment implements CreateInvoiceCallback {
    private Context context;
    private FragmentActivity activity;
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
    private TextView expressCost;
    private TextView economyExpressCost;
    private Button calculateBtn;

    /* show current cargoList */
    private CalculatorAdapter calculatorAdapter;
    private RecyclerView itemRecyclerView;

    /* selected items */
    private Long selectedCountryId = null;
    private Provider selectedProvider = null;
    private Packaging selectedPackaging = null;
    private List<Long> selectedPackagingIdList = null;
    private List<ZoneSettings> selectedZoneSettingsList = null;

    private static final List<Cargo> itemList = new ArrayList<>();

    public CalculatorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        activity = getActivity();

        SyncWorkRequest.fetchPackagingData(getContext(), 100000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_calculator, container, false);
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

        expressCost = root.findViewById(R.id.express_sum_text_view);
        economyExpressCost = root.findViewById(R.id.economy_express_sum_text_view);

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

        itemRecyclerView = root.findViewById(R.id.calculationsRecyclerView);
        calculatorAdapter = new CalculatorAdapter(context, itemList, this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        itemRecyclerView.setLayoutManager(layoutManager);
        itemRecyclerView.setAdapter(calculatorAdapter);

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

                //country & null = hide all
                if (destCountrySpinner.getSelectedItem() == null) {
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

                    return;
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

                //country & null = hide all
                if (srcCitySpinner.getSelectedItem() == null) {
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
                    return;
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
                return;
            }
            if (!secondCardRadioBtn.isChecked()) {
                selectedProvider = null;
                calculatorViewModel.setProviderId(null);
                calculatorViewModel.setCountryIdProviderId(selectedCountryId, null);
            }
        });

        secondCardRadioBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                firstCardRadioBtn.setChecked(false);
                //only fedex case
                calculatorViewModel.setProviderId(4L);
                calculatorViewModel.setCountryIdProviderId(selectedCountryId, 4L);
                return;
            }
            if (!firstCardRadioBtn.isChecked()) {
                selectedProvider = null;
                calculatorViewModel.setProviderId(null);
                calculatorViewModel.setCountryIdProviderId(selectedCountryId, null);
            }
        });

        /* choose packaging type (1 / 2) */
        packageTypeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == docTypeRadioBtn.getId()) {
                //docs
                calculatorViewModel.setType(1);
            }
            else if (checkedId == boxTypeRadioBtn.getId()) {
                //boxes
                calculatorViewModel.setType(2);
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

                calculatorViewModel.setPackagingId(selectedPackagingType.getPackagingId());

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
        //header view model
        final CourierViewModel courierViewModel = new ViewModelProvider(this).get(CourierViewModel.class);

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

        parcelSearchImageView.setOnClickListener(v -> {
            final String parcelIdStr = parcelSearchEditText.getText().toString();
            if (TextUtils.isEmpty(parcelIdStr)) {
                Toast.makeText(context, "Введите ID перевозки или номер накладной", Toast.LENGTH_SHORT).show();
                return;
            }
        });

        locationDataViewModel = new ViewModelProvider(this).get(LocationDataViewModel.class);
        calculatorViewModel = new ViewModelProvider(this).get(CalculatorViewModel.class);

        /* countries */
        locationDataViewModel.getCountryList().observe(getViewLifecycleOwner(), countryList -> {
            countryArrayAdapter.clear();
            countryArrayAdapter.addAll(countryList);
            countryArrayAdapter.notifyDataSetChanged();
        });

        /* cities */
        calculatorViewModel.getSrcCities().observe(getViewLifecycleOwner(), srcCityList -> {
            srcCityArrayAdapter.clear();
            srcCityArrayAdapter.addAll(srcCityList);
            srcCityArrayAdapter.notifyDataSetChanged();
        });

        calculatorViewModel.getDestCities().observe(getViewLifecycleOwner(), destCityList -> {
            destCityArrayAdapter.clear();
            destCityArrayAdapter.addAll(destCityList);
            destCityArrayAdapter.notifyDataSetChanged();
        });

        /* calculation */
        calculatorViewModel.getProvider().observe(getViewLifecycleOwner(), provider -> {
            selectedProvider = provider;
        });

        calculatorViewModel.getType().observe(getViewLifecycleOwner(), type -> {
            calculatorViewModel.setTypePackageIdList(type, selectedPackagingIdList);
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
            for (final PackagingType packagingType : packagingTypeList) {
                packagingTypeArrayAdapter.add(packagingType);
            }
            packagingTypeArrayAdapter.notifyDataSetChanged();
        });

        calculatorViewModel.getPackaging().observe(getViewLifecycleOwner(), packaging -> {
            selectedPackaging = packaging;
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
            Log.i(TAG, "zoneSettingsList: " + zoneSettingsList);
            selectedZoneSettingsList = zoneSettingsList;
        });
    }

    @Override
    public void onAddBtnClicked() {
        //do nothing
    }

    @Override
    public void onCameraImageClicked(int position) {
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
    public void afterFirstEditTextChanged(int position, Editable editable) {
        //do nothing
    }

    @Override
    public void afterSecondEditTextChanged(int position, Editable editable) {
        //do nothing
    }

    @Override
    public void onSpinnerItemChanged(AdapterView<?> adapterView, View view, int i, long l) {
        //do nothing
    }

    @Override
    public void onSpinnerEditTextItemSelected(int position, Object selectedObject) {
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
    public void onDeleteItemClicked(final int position) {
        if (itemList.size() <= 0) {
            return;
        }
        Log.i(TAG, "position=" + position + " size" + itemList.size());
        itemList.remove(position);
        calculatorAdapter.notifyDataSetChanged();

        Log.i(TAG, "itemCount=" + calculatorAdapter.getItemCount());
    }

    private void addCargoToInvoice() {
        final PackagingType packagingType = (PackagingType) packagingTypeSpinner.getSelectedItem();
        final String weight = weightEditText.getText().toString();
        final String length = lengthEditText.getText().toString();
        final String width = widthEditText.getText().toString();
        final String height = heightEditText.getText().toString();

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
        itemList.add(new Cargo(packagingType.getName(), Double.parseDouble(length), Double.parseDouble(width), Double.parseDouble(height), Double.parseDouble(weight)));
        calculatorAdapter.notifyItemInserted(itemList.size() - 1);
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
        if (itemList.isEmpty()) {
            Toast.makeText(context, "Добавьте хотя бы одну позицию", Toast.LENGTH_SHORT).show();
            return;
        }
        int totalQuantity = itemList.size();
        double totalVolume = 0.0;
        double totalWeight = 0.0;
        double totalLength = 0.0;
        double totalWidth = 0.0;
        double totalHeight = 0.0;
        
        double totalPrice = 0.0;

        for (final Cargo item : itemList) {
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
        if (selectedProvider != null) {
            totalPrice = totalPrice * (selectedProvider.getFuel() + 100) / 100;

        }
        totalPrice *= 1.15;

        totalQuantityTextView.setText(String.valueOf(totalQuantity));
        totalWeightTextView.setText(String.valueOf(totalWeight));
        totalDimensionsTextView.setText(String.valueOf(totalVolume));
        expressCost.setText(String.valueOf(totalPrice));
        economyExpressCost.setText(String.valueOf(totalPrice));
    }

    private void hidePackageTypeRadioBtns() {
        packageTypeRadioGroup.setVisibility(View.INVISIBLE);
        docTypeRadioBtn.setVisibility(View.INVISIBLE);
        boxTypeRadioBtn.setVisibility(View.INVISIBLE);
    }

    private void showPackageTypeRadioBtns() {
        packageTypeRadioGroup.setVisibility(View.VISIBLE);
        docTypeRadioBtn.setVisibility(View.VISIBLE);
        boxTypeRadioBtn.setVisibility(View.VISIBLE);
    }

    private void hideInputFields() {
        packagingTypeSpinner.setVisibility(View.INVISIBLE);
        packagingTypeField.setVisibility(View.INVISIBLE);

        weightEditText.setVisibility(View.INVISIBLE);
        lengthEditText.setVisibility(View.INVISIBLE);
        widthEditText.setVisibility(View.INVISIBLE);
        heightEditText.setVisibility(View.INVISIBLE);
    }

    private void showInputFields() {
        packagingTypeSpinner.setVisibility(View.VISIBLE);
        packagingTypeField.setVisibility(View.VISIBLE);

        weightEditText.setVisibility(View.VISIBLE);
        lengthEditText.setVisibility(View.VISIBLE);
        widthEditText.setVisibility(View.VISIBLE);
        heightEditText.setVisibility(View.VISIBLE);
    }

    private void hideNextViews() {
        //radio group, radioBtn 1, radioBtn 2
        //packageType spinner, its relative layout
        //weight editText, length editText, widthEditText, height editText
        //addItem btn
        addBtn.setVisibility(View.INVISIBLE);
        calculateBtn.setVisibility(View.INVISIBLE);
    }

    private static final String TAG = CalculatorFragment.class.toString();
}