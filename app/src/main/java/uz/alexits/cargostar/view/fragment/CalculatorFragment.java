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
import uz.alexits.cargostar.model.location.City;
import uz.alexits.cargostar.model.location.Country;
import uz.alexits.cargostar.model.calculation.PackagingType;
import uz.alexits.cargostar.model.shipping.Cargo;
import uz.alexits.cargostar.utils.Regex;
import uz.alexits.cargostar.viewmodel.CourierViewModel;
import uz.alexits.cargostar.viewmodel.CalculatorViewModel;
import uz.alexits.cargostar.utils.IntentConstants;
import uz.alexits.cargostar.utils.UiUtils;
import uz.alexits.cargostar.view.activity.CalculatorActivity;
import uz.alexits.cargostar.view.activity.CreateUserActivity;
import uz.alexits.cargostar.view.activity.MainActivity;
import uz.alexits.cargostar.view.activity.NotificationsActivity;
import uz.alexits.cargostar.view.activity.ProfileActivity;
import uz.alexits.cargostar.view.adapter.CalculatorAdapter;
import uz.alexits.cargostar.view.callback.CreateParcelCallback;
import uz.alexits.cargostar.viewmodel.LocationDataViewModel;
import uz.alexits.cargostar.workers.SyncWorkRequest;

import java.util.ArrayList;
import java.util.List;

public class CalculatorFragment extends Fragment implements CreateParcelCallback {
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

    //location view model
    private LocationDataViewModel locationDataViewModel;

    //country spinner items
    private ArrayAdapter<Country> countryArrayAdapter;
    private Spinner srcCountrySpinner;
    private Spinner destCountrySpinner;
    private RelativeLayout srcCountryField;
    private RelativeLayout destCountryField;

    //city spinner items
    private ArrayAdapter<City> srcCityArrayAdapter;
    private ArrayAdapter<City> destCityArrayAdapter;
    private RelativeLayout srcCityField;
    private RelativeLayout destCityField;
    private Spinner srcCitySpinner;
    private Spinner destCitySpinner;

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

    private static long selectedProviderId = 0;
    private static int selectedType = 0;

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
    private List<Cargo> itemList;
    private RecyclerView itemRecyclerView;

    public CalculatorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        activity = getActivity();
        itemList = new ArrayList<>();

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
        calculatorImageView.setOnClickListener(v -> {
            startActivity(new Intent(context, CalculatorActivity.class));
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

        srcCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView itemTextView = (TextView) view;
                final Country srcCountry = (Country) adapterView.getSelectedItem();

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        srcCountryField.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
                populateCityAdapter(srcCityArrayAdapter, srcCountry.getId());

                //country & null = hide all
                if (destCountrySpinner.getSelectedItem() == null) {
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

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        destCountryField.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
                populateCityAdapter(destCityArrayAdapter, destCountry.getId());

                //country & null = hide all
                if (srcCitySpinner.getSelectedItem() == null) {
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

                showPackageTypeRadioBtns();

                docTypeRadioBtn.setChecked(false);
                boxTypeRadioBtn.setChecked(false);

                if (firstCardRadioBtn.getText().equals(getString(R.string.cargostar))) {
                    calculatorViewModel.setSelectedProviderId(6);
                    return;
                }
                if (firstCardRadioBtn.getText().equals(getString(R.string.tnt))) {
                    calculatorViewModel.setSelectedProviderId(5);
                }
                return;
            }
            if (!b && !secondCardRadioBtn.isChecked()) {
                hidePackageTypeRadioBtns();

                docTypeRadioBtn.setChecked(false);
                boxTypeRadioBtn.setChecked(false);

                calculatorViewModel.setSelectedProviderId(0);
            }
        });

        secondCardRadioBtn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                firstCardRadioBtn.setChecked(false);

                showPackageTypeRadioBtns();

                docTypeRadioBtn.setChecked(false);
                boxTypeRadioBtn.setChecked(false);
                //only fedex case
                calculatorViewModel.setSelectedProviderId(4);
                return;
            }
            if (!b && !firstCardRadioBtn.isChecked()) {
                hidePackageTypeRadioBtns();

                docTypeRadioBtn.setChecked(false);
                boxTypeRadioBtn.setChecked(false);

                calculatorViewModel.setSelectedProviderId(0);
            }
        });

        /* choose packaging type (1 / 2) */
        packageTypeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            int selectedType = 0;

            if (checkedId == docTypeRadioBtn.getId()) {
                //docs
                selectedType = 1;
                showInputFields();
            }
            else if (checkedId == boxTypeRadioBtn.getId()) {
                //boxes
                selectedType = 2;
                showInputFields();
            }
            else {
                hideInputFields();
            }
            calculatorViewModel.setSelectedType(selectedType);
        });

        //todo: populate this spinner from data Table
        packagingTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView itemTextView = (TextView) view;

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

        //country/city spinners
       locationDataViewModel = new ViewModelProvider(this).get(LocationDataViewModel.class);

       locationDataViewModel.getCountryList().observe(getViewLifecycleOwner(), countryList -> {
           for (final Country country : countryList) {
               countryArrayAdapter.add(country);
           }
           countryArrayAdapter.notifyDataSetChanged();
       });

        //providers / packaging
        calculatorViewModel = new ViewModelProvider(this).get(CalculatorViewModel.class);

        calculatorViewModel.getSelectedProviderId().observe(getViewLifecycleOwner(), providerId -> {
            selectedProviderId = providerId;
        });

        calculatorViewModel.getSelectedType().observe(getViewLifecycleOwner(), type -> {
            selectedType = type;
        });

        calculatorViewModel.getProviderList().observe(getViewLifecycleOwner(), providers -> {
            Log.i(TAG, "providerList=" + providers);
        });

        calculatorViewModel.getPackagingTypeList().observe(getViewLifecycleOwner(), packagingTypes -> {
            Log.i(TAG, "packagingTypeList=" + packagingTypes);
        });

        calculatorViewModel.getPackagingTypesByProviderId(4, 1).observe(getViewLifecycleOwner(), packagingTypeList -> {
            Log.i(TAG, "Fedex 1=" + packagingTypeList);
        });

        calculatorViewModel.getPackagingTypesByProviderId(4, 2).observe(getViewLifecycleOwner(), packagingTypes -> {
            Log.i(TAG, "Fedex 2=" + packagingTypes);
        });

        calculatorViewModel.getPackagingTypesByProviderId(5, 1).observe(getViewLifecycleOwner(), packagingTypeList -> {
            Log.i(TAG, "tnt 1=" + packagingTypeList);
        });

        calculatorViewModel.getPackagingTypesByProviderId(5, 2).observe(getViewLifecycleOwner(), packagingTypes -> {
            Log.i(TAG, "tnt 2=" + packagingTypes);
        });

        calculatorViewModel.getPackagingTypesByProviderId(6, 1).observe(getViewLifecycleOwner(), packagingTypeList -> {
            Log.i(TAG, "cargo 1=" + packagingTypeList);
        });

        calculatorViewModel.getPackagingTypesByProviderId(6, 2).observe(getViewLifecycleOwner(), packagingTypeList -> {
            Log.i(TAG, "cargo 2=" + packagingTypeList);
        });

//        if (packagingTypeList != null) {
//            Log.i(TAG, "packagingTypeList: " + packagingTypeList);
//
//            for (final PackagingType packagingType : packagingTypeList) {
//                packagingTypeArrayAdapter.add(packagingType);
//            }
//            packagingTypeArrayAdapter.notifyDataSetChanged();
//        }
    }

    private void populateCityAdapter(final ArrayAdapter<City> arrayAdapter, final long countryId) {
        arrayAdapter.clear();

        locationDataViewModel.getCitiesByCountryId(countryId).observe(getViewLifecycleOwner(), cityList -> {
            Log.i(TAG, "country selected cities by id " + countryId + " :" + cityList);
            for (final City city : cityList) {
                arrayAdapter.add(city);
            }
            arrayAdapter.notifyDataSetChanged();
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
    public void onDeleteItemClicked(final int position) {
        itemList.remove(position);
        calculatorAdapter.notifyItemRemoved(position);
    }

    private void addCargoToInvoice() {
        final String weight = weightEditText.getText().toString();
        final String length = lengthEditText.getText().toString();
        final String width = widthEditText.getText().toString();
        final String height = heightEditText.getText().toString();

        /* check for empty fields */
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
        itemList.add(new Cargo(Double.parseDouble(length), Double.parseDouble(width), Double.parseDouble(height), Double.parseDouble(weight)));
        calculatorAdapter.notifyItemInserted(itemList.size() - 1);
    }

    private void calculateTotalPrice() {
        int totalQuantity = itemList.size() + 1;
        int totalWeight = 0;
        int totalLength = 0;
        int totalWidth = 0;
        int totalHeight = 0;

        for (final Cargo item : itemList) {
            totalWeight += item.getWeight();
            totalLength += item.getLength();
            totalWidth += item.getWidth();
            totalHeight += item.getHeight();
        }
        final String totalDimensions = totalLength + "x" + totalWidth + "x" + totalHeight;
        totalQuantityTextView.setText(String.valueOf(totalQuantity));
        totalWeightTextView.setText(String.valueOf(totalWeight));
        totalDimensionsTextView.setText(totalDimensions);

//            if (firstCardRadioBtn) {
//                //todo: providerId -> packaging -> get all packagings with providerId
//            }
//            else if (secondCardRadioBtn) {
//                //todo: providerId -> packaging -> get all packagings with providerId
//            }
//
//            switch (packageTypeRadioGroup.getCheckedRadioButtonId()) {
//                case docTypeRadioBtn.getId(): {
//                    //todo: select type 1 -> packaging-type
//                    break;
//                }
//                case boxTypeRadioBtn.getId(): {
//                    //todo: select type 2 -> packaging-type
//                    break;
//                }
//            }

        //todo: total weight (for each cargo -> sum up weights / length x width x height and sum up)

        //todo: before calculating compare my overall Volume with volumx from Packaging table. If overall volume / volumx > total weight -> total weight = overall volume / volumx. Else -> weight is weight.

        //todo: get packaging_id and look for tariff name in Packaging

        //todo: start with weight -> check between from & to values. With each step price rises according to priceStep.

        //todo: add constants such that:  1) fuel (in Provider table in percent), 2) ndc (in table Settings in percent),

        //todo: add parcel_fee in Packaging table (only for type=2)

//            expressCost.setText();
//            economyExpressCost.setText();
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