package com.example.cargostar.view.fragment;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cargostar.R;
import com.example.cargostar.model.database.SharedPrefs;
import com.example.cargostar.model.shipping.Cargo;
import com.example.cargostar.view.Constants;
import com.example.cargostar.view.UiUtils;
import com.example.cargostar.view.activity.CalculatorActivity;
import com.example.cargostar.view.activity.CreateUserActivity;
import com.example.cargostar.view.activity.MainActivity;
import com.example.cargostar.view.activity.NotificationsActivity;
import com.example.cargostar.view.activity.ProfileActivity;
import com.example.cargostar.view.adapter.CalculatorAdapter;
import com.example.cargostar.view.adapter.SpinnerAdapter;
import com.example.cargostar.view.callback.CreateParcelCallback;
import com.example.cargostar.viewmodel.HeaderViewModel;
import com.example.cargostar.viewmodel.PopulateViewModel;

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
    private CharSequence[] countryList;
    private CharSequence[] uzbekistanCityList;
    private CharSequence[] kazakhstanCityList;
    private CharSequence[] kirgizstanCityList;
    private CharSequence[] russiaCityList;
    private CharSequence[] packageTypeList;

    private RelativeLayout sourceCountryField;
    private RelativeLayout sourceCityField;
    private RelativeLayout destinationCountryField;
    private RelativeLayout destinationCityField;
    private RelativeLayout packageTypeField;

    private Spinner sourceCountrySpinner;
    private Spinner destinationCountrySpinner;
    private Spinner sourceCitySpinner;
    private Spinner destinationCitySpinner;
    private Spinner packageTypeSpinner;

    private ArrayAdapter<CharSequence> countryAdapter;
    private ArrayAdapter<CharSequence> uzbekistanCityAdapter;
    private ArrayAdapter<CharSequence> kazakhstanCityAdapter;
    private ArrayAdapter<CharSequence> russiaCityAdapter;
    private ArrayAdapter<CharSequence> kirgizstanCityAdapter;
    private ArrayAdapter<CharSequence> packageTypeAdapter;
    private CalculatorAdapter calculatorAdapter;

    private CardView firstCard;
    private CardView secondCard;
    private RadioButton firstCardRadioBtn;
    private RadioButton secondCardRadioBtn;
    private ImageView firstCardImageView;
    private ImageView secondCardImageView;

    private EditText weightEditText;
    private EditText lengthEditText;
    private EditText widthEditText;
    private EditText heightEditText;
    private Button addBtn;

    private TextView totalQuantityTextView;
    private TextView totalWeightTextView;
    private TextView totalDimensionsTextView;
    private TextView expressCost;
    private TextView economyExpressCost;
    private Button calculateBtn;

    //calc items
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
        countryList = getResources().getStringArray(R.array.countries);
        uzbekistanCityList = getResources().getStringArray(R.array.uzbekistan_cities);
        kazakhstanCityList = getResources().getStringArray(R.array.kazakhstan_cities);
        kirgizstanCityList = getResources().getStringArray(R.array.kirgizstan_cities);
        russiaCityList = getResources().getStringArray(R.array.russia_cities);
        packageTypeList = getResources().getStringArray(R.array.package_types);

        itemList = new ArrayList<>();
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
        sourceCountryField = root.findViewById(R.id.source_country_field);
        sourceCityField = root.findViewById(R.id.source_city_field);
        destinationCountryField = root.findViewById(R.id.destination_country_field);
        destinationCityField = root.findViewById(R.id.destination_city_field);
        packageTypeField = root.findViewById(R.id.package_type_field);

        sourceCountrySpinner = root.findViewById(R.id.source_country_spinner);
        sourceCitySpinner = root.findViewById(R.id.source_city_spinner);
        destinationCountrySpinner = root.findViewById(R.id.destination_country_spinner);
        destinationCitySpinner = root.findViewById(R.id.destination_city_spinner);
        packageTypeSpinner = root.findViewById(R.id.package_type_spinner);

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

        countryAdapter = new SpinnerAdapter<>(context, R.layout.spinner_item, countryList, 2);
        uzbekistanCityAdapter = new SpinnerAdapter<>(context, R.layout.spinner_item, uzbekistanCityList, 2);
        kazakhstanCityAdapter = new SpinnerAdapter<>(context, R.layout.spinner_item, kazakhstanCityList, 2);
        russiaCityAdapter = new SpinnerAdapter<>(context, R.layout.spinner_item, russiaCityList, 2);
        kirgizstanCityAdapter = new SpinnerAdapter<>(context, R.layout.spinner_item, kirgizstanCityList, 2);
        packageTypeAdapter = new SpinnerAdapter<>(context, R.layout.spinner_item, packageTypeList, 1);

        countryAdapter.setDropDownViewResource(R.layout.spinner_item);
        uzbekistanCityAdapter.setDropDownViewResource(R.layout.spinner_item);
        kazakhstanCityAdapter.setDropDownViewResource(R.layout.spinner_item);
        russiaCityAdapter.setDropDownViewResource(R.layout.spinner_item);
        kirgizstanCityAdapter.setDropDownViewResource(R.layout.spinner_item);
        packageTypeAdapter.setDropDownViewResource(R.layout.spinner_item);

        sourceCountrySpinner.setAdapter(countryAdapter);
        destinationCountrySpinner.setAdapter(countryAdapter);
        sourceCitySpinner.setAdapter(uzbekistanCityAdapter);
        destinationCitySpinner.setAdapter(uzbekistanCityAdapter);
        packageTypeSpinner.setAdapter(packageTypeAdapter);

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

        sourceCountrySpinner.setSelection(countryList.length - 2);
        destinationCountrySpinner.setSelection(countryList.length - 1);
        sourceCitySpinner.setSelection(uzbekistanCityList.length - 2);
        destinationCitySpinner.setSelection(uzbekistanCityList.length - 1);
        packageTypeSpinner.setSelection(packageTypeList.length - 1);

        sourceCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView itemTextView = (TextView) view;

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        sourceCountryField.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
                final int selectedPosition = sourceCountrySpinner.getSelectedItemPosition();

                if (selectedPosition == 0) {
                    sourceCitySpinner.setAdapter(uzbekistanCityAdapter);
                    if (destinationCountrySpinner.getSelectedItem().equals(countryList[0])) {
                        firstCardRadioBtn.setChecked(true);
                        firstCardImageView.setImageResource(R.drawable.logo_cargo_calc);
                        secondCardRadioBtn.setVisibility(View.INVISIBLE);
                        secondCardImageView.setVisibility(View.INVISIBLE);
                        secondCard.setVisibility(View.INVISIBLE);
                        return;
                    }
                    firstCardRadioBtn.setChecked(true);
                    firstCardImageView.setImageResource(R.drawable.logo_tnt_cacl);
                    secondCardImageView.setImageResource(R.drawable.logo_fedex_calc);
                    secondCardRadioBtn.setVisibility(View.VISIBLE);
                    secondCardImageView.setVisibility(View.VISIBLE);
                    secondCard.setVisibility(View.VISIBLE);
                    return;
                }
                if (destinationCountrySpinner.getSelectedItem().equals(countryList[0])) {
                    firstCardRadioBtn.setChecked(true);
                    firstCardImageView.setImageResource(R.drawable.logo_tnt_cacl);
                    secondCardRadioBtn.setVisibility(View.INVISIBLE);
                    secondCardImageView.setVisibility(View.INVISIBLE);
                    secondCard.setVisibility(View.INVISIBLE);
                }
                if (selectedPosition == 1) {
                    sourceCitySpinner.setAdapter(kazakhstanCityAdapter);
                    return;
                }
                if (selectedPosition == 2) {
                    sourceCitySpinner.setAdapter(kirgizstanCityAdapter);
                    return;
                }
                if (selectedPosition == 3) {
                    sourceCitySpinner.setAdapter(russiaCityAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        destinationCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView itemTextView = (TextView) view;

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        destinationCountryField.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
                final int selectedPosition = destinationCountrySpinner.getSelectedItemPosition();

                if (selectedPosition == 0) {
                    destinationCitySpinner.setAdapter(uzbekistanCityAdapter);
                    if (sourceCountrySpinner.getSelectedItem().equals(countryList[0])) {
                        firstCardRadioBtn.setChecked(true);
                        firstCardImageView.setImageResource(R.drawable.logo_cargo_calc);
                        secondCardRadioBtn.setVisibility(View.INVISIBLE);
                        secondCardImageView.setVisibility(View.INVISIBLE);
                        secondCard.setVisibility(View.INVISIBLE);
                        return;
                    }
                    firstCardRadioBtn.setChecked(true);
                    firstCardImageView.setImageResource(R.drawable.logo_tnt_cacl);
                    secondCardRadioBtn.setVisibility(View.INVISIBLE);
                    secondCardImageView.setVisibility(View.INVISIBLE);
                    secondCard.setVisibility(View.INVISIBLE);
                    return;
                }
                if (sourceCountrySpinner.getSelectedItem().equals(countryList[0])) {
                    firstCardRadioBtn.setChecked(true);
                    firstCardImageView.setImageResource(R.drawable.logo_tnt_cacl);
                    secondCardImageView.setImageResource(R.drawable.logo_fedex_calc);
                    secondCardRadioBtn.setVisibility(View.VISIBLE);
                    secondCardImageView.setVisibility(View.VISIBLE);
                    secondCard.setVisibility(View.VISIBLE);
                }
                if (selectedPosition == 1) {
                    destinationCitySpinner.setAdapter(kazakhstanCityAdapter);
                    return;
                }
                if (selectedPosition == 2) {
                    destinationCitySpinner.setAdapter(kirgizstanCityAdapter);
                    return;
                }
                if (selectedPosition == 3) {
                    destinationCitySpinner.setAdapter(russiaCityAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sourceCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView itemTextView = (TextView) view;

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        sourceCityField.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        destinationCitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView itemTextView = (TextView) view;

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        destinationCityField.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        packageTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final TextView itemTextView = (TextView) view;

                if (itemTextView != null) {
                    if (i < adapterView.getCount()) {
                        itemTextView.setTextColor(context.getColor(R.color.colorBlack));
                        packageTypeField.setBackgroundResource(R.drawable.edit_text_active);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addBtn.setOnClickListener(v -> {
//            final String packageType = packageTypeSpinner.getSelectedItem().toString();
//            final int weight = Integer.parseInt(weightEditText.getText().toString());
//            final int length = Integer.parseInt(lengthEditText.getText().toString());
//            final int width = Integer.parseInt(widthEditText.getText().toString());
//            final int height = Integer.parseInt(heightEditText.getText().toString());
//            final String sourceCountryStr = sourceCountrySpinner.getSelectedItem().toString();
//            final String sourceCityStr = sourceCitySpinner.getSelectedItem().toString();
//            final String destinationCountryStr = destinationCountrySpinner.getSelectedItem().toString();
//            final String destinationCityStr = destinationCitySpinner.getSelectedItem().toString();
//
//            final Country sourceCountry = new Country(sourceCountryStr);
//            final City sourceCity = new City(sourceCityStr);
//            final Country destinationCountry = new Country(destinationCountryStr);
//            final City destinationCity = new City(destinationCityStr);
//
//            final Address sourceAddress = new Address();
//            sourceAddress.setCountry(sourceCountry);
//            sourceAddress.setCity(sourceCity);
//
//            final Address destinationAddress = new Address();
//            destinationAddress.setCountry(destinationCountry);
//            destinationAddress.setCity(destinationCity);
//
//            final Cargo newItem = new Cargo(de);
//            newItem.set(sourceAddress);
//            newItem.setDestination(destinationAddress);
//            newItem.setLength(length);
//            newItem.setWidth(width);
//            newItem.setHeight(height);
//            newItem.setWeight(weight);
//            newItem.setPackageType(packageType);
//
//            itemList.add(newItem);
//            calculatorAdapter.notifyItemInserted(itemList.size() - 1);
        });

        calculateBtn.setOnClickListener(v -> {
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
//            expressCost.setText();
//            economyExpressCost.setText();
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final HeaderViewModel headerViewModel = new ViewModelProvider(this).get(HeaderViewModel.class);

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
    }

    @Override
    public void onAddBtnClicked() {

    }

    @Override
    public void onCameraImageClicked(int position) {

    }

    @Override
    public void onSenderSignatureClicked() {

    }

    @Override
    public void onRecipientSignatureClicked() {

    }

    @Override
    public void afterFirstEditTextChanged(int position, Editable editable) {

    }

    @Override
    public void afterSecondEditTextChanged(int position, Editable editable) {

    }

    @Override
    public void onSpinnerItemChanged(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onDeleteItemClicked(final int position) {
        itemList.remove(position);
        calculatorAdapter.notifyItemRemoved(position);
    }
}